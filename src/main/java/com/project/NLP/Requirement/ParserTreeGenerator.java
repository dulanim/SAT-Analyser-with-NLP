/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.project.NLP.Requirement;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


/**
 *
 * @author Vinojan
 */
public class ParserTreeGenerator {
    
    private List<CoreMap> sentences;
    private Annotation document; 
    private ArrayList<Tree> sentenceTree=new ArrayList<>();
    
    public ParserTreeGenerator(){
        
    }
        
    public ParserTreeGenerator(String text){
        
        /* creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution */
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        
        /* create an empty Annotation just with the given text  */
        document = new Annotation(text);

        /* run all Annotators on this text  */
        pipeline.annotate(document);
        
        /* Generate the TreeAnnotation for input document   */
        generateTreeAnnotation();
                
    }
    
    private void generateTreeAnnotation(){
        /* a CoreMap is essentially a Map that uses class objects as keys and has values with custom types      */
        sentences = document.get(SentencesAnnotation.class);
        for(CoreMap sentence: sentences) {
            /* Create the tree annotation for each sentnce in the document and store the trees in a ArrayList  */
            Tree tree = sentence.get(TreeAnnotation.class);
            sentenceTree.add(tree);
        }
        
    }
    
    /*Get the ArrayList which contains the generated Trees for the document */
    public ArrayList<Tree> getSentenceParseTree(){
        return sentenceTree;
    }

    
}
    
   
        
        
     