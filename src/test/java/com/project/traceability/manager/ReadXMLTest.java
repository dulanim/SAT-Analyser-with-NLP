/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.manager;

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
public class ReadXMLTest {
    
    public ReadXMLTest() {
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
     * Test of initApp method, of class ReadXML.
     */
    @Test
    public void testInitApp() {
        System.out.println("initApp");
        String projectPath = "";
        String graphType = "";
        ReadXML.initApp(projectPath, graphType);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
