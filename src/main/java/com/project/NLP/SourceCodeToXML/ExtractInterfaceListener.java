/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.SourceCodeToXML;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Attr;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.project.traceability.staticdata.StaticData;

/**
 * Walks the tree class
 *
 * @author AARTHIKA
 */
public class ExtractInterfaceListener extends Java8BaseListener {

    private Java8Parser parser;
    private String paramType, paramName, paramMod;
    private StringBuffer parameterList = new StringBuffer();
    private String methodReturn, methodName, methodMod, type;
    private String className, classMod, superClass = "";
    private String fieldName, fieldMod, fieldType,fieldStatus = StaticData.DEFAULT_STATUS;
    private Element artefactElement;
    public static Node root;
    private List<String> fieldNameList = new ArrayList();
    private List<Java8Parser.InterfaceTypeContext> implementClass = new ArrayList<>();
    private static int classId = 1;
    static int attrId = 1;
    static int methodId = 1;

    private static final String INHERITANCE = "INHERITANCE";
    private static final String COMPOSITION = "COMPOSITION";
    private String currentClassID = "";
    private String methodStatus;
    private String classStatus;
    
    public ExtractInterfaceListener(Java8Parser parser) throws ParserConfigurationException {
        this.parser = parser;
        /*classId = 1;
        */
        attrId = 1;
        methodId = 1;
    }

    /**
     * Enters the field declaration
     *
     * @param ctx
     */
    @Override
    public void enterFieldDeclaration(Java8Parser.FieldDeclarationContext ctx) {
        super.enterFieldDeclaration(ctx); //To change body of generated methods, choose Tools | Templates.
        // ctx.unannType().unannReferenceType().unannClassOrInterfaceType().
        type = "Field";
        fieldStatus = StaticData.DEFAULT_STATUS;
        String associationType = "COMPOSITION";
        if (ctx.unannType().unannPrimitiveType() != null) {
            fieldType = ctx.unannType().unannPrimitiveType().getText();
            if (ctx.fieldModifier(0) != null) {
                fieldMod = ctx.fieldModifier(0).getText();
            }
            List<Java8Parser.VariableDeclaratorContext> variables = ctx.variableDeclaratorList().variableDeclarator();
            for (Java8Parser.VariableDeclaratorContext variable : variables) {
                fieldNameList.add(variable.variableDeclaratorId().getText());
            }

            for (String nameList : fieldNameList) {
                fieldName = nameList;
                writeFieldToXML();
            }
        } else if (ctx.unannType().unannReferenceType() != null) {
            fieldType = ctx.unannType().unannReferenceType().getText();
            if (fieldType.equalsIgnoreCase("String") || fieldType.equalsIgnoreCase("Object")) {
                if (ctx.fieldModifier(0) != null) {
                    fieldMod = ctx.fieldModifier(0).getText();
                }
                List<Java8Parser.VariableDeclaratorContext> variables = ctx.variableDeclaratorList().variableDeclarator();
                for (Java8Parser.VariableDeclaratorContext variable : variables) {
                    fieldNameList.add(variable.variableDeclaratorId().getText());
                }

                for (String nameList : fieldNameList) {
                    fieldName = nameList;
                    writeFieldToXML();
                }
            } else {
                try {
                    if(fieldType.contains("<")&&fieldType.contains(">")){
                       fieldType =  fieldType.substring(fieldType.indexOf("<")+1, fieldType.indexOf(">"));
                    }
                    AST.scdb.createNodeRelationship(className, currentClassID, fieldType, associationType);
                } catch (Exception e) {

                }
            }

        }
        fieldNameList.clear();
    }

