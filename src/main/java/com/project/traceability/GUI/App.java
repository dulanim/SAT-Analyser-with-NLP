package com.project.traceability.GUI;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

public class App {

	protected Shell shell;
	private Label lblword1;
	private Label lblword2;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			App window = new App();
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
		shell.open();
		shell.layout();
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
		
//		Composite composite_1 = new Composite(shell, SWT.NONE);
//		composite_1.setBounds(30, 527, 565, 64);
		final Button btnEditAddition = new Button(shell, SWT.NONE);
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
		
		
		Button btn_Add = new Button(shell, SWT.NONE);
		btn_Add.setBounds(464, 25, 91, 29);
		btn_Add.setText("Add To>>");

	}

}
