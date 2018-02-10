package network;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import main.Player;
import message.*;

public class Network {
	
	private Server server;
	private Thread serverThread;
	
	private ArrayList<TCPClient> TCPClients;
	private ArrayList<Thread> TCPClientThreads;
	
	private ArrayList<InetAddress> addresses;
	private ArrayList<Integer> ports;
	private UDPClient UDPSender;
	
	public Network () {
		TCPClients = new ArrayList<TCPClient>();
		TCPClientThreads = new ArrayList<Thread>();
		
		addresses = new ArrayList<InetAddress>();
		ports = new ArrayList<Integer>();
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
		
		TCPClients.add(client);
		TCPClientThreads.add(clientThread);
		
		addresses.add(socket.getInetAddress());
		ports.add(socket.getPort());
		
		main.Server.addCommandOnWaitList(0, "newPlayer");
	}

	public void removeClient(TCPClient client) {
		int nClient = getNumClient(client);
		TCPClients.remove(nClient);
		TCPClientThreads.remove(nClient);
		addresses.remove(nClient);
		ports.remove(nClient);
		main.Server.removePlayer(nClient);
	}
	
	public void sendCommand(int nClient, String command, boolean correct) {
		CommandMessage message = new CommandMessage(command, correct);
		TCPClients.get(nClient).sendMessage(message);
	}

	public int getNumClient(TCPClient client) {
		return TCPClients.indexOf(client);
	}
	
	public void sendNewPlayer(ArrayList<Player> players) {
		NewPlayerMessage message = new NewPlayerMessage("jack");
		for(int i = 0; i < players.size(); i++) {
			if(i == players.size()-1) {
				for(int k = 0; k < players.size()-1; k++) {
					NewPlayerMessage message2 = new NewPlayerMessage("jack");
					TCPClients.get(i).sendMessage(message2);
				}
			}
			TCPClients.get(i).sendMessage(message);
		}
		
	}
	
	public void sendNewEntity(int numPlayer, String typeEntity, String nameEntity, float posX, float posY, float rotation) {
		for(TCPClient client : TCPClients) {
			CreateEntityMessage message = new CreateEntityMessage(numPlayer, typeEntity, nameEntity, posX, posY, rotation);
			client.sendMessage(message);
		}
	}
	
	public void sendDestroyEntity(int numPlayer, String typeEntity, String nameEntity) {
		for(TCPClient client : TCPClients) {
			DestroyEntityMessage message = new DestroyEntityMessage(numPlayer, typeEntity, nameEntity);
			client.sendMessage(message);
		}
	}
	
	public void sendFire(int numPlayer, String nameEntity, float ImpactPosX, float ImpactPosY) {
		for(TCPClient client : TCPClients) {
			FireMessage message = new FireMessage(numPlayer, nameEntity, ImpactPosX, ImpactPosY);
			client.sendMessage(message);
		}
	}
	
	public void sendUpdateMoney(int numPlayer, int money) {
		UpdateMoneyMessage message = new UpdateMoneyMessage(numPlayer, money);
		TCPClients.get(numPlayer).sendMessage(message);
	}
	
	private void sendUPDMessage(Message message) {
		for(int i = 0; i < TCPClients.size(); i++) {
			UDPSender.sendMessage(message, addresses.get(i), ports.get(i));
		}
	}

	public void sendPos(int numPlayer, String unitName, float posX, float posY) {
		PosMessage message = new PosMessage(numPlayer, unitName, posX, posY);
		sendUPDMessage(message);
	}

	public void sendRot(int numPlayer, String unitName, float rotation, int idPart) {
		RotMessage message = new RotMessage(numPlayer, unitName, rotation, idPart);
		sendUPDMessage(message);
	}
}
