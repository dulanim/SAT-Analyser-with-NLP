package com.project.extendedsat.jenkins;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.project.traceability.GUI.HomeGUI;
import com.project.traceability.GUI.ProjectCreateWindow;

import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

public class Notification extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	static Notification frame = new Notification();
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
				
					frame.setVisible(true);
					frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Notification() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 436, 207);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JLabel lblJenkinsHasCompleted = new JLabel("Jenkins has completed a new build succesfully. ");
		lblJenkinsHasCompleted.setFont(new Font("Tahoma", Font.PLAIN, 15));
		contentPane.add(lblJenkinsHasCompleted, BorderLayout.CENTER);
		
		JButton btnLaunchSat = new JButton("Launch SAT");
		btnLaunchSat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//dispose();				
				//HomeGUI.startHomeGUI();
				//ProjectCreateWindow.main(null);
				ProjectCreateWindow pw = new ProjectCreateWindow();
				pw.gopro();
			}
		});
		btnLaunchSat.setFont(new Font("Tahoma", Font.PLAIN, 15));
		contentPane.add(btnLaunchSat, BorderLayout.SOUTH);
	}

}
