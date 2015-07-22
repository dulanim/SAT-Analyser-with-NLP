/**
 * 
 */
package com.project.traceability.GUI;

/**
 * @author Gitanjali
 * Dec 4, 2014
 */

import java.awt.Dimension;
import java.util.ArrayList;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.PaintObjectEvent;
import org.eclipse.swt.custom.PaintObjectListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.project.traceability.common.PropertyFile;
import com.project.traceability.manager.EditManager;
import com.project.traceability.manager.ReadFiles;
import com.project.traceability.manager.RelationManager;
import com.project.traceability.manager.RequirementSourceClassManager;
import com.project.traceability.manager.RequirementUMLClassManager;
import com.project.traceability.manager.UMLSourceClassManager;
import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.ArtefactSubElement;
import com.project.traceability.utils.Constants.ImageType;

public class CompareWindow {

	public static Shell shell;
	public static Dimension screen = java.awt.Toolkit.getDefaultToolkit()
			.getScreenSize();
	public static TableViewer viewer;
	public static Display display;
	public static Tree tree;
	public static int column;
	public static int alterColumn;

	public static TreeItem[] classList;
	public static TreeItem[] subElements;
	public static TreeItem parent;

	TraverseListener traverseListener = new TraverseListener() {
		public void keyTraversed(TraverseEvent e) {
			if (e.detail == SWT.TRAVERSE_RETURN) {
				e.doit = false;
				resetEditors();
			}
		}
	};

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			CompareWindow window = new CompareWindow();
			window.open("", new ArrayList<String>());
			window.eventLoop(display);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public Shell open(final String project,
			final ArrayList<String> selectedFiles) {

		display = Display.getDefault();
		createContents(selectedFiles);
		ReadFiles.readFiles(PropertyFile.filePath + project + "\\");
		compareFiles(project, selectedFiles);
		return shell;
	}

