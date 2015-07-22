package com.project.traceability.model;

import java.util.List;

public class ArtefactElement {
	
	private String artefactElementId = null;
	private String name = null;
	private String type = null;
	private String visibility = null;
	private List<ArtefactSubElement> artefactSubElements = null;
	
	public ArtefactElement() {
		super();
	}

	public ArtefactElement(String artefactElementId, String name, String type, String visibility,
			List<ArtefactSubElement> artefactSubElements) {
		super();
		this.artefactElementId = artefactElementId;
		this.name = name;
		this.type = type;
		this.visibility = visibility;
		this.setArtefactSubElements(artefactSubElements);
	}


	public String getArtefactElementId() {
		return artefactElementId;
	}

	public void setArtefactElementId(String artefactElementId) {
		this.artefactElementId = artefactElementId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public List<ArtefactSubElement> getArtefactSubElements() {
		return artefactSubElements;
	}

	public void setArtefactSubElements(List<ArtefactSubElement> artefactSubElements) {
		this.artefactSubElements = artefactSubElements;
	}
	
	

}
