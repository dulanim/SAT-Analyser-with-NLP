package com.project.traceability.ontology.models;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
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

    private static ModelCreator creator = null;
    private boolean isCreated;
    private OntModel model;
    private String filename = "model.owl";
    private String filePath;
    
    public static OntClass parentClass;

    private ModelCreator() {
    }

    public static ModelCreator getModelInstance() {

        if (creator == null) {
            creator = new ModelCreator();
        }
        creator.initModel();
        return creator;
    }

    public  boolean initModel() {
        /*
		 * this method is for initiating the .owl file that 
		 * which is internally created by system when ontology model requires once
         */
    	setPath("");
    	File f = new File(filePath + File.separator +  filename);
    	if(f.exists())
    		return true;
        PrintWriter writer;
        isCreated = false;
        try {
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
            OntClass word = model.createClass(rootURI + "#" + StaticData.OWL_WORD_ROOT);
            OntClass noun = model.createClass(rootURI + "#" + StaticData.OWL_NOUN);
            OntClass attribute = model.createClass(rootURI + "#" + StaticData.OWL_ATTRIBUTE);
            OntClass classes = model.createClass(rootURI + "#" + StaticData.OWL_CLASS);
            OntClass othernoun = model.createClass(rootURI + "#" + StaticData.OWL_OTHER_NOUN);

            OntProperty hasNoun = model.createOntProperty(rootURI + "#hasNoun");
            OntProperty hasAttribute = model.createOntProperty(rootURI + "#hasAttribute");
            OntProperty hasClass = model.createOntProperty(rootURI + "#hasClass");
            OntProperty hasOtherNoun = model.createOntProperty(rootURI + "#hasOtherNoun");
            word.addSubClass(noun);
            noun.addSubClass(classes);
            noun.addSubClass(othernoun);
            noun.addSubClass(attribute);

            word.addProperty(hasNoun, noun);
            noun.addProperty(hasClass, classes);
            noun.addProperty(hasOtherNoun, othernoun);
            noun.addProperty(hasAttribute, attribute);
            /*
				 * verbs 
             */
            OntClass verb = model.createClass(rootURI + "#" + StaticData.OWL_VERB);
            OntClass method = model.createClass(rootURI + "#" + StaticData.OWL_METHOD);

            word.addSubClass(verb);
            verb.addSubClass(method);

            OntProperty hasVerb = model.createOntProperty(rootURI + "#hasMethod");
            verb.addProperty(hasVerb, method);

            model.write(writer, StaticData.OWL_FILE_FORMAT);
            isCreated = true;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isCreated;
    }

    public String getFileName() {
        return this.filename;
    }

    public void setPath(String projectPath) {
        
        projectPath = System.getProperty("user.dir")+File.separator+"res";
        this.filePath = projectPath;
    }

    public String getFilePath() {
        return filePath;
    }

    public OntModel getCreatedModel() {

        if (this.model == null) {
            // Create an empty model
            final Model base = ModelFactory.createDefaultModel();
            model = ModelFactory.createOntologyModel(OntModelSpec.RDFS_MEM_RDFS_INF, base);
            initModel();
        }
        return model;
    }

    public boolean createNewNode(String word1, String word2, Word word) {
    	  boolean isCreated = false;

    	  word1 = getNormalizedWordString(word1);
    	  word2 = getNormalizedWordString(word2);
          String wordURI1 = StaticData.OWL_ROOT_URI + "#" + word1;
          String wordURI2 = StaticData.OWL_ROOT_URI + "#" + word2;

          /*
  		 * get the word's root/parent
  		 * get the word's property name
  		 * get the word's literal / value
           */
          
          List<String> aParent = word.getParentName();
          List<String> aLiteral = word.getValue();
          List<String> aProperty = word.getPropertyName();

          for(String parent:aParent){
	          OntModel model = getCreatedModel();
	          OntClass class1 = null;// =	model.createClass(wordURI1);
	          OntClass class2 = null;// = 	model.createClass(wordURI2);
	          //get the root position
	          String wordURI = StaticData.OWL_ROOT_URI + "#" + parent;
	          OntClass rootClass = this.model.getOntClass(wordURI);
	          if (rootClass == null && parent != null && !parent.isEmpty()) {
	              /*
	  			 * this block works when parent class other than existing model
	  			 * Animal is not there in model 
	  			 * so we get this as  Class 
	  			 * so add Animal under Class
	  			 * now animal is top or parent class
	               */
	              String type = word.getType();
	              String uri = StaticData.OWL_ROOT_URI + "#" + parent;
	              wordURI = StaticData.OWL_ROOT_URI + "#" + type;
	              rootClass = model.getOntClass(wordURI);
	              OntClass newClass = model.createClass(uri);
	              rootClass.addSubClass(newClass);
	              rootClass = newClass;
	          }else if(parent == "" || parent == null){
	        	  String type = word.getType();
	              wordURI = StaticData.OWL_ROOT_URI + "#" + type;
	              rootClass = model.getOntClass(wordURI);
	          }
	          // Read the RDF/XML file
	          model.read(getInputStream(), null);
	          boolean isClass1Exists = isExistis(wordURI1);
	          boolean isClass2Exists = isExistis(wordURI2);
	
	          if (isClass2Exists || isClass1Exists) {
	              //if class 1 is not there in model
	              if (isClass2Exists) {
	                  class1 = model.createClass(wordURI1);
	                  class2 = model.getOntClass(wordURI2);
	                  rootClass.addSubClass(class1);
	                  rootClass.addSubClass(class2);
	                  for (int i = 0; i < aProperty.size(); i++) {
	                      OntProperty relation = model.createOntProperty(StaticData.OWL_ROOT_URI + "#"
	                              + StaticData.OWL_PROPERTY + aProperty.get(i));
	                      class1.addProperty(relation, aLiteral.get(i));
	                      class2.addProperty(relation, aLiteral.get(i));
	                  }
	              }
	              //if class 2 is not there in model
	              if (isClass1Exists) {
	                  class2 = model.createClass(wordURI2);
	                  class1 = model.createClass(wordURI1);
	                  rootClass.addSubClass(class2);
	                  rootClass.addSubClass(class1);
	                  for (int i = 0; i < aProperty.size(); i++) {
	                      OntProperty relation = model.createOntProperty(StaticData.OWL_ROOT_URI + "#"
	                              + StaticData.OWL_PROPERTY + aProperty.get(i));
	
	                      class1.addProperty(relation, aLiteral.get(i));
	                      class2.addProperty(relation, aLiteral.get(i));
	                  }
	              }
	          }
	
	          // if two class is not in model already add those two node
	          if (!(isClass1Exists && isClass2Exists) && (!parent.equals(null)
	                  || !parent.isEmpty() || !parent.equals(""))) {
	              class1 = model.createClass(wordURI1);
	              class2 = model.createClass(wordURI2);
	              
	              if(rootClass != null){
		              rootClass.addSubClass(class1);
		              rootClass.addSubClass(class2);
	              }
	              for (int i = 0; i < aProperty.size(); i++) {
	                  OntProperty relation = model.createOntProperty(StaticData.OWL_ROOT_URI + "#"
	                          + StaticData.OWL_PROPERTY + aProperty.get(i));
	                  class1.addProperty(relation, aLiteral.get(i));
	                  class2.addProperty(relation, aLiteral.get(i));
	              }
	          }
	          
	          if(isClass1Exists && isClass2Exists){
	        	  // both class exist is model add parent classes to existing model
	        	  class1 = model.getOntClass(wordURI1);
	        	  class2 = model.getOntClass(wordURI2);
	        	  rootClass.addSubClass(class1);
	              rootClass.addSubClass(class2);
	          }
	          try {
	              //update or write to owl file 
	              model.write(getOWLWriter(), StaticData.OWL_FILE_FORMAT);
	              isCreated = true;
	          } catch (FileNotFoundException e) {
	              e.printStackTrace();
	          } catch (IOException e) {
	              e.printStackTrace();
	          } catch (Exception e) {
	              e.printStackTrace();
	          }
         }
          StaticData.isAdded = true;
         return isCreated;
    }

    public boolean isMatchingWords(String word1, String word2) {
        boolean isMatched = false;
        int weight = 0;
        double score = 0;
        double total = 1;
        List<String> parent1 = StaticData.parent1;
        List<String> parent2 = StaticData.parent2;
        Map<String, List<String>> map = StaticData.map;
        if (!(word1.equals("") && word2.equals(""))) {
        	
        	word1 = getNormalizedWordString(word1);
        	word2 = getNormalizedWordString(word2);
            String uri1 = StaticData.OWL_ROOT_URI + "#" + word1;
            String uri2 = StaticData.OWL_ROOT_URI + "#" + word2;
            
            OntModel model = this.getCreatedModel();
            NavigationModel navigationModel = NavigationModel.getNavigatorInstane();
            if(StaticData.isStartedJustNow){	
                InputStream in = this.getInputStream();
            	model.read(in, null);///read the model.owl file from hard disk
            	this.model = model;


	            map = navigationModel.getAllClassesProperty();
	            StaticData.map = map;
	            
            }
            
           
            OntClass wordlass1 = model.getOntClass(uri1);
            OntClass wordlass2 = model.getOntClass(uri2);
            
            if (wordlass1 == null || wordlass2 == null) {
        		//if class is not in model they should not match with each other
            	return false;
            }
            //gather parent classes for separate word1 and word classes
            
            if(!StaticData.isStartedJustNow){
	            parent1 = navigationModel.getParentClassFor(wordlass1);
	            parent2 = navigationModel.getParentClassFor(wordlass2);
            	
            	map = StaticData.map;
            	
            }
            
            List<String> propertyList1 = map.get(word1);//hasName:shiyam hasSize:128cm
            List<String> propertyList2 = map.get(word2);
            int size1 = propertyList1.size();
            int size2 = propertyList2.size();

            if (size1 > size2) {
                /*
				 * list1 greater than list 2 check all property and value pair with
				 * list 1
                 */
                for (int i = 0; i < size2; i++) {
                    String propValuePair = propertyList2.get(i);
                    if (propertyList1.contains(propValuePair)) {
                        weight += 1;
                    }
                }
                total = size1;
                score = (weight / total);
            } else {
                /*
				 * list2 greater than list 1 check all property and value pair with
				 * list 2
                 */
                for (int i = 0; i < size1; i++) {
                    String propValuePair = propertyList1.get(i);
                    if (propertyList2.contains(propValuePair)) {
                        weight += 1;
                    }
                }
                total = size2;
                score = (weight / total);
            }

            if(parent1.size()>=parent2.size()){
            	//parent1's size more check all parent in parent2 is in parent1
            	//increase the weight by 1
            		for(String parent:parent2){
            			if(parent1.contains(parent)){
            				weight += 1;
            			}
            		}
            		 double newTotal = total + parent1.size();
                     score = weight / (newTotal);
                
            }else{
            	//parent2's size more check all parent in parent1 is in parent2
            	//increase the weight by 1
            	for(String parent:parent1){
        			if(parent2.contains(parent)){
        				weight += 1;
        			}
        		}
        		 double newTotal = total + parent2.size();
                 score = weight / (newTotal);
            }
            
        }

        if (score >= 0.7 && score <= 1.0) {
            isMatched = true;
        } else {
            isMatched = false;
        }
        return isMatched;
    }

    private PrintWriter getOWLWriter() throws FileNotFoundException, IOException, Exception {

        
        filePath = getFilePath();
        FileOutputStream fos = new FileOutputStream(this.filePath + File.separator
                + filename, false);
        PrintWriter writer = new PrintWriter(fos);
        return writer;
    }

    private boolean isExistis(String uriClass) {
        boolean isExists = false;
        OntModel model = this.getCreatedModel();
        Iterator<OntClass> classList = model.listClasses();
        while (classList.hasNext()) {
            OntClass tempClass = (OntClass) classList.next();
            String tempClassURI = tempClass.getURI();

            if (uriClass.equals(tempClassURI)) {
                parentClass = tempClass;
                isExists = true;
                return isExists;
            }

        }
        return isExists;
    }

    public InputStream getInputStream() {
        // Use the FileManager to find the input file 
        InputStream in = FileManager.get().open(filePath + File.separator + filename);
        if (in == null) {
            throw new IllegalArgumentException("File: " + filename + " not found");
        }
        return in;

    }
    
    public String getNormalizedWordString(String word){
    	//all name in model should be in lower case it is our convention for avoiding conflict
//    	String norm = "";
//    	if(word.length() ==1){
//	    	char chfirst = word.charAt(0);
//	    	String str = new String(new char[]{chfirst});
//	    	str.toUpperCase();
//	    	norm = str;
//    	}else if(word.length()>1){
//    		char chfirst = word.charAt(0);
//	    	String str = new String(new char[]{chfirst});
//	    	str.toLower
//	    	
//	    	String sub = word.substring(1).toLowerCase();
//	    	str += sub;
//	    	norm =str;
//    	}else{
//    		norm = "";
//    	}
    	return word.toLowerCase();
    }

}
