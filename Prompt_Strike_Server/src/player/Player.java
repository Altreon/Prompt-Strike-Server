package player;

/** * This is the player's class of the game, what else to say? */
import java.util.Enumeration;
import java.util.Hashtable;

import entity.*;

public class Player {
	
	/** * The ID number of the player */
	private int numPlayer;
	
	/** * The current amount of player's money */
	private int money;
	
	/** * The list of units */
	private Hashtable<String, Unit> units;
	/** * The list of structures */
	private Hashtable<String, Structure> structures;
	
	/**
     * Create a new player
     * 
     * @param num
     * 				The player ID
     */
	public Player (int num) {
		this.numPlayer = num;
		
		units = new Hashtable<String, Unit>();
		structures = new Hashtable<String, Structure>();
	}
	
	/**
     * Adds money to the player
     * 
     * @param amount
     * 				The amount of money to increase
     */
	public void addMoney(int amount) {
		money += amount;
	}
	
	/**
     * Removes money to the player
     * 
     * @param amount
     * 				The amount of money to decrease
     */
	public void removeMoney(int amount) {
		money -= amount;
	}
	
	public Hashtable<String, Unit> getUnits() {
		return units;
	}
	
	public Hashtable<String, Structure> getStructures() {
		return structures;
	}
	
	/**
     * @param name
     * 				The name entered by the player
     * @return True if the name belongs to a units of this player
     */
	public boolean isUnit(String name) {
		return units.containsKey(name);
	}
	
	/**
     * @param name
     * 				The name entered by the player
     * @return True if the name belongs to a structures of this player
     */
	public boolean isStructure(String name) {
		return structures.containsKey(name);
	}
	
	/** 
	 * Moves a unit on the map on their current orientation
	 * 
	 * @param name
     * 				The unit's name
	 * @param value
     * 				The distance value to reach
	 */
	public void moveUnit(String name, int value) {
		units.get(name).move(value);
	}
	
	/** 
	 * Moves a unit on the map to a relative position
	 * 
	 * @param name
     * 				The unit's name
	 * @param posX
     * 				The relative X coordinate
     * @param posY
     * 				The relative Y coordinate
	 */
	public void moveUnit(String name, int posX, int posY) {
		units.get(name).move(posX, posY);
	}
	
	/** 
	 * Rotates a part of a unit
	 * 
	 * @param name
     * 				The unit's name
	 * @param value
     * 				The rotation distance to reach
     * @param idPart
     * 				The ID part to turn
	 * */
	public void rotateUnit(String name, int value, int idPart) {
		units.get(name).rotate(value, idPart);
	}
	
	/**
     * @param name
     * 				The unit's name
     * @return True if the units have a cannon
     */
	public boolean unitCanRotateCannon(String name) {
		return units.get(name).getClass().getSimpleName().equals("Tank");
	}
	
	/**
     * @param name
     * 				The unit's name
     * @return True if the units have a cannon and can fire
     */
	public boolean unitCanFire(String name) {
		return units.get(name).getClass().getSimpleName().equals("Tank");
		//For the future, add a time between the same unit can't fire again (like a reload time)
	}
	
	/**
	 * Makes fire a unit
     * @param name
     * 				The unit's name
     * @param value
     * 				The shoot distance from the cannon
     */
	public void fireUnit(String name, int value) {
		((Tank) units.get(name)).fire(value);
	}
	
	/**
     * @param cost
     * 				The entity's cost
     * @return True if the player can spend the cost of entity to construct it
     */
	public boolean sufficientMoney(int cost) {
		return money >= cost;
	}
	
	public int getMoney() {
		return money;
	}
	
	/**
     * @param unitName
     * 				The checked unit's name
     * @param structType
     * 				The structure type's name
     * @param structName
     * 				The structure name (needed to check if the name is already pick)
     * @return True if the unit can build a specific structure
     */
	public boolean unitCanBuild(String unitName, String structType, String structName) {
		return units.get(unitName).getClass().getSimpleName().equals("Worker")
				&& !structures.containsKey(structName)
				&& !units.containsKey(structName)
				&& ((Worker) units.get(unitName)).canBuild(structType);
	}
	
