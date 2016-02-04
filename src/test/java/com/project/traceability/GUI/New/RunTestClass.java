
package com.project.traceability.GUI.New;


import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCheckBox;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class RunTestClass extends AbstractTestWindow1{

	String names[] = {"Project","123",""};
	String rootDir = "/home/shiyam/Downloads/TestCases/OrderEquirement/";
	String validFileNames[] = {"OrderRequirement.txt",""
			,"orderUML.mdj"};
	String javaValidDir = "/home/shiyam/Downloads/TestCases/OrderEquirement/src" ;
		 @Test
		 public void testProjectNameTestInput() {
			 //when path is empty
			 assertResultGivenInput(names[0]);//valid name
			 assertResultGivenInput(names[1]);//Invalid but has name
			 assertResultGivenInput(names[2]);//not valid name
		 }
		 
		 @Test
		 public void testCreateProject(){
			 String projName = "JavaTracePro";//should be valid name
			 SWTBotText userText = bot.textWithLabel("Traceabilty Project Name");
	    	 userText.setFocus();
	         userText.setText(projName);
	         
	         SWTBotButton btnOk = bot.button("Ok");
	         btnOk.click();
	         
			 SWTBotText txtRequirement = bot.textWithLabel("Requirement File");
			 txtRequirement.setFocus();
			 txtRequirement.setText(rootDir + validFileNames[0]);
			 
			 
			 SWTBotText txtUml = bot.textWithLabel("Design Diagram File");
			 txtUml.setFocus();
			 txtUml.setText(rootDir + validFileNames[0]);
			 
			 SWTBotText txtJava = bot.textWithLabel("Project Path");
			 txtJava.setFocus();
			 txtJava.setText(javaValidDir);
			 
			 SWTBotButton btnFinish = bot.button("Finish");
			 if(btnFinish.isEnabled()){
				 btnFinish.click();
			 }
			 
		 }
	   
		 @Test
		 public void testForCheckBoxClick(){
			 //to check weather check box click is work or not
			 SWTBotCheckBox chckBox = bot.checkBox();
			 chckBox.click();
			 boolean isClicked = chckBox.isChecked();
			 
			 assertEquals(true, isClicked);
		 }
		 
		 @Test
		 public void testForValidPath(){
			 String path = "/home/shiyam/SATAnalyzer/test";
			 assertForNewWorkspaceCreation(path);
		 }
		 
		 @Test
		 public void testForInvalidPath(){
			 String path = "/home/shiyam/SATAnalyzer/test/dummy";
			 assertForNewWorkspaceCreation(path);
		 }
		 
		 
	    
	    
	  
	  
}
