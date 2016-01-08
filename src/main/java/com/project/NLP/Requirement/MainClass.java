/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *..
 */
package com.project.NLP.Requirement;

import com.project.NLP.UMLToXML.xmlwriter.WriteToXML;
import com.project.traceability.model.Attribute;
import com.project.traceability.model.Dependencies;
import com.project.traceability.model.ModelData;
import com.project.traceability.model.Operation;
import com.project.traceability.model.Parameter;
import com.project.traceability.staticdata.StaticData;
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
import java.util.concurrent.locks.Lock;

/**
 *
 * @author S. Shobiga
 */
public class MainClass {

    private static String requirementDocument = "";

    public static void main(String args[]) {

        HashSet classList = new HashSet();
        HashSet attrList = new HashSet();
        HashSet methodList = new HashSet();
        HashSet relationList = new HashSet();
        StoringArtefacts storingArtefacts;
        String className = null;
        HashMap requirementObjects = new HashMap();
        ArrayList trees = new ArrayList();
        HashMap<String, HashSet> multiClassWithAttribute;
        HashMap<String, HashSet> multiClassWithMethod;
        MultipleClassListHandlingDemo multipleClassListHandlingDemo;
        HashSet attributeMulti = new HashSet();
        HashSet methodMulti = new HashSet();
        boolean passiveCheck = false;
        HashMap passiveMap;
        HashSet tempList;
        try {
            /*Reading requirement file */
            requirementDocument = readFromTextFile("BankRequirement.txt");

            System.setProperty("wordnet.database.dir", "C:\\Program Files (x86)\\WordNet\\2.1\\dict");

            if (requirementDocument == "") {
                System.out.println("Error : There is no input document !!!");
            } else {
                ParserTreeGenerator parser = new ParserTreeGenerator(requirementDocument);
                trees = parser.getSentenceParseTree();
                ParserTreeGenerator p = new ParserTreeGenerator();
                //passiveCheck = parser.isPassiveSentence();
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
                        ArrayList attributesFromClass = np.getAttributeFromClass();
                        System.out.println("CLASS LIST:" + classList);

                        /*if classList is empty skip the rest of the extraction of artefacts*/
                        if (!classList.isEmpty()) {
                            /*attributes identification */
                            AttributeIdentification attr = new AttributeIdentification(tree, attributesFromClass, classList);
                            attrList = attr.getAttributes();
                            System.out.println("ATTRIBUTE LIST: " + attrList);

                            /*if the sentence is passive swipe the attributes and methods*/
                            // passiveVoiceHandling(parser, classList, attrList);
                            passiveMap = parser.getPassiveSentenceMap();
                            
                            if (passiveMap.containsKey(tree)) {
                                tempList = classList;
                                classList = attrList;
                                attrList = tempList;
                            }

                            /* methods identification */
                            MethodIdentifier mId = new MethodIdentifier(tree, classList);
                            methodList = mId.identifyCandidateMethods(tree);

                            /* relations identificaton */
                            ClassRelationIdentifier crId = new ClassRelationIdentifier(classList, requirementObjects.keySet());
                            relationList = crId.identifyGenaralizationByComparing(classList, requirementObjects.keySet());
                            //relationList.addAll(crId.identifyGenaralizationByHypernym(classList, requirementObjects.keySet()));

                            if (classList.size() > 1) {

                                multipleClassListHandlingDemo = new MultipleClassListHandlingDemo(classList, attrList, methodList);
                                multiClassWithAttribute = multipleClassListHandlingDemo.getClassWithAttribute();
                                multiClassWithMethod = multipleClassListHandlingDemo.getClassWithMethod();

                                /*loop to control opening multiple frames*/
                                while (MultipleClassListHandlingGUI.lock) {
                                    Thread.sleep(100);
                                }

                                Iterator classIterator = classList.iterator();
                                for (int countClass = 0; countClass < classList.size(); countClass++) {
                                    attributeMulti = new HashSet();
                                    methodMulti = new HashSet();
                                    if (classIterator.hasNext()) {
                                        String classNameMulti = classIterator.next().toString();
                                        HashSet classListMulti = new HashSet();
                                        if (multiClassWithAttribute.containsKey(classNameMulti)) {
                                            attributeMulti = multiClassWithAttribute.get(classNameMulti);
                                            classListMulti.add(classNameMulti);
                                            System.out.println("--------------------------------------------------------------------------------------attribute :" + attributeMulti);
                                        }
                                        if (multiClassWithMethod.containsKey(classNameMulti)) {
                                            methodMulti = multiClassWithMethod.get(classNameMulti);
                                            classListMulti.add(classNameMulti);
                                            System.out.println("--------------------------------------------------------------------------------------method :" + methodMulti);
                                        }
                                        //storeClassDetails(classNameMulti, attributeMulti, methodMulti, relationList);
                                        if (!attributeMulti.isEmpty() || !methodMulti.isEmpty()) {
                                            if (requirementObjects.containsKey(classNameMulti)) {
                                                StoringArtefacts storeArt = (StoringArtefacts) requirementObjects.get(classNameMulti);
                                                HashSet attributes = storeArt.getAttributes();
                                                attributes.addAll(attributeMulti);
                                                HashSet methods = storeArt.getMethods();
                                                methods.addAll(methodMulti);
                                                HashSet relations = storeArt.getRelationships();
                                                relations.addAll(relationList);
                                                System.out.println("cl :" + classNameMulti + "\nAttr :" + attributes + "\nMethod :" + methods);
                                            } else {
                                                /*calling storingArtefacts class store the results inorder to find the class- attri - metho -relation */
                                                storingArtefacts = new StoringArtefacts();
                                                storingArtefacts.setClassName(classListMulti);
                                                storingArtefacts.setAttributes(attributeMulti);
                                                storingArtefacts.setMethods(methodMulti);
                                                storingArtefacts.setRelationships(relationList);
                                                requirementObjects.put(classNameMulti, storingArtefacts);
                                                //System.out.println("cl :"+ classNameMulti+"\nAttr :"+ attributes +"\nMethod :"+methods);

                                            }
                                        }
                                    }

                                }
                            } else if (classList.size() == 1) {

                                /*Storing Class details  */
                                Iterator iterator = classList.iterator();
                                if (iterator.hasNext()) {
                                    className = (String) iterator.next();

                                }
                                if (requirementObjects.containsKey(className)) {
                                    StoringArtefacts storeArt = (StoringArtefacts) requirementObjects.get(className);
                                    HashSet attributes = storeArt.getAttributes();
                                    attributes.addAll(attrList);
                                    HashSet methods = storeArt.getMethods();
                                    methods.addAll(methodList);
                                    HashSet relations = storeArt.getRelationships();
                                    relations.addAll(relationList);
                                } else {
                                    /*calling storingArtefacts class store the results inorder to find the class- attri - metho -relation */
                                    storingArtefacts = new StoringArtefacts();
                                    storingArtefacts.setClassName(classList);
                                    storingArtefacts.setAttributes(attrList);
                                    storingArtefacts.setMethods(methodList);
                                    storingArtefacts.setRelationships(relationList);
                                    requirementObjects.put(className, storingArtefacts);
                                }

                            }
                        }
                    }

                }
                /*Writing the information extracted from Requirement to text file  */
                if (!requirementObjects.isEmpty()) {
                    writeOutputToXMLFile(requirementObjects);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();

        }
    }


