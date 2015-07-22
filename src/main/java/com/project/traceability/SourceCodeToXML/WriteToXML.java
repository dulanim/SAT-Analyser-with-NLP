/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.SourceCodeToXML;

import com.project.traceability.common.PropertyFile;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;

/**
 *
 * @author AARTHIKA
 */
public class WriteToXML {

    private static Document document;
    
    public static Document getDocument(){
        return document;
    }

    public static Document createDocument() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            document = docBuilder.newDocument();

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(WriteToXML.class.getName()).log(Level.SEVERE, null, ex);
        }
        return document;
    }

    public static void createXML() {
        String destinationPath = PropertyFile.xmlSourceCodeFilePath;
        String fileName = AST.file.replaceAll(".java", "");
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new FileOutputStream(destinationPath +"\\"+ fileName+".xml" ,true));
            transformer.transform(source, result);              
            System.out.println("File saved!");
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(WriteToXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(WriteToXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(WriteToXML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
