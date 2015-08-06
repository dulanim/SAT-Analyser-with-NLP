/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.model;

import java.util.List;

/**
 *
 * @author shiyam
 */
public class ViewData {
    
    private List<String> classViewName;
    private List<String> depencyViewId;

    /**
     * @return the classViewName
     */
    public List<String> getClassViewName() {
        return classViewName;
    }

    /**
     * @param classViewName the classViewName to set
     */
    public void setClassViewName(List<String> classViewName) {
        this.classViewName = classViewName;
    }

    /**
     * @return the depencyViewId
     */
    public List<String> getDepencyViewId() {
        return depencyViewId;
    }

    /**
     * @param depencyViewId the depencyViewId to set
     */
    public void setDepencyViewId(List<String> depencyViewId) {
        this.depencyViewId = depencyViewId;
    }
    
    
}
