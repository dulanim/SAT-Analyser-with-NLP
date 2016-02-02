/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.manager;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.project.traceability.common.PropertyFile;

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


        List result = RequirementSourceClassManager.compareClassNames(PropertyFile.testFilePath + "test/");
        assertEquals(123 , RequirementSourceClassManager.relationNodes.size());


    }
}
