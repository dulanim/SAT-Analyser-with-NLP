package com.project.extendedsat.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.management.AttributeList;

import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.openide.util.Lookup;


/**
*This is for create traceability links for configuration phase 
*
* @author SAMITHA
*/

public class MainClass {

	public String PropertyFile = "config.properties";
	public 	String ConfigurationText = "configurations.txt";
	
	
	public MainClass(String propertyFile, String configurationText) {
		super();
		PropertyFile = propertyFile;
		ConfigurationText = configurationText;
	}


	public void startConfig() {
		WriteToXML propertyxml = new WriteToXML("ConfigurationArtefactFile.xml");
		ReadPropertyFile pf = new ReadPropertyFile(PropertyFile);
		HashMap<String, String> map = pf.load();
		propertyxml.convertToXML(map);

		WriteToXML configurationText = new WriteToXML("ConfigurationFile.xml");
	
		ReadPropertyFile conf = new ReadPropertyFile(ConfigurationText);
		HashMap<String, String> map2 = conf.load();
		configurationText.convertToXML(map2);

		XmlToArray x = new XmlToArray();
		ArrayList<AttributeList> list1 = x.parseXML("ConfigurationArtefactFile.xml");
		ArrayList<AttributeList> list2 = x.parseXML("ConfigurationFile.xml");
		Map<String, ArrayList<AttributeList>> mapList = CreateTraceability.compareList(list1, list2);

		CreateRelationFile relation = new CreateRelationFile("Relations.xml");
		relation.createRelations(mapList);


		RelationFileToArray r = new RelationFileToArray();
		ArrayList<HashMap<String, String>> relationlist = r.toArray("Relations.xml");

		StoreData st = new StoreData();
		st.deleteData();
		st.createdb(list1);
		st.createdb(list2);
		st.createRelation(relationlist);

		
		  Driver driver = GraphDatabase.driver( "bolt://localhost",AuthTokens.basic( "neo4j", "samitha" ) );
		  Session session = driver.session(); 
		  GraphFileGenerator g= new GraphFileGenerator();
		  //g.generateGraphFile(session);
		  
		  GenerateGexf gexf = new GenerateGexf();
		  gexf.generateGraphFile(session);
		  
		  
//		  TestGraph previewJFrame = new TestGraph();
//		  previewJFrame.script();	
		  

	}
}
