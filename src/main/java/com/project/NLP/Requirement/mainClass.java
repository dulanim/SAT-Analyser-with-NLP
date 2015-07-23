/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.Requirement;

/**
 *
 * @author S. Shobiga
 */
public class mainClass {
    public static void main(String args[]){
        stanfordCoreNLP stanford = new stanfordCoreNLP("The library system is used by the informatics students");
        stanford.generateTreeAnnotation();
        
    }
}