    /**
     * Writes the field to xml
     */
    public void writeFieldToXML() {

        Element artefactSubElement = WriteToXML.getDocument().createElement("ArtefactSubElement");
        artefactElement.appendChild(artefactSubElement);

        Attr typeAttr = WriteToXML.getDocument().createAttribute("type");
        typeAttr.setValue(type);
        artefactSubElement.setAttributeNode(typeAttr);

        Attr idAttr = WriteToXML.getDocument().createAttribute("id");       
        String attrID = currentClassID + "_F" + ExtractInterfaceListener.attrId;
        ExtractInterfaceListener.attrId++;
        idAttr.setValue(attrID);
        artefactSubElement.setAttributeNode(idAttr);

        Attr nameAttr = WriteToXML.getDocument().createAttribute("name");
        nameAttr.setValue(fieldName);
        artefactSubElement.setAttributeNode(nameAttr);

        Attr visibilityAttr = WriteToXML.getDocument().createAttribute("visibility");
        visibilityAttr.setValue(fieldMod);
        artefactSubElement.setAttributeNode(visibilityAttr);

        Attr fieldTypeAttr = WriteToXML.getDocument().createAttribute("variableType");
        fieldTypeAttr.setValue(fieldType);
        artefactSubElement.setAttributeNode(fieldTypeAttr);
        
        Attr fieldStatusAttr = WriteToXML.getDocument().createAttribute(StaticData.STATUS);
        fieldStatusAttr.setValue(fieldStatus);
        artefactSubElement.setAttributeNode(fieldStatusAttr);
    }

    /**
     * Enters method declaration
     *
     * @param ctx
     */
    @Override
    public void enterMethodDeclaration(Java8Parser.MethodDeclarationContext ctx) {
        super.enterMethodDeclaration(ctx);
        methodStatus = StaticData.DEFAULT_STATUS;
        if (ctx.methodModifier().isEmpty()) {
            methodMod = "package-private";
        } else if (ctx.methodModifier().size() == 2) {
            if (ctx.methodModifier().get(0).getText().equalsIgnoreCase("Override")) {
                //Ignore the method as it is not a class method
            }
        } else {
            methodMod = ctx.methodModifier().get(0).getText();

            //System.out.println("Override" + ctx.methodModifier().get(0).getText());
            type = "Method";
            parameterList = new StringBuffer();
            Java8Parser.MethodDeclaratorContext mdc = ctx.methodHeader().methodDeclarator();
            methodName = mdc.Identifier().getText();
            //System.out.println("Method Name: " + methodName);
            methodReturn = ctx.methodHeader().result().getText();
            //System.out.println("Method Return: " + methodReturn);

            //System.out.println("Method Modifier: " + methodMod);
            if (mdc.formalParameterList() != null) {
                if (mdc.formalParameterList().formalParameters() != null) {
                    List<Java8Parser.FormalParameterContext> param = mdc.formalParameterList().formalParameters().formalParameter();

                    for (Java8Parser.FormalParameterContext par : param) {
                        paramType = "";
                        paramName = "";
                        paramMod = "";
                        getParameter(par);
                        //System.out.println("Parameter type:" + paramType + " name:" + paramName + " mod:" + paramMod);
                        parameterList.append(paramName + ":" + paramType + ", ");
                    }
                }

                paramType = "";
                paramName = "";
                paramMod = "";
                getParameter(mdc.formalParameterList().lastFormalParameter().formalParameter());
                //System.out.println("Parameter type:" + paramType + " name:" + paramName + " mod:" + paramMod);
                parameterList.append(paramName + ":" + paramType);
                //System.out.println(parameterList.toString());
            }
            Element artefactSubElement = WriteToXML.getDocument().createElement("ArtefactSubElement");
            artefactElement.appendChild(artefactSubElement);

            Attr typeAttr = WriteToXML.getDocument().createAttribute("type");
            typeAttr.setValue(type);
            artefactSubElement.setAttributeNode(typeAttr);

            Attr idAttr = WriteToXML.getDocument().createAttribute("id");
            String methodID = currentClassID + "_M" + ExtractInterfaceListener.methodId;
            ExtractInterfaceListener.methodId++;
            idAttr.setValue(methodID);
            artefactSubElement.setAttributeNode(idAttr);

            Attr nameAttr = WriteToXML.getDocument().createAttribute("name");
            nameAttr.setValue(methodName);
            artefactSubElement.setAttributeNode(nameAttr);

            Attr visibilityAttr = WriteToXML.getDocument().createAttribute("visibility");
            visibilityAttr.setValue(methodMod);
            artefactSubElement.setAttributeNode(visibilityAttr);

            Attr fieldTypeAttr = WriteToXML.getDocument().createAttribute("returnType");
            fieldTypeAttr.setValue(methodReturn);
            artefactSubElement.setAttributeNode(fieldTypeAttr);

            Attr parameterAttr = WriteToXML.getDocument().createAttribute("parameters");
            parameterAttr.setValue(parameterList.toString());
            artefactSubElement.setAttributeNode(parameterAttr);
            
            Attr statusAttr = WriteToXML.getDocument().createAttribute(StaticData.STATUS);
            statusAttr.setValue(methodStatus);
            artefactSubElement.setAttributeNode(statusAttr);
        }

    }

