package com.project.traceability.GUI;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.project.NLP.file.operations.FilePropertyName;
import com.project.NLP.file.operations.JavaViewer;
import com.project.NLP.file.operations.XMLFileStyleFormat;
import com.project.text.undoredo.UndoRedoImpl;
import com.project.traceability.common.PropertyFile;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.util.HashSet;
import java.util.Set;
import org.eclipse.swt.custom.CTabFolderAdapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.TableItem;

public class NewFileWindow {
        public static StyledText codeText;
	static Shell shell;
	private Text text;
	static Path path;
	public static String localFilePath;
	static String[] selectedFiles;
        String formats[] = { "*.uml*;*.xmi*;*.mdj*"};
	static String textString;
	FileDialog fileDialog;
	private Text text_1;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			NewFileWindow window = new NewFileWindow();
			window.open();
			window.eventLoop(Display.getDefault());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void eventLoop(Display display) {
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}


	/**
	 * Open the window.
	 */
	public Shell open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();		
		return shell;		
	}
	
	public Shell open(TreeItem item, String path) {
		Display display = Display.getDefault();
		HomeGUI.trtmNewTreeitem = item;
		HomeGUI.projectPath = path;
		createContents();
		shell.open();
		shell.layout();		
		return shell;		
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 545);
		shell.setText("New File");
		center(shell);
                
		final Tree tree = new Tree(shell, SWT.BORDER);
		tree.setBounds(12, 57, 412, 338);
		System.out.println(HomeGUI.projectPath + "KKKKKKKK");
		text_1 = new Text(shell, SWT.BORDER);
		if (HomeGUI.projectPath != null)
			text_1.setText(HomeGUI.projectPath);
		text_1.setBounds(12, 30, 412, 21);

		Label lblParentFolderPath = new Label(shell, SWT.NONE);
		lblParentFolderPath.setBounds(12, 10, 104, 15);
		lblParentFolderPath.setText("Parent folder path");

		File projectFile = new File(PropertyFile.filePath);
		projectFile.mkdir();
		ArrayList<String> projectFiles = new ArrayList<String>(
				Arrays.asList(projectFile.list()));

		for (int i = 0; i < projectFiles.size(); i++) {
			TreeItem trtmNewTreeitem = new TreeItem(tree, SWT.NONE);
			trtmNewTreeitem.setText(projectFiles.get(i));
		}

		

		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shell.close();
			}
		});
		btnCancel.setBounds(320, 472, 49, 25);
		btnCancel.setText("Cancel");

		Label lblFileName = new Label(shell, SWT.NONE);
		lblFileName.setBounds(12, 418, 67, 15);
		lblFileName.setText("File Name :");

		text = new Text(shell, SWT.BORDER);
		text.setBounds(85, 415, 247, 21);

		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(shell, SWT.MULTI);
				fileDialog.setText("Open");
                                if(HomeGUI.isSelectionForUMLFile){
                                    fileDialog.setFilterExtensions(formats); // Windows
                                                                  // wild cards
                                }
				fileDialog.setFilterPath(PropertyFile.xmlFilePath);
				localFilePath = fileDialog.open();
                                //StaticData.umlFilePath = localFilePath;
				localFilePath = localFilePath.replace(Paths.get(localFilePath)
						.getFileName().toString(), "");
				selectedFiles = fileDialog.getFileNames();
				for (int k = 0; k < selectedFiles.length; k++) {
					text.append(selectedFiles[k] + " , ");
					path = Paths.get(localFilePath + selectedFiles[k]);
                                        String saveLocation = HomeGUI.projectPath;
					Path trgt = Paths.get(saveLocation); 
                                        String fileName = selectedFiles[k];
					if (localFilePath != null) {
                                            //Path target = FilePropertyName.getPath(trgt, fileName);
						try {
							Files.copy(path,
									trgt.resolve(path.getFileName()),
									REPLACE_EXISTING);
						} catch (IOException e1) {							
							e1.printStackTrace();
						}
					}
				
                                 
			}
                     }
		});
		btnNewButton.setBounds(349, 413, 75, 25);
		btnNewButton.setText("Browse");

		Button btnSave = new Button(shell, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
                             
				for (int j = 0; j < selectedFiles.length; j++) {
                                        TreeItem treeItm = null;
                                        if(HomeGUI.trtmNewTreeitem == null){
                                            treeItm = HomeGUI.topParent;
                                        }else{
                                            treeItm = HomeGUI.trtmNewTreeitem;
                                        }
					TreeItem treeItem = new TreeItem(
							treeItm, SWT.NONE);
					treeItem.setText(selectedFiles[j]);
                                        treeItem.setImage(new Image(HomeGUI.display,FilePropertyName.IMAGE_PATH + "file_txt.png"));
                                        
                                        if(HomeGUI.trtmNewTreeitem == null)
                                            treeItm.setExpanded(true);
                                        else
                                            treeItm.setExpanded(true);
					//HomeGUI.treeViewer.refresh(HomeGUI.tree);
					HomeGUI.projComposite.layout();
				}
				shell.close();
				if(selectedFiles.length >= 2)
					HomeGUI.hasTwoFiles = true;
				if(selectedFiles.length == 3)
					HomeGUI.hasThreeFiles = true;
                                
                               
				openFiles();
                                //FilePropertyName.copy(fileDialog, localFilePath, textString);
                               
			}
                        
		});
		btnSave.setBounds(375, 472, 49, 25);
		btnSave.setText("Open");

	}

	public static void openFiles() {
		for (int j = 0; j < selectedFiles.length; j++) {
			createTabLayout(selectedFiles[j]);
		}
	}

	public static void createTabLayout(String fileName) {
		HomeGUI.tabFolder.setVisible(true);

		final CTabItem tabItem = new CTabItem(HomeGUI.tabFolder, SWT.NONE);
		tabItem.setText(fileName);
                
		Composite composite = new Composite(HomeGUI.tabFolder, SWT.NONE);

		composite.setLayout(new GridLayout(1, false));
		composite.addControlListener(new ControlListener() {

			@Override
			public void controlResized(ControlEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void controlMoved(ControlEvent arg0) {

			}
		});

		GridData spec = new GridData();
		spec.horizontalAlignment = GridData.FILL;
		spec.grabExcessHorizontalSpace = true;
		spec.verticalAlignment = GridData.FILL;
		spec.grabExcessVerticalSpace = true;
                
                
		composite.setLayoutData(spec);

		codeText = new StyledText(composite, SWT.BORDER | SWT.MULTI
				| SWT.V_SCROLL | SWT.H_SCROLL);
		codeText.setLayoutData(spec);
                codeText.selectAll();
                codeText.setAlwaysShowScrollBars(true);
                codeText.setEditable(false);
		File file = new File(localFilePath + fileName);
                tabItem.setToolTipText(file.getPath());
		try {
			FileInputStream stream = new FileInputStream(file.getPath());
			try {
				Reader in = new BufferedReader(new InputStreamReader(stream));
				char[] readBuffer = new char[2048];
				StringBuffer buffer = new StringBuffer((int) file.length());
				int n;
				while ((n = in.read(readBuffer)) > 0) {
					buffer.append(readBuffer, 0, n);
				}
				textString = buffer.toString();

				stream.close();
			} catch (IOException e) {
				String message = "Err_file_io";
				displayError(message);
				return;
			}
		} catch (FileNotFoundException e) {
			String message = "Err_not_found";
			displayError(message);
			return;
		}
                /*
                set up opened files on CTab
                */
                
		final Display display = codeText.getDisplay();
		// display.asyncExec(new Runnable() {
		// public void run() {
		codeText.setText(textString);
                XMLFileStyleFormat xmlFileStyle;
                JavaViewer viewer;
                /*
                    setting respected style for each files
                */
                if(file.getPath().contains("xml")){
                    xmlFileStyle = new XMLFileStyleFormat(textString,codeText);
                }else if(file.getPath().contains("java")){
                    viewer = new JavaViewer();
                    viewer.read(file.getPath());
                }else{
                    xmlFileStyle = new XMLFileStyleFormat(textString,codeText);
                    codeText.setEditable(true);
                }
		new UndoRedoImpl(codeText);
		composite.setData(codeText);
		tabItem.setControl(composite);
                tabItem.setToolTipText(file.getPath());
                final CTabItem tabItemFinal = tabItem;
                
                // Add an event listener to write the selected tab to stdout
                // Add a listener to get the close button on each tab
                HomeGUI.tabFolder.addCTabFolderListener(new CTabFolderAdapter() {
                  public void itemClosed(CTabFolderEvent event) {
                      if (event.item.equals(tabItem)) {
                            event.doit = true;
                    }
                  }
                  
                  public void itemSelected(CTabFolderEvent event){
                      System.out.println("Tab CLicked");
                  }
                  //public void item
                });
                
                HomeGUI.tabFolder.addMouseMoveListener(new MouseMoveListener() {
                    public void mouseMove(MouseEvent arg0) {
                        Point point = new Point(arg0.x, arg0.y);
                        CTabItem[] items = HomeGUI.tabFolder.getItems();
                        for (int i = 0; i < items.length;i++) {
                            if (items[i].getBounds().contains(point)){
                                //lineFolder.setSelection(items[i]);
                            }
                        }
                    }
                });
                HomeGUI.tabFolder.addListener(SWT.MouseHover, new Listener() {
                    public void handleEvent(Event event) {
                      Point pt = new Point(event.x, event.y);
                      CTabItem item = HomeGUI.tabFolder.getItem(pt);
                      if(item != null)
                        HomeGUI.activeTab.put(true,item.getToolTipText().toString());
                    }
                });
	}

	public void center(Shell shell) {

		Rectangle bds = shell.getDisplay().getBounds();

		Point p = shell.getSize();
		shell.setFullScreen(true);
		int nLeft = (bds.width - p.x) / 2;
		int nTop = (bds.height - p.y) / 2;

		shell.setBounds(nLeft, nTop, p.x, p.y);
	}

	static void displayError(String msg) {
		MessageBox box = new MessageBox(shell, SWT.ICON_ERROR);
		box.setMessage(msg);
		box.open();
	}
        
        
}
