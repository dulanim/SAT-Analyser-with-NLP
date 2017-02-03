package com.project.extendedsat.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.management.AttributeList;

/**
*
*
* @author SAMITHA
*/
public class CreateTraceability {

	public static Map<String,ArrayList<AttributeList>> compareList(ArrayList<AttributeList> list1, ArrayList<AttributeList> list2){
		int count=0;
		
		ArrayList<AttributeList> l1= new ArrayList<AttributeList>();
		ArrayList<AttributeList> l2= new ArrayList<AttributeList>();	
		//System.out.println(list1.get(0).size());
		for(int i=0;i<list1.size();i++){
			for(int j=0;j<list2.get(0).size();j++){
				//System.out.println(list1.get(i));
				
				/*if(list1.get(i).equals(list2.get(j))){
					//System.out.println(list1.get(i));
					
				}*/
				if(list1.get(i).get(j).equals(list2.get(i).get(j))){
					//System.out.println(list1.get(i).get(j));
					count++;
				}
		}
			if(count>=4){
				System.out.println();
			/*	System.out.println();
				System.out.println(list1.get(i));
				System.out.println(list2.get(i));
				System.out.println();*/
				l1.add(list1.get(i));
				l2.add(list2.get(i));
			/*	System.out.println(l1.get(i));
				System.out.println(l2.get(i));*/
			}
			
			count=0;
	}
		
		Map<String,ArrayList<AttributeList>> map =new HashMap();
		  map.put("list1",l1);
		  map.put("list2",l2);
		  
		  return map;
 }
}