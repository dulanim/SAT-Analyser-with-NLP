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
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
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
public class StanfordCoreNLPModified {

    private PrintWriter out;
    private PrintWriter xmlOut;
    private Tree tree = null;
    private Properties props;
    private StanfordCoreNLP pipeline;
    private Annotation annotation;
    private List<CoreMap> sentences;
    private ArrayList collectionOfTrees = new ArrayList(); // store the tree result
    private static int countTree = 0; // variable to count the collection of tree;
    private Object headFinder;

    public StanfordCoreNLPModified() {

    }

    public StanfordCoreNLPModified(String text) {

        /*properties*/
        props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        out = new PrintWriter(System.out);
        pipeline = new StanfordCoreNLP(props);

        /* create an empty annotation with the text*/
        annotation = new Annotation(text);

        /*run all annotators on this text */
        System.out.println("\n\nRun all annotators on this text\n\n");
        pipeline.annotate(annotation);
        pipeline.prettyPrint(annotation, out);

    }

    /* An Annotation is a Map and you can get and use the various analyses individually.
     For instance, this gets the parse tree of the first sentence in the text.
     */
    public Tree[] generateTreeAnnotation() {
        Tree[] children = null;
        //System.out.println("1");
        sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            Tree tree = sentence.get(TreeAnnotation.class);
            children = tree.children();
        }

        return children;
    }

    /* nameEntityAnnotation for track the Location and Person name 
     Return the word if the tokens contains Location, person, organization,misc, time, money, percent, date
     */
    public ArrayList generateNamedEntityTagAnnotation() {
        sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        ArrayList nameEntity = new ArrayList();
        String ne = "";
        for (CoreMap sentence : sentences) {

            for (CoreLabel token : sentence.get(TokensAnnotation.class)) {
                // this is the NER label of the token
                ne = token.get(NamedEntityTagAnnotation.class);
                if (ne.equals("LOCATION") || ne.equals("PERSON") || ne.equals("ORGANIZATION") || ne.equals("MISC") || ne.equals("TIME") || ne.equals("MONEY") || ne.equals("PERCENT") || ne.equals("DATE")) {
                    nameEntity.add(token.originalText());
                }

            }
        }
        return nameEntity;

    }

    /*THIS IS THE COREFERENCE LINK GRAPH-PRONOUN*/
    public void generateCorefLink() {
        sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            if (sentence != null && sentence.size() > 0) {
                Map<Integer, edu.stanford.nlp.dcoref.CorefChain> graph = annotation.get(CorefCoreAnnotations.CorefChainAnnotation.class);

                System.out.println(graph.toString());
                String arr = graph.toString();

            }
        }
    }

}
