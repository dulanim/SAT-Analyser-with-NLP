/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.staticdata;

import com.project.progress.progressbar.LogReader;
import java.util.List;

import com.project.traceability.model.Dependencies;
import com.project.traceability.model.ModelData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author shiyam
 */
public class StaticData {
    public static final String ARTEFACTS_ROOT = "Artefacts";
    public static final String ARTEFACT_ROOT = "Artefact";
    public static final String ARTEFACTELEMENT_ROOT = "ArtefactElement";
    public static final String ARTEFACTUBELEMENT_ROOT="ArtefactSubElement";
    public static final String INTRACONNECTION_ROOT = "IntraConnections";
    public static final String CONNECTION_ROOT = "Connection";
    public static final String TYPE_CONNECTION_ROOT = "Type";
    public static final String STARTPOINT_ROOT = "StartPoint";
    public static final String MULTIPLICITY_ROOT= "Multiplicity";
    public static final String ENDPOINT_ROOT = "EndPoint";
    public static final String ANNOTATION_ROOT = "Annotation";
    public static final String NAME_ROOT = "name";
    public static final String TYPE_ROOT = "type";
    public static final String ID_ROOT = "id";
    public static final String VISIBILITY_ROOT = "visibility";
    public static final String VARIABLE_TYPE_ROOT = "variableType";
    public static final String PARAMETER_ROOT = "parameters";
    public static final String RETURN_TYPE_ROOT = "returnType";
    public static List<ModelData> classLst;
    public static List<Dependencies> depencyList;
    public static String umlFilePath = "";
    public static String sourceFilePath = "";
    public static String workspace = "";
    public static String rootPathName; //propety file it is the project root directory
    public static HashMap<String,List<String>> folderNames = new HashMap<String, List<String>>();//project folder list
    public static Set<String> paths = new HashSet<String>();
    public static HashMap<String,List<String>> internalfileNames = new HashMap<String, List<String>>();//project folder's internal file list
    public static LogReader lpOut;//= LogReader.create(System.out);
    public static LogReader lpEror;// = LogReader.create(System.err);
    
    /*
    tool tip text of windows 
    */
    public static final String PROJECT_WINDOW_TOOL_TIP = "It contains project folders\n"
            + "you can open using double press on that folder";

}
