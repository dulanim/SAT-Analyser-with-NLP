package com.project.traceability.GUI.New;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotCheckBox;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import com.project.traceability.GUI.ProjectCreateWindow;

public class AbstractTestWindow1 extends SWTBotTestCase{


 	protected SWTBot bot;
 
    protected static Thread uiThread;
 
    protected static Shell shell;
 
    private final static CyclicBarrier swtBarrier = new CyclicBarrier(2);
 
    @BeforeClass
    public static void setupApplication() {
        if (uiThread == null) {
            uiThread = new Thread(new Runnable() {
 
                @Override
                public void run() {
                    try {    
                        while (true) {
                            // open and layout the shell
                            ProjectCreateWindow window = new ProjectCreateWindow();
                            window.open();
                            shell = window.getShell();
 
                            // wait for the test setup
                            swtBarrier.await();
 
                            // run the event loop
                            window.eventLoop(Display.getDefault());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            uiThread.setDaemon(true);
            uiThread.start();
        }
    }
 
    @Before
    public final void setupSWTBot() throws InterruptedException, BrokenBarrierException {
        // synchronize with the thread opening the shell
        swtBarrier.await();
        bot = new SWTBot(shell);
    }
    
    @After
    public void closeShell() throws InterruptedException {
        // close the shell
        Display.getDefault().syncExec(new Runnable() {
            public void run() {
                shell.close();
            }
        });
    }
 
    
    protected void assertResultGivenInput(String name) {
    	 SWTBotText userText = bot.textWithLabel("Traceabilty Project Name");
    	 userText.setFocus();
         userText.setText(name);

         assert(userText.getText().equals(name));
         SWTBotButton btnOk = bot.button("Ok");
         if(btnOk.isEnabled()){
        	 btnOk.click();
//        	 String path = com.project.traceability.staticdata.StaticData.workspace;
//        	 File wrkspace = new File(path,name);
//        	 boolean isCreated = wrkspace.exists();
//        	 assertEquals(true, isCreated);
         }
    }
	 public void assertForNewWorkspaceCreation(String path){
		 SWTBotCheckBox chckBox = bot.checkBox();
		 chckBox.click();
		 
		 SWTBotText textWorkspace = bot.textWithLabel("New Workspace Path");
		 textWorkspace.setFocus();
		 textWorkspace.setText(path);
		 
		 assertEquals(path, textWorkspace.getText());
	 }
    
    
   
}
