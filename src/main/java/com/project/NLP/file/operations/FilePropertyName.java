package com.project.NLP.file.operations;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.FileDialog;
import org.w3c.dom.Document;

import com.project.traceability.GUI.CompareWindow;

/**
 *
 * @author shiyam which has contains default project path uml diagram default
 * path requirement document default path java project root default path
 */
public class FilePropertyName {

    public static String user_home = System.getProperty("user.home") + File.separator + "SATAnalyzer";
    public static String RESOURCE_PATH = user_home + File.separator + "Resources" + File.separator + "res";
    public static final String default_project_path = RESOURCE_PATH + File.separator + "Anduril";
    public static final String default_requirement_doc_path = RESOURCE_PATH + File.separator + "requirement.txt";
    public static final String default_uml_file_path = RESOURCE_PATH + File.separator + "umlFile.json";
    public static final String default_java_project_path = RESOURCE_PATH + File.separator + "JavaSourceCode";
    public static final String XML = "xml";
    public static final String REQUIREMENT = "txt";
    public static final String UML = "uml";
    public static final String SOURCE_CODE = "src";
    public static final String PROPERTY = "property";
    public static final String TEMP = "temp";
    public static final String RELATION_ARTIFACT_NAME = "Relations.xml";
    public static final String UML_ARTIFACT_NAME = "UMLArtefactFile.xml";
    public static final String REQUIREMENT_ARTIFACT_NAME = "RequirementArtefactFile.xml";
    public static String SOURCE_ARTIFACT_NAME = "SourceCodeArtefactFile.xml";
    public static final String IMAGE_PATH = user_home + File.separator + "Resources" + File.separator + "images" + File.separator;
    //new artifact name added for extension
    public static final String SOURCE_ARETEFACT_NAME_OLD = "old_version.xml";
    public static final String SOURCE_ARETEFACT_NAME_NEW = "new_version.xml";
    public static final String SOURCE_ARETEFACT_NAME_MODIFIED = "modified_version.xml";
    //finish name initialization part
    public static final Image exactimg = new Image(CompareWindow.display, IMAGE_PATH + "exact.jpg");
    public static final Image violoationimg = new Image(CompareWindow.display, IMAGE_PATH + "violation.jpg");
    public static final String exactImageData = exactimg.toString();
    public static final String violationImageData = violoationimg.toString();
    
    //For an extension project 
    //localDirectory of JENKINS 
    
    public static final String LOCAL_DIR_JENKINS = "/home/shiyam/Desktop/SatWrks/";

    //    public static Path getPath(Path target,String type){
//        if(type.contains(XML)){
//            target = Paths.get(HomeGUI.projectPath + File.separator + XML);
//        }else if(type.contains(REQUIREMENT) || type.contains("txt") || type.contains("pdf")||
//                type.contains("docs")||type.contains("doc")){
//            target = Paths.get(HomeGUI.projectPath + File.separator + REQUIREMENT);
//        }else if(type.contains("java")){
//             target = Paths.get(HomeGUI.projectPath + File.separator + SOURCE_CODE);
//        }else if(type.contains("mdj")||type.contains("json")||type.contains("xmi")||type.contains("uml")){
//             target = Paths.get(HomeGUI.projectPath + File.separator + UML);
//        }else{
//            target = Paths.get(HomeGUI.projectPath + File.separator + PROPERTY);
//        }
//        return target;
//    }
    
    private static String getXMLFileRootPath(String projectPath){
		String fileRoot = projectPath + File.separator + 
				FilePropertyName.XML + File.separator;
		
		return fileRoot;
	}
	
