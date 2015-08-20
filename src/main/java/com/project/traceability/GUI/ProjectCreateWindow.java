package com.project.traceability.GUI;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFileChooser;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.project.NLP.staticdata.StaticData;
import com.project.traceability.common.PropertyFile;

public class ProjectCreateWindow {

	protected Shell shell;
	
	static String localFilePath;
	static String[] selectedFiles;
	static Path path;
	
    String formats[] = { "*.uml*;*.xmi*;*.mdj*"};
    String req_formats[] ={"*.docs*;*.txt*;*.mdj*"};
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
		txtRequirementPath.setText("E:\\User\\Shiyamalan\\rquirement.docs");
		txtRequirementPath.setBounds(127, 37, 343, 21);
		
		Button btnNewButton_1 = new Button(grpImportRequiredFiles, SWT.NONE);
		btnNewButton_1.setEnabled(false);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				FileDialog fileDialog = new FileDialog(shell, SWT.MULTI);
				fileDialog.setText("Open");
                fileDialog.setFilterExtensions(req_formats); // Windows           
				fileDialog.setFilterPath(PropertyFile.docsFilePath);
				localFilePath = fileDialog.open();
				PropertyFile.docsFilePath = localFilePath;
				localFilePath = localFilePath.replace(Paths.get(localFilePath)
						.getFileName().toString(), "");
				txtRequirementPath.setText(PropertyFile.docsFilePath);
			}
		});
		btnNewButton_1.setBounds(476, 35, 75, 25);
		btnNewButton_1.setText("Browse");
		
		Label lblDiagramFile = new Label(grpImportRequiredFiles, SWT.NONE);
		lblDiagramFile.setText("Design Diagram File");
		lblDiagramFile.setBounds(10, 81, 113, 15);
		
		txtUmlPath = new Text(grpImportRequiredFiles, SWT.BORDER);
		txtUmlPath.setText("E:\\User\\Shiyamalan\\diagram.mdj or diagram.uml");
		txtUmlPath.setEnabled(false);
		txtUmlPath.setEditable(false);
		txtUmlPath.setBounds(127, 78, 343, 21);
		
		Button button = new Button(grpImportRequiredFiles, SWT.NONE);
		button.setEnabled(false);
		button.addSelectionListener(new SelectionAdapter() {
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
					
					txtUmlPath.setText(StaticData.umlFilePath);
			}
		});
		button.setText("Browse");
		button.setBounds(476, 76, 75, 25);
		
		Label lblProjectPath = new Label(grpImportRequiredFiles, SWT.NONE);
		lblProjectPath.setText("Project Path");
		lblProjectPath.setBounds(10, 129, 102, 15);
		
		txtProjectPath = new Text(grpImportRequiredFiles, SWT.BORDER);
		txtProjectPath.setText("E:\\Final Year Project\\Anduril\\src\\main\\java");
		txtProjectPath.setEnabled(false);
		txtProjectPath.setEditable(false);
		txtProjectPath.setBounds(127, 126, 343, 21);
		
		Button button_1 = new Button(grpImportRequiredFiles, SWT.NONE);
		button_1.setEnabled(false);
		button_1.setText("Browse");
		button_1.setBounds(476, 124, 75, 25);
		
		Label lblProjectName = new Label(shell, SWT.NONE);
		lblProjectName.setBounds(10, 45, 133, 21);
		lblProjectName.setText("Traceabilty Project Name");
		
		txtProjectName = new Text(shell, SWT.BORDER);
		txtProjectName.setBounds(158, 42, 309, 21);
		
		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.setImage(SWTResourceManager.getImage("E:\\B.Sc\\Sem-7\\Final Year Project\\Anduril\\img\\violation.jpg"));
		btnCancel.setBounds(10, 293, 75, 25);
		btnCancel.setText("Cancel");
		
		Button btnFinish = new Button(shell, SWT.NONE);
		btnFinish.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				  if(!txtProjectName.getText().isEmpty()){
	                    AddSourceCodeFile addSourceWin = new AddSourceCodeFile();
	                    //shell.setVisible(false);
	                    addSourceWin.open();         
	             }else{
	            	 
	             }
			}
		});
		btnFinish.setEnabled(false);
		btnFinish.setImage(SWTResourceManager.getImage("E:\\B.Sc\\Sem-7\\Final Year Project\\Anduril\\img\\exact.jpg"));
		btnFinish.setBounds(486, 293, 75, 25);
		btnFinish.setText("Finish");
		
		Label label = new Label(shell, SWT.NONE);
		label.setImage(SWTResourceManager.getImage("E:\\B.Sc\\Sem-7\\Final Year Project\\Anduril\\img\\violation.jpg"));
		label.setBounds(506, 42, 55, 24);
		
		Label lblTraceabilityProject = new Label(shell, SWT.NONE);
		lblTraceabilityProject.setText("Traceability Project  Path");
		lblTraceabilityProject.setBounds(10, 15, 142, 15);
		
		txtCusersshiyamdocuments = new Text(shell, SWT.BORDER);
		txtCusersshiyamdocuments.setText("C:\\Users\\shiyam\\Documents\\");
		txtCusersshiyamdocuments.setEnabled(false);
		txtCusersshiyamdocuments.setEditable(false);
		txtCusersshiyamdocuments.setBounds(158, 12, 312, 21);
		
		Button button_2 = new Button(shell, SWT.NONE);
		button_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				DirectoryDialog dialog = new DirectoryDialog(shell);
			    dialog.setFilterPath("c:\\"); // Windows specific
			    PropertyFile.filePath = dialog.open();
			}
		});
		button_2.setText("Browse");
		button_2.setBounds(476, 10, 75, 25);
		
		

	}
	
	public void copyFilesToFilePath(FileDialog fileDialog){
		//PropertyFile.filepath copying location
		
		selectedFiles = fileDialog.getFileNames();
		for (int k = 0; k < selectedFiles.length; k++) {
			path = Paths.get(localFilePath + selectedFiles[k]);
			Path target = Paths.get(PropertyFile.filePath);                                       
			if (PropertyFile.filePath != null) {
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
