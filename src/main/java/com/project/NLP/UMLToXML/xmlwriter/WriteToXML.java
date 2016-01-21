/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.UMLToXML.xmlwriter;

/**
 *
 * @author shiyam
 */
import com.project.NLP.file.operations.FilePropertyName;
import java.io.File;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.project.traceability.staticdata.StaticData;
import com.project.traceability.GUI.HomeGUI;
import com.project.traceability.GUI.ProjectCreateWindow;
import com.project.traceability.model.Attribute;
import com.project.traceability.model.Dependencies;
import com.project.traceability.model.ModelData;
import com.project.traceability.model.Operation;
import com.project.traceability.model.Parameter;
import java.io.IOException;
import javax.swing.JOptionPane;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
public class WriteToXML {
    
   
    public static HashMap<String,String> keyIDMap = new HashMap<>();
    public int count = 0;
    public static String fileName = "";
    public static String type = "UMLDiagram";//can change when requirement file writing as xml format
    public void createXML(){
        List<ModelData> classLst = StaticData.classLst;
        //List<Dependencies> depencyList = StaticData.depencyList;
            try{
          
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		
				// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement(StaticData.ARTEFACTS_ROOT);
		Element artifact = doc.createElement(StaticData.ARTEFACT_ROOT);
		doc.appendChild(rootElement);
		rootElement.appendChild(artifact);
                
                Attr attrType = doc.createAttribute("type");
                attrType.setValue(type);
                artifact.setAttributeNode(attrType);
                for(int i=0;i<classLst.size();i++){
                    ModelData tempData = classLst.get(i);
                   
                    //putting AretefactElement
                    Element artifactElement = doc.createElement(StaticData.ARTEFACTELEMENT_ROOT);
                    artifact.appendChild(artifactElement);//append an artifact main root
                    
                    //class/Inteface Behavior is adding
                    Attr classNameAttr = doc.createAttribute(StaticData.NAME_ROOT);
                    classNameAttr.setValue(tempData.getName());
                    artifactElement.setAttributeNode(classNameAttr);
                    
                    Attr typeAttr = doc.createAttribute(StaticData.TYPE_ROOT);
                    typeAttr.setValue(tempData.getType());
                    artifactElement.setAttributeNode(typeAttr);
                    
                    Attr classIDAttr = doc.createAttribute(StaticData.ID_ROOT);
                    String id = getDesignElementID();
                    classIDAttr.setValue(id);
                    artifactElement.setAttributeNode(classIDAttr);
                    
                    keyIDMap.put(tempData.getId(), id);
                    
                    //class/Interface Behavior finished
                    
                   
                    //Attribute Element is adding to ArtifactSubElement
                    createAttributeElement(doc,artifactElement,tempData);
                    //Attribute Element added
                    
                    //Operation Element is adding to ArtifactSubElement
                    createMethodElement(doc,artifactElement,tempData);
                    //Operation Element Added
                   
                     //putting INTRACONNECTION
                }
                
                Element intraConnectionElement = doc.createElement(StaticData.INTRACONNECTION_ROOT);
                artifact.appendChild(intraConnectionElement);//append an artifact main Artifact
                
                //Connection Element is adding to InterConnectionsElement
                createConnectionElement(doc,intraConnectionElement);
                
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");//No I18N
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");//No I18N
		DOMSource source = new DOMSource(doc);
                //String driveLetter = getSuitableDrive();
                

                File xmlFile = new File(getFileDir());
                
                if(!xmlFile.exists())
                    xmlFile.createNewFile();
                StreamResult result = new StreamResult(xmlFile.getPath());

                transformer.transform(source, result);

                System.out.println("XML File saved @ " + xmlFile.getPath());

	  } catch(ParserConfigurationException | TransformerException |IOException pce) {
		
                MessageBox box = new MessageBox(ProjectCreateWindow.shell,
                SWT.ERROR);
                box.setText("File Not Found Error");
                box.setMessage(pce.toString());
                box.open();
                
	  }
	}
        
    
    private String getFileDir(){
        String dir = System.getProperty("user.home") + File.separator + "temp.xml";//default
        
        try{
            String root = HomeGUI.tree.getToolTipText() + File.separator + 
                            ProjectCreateWindow.projectName;
            File f = new File(root + File.separator +FilePropertyName.XML);
            if(!f.exists())
                    f.mkdir();
            if(type.equals("Requirement")){
                
                dir = f.getPath() + File.separator + 
                        FilePropertyName.REQUIREMENT_ARTIFACT_NAME;
            }else{
                dir = f.getPath() + File.separator + 
                        FilePropertyName.UML_ARTIFACT_NAME;
            }
        }catch(Exception e){
            
        }
        return dir;
    }
    private String getDesignElementID() {
        String ID;
        if(type.equals("Requirement")){
            count++;
             ID = "R".concat(Integer.toString(count));
            
        }else{
            count++;
            ID = "D".concat(Integer.toString(count));
            
        }
        return ID;
    }

