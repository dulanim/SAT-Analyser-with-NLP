package com.project.traceability.GUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.wb.swt.SWTResourceManager;

import com.project.traceability.ontology.models.HelpFileReader;
import com.project.traceability.ontology.models.Helps;
import com.project.traceability.ontology.models.ModelCreator;
import com.project.traceability.ontology.models.NavigationModel;
import com.project.traceability.ontology.models.StaticData;
import com.project.traceability.ontology.models.Word;
public class WordExpWindows {

	protected Shell shell;
	Label lblword2; // it f word 2 name in text format
	Label lblword1; // it keeps track of word 2 name in text format
	public static Combo combo_parent; // it keeps track of parent names from model.owl file
	public static Combo combo_proeprty;//it keeps track of all property name
	public static Combo combo_values;//it keeps values according to user selection 
	public static Word word; // give explain about both word in given scenario
	public static String word1;
	public static String word2;
	private static WordExpWindows window;
	private static java.util.List<String> properties;
	private static java.util.List<String> values;
	private static java.util.List<String> parents;
	private static java.util.Stack<String> propertiesStack;
	private static java.util.Stack<String> parentsStack;
	private static java.util.List<String> removedParent;
	private static java.util.List<String> removedProperty;
	private static java.util.Stack<String> descriptionsStack;
	private java.util.List<String> list = null;
	public String selectedParentItem;
	public String selectedPropertyItem;
	public static String selectedValueItem;
	

	
	TreeItem wordTree;
	Tree tree_desc;
	TreeItem parent;
	TreeItem parent_child;
	TreeItem property;
	TreeItem property_child;
	TreeItem property_child_child;
	TreeItem descriptions;
	TreeItem description_child;
	
	StyledText styledText;
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
//			word1 = "Cocnut";
//			word2 = "CocounantTree";
//			word = new Word();
//			word.setWordType(StaticData.OWL_CLASS);
			
			parents = new ArrayList<>();
			properties = new ArrayList<>();
			values = new ArrayList<>();
			
			propertiesStack = new Stack<>();
			parentsStack = new Stack<>();
			descriptionsStack = new Stack<>();
			
			removedParent = new ArrayList<>();
			removedProperty = new ArrayList<>();
			window = getWindowInstance();
			window.open();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private WordExpWindows(){
		
	}
	
	public static WordExpWindows getWindowInstance(){
		
			if(window == null){
				window = new WordExpWindows();
			}
			return window;
	}
	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		window.setWords(word1, word2);
		window.setParentNames();
		window.setProperties(true);
		shell.open();
		shell.layout();
		
		InputDialog.display = display;
		InputDialog.shell = shell;
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(946, 635);
		shell.setText("SWT Application");
		
		com.project.traceability.common.Dimension.toCenter(shell);
		Composite composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setBounds(30, 527, 565, 64);
		final Button btnEditAddition = new Button(composite_1, SWT.NONE);
		btnEditAddition.setBounds(160, 25, 136, 29);
		
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		lblNewLabel.setBounds(30, 22, 90, 29);
		lblNewLabel.setText("Word1");
		
		Label lblNewLabel_1 = new Label(shell, SWT.NONE);
		lblNewLabel_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		lblNewLabel_1.setBounds(30, 47, 70, 17);
		lblNewLabel_1.setText("Word2");
		
		lblword1 = new Label(shell, SWT.NONE);
		lblword1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblword1.setBounds(126, 22, 154, 17);
		lblword1.setText("Mother");
		
		lblword2 = new Label(shell, SWT.NONE);
		lblword2.setBounds(126, 47, 130, 17);
		lblword2.setText("Amma");
		
		
		Button btn_Add = new Button(composite_1, SWT.NONE);
		btn_Add.setBounds(464, 25, 91, 29);
		btn_Add.setText("Add To>>");
		btn_Add.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				/*
				 * add to model method 
				 * parent single 
				 * property-value map
				 */
				ModelCreator creator = ModelCreator.getModelInstance();
				
				word.setPropertyName(properties);
				word.setValue(values);
				word.setParentName(parents);
				
				creator.createNewNode(word1, word2, word);
				
