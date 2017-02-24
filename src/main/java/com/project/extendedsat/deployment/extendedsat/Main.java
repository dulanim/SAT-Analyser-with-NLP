/*
 * TNS
 * 
 * 
 */


package com.project.extendedsat.deployment.extendedsat;



import java.util.ArrayList;

import com.project.extendedsat.deployment.GUI.AddDeploymentFilesGUI;
import com.project.extendedsat.deployment.GUI.CreatingTraceabilityGUI;
import com.project.extendedsat.deployment.GUI.DeploymentTraceabilityGUI;



public class Main {

	public String deploymentDiagram, dockerFile;	
	
	public Main(String deploymentDiagram, String dockerFile) {
		super();
		this.deploymentDiagram = deploymentDiagram;
		this.dockerFile = dockerFile;
	}

	public void startDeployment( ){	
		
		
//		//handling input files
//		AddDeploymentFilesGUI depFiles = new AddDeploymentFilesGUI(); //);
//		depFiles.open();
//		deploymentDiagram = depFiles.getDeploymentDiagram();
//		dockerFile = depFiles.getDockerFile();
//		
//		//test
//		System.out.println( deploymentDiagram );
//		System.out.print( dockerFile );
//		
		
//		DeploymentTraceabilityGUI traceabilityGUI = new DeploymentTraceabilityGUI( deploymentDiagram, dockerFile );
//		traceabilityGUI.open();
		
//		TestGraph t = new TestGraph();
//		t.script();
		
		/*
		ArrayList<String> files = depFiles.getFiles();
		
		
		for( String s : files ){
			System.out.println(s);
		}
		
		*/
		
		
	}
	
	
}




