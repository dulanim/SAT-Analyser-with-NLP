/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.GUI;


/*
 Definitive Guide to Swing for Java 2, Second Edition
 By John Zukowski     
 ISBN: 1-893115-78-X
 Publisher: APress
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;

public class CheckBoxNodeTreeSample {

    public static void main(String args[]) {
        

        JFrame frame = new JFrame("CheckBox Tree");

        CheckBoxNode accessibilityOptions[] = {
            new CheckBoxNode(
            "Move system caret with focus/selection changes", false),
            new CheckBoxNode("Always expand alt text for images", true)};
        CheckBoxNode browsingOptions[] = {
            new CheckBoxNode("Notify when downloads complete", true),
            new CheckBoxNode("Disable script debugging", true),
            new CheckBoxNode("Use AutoComplete", true),
            new CheckBoxNode("Browse in a new process", false)};
        CheckBoxNode friendsOptions[] = {
            new CheckBoxNode("Shobi", true),
            new CheckBoxNode("Aarthi", true),
            new CheckBoxNode("Megala", false),};
        CheckBoxNode subFriendsOptions[] = {
            new CheckBoxNode("Hrithik", true),
            new CheckBoxNode("Sarukan", true),
            new CheckBoxNode("Sona", false),};

        Vector accessVector = new NamedVector("Accessibility",
                accessibilityOptions);
        Vector browseVector = new NamedVector("Browsing", browsingOptions);
        Vector friends = new NamedVector("Friends", friendsOptions);
        friends.add(1, subFriendsOptions);

        Object rootNodes[] = {accessVector, browseVector, friends};
        Vector rootVector = new NamedVector("Root", rootNodes);
        JTree tree = new JTree(rootVector);

        CheckBoxNodeRenderer renderer = new CheckBoxNodeRenderer();
        tree.setCellRenderer(renderer);

        tree.setCellEditor(new CheckBoxNodeEditor(tree));
        tree.setEditable(true);

        JScrollPane scrollPane = new JScrollPane(tree);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.setSize(300, 150);
        frame.setVisible(true);
    }

}
