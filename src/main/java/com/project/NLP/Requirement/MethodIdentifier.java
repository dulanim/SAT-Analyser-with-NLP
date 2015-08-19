/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.project.NLP.Requirement;

import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;
import java.util.ArrayList;

/**
 *
 * @author Vinojan
 * 
 * Identifying methods related to a particular class.
 */
public class MethodIdentifier {
    ArrayList tree;
    
    public MethodIdentifier(){
        
    }
    
    public MethodIdentifier(ArrayList list){
        this.tree=list;
    }
    
    ArrayList identifyCandidateMethods(String sentence){
        ArrayList canClasses;
        //canClasses = new ArrayList();
        
        stanfordCoreNLP stanford = new stanfordCoreNLP(sentence);
        ArrayList treeTemp = stanford.generateTreeAnnotation();
        stanford.generateCorefLink();
        
        ArrayList phraseLists= new ArrayList();
        String phraseNotation = "@"+"VB"+"! << @"+"VB";
        System.out.println("--------Identified verb pharases are:---------");
        
        for (Object treeTemp1 : treeTemp) {
            TregexPattern VBpattern = TregexPattern.compile(phraseNotation);
            TregexMatcher matcher = VBpattern.matcher((Tree) treeTemp1);
            while (matcher.findNextMatchingNode()) {
                Tree match = matcher.getMatch();
                System.out.println("sdf  "+Sentence.listToString(match.yield()));
                phraseLists.add(Sentence.listToString(match.yield()));
            }
        }
            
        canClasses=phraseLists;
        return canClasses;
    }
    
}
