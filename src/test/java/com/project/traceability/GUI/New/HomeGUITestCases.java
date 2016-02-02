package com.project.traceability.GUI.New;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

@RunWith(BlockJUnit4ClassRunner.class)
public class HomeGUITestCases  extends HomeGUITest{

	


	
	 @Test
	  public void testNameInput() {
//		 SWTBotPreferences.PLAYBACK_DELAY = 100; // slow down tests...Otherwise we won't see anything
//
		 	//bot.menu("File").menu("New").menu("Project").click();
		 
		 	assertResultGivenInput("");

	       
	    }
	 
  
}
