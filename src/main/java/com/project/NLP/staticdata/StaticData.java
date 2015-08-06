/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.staticdata;

import java.util.List;
import com.project.traceability.model.Dependencies;
import com.project.traceability.model.ModelData;

/**
 *
 * @author shiyam
 */
public class StaticData {
    public static final String ARTEFACTS_ROOT = "Artifacts";
    public static final String ARTEFACT_ROOT = "Artifact";
    public static final String ARTEFACTELEMENT_ROOT = "ArtifactElement";
    public static final String ARTEFACTUBELEMENT_ROOT="ArtifactSubElement";
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
    
}
