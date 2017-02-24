/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.model;

/**
 *
 * @author shiyam
 */
public class Dependencies {
    
    private String dependency_type;
    private String source_id;
    private String taget_id;
    private String muliplicity_src;
    private String multiplicity_target;
    private String annotation;

    /**
     * @return the dependency_type
     */
    public String getDependency_type() {
        return dependency_type;
    }

    /**
     * @param dependency_type the dependency_type to set
     */
    public void setDependency_type(String dependency_type) {
        this.dependency_type = dependency_type;
    }

    /**
     * @return the source_id
     */
    public String getSource_id() {
        return source_id;
    }

    /**
     * @param source_id the source_id to set
     */
    public void setSource_id(String source_id) {
        this.source_id = source_id;
    }

    /**
     * @return the taget_id
     */
    public String getTaget_id() {
        return taget_id;
    }

    /**
     * @param taget_id the taget_id to set
     */
    public void setTaget_id(String taget_id) {
        this.taget_id = taget_id;
    }

    /**
     * @return the muliplicity_src
     */
    public String getMuliplicity_src() {
        return muliplicity_src;
    }

    /**
     * @param muliplicity_src the muliplicity_src to set
     */
    public void setMuliplicity_src(String muliplicity_src) {
        this.muliplicity_src = muliplicity_src;
    }

    /**
     * @return the multiplicity_target
     */
    public String getMultiplicity_target() {
        return multiplicity_target;
    }

    /**
     * @param multiplicity_target the multiplicity_target to set
     */
    public void setMultiplicity_target(String multiplicity_target) {
        this.multiplicity_target = multiplicity_target;
    }

    /**
     * @return the annotation
     */
    public String getAnnotation() {
        return annotation;
    }

    /**
     * @param annotation the annotation to set
     */
    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }
    
}
