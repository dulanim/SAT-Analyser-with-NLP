/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.GUI;
import java.io.File; 

/**
 * @author shiyam
 * @author AARTHIKA
 * @author SAMITHAP
 */
import static com.project.traceability.GUI.NewProjectWindow.projectPath;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.xml.transform.TransformerException;
import org.apache.commons.io.FileUtils;

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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.api.ProcessingTarget;
import org.gephi.preview.api.RenderTarget;
import org.gephi.preview.types.DependantOriginalColor;
import org.gephi.preview.types.EdgeColor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

import com.project.NLP.SourceCodeToXML.AccessProject;
import com.project.NLP.file.operations.FilePropertyName;
import com.project.extendedsat.config.MainClass;
import com.project.extendedsat.deployment.extendedsat.Main;
import com.project.extendedsat.test.TestAST;
import com.project.property.config.xml.reader.XMLReader;
import com.project.property.config.xml.writer.Adapter;
import com.project.property.config.xml.writer.XMLConversion;
import com.project.traceability.common.Dimension;
import com.project.traceability.common.PropertyFile;
import com.project.traceability.manager.RelationManager;
import com.project.traceability.staticdata.ScriptContents;
import com.project.traceability.staticdata.StaticData;

import processing.core.PApplet;

public class ProjectCreateWindow {
	// TraceabilityVisualization viz = new
	// TraceabilityVisualization("PropertyFileToConfigurationFile.gexf",
	// "SourceToTest.gexf", "DiployentToDocker.gexf");

	Map<String, JPanel> panels = new HashMap<>();
	String configname = "PropertyFileToConfigurationFile.gexf";
	String testname = "SourceToTest.gexf";
	String deployname = "DiployentToDocker.gexf";
	JFrame frame1 = new JFrame("SAT Analyzer Extension-Devops");
	JTabbedPane jTabbedPane = new JTabbedPane();
	public static TreeItem trtmNewTreeitem;
	public File srcJavaDir;
	public Label lalProjectWrkspace;

	public static String projectName = "DEFAULT";
	public static Shell shell;
	private Text textWrkspace;
	private Text txtProjectName;
	private Text txtRequirementPath;
	private Text txtUmlPath;
	private Text txtProjectPath;

	JFileChooser chooser;
	Button btnReqBrwse;
	Button btnUmlBrwse;
	Button btnSrcBrwse;
	Button btnFinish;

	String path_workspacepath =""; //lalProjectWrkspace.getText();
	String name_project ="";// txtProjectName.getText();
	
	String path_testfolder= "";
	
	
//	File dir = new File("testcode");
//	dir.mkdir();
	
	
	static String localFilePath;
	static String[] selectedFiles;
	static Path path;
	static Map<String, String> allProjectsNamePathMap = new HashMap<>();
	String uml_formats[] = { "*.uml*;*.xmi*;*.mdj*" };
	String req_formats[] = { "*.docs*;*.txt*" };
	String config_formats[] = { "*.properties*" };
	String all_formats[] = { "*.*" };

	private Text text_1;
	private Text text_2;
	private Text text_3;
	private Text text_5;
	private Text text_6;

	private Button button;
	private Button button_1;
	private Button button_2;
	private Button button_3;
	private Button button_5;
	private Button button_6;

