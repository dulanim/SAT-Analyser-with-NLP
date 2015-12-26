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

/**
 *
 * @author AARTHIKA
 */
public class ExtractInterfaceListener extends Java8BaseListener {
    
    
    private Java8Parser parser;
    private String paramType, paramName, paramMod;
    private StringBuffer parameterList = new StringBuffer();
    private String methodReturn, methodName, methodMod, type;
    private String className, classMod, superClass="";
    private String fieldName, fieldMod, fieldType;
    private Element artefactElement;
    public static Node root;
    private List<String> fieldNameList = new ArrayList();
    private List<Java8Parser.InterfaceTypeContext> implementClass = new ArrayList<>();
    private static int id = 1;
    private static String INHERITANCE = "INHERITANCE";
    private static String COMPOSITION = "COMPOSITION";
    private String currentClassID = "";
    public ExtractInterfaceListener(Java8Parser parser) throws ParserConfigurationException {        
        this.parser = parser;
    }

    @Override
    public void enterFieldDeclaration(Java8Parser.FieldDeclarationContext ctx) {
        super.enterFieldDeclaration(ctx); //To change body of generated methods, choose Tools | Templates.
        // ctx.unannType().unannReferenceType().unannClassOrInterfaceType().
        type = "Field";
        super.enterFieldDeclaration(ctx); 
        type = "Variable";
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
            
            AST.scdb.createNodeRelationship(className, currentClassID, fieldType,associationType);
        }       
        fieldNameList.clear();
    }
    public void writeFieldToXML(){
        

        Element artefactSubElement = WriteToXML.getDocument().createElement("ArtefactSubElement");
        artefactElement.appendChild(artefactSubElement);

        Attr typeAttr = WriteToXML.getDocument().createAttribute("type");
        typeAttr.setValue(type);
        artefactSubElement.setAttributeNode(typeAttr);

        Attr idAttr = WriteToXML.getDocument().createAttribute("id");
        idAttr.setValue("SC" + id);
        id++;
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
    }

    @Override
    public void enterMethodDeclaration(Java8Parser.MethodDeclarationContext ctx) {
        super.enterMethodDeclaration(ctx); 

        if (ctx.methodModifier().isEmpty()) {
            methodMod = "package-private";
        } else {
            if (ctx.methodModifier().size() == 2) {
                if (ctx.methodModifier().get(0).getText().equalsIgnoreCase("Override")) {
                    //Ignore the method as it is not a class method
                }
            } else {
                methodMod = ctx.methodModifier().get(0).getText();

                System.out.println("Override" + ctx.methodModifier().get(0).getText());
                type = "Method";
                parameterList = new StringBuffer();
                Java8Parser.MethodDeclaratorContext mdc = ctx.methodHeader().methodDeclarator();
                methodName = mdc.Identifier().getText();
                System.out.println("Method Name: " + methodName);
                methodReturn = ctx.methodHeader().result().getText();
                System.out.println("Method Return: " + methodReturn);

                System.out.println("Method Modifier: " + methodMod);

                if (mdc.formalParameterList() != null) {
                    if (mdc.formalParameterList().formalParameters() != null) {
                        List<Java8Parser.FormalParameterContext> param = mdc.formalParameterList().formalParameters().formalParameter();

                        for (Java8Parser.FormalParameterContext par : param) {
                            paramType = "";
                            paramName = "";
                            paramMod = "";
                            getParameter(par);
                            System.out.println("Parameter type:" + paramType + " name:" + paramName + " mod:" + paramMod);
                            parameterList.append(paramName + ":" + paramType + ", ");
                        }
                    }

                    paramType = "";
                    paramName = "";
                    paramMod = "";
                    getParameter(mdc.formalParameterList().lastFormalParameter().formalParameter());
                    System.out.println("Parameter type:" + paramType + " name:" + paramName + " mod:" + paramMod);
                    parameterList.append(paramName + ":" + paramType);
                    System.out.println(parameterList.toString());
                }
                Element artefactSubElement = WriteToXML.getDocument().createElement("ArtefactSubElement");
                artefactElement.appendChild(artefactSubElement);

                Attr typeAttr = WriteToXML.getDocument().createAttribute("type");
                typeAttr.setValue(type);
                artefactSubElement.setAttributeNode(typeAttr);

                Attr idAttr = WriteToXML.getDocument().createAttribute("id");
                idAttr.setValue("SC" + id);
                id++;
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
            }
        }

    }

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

    @Override
    public void enterClassDeclaration(Java8Parser.ClassDeclarationContext ctx) {
        type = "Class";
        String inheritanceType = "INHERITANCE";
        className = ctx.normalClassDeclaration().Identifier().getText();

        if (ctx.normalClassDeclaration().classModifier().isEmpty()) {
            classMod = "package-private";
        } else {
            classMod = ctx.normalClassDeclaration().classModifier().get(0).getText();
        }

        System.out.println("Class: " + className + " Modifier: " + classMod);

        if (ctx.normalClassDeclaration().superclass() != null) {
            superClass = ctx.normalClassDeclaration().superclass().getChild(1).getText();
        }
        if (ctx.normalClassDeclaration().superinterfaces() != null) {
            implementClass = ctx.normalClassDeclaration().superinterfaces().interfaceTypeList().interfaceType();
        }
        currentClassID = "SC" + id;
        AST.scdb.createNodeRelationship(className, currentClassID, superClass,inheritanceType);
        id++;

//implementClass = ctx.normalClassDeclaration().superinterfaces().interfaceTypeList().interfaceType();

        //
        
        /*Element artefact  = WriteToXML.getDocument().createElement("Artefact");
        AST.artefacts.appendChild(artefact);
        Attr rootType = WriteToXML.getDocument().createAttribute("type");
        rootType.setValue("SourceCode");
        artefactElement.setAttributeNode(rootType);        
        */
        artefactElement = WriteToXML.getDocument().createElement("ArtefactElement");
        //AST.artefact.appendChild(artefactElement);
       // artefact.appendChild(artefactElement);

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
    }

}
