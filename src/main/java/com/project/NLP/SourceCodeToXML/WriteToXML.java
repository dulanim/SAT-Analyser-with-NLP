/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.SourceCodeToXML;

import java.io.File;
import java.io.FileNotFoundException;
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

import com.project.NLP.file.operations.FilePropertyName;
import com.project.extension.event.trigger.EventTrigger;
import com.project.property.config.xml.writer.Adapter;

/**
 * Creates the xml
 * @author AARTHIKA
 */
public class WriteToXML {

    public  static Document document;
    private static String destinationPath;
    private static File file;
    public  static Element artefacts, artefactType, fileLocation;
    public  static String isTragging = "NONE";
    public  static String TAG = "NONE";
    public static Document getDocument() {
        return document;
    }

    /**
     * Returns the file Path
     */
    public static void getFilePath(){
        String root = Adapter.projectPath;
        
        if(isTragging.equals("Tragging")){
        	root = EventTrigger.projectPath;
        }
        //System.out.println("Root: "+root);
        File f = new File(root + File.separator +FilePropertyName.XML);
        if(!f.exists())
            f.mkdir();
        
        if(isTragging.equals("Tragging")){
        	destinationPath = FilePropertyName.getXMLFilePath(root, TAG);
        }else{
        	destinationPath = FilePropertyName.getXMLFilePath(root, TAG);
        }
        file = new File(destinationPath);
//        destinationPath = "/home/shiyam/Desktop/SatWrks/Jar/" + FilePropertyName.SOURCE_ARTIFACT_NAME;
//        file = new File(destinationPath);
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
            
            file.delete();
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
        File target;        
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");//No I18N
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");//No I18N
            DOMSource source = new DOMSource(document);
            target = createXmlFile();
            StreamResult result = new StreamResult(target.getPath());
            transformer.transform(source, result);
            //System.out.println("File saved!");
        } catch (TransformerConfigurationException ex) {
            Logger.getLogger(WriteToXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TransformerException ex) {
            Logger.getLogger(WriteToXML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }catch(IOException ex){
        	 Exceptions.printStackTrace(ex);
        }
    }

    /**
     * Creates the instance of fileoutputstream based on the existence of xml
     * @return
     * @throws IOException 
     */
    private static File createXmlFile() throws IOException{
        File target = null;
        
        if (!file.exists()) {
        	if(isTragging.equalsIgnoreCase("Tragging")){
        		String temp = destinationPath;
        		String fileName = "";
        		if(TAG.equals("OLD"))
        			fileName = FilePropertyName.SOURCE_ARETEFACT_NAME_OLD;
        		else if(TAG.equals("NEW"))
        			fileName = FilePropertyName.SOURCE_ARETEFACT_NAME_NEW;
        		destinationPath = temp.substring(0,file.getAbsolutePath().
        				lastIndexOf(File.separator)) + File.separator + fileName;
        		target = new File(new String(destinationPath));
        		destinationPath = temp;
        	}else{
        		target = new File(destinationPath);
        		return target;
        	}
        } else {
                  
            File f = new File(destinationPath);
            f.delete();
//            f.createNewFile();    
//            fos = new FileOutputStream(f.getAbsolutePath()); 
            target = createXmlFile();
        }
        return target;
    }

    /*public static void main(String args[]) {
        createDocument();
        createXML();
    }*/

}