	/**
	 * Launch the application.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			allProjectsNamePathMap.clear();
			XMLReader reader = new XMLReader();
			allProjectsNamePathMap = reader.readAllProjectName();
			ProjectCreateWindow window = new ProjectCreateWindow();
			window.open();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void gopro(){
		try {
			allProjectsNamePathMap.clear();
			XMLReader reader = new XMLReader();
			allProjectsNamePathMap = reader.readAllProjectName();
			ProjectCreateWindow window = new ProjectCreateWindow();
			window.open();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Shell getShell() {
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
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
//		frame1.setSize(1366, 768);
//		frame1.getContentPane().setSize(1366, 768);
		//frame1.setPreferredSize(new Dimension(600,600));
		frame1.add(jTabbedPane);
		

	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(622, 833);
		shell.setText("SWT Application");

		Dimension.toCenter(shell);// set the shell into center point
		Group group = new Group(shell, SWT.NONE);
		group.setText("Project");
		group.setBounds(20, 42, 556, 137);

		Label label = new Label(group, SWT.NONE);
		label.setText("New Workspace Path");
		label.setBounds(0, 5, 175, 18);

		lalProjectWrkspace = new Label(shell, SWT.NONE);
		lalProjectWrkspace.setText(StaticData.workspace);
		lalProjectWrkspace.setBounds(221, 10, 347, 17);

		textWrkspace = new Text(group, SWT.BORDER);
		textWrkspace.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {

				if (e.keyCode == 10) {

					// The Project work space is entered and pressed enter
					// button
					String path = textWrkspace.getText().toString();
					File file = new File(path);

					if (!(file.isDirectory() || file.exists())) {
						txtProjectName.setEnabled(true);
						if (!(path.lastIndexOf(File.separator) == path.length() - 1)) {
							path.concat(File.separator);
						}
						StaticData.workspace = path;
					} else {
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

				if (!str.equals("")) {
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
		group_1.setBounds(20, 190, 556, 174);

		Label label_3 = new Label(group_1, SWT.NONE);
		label_3.setText("Requirement File");
		label_3.setBounds(10, 37, 137, 18);

		txtRequirementPath = new Text(group_1, SWT.BORDER);
		txtRequirementPath.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {

				if (!txtRequirementPath.getText().equals("")) {
					if (!txtUmlPath.getText().equals("") && !txtProjectPath.getText().equals("")
							&& !text_1.getText().equals("") && !text_2.getText().equals("")
							&& !text_3.getText().equals("") && !text_5.getText().equals("")
							&& !text_6.getText().equals("")) {
						btnFinish.setEnabled(true);
					}
				} else {
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

				org.eclipse.swt.widgets.FileDialog fileDialog = new org.eclipse.swt.widgets.FileDialog(shell,
						SWT.SINGLE);
				fileDialog.setText("Open");
				fileDialog.setFilterExtensions(req_formats); // Windows
				fileDialog.setFilterPath(PropertyFile.docsFilePath);
				localFilePath = fileDialog.open();
				if (localFilePath != null) {
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

				if (!txtUmlPath.getText().equals("")) {
					if (!txtRequirementPath.getText().equals("") && !txtProjectPath.getText().equals("")
							&& !text_1.getText().equals("") && !text_2.getText().equals("")
							&& !text_3.getText().equals("") && !text_5.getText().equals("")
							&& !text_6.getText().equals("")) {
						btnFinish.setEnabled(true);
					}
				} else {
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

				org.eclipse.swt.widgets.FileDialog fileDialog = new org.eclipse.swt.widgets.FileDialog(shell,
						SWT.MULTI);
				fileDialog.setText("Open");
				fileDialog.setFilterExtensions(uml_formats); // Windows
				fileDialog.setFilterPath(StaticData.umlFilePath);
				localFilePath = fileDialog.open();
				StaticData.umlFilePath = localFilePath;
				localFilePath = localFilePath.replace(Paths.get(localFilePath).getFileName().toString(), "");
				if (localFilePath != null) {
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
				if (!txtProjectPath.getText().equals("")) {
					if (!txtRequirementPath.getText().equals("") && !txtUmlPath.getText().equals("")
							&& !text_1.getText().equals("") && !text_2.getText().equals("")
							&& !text_3.getText().equals("") && !text_5.getText().equals("")
							&& !text_6.getText().equals("")) {
						btnFinish.setEnabled(true);
					}
				} else {
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
				 * Pop up File Chooser Window
				 */
				DirectoryDialog directoryDialog = new DirectoryDialog(shell);
				directoryDialog.setText("Open");
				localFilePath = directoryDialog.open();
				StaticData.sourceFilePath = localFilePath;
				localFilePath = localFilePath.replace(Paths.get(localFilePath).getFileName().toString(), "");
				String root = "";// HomeGUI.tree.getToolTipText() +
									// File.separator +
									// txtProjectName.getText();
				String path = root + File.separator + FilePropertyName.SOURCE_CODE;
				srcJavaDir = new File(path);
				if (localFilePath != null) {
					txtProjectPath.setText(StaticData.sourceFilePath);
					boolean src = AccessProject.javaFilesExists(new File(StaticData.sourceFilePath.toString()));
					System.out.println("Java Files " + src);

					// &&!text_1.getText().equals("")&&!text_2.getText().equals("")&&!text_3.getText().equals("")&&!text_5.getText().equals("")&&!text_6.getText().equals("")
					if (src) {
						if (!txtRequirementPath.getText().equals("") && !txtUmlPath.getText().equals("")) {

							btnFinish.setEnabled(true);
						}
					} else {
						txtProjectPath.setText("");
						// JOptionPane.showMessageDialog(null, "Error in java
						// project file path...", "Java Project Error",
						// JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		btnSrcBrwse.setText("Browse");
		btnSrcBrwse.setEnabled(false);
		btnSrcBrwse.setBounds(476, 120, 75, 27);

		final Button btnOk = new Button(group, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				 path_workspacepath = lalProjectWrkspace.getText();
				 name_project = txtProjectName.getText();
				 path_testfolder=path_workspacepath+File.separator+name_project;
				 System.out.println(path_testfolder);
				try {
					FileUtils.forceMkdir(new File(path_testfolder));
					

					
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				String projectName = txtProjectName.getText();
				if (isNameValid(projectName)) {
					txtRequirementPath.setEnabled(true);
					txtUmlPath.setEnabled(true);
					txtProjectPath.setEnabled(true);
					text_1.setEnabled(true);
					text_2.setEnabled(true);
					text_3.setEnabled(true);
					text_5.setEnabled(true);
					text_6.setEnabled(true);

					btnReqBrwse.setEnabled(true);
					btnSrcBrwse.setEnabled(true);
					btnUmlBrwse.setEnabled(true);
					button.setEnabled(true);
					button_1.setEnabled(true);
					button_3.setEnabled(true);
					button_5.setEnabled(true);
					button_6.setEnabled(true);

				} else {
					/*
					 * name is not valid produce pop up message to user
					 * 
					 */
				}
			}
		});
		btnOk.setBounds(477, 67, 77, 29);
		btnOk.setText("Ok");
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(20, 648, 556, 62);

		final Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setBounds(24, 10, 459, 17);
		lblNewLabel.setText("");
		lblNewLabel.setForeground(new org.eclipse.swt.graphics.Color(Display.getCurrent(), 255, 0, 0));

		txtProjectName = new Text(group, SWT.BORDER);
		txtProjectName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				//
				// File file = new
				// File(StaticData.workspace,txtProjectName.getText());
				// if(file.exists()){
				// btnOk.setEnabled(false);
				// }else{
				// btnOk.setEnabled(true);
				// }
			}

