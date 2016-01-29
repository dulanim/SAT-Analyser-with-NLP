package com.project.property.config.xml.writer;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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

import com.project.NLP.file.operations.FilePropertyName;
import com.project.traceability.common.PropertyFile;

public class XMLWriter {
        private static XMLWriter writer;
	String filepath = PropertyFile.configuration_file_path;//PropertyFile.filePath + File.separator + "sat_config.xml";
	DocumentBuilderFactory docFactory;
	DocumentBuilder docBuilder;
	Document doc;
	Element wrkspaceElmnt;
	private XMLWriter(String init) {
		//PropertyFile.configuration_file_path = filepath;//assign configuration file path it is in home directory
		
		if(init.equals("init")){
                    init();
        }
	}
        
        private XMLWriter(){
            init();
        }
        
        public static XMLWriter getXMLWriterInstance(){
            if(writer == null)
                writer = new XMLWriter();
            return writer;
        }
        
        public static XMLWriter getXMLWriterInstance(String status){
            if(writer == null)
                writer = new XMLWriter(status);
            return writer;
        }
        private void init(){
            try{
				File file = new File(filepath);
				if(!file.exists()){
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
		}catch(ParserConfigurationException e){
             e.printStackTrace();
        }
           
        }
	private void create_root(){
		
		//FileWriter reader = new F
                Element root_element_configs = doc.createElement("Configs");
                doc.appendChild(root_element_configs);
                Element wrkspacesElmnt = doc.createElement("Workspaces");
                root_element_configs.appendChild(wrkspacesElmnt);
                createDefaultNode();//adding default to configs node
	}
	public void modifyStatus(Boolean active,String path){
		//switch project path
            int x = path.lastIndexOf(File.separator);
            if(x == path.length() -1){
                path = path.substring(0,x);
            }
            boolean modified = false;
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
                                    modified = true;
                                    call_done();
				}
			}
                        if(!modified){
//                            nList = doc.getElementsByTagName("Workspace");
//                            Element element = (Element)nList.item(0);
//                            
//                            NamedNodeMap attr = element.getAttributes();
//                            Node nodeAttr = attr.getNamedItem("active");
//                            nodeAttr.setTextContent(Boolean.toString(false));
                            
                            createWorkspaceNode(path, Boolean.toString(active));

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

            NameAttr = doc.createAttribute("currentPath");
            NameAttr.setValue("");
            config.setAttributeNode(NameAttr);
 
            root.appendChild(config);
          
            Element opened = doc.createElement("Opened");
            
            root.appendChild(opened);
            call_done();
        }
        
