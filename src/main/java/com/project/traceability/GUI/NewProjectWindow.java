package com.project.traceability.GUI;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

import com.project.traceability.common.PropertyFile;
import com.project.traceability.visualization.GraphDB;
import com.project.traceability.visualization.GraphDB.RelTypes;
import com.project.traceability.manager.ReadXML;
import com.project.traceability.manager.RelationManager;
import java.util.ArrayList;
import java.util.List;

public class NewProjectWindow {

    public static Shell shell;
    private Text text;
    public static String projectPath = null;
    public static TreeItem trtmNewTreeitem;

    /**
     * Launch the application.
     *
     * @param args
     * @wbp.parser.entryPoint
     */
    public static void main(String[] args) {
        try {
            NewProjectWindow window = new NewProjectWindow();
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
        lblProjectName.setBounds(10, 32, 81, 15);
        lblProjectName.setText("Project Name :");

        Button btnNewButton = new Button(shell, SWT.SAVE);
        btnNewButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String projectName = text.getText();
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
                RelationManager.createXML(projectPath);
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

        text = new Text(shell, SWT.BORDER);
        text.setBounds(97, 26, 316, 21);

    }

    /*public static void addPopUpMenu() {
        Menu popupMenu = new Menu(HomeGUI.tree);
        MenuItem newItem = new MenuItem(popupMenu, SWT.CASCADE);
        newItem.setText("New");

        MenuItem graphItem = new MenuItem(popupMenu, SWT.CASCADE);
        graphItem.setText("Visualization");

        MenuItem refreshItem = new MenuItem(popupMenu, SWT.NONE);
        refreshItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                HomeGUI.composite.pack(true);
            }
        });
        refreshItem.setText("Refresh");

        MenuItem deleteItem = new MenuItem(popupMenu, SWT.NONE);
        deleteItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                deleteFiles(projectPath);
            }
        });
        deleteItem.setText("Delete");

        MenuItem compareItem = new MenuItem(popupMenu, SWT.NONE);
        compareItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                FileSelectionWindow window = new FileSelectionWindow();
                String string = "";
                TreeItem[] selection = HomeGUI.tree.getSelection();
                for (int i = 0; i < selection.length; i++) {
                    string += selection[i] + " ";
                }
                System.out.println("********" + string + "***********");
                string = string.substring(10, string.length() - 2);
                window.open(string);
            }
        });
        compareItem.setText("Compare Files");

        Menu newMenu = new Menu(popupMenu);
        newItem.setMenu(newMenu);

        MenuItem fileItem = new MenuItem(newMenu, SWT.NONE);
        fileItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                File file = new File(projectPath);
                if (!file.isDirectory()) {
                    TreeItem parent = NewProjectWindow.trtmNewTreeitem
                            .getParentItem();
                    NewProjectWindow.trtmNewTreeitem = NewProjectWindow.trtmNewTreeitem
                            .getParentItem();
                    projectPath = PropertyFile.filePath + parent.getText();
                }
                NewFileWindow newFileWin = new NewFileWindow();
                newFileWin.open();
            }
        });
        fileItem.setText("File");

        Menu visualMenu = new Menu(popupMenu);
        graphItem.setMenu(visualMenu);

        final MenuItem allItem = new MenuItem(visualMenu, SWT.NONE);
        allItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String projectName = trtmNewTreeitem.getText();
                PropertyFile.setProjectName(projectName);
                PropertyFile.setGraphDbPath(projectPath + projectName + ".graphdb");
                PropertyFile.setGeneratedGexfFilePath(projectPath + projectName + ".gexf");
                PropertyFile.setRelationshipXMLPath(projectPath + "Relations.xml");
                PropertyFile.setGraphType(allItem.getText());
                System.out.println("Path: " + projectPath);
                System.out.println("DB Path: " + PropertyFile.graphDbPath);
                System.out.println("Graph Type: " + PropertyFile.graphType);
                ReadXML.initApp(projectPath, allItem.getText());
            }
        });
        allItem.setText("Full Graph");

        final MenuItem edgeItem = new MenuItem(visualMenu, SWT.CASCADE);
        edgeItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String projectName = trtmNewTreeitem.getText();
                PropertyFile.setProjectName(projectName);
                PropertyFile.setGraphDbPath(projectPath + projectName + ".graphdb");
                PropertyFile.setGeneratedGexfFilePath(projectPath + projectName + ".gexf");
                PropertyFile.setRelationshipXMLPath(projectPath + "Relations.xml");
                PropertyFile.setGraphType(edgeItem.getText());
                System.out.println("Path: " + projectPath);
                System.out.println("DB Path: " + PropertyFile.graphDbPath);
                System.out.println("Graph Type: " + PropertyFile.graphType);
                ReadXML.initApp(projectPath, edgeItem.getText());
            }
        });
        edgeItem.setText("Edge Filtered");
        
        Menu edgeMenu = new Menu(popupMenu);
        edgeItem.setMenu(edgeMenu);
        List<MenuItem> menuItem = new ArrayList<MenuItem>();
        int i = 0;
        for (RelTypes type : GraphDB.RelTypes.values()) {
            menuItem.add(new MenuItem(edgeMenu, SWT.NONE));
            final MenuItem item = menuItem.get(i);
            
            item.addSelectionListener(new SelectionAdapter() {
                @Override
                public void widgetSelected(SelectionEvent e) {
                    String projectName = trtmNewTreeitem.getText();
                    PropertyFile.setProjectName(projectName);
                    PropertyFile.setGraphDbPath(projectPath + projectName + ".graphdb");
                    PropertyFile.setGeneratedGexfFilePath(projectPath + projectName + ".gexf");
                    PropertyFile.setRelationshipXMLPath(projectPath + "Relations.xml");
                    PropertyFile.setGraphType(item.getText());
                    System.out.println("Path: " + projectPath);
                    System.out.println("DB Path: " + PropertyFile.graphDbPath);
                    System.out.println("Graph Type: " + PropertyFile.graphType);
                    ReadXML.initApp(projectPath, item.getText());
                }
            });
            menuItem.get(i).setText(type.getValue());
            i++;
        }

        final MenuItem nodeItem = new MenuItem(visualMenu, SWT.CASCADE);
        nodeItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String projectName = trtmNewTreeitem.getText();
                PropertyFile.setProjectName(projectName);
                PropertyFile.setGraphDbPath(projectPath + projectName + ".graphdb");
                PropertyFile.setGeneratedGexfFilePath(projectPath + projectName + ".gexf");
                PropertyFile.setRelationshipXMLPath(projectPath + "Relations.xml");
                PropertyFile.setGraphType(nodeItem.getText());
                System.out.println("Path: " + projectPath);
                System.out.println("DB Path: " + PropertyFile.graphDbPath);
                System.out.println("Graph Type: " + PropertyFile.graphType);
                ReadXML.initApp(projectPath, nodeItem.getText());
            }
        });
        nodeItem.setText("Node Filtered");

        Menu nodeMenu = new Menu(popupMenu);
        nodeItem.setMenu(nodeMenu);

        final MenuItem classItem = new MenuItem(nodeMenu, SWT.NONE);
        classItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String projectName = trtmNewTreeitem.getText();
                PropertyFile.setProjectName(projectName);
                PropertyFile.setGraphDbPath(projectPath + projectName + ".graphdb");
                PropertyFile.setGeneratedGexfFilePath(projectPath + projectName + ".gexf");
                PropertyFile.setRelationshipXMLPath(projectPath + "Relations.xml");
                PropertyFile.setGraphType(classItem.getText());
                System.out.println("Path: " + projectPath);
                System.out.println("DB Path: " + PropertyFile.graphDbPath);
                System.out.println("Graph Type: " + PropertyFile.graphType);
                ReadXML.initApp(projectPath, classItem.getText());
            }
        });
        classItem.setText("Class");

        final MenuItem attributeItem = new MenuItem(nodeMenu, SWT.NONE);
        attributeItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String projectName = trtmNewTreeitem.getText();
                PropertyFile.setProjectName(projectName);
                PropertyFile.setGraphDbPath(projectPath + projectName + ".graphdb");
                PropertyFile.setGeneratedGexfFilePath(projectPath + projectName + ".gexf");
                PropertyFile.setRelationshipXMLPath(projectPath + "Relations.xml");
                PropertyFile.setGraphType(attributeItem.getText());
                System.out.println("Graph Type: " + PropertyFile.graphType);
                ReadXML.initApp(projectPath, attributeItem.getText());
            }
        });
        attributeItem.setText("Attributes");

        final MenuItem methodItem = new MenuItem(nodeMenu, SWT.NONE);
        methodItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String projectName = trtmNewTreeitem.getText();
                PropertyFile.setProjectName(projectName);
                PropertyFile.setGraphDbPath(projectPath + projectName + ".graphdb");
                PropertyFile.setGeneratedGexfFilePath(projectPath + projectName + ".gexf");
                PropertyFile.setRelationshipXMLPath(projectPath + "Relations.xml");
                PropertyFile.setGraphType(methodItem.getText());
                System.out.println("Path: " + projectPath);
                System.out.println("DB Path: " + PropertyFile.graphDbPath);
                System.out.println("Graph Type: " + PropertyFile.graphType);
                ReadXML.initApp(projectPath, methodItem.getText());
            }
        });
        methodItem.setText("Methods");

        HomeGUI.tree.setMenu(popupMenu);

    }*/

    public void center(Shell shell) {

        Rectangle bds = shell.getDisplay().getBounds();

        Point p = shell.getSize();
        shell.setFullScreen(true);
        int nLeft = (bds.width - p.x) / 2;
        int nTop = (bds.height - p.y) / 2;

        shell.setBounds(nLeft, nTop, p.x, p.y);
    }

    /*public static void deleteFiles(String projectPath) {

        MessageBox messageBox = new MessageBox(HomeGUI.shell, SWT.ICON_QUESTION
                | SWT.YES | SWT.NO);
        File filePath = new File(projectPath);
        if (!filePath.isDirectory()) {
            TreeItem parent = NewProjectWindow.trtmNewTreeitem
                    .getParentItem();
            projectPath = PropertyFile.filePath
                    + parent.getText() + "/" + NewProjectWindow.trtmNewTreeitem.getText();
        }

        messageBox.setMessage("Do you really want to delete " + projectPath
                + " ?");
        messageBox.setText("Deleting " + projectPath);
        int response = messageBox.open();
        if (response == SWT.YES) {
            File file = new File(projectPath);
            String[] files = file.list();
            if (files != null) {
                for (String stringFile : files) {
                    File deleteFile = new File(projectPath + stringFile);
                    deleteFile.delete();
                }
            }
            file.delete();
        }
    }*/
}
