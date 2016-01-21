/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.common;

import java.io.File;

/**
 *
 * @author shiyam
 */
public class ErrorFinder {
 
    
    public static boolean  lookUpForFiles(String root,String type){
        boolean exists = false;
        File folder = new File(root + File.separator + type);
        String files[] = folder.list();
        if(files != null  && files.length>0){
            for(String file:files){
                if(file.contains(".mdj")
                        ||file.contains(".uml")
                        ||file.contains(".xmi")){
                    exists = true;
                    break;
                }
            }
        }      
        return exists;    
    }
    
    
    public static boolean lookUpForFolder(String root,String type){
        
        
        if(root.substring(root.length()-1,root.length()).equals(File.separator))
            root = root.substring(0,root.length()-1);
        File folder = new File(root + File.separator + type);
        
        if(folder.exists())
            return true;
        else
            return false;
    }
}
