package ra.sumbayak.ranalyzer.controller;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer.CloseFramePolicy;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import ra.sumbayak.ranalyzer.entity.Project;
import ra.sumbayak.ranalyzer.entity.RequirementDependency;
import ra.sumbayak.ranalyzer.entity.Statement;
import ra.sumbayak.ranalyzer.entity.UseCase;
import ra.sumbayak.ranalyzer.entity.UseCaseDependency;
import ra.sumbayak.ranalyzer.util.text.TextProcessing;
import ra.sumbayak.ranalyzer.util.text.wupalmer.WuPalmerCell;
import ra.sumbayak.ranalyzer.util.text.wupalmer.WuPalmerQueue;

public class DependencyController {
    
    public void viewDependency (@NotNull Project project) {
        Graph graph = new SingleGraph ("RequirementDependency Window", false, true);
        graph.addAttribute ("ui.antialias");
        graph.addAttribute ("ui.quality");
        
        List<RequirementDependency> dependencyList = project.getGraph ().getDependencyList ();
        
        for (RequirementDependency dependency : dependencyList) {
            Statement src = dependency.getSrc ();
            Statement dst = dependency.getDst ();

            String id = "D" + (dependencyList.indexOf (dependency) + 1);
            String type = dependency.getTypeText ();
            String r1 = "R" + project.getStatements ().indexOf (src);
            String r2 = "R" + project.getStatements ().indexOf (dst);

            Edge edge = graph.addEdge (id, r1, r2);

            if (edge == null)
                continue;

            edge.addAttribute ("ui.label", type);
            edge.getNode0 ().addAttribute ("ui.label", r1);
            edge.getNode1 ().addAttribute ("ui.label", r2);
        }

        graph.addAttribute ("ui.stylesheet", "node { fill-color: green; }");
        graph.display ().setCloseFramePolicy (CloseFramePolicy.CLOSE_VIEWER);
    }
    
    public void checkDependency (@NotNull Project project) {
        List<List<String>> sToken = new ArrayList<> ();
        List<List<String>> nToken = new ArrayList<> ();
        List<List<String>> dToken = new ArrayList<> ();
        
        for (Statement s : project.getStatements ())
            sToken.add (TextProcessing.tokenize (s.getValue ()));
        
        for (UseCase uc : project.getDiagram ().getUseCaseList ()) {
            nToken.add (TextProcessing.tokenize (uc.getName ()));
            dToken.add (TextProcessing.tokenize (uc.getDescription ()));
        }
        
        int row = project.getDiagram ().getUseCaseList ().size ();
        int col = project.getStatements ().size ();
        
        double[][] similarityTable = new double[row][col];
        double nWeight = 0.5;
        double dWeight = 0.5;
        
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                WuPalmerQueue nQueue = WuPalmerQueue.fromToken (nToken.get (i), sToken.get (j));
                WuPalmerQueue dQueue = WuPalmerQueue.fromToken (dToken.get (i), sToken.get (j));
                WuPalmerQueue fQueue = WuPalmerQueue.merge (nQueue, dQueue, nWeight, dWeight);
                similarityTable[i][j] = fQueue.calculateSimilarity ();
            }
        }
        
        List<WuPalmerCell> filtered = new ArrayList<> ();
        double threshold = 0.5f;
        
        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++)
                if (similarityTable[i][j] >= threshold)
                    filtered.add (new WuPalmerCell (i, j, similarityTable[i][j]));
        
        for (WuPalmerCell c1 : filtered) {
            for (WuPalmerCell c2 : filtered) {
                if (c1 == c2)
                    continue;
                
                UseCaseDependency ucDependency = project.getDiagram ().getDependency (c1.getRow (), c2.getRow ());
                if (ucDependency != null)
                    project.addDependency (ucDependency.getType (), c1.getCol (), c2.getCol ());
            }
        }
        
        viewDependency (project);
    }
}
