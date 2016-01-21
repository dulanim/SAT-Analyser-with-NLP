/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.GUI;

import static com.project.NLP.Requirement.MultipleClassListHandlingGUI.lock;
import com.project.NLP.Requirement.StoringArtefacts;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author S. Shobiga
 */
/**
 * A frame with a tree and buttons to edit the tree.
 */
class TreeEditFrame extends JFrame {

    private DefaultTreeModel model;
    private JTree tree;
    private static final int DEFAULT_WIDTH = 600;
    private static final int DEFAULT_HEIGHT = 400;
    private Vector rootVector;
    private HashMap output;
    private JScrollPane scrollPane;
    private CheckBoxNodeRenderer renderer;
    Vector[] classVector;
    Vector attributesVector;
    Vector methodVector;
    Vector relationsVector;

    private DefaultMutableTreeNode root = null;
    private DefaultMutableTreeNode classTreeNode = null;
    private DefaultMutableTreeNode methodsTreeNode = null;
    private DefaultMutableTreeNode attributesTreeNode = null;
    private DefaultMutableTreeNode relationsTreeNode = null;

    public TreeEditFrame(HashMap output) {

        this.output = output;
        setTitle("Artefacts Confirmation");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        // construct tree
        //TreeNode root = makeSampleTree();
        tree = makeSampleTree();

        //Vector rootNode = makeSampleTree();
        //makeSampleTree();
        //TreeNode roots = tree; 
        root = new DefaultMutableTreeNode("Artefacts");
        model = new DefaultTreeModel(root);
        //tree = new JTree(model);
        //tree.setEditable(true);
        System.out.println("tree: " + tree);

        scrollPane = new JScrollPane(tree);
        this.getContentPane().add(scrollPane, BorderLayout.CENTER);
        this.setVisible(true);

        makeButtons();
        //addCheckBox();
        // add scroll pane with tree
        // JScrollPane scrollPane = new JScrollPane(tree);
        add(scrollPane, BorderLayout.CENTER);

    }

    public void addCheckBox() {
        CheckBoxNodeRenderer renderer = new CheckBoxNodeRenderer();
        tree.setCellRenderer(renderer);

        tree.setCellEditor(new CheckBoxNodeEditor(tree));
        tree.setEditable(true);
    }

    public JTree makeSampleTree() {
        //CheckBoxNode classNames[];
        //CheckBoxNode attributeNames[];
        //CheckBoxNode methodNames[];

        System.out.println("dfdr");

        //HashMap output = new HashMap();
        Iterator it = output.keySet().iterator();
        Object rootNodes[] = new Object[output.size()];
        root = new DefaultMutableTreeNode("Artefacts");

        int classCount = 0;
        while (it.hasNext()) {

            String className = it.next().toString();
            System.out.println(className);
            StoringArtefacts store = (StoringArtefacts) output.get(className);
            HashSet attributes = store.getAttributes();
            HashSet methods = store.getMethods();
            HashSet relations = store.getRelationships();
            System.out.println(attributes);
            System.out.println(methods);

            rootVector = new Vector();
            classVector = new Vector[output.size()];
            attributesVector = new Vector();
            methodVector = new Vector();
            relationsVector = new Vector();

            classVector[classCount] = new NamedVector(className);
            classTreeNode = new DefaultMutableTreeNode(classVector.toString());

            CheckBoxNode[] attributeNames = new CheckBoxNode[attributes.size()];
            CheckBoxNode[] methodNames = new CheckBoxNode[methods.size()];
            CheckBoxNode[] relationshipNames = new CheckBoxNode[relations.size()];
            int attributeCount = 0;
            int methodCount = 0;
            int relationCount = 0;

            if (attributes.isEmpty()) {
                attributesVector = new NamedVector("Attributes");

            } else {
                for (Object attribute : attributes) {
                    attributeNames[attributeCount++] = new CheckBoxNode(attribute.toString(), false);
                    attributesVector = new NamedVector("Attributes", attributeNames);
                    attributesTreeNode = new DefaultMutableTreeNode(attributesVector.toString());

                }
            }
            if (methods.isEmpty()) {
                methodVector = new NamedVector("Methods");

            } else {
                for (Object method : methods) {
                    methodNames[methodCount++] = new CheckBoxNode(method.toString(), false);
                    methodVector = new NamedVector("Methods", methodNames);
                    methodsTreeNode = new DefaultMutableTreeNode(methodVector.toString());
                }
            }
            if (relations.isEmpty()) {
                relationsVector = new NamedVector("Relationship");

            } else {
                for (Object relation : relations) {
                    relationshipNames[relationCount++] = new CheckBoxNode(relation.toString(), false);
                    relationsVector = new NamedVector("Relationship", relationshipNames);
                    relationsTreeNode = new DefaultMutableTreeNode(relationsVector.toString());
                }
            }
            classVector[classCount].add(attributesVector);
            classVector[classCount].add(methodVector);
            classVector[classCount].add(relationsVector);

            rootNodes[classCount] = classVector[classCount];
            classCount++;

            root.add(classTreeNode);

        }

        //Object rootNodes[] = {classVector[classCount]};
        rootVector = new NamedVector("Root", rootNodes);
        JTree trees = new JTree(rootVector);

        renderer = new CheckBoxNodeRenderer();
        trees.setCellRenderer(renderer);

        trees.setCellEditor(new CheckBoxNodeEditor(trees));
        trees.setEditable(true);

        return trees;

    }