    /* Reading the input Natural Language Requirement File 
     *Input : text file
     *Output :String of the text file
     */
    private static String readFromTextFile(String file) {
        BufferedReader br = null;
        String req_Document = "";

        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader(file));
            while ((sCurrentLine = br.readLine()) != null) {
                System.out.println("Current Line: " + sCurrentLine);

                /*start a new sentence if the sentence contains but */
                if (sCurrentLine.contains("but")) {
                    sCurrentLine = sCurrentLine.replace("but", ". But");
                }
                /*if the sentence is having hyphen, then it is replaced by a space*/
                if (sCurrentLine.contains("-")) {
                    sCurrentLine = sCurrentLine.replace("-", " ");
                }

                req_Document += " " + sCurrentLine;
            }

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

    /*Writing output HashMap to a text file 
     *input: output result type: hashMap
     *output: none
     */
    /*
         
         /*Writing output HashMap to a text file 
         *
         */
        public  static void writeOutputToXMLFile(HashMap output){
        //write to file : "Requirement Output"
            try{
            
//                StringBuffer sbf=new StringBuffer();
//                int count = 1;
                java.util.List<ModelData> classInfoList = 
                                new ArrayList<>();
                java.util.List<Attribute> attributeList;
                java.util.List<Operation> methodList;
                java.util.List<Dependencies> dependencyList = new ArrayList<>();
                Iterator it= output.keySet().iterator();
                while(it.hasNext()){
                
                    ModelData data = new ModelData();
                    attributeList =  new ArrayList<>();
                    methodList = new ArrayList<>();
                    dependencyList = new ArrayList<>();
                
                
                    String className=it.next().toString();
                    String type = "Class";
                    
                    data.setName(className);
                    data.setType(type);
                    data.setVisibility("\"\"");
                    data.setIsStatic("\"\"");
                    data.setId(className);
                    StoringArtefacts store=(StoringArtefacts)output.get(className);
                    HashSet attributes= store.getAttributes();
                    HashSet methods=store.getMethods();
                    HashSet relations=store.getRelationships();
                 
                    
//                    sbf.append("\n\nClass : "+className);
//                    sbf.append("\n\tAttributes : ");
                    for (Object attribute : attributes) {
//                        sbf.append(attribute.toString()+",");
                        Attribute tempAttribute = new Attribute();
                        tempAttribute.setName(attribute.toString());
                        tempAttribute.setType("RequrirementAttribute");
                        tempAttribute.setDataType("\"\"");
                        tempAttribute.setVisibility("\"\"");
                        tempAttribute.setIsStatic("\"\"");
                        
                        attributeList.add(tempAttribute);
                        
                    }
//                    sbf.append("\n\tMethods : ");
                    for (Object method : methods) {
//                        sbf.append(method.toString()+",");
                        Operation operation = new Operation();
                        operation.setName(method.toString());
                        operation.setType("RequrirementMethod");
                        operation.setIsAbstract("\"\"");
                        operation.setVisibility("\"\"");
                        operation.setIsStatic("\"\"");
                        operation.setReturnType("\"\"");
                        operation.setParameterList(new ArrayList<Parameter>());
                        operation.setId("\"\"");
                        
                        methodList.add(operation);
                        
                    }
//                    sbf.append("\n\tRelations : ");
                    for (Object relation : relations) {
                        
                        Dependencies dependencies = new Dependencies();
                        ClassRelation rel=(ClassRelation)relation;
                        
                        dependencies.setDependency_type(rel.getRelationType());
                        
                        
                        if(rel.getChildElement().equals(className)){
                            dependencies.setSource_id(className);
                            dependencies.setTaget_id(rel.getParentElement());
//                            sbf.append("\t <Type> - "+rel.getRelationType()+"(Parent : "+rel.getParentElement()+")");
                        }
                        else if(rel.getParentElement().equals(className)){
                            
                            dependencies.setSource_id(rel.getChildElement());
                            dependencies.setTaget_id(className);
                            //sbf.append("\t <Type> - "+rel.getRelationType()+"(Child : "+rel.getChildElement()+")");
                        }
                        
                        dependencyList.add(dependencies);
                    }
                    
                    data.setAttributeList(attributeList);
                    data.setOperationList(methodList);
                    
                    classInfoList.add(data);
                    
                }
                StaticData.depencyList = dependencyList;
                StaticData.classLst = classInfoList;
                
                WriteToXML writer = new WriteToXML();
                WriteToXML.type = "Requirement";
                writer.createXML();
            
//                System.out.println(sbf.toString());
//            
//                BufferedWriter bwr = new BufferedWriter(new FileWriter(new File("Requirement_Output.txt")));
//               
//                //write contents of StringBuffer to a file
//                bwr.write(sbf.toString());
//               
//                //flush the stream
//                bwr.flush();
//               
//                //close the stream
//                bwr.close();
            
                }catch(Exception e){}
            }

}
