/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.manager;

import com.project.traceability.GUI.CompareWindow;
import static com.project.traceability.manager.RequirementSourceClassManager.relationNodes;
import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.ArtefactSubElement;
import com.project.traceability.semanticAnalysis.SynonymWords;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;

/**
 *
 * @author K.Kamalan
 */
public class ClassManager {

    static List<String> reqClassName = new ArrayList<>();
    static List<String> sourceClassName = new ArrayList<>();
    static List<String> umlClassName = new ArrayList<>();

    static String projectPath;

    public static List<String> getReqClassName(String projectPath) {
        RequirementSourceClassManager.projectPath = projectPath;
        RequirementsManger.readXML(projectPath);
        Map<String, ArtefactElement> reqMap = RequirementsManger.requirementArtefactElements;
        Iterator<Map.Entry<String, ArtefactElement>> requirementIterator = reqMap
                .entrySet().iterator();
        while (requirementIterator.hasNext()) {
            Map.Entry pairs = requirementIterator.next();
            ArtefactElement reqArtefactElement = (ArtefactElement) pairs
                    .getValue();
            reqClassName.add(reqArtefactElement.getName().toLowerCase());
        }

        return reqClassName;

    }

    public static List<String> getUmlClassName(String projectPath) {
        UMLArtefactManager.readXML(projectPath);
	Map<String, ArtefactElement> UMLMap = UMLArtefactManager.UMLAretefactElements;
	Iterator<Map.Entry<String, ArtefactElement>> umlIterator = null;
        umlIterator = UMLMap.entrySet().iterator();
        while (umlIterator.hasNext()) {
            Map.Entry pairs1 = umlIterator.next();
            ArtefactElement umlArtefactElement = (ArtefactElement) pairs1
							.getValue();
            umlClassName.add(umlArtefactElement.getName().toLowerCase());
        
        }


        return umlClassName;
    }

    public static List<String> getSourceClassName(String projectPath) {

        return sourceClassName;
    }

}
