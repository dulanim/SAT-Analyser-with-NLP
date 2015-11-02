package com.project.traceability.GUI;

import com.project.NLP.staticdata.FilePropertyName;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.project.NLP.staticdata.StaticData;
import com.project.property.config.xml.writer.XMLWriter;
import static com.project.traceability.GUI.HomeGUI.tree;
import static com.project.traceability.GUI.NewProjectWindow.projectPath;
import static com.project.traceability.GUI.NewProjectWindow.shell;
import static com.project.traceability.GUI.NewProjectWindow.trtmNewTreeitem;
import static com.project.traceability.GUI.WorkspaceSelectionWindow.window;
import com.project.traceability.common.PropertyFile;
import com.project.traceability.manager.RelationManager;
import javax.swing.JOptionPane;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.TreeItem;

public class ProjectCreateWindow {
        public static TreeItem trtmNewTreeitem;
        public FileDialog docsFileDialog;
	public FileDialog umlFileDialog;
	protected Shell shell;
	
	static String localFilePath;
	static String[] selectedFiles;
	static Path path;
        String formats[] = { "*.uml*;*.xmi*;*.mdj*"};
        String req_formats[] ={"*.docs*;*.txt*;*.mdj*"};
        FileDialog srcFileDialog;
	private Text txtProjectName;
	private Text txtRequirementPath;
	private Text txtUmlPath;
	private Text txtProjectPath;
	private Text txtCusersshiyamdocuments;
	

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			ProjectCreateWindow window = new ProjectCreateWindow();
                        
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
                Monitor primary = display.getPrimaryMonitor();
                Rectangle bounds = primary.getBounds();
                Rectangle rect = shell.getBounds();
    
                int x = bounds.x + (bounds.width - rect.width) / 2;
                int y = bounds.y + (bounds.height - rect.height) / 2;
                shell.setLocation(x, y);
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
		shell.setSize(587, 366);
		shell.setText("SWT Application");
		
		Group grpImportRequiredFiles = new Group(shell, SWT.NONE);
		grpImportRequiredFiles.setText("Import Required Files");
		grpImportRequiredFiles.setBounds(10, 104, 551, 181);
		
		Label lblNewLabel = new Label(grpImportRequiredFiles, SWT.NONE);
		lblNewLabel.setBounds(10, 40, 102, 15);
		lblNewLabel.setText("Requirement File");
		
		txtRequirementPath = new Text(grpImportRequiredFiles, SWT.BORDER);
		txtRequirementPath.setEnabled(false);
		txtRequirementPath.setEditable(false);
		txtRequirementPath.setText(FilePropertyName.default_requirement_doc_path);
		txtRequirementPath.setBounds(127, 37, 343, 21);
		
		final Button btnReqBrwse = new Button(grpImportRequiredFiles, SWT.NONE);
		btnReqBrwse.setEnabled(false);
		btnReqBrwse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				localFilePath = null;
				FileDialog fileDialog = new FileDialog(shell, SWT.MULTI);
				fileDialog.setText("Open");
                                fileDialog.setFilterExtensions(req_formats); // Windows           
				fileDialog.setFilterPath(PropertyFile.docsFilePath);
				localFilePath = fileDialog.open();
				docsFileDialog = fileDialog;
				
