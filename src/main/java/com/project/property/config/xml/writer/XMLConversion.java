/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.property.config.xml.writer;

import com.project.NLP.SourceCodeToXML.AST;
import com.project.NLP.UMLToXML.jsonreader.JSONReader;
import com.project.NLP.UMLToXML.xmiumlreader.XMLReader;
import com.project.NLP.UMLToXML.xmlwriter.WriteToXML;
import com.project.NLP.file.operations.FilePropertyName;
import com.project.traceability.staticdata.StaticData;

/**
 *
 * @author shiyam
 */
public class XMLConversion {
    
    
    
    
    public static boolean convertUMLFile(){
       
        
       boolean isSuccess = false;
       WriteToXML xmlWriter = new WriteToXML();
       if(StaticData.umlFilePath.contains("mdj")){
            JSONReader reader = new JSONReader();
            reader.readJson();
            xmlWriter.createXML();
            isSuccess = true;
          
        }else{
                XMLReader reader = new XMLReader();
                reader.readUMLXMI();
                isSuccess = true;
        }
       
       return isSuccess;
    }
    
    public static boolean convertJavaFiles() throws Exception{
        
        boolean isSuccess = false;
        AST ast = new AST();
        ast.startSourceCodeConversion(StaticData.sourceFilePath);
        
        new AST().startSourceCodeConversion(StaticData.sourceFilePath);
        isSuccess = true;
        
        return isSuccess;
                                
    }
    
        
}
