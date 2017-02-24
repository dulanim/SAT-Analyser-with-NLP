/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.project.traceability.model;

/**
 *
 * @author K.Kamalan
 */
public class WordsMap {
    int mapID;
    boolean isMatched;
    private  String status;

    public int getMapID() {
        return mapID;
    }
    public String getStatus(){
    	return this.status;
    }
    public void setMapID(int mapID) {
        this.mapID = mapID;
    }

    public boolean isIsMatched() {
        return isMatched;
    }

    public void setIsMatched(boolean isMatched) {
        this.isMatched = isMatched;
    }
    
    public void setStatus(String status){
    	if(status.equals("")){
    		status = "Not Matched";
    	}
    	this.status = status;
    }
    
}
