package com.project.traceability.visualization;

import com.project.NLP.SourceCodeToXML.WriteToXML;
import com.project.NLP.file.operations.FilePropertyName;
import com.project.traceability.GUI.HomeGUI;
import static com.project.traceability.GUI.HomeGUI.projectPath;
import static com.project.traceability.GUI.HomeGUI.table;
import static com.project.traceability.GUI.HomeGUI.tblclmnProperty;
import static com.project.traceability.GUI.HomeGUI.tbtmPropertyInfos;
import com.project.traceability.common.PropertyFile;
import com.project.traceability.manager.ReadXML;
import static com.project.traceability.manager.ReadXML.transferDataToDBFromXML;
import com.project.traceability.staticdata.StaticData;
import static com.project.traceability.visualization.GraphMouseListener.id;
import static com.project.traceability.visualization.GraphMouseListener.nodeData;
import static com.project.traceability.visualization.GraphMouseListener.tblclmnValue;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.filters.api.FilterController;
import org.gephi.filters.api.Query;
import org.gephi.filters.plugin.graph.EgoBuilder.EgoFilter;
import org.gephi.filters.plugin.partition.PartitionBuilder.EdgePartitionFilter;
import org.gephi.filters.plugin.partition.PartitionBuilder.NodePartitionFilter;
import org.gephi.graph.api.DirectedGraph;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.GraphView;
import org.gephi.graph.api.Node;
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
import org.gephi.partition.api.Part;
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
import org.openide.util.Lookup;
import processing.core.PApplet;
import scala.collection.mutable.HashTable;

/**
 * Model to add and visualize generated graph file (Traceability link
 * visualization).
 *
 * @author Thanu
 * @author Aarthika <>
 */
public class VisualizeGraph {

    private String graphType;
    private PreviewController previewController;
    private GraphModel graphModel;
    private PApplet applet;
    private CTabItem tabItem;
    private static Composite composite, composite2, composite3, composite4;
    private ProcessingTarget target;
    private Frame frame, frame2;
    private static VisualizeGraph visual = new VisualizeGraph();
    private static HashMap<String, org.eclipse.swt.graphics.Color> edgeColoring = new HashMap<>();
    static Menu popupMenu, insertLinkPopUp, removeLinkPopUp;
    static MenuItem removeLink, newLink;
    String nodeSelected = "", artefactSelected = "", typeSelected = "";
    static Composite comp;
    static JFrame newLinkFrame = null;
    static JFrame frameRemoval = null;
    JPanel removePanel = null;
    final static JPanel newLinkPanel = new JPanel();
    final static JComboBox artefactCombo = new JComboBox();
    final static JLabel lblArtefact = new JLabel("End Node Artefact:");
    final static JLabel lblType = new JLabel("End Node Type:");
    static DefaultComboBoxModel modelArtefact = new DefaultComboBoxModel();
    static DefaultComboBoxModel model = new DefaultComboBoxModel();
    final static JComboBox typeCombo = new JComboBox();
    final static JLabel lblNode = new JLabel("End Node ID:");
    static DefaultComboBoxModel modelNode = new DefaultComboBoxModel();
    final static JComboBox nodeCombo = new JComboBox();
    final static JPanel panelButton = new JPanel();
    ArrayList<Edge> edgesList = new ArrayList();
    final static JButton btnCreate = new JButton("Create");
    final static JButton btnCancel = new JButton("Cancel");
    final static JScrollPane scrPane = new JScrollPane(newLinkPanel);
    final static JPanel panelRemoveButton = new JPanel();
    final static JButton btnRemoveDelete = new JButton("Delete");
    final static JButton btnRemoveCancel = new JButton("Cancel");
    //final static JScrollPane scrRemovePane = new JScrollPane(removePanel);
    static ArrayList<Node> nodes = new ArrayList<>();
    static ArrayList<String> ids = new ArrayList<>();
    static ArrayList<Node> finalNodes = new ArrayList<>();
    static ArrayList<Node> nodeRelations = new ArrayList<>();
    ArrayList<JCheckBox> relIStrings = new ArrayList();
    static CheckBoxList cbList = new CheckBoxList();
    static boolean enter = true;

