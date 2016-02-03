/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *..
 */
package com.project.NLP.Requirement;

import com.project.NLP.GUI.ArtefactFrameTestGUI;
import edu.stanford.nlp.trees.Tree;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
/**
 * 
 * Class which takes the text file requirement(story card) and do preprocessing activities while reading the text.
 * Also checks whether the sentence is negative or not and pronoun resolving are done.
 * Then this class identifies the artefacts such as classes, attributes, methods and relationships for a particular sentence
 * and write the output into the text file as well call the class to write the output in the XML format. 
 * 
 * @author S. Shobiga
 * @author T. Vinojan
 */
public class NLPRequirementMain {

    private static final String REQUIREMENT_INPUT_FILE = "io/Hostel_Management_Requirement.txt"; // input file
    private static String requirementDocument = ""; //variable to hold the input document 
    public static HashMap requirementObjects = new HashMap(); // to store the final artefacts in the map
    private static HashSet<ClassRelation> requirementObjectRelations = new HashSet<>();// to store the final relationships in the map

    public static void extractRequirement() {

        HashSet classList = new HashSet(); //to store the identified class list by applying rules
        HashSet attrList = new HashSet(); //to store the identified attribute list by applying rules
        HashSet methodList = new HashSet(); //to store the identified method list by applying rules
        HashSet relationList = new HashSet(); //to store the identified relations ship list by applying rules
        StoringArtefacts storingArtefacts = new StoringArtefacts(); //variable to hold the storing artefacts
        String className = "";
        ArrayList trees = new ArrayList();
        HashMap classWithAttr;
        try {
            /*Reading requirement file */
            requirementDocument = readFromTextFile(REQUIREMENT_INPUT_FILE);
            //requirementDocument = readFromTextFile(StaticData.requirementFilePath);
            //System.setProperty("wordnet.database.dir", "/usr/local/WordNet-2.1/dict");
            System.setProperty("wordnet.database.dir", System.getProperty("user.home") + File.separator + "WordNet" + File.separator + "dict");

            if ("".equals(requirementDocument)) {
                System.out.println("Error : There is no input document !!!");
            } else {
                AnaphoraAnalyzer analyzer = new AnaphoraAnalyzer(requirementDocument);
                requirementDocument = analyzer.doPronounResolving();
                ParserTreeGenerator parser = new ParserTreeGenerator(requirementDocument);
                trees = parser.getSentenceParseTree();
                /*For individual sentence in the requirement Document */
                for (int countTree = 0; countTree < trees.size(); countTree++) {
                    Tree tree = (Tree) trees.get(countTree);
                    System.out.println("Tree: " + tree);
                    /*if sentence is not negative, then allowing the artefact extraction*/
                    NegativeSentenceDetection negativeSentence = new NegativeSentenceDetection(tree);
                    if (!negativeSentence.isNegativeSentence()) {

                        /*noun pharase identification */
                        ClassIdentification np = new ClassIdentification(tree);
                        classList = np.getClasses();
                        classWithAttr = np.getClassWithAttr();

                        ArrayList attributesFromClass = np.getAttributeFromClass();
                        System.out.println("CLASS LIST:" + classList);

                        /*if classList is empty skip the rest of the extraction of artefacts*/
                        if (!classList.isEmpty()) {
                            /*attributes identification */
                            AttributeIdentification attr = new AttributeIdentification(tree, attributesFromClass, classList);
                            attrList = attr.getAttributes();
                            System.out.println("ATTRIBUTE LIST: " + attrList);

                            if (!classList.isEmpty()) {
                                /* methods identification */
                                MethodIdentifier mId = new MethodIdentifier(tree, classList);
                                methodList = mId.identifyCandidateMethods(tree);

                                /*Storing Class details  */
                                storingClassDetails(classList, className, requirementObjects, storingArtefacts, attrList, methodList, relationList);
                                /*to handle class with attribute map (noun + noun)*/
                                if (!classWithAttr.isEmpty()) {
                                    storingArtefacts = storeTheArtefactsForClassWithAttribute(classWithAttr, storingArtefacts);
                                }
                            }

                        }
                    }
                }
                if (!requirementObjects.isEmpty()) {
                    /*After finding all classes in the document identifying relationships between them.*/
                    requirementObjectRelationsStoring(trees);

                    /*eliminate the classes which are not having any attributes, methods and relationships*/
                    HashMap requirementObjectsModified = checkArtefactsExist(requirementObjects, requirementObjectRelations);
                    ArtefactFrameTestGUI t = new ArtefactFrameTestGUI(requirementObjectsModified, requirementObjectRelations);

                    while (t.getLock()) {
                        Thread.sleep(100);
                    }
                    /*Writing the information extracted from Requirement to text file  */
                    writeOutputToTxtFile(t.getRequirementobjects(), t.getRequirementRelationsObject());
                    WriteRequirementToXML.writeToXMLFile(t.getRequirementobjects(), t.getRequirementRelationsObject());
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }

 
    /**
     * 
     * method to store the requirement Object relations in the hashmap
     * @param trees 
     */
    private static void requirementObjectRelationsStoring(ArrayList trees) {
        ClassRelationIdentifier crid = new ClassRelationIdentifier();
        requirementObjectRelations.addAll(crid.identifyGeneralizationRelations(requirementObjects.keySet()));
        requirementObjectRelations.addAll(crid.identifyAssociationUsingAttribute(requirementObjects));

        for (Object tree : trees) {
            requirementObjectRelations.addAll(crid.identifyAssociation((Tree) tree, requirementObjects.keySet()));
        }
    }


    /**
     * 
     * method to store artefacts when the attributes are within the class itself
     * @param classWithAttr
     * @param storingArtefacts
     * @return 
     */
    private static StoringArtefacts storeTheArtefactsForClassWithAttribute(HashMap classWithAttr, StoringArtefacts storingArtefacts) {
        HashSet classListWithAttr = new HashSet();
        HashSet attributeList = new HashSet();
        int sizeOfMap = classWithAttr.size();
        Iterator classWithAttrIterator = classWithAttr.keySet().iterator();
        while (classWithAttrIterator.hasNext()) {
            String classN = classWithAttrIterator.next().toString();
            classListWithAttr.add(classN);
            attributeList = (HashSet) classWithAttr.get(classN);
            System.out.println("Class from classWith attributes :" + classN);
            System.out.println("Attributes from classwith attributes: " + attributeList);
            if (requirementObjects.containsKey(classN)) {
                StoringArtefacts storeArt = (StoringArtefacts) requirementObjects.get(classN);
                storeArt.addAttributes(attributeList);

            } else {
                /*calling storingArtefacts class store the results inorder to find the class- attri - metho -relation */
                storingArtefacts = new StoringArtefacts();
                storingArtefacts.addClassName(classListWithAttr);
                storingArtefacts.addAttributes(attributeList);
                requirementObjects.put(classN, storingArtefacts);
            }
        }
        return storingArtefacts;
    }

    /**
     * 
     * method to store the class details in the map
     * @param classList
     * @param className
     * @param requirementObjects
     * @param storingArtefacts
     * @param attrList
     * @param methodList
     * @param relationList 
     */
    private static void storingClassDetails(HashSet classList, String className, HashMap requirementObjects, StoringArtefacts storingArtefacts, HashSet attrList, HashSet methodList, HashSet relationList) {
        Iterator iterator = classList.iterator();
        while (iterator.hasNext()) {
            className = (String) iterator.next();
            className = className.toLowerCase();

            if (requirementObjects.containsKey(className)) {
                StoringArtefacts storeArt = (StoringArtefacts) requirementObjects.get(className);
                storeArt.addAttributes(attrList);
                storeArt.addMethods(methodList);
                storeArt.addRelationships(relationList);
            } else {
                /*calling storingArtefacts class store the results inorder to find the class- attri - metho -relation */
                storingArtefacts = new StoringArtefacts();
                storingArtefacts.addClassName(classList);
                storingArtefacts.addAttributes(attrList);
                storingArtefacts.addMethods(methodList);
                storingArtefacts.addRelationships(relationList);
                requirementObjects.put(className, storingArtefacts);
            }
        }
    }

    /**
     * Reading the input Natural Language Requirement File
     *
     * @param file
     * @return modified text file in String
     */
    public static String readFromTextFile(String file) {
        BufferedReader br = null;
        String req_Document = "";

        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader(file));
            while ((sCurrentLine = br.readLine()) != null) {
                //sentence modification is done
                SentenceReplacement sentenceReplacement = new SentenceReplacement();
                sCurrentLine = sentenceReplacement.doModify(sCurrentLine);
                
                req_Document += " " + sCurrentLine;
            }

        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("Exception occurs: "+ e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                //ex.printStackTrace();
                System.out.println("Exception occurs: "+ ex);
            }
        }
        return req_Document;
    }

    /**
     * method which stores the relationship of class within the storingArtefacts
     *
     * @param relations
     */
    public static void addingRelationsToIdentifiedClasses(HashSet relations) {
        Iterator iter = relations.iterator();
        while (iter.hasNext()) {
            ClassRelation classRelation = (ClassRelation) iter.next();
            String parent = classRelation.getParentElement();
            String child = classRelation.getChildElement();
            StoringArtefacts storingArtefacts = (StoringArtefacts) requirementObjects.get(parent);
            storingArtefacts.addRelationships(classRelation);
            storingArtefacts = (StoringArtefacts) requirementObjects.get(child);
            storingArtefacts.addRelationships(classRelation);
        }
    }

    /**
     * method to write the output to the text file
     *
     * @param output: hashMap where the artefacts are stored (class, attributes,
     * methods)
     * @param outputRelations: hashMap where the relationships associated with
     * the classes stored
     */
    public static void writeOutputToTxtFile(HashMap output, HashSet outputRelations) {
        //write to file : "Requirement Output"
        try {
            StringBuffer sbf = new StringBuffer();
            Iterator it = output.keySet().iterator();
            while (it.hasNext()) {

                String className = it.next().toString();
                StoringArtefacts store = (StoringArtefacts) output.get(className);
                HashSet attributes = store.getAttributes();
                HashSet methods = store.getMethods();
                HashSet relations = store.getRelationships();

                sbf.append("Class : " + className);
                sbf.append("\n\tAttributes : ");
                for (Object attribute : attributes) {
                    sbf.append(attribute.toString() + ",");
                }
                sbf.append("\n\tMethods : ");
                for (Object method : methods) {
                    sbf.append(method.toString() + ",");
                }
                sbf.append("\n");
            }
            sbf.append("\nRelations : \n");
            Iterator relationsIter = outputRelations.iterator();
            while (relationsIter.hasNext()) {
                ClassRelation rel = (ClassRelation) relationsIter.next();
                sbf.append("\n Type : " + rel.getRelationType() + ", Parent : " + rel.getParentElement() + ", Child : " + rel.getChildElement());

            }

            //System.out.println(sbf.toString());
            BufferedWriter bwr = new BufferedWriter(new FileWriter("io/Requirement_Output_Bank_Machine.txt"));
            /*write contents of StringBuffer to a file*/
            bwr.write(sbf.toString());
            /*flush the stream*/
            bwr.flush();
            /*close the stream*/
            bwr.close();

        } catch (Exception e) {
            System.out.println("Exception: " + e);
            e.printStackTrace();
        }
    }

    /**
     * eliminate the classes which are not having any attributes, methods and relationships     *
     * @param output
     * @param outputRelations
     * @return
     */
    private static HashMap checkArtefactsExist(HashMap output, HashSet outputRelations) {
        boolean status = false;
        boolean statusRelation = false;
        Iterator outputIterator = output.keySet().iterator();
        while (outputIterator.hasNext()) {
            String className = outputIterator.next().toString();
            StoringArtefacts storeArt = (StoringArtefacts) output.get(className);

            Iterator relIterator = outputRelations.iterator();
            while (relIterator.hasNext()) {

                ClassRelation rel = (ClassRelation) relIterator.next();
                if (rel.getChildElement().equalsIgnoreCase(className) || rel.getParentElement().equalsIgnoreCase(className)) {
                    statusRelation = true;
                    break;
                }
            }

            if (storeArt.getAttributes().isEmpty() && storeArt.getMethods().isEmpty() && !statusRelation) {
                status = true;
                outputIterator.remove();
            }

        }

        return output;
    }

    public static void main(String[] args) {
        extractRequirement();
    }
}
