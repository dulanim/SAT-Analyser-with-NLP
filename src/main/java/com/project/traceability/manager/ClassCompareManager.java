package com.project.traceability.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.ArtefactSubElement;
import com.project.traceability.semanticAnalysis.SynonymWords;
import com.project.traceability.model.WordsMap;

public class ClassCompareManager {

	List<String> sourceCodeClasses = new ArrayList<String>();
	List<String> UMLClasses = new ArrayList<String>();
	static List<String> relationNodes = new ArrayList<String>();
	static String projectPath;

	/**
	 * check whether the designed classes are implemented in sourcecode
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<String> compareClassNames(String projectPath) {
		
		Map<String, ArtefactElement> UMLMap = UMLArtefactManager.UMLAretefactElements;
		Iterator<Entry<String, ArtefactElement>> UMLIterator = UMLMap
				.entrySet().iterator();
		Map<String, ArtefactElement> sourceMap = SourceCodeArtefactManager.sourceCodeAretefactElements;
		Iterator<Entry<String, ArtefactElement>> sourceIterator = null;
		while (UMLIterator.hasNext()) {
			Map.Entry umlPairs = UMLIterator.next();
			ArtefactElement UMLArtefactElement = (ArtefactElement) umlPairs
					.getValue();
			String name = UMLArtefactElement.getName();
			if (UMLArtefactElement.getType().equalsIgnoreCase("Class")) {
				sourceIterator = sourceMap.entrySet().iterator();
				while (sourceIterator.hasNext()) {
					Map.Entry sourcePairs = sourceIterator.next();
					ArtefactElement sourceArtefactElement = (ArtefactElement) sourcePairs
							.getValue();
                                        WordsMap w3 = new WordsMap();
                                        w3 = SynonymWords.checkSymilarity(sourceArtefactElement.getName(),
							name,sourceArtefactElement.getType());
					if(sourceArtefactElement.getType().equalsIgnoreCase("Class") && w3.isIsMatched()){
//					if (sourceArtefactElement.getType().equalsIgnoreCase(
//							"Class")
//							&& sourceArtefactElement.getName()
//									.equalsIgnoreCase(name)) {
						relationNodes.add(UMLArtefactElement
								.getArtefactElementId());
						relationNodes.add(sourceArtefactElement
								.getArtefactElementId());
						sourceMap.remove(sourceArtefactElement
								.getArtefactElementId());
						UMLMap.remove(UMLArtefactElement.getArtefactElementId());
						UMLIterator = UMLMap.entrySet().iterator();
						break;
					}
				}
			}
		}
		if (sourceMap.size() > 0 || UMLMap.size() > 0) {
			UMLIterator = UMLMap.entrySet().iterator();
			sourceIterator = sourceMap.entrySet().iterator();
//			System.out
//					.println("UMLArtefactFile has following different classes from SourceCodeArtefactFile:");
//			while (UMLIterator.hasNext()) {
//				Map.Entry<String, ArtefactElement> artefact = UMLIterator
//						.next();
////				System.out.println(artefact.getValue().getName());
//			}
//			System.out
//					.println("SourceCodeArtefactFile has following different classes from UMLArtefactFile:");
//			while (sourceIterator.hasNext()) {
//				Map.Entry<String, ArtefactElement> artefact = sourceIterator
//						.next();
////				System.out.println(artefact.getValue().getName());
//			}
		}

		return relationNodes;
	}

	@SuppressWarnings("rawtypes")
	public static int compareClassCount() {
		SourceCodeArtefactManager.readXML(projectPath);
		UMLArtefactManager.readXML(projectPath);
		Iterator it = SourceCodeArtefactManager.sourceCodeAretefactElements
				.entrySet().iterator();
		int countSourceClass = 0;
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			ArtefactElement artefactElement = (ArtefactElement) pairs
					.getValue();
			if (artefactElement.getType().equalsIgnoreCase("Class")) {

				countSourceClass++;
			}
			List<ArtefactSubElement> artefactSubElements = artefactElement
					.getArtefactSubElements();
			it.remove(); // avoids a ConcurrentModificationException
		}
		
		Iterator it1 = UMLArtefactManager.UMLAretefactElements.entrySet()
				.iterator();
		int countUMLClass = 0;
		while (it1.hasNext()) {
			Map.Entry pairs = (Entry) it1.next();
			ArtefactElement artefactElement = (ArtefactElement) pairs
					.getValue();
			if (artefactElement.getType().equalsIgnoreCase("Class")) {
				countUMLClass++;
			}
			List<ArtefactSubElement> artefactSubElements = artefactElement
					.getArtefactSubElements();
			it1.remove(); // avoids a ConcurrentModificationException
		}

		if (countSourceClass == countUMLClass) {
			System.out.println("class compared");
		}
		return countSourceClass;
	}

}
