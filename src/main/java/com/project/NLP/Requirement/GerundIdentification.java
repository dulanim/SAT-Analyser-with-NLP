/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.Requirement;

import com.project.traceability.common.PropertyFile;
import com.project.traceability.semanticAnalysis.SynonymWords;
import static com.project.traceability.semanticAnalysis.SynonymWords.database;
import static com.project.traceability.semanticAnalysis.SynonymWords.wordForms;
import edu.smu.tspell.wordnet.NounSynset;
import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.SynsetType;
import edu.smu.tspell.wordnet.WordNetDatabase;
import java.util.HashSet;

/**
 *
 * @author S. Shobiga
 */
public class GerundIdentification {

    WordNetDatabase wordNetDatabase;
    String[] wordForms;

    GerundIdentification() {

    }

    public String[] identifyProperNoun(String term) {
        System.setProperty("wordnet.database.dir", PropertyFile.wordNetDbDirectory);
        wordForms = null;
        String wordForm = term;
        Synset[] synsets = database.getSynsets(wordForm);
        
       
        //  Display the word forms and definitions for synsets retrieved
        for (int i = 0; i < synsets.length; i++) {
            SynsetType type = synsets[i].getType();
            System.out.println("Word :" + term + " Type: " + synsets[i].getWordForms()[0]);

            if (type == SynsetType.NOUN) {
                System.out.println("Noun");

            }
            if (type == SynsetType.VERB) {
                System.out.println("VERB");

            }
            if (type == SynsetType.ADJECTIVE) {
                System.out.println("ADJECTIVE");

            }
            if (type == SynsetType.ADVERB) {
                System.out.println("ADVERB");

            }
            if (type == SynsetType.ADJECTIVE_SATELLITE) {
                System.out.println("ADJECTIVE_SATELLITE");

            }
        }

        if (synsets.length > 0) {
            for (int i = 0; i < synsets.length; i++) {
                wordForms = synsets[i].getWordForms();

            }
        } else {
            System.err.println("No synsets exist that contain "
                    + "the word form '" + wordForm + "'");
        }

        return wordForms;
    }

    public static void main(String args[]) {
        GerundIdentification w = new GerundIdentification();
        String[] d = w.identifyProperNoun("riding");

    }
}
