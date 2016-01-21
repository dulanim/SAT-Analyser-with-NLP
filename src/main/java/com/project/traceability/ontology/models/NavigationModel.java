package com.project.traceability.ontology.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.iterator.ExtendedIterator;


public class NavigationModel {
	
	private static NavigationModel navigator;
	List<String> rootClasses;
	List<OntClass> ontClassLst;
	List<String> properties;
	List<String> values;
	OntModel model;
	ModelCreator generator;
	List<String> queue;
	Queue<OntClass> workingQueue;
	List<OntProperty> ontProps;
	private NavigationModel(){
		generator = ModelCreator.getModelInstance();
		generator.setPath("/home/shiyam/");
		model = generator.getCreatedModel();
		
		if(model == null){
			generator.initModel();
			model = generator.getCreatedModel();
		}
		
	}
	
	public static NavigationModel getNavigatorInstane(){
		
		if(navigator == null){
			navigator = new NavigationModel();
		}
		return navigator;
	}
	public Map<String,List<String>> getAllClassesProperty(){
		model.read(generator.getInputStream(),null);
		Iterator<OntClass> classList = model.listClasses();
		//rootClasses = new ArrayList<>();
		Map<String,List<String>> map = new HashMap<>();
		ontClassLst = new ArrayList<>();
		while(classList.hasNext()){
			OntClass tempClass = classList.next();
			String localName = tempClass.getLocalName();
			ontClassLst.add(tempClass);
			//rootClasses.add(localName);
			System.out.println("Parent CLass:>>>" + localName);
		}
		
		
		List<String> allProperties = this.getAllProperties();
		allProperties.remove("<<*************************>>");
		allProperties.remove("Add Custom Value");
		
		for(int i=0;i<ontClassLst.size();i++){
			OntClass tempClass = ontClassLst.get(i);
			map = getProperty(tempClass, map,allProperties);
		}
		return map;
		//return this.rootClasses;
	}
	
	public List<String> getSubClassFor(String className){
		model.read(generator.getInputStream(),null);
		queue = new ArrayList<>();
		workingQueue = new LinkedList<>();
		if(!className.equals("")){
			String uri = StaticData.OWL_ROOT_URI + "#" + className;
			OntClass ontClass =  model.getOntClass(uri);
			if(ontClass != null){
				Iterator<OntClass> classItr = ontClass.listSubClasses();
				while(classItr.hasNext()){
					OntClass tempClass = classItr.next();
					//getProperty(tempClass);
					queue.add(tempClass.getLocalName());
					 if(tempClass.hasSubClass()) {
						 	workingQueue.add(tempClass);
							System.out.println("Subclass For Noun:>>" + tempClass.getLocalName());
		             }
				}
				//getSubClass();
			}else{
				throw new NullPointerException("should be provided specific class name\n"
						+ "not null");
			}
		
		}else{
			throw new IllegalArgumentException("should be provided specific class name\n"
					+ "not null");
		}
		System.out.println(queue.size());
		return queue;
		
	}
	public List<String> getAllProperties(){
		model.read(generator.getInputStream(),null);
		properties = new ArrayList<>();
		ExtendedIterator<OntProperty> exItr;   
					//fetching all properties from model.owl file
		exItr = model.listOntProperties();     
		while (exItr.hasNext()) {
				OntProperty prop = exItr.next();
				System.out.println("Object Property Name: "+ prop.getLocalName());
				properties.add(prop.getLocalName());
		}
		
		this.properties.add("<<*************************>>");
		properties.add("Add Custom Value");
		return this.properties;
	}
	public List<String> getAllValues1(String propsName){
		//get all individual
		Set<String> values = new HashSet<>();
		if(!propsName.equals("")){
				String propURI = StaticData.OWL_ROOT_URI + "#";
				String uriprop = propURI + StaticData.OWL_PROPERTY + propsName;
				OntProperty property = model.getOntProperty(uriprop);
				if(property != null){
					
					
					/*
					 * get specific property from model
					 */
					Iterator<OntClass> rdfPrimer = model.listClasses();
				    


				    System.out.println( "\n== The Properties ==" );
				    
				    while(rdfPrimer.hasNext() ) {
				      OntClass cls = rdfPrimer.next();
				      
				      StmtIterator it = cls.listProperties(property);
				      
				      while(it != null  && it.hasNext()){
					      Statement stmt = it.nextStatement();
					      System.out.println( "Values ***** * "+stmt.getObject().toString() );
					      String value = stmt.getObject().toString();
					      values.add(value);
				      }
				    }
				}
					
		}
		
		List<String> listValues = new ArrayList<>(values);
		listValues.add("<<*************************>>");
		listValues.add("Add Custom Value");
		return listValues;
	}
	
	private String getValueFor(OntClass className,String propertyName){
		/*
		 * this method return value for specific class and specific property name
		 */
		String result = "";
		
		  model.read(generator.getInputStream(),null);
		  String propURI = StaticData.OWL_ROOT_URI + "#" + propertyName;//building property uri
		  OntProperty property = model.getOntProperty(propURI);//get OntProperty from model.wl
	      
	      StmtIterator it = className.listProperties(property);
	      while(it != null &&it.hasNext()){
	    	  Statement stmt = it.nextStatement();
		      result = stmt.getObject().toString();
	      }
	      
	      return result;
	}
	private Map<String,List<String>> getProperty(OntClass classSpec,
			Map<String,List<String>> map,
			List<String> allProperties){
		
		/*
		 *  get the specific property for classSpec
		 *  @param classSpec - which has class informaion
		 */
		
		List<String> list = new ArrayList<>();
		model.read(generator.getInputStream(),null);
		String propURI = StaticData.OWL_ROOT_URI + "#";
		
		OntClass cls = classSpec;
		for(int i=0;i<allProperties.size();i++){
					propURI += allProperties.get(i);
					OntProperty ontProperty = model.getOntProperty(propURI);
					
					StmtIterator it = cls.listProperties(ontProperty);
					while(it != null && it.hasNext()){
						
						String propertyName =allProperties.get(i);
						String propertyValue = getValueFor(cls, propertyName);
						it.nextStatement();
						list.add(propertyName+":" + propertyValue);
						System.out.println("PROPERTY FOR " + classSpec.getLocalName()  + "   " + 
								allProperties.get(i));
					}
					propURI = StaticData.OWL_ROOT_URI + "#";
			}
		
		
		map.put(classSpec.getLocalName(), list);
		return map;
	}
	
//	private void getSubClass(){
//		
//		
//		for(int i=0;!workingQueue.isEmpty();i++){
//			OntClass clas = workingQueue.poll();
//			if(clas.hasSubClass()){
//				Iterator<OntClass>  itr = clas.listSubClasses();
//				while(itr.hasNext()){
//					OntClass temp = itr.next();
//					workingQueue.add(temp);
//					queue.add(temp.getLocalName());
//					System.out.println("Subclass For Noun:>>" + 
//								temp.getLocalName());
//				}
//			}
//			
//		}
//		
//	}
	
	public List<OntProperty> getSpecPropertyFor(Individual ind){
		List<OntProperty> props = new ArrayList<>();
		
		List<String> allPropsNames = this.getAllProperties();
		for(int i=0;i<allPropsNames.size();i++){
			
			OntProperty prop = model.getOntProperty(StaticData.OWL_ROOT_URI +  "#" +
			    												StaticData.OWL_PROPERTY + allPropsNames.get(i));
			if(ind.hasProperty(prop)){
				props.add(prop);
			}
		}
		return props;
	}
}