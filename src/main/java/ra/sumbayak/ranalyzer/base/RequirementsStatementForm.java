package ra.sumbayak.ranalyzer.base;

public class RequirementsStatementForm extends Form {
    
    private static final String TITLE = "Edit Requirement";
    private static final String HEADER = "Requirements Requirement Form";
    private static final String OK_BUTTON_LABEL = "Edit";
    
    public static final String STATEMENT = "Requirement";
    
    public RequirementsStatementForm (String instance) {
        super (TITLE, HEADER, OK_BUTTON_LABEL);
        addField (STATEMENT);
        
        if (instance == null)
            return;
        
        fields.get (STATEMENT).setText (instance);
    }
}
