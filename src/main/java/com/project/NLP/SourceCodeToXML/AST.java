/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.SourceCodeToXML;

import static com.project.NLP.SourceCodeToXML.ExtractInterfaceListener.root;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

/**
 * Class for creating the abstract syntax tree
 * @author AARTHIKA
 */
public class AST {

    public static String file;
    public static SourceCodeDB2 scdb;
    private static Element intraConnections;
    private AST ast;

    /**
     * Invokes when the user browses the source code project folder
     *
     * @param filePath
     * @throws java.lang.Exception
     */
    public static void main(String args[]){
    	String filePath =  System.getProperty("user.home") + File.separator+ "Desktop/SatWrks/NewSatWrkspaceskal/src";
    	try{
    		new AST().startSourceCodeConversion(filePath);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void startSourceCodeConversion(String filePath) throws Exception {
        ast = new AST();
        scdb = new SourceCodeDB2();
        AccessProject project = new AccessProject();
        WriteToXML.createDocument();
        if (AccessProject.javaFilesExists(new File(filePath))) {
            List<File> files = project.getFiles();
            for (File projectFile : files) {
                //System.out.println("File Path - " + projectFile.getAbsolutePath());
                ast.sourceCodeTreeWalker(projectFile.getAbsolutePath());
                //System.out.println("Done for: " + projectFile.getAbsolutePath());
                ast.exitConverter();
            }
            scdb.shutdownDB();
        } else {
            JOptionPane.showMessageDialog(null, "Incorrect Path. The specified path does not contain any java files.", 
            		"Source-code Conversion", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * creates the tree for walking
     * @param fileName
     */
    public void sourceCodeTreeWalker(String fileName) {
        try {
            Java8Lexer lexer = new Java8Lexer(new ANTLRFileStream(fileName.trim()));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            Java8Parser parser = new Java8Parser(tokens);
            ParserRuleContext tree = parser.compilationUnit();

            ParseTreeWalker walker = new ParseTreeWalker(); // create standard walker
            ExtractInterfaceListener extractor = new ExtractInterfaceListener(parser);
            walker.walk(extractor, tree);
        } catch (ParserConfigurationException ex) {
            JOptionPane.showMessageDialog(null, 
            		"Parser problem while parsing the source code files.", "Source-code Conversion", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, 
            		"Problem occured in the given file.", "Source-code Conversion", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Exits the conversion operation by creating the xml file.
     */
    public void exitConverter() {
        //System.out.println("Exiting");
        intraConnections = WriteToXML.getDocument().createElement("IntraConnections");
        root.appendChild(intraConnections);
        ArrayList<Map> relationshipList = AST.scdb.getInheritanceRelationshipData();
        addRelationsToXML(relationshipList, "Inheritance");
        relationshipList.clear();
        relationshipList = AST.scdb.getAssociationRelationshipData();
        addRelationsToXML(relationshipList, "Composition");
        WriteToXML.createXML();
        //shutdownDB();
    }

    /**
     *
     * @param relationshipList
     * @param type
     */
    public void addRelationsToXML(ArrayList<Map> relationshipList, String type) {
        for (Map relation : relationshipList) {
            Element connections = WriteToXML.getDocument().createElement("Connections");
            intraConnections.appendChild(connections);

            Attr typeAttr = WriteToXML.getDocument().createAttribute("Type");
            typeAttr.setValue(type);
            connections.setAttributeNode(typeAttr);

            Attr startAttr = WriteToXML.getDocument().createAttribute("StartPoint");
            startAttr.setValue(relation.get("1").toString());
            connections.setAttributeNode(startAttr);

            Attr endAttr = WriteToXML.getDocument().createAttribute("EndPoint");
            endAttr.setValue(relation.get("2").toString());
            connections.setAttributeNode(endAttr);
        }
    }

    /*public static void main(String[] args) throws IOException {
        new AST().startSourceCodeConversion("D:\\myVirtusa\\src");
    }*/
}
