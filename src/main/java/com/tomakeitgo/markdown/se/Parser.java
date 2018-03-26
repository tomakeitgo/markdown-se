package com.tomakeitgo.markdown.se;

import java.io.IOException;
import java.util.Stack;

class Parser {

    private final Lexer lexer;
    private final Stack<Expression> stack = new Stack<>();

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        this.stack.push(new Expression());
    }

    public Expression parse() throws IOException {
        Token token;
        while (!(token = lexer.next()).equals(Lexer.EOF)) {
            if (token.equals(Lexer.SPACE)) {
                //ignore white space
            } else if (token.equals(Lexer.OPEN)) {
                stack.push(new Expression());
            } else if (token.equals(Lexer.CLOSE)) {
                if (stack.size() == 1) throw new UnexpectedCloseFoundException();

                Expression complete = stack.pop();
                stack.peek().add(complete);
            } else {
                stack.peek().add(new Symbol(token.getValue()));
            }
        }

        if (stack.size() > 1) throw new UnexpectedUnclosedExpressionException();
        return stack.pop();
    }

    public class UnexpectedCloseFoundException extends IllegalArgumentException {}
    public class UnexpectedUnclosedExpressionException extends IllegalArgumentException {}
}
