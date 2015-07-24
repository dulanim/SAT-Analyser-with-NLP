/**
 *
 */
package com.project.traceability.common;

import com.project.traceability.visualization.VisualizeGraph;

/**
 * 13 Nov 2014
 *
 *
 */
public class PropertyFile {

    public static final String requirementXMLPath = "D:/Final Year Project/SATAnalyzer/ATOM/RequirementArtefactFile.xml";
    public static final String umlXMLPath = "D:/Final Year Project/SATAnalyzer/ATOM/UMLArtefactFile.xml";
    public static final String sourceXMLPath = "D:/Final Year Project/SATAnalyzer/ATOM/SourceCodeArtefactFile.xml";
    public static final String wordNetDbDirectory = "C:/Program Files (x86)/WordNet/2.1/dict";
    public static String filePath = "D:/Final Year Project/SATAnalyzer/SATWork/";
    public static String xmlFilePath = "D:/Final Year Project/SATAnalyzer/ATOM";
    public static String xmlSourceCodeFilePath = "D:/Final Year Project/SATAnalyzer/XML-SourceCode";
    public static final String imagePath = "D:/Final Year Project/Github/Anduril/img"; // img folder in
    // project
    private static String projectName = null;
    public static String graphDbPath = null;
    private static String generatedGexfFilePath = null;
    private static String relationshipXMLPath = null;
    public static String graphType = null;
    private static VisualizeGraph visual = null;
    public static final String testFilePath = "E:/SATWork/test/";
    public static final String testXmlFilePath = "E:/ATOM/test/";
    public static final String testDb = "E:/SATWork/Test/test.graphDb";
    public static final String testGraphFile = "E:/SATWork/Test/test.gexf";

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
