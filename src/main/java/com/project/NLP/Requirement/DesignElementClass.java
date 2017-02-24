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
 * class which reads the dictionary as a text file and stores the words in the
 * array list
 *
 * @author S. Shobiga
 */
public class DesignElementClass {

    ArrayList designElements;

    /**
     * constructor
     */
    DesignElementClass() {
        designElements = new ArrayList();
        setDesignElementsList();
    }

    /**
     * after reading the text file, words are stored in the array list
     */
    private void setDesignElementsList() {
        String filePath = FilePropertyName.RESOURCE_PATH + File.separator + "DesignElementsDictionary.txt";
        designElements = readFromTextFile(filePath);
    }

    /**
     * method to return the dictionary words
     *
     * @return array list
     */
    public ArrayList getDesignElementsList() {
        return designElements;
    }

    /**
     * method to read the dictionary file
     *
     * @param file
     * @return
     */
    private ArrayList readFromTextFile(String file) {
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