				tree_desc.removeAll();
				shell.dispose();
			}
		});
		
		Group grpExplainWord = new Group(shell, SWT.NONE);
		grpExplainWord.setText("Explain Word");
		grpExplainWord.setBounds(30, 70, 557, 133);
		
		combo_parent = new Combo(grpExplainWord, SWT.NONE);
		combo_parent.setBounds(10, 42, 155, 29);
		
		combo_proeprty = new Combo(grpExplainWord, SWT.NONE);
		combo_proeprty.setBounds(192, 42, 155, 29);
		
		Button btnExplain = new Button(grpExplainWord, SWT.NONE);
		btnExplain.setBounds(452, 85, 91, 29);
		btnExplain.setText("Explain ");
		btnExplain.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				
				/*
				 * those two words should have properties and values
				 * may have parent 
				 * if parent is not there, get default 
				 */
				String items[] = combo_parent.getItems();
				int pos = combo_parent.getSelectionIndex();
				if(pos>=0)
					selectedParentItem = items[pos];
				else
					selectedParentItem = "";
				
				items = combo_proeprty.getItems();
				int posProps = combo_proeprty.getSelectionIndex();
				if(posProps>=0)
					selectedPropertyItem = items[posProps];
				else
					selectedPropertyItem = "hasDefault";
				
				items =  combo_values.getItems();
				pos = combo_values.getSelectionIndex();
				if(pos>=0)
					selectedValueItem = items[pos];
				else 
					selectedValueItem = "aValue";
					if(selectedPropertyItem != ""){
						if(selectedValueItem != ""){
							
							if(selectedParentItem.equals(word.getType())
									|| selectedPropertyItem.equals("hasDefault")
									|| selectedValueItem.equals("aValue")){
								/*
								 * pop up give instruction 
								 * to user that all are default value it may affect to you
								 * after adding those words may not equal
								 * to avoid select values 
								 */
								 MessageBox messageBox = new MessageBox(shell, SWT.ERROR_CANNOT_GET_TEXT
							              | SWT.OK);
							     messageBox.setMessage("Those selection has invalid selection \n"
							     		+ "It affect for adding word. \nYou should select proper value or select Add Custom Value");
							     messageBox.setText("Information For Selection");
							     messageBox.open();
							}else{
								if(selectedPropertyItem.equals("hasDefault")
										||selectedValueItem.equals("aValue")){
									 MessageBox messageBox = new MessageBox(shell, SWT.ERROR_CANNOT_GET_TEXT
								              | SWT.OK);
								     messageBox.setMessage("Those selection has invalid selection \n"
								     		+ "It affect for adding word. \nYou should select proper value for Property or Value Field");
								     messageBox.setText("Information For Selection");
								     messageBox.open();
								}else{
									/*
									 * this block does not have default value
									 */
									parents.add(selectedParentItem);
									properties.add(selectedPropertyItem);
									values.add(selectedValueItem);
									
									propertiesStack.push(selectedPropertyItem);
									parentsStack.push(selectedParentItem);
									word.setParentName(parents);
									word.setPropertyName(properties);
									word.setValue(values);
									
									if(properties.size() == 1){
										//create root with word names 
											addListValue(true);
									}else{
										//create sub words
										addListValue(false);
									}
								}
							}
						}
					}
				}
			}
		);
		
		
		
		
		combo_values = new Combo(grpExplainWord, SWT.NONE);
		combo_values.setBounds(358, 42, 136, 29);
		
		Label lblParentName = new Label(grpExplainWord, SWT.NONE);
		lblParentName.setBounds(10, 11, 136, 17);
		lblParentName.setText("Parent Name");
		
		Label lblPropertyName = new Label(grpExplainWord, SWT.NONE);
		lblPropertyName.setBounds(192, 11, 155, 17);
		lblPropertyName.setText("Property Name");
		
		Label lblPropertyValue = new Label(grpExplainWord, SWT.NONE);
		lblPropertyValue.setBounds(358, 11, 120, 17);
		lblPropertyValue.setText("Property Value ");
		
		final Button btnRemoveAddition = new Button(composite_1, SWT.NONE);
		btnRemoveAddition.setBounds(0, 25, 136, 29);
		btnRemoveAddition.setText("Remove Last Added Content");
		btnRemoveAddition.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				/*
				 * this method remove last added contents to tree
				 * after remove have to add deleted property name to property combo
				 * 
				 * if remove happened with parent enable parent combo box
				 */
				
			
				TreeItem items[]= tree_desc.getItems();
				TreeItem itemRoots[];
				if(items.length>0)
					 itemRoots = items[0].getItems();
				else{
					showPopUp("Nothing in Tree","Information Message");
					return;
				}
				boolean isParentRemoved = false;
				boolean isPropertyRemoved = false;
				boolean isDescriptionRemoved = false;
				for(TreeItem item:itemRoots){
					TreeItem selectionitem[] = item.getItems();
					List<TreeItem> itemList = Arrays.asList(selectionitem);
					
					for(TreeItem listItm:itemList){
						
							String parentText = item.getText();
							String text = listItm.getText();
							String lastAddedParent = "";
							String lastAddedProperty = "";
							String lastAddedDescripts = "";
							if(parentsStack.size()>0)
								 lastAddedParent = parentsStack.peek();
							if(propertiesStack.size()>0)
								lastAddedProperty = propertiesStack.peek();
							if(descriptionsStack.size()>0)
								lastAddedDescripts = descriptionsStack.peek();
							if(text.equals(lastAddedParent) && !isParentRemoved){
								//last parent removed from tree	
								parents.remove(lastAddedParent);
								if(!parentsStack.isEmpty())
									parentsStack.pop();
								isParentRemoved = true;
								
								listItm.dispose();
								removedParent.add(lastAddedParent);
						}else if(text.equals(lastAddedProperty) && !isPropertyRemoved){
								//last property and value removed from tree
								
								int pos = properties.indexOf(lastAddedProperty);
								properties.remove(lastAddedProperty);
								values.remove(pos);
								
								if(!propertiesStack.isEmpty())
									propertiesStack.pop();
								listItm.dispose();
								isPropertyRemoved = true;
								removedProperty.add(lastAddedProperty);
							}else if(parentText.equals("Descriptions") && !isDescriptionRemoved){
								//last added description removed
								if(text.equals(lastAddedDescripts)){
									listItm.dispose();
									descriptionsStack.pop();
									isDescriptionRemoved = true;
								}
							}
						
					}
					
					
				}
				//remove if all property and value pair is not in tree
				if(properties.size() == 0 || values.size() == 0){
					
					///all contents were removed from tree
					properties.clear();
					parents.clear();
					values.clear();
					items[0].dispose();
				}
				setNewComboItmes(combo_parent, removedParent);
				setNewComboItmes(combo_proeprty, removedProperty);
			}
		});
		Group grpWordsDescription = new Group(shell, SWT.NONE);
		grpWordsDescription.setText("Words Description");
		grpWordsDescription.setBounds(30, 209, 555, 312);
		
		tree_desc = new Tree(grpWordsDescription, SWT.BORDER);
		tree_desc.setBounds(25, 10, 493, 276);
		tree_desc.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event arg0) {
				TreeItem selection[] = tree_desc.getSelection();
				
				btnRemoveAddition.setEnabled(true);
				btnEditAddition.setEnabled(true);
				for(int i = 0;i<selection.length;i++){
					
					String temp = selection[i].toString();
					if(temp.contains("Parent")){
						btnRemoveAddition.setEnabled(true);
						btnEditAddition.setEnabled(false);
					}
				
					if(temp.contains("Property")
							||temp.contains("Descriptions")){
						//it prevents remove parent
						btnRemoveAddition.setEnabled(true);
						btnEditAddition.setEnabled(false);
					}if(temp.contains(selectedParentItem)){
						//it prevent the parent's child
						btnRemoveAddition.setEnabled(true);
						btnEditAddition.setEnabled(true);
					}if(temp.contains(word1) ||
							temp.contains(word2)){
						//it prevent the words edit 
						// it allow the remove
						btnRemoveAddition.setEnabled(true);
						btnEditAddition.setEnabled(false);
					}
					///if(parent)
				}
			}
		});
		//Font boldFont = new Font( list.getDisplay(), new FontData( "Arial", 12, SWT.BOLD ) );
		//tree_desc.setFont( boldFont );
		
		btnEditAddition.setText("Clear All");
		btnEditAddition.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
