package com.project.traceability.model;

public class ParameterModel {
	
	private String variableType;
	private String name;
	
	public ParameterModel() {
		super();
	}

	public ParameterModel(String variableType, String name) {
		super();
		this.variableType = variableType;
		this.name = name;
	}

	public String getVariableType() {
		return variableType;
	}

	public void setVariableType(String variableType) {
		this.variableType = variableType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
