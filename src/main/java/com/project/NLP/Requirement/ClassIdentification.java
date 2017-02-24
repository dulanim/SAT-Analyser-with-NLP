/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *..
 */
package com.project.NLP.Requirement;

import edu.stanford.nlp.trees.Tree;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * class to identify the classes from the sentence using tokenization
 *
 * @author S. Shobiga
 */
public class ClassIdentification {

    private Tree sTree;
    private ArrayList priorToRule1;
    private HashSet classList = new HashSet();
    private ArrayList afterRules = new ArrayList();

    private PhrasesIdentification np;
    private ArrayList attributeFromClass = new ArrayList();
    private DesignElementClass designEleClass = new DesignElementClass();
    private DictionaryForClass dictionaryForClass = new DictionaryForClass();

    ClassIdentification() {

    }

    /**
     * constructor to identify the classes for a single tree
     *
     * @param tree
     */
    ClassIdentification(Tree tree) {
        this.sTree = tree;
        np = new PhrasesIdentification(tree);
        applyRules();
    }

    /**
     * apply rules to identify the classes
     */
    private void applyRules() {
        //rule 1
        afterRules = nounPharseIdentification();
        //rule 2
        checksWhetherDesignElementExits(afterRules);
        //rule 3
        eliminateRedundantClasses(afterRules);
        //rule 4
        attributesEliminationFromClass(afterRules);

        //afterRules = innerPhrases("NN");
        // rule 3
        //checksLocationAndPeopleName(afterRules);
        /*rule 6*/
        //stemming(afterRules);
        /*rule 7*/
    }

    /**
     * rule 1 identify the classes using the tokenization by call
     * phraseIdentification class
     *
     * @return arrayList of classes
     */
    private ArrayList nounPharseIdentification() {
        ArrayList classL = np.getClassList();
        return classL;
    }

    /**
     * this method checks whether class contains the design elements. if so it
     * will skip those elements and store rest of the artefacts design elements
     * are taken from the dictionary call - classElementDictionary
     *
     * @param className
     */
    private void checksWhetherDesignElementExits(ArrayList className) {
        //ArrayList designElements = designEleClass.getDesignElementsList();
        ArrayList dictionaryElements = dictionaryForClass.getDictionaryForClass();
        className.removeAll(dictionaryElements);
        afterRules = className;
    }

    /*rule 3
     if the  concept is related to the location name or people name
     then those concepts will be elemenated from className
     */
    private void checksLocationAndPeopleName(ArrayList list) {
        ParserTreeGenerator parser = new ParserTreeGenerator();
        ArrayList nameEntity;
        for (int j = 0; j < list.size(); j++) {
            nameEntity = parser.generateNamedEntityTagAnnotation();
            /*remove the location name or people name if list of the class contains*/
            for (int i = 0; i < nameEntity.size(); i++) {
                if (afterRules.contains(nameEntity.get(i))) {
                    afterRules.remove(nameEntity.get(i));

                }
            }
        }

    }

    /**
     * rule 3 the redundant classes which are stored in the array list by
     * storing them in the hashSet
     *
     * @param list
     */
    private void eliminateRedundantClasses(ArrayList list) {
        /*add the list data to the hashset to eliminate the redundancy*/
        for (int i = 0; i < list.size(); i++) {
            classList.add(list.get(i).toString().trim());
        }
    }

    /**
     * rule 4 if the class contains the attributes, then those are removed from
     * the class list
     *
     * @param list
     */
    private void attributesEliminationFromClass(ArrayList list) {
        ArrayList attributeFromClass = np.getAttributeLists();
    }

    /**
     * rule 6 stemming input: classList output: removed class names using
     * stemming
     */
    private void stemming(ArrayList afterRules) {

        for (int i = 0; i < afterRules.size() - 1; i++) {
            for (int j = i + 1; j < afterRules.size(); j++) {
                if ((afterRules.get(j).toString()).startsWith(afterRules.get(i).toString())) {
                    afterRules.remove(j);
                }
                if ((afterRules.get(i).toString()).startsWith(afterRules.get(j).toString())) {
                    afterRules.remove(i);
                }
            }
        }
    }

    /**
     * method to get the list of classes
     *
     * @return hashSet
     */
    public HashSet getClasses() {
        return classList;
    }

    /**
     * method to return the classes which are stored with the attributes for
     * example when noun + noun rule appears
     *
     * @return hashMap key: className value: attributeList
     */
    public HashMap getClassWithAttr() {
        return np.getClassWithAttr();
    }

    /**
     * method to return the temporary class list
     *
     * @return arrayList
     */
    public ArrayList getClassesTemp() {
        return afterRules;
    }

    /**
     * method to get the lists of attributes
     *
     * @return
     */
    public ArrayList getAttributeFromClass() {
        ArrayList attribute = np.getAttributeLists();
        String att;
        for (int attrCount = 0; attrCount < attribute.size(); attrCount++) {
            att = attribute.get(attrCount).toString();
            if (!att.isEmpty() || !att.equals("")) {
                attributeFromClass.add(att);
            }
        }
        return attributeFromClass;
    }

}
