package controller;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import base.*;
import entity.*;
import javafx.application.Platform;

public class ProjectController {
    
    private DialogBox closeWithoutSave;
    
    public ProjectController () {
        closeWithoutSave = new DialogBox ("Close Project", "Close Project without saving?") {
            @Override
            public void onProceed () {
                Platform.exit ();
            }
        };
    }
    
    public Project createNewProject () {
        ProjectDescriptionwindow form = new ProjectDescriptionwindow ();
        Map<String, String> descriptions = form.show ();
        
        if (descriptions == null)
            return null;
        
        String name = descriptions.get (ProjectDescriptionwindow.PROJECT_NAME);
        return new Project (name);
    }
    
    public Project openExistingProject (Project current) {
        WindowExplorer windowExplorer = new WindowExplorer ("Open Existing Project");
        windowExplorer.addExtensionFilter ("RAnalyzer Project File", "*.ran");
        File file = windowExplorer.open ();
        
        if (file == null)
            return current;
        
        Progress progress = new Progress ("Loading Project");
        progress.show ();
    
        Document doc;
    
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ();
            DocumentBuilder builder = factory.newDocumentBuilder ();
            doc = builder.parse (file);
        }
        catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace ();
            return current;
        }
        
        doc.getDocumentElement ().normalize ();
        Element root = doc.getDocumentElement ();
        
        Project project = new Project (null);
        project.read ((Element) root.getChildNodes ().item (0));
    
        project.setFile (file);
        project.setSaved ();
        progress.dismiss ();
        return project;
    }
    
    public void saveProject (Project project) {
        Document doc;
        
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ();
            DocumentBuilder builder = factory.newDocumentBuilder ();
            doc = builder.newDocument ();
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace ();
            return;
        }
    
        Element root = doc.createElement ("RAN");
        root.setAttribute ("version", "1.0");
        doc.appendChild (root);
        project.write (doc, root);
        
        Transformer transformer;
        
        try {
            TransformerFactory factory = TransformerFactory.newInstance ();
            transformer = factory.newTransformer ();
        }
        catch (TransformerConfigurationException e) {
            e.printStackTrace ();
            return;
        }
    
        WindowExplorer windowExplorer = new WindowExplorer ("Save Projet");
        windowExplorer.addExtensionFilter ("RAnalyzer Project File", "*.ran");
        
        File file = windowExplorer.save ();
        if (file == null)
            return;
        
        DOMSource source = new DOMSource (doc);
        StreamResult result = new StreamResult (file);
        
        try {
            transformer.transform (source, result);
        }
        catch (TransformerException e) {
            e.printStackTrace ();
        }
        
        project.setFile (file);
        project.setSaved ();
    }
    
    public void closeProject (Project project) {
        if (project != null && project.isSaved ())
            Platform.exit ();
        else
            closeWithoutSave.show ();
    }
}
