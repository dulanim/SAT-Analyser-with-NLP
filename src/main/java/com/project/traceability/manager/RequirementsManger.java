package com.project.traceability.manager;

import com.project.NLP.file.operations.FilePropertyName;
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
import com.sun.javafx.scene.control.skin.VirtualFlow;

public class RequirementsManger {

    private ArtefactType artefactType = Constants.ArtefactType.REQUIREMENT;
    public static List<RequirementModel> requirementElements = null;
    public static List<ArtefactElement> expectedRequirementElements = null;
    public static Map<String, ArtefactElement> requirementArtefactElements = null; // find using nlp
	
    static String projectPath;

    /**
     * read requirement Xml file and store data in a map
     */
    public static Map<String, ArtefactElement> readXML(String projectPath) {
        RequirementsManger.projectPath = projectPath;
	// get the xml file
	File requirementXmlFile = new File(projectPath + File.separator + FilePropertyName.XML + File.separator +  FilePropertyName.REQUIREMENT_ARTIFACT_NAME);
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder;
	try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document UMLDoc = (Document) dBuilder.parse(requirementXmlFile);
            UMLDoc.getDocumentElement().normalize();
            
            /*Get Main Artefact element list in the XML file :<Artefact type="Requirement"> */
            NodeList artefactList = UMLDoc.getElementsByTagName("Artefact"); 
            	for (int temp = 0; temp < artefactList.getLength(); temp++) {
                    /*Get a Artefact node of the XML in the list */ 
                    Node artefactNode = (Node) artefactList.item(temp); 
                    	if (artefactNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element artefactElement=(Element)artefactNode;
                            /*Get ArtefactElement nodes of the XML file which childrens of Artefact Node : <ArtefactElement id="R1" name="normal order" type="Class">  */
                            NodeList artefactElementList=artefactElement.getElementsByTagName("ArtefactElement");
                            /*Storing ArtefactElements readfrom the requirement xml file */													// "Artefact"
                            expectedRequirementElements=new ArrayList<>();
                            requirementArtefactElements = new HashMap<String, ArtefactElement>();
                            for(int element=0;element<artefactElementList.getLength();element++){
                                Node artefactElement_Node=artefactElementList.item(element);
                                Element artefactElement_Element=(Element)artefactElement_Node;
                                String id=artefactElement_Element.getAttribute("id");
                                String name=artefactElement_Element.getAttribute("name");
                                String type=artefactElement_Element.getAttribute("type");
                                    
                                /*Get Subelements list from the xml file  : 
                                *<ArtefactSubElement id="R12" name="shop" type="RequrirementAttribute" variableType="" visibility=""/> 
                                *<ArtefactSubElement id="R30" name="confirm" parameters="" returnType="" type="RequrirementMethod" visibility=""/>
                                */
                                List<ArtefactSubElement> artefactSubElements=new ArrayList<>();
                                   
                                NodeList subElementList=artefactElement_Element.getElementsByTagName("ArtefactSubElement");
                                for(int sElement=0;sElement<subElementList.getLength();sElement++){
                                    Node subElement_Node=subElementList.item(sElement);
                                    Element subElement_Element=(Element)subElement_Node;
                                       
                                    ArtefactSubElement artefactSubElement;
                                        
                                    String s_id=subElement_Element.getAttribute("id");
                                    String s_name=subElement_Element.getAttribute("name");
                                    String s_type=subElement_Element.getAttribute("type");
                                    String s_visibility=subElement_Element.getAttribute("visibility");
                                    /*
                                    if(s_type.equalsIgnoreCase("RequrirementAttribute")){
                                        String variableType=subElement_Element.getAttribute("variableType");
                                        artefactSubElement=new ArtefactSubElement(s_id,s_name,s_type,s_visibility,variableType);
                                    }
                                    else{
                                         String returnType=subElement_Element.getAttribute("returnType");
                                        String parameters=subElement_Element.getAttribute("parameters");
                                        artefactSubElement=new ArtefactSubElement(s_id,s_name,s_type,s_visibility,returnType,parameters);
                                    }
                                    */
                                    artefactSubElement=new ArtefactSubElement(s_id,s_name,s_visibility,null,s_type);
                                    artefactSubElements.add(artefactSubElement);
                                }
                                    
                                ArtefactElement extractedArtefactElement=new ArtefactElement(id,name,type,null,artefactSubElements);
                                expectedRequirementElements.add(extractedArtefactElement);
                                requirementArtefactElements.put(id, extractedArtefactElement);
                            }
                               
                            NodeList intraConnectionsList = UMLDoc.getElementsByTagName("IntraConnections");
                            readIntraConnectionsXML(intraConnectionsList);
				
                        }
                    }
		} catch (ParserConfigurationException | SAXException | IOException e) {
                    e.printStackTrace();
		}
            return requirementArtefactElements;
	}

    public static void readIntraConnectionsXML(NodeList intraConnectionsList) {
        for (int temp1 = 0; temp1 < intraConnectionsList.getLength(); temp1++) {

            Node intraConnectionNode = (Node) intraConnectionsList.item(temp1);
            Element intraConnectionElement = (Element) intraConnectionNode;
            NodeList connectionsList = intraConnectionElement.getElementsByTagName("Connection");
	}

    }
	
	
    /**
    * get all artefactSubElements in an artefactElement
    * 
    * @param artefactSubElementList
    * @return
    */
    public static List<ArtefactSubElement> readArtefactSubElement(NodeList artefactSubElementList) {

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
