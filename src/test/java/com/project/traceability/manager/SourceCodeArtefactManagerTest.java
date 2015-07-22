/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.manager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.project.traceability.common.PropertyFile;
import com.project.traceability.model.ArtefactSubElement;
import com.project.traceability.model.AttributeModel;
import com.project.traceability.model.MethodModel;
import com.project.traceability.utils.Constants.ArtefactSubElementType;
import com.project.traceability.utils.Constants.ArtefactType;

/**
 *
 * @author Gitanjali
 */
public class SourceCodeArtefactManagerTest {
    
    public SourceCodeArtefactManagerTest() {
    }
    
    @Test
    public void readXMLTest(){
    	SourceCodeArtefactManager.readXML(PropertyFile.testFilePath + "abc/");
    	assertEquals(2, SourceCodeArtefactManager.sourceCodeAretefactElements.size());
    	assertEquals("Account", SourceCodeArtefactManager.sourceCodeAretefactElements.get("SC1").getName());
    	List<ArtefactSubElement> subElements = SourceCodeArtefactManager.sourceCodeAretefactElements.get("SC1").getArtefactSubElements();
    	assertEquals(10, subElements.size());
    	assertEquals("Double", (((AttributeModel)(subElements.get(1))).getVariableType()));
    	assertEquals("Double", (((MethodModel)(subElements.get(6))).getParameters().get(0).getVariableType()));
    	assertEquals("void", (((MethodModel)(subElements.get(9))).getReturnType()));
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
     * Test of readXML method, of class SourceCodeArtefactManager.
     */
    @Test
    public void testReadXML() {
        System.out.println("readXML");
        String projectPath = "";
        SourceCodeArtefactManager.readXML(projectPath);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readIntraConnectionsXML method, of class SourceCodeArtefactManager.
     */
    @Test
    public void testReadIntraConnectionsXML_Document() {
        System.out.println("readIntraConnectionsXML");
        Document sourceDoc = null;
        SourceCodeArtefactManager.readIntraConnectionsXML(sourceDoc);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readIntraConnectionsXML method, of class SourceCodeArtefactManager.
     */
    @Test
    public void testReadIntraConnectionsXML_NodeList() {
        System.out.println("readIntraConnectionsXML");
        NodeList intraConnectionsList = null;
        SourceCodeArtefactManager.readIntraConnectionsXML(intraConnectionsList);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of readArtefactSubElement method, of class SourceCodeArtefactManager.
     */
    @Test
    public void testReadArtefactSubElement() {
        System.out.println("readArtefactSubElement");
        NodeList artefactSubElementList = null;
        List expResult = null;
        List result = SourceCodeArtefactManager.readArtefactSubElement(artefactSubElementList);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of manageArtefactSubElements method, of class SourceCodeArtefactManager.
     */
    @Test
    public void testManageArtefactSubElements() {
        System.out.println("manageArtefactSubElements");
        ArtefactSubElementType attribute = null;
        Map expResult = null;
        Map result = SourceCodeArtefactManager.manageArtefactSubElements(attribute);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getArtefactType method, of class SourceCodeArtefactManager.
     */
    @Test
    public void testGetArtefactType() {
        System.out.println("getArtefactType");
        SourceCodeArtefactManager instance = new SourceCodeArtefactManager();
        ArtefactType expResult = null;
        ArtefactType result = instance.getArtefactType();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setArtefactType method, of class SourceCodeArtefactManager.
     */
    @Test
    public void testSetArtefactType() {
        System.out.println("setArtefactType");
        ArtefactType artefactType = null;
        SourceCodeArtefactManager instance = new SourceCodeArtefactManager();
        instance.setArtefactType(artefactType);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
