package com.project.traceability.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.project.traceability.manager.RequirementsManger;

/**1 Dec 2014
 * @author K.Kamalan
 *
 */
public class DefaultWords {
	
	public static List<String> defaultWord = new ArrayList<String>();
	
	public static void getDefaultWords(){
	//public static void main(String[] args){
		for(int i=0;i<RequirementsManger.requirementElements.size();i++){
			NounExtraction.getClassName(RequirementsManger.requirementElements.get(i).getContent());
			//NounExtraction.getClassName(RequirementsManger.requirementElements.get(i).getTitle());
			//get the frequency of words 
			Set<String> uniqueSet = new HashSet<String>(NounExtraction.nounWords);
			for (String temp : uniqueSet) {
//				System.out.println(temp + ": " + Collections.frequency(NounExtraction.nounWords, temp));
				if(Collections.frequency(NounExtraction.nounWords, temp)>3){
					defaultWord.add(temp);
				}
			}
		}
		
	}

}