	public static String getXMLFilePath(String projectPath,String TAG){
		String path = "";
		path += getXMLFileRootPath(projectPath);
		String fileName = "";
		if(TAG.equals("OLD")){
			fileName = FilePropertyName.SOURCE_ARETEFACT_NAME_OLD;
		}else if(TAG.equals("NEW")){
			fileName = FilePropertyName.SOURCE_ARETEFACT_NAME_NEW;
		}else if(TAG.equals("MODIFIED")){
			fileName = FilePropertyName.SOURCE_ARETEFACT_NAME_MODIFIED;
		}else{
			fileName = FilePropertyName.SOURCE_ARTIFACT_NAME;
		}
		
		path += fileName;
		return path;
	}
    public static void addSubFolderIntoProject(File folder) {

        if (!folder.exists()) {
            folder.mkdirs();
        }
        File add_file = new File(folder, FilePropertyName.XML);
        add_file.mkdirs();
        add_file = new File(folder, FilePropertyName.UML);
        add_file.mkdirs();
        add_file = new File(folder, FilePropertyName.REQUIREMENT);
        add_file.mkdirs();
        add_file = new File(folder, FilePropertyName.SOURCE_CODE);
        add_file.mkdirs();
        add_file = new File(folder, FilePropertyName.PROPERTY);
        add_file.mkdirs();
    }

    /*public static InputStream getImage(String imageFile) {        
        //getClass().getClassLoader().getResourceAsStream(imageFile);
        return StartUpProject.class.getClassLoader().getResourceAsStream(imageFile);
    }*/
    public static void copy(FileDialog fileDialog, String savePath) {

        String selectedFiles[];
        Path path;
        if (savePath != null) {
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
            throws IOException {

        if (file.isDirectory()) {

            //directory is empty, then delete it
            if (file.list().length == 0) {
                file.delete();
                System.out.println("Directory is deleted : "
                        + file.getAbsolutePath());
            } else {
                //list all the directory contents
                String files[] = file.list();
                for (String temp : files) {
                    //construct the file structure
                    File fileDelete = new File(file, temp);
                    //recursive delete
                    delete(fileDelete);
                }
                //check the directory again, if empty then delete it
                if (file.list().length == 0) {
                    file.delete();
                    System.out.println("Directory is deleted : "
                            + file.getAbsolutePath());
                }
            }

        } else {//if file, then delete it
            file.delete();
            System.out.println("File is deleted : " + file.getAbsolutePath());
        }
    }

    public static void copyFolder(File src, File dest) throws IOException {
        if (src.isDirectory()) {
            //if directory not exists, create it
            if (!dest.exists()) {
                dest.mkdir();
                System.out.println("Directory copied from "
                        + src + "  to " + dest);
            }

            //list all the directory contents
            String files[] = src.list();

            for (String file : files) {
                //construct the src and dest file structure
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                //recursive copy
                copyFolder(srcFile, destFile);
            }

        } else {
            //if file, then copy it
            //Use bytes stream to support all file types\
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dest);

            byte[] buffer = new byte[1024];

            int length;
            //copy the file content in bytes 
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }

            in.close();
            out.close();
        }
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {

        String srcFilePath = sourceFile.getAbsolutePath();
        int startIndex = 0;
        if (srcFilePath.lastIndexOf("/") > 0) {
            startIndex = srcFilePath.lastIndexOf("/");
        } else if (srcFilePath.lastIndexOf("\\") > 0) {
            startIndex = srcFilePath.lastIndexOf("\\");
        }

        System.out.println(startIndex);
        int endIndex = srcFilePath.length();
        String fileName = srcFilePath.substring(startIndex + 1, endIndex);

        destFile = new File(destFile.getAbsolutePath(), fileName);
        copyFolder(sourceFile, destFile);
    }
    public static void copyTwoFile(File sourceFile, File destFile) throws IOException {
        copyFolder(sourceFile, destFile);
    }
    
//    public static void renameAFile(File file,String newName){
//    	
////    	
////
////        File destFile = new File(destFile.getAbsolutePath(), fileName);
////    	file.renameTo(dest)
//    }
    public static void writeToXML(String filePath,Document doc){
	    	try{// write the content into xml file
	    		TransformerFactory transformerFactory = TransformerFactory.newInstance();
	    		Transformer transformer = transformerFactory.newTransformer();
	    	    transformer.setOutputProperty(OutputKeys.INDENT, "yes");//No I18N
	    	    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");//No I18N
	            DOMSource source = new DOMSource(doc);
	    	    //String driveLetter = getSuitableDrive();
	    	                
	    	    File f = new File(filePath);
	    	    StreamResult result = new StreamResult(f.getPath());
	    	    transformer.transform(source, result);
    		}catch (TransformerException tfe) {
    			tfe.printStackTrace();
    		}catch(Exception e){
    			 
    		}
    }

	
}
