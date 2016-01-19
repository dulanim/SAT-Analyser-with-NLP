/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.GUI;

/**
 *
 * @author S. Shobiga
 */
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

public class ArtefactFrame extends JFrame {

    private static final int DEFAULT_WIDTH = 350;
    private static final int DEFAULT_HEIGHT = 400;
    private HashMap requirementObjects = new HashMap();
    public static boolean log =true;
    protected JTree tree;

    public ArtefactFrame(HashMap output) {
        super();

        setTitle("Artefacts Confirmation");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLocationRelativeTo(null);
        //tree = jt;
        addTreeItems(output);
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

    protected void addTreeItems(HashMap output) {

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Artefacts");
        DefaultMutableTreeNode[] rootDummy = new DefaultMutableTreeNode[output.size()];
        DefaultMutableTreeNode classNames;
        DefaultMutableTreeNode attributes;
        DefaultMutableTreeNode methods;
        DefaultMutableTreeNode relationships;

        HashSet attributeSet = null;
        HashSet methodSet = null;
        HashSet relationSet = null;

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

            classNames = new DefaultMutableTreeNode(classNameString);
            for (Object attributeItems : attributeSet) {
                attributes.add(new DefaultMutableTreeNode(attributeItems));
            }

            for (Object methodItems : methodSet) {
                methods.add(new DefaultMutableTreeNode(methodItems));
            }

            for (Object relationshipItems : relationSet) {
                relationships.add(new DefaultMutableTreeNode(relationshipItems));
            }

            root.add(classNames);
            classNames.add(attributes);
            classNames.add(methods);
            classNames.add(relationships);

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

        JFrame frame = new JFrame("InputDialog Example #1");

        // prompt the user to enter their name
        String newArtefactName = JOptionPane.showInputDialog(frame, "New Artefact name...");

        // get the user's input. note that if they press Cancel, 'name' will be null
        System.out.printf("The user's name is '%s'.\n", newArtefactName);
        try {
            if (!newArtefactName.isEmpty()) {
                node.setUserObject(newArtefactName);
                TreePath pathSelected = tree.getSelectionPath();
                Object n = pathSelected.getLastPathComponent();
                System.out.println("new node:" + node.toString());
                ((DefaultTreeModel) tree.getModel()).nodeChanged(node);

            }
        } catch (Exception e) {
            System.out.println("Exception occurs in GUI: (ignore it)" + e);
        }
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
                        Object relationshipName = classTreeModel.getChild(artefactName, rCount);
                        System.out.println("relationships: " + relationshipName.toString());
                        relationSet.add(relationshipName.toString());
                    }

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

    public HashMap getRequirementobjects(){
        return requirementObjects;
    }
    private void makeButtons(){
        JPanel panel = new JPanel();
        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                storeItems();
                log =false;
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
                System.out.println(relation.toString());
                //System.out.println("Type - " + rel.getRelationType() + "-> Parent -" + rel.getParentElement());
            }

        }

    }
}
