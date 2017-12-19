package base;

public class ProjectDescriptionwindow extends Form {
    
    public static final String TITLE = "Create New Project";
    public static final String HEADER = "Project Description";
    public static final String OK_BUTTON_LABEL = "Continue";
    
    public static final String PROJECT_NAME = "Project Name";
    
    public ProjectDescriptionwindow () {
        super (TITLE, HEADER, OK_BUTTON_LABEL);
        addField (PROJECT_NAME);
    }
}
