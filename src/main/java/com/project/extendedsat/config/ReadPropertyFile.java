package com.project.extendedsat.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
/**
*
*
* @author SAMITHA
*/
public class ReadPropertyFile {
	
	String str;
	
	public ReadPropertyFile(String str){
		this.str=str;
		
	}

public  HashMap<String, String>  load() {
		
		Enumeration<Object> enuKeys = null;
		HashMap<String, String> hmap = new HashMap<String, String>();
			
		try {
			File file = new File(str);
			FileInputStream fileInput = new FileInputStream(file);
			Properties properties = new Properties();
			properties.load(fileInput);
			fileInput.close();

			enuKeys = properties.keys();
			
			//read the property file and write to a buffer
			while (enuKeys.hasMoreElements()) {
				String key = (String) enuKeys.nextElement();
				String value = properties.getProperty(key);
				//System.out.println(key + ": " + value);
				hmap.put(key, value);
				
			}
			

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return hmap;
	}
}
