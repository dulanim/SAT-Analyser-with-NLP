/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.sattest;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.project.traceability.manager.ManagerTestSuite;
import com.project.traceability.semanticAnalysis.SematicAnalysisTestSuite;
import com.project.traceability.visualization.VisualizationTestSuite;

/**
 * 
 * @author Thanu
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ VisualizationTestSuite.class,
		SematicAnalysisTestSuite.class, ManagerTestSuite.class})
		//GUITestSuite.class })
public class SATTestSuite {

	@BeforeClass
	public static void setUpClass() throws Exception {
	}

	@AfterClass
	public static void tearDownClass() throws Exception {
	}
}
