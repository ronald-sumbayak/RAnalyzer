package ra.sumbayak.ranalyzer.entity;

public class Statement {
    
    private String value;
    
    public Statement (String value) {
        this.value = value;
    }
    
    public String getValue () {
        return value;
    }
    
    public void setValue (String value) {
        this.value = value;
    }
}
