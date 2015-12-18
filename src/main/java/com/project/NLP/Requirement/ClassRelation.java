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
 * type (Association, Aggregation and Generalization)
 * start element(class)
 * end element(class)
 * 
 */
public class ClassRelation {
    
    String relationType;
    String startElement;
    String endElement;
    
    public ClassRelation(){
        
    }
    
    public ClassRelation (String type, String start, String end){
        this.relationType=type;
        this.startElement=start;
        this.endElement=end;
    }
    
    private void setRelationType(String type){
        this.relationType=type;
    }
    private String getRelationType(){
        return relationType;
    }
    private void setStartElement(String start){
        this.startElement=start;
    }
    private String getStartElement(){
        return this.startElement;
    }
    private void setEndElement(String end){
        this.endElement=end;
    }
    private String getEndElement(){
        return this.endElement;
    }
}

