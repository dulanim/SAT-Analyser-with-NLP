/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.Requirement;

import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;
import java.util.HashSet;

/**
 *
 * @author vinojan
 */
public class HypernymHyponym {
    private static final WordNetDatabase database=WordNetDatabase.getFileInstance();;
    private NounSynset nounSynset;
    private NounSynset[] hyponyms;
    private NounSynset[] hypernyms;
    private HashSet hypernymSet=new HashSet();
    private HashSet hyponymSet=new HashSet();
    
    public HypernymHyponym(){
       // database = WordNetDatabase.getFileInstance();
    }
    
    public HashSet getHypernymsForWord(String word){
        Synset[] synsets = database.getSynsets(word, SynsetType.NOUN);
        for(Synset synset: synsets){
            nounSynset = (NounSynset)synset;
            hypernyms = nounSynset.getHypernyms();

            for (NounSynset noun : hypernyms) {
                hypernymSet.add(noun.getWordForms()[0]);
            }
                       
        }
        return hypernymSet;
    }
    
    public HashSet getHyponymsForWord(String word){
        Synset[] synsets = database.getSynsets(word, SynsetType.NOUN);
        for(Synset synset: synsets){
            nounSynset = (NounSynset)synset;
            hyponyms = nounSynset.getHyponyms();

            for (NounSynset noun : hyponyms) {
                hyponymSet.add(noun.getWordForms()[0]);
            }
                       
        }
        return hyponymSet;
    }
    
    
}