    static org.eclipse.swt.widgets.Button updateBtn = null;
    static org.eclipse.swt.widgets.Button deleteBtn = null;
    static org.eclipse.swt.widgets.Button impactBtn = null;
    static org.eclipse.swt.widgets.Button undoBtn = null;

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
     * @param waitTime
     * @return ProcessingTarget
     */
    public ProcessingTarget getTarget(int waitTime) {
        // New Processing target, get the PApplet
        ProcessingTarget target = (ProcessingTarget) previewController
                .getRenderTarget(RenderTarget.PROCESSING_TARGET);
        this.setApplet(target.getApplet());
        applet.init();
        if (waitTime <= 0) {
            waitTime = 100;
        }
        try {
            Thread.sleep(waitTime);//wait for 10ms to render graph properly in tool
        } catch (InterruptedException ex) {
            getTarget(waitTime * 10);
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
                        attributeModel.getNodeTable().getColumn("Artefact"), graph);

        // Partition with 'Neo4j Relationship Type' column, which is in the data
        EdgePartition edge_partition = (EdgePartition) partitionController
                .buildPartition(
                        attributeModel.getEdgeTable().getColumn(
                                "Neo4j Relationship Type"), graph);
        EdgePartitionFilter edgeFilter = new EdgePartitionFilter(
                edge_partition);
        edgeFilter.unselectAll();

        if (graphType.equalsIgnoreCase("Full Graph")) { // visualize the entire project artefact element overview
        } else if (graphType.equalsIgnoreCase("Req-UML-Modified Source View")) {

        } else if (graphType.equalsIgnoreCase(
                "Requirement")) {// visualize "Class" type artefact element view
            NodePartitionFilter classFilter = new NodePartitionFilter(
                    node_partition);
            classFilter.unselectAll();

            classFilter.addPart(node_partition.getPartFromValue("Requirement"));//set filter to "Class" type artefact elements
            Query class_query = filterController.createQuery(classFilter);//filter "Class" and "Functinal" type artefact elements
            GraphView class_view = filterController.filter(class_query);//set graph view to class cluster view
            graphModel.setVisibleView(class_view);//set graph model to class view
        } else if (graphType.equalsIgnoreCase(
                "Source")) {// visualize attributes artefact element from source and UML

            NodePartitionFilter classFilter = new NodePartitionFilter(
                    node_partition);
            classFilter.unselectAll();

            classFilter.addPart(node_partition.getPartFromValue("Source"));//set filter to "Class" type artefact elements
            Query class_query = filterController.createQuery(classFilter);//filter "Class" and "Functinal" type artefact elements
            GraphView class_view = filterController.filter(class_query);//set graph view to class cluster view
            graphModel.setVisibleView(class_view);//set graph model to class view
        } else if (graphType.equalsIgnoreCase(
                "Diagram")) {// visualize methods artefact element from source and UML
            NodePartitionFilter classFilter = new NodePartitionFilter(
                    node_partition);
            classFilter.unselectAll();

            classFilter.addPart(node_partition.getPartFromValue("Diagram"));//set filter to "Class" type artefact elements
            Query class_query = filterController.createQuery(classFilter);//filter "Class" and "Functinal" type artefact elements
            GraphView class_view = filterController.filter(class_query);//set graph view to class cluster view
            graphModel.setVisibleView(class_view);//set graph model to class view
        } else {//then user asked to get rel cluter view

            for (GraphDB.RelTypes type : GraphDB.RelTypes.values()) {
                if (type.getValue().equalsIgnoreCase(graphType)) {
                    edgeFilter.unselectAll();
                    edgeFilter.addPart(edge_partition.getPartFromValue(type.name()));//set filter to specified rel type
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
            JOptionPane.showMessageDialog(null, "Failed to import file. Reload the graph.", "Gexf File Import", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Append imported data to GraphAPI
        importController.process(container,
                new DefaultProcessor(), workspace);
    }

    /**
     * Method to set Gephi preview properties
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
        /*previewModel.getProperties()
                .putValue(PreviewProperty.SHOW_EDGE_LABELS,
                        Boolean.TRUE);*/
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
        NodePartition node_partition = null;

        // Partition with 'Neo4j Relationship Type' column, which is in the data
        EdgePartition edge_partition = null;

        edge_partition = (EdgePartition) partitionController
                .buildPartition(
                        attributeModel.getEdgeTable().getColumn(
                                "Neo4j Relationship Type"), graph);

        /*for(AttributeColumn a : attributeModel.getEdgeTable().getColumns()){
            System.out.println(""+a.getTitle());
        }*/
        List<Color> colors = addColors();
        int i = 0;
        /**/
        HashMap<String, Color> colorStatus = new HashMap<>();
        colorStatus.put(StaticData.DEFAULT_STATUS, Color.GREEN);
        colorStatus.put(StaticData.ADDED_STATUS, Color.BLUE);
        colorStatus.put(StaticData.DELETED_STATUS, Color.RED);
        colorStatus.put(StaticData.MODIFIED_STATUS, Color.ORANGE);

        if (WriteToXML.isTragging.equalsIgnoreCase("Tragging")) {
            node_partition = (NodePartition) partitionController
                    .buildPartition(
                            attributeModel.getNodeTable().getColumn("Status"), graph);
            edge_partition = (EdgePartition) partitionController
                    .buildPartition(
                            attributeModel.getEdgeTable().getColumn(
                                    "Status"), graph);
        } else {
            node_partition = (NodePartition) partitionController
                    .buildPartition(
                            attributeModel.getNodeTable().getColumn("Artefact"), graph);
            edge_partition = (EdgePartition) partitionController
                    .buildPartition(
                            attributeModel.getEdgeTable().getColumn(
                                    "Neo4j Relationship Type"), graph);
        }

        //color nodes according to node partition
        NodeColorTransformer nodeColorTransformer = new NodeColorTransformer();
        for (Part p : node_partition.getParts()) {
            if (WriteToXML.isTragging.equalsIgnoreCase("Tragging")) {
                if (p.getValue() != null) {
                    nodeColorTransformer.getMap().put(p.getValue(), colorStatus.get(p.getValue().toString()));
                }
            } else {
                nodeColorTransformer.randomizeColors(node_partition);
            }            
        }        
        partitionController.transform(node_partition, nodeColorTransformer);

        //color edges according to edge partition
        EdgeColorTransformer edgeColorTransformer = new EdgeColorTransformer();

        for (Part p : edge_partition.getParts()) {
            if (WriteToXML.isTragging.equalsIgnoreCase("Tragging")) {
                if (p.getValue() != null) {
                    edgeColorTransformer.getMap().put(p.getValue(), colorStatus.get(p.getValue().toString()));
                    org.eclipse.swt.graphics.Color cl = getSWTColor(colorStatus.get(p.getValue().toString()), p);
                    edgeColoring.put(p.getValue().toString(), cl);                    
                }

            } else {
                org.eclipse.swt.graphics.Color cl = getSWTColor(colors.get(i), p);
                edgeColoring.put(p.getValue().toString(), cl);
                edgeColorTransformer.getMap().put(p.getValue(), colors.get(i));
                i++;
                if (i == colors.size()) {
                    i = 0;
                }
            }

        }
        partitionController.transform(edge_partition, edgeColorTransformer);
    }

    public org.eclipse.swt.graphics.Color getSWTColor(Color color, Part p) {
        int green = color.getGreen();
        int blue = color.getBlue();
        int red = color.getRed();
        org.eclipse.swt.graphics.Color cl = new org.eclipse.swt.graphics.Color(Display.getCurrent(), red, green, blue);
        return cl;
    }

    /**
     * List specific colors to identify the edges.
     *
     * @return
     */
    public List<Color> addColors() {
        List<Color> colors = new ArrayList<>();
        colors.add(Color.RED);
        colors.add(Color.PINK);
        colors.add(Color.BLUE);
        colors.add(Color.MAGENTA);
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        colors.add(Color.CYAN);
        colors.add(Color.orange);
        colors.add(Color.RED);
        colors.add(Color.PINK);
        colors.add(Color.BLUE);
        colors.add(new Color(40, 240, 100));
        colors.add(new Color(240, 240, 20));
        colors.add(new Color(40, 240, 40));
        colors.add(new Color(100, 100, 100));
        colors.add(new Color(200, 40, 200));

        return colors;
    }

    /**
     * Refreshes the graph when there are changes
     */
    public static void refreshGraph() {
        VisualizeGraph visual = VisualizeGraph.getInstance();
        AccessLinksTextFile.addNewLinkstoGraph();
        AccessLinksTextFile.deleteRemovalLinkstoGraph();

        visual.importFile();//import the generated graph file into Gephi toolkit API workspace
        GraphModel model = Lookup.getDefault().lookup(GraphController.class).getModel();// get graph model            

        visual.setGraph(model, PropertyFile.getGraphType());//set the graph type
        visual.setGraph(model);
        HomeGUI.isComaparing = false;
        visual.setPreview();
        visual.setLayout();

        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                HomeGUI.table.clearAll();
                HomeGUI.table.deselectAll();
                HomeGUI.table.removeAll();
            }
        });
        nodeData.clear();
        nodeData = new HashMap<>();
    }

