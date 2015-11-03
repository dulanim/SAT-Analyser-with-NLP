package com.project.traceability.manager;

import com.project.traceability.GUI.HomeGUI;
import com.project.traceability.common.PropertyFile;
import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.RequirementModel;
import com.project.traceability.visualization.GraphDB;
import com.project.traceability.visualization.VisualizeGraph;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.openide.util.Lookup;

public class ReadXML {

    public static List<String> relationNodes = new ArrayList<>();

    public static void initApp(String projectPath, String graphType) {

        relationNodes = null;
        try {
            HomeGUI.isComaparing = false;
            ReadFiles.readFiles(projectPath);
            Map<String, ArtefactElement> UMLAretefactElements = UMLArtefactManager.UMLAretefactElements;
            Map<String, ArtefactElement> sourceCodeAretefactElements = SourceCodeArtefactManager.sourceCodeAretefactElements;
            List<RequirementModel> requirementsAretefactElements = RequirementsManger.requirementElements;

            GraphDB graphDB = new GraphDB();
            graphDB.initiateGraphDB();
            graphDB.addNodeToGraphDB(sourceCodeAretefactElements);//add source code artefact elements to db
            graphDB.addNodeToGraphDB(UMLAretefactElements);//add UML artefact elements to db
            graphDB.addRequirementsNodeToGraphDB(requirementsAretefactElements);//add requirement artefact elements to db


            // trace class links between UML & source code
            relationNodes = UMLSourceClassManager
                    .compareClassNames(projectPath);
            graphDB.addRelationTOGraphDB(relationNodes);//add relationships between UML and SourceCode to db

            // trace class links between requirement & source code
            List<String> reqSrcRelationNodes = RequirementSourceClassManager
                    .compareClassNames(projectPath);
            graphDB.addRelationTOGraphDB(reqSrcRelationNodes);//add relationships between Requirments and SourceCode to db

            List<String> reqUMLRelationNodes = RequirementUMLClassManager
                    .compareClassNames(projectPath);
            graphDB.addRelationTOGraphDB(reqUMLRelationNodes);//add relationships between Requirements and UML to db

            relationNodes.addAll(reqSrcRelationNodes);
            relationNodes.addAll(reqUMLRelationNodes);
           
         
            List<String> sourceIntraRelations = IntraRelationManager.getSourceIntraRelation(projectPath);
            System.out.println("Source Intra Relation: "+ sourceIntraRelations.size());
            graphDB.addIntraRelationTOGraphDB(sourceIntraRelations);//add intra relationships between SourceCode elements to db
            relationNodes.addAll(sourceIntraRelations);
            
            List<String> UMLIntraRelations = IntraRelationManager.getUMLIntraRelation(projectPath);
            System.out.println("UML Intra Relation: "+ UMLIntraRelations.size());
            graphDB.addIntraRelationTOGraphDB(UMLIntraRelations);//add intra relationships between UML elements to db
            relationNodes.addAll(UMLIntraRelations);
            
            RelationManager.addLinks(relationNodes);

            graphDB.generateGraphFile();//generate the graph file from db

            VisualizeGraph visual = VisualizeGraph.getInstance();
            visual.importFile();//import the generated graph file into Gephi toolkit API workspace
            GraphModel model = Lookup.getDefault().lookup(GraphController.class).getModel();// get graph model
            visual.setGraph(model, PropertyFile.getGraphType());//set the graph type
            visual.showGraph();//show the graph visualization in tool
           
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}