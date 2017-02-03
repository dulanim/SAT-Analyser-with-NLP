/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import edu.stanford.nlp.trees.Tree;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author Aarthika <>
 */
public class ClassIdentificationTest {

    ClassIdentification cIdentify;
    ParserTreeGenerator generator;
    ArrayList<Tree> tree;
    String text;

    @Before
    public void initiate() {
        text = "Customer details must record the name and location.";
        generator = new ParserTreeGenerator(text);
        tree = generator.getSentenceParseTree();
        cIdentify = new ClassIdentification(tree.get(0));
    }

    @Test
    public void classIdentifyTest() {
        HashSet classList = cIdentify.getClasses();
        String className = classList.iterator().next().toString();
        assertEquals(1, classList.size());
        assertEquals("customer", className);        
    }
    
    @Test
    public void classWithAttrTest() {        
        HashMap classWithAttr = cIdentify.getClassWithAttr();
        System.out.println(""+classWithAttr.get("customer"));
        assertEquals(0, classWithAttr.size());
    }
    
    @Test
    public void attributesFromClassTest() {
        ArrayList attributesFromClass = cIdentify.getAttributeFromClass();
        assertEquals(0, attributesFromClass.size());
    }
}
