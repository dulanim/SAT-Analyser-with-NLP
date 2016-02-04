/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.common;

import com.project.NLP.file.operations.FilePropertyName;
import com.project.traceability.GUI.HomeGUI;
import com.project.traceability.staticdata.StaticData;
import java.io.File;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.TreeItem;

/**
 *
 * @author shiyam
 */
public class ErrorFinder {
        static boolean isSource = false;
        static boolean isTxt =false;
        static boolean isUml = false;
        static boolean isProperty = false;
        static boolean isXMl = false;
    
    public static boolean  lookUpForFiles(String root,String type){
        boolean exists = false;
        File folder = new File(root + File.separator + type);
        String files[] = folder.list();
        int count = 0;
        if(files != null  && files.length>0){
            for(String file:files){
                if(file.contains(".mdj")
                        ||file.contains(".uml")){
                    exists = true;
                    break;
                }else if(file.contains(".txt")
                        ||file.contains(".docs")
                        ||file.contains(".html")){
                    exists = true;
                    break;
                }else if(file.contains(".xml")){
                        if(file.equals(FilePropertyName.RELATION_ARTIFACT_NAME)){
                           count++;
                        }
                        else if(file.equals(FilePropertyName.REQUIREMENT_ARTIFACT_NAME)){
                           count++;  
                        }else if(file.equals(FilePropertyName.SOURCE_ARTIFACT_NAME)){
                           count++;
                        }else if(file.equals(FilePropertyName.UML_ARTIFACT_NAME)){
                           count++; 
                        }
                        
                        if(count == 4){
                            exists = true;
                            break;
                        }
                }else if(file.contains(".java")){
                    exists = true;
                    break;
                }
            }
        }      
        return exists;    
    }
    
    
    public static boolean lookUpForFolders(String folders[]){
        
        int count = 0;
        if(folders == null)
        	return false;
         
        for(String folder:folders){
            
           if(folder.equals(FilePropertyName.SOURCE_CODE)){
               isSource = true;
               count++;
               //souurce folder inside the project
           }else if(folder.equals(FilePropertyName.REQUIREMENT)){
               isTxt = true;
               count++;
               //txt folder inside tje project
           }else if(folder.equals(FilePropertyName.UML)){
               isUml = true;
               count++;
               //uml folder inside the project
           }else if(folder.equals(FilePropertyName.PROPERTY)){
               isProperty = true;
               count++;
               //property folder inside the project
           }else if(folder.equals(FilePropertyName.XML)){
               isXMl = true;
               count++;
               //property folder inside the project
           }
           
           if(count == 5){
            return true;
           }
        }
        
        if(count == 5){
            return true;
        }else 
            return false;
        
    }
    
    public static void setProjectFolderResolve(String projectPath,TreeItem selection){
        File f = new File(projectPath);
        String folders[] = f.list();
        
        boolean foldersExists = lookUpForFolders(folders);
        
        if(!foldersExists){
            //if folder does not exists mkdirirectories
            resolve(projectPath, selection.getItems());
        }
    }
    public static void checkEachProject(){
        
        TreeItem projects[] = HomeGUI.tree.getItems();
        String wrkspace = StaticData.workspace;
        int pos = wrkspace.lastIndexOf(File.separator);
        if(pos >=0 && pos != wrkspace.length()-1)
            wrkspace += File.separator;///home/shiyam/
        
        for(TreeItem project:projects){
            String projectName = project.getText();
            String projectPath = wrkspace + projectName;
            
            File file = new File(projectPath);
            String folders[] = file.list();
            
            boolean status = lookUpForFolders(folders);
            if(!status){
                //set errorneous folder
                project.setImage(
                        new Image(HomeGUI.shell.getDisplay(),
                                FilePropertyName.IMAGE_PATH + "folderError.png"));
                checkSubFiles(projectPath,project);
                
                //resolve();
            }else{
                
                project.setImage(
                        new Image(HomeGUI.shell.getDisplay(),
                                FilePropertyName.IMAGE_PATH + "folder_root.gif"));
                checkSubFiles(projectPath,project);
            }
        } 
    }
    
