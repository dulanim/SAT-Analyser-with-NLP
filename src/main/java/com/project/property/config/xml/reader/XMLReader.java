package com.project.property.config.xml.reader;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.project.NLP.staticdata.StaticData;
import com.project.traceability.common.PropertyFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;


public class XMLReader {

    DocumentBuilderFactory dbFactory;
    DocumentBuilder dBuilder;
    Document doc;
        public XMLReader(){
            File config_XmlFile = new File(PropertyFile.configuration_file_path);
		
		try{
                    dbFactory = DocumentBuilderFactory.newInstance();
                    dBuilder = dbFactory.newDocumentBuilder();
                    doc = dBuilder.parse(config_XmlFile);
                }catch(SAXException e){
                    JOptionPane.showMessageDialog(null, e.toString(), "Read XML Error", JOptionPane.ERROR_MESSAGE);
                }catch(ParserConfigurationException e){
                    JOptionPane.showMessageDialog(null, e.toString(), "Read XML Error", JOptionPane.ERROR_MESSAGE);
                }catch(IOException e){
                    JOptionPane.showMessageDialog(null, e.toString(), "Read XML Error", JOptionPane.ERROR_MESSAGE);
                }
        }
	
        public boolean readStatus(){
            Boolean any_true = false;
            try{
                 //get workspaces
                 NodeList nList = doc.getElementsByTagName("Workspace");
                 Boolean active = false;
                 for(int i=0;i<nList.getLength();i++){
                      Element element = (Element)nList.item(i); 
                      String status = element.getAttribute("active");
                      active = Boolean.parseBoolean(status);
                      if(active){
                          String path = element.getAttribute("path");
                          StaticData.workspace = path;
                          return true;
                      }
                 }
                 StaticData.workspace = "";
                 any_true = false;
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, e.toString(), "Read XML Configuration Status Error", JOptionPane.ERROR_MESSAGE);
            }
            
            return any_true;
        }
        public void readWorkspaces(){
            
            File config_XmlFile = new File(PropertyFile.configuration_file_path);
		
		try{
                    dbFactory = DocumentBuilderFactory.newInstance();
                    dBuilder = dbFactory.newDocumentBuilder();
                    doc = dBuilder.parse(config_XmlFile);
                    //get workspaces
                    NodeList nList = doc.getElementsByTagName("Workspace");
                    Boolean active = false;
                    for(int i=0;i<nList.getLength();i++){
                       Element element = (Element)nList.item(i); 
                       String path = element.getAttribute("path");
                       StaticData.paths.add(path);
                    }   
                }catch(SAXException e){
                    
                }catch(ParserConfigurationException e){
                    
                }catch(IOException e){
                    
                }
               
        }
}