			@Override
			public void keyReleased(KeyEvent e) {

				File file = new File(StaticData.workspace, txtProjectName.getText());
				String typedName = txtProjectName.getText();
				boolean isProjectNameExists = isProjectExists(typedName);
				lblNewLabel.setText("");
				if (file.exists() || isProjectNameExists) {
					btnOk.setEnabled(false);

					txtRequirementPath.setEnabled(false);
					txtUmlPath.setEnabled(false);
					txtProjectPath.setEnabled(false);

					btnReqBrwse.setEnabled(false);
					btnSrcBrwse.setEnabled(false);
					btnUmlBrwse.setEnabled(false);

					btnFinish.setEnabled(false);

					if (!typedName.equals(""))
						lblNewLabel
								.setText("You typed project name exists in " + allProjectsNamePathMap.get(typedName));
					else
						lblNewLabel.setText("Project Name should not empty");
				} else {
					btnOk.setEnabled(true);
					lblNewLabel.setForeground(new org.eclipse.swt.graphics.Color(Display.getCurrent(), 255, 0, 0));
					lblNewLabel.setText("Project Name is valid");
				}
			}

			private boolean isProjectExists(String text) {
				// TODO Auto-generated method stub
				boolean isExits = false;
				if (allProjectsNamePathMap != null && allProjectsNamePathMap.containsKey(text)) {
					isExits = false;
				} else {
					// no need
					List<String> names = new ArrayList<>(allProjectsNamePathMap.keySet());
					for (String name : names) {
						if (name.equals(text)) {
							isExits = true;
							return isExits;
						}
					}
					isExits = false;
				}

				return isExits;
			}
		});
		txtProjectName.setText("");
		txtProjectName.setEnabled(true);
		txtProjectName.setBounds(182, 72, 278, 24);

		final Button btnNewWrkspace = new Button(group, SWT.CHECK);
		btnNewWrkspace.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				if (!btnNewWrkspace.getSelection()) {
					buttonWrkspace.setEnabled(false);
					textWrkspace.setEnabled(false);
					txtProjectName.setEnabled(true);
					btnOk.setEnabled(true);
				} else {
					buttonWrkspace.setEnabled(true);
					textWrkspace.setEnabled(true);
					txtProjectName.setEnabled(false);
					btnOk.setEnabled(false);
				}
			}
		});
		btnNewWrkspace.setText("Create New Workspace");
		btnNewWrkspace.setBounds(270, 34, 199, 24);

		button_2 = new Button(composite, SWT.NONE);
		button_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});

		lblNewLabel.setText("");
		lblNewLabel.setForeground(new org.eclipse.swt.graphics.Color(Display.getCurrent(), 255, 0, 0));

		button_2.setText("Cancel");
		// button_2.setImage(SWTResourceManager.getImage("null"));
		button_2.setBounds(10, 29, 75, 25);

		btnFinish = new Button(composite, SWT.NONE);
		btnFinish.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				// MainClass mainClass = new
				// MainClass(text_5.getText(),text_6.getText());
				// mainClass.startConfig();
				//
				// Main m = new Main(text_1.getText(),text_2.getText());
				// m.startDeployment();
				//
				//
				// try {
				// TestAST testAST = new TestAST(text_3.getText());
				// testAST.TestingTraceability();
				// } catch (IOException e1) {
				// // TODO Auto-generated catch block
				// e1.printStackTrace();
				// } catch (TransformerException e1) {
				// // TODO Auto-generated catch block
				// e1.printStackTrace();
				// }
				//

				String projectName = txtProjectName.getText();

				// making script file for this project
				File file_root_script_folder = new File(PropertyFile.configuration_root + "scripts");
				if (!file_root_script_folder.exists())// home/shiyam/SAT_CONFIGS/scripts/
					file_root_script_folder.mkdirs(); // making script file for
														// each projects

				File script_file = FilePropertyName.createScriptFile(projectName + ".py");

				String scripts = ScriptContents.getContents(projectName);
				FilePropertyName.writeScriptContent(script_file, scripts);
				// finished the script file creation

				String reqFilePath = PropertyFile.docsFilePath;
				String umFilePath = StaticData.umlFilePath;
				String srcFilePath = StaticData.sourceFilePath;

				if (!(StaticData.workspace.lastIndexOf(File.separator) == StaticData.workspace.length() - 1)) {
					StaticData.workspace += (File.separator);
				}

				File projectRoot = new File(StaticData.workspace + projectName + File.separator);
				try {
					projectRoot.mkdir();
					ProjectCreateWindow.projectName = projectName;
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				File reqFile = new File(reqFilePath);
				File umlFile = new File(umFilePath);
				File srcFile = new File(srcFilePath);

				String projectAbsoulutePath = projectRoot.getAbsolutePath();
				System.out.println("!234");
				if (!(projectAbsoulutePath.lastIndexOf(File.separator) == projectAbsoulutePath.length() - 1)) {
					projectAbsoulutePath += (File.separator);
				}

				File srcFolder = new File(projectAbsoulutePath + FilePropertyName.SOURCE_CODE);
				try {
					srcFolder.mkdir();
					FilePropertyName.copyFolder(srcFile, srcFolder);

					File txtFolder = new File(projectAbsoulutePath + FilePropertyName.REQUIREMENT);
					txtFolder.mkdir();

					FilePropertyName.copyFile(reqFile, txtFolder);

					File umlFolder = new File(projectAbsoulutePath + FilePropertyName.UML);
					umlFolder.mkdir();

					FilePropertyName.copyFile(umlFile, umlFolder);

					File xmlFolder = new File(projectAbsoulutePath + FilePropertyName.XML);
					xmlFolder.mkdir();
					// PropertyFile.setRelationshipXMLPath(xmlFolder +
					// File.separator + FilePropertyName.RELATION_NAME);

					RelationManager.createXML(projectAbsoulutePath.substring(0, projectAbsoulutePath.length() - 1));
					// RelationManager.createXML(projectAbsoulutePath+FilePropertyName.XML);

					File propertyFolder = new File(projectAbsoulutePath + FilePropertyName.PROPERTY);
					propertyFolder.mkdir();

					// projectPath = PropertyFile.filePath + File.separator;
					projectPath = PropertyFile.filePath;
					System.out.println("---Project create window : line473 : " + projectPath);
					PropertyFile.setProjectName(projectName);
					PropertyFile.setGraphDbPath(projectPath + File.separator + FilePropertyName.PROPERTY
							+ File.separator + projectName + ".graphdb");
					PropertyFile.setGeneratedGexfFilePath(projectPath + File.separator + FilePropertyName.PROPERTY
							+ File.separator + projectName + ".gexf");
					PropertyFile.setRelationshipXMLPath(projectPath + "Relations.xml");

					HomeGUI.shell.setText("SAT- " + projectName);
					HomeGUI.newTab.setVisible(true);
					HomeGUI.tree.setVisible(true);

					System.out.println("---Project create window : line486 : " + projectPath);
					RelationManager.createXML(projectPath + projectName);

					/*
					 * write the sat_configuration.xml file with new project
					 * node and workspace node if needed
					 */
					Adapter.wrkspace = StaticData.workspace;
					Adapter.projectPath = StaticData.workspace + projectName;
					Adapter.createProjectNode();

					String temp = lalProjectWrkspace.getText().concat(File.separator);

					if (!temp.equals(StaticData.workspace)) {
						StaticData.workspace = temp;
						Adapter.createwrkpace("false");
					} else {
						StaticData.workspace = temp;
						Adapter.changeExistingWrkspaceStatus(StaticData.workspace, false);
					}
					System.out.println("Name: " + reqFilePath);
					// String[] names=reqFilePath.split(""+File.separator);
					// String requirementFileName=names[names.length-1];
					String requirementFileName = reqFilePath.substring(reqFilePath.lastIndexOf(File.separator));
					System.out.println("Re: " + requirementFileName);
					StaticData.requirementFilePath = projectAbsoulutePath + FilePropertyName.REQUIREMENT
							+ File.separator + requirementFileName;
					System.out.println("----------Requirement file path--------- " + StaticData.requirementFilePath);

					Thread requirementThread = new Thread(new Runnable() {
						public void run() {
							try {
								XMLConversion.convertRequirementFile();
							} catch (Exception ex) {
								Exceptions.printStackTrace(ex);
							}
						}
					});
					requirementThread.start();

					Thread javaFilesThread = new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								XMLConversion.convertJavaFiles();
							} catch (Exception ex) {
								Exceptions.printStackTrace(ex);
							}
						}
					});
					javaFilesThread.start();

					Thread umlThread = new Thread(new Runnable() {
						@Override
						public void run() {
							XMLConversion.convertUMLFile();
						}
					});
					umlThread.start();

					while (requirementThread.isAlive() || javaFilesThread.isAlive() || umlThread.isAlive()) {
						StringBuilder aliveThread = new StringBuilder();
						if (requirementThread.isAlive()) {
							aliveThread.append("Requirement");
							aliveThread.append(" ");
						}
						if (javaFilesThread.isAlive()) {
							aliveThread.append("Source Code");
							aliveThread.append(" ");
						}
						if (umlThread.isAlive()) {
							aliveThread.append("UML");
							aliveThread.append(" ");
						}

						lblNewLabel.setText(aliveThread.toString() + " Extraction On Progress");

					}
					System.out.println("Thread finished");
					/*
					 * XMLConversion.convertRequirementFile();
					 * XMLConversion.convertUMLFile();
					 * XMLConversion.convertJavaFiles();
					 */
					shell.dispose();
					HomeGUI.closeMain(HomeGUI.shell);
					HomeGUI.main(null);

				} catch (IOException e1) {
					displayError(e1.toString());
				} catch (Exception e12) {
					displayError(e12.toString());
					shell.dispose();
					HomeGUI.closeMain(HomeGUI.shell);
					HomeGUI.main(null);
				}
				// System.out.println("NLP OK...........");

			}

		});
		btnFinish.setText("Development");
		btnFinish.setEnabled(false);
		btnFinish.setBounds(471, 29, 75, 25);

		Button btnConfiguration = new Button(composite, SWT.NONE);
		btnConfiguration.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				MainClass mainClass = new MainClass(text_5.getText(), text_6.getText());
				mainClass.startConfig();
				panels.put("config", config(configname));

				JTabbedPane pane = new JTabbedPane();
				pane.add("Config", panels.get("config"));

				if (panels.containsKey("test")) {
					pane.add("Test", panels.get("test"));
				}
				if (panels.containsKey("deploy")) {
					pane.add("Deploy", panels.get("deploy"));
				}
				frame1.remove(jTabbedPane);
				frame1.add(pane);
				jTabbedPane = pane;
				frame1.pack();
				frame1.setVisible(true);
			}
		});
		btnConfiguration.setText("Configuration");
		btnConfiguration.setEnabled(true);
		btnConfiguration.setBounds(120, 29, 94, 25);

		Button btnDeployment = new Button(composite, SWT.NONE);
		btnDeployment.setBounds(367, 29, 75, 25);
		btnDeployment.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Main m = new Main(text_1.getText(), text_2.getText());
				m.startDeployment();
				
				panels.put("deploy", deploy(deployname));

				JTabbedPane pane = new JTabbedPane();
				pane.add("Deploy", panels.get("deploy"));

				if (panels.containsKey("test")) {
					pane.add("Test", panels.get("test"));
				}
				if (panels.containsKey("config")) {
					pane.add("Config", panels.get("config"));
				}
				frame1.remove(jTabbedPane);
				frame1.add(pane);
				jTabbedPane = pane;
				frame1.pack();
				frame1.setVisible(true);
			}
		});
		btnDeployment.setText("Deployment");
		btnDeployment.setEnabled(true);

		Button btnTesting = new Button(composite, SWT.NONE);
		btnTesting.setBounds(251, 29, 75, 25);
		btnTesting.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				try {
					TestAST testAST = new TestAST(text_3.getText());
					testAST.TestingTraceability(path_testfolder);

					panels.put("test", test(testname));
					JTabbedPane pane = new JTabbedPane();
					pane.add("Test", panels.get("test"));

					if (panels.containsKey("config")) {
						pane.add("Config", panels.get("config"));
					}
					if (panels.containsKey("deploy")) {
						pane.add("Deploy", panels.get("deploy"));
					}

					frame1.remove(jTabbedPane);
					frame1.add(pane);
					jTabbedPane = pane;
					frame1.pack();
					frame1.setVisible(true);

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (TransformerException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnTesting.setText("Testing");
		btnTesting.setEnabled(true);

		Label label_6 = new Label(shell, SWT.NONE);
		label_6.setText("New project will be created ");
		label_6.setBounds(20, 10, 189, 17);

		Group grpExetendedSatAnalizer = new Group(shell, SWT.NONE);
		grpExetendedSatAnalizer.setText("Exetended SAT Analizer");
		grpExetendedSatAnalizer.setBounds(20, 370, 556, 272);

		Label lblDeploymentDiagramFile = new Label(grpExetendedSatAnalizer, SWT.NONE);
		lblDeploymentDiagramFile.setText("Deployment Diagram File");
		lblDeploymentDiagramFile.setBounds(10, 34, 137, 18);

		text_1 = new Text(grpExetendedSatAnalizer, SWT.BORDER);
		text_1.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {

				if (!text_1.getText().equals("")) {
					if (!txtUmlPath.getText().equals("") && !txtProjectPath.getText().equals("")
							&& !txtRequirementPath.getText().equals("") && !text_2.getText().equals("")
							&& !text_3.getText().equals("") && !text_5.getText().equals("")
							&& !text_6.getText().equals("")) {
						btnFinish.setEnabled(true);
					}
				} else {
					btnFinish.setEnabled(false);
				}
			}
		});
		text_1.setEnabled(false);
		text_1.setEditable(false);
		text_1.setBounds(153, 28, 317, 27);

		button = new Button(grpExetendedSatAnalizer, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				org.eclipse.swt.widgets.FileDialog fileDialog = new org.eclipse.swt.widgets.FileDialog(shell,
						SWT.SINGLE);
				fileDialog.setText("Open");
				fileDialog.setFilterExtensions(all_formats); // Windows
				fileDialog.setFilterPath(PropertyFile.docsFilePath);
				localFilePath = fileDialog.open();
				if (localFilePath != null) {
					// PropertyFile.docsFilePath = localFilePath;
					text_1.setText(localFilePath);
				}
			}
		});
		button.setText("Browse");
		button.setEnabled(false);
		button.setBounds(476, 30, 75, 27);

		Label lblDockerFile = new Label(grpExetendedSatAnalizer, SWT.NONE);
		lblDockerFile.setText("Docker File");
		lblDockerFile.setBounds(10, 79, 137, 18);

		text_2 = new Text(grpExetendedSatAnalizer, SWT.BORDER);
		text_2.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {

				if (!text_2.getText().equals("")) {
					if (!txtUmlPath.getText().equals("") && !txtProjectPath.getText().equals("")
							&& !txtRequirementPath.getText().equals("") && !text_1.getText().equals("")
							&& !text_3.getText().equals("") && !text_5.getText().equals("")
							&& !text_6.getText().equals("")) {
						btnFinish.setEnabled(true);
					}
				} else {
					btnFinish.setEnabled(false);
				}
			}
		});
		text_2.setEnabled(false);
		text_2.setEditable(false);
		text_2.setBounds(153, 73, 317, 27);

		button_1 = new Button(grpExetendedSatAnalizer, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				org.eclipse.swt.widgets.FileDialog fileDialog = new org.eclipse.swt.widgets.FileDialog(shell,
						SWT.SINGLE);
				fileDialog.setText("Open");
				fileDialog.setFilterExtensions(all_formats); // Windows
				fileDialog.setFilterPath(PropertyFile.docsFilePath);
				localFilePath = fileDialog.open();
				if (localFilePath != null) {
					// PropertyFile.docsFilePath = localFilePath;
					text_2.setText(localFilePath);
				}
			}
		});
		button_1.setText("Browse");
		button_1.setEnabled(false);
		button_1.setBounds(476, 75, 75, 27);

		Label lblTestCases = new Label(grpExetendedSatAnalizer, SWT.NONE);
		lblTestCases.setText("Test case Path");
		lblTestCases.setBounds(10, 124, 137, 18);

		text_3 = new Text(grpExetendedSatAnalizer, SWT.BORDER);
		text_3.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {

				if (!text_3.getText().equals("")) {
					if (!txtUmlPath.getText().equals("") && !txtProjectPath.getText().equals("")
							&& !txtRequirementPath.getText().equals("") && !text_1.getText().equals("")
							&& !text_2.getText().equals("") && !text_5.getText().equals("")
							&& !text_6.getText().equals("")) {
						btnFinish.setEnabled(true);
					}
				} else {
					btnFinish.setEnabled(false);
				}
			}
		});
		text_3.setEnabled(false);
		text_3.setEditable(false);
		text_3.setBounds(153, 118, 317, 27);

		button_3 = new Button(grpExetendedSatAnalizer, SWT.NONE);
		button_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				/*
				 * Pop up File Chooser Window
				 */
				DirectoryDialog directoryDialog = new DirectoryDialog(shell);
				directoryDialog.setText("Open");
				localFilePath = directoryDialog.open();
				StaticData.sourceFilePath = localFilePath;
				localFilePath = localFilePath.replace(Paths.get(localFilePath).getFileName().toString(), "");
				String root = "";// HomeGUI.tree.getToolTipText() +
									// File.separator +
									// txtProjectName.getText();
				String path = root + File.separator + FilePropertyName.SOURCE_CODE;
				srcJavaDir = new File(path);
				if (localFilePath != null) {
					text_3.setText(localFilePath);
					boolean src = AccessProject.javaFilesExists(new File(StaticData.sourceFilePath.toString()));
					System.out.println("Test Java Files " + src);

				}
			}
		});
		button_3.setText("Browse");
		button_3.setEnabled(false);
		button_3.setBounds(476, 120, 75, 27);

		Label lblPropertyFile = new Label(grpExetendedSatAnalizer, SWT.NONE);
		lblPropertyFile.setText("Property File");
		lblPropertyFile.setBounds(10, 173, 137, 18);

		text_5 = new Text(grpExetendedSatAnalizer, SWT.BORDER);
		text_5.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {

				if (!text_5.getText().equals("")) {
					if (!txtUmlPath.getText().equals("") && !txtProjectPath.getText().equals("")
							&& !txtRequirementPath.getText().equals("") && !text_1.getText().equals("")
							&& !text_2.getText().equals("") && !text_3.getText().equals("")
							&& !text_6.getText().equals("")) {
						btnFinish.setEnabled(true);
					}
				} else {
					btnFinish.setEnabled(false);
				}
			}
		});
		text_5.setEnabled(false);
		text_5.setEditable(false);
		text_5.setBounds(153, 167, 317, 27);
		button_5 = new Button(grpExetendedSatAnalizer, SWT.NONE);

		button_5.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				org.eclipse.swt.widgets.FileDialog fileDialog = new org.eclipse.swt.widgets.FileDialog(shell,
						SWT.SINGLE);
				fileDialog.setText("Open");
				fileDialog.setFilterExtensions(config_formats); // Windows
				fileDialog.setFilterPath(PropertyFile.docsFilePath);
				localFilePath = fileDialog.open();
				if (localFilePath != null) {
					// PropertyFile.docsFilePath = localFilePath;
					text_5.setText(localFilePath);
				}
			}
		});
		button_5.setText("Browse");
		button_5.setEnabled(false);
		button_5.setBounds(476, 169, 75, 27);

		Label lblConfigurationFile = new Label(grpExetendedSatAnalizer, SWT.NONE);
		lblConfigurationFile.setText("Configuration Text File");
		lblConfigurationFile.setBounds(10, 208, 137, 18);

		text_6 = new Text(grpExetendedSatAnalizer, SWT.BORDER);
		text_6.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {

				if (!text_6.getText().equals("")) {
					if (!txtUmlPath.getText().equals("") && !txtProjectPath.getText().equals("")
							&& !txtRequirementPath.getText().equals("") && !text_1.getText().equals("")
							&& !text_2.getText().equals("") && !text_3.getText().equals("")
							&& !text_5.getText().equals("")) {
						btnFinish.setEnabled(true);
					}
				} else {
					btnFinish.setEnabled(false);
				}
			}
		});
		text_6.setEnabled(false);
		text_6.setEditable(false);
		text_6.setBounds(153, 202, 317, 27);

		button_6 = new Button(grpExetendedSatAnalizer, SWT.NONE);
		button_6.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				org.eclipse.swt.widgets.FileDialog fileDialog = new org.eclipse.swt.widgets.FileDialog(shell,
						SWT.SINGLE);
				fileDialog.setText("Open");
				fileDialog.setFilterExtensions(req_formats); // Windows
				fileDialog.setFilterPath(PropertyFile.docsFilePath);
				localFilePath = fileDialog.open();
				if (localFilePath != null) {
					// PropertyFile.docsFilePath = localFilePath;
					text_6.setText(localFilePath);
				}
			}
		});
		button_6.setText("Browse");
		button_6.setEnabled(false);
		button_6.setBounds(476, 204, 75, 27);

	}

	private boolean isNameValid(String aName) {
		/*
		 * have to write name validation here
		 */
		boolean isValid = true;

		return isValid;
	}

	public JPanel config(String configname) {

		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
		pc.newProject();
		Workspace workspace = pc.getCurrentWorkspace();

		// Import file
		ImportController importController = Lookup.getDefault().lookup(ImportController.class);
		Container container = null;
		try {
			// File file = new File("PropertyFileToConfigurationFile.gexf");
			File file = new File(configname);
			container = importController.importFile(file);
		} catch (Exception ex) {
			ex.printStackTrace();

		}

		// Append imported data to GraphAPI
		importController.process(container, new DefaultProcessor(), workspace);

		// Preview configuration
		PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
		PreviewModel previewModel = previewController.getModel();

		previewModel.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE); 
        previewModel.getProperties().putValue(PreviewProperty.SHOW_EDGE_LABELS, Boolean.TRUE); 
        previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_COLOR, new DependantOriginalColor(Color.BLUE));
