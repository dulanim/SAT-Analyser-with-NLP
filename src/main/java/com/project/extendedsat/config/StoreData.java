package com.project.extendedsat.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.management.AttributeList;

/*import org.neo4j.cypher.internal.ExecutionEngine;
import org.neo4j.cypher.internal.javacompat.ExecutionResult;*/
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
//import org.neo4j.kernel.impl.util.FileUtils;
import org.neo4j.driver.v1.*;
/**
*
*
* @author SAMITHA
*/
public class StoreData {
	

	public void createdb(ArrayList<AttributeList> list1) {

		ArrayList<AttributeList> l1 = new ArrayList<AttributeList>();

		StatementResult result = null;
		l1 = list1;

		// System.out.println(l1.size());
		Driver driver = GraphDatabase.driver("bolt://localhost", AuthTokens.basic("neo4j", "samitha"));
		Session session = driver.session();

		// session.run( "CREATE (property:PropertyNode {Visibility:'Private',
		// name:'"++"', type:'Field', value:'SATdoor', variableType:'String'})"
		// );
		for (int k = 0; k < l1.size(); k++) {
			/*
			 * session.run( "CREATE (b:Person {name:'"+str+"', title:'solla'})"
			 * );
			 */
			session.run("CREATE (" + l1.get(k).get(1).toString() + ":" + l1.get(k).get(1).toString() + " { name:'"
					+ l1.get(k).get(1).toString() + "', type:'" + l1.get(k).get(2).toString() + "', value:'"
					+ l1.get(k).get(3).toString() + "', variableType:'" + l1.get(k).get(4).toString() + "'})");
			// session.run( "MATCH
			// ("+l1.get(k).get(1).toString()+":PropertyNode"+l1.get(k).get(1).toString()+"),(cc:CreditCard)
			// CREATE (e)-[r:DO_SHOPPING_WITH ]->(cc) " );
			result = session.run("MATCH (" + l1.get(k).get(1).toString() + ":" + l1.get(k).get(1).toString()
					+ ") WHERE " + l1.get(k).get(1).toString() + ".name = '" + l1.get(k).get(1).toString() + "' RETURN "
					+ l1.get(k).get(1).toString() + ".name AS name");
			while (result.hasNext()) {
				Record record = result.next();
				System.out.println(record.get("name").asString());
			}
		}

		session.close();
		driver.close();

		/*
		 * GraphDatabaseFactory graphDbFactory = new GraphDatabaseFactory();
		 * 
		 * GraphDatabaseService graphDb =
		 * graphDbFactory.newEmbeddedDatabase("E:\\TPNeo4jDB");
		 * 
		 * ExecutionEngine execEngine = new ExecutionEngine(graphDb);
		 * ExecutionResult execResult =
		 * graphDb.execute("MATCH (java:JAVA) RETURN java"); String results =
		 * execResult.dumpToString(); System.out.println(results);
		 */

	}

	public void createRelation(ArrayList<HashMap<String, String>> relationlist) {
		Driver driver = GraphDatabase.driver("bolt://localhost", AuthTokens.basic("neo4j", "samitha"));
		Session session = driver.session();
		StatementResult result = null;
		for (int i = 0; i < relationlist.size(); i++) {
			HashMap<String, String> map = relationlist.get(i);

			Set set = map.entrySet(); // Get a set of the entries
			Iterator j = set.iterator(); // Get an iterator

			String sn = (String) ((Map.Entry) j.next()).getValue();
			String tn = (String) ((Map.Entry) j.next()).getValue();
			String rp = (String) ((Map.Entry) j.next()).getValue();

			System.out.println(sn + " " + tn + " " + rp);

			session.run("MATCH (" + sn + ":" + sn + "),(" + tn + ":" + tn + ") CREATE (" + sn + ")-[r:" + rp + "{name:"
					+ "'PropertyFileToConfigurations'" +", relationID:"+i+ "}" + " ]->(" + tn + ") ");

			result = session.run("MATCH (" + sn + ")-[r:" + rp + " ]->(" + tn + ") RETURN r.name AS name");

		}

		while (result.hasNext()) {
			Record record = result.next();
			System.out.println(record.toString());
		}
		session.close();
		driver.close();
	}

	public void deleteData() {
		Driver driver = GraphDatabase.driver("bolt://localhost", AuthTokens.basic("neo4j", "samitha"));
		Session session = driver.session();
		session.run("MATCH (n) OPTIONAL MATCH (n)-[r]-() DELETE n,r");// delete
																		// all
																		// node
																		// and
																		// relationships
		// MATCH (n) DETACH DELETE n
		
		session.close();	
		
		driver.close();
	}

