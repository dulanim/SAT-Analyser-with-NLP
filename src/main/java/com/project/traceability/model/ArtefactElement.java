package com.project.traceability.model;

import java.util.List;

import com.project.traceability.staticdata.StaticData;

public class ArtefactElement {

    private String artefactElementId = null;
    private String name = null;
    private String type = null;
    private String visibility = null;
    private String status = "";
    private List<ArtefactSubElement> artefactSubElements = null;
    private String interfacesString;
    private String superClassNameString;

    public ArtefactElement() {
        super();
    }

    public ArtefactElement(String artefactElementId, String name, String type, String visibility,
            List<ArtefactSubElement> artefactSubElements) {
        this(artefactElementId, name, type, visibility, artefactSubElements, StaticData.DEFAULT_STATUS);
    }

    public ArtefactElement(String artefactElementId, String name, String type, String visibility,
            List<ArtefactSubElement> artefactSubElements, String status) {
        super();
        this.artefactElementId = artefactElementId;
        this.name = name;
        this.type = type;
        this.visibility = visibility;
        this.setArtefactSubElements(artefactSubElements);
        this.status = status;
    }

    public String getArtefactElementId() {
        return artefactElementId;
    }

    public void setArtefactElementId(String artefactElementId) {
        this.artefactElementId = artefactElementId;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return this.status;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVisibility() {
        return visibility;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getInterfaceName() {
        return this.interfacesString;
    }

    public String getSuperClassName() {
        return superClassNameString;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public void setArtefactSubElements(List<ArtefactSubElement> artefactSubElements) {
        this.artefactSubElements = artefactSubElements;
    }
    
    public List<ArtefactSubElement> getArtefactSubElements() {
        return artefactSubElements;
    }

    public void setStatus(String status) {
        if (status.equals("")) {
            status = StaticData.DEFAULT_STATUS;
        }
        this.status = status;
    }

    public void setInterfaceName(String nameString) {
        this.interfacesString = nameString;
    }

    public void setSuperClassNames(String nameString) {
        this.superClassNameString = nameString;
    }

}
