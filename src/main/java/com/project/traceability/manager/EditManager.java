/**
 * 
 */
package com.project.traceability.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.swt.widgets.TreeItem;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.project.traceability.GUI.HomeGUI;
import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.ArtefactSubElement;
import com.project.traceability.model.AttributeModel;


/**
 * @author Gitanjali Dec 1, 2014
 */
public class EditManager {

	public static void main(String args[]) {
		// addLink("Customer", "accountNumber");
	}

	public static void addLink(Object className,
			Object nextName) {
		System.out.println(className + " " + nextName);
		List<String> newNode = new ArrayList<String>();
		if(className instanceof ArtefactElement)
			newNode.add(((ArtefactElement) className).getArtefactElementId());
		else if(className instanceof ArtefactSubElement)
			newNode.add(((ArtefactSubElement) className).getSubElementId());
		String desString = "";
		if(className instanceof ArtefactSubElement && nextName instanceof ArtefactSubElement){
			
			//if(className.getClass().equals(ArtefactSubElement.class) && nextName.getClass().equals(ArtefactSubElement.class)){						
				desString = createSubElementDescription(className, nextName);
				System.out.println(desString);
				if(desString.equals(""))
					desString = createSubElementDescription(nextName, className);
				
			} else if(className.getClass().equals(ArtefactElement.class) && nextName.getClass().equals(ArtefactElement.class)){
					desString = createArefactDescription(className, nextName);
					if(desString.equals(""))
						desString = createArefactDescription(nextName, className);
					
			}
		newNode.add(desString);
		
		if(nextName instanceof ArtefactElement)
			newNode.add(((ArtefactElement) nextName).getArtefactElementId());
		else if(nextName instanceof ArtefactSubElement)
			newNode.add(((ArtefactSubElement) nextName).getSubElementId());
		RelationManager.addLinks(newNode);
	}
	
	public static void deleteLink(TreeItem treeItem){				//delete link
		TreeItem[] attributes = treeItem.getItems()[0].getItems();
		TreeItem[] methods = treeItem.getItems()[1].getItems();
		TreeItem[] items = new TreeItem[attributes.length + methods.length +1];
		items = (TreeItem[]) ArrayUtils.addAll(attributes, methods);
		items[items.length - 1]  = treeItem;
		items[items.length - 1] = treeItem;
		for(int count = 0; count < items.length; count++){
			Object obj1 = items[count].getData("0");
			Object obj2 = items[count].getData("1");
			String sourceId = null;
			String targetId = null;
			if(obj1 instanceof ArtefactElement && obj2 instanceof ArtefactElement){
				sourceId = ((ArtefactElement) obj1).getArtefactElementId();
				targetId = ((ArtefactElement) obj2).getArtefactElementId();
			} else if(obj1 instanceof ArtefactSubElement && obj2 instanceof ArtefactSubElement){
				sourceId = ((ArtefactSubElement) obj1).getSubElementId();
				targetId = ((ArtefactSubElement) obj2).getSubElementId();
			}
			
			if(sourceId != null && targetId != null){
				if(sourceId.length() > 4)
					sourceId = sourceId.substring(sourceId.length() - 3);
				if(targetId.length() > 4)
					targetId = targetId.substring(targetId.length() - 3);
				boolean found = false;
		       
		        File file = new File(HomeGUI.projectPath + "\\Relations.xml");
		
		        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		        DocumentBuilder dBuilder;
		        try {
		            dBuilder = dbFactory.newDocumentBuilder();
		            Document doc = (Document) dBuilder.parse(file);
		            doc.getDocumentElement().normalize();
		
		            NodeList artefactNodeList = doc
		                    .getElementsByTagName("Relations");
		
		            for (int i = 0; i < artefactNodeList.getLength() && found != true; i++) {
		
		                Node artefactNode = (Node) artefactNodeList.item(i);
		                if (artefactNode.getNodeType() == Node.ELEMENT_NODE) {
		
		                    NodeList artefactElementList = doc
		                            .getElementsByTagName("Relation");
		  
		                    for (int j = 0; j < artefactElementList.getLength() && !found; j++) {
		                    	
		                       Node artefactElementNode = (Node) artefactElementList
		                                .item(j);
		                        Element artefact = (Element) artefactElementNode;
		                        String source = artefact.getElementsByTagName("SourceNode").item(0).getTextContent();
		                        String target = artefact.getElementsByTagName("TargetNode").item(0).getTextContent();
		                        String artefact_id = artefact.getAttribute("id");
		                        if ((source.equals(sourceId) || source.equals(targetId)) && (target.equals(sourceId) || target.equals(targetId))) {
		                        	artefactElementNode.getParentNode().removeChild(artefactElementNode);
		                            found = true;
		                            System.out.println("Artefact " + artefact_id + " deleted");
		                            break;
		                        }
		                        
		                    }
		                }
		            }
		            TransformerFactory transformerFactory = TransformerFactory.newInstance();
		            Transformer transformer = transformerFactory.newTransformer();
		            DOMSource source = new DOMSource(doc);
		            StreamResult result = new StreamResult(new File(HomeGUI.projectPath + "\\Relations.xml").getPath());
		            transformer.transform(source, result);
		        } catch (ParserConfigurationException e) {
		            e.printStackTrace();
		        } catch (SAXException e) {
		            e.printStackTrace();
		        } catch (IOException e) {
		            e.printStackTrace();
		        } catch (TransformerConfigurationException e) {
		            e.printStackTrace();
		        } catch (TransformerException e) {
		            e.printStackTrace();
		        }
			}
		}
	}
	
	private static String createArefactDescription(Object className, Object nextName){
		String desString = "";
		if(((ArtefactElement) className).getArtefactElementId().contains("RQ") && 
				((ArtefactElement) nextName).getArtefactElementId().contains("SC")){
			desString += "ReqClassToSourceClass";
		}
		else if(((ArtefactElement) className).getArtefactElementId().contains("RQ") && 
				((ArtefactElement) nextName).getArtefactElementId().contains("D"))
			desString += "ReqClassToUMLClass";
		else if(((ArtefactElement) className).getArtefactElementId().contains("D") && 
				((ArtefactElement) nextName).getArtefactElementId().contains("SC"))
			desString += "UMLClassToSourceClass";
		return desString;
	}
	
	private static String createSubElementDescription(Object className, Object nextName){
		String desString = "";
		if(((ArtefactSubElement) className).getSubElementId().contains("RQ") && 
				((ArtefactSubElement) nextName).getSubElementId().contains("SC"))
			if(className instanceof AttributeModel)
				desString += "ReqAttributToSourceAttribute";
			else 
				desString += "ReqMethodToSourceMethod";
		else if(((ArtefactSubElement) className).getSubElementId().contains("RQ") && 
				((ArtefactSubElement) nextName).getSubElementId().contains("D"))
			if(className instanceof AttributeModel)
				desString += "ReqAttributeToUMLAttribute";
			else 
				desString += "ReqMethodToUMLMethod";
		else if(((ArtefactSubElement) className).getSubElementId().contains("D") && 
				((ArtefactSubElement) nextName).getSubElementId().contains("SC"))
			if(className instanceof AttributeModel)
				desString += "UMLAttributeToSourceAttribute";
			else 
				desString += "UMLMethodToSourceMethod";

		return desString;
	}
}
