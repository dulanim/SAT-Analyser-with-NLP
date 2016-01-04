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
import edu.smu.tspell.wordnet.*;
import edu.stanford.nlp.trees.WordStemmer;

/**
 *
 * @author Vinojan
 */
public class ClassRelationIdentifier {
    
    /*Loading wordnet datatbase instance  */
    private static  WordNetDatabase database = WordNetDatabase.getFileInstance();
    String requirementSentence;
    ArrayList classes = new ArrayList(Arrays.asList("account"));
    private ArrayList<String> verbPhraseList=new ArrayList<>(Arrays.asList("has","have","contains","contain","includes","include","consists","consist"));
    private WordStemmer wordStemmer=new WordStemmer();
    

    public ClassRelationIdentifier() {

    }

    public ClassRelationIdentifier(String text) {
        this.requirementSentence = text;
    }

    public ClassRelationIdentifier(HashSet classList, Set classSet) {

    }

    /* 1. Comparing a new class and a existing class for relation using their names
     * ex: saving account and account -> Generalization parent: account child: saving account
     */
    public HashSet identifyGenaralizationByComparing(HashSet sentenceClassSet, Set documentClassSet) {
        HashSet classRelations = new HashSet();
        if (sentenceClassSet.isEmpty()) {
            System.err.println("Info : There is no class found in the sentence. -Relations identifier");
        } else if (sentenceClassSet.isEmpty()) {
            System.err.println("Info : There is no class found in the document until this sentence. -Relations identifier");
        } else {
            Iterator list = sentenceClassSet.iterator();
            Iterator set = documentClassSet.iterator();
            while (list.hasNext()) {
                String classFromlist = list.next().toString();
                while (set.hasNext()) {
                    String classFromSet = set.next().toString();
                    
                    if(!classFromSet.equalsIgnoreCase(classFromlist)){

                        if (classFromSet.matches(".+" + classFromlist + ".*")) {
                            System.out.println("--------Success --1-- Generalization -----");
                            ClassRelation general = new ClassRelation("Generalization", classFromSet, classFromlist);
                            classRelations.add(general);
                        } else if (classFromlist.matches(".+" + classFromSet + ".*")) {
                            System.out.println("--------Success --1-1-- Generalization -----");
                            ClassRelation general = new ClassRelation("Generalization", classFromlist, classFromSet);
                            classRelations.add(general);

                        }
                    }

                }
            }
        }

        return classRelations;
    }

    /*Using Hypernym and Hyponym to identify Generalization.
     *Ex: Color is Hypernym of Red
     */
    public HashSet identifyGenaralizationByHypernym(HashSet sentenceClassSet, Set documentClassSet) {
        
        HashSet classRelations = new HashSet();
        HashSet hypernymSet=new HashSet();
        HashSet hyponymSet=new HashSet();
        HypernymHyponym hyps=new HypernymHyponym();
        
        if (sentenceClassSet.isEmpty()) {
            System.out.println("Info : There is no class found in the sentence. -Relations identifier");
        } else if (sentenceClassSet.isEmpty()) {
            System.out.println("Info : There is no class found in the document until this sentence. -Relations identifier");
        } else {
            Iterator it=sentenceClassSet.iterator();
            while(it.hasNext()){
                String className=(String) it.next();
                hypernymSet=hyps.getHypernymsForWord(className);
                hyponymSet=hyps.getHyponymsForWord(className);
                Iterator iter=hypernymSet.iterator();
                
                    while(iter.hasNext()){
                        String hypernymClass=(String) iter.next();
                    
                        if(documentClassSet.contains(hypernymClass)){
                            ClassRelation general = new ClassRelation("Generalization",className,hypernymClass);
                            classRelations.add(general);
                        }

                    }
                    
                    iter=hyponymSet.iterator();
                    while(iter.hasNext()){
                        String hyponymClass=(String) iter.next();
                    
                        if(documentClassSet.contains(hyponymClass)){
                            ClassRelation general = new ClassRelation("Generalization",hyponymClass,className);
                            classRelations.add(general);
                        }

                    }
                    
            }
        }
        return classRelations;
    }

