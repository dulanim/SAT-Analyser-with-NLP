package com.project.traceability.semanticAnalysis;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import com.project.traceability.model.WordsMap;
import java.util.List;

import com.project.traceability.common.PropertyFile;
import com.project.traceability.ir.LevenshteinDistance;
import com.project.traceability.utils.DefaultWords;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;

/**
 *
 * @author K.Kamalan
 */
public class SynonymWords {

    public static String[] wordForms;
    public static String simpleWord1, simpleWord2;
    public static WordNetDatabase database = WordNetDatabase.getFileInstance();
    public static int classMapId, mapId;

    //get the similar words for spesific words
    public static String[] getSynSetWords(String term) {
        System.setProperty("wordnet.database.dir", PropertyFile.wordNetDbDirectory);
        wordForms = null;
        String wordForm = term;
        Synset[] synsets = database.getSynsets(wordForm);
        //  Display the word forms and definitions for synsets retrieved
        if (synsets.length > 0) {
            for (int i = 0; i < synsets.length; i++) {
                wordForms = synsets[i].getWordForms();

            }
        } else {
//				System.err.println("No synsets exist that contain " +
//						"the word form '" + wordForm + "'");
        }

        return wordForms;
    }

    
    //Check similarity for identitying classes relationship
    public static WordsMap checkSymilarity(String term1, String term2, String type) {
        String similarWords1[] = getSynSetWords(term1);
        String similarWords2[] = getSynSetWords(term2);
        boolean status = false;
        if (term1.equalsIgnoreCase(term2)
                || LevenshteinDistance.similarity(term1, term2) > .85) {
            WordsMap w1 = new WordsMap();
            w1.setIsMatched(true);
            w1.setMapID(1);
            return w1;
        } else {
            if(similarWords1!=null & similarWords2!=null){
                for (int i = 0; i < similarWords1.length; i++) {
                for (int j = 0; j < similarWords2.length; j++) {
                    if (similarWords1[i].equalsIgnoreCase(similarWords2[j])) {
                        status = true;
                        break;
                    }

                }
                if (status) {
                    break;
                }
            }
            }
            

            if (status) {
                WordsMap w1 = new WordsMap();
                w1.setIsMatched(true);
                w1.setMapID(1);
                return w1;
            } else {
                WordsMap w2 = new WordsMap();
                w2.setIsMatched(false);
                w2.setMapID(100);
                return w2;
            }

        }
    }

    //overload the method & check similarity for attributes & behaviour
    public static WordsMap checkSymilarity(String term1, String term2, String sourceType, String tagetType, List<String> classNames) {
        WordsMap w2 = new WordsMap();
        //check only 1st letter changed remaining unchanged 
        if (isFirstletterChanged(term1, term2)) {
            w2.setIsMatched(false);
            w2.setMapID(100);
            return w2;
        } //check similarity get the edit distance & if >.85 then it will be ok
        else if (term1.equalsIgnoreCase(term2) ||term1.trim().equalsIgnoreCase(term2.trim())
                || LevenshteinDistance.similarity(term1, term2) > .85) {
            System.out.println(term1.equalsIgnoreCase(term2) + " : " + LevenshteinDistance.similarity(term1, term2) + " : " + term1 + "**************" + term2 + "TRUE");
            w2.setIsMatched(true);
            w2.setMapID(1);
            return w2;
        } else if (HasSimilarWords(term1, term2, sourceType, tagetType, classNames)) {
            w2.setIsMatched(true);
            w2.setMapID(2);
            return w2;
        } else {
            w2.setIsMatched(false);
            w2.setMapID(100);
            return w2;
        }

//		return checkSymilarity(removeClassName1,removeClassName2,type);
    }

    //check whether 1st letter changed (for avoid getter setter method mismatched)
    public static boolean isFirstletterChanged(String term1, String term2) {
        if (term1.substring(1).equalsIgnoreCase(term2.substring(1)) && term1.charAt(0) != term2.charAt(0)) {
//			System.out.println(term1+" : "+term2);
            return true;
        } else {
            return false;
        }

    }
    

