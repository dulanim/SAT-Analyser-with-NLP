package com.project.property.xml.writer;


import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
public class XMLWriter {
	String filepath = "C:\\Users\\shiyam\\sat_config.xml";
	DocumentBuilderFactory docFactory;
	DocumentBuilder docBuilder;
	Document doc;
	public XMLWriter(){
		
		try{
			File file = new File(filepath);
			if(!file.exists())
				{
					file.createNewFile();
					docFactory = DocumentBuilderFactory.newInstance();
					docBuilder = docFactory.newDocumentBuilder();
					doc = docBuilder.parse(filepath);
					create_root();
				}
			docFactory = DocumentBuilderFactory.newInstance();
			docBuilder = docFactory.newDocumentBuilder();
			doc = docBuilder.parse(filepath);
		}catch(Exception e){
			
		}
		
	}
	private void create_root(){
		
		FileWriter reader = new F
		call_done();
	}
	public void modifyStatus(Boolean active,String path){
		//switch project path
		try {
			NodeList nList = doc.getElementsByTagName("Config");
			for(int i=0;i<nList.getLength();i++){
				Element element = (Element)nList.item(i);
				NodeList rootList = element.getElementsByTagName("Root");
				
				for(int j=0;j<rootList.getLength();j++){
					String tempPath = ((Element)rootList.item(0)).getAttribute("filepath");
					if(tempPath.equals(path)){
						// update  attribute
						NamedNodeMap attr = element.getAttributes();
						Node nodeAttr = attr.getNamedItem("active");
						nodeAttr.setTextContent(Boolean.toString(active));
						call_done();
						return ;
					}
				}
				
			}
			
			
		}catch(Exception e){
			
		}
	}
	public void createMainNode(String path){
		//create main project path
		Node root = doc.getElementsByTagName("Configs").item(0);
		Element config = doc.createElement("Config");
		
		Attr NameAttr = doc.createAttribute("active");
        NameAttr.setValue("true");
        config.setAttributeNode(NameAttr);
        
        Element rootElmnt = doc.createElement("Root");
        Attr Attr = doc.createAttribute("path");
        Attr.setValue("");
        rootElmnt.setAttributeNode(Attr);
        
        Element foldersElmnt = doc.createElement("Folders");
        
        
        config.appendChild(rootElmnt);
        rootElmnt.appendChild(foldersElmnt);
        root.appendChild(config);
        
        call_done();
	}
	public void deleteProjectFolder(String path,String folderName){
		try {
			NodeList nList = doc.getElementsByTagName("Config");
			for(int i=0;i<nList.getLength();i++){
				Element element = (Element)nList.item(i);
				NodeList rootList = element.getElementsByTagName("Root");
				
				for(int j=0;j<rootList.getLength();j++){
					Element root = ((Element)rootList.item(j));
					Element parent = (Element)root.getElementsByTagName("Folders").item(0);
					String tempPath = root.getAttribute("filepath");
					if(tempPath.equals(path)){
						// delete  folder name
						
						NodeList folderList = parent.getElementsByTagName("Folder");
						for(int k=0;k<folderList.getLength();k++){
							String foldername = ((Element)folderList.item(k)).getAttribute("name");
							if(foldername.equals(folderName)){
								parent.removeChild(folderList.item(k));
								call_done();
								return;
							}
						}
					
					}
				}//inner for loop finish
			}//outer for loop finish
		}catch(Exception e){
			
		}
	}
	public void createSubFolderName(String path,String folderName){
		
		//when project adding to specific path or a folder adding into path
		try {
			NodeList nList = doc.getElementsByTagName("Config");
			for(int i=0;i<nList.getLength();i++){
				Element element = (Element)nList.item(i);
				NodeList rootList = element.getElementsByTagName("Root");
				
				for(int j=0;j<rootList.getLength();j++){
					Element root = ((Element)rootList.item(j));
					Element parent = (Element)root.getElementsByTagName("Folders").item(0);
					String tempPath = root.getAttribute("filepath");
					if(tempPath.equals(path)){
						
						// append a new node to Root
						Element folder = doc.createElement("Folder");
					
	                    Attr NameAttr = doc.createAttribute("name");
	                    NameAttr.setValue(folderName);
	                    folder.setAttributeNode(NameAttr);
	                    
	                    Attr createTimeAttr = doc.createAttribute("createTime");
	                    createTimeAttr.setValue("");
	                    folder.setAttributeNode(createTimeAttr);
	                    
	                    Attr modifedTimeAttr = doc.createAttribute("modifiedTime");
	                    modifedTimeAttr.setValue("");
	                    folder.setAttributeNode(modifedTimeAttr);	
	                    
	                    parent.appendChild(folder);

	                    call_done();
	                    return ;
					}
				}
				
			}
			
			
		}catch(Exception e){
			
		}
	}
	
	public void call_done(){
		try{
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");//No I18N
	        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");//No I18N
			DOMSource source = new DOMSource(doc);
	         //String driveLetter = getSuitableDrive();
	                
	                File f = new File(filepath);
	                StreamResult result = new StreamResult(f.getPath());
	                transformer.transform(source, result);
	                System.out.println("File saved into " + f.getPath());
		
				System.out.println("Done");
		}catch (TransformerException tfe) {
			tfe.printStackTrace();
		 }catch(Exception e){
			 
		 }
	}
}
