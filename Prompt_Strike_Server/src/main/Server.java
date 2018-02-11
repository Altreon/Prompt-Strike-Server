package main;

import java.util.ArrayList;
import java.util.Enumeration;

import entity.Entity;
import entity.Structure;
import entity.Unit;
import map.Map;
import math.MATH;
import network.Network;

public class Server {
	private static ArrayList<Player> players;
	private static ArrayList<Integer> playersToRemove;
	
	private static Network network;
	
	private static Command command;
	private static ArrayList<String> commandWaitList;
	private static ArrayList<Integer> commandWaitListInt;
	
	private static Map map;
	
	private static long lastTime;
	
	private static boolean exit;

	public static void main(String[] args) {	
		exit = false;
		
		map = new Map();
		
		players = new ArrayList<Player>();
		playersToRemove = new ArrayList<Integer>();
		
		command = new Command();
		commandWaitList = new ArrayList<String>();
		commandWaitListInt = new ArrayList<Integer>();

		network = new Network();
		
		network.createServer();
		
		
		while(!exit) {
			//System.out.println("update");
			update();
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static void addCommandOnWaitList(int numPlayer, String commandText) {
		commandWaitList.add(commandText);
		commandWaitListInt.add(numPlayer);
	}
	
	
	private static void processCommand(int numPlayer, String commandText) {
		if(commandText.equals("newPlayer")) {
			newPlayer();
		}else {
			boolean commandCorrect = command.processCommand(numPlayer, commandText);
			network.sendCommand(numPlayer, commandText, commandCorrect);
		}
	}
	
	private static void newPlayer() {
		players.add(new Player(players.size()));
		
		//players.get(players.size() - 1).addWorker("worker", posX*64, 2*64);
		//network.sendCorrectCommand(players.size() - 1, "create worker worker " + posX*64 + " " + 2*64);
		network.sendNewPlayer(players);
		if(players.size() == 2) {
			//iniTwoPlayer();
			iniTwoPlayerTestEntity();
		}
	}
	
	private static void iniTwoPlayer() {
		createHeadquarter(0, "head", 0, 0);
		createHeadquarter(1, "head", (13-1)*64, (11-1)*64);
		
		addMoney(0, 100);
		addMoney(1, 100);
	}
	
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
		
		destroyEntities();
		
		while(!commandWaitListInt.isEmpty()) {
			processCommand(commandWaitListInt.remove(0), commandWaitList.remove(0));
		}
	}
	
	private static void destroyEntities () {
		for(int numPlayer : playersToRemove) {
			players.remove(numPlayer);
			for(int i = numPlayer; i < players.size(); i++) {
				players.get(i).decrementNum();
			}
		}
		playersToRemove.clear();
		
	}
	
	public static void addMoney(int numPlayer, int amount) {
		players.get(numPlayer).addMoney(amount);
		network.sendUpdateMoney(numPlayer, players.get(numPlayer).getMoney());
	}
	
	public static void removeMoney(int numPlayer, int amount) {
		players.get(numPlayer).removeMoney(amount);
		network.sendUpdateMoney(numPlayer, players.get(numPlayer).getMoney());
	}
	
	public static void applyFire(float posX, float posY, int radius, int amount, int playerExluded, String nameUnit) {
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
			}
		}
		
		network.sendFire(playerExluded, nameUnit, posX, posY);
		
	}

	public static void createTank(int numPlayer, String name, float posX, float posY) {
		players.get(numPlayer).addTank(name, posX, posY);
		network.sendNewEntity(numPlayer, "tank", name, posX, posY, 0);
	}
	
	public static void createWorker(int numPlayer, String name, float posX, float posY) {
		players.get(numPlayer).addWorker(name, posX, posY);
		network.sendNewEntity(numPlayer, "worker", name, posX, posY, 0);
	}
	
	public static void createHeadquarter(int numPlayer, String name, float posX, float posY) {
		players.get(numPlayer).addHeadquarter(name, posX, posY);
		network.sendNewEntity(numPlayer, "headquarter", name, posX, posY, 0);
	}

	public static void createFactory(int numPlayer, String name, float posX, float posY) {
		players.get(numPlayer).addFactory(name, posX, posY);
		network.sendNewEntity(numPlayer, "factory", name, posX, posY, 0);
	}

	public static void removePlayer(int nPlayer) {
		System.out.println("The player " + nPlayer + " was disconnected");
		
		playersToRemove.add(nPlayer);	
	}
}
