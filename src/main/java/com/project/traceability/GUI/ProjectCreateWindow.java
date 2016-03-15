/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.GUI;

/**
 * @author shiyam
 * @author AARTHIKA
 */
import com.project.NLP.SourceCodeToXML.AccessProject;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.openide.util.Exceptions;

public class ProjectCreateWindow {

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

    Button btnReqBrwse;
    Button btnUmlBrwse;
    Button btnSrcBrwse;
    Button btnFinish;

    static String localFilePath;
    static String[] selectedFiles;
    static Path path;
    String uml_formats[] = {"*.uml*;*.xmi*;*.mdj*"};
    String req_formats[] = {"*.docs*;*.txt*"};

    /**
     * Launch the application.
     *
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
    
    public Shell getShell(){
        return shell;
    }
    
    public  void eventLoop(Display display) {
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
    }

    /**
     * Create contents of the window.
     */
    protected void createContents() {
        shell = new Shell();
        shell.setSize(598, 508);
        shell.setText("SWT Application");

        Dimension.toCenter(shell);//set the shell into center point 
        Group group = new Group(shell, SWT.NONE);
        group.setText("Project");
        group.setBounds(20, 42, 568, 137);

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

                    //The Project work space is entered and pressed enter button
                    String path = textWrkspace.getText().toString();
                    File file = new File(path);

                    if (!(file.isDirectory()
                            || file.exists())) {
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
        group_1.setBounds(20, 190, 556, 198);

        Label label_3 = new Label(group_1, SWT.NONE);
        label_3.setText("Requirement File");
        label_3.setBounds(10, 37, 137, 18);

        txtRequirementPath = new Text(group_1, SWT.BORDER);
        txtRequirementPath.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {

                if (!txtRequirementPath.getText().equals("")) {
                    if (!txtUmlPath.getText().equals("") && !txtProjectPath.getText().equals("")) {
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

                org.eclipse.swt.widgets.FileDialog fileDialog = new org.eclipse.swt.widgets.FileDialog(shell, SWT.SINGLE);
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
                    if (!txtRequirementPath.getText().equals("") && !txtProjectPath.getText().equals("")) {
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

                org.eclipse.swt.widgets.FileDialog fileDialog = new org.eclipse.swt.widgets.FileDialog(shell, SWT.MULTI);
                fileDialog.setText("Open");
                fileDialog.setFilterExtensions(uml_formats); // Windows           
                fileDialog.setFilterPath(StaticData.umlFilePath);
                localFilePath = fileDialog.open();
                StaticData.umlFilePath = localFilePath;
                localFilePath = localFilePath.replace(Paths.get(localFilePath)
                        .getFileName().toString(), "");
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
                    if (!txtRequirementPath.getText().equals("") && !txtUmlPath.getText().equals("")) {
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
                Pop up File Chooser Window
                 */
                DirectoryDialog directoryDialog = new DirectoryDialog(shell);
                directoryDialog.setText("Open");
                localFilePath = directoryDialog.open();
                StaticData.sourceFilePath = localFilePath;
                localFilePath = localFilePath.replace(Paths.get(localFilePath)
                        .getFileName().toString(), "");
                String root = "";// HomeGUI.tree.getToolTipText() + File.separator + txtProjectName.getText();
                String path = root + File.separator + FilePropertyName.SOURCE_CODE;
                srcJavaDir = new File(path);
                if (localFilePath != null) {
                    txtProjectPath.setText(StaticData.sourceFilePath);
                    boolean src = AccessProject.javaFilesExists(new File(StaticData.sourceFilePath.toString()));
                    System.out.println("Java Files " + src);
                    
                    if (src) {
                        if (!txtRequirementPath.getText().equals("") && !txtUmlPath.getText().equals("")) {

                            btnFinish.setEnabled(true);
                        }
                    } else {
                        txtProjectPath.setText("");
                        //JOptionPane.showMessageDialog(null, "Error in java project file path...", "Java Project Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        btnSrcBrwse.setText("Browse");
        btnSrcBrwse.setEnabled(false);
        btnSrcBrwse.setBounds(476, 122, 75, 27);

        final Button btnOk = new Button(group, SWT.NONE);
        btnOk.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {

                String projectName = txtProjectName.getText();
                if (isNameValid(projectName)) {
                    txtRequirementPath.setEnabled(true);
                    txtUmlPath.setEnabled(true);
                    txtProjectPath.setEnabled(true);

                    btnReqBrwse.setEnabled(true);
                    btnSrcBrwse.setEnabled(true);
                    btnUmlBrwse.setEnabled(true);

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

                File file = new File(StaticData.workspace, txtProjectName.getText());
                if (file.exists()) {
                    btnOk.setEnabled(false);

                    txtRequirementPath.setEnabled(false);
                    txtUmlPath.setEnabled(false);
                    txtProjectPath.setEnabled(false);

                    btnReqBrwse.setEnabled(false);
                    btnSrcBrwse.setEnabled(false);
                    btnUmlBrwse.setEnabled(false);

                    btnFinish.setEnabled(false);
                } else {
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

        Composite composite = new Composite(shell, SWT.NONE);
        composite.setBounds(10, 394, 568, 97);

        Button button_2 = new Button(composite, SWT.NONE);
        button_2.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                shell.dispose();
            }
        });

        final Label lblNewLabel = new Label(composite, SWT.NONE);
        lblNewLabel.setBounds(24, 10, 459, 17);
        lblNewLabel.setText("");
        lblNewLabel.setForeground(new org.eclipse.swt.graphics.Color(Display.getCurrent(), 255, 0, 0));

        button_2.setText("Cancel");
        //button_2.setImage(SWTResourceManager.getImage("null"));
        button_2.setBounds(0, 29, 75, 25);

        btnFinish = new Button(composite, SWT.NONE);
        btnFinish.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {

                String reqFilePath = PropertyFile.docsFilePath;
                String umFilePath = StaticData.umlFilePath;
                String srcFilePath = StaticData.sourceFilePath;

                String projectName = txtProjectName.getText();

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
                    //PropertyFile.setRelationshipXMLPath(xmlFolder + File.separator + FilePropertyName.RELATION_NAME);

                    RelationManager.createXML(projectAbsoulutePath.substring(0, projectAbsoulutePath.length() - 1));
                    //RelationManager.createXML(projectAbsoulutePath+FilePropertyName.XML);

                    File propertyFolder = new File(projectAbsoulutePath + FilePropertyName.PROPERTY);
                    propertyFolder.mkdir();

                    //projectPath = PropertyFile.filePath  + File.separator;
                    projectPath = PropertyFile.filePath;
                    System.out.println("---Project create window : line473 : " + projectPath);
                    PropertyFile.setProjectName(projectName);
                    PropertyFile.setGraphDbPath(projectPath + File.separator + FilePropertyName.PROPERTY + File.separator + projectName + ".graphdb");
                    PropertyFile.setGeneratedGexfFilePath(projectPath + File.separator + FilePropertyName.PROPERTY + File.separator + projectName + ".gexf");
                    PropertyFile.setRelationshipXMLPath(projectPath + "Relations.xml");

                    HomeGUI.shell.setText("SAT- " + projectName);
                    HomeGUI.newTab.setVisible(true);
                    HomeGUI.tree.setVisible(true);

                    System.out.println("---Project create window : line486 : " + projectPath);
                    RelationManager.createXML(projectPath + projectName);

                    /*
                                write the sat_configuration.xml file with 
                                new project node and workspace node if needed
                     */
                    Adapter.wrkspace = StaticData.workspace;
                    Adapter.projectPath = StaticData.workspace + projectName;
                    Adapter.createProjectNode();

                    String temp = lalProjectWrkspace.
                            getText().concat(File.separator);

                    if (!temp.equals(StaticData.workspace)) {
                        StaticData.workspace = temp;
                        Adapter.createwrkpace("false");
                    } else {
                        StaticData.workspace = temp;
                        Adapter.changeExistingWrkspaceStatus(StaticData.workspace, false);
                    }
                    System.out.println("Name: " + reqFilePath);
                    //String[] names=reqFilePath.split(""+File.separator);
                    //String requirementFileName=names[names.length-1];
                    String requirementFileName = reqFilePath.substring(reqFilePath.lastIndexOf(File.separator));
                    System.out.println("Re: " + requirementFileName);
                    StaticData.requirementFilePath = projectAbsoulutePath + FilePropertyName.REQUIREMENT + File.separator + requirementFileName;
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
                    /* XMLConversion.convertRequirementFile();
                    XMLConversion.convertUMLFile();
                    XMLConversion.convertJavaFiles();*/
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

            }
        });
        btnFinish.setText("Finish");
        btnFinish.setEnabled(false);
        btnFinish.setBounds(493, 29, 75, 25);

        Label label_6 = new Label(shell, SWT.NONE);
        label_6.setText("New project will be created ");
        label_6.setBounds(20, 10, 189, 17);

    }

    private boolean isNameValid(String aName) {
        /*
		 * have to write name validation here
         */
        boolean isValid = true;
        return isValid;
    }

    public void displayError(String msg) {
        MessageBox box = new MessageBox(shell, SWT.ICON_ERROR);
        box.setMessage(msg);
        box.open();
    }
}
