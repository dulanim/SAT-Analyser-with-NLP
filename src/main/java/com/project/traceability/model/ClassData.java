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
public class ClassData extends ModelData{
    
    
    private boolean isAbstract;
    private boolean isFinal;
    

    /**
     * @return the isAbstract
     */
    public boolean isIsAbstract() {
        return isAbstract;
    }

    /**
     * @param isAbstract the isAbstract to set
     */
    public void setIsAbstract(String isAbstract) {
         if(isAbstract.equals("true")){
            this.isAbstract = true; 
        }else{
                this.isAbstract = false;
         }
    }

    /**
     * @return the isFinal
     */
    public boolean isIsFinal() {
        return isFinal;
    }

    /**
     * @param isFinal the isFinal to set
     */
    public void setIsFinal(String isFinal) {
        
        if(isFinal.equals("true")){
            this.isFinal = true; 
        }else{
                this.isFinal = false;
         }
    }

 
    
}
