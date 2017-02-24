/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *..
 */
package com.project.NLP.Requirement;

import edu.stanford.nlp.process.Morphology;
import edu.stanford.nlp.trees.SemanticHeadFinder;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.WordStemmer;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 *  NEED STRONG RULES FOR CLASSES AND ATTRIBUTES FOR COMPLEX SENTENCES
 * Class to identify the artefacts such as classes and attributes
 *
 * @author S. Shobiga
 */
public class PhrasesIdentification {

    private String text;
    private Tree[] tree;
    private Tree sTree;
    private ArrayList phraseLists;
    private ArrayList attributeLists;
    private ArrayList nounList;
    private ArrayList adjClassList;
    private ArrayList adjAttributeList;
    private WordStemmer wordStemmer = new WordStemmer();
    private DesignElementClass designElement;
    private DictionaryForClass dictionaryForClass;
    private ArrayList designEleList;
    private ArrayList dictionaryForClassList;
    private Morphology morphology;
    private HashMap<String, HashSet> storingClassWithAttr; // to store classes and attributes when noun + noun exists

    PhrasesIdentification() {
    }

    /**
     * parsing the tree array to extract classes and attributes
     *
     * @param tree
     */
    PhrasesIdentification(Tree[] tree) {
        this.tree = tree;
    }

    /**
     * parsing a single tree to extract the classes and attributes
     *
     * @param tree
     */
    PhrasesIdentification(Tree tree) {
        this.sTree = tree;
        designElement = new DesignElementClass();
        designEleList = designElement.getDesignElementsList();
        dictionaryForClass = new DictionaryForClass();
        dictionaryForClassList = dictionaryForClass.getDictionaryForClass();
        morphology = new Morphology();
    }

    /**
     * method to get the negative characters from the sentence
     *
     * @return arrayList of negative words in a sentence which are denoted by RB
     * and CC
     */
    public ArrayList NegativeSentenceDetection() {
        String phraseNotation = "RB|CC";//@" + phrase + "! << @" + phrase;
        TregexPattern VBpattern = TregexPattern.compile(phraseNotation);
        TregexMatcher matcher = VBpattern.matcher(sTree);
        ArrayList negativeLists = new ArrayList();
        while (matcher.findNextMatchingNode()) {
            Tree match = matcher.getMatch();
            Tree[] innerChild = match.children();
            for (Tree inChild : innerChild) {
                negativeLists.add(inChild.getLeaves().get(0).yieldWords().get(0).word());
            }
        }
        return negativeLists;
    }

