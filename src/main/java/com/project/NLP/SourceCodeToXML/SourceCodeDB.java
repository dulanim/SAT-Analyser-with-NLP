package com.project.NLP.SourceCodeToXML;

import java.util.Map;
import java.util.Map.Entry;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

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
    GraphDatabaseService graphDb;
    Node firstNode;
    Node secondNode;
    Relationship relationship;

    public SourceCodeDB() {
        //start the database server
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase("D:\\f1234.graphdb");
        // registerShutdownHook(graphDb);
    }

    public void start() {
        try (Transaction tx = graphDb.beginTx()) {
            // Database operations go here
            firstNode = graphDb.createNode();
            firstNode.addLabel(DynamicLabel.label( "Node1" ));
            firstNode.setProperty("message", "Hello");
            firstNode.setProperty("value", "abc");
            secondNode = graphDb.createNode();
            secondNode.addLabel(DynamicLabel.label( "Node2" ));
            secondNode.setProperty("message", "World!");

            relationship = firstNode.createRelationshipTo(secondNode, RelTypes.EXTENDS);
            relationship.setProperty("message", "brave Neo4j ");
            tx.success();

            System.out.print(firstNode.getProperty("message"));
            System.out.print(relationship.getProperty("message"));
            System.out.print(secondNode.getProperty("message"));

        }
    }

    public void query() {
        //graphDb = new GraphDatabaseFactory().newEmbeddedDatabase("D:\\SourceCode.graphdb");
        ExecutionEngine execEngine = new ExecutionEngine(graphDb);
        ExecutionResult execResult = execEngine.execute("MATCH (node1:Node1),(node2:Node2) \n"
                + "WHERE node1.message = \"Hello\" \n"
                + "CREATE (node1)-[r:EXTENDS]->(node2) \n"
                + "RETURN r,node2");
        String results = execResult.dumpToString();

        while (execResult.iterator().hasNext()) {
            Map<String, Object> row = execResult.iterator().next();
            for (Entry<String, Object> column : row.entrySet()) {
                System.out.println("Row"+column.getKey() + ": " + column.getValue() + "; ");
            }
        }

        /*
         MATCH (cust:Customer),(cc:CreditCard) 
         WHERE cust.id = "1001" AND cc.id= "5001" 
         CREATE (cust)-[r:DO_SHOPPING_WITH{shopdate:"12/12/2014",price:55000}]->(cc) 
         RETURN r

         */
        System.out.println(results);

    }

    public static void main(String[] args) {
        SourceCodeDB scdb = new SourceCodeDB();
        scdb.start();
        scdb.query();
    }

}
