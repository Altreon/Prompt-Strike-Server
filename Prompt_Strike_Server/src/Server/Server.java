package Server;

import java.util.ArrayList;
import java.util.Enumeration;

import Command.Command;

import entity.Entity;
import entity.Structure;
import entity.Unit;
import map.Map;
import math.MATH;

import network.Network;
import player.Player;

/** * This is the main thread of the server's application */
public class Server {
	
	/** * This is the main thread of the server's application */
	private static ArrayList<Player> players;
	private static ArrayList<Integer> playersToRemove; //Used to remove players on the server thread, and avoid some bugs
	
	/** * The network manager */
	private static Network network;
	
	/** * The list of command waiting to be apply */
	private static ArrayList<String> commandWaitList;
	/** * The list of players's ID of commandWaitList*/
	private static ArrayList<Integer> commandWaitListOwner;
		
	/** * The last time save on begin of game loop. Used to calculate the delta time */
	private static long lastTime;
	
	/** * If the server will end */
	private static boolean isEnd;

	/** * Called at execution of application. Initialization of variables */
	public static void main(String[] args) {	
		isEnd = false;
		
		Map.initialization();
				
		players = new ArrayList<Player>();
		playersToRemove = new ArrayList<Integer>();
		
		commandWaitList = new ArrayList<String>();
		commandWaitListOwner = new ArrayList<Integer>();

		network = new Network();
		
		network.createServer();		
		
		while(!isEnd) { // Game loop
			update();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			System.out.println("The server will shutdown in 5 seconds...");
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		network.closeServer();
		
		//The application end with the main thread
	}
	
	/**
     * Adds a command on wait list to apply it on game thread.
     * 
     * @param numPlayer
     * 				The ID of player who send the command
     * @param commandText
     * 				The command's string
     */
	public static void addCommandOnWaitList(int numPlayer, String commandText) {
		commandWaitList.add(commandText);
		commandWaitListOwner.add(numPlayer);
	}
	
	/**
     * Threats the command
     * 
     * @param numPlayer
     * 				The ID of player who send the command
     * @param commandText
     * 				The command's string
     */
	private static void processCommand(int numPlayer, String commandText) {
		if(commandText.equals("newPlayer")) { //Pretty ugly method to add player on game loop, but simple.
			newPlayer();
		}else {
			boolean commandCorrect = Command.processCommand(numPlayer, commandText);
			network.sendCommand(numPlayer, commandText, commandCorrect);
		}
	}
	
	/** * Adds a new player on the game party */
	private static void newPlayer() {
		players.add(new Player(players.size()));
		
		network.sendNewPlayer(players);
		if(players.size() == 2) {
			launchGame();
		}
	}
	
	/** * Stop to accept new player and launch the game */
	private static void launchGame() {
		network.stopAcceptPlayer();
		
		//for the test of the game, comment/uncomment the next lines
		iniTwoPlayer();
		//iniTwoPlayerTestEntity();
		
		System.out.println("The game begin!");
	}
	
	/** * Players start the game with only one headquarter on corner of the map */
	private static void iniTwoPlayer() {
		createHeadquarter(0, "head", 0, 0);
		createHeadquarter(1, "head", (13-1)*64, (11-1)*64);
		
		addMoney(0, 100);
		addMoney(1, 100);
	}
	
	/** * Players start the game with all entity on the map. It is for test */
	private static void iniTwoPlayerTestEntity() {
		createHeadquarter(0, "head", 4*64, 2*64);
		createFactory(0, "fact", 4*64, 3*64);
		createWorker(0, "work", 4*64, 4*64);
		createTank(0, "tank", 4*64, 5*64);
		addMoney(0, 10000);
		
		createHeadquarter(1, "head", 7*64, 2*64);
		createFactory(1, "fact", 7*64, 3*64);
		createWorker(1, "work", 7*64, 4*64);
		createTank(1, "tank", 7*64, 5*64);
		addMoney(1, 10000);
	}
	
	public static ArrayList<Unit> getAllUnits() {
		ArrayList<Unit> units = new ArrayList<Unit>();
		for(Player player : players) {
			Enumeration<Unit> unitsEnum = player.getUnits().elements();
			while(unitsEnum.hasMoreElements()) {
				units.add(unitsEnum.nextElement());
			}
		}
		return units;
	}
	
	public static ArrayList<Structure> getAllstructures() {
		ArrayList<Structure> structures = new ArrayList<Structure>();
		for(Player player : players) {
			Enumeration<Structure> structuresEnum = player.getStructures().elements();
			while(structuresEnum.hasMoreElements()) {
				structures.add(structuresEnum.nextElement());
			}
		}
		return structures;
	}
	
	public static ArrayList<Player> getPlayers() {
		return players;
	}

	/** * Updates the states of the game. It's the game loop */
	public static void update() {
		long currentTime = System.nanoTime();
		long dt = System.nanoTime() - lastTime;
		lastTime = currentTime;
		
		for( Structure structure : getAllstructures()) {
			structure.update(dt);
		}
		
		for( Unit unit : getAllUnits()) {
			unit.update(dt);
		}
		
		for(int i=0; i < players.size(); i++) {
			Enumeration<Unit> unitsEnum = players.get(i).getUnits().elements();
			while(unitsEnum.hasMoreElements()) {
				Unit unit = unitsEnum.nextElement();
				if(unit.isMoving()) {
					network.sendPos(i, unit.getName(), unit.getPos()[0], unit.getPos()[1]);
				}
				
				for(int k = 0; k < unit.getParts().length; k++) {
					if(unit.getParts()[k].isRotating()) {
						network.sendRot(i, unit.getName(), unit.getParts()[k].getRotation(), k);
					}
				}
			}
		}
		
		destroy();
				
		while(!commandWaitListOwner.isEmpty()) {
			processCommand(commandWaitListOwner.remove(0), commandWaitList.remove(0));
		}
	}
	
	/** * Called at the end of update(), it remove disconnect players or dead entity from the game*/
	private static void destroy () {
		for(int numPlayer : playersToRemove) {
			players.remove(numPlayer);
			for(int i = numPlayer; i < players.size(); i++) {
				players.get(i).decrementNum();
			}
		}
		playersToRemove.clear();
		
	}
	
	/**
     * Adds money to a player
     * 
     * @param numPlayer
     * 				The ID of player who receive the money
     * @param amount
     * 				The amount of money to increase
     */
	public static void addMoney(int numPlayer, int amount) {
		players.get(numPlayer).addMoney(amount);
		network.sendUpdateMoney(numPlayer, players.get(numPlayer).getMoney());
	}
	
	/**
     * Removes money to a player
     * 
     * @param numPlayer
     * 				The ID of player who lose the money
     * @param amount
     * 				The amount of money to decrease
     */
	public static void removeMoney(int numPlayer, int amount) {
		players.get(numPlayer).removeMoney(amount);
		network.sendUpdateMoney(numPlayer, players.get(numPlayer).getMoney());
	}
	
	/**
     * Applies the dammaged caused by fire impact (cannon shoot) to the entities on the radius of "explosion"
     * 
     * @param posX
     * 				The X center's position of impact
     * @param posY
     * 				The Y center's position of impact
     * @param radius
     * 				The cercle's radius of hitbox impact
     * @param amount
     * 				The amount of damaged
     * @param playerExluded
     * 				The player ID who shoot, so their entity are safe from the damaged
     * @param nameUnit
     * 				The units shooter's name
     */
	public static void applyFireImpact(float posX, float posY, int radius, int amount, int playerExluded, String nameUnit) {
		network.sendFire(playerExluded, nameUnit, posX, posY);
		
		ArrayList<Entity> entityTouched = new ArrayList<Entity>();
		
		for (Unit unit : getAllUnits()) {
			float[] posDamageUnit = {unit.getPos()[0] - posX, unit.getPos()[1] - posY};
			if(unit.getOwner() != playerExluded && MATH.norme(posDamageUnit) <= radius) {
				entityTouched.add(unit);
			}
			
		}
		
		for (Structure struct : getAllstructures()) {
			float[] posDamageStruct = {struct.getPos()[0] - posX, struct.getPos()[1] - posY};
			if(struct.getOwner() != playerExluded && MATH.norme(posDamageStruct) <= radius) {
				entityTouched.add(struct);
			}
			
		}
		
		for (Entity entity : entityTouched) {
			entity.changeHP(-amount);
			if(entity.getHP() <= 0) {
				players.get(entity.getOwner()).destroyEntity(entity);
				network.sendDestroyEntity(entity.getOwner(), entity.getClass().getSuperclass().getSimpleName(), entity.getName());
				
				//if the headquarter of a player is destroy : end of the game
				if(entity.getClass().getSimpleName().equals("Headquarter")) {
					endGame(entity.getOwner());
				}
			}
		}
		
	}
	
	/** 
	 * Finished the game and proclaim a winner
	 * 
	 * @param loserNumPlayer
	 * 				The player ID who lose the game (so the winner is the other player)
	 */
	public static void endGame(int loserNumPlayer) { //There is more simple to get and send the loser of the game.
		isEnd = true;
		network.sendEndGame(loserNumPlayer);
	}
	
	/** 
	 * Create a new tank on the map
	 * 
	 * @param numPlayer
	 * 				The player ID who create the tank
	 * @param name
	 * 				The tank's name
	 * @param posX
	 * 				The X position of the tank
	 * @param posY
	 * 				The Y position of the tank
	 */
	public static void createTank(int numPlayer, String name, float posX, float posY) {
		players.get(numPlayer).addTank(name, posX, posY);
		network.sendNewEntity(numPlayer, "tank", name, posX, posY, 0);
	}
	
	/** 
	 * Creates a new worker on the map
	 * 
	 * @param numPlayer
	 * 				The player ID who create the workr
	 * @param name
	 * 				The worker's name
	 * @param posX
	 * 				The X position of the worker
	 * @param posY
	 * 				The Y position of the worker
	 */
	public static void createWorker(int numPlayer, String name, float posX, float posY) {
		players.get(numPlayer).addWorker(name, posX, posY);
		network.sendNewEntity(numPlayer, "worker", name, posX, posY, 0);
	}
	
	/** 
	 * Creates a new headquarter on the map
	 * 
	 * @param numPlayer
	 * 				The player ID who create the headquarter
	 * @param name
	 * 				The headquarter's name
	 * @param posX
	 * 				The X position of the headquarter
	 * @param posY
	 * 				The Y position of the headquarter
	 */
	public static void createHeadquarter(int numPlayer, String name, float posX, float posY) {
		players.get(numPlayer).addHeadquarter(name, posX, posY);
		network.sendNewEntity(numPlayer, "headquarter", name, posX, posY, 0);
	}

	/** 
	 * Creates a new factory on the map
	 * 
	 * @param numPlayer
	 * 				The player ID who create the factory
	 * @param name
	 * 				The factory's name
	 * @param posX
	 * 				The X position of the factory
	 * @param posY
	 * 				The Y position of the factory
	 */
	public static void createFactory(int numPlayer, String name, float posX, float posY) {
		players.get(numPlayer).addFactory(name, posX, posY);
		network.sendNewEntity(numPlayer, "factory", name, posX, posY, 0);
	}

	/** 
	 * Removes a disconnected players from the game
	 * 
	 * @param numPlayer
	 * 				The player ID who disconnected
	 */
	public static void removePlayer(int numPlayer) {
		System.out.println("The player " + numPlayer + " was disconnected");
		
		if(players.size() < 2) { //if the game hadn't begin
			playersToRemove.add(numPlayer);
			
		}else { //if the game had begin,
			isEnd = true; // stop the game
		}
	}
}
