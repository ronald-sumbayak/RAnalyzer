package main;

public class Form {
    
    private String title;
    private Button positiveButton, negativeButton;
    
    public Form (String title, String positiveButtonLabel, String negativeButtonLabel) {
        this.title = title;
        positiveButton = new Button (positiveButtonLabel) {
            @Override
            public void onClick () {
        
            }
        };
        negativeButton = new Button (negativeButtonLabel) {
            @Override
            public void onClick () {
        
            }
        };
    }
}
