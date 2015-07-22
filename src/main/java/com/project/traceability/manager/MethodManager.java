package com.project.traceability.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.ArtefactSubElement;
import com.project.traceability.model.MethodModel;
import com.project.traceability.model.ParameterModel;
import com.project.traceability.semanticAnalysis.SynonymWords;
import com.project.traceability.model.WordsMap;
import com.project.traceability.utils.Constants.ArtefactSubElementType;

public class MethodManager {
	
	static List<String> relationNodes = new ArrayList<String>();
	static String projectPath;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<String> mapAttributes(String projectPath) {
		//SourceCodeArtefactManager.readXML(projectPath);
		//UMLArtefactManager.readXML(projectPath);
		Map<ArtefactElement, List<? extends ArtefactSubElement>> sourceCodeattributeArtefactMap = SourceCodeArtefactManager
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
			Iterator<Entry<ArtefactElement, List<? extends ArtefactSubElement>>> sourceCodeIterator 
								= sourceCodeattributeArtefactMap
					.entrySet().iterator();
			while (sourceCodeIterator.hasNext()) {
				Map.Entry sourcePairs = sourceCodeIterator.next();
				ArtefactElement sourceArtefactElement = (ArtefactElement) sourcePairs
						.getKey();
				List<ArtefactSubElement> sourceAttributeElements = (List<ArtefactSubElement>) sourcePairs
						.getValue();
                                WordsMap w4= new WordsMap();
                                w4 = SynonymWords.checkSymilarity(sourceArtefactElement.getName(), UMLArtefactElement.getName(),
						sourceArtefactElement.getType());
				if(w4.isIsMatched()){
//				if (sourceArtefactElement.getName().equalsIgnoreCase(
//						UMLArtefactElement.getName())) {
					for(int i = 0; i < UMLAttributeElements.size(); i++){
						for(int j = 0; j < sourceAttributeElements.size(); j++){
                                                    WordsMap w5 = new WordsMap();
                                                    w4 = SynonymWords.checkSymilarity(UMLAttributeElements.get(i).getName(), 
									sourceAttributeElements.get(j).getName(),UMLAttributeElements.get(i).getType()); 
							if(w5.isIsMatched()){
//							if(UMLAttributeElements.get(i).getName().equalsIgnoreCase
//									(sourceAttributeElements.get(j).getName())){
								if(((MethodModel)UMLAttributeElements.get(i)).getParameters() == null && 
										((MethodModel)sourceAttributeElements.get(j)).getParameters() == null){
									relationNodes.add(UMLAttributeElements.get(i).getSubElementId());
									relationNodes.add(sourceAttributeElements.get(j).getSubElementId());
									UMLAttributeElements.remove(i); 	//remove mapped objects
									i--;
									sourceAttributeElements.remove(j);
									j--;
									break;
								}
								
								else if(checkParameters(((MethodModel)UMLAttributeElements.get(i)).getParameters(), 
									((MethodModel)sourceAttributeElements.get(j)).getParameters())){
									relationNodes.add(UMLAttributeElements.get(i).getSubElementId());
									relationNodes.add(sourceAttributeElements.get(j).getSubElementId());
									UMLAttributeElements.remove(UMLAttributeElements.get(i));
									i--;
									sourceAttributeElements.remove(j);
									j--;
									break;
								}
							}
						}
					}
//					if(UMLAttributeElements.size() > 0 || sourceAttributeElements.size() > 0) {
//						System.out.println("There are some conflicts among methods in "+ sourceArtefactElement.getName() + " class.");
//						if (UMLAttributeElements.size() > 0) {
//							System.out.println("UMLArtefactFile has following different methods in " 
//										+ UMLArtefactElement.getName());
//							for(ArtefactSubElement model : UMLAttributeElements)
//								System.out.println(((MethodModel)model).getName());
//						}
//						
//						if (sourceAttributeElements.size() > 0) {
//							System.out.println("SourceCodeArtefactFile has following different methods in " 
//									+ sourceArtefactElement.getName());
//							for(ArtefactSubElement model : sourceAttributeElements)
//								System.out.println(((MethodModel)model).getName());
//						}
//					}
				}
			}
			UMLIterator.remove();
		}
		return relationNodes;
	}

	public static boolean checkParameters(List<ParameterModel> UMLParameters, List<ParameterModel> sourceCodeParameters){
		boolean isEqual = false; 
		if(UMLParameters == null || sourceCodeParameters == null)
			return false;
		else {
			for(int i = 0; i < UMLParameters.size(); i++){
				for(int j = 0; j < sourceCodeParameters.size(); j++){
					if(UMLParameters.get(i).getName().trim().equals(sourceCodeParameters.get(j).getName().trim()) &&
							UMLParameters.get(i).getVariableType().equals(sourceCodeParameters.get(j).getVariableType())){
						UMLParameters.remove(i);
						i--;
						sourceCodeParameters.remove(j);
						j--;
						break;
					}
				}
			}
//			if(UMLParameters.size() > 0 || sourceCodeParameters.size() > 0){
//				System.out.println("There are some conflicts among parameters of methods.");
//				if (UMLParameters.size() > 0) {
//					System.out.println("UMLArtefactFile has following different parameters");
//					for(ParameterModel model : UMLParameters)
//						System.out.println(((ParameterModel)model).getName());
//				}
//				
//				if (sourceCodeParameters.size() > 0) {
//					System.out.println("SourceCodeArtefactFile has following different parameters");
//					for(ParameterModel model : sourceCodeParameters)
//						System.out.println(((ParameterModel)model).getName());
//				}
//			}
		}
		if(UMLParameters.size() == 0 && sourceCodeParameters.size() == 0)
			isEqual = true;
		return isEqual;
	}

}
