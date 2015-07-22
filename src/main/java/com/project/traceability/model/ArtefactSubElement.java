package com.project.traceability.model;


public class ArtefactSubElement {
	
	private String subElementId = null;
	private String name = null;
	private String visibility = null;
	private String returnType = null;
	private String type = null;
			
	public ArtefactSubElement() {
		super();
	}

	public ArtefactSubElement(String subElementId, String name,
			String visibility, String returnType, String type) {
		super();
		this.subElementId = subElementId;
		this.name = name;
		this.visibility = visibility;
		this.returnType = returnType;
		this.type = type;
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

}
