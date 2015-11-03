/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.SourceCodeToXML;

import com.project.traceability.common.PropertyFile;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
import org.openide.util.Exceptions;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 *
 * @author AARTHIKA
 */
public class WriteToXML {

    public static Document document;
    private static final String destinationPath = PropertyFile.xmlSourceCodeFilePath;
    private static final String fileName = "SourceCodeArtefactFile";
    //AST.file.replaceAll(".java", "");
    private static final File file = new File(destinationPath + "\\" + fileName + ".xml");
    public static Element artefacts, artefactType, fileLocation;

    public static Document getDocument() {
        return document;
    }

    private static boolean checkFileExist() {
        return file.exists();
    }

    public static Document createDocument() {
        try {
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

    public static void createXML() {
        FileOutputStream fos;        
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            fos = createXmlFile();
            StreamResult result = new StreamResult(fos);
            transformer.transform(source, result);
            System.out.println("File saved!");
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(WriteToXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(WriteToXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private static FileOutputStream createXmlFile() throws FileNotFoundException{
        File file;
        FileOutputStream fos = null;
        
        file = new File(destinationPath + "\\" + fileName + ".xml");
        if (!file.exists()) {
            fos = new FileOutputStream(destinationPath + "\\" + fileName+ ".xml");
        } else {
            System.out.println("Source code xml file for the project exissts already.\nDo you waant to create a new version or update the file?\n(Enter Yes to create a new version) ");
            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
            String response;
            try {
                response = bufferRead.readLine();
                if (response.equalsIgnoreCase("Yes")) {
                    System.out.println("Enter new file name: ");
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    String newName = br.readLine();
                    fos = new FileOutputStream(destinationPath + "\\" + newName + ".xml");
                } else {
                    fos = new FileOutputStream(destinationPath + "\\" + fileName+ ".xml");
                }
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        return fos;
    }

    public static void main(String args[]) {
        createDocument();
        createXML();
    }

}
