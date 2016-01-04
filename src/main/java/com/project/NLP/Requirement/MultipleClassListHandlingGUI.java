/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.NLP.Requirement;

/**
 *
 * @author S. Shobiga
 */
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import static java.nio.file.Files.list;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * The ListCutPaste example illustrates cut, copy, paste and drag and drop using
 * three instances of JList. The TransferActionListener class listens for one of
 * the CCP actions and, when one occurs, forwards the action to the component
 * which currently has the focus.
 */
public class MultipleClassListHandlingGUI extends JPanel {

    ListTransferHandler listTrasferHandler;
    private HashSet classList;
    private HashSet attributeList;
    private HashSet methodList;
    private String deleteString = "Delete";
    private String nextString = "Next";
    private String backString = "Back";
    private String finishString = "Finish";
    private String cancelString = "Cancel";

    private static JButton deleteButton;
    private static JButton finishButton;
    private static JButton cancelButton;

    JPanel panelForAttr = new JPanel(new GridLayout(1, 0)); //grids for the selected attributes and other grid for the original attributes
    JPanel panelEntire = new JPanel(new GridLayout(0, 1));
    JPanel panelForDefaultAttr; // panel for original attributes
    JPanel panelForClass = new JPanel(new GridLayout(3, 1)); // panel for class names - button groups

    JList listForAttr_D; //default attribute list
    JScrollPane scrollPaneForAttr;
    DefaultListModel listModelForAttr_D;
    DefaultListModel listModel2;
    JList list2;
    String previouslySelectedClass = "";
    protected HashMap<String, HashSet> attributeMap = new HashMap<String, HashSet>();

    HashSet attribute;
    /*class Names in button group */
    ButtonGroup classNameButtonGroup = new ButtonGroup();
    JRadioButton classNameRadio;
    HashSet method;
    JPanel panelForMethod = new JPanel(new GridLayout(1, 0));
    JPanel panelEntireForMethod = new JPanel(new GridLayout(0, 1));
    JList listForMethod;
    JScrollPane scrollPaneForMethod;
    JPanel panel2ForMethod; // panel for original attributes
    JPanel panel1ForMethod = new JPanel(new GridLayout(3, 1)); // panel for class names - button groups
    DefaultListModel listModelForMethod;
    DefaultListModel listModel2ForMethod;
    JList list2ForMethod;
    String previouslySelectedClassForMethod = "";
    protected HashMap<String, HashSet> methodMap = new HashMap<String, HashSet>();
    /*class Names in button group */
    ButtonGroup classNameButtonGroupForMethod = new ButtonGroup();
    JRadioButton classNameRadioForMethod;

    JFrame frame;
    public MultipleClassListHandlingGUI() {

    }

    public MultipleClassListHandlingGUI(HashSet classList, HashSet attributesList, HashSet methodList) {
        super(new BorderLayout());
        //openGUI();
        listTrasferHandler = new ListTransferHandler();
        this.classList = classList;
        this.attributeList = attributesList;
        this.methodList = methodList;
        finishButton = new JButton(finishString);
        cancelButton = new JButton(cancelString);

        gridFormClass();

        frame = new JFrame("Multiple Class Handling");

        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        //ListCutPaste demo = new ListCutPaste();
        frame.setJMenuBar(createMenuBar());
        setOpaque(true); //content panes must be opaque
        frame.setContentPane(this);

        //Display the window.
        frame.pack();
        frame.setVisible(true);

    }