    /**
     * Returns the paramter
     *
     * @param par
     */
    private void getParameter(Java8Parser.FormalParameterContext par) {
        int children = par.getChildCount();
        if (children == 2) {
            paramType = par.getChild(0).getText();
            paramName = par.getChild(1).getText();
        } else if (children == 3) {
            paramMod = par.getChild(0).getText();
            paramType = par.getChild(1).getText();
            paramName = par.getChild(2).getText();
        }
    }

    @Override
    public void exitClassDeclaration(Java8Parser.ClassDeclarationContext ctx) {

    }

    /**
     * Enters the class declaration
     *
     * @param ctx
     */
    @Override
    public void enterClassDeclaration(Java8Parser.ClassDeclarationContext ctx) {
        type = "Class";
        attrId = 1;
        methodId = 1;
        String inheritanceType = "INHERITANCE";
        className = ctx.normalClassDeclaration().Identifier().getText();
        classStatus = StaticData.DEFAULT_STATUS;
        if (ctx.normalClassDeclaration().classModifier().isEmpty()) {
            classMod = "package-private";
        } else {
            classMod = ctx.normalClassDeclaration().classModifier().get(0).getText();
        }

        //System.out.println("Class: " + className + " Modifier: " + classMod);
        if (ctx.normalClassDeclaration().superclass() != null) {
            superClass = ctx.normalClassDeclaration().superclass().getChild(1).getText();
        }
        if (ctx.normalClassDeclaration().superinterfaces() != null) {
            implementClass = ctx.normalClassDeclaration().superinterfaces().interfaceTypeList().interfaceType();
        }
        
        if(WriteToXML.isTragging.equals("Tragging"))
        	currentClassID = WriteToXML.TAG + "_" + "SC" + ExtractInterfaceListener.classId;
        else{
        	currentClassID = "SC" + ExtractInterfaceListener.classId;
        }
        try {
            AST.scdb.createNodeRelationship(className, currentClassID, superClass, inheritanceType);
            AST.scdb.addClassID(className, currentClassID);
        } catch (Exception e) {
        }
        ExtractInterfaceListener.classId++;

        artefactElement = WriteToXML.getDocument().createElement("ArtefactElement");
        root = WriteToXML.getDocument().getElementsByTagName("FileSystemLocation").item(0).getParentNode();
        artefactElement = WriteToXML.getDocument().createElement("ArtefactElement");
        root.appendChild(artefactElement);
        Attr nameAttr = WriteToXML.getDocument().createAttribute("name");
        nameAttr.setValue(className);
        artefactElement.setAttributeNode(nameAttr);

        Attr typeAttr = WriteToXML.getDocument().createAttribute("type");
        typeAttr.setValue(type);
        artefactElement.setAttributeNode(typeAttr);

        Attr idAttr = WriteToXML.getDocument().createAttribute("id");
        idAttr.setValue(currentClassID);

        artefactElement.setAttributeNode(idAttr);

        Attr visibilityAttr = WriteToXML.getDocument().createAttribute("visibility");
        visibilityAttr.setValue(classMod);
        artefactElement.setAttributeNode(visibilityAttr);

        Attr superClassAttr = WriteToXML.getDocument().createAttribute("superClass");
        superClassAttr.setValue(superClass);
        artefactElement.setAttributeNode(superClassAttr);
        //Adding multiple interfaces
        Attr interfaceAttr = WriteToXML.getDocument().createAttribute("interface");
        interfaceAttr.setValue(implementClass.toString());
        artefactElement.setAttributeNode(interfaceAttr);
        
        Attr statusAttr = WriteToXML.getDocument().createAttribute(StaticData.STATUS);
        statusAttr.setValue(classStatus);
        artefactElement.setAttributeNode(statusAttr);
    }

}
