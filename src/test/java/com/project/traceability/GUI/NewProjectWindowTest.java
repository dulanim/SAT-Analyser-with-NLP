/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.GUI;

import org.eclipse.swt.widgets.Shell;
import org.junit.Test;

/**
 *
 * @author Gitanjali
 */
public class NewProjectWindowTest extends IsolatedShellTest {
	
	public NewProjectWindowTest() {
    }
	
	
	@Override
	protected Shell createShell() {
		return new NewProjectWindow().open();
	}
	
	@Test
	public void testNewProject(){
		
	}
	
	/*@BeforeClass
	public static void openShell() {
		new NewProjectWindow().open();
	}
    */
	   

    /*@BeforeClass
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

    *//**
     * Test of main method, of class NewProjectWindow.
     *//*
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        NewProjectWindow.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    *//**
     * Test of open method, of class NewProjectWindow.
     *//*
    @Test
    public void testOpen() {
        System.out.println("open");
        NewProjectWindow instance = new NewProjectWindow();
        instance.open();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    *//**
     * Test of createContents method, of class NewProjectWindow.
     *//*
    @Test
    public void testCreateContents() {
        System.out.println("createContents");
        NewProjectWindow instance = new NewProjectWindow();
        instance.createContents();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    *//**
     * Test of addPopUpMenu method, of class NewProjectWindow.
     *//*
    @Test
    public void testAddPopUpMenu() {
        System.out.println("addPopUpMenu");
        NewProjectWindow.addPopUpMenu();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    *//**
     * Test of center method, of class NewProjectWindow.
     *//*
    @Test
    public void testCenter() {
        System.out.println("center");
        Shell shell = null;
        NewProjectWindow instance = new NewProjectWindow();
        instance.center(shell);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    *//**
     * Test of deleteFiles method, of class NewProjectWindow.
     *//*
    @Test
    public void testDeleteFiles() {
        System.out.println("deleteFiles");
        String projectPath = "";
        NewProjectWindow.deleteFiles(projectPath);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
*/
	
}
