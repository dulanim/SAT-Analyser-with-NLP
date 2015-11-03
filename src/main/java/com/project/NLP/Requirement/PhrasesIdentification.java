/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *..
 */
package com.project.NLP.Requirement;

import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author S. Shobiga
 */
public class PhrasesIdentification {

    private String text;
    private Tree[] tree;
    private ArrayList tree1;
    private ArrayList phraseLists;
    private ArrayList attributeLists;
    private ArrayList nounList;

    static StanfordCoreNLPModified stanford;

    PhrasesIdentification() {

    }

    PhrasesIdentification(String text) {
        this.text = text;
        stanford = new StanfordCoreNLPModified(text);

        if (tree == null) {
            tree = stanford.generateTreeAnnotation();
        }

    }

    PhrasesIdentification(Tree[] tree) {

        this.tree = tree;

    }

    private int getTreeCount() {
        return tree.length;
    }

    /*method to identify the phrases in the tree without eliminating redundancy
     parameters are the POS.
     for example: NP, VB, NNP and etc.*/
    public ArrayList getIdentifiedPhrases1(String phrase) {
        phraseLists = new ArrayList();
        String phraseNotation = phrase;

        for (Tree child : tree) {
            TregexPattern NPpattern = TregexPattern.compile(phraseNotation);
            TregexMatcher matcher = NPpattern.matcher((Tree) child);
            while (matcher.findNextMatchingNode()) {
                Tree match = matcher.getMatch();
                //System.out.println("sdf  " + Sentence.listToString(match.yield()));
                phraseLists.add(Sentence.listToString(match.yield()));
            }
        }
        return phraseLists;
    }

    public ArrayList getIdentifiedPhrases(String phrase) {
        phraseLists = new ArrayList();
        attributeLists = new ArrayList();
        List<Tree> leaves;
        String phraseNotation = "@" + phrase + "! << @" + phrase;

        for (Tree child : tree) {

            TregexPattern VBpattern = TregexPattern.compile(phraseNotation);
            TregexMatcher matcher = VBpattern.matcher((Tree) child);

            while (matcher.findNextMatchingNode()) {
                Tree match = matcher.getMatch();
                System.out.println("\n--Matching Tree  " + match + "-------\n");
                Tree[] innerChild = match.children();
                System.out.println("innerChild length: " + innerChild.length);

                if (innerChild.length > 1) {
                    int count = 1;
                    for (Tree inChild : innerChild) {
                        System.out.println("\n--innerChild  " + inChild + "-------\n");

                        if (inChild.value().equals("NN")) {
                            leaves = inChild.getLeaves(); //leaves correspond to the tokens
                            System.out.println("leaves: " + leaves.size() + " value: " + leaves.get(0));
                            if (count != 1) {
                                //attributeLists.add((leaves.get(0).yieldWords()));
                                attributeLists.add(((leaves.get(0).yieldWords()).get(0).word()));
                                System.out.println("count == inn");
                            } else {
                                phraseLists.add(((leaves.get(0).yieldWords()).get(0).word()));
                                System.out.println(">2 else");
                            }
                            count++;
                        }

                    }
                } else {
                    for (Tree inChild : innerChild) {
                        System.out.println("\n--innerChild  " + inChild + "-------\n");

                        if (inChild.value().equals("NN")) {
                            leaves = inChild.getLeaves(); //leaves correspond to the tokens
                            //phraseLists.add(leaves.get(0 ).yieldWords());
                            phraseLists.add(((leaves.get(0).yieldWords()).get(0).word()));
                            System.out.println("maingjshfkjs");
                        }

                    }

                }

            }

        }
        return phraseLists;

    }

    public ArrayList getAttributeLists() {
        return attributeLists;
    }

