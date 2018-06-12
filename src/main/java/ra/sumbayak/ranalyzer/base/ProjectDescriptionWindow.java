package ra.sumbayak.ranalyzer.base;

public class ProjectDescriptionWindow extends Form {
    
    private static final String TITLE = "Create New project";
    private static final String HEADER = "Project Description";
    private static final String OK_BUTTON_LABEL = "Continue";
    
    public static final String PROJECT_NAME = "Project Name";
    
    public ProjectDescriptionWindow () {
        super (TITLE, HEADER, OK_BUTTON_LABEL);
        addField (PROJECT_NAME);
    }
}
