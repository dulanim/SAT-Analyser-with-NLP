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
public class ClassManagerTest {
    
    public ClassManagerTest() {
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
     * Test of getReqClassName method, of class ClassManager.
     */
    @Test
    public void testGetReqClassName() {
        System.out.println("getReqClassName");
        String projectPath = "";
        List expResult = null;
        List result = ClassManager.getReqClassName(projectPath);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUmlClassName method, of class ClassManager.
     */
    @Test
    public void testGetUmlClassName() {
        System.out.println("getUmlClassName");
        String projectPath = "";
        List expResult = null;
        List result = ClassManager.getUmlClassName(projectPath);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSourceClassName method, of class ClassManager.
     */
    @Test
    public void testGetSourceClassName() {
        System.out.println("getSourceClassName");
        String projectPath = "";
        List expResult = null;
        List result = ClassManager.getSourceClassName(projectPath);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
