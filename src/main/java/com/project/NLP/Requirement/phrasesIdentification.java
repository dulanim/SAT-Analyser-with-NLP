/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *..
 */
package com.project.NLP.Requirement;

import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;
import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author S. Shobiga
 */
public class phrasesIdentification  {
    private String text;
    
    private ArrayList tree;
    private ArrayList phraseLists;
    static stanfordCoreNLP stanford;
    phrasesIdentification(){
        
    }
    
    phrasesIdentification(String text){
        this.text=text;
        stanford=new stanfordCoreNLP(text);
        
        if(tree == null){
            tree =stanford.generateTreeAnnotation();
        }
        
    }
    
    phrasesIdentification(ArrayList tree){
        
        this.tree=tree;
       
        
    }
    private int getTreeCount(){
        return tree.size();
    }
    
    public void NPIdentification(){
        System.out.println("Identified noun pharases are:");
        
        for(int i=0;i<tree.size();i++){
            
            TregexPattern NPpattern = TregexPattern.compile("@VB !<< @VB");
            TregexMatcher matcher = NPpattern.matcher((Tree)tree.get(i));
            while (matcher.findNextMatchingNode()) {
                Tree match = matcher.getMatch();
                System.out.println(Sentence.listToString(match.yield()));
            }
        }
    }
    
    /*method to identify the phrases in the tree without eliminating redundancy
    parameters are the POS.
    for example: NP, VB, NNP and etc.*/
    public ArrayList getIdentifiedPhrases(String phrase){
        phraseLists= new ArrayList();
        String phraseNotation = "@"+phrase+"! << @"+phrase;
        System.out.println("Identified noun pharases are:");
        
        for(int i=0;i<tree.size();i++){
            
            TregexPattern NPpattern = TregexPattern.compile(phraseNotation);
            TregexMatcher matcher = NPpattern.matcher((Tree)tree.get(i));
            while (matcher.findNextMatchingNode()) {
                Tree match = matcher.getMatch();
                System.out.println("sdf  "+Sentence.listToString(match.yield()));
                phraseLists.add(Sentence.listToString(match.yield()));
            }
        }
        return phraseLists;
    }
    
    public ArrayList innerPhrases(String p){
        
        ArrayList innerPhraseList = new ArrayList();
        ArrayList list = phraseLists;
        /* run entire list*/
        for(int i=0;i<list.size();i++){
            stanfordCoreNLP s = new stanfordCoreNLP(list.get(i).toString());
            //stanfordCoreNLP s = new stanfordCoreNLP("the Bank client");
            tree =s.generateTreeAnnotation();
            list=getIdentifiedPhrases("NN");
            if(list.size()==2){
                innerPhraseList.add(list.get(0));
            }

        }
        return innerPhraseList;
    }
}
