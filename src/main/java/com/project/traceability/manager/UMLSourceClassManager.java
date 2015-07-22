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

public class UMLSourceClassManager {

	static List<String> sourceCodeClasses = new ArrayList<String>();
	static List<String> UMLClasses = new ArrayList<String>();
	public static List<String> relationNodes = new ArrayList<String>();

	static String projectPath;
	static TableItem tableItem;
	static TreeItem classItem;
	
	static Image exactImage = new Image(CompareWindow.display, PropertyFile.imagePath + "/" + "exact.jpg");
	static Image violateImage = new Image(CompareWindow.display, PropertyFile.imagePath + "/" + "violation.jpg");

	/**
	 * check whether the designed classes are implemented in sourcecode
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static List<String> compareClassNames(String projectPath) {
		UMLSourceClassManager.projectPath = projectPath;
		UMLClasses = ClassManager.getUmlClassName(projectPath);
		Map<String, ArtefactElement> UMLMap = UMLArtefactManager.UMLAretefactElements; // get
																						// map
																						// from
																						// extraction
																						// class
		Iterator<Entry<String, ArtefactElement>> UMLIterator = UMLMap
				.entrySet().iterator();

		Map<String, ArtefactElement> artefactMap = SourceCodeArtefactManager.sourceCodeAretefactElements; // get
																											// map
																											// from
																											// extraction
																											// class
		Iterator<Entry<String, ArtefactElement>> sourceIterator = null;

		if (CompareWindow.tree != null && HomeGUI.isComaparing) {
			TreeColumn column1 = new TreeColumn(CompareWindow.tree, SWT.LEFT);
			column1.setText("SourceXML File");
			column1.setWidth(300);

			TreeColumn column2 = new TreeColumn(CompareWindow.tree, SWT.LEFT);
			column2.setText("UML-XML file");
			column2.setWidth(300);
		}

		while (UMLIterator.hasNext()) {
			Map.Entry pairs = UMLIterator.next();
			ArtefactElement UMLArtefactElement = (ArtefactElement) pairs
					.getValue(); // get an UML artefact element
			String name = UMLArtefactElement.getName();
			List<ArtefactSubElement> UMLAttributeElements = UMLArtefactElement
					.getArtefactSubElements();
			if (UMLArtefactElement.getType().equalsIgnoreCase("Class")) {

				sourceIterator = artefactMap.entrySet().iterator(); // create an
																	// iterator
																	// for
																	// sourceCodeElements

				while (sourceIterator.hasNext()) {
					Map.Entry pairs1 = sourceIterator.next();
					ArtefactElement sourceArtefactElement = (ArtefactElement) pairs1
							.getValue(); // get sourceartefact element
                                        WordsMap w1 = new WordsMap();
                                        w1 = SynonymWords.checkSymilarity(
									sourceArtefactElement.getName(), name,
									sourceArtefactElement.getType());
					if (sourceArtefactElement.getType().equalsIgnoreCase(
							"Class")
							&& w1.isIsMatched()) {
						compareSubElements(classItem, UMLArtefactElement, sourceArtefactElement);
						artefactMap.remove(sourceArtefactElement
								.getArtefactElementId());
						
						UMLMap.remove(UMLArtefactElement.getArtefactElementId());
						UMLIterator = UMLMap.entrySet().iterator();
						break;

					}

				}
			}
		}
		RelationManager.addLinks(relationNodes);
		if (artefactMap.size() > 0 || UMLMap.size() > 0) {
			UMLIterator = UMLMap.entrySet().iterator();
			sourceIterator = artefactMap.entrySet().iterator();

			while (UMLIterator.hasNext()) {
				Map.Entry<String, ArtefactElement> artefact = UMLIterator
						.next();
				if (CompareWindow.tree != null
						&& !CompareWindow.shell.isDisposed() && HomeGUI.isComaparing) {
					TreeItem item = new TreeItem(CompareWindow.tree, SWT.NONE);
					item.setText(0, artefact.getValue().getName());
					item.setData("0", artefact.getValue());
					item.setImage(0, violateImage);
					item.setForeground(Display.getDefault().getSystemColor(
							SWT.COLOR_RED));
					addSubItems(0, item, artefact.getValue()
							.getArtefactSubElements());
				}
			}

			while (sourceIterator.hasNext()) {
				Map.Entry<String, ArtefactElement> artefact = sourceIterator
						.next();
				if (CompareWindow.tree != null
						&& !CompareWindow.shell.isDisposed() && HomeGUI.isComaparing) {
					TreeItem item = new TreeItem(CompareWindow.tree, SWT.NONE);
					item.setText(1, artefact.getValue().getName());
					item.setData("1", artefact.getValue());
					item.setImage(1, violateImage);
					item.setForeground(Display.getDefault().getSystemColor(
							SWT.COLOR_RED));
					addSubItems(1, item, artefact.getValue()
							.getArtefactSubElements());
					
				}
			}
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

	public static void addSubItems(int column, TreeItem item,
			List<ArtefactSubElement> list) {
		for (int i = 0; i < list.size(); i++) {
			TreeItem subItem = new TreeItem(item, SWT.NONE);
			subItem.setText(column, list.get(i).getName());
			subItem.setData("" + column + "", list.get(i));
		}
	}

	public static void compareSubElements(TreeItem classItem,
			ArtefactElement UMLArtefactElement,
			ArtefactElement sourceArtefactElement) {
		relationNodes.add(UMLArtefactElement.getArtefactElementId());
                relationNodes.add("UML Class To Source Class");
		relationNodes.add(sourceArtefactElement.getArtefactElementId());

		if (CompareWindow.tree != null && !CompareWindow.tree.isDisposed() && HomeGUI.isComaparing) {
			classItem = new TreeItem(CompareWindow.tree, SWT.NONE);
			classItem.setText(0, sourceArtefactElement.getName());
			classItem.setData("0", sourceArtefactElement);
			classItem.setImage(exactImage);
			classItem.setText(1, UMLArtefactElement.getName());
			classItem.setData("1", UMLArtefactElement);
			classItem.setForeground(Display.getDefault().getSystemColor(
					SWT.COLOR_DARK_BLUE));
		}

		ArrayList<ArtefactSubElement> UMLAttributesList = new ArrayList<ArtefactSubElement>();
		ArrayList<ArtefactSubElement> UMLMethodsList = new ArrayList<ArtefactSubElement>();

		ArrayList<ArtefactSubElement> sourceAttributesList = new ArrayList<ArtefactSubElement>();
		ArrayList<ArtefactSubElement> sourceMethodsList = new ArrayList<ArtefactSubElement>();
		
		ArrayList<WordsMap> methodWordsMapList = new ArrayList<WordsMap>();
		ArrayList<WordsMap> attributeWordsMapList = new ArrayList<WordsMap>();

		List<ArtefactSubElement> sourceAttributeElements = sourceArtefactElement
				.getArtefactSubElements();
		List<ArtefactSubElement> UMLAttributeElements = UMLArtefactElement
				.getArtefactSubElements();
		for (int i = 0; i < UMLAttributeElements.size(); i++) {
			ArtefactSubElement UMLAttribute = UMLAttributeElements.get(i);
			for (int j = 0; j < sourceAttributeElements.size(); j++) {
				ArtefactSubElement sourceElement = sourceAttributeElements
						.get(j);
                                WordsMap w2 = new WordsMap();
                                w2 = SynonymWords.checkSymilarity(UMLAttribute.getName(),
						sourceElement.getName(), sourceElement.getType(),UMLAttribute.getType(),
						UMLClasses);
				if (w2.isIsMatched()) {
					relationNodes.add(UMLAttribute.getSubElementId());
                                        relationNodes.add(UMLAttribute.getType()+" To Source "+sourceElement.getType());
					relationNodes.add(sourceElement.getSubElementId());
					if (CompareWindow.tree != null
							&& !CompareWindow.tree.isDisposed() && HomeGUI.isComaparing) {
						if ((sourceElement.getType()).equalsIgnoreCase("Field")) {
							sourceAttributesList.add(sourceElement);
							UMLAttributesList.add(UMLAttribute);
							attributeWordsMapList.add(w2);
						}

						else if ((sourceElement.getType())
								.equalsIgnoreCase("Method")) {
							sourceMethodsList.add(sourceElement);
							UMLMethodsList.add(UMLAttribute);
							methodWordsMapList.add(w2);
						}

						UMLAttributeElements.remove(UMLAttribute);
						sourceAttributeElements.remove(sourceElement);
						i--;
						j--;
						break;
					}
				}
			}
		}
		if (CompareWindow.tree != null && !CompareWindow.tree.isDisposed() && HomeGUI.isComaparing) {
			TreeItem subAttribute = new TreeItem(classItem, SWT.NONE);
			subAttribute.setText("Attributes");
			subAttribute.setForeground(Display.getDefault().getSystemColor(
					SWT.COLOR_GREEN));
			for (int k = 0; k < sourceAttributesList.size(); k++) {
				TreeItem subItem = new TreeItem(subAttribute, SWT.NONE);
				subItem.setText(0, sourceAttributesList.get(k).getName());
				subItem.setData("0", sourceAttributesList.get(k));
				subItem.setImage(0, ImageType.getImage(attributeWordsMapList.get(k)).getValue());
				subItem.setText(1, UMLAttributesList.get(k).getName());
				subItem.setData("1", UMLAttributesList.get(k));
				subItem.setImage(1, ImageType.getImage(attributeWordsMapList.get(k)).getValue());
			}
			TreeItem subMethod = new TreeItem(classItem, SWT.NONE);
			subMethod.setText("Methods");
			subMethod.setForeground(Display.getDefault().getSystemColor(
					SWT.COLOR_GREEN));
			for (int k = 0; k < sourceMethodsList.size(); k++) {
				TreeItem subItem = new TreeItem(subMethod, SWT.NONE);
				subItem.setText(0, sourceMethodsList.get(k).getName());
				subItem.setData("0", sourceMethodsList.get(k));
				subItem.setImage(0, ImageType.getImage(methodWordsMapList.get(k)).getValue());
				subItem.setText(1, UMLMethodsList.get(k).getName());
				subItem.setData("1", UMLMethodsList.get(k));
				subItem.setImage(1, ImageType.getImage(methodWordsMapList.get(k)).getValue());
			}
			if (UMLAttributeElements.size() > 0) {
				for (ArtefactSubElement model : UMLAttributeElements) {
					if (model.getType().equalsIgnoreCase("UMLAttribute")) {
						TreeItem subItem = new TreeItem(subAttribute, SWT.NONE);
						subItem.setText(1, model.getName());
						subItem.setImage(1, violateImage);
						subItem.setForeground(Display.getDefault()
								.getSystemColor(SWT.COLOR_RED));
					} else if (model.getType().equalsIgnoreCase("UMLOperation")) {
						TreeItem subItem = new TreeItem(subMethod, SWT.NONE);
						subItem.setText(1, model.getName());
						subItem.setImage(1, violateImage);
						subItem.setForeground(Display.getDefault()
								.getSystemColor(SWT.COLOR_RED));
					}

				}

			}
			if (sourceAttributeElements.size() > 0) {
				for (ArtefactSubElement model : sourceAttributeElements) {
					if (model.getType().equalsIgnoreCase("Field")) {
						TreeItem subItem = new TreeItem(subAttribute, SWT.NONE);
						subItem.setText(0, model.getName());
						subItem.setImage(0, violateImage);
						subItem.setForeground(Display.getDefault()
								.getSystemColor(SWT.COLOR_RED));
					} else if (model.getType().equalsIgnoreCase("Method")) {
						TreeItem subItem = new TreeItem(subMethod, SWT.NONE);
						subItem.setText(0, model.getName());
						subItem.setImage(0, violateImage);
						subItem.setForeground(Display.getDefault()
								.getSystemColor(SWT.COLOR_RED));
					}
				}

			}
		}

	}
}
