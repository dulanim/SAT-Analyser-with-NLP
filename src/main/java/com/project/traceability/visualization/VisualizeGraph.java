package com.project.traceability.visualization;

import com.project.traceability.GUI.HomeGUI;
import static com.project.traceability.GUI.HomeGUI.projectPath;
import static com.project.traceability.GUI.HomeGUI.table;
import static com.project.traceability.GUI.HomeGUI.tblclmnProperty;
import static com.project.traceability.GUI.HomeGUI.tbtmPropertyInfos;
import com.project.traceability.common.PropertyFile;
import com.project.traceability.manager.ReadXML;
import static com.project.traceability.manager.ReadXML.transferDataToDBFromXML;
import static com.project.traceability.visualization.GraphMouseListener.nodeData;
import static com.project.traceability.visualization.GraphMouseListener.tableCursor;
import static com.project.traceability.visualization.GraphMouseListener.tblclmnValue;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.filters.api.FilterController;
import org.gephi.filters.api.Query;
import org.gephi.filters.plugin.partition.PartitionBuilder.EdgePartitionFilter;
import org.gephi.filters.plugin.partition.PartitionBuilder.NodePartitionFilter;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.GraphView;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.layout.plugin.AutoLayout;
import org.gephi.layout.plugin.force.StepDisplacement;
import org.gephi.layout.plugin.force.yifanHu.YifanHuLayout;
import org.gephi.layout.plugin.forceAtlas2.ForceAtlas2;
import org.gephi.layout.plugin.labelAdjust.LabelAdjust;
import org.gephi.partition.api.EdgePartition;
import org.gephi.partition.api.NodePartition;
import org.gephi.partition.api.PartitionController;
import org.gephi.partition.plugin.EdgeColorTransformer;
import org.gephi.partition.plugin.NodeColorTransformer;
import org.gephi.preview.api.PreviewController;
import org.gephi.preview.api.PreviewModel;
import org.gephi.preview.api.PreviewProperty;
import org.gephi.preview.api.ProcessingTarget;
import org.gephi.preview.api.RenderTarget;
import org.gephi.preview.types.DependantColor;
import org.gephi.preview.types.DependantOriginalColor;
import org.gephi.preview.types.EdgeColor;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import processing.core.PApplet;

/**
 * Model to add and visualize generated graph file (Traceability link
 * visualization).
 *
 * @author Thanu
 *
 */
public class VisualizeGraph {

    private String graphType;
    private PreviewController previewController;
    private GraphModel graphModel;
    private PApplet applet;
    private CTabItem tabItem;
    private Composite composite, composite2, composite3;
    private ProcessingTarget target;
    private Frame frame, frame2;
    private static VisualizeGraph visual = new VisualizeGraph();

    private VisualizeGraph() {
    }

    public static VisualizeGraph getInstance() {
        return visual;
    }

    public PreviewController getPreviewController() {
        return previewController;
    }

    public void setPreviewController(PreviewController previewController) {
        this.previewController = previewController;
    }

    public GraphModel getGraphModel() {
        return graphModel;
    }

    public void setGraphModel(GraphModel graphModel) {
        this.graphModel = graphModel;
    }

    public String getGraphType() {
        return graphType;
    }

    public PApplet getApplet() {
        return applet;
    }

    public void setApplet(PApplet applet) {
        this.applet = applet;
    }

    public CTabItem getTabItem() {
        return tabItem;
    }

    public void setTabItem(CTabItem tabItem) {
        this.tabItem = tabItem;
    }

    public Composite getComposite() {
        return composite;
    }

    public void setComposite(Composite composite) {
        this.composite = composite;
    }

    public Frame getFrame() {
        return frame;
    }

    public Frame getFrame2() {
        return frame2;
    }

    public void setFrame(Frame frame) {
        this.frame = frame;
    }

    /**
     * Method to get Gephi Processing Target
     *
     * @return ProcessingTarget
     */
    public ProcessingTarget getTarget() {
        // New Processing target, get the PApplet
        ProcessingTarget target = (ProcessingTarget) previewController
                .getRenderTarget(RenderTarget.PROCESSING_TARGET);
        this.setApplet(target.getApplet());
        applet.init();

        try {
            Thread.sleep(10000);//wait for 10ms to render graph properly in tool
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        }

        // Refresh the preview and reset the zoom
        previewController.render(target);
        target.refresh();
        target.resetZoom();

        return target;
    }

