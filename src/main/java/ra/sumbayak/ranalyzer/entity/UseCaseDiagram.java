package ra.sumbayak.ranalyzer.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public class UseCaseDiagram {
    
    private List<UseCaseDependency> dependencyList = new ArrayList<> ();
    private List<Map<Integer, UseCaseDependency>> dependencyAdjacency = new ArrayList<> ();
    private List<UseCase> useCaseList = new ArrayList<> ();
    
    public void addUseCase (UseCase useCase) {
        useCaseList.add (useCase);
        dependencyAdjacency.add (new HashMap<> ());
    }
    
    public void removeUseCase (int index) {
        UseCase uc = useCaseList.get (index);
        for (UseCaseDependency dependency : dependencyList)
            if (dependency.getSrc () == uc || dependency.getDst () == uc)
                dependencyList.remove (dependency);
        useCaseList.remove (index);
        dependencyAdjacency.remove (index);
        for (Map<Integer, UseCaseDependency> adjacency : dependencyAdjacency)
            adjacency.remove (index);
    }
    
    public void addDependency (int type, UseCase src, UseCase dst) {
        UseCaseDependency dependency = new UseCaseDependency (type, src, dst);
        dependencyList.add (dependency);
        dependencyAdjacency.get (useCaseList.indexOf (src)).put (useCaseList.indexOf (dst), dependency);
    }
    
    public List<UseCase> getUseCaseList () {
        return useCaseList;
    }
    
    public List<UseCaseDependency> getDependencyList () {
        return dependencyList;
    }
    
    public UseCaseDependency getDependency (int src, int dst) {
        System.out.println (String.format ("%d %d", src+1, dst+1));
        return Stream.of (dependencyAdjacency.get (src).get (dst), dependencyAdjacency.get (dst).get (src)).filter (Objects::nonNull).findFirst ().orElse (null);
    }
}
