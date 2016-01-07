package com.project.traceability.GUI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

class InputDialog extends Dialog {
  private String message;

  private String input;
  public static String selectedValue;
  public static Display display;
  public static Shell shell;

  public static void main(String args[]){
	  
	    InputDialog dlg = new InputDialog(shell);
	    String input = dlg.open();
	    if (input != null) {
	      // User clicked OK; set the text into the label
	      System.out.println(input);
	    }

	    while (!shell.isDisposed()) {
	      if (!display.readAndDispatch()) {
	        display.sleep();
	      }
	    }
	    display.dispose();
  }
  public InputDialog(Shell parent) {
    this(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
  }

  public InputDialog(Shell parent, int style) {
    super(parent, style);
    setText("Input Dialog");
    setMessage("Please enter a value:");
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getInput() {
    return input;
  }

  public void setInput(String input) {
    this.input = input;
  }

  public String open() {
    Shell shell = new Shell(getParent(), getStyle());
    shell.setSize(390, 93);
    shell.setText(getText());
    createContents(shell);
    shell.pack();
    shell.open();
    Display display = getParent().getDisplay();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
    return input;
  }

  private void createContents(final Shell shell) {
    shell.setLayout(new GridLayout(2, true));

    Label label = new Label(shell, SWT.NONE);
    label.setText(message);
    GridData data = new GridData();
    data.horizontalSpan = 2;
    label.setLayoutData(data);

    final Text text = new Text(shell, SWT.BORDER);
    data = new GridData(GridData.FILL_HORIZONTAL);
    data.horizontalSpan = 2;
    text.setLayoutData(data);

    Button ok = new Button(shell, SWT.PUSH);
    ok.setText("OK");
    data = new GridData(GridData.FILL_HORIZONTAL);
    ok.setLayoutData(data);
    ok.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        input = text.getText();
        
        if(input.equals("")){
        	 MessageBox messageBox = new MessageBox(shell, SWT.ERROR_CANNOT_GET_TEXT
		              | SWT.OK);
		     messageBox.setMessage("You should give proper value");
		     messageBox.setText("Information For Addition");
		     messageBox.open();
        }else{
        	selectedValue = input;
        }
      }
    });

    Button cancel = new Button(shell, SWT.PUSH);
    cancel.setText("Cancel");
    data = new GridData(GridData.FILL_HORIZONTAL);
    cancel.setLayoutData(data);
    cancel.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent event) {
        input = null;
        shell.close();
      }
    });

    shell.setDefaultButton(ok);
  }
}