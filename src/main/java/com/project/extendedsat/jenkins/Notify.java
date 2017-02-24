package com.project.extendedsat.jenkins;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.project.traceability.GUI.HomeGUI;
import com.project.traceability.GUI.ProjectCreateWindow;

import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.wb.swt.SWTResourceManager;

public class Notify {

	protected Shell shell;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Notify window = new Notify();
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
		
		display.syncExec(new Runnable() {
		    public void run() {
		    	createContents();
				shell.open();
				shell.layout();
				while (!shell.isDisposed()) {
					if (!display.readAndDispatch()) {
						display.sleep();
					}
				}
		    }
		});
		
		
		
	
	}

	/**
	 * Create contents of the window.
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("New Jenkis Build Detected");
		
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblNewLabel.setBounds(60, 82, 330, 27);
		lblNewLabel.setText("Jenkins has completed a new build succesfully");
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				//ProjectCreateWindow.main(null);
				HomeGUI.main(null);
			}
		});
		btnNewButton.setBounds(140, 144, 143, 33);
		btnNewButton.setText("LAUNCH SAT ANALYZER");
		
		Button btnNewButton_1 = new Button(shell, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				System.exit(0);
			}
		});
		btnNewButton_1.setBounds(26, 214, 75, 25);
		btnNewButton_1.setText("Dismiss");

	}
}
