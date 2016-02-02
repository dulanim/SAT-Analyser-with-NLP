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
 */
public class SourceCodeArtefactManagerTest {
    
    public SourceCodeArtefactManagerTest() {
    }
    
    @Test
    public void readXMLTest(){
    	SourceCodeArtefactManager.readXML(PropertyFile.testFilePath + "test/");
    	assertEquals(5, SourceCodeArtefactManager.sourceCodeAretefactElements.size());
    	assertEquals("Account", SourceCodeArtefactManager.sourceCodeAretefactElements.get("SC1").getName());
    	List<ArtefactSubElement> subElements = SourceCodeArtefactManager.sourceCodeAretefactElements.get("SC1").getArtefactSubElements();
    	assertEquals(13, subElements.size());
    	assertEquals("Double", (((AttributeModel)(subElements.get(1))).getVariableType()));
    	assertEquals("String", (((MethodModel)(subElements.get(6))).getParameters().get(0).getVariableType()));
    	assertEquals("String", (((MethodModel)(subElements.get(9))).getReturnType()));
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
