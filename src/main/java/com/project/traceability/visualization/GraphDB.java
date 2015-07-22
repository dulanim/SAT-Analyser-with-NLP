package com.project.traceability.visualization;

import com.project.traceability.common.PropertyFile;
import com.project.traceability.model.ArtefactElement;
import com.project.traceability.model.ArtefactSubElement;
import com.project.traceability.model.AttributeModel;
import com.project.traceability.model.MethodModel;
import com.project.traceability.model.ParameterModel;
import com.project.traceability.model.RequirementModel;
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

/**
 * Model to add data to Neo4j graph DB.
 *
 * @author Thanu
 *
 */
public class GraphDB {

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

    /**
     * Method to create an new Neo4j db or to open an existing Neo4j db
     *
     */
    public void initiateGraphDB() {

        graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(PropertyFile.getGraphDbPath()).newGraphDatabase();
        Transaction tx = graphDb.beginTx();

        try {
            tx.success();

        } finally {
            tx.finish();
        }
        registerShutdownHook(graphDb);
    }

    /**
     * Method to add artefact elements to db
     *
     * @param aretefactElements ArtefactElements map
     */
    public void addNodeToGraphDB(Map<String, ArtefactElement> aretefactElements) {
        Transaction tx = graphDb.beginTx();
        try {

            Iterator<Entry<String, ArtefactElement>> iterator = aretefactElements
                    .entrySet().iterator();

            while (iterator.hasNext()) {

                Map.Entry pairs = iterator.next();
                ArtefactElement artefactElement = (ArtefactElement) pairs
                        .getValue();
                Label myLabel = DynamicLabel.label(artefactElement.getType());

                IndexManager index = graphDb.index();
                Index<Node> artefacts = index.forNodes("ArtefactElement");
                Index<Relationship> edges = index.forRelationships(RelTypes.SUB_ELEMENT.name());

                IndexHits<Node> hits = artefacts.get("ID",
                        artefactElement.getArtefactElementId());
                Node node = hits.getSingle();
                if (node == null) {
                    Node n = graphDb.createNode();

                    n.addLabel(myLabel);
                    n.setProperty("ID", artefactElement.getArtefactElementId());
                    n.setProperty("Name", artefactElement.getName());
                    n.setProperty("Type", artefactElement.getType());
                    artefacts.add(n, "ID", n.getProperty("ID"));
                    List<ArtefactSubElement> artefactsSubElements = artefactElement
                            .getArtefactSubElements();

                    for (int i = 0; i < artefactsSubElements.size(); i++) {
                        IndexHits<Node> subElement_hits = artefacts.get("ID", artefactsSubElements.get(i).getSubElementId());
                        Node subNode = subElement_hits.getSingle();
                        if (subNode == null) {
                            Node m = graphDb.createNode();
                            ArtefactSubElement temp = artefactsSubElements.get(i);
                            myLabel = DynamicLabel.label(temp.getType());
                            m.addLabel(myLabel);
                            m.setProperty("ID", temp.getSubElementId());
                            m.setProperty("Name", temp.getName());
                            m.setProperty("Type", temp.getType());

                            if (null != temp.getVisibility()) {
                                m.setProperty("Visibility", temp.getVisibility());
                            }
                            if (temp.getType().equalsIgnoreCase("UMLOperation")
                                    || temp.getType().equalsIgnoreCase("Method")) {
                                MethodModel mtemp = (MethodModel) temp;
                                if (null != mtemp.getReturnType()) {
                                    m.setProperty("Return Type",
                                            mtemp.getReturnType());
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
                                }
                                if (null != mtemp.getContent()) {
                                    m.setProperty("Content", mtemp.getContent());
                                }
                            } else if (temp.getType().equalsIgnoreCase(
                                    "UMLAttribute")
                                    || temp.getType().equalsIgnoreCase("Field")) {
                                AttributeModel mtemp = (AttributeModel) temp;
                                if (null != mtemp.getVariableType()) {
                                    m.setProperty("Variable Type",
                                            mtemp.getVariableType());
                                }

                            }
                            artefacts.add(m, "ID", m.getProperty("ID"));

                            relationship = n.createRelationshipTo(m,
                                    RelTypes.SUB_ELEMENT);
                            relationship.setProperty("message",
                                    RelTypes.SUB_ELEMENT.getValue());
                            edges.add(relationship, "ID", n.getProperty("ID") + "-" + m.getProperty("ID"));
                        }
                    }
                } else {
                    if (!node.getProperty("Name").equals(
                            artefactElement.getName())) {
                        System.out.println("Node name updated");
                    } else if (!node.getProperty("Type").equals(
                            artefactElement.getType())) {
                        System.out.println("Node type updated");
                    } else {
                        Iterator<Relationship> relations = node
                                .getRelationships(RelTypes.SUB_ELEMENT)
                                .iterator();
                        List<ArtefactSubElement> artefactsSubElements = artefactElement
                                .getArtefactSubElements();

                        while (relations.hasNext()) {
                            Node test = relations.next().getOtherNode(node);
                            for (int i = 0; i < artefactsSubElements.size(); i++) {
                                if (test.getProperty("ID").equals(
                                        artefactsSubElements.get(i)
                                        .getSubElementId())) {
                                    System.out
                                            .println("SubElement already exists.....");
                                    break;
                                }
                            }
                        }
                        System.out.println("Node already exists.....");
                    }
                }
            }
            tx.success();

        } finally {
            tx.finish();
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
                System.out.print(i + ": " + relation.get(i));
                Node source = hits.getSingle();
                String message = relation.get(++i);
                RelTypes relType = RelTypes.parseEnum(message);
                System.out.print(" -------- " + message);
                hits = artefacts.get("ID", relation.get(++i));
                System.out.println(" " + relation.get(i));
                Node target = hits.getSingle();

                if (null != source && null != target) {

                    Iterator<Relationship> relations = source.getRelationships()
                            .iterator();
                    boolean exist = false;
                    while (relations.hasNext()) {
                        if (relations.next().getOtherNode(source).equals(target)) {
                            exist = true;
                            System.out.println("Relationship already exists.....");
                        }
                    }
                    if (!exist) {
                        relationship = source.createRelationshipTo(target, relType);
                        relationship.setProperty("message", message);
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

                if (null != source && null != target) {

                    Iterator<Relationship> relations = source.getRelationships()
                            .iterator();
                    boolean exist = false;
                    while (relations.hasNext()) {
                        if (relations.next().getOtherNode(source).equals(target)) {
                            exist = true;
                            System.out.println("Relationship already exists.....");
                        }
                    }
                    if (!exist) {
                        relationship = source.createRelationshipTo(target,
                                relType);
                        relationship.setProperty("message",
                                relType.getValue());
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

                Label myLabel = DynamicLabel.label(requirement.getType());

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
                    n.setProperty("Content", requirement.getContent());
                    n.setProperty("Priority", requirement.getPriority());
                    n.setProperty("Title", requirement.getTitle());
                    artefacts.add(n, "ID", n.getProperty("ID"));
                } else {
                    if (!node.getProperty("Name").equals(
                            requirement.getName())) {
                        System.out.println("Node name updated");
                    } else if (!node.getProperty("Type").equals(
                            requirement.getType())) {
                        System.out.println("Node type updated");
                    } else {
                        System.out.println("Node already exists.....");
                    }
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
