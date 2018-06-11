package ra.sumbayak.ranalyzer.base;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ra.sumbayak.ranalyzer.entity.Project;
import ra.sumbayak.ranalyzer.entity.Statement;

public class RequirementsStatementForm extends Form {
    
    public static final String TITLE = "Add Statement";
    public static final String HEADER = "Requirements Statement Form";
    public static final String OK_BUTTON_LABEL = "Add";
    
    public static final String USE_CASE_PROMPT = "Select a Use Case";
    public static final String USE_CASE = "Use Case";
    public static final String STATEMENT = "Statement";
    
    public RequirementsStatementForm (Project project, Statement instance) {
        super (TITLE, HEADER, OK_BUTTON_LABEL);
    
        ObservableList<String> useCaseItems = FXCollections.observableArrayList ();
        useCaseItems.setAll (project.getUseCaseNameList ());
        
        addOption (USE_CASE, USE_CASE_PROMPT, useCaseItems);
        addField (STATEMENT);
        
        if (instance == null)
            return;
    
        options.get (USE_CASE).setValue (instance.getUseCase ().getFullName ());
        fields.get (STATEMENT).setText (instance.getStatement ());
    }
}
