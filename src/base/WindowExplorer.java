package base;

import java.io.File;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class WindowExplorer {
    
    private FileChooser fileChooser;
    
    public WindowExplorer (String title) {
        fileChooser = new FileChooser ();
        fileChooser.setInitialDirectory (new File ("C:\\Users\\ronald\\Documents\\Kuliah\\Software " +
                                         "Design\\PPL_D_Kelompok_3(2)"));
        fileChooser.setTitle (title);
    }
    
    public void addExtensionFilter (String extensionDescription, String... extensions) {
        fileChooser.getExtensionFilters ().setAll (new ExtensionFilter (extensionDescription, extensions));
    }
    
    public File open () {
        return fileChooser.showOpenDialog (new Stage ());
    }
    
    public File save () {
        return fileChooser.showSaveDialog (new Stage ());
    }
}
