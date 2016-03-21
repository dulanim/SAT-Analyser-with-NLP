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
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * Listens to the mouse clicks on the graph
 *
 * @author Thanu
 * @author Aarthika <>
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
    CCombo combo = null;
    static boolean lock = false;

    public void shutDB() {
        graphDb.shutdown();
    }

    @SuppressWarnings("finally")
    @Override
    public void mouseClicked(PreviewMouseEvent event, PreviewProperties properties, final Workspace workspace) {
        wkspace = workspace;
        if (event.button == PreviewMouseEvent.Button.LEFT) {
            for (Node node : Lookup.getDefault().lookup(GraphController.class).getModel(workspace).getGraph().getNodes()) {
                if (clickingInNode(node, event)) {

                    graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(
                            HomeGUI.projectPath + File.separator + FilePropertyName.PROPERTY + File.separator + HomeGUI.projectName
                            + ".graphdb");
                    //System.out.println("DB path- " + graphDb.toString());

                    Transaction tx = graphDb.beginTx();
                    try {

                        IndexManager index = graphDb.index();
                        Index<org.neo4j.graphdb.Node> artefacts = index.forNodes("ArtefactElement");
                        IndexHits<org.neo4j.graphdb.Node> hits = artefacts.get("ID", node.getNodeData().getAttributes().getValue("ID"));
                        org.neo4j.graphdb.Node neo4j_node = hits.getSingle();

                        //System.out.println(neo4j_node.toString());
                        id = neo4j_node.getProperty("ID").toString();

                        HashMap<String, Object> nodeProps = new HashMap<>();
                        for (String col : neo4j_node.getPropertyKeys()) {
                            Object val = neo4j_node.getProperty(col);
                            nodeProps.put(col, val);
                        }

                        HashMap<String, Object> values = showPopup(nodeProps, node);
                        tx.success();
                        tx.close();
                    } catch (Exception e) {
                        /*Exceptions.printStackTrace(e);
                        System.out.println(e.toString());*/
                    } finally {
                        tx.finish();
                        Display.getDefault().syncExec(new Runnable() {
                            @Override
                            public void run() {
                                VisualizeGraph.updateBtn.setEnabled(false);
                                VisualizeGraph.deleteBtn.setEnabled(false);
                            }
                        });
                        graphDb.shutdown();
                        Display.getDefault().syncExec(new Runnable() {
                            @Override
                            public void run() {
                                VisualizeGraph.updateBtn.setEnabled(true);
                                VisualizeGraph.deleteBtn.setEnabled(true);
                            }
                        });
                        GraphMouseListener.lock = true;
                        System.out.println("Releseing lock");
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
                Display.getDefault().syncExec(new Runnable() {
                    @Override
                    public void run() {
                        popupMenu.setVisible(true);
                        VisualizeGraph.getInstance().getComposite().setMenu(popupMenu);
                    }
                });
            }

        }

        properties.removeSimpleValue("display-label.node.id");
        event.setConsumed(true);

    }

    @Override
    public void mousePressed(PreviewMouseEvent pme, PreviewProperties pp, Workspace wrkspc) {
        Display.getDefault().asyncExec(new Runnable() {
            @Override
            public void run() {
                if (null != combo) {
                    combo.setVisible(false);
                    combo = null;
                }
            }
        });
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
     * Identifies the clicking node
     *
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
                combo = new CCombo(HomeGUI.table, SWT.NONE);
                combo.setVisible(true);

                HomeGUI.table.clearAll();
                HomeGUI.table.deselectAll();
                HomeGUI.table.removeAll();
                combo.removeAll();
                nodeData.clear();
                nodeData = new HashMap<>();
                int i = 0;
                for (String key : nodeProps.keySet()) {
                    Object val = nodeProps.get(key);
                    final TableEditor editor = new TableEditor(HomeGUI.table);
                    if (null != val) {
                        tableItem = new TableItem(HomeGUI.table, SWT.NONE, i);
                        tableItem.setText(0, key);
                        tableItem.setText(1, val.toString());
                        if (key.equalsIgnoreCase("Visibility")) {
                            combo.add("");
                            combo.add("Default");
                            combo.add("Public");
                            combo.add("Private");
                            combo.add("Protected");
                            combo.add("Default");
                            int index = 0;
                            //System.out.println("Item count " + combo.getItemCount());
                            for (int j = 0; j < combo.getItemCount(); j++) {
                                if (combo.getItem(j).equalsIgnoreCase(val.toString())) {
                                    combo.deselectAll();
                                    //System.out.println("Item " + val.toString() + " " + combo.getItem(j));
                                    combo.setText(combo.getItem(j));
                                    editor.grabHorizontal = true;
                                    editor.setEditor(combo, tableItem, 1);
                                    index = j;
                                    break;
                                }
                            }

                            combo.addSelectionListener(new SelectionListener() {
                                @Override
                                public void widgetSelected(SelectionEvent se) {
                                    int t = ((CCombo) se.getSource()).getSelectionIndex();
                                    //System.out.println("elected " + combo.getItem(t));
                                    //nodeData.replace("Visibility", combo.getItem(t));
                                    combo.setText(combo.getItem(t));
                                    for (TableItem it : HomeGUI.table.getItems()) {
                                        if (it.getText(0).equalsIgnoreCase("Visibility")) {
                                            editor.setEditor(combo, it, 1);
                                            //nodeData.replace("Visibility", combo.getItem(t));
                                            break;
                                        }
                                    }

                                }

                                @Override
                                public void widgetDefaultSelected(SelectionEvent se) {

                                }
                            });
                            //combo.setText(combo.getItem(index));

                        } else {
                            //combo.setVisible(false);
                        }

                        i++;
                    }
                    nodeData.put(key, val);
                }

            }
        });

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

}