    /**
     * method to extract the classes from a sentence
     *
     * @return ArrayList: arrayList of classes from a sentence
     */
    public ArrayList getClassList() {
        nounList = new ArrayList();
        attributeLists = new ArrayList();
        int adjectiveExist = 0;
        int adjectiveNoun = 0;
        String adj = "";
        String storingClass = "";
        HashSet classWithAttr = new HashSet();
        storingClassWithAttr = new HashMap<String, HashSet>();

        List<Tree> leaves;
        String phraseNotation = "(NP([<NNS|NN|NNP]$VP))";//@" + phrase + "! << @" + phrase;

        /*For the single Tree */
        TregexPattern VBpattern = TregexPattern.compile(phraseNotation);
        TregexMatcher matcher = VBpattern.matcher(sTree);
        String tempClass = "";

        while (matcher.findNextMatchingNode()) {
            Tree match = matcher.getMatch();
            Tree[] innerChild = match.children();
            adjectiveExist = 0;
            adjectiveNoun = 0;
            int separator = 0;

            if (innerChild.length > 1) {
                int count = 1;
                int loopCount = 1;
                for (Tree inChild : innerChild) {
                    if (inChild.value().equals("CC")) {
                        separator = 1;
                    }
                    if ((inChild.value().equals("JJ")) || (inChild.value().equals("VBG"))) {
                        adjectiveExist++;
                        leaves = inChild.getLeaves();
                        adj = leaves.get(0).yieldWords().get(0).word();
                        if (dictionaryForClassList.contains(adj)) {
                            adj = "";
                        }
                    }
                    //if adjective exist store the classes and attributes separately
                    if (adjectiveExist == 1) {
                        storeClassesAndAttributesWhenAdjectiveExistToIdentifyClasses(inChild, adjectiveNoun, adj);
                    } else {
                        //storeClassesAndAttributesWhenAdjectiveNotExistToIdentifyClasses(inChild, loopCount, innerChild, separator, tempClass, count);
                        if ((inChild.value().equals("NN")) || (inChild.value().equals("NNS") || (inChild.value().equals("NNP")))) {
                            leaves = inChild.getLeaves(); //leaves correspond to the tokens
                            if (separator == 0) {
                                if (loopCount == innerChild.length) {
                                    String identifiedWord = ((leaves.get(0).yieldWords()).get(0).word());
                                    String word = "";
                                    word = stemmingForAWord(identifiedWord);
                                    if (!dictionaryForClassList.contains(word)) {
                                        nounList.remove(tempClass);
                                        nounList.add(word);
                                        attributeLists.add(tempClass);
                                        
                                    }

                                } else if (count == 1) {
                                    String identifiedWord = ((leaves.get(0).yieldWords()).get(0).word());
                                    /*if the identified word is having underscore skips the stemming part . ex: user_id*/
                                    String word = stemmingForAWord(identifiedWord);
                                    nounList.add(word);
                                    tempClass = word;
                                    storingClass = word;

                                } else {
                                    /*if the identified word is having underscore skips the stemming part . ex: user_id*/
                                    if (tempClass.contains("_")) {
                                        nounList.remove(tempClass);
                                    } else {
                                        nounList.remove(morphology.stem(tempClass));
                                        nounList.remove(tempClass);
                                    }
                                    String identifiedWord = ((leaves.get(0).yieldWords()).get(0).word());

                                    tempClass += " " + identifiedWord;
                                    nounList.add(tempClass);
                                    storingClass = tempClass;
                                }

                                count++;
                            } else {
                                String identifiedWord = ((leaves.get(0).yieldWords()).get(0).word());
                                /*if the identified word is having underscore skips the stemming part . ex: user_id*/
                                String word = stemmingForAWord(identifiedWord);
                                nounList.add(word);
                                tempClass = word;
                                storingClass = word;
                            }
                        }

                    }
                    loopCount++;
                }
            } else {
                for (Tree inChild : innerChild) {
                    if ((inChild.value().equals("NN")) || (inChild.value().equals("NNS")) || (inChild.value().equals("NNP"))) {
                        leaves = inChild.getLeaves(); //leaves correspond to the tokens
                        String identifiedWord = ((leaves.get(0).yieldWords()).get(0).word());
                        if (!identifiedWord.contains("_")) {
                            nounList.add(morphology.stem(identifiedWord));
                        } else {
                            nounList.add(identifiedWord);
                        }
                    }
                    if (inChild.value().equals("JJ")) {
                        //leaves correspond to the tokens
                        leaves = inChild.getLeaves();
                        nounList.add(((leaves.get(0).yieldWords()).get(0).word()));
                    }
                }
            }
        }
        System.out.println("NOUN LIST :" + nounList);
        return nounList;
    }

    /**
     * method to identify the classes and attributes when adjective exist
     *
     * @param inChild
     * @param adjectiveNoun
     * @param adj
     */
    private void storeClassesAndAttributesWhenAdjectiveExistToIdentifyClasses(Tree inChild, int adjectiveNoun, String adj) {
        List<Tree> leaves;
        if ((inChild.value().equals("NN")) || (inChild.value().equals("NNS")) || (inChild.value().equals("NNP"))) {
            leaves = inChild.getLeaves();
            adjectiveNoun++;
            String className = "";
            if (adjectiveNoun == 1) {
                className = leaves.get(0).yieldWords().get(0).word();
                nounList.add(adj + " " + className);
            } else {
                attributeLists.add(leaves.get(0).yieldWords().get(0).word());
            }

        }
    }

