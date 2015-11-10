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
    private HashSet className;
    private HashSet attributes;
    private HashSet methods;
    private HashSet relationships;
    
    StoringArtefacts(){
        
    }
    
    public void setClassName(HashSet clName){
        className = clName;
        //removeRedundant(clName);
        
    }
    public void setAttributess(HashSet attr){
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
