package com.tomakeitgo.markdown.se;

import java.io.*;

public class MarkdownConverter {

    private final Evaluator evaluator = new Evaluator();

    public static void main(String[] args) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String s = "(___define___ p (<p class='thing'>) (</p>) ) (p hello) (p world)";

        new MarkdownConverter().convert(new ByteArrayInputStream(s.getBytes()), outputStream);

        System.out.println(new String(outputStream.toByteArray()));
    }

    public void convert(InputStream inputStream, OutputStream outputStream) throws IOException {
        outputStream.write(evaluator.eval(new Parser(new Lexer(inputStream)).parse()).getValue().getBytes());
        outputStream.flush();
    }
}
