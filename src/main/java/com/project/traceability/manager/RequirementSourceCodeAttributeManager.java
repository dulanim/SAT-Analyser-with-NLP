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
import com.project.traceability.model.AttributeModel;
import com.project.traceability.semanticAnalysis.SynonymWords;
import com.project.traceability.model.WordsMap;
import com.project.traceability.utils.Constants.ArtefactSubElementType;

/**
 * 17 Nov 2014
 *
 * @author K.Kamalan
 *
 */
public class RequirementSourceCodeAttributeManager {

    static List<String> relationNodes = new ArrayList<String>();
    static Map<ArtefactElement, List<? extends ArtefactSubElement>> sourceCodeSubArtefacts = SourceCodeArtefactManager
            .manageArtefactSubElements(ArtefactSubElementType.ATTRIBUTE);			//get read source code attribute artifacts
    static Map<ArtefactElement, List<? extends ArtefactSubElement>> reqSubArtefacts = RequirementsManger
            .manageArtefactSubElements(ArtefactSubElementType.ATTRIBUTE);
    static String projectPath;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static List<String> mapAttributes(String projectPath) {
        RequirementSourceCodeAttributeManager.projectPath = projectPath;

        Map<ArtefactElement, List<? extends ArtefactSubElement>> sourceCodeattributeArtefactMap = sourceCodeSubArtefacts;
        Map<ArtefactElement, List<? extends ArtefactSubElement>> reqAttributeArtefactMap = reqSubArtefacts;
        Iterator<Entry<ArtefactElement, List<? extends ArtefactSubElement>>> reqIterator = reqAttributeArtefactMap
                .entrySet().iterator();

        while (reqIterator.hasNext()) {
            Map.Entry reqPairs = reqIterator.next();
            ArtefactElement reqArtefactElement = (ArtefactElement) reqPairs
                    .getKey();
            List<AttributeModel> reqAttributeElements = (List<AttributeModel>) reqPairs
                    .getValue();
            Iterator<Entry<ArtefactElement, List<? extends ArtefactSubElement>>> sourceCodeIterator
                    = sourceCodeattributeArtefactMap.entrySet().iterator();
            while (sourceCodeIterator.hasNext()) {
                Map.Entry sourcePairs = sourceCodeIterator.next();
                ArtefactElement sourceArtefactElement = (ArtefactElement) sourcePairs
                        .getKey();
                List<AttributeModel> sourceAttributeElements = (List<AttributeModel>) sourcePairs
                        .getValue();
                //				System.out.println(LevenshteinDistance.printDistance(sourceArtefactElement.getName(), reqArtefactElement.getName()));
                WordsMap w1 = new WordsMap();
                w1 = SynonymWords.checkSymilarity(sourceArtefactElement.getName(), reqArtefactElement.getName(),
                        sourceArtefactElement.getType());
                if (w1.isIsMatched()) {
//				if (sourceArtefactElement.getName().equalsIgnoreCase(reqArtefactElement.getName())
//						||LevenshteinDistance.similarity(reqArtefactElement.getName(), sourceArtefactElement.getName())>.6) {
                    for (int i = 0; i < reqAttributeElements.size(); i++) {
                        AttributeModel reqAttribute = reqAttributeElements.get(i);
                        for (int j = 0; j < sourceAttributeElements.size(); j++) {
                            AttributeModel sourceAttribute = sourceAttributeElements.get(j);
                            WordsMap w2 = new WordsMap();
                            w2 = SynonymWords.checkSymilarity(sourceAttribute.getName(), reqAttribute.getName(),
                                    sourceAttribute.getType());
                            if (w2.isIsMatched()) {
                                relationNodes.add(reqAttribute
                                        .getSubElementId().substring(reqAttribute
                                                .getSubElementId().length() - 3));
                                relationNodes.add(sourceAttribute.getSubElementId());
                                reqAttributeElements.remove(reqAttribute);
                                sourceAttributeElements.remove(sourceAttribute);
                                i--;
                                j--;
                                break;
                            }
                        }
                    }
//					if(reqAttributeElements.size() > 0 || sourceAttributeElements.size() > 0) {
//						System.out.println("There are some conflicts among attributes in "+ sourceArtefactElement.getName() 
//									+ " class.");
//						if (reqAttributeElements.size() > 0) {
//							System.out.println("Requirement ArtefactFile has following different attributes in " 
//										+ reqArtefactElement.getName());
//							for(AttributeModel model : reqAttributeElements)
//								System.out.println(model.getName());
//						}
//						
//						if (sourceAttributeElements.size() > 0) {
//							System.out.println("SourceCodeArtefactFile has following different attributes in " 
//									+ sourceArtefactElement.getName());
//							for(AttributeModel model : sourceAttributeElements)
//								System.out.println(model.getName());
//						}
//					}
                }
            }
            reqIterator.remove();
        }
        return relationNodes;
    }

}