    ArrayList identifyCandidateRelations(String document) {
        ArrayList sentenceTree = new ArrayList();
        ArrayList intialRelations = new ArrayList();
        ParserTreeGenerator treeGen = new ParserTreeGenerator(document);
        sentenceTree = treeGen.getSentenceParseTree();
        System.out.println("--------Identified relations are:---------");
        intialRelations = getPhrase(sentenceTree);

        return null;

    }

    ArrayList getPhrase(ArrayList<Tree> sentenceTree) {

        String phraseNotation = "NN";  //"NP<(NP $++ (CC $++ NP))";
        ArrayList vpList = new ArrayList();
        for (Tree tree : sentenceTree) {
            System.out.print("\n---tree_sen----" + tree + "----\n");

            TregexPattern VBpattern = TregexPattern.compile(phraseNotation);
            TregexMatcher matcher = VBpattern.matcher((Tree) tree);
            while (matcher.findNextMatchingNode()) {
                Tree match = matcher.getMatch();
                String noun = Sentence.listToString(match.yield());
                if (noun.contains("-")) {
                    String nouns[] = noun.split("-");
                    if (classes.contains(nouns[nouns.length - 1])) {
                        vpList.add(noun);
                    }

                } else if (noun.contains("_")) {
                    String nouns[] = noun.split("_");
                    if (classes.contains(nouns[nouns.length - 1])) {
                        if (!vpList.contains(noun)) {
                            vpList.add(noun);
                        }
                    }

                }

                System.out.print("\n---phrase match----" + match + "----\n");

            }
        }
        System.out.print("\n---VPList----" + vpList + "----\n");
        return vpList;
    }
    
    
    /*  Comparing a class with other class for relation using their names
     * ex: saving account and account -> Generalization parent: account child: saving account
     */
    public HashSet identifyGenaralizationByComparing( Set documentClassSet) {
        HashSet classRelations = new HashSet();
        if (documentClassSet.isEmpty()) {
            System.err.println("Info : There is no class found in the document. -Relations identifier");
        }  else {
            Iterator list = documentClassSet.iterator();
           
            while (list.hasNext()) {
                String classFromlist = list.next().toString();
                //System.out.println("----Class1 name------"+classFromlist+" -------");
                 Iterator set = documentClassSet.iterator();
                while (set.hasNext()) {
                    String classFromSet = set.next().toString();
                    //System.out.println("----Class2 name------"+classFromSet+" -------");
                    if(!(classFromSet.equals(classFromlist))){
                       // System.out.println("----Same class name -------");
                        if (classFromSet.matches(".+" + classFromlist + ".*")) {
                            //System.out.println("--------Success --1-- Generalization -----");
                            ClassRelation general = new ClassRelation("Generalization", classFromSet, classFromlist);
                            classRelations.add(general);
                        } else if (classFromlist.matches(".+" + classFromSet + ".*")) {
                            //System.out.println("--------Success --1-1-- Generalization -----");
                            ClassRelation general = new ClassRelation("Generalization", classFromlist, classFromSet);
                            classRelations.add(general);

                        }
                    }

                }
            }
        }

        return classRelations;
    }
    
