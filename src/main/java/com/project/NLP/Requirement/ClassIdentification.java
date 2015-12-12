/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *..
 */
package com.project.NLP.Requirement;

import edu.stanford.nlp.trees.Tree;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;


/**
 *
 * @author S. Shobiga
 */
public class ClassIdentification {

    private Tree[] tree;
    /*Single Tree */
    private Tree sTree;
    private ArrayList priorToRule1;
    private HashSet classList = new HashSet();
    private ArrayList afterRules = new ArrayList();

    private PhrasesIdentification np;
    private ArrayList attributeFromClass = new ArrayList();
    private DesignElementClass designEleClass = new DesignElementClass();

    ClassIdentification() {

    }

    ClassIdentification(Tree[] tree){
        this.tree =tree;
        np = new PhrasesIdentification(tree);
        applyRules();
    }
    
    /*Single Tree */
    ClassIdentification(Tree tree){
        this.sTree=tree;
        np = new PhrasesIdentification(tree);
        applyRules();
    }
 

    /*apply rules to find classes*/
    private void applyRules() {
        /*calling by this function it identifies NN phrases
        and also it skips the class of people, places or things - proper noun*/
        /* rule 1 and rule 2 and rule3*/
        afterRules = nounPharseIdentification("NP");
        //afterRules = innerPhrases("NN");
        
        /*rule 2 */
        checksWhetherDesignElementExits(afterRules);
        /* rule 3*/
        //checksLocationAndPeopleName(afterRules);
        /*rule 4- eliminate redundant classes*/
        eliminateRedundantClasses(afterRules);
        /*rule 5- attribues elimination from classes */
        attributesEliminationFromClass(afterRules);
        
        /*rule 6*/
        
        /*rule 7*/
       
    }
    /*find the noun phrases in the sentence*/

    public ArrayList nounPharseIdentification(String phrase) {
        ArrayList verb = np.getIdentifiedPhrases(phrase);
        //ArrayList verb = np.getClass("");
        return verb;
    }

    /*rule 1
     *if the phrase has 2 noun phrases first noun is identified as Class and 2nd is identified as attributes
     */


    /*rule 2
     this method checks whether the attribute or class contains the design elemets.
     if so it will skip those elements and store rest of the artefacts
     parameter: className, attribute
     */
    
    public void checksWhetherDesignElementExits(ArrayList className) {
        ArrayList designElements = designEleClass.getDesignElementsList();
        className.removeAll(designElements);
        afterRules = className;
        System.out.println("cl: "+className);
       
    }
    public void checksWhetherDesignElementExits1(Object className, Object attribute) {
        ArrayList designElements = designEleClass.getDesignElementsList();
        
        if (!designElements.contains(className)) {
            System.out.println("design class... :" + String.valueOf(className));
            afterRules.add(String.valueOf(className));

        }
        if (!designElements.contains(attribute)) {
            System.out.println("design attribute... :" + String.valueOf(attribute));
            attributeFromClass.add(String.valueOf(attribute));

        }

        
    }

    /*rule 3
    if the  concept is related to the location name or people name
    then those concepts will be elemenated from className
    */
    
    public void checksLocationAndPeopleName(ArrayList list){
        ParserTreeGenerator parser = new ParserTreeGenerator();
        ArrayList nameEntity;
        for(int j=0;j<list.size();j++){
            nameEntity = parser.generateNamedEntityTagAnnotation();
            /*remove the location name or people name if list of the class contains*/
            for(int i=0;i<nameEntity.size();i++){
                if(afterRules.contains(nameEntity.get(i))){
                    afterRules.remove(nameEntity.get(i));

                }
            }
        }
        
    }
    
    /*rule 4
    redundant classes are eleminated
    */
    private void eliminateRedundantClasses(ArrayList list){
        //HashSet withoutDuplicateClass = new HashSet();
        /*add the list data to the hashset to eliminate the redundancy*/
        for(int i=0;i<list.size();i++){
            classList.add(list.get(i));
        }
        
    }
    
    /*rule 5
    attribute elimination from class
    */
    private void attributesEliminationFromClass(ArrayList list){
        ArrayList attributeFromClass  = np.getAttributeLists();
        System.out.println("attributes: "+attributeFromClass);
    }
    
    /*method to get the lists of classes*/
    public HashSet getClasses() {
        
        return classList;
    }
    public ArrayList getClassesTemp(){
        return afterRules;
    }
    /*method to get the lists of attributes*/
    public ArrayList getAttributeFromClass() {
       attributeFromClass= np.getAttributeLists();
        return attributeFromClass;
    }

    
    
    
    
    /*rule1 continuos...
     if the phrase consists PRP + NN identify the PRP as a class and NN as attribute
     but the PRP is stored already in class list. so store the NN as attributeFromClass
     */
    private void checkPRP(PhrasesIdentification PI) {
        ArrayList innerPhraseList = PI.getIdentifiedPhrases1("NP");
        //ArrayList 

        ArrayList nn;
        //Boolean status= false;

        for (int i = 0; i < innerPhraseList.size(); i++) {
            System.out.println("prp.... :" + innerPhraseList);
            if (innerPhraseList.get(i).equals("PRP")) {
                System.out.println("prp.... :" + innerPhraseList.get(i));
                attributeFromClass.add(innerPhraseList.get(i + 1));
                break;
            }
        }

    }

   

   

}
