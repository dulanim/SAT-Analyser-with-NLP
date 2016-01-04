/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.Requirement;

import edu.stanford.nlp.trees.Tree;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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
        designElements= readFromTextFile("res/DesignElementsDictionary.txt");
    }

    public ArrayList getDesignElementsList() {
        //System.out.println("from design :"+designElements);
        return designElements;
    }
    
    private ArrayList readFromTextFile(String file){
    BufferedReader br = null;
        ArrayList designEle = new ArrayList();

        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader(file));
            while ((sCurrentLine = br.readLine()) != null) {
                designEle.add(sCurrentLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return designEle;
    }

}
