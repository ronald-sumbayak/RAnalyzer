package ra.sumbayak.ranalyzer.util.text.wupalmer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.impl.WuPalmer;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;

public class WuPalmerTable {
    
    private PriorityQueue<WuPalmerCell> queue;
    private List<WuPalmerCell> prime;
    private int row, col;
    private static WuPalmer wuPalmer;
    
    private WuPalmerTable (int row, int col) {
        queue = new PriorityQueue<> (row * col, WuPalmerCell::compare);
        prime = new ArrayList<> ();
        this.row = row;
        this.col = col;
    }
    
    public static WuPalmerTable create (List<String> t1, List<String> t2) {
        WuPalmerTable queue = new WuPalmerTable (t1.size (), t2.size ());
        for (int i = 0; i < queue.row; i++)
            for (int j = 0; j < queue.col; j++)
                queue.set (i, j, wuPalmer.calcRelatednessOfWords (t1.get (i), t2.get (j)));
        return queue;
    }
    
    private void set (int row, int col, double value) {
        if (row < 0 || row >= this.row || col < 0 || col >= this.col)
            return;
        WuPalmerCell cell = new WuPalmerCell (row, col, value);
        queue.add (cell);
    }
    
    private void calcPrime () {
        prime.clear ();
        Set<Integer> bannedRow = new HashSet<> ();
        Set<Integer> bannedCol = new HashSet<> ();
        
        while (!queue.isEmpty ()) {
            WuPalmerCell top = queue.poll ();
            assert top != null;
            
            if (bannedRow.contains (top.row) || bannedCol.contains (top.col))
                continue;
            
            prime.add (top);
            bannedRow.add (top.row);
            bannedCol.add (top.col);
        }
    }
    
    public double calcSimilarity () {
        calcPrime ();
        double sum = prime.stream ().mapToDouble (value -> value.value).sum ();
        return sum / prime.size ();
    }
    
    public static void initWuPalmerImpl () {
        if (wuPalmer != null)
            return;
    
        // Taken from https://www.programcreek.com/2014/01/calculate-words-similarity-using-wordnet-in-java/
        ILexicalDatabase db = new NictWordNet ();
        WS4JConfiguration.getInstance ().setMFS (true);
        wuPalmer = new WuPalmer (db);
    }
}
