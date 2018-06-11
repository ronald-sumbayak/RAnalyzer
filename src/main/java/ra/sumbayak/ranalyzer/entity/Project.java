package ra.sumbayak.ranalyzer.entity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ra.sumbayak.ranalyzer.base.state.Project.ProjectState;
import ra.sumbayak.ranalyzer.base.state.Project.Saved;
import ra.sumbayak.ranalyzer.base.state.Project.Unsaved;

public class Project {
    
    private List<UseCaseDiagram> useCaseDiagramList = new ArrayList<> ();
    private List<Statement> statementList = new ArrayList<> ();
    private List<UseCase> useCaseList = new ArrayList<> ();
    
    private Map<String, Statement> statementMap = new HashMap<> ();
    private Map<String, UseCase> useCaseMap = new HashMap<> ();
    
    private RequirementDependencyGraph graph;
    private ProjectState state;
    private String name;
    private File file;
    
    static int ucCount;
    static int stCount;
    
    public Project (String name) {
        state = new Unsaved (this);
        this.name = name;
    }
    
    public String getName () {
        return name;
    }
    
    public void addDiagram (UseCaseDiagram diagram) {
        useCaseDiagramList.add (diagram);
    }
    
    public void removeUseCaseDiagram (UseCaseDiagram diagram) {
        List<UseCase> ucList = diagram.getUseCaseList ();
        useCaseList.removeAll (ucList);
        for (UseCase useCase : ucList)
            useCaseMap.remove (useCase.getCode ());
        useCaseDiagramList.remove (diagram);
    }
    
    public void addUseCase (UseCaseDiagram diagram, String name) {
        UseCase uc = new UseCase ("UC" + String.valueOf (++ucCount), name);
        useCaseList.add (uc);
        useCaseMap.put (uc.getCode (), uc);
        diagram.addUseCase (uc);
    }
    
    public UseCase getUseCase (String code) {
        return useCaseMap.get (code);
    }
    
    public UseCase getUseCaseByName (String name) {
        for (UseCase uc : useCaseList)
            if (uc.getName ().equals (name))
                return uc;
        return null;
    }
    
    public boolean containsUseCase (String code) {
        for (UseCase uc : useCaseList)
            if (uc.getName ().equals (code))
                return true;
        return false;
    }
    
    public void addStatement (String s, String ucCode) {
        Statement statement = new Statement ("F" + String.valueOf (++stCount), s, useCaseMap.get (ucCode));
        statementList.add (statement);
        statementMap.put (statement.getCode (), statement);
    }
    
    public void editStatement (Statement statement, String s, String ucCode) {
        statement.edit (s, useCaseMap.get (ucCode));
    }
    
    public void deleteStatement (Statement statement) {
        statementList.remove (statement);
        statementMap.remove (statement.getCode ());
    }
    
    public void setSaved () {
        state.setSaved ();
    }
    
    public void setUnsaved () {
        state.setUnsaved ();
    }
    
    public void changeState (ProjectState newState) {
        state = newState;
    }
    
    public boolean isSaved () {
        return state instanceof Saved;
    }
    
    public boolean isUnsaved () {
        return state instanceof Unsaved;
    }
    
    public List<String> getUseCaseNameList () {
        List<String> useCaseNameList = new ArrayList<> ();
        for (UseCase uc : useCaseList)
            useCaseNameList.add (uc.getFullName ());
        return useCaseNameList;
    }
    
    public final List<UseCaseDiagram> getUseCaseDiagramList () {
        return useCaseDiagramList;
    }
    
    public final List<Statement> getStatementList () {
        return statementList;
    }
    
    public void write (Document doc, Element root) {
        Element project = doc.createElement ("Project");
        project.setAttribute ("name", name);
        project.setAttribute ("ucCount", String.valueOf (ucCount));
        project.setAttribute ("stCount", String.valueOf (stCount));
        root.appendChild (project);
        
        Element uc = doc.createElement ("package");
        project.appendChild (uc);
        
        for (UseCase useCase : useCaseList)
            useCase.write (doc, uc);
        
        Element ucDiagram = doc.createElement ("package");
        project.appendChild (ucDiagram);
        
        for (UseCaseDiagram diagram : useCaseDiagramList)
            diagram.write (doc, ucDiagram);
        
        Element s = doc.createElement ("package");
        project.appendChild (s);
        
        for (Statement statement : statementList)
            statement.write (doc, s);
    }
    
    public File getFile () {
        return file;
    }
    
    public void setFile (File file) {
        this.file = file;
    }
    
    public void read (Element node) {
        // project attributes
        name = node.getAttribute ("name");
        ucCount = Integer.parseInt (node.getAttribute ("ucCount"));
        stCount = Integer.parseInt (node.getAttribute ("stCount"));
        System.out.println (String.format ("%s %d %d", name, ucCount, stCount));
        
        // load use case
        NodeList uc = node.getChildNodes ().item (0).getChildNodes ();
        for (int i = 0; i < uc.getLength (); i++) {
            Element element = (Element) uc.item (i);
            String code = element.getAttribute ("code");
            String name = element.getAttribute ("name");
            System.out.println (code + " " + name);
            UseCase useCase = new UseCase (code, name);
            useCaseList.add (useCase);
            useCaseMap.put (useCase.getCode (), useCase);
        }
        
        // load use case diagram
        NodeList ucDiagram = node.getChildNodes ().item (1).getChildNodes ();
        for (int i = 0; i < ucDiagram.getLength (); i++) {
            // create use case diagram
            Element element = (Element) ucDiagram.item (i);
            String name = element.getAttribute ("name");
            String description = element.getTextContent ();
            UseCaseDiagram diagram = new UseCaseDiagram (name);
            diagram.addDescription (description);
            addDiagram (diagram);
            
            // add use case
            NodeList ucList = element.getChildNodes ().item (0).getChildNodes ();
            for (int j = 0; j < ucList.getLength (); j++) {
                Element ucElement = (Element) ucList.item (j);
                String code = ucElement.getAttribute ("code");
                System.out.println (code);
                diagram.addUseCase (getUseCase (code));
            }
            
            // add use case dependency
            NodeList ucDescription = element.getChildNodes ().item (1).getChildNodes ();
            for (int j = 0; j < ucDescription.getLength (); j++) {
                Element descElement = (Element) ucDescription.item (j);
                int type = Integer.parseInt (descElement.getAttribute ("type"));
                String src = descElement.getAttribute ("src");
                String dst = descElement.getAttribute ("dst");
                System.out.println (src + " " + dst);
                UseCaseDependency dependency = new UseCaseDependency ();
                dependency.connect (type, getUseCase (src), getUseCase (dst));
                diagram.addDependency (dependency);
            }
        }
        
        // load statement
        NodeList s = node.getChildNodes ().item (2).getChildNodes ();
        for (int i = 0; i < s.getLength (); i++) {
            Element element = (Element) s.item (i);
            String code = element.getAttribute ("code");
            String ucCode = element.getAttribute ("uc");
            String text = element.getTextContent ();
            System.out.println (code + " " + ucCode + " " + text);
            
            Statement statement = new Statement (code, text, getUseCase (ucCode));
            statementList.add (statement);
            statementMap.put (statement.getCode (), statement);
        }
    }
    
    public void setGraph (RequirementDependencyGraph graph) {
        this.graph = graph;
    }
    
    public RequirementDependencyGraph getGraph () {
        return graph;
    }
}
