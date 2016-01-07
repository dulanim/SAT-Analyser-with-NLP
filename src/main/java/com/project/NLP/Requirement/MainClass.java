/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *..
 */
package com.project.NLP.Requirement;

import edu.stanford.nlp.process.Morphology;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.WordStemmer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class MainClass {
    
    private static String requirementDocument="";
    private static HashMap requirementObjects = new HashMap();

    public static void main(String args[]) {
        String req="Mary is agirl. She is ten years old. She has a pencil.";
        AnaphoraAnalyzer analyzer=new AnaphoraAnalyzer(req);
        analyzer.doPronounResolving();
        
        
    }

}