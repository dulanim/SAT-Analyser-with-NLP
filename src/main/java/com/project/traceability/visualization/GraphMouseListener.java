/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.visualization;

import com.project.NLP.file.operations.FilePropertyName;
import static com.project.NLP.file.operations.FilePropertyName.XML;
import com.project.traceability.GUI.HomeGUI;
import java.awt.GridLayout;
import java.util.HashMap;

import javax.swing.JPanel;

import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.Node;
import org.gephi.preview.api.PreviewMouseEvent;
import org.gephi.preview.api.PreviewProperties;
import org.gephi.preview.spi.PreviewMouseListener;
import org.gephi.project.api.Workspace;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.IndexManager;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

import static com.project.traceability.visualization.VisualizeGraph.popupMenu;
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Thanu
 */
@ServiceProvider(service = PreviewMouseListener.class)
public class GraphMouseListener implements PreviewMouseListener {

    private final String ID = "ID";
    private final String TYPE = "Type";
    private GraphDatabaseService graphDb;
    private ExecutionEngine engine;
    private ExecutionResult result;

    public static TableColumn tblclmnValue;
    public static TableCursor tableCursor;
    public static TableItem tableItem;

    private static String Id;
    public static HashMap<String, Object> nodeData = new HashMap<>();
    public static boolean update = false;

    static Workspace wkspace;
    static String id;

    public void shutDB() {
        graphDb.shutdown();
    }

    @SuppressWarnings("finally")
    @Override
    public void mouseClicked(PreviewMouseEvent event, PreviewProperties properties, final Workspace workspace) {
        wkspace = workspace;

        if (event.button == PreviewMouseEvent.Button.LEFT) {
            System.out.println("left Click");
            for (Node node : Lookup.getDefault().lookup(GraphController.class).getModel(workspace).getGraph().getNodes()) {
                if (clickingInNode(node, event)) {

                    graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(
                            HomeGUI.projectPath + File.separator + FilePropertyName.PROPERTY + File.separator + HomeGUI.projectName
                            + ".graphdb");
                    System.out.println("DB path- " + graphDb.toString());

                    Transaction tx = graphDb.beginTx();
                    try {

                        IndexManager index = graphDb.index();
                        Index<org.neo4j.graphdb.Node> artefacts = index.forNodes("ArtefactElement");
                        IndexHits<org.neo4j.graphdb.Node> hits = artefacts.get("ID", node.getNodeData().getAttributes().getValue("ID"));
                        org.neo4j.graphdb.Node neo4j_node = hits.getSingle();

                        System.out.println(neo4j_node.toString());

                        HashMap<String, Object> nodeProps = new HashMap<>();
                        for (String col : neo4j_node.getPropertyKeys()) {
                            Object val = neo4j_node.getProperty(col);
                            nodeProps.put(col, val);
                        }
                        System.out.println("Node: " + nodeProps);
                        HashMap<String, Object> values = showPopup(nodeProps, node);
                    } catch (Exception e) {
                        Exceptions.printStackTrace(e);
                        System.out.println(e.toString());
                    } finally {
                        tx.finish();
                        System.out.println("shutiing");
                        graphDb.shutdown();
                        try {
                            Thread.sleep(5);
                        } catch (Exception e) {
                            System.out.println("Error in thread sleeping");
                        }
                    }
                }
            }
        }
        if (event.button == PreviewMouseEvent.Button.RIGHT) {
            System.out.println("right Click");
            Node myNode = null;
            for (Node node : Lookup.getDefault().lookup(GraphController.class).getModel(workspace).getGraph().getNodes()) {
                if (clickingInNode(node, event)) {
                    myNode = node;
                }
            }
            String val = "";
            if (null != myNode) {
                val = myNode.getAttributes().getValue(ID).toString();
            }
            id = val;
            if (null != myNode) {

                System.out.println("entre");
                Display.getDefault().syncExec(new Runnable() {
                    @Override
                    public void run() {
                        popupMenu.setVisible(true);
                        VisualizeGraph.getInstance().getComposite().setMenu(popupMenu);
                    }
                });
            }
            System.out.println("entre ");

        }

        properties.removeSimpleValue("display-label.node.id");
        event.setConsumed(true);

    }

    @Override
    public void mousePressed(PreviewMouseEvent pme, PreviewProperties pp, Workspace wrkspc
    ) {

    }

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

    @Override
    public void mouseDragged(PreviewMouseEvent pme, PreviewProperties pp, Workspace wrkspc
    ) {
    }

