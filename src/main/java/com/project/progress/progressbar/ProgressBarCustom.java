/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.progress.progressbar;

/**
 *
 * @author shiyam
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ProgressBarCustom{
  private static Shell        shell;
  private static ProgressBar  progressBar;
  private Label lblLoadiingPleaseWait;

  public ProgressBarCustom() {
	// TODO Auto-generated constructor stub
	  create();
  }
  
  public static void main(String args[]){
	  ProgressBarCustom bar = new ProgressBarCustom();
	  
  }
	public  void create()
	{
	    Display display = new Display();
	    shell = new Shell(SWT.SHELL_TRIM);
	    shell.setText("Changing UML File to XML");
	    shell.setLayout(new GridLayout(1, false));
	
	    setUpStatusBar();
	
	
	    shell.pack();
	    shell.setSize(400, 141);
	    new Label(shell, SWT.NONE);
	    
	    lblLoadiingPleaseWait = new Label(shell, SWT.NONE);
	    lblLoadiingPleaseWait.setText("Loadiing Please wait");
	    shell.open();
	
	    while (!shell.isDisposed()){
	        if (!display.readAndDispatch()){
	            display.sleep();
	        }
	    }
	}

	public static void updateProgressBar()
	{
	    new Thread(new Runnable()
	    {
	        private int                 progress    = 0;
	        private static final int    INCREMENT   = 10;
	
	        @Override
	        public void run()
	        {
	            while (!progressBar.isDisposed())
	            {
	                Display.getDefault().asyncExec(new Runnable()
	                {
	                    @Override
	                    public void run()
	                    {
	                    	while(true){
	                        if (!progressBar.isDisposed())
	                        	progressBar.setSelection((progress += INCREMENT) % (progressBar.getMaximum() + INCREMENT));
	                    	}
	                    }
	                });
	
	                try
	                {
	                    Thread.sleep(1000);
	                }
	                catch (InterruptedException e)
	                {
	                    e.printStackTrace();
	                }
	            }
	        }
	    }).start();
	}

public static void setUpStatusBar()
{
    Composite statusBar = new Composite(shell, SWT.BORDER);
    statusBar.setLayout(new GridLayout(1, false));
    statusBar.setLayoutData(new GridData(SWT.FILL, SWT.END, true, false));

    progressBar = new ProgressBar(statusBar, SWT.SMOOTH);
    progressBar.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, false));
    progressBar.setMaximum(100);
}


}
