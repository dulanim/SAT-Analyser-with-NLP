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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Vinojan
 */

public class ClassRelationIdentifier {
    
    String requirementSentence;
    ArrayList classes=new ArrayList(Arrays.asList("account"));
    
    public ClassRelationIdentifier(){
        
    }
    
    public ClassRelationIdentifier(String text){
        this.requirementSentence=text;
        
    }
    
    public ClassRelationIdentifier(HashSet classList, Set classSet){
        
    }
    
    public HashSet identifyGenaralizationByComparing(HashSet classList, Set classSet){
        HashSet classRelations = new HashSet();
        if(classList.isEmpty()){
            System.out.println("Info : There is no class found in the sentence. -Relations identifier");
        }
        else if(classList.isEmpty()){
            System.out.println("Info : There is no class found in the document until this sentence. -Relations identifier");
        }
        else{
            Iterator list= classList.iterator();
            Iterator set= classSet.iterator();
            while(list.hasNext()){
                String classFromlist=list.next().toString();
                while(set.hasNext()){
                    // To ignore case, regular expressions must be used
                    String classFromSet=set.next().toString();
                    /*
                    Boolean b;
                    String string ="Madam";
                    // Starts with
                    b=string.matches("(?i)mad.*");

                    // Ends with
                    b = string.matches("(?i).*adam");

                    // Anywhere
                    b = string.matches("(?i).*i am.*");
                    */
                    if(classFromSet.matches(".+"+classFromlist+".*")){
                        System.out.println("--------Success --1-- Generalization -----");
                    }
                    else if(classFromlist.matches(".+"+classFromSet+".*")){
                        System.out.println("--------Success --1-1-- Generalization -----");
                        ClassRelation general =new ClassRelation("Generalization", classFromlist, classFromSet);
                        classRelations.add(general);
                        
                    }
                    if(classFromSet.endsWith(classFromlist)){
                        System.out.println("--------Success ----2-- Generalization -----");
                    }
                    else if(classFromlist.endsWith(classFromSet)){
                        System.out.println("--------Success ----2-2-- Generalization -----");
                    }
                }
            }
        }
                
                
        
        
        return classRelations;
    } 
    
    ArrayList identifyCandidateRelations(String document){
        ArrayList sentenceTree=new ArrayList();
        ArrayList intialRelations=new ArrayList();
        ParserTreeGenerator treeGen= new ParserTreeGenerator(document);
        sentenceTree=treeGen.getSentenceParseTree();
        System.out.println("--------Identified relations are:---------");
        intialRelations= getPhrase(sentenceTree);
        
        return null;
        
    }
    ArrayList getPhrase(ArrayList<Tree> sentenceTree){
         
        String phraseNotation = "NN";  //"NP<(NP $++ (CC $++ NP))";
        ArrayList vpList=new ArrayList();     
        for (Tree tree : sentenceTree) {
                    System.out.print("\n---tree_sen----"+tree+"----\n");

            TregexPattern VBpattern = TregexPattern.compile(phraseNotation);
            TregexMatcher matcher = VBpattern.matcher((Tree) tree);
            while (matcher.findNextMatchingNode()) {
                Tree match = matcher.getMatch();
                String noun=Sentence.listToString(match.yield());
                if(noun.contains("-")){
                    String nouns[]=noun.split("-");
                    if(classes.contains(nouns[nouns.length-1])){
                        vpList.add(noun);
                    }
                    
                }                
                else if(noun.contains("_")){
                    String nouns[]=noun.split("_");
                     if(classes.contains(nouns[nouns.length-1])){
                         if(!vpList.contains(noun))
                                 vpList.add(noun);
                    }
                    
                }
                
                
                
                System.out.print("\n---phrase match----"+match+"----\n");
                
            }
        }
        System.out.print("\n---VPList----"+vpList+"----\n");
        return vpList;    
     }
    
    
    
}

