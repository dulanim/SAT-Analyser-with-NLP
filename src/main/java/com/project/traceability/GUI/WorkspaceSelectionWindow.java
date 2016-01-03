package com.project.traceability.GUI;

import com.project.property.config.xml.reader.XMLReader;
import com.project.property.config.xml.writer.XMLWriter;
import com.project.traceability.GUI.HomeGUI;
import com.project.traceability.common.Dimension;
import com.project.traceability.staticdata.StaticData;
import java.awt.JobAttributes;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.JOptionPane;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.DirectoryDialog;


public class WorkspaceSelectionWindow {

	protected Shell shell;
	Button btnOk;
	String path;
        String workspacePath = "";
        Combo combo;
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			WorkspaceSelectionWindow window = new WorkspaceSelectionWindow();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
 
     
	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
                	shell.setSize(584, 346);
		shell.setText("SAT Workspace Selection Window");
		
		
                Dimension.toCenter(shell);
               
		Group grpWorkspacePath = new Group(shell, SWT.NONE);
		grpWorkspacePath.setText("workspace Path");
                grpWorkspacePath.setBounds(10, 76, 565, 185);
		
		Label lblWork = new Label(grpWorkspacePath, SWT.NONE);
		lblWork.setBounds(3, 34, 118, 29);
		lblWork.setText("Workspace Path");
		
                combo = new Combo(grpWorkspacePath, SWT.NONE);
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = combo.getSelectionIndex();
                                ArrayList<String> pathList = 
                                            new ArrayList<String>(StaticData.paths);
			        if(index >= 0){
			            workspacePath = pathList.get(index);
			        }else{
			            workspacePath = "";
			        }
			        btnOk.setEnabled(true);
			}
                        @Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
                
                combo.setBounds(121, 34, 345, 29);
		Button btnBrowse = new Button(grpWorkspacePath, SWT.NONE);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				 // TODO add your handling code here:
                        DirectoryDialog directory = new DirectoryDialog(shell);
		        directory.setFilterPath((System.getProperty("user.home")));
		        directory.setMessage("Select Your Workspace");
		        //
		        // disable the "All files" option.
		        //
		        // 
                        String wrkspce= directory.open();
		        if (!wrkspce.equals("")) {
		              String path = wrkspce;
		              File file = new File(path);
		              if(file.isDirectory()){
		                  XMLWriter writer =XMLWriter.getXMLWriterInstance();
		                  writer.createWorkspaceNode(path, Boolean.toString(false));
		                  createContentData();
		                  workspacePath = path;
		              }
		          }else {
                            System.out.println("No Selection ");
		          }
			}
		});
		btnBrowse.setBounds(472, 34, 91, 29);
		btnBrowse.setText("Browse");
		
		final Button btnCheckBox = new Button(grpWorkspacePath, SWT.CHECK);
		btnCheckBox.setBounds(3, 95, 443, 24);
		btnCheckBox.setText("Use this as default workspace. Do not ask again");
		
		
		
		Label lblSelectAWorkspace = new Label(shell, SWT.NONE);
		lblSelectAWorkspace.setBounds(10, 10, 302, 17);
		lblSelectAWorkspace.setText("Select a Workspace path");
		
		Label lblItKkepsPyour = new Label(shell, SWT.NONE);
		lblItKkepsPyour.setBounds(10, 46, 565, 24);
		lblItKkepsPyour.setText("It keeps your created Internally. when you select folder, folder should empty");
		
		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		btnCancel.setBounds(390, 284, 91, 29);
		btnCancel.setText("cancel");
		
		btnOk = new Button(shell, SWT.NONE);
		btnOk.setEnabled(false);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
			      String path = workspacePath;
                              File file = new File(path);
                                if(file.isDirectory()){
                                    XMLWriter writer = XMLWriter.getXMLWriterInstance();
                                    writer.modifyStatus(btnCheckBox.getSelection(), path);
                                    StaticData.workspace = path;
                                    writer.changeCurrnntWorkspaceVale(path);//set running workspace value
                                    //home/shiyam/project/wrkspace
                                    shell.dispose();
                                    HomeGUI.main(null);
                                }else{
                                    JOptionPane.showMessageDialog(null, "It is not directory", "Error ", JOptionPane.ERROR_MESSAGE);
                                }
			}
		});
		btnOk.setBounds(487, 284, 91, 29);
		btnOk.setText("Ok");
                
                
                createContentData();
               

	}
        
        private void createContentData(){
            
             XMLReader reader = new XMLReader();
             reader.readWorkspaces();
             combo.removeAll();
             //read workspaces and add to jComboWorkspaces
             Iterator<String> iterator = StaticData.paths.iterator();
             
             while(iterator.hasNext()){
                combo.add(iterator.next().toString());
            }
        }
}
