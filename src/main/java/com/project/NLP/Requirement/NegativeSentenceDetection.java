/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.Requirement;

import com.project.NLP.file.operations.FilePropertyName;
import edu.stanford.nlp.trees.Tree;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * class for identifying the negative sentence
 * 
 * @author S. Shobiga
 */
public class NegativeSentenceDetection {

    private Tree tree;
    private PhrasesIdentification phrasesIdentification;
    
    /**
     * constructor which takes a single tree
     * @param tree 
     */
    NegativeSentenceDetection(Tree tree) {
        this.tree = tree;
        phrasesIdentification = new PhrasesIdentification(tree);

    }

    /**
     * check whether a sentence is negative or not by comparing the negative words with the dictionary with 
     * the negative words detected by tokenizing a sentence 
     * @return 
     */
    public boolean isNegativeSentence() {
        boolean negativeSentence = false;
        String filePath = FilePropertyName.RESOURCE_PATH + File.separator + "NegativeWordsDictionary.txt";
        ArrayList wordsFromDictionary = readFromTextFile(filePath);
        ArrayList negativeWordList = phrasesIdentification.NegativeSentenceDetection();
        boolean b= phrasesIdentification.checkActiveOrPassive();
        System.out.println("NegativeWordList:" + negativeWordList);
        System.out.println("wordsFromDictionary:" + wordsFromDictionary);

        for(int i=0; i<negativeWordList.size(); i++){
            if(wordsFromDictionary.contains(negativeWordList.get(i))){
                negativeSentence= true;
                break;
            }
        }
 
        System.out.println("NEGATIVE: "+ negativeSentence);
        return negativeSentence;
    }

    /**
     * method to read the file 
     * @param file
     * @return 
     */
    private static ArrayList readFromTextFile(String file) {
        BufferedReader br = null;
        ArrayList words = new ArrayList();

        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader(file));
            while ((sCurrentLine = br.readLine()) != null) {
                words.add(sCurrentLine);
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
        return words;

    }

}