    public void gridFormClass() {

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                AbstractButton selectedButton = (AbstractButton) actionEvent.getSource();
                String classN = selectedButton.getText();
                System.out.println("selected: " + classN);

                /*attribute storing and getting attribute from panel*/
                storeAttributes();
                getAttributesFromPanel();
                /*method storing and getting methods from panel*/
                storeMethods();
                getMethodsFromPanel();

                previouslySelectedClass = classN;

                /*remove all panel*/
                panelForAttr.removeAll();
                panelForMethod.removeAll();

                //addButton();
                /*to refresh the content*/
                revalidate();
                gridForm();
                gridFormForMethod();

            }
        };

        Iterator classIterator = classList.iterator();

        for (int classListCount = 0; classListCount < classList.size(); classListCount++) {
            if (classIterator.hasNext()) {
                classNameRadio = new JRadioButton(classIterator.next().toString());
                classNameButtonGroup.add(classNameRadio);
                panelForClass.add(classNameRadio);
                classNameRadio.addActionListener(actionListener);
            }
        }

        panelForClass.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        panelForClass.setBorder(BorderFactory.createTitledBorder("Class Names"));
        panelEntire.add(panelForClass);

        setPreferredSize(new Dimension(550, 400));
        add(panelForAttr, BorderLayout.CENTER);
        add(panelForMethod, BorderLayout.CENTER);
        panelEntire.add(panelForAttr);
        panelEntire.add(panelForMethod);

        //addButton();
        finishButton.setActionCommand(finishString);
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPane.add(finishButton);
        buttonPane.add(Box.createHorizontalStrut(5));
        add(buttonPane, BorderLayout.PAGE_END);

        finishButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //panelEntire.setVisible(false);

                JDialog.setDefaultLookAndFeelDecorated(true);
                int dialogResult = JOptionPane.showConfirmDialog(null, "Do you want to confirm your changes?", "Confirmation Dialog", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (dialogResult == JOptionPane.YES_OPTION) {
                    /*if none of the button is selected*/
                    if (previouslySelectedClass.isEmpty()) {
                        /*close the gui*/
                        //frame.setVisible(false);
                        frame.dispose();
                        //System.exit(0);

                    } else {
                        /*for the final storage*/
                        /*attribute storing and getting attribute from panel*/
                        System.out.println("3");
                        storeAttributes();
                        System.out.println("4");
                        getAttributesFromPanel();
                        /*method storing and getting methods from panel*/
                        storeMethods();
                        getMethodsFromPanel();

                        /*final output dialog box */
                        System.out.println("1");
                        getClassWithAttributesAndMethods();
                        System.out.println("2");
                        /*close the gui*/
                        frame.setVisible(false);
                        //System.exit(0);
                    }

                }
                if (dialogResult == JOptionPane.NO_OPTION) {

                }
            }

       

        });

        add(panelEntire, BorderLayout.CENTER);
    }

    private void gridForm() {
        /*Attribute Items*/
        listModelForAttr_D = new DefaultListModel();
        Iterator iteratorAttr = attributeList.iterator();
        for (int attributeListCount = 0; attributeListCount < attributeList.size(); attributeListCount++) {
            if (iteratorAttr.hasNext()) {
                String attributeName = iteratorAttr.next().toString();
                if (!isDuplicatesInDisplayingWindowForAttribute(attributeName)) {
                    System.out.println("storing in model " + attributeName);
                    listModelForAttr_D.addElement(attributeName);
                }
            }
        }

        listForAttr_D = new JList(listModelForAttr_D);
        listForAttr_D.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPaneForAttr = new JScrollPane(listForAttr_D);
        scrollPaneForAttr.setPreferredSize(new Dimension(200, 100));
        listForAttr_D.setDragEnabled(true);
        listForAttr_D.setTransferHandler(listTrasferHandler);
        listForAttr_D.setDropMode(DropMode.ON_OR_INSERT);
        //listForAttr_D.setDropTarget(listModelForAttr_D);
        panelForDefaultAttr = new JPanel(new BorderLayout());
        panelForDefaultAttr.add(scrollPaneForAttr, BorderLayout.CENTER);
        panelForDefaultAttr.setBorder(BorderFactory.createTitledBorder("Default Attributes"));

        listForAttr_D.setBackground(new Color(229, 224, 198));
        listForAttr_D.setOpaque(true);
        panelForAttr.add(panelForDefaultAttr);

        add(panelForAttr, BorderLayout.CENTER);

        /*panel for new attributes*/
        listModel2 = new DefaultListModel();
        list2 = new JList(listModel2);
        list2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPaneForAttr = new JScrollPane(list2);
        scrollPaneForAttr.setPreferredSize(new Dimension(200, 100));
        list2.setDragEnabled(true);
        list2.setTransferHandler(listTrasferHandler);
        list2.setDropMode(DropMode.ON_OR_INSERT);

        JPanel panel3 = new JPanel(new BorderLayout());
        panel3.add(scrollPaneForAttr, BorderLayout.CENTER);
        panel3.setBorder(BorderFactory.createTitledBorder("New Attributes"));
        panelForAttr.add(panel3);
        setPreferredSize(new Dimension(600, 400));
        add(panelForAttr, BorderLayout.CENTER);

        panelEntire.add(panelForAttr);
        panelEntire.repaint();
        add(panelEntire, BorderLayout.CENTER);

        storeAttributes();

    }

    public void gridFormForMethod() {
        /*method Items*/
        listModelForMethod = new DefaultListModel();
        Iterator iteratorMethod = methodList.iterator();
        for (int methodListCount = 0; methodListCount < methodList.size(); methodListCount++) {
            if (iteratorMethod.hasNext()) {
                String methodName = iteratorMethod.next().toString();
                if (!isDuplicatesInDisplayingWindowForMethod(methodName)) {
                    listModelForMethod.addElement(methodName);
                }

            }
        }

        listForMethod = new JList(listModelForMethod);
        listForMethod.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPaneForMethod = new JScrollPane(listForMethod);
        scrollPaneForMethod.setPreferredSize(new Dimension(200, 100));
        listForMethod.setDragEnabled(true);
        listForMethod.setTransferHandler(listTrasferHandler);
        listForMethod.setDropMode(DropMode.ON_OR_INSERT);

        panel2ForMethod = new JPanel(new BorderLayout());
        panel2ForMethod.add(scrollPaneForMethod, BorderLayout.CENTER);
        panel2ForMethod.setBorder(BorderFactory.createTitledBorder("Default Methods"));

        listForMethod.setBackground(new Color(229, 224, 198));
        listForMethod.setOpaque(true);
        panelForMethod.add(panel2ForMethod);
        add(panelForMethod, BorderLayout.CENTER);

        /*panel for new attributes*/
        listModel2ForMethod = new DefaultListModel();
        list2ForMethod = new JList(listModel2ForMethod);
        list2ForMethod.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scrollPaneForMethod = new JScrollPane(list2ForMethod);
        scrollPaneForMethod.setPreferredSize(new Dimension(200, 100));
        list2ForMethod.setDragEnabled(true);
        list2ForMethod.setTransferHandler(listTrasferHandler);
        list2ForMethod.setDropMode(DropMode.ON_OR_INSERT);

        JPanel panel3 = new JPanel(new BorderLayout());
        panel3.add(scrollPaneForMethod, BorderLayout.CENTER);
        panel3.setBorder(BorderFactory.createTitledBorder("New Methods"));
        panelForMethod.add(panel3);

        setPreferredSize(new Dimension(550, 400));
        add(panelForMethod, BorderLayout.CENTER);

        panelEntire.add(panelForMethod);
        panelEntire.repaint();
        add(panelEntire, BorderLayout.CENTER);

        storeMethods();

    }

    private void getAttributesFromPanel() {

        attribute = new HashSet();
        String attr = "";
        if (((listModel2 != null) && !(listModel2.isEmpty()))) {
            int count = listModel2.size();
            System.out.println("count :" + count);
            if (!listModel2.isEmpty()) {
                System.out.println("2");

                for (int i = 0; i < count; i++) {
                    System.out.println("class: " + previouslySelectedClass + " attributes :" + listModel2.getElementAt(i));
                    attr = listModel2.getElementAt(i).toString();
                    //if (!attribute.contains(attr)) {
                    attribute.add(attr);
                    //}

                }
                attributeMap.put(previouslySelectedClass, attribute);
            }
        }

    }

    private void storeAttributes() {
        if (((listModel2 != null))) {
            if (attributeMap.get(previouslySelectedClass) != null) {
                System.out.println("1");

                HashSet storedAttributeList = attributeMap.get(previouslySelectedClass);
                Iterator iterator = storedAttributeList.iterator();
                for (int storedAttributeListCount = 0; storedAttributeListCount < storedAttributeList.size(); storedAttributeListCount++) {
                    System.out.println("3");

                    if (iterator.hasNext()) {
                        String attr = iterator.next().toString();
                        System.out.println("taken from class :" + previouslySelectedClass + " att: " + attr);
                        listModel2.addElement(attr);
                    }
                }
            }
        }
    }

    private void getMethodsFromPanel() {
        method = new HashSet();
        if (((listModel2ForMethod != null) && !(listModel2ForMethod.isEmpty()))) {
            int count = listModel2ForMethod.size();
            System.out.println("count :" + count);
            if (!listModel2ForMethod.isEmpty()) {
                System.out.println("4");

                for (int i = 0; i < count; i++) {
                    System.out.println("class: " + previouslySelectedClass + " methods :" + listModel2ForMethod.getElementAt(i));
                    method.add(listModel2ForMethod.getElementAt(i));

                }
                methodMap.put(previouslySelectedClass, method);
            }
        }
    }

    private void storeMethods() {
        if (((listModel2ForMethod != null))) {
            if (methodMap.get(previouslySelectedClass) != null) {
                HashSet storedMethodList = methodMap.get(previouslySelectedClass);
                Iterator iterator = storedMethodList.iterator();
                System.out.println("5");

                for (int storedMethodListCount = 0; storedMethodListCount < storedMethodList.size(); storedMethodListCount++) {
                    if (iterator.hasNext()) {
                        String method = iterator.next().toString();
                        System.out.println("taken from class :" + previouslySelectedClass + " att: " + method);
                        listModel2ForMethod.addElement(method);
                    }
                }
            }
        }
    }
    /*get the className and the respective attributes and methods*/

    public void getClassWithAttributesAndMethods() {
        HashSet attributes = new HashSet();
        HashSet methods = new HashSet();
        String outputMessage = "";
        String className = "";
        Iterator iterator = classList.iterator();
        if (!attributeMap.isEmpty() || !methodMap.isEmpty()) {
            for (int countClass = 0; countClass < classList.size(); countClass++) {
                methods = new HashSet();
                attributes = new HashSet();
                if (iterator.hasNext()) {
                    className = iterator.next().toString();
                    if(attributeMap.containsKey(className)){
                        attributes = attributeMap.get(className);
                    }
                    System.out.println("[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[["+ attributes);
                    if(methodMap.containsKey(className)){
                        methods = methodMap.get(className);
                    }
                    System.out.println("Final className :" + className + " Fianl attributes :" + attributes + " Final Methods :" + methods);

                }
                outputMessage += "\nClass Name : " + className + "\t\nAttributes : " + attributes + "\n\tMethods: " + methods + "\n";

            }
            
            
            if(!attributeMap.isEmpty() || !methodMap.isEmpty()){
                outputMessage = "You have selected the artefacts as follows:" + outputMessage;
                System.out.println(outputMessage);
            }
            else {
                outputMessage = "You haven't select anything. System assumes none of the classes consists any of the the attributes or methods";
                System.out.println(outputMessage);
            }
            int dialogResult = JOptionPane.showConfirmDialog(null, outputMessage, "Confirmation Dialog", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (dialogResult == JOptionPane.YES_OPTION) {
                //close the gui
                frame.dispose();
                //frame.setVisible(false);
                //System.exit(0);
            }
            if (dialogResult == JOptionPane.NO_OPTION) {

            }
        } else {
            outputMessage = "You haven't select anything. System assumes none of the classes consists any of the the attributes or methods";

            int dialogResult = JOptionPane.showConfirmDialog(null, outputMessage, "Confirmation Dialog", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if(dialogResult == JOptionPane.YES_OPTION){
                frame.dispose();
                //frame.setVisible(false);
                //System.exit(0);
            }
            else if(dialogResult == JOptionPane.NO_OPTION){
                
            }

        }
    }

    private boolean isDuplicatesInDisplayingWindowForAttribute(String attrName) {
        boolean result = false;
        HashSet attributes = new HashSet();
        String className = "";
        if (!previouslySelectedClass.isEmpty()) {
            if (!attributeMap.isEmpty()) {
                if (attributeMap.containsKey(previouslySelectedClass)) {
                    attributes = attributeMap.get(previouslySelectedClass);
                    if (!attributes.isEmpty()) {
                        if (attributes.contains(attrName)) {
                            result = true;
                        }
                    }
                }
            }
        }

        return result;
    }

    private boolean isDuplicatesInDisplayingWindowForMethod(String methodName) {
        boolean result = false;
        HashSet methods = new HashSet();
        if (!previouslySelectedClass.isEmpty()) {
            if (!methodMap.isEmpty()) {
                if (methodMap.containsKey(previouslySelectedClass)) {
                    methods = methodMap.get(previouslySelectedClass);
                    if (!methods.isEmpty()) {
                        if (methods.contains(methodName)) {
                            result = true;
                        }
                    }
                }
            }
        }

        return result;
    }

    public HashMap getClassWithAttribute() {
        return attributeMap;
    }

    public HashMap getClassWithMethod() {
        return methodMap;
    }
    public JFrame getFrame(){
        return frame;
    }

    /**
     * Create an Edit menu to support cut/copy/paste.
     */
    public JMenuBar createMenuBar() {
        JMenuItem menuItem = null;
        JMenuBar menuBar = new JMenuBar();
        JMenu mainMenu = new JMenu("Edit");
        mainMenu.setMnemonic(KeyEvent.VK_E);
        TransferActionListener actionListener = new TransferActionListener();

        menuItem = new JMenuItem("Cut");
        menuItem.setActionCommand((String) TransferHandler.getCutAction().
                getValue(Action.NAME));
        menuItem.addActionListener(actionListener);
        menuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        menuItem.setMnemonic(KeyEvent.VK_T);
        mainMenu.add(menuItem);

        menuItem = new JMenuItem("Copy");
        menuItem.setActionCommand((String) TransferHandler.getCopyAction().
                getValue(Action.NAME));
        menuItem.addActionListener(actionListener);
        menuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        menuItem.setMnemonic(KeyEvent.VK_C);
        mainMenu.add(menuItem);

        menuItem = new JMenuItem("Paste");
        menuItem.setActionCommand((String) TransferHandler.getPasteAction().
                getValue(Action.NAME));
        menuItem.addActionListener(actionListener);
        menuItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        menuItem.setMnemonic(KeyEvent.VK_P);
        mainMenu.add(menuItem);

        menuBar.add(mainMenu);
        return menuBar;
    }

    /**
     * Add the cut/copy/paste actions to the action map.
     */
    private void setMappings(JList list) {
        ActionMap map = list.getActionMap();
        map.put(TransferHandler.getCutAction().getValue(Action.NAME),
                TransferHandler.getCutAction());
        map.put(TransferHandler.getCopyAction().getValue(Action.NAME),
                TransferHandler.getCopyAction());
        map.put(TransferHandler.getPasteAction().getValue(Action.NAME),
                TransferHandler.getPasteAction());

    }

    /**
     * Create the GUI and show it. For thread safety, this method should be
     * invoked from the event-dispatching thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Multiple Class Handling");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //Create and set up the menu bar and content pane.
        HashSet classList = new HashSet();
        classList.add("application");
        classList.add("Saving account");
        classList.add("Transaction");

        HashSet attributes = new HashSet();
        attributes.add("balance");
        attributes.add("current amount");
        attributes.add("pin");
        attributes.add("total amount");

        HashSet method = new HashSet();
        method.add("access");
        method.add("record");
        method.add("validate");

        MultipleClassListHandlingGUI demo = new MultipleClassListHandlingGUI(classList, attributes, method);
        demo.gridFormClass();

        //ListCutPaste demo = new ListCutPaste();
        frame.setJMenuBar(demo.createMenuBar());
        demo.setOpaque(true); //content panes must be opaque
        frame.setContentPane(demo);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public void openGUI() {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();

            }
        });

    }

}
