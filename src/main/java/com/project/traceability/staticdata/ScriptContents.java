package com.project.traceability.staticdata;
/***
 * 
 * @author shiyam
 * It contains python script contents to make python script file
 *
 */
public class ScriptContents {
	
	
	public static String sat_jar_path = "/home/shiyam/Desktop/Untitled.jar";

	
	
	
	public static String getContents(String creating_project_name){
		String script_root = "import subprocess";
		String sub_function_contens = "\"java -jar " + sat_jar_path +  " " + creating_project_name +"\"" + ","
					+"shell=True";

		String script_function_open = "p = subprocess.Popen(" + sub_function_contens;
		
		String script_function_close = ")";
		
		String result_script = "";
		String function = script_function_open + script_function_close;
		result_script += script_root;
		result_script += "\n";
		result_script += function;
		
		
		return result_script;
	}
	
	
}
