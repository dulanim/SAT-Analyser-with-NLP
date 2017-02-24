/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.traceability.visualization;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/**
 * Consists a list of check boxes 
 * @author Aarthika <>
 */
public class CheckBoxList extends JList {
    
    int[] checklist;
    int size;

    protected static Border noFocusBorder
            = new EmptyBorder(1, 1, 1, 1);
    public CheckBoxList(){
        
    }

    /**
     * Renders a checkbox list for deletion
     * @param count 
     */
    public CheckBoxList(int count) {
        size = count;
        checklist = new int[size];
        for(int i=0;i<size;i++){
            checklist[i] = -1;
        }
        setCellRenderer(new CellRenderer());

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int index = locationToIndex(e.getPoint());

                if (index != -1) {
                    JCheckBox checkbox = (JCheckBox) getModel().getElementAt(index);
                    
                    checkbox.setSelected(!checkbox.isSelected());
                    if(checklist[index] == 1)
                        checklist[index] = 0;
                    else 
                        checklist[index] = 1;
                    System.out.println(""+index);
                    //repaint();
                    System.out.println("2");
                }
            }
        }
        );

        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }
    
    public int getCount(){
        return size;
    }
    public int[] getCheckList(){
        return checklist;
    }

    protected class CellRenderer implements ListCellRenderer {

        public Component getListCellRendererComponent(
                JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            JCheckBox checkbox = (JCheckBox) value;
            checkbox.setBackground(isSelected
                    ? getSelectionBackground() : getBackground());
            checkbox.setForeground(isSelected
                    ? getSelectionForeground() : getForeground());
            checkbox.setEnabled(isEnabled());
            checkbox.setFont(getFont());
            checkbox.setFocusPainted(false);
            checkbox.setBorderPainted(true);
            checkbox.setBorder(isSelected
                    ? UIManager.getBorder(
                            "List.focusCellHighlightBorder") : noFocusBorder);
            return checkbox;
        }
    }
}
