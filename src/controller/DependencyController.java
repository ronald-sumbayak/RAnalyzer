package controller;

import com.sun.istack.internal.NotNull;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer.CloseFramePolicy;

import java.util.Random;

import entity.*;

public class DependencyController {
    
    public void viewDependency (@NotNull Project project) {
        Graph graph = new SingleGraph ("Dependency Window", false, true);
        graph.addAttribute("ui.antialias");
        graph.addAttribute("ui.quality");
        RequirementDependencyGraph rdGraph = project.getGraph ();
        
        if (rdGraph == null)
            return;
        
        for (Dependency dependency : rdGraph.getDependencyList ()) {
            Requirement src = dependency.getSrc ();
            Requirement dst = dependency.getDst ();

            String id = src.getStatement ().getCode () + "-" + dst.getStatement ().getCode ();
            String type = dependency.getTypeText ();
            String r1 = src.getStatement ().getCode ();
            String r2 = dst.getStatement ().getCode ();

            Edge edge = graph.addEdge (id, r1, r2);
            
            if (edge == null)
                continue;
            
            edge.addAttribute ("ui.label", type);
            edge.getNode0 ().addAttribute ("ui.label", src.getStatement ().getCode ().replace ("F", "     R"));
            edge.getNode1 ().addAttribute ("ui.label", dst.getStatement ().getCode ().replace ("F", "     R"));
        }
    
        graph.addAttribute ("ui.stylesheet", "node { fill-color: green; }");
        graph.display ().setCloseFramePolicy (CloseFramePolicy.CLOSE_VIEWER);
    }
    
    public void checkDependency (@NotNull Project project) {
        RequirementDependencyGraph graph = new RequirementDependencyGraph ();
        Random rand = new Random ();
        int bound = project.getStatementList ().size ();
        
        for (int i = 0; i < bound; i++) {
            Statement s = project.getStatementList ().get (i);
            Statement s1;
            
            do s1 = project.getStatementList ().get (rand.nextInt (bound));
            while (s == s1);

            Requirement src = new Requirement (s);
            Requirement dst = new Requirement (s1);
            Dependency d = new Dependency (new Random ().nextInt (5), src, dst);
            graph.addDependency (d);
        }
        
        project.setGraph (graph);
        viewDependency (project);
    }
}