        public void changeCurrnntWorkspaceVale(String aValue){
            /*
            which change the currentWorkspace value 
            @param aValue is acutually a workspace path /home/shiyam/workspace
            */
                init();
                NodeList nodeList = doc.getElementsByTagName("Config");
                Element element = (Element)nodeList.item(0);

                NamedNodeMap attr = element.getAttributes();
                Node nodeAttr = attr.getNamedItem("currentPath");
                nodeAttr.setTextContent(aValue);

                nodeAttr = attr.getNamedItem("active");
                nodeAttr.setTextContent("true");
                call_done();
           
        }
	public void createWorkspaceNode(String path,String active){
		//create main project path
		//Node root = doc.getElementsByTagName("Configs").item(0);
	
        
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
	
	
	public void createProjectWorksapce(String path){
		/*
		 * create a projects node like this 
		 * <Projects workspace = /path/home/documents/path/workspace/>
		 */
			
                    Node openedNode = doc.getElementsByTagName("Opened").item(0);
			
                    Element wrkspaceElmnt = doc.createElement("Projects");
		    Attr Attr = doc.createAttribute("path");
		    Attr.setValue(path);
		     
		    wrkspaceElmnt.setAttributeNode(Attr);
		    
                    Element visbleElmnt = doc.createElement("Visible");
		    Attr = doc.createAttribute("path");
		    Attr.setValue("");
                    
                    wrkspaceElmnt.appendChild(visbleElmnt);
		    openedNode.appendChild(wrkspaceElmnt);
		    call_done();
	}
	
        
       
        
        public void setVisibleFilePath(String wrkspace,String path){
            
             NodeList wrkspaceList = doc.getElementsByTagName("Projects");
             
             for(int i=0;i<wrkspaceList.getLength();i++){
                 
                Element elmnt = (Element)wrkspaceList.item(i);
                String wrkPath = elmnt.getAttribute("path");
                
                if(wrkPath.equalsIgnoreCase(wrkspace)){
                    NodeList visbleNode = elmnt.getElementsByTagName("Visible");
                    elmnt = (Element)visbleNode.item(0);

                    elmnt.setAttribute("path", path);

                    call_done();
                    break;
                }
             }
        }
	public boolean isWorkspaceExists(String wrkspace){
		/*
		 * @wrkspace /home/shiyam/wrkspace
		 * check it exists or not in projects node in sat_configuration.xml
		 */
		boolean isExist = false;
			
		if(!wrkspace.equals("")){
			
				NodeList wrkspaces = doc.getElementsByTagName("Projects");
				for(int i=0;i<wrkspaces.getLength();i++){
					wrkspaceElmnt = (Element)wrkspaces.item(i);
					
	                String value = wrkspaceElmnt.getAttribute("path");
	                
	                if(value.equalsIgnoreCase(wrkspace)){
	                	isExist = true;
	                	break;
	                }
				}
		}else{
			isExist = false;
		}
			
		return isExist;
	}
	
	public static String getWorkspace(String projectPath){
            
                if(projectPath.equals(""))
                    return "path";
		int end = projectPath.lastIndexOf(File.separator);
		String wrkspace = projectPath.substring(0,end);
		return wrkspace;
	}
	public void createProject(String projectPath){
			/*
			 * @projectPath is /home/documents/workspace/Anduril
			 * it actually create a project node inside the workspace
			 */
			
		String wrkspace = getWorkspace(projectPath);
                    if(!isWorkspaceExists(wrkspace)){
			createProjectWorksapce(wrkspace);
                    }
			
			NodeList wrkspaces = doc.getElementsByTagName("Projects");
			for(int i=0;i<wrkspace.length();i++){
				wrkspaceElmnt = (Element)wrkspaces.item(i);
				
				NamedNodeMap attr = wrkspaceElmnt.getAttributes();
                Node nodeAttr = attr.getNamedItem("path");
                String value = nodeAttr.getTextContent();
                
                if(value.equalsIgnoreCase(wrkspace)){
                	

    				Element projectElmnt = doc.createElement("Project");
    				
        		    Attr Attr = doc.createAttribute(FilePropertyName.SOURCE_CODE);
        		    Attr.setValue("false");
        		    projectElmnt.setAttributeNode(Attr);

        		    Attr = doc.createAttribute(FilePropertyName.PROPERTY);
        		    Attr.setValue("false");
        		    projectElmnt.setAttributeNode(Attr);
        		    
        		    Attr = doc.createAttribute(FilePropertyName.XML);
        		    Attr.setValue("true");
        		    projectElmnt.setAttributeNode(Attr);
        		    
        		    Attr = doc.createAttribute(FilePropertyName.REQUIREMENT);
        		    Attr.setValue("true");
        		    projectElmnt.setAttributeNode(Attr);
        		    
        		    Attr = doc.createAttribute(FilePropertyName.UML);
        		    Attr.setValue("true");
        		    projectElmnt.setAttributeNode(Attr);
        		    
        		    Attr = doc.createAttribute("expanded");
        		    Attr.setValue("true");
        		    projectElmnt.setAttributeNode(Attr);
        		    
        		    Attr = doc.createAttribute("path");
        		    Attr.setValue(projectPath);
        		    projectElmnt.setAttributeNode(Attr);
    				wrkspaceElmnt.appendChild(projectElmnt);
        		    call_done();
        		    
        		    break;
                }
			}
			
	}
	
	public void createFileNode(String projectPath,String filePath){
		/*
		 * THis method actually creating new file node with path value and opened status varibale in
		 * sat_Configuration.xml
		 * @param projectPath :: /home/dcs/wrkspace/projects/project1
		 * @param filePath :: /home/dcs/wrkspace/projects/project1/txt/shiyam_docs.txt
		 * defaule opened varable name is set up to false 
		 */
//		String wrkSpace = getWorkspace(projectPath);
		NodeList projects = doc.getElementsByTagName("Project");
		int i;
		for( i=0;i<projects.getLength();i++){
                    Element projctElmnt = (Element) projects.item(i);
                    String path = projctElmnt.getAttribute("path");
				
                        if(path.equals(projectPath)){
                            Element filesElmnt = (Element) projctElmnt.getElementsByTagName("Files").item(0);
					
                                if(filesElmnt == null){
                                    filesElmnt = doc.createElement("Files");
                                    projctElmnt.appendChild(filesElmnt);
                                    call_done();
						
                                    filesElmnt = (Element) projctElmnt.getElementsByTagName("Files").item(0);
				}
					
                                NodeList fileList = filesElmnt.getElementsByTagName("File");
                                for(int j=0;j<fileList.getLength();j++){
                                            /*
                                            check file nnode already exists or not
                                            */
                                    Element fileElmnt = (Element)fileList.item(j);
                                    path = fileElmnt.getAttribute("path");
                                    if(path.equalsIgnoreCase(filePath)){
                                                return;
                                        }
                                }
                                Element fileElmnt = doc.createElement("File");
					
				Attr Attr = doc.createAttribute("path");
                                Attr.setValue(filePath);
                                fileElmnt.setAttributeNode(Attr);

                                Attr = doc.createAttribute("opened");
                                Attr.setValue("false");
                                fileElmnt.setAttributeNode(Attr);

                                filesElmnt.appendChild(fileElmnt);

                                call_done();
                                        
                                return;
                    }
                		
		}
                
                if(projects.getLength() == 0){
                    createProject(projectPath);
                    createFileNode(projectPath, filePath);
                    
                }
                if(i == projects.getLength()){
		/*
                    when new workspace created with file node 
                    */
                createProject(projectPath);
                createFileNode(projectPath, filePath);
                }
                
               
	}
	
	public void changeFileStatus(String filePath,boolean status){
		
		NodeList fileList = doc.getElementsByTagName("File");
		
		for(int i=0;i<fileList.getLength();i++){
			Element fileElmnt = (Element) fileList.item(i);
			String path = fileElmnt.getAttribute("path");
			if(path.equalsIgnoreCase(filePath)){
				fileElmnt.setAttribute("opened", Boolean.toString(status));
				call_done();
				break;
			}
		}
	}
	public void setProjectStatus(String projectPath,String sub,boolean status){
		
		NodeList projects = doc.getElementsByTagName("Project");
		
		for(int i=0;i<projects.getLength();i++){
			
			Element elmnt = (Element) projects.item(i);
			String path = elmnt.getAttribute("path");
			
			if(path.equalsIgnoreCase(projectPath)){
				NamedNodeMap attr = elmnt.getAttributes();
                                Node nodeAttr = attr.getNamedItem(sub);
                                nodeAttr.setTextContent(Boolean.toString(status));
                
                                call_done();
                                break;
			}
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
	
	public Map<String,Boolean> retriveExpenededProject(String projectPath){
		Map<String,Boolean> projectsInfos
						= new HashMap<>();
		
		NodeList ProjectNode = doc.getElementsByTagName("Project");
		
		for(int i=0;i<ProjectNode.getLength();i++){
			Element projectElmnt = (Element) ProjectNode.item(i);
			String path = projectElmnt.getAttribute("path");
			if(path.equalsIgnoreCase(projectPath)){
				
					String status = projectElmnt.getAttribute("expanded");
					
					if(status.equals("true")){
						String projectName = projectElmnt.getAttribute("path");
							projectName = projectName.substring(projectName.lastIndexOf(File.separator)+1, projectName.length());
						
						
						boolean srcStatus = Boolean.parseBoolean(projectElmnt.getAttribute(FilePropertyName.SOURCE_CODE));
						boolean txtStatus = Boolean.parseBoolean(projectElmnt.getAttribute(FilePropertyName.REQUIREMENT));
						boolean xmlStatus = Boolean.parseBoolean(projectElmnt.getAttribute(FilePropertyName.XML));
						boolean umlStatus = Boolean.parseBoolean(projectElmnt.getAttribute(FilePropertyName.UML));
						
						projectsInfos.put(FilePropertyName.SOURCE_CODE,srcStatus);
						projectsInfos.put(FilePropertyName.REQUIREMENT,txtStatus);
						projectsInfos.put(FilePropertyName.XML,xmlStatus);
						projectsInfos.put(FilePropertyName.UML,umlStatus);
						
						break;
					}
				
				
			}
		}
		return projectsInfos;
	}
	
	public List<String> retriveOpenedFiles(){
		
		List<String> openedFiles = new ArrayList<>();
		
		NodeList files = doc.getElementsByTagName("File");
		
		for(int i=0;i<files.getLength();i++){
			Element fileElmnt = (Element) files.item(i);
			String status = fileElmnt.getAttribute("opened");
			Boolean blnStatus = Boolean.parseBoolean(status);
			if(blnStatus){
				String filePath = fileElmnt.getAttribute("path");
				openedFiles.add(filePath);
			}
		}
		
		return openedFiles;
	}
//	public static void main(String args[]){
//		XMLWriter writer = new XMLWriter();
////		writer.createProjectWorksapce("/home/shiyam");
////		writer.createProject("/home/shiyam/RamProject");
////		writer.setProjectStatus("/home/shiyam/RamProject", "src", true);
////		writer.createFileNode("/home/shiyam/RamProject", "/home/shiyam/RamProject/sat_config.xml");
////		writer.changeFileStatus("/home/shiyam/RamProject/sat_config.xml",true);
////		
////		writer.createFileNode("/home/shiyam/RamProject", "/home/shiyam/RamProject/sat_config.xml");
////		
////		writer.createProject("/home/shiyam/12/SRR");
////		writer.createFileNode("/home/shiyam/SRR", "/home/shiyam/RamProject/sat_config.xml");
//                
////                List<String> files = writer.retriveOpenedFiles();
////                
////                for(String file:files){
////                    System.out.println(file);
////                }
//	}
        
        
}
