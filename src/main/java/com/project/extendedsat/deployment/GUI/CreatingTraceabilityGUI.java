package com.project.extendedsat.deployment.GUI;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Label;

public class CreatingTraceabilityGUI {

	protected Shell shlCreatingTraceability;

	
	
	
	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			CreatingTraceabilityGUI window = new CreatingTraceabilityGUI();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	
	
	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shlCreatingTraceability.open();
		shlCreatingTraceability.layout();
		while (!shlCreatingTraceability.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	
	
	
	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlCreatingTraceability = new Shell();
		shlCreatingTraceability.setSize(558, 337);
		shlCreatingTraceability.setText("Creating Traceability");
		
		Button btnNewButton = new Button(shlCreatingTraceability, SWT.NONE);
		btnNewButton.setBounds(232, 273, 86, 28);
		btnNewButton.setText("Cancel");
		
		ProgressBar progressBar = new ProgressBar(shlCreatingTraceability, SWT.NONE);
		progressBar.setSelection(25);
		progressBar.setBounds(10, 235, 538, 12);
		
		Label lblNewLabel = new Label(shlCreatingTraceability, SWT.NONE);
		lblNewLabel.setBounds(10, 10, 538, 203);
		lblNewLabel.setText("Creating XML......\n");

	}
}
