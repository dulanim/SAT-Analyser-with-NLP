/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.UMLToXML.jsonreader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.project.traceability.model.ViewData;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author shiyam
 */
public class ViewReader {
    
    JSONArray parsedArray;
    List<String> associationReference;
    List<String> generalizationReference;
    List<String> realizationRefence;
    List<String> classReference;
    ViewReader(JSONArray jsonArr){
        this.parsedArray = jsonArr;
        classReference = new ArrayList<String>();
        associationReference = new ArrayList<String>();
        generalizationReference = new ArrayList<String>();
        realizationRefence = new ArrayList<String>();
    }
    public void readViews(){
        
        Iterator parsedIterator;
  
        if(parsedArray != null){
           
            
           
            
            List<ViewData> viewList = new ArrayList<ViewData>();
            JSONObject jsonObjOut = (JSONObject) parsedArray.get(0);
            JSONArray ownedViewJsonArr = (JSONArray) jsonObjOut.get("ownedViews");
            parsedIterator = ownedViewJsonArr.iterator();
            
            while(parsedIterator.hasNext()){
                
            JSONObject jsonObj = (JSONObject) parsedIterator.next();
            String type = jsonObj.get("_type").toString();
            
            if(type.equals("UMLClassView")){
                
                JSONArray jsonArr = (JSONArray) jsonObj.get("subViews");
                if(jsonArr != null){
                    
                    for(int i=0;i<1;i++){
                    JSONObject jsonObjIn = (JSONObject) jsonArr.get(i);
//                    JSONArray jsonArrIn = (JSONArray) jsonObjIn.get("subViews");
//                    JSONObject jsonObjClassView = (JSONObject) jsonArrIn.get(1);
                    
                    JSONObject jsonObjectRef= (JSONObject) jsonObjIn.get("model");
                    String classRef  = jsonObjectRef.get("$ref").toString();
                    classReference.add(classRef);
                    System.out.println(classRef);
                    }
                }
            }else if(type.equals("UMLInterfaceView")){
                
                JSONArray jsonArr = (JSONArray) jsonObj.get("subViews");
                if(jsonArr != null){
                    
                    for(int i=0;i<1;i++){
                    JSONObject jsonObjIn = (JSONObject) jsonArr.get(i);
//                    JSONArray jsonArrIn = (JSONArray) jsonObjIn.get("subViews");
//                    JSONObject jsonObjClassView = (JSONObject) jsonArrIn.get(1);
                    
                    JSONObject jsonObjectRef= (JSONObject) jsonObjIn.get("model");
                    String classRef  = jsonObjectRef.get("$ref").toString();
                    classReference.add(classRef);
                    System.out.println(classRef);
                    }
                }
            }else if(type.equals("UMLAssociationView")){
                JSONObject jsonObjIn = (JSONObject) jsonObj.get("model");
                 if(jsonObjIn != null){
                    String refString = (String) jsonObjIn.get("$ref");
                    associationReference.add(refString);
                 }
                
            }else if(type.equals("UMLGeneralizationView")){
                JSONObject jsonObjIn = (JSONObject) jsonObj.get("model");
                 if(jsonObjIn != null){
                    String refString = (String) jsonObjIn.get("$ref");
                    generalizationReference.add(refString);
                 }
            }else if(type.equals("UMLInterfaceRealizationView")){
                JSONObject jsonObjIn = (JSONObject) jsonObj.get("model");
                 if(jsonObjIn != null){
                    String refString = (String) jsonObjIn.get("$ref");
                    realizationRefence.add(refString);
                 }
            }
        
        }
        }
        
    }
    public List<String> getClassView(){
        return classReference;
    }
    
    public List<String> getGeneralizationView(){
        return generalizationReference;
    }
    
    public List<String> getAssociationView(){
        return associationReference;
    }
    
    public List<String> getRelizationView(){
        return realizationRefence;
    }
}
