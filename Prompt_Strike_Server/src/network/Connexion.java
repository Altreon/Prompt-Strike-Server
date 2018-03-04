package network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/** * This is the reception of connections for the server */
public class Connexion implements Runnable {
	
	/** 
	 * The  Server socket
	 * 
	 * @see ServerSocket
	 */
	private ServerSocket server;
	
	/** * The network manager */
	private Network network;
	
	/** * If the server has done to accept player */
	private boolean done;
	
	public Connexion (Network network) {
		this.network = network;
	}

	/** * Start to accept player's connections*/
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
	        		Socket socket = server.accept(); //wait a connexion
	    	        System.out.println("Player found: " + socket);
	    	        network.addClient(socket);
	        	}
	        	catch(IOException ioe){
	        		close();
	        	}
	        }
	        close();
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
		
	}
	
	/** * Stop to accept player's connections*/
	public void close() throws IOException {
		done = true;
		if (server != null) {
			server.close();
		}
	}
	
	/** * @return True if the server's connection is close*/
	public boolean isClose() {
		return done;
	}
	
}