    /*Using Hypernym and Hyponym to identify Generalization.
     *Ex: Color is Hypernym of Red
     */
    public HashSet identifyGenaralizationByHypernym(Set documentClassSet) {
        
        HashSet classRelations = new HashSet();
        HashSet hypernymSet=new HashSet();
        HashSet hyponymSet=new HashSet();
        HypernymHyponym hyps=new HypernymHyponym();
        
        if (documentClassSet.isEmpty()) {
            System.out.println("Info : There is no class found in the sentence. -Relations identifier");
        } else {
            Iterator it=documentClassSet.iterator();
            while(it.hasNext()){
                String className=(String) it.next();
                // System.out.println("----Class name------"+className+" -------");
                hypernymSet=hyps.getHypernymsForWord(className);
                hyponymSet=hyps.getHyponymsForWord(className);
                Iterator iter=hypernymSet.iterator();
                    while(iter.hasNext()){
                        String hypernymClass=(String) iter.next();
                        //System.out.println("----------"+hypernymClass+" -------");
                        if(documentClassSet.contains(hypernymClass)){
                           // System.out.println("----general------"+hypernymClass+" -------");
                            ClassRelation general = new ClassRelation("Generalization",className,hypernymClass);
                            classRelations.add(general);
                        }

                    }
                    iter=hyponymSet.iterator();
                    while(iter.hasNext()){
                        String hyponymClass=(String) iter.next();
                    
                        if(documentClassSet.contains(hyponymClass)){
                            // System.out.println("----general------"+hyponymClass+" -------");
                            ClassRelation general = new ClassRelation("Generalization",hyponymClass,className);
                            classRelations.add(general);
                        }

                    }
            }
        }
        return classRelations;
    }
    /*Combining relations identifier mathods
    *by comparing
    *by hyperny hyponym
    */
    public HashSet identifyGeneralizationRelations(Set reqObjects){
        HashSet relations=new HashSet();
        relations=identifyGenaralizationByComparing(reqObjects);
        relations.addAll(identifyGenaralizationByHypernym(reqObjects));
        
        return relations;
    } 
    /*Extracting Association (Aggregation or Composition) relations
    *   
    */
    public HashSet identifyAssociation(Tree tree,Set documentClass){
        HashSet classRelations = new HashSet();
        String phraseNotation="S<(NP.(VP<NP))";
        String verbPhraseNotation="VBZ|VBP>(VP,(NP>S))";
        /* Stemming the sentence */        
        wordStemmer.visitTree(tree);
        TregexPattern pattern = TregexPattern.compile(phraseNotation);
        TregexMatcher matcher = pattern.matcher((Tree) tree);
        TregexPattern verbPattern = TregexPattern.compile(verbPhraseNotation);
        
            while (matcher.findNextMatchingNode()) {
                Tree match = matcher.getMatch();
                               
                TregexMatcher verbMatcher = verbPattern.matcher(match);
               // while(verbMatcher.findNextMatchingNode()){
                if(verbMatcher.findNextMatchingNode()){
                    Tree verbMatch=verbMatcher.getMatch();
                    String verb=Sentence.listToString(verbMatch.yield());
                    if(verbPhraseList.contains(verb)){
                        System.out.print("\n---verb in the list----"+verb+"----\n");
                        String noun_1_phraseNotation="NN|NNS>(NP>S)";
                        String noun_2_phraseNotation="NN|NNS>>(NP,(VBZ|VBP>(VP,NP)))";
                        TregexPattern noun_pattern = TregexPattern.compile(noun_1_phraseNotation);
                        TregexMatcher noun_matcher = noun_pattern.matcher((Tree) tree);
                        if(noun_matcher.findNextMatchingNode()){
                            Tree nounMatch=noun_matcher.getMatch();
                            String noun1=Sentence.listToString(nounMatch.yield());
                            System.out.print("\n---noun1----"+noun1+"----\n");
                            if(documentClass.contains(noun1)){
                                noun_pattern = TregexPattern.compile(noun_2_phraseNotation);
                                noun_matcher = noun_pattern.matcher((Tree) tree);
                                if(noun_matcher.findNextMatchingNode()){
                                    nounMatch=noun_matcher.getMatch();
                                    String noun2=Sentence.listToString(nounMatch.yield());
                                    System.out.print("\n---noun2----"+noun2+"----\n");
                                    if(documentClass.contains(noun2)){
                                        ClassRelation clr=new ClassRelation("Association", noun2, noun1);
                                        classRelations.add(clr);
                                    }
                                }
                            }
                           
                           
                        }
             
                        
                    }
                }
                }
               
    
       
       return classRelations;
    }
    

}
