/*
 * TNS
 * 
 * 
 */


package com.project.extendedsat.deployment.GUI;



import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.project.extendedsat.deployment.db.Gephi;
import com.project.extendedsat.deployment.db.GexfGraph;

import org.eclipse.swt.widgets.Menu;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;



public class DeploymentTraceabilityGUI {

	protected Shell shlDeploymentTraceability;
	private String deploymentDiagram;
	private String dockerFile;
	
	
	
	
	public DeploymentTraceabilityGUI( String deploymentDiagram, String dockerFile ){
		this.deploymentDiagram = deploymentDiagram;
		this.dockerFile = dockerFile; 
	}
	
	
	public DeploymentTraceabilityGUI(){
		
	}
	
	
	
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



	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			DeploymentTraceabilityGUI window = new DeploymentTraceabilityGUI();
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
		createContents( display );
		shlDeploymentTraceability.open();
		shlDeploymentTraceability.layout();
		while (!shlDeploymentTraceability.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	
	
	
	/**
	 * Create contents of the window.
	 */
	protected void createContents( Display display ) {
		
		shlDeploymentTraceability = new Shell();
		shlDeploymentTraceability.setSize(923, 830);
		shlDeploymentTraceability.setText("Deployment Traceability");
		
		
		
		/**************************************** File Menu ****************************************/
		Menu menu = new Menu(shlDeploymentTraceability, SWT.BAR);
		shlDeploymentTraceability.setMenuBar(menu);
		
		MenuItem mntmFile = new MenuItem(menu, SWT.CASCADE);
		mntmFile.setText("File");
		
		
		
		Menu menu_1 = new Menu(mntmFile);
		mntmFile.setMenu(menu_1);
		
		
		//selecting new files for deployment traceability
		MenuItem mntmNewItem = new MenuItem(menu_1, SWT.NONE);
		mntmNewItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				AddDeploymentFilesGUI depFiles = new AddDeploymentFilesGUI(); //);
				depFiles.open();
				deploymentDiagram = depFiles.getDeploymentDiagram();
				dockerFile = depFiles.getDockerFile();
			}
		});
		mntmNewItem.setText("Add Deployment Files");
		
		
		
		
		
		/**************************************** Traceability Menu ****************************************/
		MenuItem mntmTraceability = new MenuItem(menu, SWT.CASCADE);
		mntmTraceability.setText("Traceability");
		
		Menu menu_2 = new Menu(mntmTraceability);
		mntmTraceability.setMenu(menu_2);
		
		MenuItem mntmCreateLinks = new MenuItem(menu_2, SWT.NONE);
		mntmCreateLinks.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				//creating treaseability links
				GexfGraph gexf = new GexfGraph();
				gexf.createGexfGraph();
				Gephi gephi = new Gephi();
				gephi.saveAsPdf();
			}
		});
		mntmCreateLinks.setText("Create Links");
		
		

		Label label = new Label(shlDeploymentTraceability, SWT.BORDER );
		//Display display = new Display();
		
		/**************************************** Visualization Menu ****************************************/
		MenuItem mntmVisualization = new MenuItem(menu, SWT.CASCADE);
		mntmVisualization.setText("Visualization");
		
		Menu menu_3 = new Menu(mntmVisualization);
		mntmVisualization.setMenu(menu_3);
		
		MenuItem mntmNewItem_2 = new MenuItem(menu_3, SWT.NONE);
		mntmNewItem_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				//add the jpeg to window
			    //label.setText("text on the label");
				Image img = new Image( display, "graph.png");
				Image finalImg = resize( img, 0, 0);
			    label.setImage(finalImg);
			}
		});
		mntmNewItem_2.setText("Visualize");
				
		
		
		
		
		Canvas canvas = new Canvas(shlDeploymentTraceability, SWT.NONE);
		
		canvas.setBounds(0, 0, 2824, 874);
		
      
     
		
		Label lblNewLabel = new Label(canvas, SWT.NONE);
		lblNewLabel.setImage(SWTResourceManager.getImage("E:\\SATAnalyser\\graph.png"));
		lblNewLabel.setBounds(-14, 0, 904, 710);

		
		
	}
	
		
	
	private Image resize(Image image, int width, int height) {
		Image scaled = new Image(Display.getDefault(), width, height);
		GC gc = new GC(scaled);
		gc.setAntialias(SWT.ON);
		gc.setInterpolation(SWT.HIGH);
		gc.drawImage(image, 0, 0,	
		image.getBounds().width, image.getBounds().height,
		0, 0, width, height);
		gc.dispose();
		image.dispose(); // clear memory
		return scaled;
		}
	
	
	
	
}
