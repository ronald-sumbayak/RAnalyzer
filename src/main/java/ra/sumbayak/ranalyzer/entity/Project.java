package ra.sumbayak.ranalyzer.entity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ra.sumbayak.ranalyzer.base.state.project.ProjectState;
import ra.sumbayak.ranalyzer.base.state.project.Saved;
import ra.sumbayak.ranalyzer.base.state.project.Unsaved;

public class Project {
    
    private RequirementDependencyGraph graph = new RequirementDependencyGraph ();
    private UseCaseDiagram diagram = new UseCaseDiagram ();
    private List<Requirement> requirements = new ArrayList<> ();
    private ProjectState state;
    private String name;
    private File file;
    
    public Project (String name) {
        this.name = name;
        state = new Unsaved (this);
    }
    
    public String getName () {
        return name;
    }
    
    public File getFile () {
        return file;
    }
    
    public void setFile (File file) {
        this.file = file;
    }
    
    public RequirementDependencyGraph getGraph () {
        return graph;
    }
    
    public UseCaseDiagram getDiagram () {
        return diagram;
    }
    
    public void addStatement (Requirement statement) {
        requirements.add (statement);
    }
    
    public void addDependency (int type, int srcIndex, int dstIndex) {
        graph.addDependency (type, requirements.get (srcIndex), requirements.get (dstIndex));
    }
    
    public List<Requirement> getRequirements () {
        return requirements;
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
}
