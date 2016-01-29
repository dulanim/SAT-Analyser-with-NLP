/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.SourceCodeToXML;

import com.project.NLP.file.operations.FilePropertyName;
import com.project.property.config.xml.writer.Adapter;
import com.project.traceability.GUI.HomeGUI;
import com.project.traceability.GUI.ProjectCreateWindow;
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
 *
 * @author AARTHIKA
 */
public class WriteToXML {

    public static Document document;
    private static String destinationPath;
    //AST.file.replaceAll(".java", "");
    private static File file;
    public static Element artefacts, artefactType, fileLocation;

    public static Document getDocument() {
        return document;
    }

    public static void getFilePath(){
        String root = Adapter.projectPath;
        System.out.println("Root: "+root);
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
        FileOutputStream fos = null;
        
        if (!file.exists()) {
            fos = new FileOutputStream(destinationPath,false);
        } else {
            fos = new FileOutputStream(destinationPath,false);
            /*System.out.println("Source code xml file for the project exissts already.\nDo you waant to create a new version or update the file?\n(Enter Yes to create a new version) ");
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
                    fos = new FileOutputStream(destinationPath);
                }
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }*/
        }
        return fos;
    }

    public static void main(String args[]) {
        createDocument();
        createXML();
    }

}