	public void eventLoop(Display display) {
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents(ArrayList<String> files) {
		final Image image = display.getSystemImage(SWT.ICON_INFORMATION);
		shell = new Shell();
		shell.setSize(900, 900);
		shell.setText("SWT Application");

		shell.setBounds(0, 0, screen.width, screen.height);

		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		MenuItem mntmFile = new MenuItem(menu, SWT.NONE);
		mntmFile.setText("File");

		CTabItem tabItem = new CTabItem(HomeGUI.tabFolder, SWT.NONE);			//to show compared results
		tabItem.setText("Compared Results");

		Composite composite = new Composite(HomeGUI.tabFolder, SWT.NONE);
		GridLayout grid = new GridLayout();
		grid.numColumns = 2;
		composite.setLayout(grid);
		tabItem.setControl(composite);

		composite.setBounds(40, 31, 900, 1000);
		
		tree = new Tree(composite, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL		//show results in table format
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
		GridData gd_tree = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1,
				1);
		
		tree.setLayoutData(gd_tree);
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		GridData gridData = new GridData(GridData.FILL_VERTICAL);
		gridData.heightHint = 150;
		tree.setLayoutData(gridData);
		tree.addTraverseListener(traverseListener);

		tree.addListener(SWT.MouseDown, new Listener() {			//selection listener 
			public void handleEvent(Event e) {
				Point pt = new Point(e.x, e.y);
				TreeItem treeItem = tree.getItem(pt);
				column = 0;

				if (treeItem == null) {
					return;
				}
				for (int i = 0; i < tree.getColumnCount(); i++) {			//to find the column number
					Rectangle rect = treeItem.getBounds(i);
					if (rect.contains(pt)) {
						int index = tree.indexOf(treeItem);
						column = i;
						if (column == 0)
							alterColumn = 1;
						else if (column == 1)
							alterColumn = 0;
						break;
					}
				}
				Menu men = new Menu(tree);
				tree.setMenu(men);
				final MenuItem newLinkItem = new MenuItem(men, SWT.PUSH);		//to update link manually
				newLinkItem.setText("Add a link");
				boolean canAdd = true;											//added boolean value to check whether a 
																				//link can be deleted or not 
				if(tree.getSelection()[0].getText().equalsIgnoreCase("Attributes")  || 
						tree.getSelection()[0].getText().equalsIgnoreCase("Methods") )
					canAdd = false;
				else if(tree.getSelection()[0].getText(0) == "" && tree.getSelection()[0].getText(1) == "" )
					canAdd = false;
				else if(tree.getSelection()[0] == null)
					canAdd = false;
				else if(tree.getSelection()[0].getText(0) != "" && tree.getSelection()[0].getText(1) != "" )
					canAdd = false;
				if(!canAdd)
					newLinkItem.setEnabled(false);

				final TreeEditor editor = new TreeEditor(tree);			//for dynamically updating tree
				newLinkItem.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event event) {
						final TreeItem[] selection = tree.getSelection();
						TreeItem[] items = selection[0].getItems();
						boolean showBorder = true;
						final Composite composite = new Composite(tree,
								SWT.NONE);
						final CCombo text = new CCombo(composite, SWT.NONE);

						classList = tree.getItems();

						if (items.length == 0) {
							parent = selection[0].getParentItem();
							subElements = selection[0].getParentItem()
									.getItems();
							classList = null;
						}

						if (classList != null && classList.length > 0) {		//to store non matched artefact elements
							for (int i = 0; i < classList.length; i++) {
								TreeItem item = classList[i];
								System.out.println(classList[i].getText());
								if (column == 0)
									alterColumn = 1;
								if (item.getData(Integer.toString(alterColumn)) != null
										&& item.getText(alterColumn) != "" && item.getText(column) == "") {
									text.add(((ArtefactElement) item.getData(Integer.toString(alterColumn))).getName());
									text.setData(((ArtefactElement) item.getData(Integer.toString(alterColumn))).getName(), 
											item.getData(Integer.toString(alterColumn)));
								}
							}
						} else if (subElements != null && subElements.length > 0) {
							for (int i = 0; i < subElements.length; i++) {
								TreeItem item = subElements[i];
								System.out.println(subElements[i].getText());
								if (column == 0)
									alterColumn = 1;
								if (item.getData(Integer.toString(alterColumn)) != null
										&& item.getText(alterColumn) != ""&& item.getText(column) == "") {
									text.add(((ArtefactSubElement) item.getData(Integer.toString(alterColumn))).getName());
									text.setData(((ArtefactSubElement) item.getData(Integer.toString(alterColumn))).getName(), 
											item.getData(Integer.toString(alterColumn)));
								}
							}
						}

						editor.grabHorizontal = true;
						editor.setEditor(text, selection[0], column);
						final int inset = showBorder ? 1 : 0;
						composite.addListener(SWT.Resize, new Listener() {
							@Override
							public void handleEvent(Event e) {
								Rectangle rect = composite.getClientArea();
								text.setBounds(rect.x + inset, rect.y + inset,
										rect.width - inset * 2, rect.height
												- inset * 2);
							}
						});
						Listener textListener = new Listener() {		
							@Override
							public void handleEvent(final Event e) {
								switch (e.type) {
								case SWT.FocusOut:
									System.out.println(text.getData(text
											.getText()));
									if (classList != null) {		// to map new class
										if (confirmMapping(((ArtefactElement) selection[0].getData("" + column+ "")),
												(ArtefactElement) text.getData(text.getText()))) {
													String className = updateSubElements(tree.getColumn(0).getText(),
													tree.getColumn(1).getText());
													if (className.equals("RS")){		//to compare sub elements
															RequirementSourceClassManager.relationNodes = null;
															RequirementSourceClassManager.relationNodes = new ArrayList<String>();
															RequirementSourceClassManager.compareSubElements(selection[0],
																	(ArtefactElement) text.getData(text.getText()),
																	(ArtefactElement) selection[0].getData(""+ column+ ""));
															RelationManager.addLinks(RequirementSourceClassManager.relationNodes);
													}
											else if (className.equals("RU")){
												RequirementUMLClassManager.compareSubElements(selection[0],
																(ArtefactElement) selection[0].getData(""+ column+ ""),
																(ArtefactElement) text.getData(text.getText()));
												RelationManager.addLinks(RequirementUMLClassManager.relationNodes);
											}
											else if (className.equals("US")){
												UMLSourceClassManager.compareSubElements(selection[0],
														(ArtefactElement) selection[0].getData("" + column + ""),
																(ArtefactElement) text.getData(text.getText()));
												RelationManager.addLinks(UMLSourceClassManager.relationNodes);
											}
											for (int i = 0; i < classList.length; i++) {
												if (classList[i].getText(alterColumn).equalsIgnoreCase(text.getText())) {
													selection[0].dispose();
													classList[i].dispose();
													break;
												}
											}
										}
									} else if (subElements != null) {			// map new sub element
										if (confirmMapping(
												((ArtefactSubElement) selection[0].getData("" + column+ "")),
												(ArtefactSubElement) text.getData(text.getText()))) {
											for (int i = 0; i < subElements.length; i++) {
												if (subElements[i].getText(alterColumn).equalsIgnoreCase(text.getText())) {
													TreeItem newItem = new TreeItem(parent, SWT.NONE);
													newItem.setText(column,selection[0].getText(column));
													newItem.setText(alterColumn,subElements[i].getText(alterColumn));
													newItem.setImage(0, ImageType.EXACT_MATCH.getValue());
													newItem.setImage(1, ImageType.EXACT_MATCH.getValue());
													selection[0].dispose();
													subElements[i].dispose();
													break;
												}
											}
										}
									}
									composite.dispose();
									break;
								case SWT.Verify:
									String newText = text.getText();
									String leftText = newText.substring(0,
											e.start);
									String rightText = newText.substring(e.end,
											newText.length());
									GC gc = new GC(text);
									Point size = gc.textExtent(leftText
											+ e.text + rightText);
									gc.dispose();
									size = text
											.computeSize(size.x, SWT.DEFAULT);
									editor.horizontalAlignment = SWT.LEFT;
									Rectangle itemRect = selection[0]
											.getBounds(),
									rect = tree.getClientArea();
									editor.minimumWidth = Math.max(size.x,
											itemRect.width) + inset * 2;
									int left = itemRect.x,
									right = rect.x + rect.width;
									editor.minimumWidth = Math.min(
											editor.minimumWidth, right - left);
									editor.minimumHeight = size.y + inset * 2;
									editor.layout();
									break;
								case SWT.Traverse:
									switch (e.detail) {
									case SWT.TRAVERSE_RETURN:
										newLinkItem.setText(text.getText());
										// FALL THROUGH
									case SWT.TRAVERSE_ESCAPE:
										composite.dispose();
										e.doit = false;
									}
									break;
								}
							}
						};
						text.addListener(SWT.FocusOut, textListener);
						text.addListener(SWT.Traverse, textListener);
						text.addListener(SWT.Verify, textListener);
						editor.setEditor(composite, selection[0]);
						text.setText(newLinkItem.getText());
						text.setFocus();
					}
				});

				final MenuItem deleteLinkItem = new MenuItem(men, SWT.PUSH);		//for deleting a link
				deleteLinkItem.setText("Delete this link");
				deleteLinkItem.addListener(SWT.Selection, new Listener() {
					public void handleEvent(Event event) {
						final TreeItem[] selection = tree.getSelection();
						if(selection[0].getText(1) != "" && selection[0].getText(0) != ""){
							if (confirmDelete(selection[0]))
								updateTree(selection[0]);
						} else {									
							Object obj = selection[0].getData("0");		
							String id = null;
							if(obj == null)					// for avoiding to get null values
								obj = selection[0].getData("1");
							if(obj instanceof ArtefactElement)
								id = ((ArtefactElement)obj).getArtefactElementId();
							else if(obj instanceof ArtefactSubElement)
								id = ((ArtefactSubElement)obj).getSubElementId();
							ReadFiles.deleteArtefact(id);					//call delete artefact method
							selection[0].dispose();
						}
					}
				});

			}
		});
		StyledText stText= new StyledText(composite, SWT.PUSH | SWT.WRAP | SWT.BORDER);		//to add a description about compared results
		stText.setLayoutData(new GridData(GridData.FILL_BOTH));
		stText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		String text = 	"\n\n\uFFFC - Exact Match Found\n\t\t(Exact same name and edit distance more than 0.85)" +
						"\n\n\uFFFC - Partial Match\n\t\t(Sematically matched)" +
						"\n\n\uFFFC - Violation" +
						"\n\nYou can add new links and can delete unwanted links. " +
						"Just right click on the element you want to change and select your choice";
				
		stText.setText(text);
			int offset = text.indexOf("\uFFFC", 0);
			addImage(new Image(CompareWindow.display, PropertyFile.imagePath + "/" + "exact.jpg"), stText, offset);
			offset = text.indexOf("\uFFFC", offset + 1);
			addImage(new Image(CompareWindow.display, PropertyFile.imagePath + "/" + "warning.png"), stText, offset);
			offset = text.indexOf("\uFFFC", offset + 1);
			addImage(new Image(CompareWindow.display, PropertyFile.imagePath + "/" + "violation.jpg"), stText, offset);
			
			stText.addPaintObjectListener(new PaintObjectListener() {
				@Override
				public void paintObject(PaintObjectEvent event) {
					StyleRange style = event.style;
					Image image = (Image)style.data;
					if (!image.isDisposed()) {
						int x = event.x;
						int y = event.y + event.ascent - style.metrics.ascent;						
						event.gc.drawImage(image, x, y);
					}
				}
			});

	}
	
	/**
	 * 
	 */
	static void addImage(Image image, StyledText stText, int offset) {
		StyleRange style = new StyleRange ();
		style.start = offset;
		style.length = 1;
		style.data = image;
		Rectangle rect = image.getBounds();
		style.metrics = new GlyphMetrics(rect.height, 0, rect.width);
		stText.setStyleRange(style);		
	}

	/**
	 * call appropriate compare classes
	 * @param project
	 * @param selectedFiles
	 */
	private void compareFiles(String project, ArrayList<String> selectedFiles) {
		String filePath = PropertyFile.filePath + project + "\\";
		HomeGUI.isComaparing = true;
		// ReadFiles.readFiles(filePath);
		if (selectedFiles.get(0).contains("UML")
				&& selectedFiles.get(1).contains("Source")
				|| selectedFiles.get(0).contains("Source")
				&& selectedFiles.get(1).contains("UML")) {
			UMLSourceClassManager.compareClassNames(filePath);

		} else if (selectedFiles.get(0).contains("UML")
				&& selectedFiles.get(1).contains("Requirement")
				|| selectedFiles.get(0).contains("Requirement")
				&& selectedFiles.get(1).contains("UML")) {
			RequirementUMLClassManager.compareClassNames(filePath);

		} else if (selectedFiles.get(0).contains("Source")
				&& selectedFiles.get(1).contains("Requirement")
				|| selectedFiles.get(0).contains("Requirement")
				&& selectedFiles.get(1).contains("Source")) {
			RequirementSourceClassManager.compareClassNames(filePath);
		}
	}

	public void center(Shell shell) {

		Rectangle bds = shell.getDisplay().getBounds();

		Point p = shell.getSize();
		shell.setFullScreen(true);
		int nLeft = (bds.width - p.x) / 2;
		int nTop = (bds.height - p.y) / 2;

		shell.setBounds(nLeft, nTop, p.x, p.y);
	}

	void resetEditors() {
		resetEditors(false);
	}

	void resetEditors(boolean tab) {
	}

	/**
	 * confirming the new mapping
	 * @param className
	 * @param mapClass
	 * @return
	 */
	private boolean confirmMapping(Object className, Object mapClass) {
		boolean confirmed = false;
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION
				| SWT.YES | SWT.NO);
		if(className instanceof ArtefactElement && mapClass instanceof ArtefactElement)
			messageBox.setMessage("Do you really want to map " + ((ArtefactElement)className).getName() + " to "
					+ ((ArtefactElement)mapClass).getName() + " ?");
		if(className instanceof ArtefactSubElement && mapClass instanceof ArtefactSubElement)
			messageBox.setMessage("Do you really want to map " + ((ArtefactSubElement)className).getName() + " to "
					+ ((ArtefactSubElement)mapClass).getName() + " ?");
		messageBox.setText("Confirmation");
		int response = messageBox.open();
		if (response == SWT.YES){
			if(classList == null)
				EditManager.addLink(mapClass, className);
			confirmed = true;
		}
		return confirmed;
	}

	
	private String updateSubElements(String fileName1, String fileName2) {
		String className = "";
		if (fileName1.contains("UML") && fileName2.contains("Source")
				|| fileName1.contains("Source") && fileName2.contains("UML")) {
			className = "US";

		} else if (fileName1.contains("UML")
				&& fileName2.contains("Requirement")
				|| fileName1.contains("Requirement")
				&& fileName2.contains("UML")) {
			className = "RU";

		} else if (fileName1.contains("Source")
				&& fileName2.contains("Requirement")
				|| fileName1.contains("Requirement")
				&& fileName2.contains("Source")) {
			className = "RS";
		}
		return className;
	}

	/**
	 * confirm deletion
	 * @param item
	 * @return
	 */
	private boolean confirmDelete(TreeItem item) {

		boolean confirmed = false;
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION
				| SWT.YES | SWT.NO);

		messageBox.setMessage("Do you really want to delete " + item.getText()
				+ " link?");
		messageBox.setText("Confirmation");
		int response = messageBox.open();
		if (response == SWT.YES) {
			EditManager.deleteLink(item);
			confirmed = true;
		}
		return confirmed;
	}

	/**
	 * update tree when changes are made
	 * @param item
	 */
	private void updateTree(TreeItem item) {
		item.setImage(0, ImageType.VIOLATION.getValue());
		TreeItem parentItem = null;
		Tree parent = null;
		if (item.getParentItem() != null && 
				(item.getParentItem().getText().equals("Attributes") || item.getParentItem().getText().equals("Methods"))) {
			parentItem = item.getParentItem();
			TreeItem newItem = new TreeItem(parentItem, SWT.NONE);
			newItem.setText(1, item.getText(1));
			newItem.setData("1", item.getData("" + 1 + ""));
			newItem.setImage(1, ImageType.VIOLATION.getValue());
			item.setText(1, "");
			item.setData("1", null);
			item.setImage(1, null);
		} else if (item.getParentItem() == null) {
			parent = item.getParent();
			TreeItem[] attributeItems = item.getItem(0).getItems();
			TreeItem[] methodItems = item.getItem(1).getItems();
			TreeItem classItem = new TreeItem(parent, SWT.NONE);
			classItem.setText(0, item.getText(0));
			classItem.setData("0", item.getData("0"));
			classItem.setImage(0, ImageType.VIOLATION.getValue());
			TreeItem attributeItem = new TreeItem(classItem, SWT.NONE);
			attributeItem.setText(0, "Attributes");
			for (int i = 0; i < attributeItems.length; i++) {
				if (attributeItems[i].getText(0) != null) {
					TreeItem subItem = new TreeItem(attributeItem, SWT.NONE);
					subItem.setText(0, attributeItems[i].getText(0));
					subItem.setData("0", attributeItems[i].getData("0"));
					subItem.setImage(0, ImageType.VIOLATION.getValue());
				}
			}
			TreeItem methodItem = new TreeItem(classItem, SWT.NONE);
			methodItem.setText(0, "Methods");
			for (int i = 0; i < methodItems.length; i++) {
				if (methodItems[i].getText(0) != null) {
					TreeItem subItem = new TreeItem(methodItem, SWT.NONE);
					subItem.setText(0, methodItems[i].getText(0));
					subItem.setData("0", methodItems[i].getData("0"));
					subItem.setImage(0, ImageType.VIOLATION.getValue());
				}
			}
			classItem = new TreeItem(parent, SWT.NONE);
			classItem.setText(1, item.getText(1));
			classItem.setData("1", item.getData("1"));
			classItem.setImage(1, ImageType.VIOLATION.getValue());
			attributeItem = new TreeItem(classItem, SWT.NONE);
			attributeItem.setText(0, "Attributes");
			for (int i = 0; i < attributeItems.length; i++) {
				if (attributeItems[i].getText(1) != null) {
					TreeItem subItem = new TreeItem(attributeItem, SWT.NONE);
					subItem.setText(1, attributeItems[i].getText(1));
					subItem.setData("1", attributeItems[i].getData("1"));
					subItem.setImage(1, ImageType.VIOLATION.getValue());
				}
			}
			methodItem = new TreeItem(classItem, SWT.NONE);
			methodItem.setText(0, "Methods");
			for (int i = 0; i < methodItems.length; i++) {
				if (methodItems[i].getText(1) != null) {
					TreeItem subItem = new TreeItem(methodItem, SWT.NONE);
					subItem.setText(1, methodItems[i].getText(1));
					subItem.setData("1", methodItems[i].getData("1"));
					subItem.setImage(1, ImageType.VIOLATION.getValue());
				}
			}
			item.dispose();
		}
	}
}
