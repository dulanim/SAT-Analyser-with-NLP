/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.GUI;

/**
 *
 * @author shiyam
 */


import com.project.NLP.file.operations.FilePropertyName;
import com.project.property.config.xml.writer.Adapter;
import com.project.property.config.xml.writer.XMLConversion;
import static com.project.traceability.GUI.NewProjectWindow.projectPath;
import com.project.traceability.common.Dimension;
import com.project.traceability.common.PropertyFile;
import com.project.traceability.manager.RelationManager;
import com.project.traceability.staticdata.StaticData;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;


public class ProjectCreateWindow {

	
	public static TreeItem trtmNewTreeitem;
        public File srcJavaDir;
        public Label lalProjectWrkspace;
        
        public static String projectName;
	protected Shell shell;
	private Text textWrkspace;
	private Text txtProjectName;
	private Text txtRequirementPath;
	private Text txtUmlPath;
	private Text txtProjectPath;
	
	Button btnReqBrwse;
	Button btnUmlBrwse;
	Button btnSrcBrwse;
	Button btnFinish;
	
	static String localFilePath;
	static String[] selectedFiles;
	static Path path;
       
        Display display;
                
        String uml_formats[] = { "*.uml*;*.xmi*;*.mdj*"};
        String req_formats[] ={"*.docs*;*.txt*"};
    
        String errorStatus;
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
		shell.setSize(650, 550);
		shell.setText("New Project Create Window");
		
                Dimension.toCenter(shell);//set the shell into center point 
		Group group = new Group(shell, SWT.NONE);
		group.setText("Project");
		group.setBounds(20, 42, 568, 137);
		
		Label label = new Label(group, SWT.NONE);
		label.setText("New Workspace Path");
		label.setBounds(0, 5, 175, 18);
		
                
                final Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(10, 394, 568, 121);
                
		lalProjectWrkspace = new Label(shell, SWT.NONE);
		lalProjectWrkspace.setText(StaticData.workspace);
		lalProjectWrkspace.setBounds(221, 10, 347, 17);
		
