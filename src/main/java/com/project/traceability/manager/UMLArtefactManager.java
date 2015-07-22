package com.project.traceability.manager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.ArtefactSubElement;
import com.project.traceability.model.AttributeModel;
import com.project.traceability.model.ConnectionModel;
import com.project.traceability.model.MethodModel;
import com.project.traceability.utils.Constants;
import com.project.traceability.utils.Constants.ArtefactSubElementType;
import com.project.traceability.utils.Constants.ArtefactType;
import com.project.traceability.visualization.GraphDB;

public class UMLArtefactManager {

	private ArtefactType artefactType = Constants.ArtefactType.UMLDiagram;

	public static Map<String, ArtefactElement> UMLAretefactElements = null;

	static String projectPath;

	/**
	 * read UMLXml file and store data in a map
	 */
	public static void readXML(String projectPath) {
		UMLArtefactManager.projectPath = projectPath;
		// get the xml file
		File umlXmlFile = new File(projectPath + "UMLArtefactFile.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document UMLDoc = (Document) dBuilder.parse(umlXmlFile);
			UMLDoc.getDocumentElement().normalize();

			NodeList artefactList = UMLDoc.getElementsByTagName("Artefact"); // find
																				// the
																				// "Artefact"
																				// tag

			for (int temp = 0; temp < artefactList.getLength(); temp++) {

				Node artefactNode = (Node) artefactList.item(temp); // identify
																	// a node

				if (artefactNode.getNodeType() == Node.ELEMENT_NODE) {

					NodeList artefactElementList = UMLDoc
							.getElementsByTagName("ArtefactElement"); // get all
																		// "Artefact"
																		// elements
					UMLAretefactElements = new HashMap<String, ArtefactElement>();
					ArtefactElement artefactElement = null;
					List<ArtefactSubElement> artefactsSubElements = null;
					for (int temp1 = 0; temp1 < artefactElementList.getLength(); temp1++) {

						artefactsSubElements = new ArrayList<ArtefactSubElement>();
						Node artefactElementNode = (Node) artefactElementList
								.item(temp1);
						Element artefact = (Element) artefactElementNode;
						String id = artefact.getAttribute("id"); // get all the
																	// attributes
																	// of an
																	// artefactelement
						String name = artefact.getAttribute("name");
						String type = artefact.getAttribute("type");
						String visibility = artefact.getAttribute("visibility");
						NodeList artefactSubElementList = artefact
								.getElementsByTagName("ArtefactSubElement"); // find
																				// artefactsubelements
																				// in
																				// an
																				// artefactelements
						artefactsSubElements = readArtefactSubElement(artefactSubElementList);
						artefactElement = new ArtefactElement(id, name, type,
								visibility, artefactsSubElements);
						UMLAretefactElements.put(id, artefactElement);
						//GraphDB db = new GraphDB();
						//db.initiateGraphDB();
	                   // db.addNodeToGraphDB(UMLAretefactElements);
					}

					readIntraConnectionsXML(UMLDoc);
				}
			}
		} catch (ParserConfigurationException | SAXException | IOException e) {

			e.printStackTrace();
		}

	}

	public static void readIntraConnectionsXML(Document UMLDoc) {

		ArrayList<ConnectionModel> sourceIntraConnections = new ArrayList<ConnectionModel>(); 
		NodeList intraConnectionsList = UMLDoc
				.getElementsByTagName("IntraConnections");
		for (int temp1 = 0; temp1 < intraConnectionsList.getLength(); temp1++) {

			Node intraConnectionNode = (Node) intraConnectionsList.item(temp1);
			Element intraConnectionElement = (Element) intraConnectionNode;
			NodeList connectionsList = intraConnectionElement
					.getElementsByTagName("Connection");
			ConnectionModel connection = null;
			for (int temp2 = 0; temp2 < connectionsList.getLength(); temp2++) {
				Node artefactElementNode = (Node) connectionsList.item(temp2);
				Element artefact = (Element) artefactElementNode;
				String type =  artefact.getElementsByTagName("Type").item(0).getTextContent();
				String start = artefact.getElementsByTagName("StartPoint").item(0).getTextContent();
				String startMulti = artefact.getElementsByTagName("Multiplicity").item(0).getTextContent();
				String end = artefact.getElementsByTagName("EndPoint").item(0).getTextContent();
				String endMulti = artefact.getElementsByTagName("Multiplicity").item(0).getTextContent();
				String annotation = artefact.getElementsByTagName("Annotation").item(0).getTextContent();
				connection = new ConnectionModel(type, start, startMulti, end, endMulti, annotation);
				sourceIntraConnections.add(connection);
			}
		}

	}

