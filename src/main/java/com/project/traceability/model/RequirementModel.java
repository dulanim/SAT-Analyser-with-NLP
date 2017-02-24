package com.project.traceability.model;

public class RequirementModel {

	private String requirementId = null;
	private String name = null;
	private String title = null;
	private String content = null;
	private String priority = null;
	private String type = null;
	public RequirementModel(String requirementId, String name, String title,
			String content, String priority, String type) {
		this.requirementId = requirementId;
		this.name = name;
		this.title = title;
		this.content = content;
		this.priority = priority;
		this.type = type;
	}
	public RequirementModel() {
		super();
	}
	public String getRequirementId() {
		return requirementId;
	}
	public void setRequirementId(String requirementId) {
		this.requirementId = requirementId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
