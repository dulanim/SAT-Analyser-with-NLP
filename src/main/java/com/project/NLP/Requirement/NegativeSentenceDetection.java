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
public class NegativeSentenceDetection {

    private Tree tree;
    private PhrasesIdentification phrasesIdentification;
    

    NegativeSentenceDetection(Tree tree) {
        this.tree = tree;
        phrasesIdentification = new PhrasesIdentification(tree);

    }

    public boolean isNegativeSentence() {
        boolean negativeSentence = false;
        ArrayList wordsFromDictionary = readFromTextFile("res/negativeWordsDictionary.txt");
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
