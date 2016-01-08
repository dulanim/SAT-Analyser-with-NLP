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

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
import com.project.traceability.manager.ReadXML;
import static com.project.traceability.manager.ReadXML.transferDataToDBFromXML;
import java.awt.Component;
import java.io.File;
import java.util.Iterator;
import javax.xml.transform.TransformerException;
import org.neo4j.graphdb.Relationship;

/**
 *
 * @author Thanu
 */
@ServiceProvider(service = PreviewMouseListener.class)
public class GraphMouseListener implements PreviewMouseListener {

    final String ID = "ID";
    final String TYPE = "Type";
    GraphDatabaseService graphDb;
    ExecutionEngine engine;
    ExecutionResult result;

    @SuppressWarnings("finally")
    @Override
    public void mouseClicked(PreviewMouseEvent event, PreviewProperties properties, Workspace workspace) {

        for (Node node : Lookup.getDefault().lookup(GraphController.class).getModel(workspace).getGraph().getNodes()) {
            if (clickingInNode(node, event)) {

                graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(/*PropertyFile.getGraphDbPath()*/
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

                    HashMap<String, Object> values = showPopup(nodeProps, node);
                    int value = -1;
                    final String id = values.get("ID").toString();
                    if(values.containsKey("Value")){
                      value  = Integer.parseInt(values.get("Value").toString()) ;
                    }
                    int deleteVal = -1;
                    if(values.containsKey("Delete")){
                      deleteVal  = Integer.parseInt(values.get("Delete").toString());
                    }
                     

                    if (value == JOptionPane.NO_OPTION) {
                        tx.success();
                        GraphFileGenerator gg = new GraphFileGenerator();
                        gg.generateGraphFile(graphDb);
                        VisualizeGraph visual = VisualizeGraph.getInstance();
                        visual.importFile();
                        GraphModel model = Lookup.getDefault().lookup(GraphController.class).getModel();
                        visual.setGraph(model, PropertyFile.getGraphType());
                        visual.setPreview();
                        visual.setLayout();
                    } else if (value == JOptionPane.YES_OPTION) {
                        //ReadXML.initApp(HomeGUI.projectPath, PropertyFile.graphType);
                        System.out.println("Heliio");
                        transferDataToDBFromXML(projectPath);

                        VisualizeGraph visual = VisualizeGraph.getInstance();
                        visual.importFile();//import the generated graph file into Gephi toolkit API workspace
                        GraphModel model = Lookup.getDefault().lookup(GraphController.class).getModel();// get graph model            
                        visual.setGraph(model, PropertyFile.getGraphType());//set the graph type
                        HomeGUI.isComaparing = false;
                        visual.setPreview();
                        visual.setLayout();
                        tx.success();
                    }
                    
                    if(deleteVal == JOptionPane.YES_OPTION){
                        System.out.println("Heliio Deletion");
                        transferDataToDBFromXML(projectPath);

                        VisualizeGraph visual = VisualizeGraph.getInstance();
                        visual.importFile();//import the generated graph file into Gephi toolkit API workspace
                        GraphModel model = Lookup.getDefault().lookup(GraphController.class).getModel();// get graph model            
                        visual.setGraph(model, PropertyFile.getGraphType());//set the graph type
                        HomeGUI.isComaparing = false;
                        visual.setPreview();
                        visual.setLayout();
                        tx.success();
                    }

                } catch (Exception e) {
                    Exceptions.printStackTrace(e);
                    System.out.println(e.toString());
                } finally {
                    //tx.finish();
                    graphDb.shutdown();
                    return;
                }
            }
        }
        properties.removeSimpleValue("display-label.node.id");
        event.setConsumed(true);
    }

