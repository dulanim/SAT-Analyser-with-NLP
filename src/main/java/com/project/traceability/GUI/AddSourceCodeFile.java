/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.GUI;

import static com.project.traceability.GUI.NewFileWindow.shell;
import static com.project.traceability.GUI.NewProjectWindow.projectPath;
import static com.project.traceability.GUI.NewProjectWindow.shell;
import static com.project.traceability.GUI.NewProjectWindow.trtmNewTreeitem;
import com.project.traceability.SourceCodeToXML.AST;
import com.project.traceability.common.PropertyFile;
import com.project.traceability.manager.RelationManager;
import java.io.File;
import java.io.FilenameFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 *
 * @author AARTHIKA
 */
public class AddSourceCodeFile {

    public static Shell shell;
    private Text text;
    public static String projectPath = null;
    public static TreeItem trtmNewTreeitem;
    private File fileSet[];
    private String directory;

    /**
     * Launch the application.
     *
     * @param args
     * @wbp.parser.entryPoint
     */
    public static void main(String[] args) {
        try {
            AddSourceCodeFile window = new AddSourceCodeFile();
            window.open();
            window.eventLoop(Display.getDefault());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eventLoop(Display display) {
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }

    /**
     * Open the window.
     */
    public Shell open() {
        Display display = Display.getDefault();
        createContents();
        shell.open();
        shell.layout();
        return shell;
    }

    /**
     * Create contents of the window.
     */
    protected void createContents() {
        shell = new Shell();
        shell.setSize(450, 300);
        shell.setText("New Project");

        center(shell);

        Label lblProjectName = new Label(shell, SWT.NONE);
        lblProjectName.setBounds(10, 32, 110, 20);
        lblProjectName.setText("Add Your Project:");

        // Create the text box extra wide to show long paths
        final Text text = new Text(shell, SWT.BORDER);
        text.setBounds(10, 62, 300, 25);

        Button button = new Button(shell, SWT.PUSH);
        button.setText("Browse...");
        button.setBounds(330, 62, 51, 25);
        button.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent event) {
                DirectoryDialog dlg = new DirectoryDialog(shell);

                // Set the initial filter path according
                // to anything they've selected or typed in
                dlg.setFilterPath(text.getText());

                // Change the title bar text
                dlg.setText("SWT's DirectoryDialog");

                // Customizable message displayed in the dialog
                dlg.setMessage("Select a directory");

                // Calling open() will open and run the dialog.
                // It will return the selected directory, or
                // null if user cancels
                String dir = dlg.open();
                if (dir != null) {
                    // Set the text box to the new selection
                    text.setText(dir);

                    directory = dir;

                }
            }
        });

        Button btnNewButton = new Button(shell, SWT.SAVE);
        btnNewButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                /*String projectName = text.getText();
                 projectPath = PropertyFile.filePath + projectName + "/";
                 PropertyFile.setProjectName(projectName);
                 PropertyFile.setGraphDbPath(projectPath + projectName + ".graphdb");
                 PropertyFile.setGeneratedGexfFilePath(projectPath + projectName + ".gexf");
                 PropertyFile.setRelationshipXMLPath(projectPath + "Relations.xml");

                 shell.close();
                 HomeGUI.shell.setText("SAT- " + projectName);
                 HomeGUI.newTab.setVisible(true);
                 HomeGUI.tree.setVisible(true);

                 trtmNewTreeitem = new TreeItem(HomeGUI.tree, SWT.NONE);
                 trtmNewTreeitem.setText(projectName);

                 File file = new File(projectPath);
                 file.mkdir();
                 RelationManager.createXML(projectPath);*/
                fileSet = finder(directory);
                for (int i = 0; i < fileSet.length; i++) {
                    System.out.println("A:" + fileSet[i].getName());
                }
                Label lblFile = new Label(shell, SWT.NONE);
                lblFile.setBounds(10, 90, 300, 25);
                lblFile.setText("Files Found:");
                for (int i = 0; i < fileSet.length; i++) {
                    new AST().convertFileToXML(directory + "\\" + fileSet[i].getName());
                    
                    Label lblFileAdd = new Label(shell, SWT.NONE);
                    lblFileAdd.setBounds(10, (90+25*i), 300, 25);
                    lblFileAdd.setText("-"+fileSet[i].getName());
                }

                shell.close();
            }
        });
        btnNewButton.setBounds(295, 227, 51, 25);
        btnNewButton.setText("Create");

        Button btnNewButton_1 = new Button(shell, SWT.NONE);
        btnNewButton_1.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                shell.close();
            }
        });
        btnNewButton_1.setBounds(359, 227, 65, 25);
        btnNewButton_1.setText("Cancel");

        /*text = new Text(shell, SWT.BORDER);
         text.setBounds(97, 26, 316, 21);*/
    }

    public void center(Shell shell) {

        Rectangle bds = shell.getDisplay().getBounds();

        Point p = shell.getSize();
        shell.setFullScreen(true);
        int nLeft = (bds.width - p.x) / 2;
        int nTop = (bds.height - p.y) / 2;

        shell.setBounds(nLeft, nTop, p.x, p.y);
    }

    public File[] finder(String dirName) {
        File dir = new File(dirName);

        return dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".java");
            }
        });

    }

}