		textWrkspace = new Text(group, SWT.BORDER);
		textWrkspace.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				
				if(e.keyCode == 10){
					
					//The Project work space is entered and pressed enter button
					String path = textWrkspace.getText();
					File file = new File(path);
					
					if(!(file.isDirectory() ||
								file.exists())){
						txtProjectName.setEnabled(true);
						if(!(path.lastIndexOf(File.separator) == path.length()-1))
								path.concat(File.separator);
						StaticData.workspace = path;
					}else{
						MessageBox messageBox;
						messageBox = new MessageBox(shell, SWT.ERROR);
                                                messageBox.setMessage("Given Path is Invalid");
                                                messageBox.setText("Invalid Path Exception");
                                                messageBox.open();
					}
				}
			}
		});
		textWrkspace.setEnabled(false);
		textWrkspace.setEditable(false);
		textWrkspace.setBounds(181, 5, 290, 23);
		
		final Button buttonWrkspace = new Button(group, SWT.NONE);
		buttonWrkspace.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				DirectoryDialog dialog = new DirectoryDialog(shell);
                                String str = dialog.open();
			    
				    if(!str.equals("")){
			    	txtProjectName.setEnabled(true);
			    	textWrkspace.setText(str);
			    	lalProjectWrkspace.setText(str);
			    }
			}
		});
		buttonWrkspace.setText("Browse");
		buttonWrkspace.setEnabled(false);
		buttonWrkspace.setBounds(477, 5, 75, 25);
		
		Label label_1 = new Label(group, SWT.NONE);
		label_1.setText("Traceabilty Project Name");
		label_1.setBounds(0, 75, 175, 21);
		
		Group group_1 = new Group(shell, SWT.NONE);
		group_1.setText("Import Required Files");
		group_1.setBounds(20, 190, 556, 198);
		
		Label label_3 = new Label(group_1, SWT.NONE);
		label_3.setText("Requirement File");
		label_3.setBounds(10, 37, 137, 18);
		
		txtRequirementPath = new Text(group_1, SWT.BORDER);
		txtRequirementPath.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				
				if(!txtRequirementPath.getText().equals("")){
					if(!txtUmlPath.getText().equals("") && !txtProjectPath.getText().equals("")){
						btnFinish.setEnabled(true);
					}
				}else{
					btnFinish.setEnabled(false);
				}
			}
		});
		txtRequirementPath.setEnabled(false);
		txtRequirementPath.setEditable(false);
		txtRequirementPath.setBounds(153, 31, 317, 27);
		
		btnReqBrwse = new Button(group_1, SWT.NONE);
		btnReqBrwse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
			
				org.eclipse.swt.widgets.FileDialog fileDialog = new org.eclipse.swt.widgets.FileDialog(shell, SWT.SINGLE);
				fileDialog.setText("Open");
                                fileDialog.setFilterExtensions(req_formats); // Windows           
				fileDialog.setFilterPath(PropertyFile.docsFilePath);
				localFilePath = fileDialog.open();
				if(localFilePath != null){
					PropertyFile.docsFilePath = localFilePath;
					txtRequirementPath.setText(PropertyFile.docsFilePath);
				}
			}
		});
		btnReqBrwse.setText("Browse");
		btnReqBrwse.setEnabled(false);
		btnReqBrwse.setBounds(476, 31, 75, 29);
		
		Label label_4 = new Label(group_1, SWT.NONE);
		label_4.setText("Design Diagram File");
		label_4.setBounds(10, 81, 137, 18);
		
		txtUmlPath = new Text(group_1, SWT.BORDER);
		txtUmlPath.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				
				if(!txtUmlPath.getText().equals("")){
					if(!txtRequirementPath.getText().equals("") && !txtProjectPath.getText().equals("")){
						btnFinish.setEnabled(true);
					}
				}else{
					btnFinish.setEnabled(false);
				}

			}
		});
		txtUmlPath.setEnabled(false);
		txtUmlPath.setEditable(false);
		txtUmlPath.setBounds(153, 72, 317, 27);
		
		final Button btnUmlBrwse = new Button(group_1, SWT.NONE);
		btnUmlBrwse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
			    org.eclipse.swt.widgets.FileDialog fileDialog = new org.eclipse.swt.widgets.FileDialog(shell, SWT.MULTI);
				fileDialog.setText("Open");
		        fileDialog.setFilterExtensions(uml_formats); // Windows           
				fileDialog.setFilterPath(StaticData.umlFilePath);
				localFilePath = fileDialog.open();
		        StaticData.umlFilePath = localFilePath;
				localFilePath = localFilePath.replace(Paths.get(localFilePath)
								.getFileName().toString(), "");
	            if(localFilePath != null){
						txtUmlPath.setText(StaticData.umlFilePath);
	            }
			}
		});
		btnUmlBrwse.setText("Browse");
		btnUmlBrwse.setEnabled(false);
		btnUmlBrwse.setBounds(476, 74, 75, 27);
		
		Label label_5 = new Label(group_1, SWT.NONE);
		label_5.setText("Project Path");
		label_5.setBounds(10, 126, 137, 18);
		
		txtProjectPath = new Text(group_1, SWT.BORDER);
		txtProjectPath.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(!txtProjectPath.getText().equals("")){
					if(!txtRequirementPath.getText().equals("") && !txtUmlPath.getText().equals("")){
						btnFinish.setEnabled(true);
					}
				}else{
					btnFinish.setEnabled(false);
				}
				
			}
		});
		txtProjectPath.setEnabled(false);
		txtProjectPath.setEditable(false);
		txtProjectPath.setBounds(153, 120, 317, 27);
		
		final Button btnSrcBrwse = new Button(group_1, SWT.NONE);
		btnSrcBrwse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				  /*
                Pop up File Chooser Window
                */
                DirectoryDialog directoryDialog = new DirectoryDialog(shell);
                directoryDialog.setText("Open");
                localFilePath = directoryDialog.open();
                StaticData.sourceFilePath = localFilePath;
                localFilePath = localFilePath.replace(Paths.get(localFilePath)
				.getFileName().toString(), "");
                String root ="";// HomeGUI.tree.getToolTipText() + File.separator + txtProjectName.getText();
                String path = root + File.separator + FilePropertyName.SOURCE_CODE;
                srcJavaDir = new File(path);
                if(localFilePath != null){
                	txtProjectPath.setText(StaticData.sourceFilePath);
	}		
			}
		});
		btnSrcBrwse.setText("Browse");
		btnSrcBrwse.setEnabled(false);
		btnSrcBrwse.setBounds(476, 122, 75, 27);
		
                final Label lblStatus = new Label(composite, SWT.NONE);
		lblStatus.setBounds(164, 0, 155, 17);
		lblStatus.setText("Status");
                
		final Button btnOk = new Button(group, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				String projectName = txtProjectName.getText();
				if(isNameValid(projectName)){
					txtRequirementPath.setEnabled(true);
					txtUmlPath.setEnabled(true);
					txtProjectPath.setEnabled(true);
					
					btnReqBrwse.setEnabled(true);
					btnSrcBrwse.setEnabled(true);
					btnUmlBrwse.setEnabled(true);
                                        
                                        
				}else{
					/*
					 * name is not valid produce pop up message to user
					 * 
					 */
                                   lblStatus.setText(errorStatus);
                                   btnFinish.setEnabled(false);
				}
			}
		});
		btnOk.setBounds(477, 67, 77, 29);
		btnOk.setText("Ok");
		
		txtProjectName = new Text(group, SWT.BORDER);
		txtProjectName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
