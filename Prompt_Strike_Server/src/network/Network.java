package network;

import java.util.ArrayList;

public class Network {
	
	private Server server;
	private Thread serverThread;
	
	private ArrayList<Client> clients;
	private ArrayList<Thread> clientThreads;
	
	public Network () {
		clients = new ArrayList<Client>();
		clientThreads = new ArrayList<Thread>();
	}
	
	public void createServer () {
		server = new Server();
		serverThread = new Thread(server);
		serverThread.start();
	}
	
	public void addClient () {
		Client client = new Client();
		Thread clientThread = new Thread(client);
		clientThread.start();
		
		clients.add(client);
		clientThreads.add(clientThread);
	}

	public void sendCommand(int nClient, String command) {
		if(server != null) {
			//server
		}else {
			clients.get(nClient).sendCommand(command);
		}
		
	}

	public boolean isConnected() {
		return true;
	}
}
