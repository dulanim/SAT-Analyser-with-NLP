/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.Requirement;

import edu.stanford.nlp.trees.Tree;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 *
 * @author S. Shobiga
 */
public class AttributeIdentification {

    private Tree[] tree;
    /*For single tree */
    private Tree sTree;
    private PhrasesIdentification phrase;
    private ArrayList attributes;
    private ArrayList attr; // store attributes derived from classIdentification 
    private DesignElementClass designEleClass = new DesignElementClass();
    private ClassIdentification attrFromClass;
    private HashSet attributeFinalList = new HashSet();
    private HashSet classList;

    AttributeIdentification() {

    }

    AttributeIdentification(Tree[] tree) {
        this.tree = tree;
        phrase = new PhrasesIdentification(tree);
        applyRules();
    }

    /*For single Tree */
    AttributeIdentification(Tree tree) {
        this.sTree = tree;
        phrase = new PhrasesIdentification(tree);
        applyRules();
    }

    /*parameters for the constructors 
     parameter 1: tokenization result from stanford core NLP
     parameter 2: the attribute list that is derived from class identification
    
     */
    AttributeIdentification(Tree[] tree, ArrayList attr, HashSet classList) {
        this.tree = tree;
        phrase = new PhrasesIdentification(tree);
        this.attr = attr;
        this.classList = classList;
        applyRules();
    }

    /*For single Tree */
    AttributeIdentification(Tree tree, ArrayList attr, HashSet classList) {
        this.sTree = tree;
        phrase = new PhrasesIdentification(tree);
        this.attr = attr;
        this.classList = classList;
        applyRules();
    }

    private void applyRules() {
        /*get the attributes derived from class identification*/
        /*rule 1*/
        setAttributesFromClass(attr);
        /*rule3*/
        /*by traversing the tree get the attributes*/
        getAttributeList();
        /*rule 4*/
        checksWhetherDesignElementExits();

        /*rule 6*/
        removeRedundancy();
        /*rule 7*/
        // findAttributeAssociatedWithLocation();
        /*rule 8*/
        removeClassesFromAttributes();

    }

    /*
     rule 1 and rule 2
     method to set the attributes that are taken from class
     parameter - attributes generated from class Identification
     */
    public void setAttributesFromClass(ArrayList attributesFromClass) {
        attributes = attributesFromClass;
        //System.out.println("attributes from class: "+ attributes);

    }
    /*rule 3
     get the nouns follwed by VP
    
     */

    private void getAttributeList() {
        ArrayList newAtt;
        newAtt = phrase.getAttributeList();
        //newAtt=phrase.getAttribute();
        //add the new attributes to the attributes list
        if (!newAtt.isEmpty()) {
            for (int i = 0; i < newAtt.size(); i++) {
                attributes.add(newAtt.get(i));
            }
        }
        //attributes = newAtt;
        //System.out.println("newwwwwwwwwwwwwww attributes : "+ newAtt);
        //System.out.println("TOTAL attributes : "+ attributes);

        /*for(int i=0;i<newAtt.size();i++){
         attributes.add(newAtt.get(i));
         }*/
        //System.out.println("after adding : "+ attributes);
        //attributes=phrase.getIdentifiedVPPhrases("VBG");
    }

    /*rule 6*/
    private void findAttributeAssociatedWithLocation() {
        ArrayList att = phrase.getIdentifiedPhrases1("NN$NNP");
        for (int i = 0; i < att.size(); i++) {
            attributes.add(att.get(i));

        }

    }
    /*rule 4
     this method checks whether the attribute or class contains the design elemets.
     if so it will skip those elements and store rest of the artefacts
     parameter: className, attribute
     */

    public void checksWhetherDesignElementExits() {
        ArrayList designElements = designEleClass.getDesignElementsList();
        //attributes.remove("system");
        //System.out.println("designElements: "+designElements);
        //System.out.println(": "+attributes.get(0).toString());

        if (!attributes.isEmpty()) {
            for (int attrCount = 0; attrCount < attributes.size(); attrCount++) {
                String[] attrSplit = attributes.get(attrCount).toString().split(" ");
                for (int spliter = 0; spliter < attrSplit.length; spliter++) {
                    if (designElements.contains(attrSplit[spliter])) {
                        //System.out.println("Design element found in attributes..");
                        attributes.remove(attributes.get(attrCount));
                        break;
                    }
                }
                if (attrSplit.length == 0) {
                    if (designElements.contains(attributes.get(attrCount).toString())) {
                        //System.out.print("found");
                        attributes.remove(attributes.get(attrCount));

                    }
                }

            }
        }

        /*String s =attributes.get(0).toString();
         if(String.valueOf(s).equals("system"))
         System.out.println("sdfsfsdfs");
         */
        //t.removeAll(designElements);
        //System.out.println("after removing design elements: "+attributes);
    }

    /*rule 6*/
    private void removeRedundancy() {
        //System.out.println("attributes size: "+ attributes.size());
        //System.out.println("attr: "+ attributes);
        for (int i = 0; i < attributes.size(); i++) {
            String attr = attributes.get(i).toString();
            if (!attr.isEmpty() || !attr.equals("")) {
                //System.out.println("get element: "+ attributes.get(i));
                attributeFinalList.add(attr);
            }
        }
    }


    /*rule 8
     *remove classes from attributes and checks plurals or singular presents in attributes of classes
     */
    private void removeClassesFromAttributes() {

        attributeFinalList.removeAll(classList);

    }

    public ArrayList getAttributesTemp() {
        return attributes;
    }

    public HashSet getAttributes() {

        return attributeFinalList;
    }

    private void removeFromHashSet() {
        Object[] arrayAtt = attr.toArray();
        Object[] attFinalList = attributeFinalList.toArray();
        String[] stringArray = Arrays.copyOf(arrayAtt, arrayAtt.length, String[].class);
        //System.out.println("            "+stringArray[0]);
        for (int i = 0; i < arrayAtt.length; i++) {
            //System.out.println("666666666");
            for (int j = 0; j < attFinalList.length; j++) {
                //System.out.println(".."+attFinalList[j]+".."+arrayAtt[i]+"..."+ attFinalList[j].toString().equals("account")+"...."+arrayAtt[i].toString().equals("account"));

                if (attFinalList[j].toString().equalsIgnoreCase(arrayAtt[i].toString())) {
                    //System.out.println(".."+attFinalList[j]+".."+arrayAtt[i]);
                    //System.out.println("foundddddddddddddd");
                    attributeFinalList.remove(arrayAtt[i]);
                }
            }

        }

    }
}
