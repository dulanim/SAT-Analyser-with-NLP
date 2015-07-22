package com.project.traceability.GUI;

import java.awt.Dimension;
import java.util.ArrayList;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.project.traceability.common.PropertyFile;
import com.project.traceability.manager.EditManager;
import com.project.traceability.manager.ReadFiles;
import com.project.traceability.manager.RequirementSourceClassManager;
import com.project.traceability.manager.RequirementUMLClassManager;
import com.project.traceability.manager.UMLSourceClassManager;

public class CompareWindow1 {

    public static Shell shell;
    public static Dimension screen = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    public static TableViewer viewer;
    public static Table table;
    public static TabItem tabItem_1;
    public static TabItem tabItem_2;
    public static TabFolder tabFolder_1;
    public static TabFolder tabFolder_2;
    public static StyledText text_1;
    public static StyledText text_2;
    public static Composite composite_1;
    public static Composite composite_2;

    /**
     * Launch the application.
     * 
     * @param args
     */
    public static void main(String[] args) {
        try {
            CompareWindow1 window = new CompareWindow1();
            window.open("", new ArrayList<String>());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Open the window.
     */
    public void open(String project, ArrayList<String> selectedFiles) {
        final Display display = Display.getDefault();
        createContents(selectedFiles);

        Listener tableListener = new Listener() {

            Shell tooltip = null;
            Label label = null;

            public void handleEvent(Event event) {
                switch (event.type) {
                    case SWT.KeyDown:
                    case SWT.Dispose:
                    case SWT.MouseMove: {
                        if (tooltip == null) {
                            break;
                        }
                        tooltip.dispose();
                        tooltip = null;
                        label = null;
                        break;
                    }
                    case SWT.MouseHover: {
                        Point coords = new Point(event.x, event.y);
                        TableItem item = table.getItem(coords);
                        if (item != null) {
                            int columnCount = table.getColumnCount();
                            for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                                if (item.getBounds(columnIndex).contains(coords)) {
                                    /* Dispose of the old tooltip (if one exists */
                                    if (tooltip != null && !tooltip.isDisposed()) {
                                        tooltip.dispose();
                                    }

                                    /* Create a new Tooltip */
                                    if (item.getText(columnIndex) != ""
                                            && item.getText(columnIndex) != null) {
                                        tooltip = new Shell(table.getShell(),
                                                SWT.ON_TOP | SWT.NO_FOCUS
                                                | SWT.TOOL);
                                        tooltip.setBackground(table.getDisplay().getSystemColor(
                                                SWT.COLOR_INFO_BACKGROUND));
                                        FillLayout layout = new FillLayout();
                                        layout.marginWidth = 2;
                                        tooltip.setLayout(layout);

                                        label = new Label(tooltip, SWT.NONE);
                                        label.setForeground(table.getDisplay().getSystemColor(
                                                SWT.COLOR_INFO_FOREGROUND));
                                        label.setBackground(table.getDisplay().getSystemColor(
                                                SWT.COLOR_INFO_BACKGROUND));

                                        /*
                                         * Store the TableItem with the label so we
                                         * can pass the mouse event later
                                         */
                                        label.setData("_TableItem_", item);

                                        /* Set the tooltip text */

                                        label.setText(item.getData(Integer.toString(columnIndex)).toString());

                                        /* Set the size and position of the tooltip */
                                        Point size = tooltip.computeSize(
                                                SWT.DEFAULT, SWT.DEFAULT);
                                        Rectangle rect = item.getBounds(columnIndex);
                                        Point pt = table.toDisplay(rect.x, rect.y);
                                        tooltip.setBounds(pt.x, pt.y, size.x,
                                                size.y);

                                        tooltip.setVisible(true);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        };

        table.addListener(SWT.Dispose, tableListener);
        table.addListener(SWT.KeyDown, tableListener);
        table.addListener(SWT.MouseMove, tableListener);
        table.addListener(SWT.MouseHover, tableListener);

        compareFiles(project, selectedFiles);
        shell.open();
        shell.layout();
        center(shell);
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

    }

    private void compareFiles(String project, ArrayList<String> selectedFiles) {
        String filePath = PropertyFile.filePath + project + "\\";
        System.out.println(selectedFiles.size());
        if (selectedFiles.get(0).contains("UML")
                && selectedFiles.get(1).contains("Source")
                || selectedFiles.get(0).contains("Source")
                && selectedFiles.get(1).contains("UML")) {
            ReadFiles.readFiles(filePath);
            UMLSourceClassManager.compareClassNames(filePath);

        } else if (selectedFiles.get(0).contains("UML")
                && selectedFiles.get(1).contains("Requirement")
                || selectedFiles.get(0).contains("Requirement")
                && selectedFiles.get(1).contains("UML")) {
            ReadFiles.readFiles(filePath);
            RequirementUMLClassManager.compareClassNames(filePath);

        } else if (selectedFiles.get(0).contains("Source")
                && selectedFiles.get(1).contains("Requirement")
                || selectedFiles.get(0).contains("Requirement")
                && selectedFiles.get(1).contains("Source")) {
            ReadFiles.readFiles(filePath);
            RequirementSourceClassManager.compareClassNames(filePath);
        }
    }

    /**
     * Create contents of the window.
     */
    protected void createContents(ArrayList<String> files) {
        shell = new Shell();
        shell.setSize(450, 300);
        shell.setText("SWT Application");

        shell.setBounds(0, 0, screen.width, screen.height);

        Menu menu = new Menu(shell, SWT.BAR);
        shell.setMenuBar(menu);

        MenuItem mntmFile = new MenuItem(menu, SWT.NONE);
        mntmFile.setText("File");

        TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
        tabFolder.setBounds(0, 0, 683, 740);

        TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
        tabItem.setText("Compared Results");

        Composite composite = new Composite(tabFolder, SWT.NONE);
        tabItem.setControl(composite);

        table = new Table(composite, SWT.BORDER | SWT.FULL_SELECTION);
        table.setBounds(40, 31, 515, 621);
        table.setHeaderVisible(true);
        table.setLinesVisible(true);

        Menu men = new Menu(shell, SWT.POP_UP);
        table.setMenu(men);
        final MenuItem item = new MenuItem(men, SWT.PUSH);
        item.setText("Delete Selection");
        item.addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event event) {
                /*
                 * Point pt = new Point(event.x, event.y); int column = -1;
                 * TableItem tableItem = table.getItem(pt); for (int i = 0, n =
                 * table.getColumnCount(); i < n; i++) { Rectangle rect =
                 * tableItem.getBounds(i); if (rect.contains(pt)) { column = i;
                 * table.remove(table.getSelectionIndices()[column]); } }
                 */
                table.remove(table.getSelectionIndices()[2]);
            }
        });

        TableColumn tblclmnNewColumn = new TableColumn(table, SWT.NONE);
        tblclmnNewColumn.setWidth(149);
        tblclmnNewColumn.setText("Classes");

        TableColumn tblclmnNewColumn_1 = new TableColumn(table, SWT.NONE);
        tblclmnNewColumn_1.setWidth(173);
        tblclmnNewColumn_1.setText("Attributes");

        TableColumn tblclmnNewColumn_2 = new TableColumn(table, SWT.NONE);
        tblclmnNewColumn_2.setWidth(243);
        tblclmnNewColumn_2.setText("Methods");

        tabFolder_2 = new TabFolder(shell, SWT.NONE);
        tabFolder_2.setBounds(699, 403, 683, 337);

        tabItem_2 = new TabItem(tabFolder_2, SWT.NONE);
        tabItem_2.setText(files.get(0));

        tabFolder_1 = new TabFolder(shell, SWT.NONE);
        tabFolder_1.setBounds(699, 22, 651, 362);

        tabItem_1 = new TabItem(tabFolder_1, SWT.NONE);
        tabItem_1.setText(files.get(1));

        composite_1 = new Composite(tabFolder_1, SWT.NONE);
        composite_1.setLayout(new FillLayout());

        composite_2 = new Composite(tabFolder_2, SWT.NONE);
        composite_2.setLayout(new FillLayout());

        text_1 = new StyledText(composite_1, SWT.BORDER | SWT.MULTI
                | SWT.V_SCROLL | SWT.H_SCROLL);
        text_2 = new StyledText(composite_2, SWT.BORDER | SWT.MULTI
                | SWT.V_SCROLL | SWT.H_SCROLL);

        DragSource source = new DragSource(text_1, DND.DROP_COPY
                | DND.DROP_MOVE);
        source.setTransfer(new Transfer[]{TextTransfer.getInstance()});
        source.addDragListener(new DragSourceAdapter() {

            Point selection;

            @Override
            public void dragStart(DragSourceEvent e) {
                selection = text_1.getSelection();
                e.doit = selection.x != selection.y;
            }

            @Override
            public void dragSetData(DragSourceEvent e) {
                e.data = text_1.getText(selection.x, selection.y - 1);
            }

            @Override
            public void dragFinished(DragSourceEvent e) {
                text_1.replaceTextRange(selection.x, selection.y - selection.x,
                        "");
                if (e.detail == DND.DROP_MOVE) {
                    text_1.replaceTextRange(selection.x, selection.y
                            - selection.x, "");
                }
                selection = null;
            }
        });

        DropTarget target = new DropTarget(table, DND.DROP_DEFAULT
                | DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK);
        target.setTransfer(new Transfer[]{TextTransfer.getInstance()});
        target.addDropListener(new DropTargetAdapter() {

            @Override
            public void dragOperationChanged(DropTargetEvent e) {
                if (e.detail == DND.DROP_DEFAULT) {
                    e.detail = DND.DROP_COPY;
                }
            }

            @Override
            public void dragEnter(DropTargetEvent event) {
                if (event.detail == DND.DROP_DEFAULT) {
                    event.detail = (event.operations & DND.DROP_COPY) != 0 ? DND.DROP_COPY
                            : DND.DROP_NONE;
                }

                // Allow dropping text only
                for (int i = 0, n = event.dataTypes.length; i < n; i++) {
                    if (TextTransfer.getInstance().isSupportedType(
                            event.dataTypes[i])) {
                        event.currentDataType = event.dataTypes[i];
                    }
                }
            }

            public void dragOver(DropTargetEvent event) {
                event.feedback = DND.FEEDBACK_SELECT | DND.FEEDBACK_SCROLL;
            }

            @Override
            public void drop(DropTargetEvent event) {
                if (TextTransfer.getInstance().isSupportedType(
                        event.currentDataType)) {
                    // Get the dropped data
                    DropTarget target = (DropTarget) event.widget;
                    table = (Table) target.getControl();
                    final String data = (String) event.data;

                    Point pt = new Point(event.x, event.y - 100);
                    // Create a new item in the table to hold the dropped data
                    TableItem item = table.getItem(pt);
                    if (item != null) {
                        int column = -1;
                        for (int i = 0, n = table.getColumnCount(); i < n; i++) {
                            Rectangle rect = item.getBounds(i);
                            if (rect.contains(pt)) {
                                column = i;
                                try {
                                    item.setText(column, data);
                                    item.setData("" + column + "", "jasjbhj");
                                    if (column == 0) {
                                        TableItem tabbItem = new TableItem(
                                                table, SWT.NONE, table.indexOf(item) + 1);
                                        tabbItem.setText("");
                                    }
                                    if (table.getItem(table.indexOf(item) + 1).getText() != "") {
                                        TableItem tabbItem = new TableItem(
                                                table, SWT.NONE, table.indexOf(item) + 1);
                                        tabbItem.setText("");
                                        for (int j = table.indexOf(item) + 1; j >= 0; j--) {
                                            if (table.getItem(j).getText() != "") {
                                                
                                                break;
                                            }
                                        }
                                    }
                                    table.redraw();
                                } catch (Exception e) {
                                    System.out.println(e.toString());
                                }
                                break;
                            }
                        }
                    }
                }
            }
        });
    }

    public void center(Shell shell) {

        Rectangle bds = shell.getDisplay().getBounds();

        Point p = shell.getSize();
        shell.setFullScreen(true);
        int nLeft = (bds.width - p.x) / 2;
        int nTop = (bds.height - p.y) / 2;

        shell.setBounds(nLeft, nTop, p.x, p.y);
    }
}
