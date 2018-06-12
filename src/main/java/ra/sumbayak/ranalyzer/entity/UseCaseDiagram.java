package ra.sumbayak.ranalyzer.entity;

import java.util.ArrayList;
import java.util.List;

public class UseCaseDiagram {
    
    private List<UseCaseDependency> dependencyList = new ArrayList<> ();
    private List<UseCase> useCaseList = new ArrayList<> ();
    
    public void addUseCase (UseCase useCase) {
        useCaseList.add (useCase);
    }
    
    public void addDependency (UseCaseDependency dependency) {
        dependencyList.add (dependency);
    }
    
    public List<UseCase> getUseCaseList () {
        return useCaseList;
    }
    
    public List<UseCaseDependency> getDependencyList () {
        return dependencyList;
    }
}
