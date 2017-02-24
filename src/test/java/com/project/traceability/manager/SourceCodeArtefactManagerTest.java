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
import com.project.traceability.model.ArtefactSubElement;
import com.project.traceability.model.AttributeModel;
import com.project.traceability.model.MethodModel;

/**
 *
 * @author Gitanjali
 * @author Aarthika <>
 */
public class SourceCodeArtefactManagerTest {
    
    public SourceCodeArtefactManagerTest() {
    }
    
    @Test
    public void readXMLTest(){
    	SourceCodeArtefactManager.readXML(PropertyFile.testFilePath + "test/");
    	assertEquals(4, SourceCodeArtefactManager.sourceCodeAretefactElements.size());
    	assertEquals("Customer", SourceCodeArtefactManager.sourceCodeAretefactElements.get("SC1").getName());
    	List<ArtefactSubElement> subElements = SourceCodeArtefactManager.sourceCodeAretefactElements.get("SC1").getArtefactSubElements();
    	assertEquals(2, subElements.size());
    	//assertEquals("String", (((AttributeModel)(subElements.get(0))).getVariableType()));
    	assertEquals("void", (((MethodModel)(subElements.get(1))).getReturnType()));
    	assertEquals("void", (((MethodModel)(subElements.get(0))).getReturnType()));
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

   
}