    /**
     * Method to set Gephi Processing Target
     *
     * @param target ProcessingTarget
     */
    public void setTarget(ProcessingTarget target) {
        this.target = target;
    }

    /**
     * Method to set graph type which is going to be displayed
     *
     * @param graphType String
     */
    public void setGraphType(String graphType) {
        this.graphType = graphType;
        AttributeModel attributeModel = Lookup.getDefault()
                .lookup(AttributeController.class).getModel();
        FilterController filterController = Lookup.getDefault().lookup(
                FilterController.class);
        PartitionController partitionController = Lookup.getDefault().lookup(
                PartitionController.class);

        // See if graph is well imported
        DirectedGraph graph = graphModel.getDirectedGraph();

        // Partition with 'type' column, which is in the data
        NodePartition node_partition = (NodePartition) partitionController
                .buildPartition(
                        attributeModel.getNodeTable().getColumn("Type"), graph);

        // Partition with 'Neo4j Relationship Type' column, which is in the data
        EdgePartition edge_partition = (EdgePartition) partitionController
                .buildPartition(
                        attributeModel.getEdgeTable().getColumn(
                                "Neo4j Relationship Type"), graph);
        EdgePartitionFilter edgeFilter = new EdgePartitionFilter(
                edge_partition);
        edgeFilter.unselectAll();

        if (graphType.equalsIgnoreCase("Full Graph")) { // visualize the entire project artefact element overview
        } else if (graphType.equalsIgnoreCase(
                "Class")) {// visualize "Class" type artefact element view
            NodePartitionFilter classFilter = new NodePartitionFilter(
                    node_partition);
            classFilter.unselectAll();
            classFilter.addPart(node_partition.getPartFromValue("Class"));//set filter to "Class" type artefact elements
            classFilter.addPart(node_partition.getPartFromValue("Functional"));//set filter to "Functional" type artefact elements
            Query class_query = filterController.createQuery(classFilter);//filter "Class" and "Functinal" type artefact elements
            GraphView class_view = filterController.filter(class_query);//set graph view to class cluster view
            graphModel.setVisibleView(class_view);//set graph model to class view
        } else if (graphType.equalsIgnoreCase(
                "Attributes")) {// visualize attributes artefact element from source and UML

            NodePartitionFilter attributeFilter = new NodePartitionFilter(
                    node_partition);
            attributeFilter.unselectAll();
            attributeFilter.addPart(node_partition
                    .getPartFromValue("UMLAttribute"));//set filter to "UMLAttribute" type artefact elements
            attributeFilter.addPart(node_partition.getPartFromValue("Field"));//set filter to "Field" type artefact elements
            Query attribute_query = filterController
                    .createQuery(attributeFilter);//filter "Field" and "UMLAttribute" type artefact elements
            GraphView attribute_view = filterController.filter(attribute_query);//set graph view to attributes cluster view
            graphModel.setVisibleView(attribute_view);//set graph model to attribute view
        } else if (graphType.equalsIgnoreCase(
                "Methods")) {// visualize methods artefact element from source and UML
            NodePartitionFilter methodFilter = new NodePartitionFilter(
                    node_partition);
            methodFilter.unselectAll();
            methodFilter.addPart(node_partition.getPartFromValue("Method"));//set filter to "Method" type artefact elements
            methodFilter.addPart(node_partition
                    .getPartFromValue("UMLOperation"));//set filter to "UMLOperation" type artefact elements
            Query method_query = filterController.createQuery(methodFilter);//filter "Method" and "UMLOperation" type artefact elements
            GraphView method_view = filterController.filter(method_query);//set graph view to methods cluster view
            graphModel.setVisibleView(method_view);//set graph model to method view
        } else {//then user asked to get rel cluter view

            for (GraphDB.RelTypes type : GraphDB.RelTypes.values()) {
                if (type.getValue().equalsIgnoreCase(graphType)) {
                    edgeFilter.addPart(edge_partition
                            .getPartFromValue(type.name()));//set filter to specified rel type
                    Query edge_query = filterController.createQuery(edgeFilter);//filter to specified rel type
                    GraphView edge_view = filterController.filter(edge_query);//set graph view to specified rel type cluster view
                    graphModel.setVisibleView(edge_view);//set graph model to specified rel type cluster view
                    break;
                }
            }

        }
        //partion nodes using their type & color them according to the partition
        NodeColorTransformer nodeColorTransformer = new NodeColorTransformer();
        nodeColorTransformer.randomizeColors(node_partition);
        partitionController.transform(node_partition, nodeColorTransformer);

        //partion edges using their type & color them according to the partition
        EdgeColorTransformer edgeColorTransformer = new EdgeColorTransformer();
        edgeColorTransformer.randomizeColors(edge_partition);
        partitionController.transform(edge_partition, edgeColorTransformer);
    }

