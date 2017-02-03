package com.project.traceability.visualization;

import org.apache.commons.io.FileUtils;
import com.project.NLP.SourceCodeToXML.WriteToXML;
import com.project.NLP.file.operations.FilePropertyName;
import com.project.traceability.GUI.HomeGUI;
import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.ArtefactSubElement;
import com.project.traceability.model.AttributeModel;
import com.project.traceability.model.MethodModel;
import com.project.traceability.model.ParameterModel;
import com.project.traceability.model.RequirementModel;
import com.project.traceability.staticdata.StaticData;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.tooling.GlobalGraphOperations;
import org.openide.util.Exceptions;

/**
 * Model to add data to Neo4j graph DB.
 *
 * @author Thanu
 * @author Aarthika
 */
public class GraphDB {

    Index<Node> artefacts;
    Index<Relationship> edges;
    private String fileType;

    /**
     * Define relationship type.
     *
     * @author Thanu
     *
     */
    public static enum RelTypes implements RelationshipType {

        SUB_ELEMENT("Sub Element"), GETTER("Getter Method"), SETTER("Setter Method"),
        UML_CLASS_TO_SOURCE_CLASS("UML Class To Source Class"), UML_ATTRIBUTE_TO_SOURCE_FIELD("UMLAttribute To Source Field"), UML_OPERATION_TO_SOURCE_METHOD("UMLOperation To Source Method"),
        REQ_CLASS_TO_SOURCE_CLASS("Req Class To Source Class"), REQ_METHOD_TO_SOURCE_METHOD("Req Method To Source Method"), REQ_FIELD_TO_SOURCE_FIELD("Req Field To Source Field"),
        REQ_CLASS_TO_UML_CLASS("Req Class To UML Class"), REQ_METHOD_TO_UML_METHOD("Req Method To UMLOperation"), REQ_FIELD_TO_UML_ATTRIBUTE("Req Field To UMLAttribute");

        private final String value;

        private RelTypes(String val) {
            this.value = val;
        }

        @Override
        public String toString() {
            return value;
        }

        public String getValue() {
            return value;
        }

        public static RelTypes parseEnum(final String val) {

            RelTypes relType = null;
            for (RelTypes type : RelTypes.values()) {
                if (type.getValue().equals(val)) {
                    relType = type;
                }
            }
            return relType;
        }
    }

    /**
     * Define Node types.
     *
     * @author Thanu
     *
     */
    private static enum NodeTypes implements RelationshipType {

        CLASS("Class"), FIELD("Field"), METHOD("Method"), UMLATTRIBUTE(
                "UMLAttribute"), UMLOPERATION("UMLOperation");

        private final String value;

        private NodeTypes(String val) {
            this.value = val;
        }

        @Override
        public String toString() {
            return value;
        }

        public String getValue() {
            return value;
        }

        public static NodeTypes parseEnum(final String val) {

            NodeTypes nodeType = null;
            for (NodeTypes type : NodeTypes.values()) {
                if (type.getValue().equals(val)) {
                    nodeType = type;
                }
            }
            return nodeType;
        }
    }

    GraphDatabaseService graphDb;
    Relationship relationship;

    public void setGraphDb(GraphDatabaseService db) {
        this.graphDb = db;
    }