    /**
     * method to stem the word (convert to base form) only if the word is not
     * having under score For example: saving_account
     *
     * @param identifiedWord
     * @return String - modified word
     */
    private String stemmingForAWord(String identifiedWord) {
        String word;
        if (identifiedWord.contains("_")) {
            word = identifiedWord;
        } else {
            word = morphology.stem(identifiedWord);
        }
        return word;
    }

    /**
     * method to identify the attributes using the tokenization
     *
     * @return ArrayList: arrayList of attributes
     */
    public ArrayList getAttributeList() {
        nounList = new ArrayList();
        attributeLists = new ArrayList();
        ArrayList adjAtt = new ArrayList();
        int separator = 0;
        List<Tree> leaves;
        String phraseNotation = "NP([<NNS|NN|NNP]![<JJ|VBG])!$VP";// !<VBG";//@" + phrase + "! << @" + phrase;

        TregexPattern VBpattern = TregexPattern.compile(phraseNotation);
        TregexMatcher matcher = VBpattern.matcher(sTree);

        while (matcher.findNextMatchingNode()) {
            Tree match = matcher.getMatch();
            Tree[] innerChild = match.children();
            int adjectiveExist = 0;
            String adj = "";
            String attribute = "";
            String b = "";

            if (innerChild.length > 1) {
                int count = 1;

                for (Tree inChild : innerChild) {
                    if (inChild.value().equals("CC") || inChild.value().equals(",")) {
                        separator = 1;
                    }
                    if ((inChild.value().equals("JJ")) || (inChild.value().equals("VBG"))) {
                        adjectiveExist++;
                        leaves = inChild.getLeaves();
                        adj = leaves.get(0).toString();
                        if (designEleList.contains(adj)) {
                            adj = "";
                        }
                    }
                    if ((inChild.value().equals("NN")) || (inChild.value().equals("NNS")) || (inChild.value().equals("NNP"))) {
                        leaves = inChild.getLeaves(); //leaves correspond to the tokens
                        if (count == 1) {
                            if (adjectiveExist == 1) {
                                attribute = adj + " " + leaves.get(0).yieldWords().get(0).word();
                            } else {
                                attribute = leaves.get(0).yieldWords().get(0).word();
                            }
                            if (!designEleList.contains(attribute)) {
                                String identifiedWord = attribute;
                                if (!identifiedWord.contains("_")) {
                                    attributeLists.add(morphology.stem(identifiedWord));
                                } else {
                                    attributeLists.add(identifiedWord);
                                }
                            }

                        } else if (count >= 2 && separator == 0) {
                            if (!attribute.contains("_")) {

                                attributeLists.remove(morphology.stem(attribute));
                                attributeLists.remove(attribute);
                            } else {
                                attributeLists.remove(attribute);
                            }

                            attribute += " " + (leaves.get(0).yieldWords()).get(0).word();
                            attributeLists.add(attribute);
                        } else if (count >= 2 && separator == 1) {
                            attribute = (leaves.get(0).yieldWords()).get(0).word();
                            if (!attribute.contains("_")) {
                                attributeLists.add(morphology.stem(attribute));
                            } else {
                                attributeLists.add(attribute);
                            }
                            separator = 0;
                        }
                        count++;
                    }
                }
            } else {
                for (Tree inChild : innerChild) {
                    if ((inChild.value().equals("NN")) || (inChild.value().equals("NNS"))) {
                        leaves = inChild.getLeaves(); //leaves correspond to the tokens
                        String identifiedWord = ((leaves.get(0).yieldWords()).get(0).word());
                        if (!identifiedWord.contains("_")) {
                            attributeLists.add(morphology.stem(identifiedWord));
                        } else {
                            attributeLists.add(identifiedWord);
                        }
                    }
                }
            }
        }
        adjAtt = getAdjectiveAttribute();
        if (!adjAtt.isEmpty()) {
            String att = "";
            for (int i = 0; i < adjAtt.size(); i++) {
                att = adjAtt.get(i).toString();
                if (!att.isEmpty() || !att.equals("") || !(att.equals(" "))) {
                    attributeLists.add(att.trim());
                }
            }
        }

        System.out.println("ATTRIBUTE LIST :" + attributeLists);
        return attributeLists;

    }

