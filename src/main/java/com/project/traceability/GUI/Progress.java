package com.project.traceability.GUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;

import com.project.traceability.staticdata.StaticData;

public class Progress {

	protected Shell shell;
	Display display;
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Progress window = new Progress();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(432, 158);
		shell.setText("SWT Application");
		
		ProgressBar progressBar = new ProgressBar(shell, SWT.NONE);
		progressBar.setBounds(6, 32, 413, 14);
		
		StyledText styledText = new StyledText(shell, SWT.BORDER);
		styledText.setBounds(5, 55, 421, 65);
		
		
		System.out.println("Shiyaml");
		OSCommand("Run");

	}
	
	public static void OSCommand(String a)   
	{   
		try{

	       // String command = "ifconfig eth1 | grep -oP '[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}'";
			String[] command = {
                    "/bin/sh",
                    "-c",
                    "ifconfig lo0 | grep -oP '[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}'"
            };
			
			Process child = Runtime.getRuntime().exec(command);
	        System.out.println("So far so good");
	        System.out.println("Shiyaml");
	        System.out.println("Shiyaml");
	        System.out.println("Shiyaml");
	        System.out.println("Shiyaml");
	        System.out.println("Shiyaml");
	        System.out.println("Shiyaml");
	        System.out.println("Shiyaml");
	        System.out.println("Shiyaml");
	        BufferedReader r = new BufferedReader(new InputStreamReader(child.getInputStream()));
	        String s;
	        while ((s = r.readLine()) != null) {
	        System.out.println(s);
	        }
	        r.close();
	        System.out.println("Continue..");
	    }
	    catch (IOException e) {
	        e.printStackTrace();
	    }  
	}
	
}
