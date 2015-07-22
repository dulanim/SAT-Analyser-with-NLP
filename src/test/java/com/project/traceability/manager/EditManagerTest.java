/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.manager;

import org.eclipse.swt.widgets.TreeItem;
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
public class EditManagerTest {
    
    public EditManagerTest() {
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
     * Test of main method, of class EditManager.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        EditManager.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addLink method, of class EditManager.
     */
    @Test
    public void testAddLink() {
        System.out.println("addLink");
        Object className = null;
        Object subElementName = null;
        EditManager.addLink(className, subElementName);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteLink method, of class EditManager.
     */
    @Test
    public void testDeleteLink() {
        System.out.println("deleteLink");
        TreeItem treeItem = null;
        EditManager.deleteLink(treeItem);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
