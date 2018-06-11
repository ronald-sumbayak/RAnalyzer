package ra.sumbayak.ranalyzer;

import javax.swing.JFileChooser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ra.sumbayak.ranalyzer.controller.ProjectWorksheetController;

public class RAnalyzer extends Application {
    
    @Override
    public void start (Stage primaryStage) throws Exception {
        Font.loadFont (RAnalyzer.class.getResource ("/fa/fontawesome-webfont.ttf").toExternalForm (), 10);
        
        FXMLLoader loader = new FXMLLoader ();
        loader.setLocation (RAnalyzer.class.getResource ("/fx/ProjectWorksheet.fxml"));
    
        VBox rootLayout = loader.load ();
        primaryStage.setTitle ("RAnalyzer");
        primaryStage.setScene (new Scene (rootLayout));
        primaryStage.show ();
        
        ProjectWorksheetController controller = loader.getController ();
        controller.setStage (primaryStage);
    }
    
    public static void main (String args[]) {
        System.setProperty ("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
        launch (args);
    }
}
