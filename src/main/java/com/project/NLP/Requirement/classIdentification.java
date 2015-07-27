/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *..
 */
package com.project.NLP.Requirement;

import java.util.ArrayList;

/**
 *
 * @author S. Shobiga
 */
public class classIdentification {
    
    ArrayList list;
    phrasesIdentification np;
    
    classIdentification(){
        
    }
    
    classIdentification(ArrayList list){
        this.list=list;
        np=new phrasesIdentification(list);
        
    }
    
    public ArrayList nounPharseIdentification(String phrase){
        ArrayList verb=np.getIdentifiedPhrases(phrase);
        ArrayList v =np.innerPhrases("NN");
        System.out.println("Class found: "+v.toString());
        return verb;
    }
    
}
