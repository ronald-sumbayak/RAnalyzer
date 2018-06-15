package ra.sumbayak.ranalyzer.entity;

import java.util.ArrayList;
import java.util.List;

public class RequirementDependencyGraph {
    
    private List<RequirementDependency> dependencyList = new ArrayList<> ();
    
    void addDependency (int type, Statement src, Statement dst) {
        dependencyList.add (new RequirementDependency (type, src, dst));
    }
    
    public List<RequirementDependency> getDependencyList () {
        return dependencyList;
    }
}
