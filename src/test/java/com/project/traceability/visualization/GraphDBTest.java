/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.visualization;

import com.project.NLP.SourceCodeToXML.SourceCodeDB2;
import com.project.traceability.GUI.HomeGUI;
import com.project.traceability.common.PropertyFile;
import com.project.traceability.manager.IntraRelationManager;
import com.project.traceability.manager.ReadFiles;
import com.project.traceability.manager.RequirementSourceClassManager;
import com.project.traceability.manager.RequirementUMLClassManager;
import com.project.traceability.manager.RequirementsManger;
import com.project.traceability.manager.SourceCodeArtefactManager;
import com.project.traceability.manager.UMLArtefactManager;
import com.project.traceability.manager.UMLSourceClassManager;
import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.ArtefactSubElement;
import com.project.traceability.model.RequirementModel;
import com.project.traceability.visualization.GraphDB;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.helpers.collection.IteratorUtil;
import org.neo4j.tooling.GlobalGraphOperations;

/**
 *
 * @author Thanu
 */
public class GraphDBTest {

    static GraphDatabaseService graphDbService;
    static ArtefactElement sourceElement;
    static ArtefactElement UMLElement;
    static ArtefactSubElement fieldSubElement;
    static ArtefactSubElement methodSubElement;
    static ArtefactSubElement attributeSubElement;
    static ArtefactSubElement operationSubElement;
    static ArtefactElement requirement;
    static ArtefactSubElement reqfieldSubElement;
    static ArtefactSubElement reqmethodSubElement;
    static Map<String, ArtefactElement> aretefactElements;
    static List<RequirementModel> reqModel;
    static GraphDB graphDB;
    static List<String> relation = new ArrayList<>();
    static Map<String, ArtefactElement> UMLAretefactElements;
    static Map<String, ArtefactElement> sourceCodeAretefactElements;
    static Map<String, ArtefactElement> requirementsAretefactElements;
    static String filePath;
    static String projectName;
    static GraphDBTest test;
    static String projectPath;

    public GraphDBTest() {
        System.out.println("graphDBTest");
    }

    @BeforeClass
    public static void setUpClass() {
        System.out.println("setupclass");
        ReadFiles.readFiles(System.getProperty("user.home") + File.separator + "SATAnalyzer" + File.separator + "Test");
        //<ArtefactElement id="SC1" interface="[]" name="Customer" superClass="" type="Class" visibility="public">
        sourceElement = new ArtefactElement("SC1", "Customer", "Class",
                "public", null);
        //<ArtefactSubElement id="SC1_F2" name="name" type="Variable" variableType="String" visibility="private"/>
        fieldSubElement = new ArtefactSubElement("SC1_F2", "name",
                "private", null, "Variable");
        //<ArtefactSubElement id="SC1_M2" name="reciveOrder" parameters="" returnType="void" type="Method" visibility="Private"/>
        methodSubElement = new ArtefactSubElement("SC1_M2", "reciveOrder",
                "public", "void", "Method");
        List<ArtefactSubElement> sourceSubElements = new ArrayList<>();
        sourceSubElements.add(fieldSubElement);
        sourceSubElements.add(methodSubElement);
        sourceElement.setArtefactSubElements(sourceSubElements);

        //<ArtefactElement id="D1" name="Customer" type="Class">
        UMLElement = new ArtefactElement("D1", "Customer", "Class", null,
                null);
        //<ArtefactSubElement id="D1_F1" name="name" type="UMLAttribute" variableType="" visibility="Protcted"/>
        attributeSubElement = new ArtefactSubElement("D1_F1", "name",
                "private", null, "UMLAttribute");
        //<ArtefactSubElement id="D1_M2" name="recieveOrder" parameters="" returnType="" type="UMLOpertion" visibility="public"/>
        operationSubElement = new ArtefactSubElement("D1_M2", "recieveOrder",
                "private", null, "UMLOperation");
        List<ArtefactSubElement> UMLSubElements = new ArrayList<>();
        UMLSubElements.add(attributeSubElement);
        UMLSubElements.add(operationSubElement);
        sourceElement.setArtefactSubElements(UMLSubElements);

        aretefactElements = new HashMap<>();
        aretefactElements.put(sourceElement.getArtefactElementId(),
                sourceElement);
        aretefactElements.put(UMLElement.getArtefactElementId(), UMLElement);

        //<ArtefactElement id="RQ3" name="customer" type="Class">
        requirement = new ArtefactElement("RQ3", "customer", "Class", null, null);
        //<ArtefactSubElement id="RQ3_F2" name="name" type="Field" variableType="" visibility=""/>
        reqfieldSubElement = new ArtefactSubElement("RQ3_F2", "name",
                "", null, "Field");
        //<ArtefactSubElement id="RQ3_M2" name="receive" parameters="" returnType="" type="Method" visibility=""/>
        reqmethodSubElement = new ArtefactSubElement("RQ3_M2", "receive",
                "", "", "Method");

        filePath = System.getProperty("user.home") + File.separator + "SATAnalyzer" + File.separator;
        projectPath = System.getProperty("user.home") + File.separator + "SATAnalyzer" + File.separator + "Test";
        projectName = "Test";
        PropertyFile.setFilePath(filePath);
        PropertyFile.setProjectName(projectName);

        UMLAretefactElements = UMLArtefactManager.UMLAretefactElements;

        requirementsAretefactElements = RequirementsManger.requirementArtefactElements;

        sourceCodeAretefactElements = SourceCodeArtefactManager.sourceCodeAretefactElements;

        graphDB = new GraphDB();
        graphDB.setGraphDb(graphDbService);
        test = new GraphDBTest();
    }

