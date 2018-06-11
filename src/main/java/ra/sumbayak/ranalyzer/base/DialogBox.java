package ra.sumbayak.ranalyzer.base;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.StageStyle;

public abstract class DialogBox {
    
    private Alert alert;
    
    public DialogBox (String title, String header) {
        alert = new Alert (AlertType.CONFIRMATION);
        alert.setTitle (title);
        alert.setHeaderText (header);
        alert.initStyle (StageStyle.UTILITY);
    }
    
    public abstract void onProceed ();
    
    public void show () {
        Optional<ButtonType> result = alert.showAndWait ();
        if (result.isPresent () && result.get () == ButtonType.OK)
            onProceed ();
    }
}
