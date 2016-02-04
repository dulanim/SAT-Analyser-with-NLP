/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.test;

import com.project.NLP.Requirement.ClassRelation;
import com.project.NLP.Requirement.StoringArtefacts;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author S. Shobiga
 */
public class NLPMeasure {

    private HashMap machineOutput;
    private HashMap expertOutput;
    private String className_Expert;
    private String className_Machine;

    private HashSet attributeList_Expert;
    private HashSet attributeList_Machine;
    private HashSet methodList_Expert;
    private HashSet methodList_Machine;
    private HashSet classList_Expert;
    private HashSet classList_Machine;
    private HashSet machineOutputRelations;
    private HashSet expertOutputRelations;

    private int className_Machine_Count = 0;
    private int className_Expert_Count = 0;
    private int attribute_Machine_Count = 0;
    private int attribute_Expert_Count = 0;
    private int method_Machine_Count = 0;
    private int method_Expert_Count = 0;
    private int relationship_Machine_Count = 0;
    private int relationship_Expert_Count = 0;
    private int totalAttribute_Machine_Count = 0;
    private int totalMethod_Machine_Count = 0;
    private int totalRelation_Machine_Count = 0;

    private StoringArtefacts storingArtefacts_Expert;
    private StoringArtefacts storingArtefacts_Machine;

    NLPMeasure() {

    }

    NLPMeasure(HashMap machineOutput, HashMap expertOutput, HashSet machineOutputRelations, HashSet expertOutputRelations) {
        this.expertOutput = expertOutput;
        this.machineOutput = machineOutput;
        this.machineOutputRelations = machineOutputRelations;
        this.expertOutputRelations = expertOutputRelations;
        compareOutput();
        compareOutputRelations();
        machineOutputCount();
    }

    /*method to compare the output of relationships generated from the system and expert knowledge*/
    private void compareOutputRelations() {
        ClassRelation expertRelation;
        ClassRelation machineRelation;

        relationship_Expert_Count = expertOutputRelations.size();
        totalRelation_Machine_Count = machineOutputRelations.size();

        for (Object expertRelationShipItem : expertOutputRelations) {
            expertRelation = (ClassRelation) expertRelationShipItem;
            if (expertRelation != null) {
                for (Object machineRelationShipItem : machineOutputRelations) {
                    machineRelation = (ClassRelation) machineRelationShipItem;
                    if (expertRelation.getRelationType().equalsIgnoreCase(machineRelation.getRelationType())) {
                        if (expertRelation.getParentElement().equalsIgnoreCase(machineRelation.getParentElement())) {
                            if (expertRelation.getChildElement().equalsIgnoreCase(machineRelation.getChildElement())) {
                                relationship_Machine_Count++;
                            }
                        }
                    }
                }
            }

        }
    }

    /* method to compare the output generated from the system and the expert knowledge*/
    private void compareOutput() {
        boolean classStatus = false;
        Iterator expertIterator = expertOutput.keySet().iterator();
        while (expertIterator.hasNext()) {
            className_Expert = expertIterator.next().toString();
            className_Expert_Count++;

            storingArtefacts_Expert = (StoringArtefacts) expertOutput.get(className_Expert);
            attributeList_Expert = storingArtefacts_Expert.getAttributes();
            methodList_Expert = storingArtefacts_Expert.getMethods();

            //searching className
            classStatus = searchClassName(className_Expert);

            //if class is found in both files search other elements
            if (classStatus) {
                //searchAttributes
                attribute_Expert_Count += attributeList_Expert.size();
                attribute_Machine_Count += searchArtefact(attributeList_Expert, attributeList_Machine);
                System.out.println("attribute Machine count: " + attribute_Machine_Count);
                System.out.println("attribute Expert count: " + attribute_Expert_Count);

                //search method
                method_Expert_Count += methodList_Expert.size();
                method_Machine_Count += searchArtefact(methodList_Expert, methodList_Machine);

                System.out.println("method Machine count: " + method_Machine_Count);
                System.out.println("method Expert count: " + method_Expert_Count);

            }

        }

        System.out.println("class machine count: " + className_Machine_Count);
        System.out.println("class expert count: " + className_Expert_Count);

    }

    /*method to search the className*/
    private boolean searchClassName(String className_Ex) {
        boolean classStatus = false;
        //searchine whether artefacts exist in machine Result
        Iterator machineIterator = machineOutput.keySet().iterator();

        while (machineIterator.hasNext()) {
            className_Machine = machineIterator.next().toString();
            System.out.println("className Expert :" + className_Ex + " className machine: " + className_Machine);
            if (className_Ex.equalsIgnoreCase(className_Machine)) {
                className_Machine_Count++;
                classStatus = true;

                storingArtefacts_Machine = (StoringArtefacts) machineOutput.get(className_Machine);
                attributeList_Machine = storingArtefacts_Machine.getAttributes();
                methodList_Machine = storingArtefacts_Machine.getMethods();
                break;
            }

            //resetting the status
            classStatus = false;
        }
        return classStatus;
    }

    /*method to search the attribute*/
    private int searchArtefact(HashSet expertList, HashSet machineList) {
        int machineCount = 0;
        for (Object expert : expertList) {
            for (Object machine : machineList) {
                if (expert.toString().equalsIgnoreCase(machine.toString())) {
                    machineCount++;
                    System.out.println("machineCount: " + machineCount);
                }
            }

        }
        return machineCount;

    }

    private void machineOutputCount() {
        Iterator machineIterator = machineOutput.keySet().iterator();
        while (machineIterator.hasNext()) {
            String classN = machineIterator.next().toString();
            StoringArtefacts store = (StoringArtefacts) machineOutput.get(classN);
            totalAttribute_Machine_Count += store.getAttributes().size();
            totalMethod_Machine_Count += store.getMethods().size();

        }
    }

