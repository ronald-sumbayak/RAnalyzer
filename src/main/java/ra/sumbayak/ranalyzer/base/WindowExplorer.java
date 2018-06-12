package ra.sumbayak.ranalyzer.base;

import java.io.File;

import javax.swing.JFileChooser;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class WindowExplorer {
    
    private FileChooser fileChooser;
    
    public WindowExplorer (String title) {
        fileChooser = new FileChooser ();
        fileChooser.setTitle (title);
        File ranalyzerProjectsDir = new File (new JFileChooser ().getFileSystemView ().getDefaultDirectory ().toString () + "\\RAnalyzerProjects");
        if (ranalyzerProjectsDir.exists () || ranalyzerProjectsDir.mkdir ())
            fileChooser.setInitialDirectory (ranalyzerProjectsDir);
    }
    
    public void addExtensionFilter (String extensionDescription, String... extensions) {
        fileChooser.getExtensionFilters ().addAll (new ExtensionFilter (extensionDescription, extensions));
    }
    
    public File open () {
        return fileChooser.showOpenDialog (new Stage ());
    }
    
    public File save () {
        return fileChooser.showSaveDialog (new Stage ());
    }
}
