/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.project.progress.progressbar;

/**
 *
 * @author shiyam
 */
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.JLabel;

import com.project.traceability.staticdata.StaticData;

public class ProgressBarCustom{

     private static JPanel mainPanel = new JPanel();
	 private static void createAndShowUI() {
//	        JFrame frame = new JFrame("TestProgressBar");
//	        frame.getContentPane().add(new TestPBGui().getMainPanel());
//	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//	        frame.pack();
//	        frame.setLocationRelativeTo(null);
//	        frame.setVisible(true);
		 myAttemptActionPerformed();
	    }

	    public static void main(String[] args) {
	        java.awt.EventQueue.invokeLater(new Runnable() {

	            @Override
	            public void run() {
	                createAndShowUI();
	            }
	        });
	    }
	    
	    public static void myAttemptActionPerformed() {
	        Window thisWin = SwingUtilities.getWindowAncestor(mainPanel);
	        final JDialog progressDialog = new JDialog(thisWin, StaticData.statusString);
	        JPanel contentPane = new JPanel();
	        contentPane.setPreferredSize(new Dimension(300, 100));
	        
	        JPanel panel = new JPanel();
	        contentPane.add(panel);
	        final JProgressBar bar = new JProgressBar(0, 100);
	        bar.setIndeterminate(true);
	        contentPane.add(bar);
	        
	        JLabel lblAdd = new JLabel("Add");
	        contentPane.add(lblAdd);
	        progressDialog.setContentPane(contentPane);
	        progressDialog.pack();
	        progressDialog.setLocationRelativeTo(null);
	        final Task task = new Task("My attempt");
	        task.addPropertyChangeListener(new PropertyChangeListener() {
				
				@Override
				public void propertyChange(java.beans.PropertyChangeEvent evt) {
					// TODO Auto-generated method stub
					if (evt.getPropertyName().equalsIgnoreCase("progress")) {
	                    int progress = task.getProgress();
	                    if (progress == 0) {
	                        bar.setIndeterminate(true);
	                    } else {
	                        bar.setIndeterminate(false);
	                        bar.setValue(progress);
	                        progressDialog.dispose();
	                    }
	                }
				}
			});
	        task.execute();
	        progressDialog.setVisible(true);
	    }

	    public JPanel getMainPanel() {
	        return mainPanel;
	    }
	}

	class Task extends SwingWorker<Void, Void> {

	    private static final long SLEEP_TIME = 4000;
	    private String text;

	    public Task(String text) {
	        this.text = text;
	    }

	    @Override
	    public Void doInBackground() {
	        setProgress(0);
	        try {
	            Thread.sleep(SLEEP_TIME);// imitate a long-running task
	        } catch (InterruptedException e) {
	        }
	        setProgress(100);
	        return null;
	    }

	    @Override
	    public void done() {
	        System.out.println(text + " is done");
	        Toolkit.getDefaultToolkit().beep();
	    }
	}


