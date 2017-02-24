/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.SourceCodeToXML;

import static com.project.NLP.SourceCodeToXML.SourceCodeDB2.createNodes;
import static com.project.NLP.SourceCodeToXML.SourceCodeDB2.getNode;
import com.project.NLP.file.operations.FilePropertyName;
import com.project.traceability.GUI.ProjectCreateWindow;
import com.project.traceability.common.PropertyFile;
import java.io.File;
import java.util.ArrayList;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

/**
 *
 * @author Aarthika <>
 */
public class SourceCodeDBTest {

    GraphDatabaseService graphDb;
    SourceCodeDB2 scdb;

    @Before
    public void startDb() {
        String file = System.getProperty("user.home") + File.separator + 
                "SATAnalyzer" + File.separator + "Workspace" + File.separator + 
                "newfyptest" + File.separator + "property" + File.separator +
                "newfyptest" + "-source" + ".graphdb";
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(file);
        scdb = new SourceCodeDB2();
    }

    @Test
    public void getNodeTest() {
        SourceCodeDB2.setGraphDB(graphDb);
        Node n = getNode("Customer");
        assertNotNull(n);
        n = getNode("Girl");
        assertNull(n);
    }
    
    public void getInheritanceRelationshipDataTest(){
        ArrayList inheritanceList = scdb.getInheritanceRelationshipData();
        
    }
    
    @After
    public void closeDB(){
        graphDb.shutdown();
    }

}
