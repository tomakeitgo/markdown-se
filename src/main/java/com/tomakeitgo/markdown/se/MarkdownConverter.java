package com.tomakeitgo.markdown.se;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * A MarkdownConverter is used to convert markdown into its resulting expressions.
 * <p>
 * There are four predefined functions.
 * <ul>
 * <li>___define___ ( (parameters), body ) -&gt; Empty. allows new functions to be defined</li>
 * <li>___no_spaces___ ( exp ) -&gt; Symbol. Reduces the expression taken combining the results with no delimiter</li>
 * <li>___default___ ( exp ) -&gt; Symbol. Reduces the expression taken combining the results with a space delimiter</li>
 * <li>___paren___ ( exp ) -&gt; Symbol. Reduces the expression taken combining the results with a space delimiter, prefix (, and suffix )</li>
 * </ul>
 */
public class MarkdownConverter {

    private final Evaluator evaluator = new Evaluator();

    /**
     * The method convert will read the inputStream, evaluate the markdown, and write the results to the output stream.
     * The inputStream and outputStream will not be closed.
     * <p>
     * Calls to convert will modify the state of the converter. Functions defined in the global scope will carry over between runs.
     *
     * @param inputStream  The source that is to be read and evaluated
     * @param outputStream The destination for the results of the conversion
     * @throws IOException will be thrown if an error occurs reading or writing.
     */
    public void convert(InputStream inputStream, OutputStream outputStream) throws IOException {
        outputStream.write(evaluator.eval(new Parser(new Lexer(inputStream)).parse()).getValue().getBytes());
        outputStream.flush();
    }
}
