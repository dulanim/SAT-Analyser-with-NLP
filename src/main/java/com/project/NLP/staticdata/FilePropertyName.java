/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.staticdata;

import com.project.traceability.GUI.HomeGUI;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;


/**
 *
 * @author shiyam
 * which has contains default project path
 * uml diagram default path
 * requirement document default path
 * java project root default path
 */
public class FilePropertyName {
    public static String user_root =  System.getProperty("user.home") + 
    File.separator + "Documents";
    public static final String default_project_path = user_root+ File.separator + "Anduril";
    public static final String default_requirement_doc_path  = user_root + File.separator + "requirement.txt";
    public static final String default_uml_file_path = user_root + File.separator + "umlFile.json";
    public static final String default_java_project_path = user_root + File.separator + "JavaSourceCode";
    public static final String XML = "xml";
    public static final String REQUIREMENT = "txt";
    public static final String UML = "uml";
    public static final String SOURCE_CODE = "src";
    public static final String PROPERTY = "property";
    public static final String TEMP = "temp";
    public static final String UML_ARTIFACT_NAME = "UMLArtefactFile.xml";
    public static final String REQUIREMENT_ARTIFACT_NAME = "RequirementArtefactFile.xml";
    public static final String SOURCE_ARTIFACT_NAME = "SourceCodeArtefactFile.xml";
    public static final String IMAGE_PATH = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "Resources"+File.separator + 
            "Images" + File.separator;
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
}