    /**
     * Method to import graph file into Gephi Toolkit API workspace
     *
     */
    public void importFile() {
        // Init a project - and therefore a workspace
        ProjectController pc = Lookup.getDefault().lookup(
                ProjectController.class);
        pc.newProject();
        Workspace workspace = pc.getCurrentWorkspace();
        // Import file
        ImportController importController = Lookup.getDefault().lookup(
                ImportController.class);
        Container container;

        try {
            File file = new File(PropertyFile.getGeneratedGexfFilePath());
            container = importController.importFile(file);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }

        // Append imported data to GraphAPI
        importController.process(container,
                new DefaultProcessor(), workspace);
    }

    /**
     * Method to set Gephi preview properties
     *
     */
    public void setPreview() {
        // Preview configuration
        previewController = Lookup.getDefault().lookup(
                PreviewController.class);
        PreviewModel previewModel = previewController.getModel();

        previewModel.getProperties()
                .putValue(PreviewProperty.SHOW_NODE_LABELS,
                        Boolean.TRUE);
        previewModel.getProperties()
                .putValue(
                        PreviewProperty.NODE_LABEL_PROPORTIONAL_SIZE, Boolean.TRUE);
        previewModel.getProperties()
                .putValue(PreviewProperty.NODE_LABEL_COLOR,
                        new DependantOriginalColor(Color.BLACK));
        previewModel.getProperties()
                .putValue(
                        PreviewProperty.NODE_BORDER_WIDTH, 0.1f);
        Font f = previewModel.getProperties().getFontValue(
                PreviewProperty.NODE_LABEL_FONT);

        previewModel.getProperties()
                .putValue(PreviewProperty.NODE_LABEL_FONT,
                        f.deriveFont(Font.BOLD, f.getSize() - 6));
        previewModel.getProperties()
                .putValue(
                        PreviewProperty.NODE_BORDER_COLOR,
                        new DependantColor(DependantColor.Mode.PARENT));
        previewModel.getProperties()
                .putValue(PreviewProperty.NODE_OPACITY, 100);
        previewModel.getProperties()
                .putValue(PreviewProperty.EDGE_CURVED,
                        Boolean.FALSE);
        previewModel.getProperties()
                .putValue(PreviewProperty.EDGE_COLOR,
                        new EdgeColor(EdgeColor.Mode.ORIGINAL));
        previewModel.getProperties()
                .putValue(PreviewProperty.EDGE_OPACITY, 100);
        previewModel.getProperties()
                .putValue(PreviewProperty.EDGE_THICKNESS,
                        2.0);
        previewModel.getProperties()
                .putValue(PreviewProperty.EDGE_RADIUS, 0.9f);
        previewModel.getProperties()
                .putValue(PreviewProperty.SHOW_EDGE_LABELS,
                        Boolean.TRUE);
        previewModel.getProperties()
                .putValue(PreviewProperty.EDGE_LABEL_COLOR,
                        new DependantOriginalColor(Color.BLACK));
        f = previewModel.getProperties().getFontValue(
                PreviewProperty.EDGE_LABEL_FONT);

        previewModel.getProperties()
                .putValue(PreviewProperty.EDGE_LABEL_FONT,
                        f.deriveFont(Font.BOLD, f.getSize() - 3));
        previewModel.getProperties()
                .putValue(PreviewProperty.BACKGROUND_COLOR,
                        Color.LIGHT_GRAY);
        previewModel.getProperties()
                .putValue("GraphType", graphType);
        previewController.refreshPreview();
    }

