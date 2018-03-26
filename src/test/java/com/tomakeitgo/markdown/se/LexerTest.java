package com.tomakeitgo.markdown.se;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LexerTest {

    @Test
    public void givenEmptyStreamExpectEOF() throws IOException {
        Lexer lexer = lexer("");

        assertThat(lexer.next(), is(Lexer.EOF));
    }

    @Test
    public void givenOpenParenExpectOpenEOF() throws IOException {
        Lexer lexer = lexer("(");

        assertThat(lexer.next(), is(Lexer.OPEN));
        assertThat(lexer.next(), is(Lexer.EOF));
    }

    @Test
    public void givenCloseParenExpectCloseEOF() throws IOException {
        Lexer lexer = lexer(")");

        assertThat(lexer.next(), is(Lexer.CLOSE));
        assertThat(lexer.next(), is(Lexer.EOF));
    }

    @Test
    public void givenSingleSpaceExpectSpaceEOF() throws IOException {
        Lexer lexer = lexer(" ");

        assertThat(lexer.next(), is(Lexer.SPACE));
        assertThat(lexer.next(), is(Lexer.EOF));
    }

    @Test
    public void givenTwoSpacesExpectSpaceEOF() throws IOException {
        Lexer lexer = lexer("  ");

        assertThat(lexer.next(), is(Lexer.SPACE));
        assertThat(lexer.next(), is(Lexer.EOF));
    }

    @Test
    public void givenThreeSpacesExpectSpaceEOF() throws IOException {
        Lexer lexer = lexer("   ");

        assertThat(lexer.next(), is(Lexer.SPACE));
        assertThat(lexer.next(), is(Lexer.EOF));
    }

    @Test
    public void givenThreeSpacesOpenParenExpectSpaceOpenEOF() throws IOException {
        Lexer lexer = lexer("   (");

        assertThat(lexer.next(), is(Lexer.SPACE));
        assertThat(lexer.next(), is(Lexer.OPEN));
        assertThat(lexer.next(), is(Lexer.EOF));
    }

    @Test
    public void givenWhiteSpaceOpenCharactersExpectSpaceOpenEOF() throws IOException {
        Lexer lexer = lexer(" \n\t\r(");

        assertThat(lexer.next(), is(Lexer.SPACE));
        assertThat(lexer.next(), is(Lexer.OPEN));
        assertThat(lexer.next(), is(Lexer.EOF));
    }

    @Test
    public void givenOneSymbolCharEpxectSymbolEOF() throws IOException {
        Lexer lexer = lexer("a");

        assertThat(lexer.next(), is(new Token("a")));
        assertThat(lexer.next(), is(Lexer.EOF));
    }

    @Test
    public void givenTwoSymbolCharEpxectSymbolEOF() throws IOException {
        Lexer lexer = lexer("ab");

        assertThat(lexer.next(), is(new Token("ab")));
        assertThat(lexer.next(), is(Lexer.EOF));
    }

    @Test
    public void givenTwoSymbolCharSpaceExpectSymbolSpaceEOF() throws IOException {
        Lexer lexer = lexer("ab ");

        assertThat(lexer.next(), is(new Token("ab")));
        assertThat(lexer.next(), is(Lexer.SPACE));
        assertThat(lexer.next(), is(Lexer.EOF));
    }

    @Test
    public void givenTwoSymbolCharOpenExpectSymbolOpenEOF() throws IOException {
        Lexer lexer = lexer("ab(");

        assertThat(lexer.next(), is(new Token("ab")));
        assertThat(lexer.next(), is(Lexer.OPEN));
        assertThat(lexer.next(), is(Lexer.EOF));
    }

    @Test
    public void givenTwoSymbolCharCloseExpectSymbolCloseEOF() throws IOException {
        Lexer lexer = lexer("ab)");

        assertThat(lexer.next(), is(new Token("ab")));
        assertThat(lexer.next(), is(Lexer.CLOSE));
        assertThat(lexer.next(), is(Lexer.EOF));
    }

    private Lexer lexer(String input) {
        return new Lexer(new ByteArrayInputStream(input.getBytes()));
    }
}