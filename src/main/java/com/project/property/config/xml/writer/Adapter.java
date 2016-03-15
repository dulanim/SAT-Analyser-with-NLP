/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.property.config.xml.writer;

import com.project.traceability.staticdata.StaticData;

/**
 *
 * @author shiyam
 */
public class Adapter {
    
    static XMLWriter writer;
    public static String projectPath = "/home/shiyam/Desktop/SatWrks/NewSatWrkspaceskal";
    public static String wrkspace = "";
    
    static{
        
        writer = XMLWriter.getXMLWriterInstance();
    }
    public static void createProjectNode(){
        
        writer.createProject(projectPath);
    }
    
    public static void changecurrrentWorkspaceConfigStatus(){
        writer.changeCurrnntWorkspaceVale(StaticData.workspace);
      
    }
    
    public static void createwrkpace(String status){
        writer.createWorkspaceNode(StaticData.workspace, status);
    }
    
    public static void changeExistingWrkspaceStatus(String path,boolean status){
        writer.modifyStatus(status,path);
    }
}
