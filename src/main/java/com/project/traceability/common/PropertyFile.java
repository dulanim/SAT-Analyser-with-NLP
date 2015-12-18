/**
 *
 */
package com.project.traceability.common;

import com.project.NLP.file.operations.FilePropertyName;
import com.project.traceability.staticdata.StaticData;
import com.project.traceability.GUI.HomeGUI;
import com.project.traceability.visualization.VisualizeGraph;
import java.io.File;

/**
 * 13 Nov 2014
 *
 *
 */
public class PropertyFile {

    public static String requirementXMLPath = "E:/Drive Copied/Shiyamalan/projects/fyp/SAT-master/XML files/RequirementArtefactFile.xml";
    public static String umlXMLPath = "E:/Drive Copied/Shiyamalan/projects/fyp/SAT-master/XML files/UMLArtefactFile.xml";
    public static String sourceXMLPath = "E:/Drive Copied/Shiyamalan/projects/fyp/SAT-master/XML files/SourceCodeArtefactFile.xml";
    public static final String wordNetDbDirectory = "/usr/share/wordnet";
    public static String filePath = StaticData.workspace+File.separator;
    public static String xmlFilePath = "E:/Drive Copied/Shiyamalan/projects/fyp/SAT-master/XML files";
    public static String docsFilePath = "E:/Drive Copied/Shiyamalan/projects/fyp/SAT-master/XML files";
    public static final String imagePath = "/home/shiyam/Projects/FinalYear/Anduril/img"; // img folder in
    // project
    private static String projectName = null;
    public static String graphDbPath = null;
    private static String generatedGexfFilePath = null;
    private static String relationshipXMLPath = null;
    public static String graphType = null;
    private static VisualizeGraph visual = null;
    public static final String testFilePath = "E:/SATWork/";
    public static final String testXmlFilePath = "E:/ATOM/test/";
    public static final String testDb = "E:/SATWork/Test/Test.graphDb";
    public static final String testGraphFile = "E:/SATWork/Test/Test.gexf";
    public static final String xmlSourceCodeFilePath = PropertyFile.filePath + File.separator +
            FilePropertyName.XML;
    public static String configuration_file_path = System.getProperty("user.home")
                +File.separator + "sat_configuration.xml";
    public static String getProjectName() {
        return projectName;
    }

    public static void setProjectName(String projectName) {
        PropertyFile.projectName = projectName;
    }

    public static String getGraphDbPath() {
        return graphDbPath;
    }

    public static void setGraphDbPath(String graphDbPath) {
        PropertyFile.graphDbPath = graphDbPath;
    }

    public static String getGeneratedGexfFilePath() {
        return generatedGexfFilePath;
    }

    public static void setGeneratedGexfFilePath(String generatedGexfFilePath) {
        PropertyFile.generatedGexfFilePath = generatedGexfFilePath;
    }

    public static String getRelationshipXMLPath() {
        return relationshipXMLPath;
    }

    public static void setRelationshipXMLPath(String relationshipXMLPath) {
        PropertyFile.relationshipXMLPath = relationshipXMLPath;
    }

    public static String getGraphType() {
        return graphType;
    }

    public static void setGraphType(String graphType) {
        PropertyFile.graphType = graphType;
    }

    public static VisualizeGraph getVisual() {
        return visual;
    }

    public static void setVisual(VisualizeGraph visual) {
        PropertyFile.visual = visual;
    }

    public static String getFilePath() {
        return filePath;
    }

    public static void setFilePath(String filePath) {
        PropertyFile.filePath = filePath;
    }

    public static String getXmlFilePath() {
        return xmlFilePath;
    }

    public static void setXmlFilePath(String xmlFilePath) {
        PropertyFile.xmlFilePath = xmlFilePath;
    }

    public static String getTestDb() {
        return testDb;
    }

    public static String getRequirementXMLPath() {
        return requirementXMLPath;
    }

    public static String getUmlXMLPath() {
        return umlXMLPath;
    }

    public static String getSourceXMLPath() {
        return sourceXMLPath;
    }

    public static String getWordNetDbDirectory() {
        return wordNetDbDirectory;
    }

    public static String getImagePath() {
        return imagePath;
    }

    public static String getTestFilePath() {
        return testFilePath;
    }

    public static String getTestXmlFilePath() {
        return testXmlFilePath;
    }

    public static String getTestGraphFile() {
        return testGraphFile;
    }
           
}