    @AfterClass
    public static void tearDownClass() {
        System.out.println("teardownclass");
    }

    @Before
    public void setUp() {
        System.out.println("setup");
        graphDbService = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(
                PropertyFile.getTestDb()).newGraphDatabase();
        graphDB.setGraphDb(graphDbService);
    }

    @After
    public void tearDown() {
        System.out.println("teardown");
        graphDbService.shutdown();
    }

    /**
     * Test of addNodeToGraphDB method, of class GraphDB.
     */
    @Test
    public void testAddNodeToGraphDB() {
        System.out.println("addNodeToGraphDB");
        Transaction tx = graphDbService.beginTx();
        try {
            graphDB.addNodeToGraphDB("UML", UMLAretefactElements);
            graphDB.addNodeToGraphDB("SRC", sourceCodeAretefactElements);
            IndexManager index = graphDbService.index();
            Index<Node> artefacts = index.forNodes("ArtefactElement");

            IndexHits<Node> first = artefacts.get("ID",
                    sourceElement.getArtefactElementId());
            Node firstNode = first.getSingle();

            assertEquals(firstNode.getProperty("ID").toString(),
                    sourceElement.getArtefactElementId());
            assertEquals(firstNode.getProperty("Name").toString(),
                    sourceElement.getName());
            assertEquals(firstNode.getProperty("Type").toString(),
                    sourceElement.getType());
            // assertEquals(firstNode.getProperty("Visibility").toString(),sourceElement.getVisibility());

            IndexHits<Node> second = artefacts.get("ID",
                    operationSubElement.getSubElementId());
            Node secondNode = second.getSingle();
            assertEquals(secondNode.getProperty("ID").toString(),
                    operationSubElement.getSubElementId());
            assertEquals(secondNode.getProperty("Name").toString(),
                    operationSubElement.getName());
            assertEquals(secondNode.getProperty("Type").toString(),
                    operationSubElement.getType());
            // assertEquals(secondNode.getProperty("Visibility").toString(),operationSubElement.getVisibility());
            assertEquals(34, IteratorUtil.count(GlobalGraphOperations.at(
                    graphDbService).getAllNodes()));

        } catch (Exception e) {
            e.printStackTrace();
            tx.failure();
        } finally {
            tx.success();
        }
    }

    /**
     * Test of addRelationTOGraphDB method, of class GraphDB.
     */
    @Test
    public void testAddRelationTOGraphDB() {
        System.out.println("addRelationTOGraphDB");
        HomeGUI.projectPath = projectPath;
        relation = UMLSourceClassManager.compareClassNames(projectPath);
        relation.addAll(RequirementSourceClassManager
                .compareClassNames(projectPath));
        relation.addAll(RequirementUMLClassManager
                .compareClassNames(projectPath));

        Transaction tx = graphDbService.beginTx();
        try {
            graphDB.addNodeToGraphDB("UML", UMLAretefactElements);
            graphDB.addNodeToGraphDB("SRC", sourceCodeAretefactElements);
            graphDB.addNodeToGraphDB("REQ", requirementsAretefactElements);
            graphDB.addRelationTOGraphDB(relation);
            int count = 0;
            for (Relationship n : GlobalGraphOperations.at(graphDbService).getAllRelationships()) {
                System.out.println(count + " " + n);
                count++;
            }
            assertEquals(22, IteratorUtil.count(GlobalGraphOperations.at(
                    graphDbService).getAllRelationships()));
        } catch (Exception e) {
            tx.failure();
        } finally {
            tx.success();
        }

    }