    public int getTotalClassCount_Expert() {
        return expertOutput.size();
    }

    public int getTotalClassCount_Machine() {
        return machineOutput.size();
    }

    public int correctlyRetrievedTotalClassCount_Machine() {
        return className_Machine_Count;
    }

    public int getTotalAttributeCount_Expert() {
        return attribute_Expert_Count;
    }

    public int getTotalAttributeCount_Machine() {
        return totalAttribute_Machine_Count;
    }

    public int correctlyRetrievedTotalAttributeCount_Machine() {
        return attribute_Machine_Count;
    }

    public int getTotalMethodCount_Expert() {
        return method_Expert_Count;
    }

    public int getTotalMethodCount_Machine() {
        return totalMethod_Machine_Count;
    }

    public int correctlyRetrievedTotalMethodCount_Machine() {
        return method_Machine_Count;
    }

    public int getTotalRelationCount_Expert() {
        return relationship_Expert_Count;
    }

    public int getTotalRelationCount_Machine() {
        return totalRelation_Machine_Count;
    }

    public int correctlyRetrievedTotalRelationCount_Machine() {
        return relationship_Machine_Count;
    }
    /*Method to handle PRECISION
     *How many selected items are relevant from the retrieved result
     */

    public double calculatePrecisionOfClass() {
        int totalClassCount_Machine = getTotalClassCount_Machine();
        if (totalClassCount_Machine != 0) {
            System.out.println("" + correctlyRetrievedTotalClassCount_Machine() + " " + "/ " + getTotalClassCount_Machine());
            return correctlyRetrievedTotalClassCount_Machine() * 1.0 / getTotalClassCount_Machine();
        } else {
            return -999;
        }

    }

    public double calculatePrecisionOfAttribute() {
        int totalAttributeCount_Machine = getTotalAttributeCount_Machine();
        if (totalAttributeCount_Machine != 0) {
            System.out.println("" + correctlyRetrievedTotalAttributeCount_Machine() + " " + "/ " + totalAttributeCount_Machine);
            return correctlyRetrievedTotalAttributeCount_Machine() * 1.0 / totalAttributeCount_Machine;
        } else {
            return -999;
        }
    }

    public double calculatePrecisionOfMethod() {
        int totalMethodCount_Machine = getTotalMethodCount_Machine();
        if (totalMethodCount_Machine != 0) {
            System.out.println(correctlyRetrievedTotalMethodCount_Machine() + " " + "/ " + totalMethodCount_Machine);
            return correctlyRetrievedTotalMethodCount_Machine() * 1.0 / totalMethodCount_Machine;
        } else {
            return -999;
        }
    }

    public double calculatePrecisionOfRelation() {
        System.out.println(correctlyRetrievedTotalRelationCount_Machine() + " " + "/ " + getTotalRelationCount_Machine());
        return correctlyRetrievedTotalRelationCount_Machine() * 1.0 / getTotalRelationCount_Machine();
    }
    /*Method to handle RECALL
     *How many relevant items are selected
     */

    public double calculateRecallOfClass() {
        int totalClassCount_Expert = 0;
        if ((totalClassCount_Expert = getTotalClassCount_Expert()) != 0) {
            return correctlyRetrievedTotalClassCount_Machine() * 1.0 / totalClassCount_Expert;
        } else {
            return -999;
        }
    }

    public double calculateRecallOfAttribute() {
        int totalAttributeCount_Expert = 0;
        if ((totalAttributeCount_Expert = getTotalAttributeCount_Expert()) != 0) {
            return correctlyRetrievedTotalAttributeCount_Machine() * 1.0 / totalAttributeCount_Expert;
        } else {
            return -999;
        }
    }

    public double calculateRecallOfMethod() {
        int totalMethodCount_Expert = 0;
        if ((totalMethodCount_Expert = getTotalMethodCount_Expert()) != 0) {
            return correctlyRetrievedTotalMethodCount_Machine() * 1.0 / totalMethodCount_Expert;
        } else {
            return -999;
        }
    }

    public double calculateRecallOfRelation() {
        int totalRelationCount_Expert = 0;
        if ((totalRelationCount_Expert = getTotalRelationCount_Expert()) != 0) {
            return correctlyRetrievedTotalRelationCount_Machine() * 1.0 / totalRelationCount_Expert;
        } else {
            return -999;
        }
    }

    /*F1- MEASURE
     * test's accuracy measure which is an weighted average of percision and recall
     */
    public double calculateF1MeasureOfClass() {
        double divider = calculatePrecisionOfClass() + calculateRecallOfClass();
        if (divider != 0) {
            return 2 * calculatePrecisionOfClass() * calculateRecallOfClass() * 1.0 / divider;
        } else {
            return -999;
        }
    }

    public double calculateF1MeasureOfAttribute() {
        double divider = (calculatePrecisionOfAttribute() + calculateRecallOfAttribute());
        if (divider != 0) {
            return 2 * calculatePrecisionOfAttribute() * calculateRecallOfAttribute() * 1.0 / divider;
        } else {
            return -999;
        }
    }

    public double calculateF1MeasureOfMethod() {
        double divider = (calculatePrecisionOfMethod() + calculateRecallOfMethod());
        if (divider != 0) {
            return 2 * calculatePrecisionOfMethod() * calculateRecallOfMethod() * 1.0 / divider;
        } else {
            return -999;
        }
    }

    public double calculateF1MeasureOfRelation() {
        double divider = (calculatePrecisionOfRelation() + calculateRecallOfRelation());
        if (divider != 0) {
            return 2 * calculatePrecisionOfRelation() * calculateRecallOfRelation() * 1.0 / divider;
        } else {
            return -999;

        }
    }

}
