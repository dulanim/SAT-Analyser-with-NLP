/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.project.NLP.Requirement;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 *
 * @author Vinojan
 */
public class ParserTreeGenerator {
    
    private String requirementDocument;
    private List<CoreMap> sentences;
    private Annotation document; 
    private ArrayList<Tree> sentenceTree=new ArrayList<>();
    private ArrayList<String> documentSentences=new ArrayList<>();
    private Map<Integer, CorefChain> coreferenceChain;
    
    public ParserTreeGenerator(){
        
    }       
    public ParserTreeGenerator(String text){
        this.requirementDocument=text;
        
        /* creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution */
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        
        /* create an empty Annotation just with the given text  */
        document = new Annotation(requirementDocument);

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
            /*Get the sentences in the document  and store each individual sentence as a element in the ArrayList  */
            documentSentences.add(sentence.toString());
        }
        
    }
    
    /*Get the ArrayList which contains the generated Trees for the document */
    public ArrayList<Tree> getSentenceParseTree(){
        return sentenceTree;
    }

    /* nameEntityAnnotation for track the Location and Person name 
     Return the word if the tokens contains Location, person, organization,misc, time, money, percent, date
    */
    public ArrayList generateNamedEntityTagAnnotation() {
        sentences = document.get(SentencesAnnotation.class);
        ArrayList nameEntity = new ArrayList();
        String annotations = "";
        for (CoreMap sentence : sentences) {

            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                // this is the NER label of the token
                annotations = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                if (annotations.equals("LOCATION") || annotations.equals("PERSON") || annotations.equals("ORGANIZATION") || annotations.equals("MISC") || annotations.equals("TIME") || annotations.equals("MONEY") || annotations.equals("PERCENT") || annotations.equals("DATE")) {
                    nameEntity.add(token.originalText());
                }

            }
        }
        return nameEntity;

    }
    
    /* This is the coreference link graph
    *Each chain stores a set of mentions that link to each other,
    *along with a method for getting the most representative mention
    * Both sentence and token offsets start at 1!
    */
    public Map<Integer, CorefChain> generateCoreferenceChains(){
      coreferenceChain= document.get(CorefCoreAnnotations.CorefChainAnnotation.class);
      return coreferenceChain;
    }
    
    /* Return the sentences in the requirement document as ArrayList */
    public ArrayList<String> getDocumentSentences(){
        return documentSentences;
    }
    
}
    
   
        
        
     