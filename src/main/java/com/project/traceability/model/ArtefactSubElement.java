package com.project.traceability.model;

import com.project.traceability.staticdata.StaticData;


public class ArtefactSubElement {
	
	private String subElementId = null;
	private String name = null;
	private String visibility = null;
	private String returnType = null;
	private String type = null;
	private String status;
	private String variableType = "";
	private String paramString = "";
			
	public ArtefactSubElement() {
		super();
	}

	public ArtefactSubElement(String subElementId, String name,
			String visibility, String returnType, String type) {

		this(subElementId,name,visibility,returnType,type,StaticData.DEFAULT_STATUS);
	}
	
	public ArtefactSubElement(String subElementId, String name,
			String visibility, String returnType, String type,String status) {
		super();
		this.subElementId = subElementId;
		this.name = name;
		this.visibility = visibility;
		this.returnType = returnType;
		this.type = type;
		this.status = status;
	}

	public String getSubElementId() {
		return subElementId;
	}
	
	public void setSubElementId(String subElementId) {
		this.subElementId = subElementId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVisibility() {
		return visibility;
	}

	public String getStatus(){
		return this.status;
	}
	
	public String getVariableType(){
		return this.variableType;
	}
	
	public String getParamString(){
		return this.paramString;
	}
	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public void setStatus(String status){
		if(status.equals(""))
			status = StaticData.DEFAULT_STATUS;
		this.status = status;
	}
	
	public void setVariableType(String type){
		this.variableType = type;
	}
	
	public void setParamString(String paramString){
		this.paramString = paramString;
	}
}
