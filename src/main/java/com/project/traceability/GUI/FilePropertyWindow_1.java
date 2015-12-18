import java.io.File;
import java.text.SimpleDateFormat;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;



public class FilePropertyWindow_1 extends ApplicationWindow {
	Label lblSize;
	Label lblFilePath;
	Label lblType;
	Label lblExtension;
	Combo combo;
	private Text text;
	private final String currentClickedPath = "";
	private final Shell parnt = null;
	

	/**
	 * Create the application window.
	 */
	public FilePropertyWindow_1() {
		super(null);
		createActions();
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		addStatusLine();
	}

	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(null);
		
		Group grpFilefolder = new Group(container, SWT.NONE);
		grpFilefolder.setText("File/Folder");
		grpFilefolder.setBounds(10, 0, 307, 202);
		
		Label lblNae = new Label(grpFilefolder, SWT.NONE);
		lblNae.setBounds(10, 28, 70, 17);
		lblNae.setText("Name");
		
		text = new Text(grpFilefolder, SWT.BORDER);
		final String oldName = text.getText().toString();
		text.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				
				int code = e.keyCode;
				if(code == 10){
					String newName = text.getText().toString();
					if(!oldName.equals(newName)){
						Path path = new Path(currentClickedPath + File.separator +
								newName);
						File fileNew = path.toFile();
						path = new Path(currentClickedPath + File.separator +
								oldName);
						File fileOld = path.toFile();
						if(fileOld.renameTo(fileNew)){
							
						}else{
							MessageBox message = new MessageBox(parnt);
							message.setMessage("The folder can not ");
						}
						
					}
				}
			}
		});
		text.setBounds(102, 28, 145, 27);
		
		Label lblPath = new Label(grpFilefolder, SWT.NONE);
		lblPath.setBounds(10, 62, 70, 17);
		lblPath.setText("Path");
		
		lblFilePath = new Label(grpFilefolder, SWT.NONE);
		lblFilePath.setBounds(102, 62, 220, 17);
		lblFilePath.setText("New Label");
		
		Label lblTypeLeft = new Label(grpFilefolder, SWT.NONE);
		lblTypeLeft.setBounds(10, 97, 70, 17);
		lblTypeLeft.setText("Type");
		
		lblType = new Label(grpFilefolder, SWT.NONE);
		lblType.setBounds(102, 97, 70, 17);
		lblType.setText("type");
		
		Label lblExtensionjjj = new Label(grpFilefolder, SWT.NONE);
		lblExtensionjjj.setBounds(10, 133, 70, 17);
		lblExtensionjjj.setText("Extension");
		
		lblExtension = new Label(grpFilefolder, SWT.NONE);
		lblExtension.setBounds(102, 133, 70, 17);
		lblExtension.setText("extension");
		
		Label label = new Label(grpFilefolder, SWT.NONE);
		label.setText("Size");
		label.setBounds(10, 166, 70, 17);
		
		lblSize = new Label(grpFilefolder, SWT.NONE);
		lblSize.setText("10KB");
		lblSize.setBounds(102, 166, 70, 17);
		
		Group grpDate = new Group(container, SWT.NONE);
		grpDate.setBounds(10, 208, 307, 114);
		grpDate.setText("Date");
		
		Label lblAccessedDate = new Label(grpDate, SWT.NONE);
		lblAccessedDate.setBounds(0, 10, 135, 17);
		lblAccessedDate.setText("Accessed Date");
		
		Label lblModifiedDate = new Label(grpDate, SWT.NONE);
		lblModifiedDate.setBounds(141, 43, 174, 17);
		lblModifiedDate.setText("2015 Nov 06 Wed 9:02 Am");
		
		Label lblDec = new Label(grpDate, SWT.NONE);
		lblDec.setBounds(141, 10, 163, 17);
		lblDec.setText("2015 Dec 08 Tue 9:06 Am");
		
		Label lblModificationDate = new Label(grpDate, SWT.NONE);
		lblModificationDate.setBounds(0, 43, 135, 17);
		lblModificationDate.setText("Modification Date");
		
		Group grpPermision = new Group(container, SWT.NONE);
		grpPermision.setText("Permision");
		grpPermision.setBounds(10, 328, 307, 78);
		
		Label lblAccess = new Label(grpPermision, SWT.NONE);
		lblAccess.setBounds(0, 10, 70, 17);
		lblAccess.setText("Access");
		
		combo = new Combo(grpPermision, SWT.NONE);
		combo.setBounds(142, 10, 115, 29);
		
		Group grpAbout = new Group(container, SWT.NONE);
		grpAbout.setText("About");
		grpAbout.setBounds(10, 413, 307, 109);

		initializeData("/home/shiyam/LunaEclipse/eclipse/about.html");
		return container;
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Create the menu manager.
	 * @return the menu manager
	 */
	@Override
	protected MenuManager createMenuManager() {
		MenuManager menuManager = new MenuManager("menu");
		return menuManager;
	}

	
	/**
	 * Create the status line manager.
	 * @return the status line manager
	 */
	@Override
	protected StatusLineManager createStatusLineManager() {
		StatusLineManager statusLineManager = new StatusLineManager();
		return statusLineManager;
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			FilePropertyWindow_1 window = new FilePropertyWindow_1();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void initializeData(String rootPath){
		/*
		 *@Param rootPath it mentions about the file path 
		 *which clicked by user
		*/
		//init the last modification filed
		
		File file = new File(rootPath);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("Modification Data : " + sdf.format(file.lastModified()));
		
		//init the size
		double bytes = file.length();
		double kiloByte = bytes/1024;
		lblSize.setText(Double.toString(kiloByte) + " KB");
		
		//file absolute path
		String absoultePath = file.getAbsolutePath();
		if(!file.isDirectory()){
			String filePath = absoultePath.
   	    	     substring(0,absoultePath.lastIndexOf(File.separator)); //without file name in absolutePath
			String fileName = file.getAbsoluteFile().getName();
			String extension = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
			lblFilePath.setText(filePath);
			lblType.setText("File");
			text.setText(fileName);
			lblExtension.setText(extension);
			 
		}else{
			lblFilePath.setText(absoultePath);
			lblType.setText("Folder/Directory");
			String folderName = absoultePath.substring(absoultePath.lastIndexOf(File.separator) + 1,
					absoultePath.length());
			text.setText(folderName);
			lblExtension.setText("     ");
		}
		String permission[] ={ "Read and Write","Read Only"};
		combo.setItems(permission);
		
	}

	/**
	 * Configure the shell.
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("New Application");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(320, 532);
	}
}
