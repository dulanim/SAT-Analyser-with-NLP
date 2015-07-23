/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author S. Shobiga
 */
public class stanfordCoreNLP {

    PrintWriter out, xmlOut;
    Tree tree = null;
    Properties props;
    StanfordCoreNLP pipeline;
    Annotation annotation;
    List<CoreMap> sentences;

    public stanfordCoreNLP() {

    }

    public stanfordCoreNLP(String text) {

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

    /* An Annotation is a Map and you can get and use the various analyses individually.
     For instance, this gets the parse tree of the first sentence in the text.
     */
    public void generateTreeAnnotation() {
        sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            if (sentence != null && sentence.size() > 0) {
                tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
                out.println();
                tree.pennPrint(out);
            }
        }
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
                for (int i = 1; i < graph.size(); i++) {
                    System.out.println(i + " " + graph.get(i));
                }
            }
        }

    }
    
    
}
