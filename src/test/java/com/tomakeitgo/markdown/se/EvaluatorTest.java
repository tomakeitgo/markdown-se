package com.tomakeitgo.markdown.se;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EvaluatorTest {

    private Evaluator evaluator;

    @Before
    public void setUp() {
        evaluator = new Evaluator();

        eval("(___define___ p (text) (p text))");
        eval("(___define___ lostOfText () (text text text))");
        eval("(___define___ passThrough (arg) arg)");
    }

    private Symbol eval(String exp) {
        return evaluator.eval(exp(exp));
    }

    @Test
    public void givenEmptyExpressionExpectEmptySybol() {
        assertThat(eval(""), is(new Symbol("")));
    }

    @Test
    public void givenExpressionContainingSymbolExpectSymbol() {
        assertThat(eval("token"), is(new Symbol("token")));
    }

    @Test
    public void givenExpressionContainingSymbolsExpectSymbol() {
        assertThat(eval("token token token"), is(new Symbol("token token token")));
    }

    @Test
    public void givenExpressionContainingFunctionOnFirstLevelExpectNoFunctionCall() {
        assertThat(eval("(p token token)"), is(new Symbol("p token token")));
    }

    @Test
    public void givenSubexpressionNoParametersExpectReduction() {
        assertThat(eval("(lostOfText)"), is(new Symbol("text text text")));
    }

    @Test
    public void givenSubexpressionOneParametersSymbolExpectReduction() {
        assertThat(eval("(passThrough token)"), is(new Symbol("token")));
    }

    @Test
    public void givenSubexpressionExpectReduction() {
        assertThat(eval("((p token token))"), is(new Symbol("p token token")));
    }

    @Test
    public void givenSubexpressionContainingExpressionExpectReduction() {
        assertThat(eval("(p (p a b) token)"), is(new Symbol("p p a b token")));
    }

    @Test
    public void givenSubexpressionContainingExpressionAsHeadExpectReduction() {
        assertThat(eval("(p a b) (p token)"), is(new Symbol("p a b p token")));
    }

    @Test
    public void givenNoSpaceExpectNoSpacesInResult() {
        eval("(___define___ link ( _href _text ) ( <a (___no_spaces___ href=' _href ' >(___no_spaces___ _text )</a> ) ))");
        assertThat(eval("(___no_spaces___ a b c)"), is(new Symbol("abc")));
        assertThat(eval("(link http://www.google.com google)"), is(new Symbol("<a href='http://www.google.com'>google</a>")));
    }

    private Expression exp(String exp) {
        try {
            return new Parser(new Lexer(new ByteArrayInputStream(exp.getBytes()))).parse();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}