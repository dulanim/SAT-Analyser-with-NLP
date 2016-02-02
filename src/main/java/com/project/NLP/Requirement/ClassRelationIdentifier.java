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
import java.util.HashMap;

/**
 *
 * @author Vinojan
 */
public class ClassRelationIdentifier {
    
    /*Loading wordnet datatbase instance  */
    private static  WordNetDatabase database = WordNetDatabase.getFileInstance();
    String requirementSentence;
    ArrayList classes = new ArrayList(Arrays.asList("account"));
    private ArrayList<String> verbPhraseList=new ArrayList<>(Arrays.asList("be","has","have","contains","contain","includes","include","consists","consist"));
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
                System.out.println("----Class1 name------"+classFromlist+" -------");
                 Iterator set = documentClassSet.iterator();
                while (set.hasNext()) {
                    String classFromSet = set.next().toString();
                    System.out.println("----Class2 name------"+classFromSet+" -------");
                    if(!(classFromSet.equals(classFromlist))){
                        boolean isRelationIdentified=false;
                        System.out.println("----not Same class name -------");
                        if (classFromSet.matches(".+" + classFromlist + ".*")) {
                            System.out.println("--------Success --1-- Generalization -----");
                            ClassRelation general = new ClassRelation("Generalization", classFromSet, classFromlist);
                            classRelations.add(general);
                            isRelationIdentified=true;
                        } else if (classFromlist.matches(".+" + classFromSet + ".*")) {
                            System.out.println("--------Success --1-1-- Generalization -----");
                            ClassRelation general = new ClassRelation("Generalization", classFromlist, classFromSet);
                            classRelations.add(general);
                            isRelationIdentified=true;
                        }
                        if(isRelationIdentified){
                        StoringArtefacts storeArt1=(StoringArtefacts)NLPRequirementMain.requirementObjects.get(classFromSet);
                        storeArt1.removeAttribute(classFromlist);
                        StoringArtefacts storeArt2=(StoringArtefacts)NLPRequirementMain.requirementObjects.get(classFromlist);
                        storeArt2.removeAttribute(classFromSet);
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
       HashSet hypernymSet;
       HashSet hyponymSet;
       HypernymHyponym hyps=new HypernymHyponym();
        
        if (documentClassSet.isEmpty()) {
            System.out.println("Info : There is no class found in the sentence. -Relations identifier");
        } else {
            Iterator it=documentClassSet.iterator();
            while(it.hasNext()){
                String className=(String) it.next();
                System.out.println("----Class name------"+className+" -------");
                hypernymSet=hyps.getHypernymsForWord(className);
                
                //System.out.println("----Class name------"+className+" --hypernymset-----"+hypernymSet+"---hyponymset---"+hyponymSet);
                Iterator iter=hypernymSet.iterator();
                    while(iter.hasNext()){
                        String hypernymClass=(String) iter.next();
                        System.out.println("----------"+hypernymClass+" -------");
                        if(documentClassSet.contains(hypernymClass)){
                        System.out.println("----general--hypernym----"+hypernymClass+" -------");
                            ClassRelation general = new ClassRelation("Generalization",className,hypernymClass);
                            classRelations.add(general);
                        }

                    }
                hyponymSet=hyps.getHyponymsForWord(className);    
                Iterator iter2=hyponymSet.iterator();
                    while(iter2.hasNext()){
                        String hyponymClass=(String) iter2.next();
                    
                        if(documentClassSet.contains(hyponymClass)){
                          System.out.println("----general--hyponym----"+hyponymClass+" -------");
                            ClassRelation general = new ClassRelation("Generalization",hyponymClass,className);
                            classRelations.add(general);
                        }

                    }
                    hypernymSet.clear();
                    hyponymSet.clear();
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
                 System.out.println("Sentence match : "+Sentence.listToString(match.yield()));
                TregexMatcher verbMatcher = verbPattern.matcher(match);
               // while(verbMatcher.findNextMatchingNode()){
                if(verbMatcher.findNextMatchingNode()){
                    Tree verbMatch=verbMatcher.getMatch();
                    String verb=Sentence.listToString(verbMatch.yield());
                    System.out.println("Verb match : "+verb);
                    if(verbPhraseList.contains(verb)){
                        System.out.println("list contains verb : "+verb);
                        String noun_1_phraseNotation="NN|NNS>(NP>S)";
                        String noun_2_phraseNotation="NN|NNS>>(NP,(VBZ|VBP>(VP,NP)))";
                        TregexPattern noun_pattern = TregexPattern.compile(noun_1_phraseNotation);
                        TregexMatcher noun_matcher = noun_pattern.matcher((Tree) tree);
                        if(noun_matcher.findNextMatchingNode()){
                            Tree nounMatch=noun_matcher.getMatch();
                            String noun1=Sentence.listToString(nounMatch.yield());
                            
                            if(documentClass.contains(noun1)){
                                noun_pattern = TregexPattern.compile(noun_2_phraseNotation);
                                noun_matcher = noun_pattern.matcher((Tree) tree);
                                System.out.println("class list contains noun1 : "+noun1);
                                if(noun_matcher.findNextMatchingNode()){
                                    nounMatch=noun_matcher.getMatch();
                                    String noun2=Sentence.listToString(nounMatch.yield());
                                    if(!noun1.equals(noun2) && documentClass.contains(noun2)){
                                        ClassRelation clr;
                                        System.out.println("class list contains noun2 : "+noun2);
                                        if(verb.equals("be")){
                                            clr=new ClassRelation("Generalization", noun1, noun2);
                                            System.out.println("class generalization");
                                        }
                                        else{
                                            clr=new ClassRelation("Association", noun2, noun1);
                                            System.out.println("class association");
                                        }
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
    
    /*Identifying Associations between classes using their atributes 
    *For example Car has four wheels.
    * Car is a class and Wheel is class. Car has the wheel as its attribute. 
    */
    
    public HashSet identifyAssociationUsingAttribute(HashMap classMap){
        HashSet relatonSet=new HashSet();
        Iterator it;
        it = classMap.keySet().iterator();
        while(it.hasNext()){
            
            String parent=it.next().toString();
            StoringArtefacts sa =(StoringArtefacts)classMap.get(parent);
            HashSet attributes=sa.getAttributes();
            Iterator itSet=attributes.iterator();
            while(itSet.hasNext()){
                String child=itSet.next().toString();
                if(classMap.containsKey(child)){
                    ClassRelation cr=new ClassRelation("Association",child,parent);
                    System.out.println("------Association Attribute ----: "+cr.getParentElement()+" "+cr.getChildElement());
                    relatonSet.add(cr);
                }
            }
            
        }
        
        
        return relatonSet;
    }

}
