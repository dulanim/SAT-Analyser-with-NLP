package com.project.extendedsat.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.HeadlessException;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.api.ProcessingTarget;
import org.gephi.preview.api.RenderTarget;
import org.gephi.preview.types.DependantOriginalColor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Lookup;

import com.project.extendedsat.config.TestGraph;

import processing.core.PApplet;

public class TraceabilityVisualization  extends JFrame {
	static String configname = "PropertyFileToConfigurationFile.gexf";
	static String testname = "SourceToTest.gexf";
	static String deployname = "DiployentToDocker.gexf";
	public JTabbedPane tp = pane();

	
	
	public JTabbedPane getTp() {
		return tp;
	}


	public void setTp(JTabbedPane tp) {
		this.tp = tp;
	}


	public TraceabilityVisualization(String configname, String testname, String deployname) {
	
		this.configname = configname;
		this.testname = testname;
		this.deployname = deployname;
	}


	public JPanel config(String configname){
		
		
		 ProjectController pc = Lookup.getDefault().lookup(ProjectController.class); 
	        pc.newProject(); 
	        Workspace workspace = pc.getCurrentWorkspace(); 
	 
	        //Import file 
	        ImportController importController = Lookup.getDefault().lookup(ImportController.class); 
	        Container container=null; 
	        try { 
	          //  File file = new File("PropertyFileToConfigurationFile.gexf"); 
	        	 File file = new File(configname); 
	            container = importController.importFile(file); 
	        } catch (Exception ex) { 
	            ex.printStackTrace(); 
	            
	        } 
	 
	        //Append imported data to GraphAPI 
	        importController.process(container, new DefaultProcessor(), workspace); 
	 
	        //Preview configuration 
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
	        previewModel.getProperties().putValue(PreviewProperty.EDGE_RADIUS, 10f); 
	        previewModel.getProperties().putValue(PreviewProperty.BACKGROUND_COLOR, Color.WHITE); 
	        previewController.refreshPreview(); 
	 
	        //New Processing target, get the PApplet 
	        ProcessingTarget target = (ProcessingTarget) previewController.getRenderTarget(RenderTarget.PROCESSING_TARGET); 
	        PApplet applet = target.getApplet(); 
	        applet.init(); 
	 
	        //Refresh the preview and reset the zoom 
	        previewController.render(target); 
	        target.refresh(); 
	        target.resetZoom(); 
	 
	        //Add the applet to a JFrame and display 
	        JPanel panel1 = new JPanel(); 
	        panel1.setLayout(new BorderLayout()); 

	        panel1.add(applet, BorderLayout.CENTER); 
	        //new JPanel().add(applet,BorderLayout.CENTER);
	        panel1.setSize(1650,1080);
	        return panel1;
	}
	
	public JPanel test(String testname){
		 ProjectController pc = Lookup.getDefault().lookup(ProjectController.class); 
	        pc.newProject(); 
	        Workspace workspace = pc.getCurrentWorkspace(); 
	 
	        //Import file 
	        ImportController importController = Lookup.getDefault().lookup(ImportController.class); 
	        Container container=null; 
	        try { 
	          //  File file = new File("PropertyFileToConfigurationFile.gexf"); 
	        	 File file = new File(testname); 
	            container = importController.importFile(file); 
	        } catch (Exception ex) { 
	            ex.printStackTrace(); 
	            
	        } 
	 
	        //Append imported data to GraphAPI 
	        importController.process(container, new DefaultProcessor(), workspace); 
	 
	        //Preview configuration 
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
	        previewModel.getProperties().putValue(PreviewProperty.EDGE_RADIUS, 10f); 
	        previewModel.getProperties().putValue(PreviewProperty.BACKGROUND_COLOR, Color.WHITE); 
	        previewController.refreshPreview(); 
	 
	        //New Processing target, get the PApplet 
	        ProcessingTarget target = (ProcessingTarget) previewController.getRenderTarget(RenderTarget.PROCESSING_TARGET); 
	        PApplet applet = target.getApplet(); 
	        applet.init(); 
	 
	        //Refresh the preview and reset the zoom 
	        previewController.render(target); 
	        target.refresh(); 
	        target.resetZoom(); 
	 
	        //Add the applet to a JFrame and display 
	        JPanel panel2 = new JPanel(); 
	        panel2.setLayout(new BorderLayout()); 

	        panel2.add(applet, BorderLayout.CENTER); 
	        //new JPanel().add(applet,BorderLayout.CENTER);
	        panel2.setSize(1650,1080);
	        return panel2;
		
	}
	
	public JPanel deploy(String deployname){
		 ProjectController pc = Lookup.getDefault().lookup(ProjectController.class); 
	        pc.newProject(); 
	        Workspace workspace = pc.getCurrentWorkspace(); 
	 
	        //Import file 
	        ImportController importController = Lookup.getDefault().lookup(ImportController.class); 
	        Container container=null; 
	        try { 
	          //  File file = new File("PropertyFileToConfigurationFile.gexf"); 
	        	 File file = new File(deployname); 
	            container = importController.importFile(file); 
	        } catch (Exception ex) { 
	            ex.printStackTrace(); 
	            
	        } 
	 
	        //Append imported data to GraphAPI 
	        importController.process(container, new DefaultProcessor(), workspace); 
	 
	        //Preview configuration 
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
	        previewModel.getProperties().putValue(PreviewProperty.EDGE_RADIUS, 10f); 
	        previewModel.getProperties().putValue(PreviewProperty.BACKGROUND_COLOR, Color.WHITE); 
	        previewController.refreshPreview(); 
	 
	        //New Processing target, get the PApplet 
	        ProcessingTarget target = (ProcessingTarget) previewController.getRenderTarget(RenderTarget.PROCESSING_TARGET); 
	        PApplet applet = target.getApplet(); 
	        applet.init(); 
	 
	        //Refresh the preview and reset the zoom 
	        previewController.render(target); 
	        target.refresh(); 
	        target.resetZoom(); 
	 
	        //Add the applet to a JFrame and display 
	        JPanel panel3 = new JPanel(); 
	        panel3.setLayout(new BorderLayout()); 

	        panel3.add(applet, BorderLayout.CENTER); 
	       // new JPanel().add(applet,BorderLayout.CENTER);
	        panel3.setSize(1650,1080);
	        return panel3;
		
	}
	public JTabbedPane pane(){
		// TODO Auto-generated constructor stub
				setTitle("Tabbed Pane");
		        JTabbedPane jtp = new JTabbedPane();
		        getContentPane().add(jtp);
		        jtp.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		   
		        
		        
				return jtp;
		        
	}
	   public static void main(String[] args) {
	         
		   JFrame frame = new JFrame("Traceability Vizualizer");
		    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    
		   TraceabilityVisualization tp = new TraceabilityVisualization(configname, testname, deployname);
//	        tp.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	        tp.setVisible(true);
	         	        
	        frame.add(tp.pane(), BorderLayout.CENTER);
	        frame.setSize(1366, 768);
	        frame.setVisible(true);
	    }
}
