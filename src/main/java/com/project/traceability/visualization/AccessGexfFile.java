/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.visualization;

import com.project.NLP.file.operations.FilePropertyName;
import com.project.traceability.GUI.HomeGUI;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.openide.util.Exceptions;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Aarthika <>
 */
public class AccessGexfFile {
    public static void removeEdgeFromGexf(int id) {
        String gexfXML = HomeGUI.projectPath + File.separator + FilePropertyName.PROPERTY + File.separator + HomeGUI.projectName + ".gexf";

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(gexfXML);
            System.out.println("" + gexfXML);

            NodeList nlParent = document.getElementsByTagName("edges");
            System.out.println("" + nlParent.getLength());
            System.out.println("" + nlParent.item(0).getAttributes().item(0).getNodeValue());
            int count = Integer.parseInt(nlParent.item(0).getAttributes().item(0).getNodeValue());
            System.out.println("Gexf dleting :" + id);

            NodeList nlChild = document.getElementsByTagName("edge");
            parent:
            for (int i = 0; i < nlChild.getLength(); i++) {
                if (null != nlChild.item(i)) {
                    child:
                    for (int j = 0; j < nlChild.item(i).getAttributes().getLength(); j++) {
                        if (nlChild.item(i).getAttributes().item(j).getNodeName().equalsIgnoreCase("id")) {

                            if (nlChild.item(i).getAttributes().item(j).getNodeValue().equalsIgnoreCase(String.valueOf(id))) {
                                nlChild.item(i).getParentNode().removeChild(nlChild.item(i));
                                count--;
                                nlParent.item(0).getAttributes().item(0).setNodeValue(String.valueOf(count));
                                System.out.println("Gexf suc3 :" + id);
                                break parent;
                            }
                        }
                    }
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            String xmlpath = HomeGUI.projectPath + File.separator + FilePropertyName.PROPERTY;
            File file = new File(xmlpath, HomeGUI.projectName + ".gexf");

            System.out.println("file: " + file.getAbsolutePath());
            StreamResult result = new StreamResult(file.getPath());
            transformer.transform(source, result);

            System.out.println("Done nod");
            //VisualizeGraph.refreshGraph();

        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (TransformerConfigurationException ex) {
            Exceptions.printStackTrace(ex);
        } catch (TransformerException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public static boolean getIDFromGexf(int id) {
        String gexfXML = HomeGUI.projectPath + File.separator + FilePropertyName.PROPERTY + File.separator + HomeGUI.projectName + ".gexf";

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(gexfXML);
            System.out.println("" + gexfXML);

            NodeList nlChild = document.getElementsByTagName("edge");
            for (int i = 0; i < nlChild.getLength(); i++) {
                String childID = nlChild.item(i).getAttributes().item(0).getNodeValue();
                if (childID.equalsIgnoreCase(String.valueOf(id))) {
                    System.out.println(childID + " " + id);
                    return true;
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return false;
    }

    public static int addToGEXF(String start, String end, String relType) {
        String relation = "";
        if (start.isEmpty() || end.isEmpty() || relType.isEmpty()) {
            return -1;
        } else {
            for (GraphDB.RelTypes rel : GraphDB.RelTypes.values()) {
                if (rel.getValue().equalsIgnoreCase(relType)) {
                    relation = rel.name();
                    break;
                }
            }
            String gexfXML = HomeGUI.projectPath + File.separator + FilePropertyName.PROPERTY + File.separator + HomeGUI.projectName + ".gexf";
            int count = -1;
            try {
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.parse(gexfXML);
                System.out.println("" + gexfXML);

                NodeList nlParent = document.getElementsByTagName("edges");
                System.out.println("" + nlParent.getLength());
                System.out.println("" + nlParent.item(0).getAttributes().item(0).getNodeValue());
                count = Integer.parseInt(nlParent.item(0).getAttributes().item(0).getNodeValue());

                NodeList nlChild = document.getElementsByTagName("edge");

                Element edge = document.createElement("edge");
                nlChild.item(0).getParentNode().appendChild(edge);
                count++;
                nlParent.item(0).getAttributes().item(0).setNodeValue(Integer.toString(count));
                Attr idAttr = document.createAttribute("id");
                idAttr.setValue(String.valueOf(count));
                edge.setAttributeNode(idAttr);
                Attr labelAttr = document.createAttribute("label");
                labelAttr.setValue(relation);
                edge.setAttributeNode(labelAttr);
                Attr sourceAttr = document.createAttribute("source");
                sourceAttr.setValue(start);
                edge.setAttributeNode(sourceAttr);
                Attr targetAttr = document.createAttribute("target");
                targetAttr.setValue(end);
                edge.setAttributeNode(targetAttr);
                Attr typeAttr = document.createAttribute("type");
                typeAttr.setValue("Directed");
                edge.setAttributeNode(typeAttr);
                Element edgeSub = document.createElement("attvalues");
                edge.appendChild(edgeSub);

                Element edgeSubVal1 = document.createElement("attvalue");
                edgeSub.appendChild(edgeSubVal1);
                Attr forAttr1 = document.createAttribute("for");
                forAttr1.setValue("message");
                edgeSubVal1.setAttributeNode(forAttr1);
                Attr valueAttr1 = document.createAttribute("value");
                valueAttr1.setValue(relType);
                edgeSubVal1.setAttributeNode(valueAttr1);

                Element edgeSubVal2 = document.createElement("attvalue");
                edgeSub.appendChild(edgeSubVal2);
                Attr forAttr2 = document.createAttribute("for");
                forAttr2.setValue("neo4j_rt");
                edgeSubVal2.setAttributeNode(forAttr2);
                Attr valueAttr2 = document.createAttribute("value");
                valueAttr2.setValue(relation);
                edgeSubVal2.setAttributeNode(valueAttr2);

                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(document);
                String xmlpath = HomeGUI.projectPath + File.separator + FilePropertyName.PROPERTY;
                File file = new File(xmlpath, HomeGUI.projectName + ".gexf");

                System.out.println("file: " + file.getAbsolutePath());
                StreamResult result = new StreamResult(file.getPath());
                transformer.transform(source, result);

                System.out.println("Done nod");
                //VisualizeGraph.refreshGraph();

            } catch (ParserConfigurationException | SAXException | IOException ex) {
                Exceptions.printStackTrace(ex);
            } catch (TransformerConfigurationException ex) {
                Exceptions.printStackTrace(ex);
            } catch (TransformerException ex) {
                Exceptions.printStackTrace(ex);
            }
            return count;
        }

    }

}