    /*NN identification followed by VP */
    /*NN identification followed by VP */
    public ArrayList getIdentifiedVPPhrases(String phrase) {
        phraseLists = new ArrayList();
        attributeLists = new ArrayList();
        List<Tree> leaves = null;
        String phraseNotation = "@" + phrase;

        for (Tree child : tree) {

            TregexPattern VBpattern = TregexPattern.compile(phraseNotation);
            TregexMatcher matcher = VBpattern.matcher((Tree) child);

            while (matcher.findNextMatchingNode()) {
                Tree match = matcher.getMatch();
                //System.out.println("\n--Matching Tree  " + match + "-------\n");
                Tree[] innerChild = match.children();
                //System.out.println("innerChild length: " + innerChild.length);
                int count = 0;// to count whether 2 NN phrases are coming
                String a = null;
                boolean isVBThere = false;
                boolean isNPThere = false;
                for (Tree inChild : innerChild) {
                    //System.out.println("\n--innerChild  " + inChild + "-------\n");
                    if (inChild.value().equals("VB") || inChild.value().equals("VBG")) {
                        isVBThere = true;
                    }
                }
                if (isVBThere == true) {
                    for (Tree inChild : innerChild) {
                        if (inChild.value().equals("NP")) {
                            isNPThere = true;
                            Tree[] lea = inChild.children();

                            for (Tree l : lea) {
                                //System.out.println(leaf +" Leaf " + leaf.value().equals("NP") + leaf.value().equals("PP") + leaf.value().equals("NN"));
                                if (l.value().equals("NN")) {
                                    count++;

                                    if (count == 1) {
                                        a = l.yieldWords().get(0).word();
                                        attributeLists.add(a);
                                        System.out.println("new added.... :" + a);
                                    }
                                    if (count >= 2) {
                                        attributeLists.remove(a);
                                        System.out.println("removed " + a);
                                    }
                                }

                            }
                            // if(count >=2){ //only prints if there is no sentence like "bank application"
                            System.out.println("attributeNP :" + a);

                        }
                    }
                }
                if (isNPThere == true) {
                    //System.out.println("***************** " + leaves);

                }
                isNPThere = false;
                if (isVBThere == true) {
                    for (Tree inChild : innerChild) {
                        if (inChild.value().equals("PP")) {
                            isNPThere = true;
                            Tree[] lea = inChild.children();
                            for (Tree l : lea) {
                                if (l.value().equals("NP")) {
                                    System.out.println("attributePP :" + l.yieldWords());
                                }

                            }
                            //System.out.println( "           "+ lea[0].value().equals("DT"));

                        }
                    }
                }
                if (isNPThere == true) {
                    //System.out.println("***************** " + leaves);
                }
            }
        }
        return attributeLists;
    }

    public ArrayList getClass(String phrase) {
        nounList = new ArrayList();
        List<Tree> leaves = null;
        String phraseNotation ="NP[<NNS|NN]..PP";// "(PP[<<(NP[<NNS|NN])!<TO])";//"(PP[<<(NP[<NNS|NN])!<TO ])";//"(PP[<(NP[<NNS|NN])!<TO])";  //NP$(NP[<NNS|NN])
        //"(NP[<NNS|NN])..PP"; to remove NP before PP
        System.out.println("Noun 2 ....");
        for (Tree child : tree) {

            TregexPattern NPpattern = TregexPattern.compile(phraseNotation);
            TregexMatcher matcher = NPpattern.matcher((Tree) child);
            while (matcher.findNextMatchingNode()) {
                Tree match = matcher.getMatch();
                nounList.add(Sentence.listToString(match.yield()));
                System.out.print("\n---NOun pharse----"+match+"----\n");
                System.out.println("NOUN LIST: "+nounList);
                
            }
        }
        
        
        return nounList;

    }
    
    public ArrayList getAttribute(String phrase){
        attributeLists = new ArrayList();
        List<Tree> leaves = null;
        String phraseNotation = "NP[<NNS|NN]";//(PP[<<(NP[<NNS|NN])!<TO])";//"(PP[<<(NP[<NNS|NN])!<TO ])";//"(PP[<(NP[<NNS|NN])!<TO])";  //NP$(NP[<NNS|NN])
        //"(NP[<NNS|NN])..PP"; to remove NP before PP
        System.out.println("....");
        for (Tree child : tree) {
            
            TregexPattern NPpattern = TregexPattern.compile(phraseNotation);
            TregexMatcher matcher = NPpattern.matcher((Tree) child);
            while (matcher.findNextMatchingNode()) {
                Tree match = matcher.getMatch();
                attributeLists.add(Sentence.listToString(match.yield()));
                System.out.print("\n---attributes----"+match+"----\n");
                System.out.println("Attribute LIST: "+attributeLists);
                
            }
        }
        
        
        return attributeLists;

    }
}