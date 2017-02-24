/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.visualization;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author Thanu
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({com.project.traceability.visualization.GraphDBTest.class, com.project.traceability.visualization.GraphFileGeneratorTest.class})
public class VisualizationTestSuite {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
}
