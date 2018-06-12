package ra.sumbayak.ranalyzer.base;

public class RequirementsStatementForm extends Form {
    
    private static final String TITLE = "Edit Statement";
    private static final String HEADER = "Requirements Statement Form";
    private static final String OK_BUTTON_LABEL = "Edit";
    
    public static final String STATEMENT = "Statement";
    
    public RequirementsStatementForm (String instance) {
        super (TITLE, HEADER, OK_BUTTON_LABEL);
        addField (STATEMENT);
        
        if (instance == null)
            return;
        
        fields.get (STATEMENT).setText (instance);
    }
}
