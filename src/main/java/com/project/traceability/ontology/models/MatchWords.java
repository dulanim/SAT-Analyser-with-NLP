/****
 * This class contains static methods to compare string similarity
 * compareString method compare string using JaroWinkler algorithm
 * if not use Levenshtein distance to match words
 */
/****
 * @author shiyam
 * @created at 15 Dec 2015
 */
package com.project.traceability.ontology.models;

import uk.ac.shef.wit.simmetrics.similaritymetrics.JaroWinkler;
import info.debatty.java.stringsimilarity.*;
/*
 * @author shiyam
 * @uom for FYP
 */
public class MatchWords {

	static JaroWinkler algorithm;
	static{
		 algorithm = new JaroWinkler();
	}
	public  static boolean  compareStrings(String stringA, String stringB) {
	    JaroWinkler algorithm = new JaroWinkler();
	    double matchedDistance =  algorithm.getSimilarity(stringA, stringB);
	    if(matchedDistance>=0.8 && matchedDistance<=1){
	    	return true;
	    }else{
	    	//if not use another algorithm to match distance algorithm
	    	return compareStringLevenshetein(stringA, stringB);
	    }
	}
	
	public static boolean compareStringLevenshetein (String string1,String string2) {
	    Levenshtein l = new Levenshtein();
	    double editDsitance = l.distance(string1, string2);
	    
	    double matchedDistance = 1 - editDsitance;
	    
	    if(matchedDistance>=0.8 && matchedDistance<=1){
	    	return true;
	    }else{
	    	return false;
	    }
	  }
	
	public static void main(String args[]){
		//System.out.println(MatchWords.compareStrings("1", "112"));
	}
}
