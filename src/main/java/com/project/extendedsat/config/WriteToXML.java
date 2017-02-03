package com.project.extendedsat.config;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
/**
*
*
* @author SAMITHA
*/
public class WriteToXML {
	public static Document document;
	static String file;
	public WriteToXML(String Filename){
		this.file=Filename;
	}
	public static void convertToXML(HashMap<String, String> hmapa){
		
		HashMap<String, String> hmap = new HashMap<String, String>();
		hmap = hmapa;
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				
				
				document = dBuilder.newDocument();
				Element root = document.createElement("Artefacts");
				document.appendChild(root);		
				Element sub = document.createElement("Artefact");
				root.appendChild(sub);	
				
				Attr type = document.createAttribute("type");
				type.setValue("PropertyFile");
				sub.setAttributeNode(type);
				
				
				Set set = hmap.entrySet(); // Get a set of the entries
				Iterator i = set.iterator(); // Get an iterator
				int count=1;
				while (i.hasNext()) {
					Element ArtefactElement = document.createElement("ArtefactElement");
					sub.appendChild(ArtefactElement);					
					Map.Entry me = (Map.Entry) i.next(); // getting an entry
					String name= (String) me.getKey();
					String value = (String) me.getValue();
					Attr nam = document.createAttribute("name");
					nam.setValue(name);
					ArtefactElement.setAttributeNode(nam);		
					
					//this code is for set element value
					/*ArtefactElement.appendChild(document.createTextNode(value));
					sub.appendChild(ArtefactElement);*/
					
					
					Attr val = document.createAttribute("value");
					val.setValue(value);
					ArtefactElement.setAttributeNode(val);
					
					
					if(file=="ConfigurationArtefactFile.xml"){
						Attr id = document.createAttribute("id");
						id.setValue("P"+count);
						count++;
						ArtefactElement.setAttributeNode(id);
					}
					if(file=="ConfigurationFile.xml"){
						Attr id = document.createAttribute("id");
						id.setValue("C"+count);
						count++;
						ArtefactElement.setAttributeNode(id);
					}					
					
					Attr field = document.createAttribute("type");
					field.setValue("Field");
					ArtefactElement.setAttributeNode(field);
					
					Attr visibility = document.createAttribute("Visibility");
					visibility.setValue("Private");
					ArtefactElement.setAttributeNode(visibility);
					
					Attr variableType = document.createAttribute("variableType");
					variableType.setValue("String");
					ArtefactElement.setAttributeNode(variableType);
				}
				
					Transformer transformer = TransformerFactory.newInstance().newTransformer();
					transformer.setOutputProperty(OutputKeys.INDENT, "yes");
					transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
					transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
					
					//transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
					DOMSource source = new DOMSource(document);
					StreamResult target = new StreamResult(new File(file).getAbsolutePath());
					transformer.transform(source, target);

				
				
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerFactoryConfigurationError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TransformerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		
	}
}
