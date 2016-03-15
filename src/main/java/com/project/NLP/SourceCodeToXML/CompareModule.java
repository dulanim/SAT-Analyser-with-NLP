package com.project.NLP.SourceCodeToXML;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.project.NLP.UMLToXML.xmlwriter.WriteToXML;
import com.project.property.config.xml.writer.Adapter;
import com.project.traceability.manager.ClassManager;
import com.project.traceability.manager.SourceCodeArtefactManager;
import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.ArtefactSubElement;
import com.project.traceability.model.Attribute;
import com.project.traceability.model.ModelData;
import com.project.traceability.model.Operation;
import com.project.traceability.model.WordsMap;
import com.project.traceability.semanticAnalysis.SynonymWords;
import com.project.traceability.staticdata.StaticData;

/**
 * @author shiyam
 * @version 1.1.0
 * @created at 2016 03 08 01:05:52 AM
 */
public class CompareModule {

	
//	private static String fileName1 = "old_version.xml";
//	private static String fileName2 = "new_version.xml";
	private static Map<String,ArtefactElement> modified;
	private static String targetFileName = "modified_version.xml";
	public  static String filePath = "/home/shiyam/Desktop/SatWrks/Jar/";
	private static Map<String,ArtefactElement> oldArtefactMap;
	private static Map<String,ArtefactElement> newArtefactMap;
	//private static Map<String,ArtefactElement> clonedOldArtefactMap;
	private static Map<String,ArtefactElement> artefactElementMap = new HashMap<>();
	static List<String> Classes = new ArrayList<String>();
	public static void main(String[] args) {
		
		//set the project path like as following to run this 
		String projectPath = "/home/shiyam/Desktop/SatWrks/NewSatWrkspaceskal";
		compareFiles(projectPath);
		printData();
		writeToXML();
	}
	
	private static void printData() {
		// TODO Auto-generated method stub
		//System.out.println(modified);
		Iterator<Entry<String, ArtefactElement>> modifiedArtefactIterator = modified
				.entrySet().iterator();
		
		while(modifiedArtefactIterator.hasNext()){
			Entry<String, ArtefactElement> pairs = modifiedArtefactIterator.next();
			System.out.println(" Artefact Name   " + pairs.getValue().getName()
					 + "****Status****" + pairs.getValue().getStatus());
			
			List<ArtefactSubElement> subElementList = pairs.getValue().getArtefactSubElements();
			for(int i=0;i<subElementList.size();i++){
				System.out.println(" Artefact Sub Element Name   " + subElementList.get(i).getName()
						 +"***Type***"+ subElementList.get(i).getType() +"****Status****" + subElementList.get(i).getStatus());
			}
		}
	}
	
