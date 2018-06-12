package ra.sumbayak.ranalyzer.base;

import ra.sumbayak.ranalyzer.entity.UseCase;

public class UseCaseDescriptionForm extends Form {
    
    private static final String TITLE = "Edit Description";
    private static final String HEADER = "Use Case Description Form";
    private static final String OK_BUTTON_LABEL = "Submit";
    
    public static final String USE_CASE_NAME = "Use Case Name";
    public static final String DESCRIPTION = "Description";
    private static final String DESCRIPTION_PROMPT = "Enter description here";
    
    public UseCaseDescriptionForm (UseCase instance) {
        super (TITLE, HEADER, OK_BUTTON_LABEL);
        addField (USE_CASE_NAME);
        addTextArea (DESCRIPTION, DESCRIPTION_PROMPT);
        
        if (instance == null)
            return;
        
        fields.get (USE_CASE_NAME).setText (instance.getName ());
        textAreas.get (DESCRIPTION).setText (instance.getDescription ());
    }
}
