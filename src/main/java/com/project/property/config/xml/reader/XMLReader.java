package com.project.property.config.xml.reader;

import com.project.NLP.file.operations.FilePropertyName;
import com.project.property.config.xml.writer.XMLWriter;
import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.project.traceability.staticdata.StaticData;
import com.project.traceability.common.PropertyFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        public void readDefaultNode(){
            //get status
            NodeList nList = doc.getElementsByTagName("Config");
            Boolean active = false;
            for(int i=0;i<nList.getLength();i++){
               Element element = (Element)nList.item(i); 
               String tempactive = element.getAttribute("active");
               active = Boolean.parseBoolean(tempactive);
               StaticData.workspace = element.getAttribute("currentPath");
            }       
            
        }
        
        public Map<String,Map<String,Boolean>> retriveExpenededProject(String wrkspace){
		Map<String,Map<String,Boolean>> projectsInfos = new HashMap<>();
		
		NodeList wrkspaceNode = doc.getElementsByTagName("Projects");
		
		for(int i=0;i<wrkspaceNode.getLength();i++){
			Element wrkspaceElmnt = (Element) wrkspaceNode.item(i);
			String path = wrkspaceElmnt.getAttribute("path");
			if(path.equalsIgnoreCase(wrkspace)){
				
                            NodeList projectNode = wrkspaceElmnt.getElementsByTagName("Project");
                                
                            for(int j=0;j<projectNode.getLength();j++){
                                
                                Map<String,Boolean> aMap = new HashMap<>();
                                Element projectElmnt = (Element)projectNode.item(j);
                                String status = projectElmnt.getAttribute("expanded");
					
					
						String projectName = projectElmnt.getAttribute("path");
                                                projectName = projectName.substring(projectName.lastIndexOf(File.separator)+1, projectName.length());
						
						boolean projectStatus = Boolean.parseBoolean(projectElmnt.getAttribute("expanded"));
						boolean srcStatus = Boolean.parseBoolean(projectElmnt.getAttribute(FilePropertyName.SOURCE_CODE));
						boolean txtStatus = Boolean.parseBoolean(projectElmnt.getAttribute(FilePropertyName.REQUIREMENT));
						boolean xmlStatus = Boolean.parseBoolean(projectElmnt.getAttribute(FilePropertyName.XML));
						boolean umlStatus = Boolean.parseBoolean(projectElmnt.getAttribute(FilePropertyName.UML));
						boolean propertyStatus = Boolean.parseBoolean(projectElmnt.getAttribute(FilePropertyName.PROPERTY));
                                                aMap.put(projectName,projectStatus);
						aMap.put(FilePropertyName.SOURCE_CODE,srcStatus);
						aMap.put(FilePropertyName.REQUIREMENT,txtStatus);
						aMap.put(FilePropertyName.XML,xmlStatus);
						aMap.put(FilePropertyName.UML,umlStatus);
                                                aMap.put(FilePropertyName.PROPERTY,propertyStatus);
                                                projectsInfos.put(projectName,aMap);
                                        
                            }
                            break;
			}
		}
		return projectsInfos;
	}
	 public String getVisibleFile(String wrkspace){
            /*
             get visble file node 
             it needs to show visble file which was as visble when closed
             /home/shiyam/ramwrskspace/file.txt
             */
            NodeList wrkspaceNode = doc.getElementsByTagName("Projects");
            String visbleFile = "";
            for(int i=0;i<wrkspaceNode.getLength();i++){
                
                Element element = (Element)wrkspaceNode.item(i);
                String path = element.getAttribute("path");
                
                if(path.equalsIgnoreCase(wrkspace)){
                    Element elmnt = (Element)element.getElementsByTagName("Visible").item(0);
                    visbleFile = ((Element)elmnt).getAttribute("path");
                    break;
                }
            }
            
            return visbleFile;
        }
	public Map<String,List<String>> retriveOpenedFiles(String wrkspace){
		
		Map<String,List<String>> openedFiles = new HashMap<>();
		
                NodeList wrkspaceList = doc.getElementsByTagName("Projects");
                
                for(int k=0;k<wrkspaceList.getLength();k++){
                    Element wrkspaceElmnt = (Element)wrkspaceList.item(k);
                    
                    String wrksPath = wrkspaceElmnt.getAttribute("path");
                    if(wrksPath.equalsIgnoreCase(wrkspace)){
                       
                        NodeList projectList = wrkspaceElmnt.getElementsByTagName("Project");
                        for(int j=0;j<projectList.getLength();j++){
                        
                            Element projectElmnt = (Element)projectList.item(j);
                            NodeList files = projectElmnt.getElementsByTagName("File");


                            String projectpath = projectElmnt.getAttribute("path");

                            int start = projectpath.lastIndexOf(File.separator) + 1;
                            int end = projectpath.length();
                            String prjectName = projectpath.substring(start,end);

                            List<String> temp = new ArrayList();
                            for(int i=0;i<files.getLength();i++){
                                    Element fileElmnt = (Element) files.item(i);
                                    String status = fileElmnt.getAttribute("opened");
                                    Boolean blnStatus = Boolean.parseBoolean(status);
                                    if(blnStatus){

                                            String filePath = fileElmnt.getAttribute("path");
                                            temp.add(filePath);
                                    }
                            }
                        openedFiles.put(prjectName, temp);
                    }
                     break;   
                    }
                }
                
		
		return openedFiles;
	}
}
