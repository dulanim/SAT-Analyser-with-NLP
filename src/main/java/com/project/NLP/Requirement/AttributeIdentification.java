/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.Requirement;

import edu.stanford.nlp.trees.Tree;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 *
 * class to identify the attributes. Rules are applied in this class
 * @author S. Shobiga
 */
public class AttributeIdentification {

    private Tree[] tree;
    private Tree sTree;
    private PhrasesIdentification phrase;
    private ArrayList attributes = new ArrayList();
    private ArrayList attr; // store attributes derived from classIdentification 
    private DesignElementClass designEleClass = new DesignElementClass();
    private ClassIdentification attrFromClass;
    private HashSet attributeFinalList = new HashSet();
    private HashSet classList;

    AttributeIdentification() {

    }

    /**
     * constructor to apply the rules to identify the attributes for a single tree
     * @param tree 
     */
    AttributeIdentification(Tree tree) {
        this.sTree = tree;
        phrase = new PhrasesIdentification(tree);
        applyRules();
    }

    /*parameters for the constructors 
     parameter 1: tokenization result from stanford core NLP
     parameter 2: the attribute list that is derived from class identification
    
     */
    /**
     * constructor to apply the rules to identify the attributes for a single tree
     * @param tree 
     * @param attr
     * @param classList 
     */
    AttributeIdentification(Tree tree, ArrayList attr, HashSet classList) {
        this.sTree = tree;
        phrase = new PhrasesIdentification(tree);
        this.attr = attr;
        this.classList = classList;
        applyRules();
    }

    /**
     * method to apply the rules for attribute identification
     */
    private void applyRules() {
        //rule 1
        setAttributesFromClass(attr);
        //rule 2
        getAttributeList();
        //rule 3
        checksWhetherDesignElementExits();
        //rule 4
        removeRedundancy();
        //rule 5
        removeClassesFromAttributes();
        //rule 6
        // findAttributeAssociatedWithLocation();
        

    }

   
    /**
     * rule 1 method to set the attributes that are taken from class identification
     * @param attributesFromClass 
     */
    public void setAttributesFromClass(ArrayList attributesFromClass) {
        String attr;
        for(int attrCount =0; attrCount< attributesFromClass.size() ; attrCount++){
            attr = attributesFromClass.get(attrCount).toString();
            if(!attr.isEmpty()|| !attr.equals("")){
                attributes.add(attributesFromClass.get(attrCount).toString());
            }
        }
        
    }

    /**
     * rule 3
     * from the phrase identification class get the attribute list by applying the tokenization rule
     */
    private void getAttributeList() {
        ArrayList newAtt;
        newAtt = phrase.getAttributeList();
        //add the new attributes to the attributes list
        if (!newAtt.isEmpty()|| !newAtt.equals("")) {
            for (int i = 0; i < newAtt.size(); i++) {
                attributes.add(newAtt.get(i));
            }
        }
      
    }

    /**
     * rule 4
     * this method checks whether the attribute or class contains the design elements. 
     * if so it will skip those elements and store rest of the artefacts
     */
    public void checksWhetherDesignElementExits() {
        ArrayList designElements = designEleClass.getDesignElementsList();
        if (!attributes.isEmpty()) {
            for (int attrCount = 0; attrCount < attributes.size(); attrCount++) {
                String[] attrSplit = attributes.get(attrCount).toString().split(" ");
                for (int spliter = 0; spliter < attrSplit.length; spliter++) {
                    if (designElements.contains(attrSplit[spliter])) {
                        attributes.remove(attributes.get(attrCount));
                        break;
                    }
                }
                if (attrSplit.length == 0) {
                    if (designElements.contains(attributes.get(attrCount).toString())) {
                        attributes.remove(attributes.get(attrCount));
                    }
                }

            }
        }

    }

    /**
     * rule 6
     * remove redundancy from the attribute list by storing it in hashSet
     */
    private void removeRedundancy() {
        for (int i = 0; i < attributes.size(); i++) {
            String attr = attributes.get(i).toString();
            if (!attr.isEmpty() || !attr.equals("")) {
                attributeFinalList.add(attr);
            }
        }
    }


    /**
     *rule 7
     *remove classes from attributes and checks plurals or singular presents in attributes of classes
     */
    private void removeClassesFromAttributes() {
        attributeFinalList.removeAll(classList);

    }
    
    /**
     * method to get the attribute list
     * @return 
     */
    public ArrayList getAttributesTemp() {
        return attributes;
    }

    /**
     * method to get the final attribute list
     * @return hashSet
     */
    public HashSet getAttributes() {

        return attributeFinalList;
    }

    private void removeFromHashSet() {
        Object[] arrayAtt = attr.toArray();
        Object[] attFinalList = attributeFinalList.toArray();
        String[] stringArray = Arrays.copyOf(arrayAtt, arrayAtt.length, String[].class);
        for (int i = 0; i < arrayAtt.length; i++) {
            for (int j = 0; j < attFinalList.length; j++) {
                if (attFinalList[j].toString().equalsIgnoreCase(arrayAtt[i].toString())) {
                    attributeFinalList.remove(arrayAtt[i]);
                }
            }
        }
    }
}
