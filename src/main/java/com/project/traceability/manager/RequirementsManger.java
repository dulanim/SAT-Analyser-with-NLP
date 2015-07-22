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
import com.project.traceability.model.MethodModel;
import com.project.traceability.model.RequirementModel;
import com.project.traceability.utils.Constants;
import com.project.traceability.utils.Constants.ArtefactSubElementType;
import com.project.traceability.utils.Constants.ArtefactType;
import com.project.traceability.visualization.GraphDB;

public class RequirementsManger {

	private ArtefactType artefactType = Constants.ArtefactType.REQUIREMENT;

	public static List<RequirementModel> requirementElements = null;
	public static List<ArtefactElement> expectedRequirementElements = null;
	public static Map<String, ArtefactElement> requirementArtefactElements = null; // find using nlp
	
	static String projectPath;

	/**
	 * read UMLXml file and store data in a map
	 */
	public static Map<String, ArtefactElement> readXML(String projectPath) {
		RequirementsManger.projectPath = projectPath;
		// get the xml file
		File umlXmlFile = new File(projectPath + "RequirementArtefactFile.xml");
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
			//System.out.println("********************"+artefactList.getLength());
			for (int temp = 0; temp < artefactList.getLength(); temp++) {

				Node artefactNode = (Node) artefactList.item(temp); // identify
																	// a node

				if (artefactNode.getNodeType() == Node.ELEMENT_NODE) {

					NodeList artefactElementList = UMLDoc
							.getElementsByTagName("ArtefactElement"); // get all
																		// "Artefact"
																		// elements
					requirementElements = new ArrayList<RequirementModel>();
					RequirementModel requirement = null;
					for (int temp1 = 0; temp1 < artefactElementList.getLength(); temp1++) {

						Node artefactElementNode = (Node) artefactElementList
								.item(temp1);
						Element artefact = (Element) artefactElementNode;
						String id = artefact.getAttribute("id");
						String name = artefact.getAttribute("name");
						String title = artefact.getElementsByTagName("Title")
								.item(0).getTextContent();
						String content = artefact
								.getElementsByTagName("Content").item(0)
								.getTextContent();
						String priority = artefact
								.getElementsByTagName("Priority").item(0)
								.getTextContent();
						String type = artefact.getElementsByTagName("Type")
								.item(0).getTextContent();
						requirement = new RequirementModel(id, name, title,
								content, priority, type);
						requirementElements.add(requirement);
					}
					
					NodeList intraConnectionsList = UMLDoc
							.getElementsByTagName("IntraConnections");
					readIntraConnectionsXML(intraConnectionsList);
					expectedRequirementElements = InfoExtractionManager
							.run(requirementElements);
					requirementArtefactElements = new HashMap<String, ArtefactElement>();
					for (int i = 0; i < expectedRequirementElements.size(); i++) {
						requirementArtefactElements.put(
								expectedRequirementElements.get(i)
										.getArtefactElementId(),
								expectedRequirementElements.get(i));
						
//						System.out.println(expectedRequirementElements.get(i)
//								.getName() + "*********"+expectedRequirementElements.get(i).getArtefactElementId());
//						for (int j = 0; j < expectedRequirementElements.get(i)
//								.getArtefactSubElements().size(); j++) {
//							System.out.println(expectedRequirementElements
//									.get(i).getArtefactSubElements().get(j)
//									.getName()
//									+ "___________"+expectedRequirementElements.get(i).getArtefactSubElements().get(j).getSubElementId()+"-"+expectedRequirementElements
//									.get(i).getArtefactSubElements().get(j)
//									.getType());
//					}
					}
				}
			}
		//	GraphDB db = new GraphDB();
			//db.initiateGraphDB();
            //db.addNodeToGraphDB(requirementArtefactElements);
		} catch (ParserConfigurationException | SAXException | IOException e) {

			e.printStackTrace();
		}
		return requirementArtefactElements;
	}

	public static void readIntraConnectionsXML(NodeList intraConnectionsList) {
		for (int temp1 = 0; temp1 < intraConnectionsList.getLength(); temp1++) {

			Node intraConnectionNode = (Node) intraConnectionsList.item(temp1);
			Element intraConnectionElement = (Element) intraConnectionNode;
			NodeList connectionsList = intraConnectionElement
					.getElementsByTagName("Connection");
		}

	}
	
	/**
	 * get all artefactSubElements in an artefactElement
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

			Node nNod = (Node) artefactSubElementList.item(temp1);
			Element artefact = (Element) nNod;
			String id = artefact.getAttribute("id");
			String name = artefact.getAttribute("name");
			String type = artefact.getAttribute("type");
			//String visibility = artefact.getAttribute("visibility");
			
			if (type.equalsIgnoreCase("Method")) {
				//String parameters = artefact.getAttribute("parameters");
				//String returnType = artefact.getAttribute("returnType");
				//String content = artefact.getAttribute("content");
				methodAttribute = new MethodModel();
				methodAttribute.setSubElementId(id);
				methodAttribute.setName(name);
				methodAttribute.setType(type);
				//methodAttribute.setVisibility(visibility);
				//methodAttribute.setReturnType(returnType);
				//methodAttribute.setContent(content);
//				if (!parameters.equals(""))
//					methodAttribute.setParameters(ParameterManager
//							.listParameters(parameters));
				artefactSubElements.add(methodAttribute);
			} else if (type.equalsIgnoreCase("Field")) {
				attributeElement = new AttributeModel();
				//String variableType = artefact.getAttribute("variableType");
				attributeElement.setSubElementId(id);
				attributeElement.setName(name);
				attributeElement.setType(type);
				//attributeElement.setVariableType(variableType);
				//attributeElement.setVisibility(visibility);
				artefactSubElements.add(attributeElement);
			}
		}

		return artefactSubElements;
	}
	
	/**
	 * @param attribute
	 * 
	 */
	public static Map<ArtefactElement, List<? extends ArtefactSubElement>> manageArtefactSubElements(
			ArtefactSubElementType attribute) {
		List<ArtefactSubElement> artefactSubElements = null;
		List<MethodModel> methodArtefactSubElements = null;
		List<AttributeModel> attributeArtefactSubElements = null;
		Map<ArtefactElement, List<? extends ArtefactSubElement>> attributeArtefactMap = null;
		Map<ArtefactElement, List<? extends ArtefactSubElement>> methodArtefactMap = null;
		RequirementsManger.readXML(projectPath);
		Iterator it = RequirementsManger.requirementArtefactElements
				.entrySet().iterator();
		attributeArtefactMap = new HashMap<ArtefactElement, List<? extends ArtefactSubElement>>();
		methodArtefactMap = new HashMap<ArtefactElement, List<? extends ArtefactSubElement>>();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			ArtefactElement artefactElement = (ArtefactElement) pairs
					.getValue();
			artefactSubElements = artefactElement.getArtefactSubElements();
			attributeArtefactSubElements = new ArrayList<AttributeModel>();
			methodArtefactSubElements = new ArrayList<MethodModel>();
			for (int i = 0; i < artefactSubElements.size(); i++) {
				//System.out.println(artefactSubElements.get(i).getType());
				if (artefactSubElements.get(i).getType()
						.equalsIgnoreCase("Field")) {
					//System.out.println(artefactSubElements.get(i).getName());
					attributeArtefactSubElements
							.add((AttributeModel) artefactSubElements.get(i));
				} else if (artefactSubElements.get(i).getType()
						.equalsIgnoreCase("Method")) {
					//System.out.println(artefactSubElements.get(i).getName());
					methodArtefactSubElements
							.add((MethodModel) artefactSubElements.get(i));
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

	public ArtefactType getArtefactType() {
		return artefactType;
	}

	public void setArtefactType(ArtefactType artefactType) {
		this.artefactType = artefactType;
	}



}
