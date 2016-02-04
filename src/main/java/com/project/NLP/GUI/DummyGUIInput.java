/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.GUI;

import com.project.NLP.Requirement.ClassRelation;
import com.project.NLP.Requirement.StoringArtefacts;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author S. Shobiga
 */
public class DummyGUIInput {
    public static void main(String args[]){
        
    
        StoringArtefacts storing = new StoringArtefacts();
        HashSet className = new HashSet();
        HashSet attribute = new HashSet();
        HashSet method = new HashSet();
        HashSet relation = new HashSet();
        final HashMap requirementObj = new HashMap();

        String classn = "bank";
        className.add("bank");
        attribute.add("client");
        attribute.add("account");
        method.add("set branch");
        method.add("set clients");
        
        storing.addClassName(className);
        storing.addAttributes(attribute);
        storing.addMethods(method);
        storing.addRelationships(relation);
        
        requirementObj.put(classn, storing);

        classn = "client";
        attribute = new HashSet();
        attribute.add("name");
        attribute.add("age");
        attribute.add("address");
        method = new HashSet();
        method.add("withdraw");
        method.add("deposit");
        relation.add("generalization - person");
        
        storing = new StoringArtefacts();
        storing.addClassName(className);
        storing.addAttributes(attribute);
        storing.addMethods(method);
        storing.addRelationships(relation);
        
        requirementObj.put(classn, storing);
        
        classn = "c";
        attribute = new HashSet();
        attribute.add("name");
        attribute.add("age");
        attribute.add("address");
        method = new HashSet();
        method.add("withdraw");
        method.add("deposit");
        relation.add("generalization - person");
        
        storing = new StoringArtefacts();
        storing.addClassName(className);
        storing.addAttributes(attribute);
        storing.addMethods(method);
        storing.addRelationships(relation);
        
        requirementObj.put(classn, storing);
        
        classn = "clfsdfsfsient";
        attribute = new HashSet();
        attribute.add("name");
        attribute.add("age");
        attribute.add("address");
        method = new HashSet();
        method.add("withdraw");
        method.add("deposit");
        relation.add("generalization - person");
        
        storing = new StoringArtefacts();
        storing.addClassName(className);
        storing.addAttributes(attribute);
        storing.addMethods(method);
        storing.addRelationships(relation);
        
        requirementObj.put(classn, storing);
        
        classn = "dffdddddddddddddddddddddddssssssssssssssssssssssssssclient";
        attribute = new HashSet();
        attribute.add("name");
        attribute.add("age");
        attribute.add("address");
        method = new HashSet();
        method.add("withdraw");
        method.add("deposit");
        relation.add("generalization - person");
        
        storing = new StoringArtefacts();
        storing.addClassName(className);
        storing.addAttributes(attribute);
        storing.addMethods(method);
        storing.addRelationships(relation);
        
        requirementObj.put(classn, storing);
        
        HashSet requirementObjRelation = new HashSet();
        requirementObjRelation.add(new ClassRelation("Association", "client", "dffdddddddddddddddddddddddssssssssssssssssssssssssssclient"));
        requirementObjRelation.add(new ClassRelation("Association", "c", "bank"));
        requirementObjRelation.add(new ClassRelation("Association", "dffdddddddddddddddddddddddssssssssssssssssssssssssssclient", "client"));
        
        
        
        ArtefactFrameTestGUI t = new ArtefactFrameTestGUI(requirementObj,requirementObjRelation);
        //DeleteNodes t = new DeleteNodes(requirementObj);
        
    }
}
