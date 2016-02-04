/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.GUI;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.allOf;
import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.widgetOfType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.finders.ContextMenuFinder;
import org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory;
import org.eclipse.swtbot.swt.finder.waits.DefaultCondition;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCTabItem;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.hamcrest.Matcher;
import org.junit.Test;

import com.project.traceability.common.PropertyFile;
import com.project.traceability.manager.ReadFilesTest;
import com.project.traceability.manager.RequirementSourceClassManagerTest;

/**
 * 
 * @author Gitanjali
 */

public class HomeGUITest extends IsolatedShellTest {

	//private SWTWorkbenchBot bot = new SWTWorkbenchBot();
	static int projects;

	public HomeGUITest() {
	}

	protected Shell createShell() {
		PropertyFile.filePath = PropertyFile.testFilePath;
		PropertyFile.xmlFilePath = PropertyFile.testXmlFilePath;
		return new HomeGUI().open();
	}
	
	/*@Test
	public void firstTest() {
		SWTBotCTabItem ctab = bot.cTabItem("Projects");
		SWTBotTree tree = bot.tree();
		System.out.println(ctab.getText());
		SWTBotTreeItem ti = bot.tree().getTreeItem("abc");
		System.out.println(ti.getText());
		ti.setFocus();
		// ti.select().contextMenu("New");//contextMenu("New").contextMenu("File").click();

	}*/

	@Test
	public void tabItemTest() {
		assertEquals("Projects", bot.cTabItem().getText());
		SWTBotTreeItem ti = bot.tree().getTreeItem("test");
		System.out.println(ti.getText());
	}
	
	
	@Test
	public void projectWindowTest() {
		bot.menu("File").menu("New").menu("Project").click();
		assertEquals("New Project", bot.activeShell().getText());
	}

	@SuppressWarnings("unchecked")
	protected SWTBotMenu contextMenu(final Control control, final String text) {
		Matcher<MenuItem> withMnemonic = WidgetMatcherFactory
				.withMnemonic(text);
		final Matcher<MenuItem> matcher = allOf(widgetOfType(MenuItem.class),
				withMnemonic);
		final ContextMenuFinder menuFinder = new ContextMenuFinder(control);
		new SWTBot().waitUntil(new DefaultCondition() {
			public String getFailureMessage() {
				return "Could not find context menu with text: " + text;
			}

			public boolean test() throws Exception {
				return !menuFinder.findMenus(matcher).isEmpty();
			}
		});
		return new SWTBotMenu(menuFinder.findMenus(matcher).get(0), matcher);
	}

	@Test
	public void treeTest() {
		assertTrue(bot.tree().getTreeItem("test").isEnabled());
		projects = bot.tree().getAllItems().length;
		SWTBotTreeItem ti = bot.tree().getTreeItem("test");
		ti.setFocus();
		ti.select();
		final org.hamcrest.Matcher<MenuItem> matcher = allOf(widgetOfType(MenuItem.class));
		final ContextMenuFinder menuFinder = new ContextMenuFinder((Control)bot.tree().widget);
		new SWTBot().waitUntil(new DefaultCondition() {
			public String getFailureMessage() {
				return "Could not find context menu items"; //$NON-NLS-1$
			}

			public boolean test() throws Exception {
				return !menuFinder.findMenus(matcher).isEmpty();
			}
		});
		List<MenuItem> list = menuFinder.findMenus(matcher);
		ti.select().contextMenu("New").click();
		ti.setFocus();
		ti.select().contextMenu("File").click();
		System.out.println(ti.getText());
		//SWTBotContextMenu menu = new SWTBotContextMenu(bot.tree());
		HomeGUI.projectPath = PropertyFile.filePath + "test/";
		//menu.click("New").click("File");
		assertEquals("New File", bot.activeShell().getText());
		/*node = view.bot().tree().getTreeItem("GnuProject3");
		node.setFocus();
		node.select();*/
		
		
		
	}

	@Test
	public void compareWindowTest() {
		SWTBotContextMenu menu = new SWTBotContextMenu(bot.tree());
		bot.tree().select("test");
		HomeGUI.projectPath = PropertyFile.filePath + "test/";
		FileSelectionWindow window = new FileSelectionWindow();
		menu.click("Compare Files");
		assertEquals("File Selection", bot.activeShell().getText());
	}
	
