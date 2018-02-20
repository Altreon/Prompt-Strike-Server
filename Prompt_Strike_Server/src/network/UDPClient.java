package network;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import message.Message;
import message.PosMessage;

public class UDPClient implements Runnable{
	
	private DatagramSocket socket;
	
	private Network network;

	private boolean connect;
	
	public UDPClient(Network network) {
		this.network = network;
		
		start();
	}

	@Override
	public void run() {
		connect = true;
		
		while (connect) {  
			try { 
				
				byte[] buffer = new byte[256];
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
	
	public void start() {
		try {
			socket = new DatagramSocket();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    public void close() {
    	connect = false;
    	socket.close();
   }
    
    /*public void sendCommand (int numPlayer, String command, boolean correct) {
    	try {
	    	String envoi = "servr message";
	        byte[] buffer = envoi.getBytes();
	    	InetAddress adresse = InetAddress.getByName("127.0.0.1");
	        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, adresse, port);
	        
	        packet.setData(buffer);
	        
	        socket.send(packet);
	        
    	} catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


     }*/

	public void sendMessage(Message message, InetAddress adress, int port) {	
		try {
			ByteArrayOutputStream bStream = new ByteArrayOutputStream();
			ObjectOutput oo = new ObjectOutputStream(bStream);
			oo.writeObject(message);
			oo.close();
			
			byte[] buffer = bStream.toByteArray();
	        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, adress, port);
	        
	        packet.setData(buffer);
	        
	        socket.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
}
