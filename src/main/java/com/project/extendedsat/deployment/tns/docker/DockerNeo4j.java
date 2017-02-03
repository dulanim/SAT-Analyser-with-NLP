/**
 * Namesh Sanjitha
 * Jul 15, 2016
 * DockerTraceability
 * com.tns.docker
 */



package com.project.extendedsat.deployment.tns.docker;



import java.io.File;
//import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

//import org.neo4j.jdbc.Connection;
//import org.neo4j.jdbc.ResultSet;
//import org.neo4j.jdbc.Statement;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.Transaction;




public class DockerNeo4j {

	
	private GraphDatabaseService dbService;
	
	
	
	public DockerNeo4j(){
		
	}
	
	
	//update the Neo4j database graph according to the newly created dockerfileoutput.xml
	public void updateGraph() {	//throws SQLException
		
		try{
			//Connection conn = (Connection)DriverManager.getConnection( "jdbc:neo4j:bolt://localhost", "neo4j", "myneo4j" );
			
			//Statement st = (Statement)conn.createStatement();
			//String query = "CREATE (n:Person {name:\"Docker\"}) RETURN n";
			//int rs = st.executeUpdate(query);
		}
		catch( Exception e ){
			e.printStackTrace();
			System.out.println( e.getMessage() );
		}
		finally{
		}
	}
	
	
	
	//create connection to the graph database
	public void createConnection(){
		GraphDatabaseFactory dbFactory = new GraphDatabaseFactory();
		//File f = new File("./graphdb");
		dbService = dbFactory.newEmbeddedDatabase( "./graphdb" );
	
		//dbService = dbFactory.newEmbeddedDatabaseBuilder(f).newGraphDatabase();
	}
	
	
	
	//create graph from docker.xml
	public void createDockerGraph(){
		if( dbService == null )
			System.out.println( "dbService is null" );
		
		Transaction tx = dbService.beginTx();
		if( tx != null ){
			SAXBuilder builder = new SAXBuilder();
			File xmlFile = new File("docker.xml");
			
			try{
				Document doc = builder.build(xmlFile);
				Element rootNode = doc.getRootElement();
				List list = rootNode.getChildren(); 
				
				//Node dockerNodes[] = new Node[ list];
				//Node nodes[] = new Node[list.size()];
				for( int i = 0; i < list.size(); i++ ){
					
					
					List children = ((Element)(list.get(i))).getChildren();
					
					for( int j = 0; j < children.size(); i++ ){
						
						
						
					}
					
					
					String label = ((Element) list.get(i)).getChildText("base-container").toString();
					Node container = dbService.createNode( Neo4jLabels.CONTAINER );
					container.setProperty("base-container", label);
					
					Node port = dbService.createNode( Neo4jLabels.PORT );
					port.setProperty("open-port", ((Element) list.get(i)).getChildText("open-port").toString());
					
					Node software = dbService.createNode( Neo4jLabels.SOFTWARE );
					port.setProperty("software", ((Element) list.get(i)).getChildText("software").toString());
					
					Node file = dbService.createNode( Neo4jLabels.FILE );
					port.setProperty("file", ((Element) list.get(i)).getChildText("file").toString());
					
					
					
				}
			}
			catch( Exception e ){
				System.out.println( e.getMessage() );
				System.out.println("Error reading docker.xml");
			}
			
			
		}
	}
	
	
	
	
	
	
	public static void main( String args[] ){
		DockerNeo4j dn = new DockerNeo4j();
		//dn.updateGraph();
		dn.createConnection();
		dn.createDockerGraph();
	}
	
	
	
}
