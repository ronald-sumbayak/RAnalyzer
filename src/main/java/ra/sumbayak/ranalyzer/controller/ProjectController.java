package ra.sumbayak.ranalyzer.controller;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.Map;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import javafx.application.Platform;
import ra.sumbayak.ranalyzer.base.DialogBox;
import ra.sumbayak.ranalyzer.base.ProjectDescriptionWindow;
import ra.sumbayak.ranalyzer.base.WindowExplorer;
import ra.sumbayak.ranalyzer.entity.Project;
import ra.sumbayak.ranalyzer.entity.RequirementDependency;
import ra.sumbayak.ranalyzer.entity.Requirement;
import ra.sumbayak.ranalyzer.entity.UseCase;
import ra.sumbayak.ranalyzer.entity.UseCaseDependency;
import ra.sumbayak.ranalyzer.util.XMIDocument;

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
                    int ucDependencyType = Integer.valueOf (attrs.getNamedItem ("type").getNodeValue ());
                    int ucSrc = Integer.valueOf (attrs.getNamedItem ("src").getNodeValue ());
                    int ucDst = Integer.valueOf (attrs.getNamedItem ("dst").getNodeValue ());
                    project.getDiagram ().addDependency (ucDependencyType, project.getDiagram ().getUseCaseList ().get (ucSrc), project.getDiagram ().getUseCaseList ().get (ucDst));
                    break;
                    
                case "Requirement":
                    String value = attrs.getNamedItem ("value").getNodeValue ();
                    project.addStatement (new Requirement (value));
                    break;
                    
                case "RequirementDependency":
                    int rDependencyType = Integer.valueOf (attrs.getNamedItem ("type").getNodeValue ());
                    int rSrc = Integer.valueOf (attrs.getNamedItem ("src").getNodeValue ());
                    int rDst = Integer.valueOf (attrs.getNamedItem ("dst").getNodeValue ());
                    project.addDependency (rDependencyType, rSrc, rDst);
                    break;
                    
                case "package":
                    switch (attrs.getNamedItem ("type").getNodeValue ()) {
                        case "RequirementDependency":
                            if (attrs.getNamedItem ("nameWeight") != null)
                                project.getGraph ().setNameWeight (Double.valueOf (attrs.getNamedItem ("nameWeight").getNodeValue ()));
                            if (attrs.getNamedItem ("descriptionWeight") != null)
                                project.getGraph ().setDescriptionWeight (Double.valueOf (attrs.getNamedItem ("descriptionWeight").getNodeValue ()));
                            if (attrs.getNamedItem ("threshold") != null)
                                project.getGraph ().setThreshold (Double.valueOf (attrs.getNamedItem ("threshold").getNodeValue ()));
                            break;
                            
                        default: break;
                    }
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
    
        Document doc = XMIDocument.open (file);
        if (doc == null)
            return current;
        
        // extract project
        Element ran = doc.getDocumentElement ();
        Project project = new Project (ran.getAttribute ("name"));
        extractProject (ran.getChildNodes (), project);
    
        project.setFile (file);
        project.setSaved ();
        return project;
    }
    
    public void saveProject (Project project) {
        Document doc = XMIDocument.create ();
        if (doc == null)
            return;
    
        Element ran = doc.createElement ("RAN");
        ran.setAttribute ("version", "1.2");
        ran.setAttribute ("name", project.getName ());
        doc.appendChild (ran);
        
        Element pkg;
        
        if (project.getDiagram ().getUseCaseList ().size () > 0) {
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
        }
    
        if (project.getDiagram ().getDependencyList ().size () > 0) {
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
        }
    
        if (project.getRequirements ().size () > 0) {
            // write statements
            pkg = doc.createElement ("package");
            pkg.setAttribute ("type", "Requirement");
            ran.appendChild (pkg);
            for (Requirement statement : project.getRequirements ()) {
                Element s = doc.createElement ("Requirement");
                s.setAttribute ("value", statement.getValue ());
                pkg.appendChild (s);
            }
        }
    
        if (project.getGraph ().getDependencyList ().size () > 0) {
            // write requirement dependencies
            pkg = doc.createElement ("package");
            pkg.setAttribute ("type", "RequirementDependency");
            pkg.setAttribute ("nameWeight", String.valueOf (project.getGraph ().getNameWeight ()));
            pkg.setAttribute ("descriptionWeight", String.valueOf (project.getGraph ().getDescriptionWeight ()));
            pkg.setAttribute ("threshold", String.valueOf (project.getGraph ().getThreshold ()));
            ran.appendChild (pkg);
            for (RequirementDependency requirementDependency : project.getGraph ().getDependencyList ()) {
                Element rd = doc.createElement ("RequirementDependency");
                rd.setAttribute ("type", String.valueOf (requirementDependency.getType ()));
                rd.setAttribute ("src", String.valueOf (project.getRequirements ().indexOf (requirementDependency.getSrc ())));
                rd.setAttribute ("dst", String.valueOf (project.getRequirements ().indexOf (requirementDependency.getDst ())));
                pkg.appendChild (rd);
            }
        }
        
        File file = project.getFile ();
    
        if (file == null) {
            WindowExplorer windowExplorer = new WindowExplorer ("Save Project");
            windowExplorer.addExtensionFilter ("RAnalyzer Project File", "*.ran");
            windowExplorer.setInitialFileName (project.getName ());
            
            file = windowExplorer.save ();
            if (file == null)
                return;
        }
    
        try {
            TransformerFactory factory = TransformerFactory.newInstance ();
            Transformer transformer = factory.newTransformer ();
            transformer.setOutputProperty (OutputKeys.INDENT, "yes");
            transformer.setOutputProperty ("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform (new DOMSource (doc), new StreamResult (file));
            project.setFile (file);
            project.setSaved ();
        }
        catch (TransformerException e) {
            e.printStackTrace ();
        }
    }
    
    public void closeProject (Project project) {
        if (project != null && project.isSaved ())
            Platform.exit ();
        else
            closeWithoutSave.show ();
    }
}
