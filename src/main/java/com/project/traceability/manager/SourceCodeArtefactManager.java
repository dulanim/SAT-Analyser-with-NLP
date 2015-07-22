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

public class SourceCodeArtefactManager {

    private ArtefactType artefactType = Constants.ArtefactType.SOURCECODE;
    public static Document sourceDoc;
    static String projectPath;
    public static Map<String, ArtefactElement> sourceCodeAretefactElements = new HashMap<String, ArtefactElement>();

    public static void readXML(String projectPath) {
        SourceCodeArtefactManager.projectPath = projectPath;
        File sourceXmlFile = new File(projectPath
                + "SourceCodeArtefactFile.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            sourceDoc = (Document) dBuilder.parse(sourceXmlFile);
            sourceDoc.getDocumentElement().normalize();

            NodeList artefactNodeList = sourceDoc
                    .getElementsByTagName("Artefact");

            for (int temp = 0; temp < artefactNodeList.getLength(); temp++) {

                Node artefactNode = (Node) artefactNodeList.item(temp);
                if (artefactNode.getNodeType() == Node.ELEMENT_NODE) {

                    NodeList artefactElementList = sourceDoc
                            .getElementsByTagName("ArtefactElement");

                    // sourceCodeAretefactElements = new HashMap<String,
                    // ArtefactElement>();
                    ArtefactElement artefactElement = null;
                    List<ArtefactSubElement> artefactsSubElements = null;
                    for (int temp1 = 0; temp1 < artefactElementList.getLength(); temp1++) {

                        artefactsSubElements = new ArrayList<ArtefactSubElement>();

                        Node artefactElementNode = (Node) artefactElementList
                                .item(temp1);
                        Element artefact = (Element) artefactElementNode;
                        String id = artefact.getAttribute("id");
                        String name = artefact.getAttribute("name");
                        String type = artefact.getAttribute("type");
                        String visibility = artefact.getAttribute("visibility");

                        NodeList artefactSubElementList = artefact
                                .getElementsByTagName("ArtefactSubElement");
                        artefactsSubElements = readArtefactSubElement(artefactSubElementList);
                        artefactElement = new ArtefactElement(id, name, type,
                                visibility, artefactsSubElements);
                        sourceCodeAretefactElements.put(id, artefactElement);
                       // GraphDB db = new GraphDB();
                       // db.initiateGraphDB();
                        //db.addNodeToGraphDB(sourceCodeAretefactElements);
                    }

                    readIntraConnectionsXML(sourceDoc);
                }
            }
        } catch (ParserConfigurationException e) {

            e.printStackTrace();
        } catch (SAXException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public static void readIntraConnectionsXML(Document sourceDoc) {

        ArrayList<ConnectionModel> UMlIntraConnections = new ArrayList<ConnectionModel>();
        NodeList intraConnectionsList = sourceDoc
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
                String type = artefact.getElementsByTagName("Type").item(0).getTextContent();
                String start = artefact.getElementsByTagName("StartPoint").item(0).getTextContent();
                String end = artefact.getElementsByTagName("EndPoint").item(0).getTextContent();
                connection = new ConnectionModel(type, start, null, end, null, null);
            }
        }

    }

    /**
     * readInterConnections in XML
     *
     * @param nList13
     */
    public static void readIntraConnectionsXML(NodeList intraConnectionsList) {
        for (int temp1 = 0; temp1 < intraConnectionsList.getLength(); temp1++) {

            Node intraConnectionNode = (Node) intraConnectionsList.item(temp1);
            Element intraConnectionElement = (Element) intraConnectionNode;
            // System.out.println("Connection : " +
            // eement1.getElementsByTagName("Connection").item(0).getTextContent());
            NodeList connectionList = intraConnectionElement
                    .getElementsByTagName("Connection");
            for (int temp2 = 0; temp2 < connectionList.getLength(); temp2++) {
                // Node node = (Node) nList14.item(temp1);
                // Element eement2 = (Element) node;
				/*
                 * System.out.println("Type of Coneection : " +
                 * eement1.getElementsByTagName
                 * ("Type").item(0).getTextContent());
                 * System.out.println("Start Point : " +
                 * eement1.getElementsByTagName
                 * ("StartPoint").item(0).getTextContent());
                 * System.out.println("Start Point : " +
                 * eement1.getAttribute("Multiplicity"));
                 */
            }

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
            String visibility = artefact.getAttribute("visibility");

            if (type.equalsIgnoreCase("Method")) {
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
                if (!parameters.equals("")) {
                    methodAttribute.setParameters(ParameterManager
                            .listParameters(parameters));
                }
                artefactSubElements.add(methodAttribute);
            } else if (type.equalsIgnoreCase("Field")) {
                attributeElement = new AttributeModel();
                String variableType = artefact.getAttribute("variableType");
                attributeElement.setSubElementId(id);
                attributeElement.setName(name);
                attributeElement.setType(type);
                attributeElement.setVariableType(variableType);
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
    public static Map<ArtefactElement, List<? extends ArtefactSubElement>> manageArtefactSubElements(
            ArtefactSubElementType attribute) {
        List<ArtefactSubElement> artefactSubElements = null;
        List<MethodModel> methodArtefactSubElements = null;
        List<AttributeModel> attributeArtefactSubElements = null;
        Map<ArtefactElement, List<? extends ArtefactSubElement>> attributeArtefactMap = null;
        Map<ArtefactElement, List<? extends ArtefactSubElement>> methodArtefactMap = null;
        SourceCodeArtefactManager.readXML(projectPath);
        Iterator it = SourceCodeArtefactManager.sourceCodeAretefactElements
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
                if (artefactSubElements.get(i).getType()
                        .equalsIgnoreCase("Field")) {
                    attributeArtefactSubElements
                            .add((AttributeModel) artefactSubElements.get(i));
                } else if (artefactSubElements.get(i).getType()
                        .equalsIgnoreCase("Method")) {
                    methodArtefactSubElements
                            .add((MethodModel) artefactSubElements.get(i));
                }
            }
            it.remove(); // avoids a ConcurrentModificationException
            attributeArtefactMap.put(artefactElement,
                    attributeArtefactSubElements);
            methodArtefactMap.put(artefactElement, methodArtefactSubElements);
        }
        if (attribute.equals(ArtefactSubElementType.ATTRIBUTE)) {
            return attributeArtefactMap;
        } else {
            return methodArtefactMap;
        }
    }

    public ArtefactType getArtefactType() {
        return artefactType;
    }

    public void setArtefactType(ArtefactType artefactType) {
        this.artefactType = artefactType;
    }

    public static Map<String, ArtefactElement> getSourceCodeAretefactElements() {
        return sourceCodeAretefactElements;
    }
    
    
    
}
