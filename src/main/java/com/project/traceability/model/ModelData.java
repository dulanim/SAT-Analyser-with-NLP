/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.model;

import com.project.traceability.model.Attribute;
import java.util.List;

/**
 *
 * @author shiyam
 */
public class ModelData extends Property{
    private List<Attribute> attributeList;
    private List<Operation> operationList;

    public ModelData() {
    }

    
    /**
     * @return the attributeList
     */
    public List<Attribute> getAttributeList() {
        return attributeList;
    }

    /**
     * @param attributeList the attributeList to set
     */
    public void setAttributeList(List<Attribute> attributeList) {
        this.attributeList = attributeList;
    }

    /**
     * @return the operationList
     */
    public List<Operation> getOperationList() {
        return operationList;
    }

    /**
     * @param operationList the operationList to set
     */
    public void setOperationList(List<Operation> operationList) {
        this.operationList = operationList;
    }
    
    
}
