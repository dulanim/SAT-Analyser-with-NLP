/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.Requirement;

import edu.stanford.nlp.trees.Tree;
import java.util.ArrayList;

/**
 *
 * @author S. Shobiga
 */

/*This class checks whether artefact -class contains any of the design elements 
 */
public class DesignElementClass {

    ArrayList designElements;

    DesignElementClass() {
        designElements = new ArrayList();
        setDesignElementsList();
    }

    /*add design elements such as system, aplication and etc. to the arrayList
     */
    private void setDesignElementsList() {
        designElements.add("company");
        designElements.add("application");
        designElements.add("computer");
        designElements.add("data");
        designElements.add("detail");
        designElements.add("information");
        designElements.add("organization");
        designElements.add("record");
        designElements.add("system");
        designElements.add("user");

    }

    public ArrayList getDesignElementsList() {
        //System.out.println("from design :"+designElements);
        return designElements;
    }

}
