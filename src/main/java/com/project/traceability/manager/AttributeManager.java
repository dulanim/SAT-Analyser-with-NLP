package com.project.traceability.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.ArtefactSubElement;
import com.project.traceability.model.AttributeModel;
import com.project.traceability.semanticAnalysis.SynonymWords;
import com.project.traceability.model.WordsMap;
import com.project.traceability.utils.Constants.ArtefactSubElementType;

public class AttributeManager {

	static List<String> relationNodes = new ArrayList<String>();
	static Map<ArtefactElement, List<? extends ArtefactSubElement>> sourceCodeSubArtefacts = SourceCodeArtefactManager
			.manageArtefactSubElements(ArtefactSubElementType.ATTRIBUTE);			//get read source code attribute artifacts
	static Map<ArtefactElement, List<ArtefactSubElement>> UMLSubArtefacts = UMLArtefactManager
			.manageArtefactSubElements(ArtefactSubElementType.ATTRIBUTE);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<String> mapAttributes(String projectPath) {
		//SourceCodeArtefactManager.readXML(projectPath);
		//UMLArtefactManager.readXML(projectPath);
		Map<ArtefactElement, List<? extends ArtefactSubElement>> sourceCodeattributeArtefactMap = sourceCodeSubArtefacts;
		Map<ArtefactElement, List<ArtefactSubElement>> UMLattributeArtefactMap = UMLSubArtefacts;
		Iterator<Entry<ArtefactElement, List<ArtefactSubElement>>> UMLIterator = UMLattributeArtefactMap
				.entrySet().iterator();

		while (UMLIterator.hasNext()) {
			Map.Entry UMLPairs = UMLIterator.next();
			ArtefactElement UMLArtefactElement = (ArtefactElement) UMLPairs
					.getKey();
			List<ArtefactSubElement> UMLAttributeElements = UMLArtefactElement.getArtefactSubElements();
			Iterator<Entry<ArtefactElement, List<? extends ArtefactSubElement>>> sourceCodeIterator = 
					sourceCodeattributeArtefactMap.entrySet().iterator();
			
			while (sourceCodeIterator.hasNext()) {
				Map.Entry sourcePairs = sourceCodeIterator.next();
				ArtefactElement sourceArtefactElement = (ArtefactElement) sourcePairs
						.getKey();
				List<AttributeModel> sourceAttributeElements = (List<AttributeModel>) sourcePairs
						.getValue();
                                WordsMap w1 = new WordsMap();
                                w1 = SynonymWords.checkSymilarity(sourceArtefactElement.getName(), UMLArtefactElement.getName(),
						sourceArtefactElement.getType());
				if(w1.isIsMatched()){
//				if (sourceArtefactElement.getName().equalsIgnoreCase(UMLArtefactElement.getName())) {
					for (int i = 0; i < UMLAttributeElements.size(); i++) {
						ArtefactSubElement UMLAttribute = UMLAttributeElements.get(i);
						for (int j = 0; j < sourceAttributeElements.size(); j++) {
							AttributeModel sourceAttribute = sourceAttributeElements.get(j);
                                                        WordsMap w2 = new WordsMap();
                                                        w2 = SynonymWords.checkSymilarity(UMLAttribute.getName(), UMLAttribute.getName(),
									UMLAttribute.getType());
							if(w2.isIsMatched()){
//							if (UMLAttribute.getName().equalsIgnoreCase(sourceAttribute.getName())) {
								relationNodes.add(UMLAttribute.getSubElementId());
								relationNodes.add(sourceAttribute.getSubElementId());
								UMLAttributeElements.remove(UMLAttribute);
								sourceAttributeElements.remove(sourceAttribute);
								i--; j--;
								break;
							}
						}
					}
//					if(UMLAttributeElements.size() > 0 || sourceAttributeElements.size() > 0) {
//						System.out.println("There are some conflicts among attributes in "+ sourceArtefactElement.getName() 
//									+ " class.");
//						if (UMLAttributeElements.size() > 0) {
//							System.out.println("UMLArtefactFile has following different attributes in " 
//										+ UMLArtefactElement.getName());
//							for(ArtefactSubElement model : UMLAttributeElements)
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
			UMLIterator.remove();
		}
		// RelationManager.createXML(relationNodes);
		return relationNodes;
	}
}
