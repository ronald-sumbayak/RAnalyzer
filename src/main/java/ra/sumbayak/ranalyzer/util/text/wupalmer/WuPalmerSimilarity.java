package ra.sumbayak.ranalyzer.util.text.wupalmer;

import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.impl.WuPalmer;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;

/**
 * Taken from <a href="https://www.programcreek.com/2014/01/calculate-words-similarity-using-wordnet-in-java/">Calculate Words Similarity Using Wordnet in Java </a>
 */
class WuPalmerSimilarity {
    
    private static ILexicalDatabase db = new NictWordNet ();
    
    static double compute (String word1, String word2) {
        WS4JConfiguration.getInstance ().setMFS (true);
        // TODO: Can't access WordNet db from inside jar file
        return new WuPalmer (db).calcRelatednessOfWords (word1, word2);
    }
    
    public static void main (String[] args) {
        String[] words = {"add", "get", "filter", "remove", "check", "find", "collect", "create"};
        
        for (int i = 0; i < words.length-1; i++) {
            for (int j = i+1; j < words.length; j++) {
                double distance = compute (words[i], words[j]);
                System.out.println (words[i] +" -  " +  words[j] + " = " + distance);
            }
        }
    }
}
