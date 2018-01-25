package network;

import java.net.Socket;
import java.util.ArrayList;

import message.PosMessage;

public class Network {
	
	private Server server;
	private Thread serverThread;
	
	private ArrayList<TCPClient> TCPclients;
	private ArrayList<Thread> TCPclientThreads;
	private UDPClient UDPSender;
	
	public Network () {
		TCPclients = new ArrayList<TCPClient>();
		TCPclientThreads = new ArrayList<Thread>();
	}
	
	public void createServer () {
		server = new Server(this);
		serverThread = new Thread(server);
		serverThread.start();
		
		UDPSender = new UDPClient(this);
	}
	
	public void addClient (Socket socket) {
		TCPClient client = new TCPClient(socket, this);
		Thread clientThread = new Thread(client);
		clientThread.start();
		
		TCPclients.add(client);
		TCPclientThreads.add(clientThread);
		
		main.Server.newPlayer();
	}
	
	public void removeClient(TCPClient client) {
		int nClient = getNumClient(client);
		TCPclients.remove(nClient);
		TCPclientThreads.remove(nClient);
	}

	public void sendCorrectCommand(int nClient, String command) {
		for(TCPClient client : TCPclients) {
			client.sendCommand(nClient, command, true);
		}
		
	}
	
	public void sendIncorrectCommand(int nClient, String command) {
			TCPclients.get(nClient).sendCommand(nClient, command, false);
	}

	public int getNumClient(TCPClient client) {
		return TCPclients.indexOf(client);
	}

	public void sendPos(int numPlayer, String unitName, float posX, float posY) {
		PosMessage message = new PosMessage(numPlayer, unitName, posX, posY);
		UDPSender.sendMessage(message);
	}
}
