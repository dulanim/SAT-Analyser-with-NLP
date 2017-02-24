/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.SourceCodeToXML;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *Gets the java project folder path
 * @author AARTHIKA
 */
public class AccessProject {
    
    private static List<File> javaFiles = new ArrayList();

    /**
     * Gets the relevant java files
     * @param file
     * @return 
     */
    public static List<File> getJavaFiles(File file) {
        File files[] = null;
        
        if (file.isFile()) {
            //System.out.println(file.getAbsolutePath());
        } else {
            files = file.listFiles();

            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().endsWith(".java") || files[i].getName().endsWith(".JAVA")) {
                    javaFiles.add(files[i]);
                }
                if (files[i].isDirectory()) {
                    getJavaFiles(files[i].getAbsoluteFile());
                }
            }
        }
        return javaFiles;
    }
    
    /**
     * Checks whether java files exist
     * @param file
     * @return boolean
     */
    public static boolean javaFilesExists(File file){
        javaFiles.clear();
        getJavaFiles(file);
        return !javaFiles.isEmpty();
    }
    
    /**
     * Returns the files
     * @return List
     */
    public List getFiles(){
        return javaFiles;
    }

    public static void main(String args[]) {
        File mainFolder = new File("D:\\TestJava");
        List<File> files = getJavaFiles(mainFolder);

        for (File file : files) {
            //System.out.println(file.getAbsolutePath());
        }
    }
}
