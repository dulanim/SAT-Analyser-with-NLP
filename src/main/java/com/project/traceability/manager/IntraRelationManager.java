/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.manager;

import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.ArtefactSubElement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author K.Kamalan
 */
public class IntraRelationManager {

    static List<String> relationNodes = new ArrayList<String>();
    static List<String> relationNodes1 = new ArrayList<String>();
    static List<String> relationNodes2 = new ArrayList<String>();

    static String projectPath;

    public static List<String> getReqIntraRelation(String projectPath) {
        IntraRelationManager.projectPath = projectPath;
        RequirementsManger.readXML(projectPath);
        Map<String, ArtefactElement> reqMap = RequirementsManger.requirementArtefactElements;
        Iterator<Map.Entry<String, ArtefactElement>> requirementIterator = reqMap
                .entrySet().iterator();
        while (requirementIterator.hasNext()) {
            Map.Entry pairs = requirementIterator.next();
            ArtefactElement reqArtefactElement = (ArtefactElement) pairs
                    .getValue();
            List<ArtefactSubElement> reqAttributeElements = reqArtefactElement
                    .getArtefactSubElements();
            if (reqArtefactElement.getType().equalsIgnoreCase("Class")) {
                System.out.println(reqArtefactElement.getName() + "%%%%%%%%%%%%%%%%");
                compareSubElements(reqArtefactElement);
            }
        }

        return relationNodes;
    }

    public static void compareSubElements(ArtefactElement reqArtefactElement) {
        List<ArtefactSubElement> reqAttributeElements = reqArtefactElement
                .getArtefactSubElements();
        for (int i = 0; i < reqAttributeElements.size() && reqAttributeElements.get(i).getType().equalsIgnoreCase("Field"); i++) {
            //System.out.println("KAMALA" + reqAttributeElements.get(i).getName());
            int count = 0;
            for (int j = 0; j < reqAttributeElements.size(); j++) {
                if (reqAttributeElements.get(j).getType().equalsIgnoreCase("Method")) {
                    if (reqAttributeElements.get(i).getName().equalsIgnoreCase(reqAttributeElements.get(j).getName().substring(3))) {
                        //System.out.println(reqAttributeElements.get(i).getName() + "KAMALAN" + reqAttributeElements.get(j).getName());
                        count++;
                        if(reqAttributeElements.get(j).getName().toLowerCase().contains("get")){
                            //System.out.println(reqAttributeElements.get(i).getSubElementId()+":"+reqAttributeElements.get(i).getName()+":"+reqAttributeElements.get(j).getName()+":"+reqAttributeElements.get(j).getSubElementId());
                            relationNodes.add(reqAttributeElements.get(i).getSubElementId().substring(reqAttributeElements.get(i).getSubElementId().indexOf("RQ")));
                            relationNodes.add("Getter Method");
                            relationNodes.add(reqAttributeElements.get(j).getSubElementId().substring(reqAttributeElements.get(j).getSubElementId().indexOf("RQ")));
                        }else{
                            //System.out.println(reqAttributeElements.get(i).getSubElementId()+":"+reqAttributeElements.get(i).getName()+":"+reqAttributeElements.get(j).getName()+":"+reqAttributeElements.get(j).getSubElementId());
                            relationNodes.add(reqAttributeElements.get(i).getSubElementId().substring(reqAttributeElements.get(i).getSubElementId().indexOf("RQ")));
                            relationNodes.add("Setter Method");
                            relationNodes.add(reqAttributeElements.get(j).getSubElementId().substring(reqAttributeElements.get(j).getSubElementId().indexOf("RQ")));
                        }
                        if (count == 2) {
                            break;
                        }
                    }

                }

            }
        }
    }
    
    public static List<String> getSourceIntraRelation(String projectPath) {
        SourceCodeArtefactManager.readXML(projectPath);
        Map<String, ArtefactElement> sourceMap = SourceCodeArtefactManager.sourceCodeAretefactElements;
        Iterator<Map.Entry<String, ArtefactElement>> sourceIterator = null;
        sourceIterator = sourceMap.entrySet().iterator();
        while (sourceIterator.hasNext()) {
            Map.Entry pairs1 = sourceIterator.next();
            ArtefactElement sourceArtefactElement = (ArtefactElement) pairs1
                    .getValue();
            List<ArtefactSubElement> sourceAttributeElements = sourceArtefactElement
                    .getArtefactSubElements();
            for (int i = 0; i < sourceAttributeElements.size() && sourceAttributeElements.get(i).getType().equalsIgnoreCase("Field"); i++) {
                int count = 0;
                for (int j = 0; j < sourceAttributeElements.size(); j++) {
                    //System.out.println(sourceAttributeElements.get(j).getType());
                    if (sourceAttributeElements.get(j).getType().equalsIgnoreCase("Method")) {
                        
                        if (sourceAttributeElements.get(i).getName().equalsIgnoreCase(sourceAttributeElements.get(j).getName().substring(3))) {
                            count++;
                            if (sourceAttributeElements.get(j).getName().contains("get")) {
                                relationNodes1.add(sourceAttributeElements.get(i).getSubElementId());
                                relationNodes1.add("Getter Method");
                                relationNodes1.add(sourceAttributeElements.get(j).getSubElementId());
                            } else {
                                relationNodes1.add(sourceAttributeElements.get(i).getSubElementId());
                                relationNodes1.add("Setter Method");
                                relationNodes1.add(sourceAttributeElements.get(j).getSubElementId());

                            }
                            if (count == 2) {
                                break;
                            }
                        }
                    }
                }

            }

        }
        
        return relationNodes1;
    }
    
    public static List<String> getUMLIntraRelation(String projectPath){
        UMLArtefactManager.readXML(projectPath);
	Map<String, ArtefactElement> UMLMap = UMLArtefactManager.UMLAretefactElements;
	Iterator<Map.Entry<String, ArtefactElement>> umlIterator = null;
        
        umlIterator = UMLMap.entrySet().iterator();
        while (umlIterator.hasNext()) {
            Map.Entry pairs1 = umlIterator.next();
            ArtefactElement UMLArtefactElement = (ArtefactElement) pairs1
							.getValue();
            List<ArtefactSubElement> UMLAttributeElements = UMLArtefactElement
				.getArtefactSubElements();
            for (int i = 0; i < UMLAttributeElements.size() && UMLAttributeElements.get(i).getType().equalsIgnoreCase("Field"); i++) {
                int count = 0;
                for(int j = 0; j<UMLAttributeElements.size(); j++){
                    //System.out.println(UMLAttributeElements.get(j).getType());
                    if(UMLAttributeElements.get(j).getType().equalsIgnoreCase("Method")){
                        if(UMLAttributeElements.get(i).getName().equalsIgnoreCase(UMLAttributeElements.get(j).getName().substring(3))){
                            count++;
                            if(UMLAttributeElements.get(j).getName().contains("get")){
                                relationNodes2.add(UMLAttributeElements.get(i).getSubElementId());
                                relationNodes2.add("Getter Method");
                                relationNodes2.add(UMLAttributeElements.get(j).getSubElementId());
                            }else{
                                relationNodes2.add(UMLAttributeElements.get(i).getSubElementId());
                                relationNodes2.add("Setter Method");
                                relationNodes2.add(UMLAttributeElements.get(j).getSubElementId());
                            }
                        }
                    }
                }
                
            }
        }
         
        return relationNodes2;
    }
}
