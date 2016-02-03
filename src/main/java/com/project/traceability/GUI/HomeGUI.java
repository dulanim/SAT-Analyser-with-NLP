package com.project.traceability.GUI;

/**
 * @author Gitanjali Nov 12, 2014
 */
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTree;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolder2Listener;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swtbot.swt.finder.utils.SWTUtils;

import com.project.NLP.file.operations.FilePropertyName;
import com.project.NLP.file.operations.FileSave;
import com.project.property.config.xml.reader.XMLReader;
import com.project.property.config.xml.writer.XMLWriter;
import com.project.text.undoredo.UndoRedoImpl;
import com.project.traceability.common.ErrorFinder;
import com.project.traceability.common.PropertyFile;
import com.project.traceability.manager.ReadXML;
import com.project.traceability.staticdata.StaticData;
import com.project.traceability.visualization.GraphDB;
import com.project.traceability.visualization.GraphDB.RelTypes;

/**
 * Main Home Window of the tool
 */

public class HomeGUI extends JFrame implements KeyListener {

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
    public  static Display display;
    public static final Map<Boolean, String> activeTab = new HashMap<Boolean, String>();
    public static boolean isComaparing = false;
    public static boolean isSelectionForUMLFile = false;
    public static String projectPath = "";
    public static String projectName = "";
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
    public static CTabFolder propertyTab ;
    public static boolean isImport = false;
    static TreeViewer treeViewer;

    static String string = "";

    public static boolean hasThreeFiles = false;
    public static boolean hasTwoFiles = false;
    public static String selectedFolder;
    public static HomeGUI window;//globally added to refresh the project window

    public Map<String, String> recentFilePath = new HashMap<>();
    public Stack<String> recentNames = new Stack<>();

    Menu menu_1; // file Menu Drop Bar
    public static Menu menu_recent;//hold recent file infors
    MenuItem mntmRecents;//recent file holder 

    static XMLReader reader;
    static XMLWriter writer;

    public static CTabItem tbtmPropertyInfos;
    public static Table table;
    public static TableColumn tblclmnProperty;
    public static TableColumn tblclmnValue;
    public static TableCursor tableCursor;
    public static TableItem tableItem;
    private TableItem tableItem_1;
    private SashForm graphTapHolder;
    MenuItem mntmClose;
    MenuItem mntmCloseAll;

