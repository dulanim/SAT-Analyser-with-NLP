/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.visualization;

import com.project.NLP.file.operations.FilePropertyName;
import com.project.traceability.GUI.HomeGUI;
import com.project.traceability.manager.ReadXML;
import static com.project.traceability.visualization.GraphMouseListener.updateXMLFiles;
import java.io.File;
import java.util.HashMap;
import javax.xml.transform.TransformerException;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.IndexManager;
import org.openide.util.Exceptions;

/**
 * Consists the methods related to the updates of node in graph
 * @author Aarthika <>
 */
public class GraphDBEdit {

    GraphDatabaseService graphDb;
    public static boolean lock = false;

    public GraphDBEdit() {
        while(!GraphMouseListener.lock){}
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(
                HomeGUI.projectPath + File.separator + FilePropertyName.PROPERTY + File.separator + HomeGUI.projectName
                + ".graphdb");
    }

    public GraphDatabaseService getGraphDb() {
        return graphDb;
    }
    
    /**
     * Stores the data in a temporary map and edits the relevant backends
     *
     * @param nodeProp
     * @param id
     */
    public void storeUpdatedNode(HashMap<String, Object> nodeProp) {
        
        //System.out.println("Changes in Node:");
        for (String key : nodeProp.keySet()) {
            System.out.println("Key: " + key + " Value: " + nodeProp.get(key).toString());
        }
        
        final String id = nodeProp.get("ID").toString();
        edit(nodeProp, id);
    }

    /**
     * When the node is edited the method updates the changes to the relevant
     * database.
     *
     * @param nodeProps
     * @param id
     */
    public void edit(HashMap<String, Object> nodeProps, String id) {
        String xml = updateXMLFiles(id);
        try {
            ReadXML.readSourceFile(nodeProps, id, xml);
        } catch (TransformerException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        try(Transaction tx = graphDb.beginTx()) {
            IndexManager index = graphDb.index();
            Index<org.neo4j.graphdb.Node> artefacts = index.forNodes("ArtefactElement");
            IndexHits<org.neo4j.graphdb.Node> hits = artefacts.get("ID", id);

            org.neo4j.graphdb.Node neo4j_node = hits.getSingle();
            System.out.println("Properties from edition:");
            for (String key : nodeProps.keySet()) {
                System.out.println("Key: " + key + " Value: " + nodeProps.get(key).toString());
                neo4j_node.setProperty(key, nodeProps.get(key).toString());
            }
            tx.success();
            tx.close();
        } finally {
            graphDb.shutdown();
            GraphDBEdit.lock = true;
        }

    }

}