    /**
     * Method to set Gephi preview layout option
     *
     */
    public void setLayout() {
        AutoLayout autoLayout = new AutoLayout(1, TimeUnit.SECONDS);
        autoLayout.setGraphModel(graphModel);
        YifanHuLayout firstLayout = new YifanHuLayout(null,
                new StepDisplacement(1f));
        ForceAtlas2 secondLayout = new ForceAtlas2(null);
        LabelAdjust thirdLayout = new LabelAdjust(null);
        AutoLayout.DynamicProperty adjustBySizeProperty = AutoLayout
                .createDynamicProperty("forceAtlas2.adjustSizes.name",
                        Boolean.TRUE, 0.0f);
        AutoLayout.DynamicProperty linLogModeProperty = AutoLayout
                .createDynamicProperty("forceAtlas2.linLogMode.name",
                        Boolean.TRUE, 0f);
        AutoLayout.DynamicProperty gravityProperty = AutoLayout
                .createDynamicProperty("forceAtlas2.gravity.name", 5d, 0f);
        AutoLayout.DynamicProperty scallingRatioProperty = AutoLayout
                .createDynamicProperty("forceAtlas2.scalingRatio.name", 20d, 0f);
        AutoLayout.DynamicProperty dissaudeHubsProperty = AutoLayout
                .createDynamicProperty(
                        "ForceAtlas2.distributedAttraction.name", Boolean.TRUE,
                        0f);
        autoLayout.addLayout(firstLayout, 0.4f);
        autoLayout.addLayout(secondLayout, 0.4f,
                new AutoLayout.DynamicProperty[]{adjustBySizeProperty,
                    linLogModeProperty, gravityProperty,
                    scallingRatioProperty, dissaudeHubsProperty});
        autoLayout.addLayout(thirdLayout, 0.2f);
        autoLayout.execute();
    }

    /**
     * Method to set graph model and type
     *
     * @param model
     * @param graphType
     */
    public void setGraph(GraphModel model, String graphType) {
        setGraphModel(model);
        setGraphType(graphType);
    }

    /**
     * Method to set graph type
     *
     * @param model
     */
    public void setGraph(GraphModel model) {
        AttributeModel attributeModel = Lookup.getDefault()
                .lookup(AttributeController.class).getModel();
        PartitionController partitionController = Lookup.getDefault().lookup(
                PartitionController.class);

        setGraphModel(model);
        // See if graph is well imported
        DirectedGraph graph = graphModel.getDirectedGraph();

        // Partition with 'type' column, which is in the data
        NodePartition node_partition = (NodePartition) partitionController
                .buildPartition(
                        attributeModel.getNodeTable().getColumn("Type"), graph);

        // Partition with 'Neo4j Relationship Type' column, which is in the data
        EdgePartition edge_partition = (EdgePartition) partitionController
                .buildPartition(
                        attributeModel.getEdgeTable().getColumn(
                                "Neo4j Relationship Type"), graph);
        //color nodes according to node partition
        NodeColorTransformer nodeColorTransformer = new NodeColorTransformer();
        nodeColorTransformer.randomizeColors(node_partition);
        partitionController.transform(node_partition, nodeColorTransformer);

        //color edges according to edge partition
        EdgeColorTransformer edgeColorTransformer = new EdgeColorTransformer();
        edgeColorTransformer.randomizeColors(edge_partition);
        partitionController.transform(edge_partition, edgeColorTransformer);
    }