    /**
     * Launch the application.
     *
     * @param args
     */
    public static void main(String[] args) {

        try {
            //XMLWriter writer = new XMLWriter();
            window = new HomeGUI(); 		//start the project
            reader = new XMLReader();
            reader.readDefaultNode();
            
            display = Display.getDefault();
            window.open();
            window.eventLoop(display);
        } catch (Exception e) {
            displayError(e.toString());
            e.printStackTrace();
            HomeGUI.closeMain(shell);
            HomeGUI.main(null);
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

    public  void eventLoop(Display display) {
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

    public  Shell getShell(Display display){
    	/*
    	 * This method is for testing purpose
    	 */
    	createContents();
    	return shell;
    }
    
    /**
     * Create contents of the window.
     */
    public void createContents() {

        reader = new XMLReader();
        reader.readWorkspaces();
        //PropertyFile.filePath = StaticData.rootPathName;//update cuureent workspace 

        shell = new Shell(display);
        shell.setBounds(0, 0, screen.width, screen.height - 20);
        com.project.traceability.common.Dimension.toCenter(shell);
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
        sidebarSF.setWeights(new int[]{1, 5});
       

        
        tabFolder = new CTabFolder(workSF, SWT.BORDER | SWT.CLOSE);			//new CTabFolder to show opened files and compared results
        tabFolder.setData("WorkSpace");
        tabFolder.setMinimizeVisible(true);
        tabFolder.setMaximizeVisible(true);
        //final ToolBar bar = new ToolBar(tabFolder, SWT.HORIZONTAL);
//        graphTab = new CTabFolder(workSF, SWT.BORDER | SWT.NONE);		//CTabFolder for visualization
//        graphTab.setData("Graph");
//        graphTab.setMinimizeVisible(true);
//        graphTab.setMaximizeVisible(true);

        composite = new Composite(tabFolder, SWT.NONE);
        composite.setLayout(new FillLayout());        

        //final ToolBar bar = new ToolBar(tabFolder, SWT.HORIZONTAL);
        graphTapHolder = new SashForm(workSF, SWT.HORIZONTAL | SWT.SMOOTH);
        graphTapHolder.setVisible(true);
        graphTab = new CTabFolder(graphTapHolder, SWT.BORDER | SWT.NONE);		//CTabFolder for visualization
        graphTab.setMinimizeVisible(true);
        graphTab.setMaximizeVisible(true);

        workSF.setWeights(new int[]{1, 1});
        
        graphComposite = new Composite(graphTab, SWT.NONE);
        graphComposite.setLayout(new FillLayout());
        
        graphComposite = new Composite(graphTab, SWT.NONE);
        graphComposite.setLayout(new FillLayout());
        composite = new Composite(tabFolder, SWT.NONE);
        composite.setLayout(new FillLayout());

        propertyTab = new CTabFolder(graphTapHolder, SWT.BORDER);		//CTabFolder for visualization
        propertyTab.setData("Property");
        propertyTab.setMinimizeVisible(true);
        propertyTab.setMaximizeVisible(true);
	
        tbtmPropertyInfos = new CTabItem(propertyTab, SWT.NONE);
        HomeGUI.tbtmPropertyInfos.setText("Information");
        graphTapHolder.setWeights(new int[]{749, 271});

        addEditMenuPopUpMenu();
        defineListeners();

        newTab.addCTabFolder2Listener(ctfCTF2L);			//for managing maximize, minimize and restore
        newTab.addMouseListener(ctfML);

        tabFolder.addCTabFolder2Listener(ctfCTF2L);
        tabFolder.addMouseListener(ctfML);

        graphTab.addCTabFolder2Listener(ctfCTF2L);
        graphTab.addMouseListener(ctfML);
        
        propertyTab.addCTabFolder2Listener(ctfCTF2L);
        propertyTab.addMouseListener(ctfML);

        graphtabItem = new CTabItem(graphTab, SWT.NONE);		//create CTabItem for visualization
        graphtabItem.setText("Graph");      
        
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

        tree = new Tree(newTab, SWT.BORDER | SWT.BORDER | SWT.V_SCROLL
                | SWT.H_SCROLL | SWT.MULTI);				//tree for all projects
        tree.setRedraw(true);

        //new FileHiearchyView();
        tree.setToolTipText(StaticData.workspace);

        tbtmProjects.setControl(projComposite);

        tree.addListener(SWT.Expand, new Listener() {

            @Override
            public void handleEvent(Event event) {
                changeStatus(event, "expand");
            }
        });

        tree.addListener(SWT.Collapse, new Listener() {

            @Override
            public void handleEvent(Event event) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                changeStatus(event, "unexpand");
            }
        });

        tree.addListener(SWT.Selection, new Listener() {		//add listener to the tree
            public void handleEvent(Event e) {
                String string = "";
                TreeItem[] selection = tree.getSelection();
                for (int i = 0; i < selection.length; i++) {
                    string += selection[i] + " ";
                    trtmNewTreeitem = selection[i];
                }
                if (selection.length > 0) {
                    string = selection[0].getText();
                }
                selectedFolder = string;
                projectPath = tree.getToolTipText() + File.separator + string + "";
                if (selection.length > 0 && selection[0].getItemCount() >= 2) {
                    hasTwoFiles = true;
                }
                if (selection.length > 0 && selection[0].getItemCount() > 3) {
                    hasThreeFiles = true;
                }
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
                TreeItem paret = trtmNewTreeitem.getParentItem();
                TreeItem topParent = null;
                
                string = selection[0].getText();
                if(paret != null)
                	 topParent = paret.getParentItem();

                if (paret != null && topParent == null) {
                    parent = paret.getText();
                } else if(topParent != null){
                    parent = topParent.getText() + File.separator + paret.getText();
                }

                if(!string.contains(".")){
                	if(!selection[0].getExpanded())
                		selection[0].setExpanded(true);
                	else
                		selection[0].setExpanded(false);
                	return;
                }
                if (selection[0].getItems().length == 0) {
                    //string = string.substring(10, string.length() - 2);
                    //parent = parent.substring(14, parent.length() - 2);
                    String projectName;
                    int count = 0;
                    int begin = StaticData.workspace.lastIndexOf(File.separator);
                    int end = StaticData.workspace.length();
                    String type= StaticData.workspace.substring(begin, end);
                    if(!type.equals(File.separator)){
                    	StaticData.workspace +=File.separator;
                    }
                    NewFileWindow.localFilePath = StaticData.workspace+ parent
                            + File.separator;

                    if (parent.contains(File.separator)) {
                        projectName = parent.substring(0, parent.lastIndexOf(File.separator));
                    } else {
                        projectName = parent;
                    }
                    
                    
                    NewFileWindow.selectedProjectPath = StaticData.workspace 
                            + projectName;

                    String fileName =NewFileWindow.localFilePath.concat(string);
                    if (!NewFileWindow.openedFiles.contains(fileName)) {
                        NewFileWindow.createTabLayout(string, true);

                    } else {
                        /*
                         file is opened in tab visiblle that tab
                         */

                        CTabItem items[] = tabFolder.getItems();

                        for (int i = 0; i < items.length; i++) {

                            String tips = items[i].getToolTipText();
                            if (tips != null && tips.equalsIgnoreCase(NewFileWindow.localFilePath + string)) {
                                tabFolder.setSelection(items[i]);
                                break;
                            }
                        }
                    }

                } else if (selection[0].getParent().equals(tree)) {
                    trtmNewTreeitem.setExpanded(true);
                }
            }
        });

        tbtmProjects.setControl(tree);			//add tree to CTabItem

        if (StaticData.workspace.equals("")) {
            tree.setVisible(false);
        } else {
            //adding project folders and files from workspace in project tab
            setUpNewProject();
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
        mntmProjctPath.setText("Switch Project Workspace");
        //  Create the first separator
        new MenuItem(menu_1, SWT.SEPARATOR);

        mntmClose = new MenuItem(menu_1, SWT.NONE);
        mntmClose.setEnabled(false);
        mntmClose.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
            	
            	String windw = HomeGUI.activeTab.get(true);
            	if(windw.contains(".txt") || windw.contains(".docs")){
                    
                    
                MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION
                | SWT.YES | SWT.NO);

                messageBox.setMessage("Do you really want to Save " + windw
                + " ?");
                File f = new File(windw);
                messageBox.setText("Saving " + f.getName());
                int response = messageBox.open();
                if (response == SWT.YES){
                     //save the text or doc file after modification 
                     new FileSave().saveFile();
                     
                }
               }
                
               CTabItem items[] = tabFolder.getItems();
               for(CTabItem item:items){
                    if(item.getToolTipText().equals(windw)){
                             //close visible file 
                             item.dispose();
                    }
                }
            }
        });

