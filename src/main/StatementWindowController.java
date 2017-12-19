package main;

import com.jfoenix.controls.*;

import controller.StatementController;
import entity.Project;
import entity.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;

public class StatementWindowController {
    
    @FXML private JFXListView<Statement> statementListView;
    private ObservableList<Statement> statementItems;
    private StatementController controller;
    private Project project;
    
    void setProject (Project project) {
        this.project = project;
        updateStatementList ();
    }
    
    @FXML
    private void initialize () {
        controller = new StatementController ();
        statementItems = FXCollections.observableArrayList ();
        statementListView.setCellFactory (param -> new StatementCell ());
        statementListView.setItems (statementItems);
    }
    
    @FXML
    private void addStatement () {
        controller.addStatement (project);
        updateStatementList ();
    }
    
    private void editStatement (Statement statement) {
        controller.editStatement (project, statement);
        updateStatementList ();
    }
    
    private void deleteStatement (Statement statement) {
        controller.deleteStatement (project, statement);
        updateStatementList ();
    }
    
    private void updateStatementList () {
        if (project == null)
            return;
        statementItems.setAll (project.getStatementList ());
    }
    
    public class StatementCell extends JFXListCell<Statement> {
        
        @Override
        protected void updateItem (Statement item, boolean empty) {
            super.updateItem (item, empty);
            
            if (empty) {
                setText (null);
                setGraphic (null);
            }
            else {
                setText (null);
    
                GridPane grid = new GridPane ();
                grid.setHgap (15);
                grid.setVgap (4);
                grid.setPadding (new Insets (10, 10, 10, 10));
                
                Label id = new Label (item.getCode ());
                Label uc = new Label (item.getUseCase ().getCode ());
                uc.setMinWidth (50);
                Label s = new Label (item.getStatement ());
                Separator separator = new Separator (Orientation.VERTICAL);
                
                JFXButton editStatementButton = new JFXButton ("Edit");
                editStatementButton.setOnMouseClicked (event -> editStatement (item));
                
                JFXButton deleteStatementButton = new JFXButton ("Remove");
                deleteStatementButton.setOnMouseClicked (event -> deleteStatement (item));
                
                HBox hBox = new HBox ();
                hBox.getChildren ().addAll (editStatementButton, deleteStatementButton);
                hBox.setAlignment (Pos.CENTER_RIGHT);
                
                grid.addRow (0, id, uc, s, separator, hBox);
                
                ColumnConstraints c = new ColumnConstraints ();
                c.setHgrow (Priority.ALWAYS);
                
                grid.getColumnConstraints ().add (new ColumnConstraints ());
                grid.getColumnConstraints ().add (new ColumnConstraints ());
                grid.getColumnConstraints ().add (c);
                
                setGraphic (grid);
            }
        }
    }
}
