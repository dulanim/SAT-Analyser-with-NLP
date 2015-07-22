package com.project.traceability.GUI;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.project.traceability.common.PropertyFile;

public class NewFileWindow {

	static Shell shell;
	private Text text;
	static Path path;
	static String localFilePath;
	static String[] selectedFiles;
	static StyledText codeText;

	static String textString;

	FileDialog fileDialog;
	private Text text_1;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			NewFileWindow window = new NewFileWindow();
			window.open();
			window.eventLoop(Display.getDefault());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void eventLoop(Display display) {
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}


	/**
	 * Open the window.
	 */
	public Shell open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();		
		return shell;		
	}
	
	public Shell open(TreeItem item, String path) {
		Display display = Display.getDefault();
		HomeGUI.trtmNewTreeitem = item;
		HomeGUI.projectPath = path;
		createContents();
		shell.open();
		shell.layout();		
		return shell;		
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 545);
		shell.setText("New File");
		center(shell);
		final Tree tree = new Tree(shell, SWT.BORDER);
		tree.setBounds(12, 57, 412, 338);
		System.out.println(HomeGUI.projectPath + "KKKKKKKK");
		text_1 = new Text(shell, SWT.BORDER);
		if (HomeGUI.projectPath != null)
			text_1.setText(HomeGUI.projectPath);
		text_1.setBounds(12, 30, 412, 21);

		Label lblParentFolderPath = new Label(shell, SWT.NONE);
		lblParentFolderPath.setBounds(12, 10, 104, 15);
		lblParentFolderPath.setText("Parent folder path");

		File projectFile = new File(PropertyFile.filePath);
		projectFile.mkdir();
		ArrayList<String> projectFiles = new ArrayList<String>(
				Arrays.asList(projectFile.list()));

		for (int i = 0; i < projectFiles.size(); i++) {
			TreeItem trtmNewTreeitem = new TreeItem(tree, SWT.NONE);
			trtmNewTreeitem.setText(projectFiles.get(i));
		}

		tree.setSelection(HomeGUI.trtmNewTreeitem);
		tree.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				String string = "";
				TreeItem[] selection = tree.getSelection();
				for (int i = 0; i < selection.length; i++) {
					string += selection[i] + " ";
					HomeGUI.trtmNewTreeitem = selection[i];
				}
				string = string.substring(10, string.length() - 2);
				HomeGUI.projectPath = PropertyFile.filePath + string
						+ "/";
				text_1.setText(HomeGUI.projectPath);
			}
		});

		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shell.close();
			}
		});
		btnCancel.setBounds(320, 472, 49, 25);
		btnCancel.setText("Cancel");

		Label lblFileName = new Label(shell, SWT.NONE);
		lblFileName.setBounds(12, 418, 67, 15);
		lblFileName.setText("File Name :");

		text = new Text(shell, SWT.BORDER);
		text.setBounds(85, 415, 247, 21);

		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(shell, SWT.MULTI);
				fileDialog.setText("Open");
				fileDialog.setFilterPath(PropertyFile.xmlFilePath);
				localFilePath = fileDialog.open();
				localFilePath = localFilePath.replace(Paths.get(localFilePath)
						.getFileName().toString(), "");
				selectedFiles = fileDialog.getFileNames();
				for (int k = 0; k < selectedFiles.length; k++) {
					text.append(selectedFiles[k] + " , ");
					path = Paths.get(localFilePath + selectedFiles[k]);
					Path target = Paths.get(HomeGUI.projectPath);
					if (localFilePath != null) {
						try {
							Files.copy(path,
									target.resolve(path.getFileName()),
									REPLACE_EXISTING);
						} catch (IOException e1) {							
							e1.printStackTrace();
						}
					}
				}
			}
		});
		btnNewButton.setBounds(349, 413, 75, 25);
		btnNewButton.setText("Browse");

		Button btnSave = new Button(shell, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for (int j = 0; j < selectedFiles.length; j++) {
					TreeItem treeItem = new TreeItem(
							HomeGUI.trtmNewTreeitem, SWT.NONE);
					treeItem.setText(selectedFiles[j]);
					HomeGUI.trtmNewTreeitem.setExpanded(true);
					//HomeGUI.treeViewer.refresh(HomeGUI.tree);
					HomeGUI.projComposite.layout();
				}
				shell.close();
				if(selectedFiles.length >= 2)
					HomeGUI.hasTwoFiles = true;
				if(selectedFiles.length == 3)
					HomeGUI.hasThreeFiles = true;
				openFiles();
			}
		});
		btnSave.setBounds(375, 472, 49, 25);
		btnSave.setText("Open");

	}

	public static void openFiles() {
		for (int j = 0; j < selectedFiles.length; j++) {
			createTabLayout(selectedFiles[j]);
		}
	}

	public static void createTabLayout(String fileName) {
		HomeGUI.tabFolder.setVisible(true);

		CTabItem tabItem = new CTabItem(HomeGUI.tabFolder, SWT.NONE);
		tabItem.setText(fileName);

		Composite composite = new Composite(HomeGUI.tabFolder, SWT.NONE);

		composite.setLayout(new GridLayout(1, false));
		composite.addControlListener(new ControlListener() {

			@Override
			public void controlResized(ControlEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void controlMoved(ControlEvent arg0) {

			}
		});

		GridData spec = new GridData();
		spec.horizontalAlignment = GridData.FILL;
		spec.grabExcessHorizontalSpace = true;
		spec.verticalAlignment = GridData.FILL;
		spec.grabExcessVerticalSpace = true;
		composite.setLayoutData(spec);

		codeText = new StyledText(composite, SWT.BORDER | SWT.MULTI
				| SWT.V_SCROLL | SWT.H_SCROLL);
		codeText.setLayoutData(spec);

		File file = new File(localFilePath + fileName);
		try {
			FileInputStream stream = new FileInputStream(file.getPath());
			try {
				Reader in = new BufferedReader(new InputStreamReader(stream));
				char[] readBuffer = new char[2048];
				StringBuffer buffer = new StringBuffer((int) file.length());
				int n;
				while ((n = in.read(readBuffer)) > 0) {
					buffer.append(readBuffer, 0, n);
				}
				textString = buffer.toString();

				stream.close();
			} catch (IOException e) {
				String message = "Err_file_io";
				displayError(message);
				return;
			}
		} catch (FileNotFoundException e) {
			String message = "Err_not_found";
			displayError(message);
			return;
		}

		final Display display = codeText.getDisplay();
		// display.asyncExec(new Runnable() {
		// public void run() {
		codeText.setText(textString);
		List<XmlRegion> regions = new XmlRegionAnalyzer()
				.analyzeXml(textString);
		List<StyleRange> styleRanges = XmlRegionAnalyzer
				.computeStyleRanges(regions);
		for (int l = 0; l < regions.size(); l++) {
			XmlRegion xr = regions.get(l);
			int regionLength = xr.getEnd() - xr.getStart();
			switch (xr.getXmlRegionType()) {
			case MARKUP: {
				for (int i = 0; i < regionLength; i++) {
					StyleRange[] range = new StyleRange[] { styleRanges.get(l) };
					range[0].start = xr.getStart();
					range[0].length = regionLength;
					codeText.replaceStyleRanges(xr.getStart(), regionLength,
							range);
				}
				break;
			}
			case ATTRIBUTE: {
				for (int i = 0; i < regionLength; i++) {
					StyleRange[] range = new StyleRange[] { styleRanges.get(l) };
					range[0].start = xr.getStart();
					range[0].length = regionLength;
					codeText.replaceStyleRanges(xr.getStart(), regionLength,
							range);
				}
				break;
			}
			case ATTRIBUTE_VALUE: {
				for (int i = 0; i < regionLength; i++) {
					StyleRange[] range = new StyleRange[] { styleRanges.get(l) };
					range[0].start = xr.getStart();
					range[0].length = regionLength;
					codeText.replaceStyleRanges(xr.getStart(), regionLength,
							range);
				}
				break;
			}
			case MARKUP_VALUE: {
				for (int i = 0; i < regionLength; i++) {
					StyleRange[] range = new StyleRange[] { styleRanges.get(l) };
					range[0].start = xr.getStart();
					range[0].length = regionLength;
					codeText.replaceStyleRanges(xr.getStart(), regionLength,
							range);
				}
				break;
			}
			case COMMENT:
				break;
			case INSTRUCTION:
				break;
			case CDATA:
				break;
			case WHITESPACE:
				break;
			default:
				break;
			}

		}

		// }
		// });
		composite.setData(codeText);
		tabItem.setControl(composite);

	}

	public void center(Shell shell) {

		Rectangle bds = shell.getDisplay().getBounds();

		Point p = shell.getSize();
		shell.setFullScreen(true);
		int nLeft = (bds.width - p.x) / 2;
		int nTop = (bds.height - p.y) / 2;

		shell.setBounds(nLeft, nTop, p.x, p.y);
	}

	static void displayError(String msg) {
		MessageBox box = new MessageBox(shell, SWT.ICON_ERROR);
		box.setMessage(msg);
		box.open();
	}
}
