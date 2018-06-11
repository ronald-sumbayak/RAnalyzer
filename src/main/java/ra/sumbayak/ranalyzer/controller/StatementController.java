package ra.sumbayak.ranalyzer.controller;

import java.util.Map;

import ra.sumbayak.ranalyzer.base.RequirementsStatementForm;
import ra.sumbayak.ranalyzer.entity.Project;
import ra.sumbayak.ranalyzer.entity.Statement;

public class StatementController {
    
    public void addStatement (Project project) {
        // show requirements statement form
        RequirementsStatementForm form = new RequirementsStatementForm (project, null);
        Map<String, String> values = form.show ();
    
        if (values == null)
            return;
        
        String uc = values.get (RequirementsStatementForm.USE_CASE).split (" ")[0];
        String s  = values.get (RequirementsStatementForm.STATEMENT);
        System.out.println ("uc: " + uc);
        
        // add statement to project
        project.addStatement (s, uc);
        project.setUnsaved ();
    }
    
    public void editStatement (Project project, Statement statement) {
        // show requirements statement form
        RequirementsStatementForm form = new RequirementsStatementForm (project, statement);
        Map<String, String> values = form.show ();
    
        if (values == null)
            return;
        
        // edit statement
        String uc = values.get (RequirementsStatementForm.USE_CASE).split (" ")[0];
        String s  = values.get (RequirementsStatementForm.STATEMENT);
        System.out.println ("uc: " + uc);
        project.editStatement (statement, s, uc);
        project.setUnsaved ();
    }
    
    public void deleteStatement (Project project, Statement statement) {
        // destroy
        project.deleteStatement (statement);
        project.setUnsaved ();
    }
}