    /**
     * Method to show graph in tool
     *
     */
    public void showGraph() {
        HomeGUI.isComaparing = false;
        setPreview();
        setLayout();
        target = getTarget(100);

        Display.getCurrent().asyncExec(new Runnable() {
            @Override
            public void run() {
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

                createPopUpMenu();
                frame = SWT_AWT.new_Frame(composite);

                composite2 = new Composite(HomeGUI.propertyTab, SWT.H_SCROLL | SWT.V_SCROLL);
                composite2.setLayout(new GridLayout(1, false));

                composite3 = new Composite(composite2, SWT.RIGHT);
                composite3.setLayout(new FillLayout());

                createTableComponents();

                Label space = new Label(composite2, SWT.NONE);
                GridData spaceData = new GridData();
                spaceData.heightHint = 10;
                space.setLayoutData(spaceData);

                Label edgetitle = new Label(composite2, SWT.NONE);
                edgetitle.setText("Edge-Color Notations:");
                edgetitle.setFont(new org.eclipse.swt.graphics.Font(Display.getCurrent(), "Serif", 10, SWT.BOLD));
                //System.out.println("Doneeeeee");
                tbtmPropertyInfos.setControl(composite2);

                composite4 = new Composite(composite2, SWT.RIGHT);
                composite4.setLayout(new GridLayout(2, false));
                composite4.setRedraw(true);

                for (String type : edgeColoring.keySet()) {
                    Label edgeColor = new Label(composite4, SWT.BORDER | SWT.PUSH);
                    GridData gridData = new GridData();
                    gridData.widthHint = 20;
                    gridData.heightHint = 20;
                    edgeColor.setLayoutData(gridData);
                    edgeColor.setText("");
                    Label edgeDetailLabel = new Label(composite4, SWT.NONE);
                    edgeDetailLabel.setFont(new org.eclipse.swt.graphics.Font(Display.getCurrent(), "Serif", 7, SWT.BOLD));
                    if (type == null) {
                        edgeDetailLabel.setText("Null");
                    } else {
                        edgeDetailLabel.setText(type);
                    }
                    edgeColor.setCursor(new Cursor(Display.getCurrent(), SWT.NONE));
                    edgeColor.setBackground(edgeColoring.get(type));
                }

                //add refresh button to graph panel
                Button refresh = new Button("Refresh");
                refresh.addActionListener(new ActionListener() {
                    final String type = PropertyFile.getGraphType();

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ReadXML.initApp(HomeGUI.projectPath, PropertyFile.graphType);
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
                HomeGUI.graphtabItem.setControl(composite);        //set the table visible when the visualization is active

                frame.revalidate();
            }
        });

    }

    /**
     * Creates the details panel of edge coloring
     */
    public void createColorDetails() {
        for (String type : edgeColoring.keySet()) {
            Label edgeColor = new Label(composite4, SWT.BORDER | SWT.PUSH);
            GridData gridData = new GridData();
            gridData.widthHint = 20;
            gridData.heightHint = 20;
            edgeColor.setLayoutData(gridData);
            edgeColor.setText("");
            Label edgeDetailLabel = new Label(composite4, SWT.NONE);
            edgeDetailLabel.setFont(new org.eclipse.swt.graphics.Font(Display.getCurrent(), "Serif", 7, SWT.BOLD));
            edgeDetailLabel.setText(type);
            edgeColor.setCursor(new Cursor(Display.getCurrent(), SWT.NONE));
            edgeColor.setBackground(edgeColoring.get(type));
        }
    }

    /**
     * Creates the table to show node properties
     */
    public void createTableComponents() {
        table = new Table(composite2, SWT.BORDER);
        GridData data = new GridData();
        data.horizontalAlignment = GridData.FILL;
        data.grabExcessHorizontalSpace = true;
        data.verticalAlignment = GridData.FILL;

        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        table.setCursor(new Cursor(Display.getCurrent(), SWT.CURSOR_IBEAM));
        table.setLayoutData(data);
        table.setItemCount(10);
        table.setRedraw(true);

        /*
        this is first coloumn
         */
        tblclmnProperty = new TableColumn(table, SWT.NULL);
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
        //tableCursor = new TableCursor(table, SWT.NONE);
        updateBtn = new org.eclipse.swt.widgets.Button(composite3, SWT.BORDER | SWT.PUSH | SWT.VERTICAL);
        updateBtn.setText("Update");

        deleteBtn = new org.eclipse.swt.widgets.Button(composite3, SWT.BORDER | SWT.POP_UP | SWT.VERTICAL);
        deleteBtn.setText("Delete");

        impactBtn = new org.eclipse.swt.widgets.Button(composite3, SWT.BORDER | SWT.PUSH | SWT.VERTICAL);
        impactBtn.setText("Impact");

        undoBtn = new org.eclipse.swt.widgets.Button(composite3, SWT.BORDER | SWT.PUSH | SWT.VERTICAL);
        undoBtn.setText("Undo");

        undoBtn.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent se) {
                refreshGraph();
            }
        });

