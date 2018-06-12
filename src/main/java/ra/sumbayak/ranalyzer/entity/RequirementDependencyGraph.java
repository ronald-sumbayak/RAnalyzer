package ra.sumbayak.ranalyzer.entity;

import java.util.ArrayList;
import java.util.List;

public class RequirementDependencyGraph {
    
    private List<RequirementDependency> dependencyList = new ArrayList<> ();
    
    public void addDependency (RequirementDependency dependency) {
        dependencyList.add (dependency);
    }
    
    public List<RequirementDependency> getDependencyList () {
        return dependencyList;
    }
}