    /**
     * Method to create an new Neo4j db or to open an existing Neo4j db
     *
     */
    public void initiateGraphDB() {
        if (WriteToXML.isTragging.equalsIgnoreCase("Tragging")) {
            //System.out.println("Tragging");
            Path path = FileSystems.getDefault().getPath(HomeGUI.projectPath + File.separator + FilePropertyName.PROPERTY, "VERSION_" + HomeGUI.projectName + ".graphdb");
            try {
                FileUtils.deleteDirectory(new File(path.toAbsolutePath().toString()));
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
            graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(HomeGUI.projectPath + File.separator + FilePropertyName.PROPERTY + File.separator + "VERSION_" + HomeGUI.projectName
                    + ".graphdb").newGraphDatabase();
        } else {
            graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(HomeGUI.projectPath + File.separator + FilePropertyName.PROPERTY + File.separator + HomeGUI.projectName
                    + ".graphdb").newGraphDatabase();
        }
        Transaction tx = graphDb.beginTx();
        //System.out.println(""+graphDb.toString());

        try {
            tx.success();

        } finally {
            tx.finish();
        }
        registerShutdownHook(graphDb);
    }

    /**
     * Creates a new node into the database
     *
     * @param myLabel
     * @param artefacts
     * @param edges
     * @param artefactElement
     */
    public void addNewNodeToDB(Label myLabel, Index<Node> artefacts, Index<Relationship> edges, ArtefactElement artefactElement) {
        Node n = graphDb.createNode();

        n.addLabel(myLabel);
        String lbl = getArtefact(artefactElement.getArtefactElementId());
        n.setProperty("Artefact", lbl);
        addID(artefactElement, n);
        addName(artefactElement, n);
        addType(artefactElement, n);
        addVisibility(artefactElement, n);
        addStatus(artefactElement, n);
        artefacts.add(n, "ID", n.getProperty("ID"));
        List<ArtefactSubElement> artefactsSubElements = artefactElement
                .getArtefactSubElements();

        for (ArtefactSubElement artefactsSubElement : artefactsSubElements) {
            IndexHits<Node> subElement_hits = artefacts.get("ID", artefactsSubElement.getSubElementId());
            Node subNode = subElement_hits.getSingle();
            ArtefactSubElement temp = artefactsSubElement;
            if (subNode == null) {
                addNewSubNodeToDB(temp, artefacts, n, edges);
            } else {
                updateSubNodeToDB(subNode, temp);

            }
        }
    }

    private void addStatus(ArtefactElement artefactElement, Node n) {
        if (null == artefactElement.getStatus() || artefactElement.getStatus().isEmpty()) {
            n.setProperty("Status", StaticData.DEFAULT_STATUS);
        } else {
            n.setProperty("Status", artefactElement.getStatus());
        }
    }

    /**
     * Identifies the type of artefact
     *
     * @param id
     * @return
     */
    public String getArtefact(String id) {
        String lbl = "";
        switch (id.charAt(0)) {
            case 'R':
                lbl = "Requirement";
                break;
            case 'S':
                lbl = "Source";
                break;
            case 'D':
                lbl = "Diagram";
                break;
            default:
                break;
        }
        return lbl;
    }

    /**
     * Adds the visibility of a node
     *
     * @param artefactElement
     * @param n
     */
    public void addVisibility(ArtefactElement artefactElement, Node n) {
        if (null == artefactElement.getVisibility()) {
            n.setProperty("Visibility", "");
        } else {
            n.setProperty("Visibility", artefactElement.getVisibility());
        }
    }

    /**
     * Adds the type of a node
     *
     * @param artefactElement
     * @param n
     */
    public void addType(ArtefactElement artefactElement, Node n) {	
        if (null == artefactElement.getType()) {
            n.setProperty("Type", "");	
        } else {
            n.setProperty("Type", artefactElement.getType());
        }
    }

    /**
     * Adds the name of a node
     *
     * @param artefactElement
     * @param n
     */
    public void addName(ArtefactElement artefactElement, Node n) {
        if (null == artefactElement.getName()) {
            n.setProperty("Name", "");
        } else {
            n.setProperty("Name", artefactElement.getName());
        }
    }

    /**
     * Adds the id of a node
     *
     * @param artefactElement
     * @param n
     */
    public void addID(ArtefactElement artefactElement, Node n) {
        if (null == artefactElement.getArtefactElementId()) {
            n.setProperty("ID", "");
        } else {
            n.setProperty("ID", artefactElement.getArtefactElementId());
        }
    }

    /**
     * Creates a new sub node into the database
     *
     * @param temp
     * @param artefacts
     * @param n
     * @param edges
     */
    public void addNewSubNodeToDB(ArtefactSubElement temp, Index<Node> artefacts, Node n, Index<Relationship> edges) {
        Label myLabel;
        Node m = graphDb.createNode();
        myLabel = DynamicLabel.label(temp.getType());
        m.addLabel(myLabel);
        String lbl = getArtefact(temp.getSubElementId());
        m.setProperty("Artefact", lbl);
        addSubID(temp, m);
        addSubName(temp, m);
        addSubType(temp, m);
        addSubVisibility(temp, m);
        addSubStatus(temp, m);
        if (temp.getType().equalsIgnoreCase("UMLOperation")
                || temp.getType().equalsIgnoreCase("Method")) {
            addSubMethod(temp, m);

        } else if (temp.getType().equalsIgnoreCase(
                "UMLAttribute")
                || temp.getType().equalsIgnoreCase("Field")) {
            AttributeModel mtemp = (AttributeModel) temp;
            if (null != mtemp.getVariableType()) {
                m.setProperty("Variable Type",
                        mtemp.getVariableType());
            } else {
                m.setProperty("Variable Type", "");
            }

        }
        artefacts.add(m, "ID", m.getProperty("ID"));
        relationship = n.createRelationshipTo(m,
                RelTypes.SUB_ELEMENT);
        relationship.setProperty("message",
                RelTypes.SUB_ELEMENT.getValue());
        if (m.getProperty("Status") != null || n.getProperty("Status") != null) {

            boolean stat = false;
            if ((!n.getProperty("Status").toString().equalsIgnoreCase("NONE")) || (n.getProperty("Status").toString() != null)) {
                stat = true;
                relationship.setProperty("status", n.getProperty("Status").toString());
            }
            if ((!m.getProperty("Status").toString().equalsIgnoreCase("NONE")) || (m.getProperty("Status").toString() != null)) {
                if (!stat) {
                    relationship.setProperty("status", m.getProperty("Status").toString());
                }
            }
        } else {
            relationship.setProperty("status", "");
        }

        edges.add(relationship, "ID", n.getProperty("ID") + "-" + m.getProperty("ID"));
    }

    public void addSubStatus(ArtefactSubElement temp, Node m) {
        if (null == temp.getStatus() || temp.getStatus().isEmpty()) {
            m.setProperty("Status", StaticData.DEFAULT_STATUS);
        } else {
            m.setProperty("Status", temp.getStatus());
        }
    }

    /**
     * Adds the variable of a sub node
     *
     * @param temp
     * @param m
     */
    public void addSubVariable(ArtefactSubElement temp, Node m) {
        AttributeModel mtemp = (AttributeModel) temp;
        if (null != mtemp.getVariableType()) {
            m.setProperty("Variable Type",
                    mtemp.getVariableType());
        } else {
            m.setProperty("Variable Type", "");
        }
    }

    /**
     * Adds the method of a sub node
     *
     * @param temp
     * @param m
     */
    public void addSubMethod(ArtefactSubElement temp, Node m) {
        MethodModel mtemp = (MethodModel) temp;
        if (null != mtemp.getReturnType()) {
            m.setProperty("Return Type",
                    mtemp.getReturnType());
        } else {
            m.setProperty("Return Type", "");
        }
        if (null != mtemp.getParameters()) {
            List<ParameterModel> params = mtemp
                    .getParameters();
            String parameters = "";
            for (int p = 0; p < params.size(); p++) {
                parameters += params.get(p).getName() + ":"
                        + params.get(p).getVariableType();
                if (p < params.size() - 1) {
                    parameters += ",";
                }
            }
            m.setProperty("Parameters", parameters);
        } else {
            m.setProperty("Parameters", "");
        }
    }

    /**
     * Adds the visibility of a sub node
     *
     * @param temp
     * @param m
     */
    public void addSubVisibility(ArtefactSubElement temp, Node m) {
        if (null != temp.getVisibility()) {
            m.setProperty("Visibility", temp.getVisibility());
        } else {
            m.setProperty("Visibility", "");
        }
    }

    /**
     * Adds the type of a sub node
     *
     * @param temp
     * @param m
     */
    public void addSubType(ArtefactSubElement temp, Node m) {
        if (null == temp.getType()) {
            m.setProperty("Type", "");
        } else {
            m.setProperty("Type", temp.getType());
        }
    }

    /**
     * Adds the name of a sub node
     *
     * @param temp
     * @param m
     */
    public void addSubName(ArtefactSubElement temp, Node m) {
        if (null == temp.getName()) {
            m.setProperty("Name", "");
        } else {
            m.setProperty("Name", temp.getName());
        }
    }

    /**
     * Adds the id of a sub node
     *
     * @param temp
     * @param m
     */
    public void addSubID(ArtefactSubElement temp, Node m) {
        if (null == temp.getSubElementId()) {
            m.setProperty("ID", "");
        } else {
            m.setProperty("ID", temp.getSubElementId());
        }
    }

    /**
     * Updates the changes in the artefact element to the database.
     *
     * @param node
     * @param artefactElement
     */
    public void updateNodeToDB(Node node, ArtefactElement artefactElement) {
        //Updates if there are any change in the name of the artefact element
        if (!node.getProperty("Name").equals(
                artefactElement.getName())) {
            addName(artefactElement, node);
        }//Updates if there are any change in the Type of the artefact element 
        if (!node.getProperty("Type").equals(
                artefactElement.getType())) {
            addType(artefactElement, node);
        } //Updates if there are any change in the visibility of the artefact element
        if (!node.getProperty("Visibility").equals(
                artefactElement.getVisibility())) {
            addVisibility(artefactElement, node);
        }
        //Identifies if any changes (new/update) hve occured in the sub artefact element of the given artefact element

        List<ArtefactSubElement> artefactsSubElements = artefactElement
                .getArtefactSubElements();

        boolean subElementExist = false;
        parent:
        for (ArtefactSubElement artefactsSubElement : artefactsSubElements) {
            Iterator<Relationship> relations = node
                    .getRelationships(RelTypes.SUB_ELEMENT)
                    .iterator();
            child:
            while (relations.hasNext()) {
                Node test = relations.next().getOtherNode(node);
                if (test.getProperty("ID").equals(artefactsSubElement.getSubElementId())) {
                    updateSubNodeToDB(test, artefactsSubElement);
                    subElementExist = true;
                    break;
                }
            }
            if (!subElementExist) {
                addNewSubNodeToDB(artefactsSubElement, artefacts, node, edges);
            }
            subElementExist = false;
        }
    }

    /**
     * Updates the changes in the artefact sub element to the database.
     *
     * @param subNode
     * @param temp
     */
    public void updateSubNodeToDB(Node subNode, ArtefactSubElement temp) {
        //Updates if there are any change in the Name of the artefact sub element 
        if (!subNode.getProperty("Name").equals(
                temp.getName())) {
            addSubName(temp, subNode);
        }//Updates if there are any change in the Type of the artefact sub element  
        if (!subNode.getProperty("Type").equals(
                temp.getType())) {
            addSubType(temp, subNode);
        }//Updates if there are any change in the Visibility of the artefact sub element 
        if (null == subNode.getProperty("Visibility") || !subNode.getProperty("Visibility").equals(
                temp.getVisibility())) {
            if (null != temp.getVisibility()) {
                addSubVisibility(temp, subNode);
            }
        }
        //Checks if it is a method
        if (temp.getType().equalsIgnoreCase("UMLOperation")
                || temp.getType().equalsIgnoreCase("Method")) {
            //Updates if there are any change in the parameters of the artefact sub element
            addSubMethod(temp, subNode);
            //Updates if there are any change in the return type of the artefact sub element  

        }
        //Checks if it is a variable
        if (temp.getType().equalsIgnoreCase(
                "UMLAttribute")
                || temp.getType().equalsIgnoreCase("Field")) {
            //Updates if there are any change in the variable type of the artefact sub element 
            addSubVariable(temp, subNode);
        }
    }

    /**
     * Method to add artefact elements to db
     *
     * @param fileType
     * @param aretefactElements ArtefactElements map
     */
    public void addNodeToGraphDB(String fileType, Map<String, ArtefactElement> aretefactElements) {
        this.fileType = fileType;
        Transaction tx = graphDb.beginTx();
        try {
            Iterator<Entry<String, ArtefactElement>> iterator = aretefactElements
                    .entrySet().iterator();
            while (iterator.hasNext()) {

                Map.Entry pairs = iterator.next();
                ArtefactElement artefactElement = (ArtefactElement) pairs
                        .getValue();
                String lbl = getArtfefact(artefactElement);
                //System.out.println("Label " + lbl);
                Label myLabel = DynamicLabel.label(lbl);

                IndexManager index = graphDb.index();
                artefacts = index.forNodes("ArtefactElement");
                edges = index.forRelationships(RelTypes.SUB_ELEMENT.name());

                IndexHits<Node> hits = artefacts.get("ID",
                        artefactElement.getArtefactElementId());
                //System.out.println("ID * " + artefactElement.getArtefactElementId());
                Node node = hits.getSingle();
                if (node == null) {
                    addNewNodeToDB(myLabel, artefacts, edges, artefactElement);
                } else {
                    updateNodeToDB(node, artefactElement);
                }
            }
            tx.success();
        } finally {
            tx.finish();
        }
    }

    /**
     * Returns the artefact type
     *
     * @param artefactElement
     * @return
     */
    public String getArtfefact(ArtefactElement artefactElement) {
        String lbl = "";
        //System.out.println("a " + artefactElement.getArtefactElementId());
        String artefactId = artefactElement.getArtefactElementId().replace("NEW_", "");
        //System.out.println("a " + artefactId);
        switch (artefactId.charAt(0)) {
            case 'R':
                lbl = "Requirement";
                break;
            case 'S':
                lbl = "Source";
                break;
            case 'D':
                lbl = "Diagram";
                break;
            default:
                break;
        }
        return lbl;
    }

    /**
     * Checking the database
     */
    public void checkDB() {
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(HomeGUI.projectPath + File.separator + FilePropertyName.PROPERTY + File.separator + HomeGUI.projectName
                + ".graphdb").newGraphDatabase();
        try (Transaction tx = graphDb.beginTx()) {
            //System.out.println("Entered db...");
            for (Node n : GlobalGraphOperations.at(graphDb).getAllNodes()) {
                for (String m : n.getPropertyKeys()) {
                    //System.out.println("" + m + " : " + n.getProperty(m));
                }
            }
            for (Relationship r : GlobalGraphOperations.at(graphDb).getAllRelationships()) {
                //System.out.println("" + r.getStartNode().getProperty("ID") + "-" + r.getEndNode().getProperty("ID") + ":" + r.getType().name());
            }
        } finally {
            graphDb.shutdown();

        }
    }

    /**
     * Method to add relationships to db
     *
     * @param relation Relation List
     */
    public void addRelationTOGraphDB(List<String> relation) {
        Transaction tx = graphDb.beginTx();
        try {
            IndexManager index = graphDb.index();
            Index<Node> artefacts = index.forNodes("ArtefactElement");
            Index<Relationship> edges = index.forRelationships("SOURCE_TO_TARGET");

            for (int i = 0; i < relation.size(); i++) {
                IndexHits<Node> hits = artefacts.get("ID", relation.get(i));
                Node source = hits.getSingle();
                String message = relation.get(++i);
                RelTypes relType = RelTypes.parseEnum(message);
                hits = artefacts.get("ID", relation.get(++i));
                String status = "";
                if (WriteToXML.isTragging.equalsIgnoreCase("Tragging")) {
                    status = relation.get(++i);
                }
                Node target = hits.getSingle();

                if (null != source && null != target) {
                    Iterator<Relationship> relations = source.getRelationships()
                            .iterator();
                    boolean exist = false;
                    while (relations.hasNext()) {
                        if (relations.next().getOtherNode(source).equals(target)) {
                            exist = true;
                        }
                    }
                    //System.out.println(" " + target + " " + relType);
                    if (!exist && null != target && relType != null) {
                        relationship = source.createRelationshipTo(target, relType);
                        relationship.setProperty("message", message);
                        relationship.setProperty("status", status);
                        edges.add(relationship, "ID", source.getProperty("ID") + "-" + target.getProperty("ID"));
                    }
                }
            }
            tx.success();
        } finally {
            tx.finish();
        }
    }

    /**
     * Method to add intra relationships to db
     *
     * @param relation Relation List
     */
    public void addIntraRelationTOGraphDB(List<String> relation) {
        RelTypes relType;
        Transaction tx = graphDb.beginTx();
        try {
            IndexManager index = graphDb.index();
            Index<Node> artefacts = index.forNodes("ArtefactElement");
            Index<Relationship> edges = index.forRelationships("GETTER-SETTTER");

            for (int i = 0; i < relation.size(); i++) {
                IndexHits<Node> hits = artefacts.get("ID", relation.get(i));
                Node source = hits.getSingle();
                String message = relation.get(++i);
                relType = RelTypes.parseEnum(message);
                hits = artefacts.get("ID", relation.get(++i));
                Node target = hits.getSingle();
                if(WriteToXML.isTragging.equalsIgnoreCase("Tragging")){
                    i++;
                }

                if (null != source && null != target) {

                    Iterator<Relationship> relations = source.getRelationships()
                            .iterator();
                    boolean exist = false;
                    while (relations.hasNext()) {
                        if (relations.next().getOtherNode(source).equals(target)) {
                            exist = true;
                        }
                    }
                    if (!exist) {
                        relationship = source.createRelationshipTo(target, relType);
                        relationship.setProperty("message",
                                relType.getValue());
                        if (WriteToXML.isTragging.equalsIgnoreCase("Tragging")) {
                            relationship.setProperty("status",
                                    StaticData.DEFAULT_STATUS);
                        }
                        edges.add(relationship, "ID", source.getProperty("ID") + "-" + target.getProperty("ID"));
                    }
                }
            }
            tx.success();
        } finally {
            tx.finish();
        }
    }

    /**
     * Method to shutdown db
     *
     * @param graphDb
     */
    private static void registerShutdownHook(final GraphDatabaseService graphDb) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
    }

