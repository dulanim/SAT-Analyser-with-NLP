/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.visualization;

import com.project.traceability.common.PropertyFile;
import java.io.File;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperty;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.openide.util.Lookup;

/**
 *
 * @author Thanu
 */
public class VisualizeGraphTest {

    VisualizeGraph instance;
    GraphDBTest test;
    GraphDatabaseService graphDb;
    PreviewController previewController;
    PreviewModel previewModel;
    GraphModel graphModel;
    String graphType;

    public VisualizeGraphTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        instance = VisualizeGraph.getInstance();
        test = new GraphDBTest();
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(
                PropertyFile.getTestDb()).newGraphDatabase();
        previewController = Lookup.getDefault().lookup(
                PreviewController.class);
        previewModel = previewController.getModel();
        graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
        graphType = "Full Graph";
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of setGraphType method, of class VisualizeGraph.
     */
    @Test
    public void testSetGraphType() {
        System.out.println("setGraphType");
        instance.setGraphType(graphType);
    }

    /**
     * Test of importFile method, of class VisualizeGraph.
     */
    @Test
    public void testImportFile() {
        System.out.println("importFile");
        test.testGenerateGraphFile();
        PropertyFile.setProjectName("Test");
        PropertyFile.setGeneratedGexfFilePath(PropertyFile.getTestGraphFile());
        instance.importFile();
        File f = new File(PropertyFile.getTestGraphFile());
        assertTrue(f.exists());
        PropertyFile.setGeneratedGexfFilePath(null);
    }

    /**
     * Test of setPreview method, of class VisualizeGraph.
     */
    @Test
    public void testSetPreview() {
        System.out.println("setPreview");
        instance.setPreview();
        assertEquals(previewModel.getProperties().getBooleanValue(PreviewProperty.SHOW_NODE_LABELS),true);
    }

    /**
     * Test of setLayout method, of class VisualizeGraph.
     */
    @Test
    public void testSetLayout() {
        System.out.println("setLayout");
        instance.setLayout();        
    }

    /**
     * Test of setGraph method, of class VisualizeGraph.
     */
    @Test
    public void testSetGraph_GraphModel_String() {
        System.out.println("setGraph");
        instance.setGraph(graphModel, graphType);        
    }

    /**
     * Test of setGraph method, of class VisualizeGraph.
     */
    @Test
    public void testSetGraph_GraphModel() {
        System.out.println("setGraph");
        instance.setGraph(graphModel);
    }

    /**
     * Test of showGraph method, of class VisualizeGraph.
     */
    @Test
    public void testShowGraph() {
        System.out.println("showGraph");
        instance.showGraph();
        
    }
}