//        previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_SHOW_BOX, Boolean.TRUE);
//        previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_BOX_COLOR, new DependantColor(Color.YELLOW));
        
        previewModel.getProperties().putValue(PreviewProperty.EDGE_LABEL_COLOR, new DependantOriginalColor(Color.BLACK));
        previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_FONT, previewModel.getProperties().getFontValue(PreviewProperty.NODE_LABEL_FONT).deriveFont(20));
        previewModel.getProperties().putValue(PreviewProperty.EDGE_THICKNESS, new Float(2f));
       // previewModel.getProperties().putValue(PreviewProperty.NODE_BORDER_COLOR,new DependantColor(DependantColor.Mode.PARENT));
        previewModel.getProperties().putValue(PreviewProperty.EDGE_COLOR,new EdgeColor(Color.RED));	        
        previewModel.getProperties().putValue(PreviewProperty.EDGE_CURVED, Boolean.FALSE); 
        previewModel.getProperties().putValue(PreviewProperty.EDGE_OPACITY, 100);
        previewModel.getProperties().putValue(PreviewProperty.NODE_OPACITY, 200);
        previewModel.getProperties().putValue(PreviewProperty.EDGE_RADIUS, 0f); 
        previewModel.getProperties().putValue(PreviewProperty.BACKGROUND_COLOR, Color.WHITE); 
        previewController.refreshPreview(); 

		// New Processing target, get the PApplet
		ProcessingTarget target = (ProcessingTarget) previewController.getRenderTarget(RenderTarget.PROCESSING_TARGET);
		PApplet applet = target.getApplet();
		applet.init();

		// Refresh the preview and reset the zoom
		previewController.render(target);
		target.refresh();
		target.resetZoom();

		// Add the applet to a JFrame and display
		JPanel panel1 = new JPanel();
		panel1.setLayout(new BorderLayout());

		panel1.add(applet, BorderLayout.CENTER);
		// new JPanel().add(applet,BorderLayout.CENTER);
		panel1.setSize(1650, 1080);
		return panel1;
	}

	public JPanel test(String testname) {
		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
		pc.newProject();
		Workspace workspace = pc.getCurrentWorkspace();

		// Import file
		ImportController importController = Lookup.getDefault().lookup(ImportController.class);
		Container container = null;
		try {
			// File file = new File("PropertyFileToConfigurationFile.gexf");
			File file = new File(testname);
			container = importController.importFile(file);
		} catch (Exception ex) {
			ex.printStackTrace();

		}

		// Append imported data to GraphAPI
		importController.process(container, new DefaultProcessor(), workspace);

		// Preview configuration
//		PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
//		PreviewModel previewModel = previewController.getModel();
//
//		previewModel.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE); 
//        previewModel.getProperties().putValue(PreviewProperty.SHOW_EDGE_LABELS, Boolean.TRUE); 
//        previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_COLOR, new DependantOriginalColor(Color.RED));
////        previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_SHOW_BOX, Boolean.TRUE);
////        previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_BOX_COLOR, new DependantColor(Color.YELLOW));
//        
//        previewModel.getProperties().putValue(PreviewProperty.EDGE_LABEL_COLOR, new DependantOriginalColor(Color.BLACK));
//        previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_FONT, previewModel.getProperties().getFontValue(PreviewProperty.NODE_LABEL_FONT).deriveFont(20));
//        previewModel.getProperties().putValue(PreviewProperty.EDGE_THICKNESS, new Float(2f));
//       // previewModel.getProperties().putValue(PreviewProperty.NODE_BORDER_COLOR,new DependantColor(DependantColor.Mode.PARENT));
//        previewModel.getProperties().putValue(PreviewProperty.EDGE_COLOR,new EdgeColor(Color.BLUE));	        
//        previewModel.getProperties().putValue(PreviewProperty.EDGE_CURVED, Boolean.FALSE); 
//        previewModel.getProperties().putValue(PreviewProperty.EDGE_OPACITY, 100);
//        previewModel.getProperties().putValue(PreviewProperty.NODE_OPACITY, 200);
//        previewModel.getProperties().putValue(PreviewProperty.EDGE_RADIUS, 0f); 
//        previewModel.getProperties().putValue(PreviewProperty.BACKGROUND_COLOR, Color.WHITE); 
//        previewController.refreshPreview(); 
		
		PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
		PreviewModel previewModel = previewController.getModel();

		previewModel.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE);
		previewModel.getProperties().putValue(PreviewProperty.SHOW_EDGE_LABELS, Boolean.TRUE);
		previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_COLOR, new DependantOriginalColor(Color.RED));
		previewModel.getProperties().putValue(PreviewProperty.EDGE_LABEL_COLOR, new DependantOriginalColor(Color.GREEN));
		// previewModel.getProperties().putValue(PreviewProperty.NODE_BORDER_COLOR,new DependantColor(DependantColor.Mode.PARENT));
		//previewModel.getProperties().putValue(PreviewProperty.EDGE_COLOR, new DependantOriginalColor(Color.GREEN));
		previewModel.getProperties().putValue(PreviewProperty.EDGE_CURVED, Boolean.FALSE);
		previewModel.getProperties().putValue(PreviewProperty.EDGE_OPACITY, 50);
		previewModel.getProperties().putValue(PreviewProperty.EDGE_RADIUS, 0f);
		previewModel.getProperties().putValue(PreviewProperty.BACKGROUND_COLOR, Color.WHITE);
		previewController.refreshPreview();

		// New Processing target, get the PApplet
		ProcessingTarget target = (ProcessingTarget) previewController.getRenderTarget(RenderTarget.PROCESSING_TARGET);
		PApplet applet = target.getApplet();
		applet.init();

		// Refresh the preview and reset the zoom
		previewController.render(target);
		target.refresh();
		target.resetZoom();

		// Add the applet to a JFrame and display
		JPanel panel2 = new JPanel();
		panel2.setLayout(new BorderLayout());

		panel2.add(applet, BorderLayout.CENTER);
		// new JPanel().add(applet,BorderLayout.CENTER);
		panel2.setSize(1650, 1080);
		return panel2;

	}

	public JPanel deploy(String deployname) {
		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
		pc.newProject();
		Workspace workspace = pc.getCurrentWorkspace();

		// Import file
		ImportController importController = Lookup.getDefault().lookup(ImportController.class);
		Container container = null;
		try {
			// File file = new File("PropertyFileToConfigurationFile.gexf");
			File file = new File(deployname);
			container = importController.importFile(file);
		} catch (Exception ex) {
			ex.printStackTrace();

		}

		// Append imported data to GraphAPI
		importController.process(container, new DefaultProcessor(), workspace);

		// Preview configuration
		PreviewController previewController = Lookup.getDefault().lookup(PreviewController.class);
		PreviewModel previewModel = previewController.getModel();

		previewModel.getProperties().putValue(PreviewProperty.SHOW_NODE_LABELS, Boolean.TRUE); 
        previewModel.getProperties().putValue(PreviewProperty.SHOW_EDGE_LABELS, Boolean.TRUE); 
        previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_COLOR, new DependantOriginalColor(Color.BLUE));
