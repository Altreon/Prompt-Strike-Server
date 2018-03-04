package entity;

/** 
 * The main part of the tank unit
 * 
 * @see Part
 * */
public class Chassis extends Part{
	
	/** * The Chassis's rotation speed */
	private final static float SPEED_ROTATE = 45;
	
	/**
     * Create a Chassis
     * 
     * @param owner
     * 				The player ID who owns the chassis
     * @param rotation
     * 				The initial chassis's rotation
     */
	public Chassis (Unit owner, float rotation) {
		super(owner, rotation, SPEED_ROTATE);
	}
}
