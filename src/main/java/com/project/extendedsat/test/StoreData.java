package com.project.extendedsat.test;

import org.neo4j.driver.v1.*;

import javax.management.AttributeList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

//import org.neo4j.cypher.internal.ExecutionEngine;
//import org.neo4j.cypher.internal.javacompat.ExecutionResult;
//import org.neo4j.kernel.impl.util.FileUtils;


public class StoreData {


	public void createdb(ArrayList<AttributeList> list1){

		ArrayList<AttributeList> l1= new ArrayList<AttributeList>();

		StatementResult result=null;
		l1=list1;

		//System.out.println(l1.size());
		Driver driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "samitha" ) );
		Session session = driver.session();


		//session.run( "CREATE (property:PropertyNode {Visibility:'Private', name:'"++"', type:'Field', value:'SATdoor', variableType:'String'})" );
		for(int k=0;k<l1.size();k++){
	/*	
		session.run( "CREATE (b:Person {name:'"+str+"', title:'solla'})" );*/
			session.run( "CREATE ("+l1.get(k).get(1).toString()+":"+l1.get(k).get(1).toString()+" { name:'"+l1.get(k).get(0).toString()+"', type:'"+l1.get(k).get(2).toString()+"', value:'"+l1.get(k).get(1).toString()+"', variableType:'"+l1.get(k).get(2).toString()+"'})" );
			//session.run( "MATCH ("+l1.get(k).get(1).toString()+":PropertyNode"+l1.get(k).get(1).toString()+"),(cc:CreditCard) CREATE (e)-[r:DO_SHOPPING_WITH ]->(cc) " );
			result = session.run( "MATCH ("+l1.get(k).get(1).toString()+":"+l1.get(k).get(1).toString()+") WHERE "+l1.get(k).get(1).toString()+".name = '"+l1.get(k).get(1).toString()+"' RETURN "+l1.get(k).get(1).toString()+".name AS name" );
			while ( result.hasNext() )
			{
				Record record = result.next();
				System.out.println(  record.get("name").asString() );
			}
		}



		session.close();
		driver.close();
		
			
		/*GraphDatabaseFactory graphDbFactory = new GraphDatabaseFactory();

	      GraphDatabaseService graphDb = graphDbFactory.newEmbeddedDatabase("E:\\TPNeo4jDB");

	      ExecutionEngine execEngine = new ExecutionEngine(graphDb);
	      ExecutionResult execResult = graphDb.execute("MATCH (java:JAVA) RETURN java");
	      String results = execResult.dumpToString();
	      System.out.println(results);*/

	}

	public void createRelation(ArrayList<HashMap<String,String>> relationlist){
		Driver driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "samitha" ) );
		Session session = driver.session();
		StatementResult result =null;
		for(int i=0;i<relationlist.size();i++){
			HashMap<String,String> map = relationlist.get(i);

			Set set = map.entrySet(); // Get a set of the entries
			Iterator j = set.iterator(); // Get an iterator

			String sn=(String) ((Entry) j.next()).getValue();
			String tn=(String) ((Entry) j.next()).getValue();
			String rp=(String) ((Entry) j.next()).getValue();

			System.out.println(sn+" "+tn+" "+rp);

			session.run( "MATCH ("+sn+":"+sn+"),("+tn+":"+tn+") CREATE ("+sn+")-[r:"+rp+"{name:"+"'"+rp+"'"+", relationID:"+i+ "}"+" ]->("+tn+") " );

			result = session.run( "MATCH ("+sn+")-[r:"+rp+" ]->("+tn+") RETURN r.name AS name" );


		}

		while ( result.hasNext() )
		{
			Record record = result.next();
			System.out.println( record.toString());
		}
		session.close();
		driver.close();
	}

	public void deleteData(){
		Driver driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "samitha" ) );
		Session session = driver.session();
		session.run( "MATCH (n) OPTIONAL MATCH (n)-[r]-() DELETE n,r" );// delete all node and relationships
		//MATCH (n) DETACH DELETE n
		session.close();
		driver.close();
	}
	
	
	



}