    @Override
    public void mouseReleased(PreviewMouseEvent pme, PreviewProperties pp, Workspace wrkspc
    ) {
    }

    /**
     * @param node
     * @param event
     * @return
     */
    private boolean clickingInNode(Node node, PreviewMouseEvent event) {
        float xdiff = node.getNodeData().x() - event.x;
        float ydiff = -node.getNodeData().y() - event.y;//Note that y axis is inverse for node coordinates
        float radius = node.getNodeData().getRadius();

        return xdiff * xdiff + ydiff * ydiff < radius * radius;
    }

    /**
     * Method to show the pop up window when a node is clicked
     *
     * @param nodeProps HashMap
     * @return
     */
    public HashMap<String, Object> showPopup(final HashMap<String, Object> nodeProps, final Node node) {
        //JTextField field;
        //graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(/*PropertyFile.getGraphDbPath()*/HomeGUI.projectPath + File.separator + FilePropertyName.PROPERTY);

        engine = new ExecutionEngine(graphDb);
        final HashMap<String, Object> node_props = nodeProps;
        final JPanel panel = new JPanel(new GridLayout(0, 1));
        HashMap<String, Object> returnVal = new HashMap<>();
        final String id = node_props.get("ID").toString();
        returnVal.put("ID", id);
        Id = id;
        update = false;
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                HomeGUI.table.clearAll();
                HomeGUI.table.deselectAll();
                HomeGUI.table.removeAll();
                nodeData.clear();
                nodeData = new HashMap<>();
                int i = 0;
                for (String key : nodeProps.keySet()) {
                    Object val = nodeProps.get(key);
                    if (null != val) {
                        tableItem = new TableItem(HomeGUI.table, SWT.NONE, i);
                        tableItem.setText(0, key);
                        tableItem.setText(1, val.toString());
                        //field = new JTextField(nodeProps.get(key).toString());
                        //field.setName(key);
                        /*if (key.equalsIgnoreCase(ID) || key.equalsIgnoreCase(TYPE)) {
                    field.setEditable(false);
                }
                panel.add(new JLabel(key + ": "));
                panel.add(field);*/
                        i++;
                    }
                    nodeData.put(key, val);
                }

            }
        });

        /*int response = JOptionPane.showOptionDialog(VisualizeGraph.getInstance().getFrame(), panel, "Node properties", JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, options, null);//options[0]);

        if (response == JOptionPane.YES_OPTION) {
            storeUpdatedNode(panel, nodeProps, id);
        } else if (response == JOptionPane.NO_OPTION) {
            int confirm = JOptionPane.showConfirmDialog(VisualizeGraph.getInstance().getFrame(), "Are you sure you want to delete?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                deleteNodeAndRelations(id, nodeProps, confirm);
            }
            if (confirm == JOptionPane.NO_OPTION) {

            }
        }*/
        //if(update)
        // returnVal.put("Value", 1);
        return returnVal;
    }

    /**
     * Updates the relevant XML file when data changes
     *
     * @param id
     * @return
     */
    public static String updateXMLFiles(String id) {
        String xml = "";
        switch (id.charAt(0)) {
            case 'S': {
                xml = HomeGUI.projectPath + File.separator + XML + File.separator + FilePropertyName.SOURCE_ARTIFACT_NAME;
            }
            break;
            case 'R':
                xml = HomeGUI.projectPath + File.separator + XML + File.separator + FilePropertyName.REQUIREMENT_ARTIFACT_NAME;
                break;
            case 'D':
                xml = HomeGUI.projectPath + File.separator + XML + File.separator + FilePropertyName.UML_ARTIFACT_NAME;
                break;
            default:
                //do nothing
                break;
        }
        return xml;
    }

    /*public void checkRel(String id) {
        IndexManager index = graphDb.index();
        Index<Relationship> edges = index.forRelationships("SOURCE_TO_TARGET");
        IndexHits<Relationship> relHits = edges.get("ID", id);

        System.out.println("Chk: " + relHits.size() + " " + relHits.getSingle().getType().name());
    }

    public void checkNode(String id) {
        IndexManager index = graphDb.index();
        Index<org.neo4j.graphdb.Node> artefacts = index.forNodes("ArtefactElement");
        IndexHits<org.neo4j.graphdb.Node> relHits = artefacts.get("ID", id);
        System.out.println("Chk: " + relHits.size() + " " + relHits.getSingle());
    }*/
}
