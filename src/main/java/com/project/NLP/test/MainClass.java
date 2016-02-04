/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.test;

import com.project.NLP.Requirement.ClassRelation;
import com.project.NLP.Requirement.StoringArtefacts;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 *
 * @author S. Shobiga
 */
public class MainClass {

    public static void main(String args[]) {
        //RequirementDocumentTest machineOutput = new RequirementDocumentTest("io/Requirement_Output_machine.txt");
        RequirementDocumentTest machineOutput = new RequirementDocumentTest("io/Requirement_Output_Bank_Machine.txt");
        
        RequirementDocumentTest manualOutput = new RequirementDocumentTest("io/Requirement_Output_Hostel_Expert_1.txt");
        
        HashMap map = machineOutput.getRequirementObjects();
        NLPMeasure measure = new NLPMeasure(machineOutput.getRequirementObjects(), manualOutput.getRequirementObjects(), machineOutput.getRequirementObjectRelations(), manualOutput.getRequirementObjectRelations());
        
        System.out.println("Precision - class :"+ measure.calculatePrecisionOfClass());
        System.out.println("Precision - attribute :"+ measure.calculatePrecisionOfAttribute() );
        System.out.println("Precision - method :"+ measure.calculatePrecisionOfMethod());
        System.out.println("Precision - Relationship :"+ measure.calculatePrecisionOfRelation());
        
        System.out.println("Recall - class :"+ measure.calculateRecallOfClass());
        System.out.println("Recall - attribute :"+ measure.calculateRecallOfAttribute());
        System.out.println("Recall - method :"+ measure.calculateRecallOfMethod());
        System.out.println("Recall - Relationship :"+ measure.calculateRecallOfRelation());
        
        System.out.println("F1 - class :"+ measure.calculateF1MeasureOfClass());
        System.out.println("F1 - attribute :"+ measure.calculateF1MeasureOfClass());
        System.out.println("F1- method :"+ measure.calculateF1MeasureOfMethod());
        System.out.println("F1- relationship :"+ measure.calculateF1MeasureOfRelation());
        
    }
   
}