    //check whether 2 terms has similar words
    //No chenge to Number
    //remove getter setter method substring
    //after that 1st letter changed to small letter
    //divide into substring according to the letter which is in capital letter
    //remove class names & check whether they have partial word matching
    //send the words to wordnet to get the similar words 
    public static boolean HasSimilarWords(String term1, String term2, String sourceType, String targetType, List<String> classNames) {

        boolean status = false;
        String[] partialWords1;
        String[] partialWords2;
        String getType1 = "", getType2 = "";

        if (sourceType.equalsIgnoreCase(targetType)) {
            //if the term contains sub string "NO" at last then change the sub string to "NUMBER"
            if (term1.substring(term1.length() - 2).equalsIgnoreCase("No")) {
                simpleWord1 = term1.replace(term1.substring(term1.length() - 2), "Number");
            } else {
                simpleWord1 = term1;
            }
            if (term2.substring(term2.length() - 2).equalsIgnoreCase("No")) {
                simpleWord2 = term2.replace(term2.substring(term2.length() - 2), "Number");
            } else {
                simpleWord2 = term2;
            }

//		String removeClassName1 = null,removeClassName2= null;
            //remove get or set word from term to check similarity from term1
            if (term1.contains("get") || term1.contains("set")) {
                simpleWord1 = simpleWord1.substring(3);
                if (term1.contains("get")) {
                    getType1 = "get";
                } else if (term1.contains("set")) {
                    getType1 = "set";
                } else {
                    getType1 = "";
                }
//			System.out.println("************************"+simpleWord1);						
            }

            //remove get or set word from term to check similarity from term2
            if (term2.contains("get") || term2.contains("set")) {
                simpleWord2 = simpleWord2.substring(3);
                if (term2.contains("get")) {
                    getType2 = "get";
                } else if (term2.contains("set")) {
                    getType2 = "set";
                } else {
                    getType2 = "";
                }
//			System.out.println("************************"+simpleWord2);			
            }

            simpleWord1 = simpleWord1.replace(simpleWord1.charAt(0), Character.toLowerCase(simpleWord1.charAt(0)));
            simpleWord2 = simpleWord2.replace(simpleWord2.charAt(0), Character.toLowerCase(simpleWord2.charAt(0)));
//		System.out.println(simpleWord1+"###########"+simpleWord2);

//		Pattern pat = Pattern.compile("[A-Z][^A-Z]*$");
//		Matcher match = pat.matcher(simpleWord1);
//
//		int lastCapitalIndex = -1;
//		if(match.find())
//		{
//		    lastCapitalIndex = match.start();
////		    System.out.println(simpleWord1+":"+lastCapitalIndex+"@@@@@@@@@@@@@@@@@@@@");
//		}
            simpleWord1 = simpleWord1.replaceAll("(?!^)([A-Z])", " $1");
            simpleWord2 = simpleWord2.replaceAll("(?!^)([A-Z])", " $1");

//                System.out.println(simpleWord1+"KAMALAN"+simpleWord2+"KAMAL"+className);
//                
//                if(simpleWord1.toLowerCase().contains(className.toLowerCase())){
//			simpleWord1 = simpleWord1.toLowerCase().replaceAll(className.toLowerCase(), "");
//		}
//		if(simpleWord2.toLowerCase().contains(className.toLowerCase())){
//			simpleWord2 = simpleWord2.toLowerCase().replaceAll(className.toLowerCase(), "");
//		}
//                
//                System.out.println(simpleWord1+"KAMALABALAN"+simpleWord2+"KAMAL"+className);
            partialWords1 = simpleWord1.split(" ");
            partialWords2 = simpleWord2.split(" ");

            // check for partial word except classes 
            if (sourceType.equalsIgnoreCase(targetType)) {
                if (partialWords1 != null && partialWords2 != null) {
                    for (int i = 0; i < partialWords1.length; i++) {
                        for (int j = 0; j < partialWords2.length; j++) {
                            if (partialWords1[i].equalsIgnoreCase(partialWords2[j]) && !classNames.contains(partialWords1[i].toLowerCase())
                                    && !DefaultWords.getDefaultWords().contains(partialWords1[i])) {
                                if (getType1.equalsIgnoreCase(getType2)) {
                                    status = true;
                                    break;
                                }
                            }
                        }
                        if (status) {
                            break;
                        }
                    }

                }

            }

            //get similar words from WordNet dictionary
            String[] similarWordForTerm1 = getSynSetWords(simpleWord1);
            String[] similarWordForTerm2 = getSynSetWords(simpleWord2);

            if (!status) {
                System.out.println(simpleWord1 + " " + simpleWord2);
                //compare words which get from word net dictionary to get the relationship files
                if (similarWordForTerm1 != null && similarWordForTerm2 != null) {
                    for (int i = 0; i < similarWordForTerm1.length; i++) {
                        for (int j = 0; j < similarWordForTerm2.length; j++) {
                            if (similarWordForTerm1[i].equalsIgnoreCase(similarWordForTerm2[j])
                                    && !classNames.contains(similarWordForTerm1[i].toLowerCase())
                                    && !DefaultWords.getDefaultWords().contains(similarWordForTerm1[i])) {
//                            System.out.println(similarWordForTerm1[i]+":"+similarWordForTerm2[j]);
                                status = true;
                                break;
                            }
                        }
                        if (status) {
                            break;
                        }
                    }

                } else if (similarWordForTerm1 == null && similarWordForTerm2 != null) {
                    for (int i = 0; i < similarWordForTerm2.length; i++) {
                        if (simpleWord1.equalsIgnoreCase(similarWordForTerm2[i]) && !classNames.contains(simpleWord1.toLowerCase())
                                && !DefaultWords.getDefaultWords().contains(simpleWord1)) {
                            status = true;
                            break;
                        }
                    }

                } else if (similarWordForTerm2 == null && similarWordForTerm1 != null) {
                    for (int i = 0; i < similarWordForTerm1.length; i++) {
                        if (simpleWord2.equalsIgnoreCase(similarWordForTerm1[i]) && !classNames.contains(simpleWord2.toLowerCase())
                                && !DefaultWords.getDefaultWords().contains(simpleWord2.toLowerCase())) {
                            status = true;
                            break;
                        }
                    }
                } else {
                    status = false;
                }

                if (status) {
                    System.out.println(term1 + " " + term2 + " " + classNames);
                }

            }
        } else {
            status = false;
        }

//        if (status) {
//            System.out.println(term1 + "!!!!!!!!!!!!!!!!!!!!!" + term2 + "!!!!!!!!!!!!!!!!!!!!");
//        }
//        else
//            System.out.println(term1+"#####################"+term2+"####################"+className+":"+status);
        return status;

    }
}