    /**
     * method to identify the attributes when the words which are identifies as
     * nouns are in adjective phrases
     *
     * @return ArrayList of adjective attributes
     */
    public ArrayList getAdjectiveAttribute() {
        adjAttributeList = new ArrayList();
        //adjAttributeList = new ArrayList();

        int adjectiveExist = 0;
        int adjectiveNoun = 0;
        int nnCount = 0;
        String adj = "";
        List<Tree> leaves;
        String phraseNotation = "NP[<NNS|NN]!$VP";//@" + phrase + "! << @" + phrase;
        DesignElementClass designEle = new DesignElementClass();
        ArrayList designEleList = designEle.getDesignElementsList();

        /*For single Tree  */
        TregexPattern VBpattern = TregexPattern.compile(phraseNotation);
        TregexMatcher matcher = VBpattern.matcher(sTree);

        while (matcher.findNextMatchingNode()) {
            Tree match = matcher.getMatch();
            Tree[] innerChild = match.children();
            String a = "";
            boolean separatorExist = false;
            if (innerChild.length > 1) {
                int count = 1;
                adjectiveExist = 0;
                adjectiveNoun = 0;
                nnCount = 0;
                String attribute = "";
                adj = "";

                for (Tree inChild : innerChild) {
                    //checks whether there are any separators
                    if (inChild.value().equals("CC")) {
                        separatorExist = true;
                        attribute = "";
                        adjectiveExist = 0;
                        adjectiveNoun = 0;
                    }
                    //checks whether there are adjectives
                    if ((inChild.value().equals("JJ")) || (inChild.value().equals("VBG"))) {
                        adjectiveExist++;
                        leaves = inChild.getLeaves();
                        adj = leaves.get(0).toString();
                        if (designEleList.contains(adj)) {
                            adj = "";
                        }

                    }
                    //if the adjective exist store the attributes
                    if (adjectiveExist == 1) {
                        adjectiveNoun = storeAdjectiveAttribute(inChild, adjectiveNoun, nnCount, adj);
                    }
                }
                if (adjectiveExist == 1 && adjectiveNoun == 0 && !adj.isEmpty()) {
                    adjAttributeList.add(stemmingForAWord(adj));

                }
            }
        }

        System.out.println("ADJECTVE ATTRIBUTE :" + adjAttributeList);
        return adjAttributeList;

    }

    /**
     * method to store the adjective attribute
     *
     * @param inChild
     * @param adjectiveNoun
     * @param nnCount
     * @param adj
     * @return int: number of adjective noun
     */
    private int storeAdjectiveAttribute(Tree inChild, int adjectiveNoun, int nnCount, String adj) {
        List<Tree> leaves;
        String attribute = "";
        if ((inChild.value().equals("NN")) || inChild.value().equals("NNS")) {
            leaves = inChild.getLeaves();
            adjectiveNoun++;
            nnCount++;
            if (adjectiveNoun == 1) {
                attribute = stemmingForAWord(leaves.get(0).yieldWords().get(0).word());
                attribute = adj + " " + stemmingForAWord(leaves.get(0).yieldWords().get(0).word());
                adjAttributeList.add(attribute);

            }
            if (adjectiveNoun > 1) {

                adjAttributeList.remove(morphology.stem(attribute));
                adjAttributeList.remove(attribute);
                attribute += " " + leaves.get(0).yieldWords().get(0).word();
                adjAttributeList.add(attribute);
            }
        }
        return adjectiveNoun;
    }

    /**
     * method to return the classes with the attribute for example: bank client
     *
     * @return hashMap
     */
    public HashMap getClassWithAttr() {
        return storingClassWithAttr;
    }

    /**
     * method to return the attributeList
     *
     * @return
     */
    public ArrayList getAttributeLists() {
        return attributeLists;
    }

}
