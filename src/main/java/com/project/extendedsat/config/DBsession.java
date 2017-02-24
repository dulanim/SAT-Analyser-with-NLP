package com.project.extendedsat.config;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;

/**
*
*
* @author SAMITHA
*/
public class DBsession {
	
	public static Session createsession(){
		
		Driver driver = GraphDatabase.driver( "bolt://localhost",AuthTokens.basic( "neo4j", "samitha" ) );
		  Session session = driver.session(); 
		  
		  return session;
	}

}
