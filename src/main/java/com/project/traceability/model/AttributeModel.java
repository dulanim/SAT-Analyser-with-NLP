package com.project.traceability.model;


public class AttributeModel extends ArtefactSubElement{
	
	private String variableType;

	public AttributeModel(String subElementId, String name, String visibility,
			String returnType, String type, String variableType) {
		super(subElementId, name, visibility, returnType, type);
		this.variableType = variableType;
	}

	public AttributeModel() {
		super();
	}

	public String getVariableType() {
		return variableType;
	}

	public void setVariableType(String variableType) {
		this.variableType = variableType;
	}
	
	
}
