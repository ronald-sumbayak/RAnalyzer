package ra.sumbayak.ranalyzer.entity;

public class Requirement {
    
    private String value;
    
    public Requirement (String value) {
        this.value = value;
    }
    
    public String getValue () {
        return value;
    }
    
    public void setValue (String value) {
        this.value = value;
    }
    
    @Override
    public String toString () {
        return value;
    }
}
