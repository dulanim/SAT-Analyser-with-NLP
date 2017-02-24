/**
 * Namesh Sanjitha
 * Jul 14, 2016
 * DockerTraceability
 * com.tns.docker
 */
package com.project.extendedsat.deployment.tns.docker;



import java.util.ArrayList;
import java.util.Iterator;



public class DockerFile {
	
	private int numberOfLines;
	private ArrayList<Directive> lines;
	
	
	public DockerFile(){
		lines = new ArrayList<Directive>();
		numberOfLines = 0;
	}
	
	public DockerFile( ArrayList<Directive> lines ){
		this.lines = lines;
		numberOfLines = lines.size();
	}

	public int getNumberOfLines(){
		return numberOfLines;
	}
	
	public void addLine( String cmd, String arg ){
		lines.add( new Directive( cmd, arg ) );
		numberOfLines++;
	}
	
	public Directive getDirective( int index ){
		return lines.get(index);
	}
	
	public void print(){
		Iterator<Directive> d = lines.iterator();
		Directive currentDirective;
		while( d.hasNext() ){
			currentDirective = d.next();
			System.out.println( currentDirective.getCommand() + " " + currentDirective.getArgument() );
		}
	}
	
}