	@Test
	public void testVisualization(){
		//GraphDBTest graphTest = new GraphDBTest();
		//graphTest.testGenerateGraphFile();
		SWTBotContextMenu menu = new SWTBotContextMenu(bot.tree());
		bot.tree().select("test");
		HomeGUI.projectPath = PropertyFile.filePath + "test/";
		FileSelectionWindow window = new FileSelectionWindow();
		menu.click("Visualization").click("Full Graph");
		//assertEquals("Software Artefact Traceability Analyzer", bot.activeShell().getText());
	}
	 
	
	@Test
	public void testFileSelection() throws ExecutionException {
		// part of test that requires UI-thread
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				try {
					//FileSelectionWindowTest test = new FileSelectionWindowTest(); // requires UI-thread since it is gonna invoke PlatformUI.getWorkbench()
					//test.createShell();
					//test.compareWindowTest();
					ReadFilesTest readFilesTest = new ReadFilesTest();
					readFilesTest.readFilesTest();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				try {
					RequirementSourceClassManagerTest test = new RequirementSourceClassManagerTest();
					test.testCompareClassNames();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		bot.shell("Software Artefact Traceability Analyzer").activate();
		assertEquals("Software Artefact Traceability Analyzer", bot.activeShell().getText());
		SWTBotTree tree = bot.tree(1);
		assertTrue(bot.tree(0).isEnabled());
		assertEquals("SourceCodeXML File" ,tree.columns().get(0));
		assertEquals("RequirementsXML File" ,tree.columns().get(1));
		System.out.println(bot.tree(0).getAllItems().length + "PPPPPPPPPpp");
		SWTBotCTabItem ctab = bot.cTabItem();
		
	}

	/*
	 * Test method run for deleting the created project 
	 */
	@Test
	public void deleteProjectTest(){
		HomeGUI.main(null);
		Tree treeItem = HomeGUI.tree;
		SWTBotTree tree = new SWTBotTree(treeItem);
		SWTBotTreeItem items[] = tree.getAllItems();
		if(items.length>0){
			Random ra = new Random();
			int random = ra.nextInt(items.length-1);
			SWTBotTreeItem item = items[random];
			item.contextMenu("Delete").click();
		}
	}
	
	/*
	 * @BeforeClass public static void setUpClass() throws Exception { }
	 * 
	 * @AfterClass public static void tearDownClass() throws Exception { }
	 * 
	 * @Before public void setUp() { }
	 * 
	 * @After public void tearDown() { }
	 */

	/**
	 * Test of main method, of class HomeGUI.
	 */
	/*
	 * @Test public void testMain() { System.out.println("main"); String[] args
	 * = null; HomeGUI.main(args); // TODO review the generated test code and
	 * remove the default call to fail. fail("The test case is a prototype."); }
	 */

	/**
	 * Test of open method, of class HomeGUI.
	 */
	/*
	 * @Test public void testOpen() { System.out.println("open"); HomeGUI
	 * instance = new HomeGUI(); instance.open(); // TODO review the generated
	 * test code and remove the default call to fail.
	 * fail("The test case is a prototype."); }
	 */

	/**
	 * Test of initUI method, of class HomeGUI.
	 */
	/*
	 * @Test public void testInitUI() { System.out.println("initUI"); HomeGUI
	 * instance = new HomeGUI(); instance.initUI(); // TODO review the generated
	 * test code and remove the default call to fail.
	 * fail("The test case is a prototype."); }
	 */

	/**
	 * Test of center method, of class HomeGUI.
	 */
	/*
	 * @Test public void testCenter() { System.out.println("center"); Shell
	 * shell = null; HomeGUI instance = new HomeGUI(); instance.center(shell);
	 * // TODO review the generated test code and remove the default call to
	 * fail. fail("The test case is a prototype."); }
	 */

	/**
	 * Test of createContents method, of class HomeGUI.
	 */
	/*
	 * @Test public void testCreateContents() {
	 * System.out.println("createContents"); HomeGUI instance = new HomeGUI();
	 * instance.createContents(); // TODO review the generated test code and
	 * remove the default call to fail. fail("The test case is a prototype."); }
	 */
}