	/**
	 * get all artefactelements
	 * 
	 * @param artefactSubElementList
	 * @return
	 */
	public static List<ArtefactSubElement> readArtefactSubElement(
			NodeList artefactSubElementList) {

		AttributeModel attributeElement = null;
		MethodModel methodAttribute = null;
		List<ArtefactSubElement> artefactSubElements = new ArrayList<ArtefactSubElement>();
		for (int temp1 = 0; temp1 < artefactSubElementList.getLength(); temp1++) {

			Node artefactSubElementNode = (Node) artefactSubElementList
					.item(temp1);
			Element artefact = (Element) artefactSubElementNode;
			String id = artefact.getAttribute("id");
			String name = artefact.getAttribute("name");
			String type = artefact.getAttribute("type");
			String visibility = artefact.getAttribute("visibility");
			if (type.equalsIgnoreCase("UMLOperation")) {
				String parameters = artefact.getAttribute("parameters");
				String returnType = artefact.getAttribute("returnType");
				String content = artefact.getAttribute("content");
				methodAttribute = new MethodModel();
				methodAttribute.setSubElementId(id);
				methodAttribute.setName(name);
				methodAttribute.setType(type);
				methodAttribute.setVisibility(visibility);
				methodAttribute.setReturnType(returnType);
				methodAttribute.setContent(content);
				if (!parameters.equals(""))
					methodAttribute.setParameters(ParameterManager
							.listParameters(parameters));
				artefactSubElements.add(methodAttribute);
			} else if (type.equalsIgnoreCase("UMLAttribute")) {
				attributeElement = new AttributeModel();
				attributeElement.setSubElementId(id);
				attributeElement.setName(name);
				attributeElement.setType(type);
				attributeElement.setVisibility(visibility);
				artefactSubElements.add(attributeElement);
			}

		}

		return artefactSubElements;
	}

	/**
	 * @param attribute
	 * 
	 */
	public static Map<ArtefactElement, List<ArtefactSubElement>> manageArtefactSubElements(
			ArtefactSubElementType attribute) {
		List<ArtefactSubElement> artefactSubElements = null;
		List<ArtefactSubElement> methodArtefactSubElements = null;
		List<ArtefactSubElement> attributeArtefactSubElements = null;
		Map<ArtefactElement, List<ArtefactSubElement>> attributeArtefactMap = null;
		Map<ArtefactElement, List<ArtefactSubElement>> methodArtefactMap = null;
		readXML(projectPath);
		Iterator it = UMLAretefactElements.entrySet().iterator();
		attributeArtefactMap = new HashMap<ArtefactElement, List<ArtefactSubElement>>();
		methodArtefactMap = new HashMap<ArtefactElement, List<ArtefactSubElement>>();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			ArtefactElement artefactElement = (ArtefactElement) pairs
					.getValue();
			artefactSubElements = artefactElement.getArtefactSubElements();
			attributeArtefactSubElements = new ArrayList<ArtefactSubElement>();
			methodArtefactSubElements = new ArrayList<ArtefactSubElement>();
			for (int i = 0; i < artefactSubElements.size(); i++) {
				if (artefactSubElements.get(i).getType()
						.equalsIgnoreCase("UMLAttribute")) {

					attributeArtefactSubElements
							.add(artefactSubElements.get(i));
				} else if (artefactSubElements.get(i).getType()
						.equalsIgnoreCase("UMLOperation")) {
					methodArtefactSubElements.add(artefactSubElements.get(i));
				}
			}
			it.remove(); // avoids a ConcurrentModificationException
			attributeArtefactMap.put(artefactElement,
					attributeArtefactSubElements);
			methodArtefactMap.put(artefactElement, methodArtefactSubElements);
		}
		if (attribute.equals(ArtefactSubElementType.ATTRIBUTE))
			return attributeArtefactMap;
		else
			return methodArtefactMap;
	}

}
