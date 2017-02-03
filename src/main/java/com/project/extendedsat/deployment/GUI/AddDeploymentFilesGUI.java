/*
 * TNS
 * 
 */




package com.project.extendedsat.deployment.GUI;



import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;




public class AddDeploymentFilesGUI {

	protected Shell shlAddDeployemntFiles;
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private String deploymentDiagram;
	private String dockerFile;

	
	
	
	public String getDeploymentDiagram() {
		return deploymentDiagram;
	}


	public void setDeploymentDiagram(String deploymentDiagram) {
		this.deploymentDiagram = deploymentDiagram;
	}


	public String getDockerFile() {
		return dockerFile;
	}


	public void setDockerFile(String dockerFile) {
		this.dockerFile = dockerFile;
	}


	public AddDeploymentFilesGUI( String deploymentFile, String dockerFile ){
		this.deploymentDiagram = deploymentFile;
		this.dockerFile = dockerFile;
	}
	
	
	public AddDeploymentFilesGUI(){
		
	}
	
	

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			//AddDeploymentFilesGUI window = new AddDeploymentFilesGUI();
			//window.open();
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
		shlAddDeployemntFiles.open();
		shlAddDeployemntFiles.layout();
		while (!shlAddDeployemntFiles.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlAddDeployemntFiles = new Shell();
		shlAddDeployemntFiles.setSize(625, 416);
		shlAddDeployemntFiles.setText("Add Deployemnt Files");
		
		Group grpDeploymentDiagram = new Group(shlAddDeployemntFiles, SWT.NONE);
		grpDeploymentDiagram.setText("Deployment Diagram");
		grpDeploymentDiagram.setBounds(10, 10, 605, 112);
		
		
		//select deployment diagram
		Button btnNewButton = new Button(grpDeploymentDiagram, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				FileDialog dlg = new FileDialog( shlAddDeployemntFiles, SWT.OPEN );
				dlg.open();
				String s = dlg.getFilterPath() + "/" + dlg.getFileName();
				text.setText(s);
				deploymentDiagram = s;
				//files.set(0, s);
			}
		});
		btnNewButton.setBounds(475, 10, 86, 28);
		btnNewButton.setText("Browse");
		
		text = new Text(grpDeploymentDiagram, SWT.BORDER);
		text.setBounds(29, 10, 343, 30);
		
		
		//deployment diagram OK button
		//used to generate dockerfile selections
		Button btnNewButton_1 = new Button(grpDeploymentDiagram, SWT.NONE);
		btnNewButton_1.setBounds(475, 53, 86, 28);
		btnNewButton_1.setText("OK");
		
		Group grpDockerFiles = new Group(shlAddDeployemntFiles, SWT.NONE);
		grpDockerFiles.setText("Docker Files");
		grpDockerFiles.setBounds(10, 138, 605, 202);
		
		text_1 = new Text(grpDockerFiles, SWT.BORDER);
		text_1.setBounds(31, 10, 345, 30);
		
		
		//select docker file 1
		Button button = new Button(grpDockerFiles, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				FileDialog dlg = new FileDialog( shlAddDeployemntFiles, SWT.OPEN );
				dlg.open();
				dockerFile = dlg.getFilterPath() + "/" + dlg.getFileName();
				text_1.setText(dockerFile);
			}
		});
		button.setText("Browse");
		button.setBounds(476, 10, 86, 28);
		
		
		//select docker file 2
		Button button_1 = new Button(grpDockerFiles, SWT.NONE);
		button_1.setText("Browse");
		button_1.setBounds(476, 58, 86, 28);
		
		
		//select docker file 3
		Button button_2 = new Button(grpDockerFiles, SWT.NONE);
		button_2.setText("Browse");
		button_2.setBounds(476, 106, 86, 28);
		
		text_2 = new Text(grpDockerFiles, SWT.BORDER);
		text_2.setBounds(31, 56, 345, 30);
		
		text_3 = new Text(grpDockerFiles, SWT.BORDER);
		text_3.setBounds(31, 104, 345, 30);
		
		
		//ok button
		Button btnNewButton_2 = new Button(shlAddDeployemntFiles, SWT.NONE);
		btnNewButton_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				//DeploymentTraceabilityGUI gui = new DeploymentTraceabilityGUI();
				//gui.setDeploymentDiagram(deploymentDiagram);
				//gui.setDockerFile(dockerFile);
				//gui.open();
				shlAddDeployemntFiles.dispose();
				//return files;
			}
		});
		btnNewButton_2.setBounds(268, 352, 86, 28);
		btnNewButton_2.setText("OK");

	}
}



