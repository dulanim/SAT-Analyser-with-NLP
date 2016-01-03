package com.project.traceability.GUI;

/**
 * @author Gitanjali Nov 12, 2014
 */
import com.project.NLP.file.operations.FilePropertyName;
import com.project.NLP.file.operations.FileSave;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolder2Listener;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.project.traceability.staticdata.StaticData;
import com.project.property.config.xml.reader.XMLReader;
import com.project.property.config.xml.writer.XMLWriter;
import com.project.text.undoredo.UndoRedoImpl;
import com.project.traceability.common.PropertyFile;
import com.project.traceability.manager.ReadXML;
import com.project.traceability.visualization.GraphDB;
import com.project.traceability.visualization.GraphDB.RelTypes;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.DirectoryDialog;

/**
 * Main Home Window of the tool
 */
public class HomeGUI extends JFrame implements KeyListener{

	public static Dimension screen = java.awt.Toolkit.getDefaultToolkit()
			.getScreenSize();

	public static Shell shell;
	public static CTabFolder tabFolder;
	public static CTabFolder graphTab;
	public static CTabFolder newTab;
	public static Tree tree;
        public JTree jTree;
	public static Composite composite;
	public static Composite graphComposite;
	public static Composite projComposite;
        public static Display display;
        public static final Map<Boolean,String> activeTab = new HashMap<Boolean,String>();
	public static boolean isComaparing = false;
	public static boolean isSelectionForUMLFile = false;
	public static String projectPath = "";
	public static TreeItem trtmNewTreeitem;
        public static TreeItem topParent;
        public static CTabItem graphtabItem;
        public boolean isSelectedProject = true;
        public boolean isSelectedCompare = true;
        public boolean isSelectedGraph = true;
	private SashForm sidebarSF;
	public SashForm workSF;
	private CTabFolder2Listener ctfCTF2L;
	private MouseListener ctfML;

	static TreeViewer treeViewer;
	
	static String string = "";
	
	public static boolean hasThreeFiles = false;
	public static boolean hasTwoFiles = false;
	public static String selectedFolder;
        public static HomeGUI window;//globally added to refresh the project window
	
        public Map<String,String> recentFilePath = new HashMap<>();
        public Stack<String> recentNames = new Stack(); 
        
        Menu menu_1; // file Menu Drop Bar
        Menu menu_recent;//hold recent file infors
        MenuItem mntmRecents;//recent file holder 
        /**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
                        //XMLWriter writer = new XMLWriter();
                        window = new HomeGUI(); 		//start the project
			window.open();
			window.eventLoop(Display.getDefault());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 * 
	 * @return
	 */
	public Shell open() {
		
		createContents();
		shell.open();
		shell.layout();

		return shell;
	}

