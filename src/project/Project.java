package project;

import java.util.ArrayList;
import java.util.List;

import dependency.RequirementDependencyGraph;
import document.UCDiagram;
import statement.Statement;

public class Project {
    
    private List<UCDiagram> ucDiagramList = new ArrayList<> ();
    private List<Statement> statementList = new ArrayList<> ();
    private RequirementDependencyGraph rdGraph;
    
    public List<UCDiagram> getUcDiagramList () {
        return ucDiagramList;
    }
    
    public void setUcDiagramList (List<UCDiagram> ucDiagramList) {
        this.ucDiagramList = ucDiagramList;
    }
    
    public RequirementDependencyGraph getRdGraph () {
        return rdGraph;
    }
    
    public void setRdGraph (RequirementDependencyGraph rdGraph) {
        this.rdGraph = rdGraph;
    }
    
    public List<Statement> getStatementList () {
        return statementList;
    }
    
    public void setStatementList (List<Statement> statementList) {
        this.statementList = statementList;
    }
}
