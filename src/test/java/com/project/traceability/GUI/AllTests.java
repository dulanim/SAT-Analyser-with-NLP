package com.project.traceability.GUI;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;


// specify a runner class: Suite.class
@RunWith(Suite.class)

// specify an array of test classes
@Suite.SuiteClasses({
	HomeGUITest.class,
	NewProjectWindowTest.class,
	NewFileWindowTest.class
  }
)

public class AllTests {

}
