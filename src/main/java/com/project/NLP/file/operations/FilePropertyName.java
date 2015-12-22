/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.file.operations;

import com.project.traceability.GUI.HomeGUI;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;


/**
 *
 * @author shiyam
 * which has contains default project path
 * uml diagram default path
 * requirement document default path
 * java project root default path
 */
public class FilePropertyName {
    public static String user_home = System.getProperty("user.home") + File.separator + "SATAnalyzer";
    public static String RESOURCE_PATH =  System.getProperty("user.dir") + File.separator + "res";
    public static final String default_project_path = RESOURCE_PATH+ File.separator + "Anduril";
    public static final String default_requirement_doc_path  = RESOURCE_PATH + File.separator + "requirement.txt";
    public static final String default_uml_file_path = RESOURCE_PATH + File.separator + "umlFile.json";
    public static final String default_java_project_path = RESOURCE_PATH + File.separator + "JavaSourceCode";
    public static final String XML = "xml";
    public static final String REQUIREMENT = "txt";
    public static final String UML = "uml";
    public static final String SOURCE_CODE = "src";
    public static final String PROPERTY = "property";
    public static final String TEMP = "temp";
    public static final String UML_ARTIFACT_NAME = "UMLArtefactFile.xml";
    public static final String REQUIREMENT_ARTIFACT_NAME = "RequirementArtefactFile.xml";
    public static final String SOURCE_ARTIFACT_NAME = "SourceCodeArtefactFile.xml";
    public static final String IMAGE_PATH = System.getProperty("user.dir") + File.separator + "img"+ File.separator;
    public static Path getPath(Path target,String type){
        if(type.contains(XML)){
            target = Paths.get(HomeGUI.projectPath + File.separator + XML);
        }else if(type.contains(REQUIREMENT) || type.contains("txt") || type.contains("pdf")||
                type.contains("docs")||type.contains("doc")){
            target = Paths.get(HomeGUI.projectPath + File.separator + REQUIREMENT);
        }else if(type.contains("java")){
             target = Paths.get(HomeGUI.projectPath + File.separator + SOURCE_CODE);
        }else if(type.contains("mdj")||type.contains("json")||type.contains("xmi")||type.contains("uml")){
             target = Paths.get(HomeGUI.projectPath + File.separator + UML);
        }else{
            target = Paths.get(HomeGUI.projectPath + File.separator + PROPERTY);
        }
        return target;
    }
    public static void addSubFolderIntoProject(File folder){
        
        if(!folder.exists())
            folder.mkdirs();
        File add_file = new File(folder,FilePropertyName.XML);
        add_file.mkdirs();
        add_file = new File(folder,FilePropertyName.UML);
        add_file.mkdirs();
        add_file = new File(folder,FilePropertyName.REQUIREMENT);
        add_file.mkdirs();
        add_file = new File(folder,FilePropertyName.SOURCE_CODE);
        add_file.mkdirs();
        add_file = new File(folder,FilePropertyName.PROPERTY);
        add_file.mkdirs();                                                        
    }
    
    public static void copy(FileDialog fileDialog,String savePath){
		
                String selectedFiles[];
                Path path;
		if(savePath != null){
			selectedFiles = fileDialog.getFileNames();
			for (int k = 0; k < selectedFiles.length; k++) {
				
				path = Paths.get(savePath + selectedFiles[k]);
				Path target = Paths.get(savePath);
					try {
						Files.copy(path,
								target.resolve(path.getFileName()),
								REPLACE_EXISTING);
					} catch (IOException e1) {							
						e1.printStackTrace();
					}           
		}	
	}
	}
    
    public static void delete(File file)
    	throws IOException{
 
    	if(file.isDirectory()){
 
    		//directory is empty, then delete it
    		if(file.list().length==0){
                   file.delete();
    		   System.out.println("Directory is deleted : " 
                                                 + file.getAbsolutePath());  
    		}else{
    		   //list all the directory contents
        	   String files[] = file.list();
        	   for (String temp : files) {
        	      //construct the file structure
        	      File fileDelete = new File(file, temp);
        	      //recursive delete
                      delete(fileDelete);
        	   }
        	   //check the directory again, if empty then delete it
        	   if(file.list().length==0){
           	     file.delete();
        	     System.out.println("Directory is deleted : " 
                                                  + file.getAbsolutePath());
        	   }
    		}
    		
    	}else{//if file, then delete it
    		file.delete();
    		System.out.println("File is deleted : " + file.getAbsolutePath());
    	}
    }

}

