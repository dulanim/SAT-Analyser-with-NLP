package com.project.traceability.GUI;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.project.NLP.file.operations.FilePropertyName;
import com.project.traceability.common.Dimension;


public class ImportProjectWindow {

	protected static Shell shell;
	private Text txtProjectPath;
	private Text txtWrkspacePath;
	
	public static Tree tree;
	Button btnFinish;
	Display display;
	public static List<File> importFiles;
	public static String copyingLocation = "/home/shiyam/Desktop/copyingLocation";
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			
			importFiles = new ArrayList<>();
			ImportProjectWindow window = new ImportProjectWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(530, 537);
		shell.setText("Project Import Window");
		
                Dimension.toCenter(shell);//set the shell into center point 
		Group grpImportProject = new Group(shell, SWT.NONE);
		grpImportProject.setText("Import Project");
		grpImportProject.setBounds(10, 36, 493, 162);
		

		final Button btnWrkspacePath = new Button(grpImportProject, SWT.NONE);
		btnWrkspacePath.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				
				DirectoryDialog dialog = new DirectoryDialog(shell);
			    String str = dialog.open();
			    txtWrkspacePath.setText(str);
			    
				String wrkspacePath = str + File.separator;//"/home/shiyam/Desktop/SatWrks/";
				
				addProjects(wrkspacePath);
				
			}
		});
		btnWrkspacePath.setBounds(372, 66, 91, 29);
		btnWrkspacePath.setText("Browse");
		
		Label lblProjectRootPath = new Label(grpImportProject, SWT.NONE);
		lblProjectRootPath.setBounds(0, 33, 152, 27);
		lblProjectRootPath.setText("Project Root Path");
		
		txtProjectPath = new Text(grpImportProject, SWT.BORDER);
		txtProjectPath.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				
				if(e.keyCode == 10){
					//enter key pressed
					String projectPath = txtProjectPath.getText().trim();
					
					if(!projectPath.equals("")){
						builProjectTree(projectPath);
					}else{
						String content = "Given Path is Invaild";
						String title = "Information For Path Selection";
						showErrorMessage(content, title);
						
					}
				}
			}
		});
		txtProjectPath.setBounds(151, 33, 213, 27);
		
		final Button btnProjectPath = new Button(grpImportProject, SWT.NONE);
		btnProjectPath.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				importFiles.clear();
				tree.removeAll();
				
				DirectoryDialog dialog = new DirectoryDialog(shell);
			    String str = dialog.open();
			    txtProjectPath.setText(str);
			    if((!str.equals("") || !str.equals(null))){
			    	builProjectTree(str);
			    }
			}
		});
		btnProjectPath.setBounds(372, 31, 91, 29);
		btnProjectPath.setText("Browse");
		
		Label lblProjectWorkspace = new Label(grpImportProject, SWT.NONE);
		lblProjectWorkspace.setBounds(0, 76, 152, 17);
		lblProjectWorkspace.setText("Project Workspace");
		
		txtWrkspacePath = new Text(grpImportProject, SWT.BORDER);
		txtWrkspacePath.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				
				if(e.keyCode == 10){
					//enter key pressed
					String projectPath = txtWrkspacePath.getText().trim();
					
					if(!projectPath.equals("")){
						addProjects(projectPath);
					}else{
						String content = "Given Path is Invaild";
						String title = "Information For Path Selection";
						showErrorMessage(content, title);
						
					}
				}
			}
		});
		txtWrkspacePath.setBounds(151, 66, 213, 27);
		
		
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(10, 204, 493, 286);
		
		final Button btnRadioButton = new Button(grpImportProject, SWT.RADIO);
		btnRadioButton.setBounds(151, 112, 114, 24);
		btnRadioButton.setText("Project");
		btnRadioButton.setSelection(true);
		txtProjectPath.setEnabled(true);
		btnProjectPath.setEnabled(true);
		txtWrkspacePath.setEnabled(false);
		btnWrkspacePath.setEnabled(false);
		btnRadioButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if(btnRadioButton.getSelection()){
					txtProjectPath.setEnabled(true);
					btnProjectPath.setEnabled(true);
					txtWrkspacePath.setEnabled(false);
					btnWrkspacePath.setEnabled(false);
				}
			}
		});
		
		final Button btnWorkspace = new Button(grpImportProject, SWT.RADIO);
		btnWorkspace.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if(btnWorkspace.getSelection()){
					txtProjectPath.setEnabled(false);
					btnProjectPath.setEnabled(false);
					txtWrkspacePath.setEnabled(true);
					btnWrkspacePath.setEnabled(true);
				}
			}
		});
		btnWorkspace.setBounds(10, 112, 114, 24);
		btnWorkspace.setText("workspace");
		
		final Button btnDeleteThis = new Button(composite, SWT.NONE);
		btnDeleteThis.setToolTipText("\"Selected Project will be deleted when press\"");
		btnDeleteThis.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				TreeItem selection[] = tree.getSelection();
				for(int i=0;i<selection.length;i++){
					String selectedString = selection[i].toString();
					int startIndex = selectedString.indexOf("(")+1;
					int endIndex = selectedString.indexOf(")");
					String rmvString = selectedString.substring(startIndex,endIndex);
					
					File file = new File(rmvString);
					importFiles.remove(file);
					selection[i].dispose();
				}
				
				if(!(importFiles.size() >0))
					btnFinish.setEnabled(false);
			}
		});
		btnDeleteThis.setEnabled(false);
		btnDeleteThis.setBounds(10, 244, 91, 29);
		btnDeleteThis.setText("Delete This");
	
		tree = new Tree(composite, SWT.BORDER);
		tree.setBounds(10, 39, 460, 199);
		tree.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				TreeItem selection[] = tree.getSelection();
				btnDeleteThis.setEnabled(false);
				for(int i=0;i<selection.length;i++){
					String selectedStr  = selection[i].toString();
					if(selectedStr.contains("/") || selectedStr.contains("(")){
						btnDeleteThis.setEnabled(true);
					}
				}
			}
		});
		btnDeleteThis.setEnabled(false);
		btnFinish = new Button(composite, SWT.NONE);
		btnFinish.setToolTipText("\"Shown projects will be copied to given location location is shown above \"");
		btnFinish.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox messageBox = new MessageBox(shell, SWT.ERROR
			              | SWT.OK);
				if(copyingLocation.equals("")){
					//if not exists show message or pop up to user that project is not valid 
					
					 String content = "First Select the Project Workspace Location"
				     		+ "\nAfter that import projects folders";
					 String title = "Information For Selection";
					 showErrorMessage(content, title);
				}else{
						try {
							
							List<File> files = importFiles;
							String path = copyingLocation;
							
                                                        if(!(path.indexOf("/") == path.length()-1)){
                                                            path += File.separator;
                                                        }
							File dest = new File(path);
							String projects[] = dest.list();
							
							List<String> projList = Arrays.asList(projects);
							for(File file : files) {
								String projectName = getSubFolder(file.getAbsolutePath());
								if(!projList.contains(projectName)){
								    
									dest = new File(path + projectName);
									dest.mkdir();
                                                                        FilePropertyName.copyFolder(file, dest);
								}else{
									messageBox = new MessageBox(shell, SWT.ICON_QUESTION
								            | SWT.YES | SWT.NO);
								    messageBox.setMessage("Do you really want to Replace the "+
								            	projectName + "?");
								    messageBox.setText("Already Project Exists");
								    int response = messageBox.open();
								    if(response == SWT.YES){
								    	FilePropertyName.copyFolder(file, dest);
								    }
								}
							}
							
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							 String content = e1.toString();
							 String title = "Information For Selection";
							 showErrorMessage(content, title);

							e1.printStackTrace();
						}
                      shell.close();
					}
				}
			
		});
		btnFinish.setEnabled(false);
		btnFinish.setBounds(392, 240, 91, 29);
		btnFinish.setText("Finish");
		
		
		
		Label lblProjectWillBe = new Label(composite, SWT.NONE);
		lblProjectWillBe.setBounds(10, 10, 171, 17);
		lblProjectWillBe.setText("Projects will be copied to ");
		
		Label lblCopyLocation = new Label(composite, SWT.NONE);
		lblCopyLocation.setToolTipText("Selected Projects Will be Copied to " + copyingLocation);
		lblCopyLocation.setBounds(187, 10, 283, 17);
		
		Button btnCancel = new Button(composite, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				MessageBox box = new MessageBox(shell, SWT.ICON_QUESTION|
						SWT.YES|SWT.NO);
				box.setMessage("Do you want to close this window?");
				int result = box.open();
				if(result == SWT.YES){
					ImportProjectWindow.shell.close();
					HomeGUI.shell.setVisible(true);
				}
			}
		});
		btnCancel.setBounds(115, 244, 91, 29);
		btnCancel.setText("Cancel");
		
		if(copyingLocation.equals("")){
			lblCopyLocation.setText("Default");
		}else{
			lblCopyLocation.setText(copyingLocation);
		}
		
		
		
		Label lblSelectADirectory = new Label(shell, SWT.NONE);
		lblSelectADirectory.setBounds(10, 10, 490, 29);
		lblSelectADirectory.setText("Select a directory to search for existing SAT projects.");

	}
      
	public boolean isProjectExists(String projectPath){
		
		boolean isExists = false;
		int count = 0;
		if(!projectPath.equals("")){
			File file = new File(projectPath);
			File fileList[] = file.listFiles();
			
			for(int i=0;fileList != null 
					&& i<fileList.length;i++){
				
				String subFolderName = getSubFolder(fileList[i].getAbsolutePath());
				File f = fileList[i].getAbsoluteFile();
				if(count <= 4 && f.isDirectory()){
					if(subFolderName.contains(FilePropertyName.XML)){
						count++;
					}else if(subFolderName.contains(FilePropertyName.UML)){
						count++;
					}else if(subFolderName.contains(FilePropertyName.REQUIREMENT)){
						count++;
					}else if(subFolderName.contains(FilePropertyName.SOURCE_CODE)){
						count++;
					}else if(subFolderName.contains(FilePropertyName.PROPERTY)){
						count++;
					}
				}else{
					break;
				}
			}
		}
		
		if(count == 5)
			isExists = true;
		return isExists;
	}
	
	public void builProjectTree(String projectPath){
            
		/*
                 *@param projectPath /home/shiyam/wrkspace/Anduril
		 * this method if the project is in given project path
		 * build the project hiearchy tree 
		 */
		if(isProjectExists(projectPath)){
			//if project exists build the tree
			TreeItem root;
			
			File file = new File(projectPath);
			File listFile[] = file.listFiles();
			if(listFile.length>0){
				root = new TreeItem(tree, SWT.NONE);
				String projectName = getSubFolder(file.getAbsolutePath());
                                
                                if(btnFinish != null ){
                                    //this is for only importwindow
                                    root.setText(projectName + "(" + file.getAbsolutePath() + ")");
                                }else{
                                    /*
                                    other windows communicate with this method 
                                    at that time this works
                                    */
                                    root.setText(projectName);
                                }
                                root.setImage(new Image(display,
                                FilePropertyName.IMAGE_PATH.concat("folder_root.gif")));
                                
                                if(importFiles != null)
                                    importFiles.add(file);
                       			}else{
									root = new TreeItem(tree, SWT.NONE);
									root.setText("DefaultProject");
                       			}
			for(int i =0;i<listFile.length;i++){
				String subFolderName = getSubFolder(listFile[i].getAbsolutePath());
				TreeItem child = new TreeItem(root, SWT.NONE);
				child.setText(subFolderName);
				child.setImage(new Image(display,
                                FilePropertyName.IMAGE_PATH.concat("folder_root.gif")));
				
				File f = new File(listFile[i].getAbsolutePath());
				File files[] = f.listFiles();
				
				for(int j=0;j<files.length;j++){
					TreeItem child_Child = new TreeItem(child, SWT.NONE);
					String fileName = getSubFolder(files[j].getAbsolutePath());
					child_Child.setText(fileName);
					
					if(fileName.contains(".java")){
						child_Child.setImage(new Image(display,
	                        FilePropertyName.IMAGE_PATH.concat("file_java.png")));
					}else if(fileName.contains(".xml")){
						child_Child.setImage(new Image(display,
		                        FilePropertyName.IMAGE_PATH.concat("file_xml.png")));
					}else if(fileName.contains(".txt")){
						child_Child.setImage(new Image(display,
		                        FilePropertyName.IMAGE_PATH.concat("file_txt.png")));
					}else{
						child_Child.setImage(new Image(display,
		                        FilePropertyName.IMAGE_PATH.concat("file_txt_dums.png")));
					}
				}
			}
                        if(btnFinish != null)
                            btnFinish.setEnabled(true);
		}else{
			//if not exists show message or pop up to user that project is not valid 
			 if(!HomeGUI.isImport || btnFinish != null || importFiles != null){
                            String content = "Given Path does not have required file/ folder";
                            String title = "Information For Selection";
                            showErrorMessage(content, title);
             }
		}
	}
	
	private String getSubFolder(String path){
		String subFolderName = "DefaultPath";                
		if(path.contains("/")){ //For linux
			subFolderName = path.substring(path.lastIndexOf("/") + 1, 
					path.length());
		}else if(path.contains("\\")){ //For Windows
                    subFolderName = path.substring(path.lastIndexOf("\\") + 1, 
					path.length());
       }
		
		return subFolderName;
	}
	
	private void showErrorMessage(String content,String title){

		if(shell == null || shell.isDisposed())
			return ;
        MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR);
                      if(content.equals(""))
                          content = "Error In Project";
                      if(title.equals(""))
                          title = "Error";
	     messageBox.setMessage(content);
	     messageBox.setText(title);
	     messageBox.open();
	}
	
	private void addProjects(String wrkspacePath){
		
		if(!wrkspacePath.equals("")){
			File wrkspace = new File(wrkspacePath);
			String projects[] = wrkspace.list();
			importFiles.clear();
			tree.removeAll();
			for(int i=0;i<projects.length;i++){
				String projectName = projects[i];
				String projectPath =wrkspacePath +  projectName;
				builProjectTree(projectPath);
			}
		}else{
			String content = "Given Path is Invaild";
			String title = "Information For Path Selection";
			showErrorMessage(content, title);
		}
	}
}
