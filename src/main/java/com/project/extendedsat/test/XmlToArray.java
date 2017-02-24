package com.project.extendedsat.test;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.management.AttributeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class XmlToArray {

	public static ArrayList<AttributeList> parseXML(String filePath){
		//String filePath = "ConfigurationArtefactFile.xml";
		File xmlFile = new File(filePath);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		//ArrayList<Property> list = new ArrayList<Property>();
		ArrayList<AttributeList> list= new ArrayList<AttributeList>();
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();
	        
	       /* XPathFactory factory = XPathFactory.newInstance();
	        XPath xpath = factory.newXPath();*/

			NodeList nodeList = doc.getElementsByTagName("ArtefactSubElement");

			//System.out.println(nodeList.toString());
	      /*  for (int i = 0; i < nodeList.getLength(); i++) {
                //list.add(getProperty(nodeList.item(i)));

            }*/


			for (int i= 0; i< nodeList.getLength(); i++) {

				Node nNode = nodeList.item(i);
				//System.out.println(nNode);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;

					// System.out.println("name: " + eElement.getAttribute("name"));
					//System.out.println("id: " + eElement.getAttribute("id"));
					//list.add(getAttrbutes(Element eElement));
					AttributeList l =  Property.getAttributes(eElement,filePath);
					if(!(l.get(2).toString().equals("UMLOperation")||l.get(2).toString().equals("Field")))
					list.add(l);
				}



			}
			//  System.out.println(Arrays.toString(l.toArray()));

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
