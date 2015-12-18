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
    private int hashCodeOfRelation;
    
    public ClassRelation(){
        
    }
    
    public ClassRelation (String type, String child, String parent){
        this.relationType=type;
        this.childElement=child;
        this.parentElement=parent;
        this.hashCodeOfRelation=findHashCode();
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
    
    
    private int findHashCode(){
        return calculateAsciValue(relationType)+calculateAsciValue(parentElement)+calculateAsciValue(childElement);
        
    }
   
    @Override
    public int hashCode(){
        return findHashCode();
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ClassRelation other = (ClassRelation) obj;
        if (this.hashCodeOfRelation != other.hashCodeOfRelation) {
            return false;
        }
        return true;
    }
    
    private int calculateAsciValue(String name){
        int asciValue=0;
        if(name!=null && !name.equals("")){
            char[] charArray=(name.toLowerCase()).toCharArray();
            for(char c: charArray){
                asciValue+=c;
            }
        }
        return asciValue;        
        
    }
}

