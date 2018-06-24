package ra.sumbayak.ranalyzer.entity;

import java.util.ArrayList;
import java.util.List;

public class RequirementDependencyGraph {
    
    private List<RequirementDependency> dependencyList = new ArrayList<> ();
    private Double nameWeight, descriptionWeight, threshold;
    
    void addDependency (int type, Requirement src, Requirement dst) {
        dependencyList.add (new RequirementDependency (type, src, dst));
    }
    
    public List<RequirementDependency> getDependencyList () {
        return dependencyList;
    }
    
    public Double getNameWeight () {
        return nameWeight;
    }
    
    public void setNameWeight (Double nameWeight) {
        this.nameWeight = nameWeight;
    }
    
    public Double getDescriptionWeight () {
        return descriptionWeight;
    }
    
    public void setDescriptionWeight (Double descriptionWeight) {
        this.descriptionWeight = descriptionWeight;
    }
    
    public Double getThreshold () {
        return threshold;
    }
    
    public void setThreshold (Double threshold) {
        this.threshold = threshold;
    }
}
