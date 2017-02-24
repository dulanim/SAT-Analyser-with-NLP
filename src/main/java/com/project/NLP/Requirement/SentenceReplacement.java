/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.Requirement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * This is the class to modify the sentences. 
 * For example if the sentence is having 'but', it will replace the sentence by starting a new sentence. 
 * Like wise, it deals with complex sentences, hyphens, apostrophe and etc. 
 * 
 * @author S. Shobiga
 */

/*
 */
public class SentenceReplacement {

    private String sentence;
    private String fileName;

    /**
     * constructor without parameters
     */
    SentenceReplacement() {

    }

    /**
     * constructor with parameters
     * @param sentence
     * @param file 
     */
    SentenceReplacement(String sentence, String file) {
        this.sentence = sentence;
        this.fileName = file;
        this.sentence = doModify(sentence);
    }

    public String doModify(String currentLine) {
        sentence = currentLine;

        /*start a new sentence if the sentence contains but */
        if (sentence.contains("but")) {
            sentence = sentence.replace("but", ". But");
        }
        /*if the sentence is having hyphen, then it is replaced by a space*/
        if (sentence.contains("-")) {
            sentence = sentence.replace("-", " ");
        }
        /*if the sentence is having 's, then it is replaced by space EX: employee's -> employee*/
        if (sentence.contains("'s")) {
            sentence = sentence.replace("'s", " ");
        }
        if (sentence.contains("which")) {
            sentence = sentence.replace("which", ". It ");
        }
        /*if the sentence if having underscore, then it is replace by a space*/
        if (sentence.contains("_")) {
            //sCurrentLine = sCurrentLine.replace("_", " ");
        }
        if (sentence.contains(",")) {
            //    sCurrentLine = sCurrentLine.replace(",", ". ");
        }
        if (sentence.contains("if")) {
            ///  sCurrentLine = sCurrentLine.replace("if", ". ");
        }
        System.out.println("Modifications are done");
        return sentence;
    }

    /**
     * method to write the modified sentence to the text file
     * @param newSentence 
     */
    public void WriteToTextFile(String newSentence) {
        //write to file : "Requirement Output"
        try {

            StringBuffer sbf = new StringBuffer();
            sbf.append(newSentence);

            System.out.println(sbf.toString());

            BufferedWriter bwr = new BufferedWriter(new FileWriter(new File(fileName)));

            //write contents of StringBuffer to a file
            bwr.write(sbf.toString());

            //flush the stream
            bwr.flush();

            //close the stream
            bwr.close();

        } catch (Exception e) {
            System.out.println("Exception: " + e);
            e.printStackTrace();
        }
    }

}
