package com.project.traceability.GUI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Group;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class CreateNewProjectWindow extends Composite {

	private Text txtMlearningweb;
	private Text txtEusershiyamalanrquirementdocs;
	private Text txtEusershiyamalanumldiagrammdjOrUmldiagramuml;
	private Text txtEbscsemfinalYearProjectandurilsrcmainjava;
	protected Composite shell;
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
	    try {
	    	
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CreateNewProjectWindow(Composite parent, int style) {
		
		super(parent, style);
		createContents(parent,style);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	

		/**
		 * Open the window.
		 */
		public void open() {
			Display display = new Display();
		    final Shell shell = new Shell(display);
		    shell.setSize(300, 300);
		    
		    shell.setText("Composite Example");

		    final Composite composite = new Composite(shell, SWT.NONE);
		    createContents(composite,SWT.NONE);
		}
		
		/**
		 * create the contents.
		 */
		public void createContents(Composite parent, int style){
			
			
			Group grpImport = new Group(this, SWT.NONE);
			grpImport.setText("Import Required Files");
			grpImport.setBounds(10, 110, 468, 157);
			
			Button btnNewButton = new Button(grpImport, SWT.NONE);
			btnNewButton.setBounds(390, 30, 75, 25);
			btnNewButton.setText("Browse");
			
			Label lblUmlFile = new Label(grpImport, SWT.NONE);
			lblUmlFile.setBounds(3, 74, 97, 21);
			lblUmlFile.setText("UML File");
			
			Label lblProjectFiles = new Label(grpImport, SWT.NONE);
			lblProjectFiles.setBounds(3, 115, 80, 21);
			lblProjectFiles.setText("Project Files");
			
			txtEusershiyamalanrquirementdocs = new Text(grpImport, SWT.BORDER);
			txtEusershiyamalanrquirementdocs.setEditable(false);
			txtEusershiyamalanrquirementdocs.setBounds(101, 32, 283, 21);
			txtEusershiyamalanrquirementdocs.setText("E:\\User\\Shiyamalan\\rquirement.docs");
			
			txtEusershiyamalanumldiagrammdjOrUmldiagramuml = new Text(grpImport, SWT.BORDER);
			txtEusershiyamalanumldiagrammdjOrUmldiagramuml.setEditable(false);
			txtEusershiyamalanumldiagrammdjOrUmldiagramuml.setBounds(101, 71, 282, 21);
			txtEusershiyamalanumldiagrammdjOrUmldiagramuml.setText("E:\\User\\Shiyamalan\\diagram.mdj or diagram.uml");
			
			txtEbscsemfinalYearProjectandurilsrcmainjava = new Text(grpImport, SWT.BORDER);
			txtEbscsemfinalYearProjectandurilsrcmainjava.setEditable(false);
			txtEbscsemfinalYearProjectandurilsrcmainjava.setBounds(101, 112, 283, 21);
			txtEbscsemfinalYearProjectandurilsrcmainjava.setText("E:\\Final Year Project\\Anduril\\src\\main\\java");
			
			Button btnBrowse = new Button(grpImport, SWT.NONE);
			btnBrowse.setBounds(390, 69, 75, 25);
			btnBrowse.setText("Browse");
			
			Button btnBrowse_1 = new Button(grpImport, SWT.NONE);
			btnBrowse_1.setBounds(390, 110, 75, 25);
			btnBrowse_1.setText("Browse");
			
			Label lblRequirementFile = new Label(grpImport, SWT.NONE);
			lblRequirementFile.setBounds(3, 35, 97, 21);
			lblRequirementFile.setText("Requirement File");
			
			Label lblProjectName = new Label(this, SWT.NONE);
			lblProjectName.setBounds(10, 43, 80, 21);
			lblProjectName.setText("Project Name");
			
			txtMlearningweb = new Text(this, SWT.BORDER);
			txtMlearningweb.setBackground(SWTResourceManager.getColor(SWT.COLOR_GRAY));
			txtMlearningweb.setText("MLearning-web");
			txtMlearningweb.setBounds(108, 40, 282, 21);
			
			Button btnCancel = new Button(this, SWT.NONE);
			btnCancel.setBounds(7, 301, 75, 25);
			btnCancel.setText("Cancel");
			
			Button btnCreate = new Button(this, SWT.NONE);
			btnCreate.setBounds(400, 301, 75, 25);
			btnCreate.setText("Finish");

		}
}
