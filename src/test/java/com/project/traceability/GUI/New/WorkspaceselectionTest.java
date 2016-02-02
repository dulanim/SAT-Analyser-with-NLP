package com.project.traceability.GUI.New;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotCheckBox;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class WorkspaceselectionTest extends StartUpGUITest{

	String paths[] = {"","/home/Gui12","/home/shiyam/wrkspace"};
		 @Test
		 public void testEmptyInput() {
			 //when path is empty
			 assertResultGivenInput(0);
		 }
		 
	    @Test
	    public void testNonValidPathInput() {
	    	//when path is not empty but not exists
	    	assertResultGivenInput(1);
	    }
	 
	    @Test
	    public void testValidPathInput() {
	    	//when path valid and exists 
	       //assertResultGivenInput(2);
	    }
	    
	    
	    @Test
	    public void testcheckBoxPressed(){
	    	//when check box clicked on workspacce selection window
	    	//selection value should be true
	    	SWTBotCheckBox checkBox = bot.checkBox();
	    	checkBox.click();
	    	boolean isPressed = checkBox.isChecked();
	    	
	    	assertEquals(true, isPressed);
	    	
	    }
	    
	    @Test
	    public void testCheckBoxUnPressed(){
	    	//when check box un-clicked on workspace selection window
	    	//selection value should be false
	    	
	    	SWTBotCheckBox checkBox = bot.checkBox();
	    	boolean isPressed = checkBox.isChecked();
	    	
	    	if(!isPressed)
		    	checkBox.click();
	    	checkBox.click();
	    	assertEquals(false, isPressed);
	    }
//	    @Test
//	    public void testButtonPressed(){
//	    	SWTBotButton cancelBtn = bot.button("Cancel");
//	    	cancelBtn.click();
//	    	
//	    	assertEquals(true, shell.isDisposed());
//	    }
	    @Test
	    public void testBrowsePressed(){
	    	assertBrowseCancelPressed();
	    }
	  
}