    /**
     * Method to show graph in tool
     *
     */
    public void showGraph() {
        //try { Thread.sleep(500); } catch (Exception e) { }
        //Display.getDefault().syncExec(new Runnable() {
        //public void run() {
        HomeGUI.isComaparing = false;
        setPreview();
        setLayout();
        target = getTarget();

        HomeGUI.graphtabItem.setText(PropertyFile.getProjectName() + "-" + PropertyFile.getGraphType() + " View");
        composite = new Composite(HomeGUI.graphTab,
                SWT.EMBEDDED);
        composite.setLayout(new GridLayout(1, false));
        GridData spec = new GridData();
        spec.horizontalAlignment = GridData.FILL;
        spec.grabExcessHorizontalSpace = true;
        spec.verticalAlignment = GridData.FILL;
        spec.grabExcessVerticalSpace = true;
        composite.setLayoutData(spec);

        frame = SWT_AWT.new_Frame(composite);

        composite2 = new Composite(HomeGUI.propertyTab, SWT.NONE);
        composite2.setLayout(new GridLayout(1, false));

        composite3 = new Composite(composite2, SWT.RIGHT);
        composite3.setLayout(new FillLayout());

        table = new Table(composite2, SWT.BORDER | SWT.FULL_SELECTION);

        GridData data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.grabExcessHorizontalSpace = true;
        data.verticalAlignment = GridData.FILL;
        data.grabExcessVerticalSpace = true;

        table.setHeaderVisible(true);
        table.setLinesVisible(false);
        table.setLayoutData(data);

        /*
         this is first coloumn
         */
        tblclmnProperty = new TableColumn(table, SWT.FILL);
        tblclmnProperty.setWidth(100);
        tblclmnProperty.setText("Property");

        /*
         second coloumn
         */
        tblclmnValue = new TableColumn(table, SWT.FILL);
        tblclmnValue.setWidth(100);
        tblclmnValue.setText("Value");


        /*
         table holder for scrolling purpose
         */
        tableCursor = new TableCursor(table, SWT.NONE);

        final org.eclipse.swt.widgets.Button updateBtn = new org.eclipse.swt.widgets.Button(composite3, SWT.BORDER | SWT.PUSH | SWT.VERTICAL);
        updateBtn.setText("Update");

        final org.eclipse.swt.widgets.Button deleteBtn = new org.eclipse.swt.widgets.Button(composite3, SWT.BORDER | SWT.PUSH | SWT.VERTICAL);
        deleteBtn.setText("Delete");

        updateBtn.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent se) {
                GraphDBEdit gbEditor = new GraphDBEdit();
                System.out.println("Selected Upate :" + nodeData);
                gbEditor.storeUpdatedNode(nodeData);
                System.out.println("Heliio");
                transferDataToDBFromXML(projectPath, true);
                VisualizeGraph visual = VisualizeGraph.getInstance();
                visual.importFile();//import the generated graph file into Gephi toolkit API workspace
                GraphModel model = Lookup.getDefault().lookup(GraphController.class).getModel();// get graph model            
                visual.setGraph(model, PropertyFile.getGraphType());//set the graph type
                HomeGUI.isComaparing = false;
                visual.setPreview();
                visual.setLayout();
                HomeGUI.table.clearAll();
                HomeGUI.table.deselectAll();
                HomeGUI.table.removeAll();
                nodeData.clear();
                nodeData = new HashMap<>();
            }
        });

        deleteBtn.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent se) {
                GraphDBDelete gbDeletor = new GraphDBDelete();
                System.out.println("Selected Dele :" + nodeData);
                gbDeletor.deleteNodeAndRelations(nodeData);                
                System.out.println("Heliio");
                transferDataToDBFromXML(projectPath, false);

                VisualizeGraph visual = VisualizeGraph.getInstance();
                visual.importFile();//import the generated graph file into Gephi toolkit API workspace
                GraphModel model = Lookup.getDefault().lookup(GraphController.class).getModel();// get graph model            
                visual.setGraph(model, PropertyFile.getGraphType());//set the graph type
                HomeGUI.isComaparing = false;
                visual.setPreview();
                visual.setLayout();
                HomeGUI.table.clearAll();
                HomeGUI.table.deselectAll();
                HomeGUI.table.removeAll();
                nodeData.clear();
                nodeData = new HashMap<>();
            }

        });

        final TableEditor editor = new TableEditor(table);
        editor.horizontalAlignment = SWT.LEFT;
        editor.grabHorizontal = true;

        table.addListener(SWT.MouseDown, new Listener() {
            @Override
            public void handleEvent(Event event) {
                Rectangle clientArea = table.getClientArea();
                Point pt = new Point(event.x, event.y);
                int index = table.getTopIndex();
                while (index < table.getItemCount()) {
                    boolean visible = false;
                    final TableItem item = table.getItem(index);
                    item.setBackground(1, Display.getCurrent().getSystemColor(SWT.COLOR_GREEN));

                    Rectangle rect = item.getBounds(1);
                    if (rect.contains(pt)) {
                        final Text text = new Text(table, SWT.NONE);
                        Listener textListener = new Listener() {
                            @Override
                            public void handleEvent(Event e) {
                                switch (e.type) {
                                    case SWT.KeyUp:
                                        item.setText(1, text.getText());
                                        System.out.println("Replacing1: " + item.getText(0) + ":" + item.getText(1));
                                        nodeData.replace(item.getText(0), item.getText(1));
                                    case SWT.KeyDown:
                                        item.setText(1, text.getText());
                                        System.out.println("Replacing2: " + item.getText(0) + ":" + item.getText(1));
                                        nodeData.replace(item.getText(0), item.getText(1));
                                    case SWT.MouseEnter:
                                        item.setText(1, text.getText());
                                        System.out.println("Replacing3: " + item.getText(0) + ":" + item.getText(1));
                                        nodeData.replace(item.getText(0), item.getText(1));
                                    case SWT.MouseDown:
                                        item.setText(1, text.getText());
                                        System.out.println("Replacing4: " + item.getText(0) + ":" + item.getText(1));
                                        nodeData.replace(item.getText(0), item.getText(1));
                                    case SWT.FOCUSED:
                                        item.setBackground(1, Display.getCurrent().getSystemColor(SWT.COLOR_RED));

                                    case SWT.FocusOut:
                                        item.setBackground(1, Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY));
                                        item.setText(1, text.getText());
                                        System.out.println("Replacing5: " + item.getText(0) + ":" + item.getText(1));
                                        nodeData.replace(item.getText(0), item.getText(1));
                                        //System.out.println("Key: "+ item.getText(0)+" Value: "+item.getText(1));
                                        text.dispose();
                                        break;
                                    case SWT.Traverse:
                                        switch (e.detail) {
                                            case SWT.TRAVERSE_RETURN:
                                                item.setBackground(1, Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE));
                                                item.setText(1, text.getText());
                                            case SWT.TRAVERSE_ESCAPE:
                                                text.dispose();
                                                e.doit = false;
                                        }
                                        break;
                                }

                            }

                        };
                        text.addListener(SWT.FocusOut, textListener);
                        text.addListener(SWT.Traverse, textListener);
                        editor.setEditor(text, item, 1);
                        text.setText(item.getText(1));
                        text.selectAll();
                        text.setFocus();
                        return;
                    }
                    if (!visible && rect.intersects(clientArea)) {
                        visible = true;
                    }
                    if (!visible) {
                        return;
                    }
                    index++;
                }

            }

        });

        tbtmPropertyInfos.setControl(composite2);

        //add refresh button to graph panel
        Button refresh = new Button("Refresh");

        refresh.addActionListener(new ActionListener() {
            final String type = PropertyFile.getGraphType();

            @Override
            public void actionPerformed(ActionEvent e) {
                ReadXML.initApp(HomeGUI.projectPath, PropertyFile.graphType);
                /*VisualizeGraph visual = VisualizeGraph.getInstance();//PropertyFile.getVisual();
                        visual.importFile();
                        GraphModel model = Lookup.getDefault().lookup(GraphController.class).getModel();
                        visual.setGraph(model, PropertyFile.getGraphType());
                        visual.setPreview();
                        visual.setLayout();*/
            }
        });

        Panel btnPanel = new Panel();
        btnPanel.setLayout(new FlowLayout());
        btnPanel.setBackground(Color.LIGHT_GRAY);
        btnPanel.add(refresh);

        Panel panel = new Panel();
        panel.setLayout(new BorderLayout());
        panel.add(applet, BorderLayout.CENTER);
        panel.add(refresh, BorderLayout.PAGE_START);
        frame.add(panel);
        composite.setData(panel);
        HomeGUI.graphtabItem.setControl(composite);
        //set the table visible when the visualization is active

        frame.revalidate();
        // }
        //});

    }

}
