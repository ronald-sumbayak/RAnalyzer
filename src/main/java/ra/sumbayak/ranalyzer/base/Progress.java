package ra.sumbayak.ranalyzer.base;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Progress {
    
    private Dialog dialog;
    
    public Progress (String title) {
        dialog = new Alert (AlertType.NONE);
        dialog.setTitle (title);
        dialog.setHeaderText (null);
        dialog.setGraphic (null);
        dialog.getDialogPane ().getButtonTypes ().removeAll (ButtonType.OK, ButtonType.CANCEL);
    
        ProgressBar progress = new ProgressBar ();
        StackPane pane = new StackPane ();
        pane.getChildren ().add (progress);
        StackPane.setAlignment (progress, Pos.CENTER);
        dialog.getDialogPane ().setContent (pane);
    }
    
    public void show () {
        dialog.show ();
    }
    
    public void dismiss () {
        dialog.close ();
        Stage stage = (Stage) dialog.getDialogPane ().getScene ().getWindow ();
        stage.close ();
    }
}
