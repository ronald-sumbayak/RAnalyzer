package ra.sumbayak.ranalyzer.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import ra.sumbayak.ranalyzer.base.RequirementsStatementForm;
import ra.sumbayak.ranalyzer.base.WindowExplorer;
import ra.sumbayak.ranalyzer.entity.Project;
import ra.sumbayak.ranalyzer.entity.Statement;

public class StatementController {
    
    public void addStatement (Project project) {
        // open window explorer
        WindowExplorer windowExplorer = new WindowExplorer ("Open Statement File");
        windowExplorer.addExtensionFilter ("Text File", "*.txt");
        windowExplorer.addExtensionFilter ("RAnalyzer Statement File", "*.rast");
    
        File file = windowExplorer.open ();
        if (file == null)
            return;
    
        // read statements line by line
        try {
            BufferedReader reader = new BufferedReader (new FileReader (file));
            String line;
            while ((line = reader.readLine ()) != null)
                project.addStatement (new Statement (line));
        }
        catch (IOException e) {
            e.printStackTrace ();
        }
        
        project.setUnsaved ();
    }
    
    public void editStatement (Project project, int index) {
        // show requirements statement form
        RequirementsStatementForm form = new RequirementsStatementForm (project.getStatements ().get (index).getValue ());
        Map<String, String> values = form.show ();
    
        if (values == null)
            return;
        
        // edit statement
        String s = values.get (RequirementsStatementForm.STATEMENT);
        project.getStatements ().get (index).setValue (s);
        project.setUnsaved ();
    }
    
    public void deleteStatement (Project project, int index) {
        // remove statement
        project.getStatements ().remove (index);
        project.setUnsaved ();
    }
}
