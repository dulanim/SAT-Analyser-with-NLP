/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.Requirement;

import java.util.ArrayList;

/**
 *
 * @author S. Shobiga
 */

/*This class checks whether artefact -class contains any of the design elements 
*/
public class designElementClass {

    ArrayList designElements;
    
    designElementClass(){
        designElements = new ArrayList();
        setDesignElementsList();
    }
    
    
    /*add design elements such as system, aplication and etc. to the arrayList
    */
    private void setDesignElementsList(){
        designElements.add("system");
        designElements.add("application");
        designElements.add("data");
        designElements.add("computer");
        designElements.add("user");
        
    }
    
    public ArrayList getDesignElementsList(){
        
        return designElements;
    }
    
    
    
    
}
