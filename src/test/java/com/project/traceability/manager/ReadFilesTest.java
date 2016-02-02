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

import com.project.traceability.common.PropertyFile;

/**
 *
 * @author Gitanjali
 */
public class ReadFilesTest {
    
    public ReadFilesTest() {
    }
    
    @Test
    public void readFilesTest(){
    	SourceCodeArtefactManagerTest test = new SourceCodeArtefactManagerTest();
    	test.readXMLTest();
    	System.out.println(PropertyFile.filePath);
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
     * Test of readFiles method, of class ReadFiles.
     */
   /* @Test
    public void testReadFiles() {
        System.out.println("readFiles");
        String path = "";
        ReadFiles.readFiles(path);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    *//**
     * Test of deleteArtefact method, of class ReadFiles.
     *//*
    @Test
    public void testDeleteArtefact() {
        System.out.println("deleteArtefact");
        String id = "";
        ReadFiles.deleteArtefact(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/
}
