package network;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import message.CreateEntityMessage;
import message.Message;

public class TCPClient implements Runnable{
	private Socket socket;
	private DataInputStream streamIn;
	private ObjectOutputStream streamOut;
	
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
				main.Server.addCommandOnWaitList(network.getNumClient(this), command);
		        
		    } catch(IOException ioe) {
		    	System.out.println("Connexion error");
		    	stop();
		    	connect = false;
		    }
		}
	}
	
	public void start() throws IOException{  
		streamIn = new DataInputStream(socket.getInputStream());
	    streamOut = new ObjectOutputStream(socket.getOutputStream());
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

	public void sendMessage(Message message) {
		try {
	        while(streamOut == null) {};
    		streamOut.writeObject(message);
			streamOut.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
}
