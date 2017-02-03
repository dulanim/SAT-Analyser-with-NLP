package com.project.extendedsat.config;

import javax.management.AttributeList;

import org.w3c.dom.Element;

/**
*
*
* @author SAMITHA
*/
public class Property {

	private String id;
	private String name;
	private String type;
	private String variableType;
	private String value;
	
	public String getName() {
        return name;
    }
	public void setName(String name) {
       this.name=name;
    }
	public String getId() {
        return id;
    }
	public void setId(String id) {
	       this.id=id;
	}
	public String getType() {
        return type;
    }
	public void setType(String type) {
       this.type=type;
    }
	public String getVariableType() {
        return variableType;
    }
	public void setVariableType(String variableType) {
       this.variableType=variableType;
    }
	public String getValue() {			
        return value;
    }
	public void setValue(String value) {
       this.value=value;
    }
	public static AttributeList getAttributes(Element eElement) {
        
		
		AttributeList l = new AttributeList();
            //Element element = (Element) node;
		l.add(eElement.getAttribute("name"));
        l.add(eElement.getAttribute("id"));
        l.add(eElement.getAttribute("type"));
        l.add(eElement.getAttribute("value"));
        l.add(eElement.getAttribute("variableType"));
		
        
 
        return l;
    }
}