        impactBtn.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent se) {
                VisualizeGraph preview = VisualizeGraph.getInstance();//PropertyFile.getVisual();//new VisualizeGraph();
                preview.importFile();
                GraphModel graphModel = Lookup.getDefault().lookup(GraphController.class).getModel();
                FilterController filterController = Lookup.getDefault().lookup(FilterController.class);
                EgoFilter egoFilter = new EgoFilter();
                egoFilter.setPattern(id);
                egoFilter.setDepth(1);
                egoFilter.setSelf(true);

                Query queryEgo = filterController.createQuery(egoFilter);
                GraphView viewEgo = filterController.filter(queryEgo);
                graphModel.setVisibleView(viewEgo);
                preview.setGraph(graphModel);
                preview.setPreview();
                preview.setLayout();
            }

        });

        updateBtn.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent se) {
                GraphDBEdit gbEditor = new GraphDBEdit();
                System.out.println("Selected Update :" + nodeData);
                if (!nodeData.isEmpty()) {
                    gbEditor.storeUpdatedNode(nodeData);
                    while (!GraphDBEdit.lock) {
                    }
                    transferDataToDBFromXML(projectPath, true);
                    refreshGraph();
                }
            }
        });

        deleteBtn.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent se) {
                GraphDBDelete gbDeletor = new GraphDBDelete();
                //System.out.println("Selected Delete :" + nodeData);
                if (!nodeData.isEmpty()) {
                    gbDeletor.deleteNodeAndRelations(nodeData);
                    while (!GraphDBDelete.lock) {
                    }
                    transferDataToDBFromXML(projectPath, false);
                    refreshGraph();
                }

            }

        });

        final TableEditor editor = new TableEditor(table);
        editor.horizontalAlignment = SWT.LEFT;
        editor.grabHorizontal = true;

        /**
         * Listens for editing the table item
         */
        table.addListener(SWT.MouseDown, new Listener() {
            @Override
            public void handleEvent(Event event) {
                Rectangle clientArea = table.getClientArea();
                Point pt = new Point(event.x, event.y);
                int index = table.getTopIndex();
                while (index < table.getItemCount()) {
                    boolean visible = false;
                    final TableItem item = table.getItem(index);
                    Rectangle rect = item.getBounds(1);
                    if (rect.contains(pt)) {
                        final Text text = new Text(table, SWT.NONE);
                        Listener textListener = new Listener() {
                            @Override
                            public void handleEvent(Event e) {
                                switch (e.type) {
                                    case SWT.CR:
                                        if (item.getText(0).equalsIgnoreCase("ID") || item.getText(0).equalsIgnoreCase("Type")) {
                                        } else if (item.getText(0).equalsIgnoreCase("Visibility")) {

                                            item.setText(1, text.getText());
                                        } else {
                                            item.setText(1, text.getText());
                                        }
                                        //System.out.println("Replacing4: " + item.getText(0) + ":" + item.getText(1));
                                        nodeData.replace(item.getText(0), item.getText(1));
                                        //System.out.println("Key: " + item.getText(0) + " Value: " + item.getText(1));
                                        text.dispose();
                                        break;
                                    case SWT.FocusOut:
                                        if (item.getText(0).equalsIgnoreCase("ID") || item.getText(0).equalsIgnoreCase("Type")) {
                                        } else {
                                            item.setText(1, text.getText());
                                        }
                                        //System.out.println("Replacing5: " + item.getText(0) + ":" + item.getText(1));
                                        nodeData.replace(item.getText(0), item.getText(1));
                                        //System.out.println("Key: " + item.getText(0) + " Value: " + item.getText(1));
                                        text.dispose();
                                        break;
                                    case SWT.Traverse:
                                        switch (e.detail) {
                                            case SWT.TRAVERSE_RETURN:
                                                if (item.getText(0).equalsIgnoreCase("ID") || item.getText(0).equalsIgnoreCase("Type")) {
                                                } else {
                                                    item.setText(1, text.getText());
                                                }
                                                nodeData.replace(item.getText(0), item.getText(1));

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
    }

    /**
     * Creates a new menu when right clicked on the node in visual
     */
    public void createPopUpMenu() {
        popupMenu = new Menu(composite);
        newLink = new MenuItem(popupMenu, SWT.CASCADE);
        newLink.setText("Add a Link");
        removeLink = new MenuItem(popupMenu, SWT.NONE);
        removeLink.setText("Remove a Link");

        newLink.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent se) {

                // User is asked for details in creating a new link
                System.out.println("Adding a Link");
                nodeRelations = new ArrayList<>();

                Node clickNode = null;

                for (Node node : Lookup.getDefault().lookup(GraphController.class).getModel(GraphMouseListener.wkspace).getGraph().getNodes()) {
                    if (node.getAttributes().getValue("ID").toString().equalsIgnoreCase(GraphMouseListener.id)) {
                        clickNode = node;
                    }
                    nodeRelations.add(node);
                }

                for (Edge edge : Lookup.getDefault().lookup(GraphController.class).getModel(GraphMouseListener.wkspace).getGraph().getEdges()) {
                    if (edge.getSource() == clickNode) {
                        nodeRelations.remove(edge.getTarget());
                    } else if (edge.getTarget() == clickNode) {
                        nodeRelations.remove(edge.getSource());
                    }
                }

                newLinkPanel.setLayout(new java.awt.GridLayout(7, 1));

                Vector comboBoxItems2 = new Vector();
                comboBoxItems2.add("");
                comboBoxItems2.add("Requirement");
                comboBoxItems2.add("Source");
                comboBoxItems2.add("Diagram");
                modelArtefact = new DefaultComboBoxModel();
                for (int i = 0; i < comboBoxItems2.size(); i++) {
                    modelArtefact.addElement(comboBoxItems2.get(i));
                }
                artefactCombo.setModel(modelArtefact);

                Vector comboBoxItems1 = new Vector();
                comboBoxItems1.add("");
                comboBoxItems1.add("Class");
                comboBoxItems1.add("Field/Attribute");
                comboBoxItems1.add("Method/Operation");
                model = new DefaultComboBoxModel();
                for (int i = 0; i < comboBoxItems1.size(); i++) {
                    model.addElement(comboBoxItems1.get(i));
                }
                typeCombo.setEnabled(false);

                Vector comboBoxItems = new Vector();
                comboBoxItems.add("");
                modelNode = new DefaultComboBoxModel();
                for (int i = 0; i < comboBoxItems.size(); i++) {
                    modelNode.addElement(comboBoxItems.get(i));
                }
                nodeCombo.setModel(modelNode);
                nodeCombo.setEnabled(false);

                artefactCombo.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        nodes.clear();
                        nodes = new ArrayList<>();
                        if (artefactCombo.getSelectedItem() != null) {
                            artefactSelected = artefactCombo.getSelectedItem().toString();
                            String artefact = artefactCombo.getSelectedItem().toString();
                            //System.out.println("rtef " + artefact + " " + nodeRelations.size());
                            for (Node n : nodeRelations) {
                                if (n.getAttributes().getValue("Artefact").toString().equalsIgnoreCase(artefact)) {
                                    nodes.add(n);
                                }
                            }
                            int index = GraphMouseListener.id.indexOf("_");
                            if (index > 0) {
                                String s = GraphMouseListener.id.substring(index + 1);
                                switch (s.charAt(0)) {
                                    case 'F':
                                        model.removeElement("Class");
                                        model.removeElement("Method/Operation");
                                        typeCombo.setModel(model);
                                        typeCombo.setEnabled(true);
                                        break;
                                    case 'M':
                                        model.removeElement("Class");
                                        model.removeElement("Field/Attribute");
                                        typeCombo.setModel(model);
                                        typeCombo.setEnabled(true);
                                        break;
                                    default:
                                        typeCombo.setModel(model);
                                        typeCombo.setEnabled(true);
                                        break;
                                }
                            } else if (GraphMouseListener.id.charAt(0)
                                    == artefact.charAt(0)) {
                                model.removeElement("Field/Attribute");
                                model.removeElement("Method/Operation");
                                typeCombo.setModel(model);
                                typeCombo.setEnabled(true);
                            } else {
                                model.removeElement("Class");
                                typeCombo.setModel(model);
                                typeCombo.setEnabled(true);
                            }
                            modelNode.removeAllElements();
                            finalNodes.clear();
                            finalNodes = new ArrayList<>();
                        }
                    }
                });

                typeCombo.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (typeCombo.getSelectedItem() != null) {
                            typeSelected = typeCombo.getSelectedItem().toString();
                            finalNodes = new ArrayList<>();
                            String type = typeCombo.getSelectedItem().toString();
                            if (type.equalsIgnoreCase("Field/Attribute")) {
                                if (artefactCombo.getSelectedItem().toString().trim().charAt(0) != GraphMouseListener.id.charAt(0)) {
                                    for (Node n : nodes) {
                                        if (n.getAttributes().getValue("Type").toString().equalsIgnoreCase("Field")
                                                || n.getAttributes().getValue("Type").toString().equalsIgnoreCase("UMLAttribute")) {
                                            finalNodes.add(n);
                                        }
                                    }
                                }
                            } else if (type.equalsIgnoreCase("Method/Operation")) {
                                if (artefactCombo.getSelectedItem().toString().trim().charAt(0) != GraphMouseListener.id.charAt(0)) {
                                    for (Node n : nodes) {
                                        if (n.getAttributes().getValue("Type").toString().equalsIgnoreCase("Method")
                                                || n.getAttributes().getValue("Type").toString().equalsIgnoreCase("UMLOperation")) {
                                            finalNodes.add(n);
                                        }
                                    }
                                } else {
                                    typeCombo.setEnabled(false);
                                }
                            } else {
                                for (Node n : nodes) {
                                    if (n.getAttributes().getValue("Type").toString().equalsIgnoreCase(type)) {
                                        finalNodes.add(n);
                                    }
                                }
                            }
                            modelNode = new DefaultComboBoxModel();
                            for (Node n : finalNodes) {
                                modelNode.addElement(n.getAttributes().getValue("ID"));
                            }
                            nodeCombo.setModel(modelNode);
                            nodeCombo.setEnabled(true);
                        }
                    }

                });
                nodeCombo.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (nodeCombo.getSelectedItem() != null) {
                            nodeSelected = nodeCombo.getSelectedItem().toString();
                        }
                    }
                });

                btnCreate.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        createLink();
                        finalNodes.clear();
                        nodeRelations.clear();
                        model.removeAllElements();
                        modelNode.removeAllElements();
                        modelArtefact.removeAllElements();
                        artefactCombo.removeAllItems();
                        nodeCombo.removeAllItems();
                        typeCombo.removeAllItems();
                        //newLinkFrame.setVisible(false);
                        newLinkFrame.dispose();
                    }

                    public void createLink() {
                        String start = GraphMouseListener.id;
                        String end = nodeSelected;//nodeCombo.getSelectedItem().toString();
                        String type = typeSelected;//typeCombo.getSelectedItem().toString();
                        String artefact = artefactSelected;//artefactCombo.getSelectedItem().toString();
                        String relType = "";
                        String temp;
                        if (type.equalsIgnoreCase("Class")) {
                            switch (GraphMouseListener.id.charAt(0)) {
                                case 'R':
                                    if (artefact.equalsIgnoreCase("Source")) {
                                        relType = GraphDB.RelTypes.REQ_CLASS_TO_SOURCE_CLASS.toString();
                                    } else if (artefact.equalsIgnoreCase("Diagram")) {
                                        relType = GraphDB.RelTypes.REQ_CLASS_TO_UML_CLASS.toString();
                                    }
                                    break;
                                case 'S':
                                    if (artefact.equalsIgnoreCase("Requirement")) {
                                        temp = start;
                                        start = end;
                                        end = temp;
                                        relType = GraphDB.RelTypes.REQ_CLASS_TO_SOURCE_CLASS.toString();
                                    } else if (artefact.equalsIgnoreCase("Diagram")) {
                                        temp = start;
                                        start = end;
                                        end = temp;
                                        relType = GraphDB.RelTypes.UML_CLASS_TO_SOURCE_CLASS.toString();
                                    }
                                    break;
                                case 'D':
                                    if (artefact.equalsIgnoreCase("Requirement")) {
                                        temp = start;
                                        start = end;
                                        end = temp;
                                        relType = GraphDB.RelTypes.REQ_CLASS_TO_UML_CLASS.toString();
                                    } else if (artefact.equalsIgnoreCase("Source")) {
                                        relType = GraphDB.RelTypes.UML_CLASS_TO_SOURCE_CLASS.toString();
                                    }
                                    break;
                            }
                        } else if (type.equalsIgnoreCase("Field/Attribute")) {
                            switch (GraphMouseListener.id.charAt(0)) {
                                case 'R':
                                    if (artefact.equalsIgnoreCase("Source")) {
                                        relType = GraphDB.RelTypes.REQ_FIELD_TO_SOURCE_FIELD.getValue();
                                    } else if (artefact.equalsIgnoreCase("Diagram")) {
                                        relType = GraphDB.RelTypes.REQ_FIELD_TO_UML_ATTRIBUTE.toString();
                                    } else if (artefact.equalsIgnoreCase("Requirement") && !GraphMouseListener.id.contains("_")) {
                                        relType = GraphDB.RelTypes.SUB_ELEMENT.toString();
                                    }
                                    break;
                                case 'S':
                                    if (artefact.equalsIgnoreCase("Requirement")) {
                                        temp = start;
                                        start = end;
                                        end = temp;
                                        relType = GraphDB.RelTypes.REQ_FIELD_TO_SOURCE_FIELD.toString();
                                    } else if (artefact.equalsIgnoreCase("Diagram")) {
                                        temp = start;
                                        start = end;
                                        end = temp;
                                        relType = GraphDB.RelTypes.UML_ATTRIBUTE_TO_SOURCE_FIELD.toString();
                                    } else if (artefact.equalsIgnoreCase("Source") && !GraphMouseListener.id.contains("_")) {
                                        relType = GraphDB.RelTypes.SUB_ELEMENT.toString();
                                    }
                                    break;
                                case 'D':
                                    if (artefact.equalsIgnoreCase("Requirement")) {
                                        temp = start;
                                        start = end;
                                        end = temp;
                                        relType = GraphDB.RelTypes.REQ_FIELD_TO_UML_ATTRIBUTE.toString();
                                    } else if (artefact.equalsIgnoreCase("Source")) {
                                        relType = GraphDB.RelTypes.UML_ATTRIBUTE_TO_SOURCE_FIELD.toString();
                                    } else if (artefact.equalsIgnoreCase("Diagram") && !GraphMouseListener.id.contains("_")) {
                                        relType = GraphDB.RelTypes.SUB_ELEMENT.toString();
                                    }
                                    break;
                            }

                        } else if (type.equalsIgnoreCase("Method/Operation")) {
                            switch (GraphMouseListener.id.charAt(0)) {
                                case 'R':
                                    if (artefact.equalsIgnoreCase("Diagram")) {
                                        relType = GraphDB.RelTypes.REQ_METHOD_TO_UML_METHOD.toString();
                                    } else if (artefact.equalsIgnoreCase("Source")) {
                                        relType = GraphDB.RelTypes.REQ_METHOD_TO_SOURCE_METHOD.toString();
                                    } else if (artefact.equalsIgnoreCase("Requirement") && !GraphMouseListener.id.contains("_")) {
                                        relType = GraphDB.RelTypes.SUB_ELEMENT.toString();
                                    }
                                    break;
                                case 'S':
                                    if (artefact.equalsIgnoreCase("Requirement")) {
                                        relType = GraphDB.RelTypes.REQ_METHOD_TO_SOURCE_METHOD.toString();
                                    } else if (artefact.equalsIgnoreCase("Diagram")) {
                                        relType = GraphDB.RelTypes.UML_OPERATION_TO_SOURCE_METHOD.toString();
                                    } else if (artefact.equalsIgnoreCase("Source") && !GraphMouseListener.id.contains("_")) {
                                        relType = GraphDB.RelTypes.SUB_ELEMENT.toString();
                                    }
                                    break;
                                case 'D':
                                    if (artefact.equalsIgnoreCase("Requirement")) {
                                        relType = GraphDB.RelTypes.REQ_METHOD_TO_UML_METHOD.toString();
                                    } else if (artefact.equalsIgnoreCase("Source")) {
                                        relType = GraphDB.RelTypes.UML_OPERATION_TO_SOURCE_METHOD.toString();
                                    } else if (artefact.equalsIgnoreCase("Diagram") && !GraphMouseListener.id.contains("_")) {
                                        relType = GraphDB.RelTypes.SUB_ELEMENT.toString();
                                    }
                                    break;
                            }
                        }
                        int count = AccessGexfFile.addToGEXF(start, end, relType);
                        if (count != -1) {
                            addNewLinksFile(count, start, end, relType);
                        }
                        VisualizeGraph.refreshGraph();
                        nodeSelected = "";
                        typeSelected = "";
                        artefactSelected = "";
                    }

                    private void addNewLinksFile(int count, String start, String end, String relType) {
                        String newLinkFile = HomeGUI.projectPath + File.separator + FilePropertyName.PROPERTY + File.separator + "NewGraphLinks.txt";
                        String source = count + ":" + start + " " + end + " " + relType + "\n";
                        File file = new File(newLinkFile);
                        try (FileWriter writer = new FileWriter(file, true)) {
                            writer.write(System.getProperty("line.separator"));
                            writer.write(source);
                        } catch (IOException e) {
                        }
                    }
                });

                btnCancel.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        finalNodes.clear();
                        nodeRelations.clear();
                        newLinkFrame.setVisible(false);
                        model.removeAllElements();
                        modelArtefact.removeAllElements();

                        modelNode.removeAllElements();
                        artefactCombo.removeAllItems();
                        nodeCombo.removeAllItems();
                        typeCombo.removeAllItems();
                    }
                });

                newLinkFrame.pack();
                newLinkFrame.revalidate();
                newLinkFrame.setVisible(true);

            }

        });

        removeLink.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent se) {
                // User is asked for details in deleting a new link

                System.out.println("Removing a Link");
                edgesList = new ArrayList<>();

                for (Node node : Lookup.getDefault().lookup(GraphController.class).getModel(GraphMouseListener.wkspace).getGraph().getNodes()) {
                    if (node.getAttributes().getValue("ID").toString().equalsIgnoreCase(GraphMouseListener.id)) {
                        //System.out.println("Looking for " + node + " id: " + GraphMouseListener.id + " " + node.getAttributes().getValue("ID"));
                        for (Edge edge : Lookup.getDefault().lookup(GraphController.class).getModel(GraphMouseListener.wkspace).getGraph().getEdges()) {
                            if (edge.getSource() == node) {
                                edgesList.add(edge);
                            } else if (edge.getTarget() == node) {
                                edgesList.add(edge);
                            }
                        }
                        break;
                    }
                }

                int count = 0;
                relIStrings = new ArrayList();

                for (Edge e : edgesList) {
                    relIStrings.add(new JCheckBox(e.getSource().getAttributes().getValue("ID") + "-" + e.getTarget().getAttributes().getValue("ID")));
                }
                cbList = new CheckBoxList(relIStrings.size());
                cbList.setListData(relIStrings.toArray());
                //removePanel = new JPanel();
                removePanel.removeAll();
                removePanel.revalidate();
                removePanel.repaint();

                removePanel.add(cbList);
                removePanel.add(panelRemoveButton);
                removePanel.revalidate();
                removePanel.repaint();

                btnRemoveDelete.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        boolean delete = false;

                        frameRemoval.dispose();
                        List<JCheckBox> selectedList = cbList.getSelectedValuesList();
                        ids = new ArrayList<>();
                        //System.out.println("IDS " + ids.size());
                        if (edgesList.size() > 0) {
                            for (int i = 0; i < cbList.getCheckList().length; i++) {
                                if (cbList.getCheckList()[i] == 1) {
                                    String id = edgesList.get(i).getAttributes().getValue("ID").toString();
                                    String edge = (relIStrings.get(i)).getText();
                                    ids.add(String.valueOf(id));
                                    delete = true;
                                }
                            }
                            for (String id : ids) {
                                AccessGexfFile.removeEdgeFromGexf(Integer.parseInt(id));
                                addDeleteLinksFile(id);
                            }
                            //ReadXML.initApp(projectPath, graphType);
                            VisualizeGraph.refreshGraph();
                            if (delete) {
                                cbList = new CheckBoxList();
                                edgesList.clear();
                                relIStrings.clear();
                            }
                        } else {
                            //do nothing
                        }

                    }

                    private void addDeleteLinksFile(String id) {
                        String deleteLinkFile = HomeGUI.projectPath + File.separator + FilePropertyName.PROPERTY + File.separator + "DeletedGraphLinks.txt";
                        String newLinkFile = HomeGUI.projectPath + File.separator + FilePropertyName.PROPERTY + File.separator + "NewGraphLinks.txt";
                        //String source = start + " " + end + " " + relType + "\n";
                        File file = new File(newLinkFile);
                        boolean delete = false;
                        try {
                            File inputFile = new File(newLinkFile);
                            File tempFile = new File("TempFile.txt");

                            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

                            String lineToRemove = id;
                            String currentLine;
                            String chkLine = "";

                            while ((currentLine = reader.readLine()) != null) {
                                // trim newline when comparing with lineToRemove
                                if (currentLine.length() > 1) {
                                    String trimmedLine = currentLine.trim();
                                    String currentID = currentLine.substring(0, currentLine.indexOf(":"));
                                    if (currentID.equals(lineToRemove)) {
                                        delete = true;
                                        continue;
                                    }
                                    writer.write(currentLine + System.getProperty("line.separator"));
                                }

                            }
                            writer.close();
                            reader.close();
                            String name = inputFile.getAbsolutePath();
                            inputFile.delete();
                            File renameFile = new File(name);
                            boolean successful = tempFile.renameTo(renameFile);

                        } catch (IOException e) {
                        }
                        if (!delete) {
                            String source = id + " ";
                            File file2 = new File(deleteLinkFile);
                            try (FileWriter writer = new FileWriter(file2, true)) {
                                writer.write(source);
                            } catch (IOException e) {
                            }
                        }

                    }
                });

                btnRemoveCancel.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        edgesList.clear();
                        frameRemoval.dispose();
                    }
                });
                frameRemoval.pack();
                frameRemoval.revalidate();
                frameRemoval.setVisible(true);
            }
        });

        createNewLinkFrame();

        createRemovalLinkFrame();

    }

    /**
     * Creates a new frame for removing frame when prompted
     *
     * @throws HeadlessException
     */
    public void createRemovalLinkFrame() throws HeadlessException {
        frameRemoval = new JFrame();
        removePanel = new JPanel();
        removePanel.setLayout(new java.awt.GridLayout(2, 1));
        panelRemoveButton.add(btnRemoveDelete);
        panelRemoveButton.add(btnRemoveCancel);

        //System.out.println("Done new lik");
        frameRemoval.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frameRemoval.setLocation(dim.width / 2 - frameRemoval.getSize().width / 2, dim.height / 2 - frameRemoval.getSize().height / 2);
        frameRemoval.add(removePanel);
        //frameRemoval.setSize(300, 400);
        frameRemoval.setTitle("Links Removal");

        frameRemoval.pack();
        frameRemoval.revalidate();
    }

    /**
     * Creates a new frame for link addition when prompted
     */
    public void createNewLinkFrame() {
        newLinkFrame = new JFrame();
        newLinkPanel.add(lblArtefact);
        newLinkPanel.add(artefactCombo);
        newLinkPanel.add(lblType);
        newLinkPanel.add(typeCombo);
        newLinkPanel.add(lblNode);
        newLinkPanel.add(nodeCombo);

        panelButton.add(btnCreate);
        panelButton.add(btnCancel);
        newLinkPanel.add(panelButton);

        //newLinkFrame.setBounds(new java.awt.Rectangle(50, 50));
        newLinkFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        newLinkFrame.setTitle("Add Links");
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        newLinkFrame.setLocation(dim.width / 2 - newLinkFrame.getSize().width / 2, dim.height / 2 - newLinkFrame.getSize().height / 2);

        newLinkFrame.add(newLinkPanel);
        //newLinkFrame.setPreferredSize(new Dimension(100, 100));
        //newLinkFrame.setSize(300, 400);
        newLinkFrame.pack();
        newLinkFrame.revalidate();
    }

}
