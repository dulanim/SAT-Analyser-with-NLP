/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.test;

import com.project.NLP.Requirement.ClassRelation;
import com.project.NLP.Requirement.StoringArtefacts;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author S. Shobiga
 */

/*
 * class to test the precision, recall and F1 result from the natural language processing from the requirement document
 */
public class RequirementDocumentTest {

    private String fileName;
    private String className = "";
    private HashSet classList;
    private HashSet attributeList;
    private HashSet methodList;
    private HashMap requirementObjects = new HashMap();
    private HashSet requirementObjectRelations = new HashSet();
    private StoringArtefacts storingArtefacts = new StoringArtefacts();
    
    RequirementDocumentTest(String fileName) {
        this.fileName = fileName;
        readFromTextFile(fileName);

    }

    private String readFromTextFile(String file) {
        BufferedReader br = null;
        String req_Document = "";
        String[] lineArray;
        boolean relationsFound = false;
        int lineCount = 0;
        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader(file));
            while ((sCurrentLine = br.readLine()) != null) {

                if (!sCurrentLine.contains("Relations :")) {
                    if (lineCount % 3 == 0) {
                        if (lineCount != 0) {
                            storingArtefactDetails();
                        }

                        classList = new HashSet();
                        attributeList = new HashSet();
                        methodList = new HashSet();
                        storingArtefacts = new StoringArtefacts();

                    }
                    if (sCurrentLine.contains("Class :")) {
                        lineArray = sCurrentLine.split(":");
                        className = lineArray[1].trim();
                        classList.add(className);
                    }
                    if (sCurrentLine.contains("Attributes :")) {
                        lineArray = sCurrentLine.split(":");
                        if (lineArray.length == 2) {
                            String[] attrArray = lineArray[1].split(",");
                            for (int attrCount = 0; attrCount < attrArray.length; attrCount++) {
                                if (!attrArray[attrCount].trim().equals("")) {

                                    attributeList.add(attrArray[attrCount].trim());

                                }
                            }
                        }

                    }
                    if (sCurrentLine.contains("Methods :")) {
                        lineArray = sCurrentLine.split(":");
                        if (lineArray.length == 2) {
                            String[] methodArray = lineArray[1].split(",");
                            for (int methodCount = 0; methodCount < methodArray.length; methodCount++) {
                                if (!methodArray[methodCount].trim().equals("")) {
                                    methodList.add(methodArray[methodCount].trim());

                                }
                            }

                        }

                    }
                }else if(sCurrentLine.contains("Relations :")){
                    System.out.println("Relations found....");
                    relationsFound = true;
                    lineCount =0;
                    
                }
                if(relationsFound){
                    lineCount = 0;
                    if(sCurrentLine.contains("Type : ")){
                        String[] relationArray = sCurrentLine.split("[,:]");
                        //System.out.println("length: "+ relationArray.length);
                        //System.out.println(relationArray[0]+" "+ relationArray[1] + " "+ relationArray[2] +" "+ relationArray[3] + " "+ relationArray[4] +" "+ relationArray[5]);
                        requirementObjectRelations.add(new ClassRelation(relationArray[1], relationArray[5], relationArray[3]));
                    
                    }
                }
                lineCount++;
            }
            storingArtefactDetails();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return req_Document;
    }
    /* store the artefacts*/

    private void storingArtefactDetails() {

        Iterator iterator = classList.iterator();
        while (iterator.hasNext()) {
            className = (String) iterator.next();
            className = className.toLowerCase();
            if (requirementObjects.containsKey(className)) {
                StoringArtefacts storeArt = (StoringArtefacts) requirementObjects.get(className);
                storeArt.addAttributes(attributeList);
                storeArt.addMethods(methodList);

            } else {
                /*calling storingArtefacts class store the results inorder to find the class- attri - metho -relation */
                storingArtefacts = new StoringArtefacts();
                storingArtefacts.addClassName(classList);
                storingArtefacts.addAttributes(attributeList);
                storingArtefacts.addMethods(methodList);
                requirementObjects.put(className, storingArtefacts);

            }
        }
    }

    /*method to get the storingArtefacts
     *
     */
    public StoringArtefacts getStoringArtefacts() {
        return storingArtefacts;
    }

    public HashMap getRequirementObjects() {
        return requirementObjects;

    }
    /*method to get the requirementObjectRelations*/
    public HashSet getRequirementObjectRelations(){
        return requirementObjectRelations;
    }
    
    Object keySet() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