//        previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_SHOW_BOX, Boolean.TRUE);
//        previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_BOX_COLOR, new DependantColor(Color.YELLOW));
        
        previewModel.getProperties().putValue(PreviewProperty.EDGE_LABEL_COLOR, new DependantOriginalColor(Color.BLACK));
        previewModel.getProperties().putValue(PreviewProperty.NODE_LABEL_FONT, previewModel.getProperties().getFontValue(PreviewProperty.NODE_LABEL_FONT).deriveFont(20));
        previewModel.getProperties().putValue(PreviewProperty.EDGE_THICKNESS, new Float(2f));
       // previewModel.getProperties().putValue(PreviewProperty.NODE_BORDER_COLOR,new DependantColor(DependantColor.Mode.PARENT));
        previewModel.getProperties().putValue(PreviewProperty.EDGE_COLOR,new EdgeColor(Color.GREEN));	        
        previewModel.getProperties().putValue(PreviewProperty.EDGE_CURVED, Boolean.FALSE); 
        previewModel.getProperties().putValue(PreviewProperty.EDGE_OPACITY, 100);
        previewModel.getProperties().putValue(PreviewProperty.NODE_OPACITY, 200);
        previewModel.getProperties().putValue(PreviewProperty.EDGE_RADIUS, 0f); 
        previewModel.getProperties().putValue(PreviewProperty.BACKGROUND_COLOR, Color.WHITE); 
        previewController.refreshPreview(); 

		// New Processing target, get the PApplet
		ProcessingTarget target = (ProcessingTarget) previewController.getRenderTarget(RenderTarget.PROCESSING_TARGET);
		PApplet applet = target.getApplet();
		applet.init();

		// Refresh the preview and reset the zoom
		previewController.render(target);
		target.refresh();
		target.resetZoom();

		// Add the applet to a JFrame and display
		JPanel panel3 = new JPanel();
		panel3.setLayout(new BorderLayout());

		panel3.add(applet, BorderLayout.CENTER);
		// new JPanel().add(applet,BorderLayout.CENTER);
		panel3.setSize(1650, 1080);
		return panel3;

	}

	public void displayError(String msg) {
		MessageBox box = new MessageBox(shell, SWT.ICON_ERROR);
		box.setMessage(msg);
		box.open();
	}
}
