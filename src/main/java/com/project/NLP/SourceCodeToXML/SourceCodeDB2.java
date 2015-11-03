/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.SourceCodeToXML;

import com.project.traceability.common.PropertyFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.tooling.GlobalGraphOperations;

/**
 *
 * @author AARTHIKA
 */
public class SourceCodeDB2 {

    private static enum RelTypes implements RelationshipType {

        INHERITANCE,
        COMPOSITION
    }

    public static enum ClassLabel implements Label {

        CLASS_NODE
    }

    private static GraphDatabaseService graphDb;
    private static Node firstNode, secondNode;
    private String fileName = "soucredb-111";

    public SourceCodeDB2() {
        //start the database server
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(PropertyFile.filePath + "\\" + fileName + ".graphdb");
        createNodes();
    }

    public static void createNodes() {
        try (Transaction tx = graphDb.beginTx()) {
            firstNode = graphDb.createNode(ClassLabel.CLASS_NODE);
            secondNode = graphDb.createNode(ClassLabel.CLASS_NODE);
            tx.success();
        }
        
    }

    public static Node getNode(String name) {
        ExecutionEngine execEngine = new ExecutionEngine(graphDb);
        Map<String, Object> map = new HashMap<>();
        map.put("className", name);
        Node node = null;
        
        ExecutionResult execResult = execEngine.execute("MATCH (node:CLASS_NODE) \n"
                + "WHERE node.className={className} \n"
                + "RETURN node", map);
        if (execResult.iterator().hasNext()) {
            node = (Node) execResult.columnAs("node").next();
        } else {
            node = null;
        }

        return node;
    }

    public void createNodeRelationship(String class1, String classID1, String class2, String relationshipType) {
        try (Transaction tx = graphDb.beginTx()) {
            
            firstNode = getNode(class1);
            Relationship relation;
            if (firstNode != null) {
                String id = firstNode.getProperty("class_id").toString();
                if (id.isEmpty()) {
                    firstNode.setProperty("class_id", classID1);
                }
            } else {
                firstNode = graphDb.createNode(ClassLabel.CLASS_NODE);
                firstNode.setProperty("className", class1);
                firstNode.setProperty("class_id", classID1);
            }
            if (!class2.isEmpty()) {
                secondNode = getNode(class2);
                if (secondNode == null) {
                    secondNode = graphDb.createNode(ClassLabel.CLASS_NODE);
                    secondNode.setProperty("className", class2);
                    secondNode.setProperty("class_id", "");
                }
                //Adds the relationship to the nodes
                if (relationshipType.equalsIgnoreCase(RelTypes.INHERITANCE.toString())) {
                    //Adds the inheritance relationship
                    Iterable<Relationship> neighbor = firstNode.getRelationships(Direction.OUTGOING, RelTypes.INHERITANCE);
                    if (neighbor.iterator().hasNext()) {
                        for (Relationship neighbor1 : neighbor) {
                            if (!neighbor1.getEndNode().equals(secondNode)) {
                                relation = firstNode.createRelationshipTo(secondNode, RelTypes.INHERITANCE);
                                relation.setProperty("Relationship", RelTypes.INHERITANCE.toString());
                            }
                        }
                    } else {
                        relation = firstNode.createRelationshipTo(secondNode, RelTypes.INHERITANCE);
                        relation.setProperty("Relationship", RelTypes.INHERITANCE.toString());
                    }
                } else if (relationshipType.equalsIgnoreCase(RelTypes.COMPOSITION.toString())) {
                    boolean equal = false;
                    //Adds the composition relationship
                    Iterable<Relationship> neighbor = firstNode.getRelationships(Direction.OUTGOING, RelTypes.COMPOSITION);
                    if (neighbor.iterator().hasNext()) {
                        for (Relationship neighbor1 : neighbor) {                            
                            if (neighbor1.getEndNode().equals(secondNode)) {    
                                equal = true;
                                break;
                            }
                        }
                        //Checks if the relationship already exists or not. If not creates the relationship.
                        if(!equal){
                            relation = firstNode.createRelationshipTo(secondNode, RelTypes.COMPOSITION);
                            relation.setProperty("Relationship", RelTypes.COMPOSITION.toString());
                        }
                    } else {                        
                        relation = firstNode.createRelationshipTo(secondNode, RelTypes.COMPOSITION);
                        relation.setProperty("Relationship", RelTypes.COMPOSITION.toString());
                    }
                }

            }
            tx.success();
        }
    }

    public ArrayList getInheritanceRelationshipData() {
        ArrayList<Map> relationshipList = new ArrayList<>();
        Map<String, String> map;

        try (Transaction tx = graphDb.beginTx()) {

            for (Node n : GlobalGraphOperations.at(graphDb).getAllNodesWithLabel(ClassLabel.CLASS_NODE)) {
                map = new HashMap<>();
               
                for (Relationship r : n.getRelationships(RelTypes.INHERITANCE, Direction.OUTGOING)) {
                    Node o = r.getOtherNode(n);
                    map.put("1", n.getProperty("class_id").toString());
                    map.put("2", o.getProperty("class_id").toString());
                    relationshipList.add(map);
                }
            }
        }
        return relationshipList;
    }

    public ArrayList getAssociationRelationshipData() {
        ArrayList<Map> relationshipList = new ArrayList<>();
        Map<String, String> map;

        try (Transaction tx = graphDb.beginTx()) {
            int count =0;
            for (Node n : GlobalGraphOperations.at(graphDb).getAllNodesWithLabel(ClassLabel.CLASS_NODE)) {
                map = new HashMap<>();
                count++;
                
                for (Relationship r : n.getRelationships(RelTypes.COMPOSITION, Direction.OUTGOING)) {
                    Node o = r.getOtherNode(n);
                    
                    if (!o.getProperty("class_id").toString().isEmpty()) {
                        System.out.println("g " + n.getProperty("class_id") + " " + o.getProperty("class_id"));
                        map.put("1", n.getProperty("class_id").toString());
                        map.put("2", o.getProperty("class_id").toString());
                        relationshipList.add(map);
                    }

                }
            }
            System.out.println(count);
        }

        return relationshipList;
    }

    /*public static void main(String args[]){
     SourceCodeDB2 scdb2 = new SourceCodeDB2();
     scdb2.createNodeRelationship("Person", "01", "", "");
     scdb2.createNodeRelationship("Student", "02", "Person", "INHERITANCE");
     scdb2.createNodeRelationship("Employee", "03", "Person", "INHERITANCE");
     scdb2.createNodeRelationship("SavingAccount", "04", "Account", "INHERITANCE");
     scdb2.createNodeRelationship("Account", "05", "", "");
        
     scdb2.createNodeRelationship("Student", "02", "SavingAccount", "COMPOSITION");
     scdb2.createNodeRelationship("Employee", "03", "Student", "COMPOSITION");
        
     System.out.println("Relationships created");
        
     ArrayList<Map> list = scdb2.getAssociationRelationshipData();
     System.out.println("Got relationship datas " +list.size());
     for (Map list1 : list) {
     System.out.print("1 - "+list1.get("1")+" ");
     System.out.print("2 - "+list1.get("2")+" ");
            
     System.out.println();
     }
     list.clear();
     list = scdb2.getInheritanceRelationshipData();
     System.out.println("Got relationship datas " +list.size());
     for (Map list1 : list) {
     System.out.print("1 - "+list1.get("1")+" ");
     System.out.print("2 - "+list1.get("2")+" ");
            
     System.out.println();
     }
        
     }*/
}
