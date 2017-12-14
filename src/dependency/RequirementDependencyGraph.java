package dependency;

import com.sun.javafx.scene.control.skin.VirtualFlow.ArrayLinkedList;

import java.util.List;

public class RequirementDependencyGraph {
    
    private List<Dependency> dependencyList = new ArrayLinkedList<> ();
    
    public void addDependency (Dependency dependency) {
        dependencyList.add (dependency);
    }
    
    public List<Dependency> getDependencyList () {
        return dependencyList;
    }
}
