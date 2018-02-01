package main;

import java.util.ArrayList;
import java.util.Enumeration;

import entity.Entity;
import entity.Structure;
import entity.Unit;
import math.MATH;
import network.Network;

public class Server {
	private static ArrayList<Player> players;
	private static ArrayList<Integer> playersToRemove;
	
	private static Network network;
	
	private static Command command;
	private static ArrayList<String> commandWaitList;
	private static ArrayList<Integer> commandWaitListInt;
	
	private static long lastTime;

	public static void main(String[] args) {		
		players = new ArrayList<Player>();
		playersToRemove = new ArrayList<Integer>();
		
		command = new Command();
		commandWaitList = new ArrayList<String>();
		commandWaitListInt = new ArrayList<Integer>();

		network = new Network();
		
		network.createServer();
		
		while(true) {
			update();
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
			boolean commandCorrect = command.processCommand(commandText);
			network.sendCommand(numPlayer, commandText, commandCorrect);
		}
	}
	
	private static void newPlayer() {
		System.out.println("create");
		players.add(new Player(players.size() - 1));
		System.out.println("create finish");
		
		//players.get(players.size() - 1).addWorker("worker", posX*64, 2*64);
		//network.sendCorrectCommand(players.size() - 1, "create worker worker " + posX*64 + " " + 2*64);
		network.sendNewPlayer(players);
		if(players.size() == 2) {
			for(int i = 0; i < players.size(); i++) {
				int posX = 0;
				if(i == 0) {
					posX = 1;
				}else {
					posX = 8;
				}
				
				createWorker(i, "worker", posX*64, 2*64);
			}
		}
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
		//System.out.println("update");
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
				if(unit.isRotating()) {
					network.sendRot(i, unit.getName(), unit.getRotation());
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
			System.out.println("remove");
			players.remove(numPlayer);
			for(int i = numPlayer; i < players.size(); i++) {
				players.get(i).decrementNum();
			}
		}
		playersToRemove.clear();
		
	}
	
	public static void addMoney(int amont) {
		players.get(0).addMoney(amont);
	}
	
	public static void removeMoney(int amont) {
		players.get(0).removeMoney(amont);
	}
	
	public static void applyDamage(float posX, float posY, int radius, int amount, int playerExluded) {
		ArrayList<Entity> entityTouched = new ArrayList<Entity>();
		for (Unit unit : getAllUnits()) {
			float[] posDamageUnit = {unit.getPos()[0] - posX, unit.getPos()[1] - posY};
			if(!players.get(playerExluded).isUnit(unit.getName()) && MATH.norme(posDamageUnit) <= radius*64) {
				System.out.println("touch!");;
				entityTouched.add(unit);
			}
			
		}
		
		for (Structure struct : getAllstructures()) {
			float[] posDamageStruct = {struct.getPos()[0] - posX, struct.getPos()[1] - posY};
			if(!players.get(playerExluded).isStructure(struct.getName()) && MATH.norme(posDamageStruct) <= radius*64) {
				System.out.println("touch!");
				entityTouched.add(struct);
			}
			
		}
		
		for (Entity entity : entityTouched) {
			entity.changeHP(-amount);
			if(entity.getHP() <= 0) {
				players.get(0).destroyEntity(entity);
			}
		}
		
	}

	public static void createTank(int numPlayer, String name, float posX, float posY) {
		players.get(numPlayer).addTank(name, posX, posY);
		network.sendNewEntity(numPlayer, "tank", name, posX, posY, 0);
	}
	
	public static void createWorker(int numPlayer, String name, float posX, float posY) {
		players.get(numPlayer).addWorker(name, posX, posY);
		network.sendNewEntity(numPlayer, "worker", name, posX, posY, 0);
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
