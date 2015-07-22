///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package com.project.traceability.visualization;
//
//import com.project.traceability.visualization.GraphRenderer;
//import org.gephi.preview.api.Item;
//import org.gephi.preview.api.PreviewController;
//import org.gephi.preview.api.PreviewModel;
//import org.gephi.preview.api.PreviewProperties;
//import org.gephi.preview.api.PreviewProperty;
//import org.gephi.preview.api.RenderTarget;
//import org.gephi.preview.spi.ItemBuilder;
//import org.gephi.preview.spi.PreviewMouseListener;
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import static org.junit.Assert.*;
//import org.openide.util.Lookup;
//
///**
// *
// * @author Thanu
// */
//public class GraphRendererTest {
//    PreviewMouseListener pl;
//    GraphRenderer instance;
//    PreviewModel pm;
//    Item item;
//        RenderTarget target;
//        PreviewProperties properties;
//    
//    public GraphRendererTest() {
//    }
//    
//    @BeforeClass
//    public static void setUpClass() {
//    }
//    
//    @AfterClass
//    public static void tearDownClass() {
//    }
//    
//    @Before
//    public void setUp() {
//        pl = null;
//        pm = null;
//        item = null;
//        target = null;
//        properties = null;
//        instance = new GraphRenderer();
//    }
//    
//    @After
//    public void tearDown() {
//    }
//
//    /**
//     * Test of needsPreviewMouseListener method, of class GraphRenderer.
//     */
//    @Test
//    public void testNeedsPreviewMouseListener() {
//        System.out.println("needsPreviewMouseListener");
//        boolean expResult = false;
//        boolean result = instance.needsPreviewMouseListener(pl);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
// 
//    /**
//     * Test of preProcess method, of class GraphRenderer.
//     */
//    @Test
//    public void testPreProcess() {
//        System.out.println("preProcess");
//        instance.preProcess(pm);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of render method, of class GraphRenderer.
//     */
//    @Test
//    public void testRender() {
//        System.out.println("render");
//        instance.render(item, target, properties);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of isRendererForitem method, of class GraphRenderer.
//     */
//    @Test
//    public void testIsRendererForitem() {
//        System.out.println("isRendererForitem");
//        Item item = null;
//        PreviewProperties pp = null;
//          boolean expResult = false;
//        boolean result = instance.isRendererForitem(item, pp);
//        assertEquals(expResult, result);
//       
//    }
//
//    /**
//     * Test of needsItemBuilder method, of class GraphRenderer.
//     */
//    @Test
//    public void testNeedsItemBuilder() {
//        System.out.println("needsItemBuilder");
//        ItemBuilder ib = null;
//        PreviewProperties pp = Lookup.getDefault().lookup(
//                PreviewController.class).getModel().getProperties();
//        GraphRenderer instance = new GraphRenderer();
//        boolean expResult = false;
//        boolean result = instance.needsItemBuilder(ib, pp);
//        assertEquals(expResult, result);
//    }
//}