	/*
	 * public void createDB(){ //File file = new File("E:\\TPNeo4jDB"); String
	 * file="E:\\TPNeo4jDB"; GraphDatabaseFactory dbFactory = new
	 * GraphDatabaseFactory(); GraphDatabaseService db=
	 * dbFactory.newEmbeddedDatabase(file); try (Transaction tx = db.beginTx())
	 * {
	 * 
	 * Node javaNode = db.createNode(Tutorials.JAVA);
	 * javaNode.setProperty("TutorialID", "JAVA001");
	 * javaNode.setProperty("Title", "Learn Java");
	 * javaNode.setProperty("NoOfChapters", "25");
	 * javaNode.setProperty("Status", "Completed");
	 * 
	 * Node scalaNode = db.createNode(Tutorials.SCALA);
	 * scalaNode.setProperty("TutorialID", "SCALA001");
	 * scalaNode.setProperty("Title", "Learn Scala");
	 * scalaNode.setProperty("NoOfChapters", "20");
	 * scalaNode.setProperty("Status", "Completed");
	 * 
	 * Relationship relationship = javaNode.createRelationshipTo
	 * (scalaNode,TutorialRelationships.JVM_LANGIAGES);
	 * relationship.setProperty("Id","1234");
	 * relationship.setProperty("OOPS","YES");
	 * relationship.setProperty("FP","YES");
	 * 
	 * tx.success(); } System.out.println("Done successfully"); }
	 */

	/*
	 * private static final String DB_PATH = "E:\\TPNeo4jDB";
	 * 
	 * public String greeting;
	 * 
	 * // START SNIPPET: vars GraphDatabaseService graphDb; Node firstNode; Node
	 * secondNode; Relationship relationship; // END SNIPPET: vars
	 * 
	 * // START SNIPPET: createReltype private static enum RelTypes implements
	 * RelationshipType { KNOWS } // END SNIPPET: createReltype
	 * 
	 * public static void main( final String[] args ) { StoreData hello = new
	 * StoreData(); hello.createDb(); hello.removeData(); hello.shutDown(); }
	 * 
	 * void createDb() { clearDb(); // START SNIPPET: startDb graphDb = new
	 * GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
	 * 
	 * registerShutdownHook( graphDb ); // END SNIPPET: startDb
	 * 
	 * // START SNIPPET: transaction Transaction tx = graphDb.beginTx(); try {
	 * // Updating operations go here // END SNIPPET: transaction // START
	 * SNIPPET: addData firstNode = graphDb.createNode(); firstNode.setProperty(
	 * "message", "Hello, " ); secondNode = graphDb.createNode();
	 * secondNode.setProperty( "message", "World!" );
	 * 
	 * relationship = firstNode.createRelationshipTo( secondNode, RelTypes.KNOWS
	 * ); relationship.setProperty( "message", "brave Neo4j " ); // END SNIPPET:
	 * addData
	 * 
	 * // START SNIPPET: readData System.out.print( firstNode.getProperty(
	 * "message" ) ); System.out.print( relationship.getProperty( "message" ) );
	 * System.out.print( secondNode.getProperty( "message" ) ); // END SNIPPET:
	 * readData
	 * 
	 * greeting = ( (String) firstNode.getProperty( "message" ) ) + ( (String)
	 * relationship.getProperty( "message" ) ) + ( (String)
	 * secondNode.getProperty( "message" ) );
	 * 
	 * // START SNIPPET: transaction tx.success(); } finally { tx.finish(); } //
	 * END SNIPPET: transaction }
	 * 
	 * private void clearDb() { try { FileUtils.deleteRecursively( new File(
	 * DB_PATH ) ); } catch ( IOException e ) { throw new RuntimeException( e );
	 * } }
	 * 
	 * void removeData() { Transaction tx = graphDb.beginTx(); try { // START
	 * SNIPPET: removingData // let's remove the data
	 * firstNode.getSingleRelationship( RelTypes.KNOWS, Direction.OUTGOING
	 * ).delete(); firstNode.delete(); secondNode.delete(); // END SNIPPET:
	 * removingData
	 * 
	 * tx.success(); } finally { tx.finish(); } }
	 * 
	 * void shutDown() { System.out.println(); System.out.println(
	 * "Shutting down database ..." ); // START SNIPPET: shutdownServer
	 * graphDb.shutdown(); // END SNIPPET: shutdownServer }
	 * 
	 * // START SNIPPET: shutdownHook private static void registerShutdownHook(
	 * final GraphDatabaseService graphDb ) { // Registers a shutdown hook for
	 * the Neo4j instance so that it // shuts down nicely when the VM exits
	 * (even if you "Ctrl-C" the // running application).
	 * Runtime.getRuntime().addShutdownHook( new Thread() {
	 * 
	 * @Override public void run() { graphDb.shutdown(); } } ); }
	 */

}
