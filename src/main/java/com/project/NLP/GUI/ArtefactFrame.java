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
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * HAVE TO IMPROVE THE CODE.. INTERMS OF FUNCTIONALITY AND CODE
 * 
 * This a class which outputs a frame for the user interaction. Output of
 * artefacts will be displayed as tree and user can add, delete or edit any
 * nodes if they expect a modification.
 *
 * @author S. Shobiga
 */
public class ArtefactFrame extends JFrame {

    private static final int DEFAULT_WIDTH = 350; // width of the frame
    private static final int DEFAULT_HEIGHT = 400; // height of the frame
    private HashMap requirementObjects = new HashMap(); // requirementObject map to keep the artefacts
    private HashSet requirementRelationsObjects = new HashSet(); // map to keep the relationships
    public static boolean lock = true; // lock to execute the shared resource
    protected JTree tree; // tree items

    /**
     * constructor with parameters
     *
     * @param output: hash map which consists the artefacts details key:
     * className, value: classRelationIdentifier
     * @param outputRelations: hashMap which consists the relationship details
     * Key: className value: list of relationships
     */
    public ArtefactFrame(HashMap output, HashSet outputRelations) {
        super();
        setTitle("Artefacts Confirmation");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLocationRelativeTo(null);
        //call the method to add the items in the frame
        addTreeItems(output, outputRelations);
        //call the method to add the buttons
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

    /**
     * method to add the artefact results in the frame in a tree format
     *
     * @param output: hash map which consists the artefacts details key:
     * className, value: classRelationIdentifier
     * @param outputRelations: hashMap which consists the relationship details
     * Key: className value: list of relationships
     */
    protected void addTreeItems(HashMap output, HashSet outputRelations) {

        System.out.println("ADDING ARTEFACTS IN TREE FORMAT");
        DefaultMutableTreeNode root = null;
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
        if (!output.isEmpty()) {
            //set the root node of the tree as 'artefacts'
            root = new DefaultMutableTreeNode("Artefacts");
            Iterator outputIterator = output.keySet().iterator();
            while (outputIterator.hasNext()) {
                String classNameString = outputIterator.next().toString();
                StoringArtefacts store = (StoringArtefacts) output.get(classNameString);
                attributeSet = store.getAttributes();
                methodSet = store.getMethods();
                relationSet = store.getRelationships();

                //initializing defaultMutableTreeNodes
                attributes = new DefaultMutableTreeNode("Attributes");
                methods = new DefaultMutableTreeNode("Methods");
                relationships = new DefaultMutableTreeNode("Relationships");
                relationshipsGeneralization = new DefaultMutableTreeNode("Generalization");
                relationshipsAssociations = new DefaultMutableTreeNode("Association");
                classNames = new DefaultMutableTreeNode(classNameString);

                //add the items in the artefact set to the defaultMutableTreeNode to create the tree
                setDefaultMutableTreeNodeToArtefacts(attributeSet, attributes, classNames);
                setDefaultMutableTreeNodeToArtefacts(methodSet, methods, classNames);

                //add the relationship items in the outputRelations map to the defaultMutableTreeNode
                setDefaultMutableTreeNodeToRelationships(outputRelations, classNameString, relationshipsGeneralization, relationships, relationshipsAssociations, classNames);
                //add the classNodes to the root
                root.add(classNames);
            }
        }
        if (root != null) {
            //add root to the tree
            tree = new JTree(root);
        }
    }

    /**
     * add the relationship items in the outputRelations map to the
     * defaultMutableTreeNode
     *
     * @param outputRelations
     * @param classNameString
     * @param relationshipsGeneralization
     * @param relationships
     * @param relationshipsAssociations
     * @param classNames
     */
    private void setDefaultMutableTreeNodeToRelationships(HashSet outputRelations, String classNameString, DefaultMutableTreeNode relationshipsGeneralization, DefaultMutableTreeNode relationships, DefaultMutableTreeNode relationshipsAssociations, DefaultMutableTreeNode classNames) {
        Iterator relationsIterator = outputRelations.iterator();
        while (relationsIterator.hasNext()) {
            boolean status = false;
            Object relationshipItems = relationsIterator.next();
            ClassRelation rel = (ClassRelation) relationshipItems;
            Object childClass = rel.getChildElement();
            if (childClass.toString().equalsIgnoreCase(classNameString)) {
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
    }

    /**
     * add the items in the artefact set to the defaultMutableTreeNode to create
     * the tree
     *
     * @param artefactSet
     * @param artefacts
     * @param classNames
     */
    private void setDefaultMutableTreeNodeToArtefacts(HashSet artefactSet, DefaultMutableTreeNode artefacts, DefaultMutableTreeNode classNames) {
        for (Object attributeItems : artefactSet) {
            artefacts.add(new DefaultMutableTreeNode(attributeItems));
            classNames.add(artefacts);
        }
    }

    /**
     * method for the pop up menu when a user clicks on a particular node
     *
     * @param x
     * @param y
     */
    protected void showMenu(int x, int y) {
        JPopupMenu popup = new JPopupMenu();
        //initializing the menu items
        JMenuItem delete = new JMenuItem("Delete");
        JMenuItem add = new JMenuItem("Add");
        JMenuItem edit = new JMenuItem("Edit");

        TreePath path = tree.getSelectionPath();
        Object node = path.getLastPathComponent();

        //if the node selection is root, then deny the permission to edit, add, and delete functionalities
        if (node == tree.getModel().getRoot()) {
            delete.setEnabled(false);
            add.setEnabled(false);
            edit.setEnabled(false);
        }

        //if the node selection is leaf node, then add permission is denied
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

        popup.add(delete);
        popup.add(add);
        popup.add(edit);
        //action listener for the delete
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                deleteSelectedItems();
            }
        });
        //action listener for the add
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                addItems();
            }
        });
        //action listener for the edit
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                editSelectedItems();
            }
        });
        popup.show(tree, x, y);
    }

    /**
     * method to delete the node when the node is selected and from the pop up
     * selection delete operation differs according to the hierachy level in the
     * treee node
     *
     */
    protected void deleteSelectedItems() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree
                .getLastSelectedPathComponent();
        DefaultTreeModel model = (DefaultTreeModel) (tree.getModel());
        TreeNode parent = node.getParent();
        TreePath[] paths = tree.getSelectionPaths();
        //if the node selection is leaf of the relationship 
        try {
            if (node.getParent().getParent().toString().equalsIgnoreCase("Relationships")) {
                System.out.println("11");
                changeTheRelationInOtherClass_Edit(tree.getSelectionPath().getLastPathComponent(), node);

            }
            //if the selected node is association/generalization
            if (node.getParent().toString().equalsIgnoreCase("Relationships")) {
                System.out.println("in");
                //updateAss_Gen_Edit(node.toString(), node, "Delete");
                //changeTheRelationInOtherClass_Edit(tree.getSelectionPath().getLastPathComponent(), node);
            }
            //if the selected node is classes
            if (node.getParent().toString().equalsIgnoreCase("Artefacts")) {

            }
            //if the selected node is any other node of the the tree
            for (int i = 0; i < paths.length; i++) {
                node = (DefaultMutableTreeNode) (paths[i].getLastPathComponent());
                model.removeNodeFromParent(node);
            }
            while (parent.getChildCount() == 0) {
                DefaultMutableTreeNode nodess = (DefaultMutableTreeNode) (parent);
                parent = parent.getParent();
                ((DefaultTreeModel) tree.getModel()).removeNodeFromParent(nodess);
            }
        } catch (Exception e) {
            System.out.println("Exception occurs: " + e);
        }
    }

    /**
     * method to edit the selected items
     */
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

        //if selected node is the associations or generalization relationship
        int index = path.getPathCount();

        System.out.println("-----------------Index: " + index);
        if (node.toString().equalsIgnoreCase("Generalization") || (node.toString().equalsIgnoreCase("Association"))) {
            validateAss_Gen_Edit(node);
        } //if the selected node is leaf of the attributes or methods
        else if (node.getParent() != null) {
            if (node.getParent().toString().equalsIgnoreCase("Attributes") || node.getParent().toString().equalsIgnoreCase("Methods") || node.getParent().toString().equalsIgnoreCase("Artefacts")) {
                System.out.println("attr, method,  class");
                openEditFrame(node);
            } //if the selected node is the leaf node of relations
            else if (node.getParent().getParent() != null) {
                if (node.getParent().getParent().toString().equalsIgnoreCase("Relationships")) {
                    System.out.println("exe");
                    validateRelations_Edit(node);
                }
            }
        }

        JFrame frame = new JFrame("Edit Window");

    }

    /**
     * method to edit frame for the leaf nodes of attributes and methods. This
     * method gives the joptionPane to the users to get the input for the
     * changes
     *
     * @param node: selected node
     */
    protected void openEditFrame(DefaultMutableTreeNode node) {
        // prompt the user to enter their name
        String newArtefactName = JOptionPane.showInputDialog(null, "New Artefact name...");
        // get the user's input. note that if they press Cancel, 'name' will be null
        try {
            if (!newArtefactName.isEmpty()) {
                node.setUserObject(newArtefactName);
                TreePath pathSelected = tree.getSelectionPath();
                Object n = pathSelected.getLastPathComponent();
                ((DefaultTreeModel) tree.getModel()).nodeChanged(node);

            }
        } catch (Exception e) {
            System.out.println("Exception occurs in GUI: (ignore it)" + e);
        }

    }

    /**
     * method to handle when the association or generalization type is clicked
     * for changes when the node is clicked it gives the input from the users
     *
     * @param node: selected node
     */
    protected void validateAss_Gen_Edit(DefaultMutableTreeNode node) {
        Object[] relOptions = {"Select the type", "Association", "Generalization"};
        JComboBox typeField = new JComboBox(relOptions);
        Object[] message = {
            "Enter the type (Association/Generalization):", typeField,};
        /* get the user's input. note that if they press Cancel, 'name' will be null*/
        int option = JOptionPane.showConfirmDialog(null, message, "Enter the type", JOptionPane.OK_CANCEL_OPTION);
        String selectedType = typeField.getSelectedItem().toString();
        if (option == JOptionPane.OK_OPTION) {
            if (!(selectedType.equalsIgnoreCase(relOptions[0].toString()))) {
                try {
                    //node.setUserObject(selectedType);
                    //((DefaultTreeModel) tree.getModel()).nodeChanged(node);
                    //if the selected node is already exist within the parent of the selected node, then after transfering items from one node to an other, boolean result is return to denote the sibings are there
                    boolean sibilingExist = modifyCurrentNodeAss_Gen_Edit(selectedType, node, "Edit");
                    updateAss_Gen_Edit(selectedType, node, "Edit");
                    if (!sibilingExist) {
                        System.out.println("sibiling does not exist");
                        node.setUserObject(selectedType);
                        ((DefaultTreeModel) tree.getModel()).nodeChanged(node);
                    }

                    //after adding all the items to the other node, remove the current node
                    if (sibilingExist) {
                        System.out.println("removed");
                        ((DefaultTreeModel) tree.getModel()).removeNodeFromParent(node);
                    }

                } catch (Exception e) {
                    System.out.println("Exception occurs in GUI: (ignore it)" + e);
                }

            }
        }

    }

    /**
     * method to change the current node with the checking of whether the
     * association or generalization are already exist.
     *
     * @param selectedType
     * @param node
     * @param action
     */
    private boolean modifyCurrentNodeAss_Gen_Edit(String selectedType, DefaultMutableTreeNode node, String action) {
        boolean sibilingsExist = false;
        DefaultMutableTreeNode nextSibiling = null;
        DefaultMutableTreeNode previousSibiling = null;

        if (node.getNextSibling() != null) {
            nextSibiling = node.getNextSibling();
            if (nextSibiling.toString().equalsIgnoreCase(selectedType)) {
                sibilingsExist = true;
                System.out.println("next sibiling: " + nextSibiling.toString());
                int numberOfLeafs = node.getChildCount();
                //add the items in a node to an other node
                for (int leafCount = 0; leafCount < numberOfLeafs; leafCount++) {
                    //TreeNode leaf = node.getChildAt(leafCount);
                    Object leaf = tree.getModel().getChild(node, leafCount);
                    System.out.println("--------------leaf: " + leaf);
                    addItems(nextSibiling, leaf.toString(), 0);
                    System.out.println("added");

                }
                //after adding all the items to the other node, remove the current node
                //((DefaultTreeModel) tree.getModel()).removeNodeFromParent(node);
            }

        }
        if (node.getPreviousSibling() != null) {
            previousSibiling = node.getPreviousSibling();
            if (previousSibiling.toString().equalsIgnoreCase(selectedType)) {
                System.out.println("previous sibiling: " + previousSibiling.toString());
                sibilingsExist = true;
                System.out.println("previous sibiling: " + previousSibiling.toString());
                int numberOfLeafs = node.getChildCount();
                //add the items in a node to an other node
                for (int leafCount = 0; leafCount < numberOfLeafs; leafCount++) {
                    //TreeNode leaf = node.getChildAt(leafCount);
                    Object leaf = tree.getModel().getChild(node, leafCount);
                    addItems(previousSibiling, leaf.toString(), 0);

                }

            }
        } else {
            node.setUserObject(selectedType);
            ((DefaultTreeModel) tree.getModel()).nodeChanged(node);

        }
        return sibilingsExist;
    }

    /**
     * method to update the changes in both classes when edit is pressed in any
     * of the relationship type
     *
     * @param selectedType
     * @param node
     * @param action
     */
    protected void updateAss_Gen_Edit(String selectedType, DefaultMutableTreeNode node, String action) {
        Object newClass = null;
        Object selectedNodeParentClass = node.getParent().getParent();
        Object relationShipsNode = null;
        Object typeNode = null;
        Object type = null;
        Object newClassNode = null;
        Object leafNode = null;
        int assGenCount = 0;
        int relCount = node.getChildCount();
        boolean relationshipExist = false;
        boolean relationshipTypeExist = false;
        boolean relLeafExist = false;
        DefaultMutableTreeNode typeNodeChange = null;
        int assGenIndex = 0;
        System.out.println("child count of current node: " + relCount);
        for (int rCount = 0; rCount < relCount; rCount++) {
            System.out.println("1111111111111111111111");
            Object leaf = node.getChildAt(rCount);
            newClass = leaf.toString().split("->")[1];
            newClassNode = getObjectNode(newClass);

            int childClassCount = tree.getModel().getChildCount(newClassNode);
            for (int cCount = 0; cCount < childClassCount; cCount++) {

                if (tree.getModel().getChild(newClassNode, cCount).toString().equalsIgnoreCase("Relationships")) {
                    relationshipExist = true;
                    relationShipsNode = tree.getModel().getChild(newClassNode, cCount);
                    break;
                }
            }
            //if "Relaitonships" exist
            if (relationshipExist) {
                System.out.println("Relationship exist");
                assGenCount = tree.getModel().getChildCount(relationShipsNode);

                for (assGenIndex = 0; assGenIndex < assGenCount; assGenIndex++) {
                    typeNode = tree.getModel().getChild(relationShipsNode, assGenIndex);
                    //if (!(typeNode.toString().equalsIgnoreCase(node.toString()))) {
                    if (action.equalsIgnoreCase("Edit")) {
                        System.out.println("action edit");
                        typeNodeChange = (DefaultMutableTreeNode) (typeNode);
                        System.out.println("typenodechange: " + typeNodeChange);
                        System.out.println("typeNodeChange count :" + typeNodeChange.getSiblingCount());
                        System.out.println("if is running");
                        if (!(typeNode.toString().equalsIgnoreCase(node.toString()))) {
                            System.out.println("equal");
                            type = tree.getModel().getChild(typeNode, assGenIndex);
                            typeNodeChange = (DefaultMutableTreeNode) (typeNode);
                            relationshipTypeExist = true;
                            break;
                        }

                    }
                    if (action.equalsIgnoreCase("Delete")) {
                        System.out.println("eddd");
                        if ((typeNode.toString().equalsIgnoreCase(node.toString()))) {
                            type = tree.getModel().getChild(typeNode, assGenIndex);
                            typeNodeChange = (DefaultMutableTreeNode) (typeNode);
                            relationshipTypeExist = true;
                            break;
                        }
                    }

                }
                System.out.println("---------------ass gen node: " + assGenIndex);
            }
            System.out.println(action.equalsIgnoreCase("Edit"));
            if (relationshipTypeExist) {
                System.out.println("relationship type exist");
                int leafCount = tree.getModel().getChildCount(typeNode);
                if (leafCount == 1) {
                    System.out.println("delete 0");
                    if (action.equalsIgnoreCase("Edit")) {

                        //typeNodeChange.setUserObject(selectedType);
                        boolean sibilingExist = modifyCurrentNodeAss_Gen_Edit(selectedType, typeNodeChange, "edit");
                        if (sibilingExist) {
                            ((DefaultTreeModel) tree.getModel()).removeNodeFromParent(typeNodeChange);
                        } else {
                            System.out.println("normal " + selectedType);
                            typeNodeChange.setUserObject(selectedType);
                        }
                    }
                    if (action.equalsIgnoreCase("Delete")) {
                        System.out.println("delete");
                        //((DefaultTreeModel) tree.getModel()).removeNodeFromParent(typeNodeChange);
                        for (int lCount = 0; lCount < leafCount; lCount++) {
                            leafNode = tree.getModel().getChild(typeNode, lCount);
                            String[] leafString = leafNode.toString().split("->");
                            if (leafString[1].equalsIgnoreCase(selectedNodeParentClass.toString())) {
                                relLeafExist = true;
                                break;
                            }
                        }
                    }
                } else {
                    for (int lCount = 0; lCount < leafCount; lCount++) {
                        leafNode = tree.getModel().getChild(typeNode, lCount);
                        String[] leafString = leafNode.toString().split("->");
                        System.out.println("--------------Leaf node: " + leafNode);
                        if (leafString[1].equalsIgnoreCase(selectedNodeParentClass.toString())) {
                            relLeafExist = true;
                            break;
                        }
                    }
                }
            }
            if (relLeafExist) {
                System.out.println("delete 3");
                DefaultMutableTreeNode rela = null;
                /*remove the node*/
                DefaultMutableTreeNode nodess = (DefaultMutableTreeNode) (leafNode);
                //System.out.println(nodess.toString());
                if (action.equalsIgnoreCase("Edit")) {
                    ((DefaultTreeModel) tree.getModel()).removeNodeFromParent(nodess);
                }
                /*add the node with the type*/
                if (action.equalsIgnoreCase("Edit")) {
                    if (assGenIndex == 0) {
                        assGenIndex = 1;
                    } else {
                        assGenIndex = 0;
                    }
                    System.out.println("----------------relationship node: " + relationShipsNode);
                    if (relationshipTypeExist) {
                        boolean existType = false;
                        rela = (DefaultMutableTreeNode) (relationShipsNode);

                        int typeC = rela.getChildCount();
                        for (int typeCount = 0; typeCount < typeC; typeCount++) {
                            if (rela.getChildAt(typeCount).toString().equalsIgnoreCase(selectedType)) {
                                existType = true;
                                break;
                            }
                        }
                        if (!existType) {
                            System.out.println("relationshp exist");
                            addItems(relationShipsNode, node.toString(), 0);

                        }

                    }

                    Object newLeafNode = tree.getModel().getChild(relationShipsNode, assGenIndex);
                    System.out.println("-------------------new Leaf Node" + newLeafNode);
                    addItems(newLeafNode, leafNode.toString(), 0);

                }

                System.out.println(typeNodeChange.toString());
                if (action.equalsIgnoreCase("delete")) {
                    if (nodess.getParent().getChildCount() != 0) {
                        System.out.println("nodes are removing");
                        System.out.println("nodess: "+ nodess.toString());
                        ((DefaultTreeModel) tree.getModel()).removeNodeFromParent(nodess);
                    }
                    
                    if (typeNodeChange.getParent().getChildCount() != 0) {
                        System.out.println("typeNodeChange are removing");
                        System.out.println("typeNode: "+ typeNodeChange);
                        ((DefaultTreeModel) tree.getModel()).removeNodeFromParent(typeNodeChange);
                    }

                    if (rela.getChildCount() == 0) {
                        System.out.println("rela are removing");
                        ((DefaultTreeModel) tree.getModel()).removeNodeFromParent(rela);

                    }
                }

            }
            if (action.equalsIgnoreCase("Edit")) {
                ((DefaultTreeModel) tree.getModel()).nodeChanged(typeNodeChange);
            }

        }
    }

    /**
     *
     * method to change the relationships and to remove the relationships when
     * edit is clicked
     *
     * @param node : selected node
     */
    protected void validateRelations_Edit(DefaultMutableTreeNode node) {
        TreePath path = tree.getSelectionPath();
        Object nodes = path.getLastPathComponent();

        Object root = tree.getModel().getRoot();
        int childOfRoot = tree.getModel().getChildCount(root);
        Object[] childOfRootObjectArray = new Object[childOfRoot];

        /*get the current class stored in the leaf node */
        /*if the selected node is leaf node - relations*/
        if (tree.getModel().isLeaf(nodes)) {
            String classNamess = node.getParent().getParent().getParent().toString();
            childOfRootObjectArray[0] = "Select the class name";
            for (int childCount = 0, arrayCount = 1; childCount < childOfRoot; childCount++) {
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
            if (!(selectedType.equalsIgnoreCase(typeFields[0].toString()) || newArtefactName.equalsIgnoreCase(childOfRootObjectArray[0].toString()))) {
                try {
                    if (!newArtefactName.isEmpty()) {
                        changeTheRelationInOtherClass_Edit(nodes, node);

                        node.setUserObject(selectedType + " ->" + newArtefactName);
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

    /**
     * when the relationship is changed in one node, find the respective node in
     * the parent class of the changed relationship and remove that node
     *
     * @param nodes
     * @param node
     */
    protected void changeTheRelationInOtherClass_Edit(Object nodes, DefaultMutableTreeNode node) {
        /*get the current type and class */
        String nodeArray[] = nodes.toString().split("->");
        Object currentRelationClass_object = null;
        boolean nodeExists = false;
        Object leafRel = null;
        int leafRelCount = 0;
        Object relType = null;
        int relCount = 0;
        Object rel = null;
        boolean relationshipExist = false;
        boolean relationTypeExist = false;
        Object currentParentClass;
        if (node.isLeaf()) {
            currentParentClass = node.getParent().getParent().getParent();
        } else {
            currentParentClass = node.getParent().getParent();
        }
        //traverse to the class where the the selected node's class exist
        if (nodeArray.length == 2) {
            String currentRelationClass = nodeArray[1];

            //if (currentRelationClass.isEmpty() || currentRelationClass == null) {
            currentRelationClass_object = getObjectNode(currentRelationClass);

            int childCountOfClass = tree.getModel().getChildCount(currentRelationClass_object);

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
                    relType = tree.getModel().getChild(rel, rCount);
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
        }
    }

    /**
     * delete a particular node in relation leaf when the leaf node is deleted
     * in the relationship, it deletes the respective node that can appear in
     * the child or parent class.
     *
     * @param nodeExists
     * @param leafRel
     * @param leafRelCount
     * @param relType
     * @param relCount
     * @param rel
     */
    private void deleteNodes(boolean nodeExists, Object leafRel, int leafRelCount, Object relType, int relCount, Object rel) {
        if (nodeExists) {
            DefaultMutableTreeNode nodess = (DefaultMutableTreeNode) (leafRel);
            ((DefaultTreeModel) tree.getModel()).removeNodeFromParent(nodess);

            if (leafRelCount == 1) {
                /*remove the relation type heading*/
                nodess = (DefaultMutableTreeNode) (relType);
                ((DefaultTreeModel) tree.getModel()).removeNodeFromParent(nodess);
            }
            if (relCount == 1 && leafRelCount == 1) {
                /*remove the relation heading*/
                nodess = (DefaultMutableTreeNode) (rel);
                ((DefaultTreeModel) tree.getModel()).removeNodeFromParent(nodess);
            }

        }
    }

    /**
     * method to update the nodes when edit is selected and the user gives the
     * input to that node
     *
     * @param selectedType
     * @param newArtefactName
     * @param node
     */
    protected void update_Edit(String selectedType, String newArtefactName, DefaultMutableTreeNode node) {

        String currentRelationClass = newArtefactName;
        Object currentRelationClass_object = null;

        Object currentParentClass = node.getParent().getParent().getParent();
        //traverse to the class where the the selected node's class exist
        currentRelationClass_object = getObjectNode(currentRelationClass);
        //current class
        int childCountOfClass = tree.getModel().getChildCount(currentRelationClass_object);

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
            for (int rCount = 0; rCount < relCount; rCount++) {
                /*checking whether current and previous types are same*/
                relType = tree.getModel().getChild(rel, rCount);
                if (node.getParent().toString().equalsIgnoreCase(relType.toString())) {
                    relationTypeExists = true;
                    break;
                }
            }
            if (relationTypeExists) {
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
            addItems(clNode, "Relationships", childCountOfClass);
            //add the association or generalization
            rel = tree.getModel().getChild(clNode, childCountOfClass);
            addItems(rel, node.getParent().toString(), 0);
            //add the leaf
            relType = tree.getModel().getChild(rel, 0);
            addItems(relType, selectedType + " ->" + currentParentClass, 0);

        }

    }

    /**
     * add the relationship leaf nodes to the tree
     *
     * @param node: the parent node of the new node
     * @param msg : the item that has to be stored as a node
     * @param index : the position of the node
     */
    protected void addItems(Object node, Object msg, int index) {
        DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) (node);
        DefaultTreeModel model = (DefaultTreeModel) (tree.getModel());
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(msg);
        model.insertNodeInto(newNode, parentNode, index);

    }

    /**
     * method to get the object(Tree node) of the node of an object
     *
     * @param obj: item
     * @return
     */
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

    /**
     * method to add the items when user selects the add from the menu this
     * functionality will add the 'New' node below the selected node
     */
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

    /**
     * method to store the artefacts after the confirmation of user
     *
     */
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
            classNameSet.add(className.toString());

            int artefactCount = classTreeModel.getChildCount(className);

            for (int artCount = 0; artCount < artefactCount; artCount++) {
                Object artefactName = classTreeModel.getChild(className, artCount);
                if (artefactName.toString().equalsIgnoreCase("Attributes")) {
                    storeTheArtefactsInSet(classTreeModel, artefactName, attributeSet);

                }
                if (artefactName.toString().equalsIgnoreCase("Methods")) {
                    storeTheArtefactsInSet(classTreeModel, artefactName, methodSet);
                }
                if (artefactName.toString().equalsIgnoreCase("Relationships")) {
                    storeTheRelationShipInSet(classTreeModel, artefactName, className);

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

    /**
     * method to store the relationship in a set
     *
     * @param classTreeModel
     * @param artefactName
     * @param className
     */
    private void storeTheRelationShipInSet(TreeModel classTreeModel, Object artefactName, Object className) {
        int relationCount = classTreeModel.getChildCount(artefactName);
        for (int rCount = 0; rCount < relationCount; rCount++) {
            Object relName = classTreeModel.getChild(artefactName, rCount);
            if (relName.toString().equalsIgnoreCase("Generalization")) {
                int genCount = classTreeModel.getChildCount(relName);
                String type = relName.toString();
                for (int gCount = 0; gCount < genCount; gCount++) {
                    Object genName = classTreeModel.getChild(relName, gCount);
                    String[] genNameArray = genName.toString().split("->");

                    String parentClass;
                    String childClass;
                    if ((genNameArray[0].trim()).equalsIgnoreCase("parent")) {
                        parentClass = genNameArray[1].trim();
                        childClass = className.toString();
                    } else {
                        parentClass = className.toString();
                        childClass = genNameArray[1].trim();

                    }

                    ClassRelation clRelation = new ClassRelation(type, childClass, parentClass);
                    System.out.println(clRelation.getRelationType() + " " + clRelation.getChildElement() + "->" + clRelation.getParentElement());
                    requirementRelationsObjects.add(clRelation);
                }

            }
            if (relName.toString().equalsIgnoreCase("Association")) {
                int assCount = classTreeModel.getChildCount(relName);
                String type = relName.toString();
                for (int aCount = 0; aCount < assCount; aCount++) {
                    Object assName = classTreeModel.getChild(relName, aCount);
                    String[] assNameArray = assName.toString().split("->");
                    String class1 = assNameArray[0].toLowerCase().trim();
                    String parentClass;
                    String childClass;
                    if (class1.equals("parent")) {
                        parentClass = assNameArray[1].trim();
                        childClass = className.toString();
                    } else {
                        parentClass = className.toString();
                        childClass = assNameArray[1].trim();

                    }

                    ClassRelation clRelation = new ClassRelation(type, childClass, parentClass);
                    System.out.println(clRelation.getRelationType() + " " + clRelation.getChildElement() + "->" + clRelation.getParentElement());
                    requirementRelationsObjects.add(clRelation);
                }

            }

        }
    }

    /**
     * store the artefacts in a set
     *
     * @param classTreeModel
     * @param artefactName
     * @param attributeSet
     */
    private void storeTheArtefactsInSet(TreeModel classTreeModel, Object artefactName, HashSet artefactSetSet) {
        int attributeCount = classTreeModel.getChildCount(artefactName);
        for (int atrCount = 0; atrCount < attributeCount; atrCount++) {
            Object artName = classTreeModel.getChild(artefactName, atrCount);
            artefactSetSet.add(artName.toString());
        }
    }

    /**
     * method to get the artefacts
     *
     * @return : HashMap of artefacts
     */
    public HashMap getRequirementobjects() {
        return requirementObjects;
    }

    /**
     * method to get the relationship map
     *
     * @return : hashMap of relationships
     */
    public HashSet getRequirementRelationsObject() {
        if (requirementRelationsObjects.isEmpty()) {
            System.out.println("EMPTY");

        } else {
            System.out.println("Not EMPTY");
        }
        return requirementRelationsObjects;
    }

    /**
     * method to create the 'confirmation' button
     */
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

    /**
     * method to print the output in the console
     *
     * @param output : hashMap of the artefacts
     */
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
            }

        }

    }
}
