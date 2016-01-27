/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.visualization;

import com.project.NLP.file.operations.FilePropertyName;
import static com.project.NLP.file.operations.FilePropertyName.XML;
import com.project.traceability.GUI.HomeGUI;
import static com.project.traceability.GUI.HomeGUI.projectPath;
import java.awt.GridLayout;
import java.util.HashMap;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
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

import com.project.traceability.common.PropertyFile;
import static com.project.traceability.manager.ReadXML.transferDataToDBFromXML;
import java.io.File;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

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
    public void mouseClicked(PreviewMouseEvent event, PreviewProperties properties, Workspace workspace) {
        
        if(event.button== PreviewMouseEvent.Button.LEFT){
            System.out.println("left Click");
        } 
        if(event.button== PreviewMouseEvent.Button.RIGHT){
            System.out.println("right Click");
        }
        
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
        properties.removeSimpleValue("display-label.node.id");
        event.setConsumed(true);
        
        
    }

    @Override
    public void mousePressed(PreviewMouseEvent pme, PreviewProperties pp, Workspace wrkspc) {
        for (Node node : Lookup.getDefault().lookup(GraphController.class).getModel(wrkspc).getGraph().getNodes()) {
            if (clickingInNode(node, pme)) {

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
                    
                    System.out.println("Mouse Pressed : " + neo4j_node.getProperty(ID));
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

    @Override
    public void mouseDragged(PreviewMouseEvent pme, PreviewProperties pp, Workspace wrkspc) {        
    }

    @Override
    public void mouseReleased(PreviewMouseEvent pme, PreviewProperties pp, Workspace wrkspc) {        
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
