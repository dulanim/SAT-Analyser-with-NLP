/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.manager;

import com.project.traceability.utils.Constants.ArtefactSubElementType;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 *
 * @author Gitanjali
 */
public class UMLArtefactManagerTest {
    
    public UMLArtefactManagerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of readXML method, of class UMLArtefactManager.
     */
    @Test
    public void testReadXML() {
        System.out.println("readXML");
        String projectPath = "";
        UMLArtefactManager.readXML(projectPath);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readIntraConnectionsXML method, of class UMLArtefactManager.
     */
    @Test
    public void testReadIntraConnectionsXML() {
        System.out.println("readIntraConnectionsXML");
        Document UMLDoc = null;
        UMLArtefactManager.readIntraConnectionsXML(UMLDoc);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readArtefactSubElement method, of class UMLArtefactManager.
     */
    @Test
    public void testReadArtefactSubElement() {
        System.out.println("readArtefactSubElement");
        NodeList artefactSubElementList = null;
        List expResult = null;
        List result = UMLArtefactManager.readArtefactSubElement(artefactSubElementList);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of manageArtefactSubElements method, of class UMLArtefactManager.
     */
    @Test
    public void testManageArtefactSubElements() {
        System.out.println("manageArtefactSubElements");
        ArtefactSubElementType attribute = null;
        Map expResult = null;
        Map result = UMLArtefactManager.manageArtefactSubElements(attribute);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
