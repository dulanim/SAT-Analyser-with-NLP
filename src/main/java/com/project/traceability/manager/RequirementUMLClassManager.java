package com.project.traceability.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import com.project.traceability.GUI.CompareWindow;
import com.project.traceability.GUI.HomeGUI;
import com.project.traceability.common.PropertyFile;
import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.ArtefactSubElement;
import com.project.traceability.semanticAnalysis.SynonymWords;
import com.project.traceability.model.WordsMap;
import com.project.traceability.utils.Constants.ImageType;

public class RequirementUMLClassManager {

	static List<String> umlClasses = new ArrayList<String>();
	static List<String> requirementClasses = new ArrayList<String>();
	public static List<String> relationNodes = new ArrayList<String>();

	static String projectPath;
	static TableItem tableItem;
	static TreeItem classItem;
	
	static Image exactImage = new Image(CompareWindow.display, PropertyFile.imagePath + "/" + "exact.jpg");
	static Image violateImage = new Image(CompareWindow.display, PropertyFile.imagePath + "/" + "violation.jpg");

	/**
	 * check whether the requirement classes are implemented in UML
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<String> compareClassNames(String projectPath) {
		RequirementsManger.readXML(projectPath);
                requirementClasses = ClassManager.getReqClassName(projectPath);
		Map<String, ArtefactElement> reqMap = RequirementsManger.requirementArtefactElements;
		Iterator<Entry<String, ArtefactElement>> requirementIterator = reqMap
				.entrySet().iterator();
		UMLArtefactManager.readXML(projectPath);
		Map<String, ArtefactElement> UMLMap = UMLArtefactManager.UMLAretefactElements;
		Iterator<Entry<String, ArtefactElement>> umlIterator = null;

		if(CompareWindow.tree != null && HomeGUI.isComaparing){
			TreeColumn column1 = new TreeColumn(CompareWindow.tree, SWT.LEFT);
			column1.setText("RequirementsXML File");
			column1.setWidth(300);
		
			TreeColumn column2 = new TreeColumn(CompareWindow.tree, SWT.LEFT);
			column2.setText("UML-XML file");
			column2.setWidth(300);
		}
		
		while (requirementIterator.hasNext()) {
			Map.Entry pairs = requirementIterator.next();
			ArtefactElement reqArtefactElement = (ArtefactElement) pairs
					.getValue();
			String name = reqArtefactElement.getName();
			List<ArtefactSubElement> reqAttributeElements = reqArtefactElement
					.getArtefactSubElements();
			if (reqArtefactElement.getType().equalsIgnoreCase("Class")) {
				umlIterator = UMLMap.entrySet().iterator();

				while (umlIterator.hasNext()) {

					Map.Entry pairs1 = umlIterator.next();
					ArtefactElement UMLArtefactElement = (ArtefactElement) pairs1
							.getValue();
                                        WordsMap w1 = new WordsMap();
                                        w1 = SynonymWords.checkSymilarity(UMLArtefactElement.getName(), name,
									reqArtefactElement.getType());
					if (UMLArtefactElement.getType().equalsIgnoreCase("Class")
							&& (UMLArtefactElement.getName().equalsIgnoreCase(
									name) | w1.isIsMatched())) {

						compareSubElements(classItem, reqArtefactElement, UMLArtefactElement);
						UMLMap.remove(UMLArtefactElement.getArtefactElementId());
						reqMap.remove(reqArtefactElement.getArtefactElementId());
						requirementIterator = reqMap.entrySet().iterator();
						break;
					}

				}
			}
		}
		RelationManager.addLinks(relationNodes);
		
		if (UMLMap.size() > 0 || reqMap.size() > 0) {
			requirementIterator = reqMap.entrySet().iterator();
			umlIterator = UMLMap.entrySet().iterator();

			while (requirementIterator.hasNext()) {
				Map.Entry<String, ArtefactElement> artefact = requirementIterator
						.next();
				if (CompareWindow.tree != null
						&& !CompareWindow.shell.isDisposed() && HomeGUI.isComaparing) {
					TreeItem item = new TreeItem(CompareWindow.tree, SWT.NONE);
					item.setForeground(Display.getDefault().getSystemColor(
							SWT.COLOR_RED));
					item.setText(0, artefact.getValue().getName());
					item.setData("0", artefact.getValue());
					item.setImage(0, violateImage);
					addSubItems(0, item, artefact.getValue().getArtefactSubElements());
				}
			}

			while (umlIterator.hasNext()) {
				Map.Entry<String, ArtefactElement> artefact = umlIterator
						.next();
				if (CompareWindow.tree != null
						&& !CompareWindow.shell.isDisposed() && HomeGUI.isComaparing) {
					TreeItem item = new TreeItem(CompareWindow.tree, SWT.NONE);
					item.setForeground(Display.getDefault().getSystemColor(
							SWT.COLOR_RED));
					item.setText(1, artefact.getValue().getName());
					item.setData("1", artefact.getValue());
					item.setImage(1, violateImage);
					addSubItems(1, item, artefact.getValue().getArtefactSubElements());
				}
			}
		}
		return relationNodes;
	}

	@SuppressWarnings("rawtypes")
	public static int compareClassCount() {
		SourceCodeArtefactManager.readXML(projectPath);
		RequirementsManger.readXML(projectPath);
		Iterator it = UMLArtefactManager.UMLAretefactElements.entrySet()
				.iterator();
		int countUMLClass = 0;
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			ArtefactElement artefactElement = (ArtefactElement) pairs
					.getValue();
			if (artefactElement.getType().equalsIgnoreCase("Class")) {

				countUMLClass++;
			}
			List<ArtefactSubElement> artefactSubElements = artefactElement
					.getArtefactSubElements();
			it.remove(); // avoids a ConcurrentModificationException
		}
		// UMLArtefactManager.readXML();
		Iterator it1 = RequirementsManger.requirementArtefactElements
				.entrySet().iterator();
		int countReqClass = 0;
		while (it1.hasNext()) {
			Map.Entry pairs = (Entry) it1.next();
			ArtefactElement artefactElement = (ArtefactElement) pairs
					.getValue();
			if (artefactElement.getType().equalsIgnoreCase("Class")) {
				countReqClass++;
			}
			List<ArtefactSubElement> artefactSubElements = artefactElement
					.getArtefactSubElements();
			it1.remove(); // avoids a ConcurrentModificationException
		}

		if (countUMLClass == countReqClass) {
			System.out.println("class compared");
		}
		return countUMLClass;
	}
	
	public static void addSubItems(int column, TreeItem item, List<ArtefactSubElement> list){
		for(int i = 0; i < list.size(); i++){
			TreeItem subItem = new TreeItem(item, SWT.NONE);
			subItem.setText(column, list.get(i).getName());
			subItem.setData("" + column + "", list.get(i));
		}
	}
	
	public static void compareSubElements(TreeItem classItem,
			ArtefactElement reqArtefactElement,
			ArtefactElement UMLArtefactElement) {
		relationNodes.add(reqArtefactElement
				.getArtefactElementId().substring(
						reqArtefactElement
								.getArtefactElementId()
								.indexOf("RQ")));
                relationNodes.add("Req Class To UML Class");
		relationNodes.add(UMLArtefactElement
				.getArtefactElementId());
		if (CompareWindow.tree != null
				&& !CompareWindow.tree.isDisposed() && HomeGUI.isComaparing) {
			classItem = new TreeItem(CompareWindow.tree, SWT.NONE);
			classItem.setText(0, reqArtefactElement.getName());
			classItem.setData("0", reqArtefactElement);
			classItem.setImage(exactImage);
			classItem.setForeground(Display.getDefault()
					.getSystemColor(SWT.COLOR_DARK_BLUE));
			classItem.setText(1, UMLArtefactElement.getName());
			classItem.setData("1", UMLArtefactElement);

		}
		ArrayList<ArtefactSubElement> UMLAttributesList = new ArrayList<ArtefactSubElement>();
		ArrayList<ArtefactSubElement> UMLMethodsList = new ArrayList<ArtefactSubElement>();

		ArrayList<ArtefactSubElement> reqAttributesList = new ArrayList<ArtefactSubElement>();
		ArrayList<ArtefactSubElement> reqMethodsList = new ArrayList<ArtefactSubElement>();
		
		ArrayList<WordsMap> methodWordsMapList = new ArrayList<WordsMap>();
		ArrayList<WordsMap> attributeWordsMapList = new ArrayList<WordsMap>();

		List<ArtefactSubElement> UMLAttributeElements = UMLArtefactElement
				.getArtefactSubElements();
		List<ArtefactSubElement> reqAttributeElements = reqArtefactElement
				.getArtefactSubElements();
		for (int i = 0; i < UMLAttributeElements.size(); i++) {
			ArtefactSubElement UMLAttribute = UMLAttributeElements
					.get(i);
			for (int j = 0; j < reqAttributeElements.size(); j++) {
				ArtefactSubElement reqElement = reqAttributeElements
						.get(j);
                                WordsMap w2 = new WordsMap();
                                w2 = SynonymWords.checkSymilarity(
								UMLAttribute.getName(),
								reqElement.getName(),
								reqElement.getType(),UMLAttribute.getType(),requirementClasses);
				if (UMLAttribute.getName().equalsIgnoreCase(
						reqElement.getName())
						| w2.isIsMatched()) {
					relationNodes.add(reqElement
							.getSubElementId().substring(
									reqElement
											.getSubElementId()
											.indexOf("RQ")));
                                        relationNodes.add("Req "+reqElement.getType()+" To "+UMLAttribute.getType());
					relationNodes.add(UMLAttribute
							.getSubElementId());
					
					// if(UMLAttribute.getName().equalsIgnoreCase(reqElement.getName())
					// ||LevenshteinDistance.similarity(UMLAttribute.getName(),
					// reqElement.getName())>.6){
					if (CompareWindow.tree != null
							&& !CompareWindow.tree.isDisposed() && HomeGUI.isComaparing) {
						
						if ((reqElement.getType())
								.equalsIgnoreCase("Field")) {
							UMLAttributesList.add(UMLAttribute);
							reqAttributesList.add(reqElement);
							attributeWordsMapList.add(w2);
						}

						else if ((reqElement.getType())
								.equalsIgnoreCase("Method")) {
							UMLMethodsList.add(UMLAttribute);
							reqMethodsList.add(reqElement);
							methodWordsMapList.add(w2);
						}

						UMLAttributeElements
								.remove(UMLAttribute);
						reqAttributeElements.remove(reqElement);
						i--;
						j--;
						break;
					}
				}
			}
		}
		if (CompareWindow.tree != null
				&& !CompareWindow.tree.isDisposed() && HomeGUI.isComaparing) {
			TreeItem subAttribute = new TreeItem(classItem, SWT.NONE);
			subAttribute.setText("Attributes");
			subAttribute.setForeground(Display.getDefault()
					.getSystemColor(SWT.COLOR_GREEN));
			for (int k = 0; k < UMLAttributesList.size(); k++) {
				TreeItem subItem = new TreeItem(subAttribute, SWT.NONE);
				subItem.setText(1, UMLAttributesList.get(k).getName());
				subItem.setData("1", UMLAttributesList.get(k));
				subItem.setImage(1, ImageType.getImage(attributeWordsMapList.get(k)).getValue());
				subItem.setText(0, reqAttributesList.get(k).getName());
				subItem.setData("0", reqAttributesList.get(k));
				subItem.setImage(0, ImageType.getImage(attributeWordsMapList.get(k)).getValue());
			}
			
			TreeItem subMethod = new TreeItem(classItem, SWT.NONE);
			subMethod.setText("Methods");
			subMethod.setForeground(Display.getDefault()
					.getSystemColor(SWT.COLOR_GREEN));
			for (int k = 0; k < UMLMethodsList.size(); k++) {
				TreeItem subItem = new TreeItem(subMethod, SWT.NONE);
				subItem.setText(1, UMLMethodsList.get(k).getName());
				subItem.setData("1", UMLMethodsList.get(k));
				subItem.setImage(1, ImageType.getImage(methodWordsMapList.get(k)).getValue());
				subItem.setText(0, reqMethodsList.get(k).getName());
				subItem.setData("0", reqMethodsList.get(k));
				subItem.setImage(0, ImageType.getImage(methodWordsMapList.get(k)).getValue());
			}
			if (reqAttributeElements.size() > 0) {
				for (ArtefactSubElement model : reqAttributeElements) {
					if(model.getType().equalsIgnoreCase("Field")){
						TreeItem subItem = new TreeItem(subAttribute,
								SWT.NONE);
						subItem.setText(0, model.getName());
						subItem.setImage(0, violateImage);
						subItem.setForeground(Display
								.getDefault().getSystemColor(
										SWT.COLOR_RED));
					} else if(model.getType().equalsIgnoreCase("Method")){
						TreeItem subItem = new TreeItem(subMethod,
								SWT.NONE);
						subItem.setText(0, model.getName());
						subItem.setImage(0, violateImage);
						subItem.setForeground(Display
								.getDefault().getSystemColor(
										SWT.COLOR_RED));
					}									
				}
			}
			if (UMLAttributeElements.size() > 0) {
				for (ArtefactSubElement model : UMLAttributeElements) {
					if(model.getType().equalsIgnoreCase("UMLAttribute")){
						TreeItem subItem = new TreeItem(subAttribute,
								SWT.NONE);
						subItem.setText(1, model.getName());
						subItem.setImage(1, violateImage);
						subItem.setForeground(Display
								.getDefault().getSystemColor(
										SWT.COLOR_RED));
					} else if(model.getType().equalsIgnoreCase("UMLOperation")){
						TreeItem subItem = new TreeItem(subMethod,
								SWT.NONE);
						subItem.setText(1, model.getName());
						subItem.setImage(1, violateImage);
						subItem.setForeground(Display
								.getDefault().getSystemColor(
										SWT.COLOR_RED));
					}
				}

			}
		}
	}

}