	public static void writeToXML(){
		File target = new File(filePath + targetFileName);
		if(!target.exists()){
			try {
				target.createNewFile();
			}catch (IOException ex) {
	            Logger.getLogger(CompareModule.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		Iterator<Entry<String, ArtefactElement>> modifiedArtefactIterator = modified.entrySet()
													.iterator();
		StaticData.classLst = new ArrayList<>();
		while(modifiedArtefactIterator.hasNext()){
			Entry<String, ArtefactElement> pairEntry = modifiedArtefactIterator.next();
			ArtefactElement artefactElement = pairEntry.getValue();
			
			List<ArtefactSubElement> subElements = artefactElement.getArtefactSubElements();
			List<Attribute> attributeList = new ArrayList<>();
			List<Operation> methodList = new ArrayList<>();
			ModelData modelData = new ModelData();
			modelData.setName(artefactElement.getName());
			modelData.setId(artefactElement.getArtefactElementId());
			modelData.setType(artefactElement.getType());
			modelData.setVisibility(artefactElement.getVisibility());
			modelData.setIsStatic("");
			modelData.setStatus(artefactElement.getStatus());
			
			if(subElements != null){
				for(ArtefactSubElement sub:subElements){
					Attribute attribute;
					Operation operation;
					
					if(sub.getType().equalsIgnoreCase("Method")){
						operation = new Operation();
						operation.setId(sub.getSubElementId());
						operation.setIsAbstract("");
						operation.setIsStatic("");
						operation.setName(sub.getName());
						operation.setReturnType(sub.getReturnType());
						operation.setType(sub.getType());
						operation.setVisibility(sub.getVisibility());
						operation.setParamString(sub.getParamString());
						operation.setStatus(sub.getStatus());
						methodList.add(operation);
					}else{
						attribute = new Attribute();
						attribute.setId(sub.getSubElementId());
						attribute.setIsStatic("");
						attribute.setName(sub.getName());
						attribute.setType(sub.getType());
						attribute.setVisibility(sub.getVisibility());
						attribute.setDataType(sub.getVariableType());//have to set brrroooooo!!!!
						attribute.setStatus(sub.getStatus());
						attributeList.add(attribute);
					}
					
					modelData.setAttributeList(attributeList);
					modelData.setOperationList(methodList);
				}
			}
			StaticData.classLst.add(modelData);
			
		}
		WriteToXML xmlWriter = new WriteToXML();
		xmlWriter.type = "Sourcecode";
		xmlWriter.createXML();
		xmlWriter.type = "UMLDiagram";
		StaticData.classLst.clear();
		
	}
	private static void initData(String projectPath ){
		
		SourceCodeArtefactManager.isComparing = "NEW";
		SourceCodeArtefactManager.readXML(projectPath);
		newArtefactMap = new HashMap<>(SourceCodeArtefactManager.getSourceCodeAretefactElements());
		
		SourceCodeArtefactManager.isComparing = "OLD";
		SourceCodeArtefactManager.readXML(projectPath);
		oldArtefactMap = new HashMap<>(SourceCodeArtefactManager.getSourceCodeAretefactElements());
		
		SourceCodeArtefactManager.isComparing = "NONE";
	}
	@Override
		protected Object clone() throws CloneNotSupportedException {
			// TODO Auto-generated method stub
			return super.clone();
		}
	public static void compareFiles(String projectPath ){
//		File file1 = new File(filePath + fileName1);
//		File file2 = new File(filePath + fileName2);

		//String projectPath = "/home/shiyam/Desktop/SatWrks/NewSatWrkspaceskal";
		Adapter.projectPath = projectPath;
		SourceCodeArtefactManager.isComparing = "NEW";
		Classes = ClassManager.getSourceClassName(projectPath);
		initData(projectPath);
		
		if(artefactElementMap != null && artefactElementMap.size()>0)
			artefactElementMap.clear();
		else
			artefactElementMap = new HashMap<>();
		
		if(modified == null)
    		modified = new HashMap<>();
		/*
		 * if root matched, 	
		 * 		sub does not change
		 * if root matched,
		 * 		sub has changed
		 * if root did not match
		 * 		sub did not change
		 * if root did not match sub changed
		 */
		
		Iterator<Entry<String, ArtefactElement>> oldArtefactIterator = oldArtefactMap
				.entrySet().iterator();
		
		Iterator<Entry<String, ArtefactElement>> newArtefactIterator;
		while (oldArtefactIterator.hasNext()) {
			Entry<String, ArtefactElement> pairs = oldArtefactIterator.next();
			ArtefactElement OldArtefactElement = (ArtefactElement) pairs
																	.getValue(); 
			String oldName = OldArtefactElement.getName();
			String oldType =OldArtefactElement.getType();
			if (OldArtefactElement.getType().equalsIgnoreCase("Class") ||
					OldArtefactElement.getType().equalsIgnoreCase("Interface")) {

				newArtefactIterator = newArtefactMap.entrySet().iterator(); 
				while (newArtefactIterator.hasNext()) {
					Entry<String, ArtefactElement> pairs1 = newArtefactIterator.next();
					ArtefactElement newArtefactElement = (ArtefactElement)pairs1.getValue();
		            WordsMap w1 = new WordsMap();
		            String newType = newArtefactElement.getType();
		            String newName = newArtefactElement.getName();
		            
		           
		            SynonymWords.isMatchingXML = "XML Matching";
		            w1 = SynonymWords.checkSymilarity(newName, oldName,
		            		newArtefactElement.getType());
		            SynonymWords.isMatchingXML = "NONE";
		            boolean isMatched = w1.isIsMatched();
//		            if(!isMatched){
//			           //if it is not match by our dictionary 
//		               //call the check similarity algorithm or edit distance
//			           //based on edit distance we find out the similarity
//			           isMatched = MatchWords.compareStrings(newName, oldName);
//		            }
		            if ((newArtefactElement.getType().equalsIgnoreCase("Class") || 
		            			newArtefactElement.getType().equalsIgnoreCase("Interface"))
		            		&& isMatched ) {
		            	
		            	if(newType.equalsIgnoreCase(oldType)){
		            		
		            		String status = w1.getStatus();
		            		
		            		if(status.equalsIgnoreCase("String " + StaticData.MODIFIED_STATUS))
		            			status = StaticData.MODIFIED_STATUS;
		            		else 
		            			status = newArtefactElement.getStatus(); 
			            	modified.put(newArtefactElement.getArtefactElementId(),newArtefactElement);
			            	ArtefactElement artefact_elemnt =new ArtefactElement();
			            	artefact_elemnt.setArtefactElementId(newArtefactElement.getArtefactElementId());
			            	artefact_elemnt.setArtefactSubElements(new ArrayList<ArtefactSubElement>());
			            	artefact_elemnt.setName(newArtefactElement.getName());
			            	artefact_elemnt.setStatus(status);
			            	artefact_elemnt.setType(newArtefactElement.getType());
			            	artefact_elemnt.setVisibility(newArtefactElement.getVisibility());
			            	artefact_elemnt.setInterfaceName(newArtefactElement.getInterfaceName());
			            	artefact_elemnt.setSuperClassNames(newArtefactElement.getSuperClassName());
			            	modified.put(newArtefactElement.getArtefactElementId(),artefact_elemnt);
			            	checkSubElement(OldArtefactElement, 
			            			newArtefactElement,newArtefactElement.getArtefactElementId());
							newArtefactMap.remove(newArtefactElement.getArtefactElementId());
							oldArtefactMap.remove(OldArtefactElement.getArtefactElementId());
							oldArtefactIterator = oldArtefactMap.entrySet().iterator();
							break;
		            	}
					}

				}
			}
		}
			if (oldArtefactMap.size() > 0 || newArtefactMap.size() > 0) {
				oldArtefactIterator = oldArtefactMap.entrySet().iterator();
				newArtefactIterator = newArtefactMap.entrySet().iterator();

				changeStatus(oldArtefactIterator,StaticData.DELETED_STATUS);
				changeStatus(newArtefactIterator,StaticData.ADDED_STATUS);
			}
	}

	private static void changeStatus(Iterator<Entry<String, ArtefactElement>> artefactIterator, String status) {
		// TODO Auto-generated method stub
		while (artefactIterator.hasNext()) {
			Map.Entry<String, ArtefactElement> artefact = artefactIterator.next();
			ArtefactElement artefactElmnt = artefact.getValue();
			String id = "";
			artefactElmnt.setStatus(status);
			
			List<ArtefactSubElement> subArtefactOldElmnt = artefactElmnt.getArtefactSubElements();
			if(subArtefactOldElmnt !=null){
				for(ArtefactSubElement sub:subArtefactOldElmnt){
					sub.setStatus(status);
				}
			}
			id = artefactElmnt.getArtefactElementId();
//			if(modified.containsKey(id)){
//				ArtefactElement elmnt = modified.get(id);
//				String name = elmnt.getName();
//				id = id + name;
//				modified.put(id, elmnt);
//			}else{
				modified.put(id, artefactElmnt);
		}
	}

	private static void checkSubElement(ArtefactElement olderElmnt, 
			ArtefactElement newerElmnt, String artefactElementID){
		
		List<ArtefactSubElement> subOldElmntList = olderElmnt.getArtefactSubElements();
		List<ArtefactSubElement> subNewElmntList = newerElmnt.getArtefactSubElements();
		boolean isMatchedElmnt = false;
		
		//first remove exact Match from artifact list
		seekForExactMatchElement(artefactElementID,subOldElmntList,subNewElmntList);
		
		/*
		 * then checks partial and not matched artifact element 
		 * 	in these both lists (subOldElmntList, subNewElmntList)
		 * 
		 */
		for(int i=0;i<subOldElmntList.size();i++){
			ArtefactSubElement subOldElmnt = subOldElmntList.get(i);
			String subOlderName = subOldElmnt.getName();
			
			for(int j=0;j<subNewElmntList.size();j++){
			   ArtefactSubElement subNewElmnt = subNewElmntList.get(j);
			   String subNewerName = subNewElmnt.getName();
			   
			   WordsMap w2 = new WordsMap();
			   SynonymWords.isMatchingXML = "XML Matching";
               w2 = SynonymWords.checkSymilarity(subOlderName,subNewerName, newerElmnt.getType(),
            		   olderElmnt.getType(),Classes);
               
               boolean isMatched = w2.isIsMatched();
               SynonymWords.isMatchingXML = "NONE";
//               if(!isMatched){
//            	   isMatched = MatchWords.compareStrings(subOlderName, subNewerName);
//               }
               if(isMatched){
            	   String status = w2.getStatus();         		
           		   if(status.equalsIgnoreCase("String " + StaticData.MODIFIED_STATUS))
           			   status = StaticData.MODIFIED_STATUS;
           		   else 
           			   status =subNewElmnt.getStatus(); 
            	   subNewElmnt.setStatus(status);
            	   addToModifiedMap(artefactElementID,subNewElmnt);
            	   subNewElmntList.remove(subNewElmnt);
            	   subOldElmntList.remove(subOldElmnt);
            	   isMatchedElmnt = true;
            	   i--;
            	   break;
            	   
               }
					
			}//for inner loop finishes
			if(!isMatchedElmnt){
				subOldElmnt.setStatus(StaticData.DELETED_STATUS);
				addToModifiedMap(artefactElementID, subOldElmnt);
				subOldElmntList.remove(subOldElmnt);
				i--;
			}
			isMatchedElmnt = false;
			
		}//for outer loop finishes
		
		if(subNewElmntList.size()>0 || subOldElmntList.size()>0){
			if(subNewElmntList.size()>0 ){
				//for remaining data in newer sub artifact xml 
				for(int i=0;i<subNewElmntList.size();i++){
				   ArtefactSubElement subNewElmnt = subNewElmntList.get(i);
	         	   subNewElmnt.setStatus(StaticData.ADDED_STATUS);
	         	   addToModifiedMap(artefactElementID,subNewElmnt);
				}
			}
			
			if(subOldElmntList.size()>0){
				//for remaining data in older sub artifact xml 
				for(int i=0;i<subOldElmntList.size();i++){
				   ArtefactSubElement subOldElmnt = subOldElmntList.get(i);
				   subOldElmnt.setStatus(StaticData.DELETED_STATUS);
	         	   addToModifiedMap(artefactElementID,subOldElmnt);
				}
			}
		}
	}

	private static void seekForExactMatchElement(String artefactElementID,List<ArtefactSubElement> subOldElmntList,
			List<ArtefactSubElement> subNewElmntList) {
		
		for(int i=0;i<subOldElmntList.size();i++){
			
			ArtefactSubElement subOldElmnt = subOldElmntList.get(i);
			String subOlderName = subOldElmnt.getName();
			String subOldType = subOldElmnt.getType();
			for(int j=0;j<subNewElmntList.size();j++){
				ArtefactSubElement subNewElmnt = subNewElmntList.get(j);
				String subNewerName = subNewElmnt.getName();
				String subNewType = subNewElmnt.getType();
		        if(subOldType.equalsIgnoreCase(subNewType) &&
		        			subOlderName.equalsIgnoreCase(subNewerName)){
		        	// matched case
		           addToModifiedMap(artefactElementID,subNewElmnt);
	         	   subNewElmntList.remove(subNewElmnt);
	         	   subOldElmntList.remove(subOldElmnt);
	         	   
	         	   i--;
	         	   break;
	         	   
		        }
			}
		}
	}

	private static void addToModifiedMap(String artefactElementID,ArtefactSubElement subNewElmnt) {
		// TODO Auto-generated method stub
	   ArtefactElement artElmnt = modified.get(artefactElementID);
 	   List<ArtefactSubElement> listSubElmnt = new ArrayList<>(artElmnt.getArtefactSubElements());
 	   String id = "";
 	   if(listSubElmnt != null)
 		   listSubElmnt.add(subNewElmnt);
 	   artElmnt.setArtefactSubElements(listSubElmnt);
 	   
 	   id = artElmnt.getArtefactElementId();
// 	   if(modified.containsKey(id)){
//			ArtefactElement elmnt = modified.get(id);
//			String name = elmnt.getName();
//			id = id + name;
//			modified.put(id, elmnt);
// 	   }else{
			modified.put(id, artElmnt);
// 	   }
		
	}
}