    @SuppressWarnings("deprecation")
    public void cleanUp(final GraphDatabaseService graphDb) {
        IndexManager index = graphDb.index();
        Index<Node> artefact = index.forNodes("ArtefactElement");
        artefact.delete();
        for (Node node : graphDb.getAllNodes()) {
            for (Relationship rel : node.getRelationships()) {
                rel.delete();
            }
            artefact.remove(node);
            node.delete();
        }
    }

    /**
     * Method to add requirements artefact elements to db
     *
     * @param requirementsAretefactElements RequirementModel List
     */
    public void addRequirementsNodeToGraphDB(
            List<RequirementModel> requirementsAretefactElements) {
        Transaction tx = graphDb.beginTx();
        try {

            for (int i = 0; i < requirementsAretefactElements.size(); i++) {

                RequirementModel requirement = requirementsAretefactElements.get(i);

                String lbl = "";
                switch (requirement.getRequirementId().charAt(0)) {
                    case 'R':
                        lbl = "Requirement";
                        break;
                    case 'S':
                        lbl = "Source";
                        break;
                    case 'D':
                        lbl = "Diagram";
                        break;
                    default:
                        break;
                }
                Label myLabel = DynamicLabel.label(lbl);

                IndexManager index = graphDb.index();
                Index<Node> artefacts = index.forNodes("ArtefactElement");

                IndexHits<Node> hits = artefacts.get("ID", requirement.getRequirementId());
                Node node = hits.getSingle();
                if (node == null) {
                    Node n = graphDb.createNode();

                    n.addLabel(myLabel);
                    n.setProperty("ID", requirement.getRequirementId());
                    n.setProperty("Name", requirement.getName());
                    n.setProperty("Type", requirement.getType());
                    //n.setProperty("Content", requirement.getContent());
                    n.setProperty("Priority", requirement.getPriority());
                    n.setProperty("Title", requirement.getTitle());
                    artefacts.add(n, "ID", n.getProperty("ID"));
                } else if (!node.getProperty("Name").equals(
                        requirement.getName())) {
                } else if (!node.getProperty("Type").equals(
                        requirement.getType())) {
                } else {
                    //Node already exists
                }
            }
            tx.success();

        } finally {
            tx.finish();
        }
    }

    /**
     * Method to generate gexf graph file from db using Gephi Toolkit API
     *
     */
    public void generateGraphFile() {
        GraphFileGenerator preview = new GraphFileGenerator();
        preview.generateGraphFile(graphDb);
        graphDb.shutdown();
    }
}
