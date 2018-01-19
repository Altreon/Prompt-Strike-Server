package main;

import java.util.ArrayList;
import java.util.Enumeration;

import entity.Entity;
import entity.Structure;
import entity.Unit;
import math.MATH;
import network.Network;

public class Server {
	public static ArrayList<Player> players;
	
	public static Network network;
	
	private long lastTime;

	public static void main(String[] args) {
		players = new ArrayList<Player>();

		network = new Network();
		
		network.createServer();
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

	public void update() {
		long currentTime = System.currentTimeMillis();
		int dt = (int) (System.currentTimeMillis() - lastTime);
		
		for( Structure structure : getAllstructures()) {
			structure.update(dt);
		}
		
		for( Unit unit : getAllUnits()) {
			unit.update(dt);
		}
		
		destroyEntities();
		
		lastTime = currentTime;
	}
	
	private void destroyEntities () {

		
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

	public static void createTank(String name, float posX, float posY) {
		players.get(0).addTank(name, posX, posY);
	}
	
	public static void createWorker(String name, float posX, float posY) {
		players.get(0).addWorker(name, posX, posY);
	}

	public static void createFactory(String name, float posX, float posY) {
		players.get(0).addFactory(name, posX, posY);
	}

}
