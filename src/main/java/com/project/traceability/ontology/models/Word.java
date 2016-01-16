
package com.project.traceability.ontology.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Word {

	
	private List<String> property;// person's name or person works in university
	private List<String> value;//what is example Person's name Person'age
	private List<String> parent;//point what is the root class name of specific word
	private String type;//it tells the specific word's actual type class/attribute/method
	public Word(){
		property = new ArrayList<String>();
		value = new ArrayList<String>();
	}
	public Word(List<String> property,List<String> value,String parent,String type){
		
		this.property = property;
		this.value = value;
		this.parent = new ArrayList<>();
		this.parent.add(parent);
		this.type = type;
		
	}
	public Word(String property,String value,String parent,String type){
		
		this.property = new ArrayList<String>();
		this.value = new ArrayList<String>();
		this.property.add(property);
		this.value.add(value);
		this.parent = new ArrayList<>();
		this.parent.add(parent);
		this.type = type;
		
	}
	public void setPropertyName(List<String> aProperty){
			
		if(aProperty.equals(null)){
			this.property = new ArrayList<>();
			this.property.add("hasDefault");
		}else{
			this.property = aProperty;
		}
	}
	
	public void removePropertyName(List<String> aProperty,String rmvName){
		
		if(!rmvName.equals("")){
			
			if(aProperty.size()>0)
				aProperty.remove(rmvName);
			this.property = aProperty;
		}
	}
	public void setPropertyName(String aProperty){
		if(!aProperty.equals("")){
			this.property = new ArrayList<>();
			this.property.add(aProperty);
		}else{
			this.property = new ArrayList<>();
			this.property.add("hasDefault");
		}
	}
	public void setValue(List<String> aValue){
		if(aValue.equals(null)){
			this.value = new ArrayList<>();
			this.value.add("DefaultValue");
		}else{
			this.value = aValue;
		}
	}
	public void setValue(String aValue){
		if(aValue.equals(null)){
			this.value = new ArrayList<>();
			this.value.add("DefaultValue");
		}else{
			this.value = new ArrayList<>();
			this.value.add(aValue);
		}
	}
	public void setWordType(String aType){
		if(aType.equals("")){
			this.type = "Word";
		}else{
			this.type = aType;
		}
	}
	public void setParentName(String aParent){
		if(aParent.equals("")){
			this.parent = new ArrayList<>();
			this.parent.add(StaticData.OWL_WORD_ROOT);
		}else{
			this.parent = new ArrayList<>();
			this.parent.add(aParent);
		}
	}
	public void setParentName(List<String> aParent){
		if(aParent.equals(null)){
			this.parent = new ArrayList<String>();
			String str = StaticData.OWL_WORD_ROOT;
			this.parent.add(str);
		}else{
			this.parent = aParent;
		}
	}
	
	public List<String> getPropertyName(){
		return this.property;
	}
	
	public List<String> getValue(){
		return this.value;
	}
	
	public String getParentName(){
		Set<String> set = new HashSet<>(this.parent);
		String str = "";
		if(set.size() == 1){
			str = set.iterator().next();
		}
		return str;
	}
	
	
	
	public String getType(){
		return this.type;
	}
}