    private void createAttributeElement(Document doc,Element artifactElement, ModelData tempData) {
        try{
            
            List<Attribute> attributeLst = tempData.getAttributeList();
            for(int i=0;i<attributeLst.size();i++){
                Attribute attribute = attributeLst.get(i);
                 //putting AretefactSubElement
                    Element artifactSubElement = doc.createElement(StaticData.ARTEFACTUBELEMENT_ROOT);
                    artifactElement.appendChild(artifactSubElement);//append an artifact main root
                    
                    Attr attrName = doc.createAttribute(StaticData.NAME_ROOT);
                    attrName.setValue(attribute.getName());
                    artifactSubElement.setAttributeNode(attrName);
                    
                    Attr attrType = doc.createAttribute(StaticData.TYPE_ROOT);
                    attrType.setValue(attribute.getType());
                    artifactSubElement.setAttributeNode(attrType);
                    
                    Attr attrVisibility = doc.createAttribute(StaticData.VISIBILITY_ROOT);
                    attrVisibility.setValue(attribute.getVisibility());
                    artifactSubElement.setAttributeNode(attrVisibility);
                    
                    Attr attrID = doc.createAttribute(StaticData.ID_ROOT);
                    attrID.setValue(getDesignElementID());
                    artifactSubElement.setAttributeNode(attrID);
                    
                    Attr attrVariableType = doc.createAttribute(StaticData.VARIABLE_TYPE_ROOT);
                    attrVariableType.setValue(attribute.getDataType());
                    artifactSubElement.setAttributeNode(attrVariableType);
            }
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void createMethodElement(Document doc, Element artifactElement, ModelData tempData) {
        try{
            
            List<Operation> mtdList = tempData.getOperationList();
            for(int i=0;i<mtdList.size();i++){
                Operation operation = mtdList.get(i);
                //putting AretefactSubElement
                Element artifactSubElement = doc.createElement(StaticData.ARTEFACTUBELEMENT_ROOT);
                artifactElement.appendChild(artifactSubElement);//append an artifactSubElement main of ArtifactElement
                
                 Attr attrName = doc.createAttribute(StaticData.NAME_ROOT);
                 attrName.setValue(operation.getName());
                 artifactSubElement.setAttributeNode(attrName);
                 
                 Attr attrType = doc.createAttribute(StaticData.TYPE_ROOT);
                 attrType.setValue(operation.getType());
                 artifactSubElement.setAttributeNode(attrType);
                    
                 Attr attrVisibility = doc.createAttribute(StaticData.VISIBILITY_ROOT);
                 attrVisibility.setValue(operation.getVisibility());
                 artifactSubElement.setAttributeNode(attrVisibility);
                    
                 Attr attrID = doc.createAttribute(StaticData.ID_ROOT);
                 attrID.setValue(getDesignElementID());
                 artifactSubElement.setAttributeNode(attrID);
                 
                 Attr attrReturnType = doc.createAttribute(StaticData.RETURN_TYPE_ROOT);
                 attrReturnType.setValue(operation.getReturnType());
                 artifactSubElement.setAttributeNode(attrReturnType);
                 
                 List<Parameter> paramList = operation.getParameterList();
                 Attr attrParameters = doc.createAttribute(StaticData.PARAMETER_ROOT);
                 String parameterString = "";
                 for(int j=0;paramList !=null && j<paramList.size();j++){
                     Parameter param = paramList.get(j);
                     parameterString += param.getParameterName().concat(":").concat(param.getParameterType());
                     
                     if(j != paramList.size()-1)
                     parameterString += ", ";
                 }
                 attrParameters.setValue(parameterString);
                 artifactSubElement.setAttributeNode(attrParameters);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private void createConnectionElement(Document doc, Element intraConnectionElement) {
        
        try{
            
            List<Dependencies> dependenciesesLst = StaticData.depencyList;
            for(int i=0;dependenciesesLst != null  && i<dependenciesesLst.size();i++){
                Dependencies dependencies = dependenciesesLst.get(i);
                //putting AretefactSubElement
                Element connectionElement = doc.createElement(StaticData.CONNECTION_ROOT);
                intraConnectionElement.appendChild(connectionElement);//append an artifact main root

		Element typeElement = doc.createElement(StaticData.TYPE_CONNECTION_ROOT);
		typeElement.appendChild(doc.createTextNode(dependencies.getDependency_type()));
                connectionElement.appendChild(typeElement);
                
                Element startPonintElement = doc.createElement(StaticData.STARTPOINT_ROOT);
                String id = getCurrentDesignId(dependencies.getSource_id());
                startPonintElement.appendChild(doc.createTextNode(id));
		connectionElement.appendChild(startPonintElement);
                
                Element multiplicitySrcElement = doc.createElement(StaticData.MULTIPLICITY_ROOT);
		multiplicitySrcElement.appendChild(
                        doc.createTextNode(getNomilizedString(dependencies.getMuliplicity_src())));
		connectionElement.appendChild(multiplicitySrcElement);
                
                Element endPonintElement = doc.createElement(StaticData.ENDPOINT_ROOT);
                id = getCurrentDesignId(dependencies.getTaget_id());
                endPonintElement.appendChild(doc.createTextNode(id));
		connectionElement.appendChild(endPonintElement);
                
                Element multiplicityTargetElement = doc.createElement(StaticData.MULTIPLICITY_ROOT);
		String textTarget = getNomilizedString(dependencies.getMultiplicity_target());
                
                multiplicityTargetElement.appendChild(doc.createTextNode(textTarget));
		connectionElement.appendChild(multiplicityTargetElement);
                
                Element annotationElement = doc.createElement(StaticData.ANNOTATION_ROOT);
                String textAnnotation = getNomilizedString(dependencies.getAnnotation());
		annotationElement.appendChild(doc.createTextNode(textAnnotation));
		connectionElement.appendChild(annotationElement);

            }
            
            
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    
    public String getCurrentDesignId(String reference){
        
        return keyIDMap.get(reference);
    }
    
    public String getNomilizedString(String temp){
        
        String result = "";
        
        if( temp == null || temp.isEmpty() || temp.equals("null")
                ||temp.equals("data")){
            return result;
        }else{
            return temp;
        }
    }

  /*  private String getSuitableDrive() {
                List <File>files = Arrays.asList(File.listRoots());
                Map<String,String> driveLetterMap = new HashMap<String,String>();
                  for (File f1 : files) {
                    String s1 = FileSystemView.getFileSystemView().getSystemDisplayName (f1);
                    String s2 = FileSystemView.getFileSystemView().getSystemTypeDescription(f1);
                    System.out.println("getSystemDisplayName : " + s1);
                    System.out.println("getSystemTypeDescription : " + s2);
                   
                   if(s2.equals("Local Disk")){
                     //  AccessController.checkPermission(new FilePermission("/tmp", "read,write"));
                       String driveLetter = s1.split(" ")[2].substring(1,3);
                       File f = new File(driveLetter.concat("\\\\"));
                        if(f.canWrite()) {
                          // write access
                            return driveLetter; 
                        }
                   }
                  }
                 return "D:\\";//default
    }*/
}
