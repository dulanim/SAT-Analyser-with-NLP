/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.manager;

import com.project.traceability.common.PropertyFile;
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
public class RequirementSourceClassManagerTest {
    
    public RequirementSourceClassManagerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    	PropertyFile.filePath = PropertyFile.testFilePath;
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    	PropertyFile.filePath = PropertyFile.testFilePath;
    }
    
    @Before
    public void setUp() {
    	PropertyFile.filePath = PropertyFile.testFilePath;
    }
    
    @After
    public void tearDown() {
    	PropertyFile.filePath = PropertyFile.testFilePath;
    }

    /**
     * Test of compareClassNames method, of class RequirementSourceClassManager.
     */
    @Test
    public void testCompareClassNames() {
        System.out.println("compareClassNames");
        PropertyFile.filePath = PropertyFile.testFilePath;
//        String projectPath = "";
//        List expResult = null;
        List result = RequirementSourceClassManager.compareClassNames(PropertyFile.testFilePath + "abc/");
        assertEquals(54 , RequirementSourceClassManager.relationNodes.size());
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of compareClassCount method, of class RequirementSourceClassManager.
     */
    /*@Test
    public void testCompareClassCount() {
        System.out.println("compareClassCount");
        int expResult = 0;
        int result = RequirementSourceClassManager.compareClassCount();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    *//**
     * Test of addSubItems method, of class RequirementSourceClassManager.
     *//*
    @Test
    public void testAddSubItems() {
        System.out.println("addSubItems");
        int column = 0;
        TreeItem item = null;
        List<ArtefactSubElement> list = null;
        RequirementSourceClassManager.addSubItems(column, item, list);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    *//**
     * Test of compareSubElements method, of class RequirementSourceClassManager.
     *//*
    @Test
    public void testCompareSubElements() {
        System.out.println("compareSubElements");
        TreeItem classItem = null;
        ArtefactElement reqArtefactElement = null;
        ArtefactElement sourceArtefactElement = null;
        RequirementSourceClassManager.compareSubElements(classItem, reqArtefactElement, sourceArtefactElement);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/
}
