package ra.sumbayak.ranalyzer.controller;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.Map;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import javafx.application.Platform;
import ra.sumbayak.ranalyzer.base.DialogBox;
import ra.sumbayak.ranalyzer.base.Progress;
import ra.sumbayak.ranalyzer.base.ProjectDescriptionWindow;
import ra.sumbayak.ranalyzer.base.WindowExplorer;
import ra.sumbayak.ranalyzer.entity.Project;
import ra.sumbayak.ranalyzer.entity.RequirementDependency;
import ra.sumbayak.ranalyzer.entity.Statement;
import ra.sumbayak.ranalyzer.entity.UseCase;
import ra.sumbayak.ranalyzer.entity.UseCaseDependency;
import ra.sumbayak.ranalyzer.utils.DocumentUtil;

public class ProjectController {
    
    private DialogBox closeWithoutSave;
    
    public ProjectController () {
        closeWithoutSave = new DialogBox ("Close project", "Close project without saving?") {
            @Override
            public void onProceed () {
                Platform.exit ();
            }
        };
    }
    
    public Project createNewProject () {
        ProjectDescriptionWindow form = new ProjectDescriptionWindow ();
        Map<String, String> descriptions = form.show ();
        
        if (descriptions == null)
            return null;
        
        String name = descriptions.get (ProjectDescriptionWindow.PROJECT_NAME);
        return new Project (name);
    }
    
    private void extractProject (NodeList nodeList, Project project) {
        for (int i = 0; i < nodeList.getLength (); i++) {
            Node node = nodeList.item (i);
        
            if (node.getNodeType () != Node.ELEMENT_NODE)
                continue;
            
            NamedNodeMap attrs = node.getAttributes ();
        
            switch (node.getNodeName ()) {
                case "UseCase":
                    String name = attrs.getNamedItem ("name").getNodeValue ();
                    String description = attrs.getNamedItem ("description").getNodeValue ();
                    UseCase useCase = new UseCase (name, description);
                    project.getDiagram ().addUseCase (useCase);
                    break;
                    
                case "UseCaseDependency":
                    int ucType = Integer.valueOf (attrs.getNamedItem ("type").getNodeValue ());
                    int ucSrc = Integer.valueOf (attrs.getNamedItem ("src").getNodeValue ());
                    int ucDst = Integer.valueOf (attrs.getNamedItem ("dst").getNodeValue ());
                    UseCaseDependency useCaseDependency = new UseCaseDependency ();
                    useCaseDependency.connect (ucType, project.getDiagram ().getUseCaseList ().get (ucSrc), project.getDiagram ().getUseCaseList ().get (ucDst));
                    project.getDiagram ().addDependency (useCaseDependency);
                    break;
                    
                case "Statement":
                    String value = attrs.getNamedItem ("value").getNodeValue ();
                    project.addStatement (new Statement (value));
                    break;
                    
                case "RequirementDependency":
                    int rDependencyType = Integer.valueOf (attrs.getNamedItem ("type").getNodeValue ());
                    int rSrc = Integer.valueOf (attrs.getNamedItem ("src").getNodeValue ());
                    int rDst = Integer.valueOf (attrs.getNamedItem ("dst").getNodeValue ());
                    RequirementDependency requirementDependency = new RequirementDependency ();
                    requirementDependency.connect (rDependencyType, project.getStatements ().get (rSrc), project.getStatements ().get (rDst));
                    project.getGraph ().addDependency (requirementDependency);
                    break;
                    
                default: break;
            }
            
            if (node.hasChildNodes ())
                extractProject (node.getChildNodes (), project);
        }
    }
    
    public Project openExistingProject (Project current) {
        WindowExplorer windowExplorer = new WindowExplorer ("Open Existing Project");
        windowExplorer.addExtensionFilter ("RAnalyzer Project File", "*.ran");
        
        File file = windowExplorer.open ();
        if (file == null)
            return current;
    
        Document doc = DocumentUtil.open (file);
        if (doc == null)
            return current;
        
        Progress progress = new Progress ("Loading project");
        progress.show ();
        
        // extract project
        Element ran = doc.getDocumentElement ();
        Project project = new Project (ran.getAttribute ("name"));
        extractProject (ran.getChildNodes (), project);
    
        project.setFile (file);
        project.setSaved ();
        progress.dismiss ();
        return project;
    }
    
    public void saveProject (Project project) {
        Document doc = DocumentUtil.create ();
        if (doc == null)
            return;
    
        Element ran = doc.createElement ("RAN");
        ran.setAttribute ("version", "1.1");
        ran.setAttribute ("name", project.getName ());
        doc.appendChild (ran);
        
        Element pkg;
        
        // write use cases
        pkg = doc.createElement ("package");
        pkg.setAttribute ("type", "UseCase");
        ran.appendChild (pkg);
        for (UseCase useCase : project.getDiagram ().getUseCaseList ()) {
            Element uc = doc.createElement ("UseCase");
            uc.setAttribute ("name", useCase.getName ());
            uc.setAttribute ("description", useCase.getDescription ());
            pkg.appendChild (uc);
        }
        
        // write use case dependencies
        pkg = doc.createElement ("package");
        pkg.setAttribute ("type", "UseCaseDependency");
        ran.appendChild (pkg);
        for (UseCaseDependency useCaseDependency : project.getDiagram ().getDependencyList ()) {
            Element ucd = doc.createElement ("UseCaseDependency");
            ucd.setAttribute ("type", String.valueOf (useCaseDependency.getType ()));
            ucd.setAttribute ("src", String.valueOf (project.getDiagram ().getUseCaseList ().indexOf (useCaseDependency.getSrc ())));
            ucd.setAttribute ("dst", String.valueOf (project.getDiagram ().getUseCaseList ().indexOf (useCaseDependency.getDst ())));
            pkg.appendChild (ucd);
        }
        
        // write statements
        pkg = doc.createElement ("package");
        pkg.setAttribute ("type", "Statement");
        ran.appendChild (pkg);
        for (Statement statement : project.getStatements ()) {
            Element s = doc.createElement ("Statement");
            s.setAttribute ("value", statement.getValue ());
            pkg.appendChild (s);
        }
    
        // write requirement dependencies
        pkg = doc.createElement ("package");
        pkg.setAttribute ("type", "RequirementDependency");
        ran.appendChild (pkg);
        for (RequirementDependency requirementDependency : project.getGraph ().getDependencyList ()) {
            Element rd = doc.createElement ("RequirementDependency");
            rd.setAttribute ("type", String.valueOf (requirementDependency.getType ()));
            rd.setAttribute ("src", String.valueOf (project.getStatements ().indexOf (requirementDependency.getSrc ())));
            rd.setAttribute ("dst", String.valueOf (project.getStatements ().indexOf (requirementDependency.getDst ())));
            pkg.appendChild (rd);
        }
        
        Transformer transformer;
        
        try {
            TransformerFactory factory = TransformerFactory.newInstance ();
            transformer = factory.newTransformer ();
        }
        catch (TransformerConfigurationException e) {
            e.printStackTrace ();
            return;
        }
    
        WindowExplorer windowExplorer = new WindowExplorer ("Save Project");
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
