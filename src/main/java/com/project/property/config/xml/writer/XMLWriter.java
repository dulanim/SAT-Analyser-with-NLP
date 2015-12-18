package com.project.property.config.xml.writer;


import com.project.traceability.staticdata.StaticData;
import com.project.traceability.common.PropertyFile;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.util.Date;
public class XMLWriter {
	String filepath = PropertyFile.configuration_file_path;//PropertyFile.filePath + File.separator + "sat_config.xml";
	DocumentBuilderFactory docFactory;
	DocumentBuilder docBuilder;
	Document doc;
	public XMLWriter() {
		PropertyFile.configuration_file_path = filepath;//assign configuration file path it is in home directory
		try{
			File file = new File(filepath);
			if(!file.exists())
				{
					file.createNewFile();
                                        docFactory = DocumentBuilderFactory.newInstance();
					docBuilder = docFactory.newDocumentBuilder();
					doc = docBuilder.newDocument();
                                        create_root();
					
                                }
                        docFactory = DocumentBuilderFactory.newInstance();
                        docBuilder = docFactory.newDocumentBuilder();
                        doc = docBuilder.parse(filepath);
                                
		}catch(IOException e){
			e.printStackTrace();
		}catch(SAXException e){
                        e.printStackTrace();
                }catch(Exception e){
                    e.printStackTrace();
                }
		
	}
	private void create_root(){
		
		//FileWriter reader = new F
                Element root_element_configs = doc.createElement("Configs");
                doc.appendChild(root_element_configs);
                Element wrkspacesElmnt = doc.createElement("Workspaces");
                root_element_configs.appendChild(wrkspacesElmnt);
		call_done();
                createDefaultNode();//adding default to configs node
	}
	public void modifyStatus(Boolean active,String path){
		//switch project path
		try {
			NodeList nList = doc.getElementsByTagName("Workspace");
			for(int i=0;i<nList.getLength();i++){
				Element element = (Element)nList.item(i);
				String workspace_path = element.getAttribute("path");
                               
                                if(workspace_path.equals(path)){
				// update  attribute
                                    NamedNodeMap attr = element.getAttributes();
                                    Node nodeAttr = attr.getNamedItem("active");
                                    nodeAttr.setTextContent(Boolean.toString(active));
                                    call_done();
                                    
				}else{
                                    NamedNodeMap attr = element.getAttributes();
                                    Node nodeAttr = attr.getNamedItem("active");
                                    nodeAttr.setTextContent(Boolean.toString(false));
                                    call_done();
                                }
			}	
		}catch(Exception e){
			
		}
	}
        
        public void createDefaultNode(){
            //adding default config node
            Node root = doc.getElementsByTagName("Configs").item(0);
            Element config = doc.createElement("Config");

            Attr NameAttr = doc.createAttribute("active");
            NameAttr.setValue("");
            config.setAttributeNode(NameAttr);

            NameAttr = doc.createAttribute("default");
            NameAttr.setValue("");
            config.setAttributeNode(NameAttr);
           
            root.appendChild(config);
        
            call_done();
        }
        public Boolean readDefaultNode(){
            //get status
            NodeList nList = doc.getElementsByTagName("Config");
            Boolean active = false;
            for(int i=0;i<nList.getLength();i++){
               Element element = (Element)nList.item(i); 
               String tempactive = element.getAttribute("active");
               active = Boolean.parseBoolean(tempactive);
               StaticData.workspace = element.getAttribute("default");
            }       
            return active;
        }
	public void createWorkspaceNode(String path,String active){
		//create main project path
	Node root = doc.getElementsByTagName("Configs").item(0);
	
        
        Node wrkspacesNode = doc.getElementsByTagName("Workspaces").item(0);
        
        Element wrkspaceElmnt = doc.createElement("Workspace");
        Attr Attr = doc.createAttribute("path");
        Attr.setValue(path);
        wrkspaceElmnt.setAttributeNode(Attr);
        
        Attr = doc.createAttribute("createTime");
        Attr.setValue(new Date().toString());//add date time optional field
        wrkspaceElmnt.setAttributeNode(Attr);
        
        Attr = doc.createAttribute("active");
        Attr.setValue(active);
        wrkspaceElmnt.setAttributeNode(Attr);
        
        wrkspacesNode.appendChild(wrkspaceElmnt);
        
        call_done();
	}
	public void deleteProjectFolder(String path,String folderName){
		try {
			NodeList nList = doc.getElementsByTagName("Config");
			for(int i=0;i<nList.getLength();i++){
				Element element = (Element)nList.item(i);
				NodeList rootList = element.getElementsByTagName("Root");
				
				for(int j=0;j<rootList.getLength();j++){
					Element root = ((Element)rootList.item(j));
					Element parent = (Element)root.getElementsByTagName("Folders").item(0);
					String tempPath = root.getAttribute("filepath");
					if(tempPath.equals(path)){
						// delete  folder name
						
						NodeList folderList = parent.getElementsByTagName("Folder");
						for(int k=0;k<folderList.getLength();k++){
							String foldername = ((Element)folderList.item(k)).getAttribute("name");
							if(foldername.equals(folderName)){
								parent.removeChild(folderList.item(k));
								call_done();
								return;
							}
						}
					
					}
				}//inner for loop finish
			}//outer for loop finish
		}catch(Exception e){
			
		}
	}
       
	public void createSubFolderName(String path,String folderName){
		
		//when project adding to specific path or a folder adding into path
		try {
			NodeList nList = doc.getElementsByTagName("Config");
			for(int i=0;i<nList.getLength();i++){
				Element element = (Element)nList.item(i);
				NodeList rootList = element.getElementsByTagName("Root");
				
				for(int j=0;j<rootList.getLength();j++){
					Element root = ((Element)rootList.item(j));
					Element parent = (Element)root.getElementsByTagName("Folders").item(0);
					String tempPath = root.getAttribute("path");
					if(tempPath.equals(path)){
						
						// append a new node to Root
                                            Element folder = doc.createElement("Folder");
					
                                            Attr NameAttr = doc.createAttribute("name");
                                            NameAttr.setValue(folderName);
                                            folder.setAttributeNode(NameAttr);
	                    
                                            Attr createTimeAttr = doc.createAttribute("createTime");
                                            createTimeAttr.setValue("");
                                            folder.setAttributeNode(createTimeAttr);
	                    
                                            Attr modifedTimeAttr = doc.createAttribute("modifiedTime");
                                            modifedTimeAttr.setValue("");
                                            folder.setAttributeNode(modifedTimeAttr);	
	                    
                                            parent.appendChild(folder);

                                            call_done();
                                            return ;
					}
				}
				
			}
			
			
		}catch(Exception e){
			
		}
	}
	
	public void call_done(){
		try{
			
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");//No I18N
	        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");//No I18N
                DOMSource source = new DOMSource(doc);
	         //String driveLetter = getSuitableDrive();
	                
	        File f = new File(filepath);
	        StreamResult result = new StreamResult(f.getPath());
	        transformer.transform(source, result);
	        System.out.println("File saved into " + f.getPath());
		
                System.out.println("Done");
		}catch (TransformerException tfe) {
			tfe.printStackTrace();
		}catch(Exception e){
			 
		}
	}
}