        mntmClose.setText("Close");

        mntmCloseAll = new MenuItem(menu_1, SWT.NONE);
        mntmCloseAll.setEnabled(false);
        mntmCloseAll.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {

                String windw = HomeGUI.activeTab.get(true);
            	if(windw.contains(".txt") || windw.contains(".docs")){
                    
                    
                MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION
                | SWT.YES | SWT.NO);

                messageBox.setMessage("Do you really want to Save " + windw
                + " ?");
                File f = new File(windw);
                messageBox.setText("Saving " + f.getName());
                int response = messageBox.open();
                if (response == SWT.YES){
                     //save the text or doc file after modification 
                     new FileSave().saveFile();
                     
                }
               }
                
               CTabItem items[] = tabFolder.getItems();
               for(CTabItem item:items){
                    if(item.getToolTipText().equals(windw)){
                             //close visible file 
                             item.dispose();
                    }
                }
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
        for (String paths : list) {
            mntmWorkspace = new MenuItem(menu_5, SWT.NONE);
            final MenuItem temp = mntmWorkspace;
            mntmWorkspace.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    //load current location project files
                    String path = temp.getText().trim();
                    
                    if(!path.equals("")
                    		&& path.substring(path.length()-1,path.length()).equals(File.separator))
                    	path = path.substring(0, path.length()-1);
                    File f = new File(path);
                    if(!(f.isDirectory() && f.exists())){
                    	MessageBox box  = new MessageBox(shell,SWT.ERROR);
                    	box.setMessage("This is not a directory");
                    	box.setText("Path Invalid!");
                    	box.open();
                    	return ;
                    }
                    	
                    StaticData.workspace = path;
                    XMLWriter xMLWriter = XMLWriter.getXMLWriterInstance("nothing");
                    xMLWriter.changeCurrnntWorkspaceVale(path);
                    //writer.modifyStatus(false, PropertyFile.filePath);

                    closeMain(shell);
                    HomeGUI.main(null);
                }
            });
            mntmWorkspace.setText(paths);
        }
        mntmWorkspace = new MenuItem(menu_5, SWT.NONE);
        mntmWorkspace.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                int x = JOptionPane.showConfirmDialog(null, "Do you want to change your current workspace?\n" + StaticData.workspace, "Worspace Confirmation",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (x == JOptionPane.YES_OPTION) {
                    DirectoryDialog dialog = new DirectoryDialog(shell);
                    dialog.setFilterPath("c:\\"); // Windows specific
                    PropertyFile.filePath = dialog.open();

                    XMLWriter writer = null;
                    if (writer == null) {
                        writer = XMLWriter.getXMLWriterInstance();
                    }

                    if (!StaticData.workspace.equals(PropertyFile.filePath)) {
                        StaticData.workspace = PropertyFile.filePath;
                        writer.createWorkspaceNode(StaticData.workspace, "true");
                        shell.dispose();
                        //StartUpProject bar =  new StartUpProject();
                        StartUpProject.main(null);
                    }
                } else if (x == JOptionPane.NO_OPTION) {

                }
            }
        });
        mntmWorkspace.setText("Other...");

        MenuItem mntmRestart = new MenuItem(menu_1, SWT.NONE);
        mntmRestart.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                //restart functionality
                closeMain(shell);
                HomeGUI.main(null);
            }
        });
        mntmRestart.setText("Restart");

        MenuItem mntmRefresh = new MenuItem(menu_1, SWT.NONE);
        mntmRefresh.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                //referesh functionality
