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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.IndexManager;

/**
 *
 * @author Aarthika <>
 */
public class GraphDBDelete {

    GraphDatabaseService graphDb;
    private Set<org.neo4j.graphdb.Node> deleteNodeProps;
    private Set<Relationship> relProps;
    String id;

    public GraphDBDelete() {
        System.out.println("chk: "+graphDb);
        
    }

    public void deleteNodeAndRelations(final HashMap<String, Object> nodeProps) {
        //delete the node        
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(
                HomeGUI.projectPath + File.separator + FilePropertyName.PROPERTY + File.separator + HomeGUI.projectName
                + ".graphdb");
        id = nodeProps.get("ID").toString();
        try (Transaction tx = graphDb.beginTx()) {
            IndexManager index = graphDb.index();
            Index<org.neo4j.graphdb.Node> artefacts = index.forNodes("ArtefactElement");
            IndexHits<org.neo4j.graphdb.Node> hits = artefacts.get("ID", id);

            org.neo4j.graphdb.Node neo4j_node = hits.getSingle();
            Iterator<Relationship> nodeRelation = neo4j_node.getRelationships().iterator();
            deleteNodeProps = new HashSet<>();
            relProps = new HashSet<>();
            deleteNodeProps.add(neo4j_node);

            deleteNode(nodeRelation, neo4j_node);
            System.out.println("Size: " + deleteNodeProps.size() + " " + relProps.size());
            delete(deleteNodeProps, relProps, id);
            //new GraphDB().checkDB();
            tx.success();
            // tx.close();
        } finally {
            graphDb.shutdown();
        }
        //graphDb.shutdown();
        //updateXMLFiles(id, nodeProps);
        //returnVal.put("ID", id);
        //returnVal.put("Value", confirm);
    }

    public void deleteNode(Iterator<Relationship> nodeRelation, org.neo4j.graphdb.Node neo4j_node) {
        //System.out.println("Entering ub node delete " + neo4j_node.toString());        
        if (!nodeRelation.hasNext()) {
            //delete the node if there are no relatioships liked to it.
            deleteNodeProps.add(neo4j_node);
            //neo4j_node.delete();
        } else {
            while (nodeRelation.hasNext()) {
                Relationship rel = nodeRelation.next();
                //check if the relationship has any subelements to it.
                if (rel.isType(GraphDB.RelTypes.SUB_ELEMENT) && rel.getStartNode().toString().equalsIgnoreCase(neo4j_node.toString())) {
                    org.neo4j.graphdb.Node subNode = rel.getEndNode();
                    Iterator<Relationship> subRel = subNode.getRelationships().iterator();
                    deleteNode(subRel, subNode);
                } else if (rel.isType(GraphDB.RelTypes.SUB_ELEMENT) && rel.getEndNode().toString().equalsIgnoreCase(neo4j_node.toString())) {
                } else {
                    relProps.add(rel);
                    //rel.delete();
                    //deleteNodeProps.add(neo4j_node);
                }

            }
            // deleteNodeProps.add(neo4j_node);
            // neo4j_node.delete();
        }
    }

    public void delete(Set<org.neo4j.graphdb.Node> nodeProps, Set<Relationship> relProps, String id) {
        String xml = updateXMLFiles(id);        
        ReadXML.deleteNodeFromSourceFile(deleteNodeProps, relProps, xml);

        try (Transaction tx = graphDb.beginTx()) {
            ExecutionEngine execEngine = new ExecutionEngine(graphDb);
            Map<String, Object> map = new HashMap<>();
            map.put("id", id);
            IndexManager index = graphDb.index();
            Index<org.neo4j.graphdb.Node> artefacts = index.forNodes("ArtefactElement");
            Index<Relationship> edges = index.forRelationships("SOURCE_TO_TARGET");

            for (Relationship next : relProps) {
                org.neo4j.graphdb.Node source = next.getStartNode();
                org.neo4j.graphdb.Node target = next.getEndNode();
                System.out.print("Source: " + source.getProperty("ID") + " Target: " + target.getProperty("ID"));
                map.put("relID", next.getStartNode().getProperty("ID") + "-" + next.getEndNode().getProperty("ID"));
                IndexHits<Relationship> relHits = edges.get("ID", source.getProperty("ID") + "-" + target.getProperty("ID"));
                Relationship rel = relHits.getSingle();
                String relid = source.getProperty("ID") + "-" + target.getProperty("ID");
                if (rel != null) {
                    deleteRelDB(rel);
                }
            }

            for (org.neo4j.graphdb.Node next : nodeProps) {
                IndexHits<org.neo4j.graphdb.Node> nodeHits = artefacts.get("ID", next.getProperty("ID"));
                System.out.println("Node : " + next.getProperty("ID"));
                org.neo4j.graphdb.Node node = nodeHits.getSingle();
                if (node.hasRelationship(GraphDB.RelTypes.SUB_ELEMENT)) {
                    Iterable<Relationship> nodeRel = node.getRelationships(GraphDB.RelTypes.SUB_ELEMENT);
                    for (Relationship relDel : nodeRel) {
                        String relid = relDel.getStartNode().getProperty("ID") + "-" + relDel.getEndNode().getProperty("ID");
                        System.out.println("Source: " + relDel.getStartNode().getProperty("ID") + " Target: " + relDel.getEndNode().getProperty("ID"));
                        map.put("relID", relDel.getStartNode().getProperty("ID") + "-" + relDel.getEndNode().getProperty("ID"));
                        map.put("subid", relDel.getEndNode().getProperty("ID"));
                        deleteRelDB(relDel);
                    }
                    deleteNodeDB(node);
                    System.out.println("Deleted a:");
                } else {

                    deleteNodeDB(node);
                    System.out.println("Deletedb:");
                }
            }
            System.out.println("Complete Deletion");
            tx.success();
        } finally {
            System.out.println("Complete Deletion");
            //graphDb.shutdown();
        }

    }

    public void deleteNodeDB(org.neo4j.graphdb.Node node) {
        try (Transaction tx = graphDb.beginTx()) {
            System.out.println("Before:" + node);
            if (node.hasRelationship()) {
                for (Relationship key : node.getRelationships()) {
                    if (key.isType(GraphDB.RelTypes.SUB_ELEMENT) && key.getStartNode() == node) {
                        key.getEndNode().delete();
                        System.out.println("REl" + key);
                        key.delete();
                    } else {
                        System.out.println("ll" + key);
                        key.delete();
                    }
                }
            }

            //node.delete();
            System.out.println("After:" + node);
            System.out.println("Detail:" + node.hasRelationship());

            tx.success();
            tx.close();
        }
        finally{
            //graphDb.shutdown();
        }
    }

    public void deleteRelDB(Relationship rel) {
        try (Transaction tx = graphDb.beginTx()) {
            System.out.println("Before:" + rel);
            if (rel.isType(GraphDB.RelTypes.SUB_ELEMENT)) {
                rel.getEndNode().delete();
                rel.delete();
            } else {
                rel.delete();
            }
            tx.success();
            tx.close();
        }
        finally{
            //graphDb.shutdown();
        }
    }

}
