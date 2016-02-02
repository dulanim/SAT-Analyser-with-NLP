/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.Requirement;

import java.util.HashSet;

/**
 * class to store the artefacts 
 * @author T. Vinojan
 */
public class StoringArtefacts {
    private HashSet className=new HashSet();
    private HashSet attributes=new HashSet();
    private HashSet methods=new HashSet();
    private HashSet relationships=new HashSet();
    
    /**
     * constructor
     */
    public StoringArtefacts(){
    
    }
    /**
     * constructor with parameters
     * @param clName 
     */
    public void setClassName(HashSet clName){
        className = clName;
        //removeRedundant(clName);
        
    }
    
    /**
     * method to set the attributes
     * @param attr 
     */
    public void setAttributes(HashSet attr){
        attributes= attr;
        
    }
    /**
     * method to set method names
     * @param met 
     */
    public void setMethods(HashSet met){
        methods = met;
    }
    /**
     * method to set the relationships
     * @param rel 
     */
    public void setRelationships(HashSet rel){
        relationships = rel;
        
    }
    /**
     * method to get class names
     * @return hashSet
     */
    public HashSet getClassName(){
        return className;
    }
    
    /**
     * method to get the attributes
     * @return hashSet
     */
    public HashSet getAttributes(){
        return attributes;
    }
    /**
     * method to get the method set
     * @return hashSet
     */
    public HashSet getMethods(){

        return methods;
    }
    
    /**
     * method to get the relationships
     * @return hashSet
     */
    public HashSet getRelationships(){

        return relationships;
    }
    
    /**
     * method to add attributes
     * @param attribute 
     */
    public void addAttributes(String attribute){
        this.attributes.add(attribute);
    }
    
    /**
     * method to add method names
     * @param method 
     */
    public void addMethods(String method){
        this.methods.add(method);
    }
    
    /**
     * method to add relationships
     * @param crl 
     */
    public void addRelationships(ClassRelation crl){
        this.relationships.add(crl);
    }
    
    /**
     * method to add class names
     * @param clName 
     */
    public void addClassName(HashSet clName){
        className.addAll(clName);
      
        
    }
    /**
     * method to add attributes 
     * @param attr 
     */
    public void addAttributes(HashSet attr){
        attributes.addAll(attr);
        
    }
    /**
     * method to add method names as a set
     * @param met 
     */
    public void addMethods(HashSet met){
        methods.addAll(met);
    }
    /**
     * method to add the relationships as a set
     * @param rel 
     */
    public void addRelationships(HashSet rel){
        relationships.addAll(rel);  
        
    }
    /**
     * method to remove the attribute name
     * @param name
     * @return 
     */
    public boolean removeAttribute(String name){
        if(attributes.contains(name)){
            attributes.remove(name);
            return true;
        }
        return false;
        
    }

}
