/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.visualization;

import com.project.traceability.visualization.GraphMouseListener;
import org.gephi.preview.api.PreviewMouseEvent;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.project.api.Workspace;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Thanu
 */
public class GraphMouseListenerTest {

    PreviewMouseEvent pme;
    PreviewProperties pp;
    Workspace wrkspc;
    GraphMouseListener instance;

    public GraphMouseListenerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        pme = null;
        pp = null;
        wrkspc = null;
        instance = new GraphMouseListener();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of mouseClicked method, of class GraphMouseListener.
     */
    @Test
    public void testMouseClicked() {
        System.out.println("mouseClicked");
        instance.mouseClicked(pme, pp, wrkspc);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of mousePressed method, of class GraphMouseListener.
     */
    @Test
    public void testMousePressed() {
        System.out.println("mousePressed");

        instance.mousePressed(pme, pp, wrkspc);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of mouseDragged method, of class GraphMouseListener.
     */
    @Test
    public void testMouseDragged() {
        System.out.println("mouseDragged");
        instance.mouseDragged(pme, pp, wrkspc);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of mouseReleased method, of class GraphMouseListener.
     */
    @Test
    public void testMouseReleased() {
        System.out.println("mouseReleased");
        instance.mouseReleased(pme, pp, wrkspc);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
