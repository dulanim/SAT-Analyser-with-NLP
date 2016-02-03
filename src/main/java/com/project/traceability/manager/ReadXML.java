package com.project.traceability.manager;

import com.project.NLP.file.operations.FilePropertyName;
import static com.project.NLP.file.operations.FilePropertyName.XML;
import com.project.traceability.GUI.HomeGUI;
import com.project.traceability.common.PropertyFile;
import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.RequirementModel;
import com.project.traceability.staticdata.StaticData;
import com.project.traceability.visualization.AccessLinksTextFile;
import com.project.traceability.visualization.GraphDB;
import com.project.traceability.visualization.GraphDBDelete;
import com.project.traceability.visualization.VisualizeGraph;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.neo4j.graphdb.Relationship;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ReadXML {

    public static List<String> relationNodes = new ArrayList<>();

    public static void initApp(String projectPath, String graphType) {

        try {
            System.out.println("Enter");
            HomeGUI.isComaparing = false;
            transferDataToDBFromXML(projectPath, true);
            System.out.println("Enter");

            VisualizeGraph visual = VisualizeGraph.getInstance();
            AccessLinksTextFile.addNewLinkstoGraph();
            AccessLinksTextFile.deleteRemovalLinkstoGraph();
            System.out.println("Enter");
            visual.importFile();//import the generated graph file into Gephi toolkit API workspace
            GraphModel model = Lookup.getDefault().lookup(GraphController.class).getModel();// get graph model            
            visual.setGraph(model, PropertyFile.getGraphType());//set the graph type
            visual.setGraph(model);
            System.out.println("Enter");
            visual.showGraph();//show the graph visualization in tool
            System.out.println("Enter");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void transferDataToDBFromXML(String projectPath, boolean op) {
        relationNodes = null;
        ReadFiles.readFiles(projectPath);
        Map<String, ArtefactElement> UMLAretefactElements = UMLArtefactManager.UMLAretefactElements;
        Map<String, ArtefactElement> sourceCodeAretefactElements = SourceCodeArtefactManager.sourceCodeAretefactElements;
        Map<String, ArtefactElement> requirementsAretefactElements = RequirementsManger.requirementArtefactElements;

        if (!op) {
            File f = new File(HomeGUI.projectPath + File.separator + FilePropertyName.PROPERTY + File.separator + HomeGUI.projectName
                    + ".graphdb");
            if (f.exists()) {
                System.out.println("File exist is deleted");
                f.delete();
            }
        }
        GraphDB graphDB = new GraphDB();
        graphDB.initiateGraphDB();

        System.out.println("Entering UML.....");
        graphDB.addNodeToGraphDB("UML", UMLAretefactElements);//add UML artefact elements to db
        System.out.println("Entering Req.....");
        graphDB.addNodeToGraphDB("REQ", requirementsAretefactElements);//add requirement artefact elements to db
        System.out.println("Entering SourceCode.....");
        graphDB.addNodeToGraphDB("SRC", sourceCodeAretefactElements);//add source code artefact elements to db

        //trace class links between UML & source code
        relationNodes = UMLSourceClassManager.compareClassNames(projectPath);
        graphDB.addRelationTOGraphDB(relationNodes);//add relationships between UML and SourceCode to db

        // trace class links between requirement & source code
        List<String> reqSrcRelationNodes = RequirementSourceClassManager
                .compareClassNames(projectPath);
        graphDB.addRelationTOGraphDB(reqSrcRelationNodes);//add relationships between Requirments and SourceCode to db

        List<String> reqUMLRelationNodes = RequirementUMLClassManager
                .compareClassNames(projectPath);
        graphDB.addRelationTOGraphDB(reqUMLRelationNodes);//add relationships between Requirements and UML to db

        relationNodes.addAll(reqSrcRelationNodes);
        relationNodes.addAll(reqUMLRelationNodes);

        List<String> sourceIntraRelations = IntraRelationManager.getSourceIntraRelation(projectPath);
        System.out.println("Source Intra Relation: " + sourceIntraRelations.size());
        graphDB.addIntraRelationTOGraphDB(sourceIntraRelations);//add intra relationships between SourceCode elements to db
        relationNodes.addAll(sourceIntraRelations);

        List<String> UMLIntraRelations = IntraRelationManager.getUMLIntraRelation(projectPath);
        System.out.println("UML Intra Relation: " + UMLIntraRelations.size());
        graphDB.addIntraRelationTOGraphDB(UMLIntraRelations);//add intra relationships between UML elements to db
        relationNodes.addAll(UMLIntraRelations);

        RelationManager.addLinks(relationNodes);

        graphDB.generateGraphFile();//generate the graph file from db
        GraphDBDelete.lock = false;
    }

    public static void deleteNodeFromSourceFile(Set<org.neo4j.graphdb.Node> deleteNodeProps, Set<Relationship> relProps, String xml) {
        String path = xml.substring(xml.lastIndexOf(File.separator));
        try {

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xml);
            //System.out.println("Nodes deleted: " + deleteNodeProps.size());
            // System.out.println("Relationships deleted: " + relProps.size());

            deleteRelations(relProps);
            for (org.neo4j.graphdb.Node nodeProp : deleteNodeProps) {
                System.out.println("" + nodeProp.getProperty("ID"));
                NodeList nodeList = document.getElementsByTagName("ArtefactElement");
                NodeList subList = document.getElementsByTagName("ArtefactSubElement");
                boolean found = false;

                for (int x = 0, size = nodeList.getLength(); x < size; x++) {
                    NodeList subNodeList = nodeList.item(x).getChildNodes();
                    //System.out.println("" + nodeList.item(x));
                    if (null != nodeList.item(x)) {
                        if (nodeList.item(x).getAttributes().getNamedItem("id").getNodeValue().equalsIgnoreCase(nodeProp.getProperty("ID").toString())) {
                            System.out.println("Donesc");
                            //System.out.println(""+nodeList.item(x).getAttributes().toString());
                            nodeList.item(x).getParentNode().removeChild(nodeList.item(x));
                            found = true;

                            break;
                        }
                    }
                }

                if (!found) {
                    for (int y = 0, sizeSb = subList.getLength(); y < sizeSb; y++) {
                        if (subList.item(y) != null) {
                            //System.out.println(""+subNodeList.item(y).getAttributes().toString());
                            if (subList.item(y).getAttributes().getNamedItem("id").getNodeValue().equalsIgnoreCase(nodeProp.getProperty("ID").toString())) {
                                System.out.println("Done");
                                subList.item(y).getParentNode().removeChild(subList.item(y));
                                found = false;
                                break;
                            }
                        }
                    }
                }
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            String xmlpath = HomeGUI.projectPath + File.separator + XML;
            File file = new File(xmlpath, path);

            System.out.println("file: " + file.getAbsolutePath());
            StreamResult result = new StreamResult(file.getPath());
            transformer.transform(source, result);

            System.out.println("Done nod");

        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (TransformerConfigurationException ex) {
            Exceptions.printStackTrace(ex);
        } catch (TransformerException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public static void readSourceFile(HashMap<String, Object> nodeProps, String id, String xmlFile) throws TransformerException {

        System.out.println(xmlFile);
        String xml = xmlFile.substring(xmlFile.lastIndexOf(File.separator));
        System.out.println("XML:" + xml);
        try {
            for (String key : nodeProps.keySet()) {
                System.out.println("Key: " + key + " Value: " + nodeProps.get(key));
            }
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xmlFile);

            NodeList nodeList = document.getElementsByTagName("ArtefactElement");
            parent:
            for (int x = 0, size = nodeList.getLength(); x < size; x++) {
                System.out.println("" + nodeList.item(x).getNodeName());
                String ID = nodeList.item(x).getAttributes().getNamedItem("id").getNodeValue();
                if (ID.equals(id)) {
                    NamedNodeMap attr = nodeList.item(x).getAttributes();
                    for (String key : nodeProps.keySet()) {
                        String keyval = key.replaceAll("\\s", "");
                        char a = keyval.charAt(0);
                        keyval = keyval.replace(a, keyval.toLowerCase().charAt(0));
                        if (key.equals("ID")) {
                            keyval = "id";
                        }
                        Node nodeAttr = attr.getNamedItem(keyval);
                        if (null == nodeAttr) {
                            System.out.println("Null ITme");
                        } else if (null == nodeProps.get(key) || nodeProps.get(key).toString().isEmpty()) {
                            nodeAttr.setTextContent("");
                        } else {
                            nodeAttr.setTextContent(nodeProps.get(key).toString());
                        }

                    }
                    break;
                }

                NodeList children = nodeList.item(x).getChildNodes();
                child:
                for (int y = 0, sizeChildren = children.getLength(); y < sizeChildren; y++) {
                    System.out.println("ch " + children.item(y).getNodeName());
                    if (children.item(y).getNodeType() == Node.ELEMENT_NODE) {
                        System.out.println("Element");
                        String val = children.item(y).getAttributes().getNamedItem("id").getNodeValue();
                        if (val.equals(id)) {
                            NamedNodeMap attr = children.item(y).getAttributes();
                            /*for (int i = 0; i < attr.getLength(); i++) {
                                String key = attr.item(i).getNodeName();
                                String value = attr.item(i).getNodeValue();
                                System.out.println(key + " "+value);
                                //attr.item(i).setNodeValue(value); 
                            }
                             */
                            for (String key : nodeProps.keySet()) {
                                System.out.println("" + key);
                            }
                            for (String key : nodeProps.keySet()) {
                                String keyval = key.replaceAll("\\s", "");
                                char a = keyval.charAt(0);
                                keyval = keyval.replace(a, keyval.toLowerCase().charAt(0));
                                if (key.equals("ID")) {
                                    keyval = "id";
                                }
                                Node attrItem = attr.getNamedItem(keyval);
                                System.out.println("Item: " + attrItem + " " + keyval);
                                System.out.println("val " + nodeProps.get(key));

                                if (null == attrItem) {
                                    System.out.println("Null atrITme");
                                } else if (null == nodeProps.get(key) || nodeProps.get(key).toString().isEmpty()) {
                                    attrItem.setNodeValue("");
                                } else {
                                    attrItem.setNodeValue(nodeProps.get(key).toString());
                                }

                                System.out.println(keyval + " " + nodeProps.get(key).toString() + " " + key);
                            }

                            break parent;
                        }
                    }

                }

            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            String xmlpath = HomeGUI.projectPath + File.separator + XML;
            File file = new File(xmlpath, xml);
            StreamResult result = new StreamResult(file.getPath());
            transformer.transform(source, result);

            System.out.println("Done");

        } catch (ParserConfigurationException | IOException | SAXException pce) {
            pce.printStackTrace();
        }
    }

    private static void deleteRelations(Set<Relationship> relProps) {
        String xml = HomeGUI.projectPath + File.separator + XML + File.separator + FilePropertyName.RELATION_ARTIFACT_NAME;
        String startNode, endNode, message;

        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(xml);
            int count = 0;
            for (Relationship relProp : relProps) {
                startNode = relProp.getStartNode().getProperty("ID").toString();
                endNode = relProp.getEndNode().getProperty("ID").toString();
                //System.out.println(startNode + "-" + endNode);
                message = relProp.getType().name().toLowerCase().replaceAll("_", "");
                //if type is sub element ignore..
                if (!relProp.getType().name().equalsIgnoreCase(GraphDB.RelTypes.SUB_ELEMENT.toString())) {
                    NodeList nodeList = document.getElementsByTagName("Relation");
                    parent:
                    for (int x = 0, size = nodeList.getLength(); x < size;) {
                        NodeList subNodeList = nodeList.item(x).getChildNodes();

                        if (subNodeList.item(1).getTextContent().equalsIgnoreCase(startNode)
                                && subNodeList.item(3).getTextContent().toLowerCase().equalsIgnoreCase(message)
                                && subNodeList.item(5).getTextContent().equalsIgnoreCase(endNode)) {
                            for (int y = 0, sizeSub = subNodeList.getLength(); y < sizeSub; y++) {
                                if (subNodeList.item(y) != null) {
                                    nodeList.item(x).removeChild(subNodeList.item(y));
                                }

                            }
                            if (nodeList.item(x) != null) {
                                nodeList.item(x).getParentNode().removeChild(nodeList.item(x));
                                break;
                            }
                        }
                        x++;
                    }
                }
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            String xmlpath = HomeGUI.projectPath + File.separator + XML;
            File file = new File(xmlpath, FilePropertyName.RELATION_ARTIFACT_NAME);
            StreamResult result = new StreamResult(file.getPath());
            transformer.transform(source, result);

            System.out.println("Done Rel");
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (TransformerConfigurationException ex) {
            Exceptions.printStackTrace(ex);
        } catch (TransformerException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
