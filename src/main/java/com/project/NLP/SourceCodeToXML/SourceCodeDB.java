package com.project.NLP.SourceCodeToXML;

import com.project.traceability.common.PropertyFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.helpers.collection.MapUtil;

/**
 *
 * @author AARTHIKA
 */
@Deprecated
public class SourceCodeDB {

    private static enum RelTypes implements RelationshipType {

        EXTENDS
    }

    public static enum ClassLabel implements Label {

        CLASS_NODE
    }

    private static GraphDatabaseService graphDb;
    private static Node classNode;
    private String fileName = "newdb";

    public SourceCodeDB() {
        //start the database server
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(PropertyFile.filePath + "\\"+fileName+".graphdb");
    }

    public static void createNodes() {
        classNode = graphDb.createNode(ClassLabel.CLASS_NODE);
    }

    /**
     * Adds the class generalization details to db
     *
     * @param class1
     * @param id1
     * @param class2
     * @param type
     */
    public void addInheritanceRelationshipData(String class1, String id1, String class2,String type) {
        if (!checkDataExists(class1, id1)) {
            try (Transaction tx = graphDb.beginTx()) {
                createNodes();
                System.out.println(class1+id1+class2+type);
                classNode.setProperty("className", class1);
                classNode.setProperty("class_id", id1);
                if (!class2.isEmpty()) {
                    classNode.setProperty("superClassName", class2);
                    classNode.setProperty("relationshipType", type);
                }
                
                tx.success();
            }
        }
    }

    public void addAssociationRelationshipData(String class1, String id1, String class2,String type) {
        try (Transaction tx = graphDb.beginTx()) {
            createNodes();
            System.out.println(class1+id1+class2+type);
            classNode.setProperty("className", class1);
            classNode.setProperty("class_id", id1);
            if (!class2.isEmpty()) {
                classNode.setProperty("superClassName", class2);
                classNode.setProperty("relationshipType", type);
            }
              
            tx.success();
        }
        
    }
    
    public boolean checkDataExists(String class1, String id1) {
        ExecutionEngine execEngine = new ExecutionEngine(graphDb);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("className", class1);
        map.put("class_id", id1);
        ExecutionResult execResult = execEngine.execute("MATCH (node:CLASS_NODE) \n"
                + "WHERE node.className={className} AND node.class_id={class_id}\n"
                + "RETURN node,node.class_id", map);
        if (execResult.iterator().hasNext()) {
            return true;
        }
        return false;
    }

    /**
     * Returns the inheritance relationship data
     * @return 
     */
    public ArrayList getRelationshipData() {
        boolean addData = true;
        //Identify the relationship class and get the id
        ArrayList<Map> relationshipList = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        ExecutionEngine execEngine = new ExecutionEngine(graphDb);
        ExecutionResult execResult = execEngine.execute("MATCH (node:CLASS_NODE) \n"
                + "WHERE node.superClassName IS NOT NULL \n"
                + "RETURN node.class_id,node.superClassName,node.relationshipType");
        
        while (execResult.iterator().hasNext()) {
            Map<String, Object> row = execResult.iterator().next();
            for (Entry<String, Object> column : row.entrySet()) {
                if(column.getKey().equalsIgnoreCase("node.superClassName")){
                    String id = getClassID(column.getValue().toString());
                    System.out.println(column.getValue().toString());
                    if(!id.isEmpty()){
                        map.put(column.getKey(), getClassID(column.getValue().toString()));
                        addData = true;
                    }
                    else
                        addData = false;
                }
                else{
                    if(column.getValue()!=null){
                        
                    System.out.println(column.getKey()+column.getValue());
                    map.put(column.getKey(), column.getValue().toString());
                    }
                }                
            }
            if(addData){
                relationshipList.add(map);
            }
        }
        return relationshipList;
    }

    /**
     * Return the class id if the class name is given
     * @param className
     * @return
     */
    public String getClassID(String className) {
        String classID = "";
        ExecutionEngine execEngine = new ExecutionEngine(graphDb);
        ExecutionResult execResult = execEngine.execute("MATCH (node:CLASS_NODE) \n"
                + "WHERE node.className={className} \n"
                + "RETURN node,node.class_id", MapUtil.map("className", className));
        System.out.println(className + execResult);
        if(!execResult.iterator().hasNext()){
            classID = "";
        }
        else{
            classID = execResult.columnAs("node.class_id").next().toString();
        }
        return classID;
    }

    public static void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
    }

    public static void main(String[] args) {
        /*SourceCodeDB scdb = new SourceCodeDB();
        scdb.addRelationshipData("Student", "SC1", "Person");
        scdb.addRelationshipData("Person", "SC2", "");
        scdb.addRelationshipData("Employee", "SC3", "Person");
        scdb.addRelationshipData("Train", "SC4", "");
        System.out.println("Added Data");
        scdb.getRelationshipData();
        System.out.println("Got Data");
        //scdb.getClassID("Train");
        //System.out.println("Got Data ID");
        registerShutdownHook(graphDb);*/
    }

}
