package ra.sumbayak.ranalyzer.util.text.wupalmer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class WuPalmerQueue {
    
    private PriorityQueue<WuPalmerCell> queue;
    private WuPalmerCell[][] table;
    private List<WuPalmerCell> result;
    private int row, col;
    
    public WuPalmerQueue (int row, int col) {
        queue = new PriorityQueue<> (row * col, WuPalmerCell::compare);
        table = new WuPalmerCell[row][col];
        result = new ArrayList<> ();
        this.row = row;
        this.col = col;
    }
    
    public static WuPalmerQueue fromToken (List<String> t1, List<String> t2) {
        System.out.println (String.valueOf (t1));
        System.out.println (String.valueOf (t2));
        WuPalmerQueue queue = new WuPalmerQueue (t1.size (), t2.size ());
        for (int i = 0; i < queue.row; i++)
            for (int j = 0; j < queue.col; j++)
                queue.set (i, j, WuPalmerSimilarity.compute (t1.get (i), t2.get (j)));
        return queue;
    }
    
    public static WuPalmerQueue merge (WuPalmerQueue q1, WuPalmerQueue q2, double w1, double w2) {
        WuPalmerQueue queue = new WuPalmerQueue (q1.row, q1.col);
        assert q1.row == q2.row && q1.col != q2.col;
        for (int i = 0; i < queue.row; i++)
            for (int j = 0; j < queue.col; j++)
                queue.set (i, j, q1.get (i, j) * w1 + q2.get (i, j) * w2);
        return queue;
    }
    
    private double get (int row, int col) {
        return table[row][col].value;
    }
    
    private void set (int row, int col, double value) {
        if (row < 0 || row >= this.row || col < 0 || col >= this.col)
            return;
        WuPalmerCell cell = new WuPalmerCell (row, col, value);
        queue.add (cell);
        table[row][col] = cell;
    }
    
    private void calculateMax () {
        result.clear ();
        Set<Integer> bannedRow = new HashSet<> ();
        Set<Integer> bannedCol = new HashSet<> ();
        
        while (!queue.isEmpty ()) {
            WuPalmerCell top = queue.poll ();
            assert top != null;
            
            if (bannedRow.contains (top.row) || bannedCol.contains (top.col))
                continue;
            
            result.add (top);
            bannedRow.add (top.row);
            bannedCol.add (top.col);
        }
    }
    
    public double calculateSimilarity () {
        calculateMax ();
        double sum = result.stream ().mapToDouble (value -> value.value).sum ();
        return sum / result.size ();
    }
}
