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
    private ArrayList adjClassList;
    private ArrayList adjAttributeList;
    
    static StanfordCoreNLPModified stanford;

    PhrasesIdentification() {

    }

    PhrasesIdentification(String text) {
        this.text = text;
        System.out.println("######################" + text);
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
    
   
        public ArrayList getIdentifiedPhrases(String phrase) {
        nounList = new ArrayList();
        attributeLists = new ArrayList();
        int VBGExist =0;
        String vbg;
        List<Tree> leaves;
        String phraseNotation = "NP([<NNS|NN]$VP)";//@" + phrase + "! << @" + phrase;

        for (Tree child : tree) {

            TregexPattern VBpattern = TregexPattern.compile(phraseNotation);
            TregexMatcher matcher = VBpattern.matcher((Tree) child);

            while (matcher.findNextMatchingNode()) {
                Tree match = matcher.getMatch();
                System.out.println("\n--Matching Tree  " + match + "-------\n");
                Tree[] innerChild = match.children();
                System.out.println("innerChild length: " + innerChild.length);
                VBGExist=0;
                vbg ="";
                if (innerChild.length > 1) {
                    int count = 1;
                    for (Tree inChild : innerChild) {
                        System.out.println("\n--innerChild  " + inChild + "-------\n");

                        
                        
                        if ((inChild.value().equals("NN")) || (inChild.value().equals("NNS"))) {
                            leaves = inChild.getLeaves(); //leaves correspond to the tokens
                            System.out.println("leaves: " + leaves.size() + " value: " + leaves.get(0));
                            if (count != 1) {
                                //attributeLists.add((leaves.get(0).yieldWords()));
                                attributeLists.add(((leaves.get(0).yieldWords()).get(0).word()));
                                System.out.println("count == inn");
                            } else {
                                
                                nounList.add(((leaves.get(0).yieldWords()).get(0).word()));
                                System.out.println(">2 else");
                            }
                            count++;
                        }
                        
                    
                    }
                } else {
                    for (Tree inChild : innerChild) {
                        System.out.println("\n--innerChild  " + inChild + "-------\n");

                        if ((inChild.value().equals("NN")) || (inChild.value().equals("NNS")) || (inChild.value().equals("JJ")) ) {
                            leaves = inChild.getLeaves(); //leaves correspond to the tokens
                            //phraseLists.add(leaves.get(0 ).yieldWords());
                            nounList.add(((leaves.get(0).yieldWords()).get(0).word()));
                            System.out.println("maingjshfkjs");
                        }

                    }

                }

            }

        }
        ArrayList clList = getAdjectiveClass();
        if(!clList.isEmpty()){
            for(int i=0;i<clList.size();i++){
                nounList.add(clList.get(i));
            }
        }
        
        System.out.println("NOUN LIST :"+nounList);
        return nounList;

    }

        public ArrayList getIdentifiedVPPhrases(String phrase) {
        nounList = new ArrayList();
        attributeLists = new ArrayList();
        int adjectiveExist =0;
        int adjectiveNoun =0;
        int separator =0;
        List<Tree> leaves;
        String phraseNotation = "NP[<NNS|NN]!$VP";// !<VBG";//@" + phrase + "! << @" + phrase;

        for (Tree child : tree) {

            TregexPattern VBpattern = TregexPattern.compile(phraseNotation);
            TregexMatcher matcher = VBpattern.matcher((Tree) child);

            while (matcher.findNextMatchingNode()) {
                Tree match = matcher.getMatch();
                System.out.println("\n--Matching Tree  " + match + "-------\n");
                Tree[] innerChild = match.children();
                System.out.println("innerChild length: " + innerChild.length);
                String a="";
                
                if (innerChild.length > 1) {
                    int count = 1;
                    adjectiveExist=0;
                    adjectiveNoun=0;
                    for (Tree inChild : innerChild) {
                        System.out.println("\n--innerChild  " + inChild + "-------\n");
                        if(inChild.value().equals("CC")){
                                separator =1;
                        }
                        if ((inChild.value().equals("NN"))||(inChild.value().equals("NNS"))) {
                            leaves = inChild.getLeaves(); //leaves correspond to the tokens
                            System.out.println("leaves: " + leaves.size() + " value: " + leaves.get(0));
                            
                            if (count == 1) {
                                //attributeLists.add((leaves.get(0).yieldWords()));
                                a = leaves.get(0).yieldWords().get(0).word();
                                attributeLists.add(a);
                                System.out.println("count == inn");
                            } else if(count >=2 && separator ==0) {
                                attributeLists.remove(a);
                                a+=" "+(leaves.get(0).yieldWords()).get(0).word();
                                attributeLists.add(a);
                                System.out.println(">2 else");
                            }else if (count >=2 && separator==1){
                                a = (leaves.get(0).yieldWords()).get(0).word();
                                attributeLists.add(a);
                            }
                            
                            count++;
                        }
                        
                        //if the phrase contatins NN followed by adjectives then NN will be considered as class
                        if(inChild.value().equals("JJ") ){
                            adjectiveExist++;
                            leaves=inChild.getLeaves();
                            System.out.println("LLLLLLLLEaves:"+leaves.size()+" "+leaves.get(0)+"  ..."+leaves);
                            
                        }
                        if(adjectiveExist ==1){
                            if(inChild.value().equals("NN")){
                                leaves=inChild.getLeaves();
                                adjectiveNoun++;
                                String cl ="";
                                if(adjectiveNoun ==1){
                                    cl = leaves.get(0).yieldWords().get(0).word();
                                    System.out.println("added...."+cl);
                                    nounList.add(cl);
                                }
                                if(adjectiveNoun>1){
                                    nounList.remove(cl);
                                    cl+=" "+leaves.get(0).yieldWords().get(0).word();
                                    nounList.add(cl);
                                    System.out.println("added and removed ."+cl);
                                }
                            }
                        }

                    }
                } else {
                    for (Tree inChild : innerChild) {
                        System.out.println("\n--innerChild  " + inChild + "-------\n");

                        if ((inChild.value().equals("NN"))||(inChild.value().equals("NNS"))) {
                            leaves = inChild.getLeaves(); //leaves correspond to the tokens
                            //phraseLists.add(leaves.get(0 ).yieldWords());
                            attributeLists.add(((leaves.get(0).yieldWords()).get(0).word()));
                            System.out.println("maingjshfkjs");
                        }

                    }

                }

            }

        }
       /* ArrayList adjAtt = getAdjectiveAttribute();
        
        if(!adjAtt.isEmpty()){
            for(int i=0;i<adjAtt.size();i++){
                attributeLists.add(adjAtt.get(i));
            }
        }
        */
        System.out.println("Attribute LIST :"+attributeLists);
        return attributeLists;

    }

        
        
    public ArrayList getAdjectiveClass(){
        adjClassList = new ArrayList();
        //adjAttributeList = new ArrayList();
        
        int adjectiveExist =0;
        int adjectiveNoun =0;
        int nnCount =0;
        String nn="";
        String adj="";
        List<Tree> leaves;
        String phraseNotation = "NP[<NNS|NN]!$VP";//@" + phrase + "! << @" + phrase;

        for (Tree child : tree) {

            TregexPattern VBpattern = TregexPattern.compile(phraseNotation);
            TregexMatcher matcher = VBpattern.matcher((Tree) child);

            while (matcher.findNextMatchingNode()) {
                Tree match = matcher.getMatch();
                System.out.println("\n--Matching Tree  " + match + "-------\n");
                Tree[] innerChild = match.children();
                System.out.println("innerChild length: " + innerChild.length);
                String a="";
                
                if (innerChild.length > 1) {
                    int count = 1;
                    adjectiveExist=0;
                    adjectiveNoun=0;
                    nnCount =0;
                    String cl ="";
                    adj="";
                    for (Tree inChild : innerChild) {
                        System.out.println("\n--innerChild  " + inChild + "-------\n");

                        //if the phrase contatins NN followed by adjectives then NN will be considered as class
                        if(inChild.value().equals("JJ") ){
                            adjectiveExist++;
                            leaves=inChild.getLeaves();
                            System.out.println("LLLLLLLLEaves:"+leaves.size()+" "+leaves.get(0)+"  ..."+leaves);
                            adj=leaves.get(0).toString();
                            System.out.println("adj: "+adj);
                        }
                        if(adjectiveExist ==1){
                            if(inChild.value().equals("NN")){
                                leaves=inChild.getLeaves();
                                adjectiveNoun++;
                                nnCount++;
                                if(adjectiveNoun ==1){
                                    nn=cl = leaves.get(0).yieldWords().get(0).word();
                                    adj +=" "+ leaves.get(0).yieldWords().get(0).word();
                                    adjClassList.add(adj);
                                    System.out.println("added...."+adj);
                                    
                                    //adjClassList.add(cl);
                                    //adjAttributeList.add(adj+" "+cl);
                                    
                                }
                                if(adjectiveNoun>1){
                                   
                                    //adjClassList.remove(cl);
                                    cl+=" "+leaves.get(0).yieldWords().get(0).word();
                                    //adjClassList.add(cl);
                                    System.out.println("added and removed ."+cl);
                                }
                            }
                        }
                        if(nnCount ==1 ){
                           // adjClassList.remove(nn);
                            //nounList.add(nn);
                        }

                    }
                } 

                

            }

        }
        System.out.println("ADJECTVE CLASSSSSSSS :"+nounList);
        return adjClassList;

        }
        
            
    public ArrayList getAdjectiveAttribute(){
        adjAttributeList = new ArrayList();
        //adjAttributeList = new ArrayList();
        
        int adjectiveExist =0;
        int adjectiveNoun =0;
        int nnCount =0;
        String nn="";
        String adj="";
        List<Tree> leaves;
        String phraseNotation = "NP[<NNS|NN]!$VP";//@" + phrase + "! << @" + phrase;

        for (Tree child : tree) {

            TregexPattern VBpattern = TregexPattern.compile(phraseNotation);
            TregexMatcher matcher = VBpattern.matcher((Tree) child);

            while (matcher.findNextMatchingNode()) {
                Tree match = matcher.getMatch();
                System.out.println("\n--Matching Tree  " + match + "-------\n");
                Tree[] innerChild = match.children();
                System.out.println("innerChild length: " + innerChild.length);
                String a="";
                
                if (innerChild.length > 1) {
                    int count = 1;
                    adjectiveExist=0;
                    adjectiveNoun=0;
                    nnCount =0;
                    String cl ="";
                    adj="";
                    for (Tree inChild : innerChild) {
                        System.out.println("\n--innerChild  " + inChild + "-------\n");

                        //if the phrase contatins NN followed by adjectives then NN will be considered as class
                        if(inChild.value().equals("JJ") ){
                            adjectiveExist++;
                            leaves=inChild.getLeaves();
                            System.out.println("LLLLLLLLEaves:"+leaves.size()+" "+leaves.get(0)+"  ..."+leaves);
                            adj=leaves.get(0).toString();
                            System.out.println("adj: "+adj);
                        }
                        if(adjectiveExist ==1){
                            if(inChild.value().equals("NN")){
                                leaves=inChild.getLeaves();
                                adjectiveNoun++;
                                nnCount++;
                                if(adjectiveNoun ==1){
                                    nn=cl = leaves.get(0).yieldWords().get(0).word();
                                    System.out.println("added...."+cl);
                                    //adjClassList.add(cl);
                                    adjAttributeList.add(adj+" "+cl);
                                    
                                }
                                if(adjectiveNoun>1){
                                   
                                    adjAttributeList.remove(adj+" "+cl);
                                    
                                }
                                
                            }
                        }
                        
                    }
                } 

                

            }

        }
        System.out.println("ADJECTVE ATTRIBUTE :"+adjAttributeList);
        return adjAttributeList;

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
        System.out.println("ATTRIBUTES>>>>"+attributeLists);
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
            int count =0;
            int indexOfClass=0;
            while (matcher.findNextMatchingNode()) {
                Tree match = matcher.getMatch();
                System.out.println("..............."+match.firstChild() + " ...."+match.size()+"...."+match.lastChild()+"....");
                Tree[] arrayNouns = match.children();
                /*if the NP contains more children get the first NN within the NP and add it to class list*/
                if(match.numChildren() >=2){
                    System.out.println(">= 2 children");
                    count =0;indexOfClass=0;
                    int i=0;
                    for(Tree arrayNoun :arrayNouns){
                        
                        if(arrayNoun.value().equals("NN")){
                            System.out.println(count+"  "+arrayNouns[count]);
                            count++;
                            if(count==1){
                                indexOfClass=count;
                                nounList.add(arrayNouns[i].toString());
                            }
                        }
                        i++;
                    }
                    //Tree firstNN= match.firstChild();
                    //if(indexOfClass!=0)
                    //nounList.add(arrayNouns[indexOfClass].toString());
                    
                    
                    
                }
                else{
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
            int count =0;
            int indexOfAttribute=0;
            while (matcher.findNextMatchingNode()) {
                Tree match = matcher.getMatch();
                
                System.out.println("..............."+match.firstChild() + " ...."+match.size()+"...."+match.lastChild()+"....");
                Tree[] arrayNouns = match.children();
                /*if the NP contains more children get the first NN within the NP and add it to class list*/
                if(match.numChildren() >=2){
                    System.out.println(">= 2 children");
                    count =0;indexOfAttribute=0;
                    int i=0;
                    for(Tree arrayNoun :arrayNouns){
                        
                        if((arrayNoun.value().equals("NN"))||arrayNoun.value().equals("NNS")){
                            System.out.println(count+"  "+arrayNouns[count]);
                            count++;
                            //if(count>=1){
                                indexOfAttribute=i;
                              //  attributeLists.add(arrayNouns[i].toString());
                            //}
                            
                        }
                        i++;
                    }
                    
                    if(count>=1){
                        attributeLists.add(arrayNouns[i-1].toString());
                    }
                    //Tree firstNN= match.firstChild();
                    //if(indexOfClass!=0)
                    //nounList.add(arrayNouns[indexOfClass].toString());
                    
                    
                    
                }
                else{
                attributeLists.add(Sentence.listToString(match.yield()).toString());
                }
                System.out.print("\n---attribute pharse----" + match + "----\n");
                System.out.println("Attribute LIST: " + attributeLists);

            }
        }

        return attributeLists;

    }
  
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
/*
    public ArrayList getClass(String phrase) {
        nounList = new ArrayList();
        List<Tree> leaves = null;
        String phraseNotation = "(NP[<,NNS|NN]!$IN!$TO..PP[!>(VP$TO)])";//..(SBAR<S))";//"NP[<,NNS|NN]!$IN!$TO..PP..TO";//"NP[<NNS|NN]!$IN!$TO!$PP..PP..SBAR..PRN";///!$VB!<PP ";//NP[<NNS|NN]..PP// "(PP[<<(NP[<NNS|NN])!<TO])";//"(PP[<<(NP[<NNS|NN])!<TO ])";//"(PP[<(NP[<NNS|NN])!<TO])";  //NP$(NP[<NNS|NN])
        //"(NP[<NNS|NN])..PP"; to remove NP before PP
        System.out.println("Noun 2 ....");
        for (Tree child : tree) {

            TregexPattern NPpattern = TregexPattern.compile(phraseNotation);
            TregexMatcher matcher = NPpattern.matcher((Tree) child);
            while (matcher.findNextMatchingNode()) {
                Tree match = matcher.getMatch();
                System.out.println("..............."+match.firstChild() + " ...."+match.size()+"...."+match.lastChild()+"....");
                if(match.numChildren() ==2){
                    
                    Tree firstNN= match.firstChild();
                    nounList.add(Sentence.listToString(firstNN.yield()));
                    
                    
                }
                else{
                nounList.add(Sentence.listToString(match.yield()).toString());
                }
                System.out.print("\n---NOun pharse----" + match + "----\n");
                System.out.println("NOUN LIST: " + nounList);

            }
        }

        return nounList;

    }
*/
/*    public ArrayList getAttribute() {
        attributeLists = new ArrayList();
        ArrayList tempAtt = new ArrayList();
        ArrayList temp;
        String attribute = "";

        List<Tree> leaves = null;
        String phraseNotation = "(NP[<NN|NNP|NNS])!<S";//!$TO";//(NP[<(NNS|NN|NNP)<(NN$NN)]  )";//(PP[<<(NP[<NNS|NN])!<TO])";//"(PP[<<(NP[<NNS|NN])!<TO ])";//"(PP[<(NP[<NNS|NN])!<TO])";  //NP$(NP[<NNS|NN])
        //"(NP[<NNS|NN])..PP"; to remove NP before PP
        for (Tree child : tree) {
            checkTregexPattern(phraseNotation, child, tempAtt);

        }

        //eliminating the DT and other phrases expcept NN, NNP, NNS
        if (!tempAtt.isEmpty()) {
            for (int i = 0; i < tempAtt.size(); i++) {
                //set the tree to null before calling innerPhraseChecking()
                tree = null;
                innerPhrasesChecking(tempAtt.get(i).toString());

                //if a noun phrase contains 2 nouns add those to the attribute list or if it is one noun phrase add it to the list
                phraseNotation = "(NN$NN)|NN"; //(NN$NN)|NN) "(NP[<NN|(NN$NN)])
                temp = new ArrayList();
                for (Tree ch : tree) {
                    checkTregexPattern(phraseNotation, ch, temp);

                }
                for (int j = 0; j < temp.size(); j++) {

                    attribute += temp.get(j).toString() + " ";
                }
                temp = null;
                if (!attribute.isEmpty()) {
                    attributeLists.add(attribute);
                    attribute = "";
                }

                //single NN phrases
                //phraseNotation = "";
            }

        }

        //if the noun phrase is the sister of TO, and two NN are there, get the last NN
        // System.out.println("Text: "+text);
        // innerPhrasesChecking(text);
        // phraseNotation = "(TO$NP)<-NN";//(NP[<(NNS|NN|NNP)<(NN$NN)]  )";//(PP[<<(NP[<NNS|NN])!<TO])";//"(PP[<<(NP[<NNS|NN])!<TO ])";//"(PP[<(NP[<NNS|NN])!<TO])";  //NP$(NP[<NNS|NN])
        //"(NP[<NNS|NN])..PP"; to remove NP before PP
        /*for (Tree child : tree) {
         System.out.println("------------------------");
         checkTregexPattern(phraseNotation, child, tempAtt);

         }

         */
/*        return attributeLists;

    }
*/
    private void checkTregexPattern(String phraseNotation, Tree child, ArrayList list) {
        TregexPattern NPpattern = TregexPattern.compile(phraseNotation);
        TregexMatcher matcher = NPpattern.matcher((Tree) child);
        while (matcher.findNextMatchingNode()) {
            Tree match = matcher.getMatch();
            list.add(Sentence.listToString(match.yield()).toString());

            System.out.print("\n---attributes----" + match + "----\n");
            System.out.println("Attribute LIST: " + list);

        }
    }

    private void innerPhrasesChecking(String t) {
        System.out.println(t);
        stanford = new StanfordCoreNLPModified(t);

        if (tree == null) {
            tree = stanford.generateTreeAnnotation();
        }

    }

    private void innerPhrasesIdentification(Tree t) {
        this.tree = tree;

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
/*
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
*/
    public ArrayList getAttributeLists() {
        return attributeLists;
    }

    /*NN identification followed by VP */
    /*NN identification followed by VP */
/*    public ArrayList getIdentifiedVPPhrases(String phrase) {
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
*/
    
    
}