	public void eventLoop(Display display) {
                this.display = display;
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public void initUI() {

		Label label = new Label(shell, SWT.LEFT);

		Point p = label.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		label.setBounds(5, 5, p.x + 5, p.y + 5);
	}

	public void center(Shell shell) {

		Rectangle bds = shell.getDisplay().getBounds();

		Point p = shell.getSize();
		shell.setFullScreen(true);
		int nLeft = (bds.width - p.x) / 2;
		int nTop = (bds.height - p.y) / 2;

		shell.setBounds(nLeft, nTop, p.x, p.y);
	}

	/**
	 * Create contents of the window.
	 */
	public void createContents() {
		
		XMLReader reader = new XMLReader();
		reader.readWorkspaces();
		//PropertyFile.filePath = StaticData.rootPathName;//update cuureent workspace 
	
		
		shell = new Shell();
		shell.setBounds(0, 0, screen.width, screen.height - 20);
		center(shell);
		shell.setLayout(new FillLayout());		

                
                
		sidebarSF = new SashForm(shell, SWT.HORIZONTAL | SWT.SMOOTH);  	//sashforms for managing 3 tabfolders
		
           
		newTab = new CTabFolder(sidebarSF, SWT.BORDER | SWT.CLOSE);		//to show all the projects in the workspace
		newTab.setData("Project");
                newTab.redraw();
		newTab.setSize(400, 900);
                newTab.setToolTipText(StaticData.PROJECT_WINDOW_TOOL_TIP);
		newTab.setMinimizeVisible(true);
		newTab.setMaximizeVisible(true);

		projComposite = new Composite(newTab, 0);					//composite to contain all widgets
		projComposite.setData(newTab);
		projComposite.setLayout(new FillLayout());

		workSF = new SashForm(sidebarSF, SWT.VERTICAL | SWT.SMOOTH);		// sash form for vertical CTabFloders
		sidebarSF.setWeights(new int[] { 1, 5 });

		tabFolder = new CTabFolder(workSF, SWT.BORDER | SWT.CLOSE);			//new CTabFolder to show opened files and compared results
		tabFolder.setData("WorkSpace");
		tabFolder.setMinimizeVisible(true);
		tabFolder.setMaximizeVisible(true);
                //final ToolBar bar = new ToolBar(tabFolder, SWT.HORIZONTAL);
		graphTab = new CTabFolder(workSF, SWT.BORDER | SWT.NONE);		//CTabFolder for visualization
		graphTab.setData("Graph");
		graphTab.setMinimizeVisible(true);
		graphTab.setMaximizeVisible(true);
		
		workSF.setWeights(new int[] { 1, 1 });

		composite = new Composite(tabFolder, SWT.NONE);
		composite.setLayout(new FillLayout());

		graphComposite = new Composite(graphTab, SWT.NONE);
		graphComposite.setLayout(new FillLayout());

                
                     
                addEditMenuPopUpMenu();
                //create ToolBar and ToolItems
//                final ToolItem openToolItem = new ToolItem(bar, SWT.PUSH);
//                final ToolItem saveToolItem = new ToolItem(bar, SWT.PUSH);
//                final ToolItem sep1 = new ToolItem(bar, SWT.SEPARATOR);
//                final ToolItem cutToolItem = new ToolItem(bar, SWT.PUSH);
//                final ToolItem copyToolItem = new ToolItem(bar, SWT.PUSH);
//                final ToolItem pasteToolItem = new ToolItem(bar, SWT.PUSH);
//                
//                            //set the size and location of the user interface widgets
//                bar.setSize(50, 55);
//                bar.setLocation(10, 0);
//                Device dev = shell.getDisplay();
//                final Image saveIcon = new Image(dev, "/home/shiyam/Desktop/Resources/Images/file_txt.png");
//                final Image openIcon = new Image(dev, "/home/shiyam/Desktop/Resources/Images/file_txt.png");
//                final Image childIcon = new Image(dev, "/home/shiyam/Desktop/Resources/Images/file_txt.png");
//                final Image cutIcon = new Image(dev, "/home/shiyam/Desktop/Resources/Images/file_txt.png");
//                final Image copyIcon = new Image(dev, "/home/shiyam/Desktop/Resources/Images/file_txt.png");
//                final Image pasteIcon = new Image(dev, "/home/shiyam/Desktop/Resources/Images/file_txt.png");
//                //t.setBounds(0, 56, 490, 395);
//
//                //Configure the ToolBar
//                openToolItem.setImage(openIcon);
//                openToolItem.setText("Open");
//                openToolItem.setToolTipText("Open File");
//                saveToolItem.setImage(saveIcon);
//                saveToolItem.setText("Save");
//                saveToolItem.setToolTipText("Save File");
//                cutToolItem.setImage(cutIcon);
//                cutToolItem.setText("Cut");
//                cutToolItem.setToolTipText("Cut");
//                copyToolItem.setImage(copyIcon);
//                copyToolItem.setText("Copy");
//                copyToolItem.setToolTipText("Copy");
//                pasteToolItem.setImage(pasteIcon);
//                pasteToolItem.setText("Paste");
//                pasteToolItem.setToolTipText("Paste");
                
                
		defineListeners();
		
		newTab.addCTabFolder2Listener(ctfCTF2L);			//for managing maximize, minimize and restore
		newTab.addMouseListener(ctfML);

		tabFolder.addCTabFolder2Listener(ctfCTF2L);
		tabFolder.addMouseListener(ctfML);

		graphTab.addCTabFolder2Listener(ctfCTF2L);
		graphTab.addMouseListener(ctfML);


		graphtabItem = new CTabItem(graphTab, SWT.NONE);		//create CTabItem for visualization
		graphtabItem.setText("Graph");
                
		File projectFile = new File(PropertyFile.filePath);		//to access the workspace
		projectFile.mkdir();
		
		ArrayList<String> projectFiles;
                /*if(StaticData.fileNames != null){
		projectFiles = StaticData.fileNames.keySet().
                }else{
                    projectFiles = new ArrayList<>();
                }*/
                //load all the projects
                
                CTabItem tbtmProjects = new CTabItem(newTab, SWT.NONE);		//CTabItem to show projects
		tbtmProjects.setText("Projects");
		/*if (projectFiles.isEmpty())
			newTab.setVisible(false);*/
		

		tree = new Tree(newTab, SWT.BORDER|SWT.BORDER | SWT.V_SCROLL
        | SWT.H_SCROLL|SWT.MULTI);				//tree for all projects
                tree.setRedraw(true);
                
                //new FileHiearchyView();
                tree.setToolTipText(StaticData.workspace);
		
		tbtmProjects.setControl(projComposite);

		tree.addListener(SWT.Selection, new Listener() {		//add listener to the tree
			public void handleEvent(Event e) {
				String string = "";
				TreeItem[] selection = tree.getSelection();
				for (int i = 0; i < selection.length; i++) {
					string += selection[i] + " ";
					trtmNewTreeitem = selection[i];
				}
                                if(selection.length > 0)
                                    string = selection[0].getText();
				selectedFolder = string;
				projectPath = tree.getToolTipText() + File.separator + string + "";
				if(selection.length >0 && selection[0].getItemCount() >= 2)
					hasTwoFiles = true;
				if(selection.length>0 && selection[0].getItemCount() > 3)
					hasThreeFiles = true;				
				addPopUpMenu();
			}

		});

		tree.addMouseListener(new MouseAdapter() {	
                    //mouse double click listener to display the files
                    /*
                    Open files when double clicks 
                    */
			@Override
			public void mouseDoubleClick(MouseEvent event) {
				String string = "";
				String parent = "";
				TreeItem[] selection = tree.getSelection();
                                TreeItem paret = trtmNewTreeitem.getParentItem();;
                                TreeItem topParent= paret.getParentItem();
                                
                                if(topParent == null)
                                {
                                    string = selection[0].getText();
                                    parent = paret.getText();
                                }else{
                                    string = selection[0].getText();
                                    parent = topParent.getText() + File.separator + paret.getText();
                                }
                                
                                //String projectDir = projectPath;
                                //Tree tree = (Tree) event.getSource();
                                //ITreeSelection selection1 = ((ITreeSelection)event.getSelection());
                                
				for (int i = 0; i < selection.length; i++) {
//					string += selection[i] + " ";
//					parent += selection[i].getParent() + " ";
//					trtmNewTreeitem = selection[i];
                                      
				}

				if(selection[0].getItems().length == 0) {
					//string = string.substring(10, string.length() - 2);
					//parent = parent.substring(14, parent.length() - 2);
					NewFileWindow.localFilePath = StaticData.workspace + File.separator + parent
							+File.separator;
                                        
                                        recentFilePath.put(NewFileWindow.localFilePath+string,string);
                                        recentNames.push(string);
                                        
                                        
                                        for(int i=0;i<recentNames.size();i++){
                                            
                                            if(i<6){
                                                 MenuItem mntmRecents = new MenuItem(menu_recent, SWT.CASCADE);
                                                 mntmRecents.addSelectionListener(new SelectionAdapter() {
			
                                                 });
                                                 mntmRecents.setText(recentNames.pop());
                                            }else 
                                                break;
                                        }
                                       
					NewFileWindow.createTabLayout(string);
                                        
                                        
				} else if(selection[0].getParent().equals(tree)) {
					trtmNewTreeitem.setExpanded(true);
				}
			}
		});

		tbtmProjects.setControl(tree);			//add tree to CTabItem

		if (StaticData.workspace.equals(""))
			tree.setVisible(false);
		else {
                    //adding project folders and files from workspace in project tab
                        setUpNewProject(StaticData.workspace,"Initial State");
		}

		shell.setText("Software Artefact Traceability Analyzer");

		Menu menu = new Menu(shell, SWT.BAR);			//for adding multiple menu items
		shell.setMenuBar(menu);

		MenuItem mntmNewSubmenu = new MenuItem(menu, SWT.CASCADE);
		mntmNewSubmenu.setText("File");
               
		menu_1 = new Menu(mntmNewSubmenu);
		mntmNewSubmenu.setMenu(menu_1);
                
		MenuItem mntmNew = new MenuItem(menu_1, SWT.CASCADE);
		mntmNew.setText("New");

		Menu menu_4 = new Menu(mntmNew);
		mntmNew.setMenu(menu_4);

		MenuItem mntmProject = new MenuItem(menu_4, SWT.NONE);
		mntmProject.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
//				NewProjectWindow newProjWin = new NewProjectWindow();
//				newProjWin.open();
				
				ProjectCreateWindow.main(null);
			}
		});
		mntmProject.setText("Project");

		MenuItem mntmSave = new MenuItem(menu_1, SWT.NONE);
		mntmSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new FileSave().saveFile();
			}
		});
               
		mntmSave.setText("Save");
		mntmSave.setAccelerator(SWT.CTRL | 'S');
		MenuItem mntmProjctPath = new MenuItem(menu_1, SWT.CASCADE);
		mntmProjctPath.setText("Switch Project Path");
                  //  Create the first separator
                new MenuItem(menu_1, SWT.SEPARATOR);
                
                MenuItem mntmClose = new MenuItem(menu_1, SWT.NONE);
		mntmClose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new FileSave().saveFile();
			}
		});
               
		mntmClose.setText("Close");
                
                MenuItem mntmCloseAll = new MenuItem(menu_1, SWT.NONE);
		mntmCloseAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				new FileSave().saveFile();
			}
		});
               
		mntmCloseAll.setText("Close All");
                
                new MenuItem(menu_1, SWT.SEPARATOR);
		Menu menu_5 = new Menu(mntmProjctPath);
		mntmProjctPath.setMenu(menu_5);
		
		MenuItem mntmWorkspace;// = new MenuItem(menu_5, SWT.NONE);
                Set<String> set = StaticData.paths;
                set.remove(StaticData.workspace);
                List<String> list = new ArrayList<>(set);
		for(String paths:list){
			mntmWorkspace	= new MenuItem(menu_5, SWT.NONE);
			final MenuItem temp = mntmWorkspace;
			mntmWorkspace.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					//load current location project files
					//XMLWriter writer = new XMLWriter();
					String path = temp.getText().trim();
                                        StaticData.workspace = path;
					//writer.modifyStatus(true, path);
					//writer.modifyStatus(false, PropertyFile.filePath);
					shell.dispose();
                                        HomeGUI.main(null);
				}
			});
			mntmWorkspace.setText(paths);
		}
		mntmWorkspace = new MenuItem(menu_5, SWT.NONE);
		mntmWorkspace.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int x = JOptionPane.showConfirmDialog(null, "Do you want to change your current workspace?\n"+StaticData.workspace, "Worspace Confirmation", 
                                            JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                                   if(x == JOptionPane.YES_OPTION){
                                        DirectoryDialog dialog = new DirectoryDialog(shell);
                                        dialog.setFilterPath("c:\\"); // Windows specific
                                        PropertyFile.filePath = dialog.open();
                                        
                                        XMLWriter writer = null;
                                        if(writer == null){
                                            writer = new XMLWriter();
                                        }
                                        
                                        if(!StaticData.workspace.equals(PropertyFile.filePath)){
                                            StaticData.workspace = PropertyFile.filePath;
                                            writer.createWorkspaceNode(StaticData.workspace, "true");
                                            shell.dispose();
                                            ProgressBar bar =  new ProgressBar();
                                            bar.main(null);
                                        }
                                   }else if(x == JOptionPane.NO_OPTION){
                                       
                                   }
			}
		});
		mntmWorkspace.setText("Other...");
		
		MenuItem mntmRestart = new MenuItem(menu_1, SWT.NONE);
		mntmRestart.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//restart functionality
                                shell.dispose();
                                ProgressBar bar = new ProgressBar();
                                bar.main(null);
			}
		});
		mntmRestart.setText("Restart");
		
		MenuItem mntmRefresh = new MenuItem(menu_1, SWT.NONE);
		mntmRefresh.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//referesh functionality
                            shell.dispose();
                            HomeGUI.main(null);
			}
		});
		mntmRefresh.setText("Refresh");
		  //  Create the first separator
                new MenuItem(menu_1, SWT.SEPARATOR);
                
                MenuItem mntmImport = new MenuItem(menu_1, SWT.NONE);
		mntmImport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//Import Functionality Here
                            ImportProjectWindow.copyingLocation = StaticData.workspace;
                            ImportProjectWindow.main(null);
                            shell.dispose();
                            HomeGUI.main(null);
			}
		});
		mntmImport.setText("Import Project");
                
                MenuItem mntmExport = new MenuItem(menu_1, SWT.NONE);
		mntmExport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//referesh functionality
                            shell.dispose();
                            HomeGUI.main(null);
                            
			}
		});
		mntmExport.setText("Export Project");
                //  Create the first separator
                new MenuItem(menu_1, SWT.SEPARATOR);
                
                mntmRecents = new MenuItem(menu_1, SWT.CASCADE);
		mntmRecents.addSelectionListener(new SelectionAdapter() {
			
		});
		mntmRecents.setText("Recent Files");
                
                menu_recent = new Menu(mntmRecents);
		mntmRecents.setMenu(menu_recent);
                
                //  Create the first separator
                new MenuItem(menu_1, SWT.SEPARATOR);
		MenuItem mntmExit = new MenuItem(menu_1, SWT.NONE);
		mntmExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.exit(0);
			}
		});
		mntmExit.setText("Exit");
                //shiyam edited
		//MenuItem mntmView = new MenuItem(menu, SWT.CASCADE);
		//mntmView.setText("View");
                Menu editMenu = new Menu(menu);
                MenuItem editMenuItem = new MenuItem(menu,SWT.CASCADE);
                editMenuItem.setText("Edit");
                editMenuItem.setMenu(editMenu);
                
                MenuItem copy = new MenuItem(editMenu,SWT.NONE);
                copy.setText("Copy");
                copy.setAccelerator(SWT.CTRL | 'C');
                copy.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				/*
                            run how to copy the text
                            */
                            NewFileWindow.codeText.copy();
			}
		});
                
                MenuItem paste = new MenuItem(editMenu,SWT.NONE);
                paste.setText("Paste");
                paste.setAccelerator(SWT.CTRL | 'V');
                paste.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				/*
                            run how to paste the text
                            */
                            NewFileWindow.codeText.paste();
			}
		});
                
                MenuItem cut = new MenuItem(editMenu,SWT.NONE);
                cut.setText("Cut");
                cut.setAccelerator(SWT.CTRL | 'X');
                cut.setImage(new Image(shell.getDisplay(),FilePropertyName.IMAGE_PATH+"file_txt.png"));
                cut.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				/*
                            run how to paste the text
                            */
                            NewFileWindow.codeText.cut();
			}
		});
                
                
                MenuItem redo = new MenuItem(editMenu,SWT.NONE);
                redo.setText("Redo");
                redo.setImage(new Image(shell.getDisplay(),FilePropertyName.IMAGE_PATH+"file_txt.png"));
                redo.setAccelerator(SWT.CTRL | 'Y');
                redo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				/*
                            run how to redo perform
                            */
                            //NewFileWindow.codeText.cut();
                            new UndoRedoImpl(NewFileWindow.codeText).redo();
			}
		});
                
                MenuItem undo = new MenuItem(editMenu,SWT.NONE);
                undo.setText("Undo");
                undo.setAccelerator(SWT.CTRL | 'Z');
                undo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				/*
                            run how to redo perform
                            */
                            //NewFileWindow.codeText.cut();
                            new UndoRedoImpl(NewFileWindow.codeText).undo();
			}
		});
                
                
                MenuItem selectAll = new MenuItem(editMenu,SWT.NONE);
                selectAll.setText("Select All");
                selectAll.setAccelerator(SWT.CTRL | 'A');
                selectAll.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				/*
                            run how to select All text
                            */
                            //NewFileWindow.codeText.cut();
			}
		});
                
                class Cut extends SelectionAdapter {
                                public void widgetSelected(SelectionEvent event) {
                                        
                                }
                }
                // Create the File item's drop down menu
                Menu viewMenu = new Menu(menu);

                // Create all the items in the bar menu
                MenuItem mnViItem = new MenuItem(menu, SWT.CASCADE);
                mnViItem.setText("View");
                mnViItem.setMenu(viewMenu);

                // Create all the items in the File drop down menu
                final MenuItem projectWindowItem = new MenuItem(viewMenu, SWT.CHECK);
                projectWindowItem.setText("Project Window");
                projectWindowItem.setSelection(true);
                projectWindowItem.addSelectionListener(new SelectionAdapter() {
                       
			@Override
			public void widgetSelected(SelectionEvent e) {      
                            if(false){
                                projectWindowItem.setSelection(false);
                                isSelectedProject = false;
                                newTab.setVisible(false);
                            }else{
                                //open the proect window
                                projectWindowItem.setSelection(true);
                                isSelectedProject = true;
                                newTab.setVisible(true);
                            }
			}
		});
                
                final MenuItem CompareWindowItem = new MenuItem(viewMenu, SWT.CHECK);
                CompareWindowItem.setText("Compare Window");
                CompareWindowItem.setSelection(true);
                CompareWindowItem.addSelectionListener(new SelectionAdapter() {
                       
			@Override
			public void widgetSelected(SelectionEvent e) {
                             //projectWindowItem.setSelection(false);
                            if(isSelectedCompare){
                                isSelectedCompare = false;
                                CompareWindowItem.setSelection(isSelectedCompare);
                                tabFolder.setVisible(false);
                            }else{
                                //open the proect window
                                isSelectedCompare = true;
                                CompareWindowItem.setSelection(isSelectedCompare);
                                tabFolder.setVisible(true);
                            }
			}
		});
                final MenuItem GraphWindowItem = new MenuItem(viewMenu, SWT.CHECK);
                GraphWindowItem.setText("Graph Window");
                GraphWindowItem.setSelection(true);
                GraphWindowItem.addSelectionListener(new SelectionAdapter() {
                       
			@Override
			public void widgetSelected(SelectionEvent e) {
                             //projectWindowItem.setSelection(false);
                            if(isSelectedCompare){
                                GraphWindowItem.setSelection(false);
                                graphTab.setVisible(false);
                                isSelectedGraph = false;
                            }else{
                                //open the compare window
                                isSelectedGraph = true;
                                GraphWindowItem.setSelection(isSelectedGraph);
                                graphTab.setVisible(true);
                            }
			}
		});
                
                
