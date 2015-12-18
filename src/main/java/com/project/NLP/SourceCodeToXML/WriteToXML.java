/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.SourceCodeToXML;

import com.project.NLP.file.operations.FilePropertyName;
import com.project.traceability.GUI.HomeGUI;
import com.project.traceability.GUI.ProjectCreateWindow;
import com.project.traceability.common.PropertyFile;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.openide.util.Exceptions;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author AARTHIKA
 */
public class WriteToXML {

    private static Document document;
    //private static final String fileName = FilePropertyName.SOURCE_ARTIFACT_NAME;
                //AST.file.replaceAll(".java", "");
    public static File f = new File(PropertyFile.filePath+ File.separator 
                        +FilePropertyName.SOURCE_CODE);
    private static final String destinationPath = HomeGUI.tree.getToolTipText() + File.separator + ProjectCreateWindow.projectName
            +File.separator + FilePropertyName.XML;
           
    private static String fileName =  FilePropertyName.SOURCE_ARTIFACT_NAME;
    private static final File file = new File(destinationPath,fileName);
    
    public static Document getDocument(){
        return document;
    }

    private static boolean checkFileExist(){
        return file.exists();
    }
    
    public static Document createDocument() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            if(checkFileExist())
                document = docBuilder.parse(file);
            else
                document = docBuilder.newDocument();

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(WriteToXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException | IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return document;
    }

    public static void createXML() {
        
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
	    transformer.setOutputProperty(OutputKeys.INDENT, "yes");//No I18N
	    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");//No I18N
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new FileOutputStream
                        (destinationPath +File.separator+ fileName));
            transformer.transform(source, result);              
            System.out.println("File saved!");
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(WriteToXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException | TransformerException ex) {
            Logger.getLogger(WriteToXML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
