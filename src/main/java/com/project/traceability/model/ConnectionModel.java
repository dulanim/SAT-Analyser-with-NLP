/**
 * 
 */
package com.project.traceability.model;

/**
 * @author Gitanjali
 * Nov 24, 2014
 */
public class ConnectionModel {
	
	private String type = null;
	private String startPoint = null;
	private String startMuliplicity = null;
	private String endPoint = null;
	private String endMultiplicity = null;
	private String annotation = null;
	
	
	/**
	 * 
	 */
	public ConnectionModel() {
		super();
	}


	/**
	 * @param type
	 * @param startPoint
	 * @param startMuliplicity
	 * @param endPoint
	 * @param endMultiplicity
	 * @param annotation
	 */
	public ConnectionModel(String type, String startPoint,
			String startMuliplicity, String endPoint, String endMultiplicity,
			String annotation) {
		super();
		this.type = type;
		this.startPoint = startPoint;
		this.startMuliplicity = startMuliplicity;
		this.endPoint = endPoint;
		this.endMultiplicity = endMultiplicity;
		this.annotation = annotation;
	}


	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}


	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}


	/**
	 * @return the startPoint
	 */
	public String getStartPoint() {
		return startPoint;
	}


	/**
	 * @param startPoint the startPoint to set
	 */
	public void setStartPoint(String startPoint) {
		this.startPoint = startPoint;
	}


	/**
	 * @return the startMuliplicity
	 */
	public String getStartMuliplicity() {
		return startMuliplicity;
	}


	/**
	 * @param startMuliplicity the startMuliplicity to set
	 */
	public void setStartMuliplicity(String startMuliplicity) {
		this.startMuliplicity = startMuliplicity;
	}


	/**
	 * @return the endPoint
	 */
	public String getEndPoint() {
		return endPoint;
	}


	/**
	 * @param endPoint the endPoint to set
	 */
	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}


	/**
	 * @return the endMultiplicity
	 */
	public String getEndMultiplicity() {
		return endMultiplicity;
	}


	/**
	 * @param endMultiplicity the endMultiplicity to set
	 */
	public void setEndMultiplicity(String endMultiplicity) {
		this.endMultiplicity = endMultiplicity;
	}


	/**
	 * @return the annotation
	 */
	public String getAnnotation() {
		return annotation;
	}


	/**
	 * @param annotation the annotation to set
	 */
	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}
	
	
	
}
