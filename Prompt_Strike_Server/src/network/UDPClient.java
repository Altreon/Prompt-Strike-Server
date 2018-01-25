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
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import message.PosMessage;

public class UDPClient implements Runnable{
	
	public final static int port = 2345;
	
	private DatagramSocket socket;
	
	private Network network;

	public UDPClient(Network network) {
		this.network = network;
		
		start();
	}

	@Override
	public void run() {
		boolean connect = true;
		
		while (connect) {  
			try { 
				
				byte[] buffer = new byte[8192];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                
                socket.receive(packet);
				
				String command = new String(packet.getData());
				System.out.println(command);
		        
		    } catch(IOException ioe) {
		    	System.out.println("Connexion error");
		    	stop();
		    	connect = false;
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
	
    public void stop() {
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

	public void sendMessage(PosMessage message) {
		try {
			ByteArrayOutputStream bStream = new ByteArrayOutputStream();
			ObjectOutput oo = new ObjectOutputStream(bStream);
			oo.writeObject(message);
			oo.close();
			
			byte[] buffer = bStream.toByteArray();
	    	InetAddress adresse = InetAddress.getByName("127.0.0.1");
	        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, adresse, port);
	        
	        packet.setData(buffer);
	        
	        socket.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
}
