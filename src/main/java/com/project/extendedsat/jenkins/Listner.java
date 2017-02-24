package com.project.extendedsat.jenkins;


import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Listner implements Runnable{
	 
	
	
	private ServerSocket serverSocket;
	private Socket socket;
	private boolean status;
	
	
	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket( 8888 );			
			DataInputStream dis;
			
			
			while( (socket = serverSocket.accept()) != null ){
				
				dis = new DataInputStream( socket.getInputStream() );
				status = dis.readBoolean();
				
				if( status ){
					//launch notification
//					Notification notice = new Notification();
//					notice.setVisible(true);				
					//notice.show();
					
					try {
						Notify window = new Notify();
						window.open();
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					System.out.println("SATAnalyser Launched");
				}
				
				socket.close();
			}
			
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	
	
}
