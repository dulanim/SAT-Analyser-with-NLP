package com.project.extendedsat.test;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.management.AttributeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CreateRelationFile {

	public static Document document;
	static String file;
	public CreateRelationFile(String Filename){
		this.file=Filename;
	}
	public static void createRelations(Map<String,ArrayList<AttributeList>> map){

		ArrayList<AttributeList> l1;
		ArrayList<AttributeList> l2;

		Set set = map.entrySet(); // Get a set of the entries
		Iterator i = set.iterator(); // Get an iterator



		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();



			/*Element sub = document.createElement("Relation");
			root.appendChild(sub);	*/

			l1 = (ArrayList<AttributeList>) ((Map.Entry) i.next()).getValue();
			l2 = (ArrayList<AttributeList>)((Map.Entry) i.next()).getValue();


			document = dBuilder.newDocument();
			Element root = document.createElement("Relations");
			document.appendChild(root);

			int count=1;
			for(int k=0;k<l1.size();k++){
				//System.out.println(l2.get(k));

				Element relation = document.createElement("Relation");
				root.appendChild(relation);

				Attr val = document.createAttribute("id");
				val.setValue(""+count);
				count++;
				relation.setAttributeNode(val);

				//System.out.println(l2.get(k).get(1).toString());
				Element SourceNode = document.createElement("SourceNode");
				SourceNode.appendChild(document.createTextNode(l1.get(k).get(1).toString()));
				relation.appendChild(SourceNode);
				AttributeList aa=l2.get(k);

				Element RelationPath = document.createElement("RelationPath");
				if(l2.get(k).get(1).toString().startsWith("S")){
					RelationPath.appendChild(document.createTextNode("SourceCodeToTestCases"));
				}else if (l2.get(k).get(1).toString().startsWith("D")){
					RelationPath.appendChild(document.createTextNode("UMLDiagramToTestCases"));
				}
				relation.appendChild(RelationPath);

				Element TargetNode = document.createElement("TargetNode");
				TargetNode.appendChild(document.createTextNode(l2.get(k).get(1).toString()));
				relation.appendChild(TargetNode);


			}


			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

			//transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			DOMSource source = new DOMSource(document);
			System.out.println(file);
			StreamResult target = new StreamResult(new File(file).getAbsolutePath());
			transformer.transform(source, target);



		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
}
