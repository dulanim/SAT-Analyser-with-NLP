package com.project.traceability.ontology.models;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class HelpFileReader {

	
	private String fileName = "sat_help.xml";
	private String filePath = System.getProperty("user.dir") + File.separator + "res"
			+ File.separator;
	
	private String absoulutePath;
	

    DocumentBuilderFactory dbFactory;
    DocumentBuilder dBuilder;
    Document doc;
	public HelpFileReader() {
		// TODO Auto-generated constructor stub'
		absoulutePath = filePath + fileName;
		
		   File config_XmlFile = new File(absoulutePath);
			
				try{
	                    dbFactory = DocumentBuilderFactory.newInstance();
	                    dBuilder = dbFactory.newDocumentBuilder();
	                    doc = dBuilder.parse(config_XmlFile);
	                }catch(SAXException e){
	                    JOptionPane.showMessageDialog(null, e.toString(), "Read XML Error", JOptionPane.ERROR_MESSAGE);
	                }catch(ParserConfigurationException e){
	                    JOptionPane.showMessageDialog(null, e.toString(), "Read XML Error", JOptionPane.ERROR_MESSAGE);
	                }catch(IOException e){
	                    JOptionPane.showMessageDialog(null, e.toString(), "Read XML Error", JOptionPane.ERROR_MESSAGE);
	                }
	        
	}
	
	
	public List<Helps> readWordExpWindowHelps(){
		/*
		 * reads help system information from sat_help.xml file 
		 * which is in home of users machine
		 * this method actually reads the WordAdditionHelp node
		 */
		List<Helps> helpList = new ArrayList<>();
		try{
            //get workspaces
            NodeList nList = doc.getElementsByTagName("WordAdditionHelp");
            Element eRoot = (Element)nList.item(0);
            
            NodeList helpsList = eRoot.getElementsByTagName("Help");
            Helps aHelp;
            for(int i=0;i<helpsList.getLength();i++){
            	
            	Element helpElmnt = (Element)helpsList.item(i);
            	String title = helpElmnt.getAttribute("title");
            	String tag = helpElmnt.getAttribute("tag");
            	aHelp = new Helps();
            	aHelp.setHelpTitlte(title);
            	aHelp.setHelpTag(tag);
            	
            	NodeList tipsElmnt = helpElmnt.getElementsByTagName("description");
            	
            	for(int j=0;j<tipsElmnt.getLength();j++){
            		Element desc1 = (Element)tipsElmnt.item(j);
            		String aTip = desc1.getAttribute("content");
            		aHelp.setDescriptions(aTip);
            	}
            	helpList.add(aHelp);
            }
            
            
       }catch(Exception e){
           JOptionPane.showMessageDialog(null, e.toString(), "Read XML Configuration Status Error", JOptionPane.ERROR_MESSAGE);
       }
		return helpList;
		
	}
	
}
