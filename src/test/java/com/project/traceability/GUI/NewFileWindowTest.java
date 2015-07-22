/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.GUI;

import static org.junit.Assert.assertEquals;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.junit.Test;

import com.project.traceability.common.PropertyFile;

/**
 * 
 * @author Gitanjali
 */
public class NewFileWindowTest extends IsolatedShellTest {

	
	private SWTWorkbenchBot bot = new SWTWorkbenchBot();
	public NewFileWindowTest() {
	}

	@Override
	protected Shell createShell() {
		System.out.println("PPPPPPP");
		return new NewFileWindow().open();
	}
	
	@Test
	public void tabItemTest() {
		NewProjectWindow.projectPath = PropertyFile.filePath + "abc/";
		//SWTBotButton button = new SWTBotButton(bot.button("Browse"));
		assertEquals("Cancel", bot.button(0).getText());
		assertEquals("Browse", bot.button(1).getText());
		assertEquals("Open", bot.button(2).getText());
	}
	// @BeforeClass
	// public static void setUpClass() throws Exception {
	// }
	//
	// @AfterClass
	// public static void tearDownClass() throws Exception {
	// }
	//
	// @Before
	// public void setUp() {
	// }
	//
	// @After
	// public void tearDown() {
	// }
	//
	// /**
	// * Test of main method, of class NewFileWindow.
	// */
	// @Test
	// public void testMain() {
	// System.out.println("main");
	// String[] args = null;
	// NewFileWindow.main(args);
	// // TODO review the generated test code and remove the default call to
	// fail.
	// fail("The test case is a prototype.");
	// }
	//
	// /**
	// * Test of open method, of class NewFileWindow.
	// */
	// @Test
	// public void testOpen() {
	// System.out.println("open");
	// NewFileWindow instance = new NewFileWindow();
	// instance.open();
	// // TODO review the generated test code and remove the default call to
	// fail.
	// fail("The test case is a prototype.");
	// }
	//
	// /**
	// * Test of createContents method, of class NewFileWindow.
	// */
	// @Test
	// public void testCreateContents() {
	// System.out.println("createContents");
	// NewFileWindow instance = new NewFileWindow();
	// instance.createContents();
	// // TODO review the generated test code and remove the default call to
	// fail.
	// fail("The test case is a prototype.");
	// }
	//
	// /**
	// * Test of openFiles method, of class NewFileWindow.
	// */
	// @Test
	// public void testOpenFiles() {
	// System.out.println("openFiles");
	// NewFileWindow.openFiles();
	// // TODO review the generated test code and remove the default call to
	// fail.
	// fail("The test case is a prototype.");
	// }
	//
	// /**
	// * Test of createTabLayout method, of class NewFileWindow.
	// */
	// @Test
	// public void testCreateTabLayout() {
	// System.out.println("createTabLayout");
	// String fileName = "";
	// NewFileWindow.createTabLayout(fileName);
	// // TODO review the generated test code and remove the default call to
	// fail.
	// fail("The test case is a prototype.");
	// }
	//
	// /**
	// * Test of center method, of class NewFileWindow.
	// */
	// @Test
	// public void testCenter() {
	// System.out.println("center");
	// Shell shell = null;
	// NewFileWindow instance = new NewFileWindow();
	// instance.center(shell);
	// // TODO review the generated test code and remove the default call to
	// fail.
	// fail("The test case is a prototype.");
	// }
	//
	// /**
	// * Test of displayError method, of class NewFileWindow.
	// */
	// @Test
	// public void testDisplayError() {
	// System.out.println("displayError");
	// String msg = "";
	// NewFileWindow.displayError(msg);
	// // TODO review the generated test code and remove the default call to
	// fail.
	// fail("The test case is a prototype.");
	// }
}
