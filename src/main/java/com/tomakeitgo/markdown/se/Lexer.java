package com.tomakeitgo.markdown.se;

import java.io.*;

class Lexer {

    public static final Token EOF = new Token(null);
    public static final Token OPEN = new Token("(");
    public static final Token CLOSE = new Token(")");
    public static final Token SPACE = new Token(" ");

    private final Reader inputStream;

    public Lexer(InputStream inputStream) {
        this.inputStream = new BufferedReader(new InputStreamReader(inputStream));
    }

    public Token next() throws IOException {
        int current = inputStream.read();

        if (current == -1) return EOF;
        if (current == '(') return OPEN;
        if (current == ')') return CLOSE;
        if (isWhiteSpace(current)) return consumeSpaces();

        return consumeSymbol((char) current);
    }

    private Token consumeSymbol(char current) throws IOException {
        StringBuilder token = new StringBuilder();
        token.append(current);

        inputStream.mark(1);

        int next;
        while (isSymbolCharacter(next = inputStream.read())) {
            token.append((char) next);
            inputStream.mark(1);
        }

        inputStream.reset();

        return new Token(token.toString());
    }

    private Token consumeSpaces() throws IOException {
        int next;
        do {
            inputStream.mark(1);
        } while (isWhiteSpace(next = inputStream.read()));
        inputStream.reset();

        return SPACE;
    }

    private boolean isSymbolCharacter(int current) {
        return !(isWhiteSpace(current) || current == -1 || current == '(' || current == ')');
    }

    private boolean isWhiteSpace(int current) {
        return Character.isWhitespace((char) current);
    }
}
