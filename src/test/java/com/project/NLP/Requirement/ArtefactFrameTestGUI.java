/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.Requirement;

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
 *
 * @author S. Shobiga
 */

public class ArtefactFrameTestGUI {

    HashMap output = new HashMap();
    HashMap requirementObjects = new HashMap();
    HashSet requirementRelationsObjects = new HashSet();
    ArtefactFrame artefactFrame;

    public ArtefactFrameTestGUI(final HashMap output, final HashSet outputRelations) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                //JFrame frame = new TreeEditFrame(output);
                final ArtefactFrame artefactFrame = new ArtefactFrame(output,outputRelations);
                final JFrame frame = artefactFrame;
                requirementObjects = artefactFrame.getRequirementobjects();
                requirementRelationsObjects = artefactFrame.getRequirementRelationsObject();
                frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

                frame.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                        artefactFrame.storeItems();
                        ArtefactFrame.lock = false;
                        frame.dispose();    
                    }
                });
                frame.setVisible(true);
            }
        });
    }

    public HashMap getRequirementobjects() {
        return requirementObjects;
    }
    public HashSet getRequirementRelationsObject(){
        return requirementRelationsObjects;
    }

    public boolean getLock() {
        return ArtefactFrame.lock;
    }

}
