/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.SourceCodeToXML;

import static com.project.NLP.SourceCodeToXML.ExtractInterfaceListener.root;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.openide.util.Exceptions;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

/**
 *
 * @author AARTHIKA
 */
public class AST {

    public static String file;
    public static SourceCodeDB2 scdb;
    private static Element intraConnections;

    /**
     * Invokes when the user browses the source code project folder 
     * @param fileName 
     */
    public void startSourceCodeConversion(String fileName) {
        AccessProject project = new AccessProject();
        WriteToXML.createDocument();
        if (project.javaFilesExists(new File(fileName))) {
            List<File> files = project.getFiles();
            for (File file : files) {
                System.out.println("File Path - " + file.getAbsolutePath());
                new AST().convertFileToXML(file.getAbsolutePath());
            }
        }
        else{
            //invoke error message
        }
    }
    
    /**
     * 
     * @param fileName 
     */
    public void convertFileToXML(String fileName) {
        try {
            Java8Lexer lexer = new Java8Lexer(new ANTLRFileStream(fileName.trim()));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            Java8Parser parser = new Java8Parser(tokens);
            ParserRuleContext tree = parser.compilationUnit();

            ParseTreeWalker walker = new ParseTreeWalker(); // create standard walker
            ExtractInterfaceListener extractor = new ExtractInterfaceListener(parser);
            walker.walk(extractor, tree);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(AST.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    /**
     * Exits the conversion operation by creating the xml file.
     */
    public void exitConverter() {
        System.out.println("Exiting");
        intraConnections = WriteToXML.getDocument().createElement("IntraConnections");
        root.appendChild(intraConnections);

        ArrayList<Map> relationshipList = AST.scdb.getInheritanceRelationshipData();
        addRelationsToXML(relationshipList, "Inheritance");
        relationshipList.clear();
        relationshipList = AST.scdb.getAssociationRelationshipData();
        addRelationsToXML(relationshipList, "Composition");
        WriteToXML.createXML();
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
    
    /*
    public static void main(String[] args) throws IOException {
        AccessProject project = new AccessProject();
        WriteToXML.createDocument();
        scdb = new SourceCodeDB2();
        boolean enter = true;
        while (enter) {
            System.out.print("Enter project folder path: ");
            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
            String filePath = bufferRead.readLine();
            String splitName[] = filePath.split("\\\\");
            AST.file = splitName[splitName.length - 1];
            System.out.println(AST.file);
            if (filePath.trim().isEmpty()) {
                new AST().exitConverter();
                enter = false;
            } else {
                if (project.javaFilesExists(new File(filePath))) {
                    List<File> files = project.getFiles();
                    for (File file : files) {
                        System.out.println("File Path - " + file.getAbsolutePath());
                        new AST().convertFileToXML(file.getAbsolutePath());
                    }
                }
            }
        }
    }
    */
}
