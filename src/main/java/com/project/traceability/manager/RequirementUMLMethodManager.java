package com.project.traceability.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.project.traceability.ir.LevenshteinDistance;
import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.ArtefactSubElement;
import com.project.traceability.model.MethodModel;
import com.project.traceability.semanticAnalysis.SynonymWords;
import com.project.traceability.model.WordsMap;
import com.project.traceability.utils.Constants.ArtefactSubElementType;

/**
 * 19 Nov 2014
 *
 * @author K.Kamalan
 *
 */
public class RequirementUMLMethodManager {

    static List<String> relationNodes = new ArrayList<String>();

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static List<String> mapAttributes(String projecpath) {
        Map<ArtefactElement, List<? extends ArtefactSubElement>> reqAttributeArtefactMap = RequirementsManger
                .manageArtefactSubElements(ArtefactSubElementType.METHOD);
        Map<ArtefactElement, List<ArtefactSubElement>> UMLattributeArtefactMap = UMLArtefactManager
                .manageArtefactSubElements(ArtefactSubElementType.METHOD);

        Iterator<Entry<ArtefactElement, List<ArtefactSubElement>>> UMLIterator = UMLattributeArtefactMap
                .entrySet().iterator();
        while (UMLIterator.hasNext()) {
            Map.Entry UMLPairs = UMLIterator.next();
            ArtefactElement UMLArtefactElement = (ArtefactElement) UMLPairs
                    .getKey();
            List<ArtefactSubElement> UMLAttributeElements = (List<ArtefactSubElement>) UMLPairs
                    .getValue();
            Iterator<Entry<ArtefactElement, List<? extends ArtefactSubElement>>> reqIterator = reqAttributeArtefactMap
                    .entrySet().iterator();
            while (reqIterator.hasNext()) {
                Map.Entry reqPairs = reqIterator.next();
                ArtefactElement reqArtefactElement = (ArtefactElement) reqPairs
                        .getKey();
                List<ArtefactSubElement> reqAttributeElements = (List<ArtefactSubElement>) reqPairs
                        .getValue();
                WordsMap w1 = new WordsMap();
                w1 = SynonymWords.checkSymilarity(reqArtefactElement.getName(), UMLArtefactElement.getName(), reqArtefactElement.getType());
                                if (w1.isIsMatched()) {
//				if (reqArtefactElement.getName().equalsIgnoreCase(
//						UMLArtefactElement.getName())
//						|| LevenshteinDistance.similarity(
//								reqArtefactElement.getName(),
//								UMLArtefactElement.getName()) > .6) {
                    for (int i = 0; i < UMLAttributeElements.size(); i++) {
                        for (int j = 0; j < reqAttributeElements.size(); j++) {
                            WordsMap w2 = new WordsMap();
                            w2 = SynonymWords.checkSymilarity(UMLAttributeElements.get(i).getName(), reqAttributeElements.get(j).getName(),
                                    UMLAttributeElements.get(i).getType());
                            if (w2.isIsMatched()) {
//							if (UMLAttributeElements
//									.get(i)
//									.getName()
//									.equalsIgnoreCase(
//											reqAttributeElements.get(j)
//													.getName())
//									|| LevenshteinDistance.similarity(
//											UMLAttributeElements.get(i)
//													.getName(),
//											reqAttributeElements.get(j)
//													.getName()) > .6) {

                                // System.out.println(UMLAttributeElements.get(i).getSubElementId()+"***********"+reqAttributeElements.get(j).getSubElementId());
                                WordsMap w3 = new WordsMap();
                                w3 = SynonymWords.checkSymilarity(
                                        UMLAttributeElements.get(i).getName(),
                                        reqAttributeElements.get(j).getName(), UMLAttributeElements.get(i).getType());
                                if (w3.isIsMatched()) {
									// if(UMLAttributeElements.get(i).getName().equalsIgnoreCase
                                    // (reqAttributeElements.get(j).getName())||LevenshteinDistance.similarity(UMLAttributeElements.get(i).getName(),
                                    // reqAttributeElements.get(j).getName())>.6){

                                    relationNodes.add(reqAttributeElements
                                            .get(j)
                                            .getSubElementId()
                                            .substring(
                                                    reqAttributeElements.get(j)
                                                    .getSubElementId()
                                                    .length() - 3));
                                    relationNodes.add(UMLAttributeElements.get(
                                            i).getSubElementId());
                                    UMLAttributeElements.remove(i); // remove
                                    // mapped
                                    // objects
                                    i--;
                                    reqAttributeElements.remove(j);
                                    j--;
                                    break;
                                }
                            }
                        }

//						if (UMLAttributeElements.size() > 0
//								|| reqAttributeElements.size() > 0) {
//							System.out
//									.println("There are some conflicts among methods in "
//											+ reqArtefactElement.getName()
//											+ " class.");
//							if (UMLAttributeElements.size() > 0) {
//								System.out
//										.println("UMLArtefactFile has following different methods in "
//												+ UMLArtefactElement.getName());
////								for (ArtefactSubElement model : UMLAttributeElements)
////									System.out.println(((MethodModel) model)
////											.getName());
//							}
//
//							if (reqAttributeElements.size() > 0) {
//								System.out
//										.println("Requirement ArtefactFile has following different methods in "
//												+ reqArtefactElement.getName());
////								for (ArtefactSubElement model : reqAttributeElements)
////									System.out.println(((MethodModel) model)
////											.getName());
//							}
//
//						}
//						if (UMLAttributeElements.size() > 0
//								|| reqAttributeElements.size() > 0) {
//							System.out
//									.println("There are some conflicts among methods in "
//											+ reqArtefactElement.getName()
//											+ " class.");
//							if (UMLAttributeElements.size() > 0) {
//								System.out
//										.println("UMLArtefactFile has following different methods in "
//												+ UMLArtefactElement.getName());
////								for (ArtefactSubElement model : UMLAttributeElements)
////									System.out.println(((MethodModel) model)
////											.getName());
//							}
//
//							if (reqAttributeElements.size() > 0) {
//								System.out
//										.println("Requirement ArtefactFile has following different methods in "
//												+ reqArtefactElement.getName());
////								for (ArtefactSubElement model : reqAttributeElements)
////									System.out.println(((MethodModel) model)
////											.getName());
//
//							}
//						}
                    }

                }

            }
            UMLIterator.remove();
        }

        return relationNodes;

    }

}
