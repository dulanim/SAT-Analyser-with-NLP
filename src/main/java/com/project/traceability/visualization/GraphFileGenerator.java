package com.project.traceability.visualization;

import com.project.traceability.common.PropertyFile;
import it.uniroma1.dis.wsngroup.gexf4j.core.Edge;
import it.uniroma1.dis.wsngroup.gexf4j.core.EdgeType;
import it.uniroma1.dis.wsngroup.gexf4j.core.Gexf;
import it.uniroma1.dis.wsngroup.gexf4j.core.Graph;
import it.uniroma1.dis.wsngroup.gexf4j.core.Mode;
import it.uniroma1.dis.wsngroup.gexf4j.core.Node;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.Attribute;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.AttributeClass;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.AttributeList;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.AttributeType;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.GexfImpl;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.StaxGraphWriter;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.data.AttributeListImpl;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.helpers.collection.IteratorUtil;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Model to add generate graph file graph db.
 *
 * @author Thanu
 *
 */
public class GraphFileGenerator {

    private ExecutionEngine engine;
    private ExecutionResult result;
    private GraphDatabaseService graphDb;
    private Graph graph;
    private HashMap<String, it.uniroma1.dis.wsngroup.gexf4j.core.Node> nodes;
    private HashMap<String, it.uniroma1.dis.wsngroup.gexf4j.core.Edge> edges;

    public ExecutionEngine getEngine() {
        return engine;
    }

    public void setEngine(ExecutionEngine engine) {
        this.engine = engine;
    }

    public ExecutionResult getResult() {
        return result;
    }

    public void setResult(ExecutionResult result) {
        this.result = result;
    }

    public GraphDatabaseService getGraphDb() {
        return graphDb;
    }

    public void setGraphDb(GraphDatabaseService graphDb) {
        this.graphDb = graphDb;
    }