				if(localFilePath != null){
					PropertyFile.docsFilePath = localFilePath;
					txtRequirementPath.setText(PropertyFile.docsFilePath);
				}
				
			}
		});
		
		
		btnReqBrwse.setBounds(476, 35, 75, 25);
		btnReqBrwse.setText("Browse");
		
		Label lblDiagramFile = new Label(grpImportRequiredFiles, SWT.NONE);
		lblDiagramFile.setText("Design Diagram File");
		lblDiagramFile.setBounds(10, 81, 113, 15);
		
		txtUmlPath = new Text(grpImportRequiredFiles, SWT.BORDER);
		txtUmlPath.setText(FilePropertyName.default_uml_file_path);
		txtUmlPath.setEnabled(false);
		txtUmlPath.setEditable(false);
		txtUmlPath.setBounds(127, 78, 343, 21);
		
		final Button btnUmlBrwse = new Button(grpImportRequiredFiles, SWT.NONE);
		btnUmlBrwse.setEnabled(false);
		btnUmlBrwse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
			        
	                FileDialog fileDialog = new FileDialog(shell, SWT.MULTI);
					fileDialog.setText("Open");
	                fileDialog.setFilterExtensions(formats); // Windows           
					fileDialog.setFilterPath(PropertyFile.xmlFilePath);
					localFilePath = fileDialog.open();
	                StaticData.umlFilePath = localFilePath;
					localFilePath = localFilePath.replace(Paths.get(localFilePath)
							.getFileName().toString(), "");
					umlFileDialog = fileDialog;
					if(localFilePath != null){
						txtUmlPath.setText(StaticData.umlFilePath);
				}		
			}
		});
		btnUmlBrwse.setText("Browse");
		btnUmlBrwse.setBounds(476, 76, 75, 25);
		
		Label lblProjectPath = new Label(grpImportRequiredFiles, SWT.NONE);
		lblProjectPath.setText("Project Path");
		lblProjectPath.setBounds(10, 129, 102, 15);
		
		txtProjectPath = new Text(grpImportRequiredFiles, SWT.BORDER);
		txtProjectPath.setEnabled(false);
		txtProjectPath.setEditable(false);
		txtProjectPath.setBounds(127, 126, 343, 21);
		
		final Button btnSrcBrwse = new Button(grpImportRequiredFiles, SWT.NONE);
		btnSrcBrwse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
			}
		});
		btnSrcBrwse.setEnabled(false);
		btnSrcBrwse.setText("Browse");
		btnSrcBrwse.setBounds(476, 124, 75, 25);
		
		Label lblProjectName = new Label(shell, SWT.NONE);
		lblProjectName.setBounds(10, 45, 133, 21);
		lblProjectName.setText("Traceabilty Project Name");
		
                final Button btnFinish = new Button(shell, SWT.NONE);
		btnFinish.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
                            try{
                                if(!FilePropertyName.default_requirement_doc_path.equals(txtRequirementPath.getText())){
                                    copy(docsFileDialog,PropertyFile.docsFilePath,FilePropertyName.REQUIREMENT);
                                }
                                if(!FilePropertyName.default_uml_file_path.equals(txtUmlPath.getText())){
                                    copy(umlFileDialog,StaticData.umlFilePath,FilePropertyName.UML);
                                }
                                if(!FilePropertyName.default_java_project_path.equals(txtRequirementPath.getText())){
                                    //add copy files for source code 
                                    
                                }
				
				String projectName = txtProjectName.getText();
                                projectPath = PropertyFile.filePath  + File.separator;
                                PropertyFile.setProjectName(projectName);
                                PropertyFile.setGraphDbPath(projectPath + projectName + ".graphdb");
                                PropertyFile.setGeneratedGexfFilePath(projectPath+ projectName + ".gexf");
                                PropertyFile.setRelationshipXMLPath(projectPath + "Relations.xml");

                                shell.close();
                                HomeGUI.shell.setText("SAT- " + projectName);
                                HomeGUI.newTab.setVisible(true);
                                HomeGUI.tree.setVisible(true);

                               trtmNewTreeitem = new TreeItem(tree, SWT.NONE);
                               trtmNewTreeitem.setText(projectName);

                                File file = new File(projectPath);
                                file.mkdir();
                                FilePropertyName.addSubFolderIntoProject(file);
                                RelationManager.createXML(projectPath);
                                HomeGUI.setUpNewProject(projectPath,"Run Time");
				shell.dispose();
                            }catch(Exception e){
                                JOptionPane.showMessageDialog(null, e.toString(),"Error Message",JOptionPane.ERROR_MESSAGE);
                            }
			}
		});
		btnFinish.setImage(SWTResourceManager.getImage("/home/shiyam/Projects/FinalYear/Anduril/img/exact.jpg"));
		btnFinish.setBounds(486, 293, 75, 25);
		btnFinish.setText("Finish");
		btnFinish.setEnabled(false);
		final Label crctLabl = new Label(shell, SWT.NONE);
		crctLabl.setImage(SWTResourceManager.getImage("/home/shiyam/Projects/FinalYear/Anduril/img/violation.jpg"));
		crctLabl.setBounds(506, 42, 55, 24);
		
		txtProjectName = new Text(shell, SWT.BORDER);
                txtProjectName.setText("");
		txtProjectName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				
                                
                                PropertyFile.filePath = txtCusersshiyamdocuments.getText().trim();
                                
                                btnReqBrwse.setEnabled(true);
				btnUmlBrwse.setEnabled(true);
				btnSrcBrwse.setEnabled(true);
				if(arg0.keyCode == 13){
				File file = new File(PropertyFile.filePath);
				final String[] directories = file.list(new FilenameFilter() {
				  @Override
				  public boolean accept(File current, String name) {
				    return new File(current, name).isDirectory();
				  }
				});
		
				String newName = txtProjectName.getText();//typed name currently
				String temp = PropertyFile.filePath + File.separator;
				PropertyFile.filePath += File.separator +  newName;
					for (String directorie : directories) {
						if(directorie.equals(newName)){
							crctLabl.setImage(SWTResourceManager.getImage("/home/shiyam/Projects/FinalYear/Anduril/img/violation.jpg"));
							  JOptionPane.showMessageDialog(null, newName + " is already existing in \n" + StaticData.workspace,"Error Message",JOptionPane.ERROR_MESSAGE);
                                                        return;
						}else{
							
							crctLabl.setImage(SWTResourceManager.getImage("/home/shiyam/Projects/FinalYear/Anduril/img/exact.jpg"));;
							File folder = new File(PropertyFile.filePath);
							if(!folder.exists()) {
                                                            String projectpath = PropertyFile.filePath+File.separator;
								/*
								 * File Make Directory
								 */
                                                                FilePropertyName.addSubFolderIntoProject(folder);
//								XMLWriter writer = new XMLWriter();
//								writer.createMainNode(projectpath,newName);
							}
							btnReqBrwse.setEnabled(true);
							btnUmlBrwse.setEnabled(true);
							btnSrcBrwse.setEnabled(true);
                                                        btnFinish.setEnabled(true);
							return;
						}
				}
                                //if no directories in specific folder add that project folder
                                if(directories.length == 0){
                                    String projectPath = PropertyFile.filePath+File.separator;
                                    File folder = new File(projectPath);
                                    /*
                                    * File Make Directory
                                    */
                                    FilePropertyName.addSubFolderIntoProject(folder);
                                    btnReqBrwse.setEnabled(true);
                                    btnUmlBrwse.setEnabled(true);
                                    btnSrcBrwse.setEnabled(true);
                                    btnFinish.setEnabled(true);
                                }
			}

			}
		});
		txtProjectName.setBounds(158, 42, 309, 21);
		
		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.setImage(SWTResourceManager.getImage("/home/shiyam/Projects/FinalYear/Anduril/img/violation.jpg"));
		btnCancel.setBounds(10, 293, 75, 25);
		btnCancel.setText("Cancel");
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				//close sub project create window
				shell.dispose();
			}
		});
		
		
		Label lblTraceabilityProject = new Label(shell, SWT.NONE);
		lblTraceabilityProject.setText("Traceability Project  Path");
		lblTraceabilityProject.setBounds(10, 15, 142, 15);
		
		txtCusersshiyamdocuments = new Text(shell, SWT.BORDER);
		txtCusersshiyamdocuments.setText(StaticData.workspace);
		txtCusersshiyamdocuments.setEnabled(false);
		txtCusersshiyamdocuments.setEditable(false);
		txtCusersshiyamdocuments.setBounds(158, 12, 312, 21);
		
		Button button_2 = new Button(shell, SWT.NONE);
		button_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
                             int x = JOptionPane.showConfirmDialog(null, "Do you want to change your current workspace?\n"+StaticData.workspace, "Worspace Confirmation", 
                                            JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                                   if(x == JOptionPane.YES_OPTION){
                                        DirectoryDialog dialog = new DirectoryDialog(shell);
                                        dialog.setFilterPath("c:\\"); // Windows specific
                                        PropertyFile.filePath = dialog.open();
                                        txtCusersshiyamdocuments.setText(PropertyFile.filePath);;
                                       StaticData.workspace = PropertyFile.filePath;
                                   }else if(x == JOptionPane.NO_OPTION){
                                       
                                   }
                           
			}
		});
		button_2.setText("Browse");
		button_2.setBounds(476, 10, 75, 25);
	}
	
	public void copy(FileDialog fileDialog,String filePath,String folder){
		
		localFilePath = filePath;
		if(localFilePath != null){
			//PropertyFile.docsFilePath = localFilePath;
			localFilePath = localFilePath.replace(Paths.get(localFilePath)
					.getFileName().toString(), "");
			selectedFiles = fileDialog.getFileNames();
			for (int k = 0; k < selectedFiles.length; k++) {
				
				path = Paths.get(localFilePath + selectedFiles[k]);
				Path target = Paths.get(StaticData.workspace + File.separator + txtProjectName.getText().toString()
                                +File.separator+folder);                                       
				if (localFilePath != null) {
					try {
						Files.copy(path,
								target.resolve(path.getFileName()),
								REPLACE_EXISTING);
					} catch (IOException e1) {							
						e1.printStackTrace();
					}
				}              
		}	
	}
	}
}