    /**
     * Makes the buttons to add a sibling, add a child, and delete a node.
     */
    public void makeButtons() {
        System.out.println("fdfdfd");
        revalidate();

        JPanel panel = new JPanel();
        JButton addSiblingButton = new JButton("Add Sibling");
        addSiblingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {

                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree
                        .getLastSelectedPathComponent();

                if (selectedNode == null) {
                    return;
                }

                DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectedNode.getParent();

                if (parent == null) {
                    return;
                }

                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("New");

                int selectedIndex = parent.getIndex(selectedNode);
                model.insertNodeInto(newNode, parent, selectedIndex + 1);

                // now display new node
                TreeNode[] nodes = model.getPathToRoot(newNode);
                TreePath path = new TreePath(nodes);
                tree.scrollPathToVisible(path);
            }
        });
        panel.add(addSiblingButton);

        JButton addChildButton = new JButton("Add New Artefact");
        addChildButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                //CheckBoxNodeRenderer selectedNode = (CheckBoxNodeRenderer)tree.getLastSelectedPathComponent();

                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree
                        .getLastSelectedPathComponent();

                if (selectedNode == null) {
                    return;
                }

                DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("New");
                //CheckBoxNodeRenderer newNode = new CheckBoxNodeRenderer("New");
                model.insertNodeInto(newNode, selectedNode, selectedNode.getChildCount());

                // now display new node
                TreeNode[] nodes = model.getPathToRoot(newNode);
                TreePath path = new TreePath(nodes);
                tree.scrollPathToVisible(path);
            }
        });
        panel.add(addChildButton);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree
                        .getLastSelectedPathComponent();

                if (selectedNode == null) {
                    System.out.println("node null");
                    return;
                }
                //DefaultMutableTreeNode newNode = new DefaultMutableTreeNode("New");

                DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectedNode.getParent();
                if (parent == null) {
                    System.out.println("parent null");
                    return;
                }
                int selectedIndex = parent.getIndex(selectedNode);
                System.out.println(selectedNode + " " + selectedIndex);
                //parent.getChildAt(selectedIndex+1);
                //parent.removeAllChildren();

                //model.insertNodeInto(newNode, parent, selectedIndex + 1);
                System.out.println("selected items "+ selectedNode.getUserObject().toString());
                System.out.println("item from attr vector: "+ attributesVector.get(0).toString());
                System.out.println("string parent"+ parent.toString());
                System.out.println("string parent of parent"+ parent.getParent().toString());
                
                System.out.println("contains: "+attributesVector.contains(selectedNode.toString()));
                attributesVector.remove(selectedNode.toString());
                System.out.println("attributes: "+attributesVector.get(selectedIndex).toString());
               // attributesVector.remove(selectedIndex);
                
                parent.remove(selectedIndex);
                System.out.println(tree.isSelectionEmpty());

                
                //model.removeNodeFromParent(parent);
                TreeNode[] nodes = parent.getPath();
                // now display new node
                //TreeNode[] nodes = model.getPathToRoot(newNode);
                TreePath path = new TreePath(nodes);

                //tree = new JTree(parent);
                tree.scrollPathToVisible(path);
                setVisible(true);
                //add(scrollPane, BorderLayout.CENTER);

                revalidate();

                // Notify the model, which will add it and create an event, and
                // send it up the tree...
//                ((DefaultTreeModel) tree.getModel()).insertNodeInto(child,
//                        root, 0);
//                tree = new JTree(model);
//                scrollPane = new JScrollPane(tree);
//                getContentPane().add(scrollPane, BorderLayout.CENTER);
//                setVisible(true);
//                
            }
        });
        panel.add(deleteButton);

        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree
                        .getLastSelectedPathComponent();

                JDialog.setDefaultLookAndFeelDecorated(true);
                int dialogResult = JOptionPane.showConfirmDialog(null, "Do you want to confirm your changes?", "Confirmation Dialog", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    /*if none of the button is selected*/
                    System.out.println("confirmed...");
                }

            }
        });
        panel.add(confirmButton);

        add(panel, BorderLayout.SOUTH);
    }

}
