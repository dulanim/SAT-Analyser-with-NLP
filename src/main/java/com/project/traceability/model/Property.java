/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.model;

/**
 *
 * @author shiyam
 */
public class Property {
    
    private String id;
    private String name;
    private String visibility;
    private String type;
    private boolean isStatic;
    private String status;
    
    
    

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the visibility
     */
    public String getVisibility() {
        return visibility;
    }
    public String getStatus(){
    	return status;
    }
    /**
     * @param visibility the visibility to set
     */
    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    /**
     * @return the isStatic
     */
    public boolean isIsStatic() {
        return isStatic;
    }

    /**
     * @param isStatic the isStatic to set
     */
    public void setIsStatic(String isStatic) {
        this.isStatic = Boolean.parseBoolean(isStatic);
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * @param status the type to set
     */
    public void setStatus(String status) {
        this.status = status;
    }
}
