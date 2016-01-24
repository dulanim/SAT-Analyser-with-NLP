/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.GUI;

import com.project.NLP.Requirement.ClassRelation;
import com.project.NLP.Requirement.StoringArtefacts;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JComboBox;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author S. Shobiga
 */
public class ArtefactFrame extends JFrame {

    private static final int DEFAULT_WIDTH = 350;
    private static final int DEFAULT_HEIGHT = 400;
    private HashMap requirementObjects = new HashMap();
    private HashSet requirementRelationsObjects = new HashSet();
    public static boolean lock = true;
    protected JTree tree;

    public ArtefactFrame(HashMap output, HashSet outputRelations) {
        super();

        setTitle("Artefacts Confirmation");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLocationRelativeTo(null);
        //tree = jt;
        addTreeItems(output, outputRelations);
        makeButtons();
        getContentPane().add(tree);
        tree.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent event) {
                if (((event.getModifiers() & InputEvent.BUTTON3_MASK) != 0)
                        && (tree.getSelectionCount() > 0)) {
                    showMenu(event.getX(), event.getY());
                }
            }
        });

        add(new JScrollPane(tree));
    }

    protected void addTreeItems(HashMap output, HashSet outputRelations) {

        System.out.println("ADD TREE ITEMS .................");
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Artefacts");
        DefaultMutableTreeNode[] rootDummy = new DefaultMutableTreeNode[output.size()];
        DefaultMutableTreeNode classNames;
        DefaultMutableTreeNode attributes;
        DefaultMutableTreeNode methods;
        DefaultMutableTreeNode relationships;
        DefaultMutableTreeNode relationshipsGeneralization;
        DefaultMutableTreeNode relationshipsAssociations;

        HashSet attributeSet = null;
        HashSet methodSet = null;
        HashSet relationSet = null;
        HashSet relationGenSet = new HashSet();
        HashSet relationAssSet = new HashSet();

        Iterator outputIterator = output.keySet().iterator();
        while (outputIterator.hasNext()) {
            String classNameString = outputIterator.next().toString();
            StoringArtefacts store = (StoringArtefacts) output.get(classNameString);
            attributeSet = store.getAttributes();
            methodSet = store.getMethods();
            relationSet = store.getRelationships();

            attributes = new DefaultMutableTreeNode("Attributes");
            methods = new DefaultMutableTreeNode("Methods");
            relationships = new DefaultMutableTreeNode("Relationships");
            relationshipsGeneralization = new DefaultMutableTreeNode("Generalization");
            relationshipsAssociations = new DefaultMutableTreeNode("Association");

            classNames = new DefaultMutableTreeNode(classNameString);
            for (Object attributeItems : attributeSet) {
                attributes.add(new DefaultMutableTreeNode(attributeItems));
                System.out.println("attributes: ..." + attributeItems);
                classNames.add(attributes);

            }

            for (Object methodItems : methodSet) {
                methods.add(new DefaultMutableTreeNode(methodItems));
                System.out.println("methods: ..." + methodItems);
                classNames.add(methods);

            }

            Iterator relationsIterator = outputRelations.iterator();
            while (relationsIterator.hasNext()) {
                boolean status = false;
                Object relationshipItems = relationsIterator.next();
                ClassRelation rel = (ClassRelation) relationshipItems;
                Object childClass = rel.getChildElement();
                if (childClass.toString().equalsIgnoreCase(classNameString)) {

                    //relationships.add(new DefaultMutableTreeNode("Type - " + rel.getRelationType() + "-> Parent -" + rel.getParentElement()));
                    System.out.println("Relationships: " + "Type - " + rel.getRelationType() + "-> Parent -" + rel.getParentElement());

                    if (rel.getRelationType().equalsIgnoreCase("Generalization")) {
                        status = true;
                        relationshipsGeneralization.add(new DefaultMutableTreeNode("Parent ->" + rel.getParentElement()));
                        relationships.add(relationshipsGeneralization);

                    }
                    if (rel.getRelationType().equalsIgnoreCase("Association")) {
                        relationshipsAssociations.add(new DefaultMutableTreeNode("Parent ->" + rel.getParentElement()));
                        status = true;
                        relationships.add(relationshipsAssociations);

                    }

                }
                Object parentClass = rel.getParentElement();
                if (parentClass.toString().equalsIgnoreCase(classNameString)) {
                    //relationships.add(new DefaultMutableTreeNode("Type - " + rel.getRelationType() + "-> Parent -" + rel.getParentElement()));
                    System.out.println("Relationships: " + "Type - " + rel.getRelationType() + "-> Child -" + rel.getChildElement());

                    if (rel.getRelationType().equalsIgnoreCase("Generalization")) {
                        status = true;
                        relationshipsGeneralization.add(new DefaultMutableTreeNode("Child ->" + rel.getChildElement()));
                        relationships.add(relationshipsGeneralization);

                    }
                    if (rel.getRelationType().equalsIgnoreCase("Association")) {
                        relationshipsAssociations.add(new DefaultMutableTreeNode("Child ->" + rel.getChildElement()));
                        relationships.add(relationshipsAssociations);
                        status = true;
                    }
                }
                if (status) {
                    classNames.add(relationships);
                }
            }
            root.add(classNames);
        }
        tree = new JTree(root);
    }

    protected void showMenu(int x, int y) {
        JPopupMenu popup = new JPopupMenu();
        JMenuItem delete = new JMenuItem("Delete");
        JMenuItem add = new JMenuItem("Add");
        JMenuItem edit = new JMenuItem("Edit");

        TreePath path = tree.getSelectionPath();
        Object node = path.getLastPathComponent();
        /*if the node selection is root, then deny the permission to edit, add, and delete functionalities*/
        if (node == tree.getModel().getRoot()) {
            delete.setEnabled(false);
            add.setEnabled(false);
            edit.setEnabled(false);
        }
        /* if the node selection is leaf node, then only the delete permission is given*/
        if (tree.getModel().isLeaf(node)) {
            add.setEnabled(false);

        }
        /*checking whether 2nd level (artefacts -> classNames has 3 child 
         * namely attributes, methods, relationships. if yes deny the permission to add items
         */
        if (path.getPathCount() == 3) {
            edit.setEnabled(false);
        }
        if (path.getPathCount() == 2 && tree.getModel().getChildCount(node) == 3) {
            add.setEnabled(false);
        }
        if (path.getPathCount() == 3 && tree.getModel().getChildCount(node) == 0) {
            delete.setEnabled(true);
            add.setEnabled(true);
        }

        System.out.println(node.toString());
        System.out.println(path.getPathCount());

        popup.add(delete);
        popup.add(add);
        popup.add(edit);
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                deleteSelectedItems();
            }
        });

        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                addItems();
            }
        });

        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                editSelectedItems();
            }
        });
        popup.show(tree, x, y);
    }

    protected void deleteSelectedItems() {
        DefaultMutableTreeNode node;
        DefaultTreeModel model = (DefaultTreeModel) (tree.getModel());
        TreePath[] paths = tree.getSelectionPaths();
        for (int i = 0; i < paths.length; i++) {
            node = (DefaultMutableTreeNode) (paths[i].getLastPathComponent());
            model.removeNodeFromParent(node);
        }
    }

    protected void editSelectedItems() {
        DefaultTreeModel model = (DefaultTreeModel) (tree.getModel());
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
                .getLastSelectedPathComponent();

        if (node == null) {
            return;
        }

        TreePath path = tree.getSelectionPath();
        Object nodes = path.getLastPathComponent();
        Object root = tree.getModel().getRoot();
        /*if the selected node is the leaf node of relations*/
        if (node.getParent().getParent().toString().equalsIgnoreCase("Relationships")) {
            validateRelations_Edit(node);
        }

        /*if selected node is the associations or generalization relationship*/
        if (node.toString().equalsIgnoreCase("Generalization") || (node.toString().equalsIgnoreCase("Association"))) {
            validateAss_Gen_Edit(node);
        }
        JFrame frame = new JFrame("Edit Window");

    }

    /*method to handle when the association or generalization type is clicked for changes
     */
    protected void validateAss_Gen_Edit(DefaultMutableTreeNode node) {
        TreePath path = tree.getSelectionPath();
        Object nodes = path.getLastPathComponent();

        Object root = tree.getModel().getRoot();
        int childOfRoot = tree.getModel().getChildCount(root);
        Object[] childOfRootObjectArray = new Object[childOfRoot];

        Object[] relOptions = {"Select the type", "Association", "Generalization"};
        JComboBox typeField = new JComboBox(relOptions);
        Object[] message = {
            "Enter the type (Parent/Child):", typeField,};
        /* get the user's input. note that if they press Cancel, 'name' will be null*/

        int option = JOptionPane.showConfirmDialog(null, message, "Enter the type", JOptionPane.OK_CANCEL_OPTION);
        String selectedType = typeField.getSelectedItem().toString();
        if (option == JOptionPane.OK_OPTION) {
            System.out.println(selectedType + " type: " + relOptions[0].toString());
            if (!(selectedType.equalsIgnoreCase(relOptions[0].toString()))) {
                try {
                    node.setUserObject(selectedType);
                    TreePath pathSelected = tree.getSelectionPath();
                    Object n = pathSelected.getLastPathComponent();
                    System.out.println("new node:" + node.toString());
                    ((DefaultTreeModel) tree.getModel()).nodeChanged(node);
                    updateAss_Gen_Edit(selectedType, node);

                } catch (Exception e) {
                    System.out.println("Exception occurs in GUI: (ignore it)" + e);
                }

            }
        }

    }
    /*method to update the changes in both classes when edit is pressed in any of the relationship type*/

    protected void updateAss_Gen_Edit(String selectedType, DefaultMutableTreeNode node) {
        Object newClass = null;
        Object currentParentClass = node.getParent(); // parent class of the selected node -> className
        Object selectedNodeParentClass = node.getParent().getParent();
                
        int relCount = node.getChildCount();
        boolean relationshipExist = false;
        boolean relationshipTypeExist = false;
        boolean relLeafExist = false;
        Object relationShipsNode = null;
        int assGenCount = 0;
        Object typeNode = null;
        Object type = null;
        Object newClassNode = null;
        DefaultMutableTreeNode typeNodeChange = null;
        for (int rCount = 0; rCount < relCount; rCount++) {

            Object leaf = node.getChildAt(rCount);
            newClass = leaf.toString().split("->")[1];
            newClassNode = getObjectNode(newClass);
            int childClassCount = tree.getModel().getChildCount(newClassNode);
            for (int cCount = 0; cCount < childClassCount; cCount++) {

                System.out.println(tree.getModel().getChild(newClassNode, cCount).toString());
                if (tree.getModel().getChild(newClassNode, cCount).toString().equalsIgnoreCase("Relationships")) {
                    relationshipExist = true;
                    relationShipsNode = tree.getModel().getChild(newClassNode, cCount);
                    System.out.println(relationShipsNode.toString());
                    break;
                }
            }
            if (relationshipExist) {
                assGenCount = tree.getModel().getChildCount(relationShipsNode);
                System.out.println(assGenCount);
                for (int aCount = 0; aCount < assGenCount; aCount++) {
                    typeNode = tree.getModel().getChild(relationShipsNode, aCount);
                    System.out.println("typeNode " + typeNode.toString() + "  node: " + node.toString());
                    if (!(typeNode.toString().equalsIgnoreCase(node.toString()))) {
                        System.out.println("typeNode " + typeNode.toString());
                        type = tree.getModel().getChild(typeNode, aCount);
                        typeNodeChange = (DefaultMutableTreeNode) (typeNode);
                        
                        relationshipTypeExist = true;
                        break;
                    }
                }
            }
            if (relationshipTypeExist) {
                int leafCount = tree.getModel().getChildCount(typeNode);
                if (leafCount == 1) {
                    System.out.println("relationship found");
                    typeNodeChange.setUserObject(node.toString());
                } else {
                    for (int lCount = 0; lCount < leafCount; lCount++) {
                        Object leafNode = tree.getModel().getChild(typeNode, lCount);
                        String[] leafString = leafNode.toString().split("->");
                        System.out.println("leaf String: "+ leafString[1]);
                        System.out.println("new classNode: "+ selectedNodeParentClass.toString());
                        if (leafString[1].equalsIgnoreCase(selectedNodeParentClass.toString())) {
                            /*remove the node*/
                            DefaultMutableTreeNode nodess = (DefaultMutableTreeNode) (leafNode);
                            System.out.println("nodess 3: " + nodess);
                            ((DefaultTreeModel) tree.getModel()).removeNodeFromParent(nodess);
                            /*add the node with the type*/
                            addItems(relationShipsNode, node.toString(), 0);
                            Object newLeafNode = tree.getModel().getChild(relationShipsNode, 0);
                            System.out.println("newleafNode: "+ newLeafNode);
                            addItems(newLeafNode, leafNode.toString(), 0);
                            break;
                        }
                    }
                }
                
                ((DefaultTreeModel) tree.getModel()).nodeChanged(typeNodeChange);
                System.out.println("typeNode: "+ typeNodeChange);
            }

        }
    }

    /*method to change the relationships and to remove the relationships when edit is clicked
     */
    protected void validateRelations_Edit(DefaultMutableTreeNode node) {
        TreePath path = tree.getSelectionPath();
        Object nodes = path.getLastPathComponent();

        Object root = tree.getModel().getRoot();
        int childOfRoot = tree.getModel().getChildCount(root);
        Object[] childOfRootObjectArray = new Object[childOfRoot];

        /*get the current class stored in the leaf node */
        String currentClass = nodes.toString().split("->")[1];

        /*if the selected node is leaf node - relations*/
        if (tree.getModel().isLeaf(nodes)) {
            String classNamess = node.getParent().getParent().getParent().toString();
            System.out.println("cllllllllllllllllaaaaaaassssssss" + classNamess);
            String classNameOfSelectedNode = path.getParentPath().getParentPath().getParentPath().toString().split("\\W")[3].trim();
            System.out.println("classnnnnaaameess: " + classNameOfSelectedNode);
            childOfRootObjectArray[0] = "Select the class name";
            for (int childCount = 0, arrayCount = 1; childCount < childOfRoot; childCount++) {
                System.out.println(childCount + "  " + arrayCount);
                Object classNames = tree.getModel().getChild(root, childCount);
                if (!classNamess.equalsIgnoreCase(classNames.toString())) {
                    childOfRootObjectArray[arrayCount++] = tree.getModel().getChild(root, childCount);
                }
            }

        }

        Object[] typeFields = {"Select the type", "Parent", "Child"};
        /* get the user's input. note that if they press Cancel, 'name' will be null*/
        JComboBox typeField = new JComboBox(typeFields);
        JComboBox relationClass = new JComboBox(childOfRootObjectArray);
        Object[] message = {
            "Enter the type (Parent/Child):", typeField,
            "Enter the class name:", relationClass
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Enter all your values", JOptionPane.OK_CANCEL_OPTION);
        String selectedType = typeField.getSelectedItem().toString();
        String newArtefactName = relationClass.getSelectedItem().toString();
        if (option == JOptionPane.OK_OPTION) {
            System.out.println(selectedType + " type: " + typeFields[0].toString());
            System.out.println(newArtefactName + " class :" + childOfRootObjectArray[0].toString());
            if (!(selectedType.equalsIgnoreCase(typeFields[0].toString()) || newArtefactName.equalsIgnoreCase(childOfRootObjectArray[0].toString()))) {
                try {
                    if (!newArtefactName.isEmpty()) {
                        changeTheRelationInOtherClass_Edit(nodes, node);

                        node.setUserObject(selectedType + " ->" + newArtefactName);
                        TreePath pathSelected = tree.getSelectionPath();
                        Object n = pathSelected.getLastPathComponent();
                        ((DefaultTreeModel) tree.getModel()).nodeChanged(node);

                        update_Edit(selectedType, newArtefactName, node);
                    }
                } catch (Exception e) {
                    System.out.println("Exception occurs in GUI: (ignore it)" + e);
                    e.printStackTrace();
                }

            }
        }

    }

    /*
     when the relationship is changed in one node, find the respective node in the parent class of the changed
     relationship and remove that node
     */
    protected void changeTheRelationInOtherClass_Edit(Object nodes, DefaultMutableTreeNode node) {
        /*get the current type and class */
        String nodeArray[] = nodes.toString().split("->");
        String currentType = nodeArray[0];
        String currentRelationClass = nodeArray[1];
        Object currentRelationClass_object = null;
        boolean nodeExists = false;
        Object leafRel = null;
        int leafRelCount = 0;
        Object relType = null;
        int relCount = 0;
        Object rel = null;
        boolean relationshipExist = false;
        boolean relationTypeExist = false;
        Object currentParentClass = node.getParent().getParent().getParent();
        /*traverse to the class where the the selected node's class exist*/

        currentRelationClass_object = getObjectNode(currentRelationClass);

        int childCountOfClass = tree.getModel().getChildCount(currentRelationClass_object);
        System.out.println("childCountOfClass: " + childCountOfClass);

        for (int childCount = 0; childCount < childCountOfClass; childCount++) {
            rel = tree.getModel().getChild(currentRelationClass_object, childCount);
            if (rel.toString().equalsIgnoreCase("Relationships")) {
                relationshipExist = true;
                break;
            }
        }
        relCount = tree.getModel().getChildCount(rel);
        if (relationshipExist) {
            for (int rCount = 0; rCount < relCount; rCount++) {
                /*checking whether current and previous types are same*/
                System.out.println("count: " + rCount);
                relType = tree.getModel().getChild(rel, rCount);
                System.out.println("reltype: " + relType);
                if (node.getParent().toString().equalsIgnoreCase(relType.toString())) {
                    relationTypeExist = true;
                    break;
                }
            }
        }

        leafRelCount = tree.getModel().getChildCount(relType);
        if (relationTypeExist) {
            for (int leafCount = 0; leafCount < leafRelCount; leafCount++) {
                leafRel = tree.getModel().getChild(relType, leafCount);
                String[] rmvLeaf = leafRel.toString().split("->");

                if (rmvLeaf[1].equalsIgnoreCase(currentParentClass.toString())) {
                    nodeExists = true;
                    deleteNodes(nodeExists, leafRel, leafRelCount, relType, relCount, rel);
                    break;
                }
            }
        }
        System.out.println("bbbbbreeeeeeeeeeeaaaaaaaaakkkkk");

    }

    /*delete a particular node in relation leaf*/
    private void deleteNodes(boolean nodeExists, Object leafRel, int leafRelCount, Object relType, int relCount, Object rel) {
        if (nodeExists) {
            DefaultMutableTreeNode nodess = (DefaultMutableTreeNode) (leafRel);
            System.out.println("leafRel : " + leafRel);
            System.out.println("nodess: " + nodess);
            ((DefaultTreeModel) tree.getModel()).removeNodeFromParent(nodess);

            if (leafRelCount == 1) {
                /*remove the relation type heading*/
                nodess = (DefaultMutableTreeNode) (relType);
                System.out.println("nodess 2: " + nodess);
                ((DefaultTreeModel) tree.getModel()).removeNodeFromParent(nodess);

            }
            if (relCount == 1 && leafRelCount == 1) {
                /*remove the relation heading*/
                nodess = (DefaultMutableTreeNode) (rel);
                System.out.println("nodess 3: " + nodess);
                ((DefaultTreeModel) tree.getModel()).removeNodeFromParent(nodess);

            }

            System.out.println("dsfsfsfsfsjhgkdjfhgjkdghkd");

        }
    }

    protected void update_Edit(String selectedType, String newArtefactName, DefaultMutableTreeNode node) {

        String currentType = selectedType;
        String currentRelationClass = newArtefactName;
        Object currentRelationClass_object = null;

        Object currentParentClass = node.getParent().getParent().getParent();
        /*traverse to the class where the the selected node's class exist*/
        currentRelationClass_object = getObjectNode(currentRelationClass);

        //currentparentClass
        int childCountOfClass = tree.getModel().getChildCount(currentRelationClass_object);

        System.out.println("childCountOfClass: " + childCountOfClass);

        if (selectedType.equalsIgnoreCase("Parent")) {
            selectedType = "Child";
        } else {
            selectedType = "Parent";
        }
        Object rel = null;
        Object relType = null;
        boolean relationshipExists = false;
        boolean relationTypeExists = false;
        int relCount = 0;
        for (int childCount = 0; childCount < childCountOfClass; childCount++) {
            rel = tree.getModel().getChild(currentRelationClass_object, childCount);
            if (rel.toString().equalsIgnoreCase("Relationships")) {
                relationshipExists = true;
                relCount = tree.getModel().getChildCount(rel);
                break;
            }
        }
        if (relationshipExists) {
            //checks whether next level of node exists
            System.out.println("relationship node exists ddddddddddddddddddddddddddd");
            for (int rCount = 0; rCount < relCount; rCount++) {
                /*checking whether current and previous types are same*/
                relType = tree.getModel().getChild(rel, rCount);
                if (node.getParent().toString().equalsIgnoreCase(relType.toString())) {
                    relationTypeExists = true;
                    break;
                }
            }
            if (relationTypeExists) {
                System.out.println("generalzation or associatiat fsdjfksjdfjddddddddddddd");
                int leafRelCount = tree.getModel().getChildCount(relType);
                //new leaf node added
                addItems(relType, selectedType + " ->" + currentParentClass, leafRelCount);

            } else {
                //add association or generalization node
                addItems(rel, node.getParent().toString(), 0);
                //add the leaf node
                relType = tree.getModel().getChild(rel, 0);
                addItems(relType, selectedType + " ->" + currentParentClass, 0);

            }

        } else {
            //add the relationships
            Object clNode = getObjectNode(newArtefactName);
            addItems(clNode, "Relations", childCountOfClass);
            //add the association or generalization
            System.out.println("enddfsddddddddddddddddd");
            rel = tree.getModel().getChild(clNode, childCountOfClass);
            System.out.println(rel);
            addItems(rel, node.getParent().toString(), 0);

            //add the leaf
            relType = tree.getModel().getChild(rel, 0);
            addItems(relType, selectedType + " ->" + currentParentClass, 0);

        }

    }

    protected void addItems(Object node, Object msg, int index) {
        DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) (node);
        DefaultTreeModel model = (DefaultTreeModel) (tree.getModel());
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(msg);
        model.insertNodeInto(newNode, parentNode, index);

    }

    protected Object getObjectNode(Object obj) {
        Object r = tree.getModel().getRoot();
        int rootChild = tree.getModel().getChildCount(r);
        for (int rChild = 0; rChild < rootChild; rChild++) {
            Object classN = tree.getModel().getChild(r, rChild);
            if (classN.toString().equalsIgnoreCase(obj.toString())) {
                obj = classN;
            }
        }
        return obj;
    }

    protected void addItems() {
        DefaultTreeModel model = (DefaultTreeModel) (tree.getModel());
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
                .getLastSelectedPathComponent();

        if (node == null) {
            return;
        }
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("New");
        model.insertNodeInto(newNode, node, node.getChildCount());

        // now display new node
        TreeNode[] nodes = model.getPathToRoot(newNode);
        TreePath path = new TreePath(nodes);
        tree.scrollPathToVisible(path);

    }

    public void storeItems() {

        Object rootArtefact = tree.getModel().getRoot();
        int childCountOfRRoot = tree.getModel().getChildCount(rootArtefact);
        for (int child = 0; child < childCountOfRRoot; child++) {
            HashSet classNameSet = new HashSet();
            HashSet attributeSet = new HashSet();
            HashSet methodSet = new HashSet();
            HashSet relationSet = new HashSet();

            TreeModel classTreeModel = tree.getModel();
            Object className = classTreeModel.getChild(rootArtefact, child);
            System.out.println("classNames: " + className.toString());
            classNameSet.add(className.toString());

            int artefactCount = classTreeModel.getChildCount(className);

            for (int artCount = 0; artCount < artefactCount; artCount++) {
                Object artefactName = classTreeModel.getChild(className, artCount);
                System.out.println("artefact element: " + artefactName.toString());
                if (artefactName.toString().equalsIgnoreCase("Attributes")) {
                    int attributeCount = classTreeModel.getChildCount(artefactName);
                    System.out.println("attributeCount : " + attributeCount);

                    for (int atrCount = 0; atrCount < attributeCount; atrCount++) {
                        Object attrName = classTreeModel.getChild(artefactName, atrCount);
                        System.out.println("attrName: " + attrName.toString());
                        attributeSet.add(attrName.toString());
                    }

                }
                if (artefactName.toString().equalsIgnoreCase("Methods")) {
                    int methodCount = classTreeModel.getChildCount(artefactName);
                    System.out.println("methodCount : " + methodCount);
                    for (int mCount = 0; mCount < methodCount; mCount++) {
                        Object methodName = classTreeModel.getChild(artefactName, mCount);
                        System.out.println("methods: " + methodName.toString());
                        methodSet.add(methodName.toString());
                    }
                }
                if (artefactName.toString().equalsIgnoreCase("Relationships")) {
                    int relationCount = classTreeModel.getChildCount(artefactName);
                    System.out.println("relationCount : " + relationCount);

                    for (int rCount = 0; rCount < relationCount; rCount++) {
                        Object relName = classTreeModel.getChild(artefactName, rCount);
                        if (relName.toString().equalsIgnoreCase("Generalization")) {
                            int genCount = classTreeModel.getChildCount(relName);
                            for (int gCount = 0; gCount < genCount; gCount++) {
                                Object genName = classTreeModel.getChild(relName, gCount);
                                String[] genNameArray = genName.toString().split("->");
                                System.out.println("string[0]: " + genNameArray[0] + " String [1]: " + genNameArray[1]);
                                String type = genNameArray[0].trim();
                                String parent = genNameArray[1].trim();
                                String childs = className.toString();
                                if (type.equalsIgnoreCase("Parent")) {
                                    ClassRelation clRelation = new ClassRelation(type, childs, parent);
                                    System.out.println(clRelation.getRelationType() + " " + clRelation.getChildElement() + "->" + clRelation.getParentElement());
                                    requirementRelationsObjects.add(clRelation);
                                }

                            }
                        }
                        if (relName.toString().equalsIgnoreCase("Association")) {
                            int assCount = classTreeModel.getChildCount(relName);
                            for (int aCount = 0; aCount < assCount; aCount++) {
                                Object assName = classTreeModel.getChild(relName, aCount);
                                String[] assNameArray = assName.toString().split("->");
                                System.out.println("string[0]: " + assNameArray[0] + " String [1]: " + assNameArray[1]);

                                if (assNameArray[0].equalsIgnoreCase("Parent")) {
                                    ClassRelation clRelation = new ClassRelation(assNameArray[0], className.toString(), assNameArray[1]);
                                    System.out.println(clRelation.getRelationType() + " " + clRelation.getChildElement() + "->" + clRelation.getParentElement());
                                    requirementRelationsObjects.add(clRelation);
                                }

                            }

                        }

                    }

//                    for(int  rCount = 0; rCount < relationCount; rCount++) {
//                        Object relationshipName = classTreeModel.getChild(artefactName, rCount);
//                        ClassRelation rel = (ClassRelation) relationshipName;
//                        
//                    }
//                    for (int rCount = 0; rCount < relationCount; rCount++) {
//                        Object relationshipName = classTreeModel.getChild(artefactName, rCount);
//                        ClassRelation rel = (ClassRelation) relationshipName;
//                        System.out.println("relationships: " + relationshipName.toString());
//                        System.out.println("Type - " + rel.getRelationType() + "-> Parent -" + rel.getParentElement());
//                        relationSet.add("Type - " + rel.getRelationType() + "-> Parent -" + rel.getParentElement());
//                    }
                }

            }
            StoringArtefacts storingArtefacts = new StoringArtefacts();
            storingArtefacts.addClassName(classNameSet);
            storingArtefacts.addAttributes(attributeSet);
            storingArtefacts.addMethods(methodSet);
            storingArtefacts.addRelationships(relationSet);

            requirementObjects.put(className.toString(), storingArtefacts);

        }

    }

    public HashMap getRequirementobjects() {
        return requirementObjects;
    }

    public HashSet getRequirementRelationsObject() {
        System.out.println("Relations..................................ssss");
        if (requirementRelationsObjects.isEmpty()) {
            System.out.println("EMPTY");

        } else {
            System.out.println("Not EMPTY");
        }
        return requirementRelationsObjects;
    }

    private void makeButtons() {
        JPanel panel = new JPanel();
        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                storeItems();
                lock = false;
                dispose();

            }
        });
        panel.add(confirmButton);
        add(panel, BorderLayout.SOUTH);

    }

    protected void print(HashMap output) {
        System.out.println("PRINTING........");
        Iterator it = output.keySet().iterator();
        while (it.hasNext()) {

            String className = it.next().toString();
            System.out.println("className :" + className);
            StoringArtefacts store = (StoringArtefacts) output.get(className);
            HashSet attributes = store.getAttributes();
            HashSet methods = store.getMethods();
            HashSet relations = store.getRelationships();
            System.out.println("attributes :");
            for (Object attribute : attributes) {
                System.out.println(attribute.toString());
            }
            System.out.println("methods");
            for (Object method : methods) {
                System.out.println(method.toString());
            }
            System.out.println("Relations");

            for (Object relation : relations) {
                ClassRelation rel = (ClassRelation) relation;
                System.out.println("Type - " + rel.getRelationType() + "-> Parent -" + rel.getParentElement());
                //System.out.println("Type - " + rel.getRelationType() + "-> Parent -" + rel.getParentElement());
            }

        }

    }
}
