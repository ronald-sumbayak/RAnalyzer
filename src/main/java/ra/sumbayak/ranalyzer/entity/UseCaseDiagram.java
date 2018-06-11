package ra.sumbayak.ranalyzer.entity;

import org.w3c.dom.*;

import java.util.*;

public class UseCaseDiagram {
    
    private List<UseCaseDependency> dependencyList = new ArrayList<> ();
    private List<UseCase> useCaseList = new ArrayList<> ();
    private String description;
    
    private String name;
    
    public UseCaseDiagram (String name) {
        this.name = name;
    }
    
    public String getName () {
        return name;
    }
    
    public void addUseCase (UseCase useCase) {
        useCaseList.add (useCase);
    }
    
    public void addDependency (UseCaseDependency dependency) {
        dependencyList.add (dependency);
    }
    
    public void addDescription (String description) {
        this.description = description;
    }
    
    public String getDescription () {
        return description;
    }
    
    List<UseCase> getUseCaseList () {
        return useCaseList;
    }
    
    public List<UseCaseDependency> getDependencyList () {
        return dependencyList;
    }
    
    public int getDependencyCount () {
        return dependencyList.size ();
    }
    
    public int getUseCaseCount () {
        return useCaseList.size ();
    }
    
    public boolean hasDescription () {
        return description != null;
    }
    
    public void write (Document doc, Element root) {
        Element ucDiagram = doc.createElement ("UseCaseDiagram");
        ucDiagram.setAttribute ("name", name);
        ucDiagram.setTextContent (description);
        root.appendChild (ucDiagram);
        
        Element uc = doc.createElement ("package");
        ucDiagram.appendChild (uc);
        
        for (UseCase useCase : useCaseList) {
            Element useCaseNode = doc.createElement ("UseCase");
            useCaseNode.setAttribute ("code", useCase.getCode ());
        }
        
        Element ucDependency = doc.createElement ("package");
        ucDiagram.appendChild (ucDependency);
        
        for (UseCaseDependency dependency : dependencyList)
            dependency.write (doc, ucDependency);
    }
}
