/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.GUI;


/**
 *
 * @author shiyam
 */
public class StartUpProject {
    
    
     public static void main(String args[]) {
        com.project.property.config.xml.reader.XMLReader reader = new com.project.property.config.xml.reader.XMLReader();
                   
             if(reader.readStatus()){
                HomeGUI.main(null);
                      
         }else{
             WorkspaceSelectionWindow.main(null);
          }
     }
        
}
