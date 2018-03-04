package network;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import message.Message;

public class UDPClient implements Runnable{
	
	/** 
	 * The UDP's socket
	 * 
	 * @see DatagramSocket
	 */
	private DatagramSocket socket;
	
	/** * If the UDP sender continue to work */
	private boolean connect;
	
	public UDPClient() {		
		start();
	}

	/** * Starts the UDP sender */
	@Override
	public void run() {
		connect = true;
		
		while (connect) {  
			try { 
				
				byte[] buffer = new byte[256]; // 256 more than enough (conventional) to store any message for this application
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                
                socket.receive(packet);
				
				String command = new String(packet.getData());
				System.out.println(command);
		        
		    } catch(IOException ioe) {
		    	System.out.println("Connexion error");
		    	close();
		    }
		}
	}
	
	/** * Opens the UDP's socket  */
	public void start() {
		try {
			socket = new DatagramSocket();
		} catch (SocketException se) {
			System.out.println("Error creating UDP-Sender");
			se.printStackTrace();
		}
	}
	
	/** * Closes the UDP's socket  */
    public void close() {
    	connect = false;
    	socket.close();
   }

    /**
     * Sends a UDP message to a client
     * 
     * @param message
     * 				The message to send
     * @param address
     * 				The IP address to send the message
     * @param port
     * 				The port to send the message
     * @see Message
     */ 
	public void sendMessage(Message message, InetAddress address, int port) {	
		try {
			ByteArrayOutputStream bStream = new ByteArrayOutputStream();
			ObjectOutput oo = new ObjectOutputStream(bStream);
			oo.writeObject(message);
			oo.close();
			
			byte[] buffer = bStream.toByteArray();
	        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, port);
	        
	        packet.setData(buffer);
	        
	        //here, a waiting for initialization is useless because it happen on server initialization, before add any client
	        
	        socket.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
}
