package com.project.extendedsat.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
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

import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.helpers.collection.IteratorUtil;
import org.openide.util.Exceptions;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import it.uniroma1.dis.wsngroup.gexf4j.core.Edge;
import it.uniroma1.dis.wsngroup.gexf4j.core.EdgeType;
import it.uniroma1.dis.wsngroup.gexf4j.core.Gexf;
import it.uniroma1.dis.wsngroup.gexf4j.core.Graph;
import it.uniroma1.dis.wsngroup.gexf4j.core.Mode;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.Attribute;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.AttributeClass;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.AttributeList;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.AttributeType;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.GexfImpl;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.StaxGraphWriter;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.data.AttributeListImpl;

/**
 *
 *
 * @author SAMITHA
 */

public class GraphFileGenerator {

	Session session;
	private Graph graph;
	StatementResult result = null;
	private HashMap<String, it.uniroma1.dis.wsngroup.gexf4j.core.Node> nodes;
	private HashMap<String, it.uniroma1.dis.wsngroup.gexf4j.core.Edge> edges;

	public void generateGraphFile(Session session) {
		System.out.println("Gexf graph " + session.toString());
		this.session = session;
		Gexf gexf = new GexfImpl();
		gexf.setVisualization(true);
		graph = gexf.getGraph();
		graph.setDefaultEdgeType(EdgeType.DIRECTED).setMode(Mode.DYNAMIC);

		// Transaction tx = graphDb.beginTx();

		// engine = new ExecutionEngine(graphDb);

		addNodes();
		addEdges();

		StaxGraphWriter graphWriter = new StaxGraphWriter();
		File f = new File("GexfFile");

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
			StreamResult stream = new StreamResult("GexfFile");
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

	public void addNodes() {

		AttributeList nodeAttrList = new AttributeListImpl(AttributeClass.NODE);
		graph.getAttributeLists().add(nodeAttrList);
		HashMap<String, Attribute> val = new HashMap<>();
		System.out.println("gg " + session);

		try (org.neo4j.driver.v1.Transaction tx = session.beginTransaction()) {
			result = DBsession.createsession().run("MATCH (n) RETURN n");
			ArrayList al = new ArrayList<Record>();

			while (result.hasNext()) {
				Record record = result.next();
				// System.out.println(record.get("name").asString());
				al.add(record);

			}
			Iterator<org.neo4j.graphdb.Node> itr = al.iterator();

			// Iterator<org.neo4j.graphdb.Node> n_column = l.listIterator();

			for (org.neo4j.graphdb.Node node : IteratorUtil.asIterable(itr)) {
				Iterable<String> property = node.getPropertyKeys();

				String id = (String) node.getProperty("ID");
				it.uniroma1.dis.wsngroup.gexf4j.core.Node new_node = graph.createNode(id);
				new_node.setLabel(id);

				for (String prop : property) {
					if (!val.containsKey(prop)) {
						Attribute attr = nodeAttrList.createAttribute(prop, AttributeType.STRING, prop);
						val.put(prop, attr);
						new_node.getAttributeValues().addValue(attr, (String) node.getProperty(prop));
					} else {
						new_node.getAttributeValues().addValue(val.get(prop), (String) node.getProperty(prop));
					}
				}
				nodes.put(id, new_node);
			}
			System.out.println("Size: " + nodes.size());
			tx.success();

		}

	}

	/**
	 * Method to add edges to gexf graph file
	 *
	 */
	public void addEdges() {
		AttributeList edgeAttrList = new AttributeListImpl(AttributeClass.EDGE);
		graph.getAttributeLists().add(edgeAttrList);

		Attribute attr_rt = edgeAttrList.createAttribute("neo4j_rt", AttributeType.STRING, "Neo4j Relationship Type");
		Attribute attr_msg = edgeAttrList.createAttribute("message", AttributeType.STRING, "Message");
		Attribute attr_stat = edgeAttrList.createAttribute("status", AttributeType.STRING, "Status");

		try (Transaction tx = (Transaction) session.beginTransaction()) {
			result = session.run("MATCH (n) RETURN n");
			Iterator<org.neo4j.graphdb.Node> column = (Iterator<Node>) result.consume();

			edges = new HashMap<>();

			for (org.neo4j.graphdb.Node node : IteratorUtil.asIterable(column)) {

				Iterable<Relationship> relationships = node.getRelationships(Direction.OUTGOING);

				for (Relationship rel : relationships) {
					it.uniroma1.dis.wsngroup.gexf4j.core.Node source = nodes.get(node.getProperty("ID"));
					it.uniroma1.dis.wsngroup.gexf4j.core.Node target = nodes.get(rel.getEndNode().getProperty("ID"));
					String id = Long.toString(rel.getId());

					if (!edges.containsKey(id)) {
						Edge e = source.connectTo(Long.toString(rel.getId()), rel.getType().name(), EdgeType.DIRECTED,
								target);
						/*
						 * if
						 * (WriteToXML.isTragging.equalsIgnoreCase("Tragging"))
						 * { String stat = (String) rel.getProperty("status");
						 * if (stat.isEmpty()) {
						 * e.getAttributeValues().addValue(attr_stat, ""); }
						 * else { e.getAttributeValues().addValue(attr_stat,
						 * (String) rel.getProperty("status")); } }
						 */
						e.getAttributeValues().addValue(attr_msg, (String) rel.getProperty("message"));
						e.getAttributeValues().addValue(attr_rt, rel.getType().name());
						edges.put(id.toString(), e);
					}

				}
			}
			tx.success();
		}
	}

}
