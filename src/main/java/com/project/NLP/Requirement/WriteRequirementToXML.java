/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.Requirement;

import com.project.NLP.UMLToXML.xmlwriter.WriteToXML;
import com.project.traceability.model.Attribute;
import com.project.traceability.model.Dependencies;
import com.project.traceability.model.ModelData;
import com.project.traceability.model.Operation;
import com.project.traceability.model.Parameter;
import com.project.traceability.staticdata.StaticData;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author vino-pc
 */
public class WriteRequirementToXML {
    
    /*Writing requirementClasses HashMap to a text file 
    *@Param HashMap requirementClasses 
    */
    public  static void writeToXMLFile(HashMap requirementClasses,HashSet requirementClassRelations){
        try{
            
            List<ModelData> classInfoList =new ArrayList<>();
            List<Attribute> attributeList;
            List<Operation> methodList;
            List<Dependencies> dependencyList = new ArrayList<>();
            Iterator it= requirementClasses.keySet().iterator();
            while(it.hasNext()){
                ModelData data = new ModelData();
                attributeList =  new ArrayList<>();
                methodList = new ArrayList<>();
                //dependencyList = new ArrayList<>();
                
                String className=it.next().toString();
                String type = "Class";
                   
                data.setName(className);
                data.setType(type);
                data.setVisibility("");
                data.setIsStatic("");
                data.setId(className);
                StoringArtefacts store=(StoringArtefacts)requirementClasses.get(className);
                HashSet attributes= store.getAttributes();
                HashSet methods=store.getMethods();
                //HashSet relations=store.getRelationships();
                                    
                for (Object attribute : attributes) {
                    Attribute tempAttribute = new Attribute();
                    tempAttribute.setName(attribute.toString());
                    tempAttribute.setType("Field");
                    tempAttribute.setDataType("");
                    tempAttribute.setVisibility("");
                    tempAttribute.setIsStatic("");
                      
                    attributeList.add(tempAttribute);
                }
                for (Object method : methods) {
                    Operation operation = new Operation();
                    operation.setName(method.toString());
                    operation.setType("Method");
                    operation.setIsAbstract("");
                    operation.setVisibility("");
                    operation.setIsStatic("");
                    operation.setReturnType("");
                    operation.setParameterList(new ArrayList<Parameter>());
                    operation.setId("");
                        
                    methodList.add(operation);
                }
                /*
                for (Object relation : relations) {
                    Dependencies dependencies = new Dependencies();
                    ClassRelation rel=(ClassRelation)relation;
                       
                    dependencies.setDependency_type(rel.getRelationType());
                     System.out.println("XML Creating 1 ---- class : "+className);
                    System.out.println("XML Creating 2 ---- relation type : "+rel.getRelationType());
                                           
                    if(rel.getChildElement().equals(className)){
                        dependencies.setSource_id(className);
                        dependencies.setTaget_id(rel.getParentElement());
                        System.out.println("XML Creating 3 ---- source : "+className+" target : "+rel.getParentElement());
                    }
                    else if(rel.getParentElement().equals(className)){
                        dependencies.setSource_id(rel.getChildElement());
                        dependencies.setTaget_id(className);
                        System.out.println("XML Creating 4 ---- source : "+rel.getChildElement()+" target : "+className);
                    }
                        
                    dependencyList.add(dependencies);
                }
                */
                    
                data.setAttributeList(attributeList);
                data.setOperationList(methodList);
                   
                classInfoList.add(data);
                    
            }
            
            Iterator relationsIterator=requirementClassRelations.iterator();
            while (relationsIterator.hasNext()) {
                    Dependencies dependencies = new Dependencies();
                    ClassRelation rel=(ClassRelation)relationsIterator.next();
                       
                    dependencies.setDependency_type(rel.getRelationType());
                    dependencies.setSource_id(rel.getChildElement());
                    dependencies.setTaget_id(rel.getParentElement());
                        
                    dependencyList.add(dependencies);
                }
            StaticData.depencyList = dependencyList;
            System.out.println("WREquirementTXML : dependency list size : "+dependencyList.size());
            System.out.println("WREquirementTXML : dependency list size : "+StaticData.depencyList.size());
            StaticData.classLst = classInfoList;
                
            WriteToXML writer = new WriteToXML();
            WriteToXML.type = "Requirement";
            writer.createXML();
           
            
            /*For validating requirementClasses---------*/
            
            StringBuffer sbf=new StringBuffer();
                Iterator it1= requirementClasses.keySet().iterator();
                while(it1.hasNext()){
                
                    String className=it1.next().toString();
                    StoringArtefacts store=(StoringArtefacts)requirementClasses.get(className);
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
                }
                sbf.append("\nRelations : \n");
                    Iterator relationsIter=requirementClassRelations.iterator();
                    while (relationsIter.hasNext()) {
                        ClassRelation rel=(ClassRelation)relationsIter.next();
                        sbf.append("\n Type : "+rel.getRelationType()+", Parent : "+rel.getParentElement()+", Child : "+rel.getChildElement()+"\n");
                        
                    }
            BufferedWriter bwr = new BufferedWriter(new FileWriter("Requirement_Output.txt"));
            /*write contents of StringBuffer to a file*/
            bwr.write(sbf.toString());
            /*flush the stream*/
            bwr.flush();
            /*close the stream*/
            bwr.close();
            
                System.out.println(sbf.toString());
            
        }catch(Exception e){
            System.err.println("Error: Writing to XML : "+e.getMessage());
        }
    }
    
}