//                Menu viewMenu = new Menu(shell, SWT.DROP_DOWN);
//                mntmView.setMenu(viewMenu);
//
//                MenuItem menuItemProjectWindow = new MenuItem(viewMenu, SWT.PUSH);
//                menuItemProjectWindow.setText("&Show Project Window");
//                
//                MenuItem menuItemCompareWindow = new MenuItem(viewMenu, SWT.PUSH);
//                menuItemCompareWindow.setText("&Show Compare Window");
//                
//                MenuItem menuItemGraphWindow = new MenuItem(viewMenu, SWT.PUSH);
//                menuItemGraphWindow.setText("&Show Graph Window");

		//Menu menu_2 = new Menu(mntmView);
		//mntmView.setMenu(menu_2);

		MenuItem mntmHelp = new MenuItem(menu, SWT.CASCADE);
		mntmHelp.setText("Help");

		Menu menu_3 = new Menu(mntmHelp);
		mntmHelp.setMenu(menu_3);

	}	

	
	private void defineListeners() {
		ctfCTF2L = new CTabFolder2Adapter() {
			public void close(CTabFolderEvent e) {
				e.doit = false;
				CTabFolder ctf = (CTabFolder) e.getSource();
				String ctfname = (String) ctf.getData();
				CTabItem item = ctf.getSelection();
				item.dispose();
				//ctf.dispose();
				workSF.layout();
				sidebarSF.layout();
			}

			public void maximize(CTabFolderEvent e) {
				e.doit = false;
				CTabFolder ctf = (CTabFolder) e.getSource();
				String ctfname = (String) ctf.getData();
				ctf.setMaximized(true);
				if (!ctfname.equalsIgnoreCase("Project")) {
					workSF.setMaximizedControl(ctf);
					sidebarSF.setMaximizedControl(workSF);
				} else
					sidebarSF.setMaximizedControl(ctf);

				System.out.println("Maximized: " + ctfname);
			}

			public void minimize(CTabFolderEvent e) {
				e.doit = false;
				CTabFolder ctf = (CTabFolder) e.getSource();
				String ctfname = (String) ctf.getData();
				
				if (ctfname.equalsIgnoreCase("WorkSpace")) {
					CTabFolder next = (CTabFolder) workSF.getTabList()[1];
					workSF.setMaximizedControl(next);
				} else if (ctfname.equalsIgnoreCase("Graph")) {
					CTabFolder next = (CTabFolder) workSF.getTabList()[0];
					workSF.setMaximizedControl(next);
				} else if (ctfname.equalsIgnoreCase("Project")) {
					CTabFolder next = (CTabFolder) workSF.getTabList()[1];
					sidebarSF.setMaximizedControl(workSF);
				}
				ctf.setMinimized(true);

				workSF.layout(true);
				sidebarSF.layout(true);
			}

			public void restore(CTabFolderEvent e) {
				e.doit = false;
				CTabFolder ctf = (CTabFolder) e.getSource();
				String ctfname = (String) ctf.getData();

				// if restoring from maximized state
				if (!ctfname.equalsIgnoreCase("Project")) {
					if (ctf.equals(workSF.getMaximizedControl())) {
						ctf.setMaximized(false);
						workSF.setMaximizedControl(null);
						sidebarSF.setMaximizedControl(null);
					}
				} else if (ctf.equals(sidebarSF.getMaximizedControl())) {
					ctf.setMaximized(false);
					sidebarSF.setMaximizedControl(null);
				}
				// if restoring from minimized state
				else {
					int w = ctf.getSize().x;
					ctf.setMinimized(false);

				}

				System.out.println("Restored: " + ctfname);
			}
		};

		ctfML = new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				CTabFolder ctf = (CTabFolder) e.getSource();
				if (!ctf.isDisposed()) {
					String ctfname = (String) ctf.getData();
					System.out.println("Clicked: " + ctfname);
				}
			}
		};
	}

	public static void deleteFiles(String projectPath) {

		TreeItem parent = trtmNewTreeitem.getParentItem();
	
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION
				| SWT.YES | SWT.NO);
                
                
		File filePath = new File(projectPath);
		if (!filePath.isDirectory()) {
			projectPath = PropertyFile.filePath + parent.getText() + "/"
					+ trtmNewTreeitem.getText();
		}
		messageBox.setMessage("Do you really want to delete " + projectPath
				+ " ?");
		messageBox.setText("Deleting " + projectPath);
		int response = messageBox.open();
		if (response == SWT.YES) {
                    
                    if(trtmNewTreeitem.getText().equals(FilePropertyName.XML)
                            ||trtmNewTreeitem.getText().equals(FilePropertyName.UML)||
                            trtmNewTreeitem.getText().equals(FilePropertyName.PROPERTY)
                            ||trtmNewTreeitem.getText().equals(FilePropertyName.SOURCE_CODE)
                            ||trtmNewTreeitem.getText().equals(FilePropertyName.REQUIREMENT)){
                        
                        
                         messageBox = new MessageBox(shell, SWT.ICON_ERROR|
				SWT.ERROR_CANNOT_GET_SELECTION);
                         messageBox.setMessage("File Delete Error\n" + "Those directory can not delete");
                         messageBox.setText("Error");
                         messageBox.open();
                         return;
                    }
			File file = new File(projectPath);
                        try{
                            FilePropertyName.delete(file);
                        }catch(IOException e){
                            messageBox = new MessageBox(shell, SWT.ICON_ERROR|
				SWT.ERROR_IO);
                            messageBox.setMessage("File Delete Error\n" + e.toString());
                            messageBox.setText("Error");
                            messageBox.open();
                        }catch(Exception e){
                            messageBox = new MessageBox(shell, SWT.ICON_ERROR|
				SWT.ERROR_IO);
                            messageBox.setMessage("File Delete Error \n" + e.toString());
                            messageBox.setText("Error");
                            messageBox.open();
                        }
			HomeGUI.trtmNewTreeitem.dispose();
		}
		projComposite.layout();
	}

    public void addEditMenuPopUpMenu(){
        Menu popupMenu = new Menu(workSF);
        MenuItem cutItem = new MenuItem(popupMenu, SWT.CASCADE);
        cutItem.setText("Cut");
        
        MenuItem copyItem = new MenuItem(popupMenu, SWT.CASCADE);
        copyItem.setText("Copy");
        
        MenuItem pasteItem = new MenuItem(popupMenu, SWT.CASCADE);
        pasteItem.setText("Paste");
        
        MenuItem selectAllItem = new MenuItem(popupMenu,SWT.CASCADE);
        selectAllItem.setText("Select All");
        
        MenuItem refreshItem = new MenuItem(popupMenu,SWT.CASCADE);
        refreshItem.setText("Refresh Window");
        
        workSF.setMenu(popupMenu);
    }
    public static void addPopUpMenu() {
        Menu popupMenu = new Menu(tree);
        MenuItem newItem = new MenuItem(popupMenu, SWT.CASCADE);
        newItem.setText("New");

        MenuItem graphItem = new MenuItem(popupMenu, SWT.CASCADE);
        graphItem.setText("Visualization");
        graphItem.setEnabled(hasThreeFiles);

        MenuItem refreshItem = new MenuItem(popupMenu, SWT.NONE);
        refreshItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                composite.pack(true);
            }
        });
        refreshItem.setText("Refresh");
        
        MenuItem projectItem = new MenuItem(popupMenu, SWT.CASCADE);
        projectItem.setText("Project");
        Boolean notProject = false;
        String name = trtmNewTreeitem.getText();
        if(name.equals(FilePropertyName.UML) || name.equals(FilePropertyName.XML)||
                name.equals(FilePropertyName.SOURCE_CODE) || name.equals(FilePropertyName.REQUIREMENT)||
                       name.equals(FilePropertyName.PROPERTY)){
            notProject = true;
        }
        File file = new File(tree.getToolTipText() + File.separator + name);
        projectItem.setEnabled((file.isDirectory()&& !notProject));
        MenuItem deleteItem = new MenuItem(popupMenu, SWT.NONE);
        deleteItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                
               
                deleteFiles(projectPath);
              
            	
        	
            }
        });
        deleteItem.setText("Delete");

        MenuItem compareItem = new MenuItem(popupMenu, SWT.NONE);
        compareItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                final FileSelectionWindow window = new FileSelectionWindow();

                TreeItem[] selection = tree.getSelection();
                string = "";
                for (int i = 0; i < selection.length; i++) {
                    string += selection[i] + " ";
                }
                string = string.substring(10, string.length() - 2);
                window.open(string);
            }
        });
        compareItem.setText("Compare Files");
        compareItem.setEnabled(hasTwoFiles);

        Menu newMenu = new Menu(popupMenu);
        newItem.setMenu(newMenu);

        MenuItem fileItem = new MenuItem(newMenu, SWT.NONE);
        fileItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
               
                try{
                    File file = new File(HomeGUI.projectPath);
                    String lastTxt[] = HomeGUI.projectPath.split("/");
                    String lastName = lastTxt[lastTxt.length-1];
                    TreeItem item[] = tree.getSelection();
                    TreeItem parent = trtmNewTreeitem.getParentItem();
                    topParent= trtmNewTreeitem;
                    if(parent == null){
                        trtmNewTreeitem = trtmNewTreeitem.getParentItem();
                        projectPath = HomeGUI.projectPath;
                    }else{
                        projectPath = StaticData.workspace + File.separator +  parent.getText() +
                                File.separator + lastName;
                    }
                    System.out.println(trtmNewTreeitem + projectPath);
                    NewFileWindow newFileWin = new NewFileWindow();
                    newFileWin.open(trtmNewTreeitem, projectPath);
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        fileItem.setText("File");

                
        Menu projectMenu = new Menu(popupMenu);
        projectItem.setMenu(projectMenu);
        
        MenuItem closeItem = new MenuItem(projectMenu, SWT.NONE);
        closeItem.addSelectionListener(new SelectionAdapter() {
            
        });
        closeItem.setText("Close");
        
        MenuItem deleteProItem = new MenuItem(projectMenu, SWT.NONE);
        deleteProItem.addSelectionListener(new SelectionAdapter() {
            
        });
        deleteProItem.setText("Delete");
        
        Menu visualMenu = new Menu(popupMenu);
        graphItem.setMenu(visualMenu);

        final MenuItem allItem = new MenuItem(visualMenu, SWT.NONE);
        allItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                HomeGUI.setupProject(allItem.getText());
            }
        });
        allItem.setText("Full Graph");

        final MenuItem edgeItem = new MenuItem(visualMenu, SWT.CASCADE);
        edgeItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                HomeGUI.setupProject(edgeItem.getText());
            }
        });
        edgeItem.setText("Edge Filtered");

        Menu edgeMenu = new Menu(popupMenu);
        edgeItem.setMenu(edgeMenu);
        List<MenuItem> menuItem = new ArrayList<MenuItem>();
        int i = 0;
        for (RelTypes type : GraphDB.RelTypes.values()) {
            menuItem.add(new MenuItem(edgeMenu, SWT.NONE));
            final MenuItem item = menuItem.get(i);

            item.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    HomeGUI.setupProject(item.getText());
                }
            });
            menuItem.get(i).setText(type.getValue());
            i++;
        }

        final MenuItem nodeItem = new MenuItem(visualMenu, SWT.CASCADE);
        nodeItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                HomeGUI.setupProject(nodeItem.getText());
            }
        });
        nodeItem.setText("Node Filtered");

        Menu nodeMenu = new Menu(popupMenu);
        nodeItem.setMenu(nodeMenu);

        final MenuItem classItem = new MenuItem(nodeMenu, SWT.NONE);
        classItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                HomeGUI.setupProject(classItem.getText());
            }
        });
        classItem.setText("Class");

        final MenuItem attributeItem = new MenuItem(nodeMenu, SWT.NONE);
        attributeItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                HomeGUI.setupProject(attributeItem.getText());
            }
        });
        attributeItem.setText("Attributes");

        final MenuItem methodItem = new MenuItem(nodeMenu, SWT.NONE);
        methodItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                HomeGUI.setupProject(methodItem.getText());
            }
        });
        methodItem.setText("Methods");
        tree.setMenu(popupMenu);
    }
    
    public static void setupProject(String graphType){
          String projectName = trtmNewTreeitem.getText();
                PropertyFile.setProjectName(projectName);
                PropertyFile.setGraphDbPath(projectPath +File.separator + FilePropertyName.PROPERTY+ projectName
                        + ".graphdb");
                PropertyFile.setGeneratedGexfFilePath(projectPath + File.separator +  projectName
                        + ".gexf");
                PropertyFile.setRelationshipXMLPath(projectPath + File.separator + projectName + FilePropertyName.XML + File.separator
                        + "Relations.xml");
                PropertyFile.setGraphType(graphType);
                System.out.println("Path: " + projectPath);
                System.out.println("DB Path: " + PropertyFile.getGraphDbPath());
                System.out.println("Graph Type: " + PropertyFile.getGraphType());
                ReadXML.initApp(projectPath, graphType);
    }
    public static void setUpNewProject(String path,String tag){
     
//      tree = new Tree(newTab, SWT.BORDER|SWT.BORDER | SWT.V_SCROLL
//        | SWT.H_SCROLL);
            try{
                    File project_root_folder = new File(path);
                    ArrayList<String> project_sub_folder = new ArrayList<String>(
                    Arrays.asList(project_root_folder.list()));
                    
                    for(String name:project_sub_folder){//Anduril Shaym Uni 
                        TreeItem rootNewTreeitem = null;
//                        rootNewTreeitem.set
                        if(tag.equals("Initial State")){
                             rootNewTreeitem = new TreeItem(tree, SWT.NONE);
                             rootNewTreeitem.setImage(new Image(display, FilePropertyName.IMAGE_PATH.concat("folder.gif")));
                        }else{
                            rootNewTreeitem = new TreeItem(ProjectCreateWindow.trtmNewTreeitem,SWT.NONE);
                        }
                        rootNewTreeitem.setText(name);
                       
                        File temp_file = new File(path + File.separator+name);//name:Anduril
                        ArrayList<String> internal_folders = new ArrayList<String>(
                            Arrays.asList(temp_file.list()));//Anduril's file / folder list
                        
                        
                        for(String tempInternalFolderName:internal_folders){//xml uml 
                            
                            File tempInternalFolder = new File(temp_file.getPath() + File.separator + tempInternalFolderName);
                            
                            if(tempInternalFolder.isDirectory()){
                                TreeItem trtmNewTreeitem = new TreeItem(rootNewTreeitem, SWT.NONE);
                                trtmNewTreeitem.setText(tempInternalFolderName);
                                trtmNewTreeitem.setImage(new Image(display,
                                            FilePropertyName.IMAGE_PATH.concat("folder.gif")));
                                
                                ArrayList<String> internal_files = new ArrayList<String>(
                                Arrays.asList(tempInternalFolder.list()));
                                for(String tempFileName:internal_files){
                                    TreeItem fileTreeItem = new TreeItem(trtmNewTreeitem, SWT.NONE);
                                    fileTreeItem.setText(tempFileName);
                                    fileTreeItem.setImage(new Image(display,
                                            FilePropertyName.IMAGE_PATH.concat("file_txt.png")));
                                }
                            }else{
                                TreeItem fileTreeItem = new TreeItem(rootNewTreeitem, SWT.NONE);
                                fileTreeItem.setText(tempInternalFolderName);
                                fileTreeItem.setImage(new Image(display,FilePropertyName.IMAGE_PATH.concat("file_txt.png")));
                            }
                        }
                        
                    }
                    }catch(Exception e){
                        JOptionPane.showMessageDialog(null,"Selected Path Has some unrelated File\nSelect New Empty Workspace", "File Open Error", JOptionPane.ERROR_MESSAGE);
                        shell.dispose();
                        WorkspaceSelectionWindow window = new WorkspaceSelectionWindow();
                        window.main(null);
                        
                    }
    }
    public void keyPressed(KeyEvent e) {
        // Listen to CTRL+Z for Undo, to CTRL+Y or CTRL+SHIFT+Z for Redo
        boolean isCtrl = (e.stateMask & SWT.CTRL) > 0;
        boolean isAlt = (e.stateMask & SWT.ALT) > 0;
        if (isCtrl && !isAlt) {
            boolean isShift = (e.stateMask & SWT.SHIFT) > 0;
            if (!isShift && e.keyCode == 'S') {
                new FileSave().saveFile();
            } else if (!isShift && e.keyCode == 'y' || isShift
                    && e.keyCode == 'z') {
                
            }
        }
    }    
    
    public void keyReleased(KeyEvent e){
        
    }
    
    public void keyTyped(KeyEvent e){
        
    }
}
