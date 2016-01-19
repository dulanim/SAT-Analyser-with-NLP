/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.GUI;

import com.project.NLP.Requirement.StoringArtefacts;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * This program demonstrates tree editing.
 *
 * @version 1.03 2007-08-01
 * @author Cay Horstmann
 */
public class ArtefactFrameTestGUI {

    HashMap output = new HashMap();
    HashMap requirementObjects = new HashMap(); 
    ArtefactFrame artefactFrame;
    public ArtefactFrameTestGUI(final HashMap output) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                //JFrame frame = new TreeEditFrame(output);
                ArtefactFrame artefactFrame = new ArtefactFrame(output);
                JFrame frame = artefactFrame;
                requirementObjects = artefactFrame.getRequirementobjects();
                frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                
                frame.setVisible(true);
            }
        });
    }
    
    public HashMap getRequirementobjects(){
        return requirementObjects;
    }
    
    public boolean getLog(){
        return ArtefactFrame.log;
    }
    

   
}
