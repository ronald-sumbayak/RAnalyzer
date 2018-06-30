package ra.sumbayak.ranalyzer.boundary;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ra.sumbayak.ranalyzer.controller.DependencyController;
import ra.sumbayak.ranalyzer.controller.ProjectController;
import ra.sumbayak.ranalyzer.entity.Project;

public class ProjectWorksheetController {
    
    @FXML private UCDiagramWindowController ucDiagramWindowController;
    @FXML private StatementWindowController statementWindowController;
    @FXML private MenuBar menuBar;
    @FXML private TextField nameWeightTextField;
    @FXML private TextField descriptionWeightTextField;
    @FXML private TextField thresholdTextField;
    @FXML private Button viewDependencyButton;
    @FXML private Button checkDependencyButton;
    @FXML private ProgressBar progressBar;
    
    private ProjectController projectController;
    private DependencyController dependencyController;
    private Project project;
    private Stage stage;
    
    @FXML
    public void initialize () {
        projectController = new ProjectController ();
        dependencyController = new DependencyController ();
        ucDiagramWindowController.setProject (project);
        statementWindowController.setProject (project);
    }
    
    public void setStage (Stage stage) {
        this.stage = stage;
        //openExistingProject ();
    }
    
    @FXML
    private void createNewProject () {
        project = projectController.createNewProject ();
        showProject ();
    }
    
    @FXML
    public void openExistingProject () {
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
        if (project.getGraph ().getNameWeight () != null)
            nameWeightTextField.setText (String.valueOf (project.getGraph ().getNameWeight ()));
        if (project.getGraph ().getDescriptionWeight () != null)
            descriptionWeightTextField.setText (String.valueOf (project.getGraph ().getDescriptionWeight ()));
        if (project.getGraph ().getThreshold () != null)
            thresholdTextField.setText (String.valueOf (project.getGraph ().getThreshold ()));
        setLoading (false);
    }
    
    @FXML
    private void saveProject () {
        if (project == null || project.isSaved ())
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
        
        new Thread (() -> {
            Double nameWeight, descriptionWeight, threshold;
            nameWeight = descriptionWeight = threshold = 0.5;

            if (nameWeightTextField.getLength () > 0)
                nameWeight = Double.valueOf (nameWeightTextField.getText ());
            if (descriptionWeightTextField.getLength () > 0)
                descriptionWeight = Double.valueOf (descriptionWeightTextField.getText ());
            if (thresholdTextField.getLength () > 0)
                threshold = Double.valueOf (thresholdTextField.getText ());
            
            dependencyController.checkDependency (ProjectWorksheetController.this, project, nameWeight, descriptionWeight, threshold);
        }).start ();
    }
    
    public void setLoading (boolean loading) {
        menuBar.setDisable (loading);
        nameWeightTextField.setDisable (loading);
        descriptionWeightTextField.setDisable (loading);
        thresholdTextField.setDisable (loading);
        viewDependencyButton.setDisable (loading);
        checkDependencyButton.setDisable (loading);
        progressBar.setProgress (0);
        progressBar.setVisible (loading);
        ucDiagramWindowController.setLoading (loading);
        statementWindowController.setLoading (loading);
    }
    
    public void setProgress (double step) {
        progressBar.setProgress (step);
    }
}
