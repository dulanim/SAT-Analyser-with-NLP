/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.Requirement;

import com.project.NLP.file.operations.FilePropertyName;
import java.io.BufferedReader;
import java.io.File;
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

    /**
     * read dictionary file and store words in the array list
     */
    private void setDictionaryForClass() {
        String filePath = FilePropertyName.RESOURCE_PATH + File.separator + "ClassElementsDictionary.txt";
        dictionaryForClass= readFromTextFile(filePath);
    }

    /**
     * get dictionary words 
     * @return arrayList
     */
    public ArrayList getDictionaryForClass() {
        //System.out.println("from design :"+designElements);
        return dictionaryForClass;
    }
    
    /**
     * method to read the text file
     * @param file
     * @return 
     */
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

