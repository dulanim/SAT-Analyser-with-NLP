package com.project.traceability.manager;

/**
 * @author Gitanjali
 * Nov 6, 2014
 * InfoExtractionManager.java
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.ArtefactSubElement;
import com.project.traceability.model.AttributeModel;
import com.project.traceability.model.MethodModel;
import com.project.traceability.model.RequirementModel;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.ArrayCoreMap;
import edu.stanford.nlp.util.CoreMap;

public class InfoExtractionManager {
	private static List<ArtefactElement> expectedClassNames;
	private static List<ArtefactElement> expectedSubClassNames;
	private static List<ArtefactSubElement> expectedAttributeNames;
	private static List<ArtefactSubElement> expectedMethodNames;
	
	private static List<String> attributeNames;
	private static ArtefactElement artefactElement = null;

	public static List<ArtefactElement> run(
			List<RequirementModel> requirementAretefactElements) {

		expectedClassNames = new ArrayList<ArtefactElement>();
		expectedSubClassNames = new ArrayList<ArtefactElement>();
		for (int i = 0; i < requirementAretefactElements.size(); i++) {
			extactClass(requirementAretefactElements.get(i).getTitle(),
					requirementAretefactElements.get(i).getContent(), requirementAretefactElements.get(i).getRequirementId());
		}
		expectedClassNames.addAll(expectedSubClassNames);
		
		Collections.sort(expectedClassNames, new Comparator<ArtefactElement>() {	//sorting based on class name length
		    public int compare(ArtefactElement one, ArtefactElement other) {
		    	if (one.getName().length() > other.getName().length())
		         {
		             return -1;
		         }
		        else if (one.getName().length()  < other.getName().length())
		        {
		            return 1;
		        }
		        return 0;    
		    }
		}); 
		for (int i = 0; i < requirementAretefactElements.size(); i++) {
			if (!requirementAretefactElements.get(i).getContent()
					.contains("such as")) {
//				System.out.println(requirementAretefactElements.get(i).getRequirementId());
				getBehaviors("do " + requirementAretefactElements.get(i).getTitle().toLowerCase(),
						requirementAretefactElements.get(i).getRequirementId());
			}
		}
		return expectedClassNames;
	}

	public static void extactClass(String title, String content, String id) {

		attributeNames = new ArrayList<String>();
		artefactElement = new ArtefactElement();
		expectedAttributeNames = new ArrayList<ArtefactSubElement>();
		expectedMethodNames = new ArrayList<ArtefactSubElement>();

		String[] defaultWords = { "database", "record", "system", "company",
				"information", "organization", "detail" };

		if (content.contains("such as")) {
			artefactElement = InfoExtractionManager.getClassName(title,id); // title
			String[] splitSentence = content.split("such as");

			getAttributes(splitSentence[1],id);
			expectedAttributeNames.addAll(expectedMethodNames);
			artefactElement.setArtefactSubElements(expectedAttributeNames);
			expectedClassNames.add(artefactElement);
		} else {
			// nothing
		}
	}

	public static ArrayList<String> getAttributes(String str, String id) {
		attributeNames = new ArrayList<String>();
		AttributeModel attribute = null;
		MethodModel method = null;
		Pattern p = Pattern.compile("\\((.*?)\\)", Pattern.DOTALL);

		String[] attributeString = str.split(",");
		for (int i = 0; i < attributeString.length; i++) {
			if (attributeString[i].contains("and")) {
				String[] attr = attributeString[i].split("and");
				for (int j = 0; j < attr.length; j++) {
					Matcher m = p.matcher(attr[j].toLowerCase().trim());
					if (m.find()) {
						getSubClasses(m.group(1), m.group(0),
								artefactElement.getName(),artefactElement.getArtefactElementId());
					} else {
						attribute = new AttributeModel();
						method = new MethodModel();
						attribute.setSubElementId(UUID.randomUUID().toString().concat(id));
						attribute.setName(attr[j].toLowerCase().trim());
						attribute.setType("Field");
						expectedAttributeNames.add(attribute);
						
						method.setSubElementId(UUID.randomUUID().toString().concat(id));
						method.setName("get" + attr[j].toLowerCase().trim());
						method.setType("Method");
						expectedMethodNames.add(method);
						
						method = new MethodModel();
						method.setSubElementId(UUID.randomUUID().toString().concat(id));
						method.setName("set" + attr[j].toLowerCase().trim());
						method.setType("Method");
						expectedMethodNames.add(method);
						
						attributeNames.add(attr[j].toLowerCase().trim());
					}
				}
			} else {
				Matcher m = p.matcher(attributeString[i].toLowerCase().trim());
				if (m.find()) {
					getSubClasses(m.group(1),
							attributeString[i].replace(m.group(0), ""),
							artefactElement.getName(),id);
				} else {
					attributeNames.add(attributeString[i].toLowerCase().trim());
					attribute = new AttributeModel();
					method = new MethodModel();
					attribute.setSubElementId(UUID.randomUUID().toString().concat(id));
					attribute.setName(attributeString[i].toLowerCase().trim());
					attribute.setType("Field");
					expectedAttributeNames.add(attribute);
					
					method.setSubElementId(UUID.randomUUID().toString().concat(id));
					method.setName("get" + attributeString[i].toLowerCase().trim());
					method.setType("Method");
					expectedMethodNames.add(method);
					
					method = new MethodModel();
					method.setSubElementId(UUID.randomUUID().toString().concat(id));
					method.setName("set" + attributeString[i].toLowerCase().trim());
					method.setType("Method");
					expectedMethodNames.add(method);
				}
			}
		}
		return (ArrayList<String>) attributeNames;
	}

	public static ArtefactElement getClassName(String str,String id) {

		StanfordCoreNLP pipeline = new StanfordCoreNLP();
		Annotation annotation = new Annotation(str);
		pipeline.annotate(annotation);
		List<CoreMap> sentences = annotation
				.get(CoreAnnotations.SentencesAnnotation.class);

		if (sentences != null && sentences.size() > 0) {
			ArrayCoreMap sentence = (ArrayCoreMap) sentences.get(0);
			Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
			List<Tree> leaves = tree.getLeaves();
			for (int i = 0; i < leaves.size(); i++) {
				Tree leaf = leaves.get(i);
				Tree preLeaf = null;
				Tree preParent = null;
				if (i != 0)
					preLeaf = leaves.get(i - 1);
				Tree parent = leaf.parent(tree);
				if (i != 0)
					preParent = preLeaf.parent(tree);

				if (parent.label().value().equals("IN")) {
					leaves.remove(leaf);
					i--;
				}
				if (i != 0 && preParent.label().value().equals("JJ")
						&& parent.label().value().equals("NN")) {
					artefactElement = new ArtefactElement();
					artefactElement.setName(preLeaf.label().value()
							.toLowerCase()
							+ " " + leaf.label().value().toLowerCase());
					artefactElement.setArtefactElementId(UUID.randomUUID()
							.toString().concat(id));
					artefactElement.setType("Class");
				} else if (i != 0 && preParent.label().value().equals("NN")
						&& parent.label().value().equals("NN")) {
					artefactElement = new ArtefactElement();
					artefactElement.setName(preLeaf.label().value()
							.toLowerCase()
							+ " " + leaf.label().value().toLowerCase());
					artefactElement.setArtefactElementId(UUID.randomUUID()
							.toString().concat(id));
					artefactElement.setType("Class");
				} else if (parent.label().value().equals("NN")
						|| parent.label().value().equals("NNP")) {
					artefactElement = new ArtefactElement();
					artefactElement.setName(leaf.label().value().toLowerCase());
					artefactElement.setArtefactElementId(UUID.randomUUID()
							.toString().concat(id));
					artefactElement.setType("Class");
				}
			}
		}
		return artefactElement;
	}

	public static void getBehaviors(String str, String id) {

		MethodModel artefactSubElement = null;
		//System.out.println(id);
		StanfordCoreNLP pipeline = new StanfordCoreNLP();
		Annotation annotation;
		annotation = new Annotation(str);
		pipeline.annotate(annotation);

		List<CoreMap> sentences = annotation
				.get(CoreAnnotations.SentencesAnnotation.class);
		if (sentences != null && sentences.size() > 0) {
			ArrayCoreMap sentence = (ArrayCoreMap) sentences.get(0);
			Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
			List<Tree> leaves = tree.getLeaves();
			for (int i = 0; i < leaves.size(); i++) {
				Tree leaf = leaves.get(i);
				Tree parent = leaf.parent(tree);
				if (parent.label().value().equals("VB")) {
					if (!leaf.label().value().equals("do")) {
						boolean isFound = false;
						for (int j = 0; j < expectedClassNames.size() && !isFound; j++) {
							if (str.contains(expectedClassNames.get(j)
									.getName())) {
								isFound = true;
								ArtefactElement artefactElement = expectedClassNames
										.get(j);
								List<ArtefactSubElement> methodList = artefactElement
										.getArtefactSubElements();
								if (methodList == null)
									methodList = new ArrayList<ArtefactSubElement>();
								artefactSubElement = new MethodModel();
								artefactSubElement.setName(leaf.label().value()
										.toLowerCase());
								artefactSubElement.setSubElementId(UUID
										.randomUUID().toString().concat(id));
								artefactSubElement.setType("Method");
								methodList.add(artefactSubElement);
								expectedClassNames.get(j).setArtefactSubElements(methodList);
							}
						}
						if (!isFound) {
							for (int j = 0; j < expectedClassNames.size(); j++) {
//								System.out.println(expectedClassNames.get(j)
//										.getName());
								ArtefactElement artefactElement = expectedClassNames
										.get(j);
								List<ArtefactSubElement> methodList = artefactElement
										.getArtefactSubElements();
								if (methodList == null)
									methodList = new ArrayList<ArtefactSubElement>();
								artefactSubElement = new MethodModel();
								artefactSubElement.setName(leaf.label().value()
										.toLowerCase());
								artefactSubElement.setSubElementId(UUID
										.randomUUID().toString().concat(id));
								artefactSubElement.setType("Method");
								methodList.add(artefactSubElement);								
								expectedClassNames.get(j).setArtefactSubElements(methodList);
							}
						}
					}
				}
			}
		}
	}

	private static boolean checkDefaultWords(String str) {
		boolean isDefault = false;
		String[] defaultWords = { "database", "record", "system", "company",
				"information", "organization", "detail" };
		for (int i = 0; i < defaultWords.length; i++) {
			if (str.equalsIgnoreCase(defaultWords[i]))
				isDefault = true;
		}
		return isDefault;
	}

	private static void getSubClasses(String subClassName,
			String attributeName, String superClassName, String id) {
		ArtefactElement artefact = null;
		AttributeModel attribute = null;
		MethodModel method = null;
		List<ArtefactSubElement> artefactSubElementList = null;
		
		if (expectedSubClassNames.size() == 0) {
			artefact = new ArtefactElement();
			artefact.setName(subClassName);
			artefact.setArtefactElementId(UUID.randomUUID().toString().concat(id));
			artefact.setType("Class");
			attribute = new AttributeModel();
			method = new MethodModel();
			attribute.setSubElementId(UUID.randomUUID().toString().concat(id));
			attribute.setName(attributeName.trim());
			attribute.setType("Field");
			
			artefactSubElementList = new ArrayList<ArtefactSubElement>();
			artefactSubElementList.add(attribute);
			
			method.setSubElementId(UUID.randomUUID().toString().concat(id));
			method.setName("set" + attributeName.trim());
			method.setType("Method");
			artefactSubElementList.add(method);
			
			method = new MethodModel();
			method.setSubElementId(UUID.randomUUID().toString().concat(id));
			method.setName("get" + attributeName.trim());
			method.setType("Method");
			artefactSubElementList.add(method);
			
			artefact.setArtefactSubElements(artefactSubElementList);
			expectedSubClassNames.add(artefact);
		} else {
			boolean isExist = false;
			for (int i = 0; i < expectedSubClassNames.size(); i++) {
				if (subClassName.equalsIgnoreCase(expectedSubClassNames.get(i)
						.getName())) {
					isExist = true;
					artefact = expectedSubClassNames.get(i);
					expectedSubClassNames.remove(i);
					break;
				}
			}
			if (!isExist) {
				artefact = new ArtefactElement();
				artefact.setName(subClassName);
				artefact.setArtefactElementId(UUID.randomUUID().toString().concat(id));
				artefact.setType("Class");
			}
			attribute = new AttributeModel();
			attribute.setSubElementId(UUID.randomUUID().toString().concat(id));
			attribute.setName(attributeName.trim());
			attribute.setType("Field");
			if (!isExist)
				artefactSubElementList = new ArrayList<ArtefactSubElement>();
			else
				artefactSubElementList = artefact.getArtefactSubElements();
			artefactSubElementList.add(attribute);
			
			method = new MethodModel();
			method.setSubElementId(UUID.randomUUID().toString().concat(id));
			method.setName("set" + attributeName.trim());
			method.setType("Method");
			artefactSubElementList.add(method);
			
			method = new MethodModel();
			method.setSubElementId(UUID.randomUUID().toString().concat(id));
			method.setName("get" + attributeName.trim());
			method.setType("Method");
			artefactSubElementList.add(method);
			
			artefact.setArtefactSubElements(artefactSubElementList);
			expectedSubClassNames.add(artefact);
		}
	}
}
