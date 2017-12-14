package document;

import main.Window;
import project.Project;

public class DocumentAdder {
    
    private Window windowExplorer;
    private Project project;

    public DocumentAdder (Project project) {
        this.project = project;
    }

    public void openWindowExplorer () {
        windowExplorer.show ();
    }

    public void openUCDiagram () {
        
    }

    public void addUCDiagram () {
        
    }
    
    public void closeWindowExplorer () {
        windowExplorer.close ();
    }
}
