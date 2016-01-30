/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.visualization;

import com.project.NLP.file.operations.FilePropertyName;
import static com.project.NLP.file.operations.FilePropertyName.XML;
import com.project.traceability.GUI.HomeGUI;
import static com.project.traceability.visualization.CheckBoxList.checklist;
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

import static com.project.traceability.visualization.VisualizeGraph.newLink;
import static com.project.traceability.visualization.VisualizeGraph.popupMenu;
import static com.project.traceability.visualization.VisualizeGraph.removeLink;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.gephi.graph.api.Edge;
import org.neo4j.graphdb.Relationship;
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

    public void shutDB() {
        graphDb.shutdown();
    }

    @SuppressWarnings("finally")
    @Override
    public void mouseClicked(PreviewMouseEvent event, PreviewProperties properties, final Workspace workspace) {

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
            final String id = val;
            if (null != myNode) {
                Display.getDefault().asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("entre");
                        newLink.addSelectionListener(new SelectionAdapter() {
                            @Override
                            public void widgetSelected(SelectionEvent se) {
                                // User is asked for details in creating a new link
                                System.out.println("Adding a Link");

                                final ArrayList<Node> nodeRelations = new ArrayList<>();
                                Node clickNode = null;

                                for (Node node : Lookup.getDefault().lookup(GraphController.class).getModel(workspace).getGraph().getNodes()) {
                                    if (node.getAttributes().getValue("ID").toString().equalsIgnoreCase(id)) {
                                        clickNode = node;
                                    }
                                    nodeRelations.add(node);
                                }

                                for (Edge edge : Lookup.getDefault().lookup(GraphController.class).getModel(workspace).getGraph().getEdges()) {
                                    if (edge.getSource() == clickNode) {
                                        nodeRelations.remove(edge.getTarget());
                                    } else if (edge.getTarget() == clickNode) {
                                        nodeRelations.remove(edge.getSource());
                                    }
                                }

                                final JFrame frame = new JFrame("Links Addition");
                                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                frame.setTitle("Links Addition");
                                JPanel panel = new JPanel();
                                panel.setLayout(new GridLayout(7, 1));

                                String[] artefactsList = {"", "Requirement", "Source", "Diagram"};
                                String[] nodeList;

                                JLabel lblArtefact = new JLabel("End Node Artefact:");
                                final JComboBox artefactCombo = new JComboBox(artefactsList);
                                JLabel lblType = new JLabel("End Node Type:");
                                Vector comboBoxItems1 = new Vector();
                                comboBoxItems1.add("");
                                comboBoxItems1.add("Class");
                                comboBoxItems1.add("Field/Attribute");
                                comboBoxItems1.add("Method/Operation");

                                final DefaultComboBoxModel model = new DefaultComboBoxModel(comboBoxItems1);
                                final JComboBox typeCombo = new JComboBox(model);
                                typeCombo.setEnabled(false);

                                JLabel lblNode = new JLabel("End Node ID:");
                                Vector comboBoxItems = new Vector();
                                final DefaultComboBoxModel modelNode = new DefaultComboBoxModel(comboBoxItems);
                                final JComboBox nodeCombo = new JComboBox(modelNode);
                                nodeCombo.setEnabled(false);

                                final ArrayList<Node> nodes = new ArrayList<>();
                                final ArrayList<Node> finalNodes = new ArrayList<>();

                                artefactCombo.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        nodes.clear();
                                        finalNodes.clear();
                                        if (artefactCombo.getSelectedItem() != null) {
                                            String artefact = artefactCombo.getSelectedItem().toString();
                                            for (Node n : nodeRelations) {
                                                if (n.getAttributes().getValue("Artefact").toString().equalsIgnoreCase(artefact)) {
                                                    nodes.add(n);
                                                }
                                            }
                                            int index = id.indexOf("_");
                                            if (index > 0) {
                                                String s = id.substring(index + 1);
                                                System.out.println("" + s);
                                                switch (s.charAt(0)) {
                                                    case 'F':
                                                        model.removeElement("Class");
                                                        model.removeElement("Method/Operation");
                                                        typeCombo.setEnabled(true);
                                                        break;
                                                    case 'M':
                                                        model.removeElement("Class");
                                                        model.removeElement("Field/Attribute");
                                                        typeCombo.setEnabled(true);
                                                        break;
                                                }
                                            } else {
                                                typeCombo.setEnabled(true);
                                            }
                                            modelNode.removeAllElements();
                                        }
                                    }
                                });

                                typeCombo.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        if (typeCombo.getSelectedItem() != null) {
                                            finalNodes.clear();
                                            String type = typeCombo.getSelectedItem().toString();
                                            if (type.equalsIgnoreCase("Field/Attribute")) {
                                                if (artefactCombo.getSelectedItem().toString().trim().charAt(0) != id.charAt(0)) {
                                                    for (Node n : nodes) {
                                                        if (n.getAttributes().getValue("Type").toString().equalsIgnoreCase("Field")
                                                                || n.getAttributes().getValue("Type").toString().equalsIgnoreCase("UMLAttribute")) {
                                                            finalNodes.add(n);
                                                        }
                                                    }
                                                }
                                            } else if (type.equalsIgnoreCase("Method/Operation")) {
                                                if (artefactCombo.getSelectedItem().toString().trim().charAt(0) != id.charAt(0)) {
                                                    for (Node n : nodes) {
                                                        if (n.getAttributes().getValue("Type").toString().equalsIgnoreCase("Method")
                                                                || n.getAttributes().getValue("Type").toString().equalsIgnoreCase("UMLOperation")) {
                                                            finalNodes.add(n);
                                                        }
                                                    }
                                                } else {
                                                    typeCombo.setEnabled(false);
                                                }
                                            } else {
                                                for (Node n : nodes) {
                                                    if (n.getAttributes().getValue("Type").toString().equalsIgnoreCase(type)) {
                                                        finalNodes.add(n);
                                                    }
                                                }
                                            }
                                            for (Node n : finalNodes) {
                                                modelNode.addElement(n.getAttributes().getValue("ID"));
                                            }
                                            nodeCombo.setEnabled(true);
                                        }
                                    }
                                });

                                JPanel panelButton = new JPanel();
                                JButton btnCreate = new JButton("Create");
                                btnCreate.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        createLink();
                                        finalNodes.clear();
                                        nodeRelations.clear();
                                        frame.dispose();
                                    }

                                    public void createLink() {
                                        String start = id;
                                        String end = nodeCombo.getSelectedItem().toString();
                                        String type = typeCombo.getSelectedItem().toString();
                                        String artefact = artefactCombo.getSelectedItem().toString();
                                        String relType = "";
                                        String temp;
                                        if (type.equalsIgnoreCase("Class")) {
                                            switch (id.charAt(0)) {
                                                case 'R':
                                                    if (artefact.equalsIgnoreCase("Source")) {
                                                        relType = GraphDB.RelTypes.REQ_CLASS_TO_SOURCE_CLASS.toString();
                                                    } else if (artefact.equalsIgnoreCase("Diagram")) {
                                                        relType = GraphDB.RelTypes.REQ_CLASS_TO_UML_CLASS.toString();
                                                    }
                                                    break;
                                                case 'S':
                                                    if (artefact.equalsIgnoreCase("Requirement")) {
                                                        temp = start;
                                                        start = end;
                                                        end = temp;
                                                        relType = GraphDB.RelTypes.REQ_CLASS_TO_SOURCE_CLASS.toString();
                                                    } else if (artefact.equalsIgnoreCase("Diagram")) {
                                                        temp = start;
                                                        start = end;
                                                        end = temp;
                                                        relType = GraphDB.RelTypes.UML_CLASS_TO_SOURCE_CLASS.toString();
                                                    }
                                                    break;
                                                case 'D':
                                                    if (artefact.equalsIgnoreCase("Requirement")) {
                                                        temp = start;
                                                        start = end;
                                                        end = temp;
                                                        relType = GraphDB.RelTypes.REQ_CLASS_TO_UML_CLASS.toString();
                                                    } else if (artefact.equalsIgnoreCase("Source")) {
                                                        relType = GraphDB.RelTypes.UML_CLASS_TO_SOURCE_CLASS.toString();
                                                    }
                                                    break;
                                            }
                                        } else if (type.equalsIgnoreCase("Field/Attribute")) {
                                            switch (id.charAt(0)) {
                                                case 'R':
                                                    if (artefact.equalsIgnoreCase("Source")) {
                                                        relType = GraphDB.RelTypes.REQ_FIELD_TO_SOURCE_FIELD.getValue();
                                                    } else if (artefact.equalsIgnoreCase("Diagram")) {
                                                        relType = GraphDB.RelTypes.REQ_FIELD_TO_UML_ATTRIBUTE.toString();
                                                    } else if (artefact.equalsIgnoreCase("Requirement") && !id.contains("_")) {
                                                        relType = GraphDB.RelTypes.SUB_ELEMENT.toString();
                                                    }
                                                    break;
                                                case 'S':
                                                    if (artefact.equalsIgnoreCase("Requirement")) {
                                                        temp = start;
                                                        start = end;
                                                        end = temp;
                                                        relType = GraphDB.RelTypes.REQ_FIELD_TO_SOURCE_FIELD.toString();
                                                    } else if (artefact.equalsIgnoreCase("Diagram")) {
                                                        temp = start;
                                                        start = end;
                                                        end = temp;
                                                        relType = GraphDB.RelTypes.UML_ATTRIBUTE_TO_SOURCE_FIELD.toString();
                                                    } else if (artefact.equalsIgnoreCase("Source") && !id.contains("_")) {
                                                        relType = GraphDB.RelTypes.SUB_ELEMENT.toString();
                                                    }
                                                    break;
                                                case 'D':
                                                    if (artefact.equalsIgnoreCase("Requirement")) {
                                                        temp = start;
                                                        start = end;
                                                        end = temp;
                                                        relType = GraphDB.RelTypes.REQ_FIELD_TO_UML_ATTRIBUTE.toString();
                                                    } else if (artefact.equalsIgnoreCase("Source")) {
                                                        relType = GraphDB.RelTypes.UML_ATTRIBUTE_TO_SOURCE_FIELD.toString();
                                                    } else if (artefact.equalsIgnoreCase("Diagram") && !id.contains("_")) {
                                                        relType = GraphDB.RelTypes.SUB_ELEMENT.toString();
                                                    }
                                                    break;
                                            }

                                        } else if (type.equalsIgnoreCase("Method/Operation")) {
                                            switch (id.charAt(0)) {
                                                case 'R':
                                                    if (artefact.equalsIgnoreCase("Diagram")) {
                                                        relType = GraphDB.RelTypes.REQ_METHOD_TO_UML_METHOD.toString();
                                                    } else if (artefact.equalsIgnoreCase("Source")) {
                                                        relType = GraphDB.RelTypes.REQ_METHOD_TO_SOURCE_METHOD.toString();
                                                    } else if (artefact.equalsIgnoreCase("Requirement") && !id.contains("_")) {
                                                        relType = GraphDB.RelTypes.SUB_ELEMENT.toString();
                                                    }
                                                    break;
                                                case 'S':
                                                    if (artefact.equalsIgnoreCase("Requirement")) {
                                                        relType = GraphDB.RelTypes.REQ_METHOD_TO_SOURCE_METHOD.toString();
                                                    } else if (artefact.equalsIgnoreCase("Diagram")) {
                                                        relType = GraphDB.RelTypes.UML_OPERATION_TO_SOURCE_METHOD.toString();
                                                    } else if (artefact.equalsIgnoreCase("Source") && !id.contains("_")) {
                                                        relType = GraphDB.RelTypes.SUB_ELEMENT.toString();
                                                    }
                                                    break;
                                                case 'D':
                                                    if (artefact.equalsIgnoreCase("Requirement")) {
                                                        relType = GraphDB.RelTypes.REQ_METHOD_TO_UML_METHOD.toString();
                                                    } else if (artefact.equalsIgnoreCase("Source")) {
                                                        relType = GraphDB.RelTypes.UML_OPERATION_TO_SOURCE_METHOD.toString();
                                                    } else if (artefact.equalsIgnoreCase("Diagram") && !id.contains("_")) {
                                                        relType = GraphDB.RelTypes.SUB_ELEMENT.toString();
                                                    }
                                                    break;
                                            }
                                        }

                                        System.out.println("" + start + " " + end + " " + GraphDB.RelTypes.parseEnum(relType) + " " + relType + " " + artefact + " " + type);
                                        int count = addToGEXF(start, end, relType);
                                        VisualizeGraph.refreshGraph();
                                        addNewLinksFile(count, start, end, relType);
                                    }

                                    private void addNewLinksFile(int count, String start, String end, String relType) {
                                        String newLinkFile = HomeGUI.projectPath + File.separator + FilePropertyName.PROPERTY + File.separator + "NewGraphLinks.txt";
                                        String source = count + ":" + start + " " + end + " " + relType + "\n";
                                        File file = new File(newLinkFile);
                                        try (FileWriter writer = new FileWriter(file, true)) {
                                            writer.write(System.getProperty("line.separator"));
                                            writer.write(source);
                                        } catch (IOException e) {
                                        }
                                    }
                                });

                                JButton btnCancel = new JButton("Cancel");
                                btnCancel.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        finalNodes.clear();
                                        nodeRelations.clear();
                                        frame.dispose();
                                    }
                                });
                                panel.add(lblArtefact);
                                panel.add(artefactCombo);
                                panel.add(lblType);
                                panel.add(typeCombo);
                                panel.add(lblNode);
                                panel.add(nodeCombo);

                                panelButton.add(btnCreate);
                                panelButton.add(btnCancel);
                                panel.add(panelButton);

                                JScrollPane scrPane = new JScrollPane(panel);
                                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                                frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);

                                frame.add(panel);
                                frame.pack();
                                frame.setPreferredSize(new Dimension(100, 100));
                                frame.setVisible(true);

                            }
                        });

                        removeLink.addSelectionListener(new SelectionAdapter() {
                            @Override
                            public void widgetSelected(SelectionEvent se) {
                                // User is asked for details in deleting a new link
                                System.out.println("Removing a Link");
                                Iterator<Relationship> nodeRelations = null;

                                final ArrayList<Edge> edgesList = new ArrayList();

                                for (Node node : Lookup.getDefault().lookup(GraphController.class).getModel(workspace).getGraph().getNodes()) {
                                    if (node.getAttributes().getValue("ID").toString().equalsIgnoreCase(id)) {
                                        System.out.println("Looking for "+node+" id: "+id+ " "+ node.getAttributes().getValue("ID"));
                                        for (Edge edge : Lookup.getDefault().lookup(GraphController.class).getModel(workspace).getGraph().getEdges()) {
                                            if (edge.getSource() == node) {
                                                edgesList.add(edge);
                                            } else if (edge.getTarget() == node) {
                                                edgesList.add(edge);
                                            }
                                        }
                                    }
                                }

                                int count = 0;
                                final ArrayList<JCheckBox> relIStrings = new ArrayList();

                                for (Edge e : edgesList) {
                                    relIStrings.add(new JCheckBox(e.getSource().getAttributes().getValue("ID") + "-" + e.getTarget().getAttributes().getValue("ID")));
                                }

                                try {
                                    Thread.sleep(10);
                                } catch (InterruptedException ex) {
                                    Exceptions.printStackTrace(ex);
                                }

                                final JFrame frame = new JFrame("Links Removal");
                                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                JPanel panel = new JPanel();
                                panel.setLayout(new GridLayout(2, 1));
                                final CheckBoxList cbList = new CheckBoxList(relIStrings.size());
                                cbList.setListData(relIStrings.toArray());

                                JPanel panelButton = new JPanel();
                                JButton btnDelete = new JButton("Delete");
                                btnDelete.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        frame.dispose();
                                        System.out.println("1");
                                        List<JCheckBox> selectedList = cbList.getSelectedValuesList();
                                        ArrayList<String> ids = new ArrayList<>();
                                        for (int i = 0; i < checklist.length; i++) {
                                            if (checklist[i] == 1) {
                                                System.out.println("" + (relIStrings.get(i)).getText());
                                                String id = edgesList.get(i).getAttributes().getValue(ID).toString();
                                                System.out.println("ID to be: "+id);
                                                String edge = (relIStrings.get(i)).getText();
                                                ids.add(String.valueOf(id));
                                            }
                                        }
                                        for (String id : ids) {
                                            removeEdgeFromGexf(Integer.parseInt(id));
                                            System.out.println("Deleeted from gexf "+id);
                                            addDeleteLinksFile(id);
                                        }
                                        VisualizeGraph.refreshGraph();

                                        System.out.println("3");
                                        edgesList.clear();
                                        relIStrings.clear();
                                    }

                                    private void addDeleteLinksFile(String id) {
                                        String deleteLinkFile = HomeGUI.projectPath + File.separator + FilePropertyName.PROPERTY + File.separator + "DeletedGraphLinks.txt";
                                        String newLinkFile = HomeGUI.projectPath + File.separator + FilePropertyName.PROPERTY + File.separator + "NewGraphLinks.txt";
                                        //String source = start + " " + end + " " + relType + "\n";
                                        File file = new File(newLinkFile);
                                        boolean delete = false;
                                        /*try (FileReader reader = new FileReader(file)) {
                                            BufferedReader bufferedReader = new BufferedReader(reader);
                                            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
                                            
                                            String line, line2;
                                            while ((line = bufferedReader.readLine()) != null) {
                                                String[] newLink = line.split(" ");
                                                if (newLink.length > 2) {
                                                    String idFile = line.substring(0, line.indexOf(":"));
                                                    if (Integer.parseInt(id) != -1) {
                                                        line = line.replaceAll(line, "");
                                                        writer.write(line);
                                                        delete = true;
                                                    }
                                                    //GraphMouseListener.addToGEXF(start, end, type);
                                                }
                                            }
                                            bufferedReader.close();
                                            reader.close();
                                            writer.close();

                                        } */
                                        try {
                                            File inputFile = new File(newLinkFile);
                                            File tempFile = new File("TempFile.txt");

                                            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                                            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

                                            String lineToRemove = id;
                                            String currentLine;

                                            while ((currentLine = reader.readLine()) != null) {
                                                // trim newline when comparing with lineToRemove
                                                if(currentLine.length()>1){
                                                    String trimmedLine = currentLine.trim();
                                                String currentID = currentLine.substring(0, currentLine.indexOf(":"));
                                                if (currentID.equals(lineToRemove)) {
                                                    System.out.println("Curr "+currentLine);
                                                    delete = true;
                                                    continue;
                                                }
                                                writer.write(currentLine + System.getProperty("line.separator"));
                                                }
                                                
                                            }
                                            writer.close();
                                            reader.close();
                                            String name = inputFile.getAbsolutePath();
                                            inputFile.delete();
                                            File renameFile = new File(name);
                                            boolean successful = tempFile.renameTo(renameFile);

                                        } 
                                        catch (IOException e) {
                                        }
                                        if (!delete) {
                                            String source = id + " ";
                                            File file2 = new File(deleteLinkFile);
                                            try (FileWriter writer = new FileWriter(file2, true)) {
                                                writer.write(source);
                                            } catch (IOException e) {
                                            }
                                        }

                                    }
                                });

                                JButton btnCancel = new JButton("Cancel");
                                btnCancel.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        edgesList.clear();
                                        frame.dispose();
                                    }
                                });

                                panel.add(cbList);
                                panelButton.add(btnDelete);
                                panelButton.add(btnCancel);
                                panel.add(panelButton);

                                JScrollPane scrPane = new JScrollPane(panel);
                                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                                frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);

                                frame.add(panel);
                                frame.pack();
                                frame.setPreferredSize(new Dimension(100, 100));
                                frame.setVisible(true);

                                System.out.println("Total Rel: " + count);

                            }

                        });
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
                    System.out.println("Gexf suc1 :" + id);
                    child:
                    for (int j = 0; j < nlChild.item(i).getAttributes().getLength(); j++) {
                        if (nlChild.item(i).getAttributes().item(j).getNodeName().equalsIgnoreCase("id")) {
                            System.out.println("Gexf suc 2:" + id);
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
