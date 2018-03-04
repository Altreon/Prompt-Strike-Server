package entity;

import Server.Server;
import map.Map;

/** 
 * The turret part of the tank unit
 * 
 * @see Part
 * */
public class Cannon extends Part{
	
	/** * The Cannon's rotation speed */
	private final static float SPEED_ROTATE = 45;
	
	/** * The area of impact hitbox */
	private final int IMPACT_RADIUS = Map.getTileSize()/2;
	/** * The impact's damage on entity */
	private final int IMPACT_DAMAGE = 1;

	/**
     * Create a cannon
     * 
     * @param owner
     * 				The player ID who owns the cannon
     * @param rotation
     * 				The initial cannon's rotation
     */
	public Cannon (Unit owner, float rotation) {
		super(owner, rotation, SPEED_ROTATE);
	}

	/**
     * Makes fire the cannon
     * 
     * @param distance
     * 				The shoot distance from the cannon
     * @param playerOwner
     * 				The player ID who owns the cannon
     * @param nameUnit
     * 				The name of unit on whose is the cannon
     */
	public void fire(int distance, int playerOwner, String nameUnit) {
		float ImpactPosX = (float) (owner.pos[0] + distance*Map.getTileSize()*Math.cos(rotation));
		float ImpactPosY = (float) (owner.pos[1] + distance*Map.getTileSize()*Math.sin(rotation));
		
		Server.applyFireImpact(ImpactPosX, ImpactPosY, IMPACT_RADIUS, IMPACT_DAMAGE, playerOwner, nameUnit);
	}
}