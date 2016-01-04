/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.Requirement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author S. Shobiga
 */

/*This is the class to modify the sentences. For example if the sentence is having but it will divide the 
 sentence into two and '-' are removed and replaced by space.
 */
public class SentenceReplacement {

    private String sentence;
    private String fileName;

    SentenceReplacement(String sentence, String file) {
        this.sentence = sentence;
        this.fileName = file;
        splitSentence();
    }

    private void splitSentence() {
        String replacingWord = "";
        String newSentence = "";
        if (sentence.contains("but")) {
            newSentence = sentence.replace("but", ". But");
            int position = sentence.indexOf("but");
            
            System.out.println("New Sentence: " + newSentence);
            try{
                FileWriter filewriter = new FileWriter(fileName);
                filewriter.append(newSentence);
                filewriter.close();

            }
            catch(Exception e){
                
            }
            //WriteToTextFile(newSentence);
        }
    }

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
