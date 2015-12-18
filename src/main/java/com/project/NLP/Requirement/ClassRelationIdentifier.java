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

/**
 *
 * @author Vinojan
 */
public class ClassRelationIdentifier {

    private static  WordNetDatabase database = WordNetDatabase.getFileInstance();
    String requirementSentence;
    ArrayList classes = new ArrayList(Arrays.asList("account"));

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
                    while(it.hasNext()){
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

}
