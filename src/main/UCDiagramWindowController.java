package main;

import com.jfoenix.controls.*;

import controller.UCDiagramController;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import entity.Project;
import entity.UseCaseDiagram;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class UCDiagramWindowController {
    
    @FXML
    private JFXListView<UseCaseDiagram> ucDiagramListView;
    private ObservableList<UseCaseDiagram> ucDiagramItems;
    
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
    
    private void addDescription (UseCaseDiagram useCaseDiagram) {
        controller.addDescription (project, useCaseDiagram);
        updateUCDiagramList ();
    }
    
    private void removeUCDiagram (UseCaseDiagram useCaseDiagram) {
        controller.removeUCDiagram (project, useCaseDiagram);
        updateUCDiagramList ();
    }
    
    private void updateUCDiagramList () {
        if (project == null)
            return;
        ucDiagramItems.setAll (project.getUseCaseDiagramList ());
    }
    
    public class UCDiagramCell extends JFXListCell<UseCaseDiagram> {
    
        @Override
        protected void updateItem (UseCaseDiagram item, boolean empty) {
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
                
                FontAwesomeIconView faIconView = new FontAwesomeIconView (FontAwesomeIcon.FOLDER_ALT);
                faIconView.setGlyphSize (32);
                grid.add (faIconView, 0, 0, 1, 2);
                
                Label name = new Label (item.getName ());
                name.setStyle ("-fx-font-weight: bold");
                grid.add (name, 1, 0);
                
                String s = String.format ("%d UseCase - %d Dependency",
                                          item.getUseCaseCount (),
                                          item.getDependencyCount ());
                Label ucd = new Label (s);
                ucd.setStyle ("-fx-text-fill: #666666");
                grid.add (ucd, 1, 1);
                
                if (item.hasDescription ()) {
                    String d = "Description:\n" + item.getDescription ();
                    Label dLabel = new Label (d);
                    grid.add (dLabel, 1, 2, 4, 1);
                }
    
                Separator separator = new Separator (Orientation.VERTICAL);
                grid.add (separator, 2, 0, 1, 2);
                
                JFXButton addDescriptionButton = new JFXButton ("Add Description");
                addDescriptionButton.setOnMouseClicked (event -> addDescription (item));
                    //addDescriptionButton.setVisible (false);
                
                JFXButton removeButton = new JFXButton ("Remove");
                removeButton.setOnMouseClicked (event -> removeUCDiagram (item));
                
                HBox hBox = new HBox ();
                hBox.getChildren ().addAll (addDescriptionButton, removeButton);
                if (item.hasDescription ())
                    hBox.getChildren ().remove (addDescriptionButton);
                hBox.setAlignment (Pos.CENTER_RIGHT);
                grid.add (hBox, 3, 0, 1, 2);
                
                ColumnConstraints c = new ColumnConstraints ();
                c.setHgrow (Priority.ALWAYS);
                grid.getColumnConstraints ().addAll (new ColumnConstraints (), c);
                
                setGraphic (grid);
                setText (null);
            }
        }
    }
}
