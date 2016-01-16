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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import javax.xml.transform.TransformerException;
import org.neo4j.graphdb.Relationship;

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
    private Set<org.neo4j.graphdb.Node> deleteNodeProps;
    private Set<Relationship> relProps;

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
                    if (values.containsKey("Value")) {
                        value = Integer.parseInt(values.get("Value").toString());
                    }

                    if (value == JOptionPane.NO_OPTION) {
                        System.out.println("Heliio");
                        transferDataToDBFromXML(projectPath, false);

                        VisualizeGraph visual = VisualizeGraph.getInstance();
                        visual.importFile();//import the generated graph file into Gephi toolkit API workspace
                        GraphModel model = Lookup.getDefault().lookup(GraphController.class).getModel();// get graph model            
                        visual.setGraph(model, PropertyFile.getGraphType());//set the graph type
                        HomeGUI.isComaparing = false;
                        visual.setPreview();
                        visual.setLayout();
                        tx.success();
                    } else if (value == JOptionPane.YES_OPTION) {
                        //ReadXML.initApp(HomeGUI.projectPath, PropertyFile.graphType);
                        System.out.println("Heliio");
                        transferDataToDBFromXML(projectPath, true);

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
                deleteNodeAndRelations(id, nodeProps, confirm);
            }
            if (confirm == JOptionPane.NO_OPTION) {

            }
        }

        returnVal.put("Value", response);
        return returnVal;

    }

    public void deleteNodeAndRelations(final String id, final HashMap<String, Object> nodeProps, int confirm) {
        //delete the node
        IndexManager index = graphDb.index();
        Index<org.neo4j.graphdb.Node> artefacts = index.forNodes("ArtefactElement");
        IndexHits<org.neo4j.graphdb.Node> hits = artefacts.get("ID", id);

        org.neo4j.graphdb.Node neo4j_node = hits.getSingle();
        Iterator<Relationship> nodeRelation = neo4j_node.getRelationships().iterator();
        deleteNodeProps = new HashSet<>();
        relProps = new HashSet<>();
        deleteNodeProps.add(neo4j_node);

        deleteNode(nodeRelation, neo4j_node);
        System.out.println("Size: " + deleteNodeProps.size() + " " + relProps.size());
        delete(deleteNodeProps, relProps, id);
        new GraphDB().checkDB();

        //graphDb.shutdown();
        //updateXMLFiles(id, nodeProps);
        //returnVal.put("ID", id);
        //returnVal.put("Value", confirm);
    }

    public void deleteNode(Iterator<Relationship> nodeRelation, org.neo4j.graphdb.Node neo4j_node) {
        //System.out.println("Entering ub node delete " + neo4j_node.toString());        
        if (!nodeRelation.hasNext()) {
            //delete the node if there are no relatioships liked to it.
            deleteNodeProps.add(neo4j_node);
            //neo4j_node.delete();
        } else {
            while (nodeRelation.hasNext()) {
                Relationship rel = nodeRelation.next();
                //check if the relationship has any subelements to it.
                if (rel.isType(GraphDB.RelTypes.SUB_ELEMENT) && rel.getStartNode().toString().equalsIgnoreCase(neo4j_node.toString())) {
                    org.neo4j.graphdb.Node subNode = rel.getEndNode();
                    Iterator<Relationship> subRel = subNode.getRelationships().iterator();
                    deleteNode(subRel, subNode);
                } else if (rel.isType(GraphDB.RelTypes.SUB_ELEMENT) && rel.getEndNode().toString().equalsIgnoreCase(neo4j_node.toString())) {
                } else {
                    relProps.add(rel);
                    //rel.delete();
                    //deleteNodeProps.add(neo4j_node);
                }

            }
            // deleteNodeProps.add(neo4j_node);
            // neo4j_node.delete();
        }
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

    public void delete(Set<org.neo4j.graphdb.Node> nodeProps, Set<Relationship> relProps, String id) {
        String xml = updateXMLFiles(id);
        ReadXML.deleteNodeFromSourceFile(deleteNodeProps, relProps, xml);
        Transaction tx = graphDb.beginTx();
        ExecutionEngine execEngine = new ExecutionEngine(graphDb);
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        ExecutionResult execResult = null;

        try {
            IndexManager index = graphDb.index();
            Index<org.neo4j.graphdb.Node> artefacts = index.forNodes("ArtefactElement");
            Index<Relationship> edges = index.forRelationships("SOURCE_TO_TARGET");

            for (Relationship next : relProps) {
                org.neo4j.graphdb.Node source = next.getStartNode();
                org.neo4j.graphdb.Node target = next.getEndNode();
                System.out.print("Source: " + source.getProperty("ID") + " Target: " + target.getProperty("ID"));
                map.put("relID", next.getStartNode().getProperty("ID") + "-" + next.getEndNode().getProperty("ID"));
                IndexHits<Relationship> relHits = edges.get("ID", source.getProperty("ID") + "-" + target.getProperty("ID"));
                Relationship rel = relHits.getSingle();
                String relid = source.getProperty("ID") + "-" + target.getProperty("ID");
                if (rel != null) {
                    deleteRelDB(rel);
                }
            }

            for (org.neo4j.graphdb.Node next : nodeProps) {
                IndexHits<org.neo4j.graphdb.Node> nodeHits = artefacts.get("ID", next.getProperty("ID"));
                System.out.println("Node : " + next.getProperty("ID"));
                org.neo4j.graphdb.Node node = nodeHits.getSingle();
                if (node.hasRelationship(GraphDB.RelTypes.SUB_ELEMENT)) {
                    Iterable<Relationship> nodeRel = node.getRelationships(GraphDB.RelTypes.SUB_ELEMENT);
                    for (Iterator<Relationship> iterator = nodeRel.iterator(); iterator.hasNext();) {
                        Relationship relDel = iterator.next();
                        String relid = relDel.getStartNode().getProperty("ID") + "-" + relDel.getEndNode().getProperty("ID");
                        System.out.println("Source: " + relDel.getStartNode().getProperty("ID") + " Target: " + relDel.getEndNode().getProperty("ID"));
                        map.put("relID", relDel.getStartNode().getProperty("ID") + "-" + relDel.getEndNode().getProperty("ID"));
                        map.put("subid", relDel.getEndNode().getProperty("ID"));
                        deleteRelDB(relDel);

                    }
                    deleteNodeDB(node);
                    System.out.println("Deleted a:");
                } else {

                    deleteNodeDB(node);
                    System.out.println("Deletedb:");
                }
            }
            System.out.println("Complete Deletion");
            tx.success();
            tx.close();
        } finally {
            tx.finish();
            graphDb.shutdown();
        }

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

    public void deleteNodeDB(org.neo4j.graphdb.Node node) {
        try (Transaction tx = graphDb.beginTx()) {
            System.out.println("Before:" + node);
            if (node.hasRelationship()) {
                for (Relationship key : node.getRelationships()) {
                    if (key.isType(GraphDB.RelTypes.SUB_ELEMENT) && key.getStartNode() == node) {
                        key.getEndNode().delete();
                        System.out.println("REl" + key);
                        key.delete();
                    } else {
                        System.out.println("ll" + key);
                        key.delete();
                    }
                }
            }

            node.delete();
            System.out.println("After:" + node);
            System.out.println("Detail:" + node.hasRelationship());
            
            tx.success();
            tx.close();
        }
    }

    public void deleteRelDB(Relationship rel) {
        try (Transaction tx = graphDb.beginTx()) {
            System.out.println("Before:" + rel);
            if (rel.isType(GraphDB.RelTypes.SUB_ELEMENT)) {
                rel.getEndNode().delete();
                rel.delete();
            } else {
                rel.delete();
            }
            tx.success();
            tx.close();
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
        String xml = updateXMLFiles(id);
        try {
            ReadXML.readSourceFile(nodeProps, id, xml);
        } catch (TransformerException ex) {
            Exceptions.printStackTrace(ex);
        }
        try (Transaction tx = graphDb.beginTx()) {
            IndexManager index = graphDb.index();
            Index<org.neo4j.graphdb.Node> artefacts = index.forNodes("ArtefactElement");
            IndexHits<org.neo4j.graphdb.Node> hits = artefacts.get("ID", id);

            org.neo4j.graphdb.Node neo4j_node = hits.getSingle();
            System.out.println("Properties from edition:");
            for (String key : nodeProps.keySet()) {
                System.out.println("Key: " + key + " Value: " + nodeProps.get(key).toString());
                neo4j_node.setProperty(key, nodeProps.get(key).toString());
            }
            tx.success();
            tx.close();
        } finally {
            graphDb.shutdown();
        }

    }

    /**
     * Updates the relevant XML file when data changes
     *
     * @param id
     * @param nodeProps
     */
    public String updateXMLFiles(String id) {
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
