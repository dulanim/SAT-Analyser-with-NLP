package com.project.extendedsat.jenkins;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class SatLauncher extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Thread t = new Thread( new Listner() );
			    	t.start();
					SatLauncher frame = new SatLauncher();
					 frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
					 JPanel panel = (JPanel)frame.getContentPane();
					 
				     panel.setLayout(null);
					frame.setSize(1050, 500);
					frame.setTitle("SAT Analyzer Jenkins Interface Manager");
					JLabel label = new JLabel();  
			        label.setIcon(new ImageIcon("satjenkin.png"));
			        panel.add(label);
			        Dimension size = label.getPreferredSize();
			        label.setBounds(0, 50, size.width, size.height);
			       frame.getContentPane().setBackground(Color.WHITE);
					frame.setVisible(true);
					
//			    	Thread t = new Thread( new Listner() );
//			    	t.start();
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public SatLauncher() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}

}
