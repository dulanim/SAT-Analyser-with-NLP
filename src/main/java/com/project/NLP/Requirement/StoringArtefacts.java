/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.Requirement;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author S. Shobiga
 */
public class StoringArtefacts {
    private HashSet className=new HashSet();
    private HashSet attributes=new HashSet();
    private HashSet methods=new HashSet();
    private HashSet relationships=new HashSet();
    
    public StoringArtefacts(){
    
    }
    
    public void setClassName(HashSet clName){
        className = clName;
        //removeRedundant(clName);
        
    }
    public void setAttributes(HashSet attr){
        attributes= attr;
        
    }
    public void setMethods(HashSet met){
        methods = met;
    }
    public void setRelationships(HashSet rel){
        relationships = rel;
        
    }
    
    public HashSet getClassName(){
        return className;
    }
    
    public HashSet getAttributes(){

        return attributes;
    }
    
    public HashSet getMethods(){

        return methods;
    }
    
    public HashSet getRelationships(){

        return relationships;
    }
    
    public void addAttributes(String attribute){
        this.attributes.add(attribute);
    }
    
    public void addMethods(String method){
        this.methods.add(method);
    }
    
    public void addRelationships(ClassRelation crl){
        this.relationships.add(crl);
    }
    
    public void addClassName(HashSet clName){
        className.addAll(clName);
      
        
    }
    public void addAttributes(HashSet attr){
        attributes.addAll(attr);
        
    }
    public void addMethods(HashSet met){
        methods.addAll(met);
    }
    public void addRelationships(HashSet rel){
        relationships.addAll(rel);  
        
    }
/*    private void removeRedundant(HashSet setItems){
        Iterator iterator = setItems.iterator();
        while(iterator.hasNext()){
            if(className.isEmpty()){
                className.add(iterator.next());
            }
            
        }
        
    }
*/   
}
