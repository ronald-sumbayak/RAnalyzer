package document;

import main.Form;
import main.Progress;

public class DescriptionAdder {
    
    private Progress progress;
    private UCDiagram ucDiagram;
    private Form ucdForm;

    public DescriptionAdder (UCDiagram ucDiagram) {
        this.ucDiagram = ucDiagram;
    }
    
    public void showProgressBar () {
        progress.show ();
    }

    public void showUCDescriptionForm () {
        
    }

    public void addUCDescription() {
        
    }
}
