package network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import Server.Server;
import message.*;
import player.Player;

/** * This is the network manager of the server, it is used to send and receive informations throw the network */
public class Network {
	
	/** * The reception of connections for the server */
	private Connexion connexion;
	/** * The connexon's Thread */
	private Thread serverThread;
	
	/** * The list of Clients TCP connection in the server */
	private ArrayList<TCPClient> TCPClients;
	/** * The list of Clients connect in the server */
	private ArrayList<Thread> TCPClientThreads;
	
	/** * The list of clients's IP addresses */
	private ArrayList<InetAddress> addresses;
	/** * The list of clients's ports */
	private ArrayList<Integer> ports;
	
	/** * The UPD message sender */
	private UDPClient UDPSender;
	
	/** * Create a Network */
	public Network () {
		TCPClients = new ArrayList<TCPClient>();
		TCPClientThreads = new ArrayList<Thread>();
		
		addresses = new ArrayList<InetAddress>();
		ports = new ArrayList<Integer>();
	}
	
	/** * Initializes network class and open the connection for clients */
	public void createServer () {
		connexion = new Connexion(this);
		serverThread = new Thread(connexion);
		serverThread.start();
		
		UDPSender = new UDPClient();
	}
	
	/** * Shutdowns network class*/
	public void closeServer() {
		if(!connexion.isClose()) {
			try {
				connexion.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		UDPSender.close();
		while(!TCPClients.isEmpty()) {
			TCPClients.get(0).stop();
		}
	}
	
	/** 
	 * Adds a new client on the server
	 * 
	 * @param socket
     * 				the socket of the client
     * @see Socket
	 * @see TCPClient
	 * */
	public void addClient (Socket socket) {
		TCPClient client = new TCPClient(socket, this);
		Thread clientThread = new Thread(client);
		clientThread.start();
		
		TCPClients.add(client);
		TCPClientThreads.add(clientThread);
		
		addresses.add(socket.getInetAddress());
		ports.add(socket.getPort());
		
		Server.addCommandOnWaitList(-1, "newPlayer");
	}

	/** 
	 * Removes a new client on the server
	 * 
	 * @param client
     * 				the client to remove
	 * @see TCPClient
	 * */
	protected void removeClient(TCPClient client) {
		int nClient = getNumClient(client);
		
		TCPClients.remove(nClient);
		TCPClientThreads.remove(nClient);
		
		addresses.remove(nClient);
		ports.remove(nClient);
		Server.removePlayer(nClient);
	}
	
	/** * Stop to receive new players*/
	public void stopAcceptPlayer() {
		try {
			connexion.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected int getNumClient(TCPClient client) {
		return TCPClients.indexOf(client);
	}
	
	
	//functions used to send message throw the network
	
	
	public void sendCommand(int nClient, String command, boolean correct) {
		CommandMessage message = new CommandMessage(command, correct);
		TCPClients.get(nClient).sendMessage(message);
	}
	
	public void sendNewPlayer(ArrayList<Player> players) {
		//Currently, the name of player is unused, so all our players are calling "jack", because why not?
		String playerName = "jack";
		
		NewPlayerMessage message = new NewPlayerMessage(playerName);
		
		for(int i = 0; i < players.size(); i++) {
			
			//used to send all player on server for the player in connection (the last)
			if(i == players.size()-1) {
				for(int k = 0; k < players.size()-1; k++) {
					NewPlayerMessage message2 = new NewPlayerMessage(playerName); //usually should be (players.get(k).getName()), for the future
					TCPClients.get(i).sendMessage(message2);
				}
			}
			TCPClients.get(i).sendMessage(message);
		}
		
	}
	
	public void sendEndGame(int loserNumPlayer) {
		EndGameMessage winMessage = new EndGameMessage(true);
		EndGameMessage loseMessage = new EndGameMessage(false);
		for(int i = 0; i < TCPClients.size(); i++) {
			if(i == loserNumPlayer) {
				TCPClients.get(i).sendMessage(loseMessage);
			}else {
				TCPClients.get(i).sendMessage(winMessage);
			}
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
