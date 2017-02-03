/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.Requirement;

/**
 *
 * @author S. Shobiga
 */
public class MainClass {
    public static void main(String args[]){

       // System.setProperty("wordnet.database.dir", "C:\\Users\\SAMITHA-LAP\\WordNet\\dict\\");
        Runtime.getRuntime().maxMemory();
        NLPRequirementMain.extractRequirement();
    }
}
