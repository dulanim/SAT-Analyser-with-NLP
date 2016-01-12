package com.project.traceability.ontology.models;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;


public class ModelCreator {

	private  static ModelCreator creator = null;
	private  boolean isCreated;
	private  OntModel model;
	private  String filename =  "model.owl";
	private  String filePath;
	
	public static OntClass parentClass;
	private ModelCreator(){
		
	}
	
	
	public static ModelCreator getModelInstance(){
		
		if(creator == null){
			creator = new ModelCreator();
		}
		return creator;
	}
	public  boolean initModel(){
		/*
		 * this method is for initiating the .owl file that 
		 * which is internally created by system when ontology model requires once
		 */
		PrintWriter writer;
		isCreated = false;
		try{
				String rootURI = StaticData.OWL_ROOT_URI;
				writer = getOWLWriter();
				model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM);
				
				
				/*
				 * create resource Word as Root
				 */
				
				//create classes
				/*
				 * Noun 
				 */
				OntClass word = model.createClass(rootURI+"#" + StaticData.OWL_WORD_ROOT);
				OntClass noun = model.createClass(rootURI+"#" + StaticData.OWL_NOUN);
				OntClass attribute = model.createClass(rootURI+"#" + StaticData.OWL_ATTRIBUTE);
				OntClass classes = model.createClass(rootURI+"#" + StaticData.OWL_CLASS);
				OntClass othernoun  = model.createClass(rootURI+"#" + StaticData.OWL_OTHER_NOUN);
				
				
				OntProperty hasNoun = model.createOntProperty(rootURI + "#hasNoun");
				OntProperty hasAttribute =model.createOntProperty(rootURI + "#hasAttribute");
				OntProperty hasClass= model.createOntProperty(rootURI + "#hasClass");
				OntProperty hasOtherNoun = model.createOntProperty(rootURI + "#hasOtherNoun");
				word.addSubClass(noun);
				noun.addSubClass(classes);
				noun.addSubClass(othernoun);
				noun.addSubClass(attribute);
				
				word.addProperty(hasNoun, noun);
				noun.addProperty(hasClass, classes);
				noun.addProperty(hasOtherNoun,othernoun);
				noun.addProperty(hasAttribute,attribute);
				/*
				 * verbs 
				 */
				OntClass verb = model.createClass(rootURI+"#" + StaticData.OWL_VERB);
				OntClass method = model.createClass(rootURI+"#" + StaticData.OWL_METHOD);
				
				word.addSubClass(verb);
				verb.addSubClass(method);
				
				OntProperty hasVerb = model.createOntProperty(rootURI + "#hasMethod");
				verb.addProperty(hasVerb, method);
				
				model.write(writer,StaticData.OWL_FILE_FORMAT);
				isCreated = true;
		}catch(IOException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		return isCreated;
	}
	
	public String getFileName(){
		return this.filename;
	}
	
	public void setPath(String projectPath){
		this.filePath = projectPath;
	}
	public String getFilePath(){
		return filePath;
	}
	
