package com.project.traceability.ontology.models;

/****
 * @author shiyam
 * @created at 15 Dec 2015
 */
import java.util.ArrayList;
import java.util.List;

/*
 * THis is a mode class for storing a sample help information.
 * This is being retried for storing temporary help information in ArrayList
 * at runtime when read the sat_help.xml file
 * sat_help.xml file has many help contents it keeps separate node for each window
 * 
 * @author R.Srishiyamalan
 * created @Wed Dec 2015 23 11:07:20 PM
 * place Jaffna
 * mail id = "shiyam.11@cse.mrt.ac.lk"
 * 
 */
public class Helps {

	private String title = "";
	private String tag = "TAG";
	private List<String> descriptions;
	List<String> str;
	
	public Helps(){
		str = new ArrayList<>();
	}
	
	public void setHelpTitlte(String aTitle){
		
		if(aTitle.equals("")){
			title = "Help Title";
		}else{
			this.title = aTitle;
		}
	}
	
	public void setHelpTag(String aTag){
		if(aTag.equals("")){
			title = "Help Tags / Questions / Tips";
		}else{
			this.title = aTag;
		}
	}
	
	public void setDescriptions(List<String> desc){
			this.descriptions = desc;
	}
	
	public void setDescriptions(String aTip){
		
		str.add(aTip);
		setDescriptions(str);
	}
	
	public String getHelpTitle(){
		return this.title;
	}
	
	public String getHelpTag(){
		return this.tag;
	}
	public List<String> getHelpTips(){
		return this.descriptions;
	}
}