	/**
	 * Makes a unit build a structures on the map
	 * 
     * @param unitName
     * 				The builder unit's name
     * @param structType
     * 				The structure type's name
     * @param structName
     * 				The structure name
     */
	public void buildUnit(String unitName, String structType, String structName) {
		((Worker) units.get(unitName)).build(structType, structName);
		
	}
	
	/**
     * @param unitName
     * 				The checked unit's name
     * @return True if the unit can gather something at their current position
     */
	public boolean unitCanGather(String unitName) {
		return units.get(unitName).getClass().getSimpleName().equals("Worker")
				&& ((Worker) units.get(unitName)).canGather();
	}
	
	/**
	 * Makes Start a unit to gather
	 * 
     * @param unitName
     * 				The gatherer unit's name
     */
	public void gatherUnit(String unitName) {
		((Worker) units.get(unitName)).gather();
	}

	/**
     * @param structName
     * 				The checked structure's name
     * @param unitType
     * 				The unit type's name
     * @param unitName
     * 				The unit name (needed to check if the name is already pick)
     * @return True if the structure can build a specific unit
     */
	public boolean structCanProduce(String structName, String unitType, String unitName) {
		return !units.containsKey(unitName)
				&& !structures.containsKey(unitName)
				&& structures.get(structName).canProduce(unitType);
	}

	/**
	 * Produces a unit on the map with a structure
	 * 
     * @param structName
     * 				The builder structure's name
     * @param unitType
     * 				The unit type's name
     * @param unitName
     * 				The unit name
     */
	public void structProduce(String structName, String unitType, String unitName) {
		structures.get(structName).produce(unitType, unitName);
		
	}
	
	/**
	 * Adds a new tank for this player
	 * 
     * @param name
     * 				The tank's name
     * @param posX
     * 				The X position of the tank
     * @param posY
     * 				The Y position of the tank
     */
	public void addTank(String name, float posX, float posY) {
		units.put(name, new Tank(numPlayer, name, posX, posY, 0));
		
	}
	
	/**
	 * Adds a new worker for this player
	 * 
     * @param name
     * 				The worker's name
     * @param posX
     * 				The X position of the worker
     * @param posY
     * 				The Y position of the worker
     */
	public void addWorker(String name, float posX, float posY) {
		units.put(name, new Worker(numPlayer, name, posX, posY, 0));
	}
	
	/**
	 * Adds a new headquarter for this player
	 * 
     * @param name
     * 				The headquarter's name
     * @param posX
     * 				The X position of the headquarter
     * @param posY
     * 				The Y position of the headquarter
     */
	public void addHeadquarter(String name, float posX, float posY) {
		structures.put(name, new Headquarter(numPlayer, name, posX, posY));
	}
	
	/**
	 * Adds a new factory for this player
	 * 
     * @param name
     * 				The factory's name
     * @param posX
     * 				The X position of the factory
     * @param posY
     * 				The Y position of the factory
     */
	public void addFactory(String name, float posX, float posY) {
		structures.put(name, new Factory(numPlayer, name, posX, posY));
	}

	/**
	 * Removes a player's entity 
	 * 
     * @param entity
     * 				The entity to remove
     * @see Entity
     */
	public void destroyEntity(Entity entity) {
		String entityClass = entity.getClass().getSuperclass().getSimpleName();
		
		if(entityClass.equals("Unit")) {
			units.remove(entity.getName());
		}else {
			structures.remove(entity.getName());
		}
		
	}

	/** * decrement the ID of player, useful when a players was disconnected */
	public void decrementNum() {
		numPlayer--;
		
		Enumeration<Unit> unitsEnum = units.elements();
		while(unitsEnum.hasMoreElements()) {
			Unit unit = unitsEnum.nextElement();
			unit.setOwner(numPlayer);
		}
		
		Enumeration<Structure> structuresEnum = structures.elements();
		while(structuresEnum.hasMoreElements()) {
			Structure struct = structuresEnum.nextElement();
			struct.setOwner(numPlayer);
		}
		
	}
}
