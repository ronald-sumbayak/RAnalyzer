package ra.sumbayak.ranalyzer.controller;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.util.Pair;
import ra.sumbayak.ranalyzer.base.UseCaseDescriptionForm;
import ra.sumbayak.ranalyzer.base.WindowExplorer;
import ra.sumbayak.ranalyzer.entity.Project;
import ra.sumbayak.ranalyzer.entity.UseCase;
import ra.sumbayak.ranalyzer.entity.UseCaseDependency;
import ra.sumbayak.ranalyzer.util.XMIDocument;

public class UCDiagramController {
    
    private void traverseNode (NodeList nodeList, @NotNull Map<String, Pair<String, String>> useCases, @NotNull List<Pair<Integer, Pair<String, String>>> dependencies) {
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
                        String description = null;
                        
                        // try to extract description if any
                        if (node.hasChildNodes ()) {
                            NodeList extensions = node.getChildNodes ();
                            for (int j = 0; j < extensions.getLength (); j++) {
                                Node ext = extensions.item (j);
                                if (ext.getNodeName ().equals ("xmi:Extension")) {
                                    NodeList extChilds = ext.getChildNodes ();
                                    for (int k = 0; k < extChilds.getLength (); k++) {
                                        Node extChild = extChilds.item (k);
                                        if (extChild.getNodeName ().equals ("documentation")) {
                                            description = extChild.getAttributes ().getNamedItem ("value").getNodeValue ();
                                            if (description.length () == 0)
                                                description = null;
                                        }
                                    }
                                }
                            }
                        }
                        
                        useCases.put (id, new Pair<> (name, description));
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
        
        Document doc = XMIDocument.open (file);
        if (doc == null)
            return;
        
        // extract elements
        List<Pair<Integer, Pair<String, String>>> dependencies = new ArrayList<> ();
        Map<String, Pair<String, String>> useCases = new HashMap<> ();
        traverseNode (doc.getChildNodes (), useCases, dependencies);
        
        // create use case
        Map<String, UseCase> useCaseByName = new HashMap<> ();
        for (Map.Entry<String, Pair<String, String>> item : useCases.entrySet ()) {
            String name = item.getValue ().getKey ();
            String description = item.getValue ().getValue ();
            
            UseCase useCase = new UseCase (name, description);
            project.getDiagram ().addUseCase (useCase);
            useCaseByName.put (item.getKey (), useCase);
        }
        
        // create dependency, connect, and add it to diagram
        for (Pair<Integer, Pair<String, String>> d : dependencies) {
            Integer type = d.getKey ();
            String src = d.getValue ().getKey ();
            String dst = d.getValue ().getValue ();
    
            project.getDiagram ().addDependency (type, useCaseByName.get (src), useCaseByName.get (dst));
        }
        
        project.setUnsaved ();
    }
    
    public void editDescription (Project project, int index) {
        // show use case description form
        UseCaseDescriptionForm form = new UseCaseDescriptionForm (project.getDiagram ().getUseCaseList ().get (index));
        Map<String, String> values = form.show ();
        
        if (values == null)
            return;
        
        // edit name and description
        String name = values.get (UseCaseDescriptionForm.USE_CASE_NAME);
        String description = values.get (UseCaseDescriptionForm.DESCRIPTION);
        UseCase useCase = project.getDiagram ().getUseCaseList ().get (index);
        useCase.setName (name);
        useCase.setDescription (description);
        project.setUnsaved ();
    }
    
    public void removeUseCase (Project project, int index) {
        // remove use case
        project.getDiagram ().removeUseCase (index);
        project.setUnsaved ();
    }
}
