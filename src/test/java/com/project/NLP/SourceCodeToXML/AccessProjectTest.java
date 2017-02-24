/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.SourceCodeToXML;

import static com.project.NLP.SourceCodeToXML.AccessProject.getJavaFiles;
import static com.project.NLP.SourceCodeToXML.AccessProject.javaFilesExists;
import java.io.File;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Aarthika <>
 */
public class AccessProjectTest {

    File javaFile = null;
    File imgFile = null;

    @Before
    public void addFile(){
        javaFile = new File(System.getProperty("user.dir") + File.separator
            + "src" + File.separator + "main" + File.separator + "java");
         imgFile = new File(System.getProperty("user.dir") + File.separator
            + "img" + File.separator);
    }
    
    @Test
    public void getJavFilesTest() {
        //System.out.println("File: " + javaFile.toString());
        List<File> javaFiles = getJavaFiles(javaFile);
        assertNotNull(javaFiles.size());
        javaFiles.clear();
        javaFiles = getJavaFiles(imgFile);
        //System.out.println("File: " + imgFile.toString());
        assertEquals(0, javaFiles.size());
       
    }
    
    @Test
    public void javaFilesExistsTest() {
        //System.out.println("File: " + javaFile.toString());
        boolean javaFileExist = javaFilesExists(javaFile);
        assertEquals(true , javaFileExist);
        javaFileExist = javaFilesExists(imgFile);
        //System.out.println("File: " + imgFile.toString());
        assertEquals(false, javaFileExist);
    }
    

}
