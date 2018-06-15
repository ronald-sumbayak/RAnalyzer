package ra.sumbayak.ranalyzer.util.text;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

/**
 * Using apache.opennlp.
 * See <a href="https://opennlp.apache.org/docs/1.8.4/manual/opennlp.html">Apache OpenNLP Developer Documentation</a>
 */
public class TextProcessing {
    
    public static List<String> tokenize (String text) {
        try {
            InputStream modelIn = TextProcessing.class.getResourceAsStream ("/openlp/models/en-token.bin");
            TokenizerModel model = new TokenizerModel (modelIn);
            Tokenizer tokenizer = new TokenizerME (model);
            String tokens[] = tokenizer.tokenize (text);
            return new ArrayList<> (Arrays.asList (tokens));
        }
        catch (IOException e) {
            e.printStackTrace ();
            return null;
        }
    }
}
