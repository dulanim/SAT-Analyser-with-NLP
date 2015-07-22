package com.project.traceability.model;

import java.util.List;

public class MethodModel extends ArtefactSubElement{
	
	private String returnType = null;
	private String content = null;
	private List<ParameterModel> parameters = null;
	public MethodModel() {
		super();
	}
	
	public MethodModel(String subElementId, String name, String visibility,
			String returnType, String type, String content, List<ParameterModel> parameters) {
		super(subElementId, name, visibility, returnType, type);
		this.returnType = returnType;
		this.content = content;
		this.parameters = parameters;
	}
	
	public String getReturnType() {
		return returnType;
	}
	
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<ParameterModel> getParameters() {
		return parameters;
	}
	public void setParameters(List<ParameterModel> parameters) {
		this.parameters = parameters;
	}
	
	

}
