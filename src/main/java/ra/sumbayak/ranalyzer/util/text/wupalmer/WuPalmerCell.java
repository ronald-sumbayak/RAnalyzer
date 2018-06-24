package ra.sumbayak.ranalyzer.util.text.wupalmer;

public class WuPalmerCell {
    
    double value;
    int row, col;
    
    public WuPalmerCell (int row, int col, double value) {
        this.value = Math.min (value, 1);
        this.row = row;
        this.col = col;
    }
    
    static int compare (WuPalmerCell o1, WuPalmerCell o2) {
        return Double.compare (o1.value, o2.value);
    }
    
    public double getValue () {
        return value;
    }
    
    public int getRow () {
        return row;
    }
    
    public int getCol () {
        return col;
    }
    
    @Override
    public String toString () {
        return String.valueOf (value);
    }
}
