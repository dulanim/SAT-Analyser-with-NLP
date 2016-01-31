/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.Requirement;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author S. Shobiga
 */
public class DictionaryForClass {
  ArrayList dictionaryForClass;

    DictionaryForClass() {
        dictionaryForClass = new ArrayList();
        setDictionaryForClass();
    }

    /*add design elements such as system, aplication and etc. to the arrayList
     */
    private void setDictionaryForClass() {
        dictionaryForClass= readFromTextFile("res/ClassElementsDictionary.txt");
    }

    public ArrayList getDictionaryForClass() {
        //System.out.println("from design :"+designElements);
        return dictionaryForClass;
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

