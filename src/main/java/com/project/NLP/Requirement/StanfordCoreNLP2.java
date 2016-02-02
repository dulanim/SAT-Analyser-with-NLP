/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *..
 */
package com.project.NLP.Requirement;

import edu.stanford.nlp.dcoref.CorefCoreAnnotations;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.trees.tregex.TregexMatcher;
import edu.stanford.nlp.trees.tregex.TregexPattern;
import edu.stanford.nlp.util.CoreMap;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Queue;
import java.util.Stack;

/**
 *
 * @author S. Shobiga
 */
public class StanfordCoreNLP2 {

    PrintWriter out, xmlOut;
    Tree tree = null;
    Properties props;
    StanfordCoreNLP pipeline;
    Annotation annotation;
    List<CoreMap> sentences;
    ArrayList collectionOfTrees=new ArrayList(); // store the tree result
    static int countTree =0; // variable to count the collection of tree;

    public StanfordCoreNLP2() {

    }

    public StanfordCoreNLP2(String text) {

        /*properties*/
        props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");

        out = new PrintWriter(System.out);

        pipeline = new StanfordCoreNLP(props);

        /* create an empty annotation with the text*/
        annotation = new Annotation(text);
        //annotation = new Annotation("The system shall record customer details, such as name, dob, address, telephone number and account number");
        // annotation=new Annotation("系统将记录客户的信息，例如姓名，出生日期和帐号。");

        /*run all annotators on this text */
        System.out.println("\n\nRun all annotators on this text\n\n");
        pipeline.annotate(annotation);
        pipeline.prettyPrint(annotation, out);

    }

    /*set the new string */
/*    public void setNewText(String text){
        System.out.println("annotated text: "+text);
        Annotation annot = new Annotation(text);
        annotation =annot;
        pipeline.annotate(annotation);
        
    }
*/
    /* An Annotation is a Map and you can get and use the various analyses individually.
     For instance, this gets the parse tree of the first sentence in the text.
     */
    public ArrayList generateTreeAnnotation() {
        //System.out.println("1");
        sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            //System.out.println("2");
            if (sentence != null && sentence.size() > 0) {
                
                //System.out.println("3");
                tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
                //System.out.println("4");
                out.println();
                tree.pennPrint(out);
                collectionOfTrees.add(tree);
                //System.out.println("5");
            }
        }
//        System.out.println("array list result for size: "+collectionOfTrees.size());
//         for(int i =0;i<collectionOfTrees.size();i++){
//             System.out.println(i+" "+collectionOfTrees.get(i));
//        }
//        
        return collectionOfTrees;
    }

    public String generateTextAnnotation() {
        sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        String word = "";
        for (CoreMap sentence : sentences) {

            for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
                // this is the text of the token
                word = token.get(TextAnnotation.class);
                // System.out.println(word);
            }
        }
        return word;

    }

    public String generatePartOfSpeechAnnotation() {
        sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        String pos = "";
        for (CoreMap sentence : sentences) {

            for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
                // this is the POS tag of the token
                pos = token.get(PartOfSpeechAnnotation.class);
                //System.out.println(pos);
            }
        }
        return pos;

    }

    public String generateNamedEntityTagAnnotation() {
        sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        String ne = "";
        for (CoreMap sentence : sentences) {

            for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
                // this is the NER label of the token
                ne = token.get(NamedEntityTagAnnotation.class);
                //System.out.println(ne);
            }
        }
        return ne;

    }

    /*THIS IS THE COREFERENCE LINK GRAPH-PRONOUN*/
    public void generateCorefLink() {
        sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            if (sentence != null && sentence.size() > 0) {
                Map<Integer, edu.stanford.nlp.dcoref.CorefChain> graph = annotation.get(CorefCoreAnnotations.CorefChainAnnotation.class);
                
                System.out.println( graph.toString());
                String arr=graph.toString();
                
//                String arr2[] = arr.split("CHAIN");
//                
//                for(int i =0 ;i<arr2.length;i++){
//                     //System.out.println("array: "+arr2[i] );
//                     String arr3[] = arr2[i].split("-");
//                     for(int j=0;j<arr3.length;j++){
//                         if(!arr3[j].matches("-?\\d+(\\.\\d+)?")){
//                            //do nothing
//                             //System.out.println("inner array: "+arr3[j] );
//                         }
//                         else{
//                              System.out.println("inner array: "+arr3[j] );
//                         }
//                     }
//                }
//               
            }
        }
    }
    
  
    
    
    
    
}
