package ra.sumbayak.ranalyzer.boundary;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import ra.sumbayak.ranalyzer.controller.DependencyController;
import ra.sumbayak.ranalyzer.controller.ProjectController;
import ra.sumbayak.ranalyzer.entity.Project;

public class ProjectWorksheetController {
    
    private ProjectController projectController;
    private DependencyController dependencyController;
    private Project project;
    private Stage stage;
    
    @FXML private UCDiagramWindowController ucDiagramWindowController;
    @FXML private StatementWindowController statementWindowController;
    
    @FXML
    public void initialize () {
        projectController = new ProjectController ();
        dependencyController = new DependencyController ();
        ucDiagramWindowController.setProject (project);
        statementWindowController.setProject (project);
    }
    
    public void setStage (Stage stage) {
        this.stage = stage;
    }
    
    @FXML
    private void createNewProject () {
        project = projectController.createNewProject ();
        showProject ();
    }
    
    @FXML
    private void openExistingProject () {
        project = projectController.openExistingProject (project);
        showProject ();
    }
    
    private void showProject () {
        if (project == null)
            return;
        String title = "RAnalyzer - " + project.getName ();
        if (project.getFile () != null)
            title = String.format ("%s [%s]", title, project.getFile ().getPath ());
        stage.setTitle (title);
        ucDiagramWindowController.setProject (project);
        statementWindowController.setProject (project);
    }
    
    @FXML
    private void saveProject () {
        if (project == null)
            return;
        projectController.saveProject (project);
    }
    
    @FXML
    private void closeProject () {
        projectController.closeProject (project);
    }
    
    @FXML
    private void viewDependency () {
        if (project == null)
            return;
        dependencyController.viewDependency (project);
    }
    
    @FXML
    private void checkDependency () {
        if (project == null)
            return;
        dependencyController.checkDependency (project);
    }
}
