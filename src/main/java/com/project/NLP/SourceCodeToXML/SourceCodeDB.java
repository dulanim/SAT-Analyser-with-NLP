package com.project.NLP.SourceCodeToXML;

import com.project.traceability.common.PropertyFile;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.helpers.collection.IteratorUtil;
import org.neo4j.helpers.collection.MapUtil;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author AARTHIKA
 */
public class SourceCodeDB {

    private static enum RelTypes implements RelationshipType {

        EXTENDS
    }

    public static enum ClassLabel implements Label {

        CLASS_NODE
    }
    private static GraphDatabaseService graphDb;
    private static Node classNode;
    private static Node superClassNode;
    private static Relationship relationship;

    public SourceCodeDB() {
        //start the database server
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(PropertyFile.filePath + "\\test6.graphdb");
        registerShutdownHook(graphDb);
    }

    public static void createNodes() {
        classNode = graphDb.createNode(ClassLabel.CLASS_NODE);
        superClassNode = graphDb.createNode(ClassLabel.CLASS_NODE);
        relationship = classNode.createRelationshipTo(superClassNode, RelTypes.EXTENDS);
    }

    public void addRelationshipData(String class1, String id1, String class2) {
        try (Transaction tx = graphDb.beginTx()) {
            // Perform DB operations			
            createNodes();
            classNode.setProperty("className", class1);
            classNode.setProperty("class_id", id1);
            if (!class2.isEmpty()) {
                superClassNode.setProperty("superClassName", class2);
            }
            tx.success();
        }
    }

    public void getRelationshipData() {
        //Identify the relationship class and get the id
        ExecutionEngine execEngine = new ExecutionEngine(graphDb);
        ExecutionResult execResult = execEngine.execute("MATCH (node1:CLASS_NODE),(node2:CLASS_NODE) \n"
                + "CREATE (node1)-[r:EXTENDS]->(node2) \n"
                + "RETURN node1,node2");
        String results = execResult.dumpToString();
        //System.out.println("Result\n"+results);

        /*while (execResult.iterator().hasNext()) {
            Map<String, Object> row = execResult.iterator().next();
            for (Entry<String, Object> column : row.entrySet()) {
                System.out.println("Row" + column.getKey() + ": " + column.getValue() + "; ");
            }
        }*/
    }

    public void getClassID(String className) {
        String classID = "";
        ExecutionEngine execEngine = new ExecutionEngine(graphDb);
        ExecutionResult execResult = execEngine.execute("MATCH (node:CLASS_NODE) \n"
                + "WHERE node.className={className} \n"
                + "RETURN node.class_id,node", MapUtil.map("className", className));
        String results = execResult.dumpToString();
                                    
        Iterator<Node> n_column = execResult.columnAs("node");
        for (Node node : IteratorUtil.asIterable(n_column)) {
            classID = node+":"+node.getProperty("class_id");
        }

        System.out.println("ID - "+classID);
        System.out.println(results);
    }

    private static void registerShutdownHook(final GraphDatabaseService graphDb) {

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
    }

    public static void main(String[] args) {
        SourceCodeDB scdb = new SourceCodeDB();
        scdb.addRelationshipData("Student", "SC1", "Person");
        scdb.addRelationshipData("Person", "SC2", "");
        scdb.addRelationshipData("Employee", "SC3", "Person");
        scdb.addRelationshipData("Train", "SC4", "");
        System.out.println("Added Data");
        scdb.getRelationshipData();
        System.out.println("Got Data");
        scdb.getClassID("Train");
        System.out.println("Got Data ID");
        registerShutdownHook(graphDb);

    }

}
