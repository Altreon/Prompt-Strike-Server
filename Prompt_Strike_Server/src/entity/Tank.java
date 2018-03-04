package entity;

import map.Map;

/** 
 * The tank is the only offensive unit for now.
 * It is making in factory and can attack player's enemies entities with a cannon 
 * 
 * @see Factory
 * @see Cannon
 */
public class Tank extends Unit {
	
	/** * The construction cost for the players */
	private static final int COST = 20;
	/** * The tank's move speed in pixels per seconds */
	private static final float SPEED_MOVE = Map.getTileSize();
	
	/** * The main part of the tank */
	private Chassis chassis;
	/** * The cannon of the tank */
	private Cannon cannon;

	/**
     * Create a Tank
     * 
     * @param owner
     * 				The player ID who owns the tank
     * @param name
     * 				The tank's name
     * @param posX
     * 				The X position of the tank
     * @param posY
     * 				The Y position of the tank
     * @param rotation
     * 				The tank's initial rotation
     */
	public Tank (int owner, String name, float posX, float posY, float rotation) {
		super(owner, name, posX, posY, SPEED_MOVE);
		
		parts = new Part[2];
		chassis = new Chassis(this, rotation);
		cannon = new Cannon(this, rotation);
		parts = new Part[] {chassis, cannon};
	}
	
	public static int getCost () {
		return COST;
	}

	/**
     * Fires with the tank's cannon
     * 
     * @param distance
     * 				The shoot distance from the cannon
     */
	public void fire(int distance) {
		cannon.fire(distance, owner, name);
	}
}