//				
//				File file = new File(StaticData.workspace,txtProjectName.getText());
//				if(file.exists()){
//					btnOk.setEnabled(false);
//				}else{
//					btnOk.setEnabled(true);
//				}
			}
			@Override
			public void keyReleased(KeyEvent e) {
				
				File file = new File(StaticData.workspace,txtProjectName.getText());
				if(file.exists()){
					btnOk.setEnabled(false);
					
					txtRequirementPath.setEnabled(false);
					txtUmlPath.setEnabled(false);
					txtProjectPath.setEnabled(false);
					
					btnReqBrwse.setEnabled(false);
					btnSrcBrwse.setEnabled(false);
					btnUmlBrwse.setEnabled(false);
					
					btnFinish.setEnabled(false);
				}else{
					btnOk.setEnabled(true);
				}
			}
		});
		txtProjectName.setText("");
		txtProjectName.setEnabled(true);
		txtProjectName.setBounds(182, 72, 278, 24);
		
		final Button btnNewWrkspace = new Button(group, SWT.CHECK);
		btnNewWrkspace.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				if(!btnNewWrkspace.getSelection()){
         			buttonWrkspace.setEnabled(false);
         			textWrkspace.setEnabled(false);
         			txtProjectName.setEnabled(true);
         			btnOk.setEnabled(true);
         		}else{
         			buttonWrkspace.setEnabled(true);
         			textWrkspace.setEnabled(true);
         			txtProjectName.setEnabled(false);
         			btnOk.setEnabled(false);
         		}
			}
		});
		btnNewWrkspace.setText("Create New Workspace");
		btnNewWrkspace.setBounds(270, 34, 199, 24);
		
		
		
		
		
		Button button_2 = new Button(composite, SWT.NONE);
		button_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		button_2.setText("Cancel");
		//button_2.setImage(SWTResourceManager.getImage("null"));
		button_2.setBounds(0, 29, 75, 25);
		
		btnFinish = new Button(composite, SWT.NONE);
		btnFinish.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
                            
                                final ProgressBar progressBar = new ProgressBar(composite, SWT.HORIZONTAL | SWT.SMOOTH);
                                progressBar.setBounds(0, 73, 568, 14);
                                
                                final Label lblProgress = new Label(composite, SWT.NONE);
                                lblProgress.setBounds(163, 104, 405, 17);
                                lblProgress.setText("New Label");
                                
                                new Thread(new Runnable(){
                                    @Override
                                    public void run()
                                    {
                                        int counter = 0;
                                        while (lblProgress != null && !lblProgress.isDisposed())
                                        {
                                            String text = StaticData.statusString;
                                                    if (lblProgress != null && !lblProgress.isDisposed()
                                                            && !text.equals("Finish The Progress")){
                                                        lblProgress.setText(text);
                                                        progressBar.setSelection(progressBar
                                                                .getSelection() + 1);
                                                    }
                                                    if(text.equals("Finish The Progress")){
                                                        progressBar.dispose();
                                                    }
                                                
                                            

                                            try
                                            {
                                                Thread.sleep(1000);
                                            }catch (InterruptedException e){
                                                displayError(e.toString());
                                            }
                                        }
                                    }
                                }).start();
                            

                            String reqFilePath = PropertyFile.docsFilePath;
                            String umFilePath =  StaticData.umlFilePath;
                            String srcFilePath = StaticData.sourceFilePath;

                            String projectName = txtProjectName.getText();

                            if(!(StaticData.workspace.lastIndexOf(File.separator) == StaticData.workspace.length()-1))
                                    StaticData.workspace += (File.separator);

                            File projectRoot = new File(StaticData.workspace + projectName +File.separator);
                                    try {
                                            StaticData.statusString = "Making the " +  projectName +" Project Path" + 
                                            projectRoot.mkdir();
                                            ProjectCreateWindow.projectName = projectName;
                                    } catch (Exception e1) {
                                            // TODO Auto-generated catch block
                                            e1.printStackTrace();
                                    }
                            File reqFile = new File(reqFilePath);
                            File umlFile= new File(umFilePath);
                            File srcFile = new File(srcFilePath);

                            String projectAbsoulutePath = projectRoot.getAbsolutePath() ;

                            if(!(projectAbsoulutePath.lastIndexOf(File.separator) == projectAbsoulutePath.length()-1))
                                    projectAbsoulutePath += (File.separator);

                            File srcFolder = new File(projectAbsoulutePath + FilePropertyName.SOURCE_CODE);
                            try {
                                        StaticData.statusString = "Making " + "src Java File path";
                                        srcFolder.mkdir();

                                        StaticData.statusString = "Copying " + "src Java Files to " + projectName;
                                        FilePropertyName.copyFolder(srcFile, srcFolder);


                                        StaticData.statusString = "Making " + "Text File path";
                                        File txtFolder = new File(projectAbsoulutePath+FilePropertyName.REQUIREMENT);
                                        txtFolder.mkdir();
					
                                        StaticData.statusString = "Copying " + "File" + reqFile.getName() +" To " + projectName +
                                                    File.separator + FilePropertyName.REQUIREMENT;
					FilePropertyName.copyFile(reqFile, txtFolder);
					
                                        StaticData.statusString = "Making " + "File Path"  +" To " + projectName +
                                                    File.separator + FilePropertyName.UML;
					File umlFolder = new File(projectAbsoulutePath+FilePropertyName.UML);
					umlFolder.mkdir();
					
                                        
                                        StaticData.statusString = "Copying " + "Uml " + umlFile.getName() +" To " + projectName +
                                                    File.separator + FilePropertyName.UML;
					FilePropertyName.copyFile(umlFile, umlFolder);
					
                                        
                                        StaticData.statusString = "Making " + "XML File Path"  +" To " + projectName +
                                                    File.separator + FilePropertyName.XML;
					File xmlFolder = new File(projectAbsoulutePath+FilePropertyName.XML);
					xmlFolder.mkdir();
					//PropertyFile.setRelationshipXMLPath(xmlFolder + File.separator + FilePropertyName.RELATION_NAME);
					
                                        StaticData.statusString = "Preparing " + "Relations.xml File Path"  +" To " + projectName +
                                                    File.separator + FilePropertyName.XML;
                                        RelationManager.createXML(projectAbsoulutePath.substring(0,projectAbsoulutePath.length()-1));
					
                                        
                                        
                                        StaticData.statusString = "Making " + "Property File Path"  +" To " + projectName +
                                                    File.separator + FilePropertyName.PROPERTY;
					File propertyFolder = new File(projectAbsoulutePath+FilePropertyName.PROPERTY);
					propertyFolder.mkdir();
                                        
                                
                                        
                                StaticData.statusString = "Making " + " System Files"  +" To " + projectName +
                                                    File.separator;
                                projectPath = PropertyFile.filePath  + File.separator;
                                PropertyFile.setProjectName(projectName);
                                PropertyFile.setGraphDbPath(projectPath + projectName + ".graphdb");
                                PropertyFile.setGeneratedGexfFilePath(projectPath+ projectName + ".gexf");
                                PropertyFile.setRelationshipXMLPath(projectPath + "Relations.xml");

                                
                                HomeGUI.shell.setText("SAT- " + projectName);
                                HomeGUI.newTab.setVisible(true);
                                HomeGUI.tree.setVisible(true);

                                RelationManager.createXML(projectPath);
                                
                                
                                 /*
                                write the sat_configuration.xml file with 
                                new project node and workspace node if needed
                                */
                                
                                StaticData.statusString = "Setting Up " + " Project Workspaces";
                                Adapter.wrkspace = StaticData.workspace;
                                Adapter.projectPath = StaticData.workspace + projectName;
                                Adapter.createProjectNode();
                                
                                String temp =lalProjectWrkspace.
                                                getText().concat(File.separator);
                                        
                                if(!temp.equals(StaticData.workspace)){
                                        StaticData.workspace = temp;
                                        Adapter.createwrkpace("false");
                                }else{
                                    StaticData.workspace = temp;
                                    Adapter.changeExistingWrkspaceStatus(StaticData.workspace
                                            ,false);
                                }
                                
                                
                                
                                StaticData.statusString = "Converting " + " Required Design XML Files"  +" To " + projectName +
                                                    File.separator + FilePropertyName.XML;
                                XMLConversion.convertUMLFile();
                                
                                StaticData.statusString = "Converting " + " Required Java XML Files"  +" To " + projectName +
                                                    File.separator + FilePropertyName.XML;
                                XMLConversion.convertJavaFiles();
                                
                                StaticData.statusString = "Finish The Progress";
                                shell.dispose();
                                
				HomeGUI.closeMain(HomeGUI.shell);
                                HomeGUI.main(null);
                                
				} catch (IOException e1) {
                                    displayError(e1.toString());
                                }catch(Exception e12){
                                    displayError(e12.toString());
                                    shell.dispose();
                                    HomeGUI.closeMain(HomeGUI.shell);
                                    HomeGUI.main(null);
                                }
				
				
			
                                
                               
                                
			}
		});
		btnFinish.setText("Finish");
		btnFinish.setEnabled(false);
		btnFinish.setBounds(493, 29, 75, 25);
		
		
		
		Label label_6 = new Label(shell, SWT.NONE);
		label_6.setText("New Project Will be created ");
		label_6.setBounds(20, 10, 189, 17);
		
                
                
		
		

	}
	
	private boolean isNameValid(String aName){
		/*
		 * have to write name validation here
		 */
                      
		boolean isValid = true;
                 if(aName.equals("")||aName.isEmpty()){
                        errorStatus = "Name should have valid characters Eg: Exampler";
                        isValid = false;
                 }
                 else{
                        if(!(Character.isUpperCase(aName.charAt(0)))){
                            errorStatus = "Project Name start with capital";
                            isValid = false;
                        }
                 }
                 
            
            return isValid;
	}
        
        public void displayError(String msg){
            MessageBox box = new MessageBox(shell, SWT.ICON_ERROR);
            box.setMessage(msg);
            box.open();
        }
}