package com.project.traceability.manager;

import com.project.traceability.common.DefaultWords;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author Gitanjali Nov 19, 2014, Thanu Dec 23 2014
 */
public class ReadFiles{

    static String projectPath;
    public static List<String> relationNodes = new ArrayList<String>();

    public static void readFiles(String path) {
        projectPath = path;
        SourceCodeArtefactManager.readXML(projectPath);
        UMLArtefactManager.readXML(projectPath);
        RequirementsManger.readXML(projectPath);
        DefaultWords.getDefaultWords();
    }

    /**
     * Method to delete an artefact from its extracted XML file
     *
     * @param id ArtefactElement Id
     */
    public static void deleteArtefact(String id) {

        char type = id.toLowerCase().charAt(0); //get the type of XML file from artefact elemnt id's first character
        /*s - SourceCodeArtefactFile.xml
         r - RequirementArtefactFile.xml
         d - UMLArtefactFile.xml*/

        File file = new File(projectPath
                + "SourceCodeArtefactFile.xml");// file path is set to SourceCodeArtefactFile.xml by default
        String xml = null;
        switch (type) {
            case 's':
                xml = "SourceCodeArtefactFile.xml";
                break;
            case 'r':
                xml = "RequirementArtefactFile.xml";
                break;
            case 'd':
                xml = "UMLArtefactFile.xml";
                break;
        }
        file = new File(projectPath, xml);//set the file path to corresponding XML file of the artefact element

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = (Document) dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList artefactNodeList = doc.getElementsByTagName("Artefact");
            boolean found = false;

            for (int i = 0; i < artefactNodeList.getLength() && found != true; i++) {

                Node artefactNode = (Node) artefactNodeList.item(i);
                if (artefactNode.getNodeType() == Node.ELEMENT_NODE) {

                    NodeList artefactElementList = doc
                            .getElementsByTagName("ArtefactElement");

                    for (int j = 0; j < artefactElementList.getLength() && !found; j++) {
                        Node artefactElementNode = (Node) artefactElementList
                                .item(j);
                        Element artefact = (Element) artefactElementNode;
                        String artefact_id = artefact.getAttribute("id");
                        if (id.equals(artefact_id)) { //delete the artefact which has "artefact_id"
                            artefactElementNode.getParentNode().removeChild(artefactElementNode);
                            found = true;
                            System.out.println("Artefact " + artefact_id + " deleted");
                            break;
                        }
                        NodeList artefactSubElementList = doc
                                .getElementsByTagName("ArtefactSubElement");
                        for (int k = 0; k < artefactSubElementList.getLength() && found != true; k++) { //if the artefact element which has "artefact_id is not ArtefactElement then delete ArtefactSubElement which has artefact_id
                            Node artefactSubElementNode = (Node) artefactSubElementList
                                    .item(k);
                            Element subArtefact = (Element) artefactSubElementNode;
                            String sub_artefact_id = subArtefact.getAttribute("id");
                            if (id.equals(sub_artefact_id)) {
                                artefactSubElementNode.getParentNode().removeChild(artefactSubElementNode);
                                found = true;
                                System.out.println("Artefact " + sub_artefact_id + " deleted");
                                break;
                            }
                        }
                    }
                }
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(projectPath, xml).getPath());
            transformer.transform(source, result);
        } catch (ParserConfigurationException | SAXException | IOException e) {
        } catch (TransformerConfigurationException e) {
        } catch (TransformerException e) {
        }

    }

     /**
     * Method to delete relations related to artefact which has particular id from  XML file
     *
     * @param id ArtefactElement Id
     */
    public static void deleteRelation(String id) {
        File file = new File(projectPath
                + "Relations.xml");// file path is set to Relations.xml
        String xml = "Relations.xml";
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = (Document) dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList relationsNodeList = doc.getElementsByTagName("Relations");//get relations nodes
            
            for (int i = 0; i < relationsNodeList.getLength(); i++) {
                
                Node relationsNode = (Node) relationsNodeList.item(i);
                if (relationsNode.getNodeType() == Node.ELEMENT_NODE) {

                    NodeList relationElementList = doc
                            .getElementsByTagName("Relation");//get relation nodes

                    for (int j = 0; j < relationElementList.getLength(); j++) {
                       Node relationElementNode = (Node) relationElementList
                                .item(j);
                        Element relationElement = (Element) relationElementNode;
                        String source = relationElement.getElementsByTagName("SourceNode").item(0).getTextContent();//get source node
                        String target = relationElement.getElementsByTagName("TargetNode").item(0).getTextContent();//get target node
                        
                        if (id.equals(target)||id.equals(source)) {
                            relationElementNode.getParentNode().removeChild(relationElementNode);
                        }
                    }
                }
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(projectPath, xml).getPath());
            transformer.transform(source, result);
        } catch (ParserConfigurationException | SAXException | IOException e) {
        } catch (TransformerConfigurationException e) {
        } catch (TransformerException e) {
        }
    }
}