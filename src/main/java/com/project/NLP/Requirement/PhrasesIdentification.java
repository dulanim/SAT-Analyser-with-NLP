/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *..
 */
package com.project.NLP.Requirement;

import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.trees.EnglishGrammaticalRelations;
import static edu.stanford.nlp.trees.EnglishGrammaticalRelations.AUX_PASSIVE_MODIFIER;
import edu.stanford.nlp.trees.GrammaticalRelation;
import edu.stanford.nlp.trees.SemanticHeadFinder;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.WordStemmer;
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
    /*For Single Tree */
    private Tree sTree;
    private ArrayList phraseLists;
    private ArrayList attributeLists;
    private ArrayList nounList;
    private ArrayList adjClassList;
    private ArrayList adjAttributeList;
    private WordStemmer wordStemmer = new WordStemmer();
    private DesignElementClass designElement;
    private ArrayList designEleList;
    private GrammaticalRelation grammaticalRelation;
    

    //static StanfordCoreNLPModified stanford;

    PhrasesIdentification() {

    }

    PhrasesIdentification(Tree[] tree) {

        this.tree = tree;

    }

    /*For single tree */
    PhrasesIdentification(Tree tree) {

        this.sTree = tree;
        designElement = new DesignElementClass();
        designEleList = designElement.getDesignElementsList();

    }

    private int getTreeCount() {
        return tree.length;
    }

    /*method to get the negative characters from the sentence
    *input: none
    *output: list of words denoted by RB and CC in a sentence
    */
    public ArrayList NegativeSentenceDetection() {
        List<Tree> leaves;
        String phraseNotation = "RB|CC";//@" + phrase + "! << @" + phrase;
        TregexPattern VBpattern = TregexPattern.compile(phraseNotation);
        TregexMatcher matcher = VBpattern.matcher(sTree);
        ArrayList negativeLists = new ArrayList();
        while (matcher.findNextMatchingNode()) {
            Tree match = matcher.getMatch();
            System.out.println("\n--Matching Tree  " + match + "-------\n");
            Tree[] innerChild = match.children();
            System.out.println("innerChild length: " + innerChild.length);

            for (Tree inChild : innerChild) {
                negativeLists.add(inChild.getLeaves().get(0).yieldWords().get(0).word());

            }

        }
        return negativeLists;
    }

    /*method to check whethere a sentence is active voice or passive voice
    *input: none
    *output: boolean value
    */
    public boolean checkActiveOrPassive(){
        String phraseNotation = "RB|CC";//@" + phrase + "! << @" + phrase;
        TregexPattern VBpattern = TregexPattern.compile(phraseNotation);
        TregexMatcher matcher = VBpattern.matcher(sTree);
        
        System.out.println("passive : "+ AUX_PASSIVE_MODIFIER.toPrettyString());
        SemanticHeadFinder semanticHeadFinder = new SemanticHeadFinder();
        System.out.println("verbal auxilary: "+semanticHeadFinder.isVerbalAuxiliary(sTree));
        
        
        ArrayList negativeLists = new ArrayList();
        while (matcher.findNextMatchingNode()) {
            Tree match = matcher.getMatch();
            
        }
        return true;
    }
            
    
    public ArrayList getClassList() {
        nounList = new ArrayList();
        attributeLists = new ArrayList();
        int adjectiveExist = 0;
        int adjectiveNoun = 0;
        String adj = "";

        List<Tree> leaves;
        String phraseNotation = "(NP([<NNS|NN]$VP))";//@" + phrase + "! << @" + phrase;

        /*For the single Tree */
        //wordStemmer.visitTree(sTree);
        TregexPattern VBpattern = TregexPattern.compile(phraseNotation);
        TregexMatcher matcher = VBpattern.matcher(sTree);

        while (matcher.findNextMatchingNode()) {
            Tree match = matcher.getMatch();
            System.out.println("\n--Matching Tree  " + match + "-------\n");
            Tree[] innerChild = match.children();
            System.out.println("innerChild length: " + innerChild.length);
            adjectiveExist = 0;
            adjectiveNoun = 0;

            if (innerChild.length > 1) {
                int count = 1;
                for (Tree inChild : innerChild) {
                    if ((inChild.value().equals("JJ")) || (inChild.value().equals("VBG"))) {
                        adjectiveExist++;
                        leaves = inChild.getLeaves();
                        adj = leaves.get(0).yieldWords().get(0).word();
                        if (designEleList.contains(adj)) {
                            adj = "";
                        }
                    }
                    if (adjectiveExist == 1) {
                        if ((inChild.value().equals("NN"))) {
                            leaves = inChild.getLeaves();
                            adjectiveNoun++;
                            String className = "";
                            if (adjectiveNoun == 1) {
                                className = leaves.get(0).yieldWords().get(0).word();
                                System.out.println("added...." + className);
                                nounList.add(adj + " " + className);
                            }
                            /*if (adjectiveNoun > 1 && adjectiveNoun != inChild.size()) {
                                nounList.remove(className);
                                className += " " + leaves.get(0).yieldWords().get(0).word();
                                nounList.add(className);
                                System.out.println("added and removed ." + className);
                            }*/
                            else{
                                attributeLists.add(leaves.get(0).yieldWords().get(0).word());
                                System.out.println("attribute is added..");
                            }
                            
                        }
                    } else {
                        if ((inChild.value().equals("NN")) || (inChild.value().equals("NNS"))) {
                            leaves = inChild.getLeaves(); //leaves correspond to the tokens
                            System.out.println("leaves: " + leaves.size() + " value: " + leaves.get(0));
                            if (count != 1) {
                                attributeLists.add(((leaves.get(0).yieldWords()).get(0).word()));
                                System.out.println("count == inn");
                            } else {
                                nounList.add(((leaves.get(0).yieldWords()).get(0).word()));
                                System.out.println(">2 else");
                            }
                            count++;
                        }
                    }

                }
            } else {
                for (Tree inChild : innerChild) {
                    System.out.println("\n--innerChild  " + inChild + "-------\n");
                    if ((inChild.value().equals("NN")) || (inChild.value().equals("NNS")) || (inChild.value().equals("JJ"))) {
                        leaves = inChild.getLeaves(); //leaves correspond to the tokens
                        //phraseLists.add(leaves.get(0 ).yieldWords());
                        nounList.add(((leaves.get(0).yieldWords()).get(0).word()));
                        System.out.println("maingjshfkjs");
                    }

                }

            }

        }

        System.out.println("NOUN LIST :" + nounList);
        return nounList;

    }

    public ArrayList getAttributeList() {
        nounList = new ArrayList();
        attributeLists = new ArrayList();
        ArrayList adjAtt = new ArrayList();
        int separator = 0;
        List<Tree> leaves;
        String phraseNotation = "NP([<NNS|NN]![<JJ|VBG])!$VP";// !<VBG";//@" + phrase + "! << @" + phrase;

        /*For the Single Tree */
        //wordStemmer.visitTree(sTree);
        TregexPattern VBpattern = TregexPattern.compile(phraseNotation);
        TregexMatcher matcher = VBpattern.matcher(sTree);

        while (matcher.findNextMatchingNode()) {
            Tree match = matcher.getMatch();
            System.out.println("\n--Matching Tree  " + match + "-------\n");
            Tree[] innerChild = match.children();
            System.out.println("innerChild length: " + innerChild.length);

            String attribute = "";
            String b = "";

            if (innerChild.length > 1) {
                int count = 1;

                for (Tree inChild : innerChild) {
                    System.out.println("\n--innerChild  " + inChild + "-------\n");
                    if (inChild.value().equals("CC")) {
                        separator = 1;
                    }
                    if ((inChild.value().equals("NN")) || (inChild.value().equals("NNS"))) {
                        leaves = inChild.getLeaves(); //leaves correspond to the tokens
                        System.out.println("leaves: " + leaves.size() + " value: " + leaves.get(0));

                        if (count == 1) {
                            //attributeLists.add((leaves.get(0).yieldWords()));
                            attribute = leaves.get(0).yieldWords().get(0).word();

                            if (!designEleList.contains(attribute)) {
                                attributeLists.add(attribute);
                                System.out.println("count == inn");

                            }

                        } else if (count >= 2 && separator == 0) {
                            attributeLists.remove(attribute);
                            attribute += " " + (leaves.get(0).yieldWords()).get(0).word();
                            attributeLists.add(attribute);
                            System.out.println(">2 else");
                        } else if (count >= 2 && separator == 1) {
                            attribute = (leaves.get(0).yieldWords()).get(0).word();
                            attributeLists.add(attribute);
                        }

                        count++;
                    }
                }
            } else {
                for (Tree inChild : innerChild) {
                    System.out.println("\n--innerChild  " + inChild + "-------\n");

                    if ((inChild.value().equals("NN")) || (inChild.value().equals("NNS"))) {
                        leaves = inChild.getLeaves(); //leaves correspond to the tokens
                        //phraseLists.add(leaves.get(0 ).yieldWords());
                        attributeLists.add(((leaves.get(0).yieldWords()).get(0).word()));
                        System.out.println("maingjshfkjs");
                    }

                }

            }

        }

        adjAtt = getAdjectiveAttribute();

        if (!adjAtt.isEmpty()) {
            for (int i = 0; i < adjAtt.size(); i++) {
                attributeLists.add(adjAtt.get(i));
            }
        }

        System.out.println("Attribute LIST :" + attributeLists);
        return attributeLists;

    }

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
        //wordStemmer.visitTree(sTree);
        TregexPattern VBpattern = TregexPattern.compile(phraseNotation);
        TregexMatcher matcher = VBpattern.matcher(sTree);

        while (matcher.findNextMatchingNode()) {
            Tree match = matcher.getMatch();
            System.out.println("\n--Matching Tree  " + match + "-------\n");
            Tree[] innerChild = match.children();
            System.out.println("innerChild length: " + innerChild.length);
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
                    System.out.println("\n--innerChild  " + inChild + "-------\n");

                    //checks whether there are any separators
                    if (inChild.value().equals("CC")) {
                        separatorExist = true;
                        attribute = "";
                        adjectiveExist = 0;
                        adjectiveNoun = 0;
                    }
                    //if the phrase contatins NN followed by adjectives then NN will be considered as class
                    if ((inChild.value().equals("JJ")) || (inChild.value().equals("VBG"))) {
                        adjectiveExist++;
                        leaves = inChild.getLeaves();
                        System.out.println("LLLLLLLLEaves:" + leaves.size() + " " + leaves.get(0) + "  ..." + leaves);
                        adj = leaves.get(0).toString();
                        System.out.println("adj: " + adj);
                        if (designEleList.contains(adj)) {
                            adj = "";
                        }

                    }
                    if (adjectiveExist == 1) {
                        if (inChild.value().equals("NN")) {
                            leaves = inChild.getLeaves();
                            adjectiveNoun++;
                            nnCount++;
                            if (adjectiveNoun == 1) {
                                attribute = leaves.get(0).yieldWords().get(0).word();
                                System.out.println("added...." + attribute);
                                attribute = adj + " " + leaves.get(0).yieldWords().get(0).word();
                                //adjClassList.add(cl);
                                adjAttributeList.add(attribute);

                            }
                            if (adjectiveNoun > 1) {

                                //adjAttributeList.remove(adj + " " + cl);
                                adjAttributeList.remove(attribute);
                                attribute += " " + leaves.get(0).yieldWords().get(0).word();
                                adjAttributeList.add(attribute);
                                System.out.println("2 or more adjective nouns are added: " + attribute);
                            }

                        }
                    }

                }
                if (adjectiveExist == 1 && adjectiveNoun == 0) {
                    adjAttributeList.add(adj);
                }
            }

        }

        System.out.println("ADJECTVE ATTRIBUTE :" + adjAttributeList);
        return adjAttributeList;

    }

    public ArrayList getAdjectiveClass() {
        adjClassList = new ArrayList();
        //adjAttributeList = new ArrayList();

        int adjectiveExist = 0;
        int adjectiveNoun = 0;
        int nnCount = 0;
        String nn = "";
        String adj = "";
        List<Tree> leaves;
        String phraseNotation = "NP[<NNS|NN]!$VP";//@" + phrase + "! << @" + phrase;
        DesignElementClass designElement = new DesignElementClass();
        ArrayList designEleList = designElement.getDesignElementsList();
        for (Tree child : tree) {
            wordStemmer.visitTree(child);
            TregexPattern VBpattern = TregexPattern.compile(phraseNotation);
            TregexMatcher matcher = VBpattern.matcher((Tree) child);

            while (matcher.findNextMatchingNode()) {
                Tree match = matcher.getMatch();
                System.out.println("\n--Matching Tree  " + match + "-------\n");
                Tree[] innerChild = match.children();
                System.out.println("innerChild length: " + innerChild.length);
                String a = "";

                if (innerChild.length > 1) {
                    int count = 1;
                    adjectiveExist = 0;
                    adjectiveNoun = 0;
                    nnCount = 0;
                    String cl = "";
                    adj = "";
                    for (Tree inChild : innerChild) {
                        System.out.println("\n--innerChild  " + inChild + "-------\n");

                        //if the phrase contatins NN followed by adjectives then NN will be considered as class
                        if (inChild.value().equals("JJ")) {
                            adjectiveExist++;
                            if (!designEleList.contains(adj)) {
                                leaves = inChild.getLeaves();
                                System.out.println("LLLLLLLLEaves:" + leaves.size() + " " + leaves.get(0) + "  ..." + leaves);
                                adj = leaves.get(0).toString();
                                System.out.println("adj: " + adj);

                            }
                        }
                        if (adjectiveExist == 1) {
                            if (inChild.value().equals("NN")) {
                                leaves = inChild.getLeaves();
                                adjectiveNoun++;
                                nnCount++;
                                if (adjectiveNoun == 1) {
                                    //nn = cl = leaves.get(0).yieldWords().get(0).word();
                                    adj += " " + leaves.get(0).yieldWords().get(0).word();
                                    adjClassList.add(adj);
                                    cl = adj;
                                    System.out.println("added...." + adj);

                                    //adjClassList.add(cl);
                                    //adjAttributeList.add(adj+" "+cl);
                                }
                                if (adjectiveNoun > 1) {

                                    adjClassList.remove(cl);
                                    cl += " " + leaves.get(0).yieldWords().get(0).word();
                                    adjClassList.add(cl);
                                    System.out.println("added and removed ." + cl);
                                }
                            }
                        }
                        if (nnCount == 1) {
                            // adjClassList.remove(nn);
                            //nounList.add(nn);
                        }

                    }
                }

            }

        }
        System.out.println("ADJECTVE CLASSSSSSSS :" + nounList);
        return adjClassList;

    }

    public ArrayList getIdentifiedVPPhrases2(String phrase) {
        phraseLists = new ArrayList();
        attributeLists = new ArrayList();
        List<Tree> leaves = null;
        String phraseNotation = "NP[<NNS|NN]!$VP";//"@" + phrase;

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
        System.out.println("ATTRIBUTES>>>>" + attributeLists);
        return attributeLists;
    }

    public ArrayList getClass(String phrase) {
        nounList = new ArrayList();
        List<Tree> leaves = null;
        String phraseNotation = "NP[<NNS|NN]$VP";//..(SBAR<S))";//"NP[<,NNS|NN]!$IN!$TO..PP..TO";//"NP[<NNS|NN]!$IN!$TO!$PP..PP..SBAR..PRN";///!$VB!<PP ";//NP[<NNS|NN]..PP// "(PP[<<(NP[<NNS|NN])!<TO])";//"(PP[<<(NP[<NNS|NN])!<TO ])";//"(PP[<(NP[<NNS|NN])!<TO])";  //NP$(NP[<NNS|NN])
        //"(NP[<NNS|NN])..PP"; to remove NP before PP

        for (Tree child : tree) {

            TregexPattern NPpattern = TregexPattern.compile(phraseNotation);
            TregexMatcher matcher = NPpattern.matcher((Tree) child);
            int count = 0;
            int indexOfClass = 0;
            while (matcher.findNextMatchingNode()) {
                Tree match = matcher.getMatch();
                System.out.println("..............." + match.firstChild() + " ...." + match.size() + "...." + match.lastChild() + "....");
                Tree[] arrayNouns = match.children();
                /*if the NP contains more children get the first NN within the NP and add it to class list*/
                if (match.numChildren() >= 2) {
                    System.out.println(">= 2 children");
                    count = 0;
                    indexOfClass = 0;
                    int i = 0;
                    for (Tree arrayNoun : arrayNouns) {

                        if (arrayNoun.value().equals("NN")) {
                            System.out.println(count + "  " + arrayNouns[count]);
                            count++;
                            if (count == 1) {
                                indexOfClass = count;
                                nounList.add(arrayNouns[i].toString());
                            }
                        }
                        i++;
                    }
                    //Tree firstNN= match.firstChild();
                    //if(indexOfClass!=0)
                    //nounList.add(arrayNouns[indexOfClass].toString());

                } else {
                    nounList.add(Sentence.listToString(match.yield()).toString());
                }
                System.out.print("\n---NOun pharse----" + match + "----\n");
                System.out.println("NOUN LIST: " + nounList);

            }
        }

        return nounList;

    }

    public ArrayList getAttribute() {
        nounList = new ArrayList();
        List<Tree> leaves = null;
        String phraseNotation = "NP[<NNS|NN]!$VP";//..(SBAR<S))";//"NP[<,NNS|NN]!$IN!$TO..PP..TO";//"NP[<NNS|NN]!$IN!$TO!$PP..PP..SBAR..PRN";///!$VB!<PP ";//NP[<NNS|NN]..PP// "(PP[<<(NP[<NNS|NN])!<TO])";//"(PP[<<(NP[<NNS|NN])!<TO ])";//"(PP[<(NP[<NNS|NN])!<TO])";  //NP$(NP[<NNS|NN])
        //"(NP[<NNS|NN])..PP"; to remove NP before PP
        System.out.println("ATTRIBUTES.........");
        for (Tree child : tree) {

            TregexPattern NPpattern = TregexPattern.compile(phraseNotation);
            TregexMatcher matcher = NPpattern.matcher((Tree) child);
            int count = 0;
            int indexOfAttribute = 0;
            while (matcher.findNextMatchingNode()) {
                Tree match = matcher.getMatch();

                System.out.println("..............." + match.firstChild() + " ...." + match.size() + "...." + match.lastChild() + "....");
                Tree[] arrayNouns = match.children();
                /*if the NP contains more children get the first NN within the NP and add it to class list*/
                if (match.numChildren() >= 2) {
                    System.out.println(">= 2 children");
                    count = 0;
                    indexOfAttribute = 0;
                    int i = 0;
                    for (Tree arrayNoun : arrayNouns) {

                        if ((arrayNoun.value().equals("NN")) || arrayNoun.value().equals("NNS")) {
                            System.out.println(count + "  " + arrayNouns[count]);
                            count++;
                            //if(count>=1){
                            indexOfAttribute = i;
                            //  attributeLists.add(arrayNouns[i].toString());
                            //}

                        }
                        i++;
                    }

                    if (count >= 1) {
                        attributeLists.add(arrayNouns[i - 1].toString());
                    }

                } else {
                    attributeLists.add(Sentence.listToString(match.yield()).toString());
                }
                System.out.print("\n---attribute pharse----" + match + "----\n");
                System.out.println("Attribute LIST: " + attributeLists);

            }
        }

        return attributeLists;

    }
    /*method to identify the phrases in the tree without eliminating redundancy
     parameters are the POS.
     for example: NP, VB, NNP and etc.*/

    public ArrayList getIdentifiedPhrases1(String phrase) {
        phraseLists = new ArrayList();
        String phraseNotation = phrase;

        /*For single Tree */
        TregexPattern NPpattern = TregexPattern.compile(phraseNotation);
        TregexMatcher matcher = NPpattern.matcher(sTree);
        while (matcher.findNextMatchingNode()) {
            Tree match = matcher.getMatch();
            //System.out.println("sdf  " + Sentence.listToString(match.yield()));
            phraseLists.add(Sentence.listToString(match.yield()));
        }
        return phraseLists;
    }

    public ArrayList getAttributeLists() {
        return attributeLists;
    }

}
