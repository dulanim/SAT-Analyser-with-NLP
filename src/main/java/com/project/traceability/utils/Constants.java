package com.project.traceability.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.graphics.Image;

import com.project.traceability.GUI.CompareWindow;
import com.project.traceability.common.PropertyFile;

import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.ArtefactSubElement;
import com.project.traceability.model.WordsMap;

public class Constants {

	public static List<String> removableTerms = new ArrayList<String>();

	public Constants() {

	}

	public enum ArtefactType {
		SOURCECODE("SourceCode"), REQUIREMENT("Requirement"), UMLDiagram(
				"UMLDiagram");

		private final String value;

		private ArtefactType(String val) {
			this.value = val;
		}

		@Override
		public String toString() {
			return value;
		}

		public String getValue() {
			return value;
		}

		public static ArtefactType parseEnum(final String val) {

			ArtefactType artefactType = null;
			for (ArtefactType type : ArtefactType.values()) {
				if (type.getValue().equals(val)) {
					artefactType = type;
				}
			}
			return artefactType;
		}
	}

	public enum ArtefactSubElementType {
		ATTRIBUTE("Attribute"), METHOD("Method");

		private final String value;

		private ArtefactSubElementType(String val) {
			this.value = val;
		}

		@Override
		public String toString() {
			return value;
		}

		public String getValue() {
			return value;
		}

		public static ArtefactSubElementType parseEnum(final String val) {

			ArtefactSubElementType artefactSubElementType = null;
			for (ArtefactSubElementType type : ArtefactSubElementType.values()) {
				if (type.getValue().equals(val)) {
					artefactSubElementType = type;
				}
			}
			return artefactSubElementType;
		}
	}

	public enum ImageType {

		EXACT_MATCH(new Image(CompareWindow.display, PropertyFile.imagePath
				+ "/" + "exact.jpg")), 
		VIOLATION(new Image(CompareWindow.display,
				PropertyFile.imagePath + "/" + "violation.jpg")),
		WARNING(new Image(CompareWindow.display, PropertyFile.imagePath + "/" + "warning.png"));

		private final Image value;

		private ImageType(Image val) {
			this.value = val;
		}

		public Image getValue() {
			return value;
		}

		public static ImageType getImage(WordsMap wordsMap) {
			ImageType image = null;
			if (wordsMap.getMapID() == 1)
				image = EXACT_MATCH;
			else if (wordsMap.getMapID() == 100)
				image = VIOLATION;
			else
				image = WARNING;
			return image;
		}

		public static ImageType getImage(ArtefactElement element) {
			ImageType image = null;
			image = VIOLATION;
			return image;
		}
		
		public static ImageType getImage(ArtefactSubElement element) {
			ImageType image = null;
			image = VIOLATION;
			return image;
		}
		

		public static ImageType parseEnum(final Image val) {

			ImageType imageType = null;
			for (ImageType type : ImageType.values()) {
				if (type.getValue().equals(val)) {
					imageType = type;
				}
			}
			return imageType;
		}
	}

		
	public static List<String> getRemovableTerms(){
		String[] terms = {"system", "details", "company", "information", "requirement", "organization", "database"};

		removableTerms.addAll(Arrays.asList(terms));
		return removableTerms;
	}

}
