package com.project.extendedsat.test;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RelationFileToArray {

	public ArrayList<HashMap<String,String>> toArray(String filePath){
		File xmlFile = new File(filePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        
        
        ArrayList<HashMap<String,String>> list= new ArrayList<HashMap<String,String>>();
        
        try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
	        doc.getDocumentElement().normalize();
	        
	        NodeList nodeList = doc.getElementsByTagName("Relation");
	        
	        for (int i= 0; i< nodeList.getLength(); i++) {

	            Node nNode = nodeList.item(i);
	            //System.out.println(nNode);
	            if (nNode.getNodeType() == Node.ELEMENT_NODE) {

	                Element eElement = (Element) nNode;
	                Map<String,String> map= new HashMap<String,String>(); 
	                   // System.out.println("name: " + eElement.getAttribute("name"));
	                    //System.out.println("id: " + eElement.getAttribute("id"));
	                    //list.add(getAttrbutes(Element eElement));
	              
	                map.put("SourceNode", eElement.getElementsByTagName("SourceNode").item(0).getTextContent());
	                map.put("TargetNode", eElement.getElementsByTagName("TargetNode").item(0).getTextContent());
	                map.put("RelationPath", eElement.getElementsByTagName("RelationPath").item(0).getTextContent());
	                
	               
	                   list.add((HashMap<String, String>) map);
	                   
	                }
	            	
	            

	        }

	        
	        
	        
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
		
	}
}
