package com.tomakeitgo.markdown.se;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EvaluatorTest {

    private Evaluator evaluator;

    @Before
    public void setUp() {
        evaluator = new Evaluator();


        Expression expression = new Expression();
        Expression definitionForP = new Expression();
        definitionForP.add(new Symbol("___define___"));
        definitionForP.add(new Symbol("p"));
        Expression arguments = new Expression();
        arguments.add(new Symbol("text"));
        definitionForP.add(arguments);
        Expression body = new Expression();
        body.add(new Symbol("(p"));
        body.add(new Symbol("text"));
        body.add(new Symbol(")"));
        definitionForP.add(body);
        expression.add(definitionForP);
        evaluator.eval(expression);

        expression = new Expression();
        Expression noArgsToSymbol = new Expression();
        noArgsToSymbol.add(new Symbol("___define___"));
        noArgsToSymbol.add(new Symbol("lostOfText"));
        arguments = new Expression();
        noArgsToSymbol.add(arguments);

        noArgsToSymbol.add(new Symbol("text text text"));
        expression.add(noArgsToSymbol);
        evaluator.eval(expression);

        expression = new Expression();
        Expression oneArgPassThrough = new Expression();
        oneArgPassThrough.add(new Symbol("___define___"));
        oneArgPassThrough.add(new Symbol("passThrough"));
        arguments = new Expression();
        arguments.add(new Symbol("arg"));
        oneArgPassThrough.add(arguments);

        oneArgPassThrough.add(new Symbol("arg"));
        expression.add(oneArgPassThrough);
        evaluator.eval(expression);
    }

    @Test
    public void givenEmptyExpressionExpectEmptySybol() {
        Symbol eval = evaluator.eval(new Expression());

        assertThat(eval, is(new Symbol("")));
    }

    @Test
    public void givenExpressionContainingSymbolExpectSymbol() {
        Expression expression = new Expression();
        expression.add(new Symbol("token"));
        Symbol eval = evaluator.eval(expression);

        assertThat(eval, is(new Symbol("token")));
    }

    @Test
    public void givenExpressionContainingSymbolsExpectSymbol() {
        Expression expression = new Expression();
        expression.add(new Symbol("token"));
        expression.add(new Symbol("token"));
        expression.add(new Symbol("token"));
        Symbol eval = evaluator.eval(expression);

        assertThat(eval, is(new Symbol("token token token")));
    }

    @Test
    public void givenExpressionContainingFunctionOnFirstLevelExpectNoFunctionCall() {
        Expression expression = new Expression();
        expression.add(new Symbol("p"));
        expression.add(new Symbol("token"));
        expression.add(new Symbol("token"));
        Symbol eval = evaluator.eval(expression);

        assertThat(eval, is(new Symbol("p token token")));
    }

    @Test
    public void givenSubexpressionNoParametersExpectReduction() {
        Expression expression = new Expression();
        Expression subexpression = new Expression();
        subexpression.add(new Symbol("lostOfText"));
        expression.add(subexpression);
        Symbol eval = evaluator.eval(expression);

        assertThat(eval, is(new Symbol("text text text")));
    }

    @Test
    public void givenSubexpressionOneParametersSymbolExpectReduction() {
        Expression expression = new Expression();
        Expression subexpression = new Expression();
        subexpression.add(new Symbol("passThrough"));
        subexpression.add(new Symbol("token"));
        expression.add(subexpression);
        Symbol eval = evaluator.eval(expression);

        assertThat(eval, is(new Symbol("token")));
    }

    @Test
    public void givenSubexpressionExpectReduction() {
        Expression expression = new Expression();
        Expression subexpression = new Expression();
        subexpression.add(new Symbol("p"));
        subexpression.add(new Symbol("token"));
        subexpression.add(new Symbol("token"));
        expression.add(subexpression);
        Symbol eval = evaluator.eval(expression);

        assertThat(eval, is(new Symbol("(p token token)")));
    }

    @Test
    public void givenSubexpressionContainingExpressionExpectReduction() {
        Expression expression = new Expression();
        Expression subexpression = new Expression();
        subexpression.add(new Symbol("p"));
        Expression item = new Expression();
        item.add(new Symbol("p"));
        item.add(new Symbol("a"));
        item.add(new Symbol("b"));
        subexpression.add(item);
        subexpression.add(new Symbol("token"));
        expression.add(subexpression);
        Symbol eval = evaluator.eval(expression);

        assertThat(eval, is(new Symbol("(p (p a b) token)")));
    }

    @Test
    public void givenSubexpressionContainingExpressionAsHeadExpectReduction() {
        Expression expression = new Expression();
        Expression subexpression = new Expression();
        Expression item = new Expression();
        item.add(new Symbol("p"));
        item.add(new Symbol("a"));
        item.add(new Symbol("b"));
        subexpression.add(item);
        subexpression.add(new Symbol("p"));
        subexpression.add(new Symbol("token"));
        expression.add(subexpression);
        Symbol eval = evaluator.eval(expression);

        assertThat(eval, is(new Symbol("(p a b) p token")));
    }

}