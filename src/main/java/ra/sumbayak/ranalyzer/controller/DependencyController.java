package ra.sumbayak.ranalyzer.controller;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer.CloseFramePolicy;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import ra.sumbayak.ranalyzer.base.Progress;
import ra.sumbayak.ranalyzer.entity.Project;
import ra.sumbayak.ranalyzer.entity.RequirementDependency;
import ra.sumbayak.ranalyzer.entity.Statement;
import ra.sumbayak.ranalyzer.entity.UseCase;
import ra.sumbayak.ranalyzer.entity.UseCaseDependency;
import ra.sumbayak.ranalyzer.util.text.TextProcessing;
import ra.sumbayak.ranalyzer.util.text.wupalmer.WuPalmerCell;
import ra.sumbayak.ranalyzer.util.text.wupalmer.WuPalmerTable;

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
        Progress progress = new Progress ("Loading");
        progress.show ();
        
        try {
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
            
            WuPalmerTable.initWuPalmerImpl ();
            
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    if (sToken.get (j).size () < 1)
                        continue;
                    
                    if (nToken.get (i).size () > 0)
                        similarityTable[i][j] += nWeight * WuPalmerTable.create (nToken.get (i), sToken.get (j))
                                                                        .calcSimilarity ();
                    if (dToken.get (i).size () > 0)
                        similarityTable[i][j] += dWeight * WuPalmerTable.create (dToken.get (i), sToken.get (j))
                                                                        .calcSimilarity ();
                }
            }
            
            List<WuPalmerCell> filtered = new ArrayList<> ();
            double threshold = 0.1f;
            
            for (int i = 0; i < row; i++)
                for (int j = 0; j < col; j++)
                    if (similarityTable[i][j] >= threshold)
                        filtered.add (new WuPalmerCell (i, j, similarityTable[i][j]));
            
            for (int i = 0; i < filtered.size (); i++) {
                for (int j = i+1; j < filtered.size (); j++) {
                    WuPalmerCell c1 = filtered.get (i);
                    WuPalmerCell c2 = filtered.get (j);
                    
                    UseCaseDependency ucDependency = project.getDiagram ().getDependency (c1.getRow (), c2.getRow ());
                    if (ucDependency != null)
                        project.addDependency (ucDependency.getType (), c1.getCol (), c2.getCol ());
                }
            }
    
            progress.dismiss ();
            viewDependency (project);
        }
        catch (Exception e) {
            e.printStackTrace ();
            progress.dismiss ();
        }
    }
}
