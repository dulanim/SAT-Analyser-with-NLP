/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.manager;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Gitanjali
 */
public class RelationManagerTest {
    
    public RelationManagerTest() {
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
     * Test of createXML method, of class RelationManager.
     */
    @Test
    public void testCreateXML_List() {
        System.out.println("createXML");
        List<String> relationNodes = null;
        RelationManager.createXML(relationNodes);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createXML method, of class RelationManager.
     */
    @Test
    public void testCreateXML_String() {
        System.out.println("createXML");
        String projectPath = "";
        RelationManager.createXML(projectPath);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addLinks method, of class RelationManager.
     */
    @Test
    public void testAddLinks() {
        System.out.println("addLinks");
        List<String> relationNodes = null;
        RelationManager.addLinks(relationNodes);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readAll method, of class RelationManager.
     */
    @Test
    public void testReadAll() {
        System.out.println("readAll");
        List expResult = null;
        List result = RelationManager.readAll();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removeDuplicate method, of class RelationManager.
     */
    @Test
    public void testRemoveDuplicate() {
        System.out.println("removeDuplicate");
        List<String> relationNodes = null;
        List<String> existingRelationNode = null;
        List expResult = null;
        List result = RelationManager.removeDuplicate(relationNodes, existingRelationNode);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
