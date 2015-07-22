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
public class ClassCompareManagerTest {
    
    public ClassCompareManagerTest() {
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
     * Test of compareClassNames method, of class ClassCompareManager.
     */
    @Test
    public void testCompareClassNames() {
        System.out.println("compareClassNames");
        String projectPath = "";
        List expResult = null;
        List result = ClassCompareManager.compareClassNames(projectPath);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of compareClassCount method, of class ClassCompareManager.
     */
    @Test
    public void testCompareClassCount() {
        System.out.println("compareClassCount");
        int expResult = 0;
        int result = ClassCompareManager.compareClassCount();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
