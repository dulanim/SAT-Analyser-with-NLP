/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.visualization;

import com.project.traceability.common.PropertyFile;
import it.uniroma1.dis.wsngroup.gexf4j.core.EdgeType;
import it.uniroma1.dis.wsngroup.gexf4j.core.Gexf;
import it.uniroma1.dis.wsngroup.gexf4j.core.Mode;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.GexfImpl;
import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

/**
 * 
 * @author Thanu
 */
public class GraphFileGeneratorTest {

	GraphFileGenerator instance;
	GraphDatabaseService graphDb;

	public GraphFileGeneratorTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
		instance = new GraphFileGenerator();
		
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(
				PropertyFile.getTestDb()).newGraphDatabase();	
		instance.setGraphDb(graphDb);
		Gexf gexf = new GexfImpl();
		gexf.setVisualization(true);
		instance.setGraph(gexf.getGraph());
		instance.getGraph().setDefaultEdgeType(EdgeType.DIRECTED)
				.setMode(Mode.STATIC);

	}

	@After
	public void tearDown() {
		graphDb.shutdown();
	}

	/**
	 * Test of generateGraphFile method, of class GraphFileGenerator.
	 */
	@Test
	public void testGenerateGraphFile() {
		System.out.println("generateGraphFile");
		PropertyFile.setProjectName("Test");
		PropertyFile.setGeneratedGexfFilePath(PropertyFile.getTestGraphFile());
		Transaction tx = graphDb.beginTx();
		try {
			instance.generateGraphFile(graphDb);
			File f = new File(PropertyFile.getTestGraphFile());
			assertTrue(f.exists());
			assertEquals(284, instance.getEdges().size());
			assertEquals(147, instance.getNodes().size());
			PropertyFile.setGeneratedGexfFilePath(null);
		} catch (Exception e) {
			tx.failure();
		} finally {
			tx.finish();
		}
	}

	/**
	 * Test of addNodes method, of class GraphFileGenerator.
	 */
	@Test
	public void testAddNodes() {
		System.out.println("addNodes");
		instance.setEngine(new ExecutionEngine(graphDb));
		instance.addNodes();
		assertNotNull(instance.getNodes());
		assertFalse(instance.getNodes().isEmpty());

	}

	/**
	 * Test of addEdges method, of class GraphFileGenerator.
	 */
	@Test
	public void testAddEdges() {
		System.out.println("addEdges");
		instance.setEngine(new ExecutionEngine(graphDb));
                instance.addNodes();
		instance.addEdges();
		assertEquals(284, instance.getEdges().size());
	}

	/**
	 * Test of importGraphFile method, of class GraphFileGenerator.
	 */
	@Test
	public void testImportGraphFile() {
		System.out.println("importGraphFile");
		PropertyFile.setProjectName("Test");
		PropertyFile.setGeneratedGexfFilePath(PropertyFile.getTestGraphFile());
		instance.importGraphFile();
		File f = new File(PropertyFile.getTestGraphFile());
		assertTrue(f.exists());
		PropertyFile.setGeneratedGexfFilePath(null);

	}
}
