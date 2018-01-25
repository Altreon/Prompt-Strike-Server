package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPClient implements Runnable{
	private Socket socket;
	private DataInputStream streamIn;
	private DataOutputStream streamOut;
	
	private Network network;

	public TCPClient(Socket socket, Network network) {
		this.socket = socket;
		this.network = network;
	}

	@Override
	public void run() {
		boolean connect = true;
		
		try {  
		    start();
		} catch(IOException ioe) {  
			System.out.println("Unexpected exception: " + ioe.getMessage());
		}
		while (connect) {  
			try {  
				String command = streamIn.readUTF();
				main.Server.processCommand(network.getNumClient(this), command);
		        
		    } catch(IOException ioe) {
		    	System.out.println("Connexion error");
		    	stop();
		    	connect = false;
		    }
		}
	}
	
	public void start() throws IOException{  
		streamIn = new DataInputStream(socket.getInputStream());
	    streamOut = new DataOutputStream(socket.getOutputStream());
	}
	
    public void stop() {
    	try {
    		streamIn.close();
	    	streamOut.close();
	        socket.close();
    	}
    	catch(IOException ioe) {
    		System.out.println("Error closing ...");
    	}
    	network.removeClient(this);
   }
    
    public void sendCommand (int numPlayer, String command, boolean correct) {
    	try {
    		while(streamOut == null) {};
    		streamOut.writeInt(numPlayer);
			streamOut.writeUTF(command);
			streamOut.writeBoolean(correct);
			streamOut.flush();
		} catch (IOException ioe) {
			System.out.println("Error sending command:" + ioe.getMessage());
		}
    }
}
