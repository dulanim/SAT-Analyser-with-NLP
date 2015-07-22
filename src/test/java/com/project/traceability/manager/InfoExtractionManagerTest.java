/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.manager;

import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.RequirementModel;
import java.util.ArrayList;
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
public class InfoExtractionManagerTest {
    
    public InfoExtractionManagerTest() {
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
     * Test of run method, of class InfoExtractionManager.
     */
    @Test
    public void testRun() {
        System.out.println("run");
        List<RequirementModel> requirementAretefactElements = null;
        List expResult = null;
        List result = InfoExtractionManager.run(requirementAretefactElements);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of extactClass method, of class InfoExtractionManager.
     */
    @Test
    public void testExtactClass() {
        System.out.println("extactClass");
        String title = "";
        String content = "";
        String id = "";
        InfoExtractionManager.extactClass(title, content, id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAttributes method, of class InfoExtractionManager.
     */
    @Test
    public void testGetAttributes() {
        System.out.println("getAttributes");
        String str = "";
        String id = "";
        ArrayList expResult = null;
        ArrayList result = InfoExtractionManager.getAttributes(str, id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getClassName method, of class InfoExtractionManager.
     */
    @Test
    public void testGetClassName() {
        System.out.println("getClassName");
        String str = "";
        String id = "";
        ArtefactElement expResult = null;
        ArtefactElement result = InfoExtractionManager.getClassName(str, id);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBehaviors method, of class InfoExtractionManager.
     */
    @Test
    public void testGetBehaviors() {
        System.out.println("getBehaviors");
        String str = "";
        String id = "";
        InfoExtractionManager.getBehaviors(str, id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
