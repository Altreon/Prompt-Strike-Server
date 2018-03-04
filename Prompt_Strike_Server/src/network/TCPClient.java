package network;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Server.Server;
import message.Message;

/** * This is the TCP connection to a client */
public class TCPClient implements Runnable{
	
	/** 
	 * The client's socket
	 * 
	 * @see Socket
	 */
	private Socket socket;
	
	/** 
	 * The data input of socket
	 * 
	 * @see DataInputStream
	 */
	private DataInputStream streamIn;
	/** 
	 * The data output of socket
	 * 
	 * @see ObjectOutputStream
	 */
	private ObjectOutputStream streamOut;
	
	/** * The network manager */
	private Network network;
	
	/** * If the client's connection continue to receive data */
	private boolean connect;

	/**
     * Create a TCPClient
     * 
     * @param socket
     * 				The client's socket
     * @param network
     * 				The network manager
     */
	public TCPClient(Socket socket, Network network) {
		this.socket = socket;
		this.network = network;
	}

	/** * Starts the client's connections */
	@Override
	public void run() {
		connect = true;
		
		try {  
		    start();
		} catch(IOException ioe) {
			System.out.println("Error creating TCP-Client");
			ioe.printStackTrace();
		}
		while (connect) {  
			try {  
				String command = streamIn.readUTF();
				Server.addCommandOnWaitList(network.getNumClient(this), command);
		        
		    } catch(IOException ioe) {
		    	System.out.println("Connexion error");
		    	stop();
		    }
		}
		
		close();
	}
	
	/** * Gets the input and output communication of the client's socket */
	public void start() throws IOException{  
		streamIn = new DataInputStream(socket.getInputStream());
	    streamOut = new ObjectOutputStream(socket.getOutputStream());
	}
	
	/** * Stops the communication between the client and the server */
	public void stop () {
		connect = false;
		try {
    		streamIn.close();
	    	streamOut.close();
	        socket.close();
    	}
    	catch(IOException ioe) {
    		System.out.println("Error closing ...");
    		ioe.printStackTrace();
    	}
	}
	
	/** * Delete this client for the server */
    public void close() {
    	network.removeClient(this);
   }

    /**
     * Sends a TCP message to the client
     * 
     * @param message
     * 				The message to send
     * @see Message
     */ 
	public void sendMessage(Message message) {
		try {
	        while(streamOut == null) { //Used to wait the initialization of TCP thread before sending any message
	        	try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	        }
    		streamOut.writeObject(message);
			streamOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		
	}
}
