package ra.sumbayak.ranalyzer.base;

public class UseCaseDescriptionForm extends Form {
    
    public static final String TITLE = "Add Description";
    public static final String HEADER = "Use Case Description Form";
    public static final String OK_BUTTON_LABEL = "Submit";
    
    public static final String DESCRIPTION_PROMPT = "Enter description here";
    public static final String DESCRIPTION = "Description";
    
    public UseCaseDescriptionForm () {
        super (TITLE, HEADER, OK_BUTTON_LABEL);
        addTextArea (DESCRIPTION, DESCRIPTION_PROMPT);
    }
}
