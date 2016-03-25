package com.project.extension.event.trigger;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.project.NLP.SourceCodeToXML.AST;
import com.project.NLP.SourceCodeToXML.CompareModule;
import com.project.NLP.SourceCodeToXML.WriteToXML;
import com.project.NLP.file.operations.FilePropertyName;
import com.project.property.config.xml.reader.XMLReader;
import com.project.traceability.staticdata.StaticData;

public class EventTrigger {

	private static DocumentBuilderFactory docFactory = null;
	public static String sourceCodeDir = System.getProperty("user.home") + File.separator + "Desktop/SatWrks/NewSatWrkspaceskal/src"; // java project file path 
	public static String projectName = "";
	public static String projectPath = "";
	
	public static void main(String args[]){
		
		EventTrigger.projectName = "NewSatWrkspaceskal";
		if(args != null && args.length >0){
			//getting project name from running time argument
			EventTrigger.projectName = args[0];
		}else{
			EventTrigger.projectName = "NewSatWrkspaceskal"; // Dummy Project Name
		}
		EventTrigger.projectPath = getProjectPath(projectName);
		EventTrigger.sourceCodeDir = getJenkinsSourcePath(projectName);
		
		//if project does not exists return
		if(EventTrigger.projectPath.equals("") || 
				EventTrigger.projectPath.isEmpty()){
			
//			System.out.println("System running has stopped because "
//					+ "no file exists in any workspace");
			return ;
		}
		//if project exists start to run
		new EventTrigger().init(EventTrigger.projectPath);
	}
	public static String getProjectPath(String projectName) {
		//get the actual project from SAT workspace
		
		/*
		 * first read sat_configuration.xml file for matching project name with 
		 * project name if it founds 
		 * 
		 * if not throw an exception cancel the running step 
		 */
		
		XMLReader reader = new XMLReader();
		String path = reader.readForProjectName(projectName);
		return path;
	}
	private static String getJenkinsSourcePath(String projectName) {
		// TODO Auto-generated method stub
		
		String testPath = "/home/shiyam/Desktop/SatWrks/NewSatWrkspaceskal/src";
		if(projectName.equals("") || projectName.isEmpty()){
		
			String localRoot = FilePropertyName.LOCAL_DIR_JENKINS;
			
			testPath = localRoot + projectName + File.separator;
		}
		return testPath;
	}
	

	public void init(String projectPath){
		///initialization starts when trigger event from JENKINS
		boolean isCopied = copyFile(projectPath);// run this method to copy old sourceArtefact.xml to old_version.xml
		
		if(isCopied) {
			
			WriteToXML.isTragging = "Tragging";
			WriteToXML.TAG = "NEW";
			runAntlr(sourceCodeDir);
			
			WriteToXML.isTragging = "NONE";
			WriteToXML.TAG = "NONE";
			
			File file = new File(FilePropertyName.getXMLFilePath(projectPath, "NEW"));
			System.out.println("Finished New Copying File new_version.xml  " +  file.getPath() + "?" +
						file.exists());
			
			
			while(!file.exists());
			
			CompareModule.compareFiles(projectPath);
			CompareModule.writeToXML();
			
			System.out.println("Finished New Copying File modified_version.xml  ");
		}
	}
	
	private boolean copyFile(String projectPath){
		///sourceArtefact.xml to old_version.xml
		//when copy data modify the ID of each artifact 
			
		//ID becomes from "SC_" to "OLD_SC_" format
		
		//String fileRoot = getXMLFileRootPath(projectPath);// + File.separator + FilePropertyName.XML + File.separator;
		final String TAG = "OLD";
		boolean isCopied = false;
		File file = new File(FilePropertyName.getXMLFilePath(projectPath, "DEFAULT"));
		if(!file.exists()){
			runAntlr(sourceCodeDir);
			isCopied = copyFile(projectPath);
			
		}else{
			File destFile = new File(FilePropertyName.getXMLFilePath(projectPath, "OLD"));
			try {
				FilePropertyName.copyTwoFile(file, destFile);
				System.out.println("File copied in " + file.getAbsolutePath() + " Copied New file is " + destFile.getPath());
			} catch (IOException ex) {
	            Logger.getLogger(EventTrigger.class.getName()).log(Level.SEVERE, null, ex);
			}
			try {
				changeArtefactElementID(TAG,destFile.getPath(),StaticData.ARTEFACTELEMENT_ROOT);
				changeArtefactSubElementID(TAG,destFile.getPath());
				if(destFile.exists())
					isCopied = true;
			} catch (ParserConfigurationException | SAXException | IOException ex) {
				// TODO Auto-generated catch block
				Logger.getLogger(EventTrigger.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		
		return isCopied;
	}
	private void changeArtefactSubElementID(String tAG, String filepath) {
		// TODO Auto-generated method stub
		//this method changes existing ID to this format
		//format will be tAG_SC_ or tAG_SC
		try {
			changeArtefactElementID(tAG,filepath,StaticData.ARTEFACTUBELEMENT_ROOT);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void changeArtefactElementID(String tag,String filepath,String idTag) throws ParserConfigurationException,
								SAXException, IOException {
		//this method changes existing ID to this format
		//format will be OLD_SC_
		Document doc = getDocumnet(filepath);
		NodeList nodeList = doc.getElementsByTagName(idTag);
		
		if(nodeList != null){
			for(int i=0;i<nodeList.getLength();i++){
				Node node = nodeList.item(i);
				Element artefactElmnt = (Element) node;
				
				//Attr idAttr = artefactElmnt.getAttributeNode(StaticData.ID_ROOT);
				NamedNodeMap attr = artefactElmnt.getAttributes();
	            Node nodeAttr = attr.getNamedItem(StaticData.ID_ROOT);
	            
				String oldIDValue = nodeAttr.getTextContent();
				String newValue = tag + "_"+oldIDValue;
				nodeAttr.setTextContent(newValue);
				FilePropertyName.writeToXML(filepath, doc);//modified
			}

			//System.out.println("------XML File has been saved or modfied in " + filepath + " successfully------");
		}
	}

	private void runAntlr(String sourceCodeDir){
		///produce new_version.xml 
		try {
			new AST().startSourceCodeConversion(sourceCodeDir);
		} catch (Exception ex) {
			Logger.getLogger(EventTrigger.class.getName()).log(Level.SEVERE, null, ex);
		}
		
	}
	
	private Document getDocumnet(String filepath) throws ParserConfigurationException, 
						SAXException, IOException{
		DocumentBuilderFactory docFactory = EventTrigger.getDocumentBuildFactoryInstance();
	    DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	    Document doc = docBuilder.parse(filepath);    
	    
	    return doc;
	}
	
	private static DocumentBuilderFactory getDocumentBuildFactoryInstance(){
		if(docFactory == null)
			docFactory = DocumentBuilderFactory.newInstance();
		return docFactory;
	}
}
