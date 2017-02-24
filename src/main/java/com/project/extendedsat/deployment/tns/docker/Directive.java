/**
 * Namesh Sanjitha
 * Jul 14, 2016
 * DockerTraceability
 * com.tns.docker
 */
package com.project.extendedsat.deployment.tns.docker;




public class Directive {

	private int lineNum;
	private String command;
	private String argument;
	
	
	public Directive(){
		
	}	
	
	public Directive( String command, String argument ){
		this.command = command;
		this.argument = argument;
	}
	
	
	public int getLineNum() {
		return lineNum;
	}

	public void setLineNum(int lineNum) {
		this.lineNum = lineNum;
	}


	public String getCommand() {
		return command;
	}


	public void setCommand(String command) {
		this.command = command;
	}


	public String getArgument() {
		return argument;
	}


	public void setArgument(String argument) {
		this.argument = argument;
	}
	
	
	
}
