package com.project.extendedsat.test;

import it.uniroma1.dis.wsngroup.gexf4j.core.*;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.Attribute;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.AttributeClass;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.AttributeList;
import it.uniroma1.dis.wsngroup.gexf4j.core.data.AttributeType;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.GexfImpl;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.StaxGraphWriter;
import it.uniroma1.dis.wsngroup.gexf4j.core.impl.data.AttributeListImpl;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;

public class GenerateGexf {

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

		//addNodes();
		addEdges();

		StaxGraphWriter graphWriter = new StaxGraphWriter();
		File f = new File("SourceToTest.gexf");

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

		} catch (Exception e){
			e.printStackTrace();
		}


	}

	public void addNodes() {
		
		AttributeList nodeAttrList = new AttributeListImpl(AttributeClass.NODE);
		graph.getAttributeLists().add(nodeAttrList);
		HashMap<String, Attribute> val = new HashMap<>();

		result = session.run("MATCH (n) RETURN n.name AS name");
		nodes = new HashMap<>();
		// System.out.println("new "+result.list().toString());
		ArrayList<Record> list = new ArrayList<Record>();

		while (result.hasNext()) {
			Record record = result.next();
			System.out.println(record.toString());
			list.add(record);

		}

		for (Record temp : list) {
			// System.out.println(temp);

			Iterable<String> property = temp.keys();

			String id = (String) temp.toString();
			it.uniroma1.dis.wsngroup.gexf4j.core.Node new_node = graph.createNode(id);
			new_node.setLabel(id);

			for (String prop : property) {
				if (!val.containsKey(prop)) {
					Attribute attr = nodeAttrList.createAttribute(prop, AttributeType.STRING, prop);
					val.put(prop, attr);
					new_node.getAttributeValues().addValue(attr, (String) temp.toString());
				} else {
					new_node.getAttributeValues().addValue(val.get(prop), (String) temp.toString());
				}
			}
			System.out.println(" &&&  "+id+" "+new_node);
			nodes.put(id, new_node);
		}
	}

	public void addEdges() {

		StatementResult result2 = session.run("match (n)-[r]->() return count(r)"); //getting the count of relationships		
		//System.out.println("   *********   "+result2.next().get(0));
		int val = result2.next().get(0).asInt();
		//System.out.println("new"+val);
		
		AttributeList edgeAttrList = new AttributeListImpl(AttributeClass.EDGE);
		graph.getAttributeLists().add(edgeAttrList);

//		Attribute attr_rt = edgeAttrList.createAttribute("neo4j_rt", AttributeType.STRING, "Neo4j Relationship Type");
//		Attribute attr_msg = edgeAttrList.createAttribute("message", AttributeType.STRING, "Message");
//		Attribute attr_stat = edgeAttrList.createAttribute("status", AttributeType.STRING, "Status");

		//result = session.run("MATCH (n) RETURN n");
		//List<String> key = result.keys();			
		
		
		 edges = new HashMap<>();

			StatementResult ress = session.run("MATCH (n) where not( (n)-[]->() or ()-[]->(n)) RETURN n.value AS name, n.name AS rName");
			while (ress.hasNext()) {

				Record sr=ress.next();

				String src=sr.get(0).toString().replace("\"","");
				graph.createNode(src).setLabel(src+"["+sr.get(1).toString().replace("\"","")+"]").setSize(8);
			}



			for(int i=0;i<val;i++){
				StatementResult res1 = session.run("MATCH (n)-[r]->() WHERE r.relationID ="+i+" RETURN n.value AS name, n.name AS rName");
				StatementResult res2 = session.run("MATCH ()-[r]->(n) WHERE r.relationID ="+i+" RETURN n.value AS name, n.name AS rName");
				StatementResult res3 = session.run("MATCH (n)-[r]->() WHERE r.relationID ="+i+" RETURN r.name AS name");
				//StatementResult res3 = session.run("MATCH ()-[r]->(n) WHERE r.relationID ="+i+" RETURN n.name AS name");
				//System.out.println("****"+res1.next().get(2).toString());
				Record sr=res1.next();
				Record tr=res2.next();
				String src=sr.get(0).toString().replace("\"","");
				String trt=tr.get(0).toString().replace("\"","");
				String rel=res3.next().get(0).toString().replace("\"","");
//				if (src.startsWith("S"))
//
//				System.out.println(src);
				it.uniroma1.dis.wsngroup.gexf4j.core.Node source = graph.createNode(src).setLabel(src+"["+sr.get(1).toString().replace("\"","")+"]").setSize(8);
				it.uniroma1.dis.wsngroup.gexf4j.core.Node target = graph.createNode(trt).setLabel(trt+"["+tr.get(1).toString().replace("\"","")+"]").setSize(8);
				String id = Long.toString(i);
				//graph.createNode(res2.next().toString());
				//nodes.get(node.getProperty("ID"));
				if (!edges.containsKey(id)) {
					Edge e = source.connectTo(Long.toString(i), "", EdgeType.DIRECTED,target);
					e.setLabel(rel).setThickness(100);
//					e.getAttributeValues().addValue(attr_msg, "getmessage");
//					e.getAttributeValues().addValue(attr_rt, "gettype");
					edges.put(id.toString(), e);
				}
				
			}
		 
	}

}
