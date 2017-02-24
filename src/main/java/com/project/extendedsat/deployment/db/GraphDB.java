package com.project.extendedsat.deployment.db;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.impls.neo4j.Neo4jGraph;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLWriter;



public class GraphDB {

	private GraphDatabaseService graphDB;
	private String DeploymentFile = "diagram.xml";
	
	private static void registerShutdownHook( final GraphDatabaseService graphDb )
	{
	    // Registers a shutdown hook for the Neo4j instance so that it
	    // shuts down nicely when the VM exits (even if you "Ctrl-C" the
	    // running application).
	    Runtime.getRuntime().addShutdownHook( new Thread()
	    {
	        @Override
	        public void run()
	        {
	            graphDb.shutdown();
	        }
	    } );
	}
	
	//save xml data to graph database
	public void saveDiagramInfoAsGraph(){
		GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
		graphDB = dbFactory.newEmbeddedDatabase( "graphdb" );
		registerShutdownHook( graphDB );
		
		//graphDB = new GraphDatabaseFactory().newEmbeddedDatabase("graphdb");
		System.out.print("graphDB: ");
		System.out.println(graphDB);
		
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File(DeploymentFile);
		Document doc;
		
		
		try {
			doc = builder.build(xmlFile);
			
			//getting root element
			Element rootNode = doc.getRootElement();
			List list = rootNode.getChildren(); 
			
			Element element, childElement;
			Node previousNode = null;
			String id;
			
			graphDB.beginTx();
			
			for(int i= 0; i<list.size(); i++){
				element = (Element) list.get(i);
				id = element.getAttribute( "id" ).toString();
				Node node = graphDB.createNode();
				node.addLabel( Labels.NODE );
				
				
				//getiing a child element
				childElement = element.getChild( "software" );
				if( childElement != null ){
					Node software = graphDB.createNode();
					software.addLabel(Labels.SOFTWARE);
					software.setProperty("id", childElement.getAttribute("id").toString());
					software.setProperty("name", element.getChildText("software"));
					node.createRelationshipTo(software, RelationshipTypes.RUNNING);
				}
				
				
				childElement = element.getChild( "ip" );
				if( childElement != null ){
					Node ip = graphDB.createNode();
					ip.addLabel(Labels.SOFTWARE);
					ip.setProperty("id", childElement.getAttribute("id").toString());
					ip.setProperty("ip", element.getChildText("ip"));
					node.createRelationshipTo(ip, RelationshipTypes.HAS_IP);
				}
				
				
				childElement = element.getChild( "port" );
				if( childElement != null ){
					Node port = graphDB.createNode();
					port.addLabel(Labels.SOFTWARE);
					port.setProperty("id", childElement.getAttribute("id").toString());
					port.setProperty("port", element.getChildText("port"));
					node.createRelationshipTo(port, RelationshipTypes.OPEN_PORT);
				}
				
				
				if( previousNode != null ){
					previousNode.createRelationshipTo(node, RelationshipTypes.CONNECTED_TO);					
				}
				previousNode = node;
			}
			
			
			graphDB.shutdown();
			
			
		}
		catch( Exception e) {
			e.printStackTrace();
			System.out.println("Msg: " + e.getMessage());
		}
		finally{
			graphDB.shutdown();
		}
		
	}
	
	
	
	public void saveGraphToGmlFile(){
		Graph graph = new Neo4jGraph( graphDB );
		try {
			graphDB = new GraphDatabaseFactory().newEmbeddedDatabase( "graphdb" );
			registerShutdownHook( graphDB );
			
			graphDB.beginTx();
			GraphMLWriter.outputGraph(graph, "./graph.gml");
			
			graphDB.shutdown();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	public static void main(String args[]){
		GraphDB graphdb = new GraphDB();
		graphdb.saveDiagramInfoAsGraph();
		graphdb.saveGraphToGmlFile();
	}
	
	
	
	
	
	
	
	
	
	
}
