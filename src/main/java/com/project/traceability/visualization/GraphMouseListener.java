/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.visualization;

import com.project.NLP.file.operations.FilePropertyName;
import com.project.traceability.GUI.HomeGUI;
import static com.project.traceability.GUI.HomeGUI.projectPath;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.gephi.filters.api.FilterController;
import org.gephi.filters.api.Query;
import org.gephi.filters.plugin.graph.EgoBuilder.EgoFilter;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.GraphView;
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
import org.neo4j.helpers.collection.IteratorUtil;
import org.neo4j.helpers.collection.MapUtil;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;

import com.project.traceability.common.PropertyFile;
import com.project.traceability.manager.ReadFiles;
import com.project.traceability.manager.ReadXML;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Map;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.eclipse.swt.awt.SWT_AWT;
import org.gephi.streaming.api.GraphUpdaterEventHandler;
import org.gephi.streaming.api.event.ElementEvent;
import org.gephi.streaming.api.event.ElementType;
import org.gephi.streaming.api.event.EventType;
import org.gephi.streaming.api.event.GraphEvent;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.tooling.GlobalGraphOperations;

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
    GraphUpdaterEventHandler gupdate;

    @SuppressWarnings("finally")
    @Override
    public void mouseClicked(PreviewMouseEvent event, PreviewProperties properties, Workspace workspace) {

        for (Node node : Lookup.getDefault().lookup(GraphController.class).getModel(workspace).getGraph().getNodes()) {
            if (clickingInNode(node, event)) {

                graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(/*PropertyFile.getGraphDbPath()*/
                        HomeGUI.projectPath + File.separator + FilePropertyName.PROPERTY);
                System.out.println("DB path- " + graphDb.toString());
                Transaction tx = graphDb.beginTx();                
                try {

                    IndexManager index = graphDb.index();
                    
                    /*ResourceIterable <String> st = GlobalGraphOperations.at(graphDb).getAllPropertyKeys();
                     System.out.println("Names");
                     for (String st1 : st) {
                     System.out.println("Key: "+st1);
                     }*/
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
                    int value = Integer.parseInt(values.get("Value").toString());
                    final String id = values.get("ID").toString();

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
                        VisualizeGraph preview = VisualizeGraph.getInstance();//PropertyFile.getVisual();//new VisualizeGraph();
                        preview.importFile();
                        GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
                        FilterController filterController = Lookup.getDefault().lookup(FilterController.class);
                        EgoFilter egoFilter = new EgoFilter();
                        egoFilter.setPattern(id);
                        egoFilter.setDepth(1);
                        egoFilter.setSelf(true);

                        Query queryEgo = filterController.createQuery(egoFilter);
                        GraphView viewEgo = filterController.filter(queryEgo);
                        graphModel.setVisibleView(viewEgo);
                        preview.setGraph(graphModel);
                        preview.setPreview();
                        preview.setLayout();
                    }

                } catch (Exception e) {
                    Exceptions.printStackTrace(e);
                    System.out.println(e.toString());
                } finally {
                    tx.finish();
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

        final JPanel btnPanel = new JPanel(new FlowLayout());

        JButton updateButton = new JButton("Update");
        updateButton.setPreferredSize(new Dimension(10, 10));
        updateButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println(id);
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
                //panel.setVisible(false);
                
                //VisualizeGraph.getInstance().getFrame().;
                
                if (newNodeProps.equals(nodeProps)) {
                    System.out.println("No changes in the node");
                } else {
                    System.out.println("Changes in Node:");
                    for (String key : newNodeProps.keySet()) {
                        System.out.println("Key: " + key + " Value: " + newNodeProps.get(key).toString());
                    }
                    edit(newNodeProps, id);
                }
                //VisualizeGraph.getInstance().getFrame().setEnabled(false);
                //VisualizeGraph.getInstance().getFrame().setVisible(false);
                //VisualizeGraph.getInstance().getFrame().dispose();
                
            }
        });

        updateButton.setPreferredSize(new Dimension(10, 10));

        JButton deleteButton = new JButton("Delete");
        deleteButton.setPreferredSize(new Dimension(10, 10));
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                result = engine.execute("MATCH (n)-[q]-() WHERE n.ID={id} WITH n,q OPTIONAL MATCH (n)-[r:SUB_ELEMENT]-(m) WITH n,q,m OPTIONAL MATCH(m)-[k]-(o) RETURN n,q,m,k", MapUtil.map("id", id));

                IndexManager index = graphDb.index();
                Index<org.neo4j.graphdb.Node> artefact = index.forNodes("ArtefactElement");

                Iterator<org.neo4j.graphdb.Node> n_column = result.columnAs("n");

                for (org.neo4j.graphdb.Node node : IteratorUtil
                        .asIterable(n_column)) {
                    artefact.remove(node);
                }

                Iterator<org.neo4j.graphdb.Node> m_column = result.columnAs("m");

                for (org.neo4j.graphdb.Node node : IteratorUtil
                        .asIterable(m_column)) {
                    artefact.remove(node);
                }

                Iterator<org.neo4j.graphdb.Node> k_column = result.columnAs("k");

                for (org.neo4j.graphdb.Node node : IteratorUtil
                        .asIterable(k_column)) {
                    artefact.remove(node);
                }
                result = engine.execute("MATCH (n)-[q]-() WHERE n.ID={id} WITH n,q OPTIONAL MATCH (n)-[r:SUB_ELEMENT]-(m) WITH n,q,m OPTIONAL MATCH(m)-[k]-(o) DELETE n,q,m,k", MapUtil.map("id", id));
                ReadFiles.deleteArtefact(id);
                ReadFiles.deleteRelation(id);
            }
        });

        panel.add(updateButton);
        panel.add(deleteButton);
        VisualizeGraph.getInstance().getFrame().add(panel);

        Object[] options = {"Update", "Delete"};

        int value = JOptionPane.showOptionDialog(VisualizeGraph.getInstance().getFrame(), panel, "Node properties", JOptionPane.YES_NO_OPTION,
                JOptionPane.PLAIN_MESSAGE, null, options, null);//options[0]);

        if (value == JOptionPane.NO_OPTION) {
            int val = JOptionPane.showConfirmDialog(VisualizeGraph.getInstance().getFrame(), "Are you sure you want to delete?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (val == JOptionPane.YES_OPTION) {

            }
        }

        returnVal.put(
                "Value", value);
        return returnVal;

    }

    /**
     * When the node is edited the method updates the changes to the relevant
     * database.
     *
     * @param nodeProps
     * @param id
     */
    public void edit(HashMap<String, Object> nodeProps, String id) {

        /*ExecutionEngine execEngine = new ExecutionEngine(graphDb);
         Map<String, Object> map = new HashMap<>();
         map.put("id", id);
         map.put("newNode", nodeProps);
         org.neo4j.graphdb.Node node = null;

         ExecutionResult execResult = execEngine.execute("MATCH (node { ID: id}) \n"
         + "SET node ={newNode} \n"
         + "RETURN node", map);
         if (execResult.iterator().hasNext()) {
         node = (org.neo4j.graphdb.Node) execResult.columnAs("node").next();
         System.out.println("Edited Node: " + node.toString());
         } else {
         System.out.println("No Edition");
         }
         */
        IndexManager index = graphDb.index();
        Index<org.neo4j.graphdb.Node> artefacts = index.forNodes("ArtefactElement");
        IndexHits<org.neo4j.graphdb.Node> hits = artefacts.get("ID", id);

        org.neo4j.graphdb.Node neo4j_node = hits.getSingle();
        System.out.println("Properties from edition:");
        for (String key : nodeProps.keySet()) {
            System.out.println("Key: " + key + " Value: " + nodeProps.get(key).toString());
            neo4j_node.setProperty(key, nodeProps.get(key).toString());
        }

        System.out.println("");

        System.out.println(neo4j_node.toString());

        for (String key : neo4j_node.getPropertyKeys()) {
            System.out.println("Key: " + key + " Value: " + neo4j_node.getProperty(key));

        }
        Map<String, Object> map = new HashMap<>();
        map.put(ID, id);
        map.put("newNode", nodeProps);
        System.out.println("DB check 1: "+graphDb.toString());

        ExecutionEngine execEngine = new ExecutionEngine(graphDb);
        ExecutionResult execResult = execEngine.execute("MATCH (n) WHERE n.ID={ID} SET n = {newNode} return n", map);
        if (execResult.iterator().hasNext()) {
            System.out.println("updated\n" + execResult.dumpToString());

        }

        Transaction tx = graphDb.beginTx();
        try {
            index = graphDb.index();
            artefacts = index.forNodes("ArtefactElement");
            hits = artefacts.get("ID", id);
            System.out.println("Node from DB\n" + hits.getSingle().toString());

            GraphFileGenerator preview = new GraphFileGenerator();
            preview.generateGraphFile(graphDb);
            /*VisualizeGraph visual = VisualizeGraph.getInstance();
            visual.importFile();
            GraphModel model = Lookup.getDefault().lookup(GraphController.class).getModel();
            visual.setGraph(model, PropertyFile.getGraphType());
            visual.setPreview();
            visual.setLayout();*/
            graphDb.shutdown();
            ReadXML.readSourceFile(nodeProps, id);           
            
            tx.success();

        } catch (Exception e) {
            Exceptions.printStackTrace(e);
            System.out.println(e.toString());
        }

    }
}
