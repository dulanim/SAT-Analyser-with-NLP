package com.project.property.xml.reader;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.project.NLP.staticdata.StaticData;


public class XMLReader {


	
	public void read(){
		File fXmlFile = new File("C:\\Users\\shiyam\\sat_config.xml");
		
		try{
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		
		NodeList nList = doc.getElementsByTagName("Config");
		StaticData.paths = new String[nList.getLength()];
			for(int i=0;i<nList.getLength();i++){
				Node node = nList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) node;
					Boolean status = Boolean.parseBoolean(eElement.getAttribute("active"));
					NodeList childList = eElement.getElementsByTagName("Root");
					Element rootElement = (Element)childList.item(0);
					if(status){
						
						StaticData.rootPathName = rootElement.getAttribute("filepath");
						
						NodeList folderList = rootElement.getElementsByTagName("Folder");
						StaticData.fileNames = new String [folderList.getLength()];
						for(int j=0;j<folderList.getLength();j++){
							Element element = (Element)folderList.item(j);
							StaticData.fileNames[j] = element.getAttribute("name");
						}
						StaticData.paths[i] = rootElement.getAttribute("filepath") + "*";
					}else{
						StaticData.paths[i] = rootElement.getAttribute("filepath");
					}
					
				}
			}
		}catch(Exception e){
			
		}
	        
	}
}
