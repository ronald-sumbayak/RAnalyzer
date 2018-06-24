package ra.sumbayak.ranalyzer.controller;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer.CloseFramePolicy;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ra.sumbayak.ranalyzer.base.Progress;
import ra.sumbayak.ranalyzer.boundary.ProjectWorksheetController;
import ra.sumbayak.ranalyzer.entity.Project;
import ra.sumbayak.ranalyzer.entity.Requirement;
import ra.sumbayak.ranalyzer.entity.RequirementDependency;
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
            Requirement src = dependency.getSrc ();
            Requirement dst = dependency.getDst ();

            String id = "D" + (dependencyList.indexOf (dependency) + 1);
            String type = dependency.getTypeText ();
            String r1 = "R" + project.getRequirements ().indexOf (src);
            String r2 = "R" + project.getRequirements ().indexOf (dst);

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
    
    public void checkDependency (ProjectWorksheetController worksheet, @NotNull Project project, double nameWeight, double descriptionWeight, double threshold) {
        if (nameWeight + descriptionWeight != 1)
            return;
        
        project.getGraph ().setNameWeight (nameWeight);
        project.getGraph ().setDescriptionWeight (descriptionWeight);
        project.getGraph ().setThreshold (threshold);
        
        Progress progress = new Progress (worksheet);
        progress.show ();
        
        System.out.println (Arrays.toString (project.getDiagram ().getUseCaseList ().toArray ()));
        System.out.println (Arrays.toString (project.getDiagram ().getDependencyList ().toArray ()));
        System.out.println (Arrays.toString (project.getRequirements ().toArray ()));
        
        try {
            int row = project.getRequirements ().size ();
            int col = project.getDiagram ().getUseCaseList ().size ();
            
            progress.setProgressStep (0.95 / (row + col + (2 * row * col)));
            
            List<String[]> rToken = new ArrayList<> ();
            List<String[]> nToken = new ArrayList<> ();
            List<String[]> dToken = new ArrayList<> ();
            
            System.out.println ("\nPOS Tag");
            
            for (int i = 0; i < row; i++) {
                rToken.add (TextProcessing.verbTag (project.getRequirements ().get (i).getValue ()));
                System.out.println (String.format ("R%d: %s", i+1, Arrays.toString (rToken.get (i))));
                progress.makeProgress ();
            }
            
            for (int i = 0; i < col; i++) {
                nToken.add (TextProcessing.verbTag (project.getDiagram ().getUseCaseList ().get (i).getName ()));
                dToken.add (TextProcessing.verbTag (project.getDiagram ().getUseCaseList ().get (i).getDescription ()));
                System.out.println (String.format ("UC%d (Name): %s", i+1, Arrays.toString (nToken.get (i))));
                System.out.println (String.format ("UC%d (Description): %s", i+1, Arrays.toString (dToken.get (i))));
                progress.makeProgress ();
            }
            
            double[][] similarityTable = new double[row][col];
            
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    progress.makeProgress ();
                    
                    if (rToken.get (i).length < 1)
                        continue;
    
                    System.out.println (String.format ("\nR%d - UC%d (Name)", i+1, j+1));
                    if (nToken.get (j).length > 0)
                        similarityTable[i][j] += nameWeight * WuPalmerTable.create (rToken.get (i), nToken.get (j))
                                                                           .calcSimilarity ();
    
                    System.out.println (String.format ("\nR%d - UC%d (Description)", i+1, j+1));
                    if (dToken.get (j).length > 0)
                        similarityTable[i][j] += descriptionWeight * WuPalmerTable.create (rToken.get (i), dToken.get (j))
                                                                                  .calcSimilarity ();
                }
            }
            
            List<WuPalmerCell> filtered = new ArrayList<> ();
            
            System.out.println ("\nSimilarityTable:");
            for (double[] r : similarityTable)
                System.out.println (Arrays.toString (r));
            
            for (int i = 0; i < row; i++)
                for (int j = 0; j < col; j++)
                    if (progress.makeProgress ())
                        if (similarityTable[i][j] >= threshold)
                            filtered.add (new WuPalmerCell (i, j, similarityTable[i][j]));
            
            double progressStep = 0.05 / ((filtered.size () - 1) * filtered.size () / 2.0);
            if (Double.isInfinite (progressStep))
                progressStep = 0.05;
            progress.setProgressStep (progressStep);
            
            System.out.println ("\nAbove threshold: " + Arrays.toString (filtered.toArray ()));
            
            for (int i = 0; i < filtered.size (); i++) {
                for (int j = i+1; j < filtered.size (); j++) {
                    WuPalmerCell c1 = filtered.get (i);
                    WuPalmerCell c2 = filtered.get (j);
                    
                    UseCaseDependency ucDependency = project.getDiagram ().getDependency (c1.getRow (), c2.getRow ());
                    if (ucDependency != null)
                        project.addDependency (ucDependency.getType (), c1.getCol (), c2.getCol ());
                    progress.makeProgress (progressStep);
                }
            }
            
            progress.makeProgress ();
            viewDependency (project);
        }
        catch (Exception e) {
            e.printStackTrace ();
        }
        
        progress.dismiss ();
    }
}