//				TreeItem selection[] = tree_desc.getSelection();
//				
//				for(int i=0;i<selection.length;i++){
//					
//					String tempSelection = selection[i].toString();
//					if(!(tempSelection.contains("Property") || tempSelection.contains("Parent")
//							|| tempSelection.contains("Discriptions"))){
//						//selection[i];
//						
//						System.out.println(tempSelection + " is editing now");
//					}
//					
//				}
				TreeItem item[] = tree_desc.getItems();
				if(item.length>=1){

					removedProperty = new ArrayList<>(parents);
					removedProperty = new ArrayList<>(properties);
					
					parents.clear();
					properties.clear();
					setNewComboItmes(combo_parent, removedParent);
					setNewComboItmes(combo_proeprty, removedProperty);
					
					item[0].dispose();
				}else{
					String message = "Nothing in Tree\n Before Clear add contents";
					String title = "Information";
					showPopUp(message,title);
					return ;
				}
				
			}

			
		});
		
		Label label = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(10, 57, 566, 7);
		
		Label lblWordsExplanationWindow = new Label(shell, SWT.NONE);
		lblWordsExplanationWindow.setBounds(235, -1, 360, 29);
		lblWordsExplanationWindow.setText("Words Explanation Window");

		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBounds(591, 10, 325, 604);
		
		Text text = new Text(composite, SWT.BORDER);
		text.setBounds(10, 35, 194, 27);
		
		Button btnNewButton_1= new Button(composite, SWT.NONE);
		btnNewButton_1.setBounds(214, 33, 91, 29);
		btnNewButton_1.setText("Search");
		
	
		
		styledText = new StyledText(composite, SWT.MULTI | SWT.WRAP | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	    
		styledText.setBounds(10, 68, 315, 536);
		styledText.setLayoutData(new GridData(GridData.FILL_BOTH));
		styledText.setLineIndent(0, 1, 50);
	    Font font = new Font(shell.getDisplay(), "Times New Roman", 12, SWT.NORMAL);
	    styledText.setFont(font);
	    
	    createStyledText();
	    
	    StyleRange styleRange1 = new StyleRange();
	    styleRange1.start = 2;
	    styleRange1.length = 16;
	    styleRange1.foreground = shell.getDisplay().getSystemColor(SWT.COLOR_BLUE);
	    styleRange1.background = shell.getDisplay().getSystemColor(SWT.COLOR_YELLOW);
	    styleRange1.fontStyle = SWT.BOLD;
	    
	    StyleRange styleRange2 = new StyleRange();
	    styleRange2.start = 14;
	    styleRange2.length = 3;
	    styleRange2.fontStyle = SWT.NORMAL;
	    styleRange2.foreground = shell.getDisplay().getSystemColor(SWT.COLOR_YELLOW);
	    styleRange2.background = shell.getDisplay().getSystemColor(SWT.COLOR_BLUE);
	    
	    styledText.setText("Help1\n"
	    		+ "\tQuestion1\n"
	    		+ "	   What is the Parent?\n"
	    		+ "    	1.This is the sample parent");
	    styledText.setEditable(false);
		composite_1 = new Composite(shell, SWT.NONE);
		composite_1.setBounds(30, 527, 565, 64);
		
		addHelpContents();
		
	}
	
	
	public void setWords(String word1,String word2){
		//just add word name to separate label field on GUI
		
		if(!(word1.equals("") || word1.equals(null)) && 
				!(word2.equals("") || word2.equals(null))){
			lblword1.setText(word1);
			lblword2.setText(word2);
		}else{
			if(word1.equals("")){
				throw new IllegalArgumentException("Word1 has null statement \n "
						+ "which should not null");
			}else{
				throw new IllegalArgumentException("Word2 has null statement \n "
						+ "which should not null");
			}
		}
	}
	private void showPopUp(String message,String title) {
		// TODO Auto-generated method stub
		MessageBox messageBox = new MessageBox(shell, SWT.ERROR_CANNOT_GET_TEXT
        		| SWT.OK);
		messageBox.setMessage(message);
		messageBox.setText(title);
		messageBox.open();
	}
	private void setParentNames(){
		
		/*
		 * add parent name which are in model file 
		 * read the model file and get the all parent classes 
		 * and put to parent combo box
		 */
		
		NavigationModel navigator = NavigationModel.getNavigatorInstane();
		if(combo_parent !=null){
			final java.util.List<String> list = navigator.
										getSubClassFor(word.getType());
			for(int i=0;i<list.size();i++){
				combo_parent.add(list.get(i),i);
			}
			
			combo_parent.addSelectionListener(new SelectionListener() {
				
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					// TODO Auto-generated method stub
                    String items[] = combo_parent.getItems();
					selectedParentItem = items[combo_parent.getSelectionIndex()];
                                        
                    if(selectedParentItem.equals("<<---------------------------->>")){
	                     /*
	                      * pop up give instruction press Add Custom Value or other value above this pattern
	                      */
	                    MessageBox messageBox = new MessageBox(shell, SWT.ERROR_CANNOT_GET_TEXT
	                                        		| SWT.OK);
	                    messageBox.setMessage("You should select Add Custom Value to add custom value");
	                    messageBox.setText("Information For Selection");
	                    messageBox.open();
	                    return;
                    }else if(selectedParentItem.equals("Add Custom Parent Value")){
                        InputDialog.type = "Parent";
                        InputDialog.main(null);
                        selectedParentItem = InputDialog.selectedValue;
                    }                    
				}
				
				@Override
				public void widgetDefaultSelected(SelectionEvent arg0) {
					/*
					 * if user did not select get the word's type as default					
					 */
					if(!word.equals("")){
						selectedParentItem = word.getType();
					}else{
						selectedParentItem = "Word";//default 
					}
				}
			});
			
		}else{
			throw new NullPointerException(" Nullpoint Execpeption of Parent Combobox "
					+ "\nInitialize First");
		}
		
		
	}
	
	private void setProperties(boolean flag){
		/*
		 * add parent name which are in model file 
		 * read the model file and get the all parent classes 
		 * and put to parent combo box
		 */
		
		NavigationModel navigator = NavigationModel.getNavigatorInstane();
		
		if(flag){
			list = navigator.getAllProperties();
		}else{
			
		}
		if(combo_proeprty !=null){
			for(int i=0;i<list.size();i++){
				combo_proeprty.add(list.get(i),i);
			}
		}else{
			throw new NullPointerException(" Nullpoint Execpeption of Parent Combobox "
					+ "\nInitialize First");
		}
		combo_proeprty.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

                String items[] = combo_proeprty.getItems();
				selectedPropertyItem = items[combo_proeprty.getSelectionIndex()];
				if(selectedPropertyItem.equals("Add Custom Property")){
                                        InputDialog.type = "Property";
					
                  InputDialog.type = "Property";
                  InputDialog.main(null);
//					selectedValueItem = InputDialog.selectedValue;
//                    combo_proeprty.add(selectedValueItem);
				}else if(selectedPropertyItem.equals("<<---------------------------->>")){
					/*
					 * pop up give instruction press Add Custom Value or other value above this pattern
					 */
					 MessageBox messageBox = new MessageBox(shell, SWT.ERROR_CANNOT_GET_TEXT
				              | SWT.OK);
                                         messageBox.setMessage("You should select proper value or select Add Custom Value");
                                         messageBox.setText("Information For Selection");
                                         messageBox.open();
				}else{
					//get the selected value
					setPropertyNames(selectedPropertyItem);
					//combo_values.add
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				selectedPropertyItem = "hasDefault";
				
			}
		});
	}
	public void setPropertyNames(String propsName){
		/*
		 * it fetch the all properties from model 
		 * actually communicate with Navigator model
		 * it gives the result back to this method
		 */
		NavigationModel navigator = NavigationModel.getNavigatorInstane();
		final java.util.List<String> values = navigator.getAllValues1(propsName);
		combo_values.removeAll();
		for(int i=0;i<values.size();i++){
			combo_values.add(values.get(i));
		}
		combo_values.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				int index = combo_values.getSelectionIndex();
				String items[] = combo_values.getItems();
				String selectedItem = items[index];
				if(selectedItem.equals("Add Custom Value")){
					InputDialog.type = "Value";
					InputDialog.main(null);
//					selectedValueItem = InputDialog.selectedValue;
				}else if(selectedItem.equals("<<---------------------------->>")){
					/*
					 * pop up give instruction press Add Custom Value or other value above this pattern
					 */
					 MessageBox messageBox = new MessageBox(shell, SWT.ERROR_CANNOT_GET_TEXT
				              | SWT.OK);
				     messageBox.setMessage("You should select proper value or select Add Custom Value");
				     messageBox.setText("Information For Selection");
				     messageBox.open();
				}else{
					//get the selected value
					selectedValueItem = selectedItem;
					//combo_values.add
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub
				selectedValueItem = "aValue"; // default value should notify to user
			}
		});
	}
	
	private void addListValue(boolean flag){
		
		String strMessage = "";
		strMessage = word1  + " or " + word2 + " has parent " + selectedParentItem +" Those have " +
		"" + selectedPropertyItem + " with the value " + selectedValueItem;
		
		descriptionsStack.push(strMessage);
		if(flag){
			//combo_parent.setEnabled(false);
			wordTree = new TreeItem(tree_desc, SWT.NONE);
			wordTree.setText(word1 + "/"  + word2);
			
			parent = new TreeItem(wordTree,SWT.NONE);
			parent.setText("Parent");
			
			parent_child = new TreeItem(parent,SWT.NONE);
			parent_child.setText(selectedParentItem);
			
			if(selectedParentItem == null || selectedParentItem.equals(""))
					parent_child.dispose();
			
			property = new TreeItem(wordTree,SWT.NONE);
			property.setText("Property");
			
			property_child = new TreeItem(property,SWT.NONE);
			property_child.setText(selectedPropertyItem);
			
			property_child_child = new TreeItem(property_child,SWT.NONE);
			property_child_child.setText(selectedValueItem);
			
			wordTree.setExpanded(true);
			parent.setExpanded(true);
			property.setExpanded(true);
			property_child.setExpanded(true);
			
			descriptions = new TreeItem(wordTree,SWT.NONE);
			descriptions.setText("Descriptions");
			
			description_child = new TreeItem(descriptions,SWT.NONE);
			description_child.setText(strMessage);
		}else{

			parent_child = new TreeItem(parent,SWT.NONE);
			parent_child.setText(selectedParentItem);
			
			if(selectedParentItem == null || selectedParentItem.equals(""))
				parent_child.dispose();
			
			property_child = new TreeItem(property,SWT.NONE);
			property_child.setText(selectedPropertyItem);
			
			property_child_child = new TreeItem(property_child,SWT.NONE);
			property_child_child.setText(selectedValueItem);
			
			description_child = new TreeItem(descriptions,SWT.NONE);
			description_child.setText(strMessage);
			
			property_child.setExpanded(true);
		}
		
//		parent_child.addListener(SWT.MouseHover, new Listener() {
//			
//			@Override
//			public void handleEvent(Event arg0) {
//				// TODO Auto-generated method stub
//				
//				String pointText = arg0.text;
//				System.out.println(pointText);
//			}
//		});
		//NavigationModel navigator = NavigationModel.getNavigatorInstane();
//		java.util.List<String> list = navigator.getAllProperties();
		String itemsProperty[] = combo_proeprty.getItems();
		String itemsParent[] = combo_parent.getItems();
		List<String> list = new ArrayList<>(Arrays.asList(itemsProperty));
		list = removeListItem(list, selectedPropertyItem);
//		
//		this.list = list;
		combo_values.removeAll();
		combo_proeprty.removeAll();
		for(int i=0;i<list.size();i++){
			combo_proeprty.add(list.get(i));
		}
		
		list = new ArrayList<>(Arrays.asList(itemsParent));
		list = removeListItem(list, selectedParentItem);
		if(list != null)
			combo_parent.removeAll();
		for(int i=0; list != null && i<list.size();i++){
				combo_parent.add(list.get(i));
		}
	}
	
	private void setNewComboItmes(Combo combo,List<String> itmList){
		
		String comboItems[] = combo.getItems();
		combo.removeAll();
		List<String> list = new ArrayList<>(Arrays.asList(comboItems));
		
		String item1 = list.get(list.size()-1);
		String item2 = list.get(list.size()-2);
		
		list.remove(item1);
		list.remove(item2);
		
		for(String item:itmList){
			if(!list.contains(item))
				list.add(item);
		}
		list.add(item2);
		list.add(item1);
		
		for(int i=0;list != null && i<list.size();i++){
			combo.add(list.get(i));
		}
	}
	private java.util.List<String> removeListItem(java.util.List<String> list,String rmvItem){
		java.util.List<String> result = null;
		
		if(!rmvItem.equals("")){
				if(list.size()>2)
					list.remove(rmvItem);
				result = list;
		}
		return result;
	}
	
	private void createStyledText() {
	    //text = new StyledText(shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL
	      //  | SWT.H_SCROLL);
	    GridData spec = new GridData();
	    spec.horizontalAlignment = GridData.FILL;
	    spec.grabExcessHorizontalSpace = true;
	    spec.verticalAlignment = GridData.FILL;
	    spec.grabExcessVerticalSpace = true;
	    styledText.setLayoutData(spec);
	    styledText.setEditable(false);
	    Color bg = Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
	    styledText.setBackground(bg);
	    
	  }
	public void addHelpContents(){
		
		
		HelpFileReader fileReader = new HelpFileReader();
		java.util.List<Helps> list = fileReader.readWordExpWindowHelps();
		String msg = "";
		for(int i = 0;i<list.size();i++){
			
			//styledText
			Helps aHelp = list.get(i);
			msg += aHelp.getHelpTitle() + "\n  "
						+"" + aHelp.getHelpTag() + "\n  " + "" 
						;
			String desc = "";
			for(int j=0;j<aHelp.getHelpTips().size();j++){
				List<String> descList = aHelp.getHelpTips();
				desc += descList.get(j) + "\n  ";
			}
			
			msg += desc;
		}
		styledText.setText(msg);
	}
}
