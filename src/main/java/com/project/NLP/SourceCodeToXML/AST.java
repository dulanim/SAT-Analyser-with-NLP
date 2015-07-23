/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.SourceCodeToXML;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import jdk.nashorn.internal.parser.Token;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.*;
import org.openide.util.Exceptions;

/**
 *
 * @author AARTHIKA
 */
public class AST {

    public static String file;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        while (true) {
            System.out.print("Enter :");
            BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
            String fileName = bufferRead.readLine();
            String splitName[] = fileName.split("\\\\");
            AST.file = splitName[splitName.length - 1];
            if (fileName.trim().isEmpty()) {
                System.exit(0);
            } else {
                new AST().convertFileToXML(fileName);
            }
        }
    }

    public void convertFileToXML(String fileName) {
        try {
            // TODO code application logic here
            String splitName[] = fileName.split("\\\\");
            AST.file = splitName[splitName.length - 1];
            System.out.println(fileName);
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

}
