package main;

public abstract class Button {

    private String label;
    
    public Button (String label) {
        this.label = label;
    }
    
    public abstract void onClick ();
    
    public String getLabel () {
        return label;
    }
    
    public void setLabel (String label) {
        this.label = label;
    }
}
