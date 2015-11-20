/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.project.NLP.Requirement;

/**
 *
 * @author Vinojan
 * 
 * Blue print of relation between two classes which includes
 * type (Association and Generalization)
 * start element(class) -child
 * end element(class) -parent
 * 
 */
public class ClassRelation {
    
    private String relationType;
    private String childElement;
    private String parentElement;
    
    public ClassRelation(){
        
    }
    
    public ClassRelation (String type, String child, String parent){
        this.relationType=type;
        this.childElement=child;
        this.parentElement=parent;
    }
    
    public void setRelationType(String type){
        this.relationType=type;
    }
    public  String getRelationType(){
        return relationType;
    }
    public  void setCildElement(String child){
        this.childElement=child;
    }
    public  String getChildElement(){
        return this.childElement;
    }
    public  void setParentElement(String parent){
        this.parentElement=parent;
    }
    public String getParentElement(){
        return this.parentElement;
    }
}

