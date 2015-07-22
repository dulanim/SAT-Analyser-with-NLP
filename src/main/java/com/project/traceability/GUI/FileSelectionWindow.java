/**
 * 
 */
package com.project.traceability.GUI;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.layout.grouplayout.GroupLayout;

import com.project.traceability.common.PropertyFile;

/**
 * @author Gitanjali Nov 17, 2014
 */
public class FileSelectionWindow {

	Shell shell;
	Composite composite;

	/**
	 * Launch the application.
	 * O
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			FileSelectionWindow window = new FileSelectionWindow();
			window.open("project");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public Shell open(String project) {
		Display display = Display.getDefault();
		createContents(project);
		shell.open();
		shell.layout();
		return shell;
	}
	
	public void eventLoop(Display display) {
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents(final String project) {
		shell = new Shell();
		shell.setSize(449, 299);
		shell.setText("File Selection");
		center(shell);
		Label lblSelectTwoFiles = new Label(shell, SWT.NONE);
		FormData fd_lblSelectTwoFiles = new FormData();
		fd_lblSelectTwoFiles.left = new FormAttachment(0, 10);
		fd_lblSelectTwoFiles.right = new FormAttachment(0, 177);
		lblSelectTwoFiles.setLayoutData(fd_lblSelectTwoFiles);
		lblSelectTwoFiles.setText("Select two files");

		composite = new Composite(shell, SWT.NONE);

		fd_lblSelectTwoFiles.bottom = new FormAttachment(composite, -6);
		FormData fd_composite = new FormData();
		fd_composite.right = new FormAttachment(0, 300);
		fd_composite.top = new FormAttachment(0, 38);
		fd_composite.left = new FormAttachment(0, 10);
		composite.setLayoutData(fd_composite);
		
		final Label lblNewLabel = new Label(composite, SWT.NONE);
		lblNewLabel.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_RED));
		
		GroupLayout gl_composite = new GroupLayout(composite);
		gl_composite.setHorizontalGroup(
			gl_composite.createParallelGroup(GroupLayout.LEADING)
				.add(gl_composite.createSequentialGroup()
					.add(61)
					.add(lblNewLabel, GroupLayout.PREFERRED_SIZE, 197, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(32, Short.MAX_VALUE))
		);
		gl_composite.setVerticalGroup(
			gl_composite.createParallelGroup(GroupLayout.TRAILING)
				.add(GroupLayout.LEADING, gl_composite.createSequentialGroup()
					.add(140)
					.add(lblNewLabel)
					.addContainerGap(27, Short.MAX_VALUE))
		);
		composite.setLayout(gl_composite);

		File projectFile = new File(PropertyFile.filePath);
		ArrayList<String> projectFiles = new ArrayList<String>(
				Arrays.asList(projectFile.list()));
		final Set<String> selectedFilesSet = new HashSet<String>();
		for (int i = 0; i < projectFiles.size(); i++) {
			if (projectFiles.get(i).equals(project)) {
				File file = new File(PropertyFile.filePath + project + "/");
				ArrayList<String> files = new ArrayList<String>(
						Arrays.asList(file.list()));
				files.remove("Relations.xml");
				int count = 0;
				for (int j = 0; j < files.size(); j++) {
					if(files.get(j).contains(".xml")) {
						final Button btnCheckButton = new Button(composite,
								SWT.CHECK);
						btnCheckButton.setText(files.get(j));
						btnCheckButton.addSelectionListener(new SelectionAdapter() {
							@Override
							public void widgetSelected(SelectionEvent e) {
								if (btnCheckButton.getSelection()) {
									selectedFilesSet.add(btnCheckButton.getText());
								} else if (!btnCheckButton.getSelection()) {
									selectedFilesSet.remove(btnCheckButton.getText());
								}
							}
						});
						btnCheckButton.setBounds(30, (j + 2 - count) * 20, 400,
								(j + 1 - count) * 20);
					} else
						count++;
				}
			}
		}
		shell.setLayout(new FormLayout());
		Button btnCompare = new Button(shell, SWT.NONE);
		fd_composite.bottom = new FormAttachment(btnCompare, -6);
		FormData fd_btnCompare = new FormData();
		fd_btnCompare.top = new FormAttachment(0, 226);
		fd_btnCompare.left = new FormAttachment(0, 284);
		btnCompare.setLayoutData(fd_btnCompare);
		btnCompare.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(selectedFilesSet.size() == 0)
					lblNewLabel.setText("Select two files first");
				else if(selectedFilesSet.size() != 2)
					lblNewLabel.setText("Select only two files");
				else {
					final CompareWindow window = new CompareWindow();
					final ArrayList<String> selectedFiles = new ArrayList<String>(selectedFilesSet);
					lblNewLabel.setText("");
					shell.close();
					window.open(project, selectedFiles);
					/*Runnable longJob = new Runnable() {
						boolean done = false;
						int id;
						@Override
						public void run() {
							Thread thread = new Thread(new Runnable() {
								@Override
								public void run() {
									Display.getCurrent().asyncExec(new Runnable() {
										@Override
										public void run() {
											
										}
									});
									//if (display.isDisposed()) return;
									Display.getCurrent().syncExec(new Runnable() {
										@Override
										public void run() {
											
										}
									});
									done = true;
									Display.getCurrent().wake();
								}
							});
							thread.start();
							while (!done && !shell.isDisposed()) {
								if (!Display.getCurrent().readAndDispatch())
									Display.getCurrent().sleep();
							}
						}
					};
					BusyIndicator.showWhile(Display.getCurrent(), longJob); */				
				}
			}
		});
		btnCompare.setText("Compare");

		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shell.close();
			}
		});
		FormData fd_btnCancel = new FormData();
		fd_btnCancel.right = new FormAttachment(0, 412);
		fd_btnCancel.top = new FormAttachment(0, 226);
		fd_btnCancel.left = new FormAttachment(0, 363);
		btnCancel.setLayoutData(fd_btnCancel);
		btnCancel.setText("Cancel");

	}

	public void center(Shell shell) {

		Rectangle bds = shell.getDisplay().getBounds();

		Point p = shell.getSize();
		shell.setFullScreen(true);
		int nLeft = (bds.width - p.x) / 2;
		int nTop = (bds.height - p.y) / 2;

		shell.setBounds(nLeft, nTop, p.x, p.y);
	}
}
