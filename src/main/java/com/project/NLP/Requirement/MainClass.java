/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *..
 */
package com.project.NLP.Requirement;

import edu.stanford.nlp.trees.Tree;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MainClass {
    
    private static String requirementDocument="";
    private static HashMap requirementObjects = new HashMap();

    public static void main(String args[]) {
        System.setProperty("wordnet.database.dir", "/usr/local/WordNet-2.1/dict");

        HashSet classList = new HashSet();
        HashSet attrList = new HashSet();
        HashSet methodList =new HashSet();
        HashSet relationList = new HashSet();
        StoringArtefacts storingArtefacts;
        String className =null;
        
        ArrayList trees =new ArrayList();  
        
        
        /*Reading requirement file */
        requirementDocument=readFromTextFile("OrderRequirement.txt");
                
        if (requirementDocument=="") {
            System.out.println("Error : There is no input document !!!");
        }
        else{
            ParserTreeGenerator parser=new ParserTreeGenerator(requirementDocument);
            trees=parser.getSentenceParseTree();
            
            /*For individual sentence in the requirement Document */
            for(int i=0; i<trees.size();i++){
                Tree tree=(Tree) trees.get(i);
                 System.out.println(tree); 
               
                /*noun pharase identification */
                    ClassIdentification np = new ClassIdentification(tree);
                    classList = np.getClasses();
                    ArrayList attributesFromClass = np.getAttributeFromClass();
                    System.out.println("CLASS LIST:" + classList);
                    
                /*attributes identification */
                    AttributeIdentification attr = new AttributeIdentification(tree, attributesFromClass, classList);
                    attrList = attr.getAttributes();
                    System.out.println("ATTRIBUTE LIST: " + attrList); 
                    
                /* methods identification */
                    MethodIdentifier mId=new MethodIdentifier(tree, classList);
                    methodList=mId.identifyCandidateMethods(tree);
                
                /* relations identificaton */
                   // ClassRelationIdentifier crId=new ClassRelationIdentifier(classList, requirementObjects.keySet());
                    //if(i!=0){
                     //  relationList= crId.identifyGenaralizationByComparing(classList, requirementObjects.keySet());
                     //  relationList.addAll(crId.identifyGenaralizationByHypernym(classList,requirementObjects.keySet()));
                    //}
                    
                /*Storing Class details  */    
                    Iterator iterator = classList.iterator();
                    if(iterator.hasNext()){
                        className=(String) iterator.next();
                        }
                    if(requirementObjects.containsKey(className)){
                        StoringArtefacts storeArt=(StoringArtefacts) requirementObjects.get(className);
                        storeArt.addAttributes(attrList);
                        storeArt.addMethods(methodList);
                        storeArt.addRelationships(relationList);
                       
                    }
                    else{
                        /*calling storingArtefacts class store the results inorder to find the class- attri - metho -relation */
                        storingArtefacts = new StoringArtefacts();
                        storingArtefacts.addClassName(classList);
                        storingArtefacts.addAttributes(attrList);
                        storingArtefacts.addMethods(methodList);
                        storingArtefacts.addRelationships(relationList);
                        requirementObjects.put(className,storingArtefacts);
                    }
                    

                }
                 
                /*After finding all classes in the document identifying relationships betweenm them.
                *
                */
                HashSet relationSet=new HashSet();
                ClassRelationIdentifier crid=new ClassRelationIdentifier();
                relationSet=crid.identifyGeneralizationRelations(requirementObjects.keySet());
                addingRelationsToIdentifiedClasses(relationSet);
                
              
                /*Writing the information extracted from Requirement to text file  */
                 writeOutputToTxtFile(requirementObjects);
            }
             
    }
        
    /* Reading the input Natural Language Requirement File 
        *Input : text file
        *Output :String of the text file
    */
    private static String readFromTextFile(String file) {
            BufferedReader br = null;
            StringBuilder req_Document=new StringBuilder("");
            
            try {
                String sCurrentLine;
                br = new BufferedReader(new FileReader(file));
                while ((sCurrentLine = br.readLine()) != null) {
                    req_Document= req_Document.append(" "+sCurrentLine);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (br != null) {
                        br.close();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            
            return req_Document.toString();
        }
 /*
         
         /*Writing output HashMap to a text file 
         *
         */
        public  static void writeOutputToTxtFile(HashMap output){
        //write to file : "Requirement Output"
            try{
            
                StringBuffer sbf=new StringBuffer();
                Iterator it= output.keySet().iterator();
                while(it.hasNext()){
                
                    String className=it.next().toString();
                    StoringArtefacts store=(StoringArtefacts)output.get(className);
                    HashSet attributes= store.getAttributes();
                    HashSet methods=store.getMethods();
                    HashSet relations=store.getRelationships();
                
                    sbf.append("\n\nClass : "+className);
                    sbf.append("\n\tAttributes : ");
                    for (Object attribute : attributes) {
                        sbf.append(attribute.toString()+",");
                    }
                    sbf.append("\n\tMethods : ");
                    for (Object method : methods) {
                        sbf.append(method.toString()+",");
                    }
                    sbf.append("\n\tRelations : ");
                    for (Object relation : relations) {
                        ClassRelation rel=(ClassRelation)relation;
                        if(rel.getChildElement().equals(className)){
                            sbf.append("\t <Type> - "+rel.getRelationType()+"(Parent : "+rel.getParentElement()+")");
                        }
                        else if(rel.getParentElement().equals(className)){
                             sbf.append("\t <Type> - "+rel.getRelationType()+"(Child : "+rel.getChildElement()+")");
                        }
                    }
                    
                
                }
            
                System.out.println(sbf.toString());
            
                BufferedWriter bwr = new BufferedWriter(new FileWriter(new File("Requirement_Output.txt")));
               
                //write contents of StringBuffer to a file
                bwr.write(sbf.toString());
               
                //flush the stream
                bwr.flush();
               
                //close the stream
                bwr.close();
            
                }catch(Exception e){}
            }
        
        public static void addingRelationsToIdentifiedClasses(HashSet relations){
            Iterator iter=relations.iterator();
            while(iter.hasNext()){
                ClassRelation classRelation=(ClassRelation)iter.next();
                String parent=classRelation.getParentElement();
                String child=classRelation.getChildElement();
                StoringArtefacts storingArtefacts=(StoringArtefacts)requirementObjects.get(parent);
                storingArtefacts.addRelationships(classRelation);
                storingArtefacts=(StoringArtefacts)requirementObjects.get(child);
                storingArtefacts.addRelationships(classRelation);
            }
        }
    
        
}