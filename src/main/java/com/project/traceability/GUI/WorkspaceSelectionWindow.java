package com.project.traceability.GUI;

import com.project.property.config.xml.reader.XMLReader;
import com.project.property.config.xml.writer.XMLWriter;
import com.project.traceability.common.Dimension;
import com.project.traceability.staticdata.StaticData;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
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
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            WorkspaceSelectionWindow window = new WorkspaceSelectionWindow();
            window.open();
            window.eventLoop(Display.getDefault());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Shell getShell(){
    	
    	/*
    	 * for testing purpose only
    	 * 
    	 */
    	return shell;
    }
    /**
     * Open the window.
     */
    public void open() {
        createContents();
        shell.open();
        shell.layout();
        
    }
    
    public void eventLoop(Display display) {
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
        //grpWorkspacePath.setText("workspace Path");
        grpWorkspacePath.setBounds(10, 76, 565, 185);

        Label lblWork = new Label(grpWorkspacePath, SWT.NONE);
        lblWork.setBounds(3, 34, 118, 29);
        lblWork.setText("Workspace Path");

        combo = new Combo(grpWorkspacePath, SWT.NONE);
        combo.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                selectItem();
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
                String wrkspce = directory.open();
                if (wrkspce != null) {
                    String path = wrkspce;
                    File file = new File(path);
                    if (file.isDirectory()) {
                        XMLWriter writer = XMLWriter.getXMLWriterInstance();
                        writer.createWorkspaceNode(path, Boolean.toString(false));
                        createContentData();
                        workspacePath = path;
                        setSelectedItem(path);
                    }
                } else {
                    System.out.println("No Selection ");
                    shell.setActive();
                }
            }

            private void setSelectedItem(String workspacePath) {
                // TODO Auto-generated method stub

                String items[] = combo.getItems();
                List<String> itemList = Arrays.asList(items);

                if (itemList.contains(workspacePath)) {
                    int pos = itemList.indexOf(workspacePath);
                    combo.select(pos);
                    selectItem();
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
        lblSelectAWorkspace.setText("Select a Workspace ath");

        Label lblItKkepsPyour = new Label(shell, SWT.NONE);
        lblItKkepsPyour.setBounds(10, 46, 565, 24);
        lblItKkepsPyour.setText("It keeps your created internally. When you select folder, folder should empty");

        Button btnCancel = new Button(shell, SWT.PUSH);
        btnCancel.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                shell.dispose();
            }
        });
        btnCancel.setBounds(370, 275, 91, 29);
        btnCancel.setText("Cancel");

        btnOk = new Button(shell, SWT.PUSH);
        btnOk.setEnabled(false);
        btnOk.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {

                String path = workspacePath;
                File file = new File(path);
                if (file.isDirectory()) {
                    XMLWriter writer = XMLWriter.getXMLWriterInstance();
                    writer.modifyStatus(btnCheckBox.getSelection(), path);
                    StaticData.workspace = path;
                    //System.out.println("Worspace new- "+StaticData.workspace);
                    writer.changeCurrnntWorkspaceVale(path);//set running workspace value
                    //home/shiyam/project/wrkspace
                    shell.dispose();
                    HomeGUI.main(null);
                } else {
                    JOptionPane.showMessageDialog(null, "It is not directory", "Error ", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnOk.setBounds(467, 275, 91, 29);
        btnOk.setText("Ok");

        createContentData();

    }

    protected void selectItem() {
        // select combo item and enable ok button 
        int index = combo.getSelectionIndex();
        ArrayList<String> pathList = new ArrayList<>(StaticData.paths);
        if (index >= 0) {
            workspacePath = pathList.get(index);
        } else {
            workspacePath = "";
        }
        btnOk.setEnabled(true);
    }

    private void createContentData() {

        XMLReader reader = new XMLReader();
        reader.readWorkspaces();
        combo.removeAll();
        //read workspaces and add to jComboWorkspaces
        Iterator<String> iterator = StaticData.paths.iterator();

        while (iterator.hasNext()) {
            combo.add(iterator.next().toString());
        }
    }
    
   
}
