package entity;

import java.util.ArrayList;
import java.util.List;

public class RequirementDependencyGraph {
    
    private List<Dependency> dependencyList = new ArrayList<> ();
    
    public List<Dependency> getDependencyList () {
        return dependencyList;
    }
    
    public Dependency getDependency (int i) {
        return dependencyList.get (i);
    }
    
    public void addDependency (Dependency dependency) {
        dependencyList.add (dependency);
    }
}
