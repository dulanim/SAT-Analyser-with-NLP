/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.Requirement;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefChain.CorefMention;
import edu.stanford.nlp.util.IntPair;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import java.util.Set;

/**
 * Doing anaphora analysis using Coreference in StanfordCoreNLP
 * 1. Replace pronouns with the original noun.
 *
 * @author vinojan
 */
public class AnaphoraAnalyzer {
    
    private String document;
    private ArrayList<String> sentencesFromDoc;
    private ArrayList<String[]> wordsFromDoc; //=new ArrayList<>();
    private ParserTreeGenerator generator;
    private Map<Integer, CorefChain> graph;
    /*
    public AnaphoraAnalyzer(){
    
    }
    */
    public AnaphoraAnalyzer(String documnet){
        this.document=documnet;
        //System.out.println("document --- :\n"+documnet );
        getCoreferenChains();
        this.wordsFromDoc=splitSentence(sentencesFromDoc);
    }
    /*
     public AnaphoraAnalyzer(Map<Integer, CorefChain> graph, ArrayList<String> sentencesFromDoc ){
         this.graph=graph;
         this.sentencesFromDoc=sentencesFromDoc;
         this.wordsFromDoc=splitSentence(sentencesFromDoc);
     }
    */
    
    private void getCoreferenChains(){
        generator=new ParserTreeGenerator(document);
        graph=generator.generateCoreferenceChains();
        sentencesFromDoc=generator.getDocumentSentences();
    }
    
    
    public String doPronounResolving(){
        for(int i=1;i<=graph.size();i++){
            CorefChain cc=graph.get(i);
            if(cc!=null){
                //System.out.println("-----"+cc.toString());
                //System.out.println("---TextualOrder--"+cc.getMentionsInTextualOrder());
                Map<IntPair,Set<CorefChain.CorefMention>> mentionMap=cc.getMentionMap();
                //System.out.println("--MentionMap-----"+mentionMap);
                int mentionSize=mentionMap.size();

                Set intPairSet=mentionMap.keySet();

                  // System.out.println("-----"+cc.getMentionsWithSameHead(1,i));
                //System.out.println("---RepresentativeMention-----"+cc.getRepresentativeMention());
                String mentionSpan=cc.getRepresentativeMention().mentionSpan; 
                //System.out.println("----get the mentionspan---"+mentionSpan);
                String animacy=cc.getRepresentativeMention().animacy.toString();
                //System.out.println("----get the animacy---"+animacy);
                if(animacy.equalsIgnoreCase("ANIMATE") && mentionSize>1){
                    Iterator it=intPairSet.iterator();
                    while(it.hasNext()){
                        IntPair ip=(IntPair)it.next();
                        Set coref=mentionMap.get(ip);
                        Iterator itC=coref.iterator();
                        while(itC.hasNext()){
                            CorefChain.CorefMention cm=(CorefMention) itC.next();
                            String mentionPronoun=cm.mentionSpan;
                            //mentionPronoun.replace(mentionPronoun,mentionSpan)
                            //System.out.println("---Sentences ------- :"+sentencesFromDoc);
                            //System.out.println("---Words ------- :"+wordsFromDoc);
                            //for(String[] str:wordsFromDoc){
                            //     System.out.println("---Words from array ------- :"+str[0] + " "+str[1]);
                            //}
                            
                            //System.out.println("--- cm.mentionSpan---  "+mentionPronoun+ " int pair : "+ip);
                            int sentenceIndex=ip.getSource()-1;
                            int wordIndex=ip.getTarget()-1;
                                try{
                                String docWord=wordsFromDoc.get(sentenceIndex)[wordIndex];
                                //System.out.println("From arraylist : "+docWord);
                                if(mentionPronoun.equalsIgnoreCase(docWord)){
                                    wordsFromDoc.get(sentenceIndex)[wordIndex]=mentionSpan;
                                }
                                        }catch(ArrayIndexOutOfBoundsException e){
                                        //System.err.println("----- AnaphoraAnalyzer ------- : "+e.getMessage());
                            }
                        }
                    }
                }
            }
        
        }

        return getPronounResolvedDocument();
    }

    /* Get words of the sentence as an array */
    private ArrayList<String[]> splitSentence(ArrayList<String> sentenceList){
        ArrayList<String[]> wordList=new ArrayList<>();
        for(String sentence:sentenceList){
            String[] splitedString=sentence.split(" ");
            wordList.add(splitedString);
        }

        return wordList;
    }

    /*Rebuild the document with resolved pronouns */
    private String getPronounResolvedDocument(){
        StringBuilder sb=new StringBuilder();
        for(String[] sentence: wordsFromDoc){
            int wordCount=sentence.length;
            for(int i=0;i<wordCount;i++){
                sb.append(sentence[i]);
                /*
                if(i==(wordCount-1)){
                   sb.append("."); 
                }
                else{
                  sb.append(" ");  
                }
                */
                sb.append(" "); 

            }
           // sb.append(" ");

        }
        return sb.toString();
    }

    }


