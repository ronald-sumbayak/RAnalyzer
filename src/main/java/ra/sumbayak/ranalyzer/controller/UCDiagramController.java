package ra.sumbayak.ranalyzer.controller;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javafx.util.Pair;
import ra.sumbayak.ranalyzer.base.UseCaseDescriptionForm;
import ra.sumbayak.ranalyzer.base.WindowExplorer;
import ra.sumbayak.ranalyzer.entity.Project;
import ra.sumbayak.ranalyzer.entity.UseCase;
import ra.sumbayak.ranalyzer.entity.UseCaseDependency;
import ra.sumbayak.ranalyzer.entity.UseCaseDiagram;

public class UCDiagramController {
    
    private void traverseNode (NodeList nodeList, @NotNull Map<String, String> useCases, @NotNull List<Pair<Integer, Pair<String, String>>> dependencies) {
        for (int i = 0; i < nodeList.getLength (); i++) {
            Node node = nodeList.item (i);
            
            if (node.getNodeType () != Node.ELEMENT_NODE)
                continue;
            
            NamedNodeMap nodeMap = node.getAttributes ();
            Node idNode = nodeMap.getNamedItem ("xmi:id");
            
            if (idNode != null) {
                String id = idNode.getNodeValue ();
                
                switch (nodeMap.getNamedItem ("xmi:type").getNodeValue ()) {
                    case "uml:UseCase":
                        String name = nodeMap.getNamedItem ("name").getNodeValue ();
                        useCases.put (id, name);
                        break;
                        
                    case "uml:Include":
                        String includingCase = nodeMap.getNamedItem ("includingCase").getNodeValue ();
                        String addition = nodeMap.getNamedItem ("addition").getNodeValue ();
                        dependencies.add (new Pair<> (UseCaseDependency.TYPE_INCLUDE, new Pair<> (includingCase, addition)));
                        break;
                    
                    case "uml:Extend":
                        String extension = nodeMap.getNamedItem ("extension").getNodeValue ();
                        String extendedCase = nodeMap.getNamedItem ("extendedCase").getNodeValue ();
                        dependencies.add (new Pair<> (UseCaseDependency.TYPE_EXTEND, new Pair<> (extension, extendedCase)));
                        break;
                        
                    default: break;
                }
            }
            
            if (node.hasChildNodes ())
                traverseNode (node.getChildNodes (), useCases, dependencies);
        }
    }
    
    public void addDocument (@NotNull Project project) {
        // open window explorer
        WindowExplorer windowExplorer = new WindowExplorer ("Open Use Case Diagram");
        windowExplorer.addExtensionFilter ("Use Case Diagram File", "*.xmi");
        File file = windowExplorer.open ();
        
        if (file == null)
            return;
        
        Document doc;
        
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance ();
            DocumentBuilder builder = factory.newDocumentBuilder ();
            doc = builder.parse (file);
        }
        catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace ();
            return;
        }
        
        List<Pair<Integer, Pair<String, String>>> dependencies = new ArrayList<> ();
        Map<String, String> useCases = new HashMap<> ();
    
        // open document
        doc.getDocumentElement ().normalize ();
        traverseNode (doc.getChildNodes (), useCases, dependencies);
        
        // create diagram
        UseCaseDiagram diagram = new UseCaseDiagram (file.getName ());
        
        // create use case
        Map<String, UseCase> uc = new HashMap<> ();
        for (Map.Entry<String, String> useCase : useCases.entrySet ()) {
            String name = useCase.getValue ();
            
            if (!project.containsUseCase (name))
                project.addUseCase (diagram, name);
            uc.put (useCase.getKey (), project.getUseCaseByName (name));
        }
        
        // create dependency, connect, add to diagram
        for (Pair<Integer, Pair<String, String>> d : dependencies) {
            Integer type = d.getKey ();
            String src = d.getValue ().getKey ();
            String dst = d.getValue ().getValue ();
            
            UseCaseDependency dependency = new UseCaseDependency ();
            dependency.connect (type, uc.get (src), uc.get (dst));
            diagram.addDependency (dependency);
        }
        
        // add diagram to project
        project.addDiagram (diagram);
        project.setUnsaved ();
    }
    
    public void addDescription (Project project, UseCaseDiagram useCaseDiagram) {
        UseCaseDescriptionForm form = new UseCaseDescriptionForm ();
        Map<String, String> values = form.show ();
        
        if (values == null)
            return;
        
        String description = values.get (UseCaseDescriptionForm.DESCRIPTION);
        useCaseDiagram.addDescription (description);
        
        project.setUnsaved ();
    }
    
    public void removeUCDiagram (Project project, UseCaseDiagram useCaseDiagram) {
        // destroy
        project.removeUseCaseDiagram (useCaseDiagram);
        project.setUnsaved ();
    }
}
