/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.Requirement;

import edu.stanford.nlp.trees.Tree;
import java.io.File;
import java.util.ArrayList;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Aarthika <>
 */
public class ParserTreeGeneratorTest {

    File javaFile = null;
    File imgFile = null;
    ParserTreeGenerator generator;
    ArrayList<Tree> tree;
    String text;

    @Test
    public void getJavFilesTest() {
        text = "Customer details must record the name and location.";     
        generator = new ParserTreeGenerator(text);
        tree = generator.getSentenceParseTree();
        assertNotNull(tree.size());
    }

    @Test
    public void getEmptyFilesTest() {        
        generator = new ParserTreeGenerator("");
        tree = generator.getSentenceParseTree();
        assertEquals(0, tree.size());
    }
    
}
