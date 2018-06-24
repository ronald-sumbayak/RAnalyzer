package ra.sumbayak.ranalyzer.util.text;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTagger;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

/**
 * Using apache.opennlp.
 * See <a href="https://opennlp.apache.org/docs/1.8.4/manual/opennlp.html">Apache OpenNLP Developer Documentation</a>
 */
public class TextProcessing {
    
    private static final List<String> acceptedTag = Arrays.asList ("VV", "VBN", "NN", "NP", "JJ");
    
    public static String[] tokenize (String text) {
        try {
            InputStream modelIn = TextProcessing.class.getResourceAsStream ("/openlp/models/en-token.bin");
            TokenizerModel model = new TokenizerModel (modelIn);
            Tokenizer tokenizer = new TokenizerME (model);
            return tokenizer.tokenize (text);
        }
        catch (IOException e) {
            e.printStackTrace ();
            return null;
        }
    }
    
    public static String[] tag (String[] text) {
        try {
            InputStream modelIn = TextProcessing.class.getResourceAsStream ("/openlp/models/en-pos-maxent.bin");
            POSModel model = new POSModel (modelIn);
            POSTagger tagger = new POSTaggerME (model);
            return tagger.tag (text);
        }
        catch (IOException e) {
            e.printStackTrace ();
            return null;
        }
    }
    
    public static String[] verbTag (String text) {
        String[] tokens = tokenize (text);
        //System.out.println (Arrays.toString (tokens));
        String[] tags = tag (tokens);
        //System.out.println (Arrays.toString (tags));
        assert tokens != null && tags != null;
        
        List<String> filtered = new ArrayList<> ();
        for (int i = 0; i < tokens.length; i++) {
            System.out.println (String.format ("%s %s", tokens[i], tags[i]));
            int finalI = i;
            if (acceptedTag.stream ().anyMatch (s -> tags[finalI].startsWith (s)))
                filtered.add (tokens[i]);
        }
        
        String[] result = new String[filtered.size ()];
        for (int i = 0; i < filtered.size (); i++)
            result[i] = filtered.get (i);
        //System.out.println ("result: " + Arrays.toString (result));
        
        return result;
    }
}
