/**
 *
 */
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
 * 18 Nov 2014
 *
 * @author K.Kamalan
 *
 */
public class RequirementSourceCodeMethodManager {

    static List<String> relationNodes = new ArrayList<String>();
    static String projectPath;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static List<String> mapAttributes(String projectPath) {
        Map<ArtefactElement, List<? extends ArtefactSubElement>> sourceCodeattributeArtefactMap = SourceCodeArtefactManager
                .manageArtefactSubElements(ArtefactSubElementType.METHOD);
        Map<ArtefactElement, List<? extends ArtefactSubElement>> reqAttributeArtefactMap = RequirementsManger
                .manageArtefactSubElements(ArtefactSubElementType.METHOD);

        Iterator<Entry<ArtefactElement, List<? extends ArtefactSubElement>>> reqIterator = reqAttributeArtefactMap
                .entrySet().iterator();
        while (reqIterator.hasNext()) {
            Map.Entry reqPairs = reqIterator.next();
            ArtefactElement reqArtefactElement = (ArtefactElement) reqPairs
                    .getKey();
            List<ArtefactSubElement> reqAttributeElements = (List<ArtefactSubElement>) reqPairs
                    .getValue();
            Iterator<Entry<ArtefactElement, List<? extends ArtefactSubElement>>> sourceCodeIterator = sourceCodeattributeArtefactMap
                    .entrySet().iterator();
            while (sourceCodeIterator.hasNext()) {
                Map.Entry sourcePairs = sourceCodeIterator.next();
                ArtefactElement sourceArtefactElement = (ArtefactElement) sourcePairs
                        .getKey();
                List<ArtefactSubElement> sourceAttributeElements = (List<ArtefactSubElement>) sourcePairs
                        .getValue();
                //				System.out.println(LevenshteinDistance.printDistance(sourceArtefactElement.getName(), reqArtefactElement.getName()));
                WordsMap w1 = new WordsMap();
                w1 = SynonymWords.checkSymilarity(sourceArtefactElement.getName(), reqArtefactElement.getName(),
                        sourceArtefactElement.getType());
                                if (w1.isIsMatched()) {
//				if (sourceArtefactElement.getName().equalsIgnoreCase(
//						reqArtefactElement.getName())
//						|| LevenshteinDistance.similarity(
//								reqArtefactElement.getName(),
//								sourceArtefactElement.getName()) > .6) {
                    for (int i = 0; i < reqAttributeElements.size(); i++) {
                        for (int j = 0; j < sourceAttributeElements.size(); j++) {
                            WordsMap w2 = new WordsMap();
                            w2 = SynonymWords.checkSymilarity(reqAttributeElements.get(i).getName(),
                                    sourceAttributeElements.get(j).getName(), reqAttributeElements.get(i).getType());
                            if (w2.isIsMatched()) {

//							if (reqAttributeElements
//									.get(i)
//									.getName()
//									.equalsIgnoreCase(
//											sourceAttributeElements.get(j)
//													.getName())
//									|| LevenshteinDistance.similarity(
//											reqAttributeElements.get(i)
//													.getName(),
//											sourceAttributeElements.get(j)
//													.getName()) > .6) {
                                // System.out.println(reqAttributeElements.get(i).getName()+"//////////"+sourceAttributeElements.get(j).getName());
                                WordsMap w3 = new WordsMap();
                                w3 = SynonymWords.checkSymilarity(
                                        sourceAttributeElements.get(j)
                                        .getName(),
                                        reqAttributeElements.get(i).getName(), sourceAttributeElements.get(i).getType());
                                if (w3.isIsMatched()) {
									// if(reqAttributeElements.get(i).getName().equalsIgnoreCase
                                    // (sourceAttributeElements.get(j).getName())||LevenshteinDistance.similarity(reqAttributeElements.get(i).getName(),
                                    // sourceAttributeElements.get(j).getName())>.6){
                                    // System.out.println(reqAttributeElements.get(i).getSubElementId()+"++++++"+sourceAttributeElements.get(j).getSubElementId());

                                    relationNodes.add(reqAttributeElements
                                            .get(i)
                                            .getSubElementId()
                                            .substring(
                                                    reqAttributeElements.get(i)
                                                    .getSubElementId()
                                                    .length() - 3));
                                    relationNodes.add(sourceAttributeElements
                                            .get(j).getSubElementId());
                                    reqAttributeElements.remove(i); // remove
                                    // mapped
                                    // objects
                                    i--;
                                    sourceAttributeElements.remove(j);
                                    j--;
                                    break;
                                }
                            }
                        }

//						if (reqAttributeElements.size() > 0
//								|| sourceAttributeElements.size() > 0) {
//							System.out
//									.println("There are some conflicts among methods in "
//											+ sourceArtefactElement.getName()
//											+ " class.");
//							if (reqAttributeElements.size() > 0) {
//								System.out
//										.println("Requirement ArtefactFile has following different methods in "
//												+ reqArtefactElement.getName());
////								for (ArtefactSubElement model : reqAttributeElements)
////									System.out.println(((MethodModel) model)
////											.getName());
//							}
//
//							if (sourceAttributeElements.size() > 0) {
//								System.out
//										.println("SourceCodeArtefactFile has following different methods in "
//												+ sourceArtefactElement
//														.getName());
////								for (ArtefactSubElement model : sourceAttributeElements)
////									System.out.println(((MethodModel) model)
////											.getName());
//							}
//
//					}
                    }
//					if(reqAttributeElements.size() > 0 || sourceAttributeElements.size() > 0) {
//						System.out.println("There are some conflicts among methods in "+ sourceArtefactElement.getName() + " class.");
//						if (reqAttributeElements.size() > 0) {
//							System.out.println("Requirement ArtefactFile has following different methods in " 
//										+ reqArtefactElement.getName());
////							for(ArtefactSubElement model : reqAttributeElements)
////								System.out.println(((MethodModel)model).getName());
//						}
//						
//						if (sourceAttributeElements.size() > 0) {
//							System.out.println("SourceCodeArtefactFile has following different methods in " 
//									+ sourceArtefactElement.getName());
////							for(ArtefactSubElement model : sourceAttributeElements)
////								System.out.println(((MethodModel)model).getName());
//
//						}
//					}
                }

            }
            reqIterator.remove();

        }

        return relationNodes;

    }

}