    @Override
    public void mousePressed(PreviewMouseEvent pme, PreviewProperties pp, Workspace wrkspc) {

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
        JTextField field;
        //graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(/*PropertyFile.getGraphDbPath()*/HomeGUI.projectPath + File.separator + FilePropertyName.PROPERTY);

        engine = new ExecutionEngine(graphDb);
        final HashMap<String, Object> node_props = nodeProps;
        final JPanel panel = new JPanel(new GridLayout(0, 1));
        HashMap<String, Object> returnVal = new HashMap<>();
        final String id = node_props.get("ID").toString();
        returnVal.put("ID", id);

        for (String key : nodeProps.keySet()) {
            Object val = nodeProps.get(key);
            if (null != val) {
                field = new JTextField(nodeProps.get(key).toString());
                field.setName(key);
                if (key.equalsIgnoreCase(ID) || key.equalsIgnoreCase(TYPE)) {
                    field.setEditable(false);
                }
                panel.add(new JLabel(key + ": "));
                panel.add(field);
            }
        }

        Object[] options = {"Update", "Delete"};

        int response = JOptionPane.showOptionDialog(VisualizeGraph.getInstance().getFrame(), panel, "Node properties", JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, options, null);//options[0]);

        if (response == JOptionPane.YES_OPTION) {
            storeUpdatedNode(panel, nodeProps, id);
        } else if (response == JOptionPane.NO_OPTION) {
            int confirm = JOptionPane.showConfirmDialog(VisualizeGraph.getInstance().getFrame(), "Are you sure you want to delete?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                //delete the node
                IndexManager index = graphDb.index();
                Index<org.neo4j.graphdb.Node> artefacts = index.forNodes("ArtefactElement");
                IndexHits<org.neo4j.graphdb.Node> hits = artefacts.get("ID", id);

                org.neo4j.graphdb.Node neo4j_node = hits.getSingle();
                System.out.println("Properties from edition:");
                for (String key : nodeProps.keySet()) {
                    System.out.println("Key: " + key + " Value: " + nodeProps.get(key).toString());
                }
                Iterator<Relationship> nodeRelation = neo4j_node.getRelationships().iterator();
                if (!nodeRelation.hasNext()) {
                    //delete the node if there are no relatioships liked to it.
                    neo4j_node.delete();
                } else {
                    while (nodeRelation.hasNext()) {
                        Relationship rel = nodeRelation.next();
                        System.out.println(rel.getStartNode()+" to "+rel.getEndNode()+" is deleted.");
                        rel.delete();                        
                    }
                    neo4j_node.delete();
                }
                updateXMLFiles(id, nodeProps);
                returnVal.put("ID", id);
                returnVal.put("Delete", confirm);
                graphDb.shutdown();
            }
            if(confirm == JOptionPane.NO_OPTION){
                
            }
        }

        returnVal.put("Value", response);
        return returnVal;

    }

    /**
     * Stores the data in a temporary map and edits the relevant backends
     *
     * @param panel
     * @param nodeProps
     * @param id
     */
    public void storeUpdatedNode(JPanel panel, HashMap nodeProps, String id) {
        HashMap<String, Object> newNodeProps = new HashMap<>();
        //Updates the dataabase if there are any changes in the node properrties values.
        Component[] panelComp = panel.getComponents();
        for (Component comp : panelComp) {
            if (comp.getClass() == JTextField.class) {
                //if(!(comp.getName().equalsIgnoreCase(ID)||comp.getName().equalsIgnoreCase(TYPE))){
                JTextField data = (JTextField) comp;
                newNodeProps.put(comp.getName(), data.getText());
                //}
            }
        }
        if (newNodeProps.equals(nodeProps)) {
            System.out.println("No changes in the node");
        } else {
            System.out.println("Changes in Node:");
            for (String key : newNodeProps.keySet()) {
                System.out.println("Key: " + key + " Value: " + newNodeProps.get(key).toString());
            }
            edit(newNodeProps, id);
        }
    }

    /**
     * When the node is edited the method updates the changes to the relevant
     * database.
     *
     * @param nodeProps
     * @param id
     */
    public void edit(HashMap<String, Object> nodeProps, String id) {
        IndexManager index = graphDb.index();
        Index<org.neo4j.graphdb.Node> artefacts = index.forNodes("ArtefactElement");
        IndexHits<org.neo4j.graphdb.Node> hits = artefacts.get("ID", id);

        org.neo4j.graphdb.Node neo4j_node = hits.getSingle();
        System.out.println("Properties from edition:");
        for (String key : nodeProps.keySet()) {
            System.out.println("Key: " + key + " Value: " + nodeProps.get(key).toString());
            neo4j_node.setProperty(key, nodeProps.get(key).toString());
        }
        updateXMLFiles(id, nodeProps);
        graphDb.shutdown();
    }

    /**
     * Updates the relevant XML file when data changes
     *
     * @param id
     * @param nodeProps
     */
    public void updateXMLFiles(String id, HashMap<String, Object> nodeProps) {
        String xml = "";
        switch (id.charAt(0)) {
            case 'S': {
                try {
                    xml = HomeGUI.projectPath + File.separator + XML + File.separator + FilePropertyName.SOURCE_ARTIFACT_NAME;
                    ReadXML.readSourceFile(nodeProps, id, xml);
                } catch (TransformerException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            break;
            case 'R':
                try {
                    xml = HomeGUI.projectPath + File.separator + XML + File.separator + FilePropertyName.REQUIREMENT_ARTIFACT_NAME;
                    ReadXML.readSourceFile(nodeProps, id, xml);
                } catch (TransformerException ex) {
                    Exceptions.printStackTrace(ex);
                }
                break;
            case 'D':
                try {
                    xml = HomeGUI.projectPath + File.separator + XML + File.separator + FilePropertyName.UML_ARTIFACT_NAME;
                    ReadXML.readSourceFile(nodeProps, id, xml);
                } catch (TransformerException ex) {
                    Exceptions.printStackTrace(ex);
                }
                break;
            default:
                //do nothing
                break;
        }
    }
}
