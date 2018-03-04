package entity;

import map.Map;
import math.MATH;

/** 
 * The Unit is an subtype of entity, It use by player to face their opponent
 * 
 * @see Entity
 */
public abstract class Unit extends Entity{
	
	/** * The part's list of the unit */
	protected Part[] parts;
	/** * The ID of main part in part's list */
	private final int MAINPART = 0;
	
	/** * The unit's move speed in pixels per seconds */
	private float speedMove;
	
	/** * The distance remaining to move */
	private float moveDistance;
	/** * The current move's direction (1 for forward or -1 for backward )*/
	private float moveDirection;
	
	/** 
	 * The distance (negative or positive) queued at reach after a rotation.
	 * Used for two-argument movement
	 *  */
	private int waitMoveDistance;
		
	/**
     * Create a Unit
     * 
     * @param owner
     * 				The player ID who owns the unit
     * @param name
     * 				The unit's name
     * @param posX
     * 				The X position of the unit
     * @param posY
     * 				The Y position of the unit
     * @param speedMove
     * 				The unit's move speed
     */
	public Unit(int owner, String name, float posX, float posY, float speedMove) {
		super(owner, name, posX, posY);
		this.speedMove = speedMove;
	}
	
	/**
     * Updates the position and rotation of the unit each game loop
     * 
     * @param dt
     * 				The delta time
     */
	@Override
	public void update(long dt) {
		if(isMoving()){
			updateMove(dt);
		}
		
		for(Part part : parts) {
			if(part.isRotating()){
				part.updateRotate(dt);
			}
		}
		
		//use to automatically move unit when a movement is queued and is no-rotating
		if(!parts[MAINPART].isRotating() && waitMoveDistance != 0){
			move(waitMoveDistance);
			waitMoveDistance = 0;
		}
	}
	
	public Part[] getParts () {
		return parts;
	}
	
	/** 
	 * Moves the unit on the map on their current orientation
	 * 
	 * @param distance
     * 				The distance to reach
	 * */
	public void move(int distance) {
		if(distance > 0) {
			moveDistance = distance*Map.getTileSize();
			moveDirection = 1;
		}else {
			moveDistance = -distance*Map.getTileSize();
			moveDirection = -1;
		}
	}

	/** 
	 * Moves the unit on the map to a relative position
	 * 
	 * @param x
     * 				The relative X coordinate
     * @param y
     * 				The relative Y coordinate
	 * */
	public void move(int x, int y) {
		float[] vectDir = {x, y};
		float[] vectRot = {(float) Math.cos(parts[MAINPART].getRotation()), (float) Math.sin(parts[MAINPART].getRotation())};
		
		float angle = (float) MATH.toDegrees(MATH.angleBetweenTwoVectors(vectDir,  vectRot));
		
		if(MATH.det(vectRot, vectDir) < 0) {
			angle = -angle;
		}
		
		rotate(angle, MAINPART);
		
		//queued the movement
		waitMoveDistance = (int) (MATH.norme(vectDir));
		
	}

	/** 
	 * Rotates a part of the unit
	 * 
	 * @param distance
     * 				The rotation distance to reach
     * @param idPart
     * 				The ID part to turn
	 * */
	public void rotate(float distance, int idPart) {
		if(distance > 0) {
			parts[idPart].rotateDistance = distance;
			parts[idPart].rotateDirection = 1;
		}else {
			parts[idPart].rotateDistance = -distance;
			parts[idPart].rotateDirection = -1;
		}
	}
	
	/** * @return true if the unit is moving */
	public boolean isMoving () {
		if (moveDistance == 0) {
			return false;
		}else {
			return true;
		}
	}
	
	/**
     * Updates the move's state and position at each game loop
     * 
     * @param dt
     * 				The delta time
     */
	public void updateMove (long dt) {
		float[] newPos = new float[2];
		newPos[0] = (float) (pos[0] + speedMove*(dt/1.0E9D) * moveDirection * Math.cos(parts[MAINPART].rotation));
		newPos[1] = (float) (pos[1] + speedMove*(dt/1.0E9D) * moveDirection * Math.sin(parts[MAINPART].rotation));
		
		moveDistance -= (float) (MATH.dist(pos, newPos));
		
		if(canMoveX(newPos)) {
			pos[0] = newPos[0];
		}
		if(canMoveY(newPos)) {
			pos[1] = newPos[1];
		}
		
		
		if(moveDistance <= 0) {
			moveDistance = 0;
		}
	}
	
	/** 
	  * @param atPos
     * 				The position the unit is trying to reach
     * 
	 * @return true if the unit can move in abscissa axis to a specific point 
	 */
	private boolean canMoveX(float[] atPos) {
		return atPos[0] >= 0 && atPos[0] <= (Map.getMapSizeX()-1)*Map.getTileSize();
	}
	
	/** 
	  * @param atPos
    * 				The position the unit is trying to reach
    * 
	 * @return true if the unit can move in ordered axis to a specific point 
	 */
	private boolean canMoveY(float[] atPos) {
		return atPos[1] >= 0 && atPos[1] <= (Map.getMapSizeY()-1)*Map.getTileSize();
	}
}
