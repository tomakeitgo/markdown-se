package com.tomakeitgo.markdown.se;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ParserTest {

    @Test
    public void givenEmptyInputExpectEmptyExpression() throws IOException {
        Parser parser = parse("");
        Expression expression = parser.parse();

        assertThat(expression, is(new Expression()));
    }

    @Test
    public void givenWhiteSpaceExpectEmptyExpression() throws IOException {
        Parser parser = parse("   ");
        Expression expression = parser.parse();

        assertThat(expression, is(new Expression()));
    }

    @Test
    public void givenSingleTokenExpectExpressionWithSingleSymbol() throws IOException {
        Parser parser = parse("symbol");
        Expression expression = parser.parse();

        Expression expected = new Expression();
        expected.add(new Symbol("symbol"));
        assertThat(expression, is(expected));
    }

    @Test
    public void givenTwoTokenExpectExpressionWithSingleSymbol() throws IOException {
        Parser parser = parse("symbol symbol");
        Expression expression = parser.parse();

        Expression expected = new Expression();
        expected.add(new Symbol("symbol"));
        expected.add(new Symbol("symbol"));
        assertThat(expression, is(expected));
    }

    @Test
    public void givenOpenCloseExpectExpressionInExpression() throws IOException {
        Parser parser = parse("()");
        Expression expression = parser.parse();

        Expression expected = new Expression();
        expected.add(new Expression());
        assertThat(expression, is(expected));
    }

    @Test(expected = Parser.UnexpectedCloseFoundException.class)
    public void givenSingleCloseExpectException() throws IOException {
        Parser parser = parse(")");
        parser.parse();
    }

    @Test(expected = Parser.UnexpectedUnclosedExpressionException.class)
    public void givenSingleOpenExpectException() throws IOException {
        Parser parser = parse("(");
        parser.parse();
    }


    @Test
    public void givenComplexExpressionExpectCorrectResults() throws IOException {
        Parser parser = parse("hello world (strike-through a n) ((a) a v)");
        Expression expression = parser.parse();

        Expression expected = new Expression();
        expected.add(new Symbol("hello"));
        expected.add(new Symbol("world"));
        Expression st = new Expression();
        st.add(new Symbol("strike-through"));
        st.add(new Symbol("a"));
        st.add(new Symbol("n"));
        expected.add(st);

        Expression nestedOuter = new Expression();
        Expression nestedInner = new Expression();
        nestedInner.add(new Symbol("a"));
        nestedOuter.add(nestedInner);
        nestedOuter.add(new Symbol("a"));
        nestedOuter.add(new Symbol("v"));
        expected.add(nestedOuter);
        assertThat(expression, is(expected));
    }

    private Parser parse(String input) {
        return new Parser(new Lexer(new ByteArrayInputStream(input.getBytes())));
    }
}