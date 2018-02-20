package network;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import main.Command;

public class Server implements Runnable {
	
	private ServerSocket server;
	private Network network;
	private boolean done;
	
	public Server (Network network) {
		this.network = network;
	}

	@Override
	public void run() {
		try {
			System.out.println("server initializing...");
	        server = new ServerSocket(10);
	        System.out.println("Server started: " + server);
	        System.out.println("Waiting for players..."); 
	        done = false;
	        while (!done) {  
	        	try {
	        		Socket socket = server.accept();
	    	        System.out.println("Player found: " + socket);
	    	        network.addClient(socket);
	        	}
	        	catch(IOException ioe){
	        		close();
	        	}
	        }
	        close();
		}catch(IOException ioe) {
			System.out.println(ioe); 
		}
		
	}
	
	public void close() throws IOException {
		done = true;
		if (server != null) {
			server.close();
		}
	}
	
	public boolean isClose() {
		return done;
	}
	
}
