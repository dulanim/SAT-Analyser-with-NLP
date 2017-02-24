/**
 *Namesh Sanjitha
 *Jul 13, 2016
 *DockerTraceability
 *com.tns.docker
 */


package com.project.extendedsat.deployment.tns.docker;




import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;





public class DockerParser {

	private File file;
	private DockerFile dockerFile;
	public static final ArrayList<String> cmds;
	
	
	static{
		cmds = new ArrayList<String>();
		cmds.add( "FROM" );
		cmds.add( "EXPOSE" );
		cmds.add( "ADD" );
		cmds.add( "RUN" );
		cmds.add( "CMD" );
		cmds.add( "ENV" );
	}
	
	//constructors
	public DockerParser(){

	}
	
	public DockerParser( File file ){
		this.file = file;
		dockerFile = new DockerFile();
	}
	
	
	//getters and setters
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
	
	public DockerFile getDockerFile() {
		return dockerFile;
	}

	public void setDockerFile(DockerFile dockerFile) {
		this.dockerFile = dockerFile;
	}

	
	
	
	//read the docker file and store the content in memory
	public void readDockerFile() throws IOException {
		
		//creating scanner to read dockerfile
		Scanner scanner = new Scanner( file );			
		String line, words[], arg;
		
		while( scanner.hasNextLine() ){
			line = scanner.nextLine();
			words = line.split( " " );
			
			if( cmds.contains( words[0]) ){
				arg = new String();
				for( int i = 1; i < words.length; i++ ){
					arg += words[i];
					arg += " ";
				}
				dockerFile.addLine( words[0], arg );
			}
			
		}
		
		scanner.close();		
	}
	

	//create the xml file of the input docker file
	/*
	public void createXML() throws FileNotFoundException, IOException{
		
		//creating streams to create output file
		File outputFile = new File( "DockerOutput.xml" );
		XStream xStream = new XStream( new StaxDriver() );
		ObjectOutputStream os = xStream.createObjectOutputStream( new FileOutputStream( outputFile ) );
		xStream.autodetectAnnotations(true);
		
		//String xmlString = new String();
		for( int i = 0; i < dockerFile.getNumberOfLines(); i++ ){
			os.writeObject( dockerFile.getDirective(i) );
		}
		
		os.close();
	}
	*/
	
	
	
	
	
	public void createNewXML() throws IOException{
		
		//creating root element
		Element deploymentDiagram = new Element("deployment-diagram");
		Document doc = new Document( deploymentDiagram );
		//doc.setRootElement( deploymentDiagram );
		
		
		Element container = new Element( "container" );
		container.setAttribute( new Attribute("id", "C1"));
		container.setAttribute( new Attribute("base-container", dockerFile.getDirective(0).getArgument()));
		
		Element child;
		for( int i = 0; i < dockerFile.getNumberOfLines(); i++ ){
			if( dockerFile.getDirective(i).getCommand().equals("FROM") ){
				child = new Element("base-container");
				child.setAttribute("id", Integer.toString(i) );
				child.setText( dockerFile.getDirective(i).getArgument() );
				container.addContent( child );
			}
			else if( dockerFile.getDirective(i).getCommand().equals("ADD") ){
				child = new Element("file");
				child.setAttribute("id", Integer.toString(i) );
				child.setText( dockerFile.getDirective(i).getArgument() );
				container.addContent( child );
			}
			else if( dockerFile.getDirective(i).getCommand().equals("EXPOSE") ){
				child = new Element("open-port");
				child.setAttribute("id", Integer.toString(i) );
				child.setText( dockerFile.getDirective(i).getArgument() );
				container.addContent( child );
			}
			else if( dockerFile.getDirective(i).getCommand().equals("RUN") ){
				if( dockerFile.getDirective(i).getArgument().contains("apt-get install") ){
					child = new Element("software");
					child.setAttribute("id", Integer.toString(i) );
					String software = dockerFile.getDirective(i).getArgument().substring( 16 );
					child.setText( software );
					container.addContent( child );
				}
			}
			
		}
		doc.getRootElement().addContent( container );
		
		XMLOutputter xmlOutputter = new XMLOutputter();
		xmlOutputter.setFormat( Format.getPrettyFormat() );
		xmlOutputter.output( doc, new FileWriter("docker.xml") );
		
	}
	
	
	
	
	/////////////////////// Demo methods describing the functionality of jdom2 ////////
	public void demo(){
		
		try {

			Element company = new Element("company");
			Document doc = new Document(company);
			doc.setRootElement(company);

			Element staff = new Element("staff");
			staff.setAttribute(new Attribute("id", "1"));
			staff.addContent(new Element("firstname").setText("yong"));
			staff.addContent(new Element("lastname").setText("mook kim"));
			staff.addContent(new Element("nickname").setText("mkyong"));
			staff.addContent(new Element("salary").setText("199999"));

			doc.getRootElement().addContent(staff);

			Element staff2 = new Element("staff");
			staff2.setAttribute(new Attribute("id", "2"));
			staff2.addContent(new Element("firstname").setText("low"));
			staff2.addContent(new Element("lastname").setText("yin fong"));
			staff2.addContent(new Element("nickname").setText("fong fong"));
			staff2.addContent(new Element("salary").setText("188888"));

			doc.getRootElement().addContent(staff2);

			// new XMLOutputter().output(doc, System.out);
			XMLOutputter xmlOutput = new XMLOutputter();

			// display nice nice
			xmlOutput.setFormat(Format.getPrettyFormat());
			xmlOutput.output(doc, new FileWriter("c:\\file.xml"));

			System.out.println("File Saved!");
		  } catch (IOException io) {
			System.out.println(io.getMessage());
		  }
		
	}
	
	
	
	public void demo2(){
		try{
			//root element
			Element carsElement = new Element("cars");
			Document doc = new Document(carsElement);			

			//supercars element
			Element supercarElement = new Element("supercars");
			supercarElement.setAttribute(new Attribute("company","Ferrari"));

			//supercars element
			Element carElement1 = new Element("carname");
			carElement1.setAttribute(new Attribute("type","formula one"));
			carElement1.setText("Ferrari 101");

			Element carElement2 = new Element("carname");
			carElement2.setAttribute(new Attribute("type","sports"));
			carElement2.setText("Ferrari 202");

			supercarElement.addContent(carElement1);
			supercarElement.addContent(carElement2);

			doc.getRootElement().addContent(supercarElement);

			XMLOutputter xmlOutput = new XMLOutputter();

			// display ml
			xmlOutput.setFormat(Format.getPrettyFormat());
			xmlOutput.output(doc, System.out); 
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	
	
	
	
	
	
	public static void main( String args[] ) throws FileNotFoundException, IOException{
		
		
		DockerParser docParse = new DockerParser( new File("DockerFile") );
		
		
		docParse.readDockerFile();
		docParse.getDockerFile().print();
		
		//docParse.createXML();
		docParse.createNewXML();
		//docParse.demo2();
		
	}
	
	
	
	
}




