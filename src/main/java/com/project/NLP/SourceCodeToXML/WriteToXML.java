/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.SourceCodeToXML;

import com.project.NLP.file.operations.FilePropertyName;
import com.project.property.config.xml.writer.Adapter;
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
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Creates the xml
 * @author AARTHIKA
 */
public class WriteToXML {

    public static Document document;
    private static String destinationPath;
    private static File file;
    public static Element artefacts, artefactType, fileLocation;

    public static Document getDocument() {
        return document;
    }

    /**
     * Returns the file Path
     */
    public static void getFilePath(){
        String root = Adapter.projectPath;
        //System.out.println("Root: "+root);
        File f = new File(root + File.separator +FilePropertyName.XML);
        if(!f.exists())
            f.mkdir();
        destinationPath = f.getPath() + File.separator + FilePropertyName.SOURCE_ARTIFACT_NAME;
        file = new File(destinationPath);
        /*destinationPath = "D:\\myVirtusa\\xml\\Source1.xml";
        file = new File(destinationPath);*/
    }
    
    private static boolean checkFileExist() {
        return file.exists();
    }

    /**
     * Creates a document for xml
     * @return 
     */
    public static Document createDocument() {        
        try {
            getFilePath();
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            if (checkFileExist()) {
                document = docBuilder.parse(file);
            } else {
                document = docBuilder.newDocument();
                artefacts = document.createElement("Artefacts");
                document.appendChild(artefacts);
                artefactType = document.createElement("Artefact");
                artefacts.appendChild(artefactType);

                Attr type = document.createAttribute("type");
                type.setValue("SourceCode");
                artefactType.setAttributeNode(type);

                fileLocation = document.createElement("FileSystemLocation");
                artefactType.appendChild(fileLocation);

            }

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(WriteToXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException | IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return document;
    }

    /**
     * Creates the final xml
     */
    public static void createXML() {
        FileOutputStream fos;        
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");//No I18N
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");//No I18N
            DOMSource source = new DOMSource(document);
            fos = createXmlFile();
            StreamResult result = new StreamResult(fos);
            transformer.transform(source, result);
            //System.out.println("File saved!");
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(WriteToXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(WriteToXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     * Creates the instance of fileoutputstream based on the existence of xml
     * @return
     * @throws FileNotFoundException 
     */
    private static FileOutputStream createXmlFile() throws FileNotFoundException{
        FileOutputStream fos = null;
        
        if (!file.exists()) {
            fos = new FileOutputStream(destinationPath,false);
        } else {
            fos = new FileOutputStream(destinationPath,false);            
        }
        return fos;
    }

    /*public static void main(String args[]) {
        createDocument();
        createXML();
    }*/

}