    /**
     * Test of addIntraRelationTOGraphDB method, of class GraphDB.
     */
    @Test
    public void testAddIntraRelationTOGraphDB() {
        System.out.println("addIntraRelationTOGraphDB");
        relation = UMLSourceClassManager.compareClassNames(projectPath);
        relation.addAll(RequirementSourceClassManager
                .compareClassNames(projectPath));
        relation.addAll(RequirementUMLClassManager
                .compareClassNames(projectPath));
        relation.addAll(IntraRelationManager.getSourceIntraRelation(projectPath));
        relation.addAll(IntraRelationManager.getUMLIntraRelation(projectPath));
        

        Transaction tx = graphDbService.beginTx();
        try {
            graphDB.addNodeToGraphDB("UML", UMLAretefactElements);
            graphDB.addNodeToGraphDB("SRC", sourceCodeAretefactElements);
            graphDB.addNodeToGraphDB("REQ", requirementsAretefactElements);
            graphDB.addRelationTOGraphDB(relation);            
            graphDB.addIntraRelationTOGraphDB(relation);
            System.out.println("DBRel count" + IteratorUtil.count(GlobalGraphOperations.at(
                    graphDbService).getAllRelationships()));
            assertEquals(36, IteratorUtil.count(GlobalGraphOperations.at(
                    graphDbService).getAllRelationships()));
        } catch (Exception e) {
            tx.failure();
        } finally {
            tx.success();
        }

    }

    /**
     * Test of addRequirementsNodeToGraphDB method, of class GraphDB.
     */
    @Test
    public void testAddRequirementsNodeToGraphDB() {
        System.out.println("addRequirementsNodeToGraphDB");
        Transaction tx = graphDbService.beginTx();
        try {

            graphDB.addNodeToGraphDB("REQ", requirementsAretefactElements);
            IndexManager index = graphDbService.index();
            Index<Node> artefacts = index.forNodes("ArtefactElement");
            
            IndexHits<Node> hits = artefacts.get("ID",
                    requirement.getArtefactElementId());
            Node firstNode = hits.getSingle();

            assertEquals(firstNode.getProperty("ID").toString(),
                    requirement.getArtefactElementId());
            assertEquals(firstNode.getProperty("Name").toString(),
                    requirement.getName());
            assertEquals(firstNode.getProperty("Type").toString(),
                    requirement.getType());
            // assertEquals(firstNode.getProperty("Visibility").toString(),sourceElement.getVisibility());

            IndexHits<Node> second = artefacts.get("ID",
                    reqmethodSubElement.getSubElementId());
            Node secondNode = second.getSingle();
            assertEquals(secondNode.getProperty("ID").toString(),
                    reqmethodSubElement.getSubElementId());
            assertEquals(secondNode.getProperty("Name").toString(),
                    reqmethodSubElement.getName());
            assertEquals(secondNode.getProperty("Type").toString(),
                    reqmethodSubElement.getType());
            int count = 0;
            System.out.println("Relatinship nodes");
            for (Node n : GlobalGraphOperations.at(graphDbService).getAllNodes()) {
                System.out.println(count + " " + n);
                count++;
            }
            // assertEquals(secondNode.getProperty("Visibility").toString(),operationSubElement.getVisibility());
            assertEquals(15, IteratorUtil.count(GlobalGraphOperations.at(
                    graphDbService).getAllNodes()));

        } catch (Exception e) {
            tx.failure();
        } finally {
            tx.success();
        }
    }

    /**
     * Test of generateGraphFile method, of class GraphDB.
     */
    @Test
    public void testGenerateGraphFile() {
        System.out.println("generateGraphFile");
        relation = UMLSourceClassManager.compareClassNames(projectPath);
        relation.addAll(RequirementSourceClassManager
                .compareClassNames(projectPath));
        relation.addAll(RequirementUMLClassManager
                .compareClassNames(projectPath));
        relation.addAll(IntraRelationManager.getSourceIntraRelation(projectPath));
        relation.addAll(IntraRelationManager.getUMLIntraRelation(projectPath));
        
        Transaction tx = graphDbService.beginTx();
        try {
            PropertyFile.setProjectName("Test");
            PropertyFile.setGeneratedGexfFilePath(PropertyFile
                    .getTestGraphFile());
            graphDB.addNodeToGraphDB("UML", UMLAretefactElements);
            graphDB.addNodeToGraphDB("SRC", sourceCodeAretefactElements);
            graphDB.addNodeToGraphDB("REQ", requirementsAretefactElements);
            graphDB.addRelationTOGraphDB(relation);            
            graphDB.addIntraRelationTOGraphDB(relation);
            graphDB.generateGraphFile();
            File f = new File(PropertyFile.getTestGraphFile());
            assertTrue(f.exists());
            PropertyFile.setGeneratedGexfFilePath(null);
        } catch (Exception e) {
            tx.failure();
        } finally {
            tx.success();
        }
    }
}