//                            closeMain(shell);
//                            HomeGUI.main(null);

                tree.removeAll();
                initWindow();
                setUpNewProject();
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
            	HomeGUI.isImport = true;
                ImportProjectWindow.copyingLocation = StaticData.workspace;
                ImportProjectWindow.main(null);

                closeMain(shell);
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

                closeMain(shell);
                System.exit(5);
            }
        });
        mntmExit.setText("Exit");
        //shiyam edited
        //MenuItem mntmView = new MenuItem(menu, SWT.CASCADE);
        //mntmView.setText("View");
        Menu editMenu = new Menu(menu);
        MenuItem editMenuItem = new MenuItem(menu, SWT.CASCADE);
        editMenuItem.setText("Edit");
        editMenuItem.setMenu(editMenu);

        MenuItem copy = new MenuItem(editMenu, SWT.NONE);
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

        MenuItem paste = new MenuItem(editMenu, SWT.NONE);
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

        MenuItem cut = new MenuItem(editMenu, SWT.NONE);
        cut.setText("Cut");
        cut.setAccelerator(SWT.CTRL | 'X');
        cut.setImage(new Image(shell.getDisplay(), FilePropertyName.IMAGE_PATH + "file_txt.png"));
        cut.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                /*
                 run how to paste the text
                 */
                NewFileWindow.codeText.cut();
            }
        });

        MenuItem redo = new MenuItem(editMenu, SWT.NONE);
        redo.setText("Redo");
        redo.setImage(new Image(shell.getDisplay(), FilePropertyName.IMAGE_PATH + "file_txt.png"));
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

        MenuItem undo = new MenuItem(editMenu, SWT.NONE);
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

        MenuItem selectAll = new MenuItem(editMenu, SWT.NONE);
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
                if (false) {
                    projectWindowItem.setSelection(false);
                    isSelectedProject = false;
                    newTab.setVisible(false);
                } else {
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
                if (isSelectedCompare) {
                    isSelectedCompare = false;
                    CompareWindowItem.setSelection(isSelectedCompare);
                    tabFolder.setVisible(false);
                } else {
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
                if (isSelectedCompare) {
                    GraphWindowItem.setSelection(false);
                    graphTab.setVisible(false);
                    isSelectedGraph = false;
                } else {
                    //open the compare window
                    isSelectedGraph = true;
                    GraphWindowItem.setSelection(isSelectedGraph);
                    graphTab.setVisible(true);
                }
            }
        });

        tabFolder.addListener(SWT.MouseHover, new Listener() {
            public void handleEvent(Event event) {
                Point pt = new Point(event.x, event.y);
                CTabItem item = HomeGUI.tabFolder.getItem(pt);
                XMLWriter writer = XMLWriter.getXMLWriterInstance();
                if (item != null) {
                    
                 //   String itemTitle = item.getText();
                    if(HomeGUI.activeTab != null){
                        String itemTitle = item.getText();
                        if(!itemTitle.contains("Compare")){
                             mntmClose.setEnabled(true);
                             mntmCloseAll.setEnabled(true);
                             HomeGUI.activeTab.put(true, item.getToolTipText());
                             String filePath = item.getToolTipText();
                             writer.setVisibleFilePath(StaticData.workspace, filePath);
                        }else{
                        	mntmClose.setEnabled(false);
                        	mntmCloseAll.setEnabled(false);
                        }
                    }

                   
                } else {
                    writer.setVisibleFilePath(StaticData.workspace, "");
                }
            }
        });
        final TreeEditor editor = new TreeEditor(tree);
        editor.horizontalAlignment = SWT.LEFT;
        editor.grabHorizontal = true;
        tree.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent event) {
                if (event.keyCode == SWT.F2 && tree.getSelectionCount() == 1) {
                    final TreeItem item = tree.getSelection()[0];

                    final Text text = new Text(tree, SWT.NONE);
                    text.setText(item.getText());
                    text.selectAll();
                    text.setFocus();

                    text.addFocusListener(new FocusAdapter() {
                        public void focusLost(FocusEvent event) {
                            item.setText(text.getText());
                            text.dispose();
                        }
                    });

                    text.addKeyListener(new KeyAdapter() {
                        public void keyPressed(KeyEvent event) {
                            switch (event.keyCode) {
                                case SWT.CR:
                                    item.setText(text.getText());

                                case SWT.ESC:
                                    text.dispose();
                                    break;
                            }
                        }
                    });
                    editor.setEditor(text, item);
                }
            }
        });

        MenuItem mntmHelp = new MenuItem(menu, SWT.CASCADE);
        mntmHelp.setText("Help");

        Menu menu_3 = new Menu(mntmHelp);
        mntmHelp.setMenu(menu_3);

        initWindow();
    }

    private void defineListeners() {
        ctfCTF2L = new CTabFolder2Adapter() {
            public void close(CTabFolderEvent e) {
                e.doit = false;
                CTabFolder ctf = (CTabFolder) e.getSource();
                String ctfname = (String) ctf.getData();
                CTabItem item = ctf.getSelection();
                
                if(item != null)
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
                } else {
                    sidebarSF.setMaximizedControl(ctf);
                }

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
                } // if restoring from minimized state
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

        TreeItem[] selection = tree.getSelection();
        TreeItem paret = trtmNewTreeitem.getParentItem();
        TreeItem topParent = null;
        String parent = "";
                
        string = selection[0].getText();
        if(paret != null)
                topParent = paret.getParentItem();

         if (paret != null && topParent == null) {
                    parent = paret.getText();
         } else if(topParent != null){
                    parent = topParent.getText() + File.separator + paret.getText();
          }

        MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION
                | SWT.YES | SWT.NO);

        File filePath = new File(projectPath);
        if (!filePath.isDirectory()) {
            projectPath = StaticData.workspace + File.separator + parent + File.separator
                    + selection[0].getText();
        }
        messageBox.setMessage("Do you really want to delete " + projectPath
                + " ?");
        messageBox.setText("Deleting " + projectPath);
        int response = messageBox.open();
        if (response == SWT.YES) {

            if (trtmNewTreeitem.getText().equals(FilePropertyName.XML)
                    || trtmNewTreeitem.getText().equals(FilePropertyName.UML)
                    || trtmNewTreeitem.getText().equals(FilePropertyName.PROPERTY)
                    || trtmNewTreeitem.getText().equals(FilePropertyName.SOURCE_CODE)
                    || trtmNewTreeitem.getText().equals(FilePropertyName.REQUIREMENT)) {

                messageBox = new MessageBox(shell, SWT.ICON_ERROR
                        | SWT.ERROR_CANNOT_GET_SELECTION);
                messageBox.setMessage("File Delete Error\n" + "Those directory can not delete");
                messageBox.setText("Error");
                messageBox.open();
                return;
            }
            File file = new File(projectPath);
            try {
                FilePropertyName.delete(file);
                ErrorFinder.checkEachProject();
            } catch (IOException e) {
                messageBox = new MessageBox(shell, SWT.ICON_ERROR
                        | SWT.ERROR_IO);
                messageBox.setMessage("File Delete Error\n" + e.toString());
                messageBox.setText("Error");
                messageBox.open();
            } catch (Exception e) {
                messageBox = new MessageBox(shell, SWT.ICON_ERROR
                        | SWT.ERROR_IO);
                messageBox.setMessage("File Delete Error \n" + e.toString());
                messageBox.setText("Error");
                messageBox.open();
            }
            HomeGUI.trtmNewTreeitem.dispose();
        }
        projComposite.layout();
    }

    public void addEditMenuPopUpMenu() {
        Menu popupMenu = new Menu(workSF);
        MenuItem cutItem = new MenuItem(popupMenu, SWT.CASCADE);
        cutItem.setText("Cut");

        MenuItem copyItem = new MenuItem(popupMenu, SWT.CASCADE);
        copyItem.setText("Copy");

        MenuItem pasteItem = new MenuItem(popupMenu, SWT.CASCADE);
        pasteItem.setText("Paste");

        MenuItem selectAllItem = new MenuItem(popupMenu, SWT.CASCADE);
        selectAllItem.setText("Select All");

        MenuItem refreshItem = new MenuItem(popupMenu, SWT.CASCADE);
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
                initWindow();
                setUpNewProject();
            }
        });
        refreshItem.setText("Refresh");

        MenuItem projectItem = new MenuItem(popupMenu, SWT.CASCADE);
        projectItem.setText("Project");
        Boolean notProject = false;
        String name = trtmNewTreeitem.getText();
        if (name.equals(FilePropertyName.UML) || name.equals(FilePropertyName.XML)
                || name.equals(FilePropertyName.SOURCE_CODE) || name.equals(FilePropertyName.REQUIREMENT)
                || name.equals(FilePropertyName.PROPERTY)) {
            notProject = true;
        }
        File file = new File(tree.getToolTipText() + File.separator + name);
        projectItem.setEnabled((file.isDirectory() && !notProject));
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
                
                boolean isAllFileExists = 
                        ErrorFinder.lookUpForFiles(projectPath,
                                FilePropertyName.XML);
                string = string.substring(10, string.length() - 2);
                if(isAllFileExists){
                    window.open(string);
                }else{
                    displayError("Project does not have rquired files." +"\nHelp:=>"
                            + " press resolve on error folders");
                }
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

                try {
                    File file = new File(HomeGUI.projectPath);
                    String lastTxt[] = HomeGUI.projectPath.split("/");
                    String lastName = lastTxt[lastTxt.length - 1];
                    TreeItem item[] = tree.getSelection();
                    TreeItem parent = trtmNewTreeitem.getParentItem();
                    topParent = trtmNewTreeitem;
                    if (parent == null) {
                        trtmNewTreeitem = trtmNewTreeitem.getParentItem();
                        projectPath = HomeGUI.projectPath;
                    } else {
                        projectPath = StaticData.workspace + File.separator + parent.getText()
                                + File.separator + lastName;
                    }
                    System.out.println(trtmNewTreeitem + projectPath);
                    NewFileWindow newFileWin = new NewFileWindow();
                    newFileWin.open(trtmNewTreeitem, projectPath);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        fileItem.setText("File");

        Menu projectMenu = new Menu(popupMenu);
        projectItem.setMenu(projectMenu);

        MenuItem closeItem = new MenuItem(projectMenu, SWT.NONE);
        closeItem.addSelectionListener(new SelectionAdapter() {
                //close method go here 
        });
        closeItem.setText("Close");

        MenuItem resolveProItem = new MenuItem(projectMenu, SWT.NONE);
        resolveProItem.addSelectionListener(new SelectionAdapter() {

            //Resolve method going to here
            //if text file is not pop up file upload window
            //if source file is not pop up file upload window
            //if uml file is not pop up file upload window
            //if xml files are not pop up file upload window
//            TreeItem[] items = HomeGUI.tree.getSelection();
//            if(items.length>0){
//            	String projectName = items[0].getText();
//            }
//            
            //HomeGUI.main(null);
                    
            
            
        });
        resolveProItem.setText("Resolve");

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


    public static void setupProject(String graphType) {
        projectName = trtmNewTreeitem.getText();
        PropertyFile.setProjectName(projectName);
        PropertyFile.setGraphDbPath(projectPath + File.separator + FilePropertyName.PROPERTY + File.separator + projectName
                + ".graphdb");
        PropertyFile.setGeneratedGexfFilePath(projectPath + File.separator + FilePropertyName.PROPERTY + File.separator + projectName
                + ".gexf");
        PropertyFile.setRelationshipXMLPath(projectPath + File.separator + projectName + FilePropertyName.XML + File.separator
                + "Relations.xml");
        PropertyFile.setGraphType(graphType);
        System.out.println("Path: " + projectPath);
        System.out.println("DB Path: " + PropertyFile.getGraphDbPath());
        System.out.println("Graph Type: " + PropertyFile.getGraphType());
        ReadXML.initApp(projectPath, graphType);
    }

    public static void setUpNewProject() {

        tree.removeAll();
        File workspace = new File(StaticData.workspace);
        String projectName[] = workspace.list();

        String workSpcePath = workspace.getAbsolutePath();
        if (!(workSpcePath.lastIndexOf(File.separator) == workSpcePath.length() - 1)) {
            workSpcePath += File.separator;
        }
        createWrkspace();
        if (projectName != null && projectName.length > 0) {
            /*
             if project exists in given directory show that
             */

            for (String name : projectName) {
                String projectPath = workSpcePath + name;
                ImportProjectWindow window = new ImportProjectWindow();
                window.tree = tree;
                window.builProjectTree(projectPath);

            }
        }

        showExpandedProject();///show expanded projects ]
        
        ErrorFinder.checkEachProject();
    }

    private static void checkForError(){
        
        /*
        this method load all project in workspace and check weather 
        it has error or not
        */
        TreeItem items[] = tree.getItems();
        
        if(items != null && items.length>0){
            
            String wrkspace = HomeGUI.tree.getToolTipText();
            for(TreeItem item1:items){
                TreeItem subItem[] = item1.getItems();
                
                for(TreeItem itm:subItem){
                    String subFolder = itm.getText();
                    String root = subFolder + File.separator;
                    ErrorFinder.lookUpForFiles(root, subFolder);
                }
            }
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

    public void keyReleased(KeyEvent e) {

    }

    public void keyTyped(KeyEvent e) {

    }

    public static void closeMain(Shell shell) {
        shell.dispose();

        writer = XMLWriter.getXMLWriterInstance();
        List<String> files = new ArrayList<>(NewFileWindow.openedFiles);
        for (int i = 0; i < files.size(); i++) {
            writer.changeFileStatus(files.get(i), true);
        }
    }

    public static void showExpandedProject() {

        TreeItem item[] = tree.getItems();

        reader = new XMLReader();
        Map<String, Map<String, Boolean>> projct = reader.
                retriveExpenededProject(StaticData.workspace);

        for (TreeItem itemTemp : item) {

            String temp = itemTemp.getText();

            Map<String, Boolean> status = projct.get(temp);
            if (status != null && status.get(temp)) {
                itemTemp.setExpanded(status.get(temp));

                TreeItem subItems[] = itemTemp.getItems();
                for (TreeItem sub : subItems) {
                    String subTemp = sub.getText();

                    if (subTemp.equals(FilePropertyName.SOURCE_CODE)) {
                        sub.setExpanded(status.get(FilePropertyName.SOURCE_CODE));
                    } else if (subTemp.equals(FilePropertyName.REQUIREMENT)) {
                        sub.setExpanded(status.get(FilePropertyName.REQUIREMENT));
                    } else if (subTemp.equals(FilePropertyName.PROPERTY)) {
                        sub.setExpanded(status.get(FilePropertyName.PROPERTY));
                    } else if (subTemp.equals(FilePropertyName.UML)) {
                        sub.setExpanded(status.get(FilePropertyName.UML));
                    } else {
                        sub.setExpanded(status.get(FilePropertyName.XML));
                    }
                }

            }
//                System.out.println(temp);
//                if(projct.containsKey(temp)){
//                    itemTemp.setExpanded(true);
//               }
        }

    }

    public static void createWrkspace() {
        File wrkspace = new File(StaticData.workspace);
        if (!wrkspace.exists()) {
            wrkspace.mkdir();
        }
    }

    public static void changeStatus(Event event, String tpe) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        TreeItem item = (TreeItem) event.item;
        String temp = item.getText();
        String projectPath = StaticData.workspace;
        TreeItem parent = item.getParentItem();
        Boolean status;
        String type;
        if (parent == null) {
            //project is expanded 
            projectPath += File.separator + temp;
            type = "expanded";
            if (tpe.equals("expand")) {
                status = true;
            } else {
                status = false;
            }
        } else {
            //sub is expanded
            String projectName = parent.getText();
            projectPath += File.separator + projectName;
            type = item.getText();
            if (tpe.equals("expand")) {
                status = true;
            } else {
                status = false;
            }
        }

        writer = XMLWriter.getXMLWriterInstance();
        writer.setProjectStatus(projectPath, type, status);
    }

    private static void displayError(String msg) {
        MessageBox box = new MessageBox(shell, SWT.ICON_ERROR);
        box.setMessage(msg);
        box.open();
    }

    public static void initWindow() {
        /*
         initialize opened file when those were opened in tab
         */
        reader = new XMLReader();
        reader.readDefaultNode();

        Map<String, List<String>> files = reader.retriveOpenedFiles(StaticData.workspace);

        Set<String> set1 = files.keySet();
        List<String> projects = new ArrayList<>(set1);

        for (String name : projects) {
            List<String> filesLst = files.get(name);
            if (filesLst != null) {
                for (String filepath : filesLst) {

                    int start = filepath.lastIndexOf(File.separator) + 1;

                    NewFileWindow.localFilePath = filepath.substring(0, start);

                    int end;
                    end = filepath.length();
                    String fileName = filepath.substring(start, end);
                    File file = new File(filepath);
                    if (file.exists()) {

                        if (NewFileWindow.openedFiles != null
                                && NewFileWindow.openedFiles.contains(filepath)) {
                            NewFileWindow.createTabLayout(fileName, false);
                        } else if (NewFileWindow.openedFiles == null) {
                            NewFileWindow.createTabLayout(fileName, false);
                        }
//                                }else if(NewFileWindow.openedFiles.contains(filepath)){
//                                    try{
//                                        NewFileWindow.openFile(filepath);
//                                    }catch(FileNotFoundException e){
//                                        displayError(e.toString());
//                                    }catch(IOException e){
//                                        displayError(e.toString());
//                                    }
//                                }
                    }
                }
            }
        }

        String visiblePath = reader.getVisibleFile(StaticData.workspace);
        CTabItem items[] = tabFolder.getItems();
        for (int i = 0; i < items.length; i++) {
            String tips = items[i].getToolTipText();
            if (tips != null && tips.equalsIgnoreCase(visiblePath)) {

                tabFolder.setSelection(items[i]);
            }
        }
    }
    
    private static void resolve(){
            ErrorFinder.setProjectFolderResolve(projectPath, topParent);
            
    }
}
