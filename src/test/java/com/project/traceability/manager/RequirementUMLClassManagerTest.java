/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.manager;

import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.ArtefactSubElement;
import java.util.List;
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
public class RequirementUMLClassManagerTest {
    
    public RequirementUMLClassManagerTest() {
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
     * Test of compareClassNames method, of class RequirementUMLClassManager.
     */
    @Test
    public void testCompareClassNames() {
        System.out.println("compareClassNames");
        String projectPath = "";
        List expResult = null;
        List result = RequirementUMLClassManager.compareClassNames(projectPath);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of compareClassCount method, of class RequirementUMLClassManager.
     */
    @Test
    public void testCompareClassCount() {
        System.out.println("compareClassCount");
        int expResult = 0;
        int result = RequirementUMLClassManager.compareClassCount();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of addSubItems method, of class RequirementUMLClassManager.
     */
    @Test
    public void testAddSubItems() {
        System.out.println("addSubItems");
        int column = 0;
        TreeItem item = null;
        List<ArtefactSubElement> list = null;
        RequirementUMLClassManager.addSubItems(column, item, list);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of compareSubElements method, of class RequirementUMLClassManager.
     */
    @Test
    public void testCompareSubElements() {
        System.out.println("compareSubElements");
        TreeItem classItem = null;
        ArtefactElement reqArtefactElement = null;
        ArtefactElement UMLArtefactElement = null;
        RequirementUMLClassManager.compareSubElements(classItem, reqArtefactElement, UMLArtefactElement);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