    public static void checkSubFiles(String projectPath,TreeItem project){
        
                boolean isSourceFiles =
                            lookUpForFiles(projectPath, FilePropertyName.SOURCE_CODE);
                boolean isXMLFiles = 
                        lookUpForFiles(projectPath, FilePropertyName.XML);
                boolean isUmlFiles =
                        lookUpForFiles(projectPath, FilePropertyName.UML);
                boolean isTxtFiles = 
                        lookUpForFiles(projectPath, FilePropertyName.REQUIREMENT);
                
                 boolean isPropertyFiles = 
                        lookUpForFiles(projectPath, FilePropertyName.PROPERTY);
                
                TreeItem subFolders[] = project.getItems();
                for(TreeItem folder:subFolders){
                    String text = folder.getText();
                    if(text.equals(FilePropertyName.SOURCE_CODE)
                            && !isSourceFiles){
                        folder.setImage(new Image(HomeGUI.shell.getDisplay(),
                                FilePropertyName.IMAGE_PATH + "folderError.png"));
                        
                        project.setImage(
                                new Image(HomeGUI.shell.getDisplay(),
                                        FilePropertyName.IMAGE_PATH + "folderError.png"));
                    }else if(text.equals(FilePropertyName.XML)
                            && !isXMLFiles){
                        folder.setImage(new Image(HomeGUI.shell.getDisplay(),
                                FilePropertyName.IMAGE_PATH + "folderError.png"));
                        project.setImage(
                                new Image(HomeGUI.shell.getDisplay(),
                                        FilePropertyName.IMAGE_PATH + "folderError.png"));
                    }else if(text.equals(FilePropertyName.UML)
                            && !isUmlFiles){
                        folder.setImage(new Image(HomeGUI.shell.getDisplay(),
                                FilePropertyName.IMAGE_PATH + "folderError.png"));
                        project.setImage(
                                new Image(HomeGUI.shell.getDisplay(),
                                        FilePropertyName.IMAGE_PATH + "folderError.png"));
                    }else if(text.equals(FilePropertyName.REQUIREMENT)
                            && !isTxtFiles){
                        folder.setImage(new Image(HomeGUI.shell.getDisplay(),
                                FilePropertyName.IMAGE_PATH + "folderError.png"));
                        project.setImage(
                                new Image(HomeGUI.shell.getDisplay(),
                                        FilePropertyName.IMAGE_PATH + "folderError.png"));
                    }else if(text.equals(FilePropertyName.PROPERTY)
                            && !isPropertyFiles){
                        folder.setImage(new Image(HomeGUI.shell.getDisplay(),
                                FilePropertyName.IMAGE_PATH + "folderError.png"));
                        project.setImage(
                                new Image(HomeGUI.shell.getDisplay(),
                                        FilePropertyName.IMAGE_PATH + "folderError.png"));
                    }
                }
    }
    
    public static void resolve(String projectName,TreeItem items[]){
        String wrkspace = StaticData.workspace;
        int pos = wrkspace.lastIndexOf(File.separator);
        if(pos >=0 && pos != wrkspace.length()-1)
            wrkspace += File.separator;///home/shiyam/
        File file = null;
        for(TreeItem item:items){
            String txt = item.getText();
            if(txt.equals(FilePropertyName.SOURCE_CODE) && !isSource){
                 file = new File(wrkspace + projectName + File.separator+
                         FilePropertyName.SOURCE_CODE);
                 
                 if(!file.exists()){
                    
                     if(file.isDirectory()){
                        file.mkdir();
                     }
                 }
            }else if(txt.equals(FilePropertyName.REQUIREMENT) && !isTxt){
                  file = new File(wrkspace + projectName + File.separator+
                         FilePropertyName.REQUIREMENT);
                 
                 if(!file.exists())
                     file.mkdir();
            }else if(txt.equals(FilePropertyName.PROPERTY) && !isProperty){
                //item.setImage(null);
                 file = new File(wrkspace + projectName + File.separator+
                         FilePropertyName.PROPERTY);
                 
                 if(!file.exists())
                     file.mkdir();
            }else if(txt.equals(FilePropertyName.XML) && !isXMl){
                //item.setImage(null);
                 file = new File(wrkspace + projectName + File.separator+
                         FilePropertyName.XML);
                 
                 if(!file.exists())
                     file.mkdir();
            }else if(txt.equals(FilePropertyName.XML) && !isXMl){
                //item.setImage(null);
                 file = new File(wrkspace + projectName + File.separator+
                         FilePropertyName.UML);
                 
                 if(!file.exists())
                     file.mkdir();
            }
        }
    }
}
