package com.project.extendedsat.deployment.db;

import java.io.File;
import java.io.FileNotFoundException;

import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

public class Gephi {

	
	public void saveAsPdf(){
		File graphmlFile = new File("graph.gexf");

		//Init a project - and therefore a workspace
		ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
		pc.newProject();
		Workspace workspace = pc.getCurrentWorkspace();

		// get import controller
		ImportController importController = Lookup.getDefault().lookup(ImportController.class);

		//Import file
		Container container;
		try {
			container = importController.importFile(graphmlFile);
			//Append imported data to GraphAPI
			importController.process(container, new DefaultProcessor(), workspace);

			//Export graph to PDF
			ExportController ec = Lookup.getDefault().lookup(ExportController.class);
			ec.exportFile(new File("graph.png"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	
	
	public static void main(String args[]){
		Gephi g = new Gephi();
		g.saveAsPdf();
	}



}