    public Graph getGraph() {
        return graph;
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public HashMap<String, Node> getNodes() {
        return nodes;
    }

    public void setNodes(HashMap<String, Node> nodes) {
        this.nodes = nodes;
    }

    public HashMap<String, Edge> getEdges() {
        return edges;
    }

    public void setEdges(HashMap<String, Edge> edges) {
        this.edges = edges;
    }

    public static void main(String[] args) {

        GraphFileGenerator gg = new GraphFileGenerator();
        GraphDatabaseService graphDb = new GraphDatabaseFactory()
                .newEmbeddedDatabaseBuilder(PropertyFile.getGraphDbPath())
                .newGraphDatabase();
        gg.setGraphDb(graphDb);
        gg.generateGraphFile(gg.getGraphDb());
        graphDb.shutdown();
    }

    /** Method to generate gexf graph file from db using Gephi Toolkit API
     * @param graphDb GraphDatabaseService
     */
    public void generateGraphFile(GraphDatabaseService graphDb) {
        this.graphDb = graphDb;
        Gexf gexf = new GexfImpl();
        gexf.setVisualization(true);
        graph = gexf.getGraph();
        graph.setDefaultEdgeType(EdgeType.DIRECTED).setMode(Mode.STATIC);

        //Transaction tx = graphDb.beginTx();
        engine = new ExecutionEngine(graphDb);

        addNodes();
        addEdges();

        StaxGraphWriter graphWriter = new StaxGraphWriter();
        File f = new File(PropertyFile.getGeneratedGexfFilePath());


        Writer out;
        try {
            out = new FileWriter(f, false);
            graphWriter.writeToStream(gexf, out, "UTF-8");
            System.out.println(f.getAbsolutePath());

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = (Document) dBuilder.parse(f);
            doc.getDocumentElement().normalize();

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource source = new DOMSource(doc);
            StreamResult stream = new StreamResult(new File(PropertyFile.filePath+PropertyFile.getProjectName(),PropertyFile.getProjectName()+".gexf").getPath());
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(source, stream);

        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Exceptions.printStackTrace(ex);
        } catch (TransformerConfigurationException ex) {
            Exceptions.printStackTrace(ex);
        } catch (TransformerException ex) {
            Exceptions.printStackTrace(ex);
        }

    }

    /** Method to add nodes to gexf graph file
     * 
     */
    public void addNodes() {
        AttributeList nodeAttrList = new AttributeListImpl(AttributeClass.NODE);
        graph.getAttributeLists().add(nodeAttrList);

        HashMap<String, Attribute> val = new HashMap<>();

        try (Transaction tx = graphDb.beginTx()) {

            result = engine.execute("MATCH (n) RETURN n");

            Iterator<org.neo4j.graphdb.Node> n_column = result.columnAs("n");
            nodes = new HashMap<>();

            for (org.neo4j.graphdb.Node node : IteratorUtil
                    .asIterable(n_column)) {
                Iterable<String> property = node.getPropertyKeys();

                String id = (String) node.getProperty("ID");
                it.uniroma1.dis.wsngroup.gexf4j.core.Node new_node = graph
                        .createNode(id);
                new_node.setLabel(id);

                for (String prop : property) {
                    if (!val.containsKey(prop)) {
                        Attribute attr = nodeAttrList.createAttribute(prop,
                                AttributeType.STRING, prop);
                        val.put(prop, attr);
                        new_node.getAttributeValues().addValue(attr,
                                (String) node.getProperty(prop));
                    } else {
                        new_node.getAttributeValues().addValue(val.get(prop),
                                (String) node.getProperty(prop));
                    }
                }
                nodes.put(id, new_node);
            }
            System.out.println("Size: "+nodes.size());
            tx.success();
        }
    }

    /** Method to add edges to gexf graph file
     * 
     */
    public void addEdges() {
        AttributeList edgeAttrList = new AttributeListImpl(AttributeClass.EDGE);
        graph.getAttributeLists().add(edgeAttrList);

        Attribute attr_rt = edgeAttrList.createAttribute("neo4j_rt",
                AttributeType.STRING, "Neo4j Relationship Type");
        Attribute attr_msg = edgeAttrList.createAttribute("message",
                AttributeType.STRING, "Message");

        try (Transaction tx = graphDb.beginTx()) {
            result = engine.execute("MATCH (n) RETURN n");
            Iterator<org.neo4j.graphdb.Node> column = result.columnAs("n");

            edges = new HashMap<>();

            for (org.neo4j.graphdb.Node node : IteratorUtil.asIterable(column)) {

                Iterable<Relationship> relationships = node
                        .getRelationships(Direction.OUTGOING);

                for (Relationship rel : relationships) {
                    it.uniroma1.dis.wsngroup.gexf4j.core.Node source = nodes
                            .get(node.getProperty("ID"));
                    it.uniroma1.dis.wsngroup.gexf4j.core.Node target = nodes
                            .get(rel.getEndNode().getProperty("ID"));
                    String id = Long.toString(rel.getId());

                    if (!edges.containsKey(id)) {
                        Edge e = source
                                .connectTo(Long.toString(rel.getId()), rel
                                .getType().name(), EdgeType.DIRECTED,
                                target);
                        e.getAttributeValues().addValue(attr_msg,
                                (String) rel.getProperty("message"));
                        e.getAttributeValues().addValue(attr_rt,
                                rel.getType().name());
                        edges.put(id.toString(), e);
                    }

                }
            }
            tx.success();
        }
    }

    /** Method to import graph file into Gephi Toolkit API workspace
     * 
     */
    public void importGraphFile() {
        ProjectController pc = Lookup.getDefault().lookup(
                ProjectController.class);
        pc.newProject();
        Workspace workspace = pc.getCurrentWorkspace();

        // Import file
        ImportController importController = Lookup.getDefault().lookup(
                ImportController.class);
        Container container = null;
        try {
            File file = new File(PropertyFile.getGeneratedGexfFilePath());
            container = importController.importFile(file);
        } catch (Exception ex) {
            ex.printStackTrace();
            //return null;
        }

        // Append imported data to GraphAPI
        importController.process(container, new DefaultProcessor(), workspace);
    }
}