	public OntModel getCreatedModel(){
		
		if(this.model == null) {
			// Create an empty model
			final Model base = ModelFactory.createDefaultModel();
			model = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM_RDFS_INF, base);
		}
		return model;
	}
	
	@SuppressWarnings("finally")
	public boolean createNewNode(String word1,String word2,Word word){
		boolean isCreated = false;
		
		String wordURI1 = StaticData.OWL_ROOT_URI + "#" + word1;
		String wordURI2 = StaticData.OWL_ROOT_URI + "#" + word2;
	    
		/*
		 * get the word's root/parent
		 * get the word's property name
		 * get the word's literal / value
		 */
		String aParent = word.getParentName();
		List<String> aLiteral = word.getValue();
		List<String> aProperty = word.getPropertyName();
		
		
		OntModel model = 	this.getCreatedModel();
	    OntClass class1 = null;// =	model.createClass(wordURI1);
	    OntClass class2 = null;// = 	model.createClass(wordURI2);
	    //get the root position
		String wordURI = StaticData.OWL_ROOT_URI + "#"  + aParent;
		OntClass rootClass = this.model.getOntClass(wordURI);
		if(rootClass == null){
			/*
			 * this block works when parent class other than existing model
			 * Animal is not there in model 
			 * so we get this as  Class 
			 * so add Animal under Class
			 * now animal is top or parent class
			 */
			String type  = word.getType();
			String uri = StaticData.OWL_ROOT_URI + "#"  + aParent;
			wordURI = StaticData.OWL_ROOT_URI + "#"  + type;
			rootClass = this.model.getOntClass(wordURI);
			OntClass newClass = this.model.createClass(uri);
			rootClass.addSubClass(newClass);
			rootClass = newClass;
		}
		// Read the RDF/XML file
		model.read(getInputStream(), null);
		boolean isClass1Exists = isExistis(wordURI1);
		boolean isClass2Exists = isExistis(wordURI2);
		
		
		if(isClass2Exists || isClass1Exists){
			//if class 1 is not there in model
			if(isClass2Exists){
		    	class1 	  =	model.createClass(wordURI1);
		    	rootClass.addSubClass(class1);
		    	for(int i=0;i<aProperty.size();i++){
			    	OntProperty relation = model.createOntProperty(StaticData.OWL_ROOT_URI +  "#" +
			    												StaticData.OWL_PROPERTY + aProperty.get(i));
					class1.addProperty(relation,aLiteral.get(i));
		    	}
			}
			//if class 2 is not there in model
			if(isClass1Exists){
		    	class2 = model.createClass(wordURI2);
		    	rootClass.addSubClass(class2);
		    	for(int i=0;i<aProperty.size();i++){
		    		OntProperty relation = model.createOntProperty(StaticData.OWL_ROOT_URI +  "#" +
			    												StaticData.OWL_PROPERTY + aProperty.get(i));
					
		    		class2.addProperty(relation,aLiteral.get(i));
		    	}
			}
		}
		 
		
		// if two class is not in model already add those two node
		if(!(isClass1Exists && isClass2Exists) && (!aParent.equals(null) 
				|| !aParent.isEmpty() ||!aParent.equals(""))){
			class1 = model.createClass(wordURI1);
			class2 = model.createClass(wordURI2);
			rootClass.addSubClass(class1);
			rootClass.addSubClass(class2);
			for(int i=0;i<aProperty.size();i++){
				OntProperty relation = model.createOntProperty(StaticData.OWL_ROOT_URI +  "#" +
		    												StaticData.OWL_PROPERTY + aProperty.get(i));
		    	class1.addProperty(relation,aLiteral.get(i));
				class2.addProperty(relation,aLiteral.get(i));
	    	}
		}
		try {
			//update or write to owl file 
			model.write(getOWLWriter(),StaticData.OWL_FILE_FORMAT);
			isCreated = true;
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//send notification back to caller to success or not 
			return isCreated;
		}
	}
	
	
	public boolean isMatchingWords(String word1,String word2){
		boolean isMatched = false;
		int weight = 0;
		double score = 0;
		double total = 1;
		if(!(word1.equals("") && word2.equals(""))){
			String uri1 = StaticData.OWL_ROOT_URI + "#" + word1;
			String uri2 = StaticData.OWL_ROOT_URI + "#" + word2;
			
			OntModel model = this.getCreatedModel();
			InputStream in = this.getInputStream();
			model.read(in,null);
			OntClass parent1 = model.getOntClass(uri1);
			OntClass parent2 = model.getOntClass(uri2);
			
			NavigationModel navigator = NavigationModel.getNavigatorInstane();
			Map<String,List<String>> map = navigator.getAllClassesProperty();
			
			List<String> propertyList1 = map.get(word1);//hasName:shiyam hasSize:128cm
			List<String> propertyList2 = map.get(word2);
			
			int size1 = propertyList1.size();
			int size2 = propertyList2.size();
			
			if(size1>size2){
				/*
				 * list1 greater than list 2 check all property and value pair with
				 * list 1
				 */
				for(int i=0;i<size2;i++){
					String propValuePair = propertyList2.get(i);
					if(propertyList1.contains(propValuePair)){
						weight += 1;
					}
				}
				total = size1;
				score = (weight/total);
			}else{
				/*
				 * list2 greater than list 1 check all property and value pair with
				 * list 2
				 */
				for(int i=0;i<size1;i++){
					String propValuePair = propertyList1.get(i);
					if(propertyList2.contains(propValuePair)){
						weight += 1;
					}
				}
				total = size2;
				score = (weight/total);
			}
			
			if(parent1.getLocalName().equalsIgnoreCase(parent2.getLocalName())){
				double newTotal = size1+size2 + 1;
				score += (weight+1)/(newTotal);
			}
		}
			
		if(score>=0.7 && score<=1.0){
			isMatched = true;
		}else{
			isMatched = false;
		}
			return isMatched;
	}
	private PrintWriter getOWLWriter() throws FileNotFoundException,IOException,Exception {
		
		FileOutputStream fos=  new FileOutputStream(this.filePath + 
												filename,false);
		PrintWriter writer = new PrintWriter(fos);
		return writer;
	}
	
	private boolean isExistis(String uriClass){
		boolean isExists = false;
		OntModel model = this.getCreatedModel();
		Iterator<OntClass> classList = model.listClasses();
		while(classList.hasNext()){
			OntClass tempClass = (OntClass) classList.next();
		    String tempClassURI = tempClass.getURI();
		    
		    if(uriClass.equals(tempClassURI)){
		    	parentClass = tempClass;
		    	return isExists;
		    }
		    
		}
		return isExists;
	}
	
	public InputStream getInputStream(){
		// Use the FileManager to find the input file
				InputStream in = FileManager.get().open(filePath  + filename);

				if (in == null)
					throw new IllegalArgumentException("File: "+filename+" not found");
			return in;
	
	}
	
	
}
