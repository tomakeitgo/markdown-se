package com.tomakeitgo.markdown.se;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MarkdownConverter {

    private final Evaluator evaluator = new Evaluator();

    public static void main(String[] args) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String s = "" +
                "(___define___ p (text) (<p class='thing'> text </p>) ) " +
                "(___define___ link (target text) (<a href=' target '> text </a>)) " +
                "(___define___ html ( inner ) ( <html> inner </html>) ) " +
                "(html (p hello world) (p how are you?) " +
                "(link http://www.google.com google) )";

        new MarkdownConverter().convert(MarkdownConverter.class.getResourceAsStream("test.txt"), outputStream);

        System.out.println(new String(outputStream.toByteArray()));
    }

    public void convert(InputStream inputStream, OutputStream outputStream) throws IOException {
        outputStream.write(evaluator.eval(new Parser(new Lexer(inputStream)).parse()).getValue().getBytes());
        outputStream.flush();
    }
}
