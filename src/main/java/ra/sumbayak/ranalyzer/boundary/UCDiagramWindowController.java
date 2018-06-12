package ra.sumbayak.ranalyzer.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import ra.sumbayak.ranalyzer.entity.Project;
import ra.sumbayak.ranalyzer.entity.UseCase;

public class UCDiagramWindowController {
    
    @FXML
    private JFXListView<UseCase> ucDiagramListView;
    private ObservableList<UseCase> ucDiagramItems;
    
    private UCDiagramController controller;
    private Project project;
    
    void setProject (Project project) {
        this.project = project;
        updateUCDiagramList ();
    }
    
    @FXML
    private void initialize () {
        controller = new UCDiagramController ();
        ucDiagramItems = FXCollections.observableArrayList ();
        ucDiagramListView.setCellFactory (param -> new UCDiagramCell ());
        ucDiagramListView.setItems (ucDiagramItems);
    }
    
    @FXML
    private void addUCDiagram () {
        if (project == null)
            return;
        controller.addDocument (project);
        updateUCDiagramList ();
    }
    
    private void editDescription (int index) {
        controller.addDescription (project, index);
        updateUCDiagramList ();
    }
    
    private void removeUCDiagram (int index) {
        controller.removeUCDiagram (project, index);
        updateUCDiagramList ();
    }
    
    private void updateUCDiagramList () {
        if (project == null)
            return;
        ucDiagramItems.setAll (project.getDiagram ().getUseCaseList ());
    }
    
    public class UCDiagramCell extends JFXListCell<UseCase> {
    
        @Override
        protected void updateItem (UseCase item, boolean empty) {
            super.updateItem (item, empty);
            if (empty) {
                setGraphic (null);
                setText (null);
            }
            else {
                GridPane grid = new GridPane ();
                grid.setHgap (15);
                grid.setVgap (4);
                grid.setPadding (new Insets (10, 10, 10, 10));
    
                Label id = new Label ("UC" + String.valueOf (getIndex ()));
                Label name = new Label (item.getName ());
                
                if (item.getDescription () != null) {
                    String d = "Description:\n" + item.getDescription ();
                    Label dLabel = new Label (d);
                    grid.add (dLabel, 1, 2, 4, 1);
                }
    
                Separator separator = new Separator (Orientation.VERTICAL);
                grid.add (separator, 2, 0, 1, 2);
    
                JFXButton editDescriptionButton = new JFXButton ("Edit");
                editDescriptionButton.setOnMouseClicked (event -> editDescription (getIndex ()));
    
                JFXButton removeButton = new JFXButton ("Remove");
                removeButton.setOnMouseClicked (event -> removeUCDiagram (getIndex ()));
                
                HBox hBox = new HBox ();
                hBox.getChildren ().addAll (editDescriptionButton, removeButton);
                hBox.setAlignment (Pos.CENTER_RIGHT);
    
                grid.addRow (0, id, name, separator, hBox);
                
                ColumnConstraints c = new ColumnConstraints ();
                c.setHgrow (Priority.ALWAYS);
                grid.getColumnConstraints ().addAll (new ColumnConstraints (), c);
                
                setGraphic (grid);
                setText (null);
            }
        }
    }
}
