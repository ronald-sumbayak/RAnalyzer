package ra.sumbayak.ranalyzer.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class Form {
    
    private Dialog<Map<String, String>> dialog;
    private GridPane grid;
    private Node okButton;
    private List<ComboBox<String>> optionList = new ArrayList<> ();
    private List<TextArea> textAreaList = new ArrayList<> ();
    private List<TextField> fieldList = new ArrayList<> ();
    private int fieldCount;
    
    Map<String, ComboBox<String>> options = new HashMap<> ();
    Map<String, TextArea> textAreas = new HashMap<> ();
    Map<String, TextField> fields = new HashMap<> ();
    
    public Form (String title, String header, String okButtonLabel) {
        dialog = new Dialog<> ();
        dialog.setTitle (title);
        dialog.setHeaderText (header);
    
        ButtonType okButtonType = new ButtonType (okButtonLabel, ButtonData.OK_DONE);
        dialog.getDialogPane ().getButtonTypes ().addAll (okButtonType, ButtonType.CANCEL);
    
        grid = new GridPane ();
        grid.setHgap (10);
        grid.setVgap (10);
        grid.setPadding (new Insets (20, 150, 10, 10));
    
        okButton = dialog.getDialogPane ().lookupButton (okButtonType);
        okButton.setDisable (true);
    
        dialog.setResultConverter (dialogButton -> {
            if (dialogButton != okButtonType)
                return null;
            
            Map<String, String> result = new HashMap<> ();
            
            for (Map.Entry<String, TextField> field : fields.entrySet ())
                result.put (field.getKey (), field.getValue ().getText ());
            
            for (Map.Entry<String, ComboBox<String>> option : options.entrySet ())
                result.put (option.getKey (), option.getValue ().getValue ());
            
            for (Map.Entry<String, TextArea> textArea : textAreas.entrySet ())
                result.put (textArea.getKey (), textArea.getValue ().getText ());
            
            return result;
        });
    }
    
    public void addField (String label) {
        TextField textField = new TextField ();
        textField.setPromptText (label);
        textField.textProperty ().addListener ((observable, oldValue, newValue) -> validate ());
    
        grid.add (new Label (label), 0, fieldCount);
        grid.add (textField, 1, fieldCount);
        fieldCount++;
        fieldList.add (textField);
        fields.put (label, textField);
    }
    
    public void addOption (String label, String promptText, ObservableList<String> comboBoxOptions) {
        ComboBox<String> comboBox = new ComboBox<> ();
        comboBox.setPromptText (promptText);
        comboBox.setItems (comboBoxOptions);
        comboBox.valueProperty ().addListener ((observable, oldValue, newValue) -> validate ());
    
        grid.add (new Label (label), 0, fieldCount);
        grid.add (comboBox, 1, fieldCount);
        fieldCount++;
        optionList.add (comboBox);
        options.put (label, comboBox);
    }
    
    public void addTextArea (String label, String promptText) {
        TextArea textArea = new TextArea ();
        textArea.setPromptText (promptText);
        textArea.textProperty ().addListener ((observable, oldValue, newValue) -> validate ());
        
        grid.add (new Label (label), 0, fieldCount);
        grid.add (textArea, 1, fieldCount);
        fieldCount++;
        textAreaList.add (textArea);
        textAreas.put (label, textArea);
    }
    
    private void validate () {
        okButton.setDisable (false);
        
        for (TextField field : fieldList)
            if (field.getText ().isEmpty ())
                okButton.setDisable (true);
        
        for (ComboBox<String> option : optionList)
            if (option.getValue () == null)
                okButton.setDisable (true);
        
        for (TextArea textArea : textAreaList)
            if (textArea.getText ().isEmpty ())
                okButton.setDisable (true);
    }
    
    public Map<String, String> show () {
        dialog.getDialogPane ().setContent (grid);
        Optional<Map<String, String>> result = dialog.showAndWait ();
        return result.orElse (null);
    }
}
