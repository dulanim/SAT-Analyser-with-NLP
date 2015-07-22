/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.GUI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCheckBox;
import org.junit.Test;

import com.project.traceability.common.PropertyFile;

/**
 *
 * @author Gitanjali
 */
public class FileSelectionWindowTest extends IsolatedShellTest{
    
    public FileSelectionWindowTest() {
    }
    
    @Override
	protected Shell createShell() {
    	PropertyFile.xmlFilePath = PropertyFile.testXmlFilePath;
		return new FileSelectionWindow().open("abc");
	}
    
    @Test
	public void compareWindowTest() {
    	SWTBotCheckBox ckBox = bot.checkBox();
    	assertEquals("RequirementArtefactFile.xml", ckBox.getText());
    	assertTrue(bot.button("Compare").isVisible());
    	bot.checkBox(0).click();
    	bot.checkBox(1).click();
    	bot.button("Compare").click();
    	System.out.println("PPPPPPPPPPP");
    	
    	/*SWTBotContextMenu menu = new SWTBotContextMenu(bot.tree());
		NewProjectWindow.projectPath = PropertyFile.filePath + "abc/";
		HomeGUI.tree = bot.tree().widget;
		menu.click("Compare Files");
		assertEquals("File Selection", bot.activeShell().getText());*/
	}
    
    

   /* @BeforeClass
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
     * Test of main method, of class FileSelectionWindow.
     *//*
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        FileSelectionWindow.main(args);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    *//**
     * Test of open method, of class FileSelectionWindow.
     *//*
    @Test
    public void testOpen() {
        System.out.println("open");
        String project = "";
        FileSelectionWindow instance = new FileSelectionWindow();
        instance.open(project);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    *//**
     * Test of createContents method, of class FileSelectionWindow.
     *//*
    @Test
    public void testCreateContents() {
        System.out.println("createContents");
        String project = "";
        FileSelectionWindow instance = new FileSelectionWindow();
        instance.createContents(project);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    *//**
     * Test of center method, of class FileSelectionWindow.
     *//*
    @Test
    public void testCenter() {
        System.out.println("center");
        Shell shell = null;
        FileSelectionWindow instance = new FileSelectionWindow();
        instance.center(shell);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
*/
	
}
