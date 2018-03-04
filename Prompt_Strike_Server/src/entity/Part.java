package entity;

import math.MATH;

/** 
 * This class is used to control rotation of different parts of units
 * 
 * @see Unit
 * */
public class Part{
	
	/** * The rotation's speed in degrees per seconds */
	private float speedRotate;
	
	/** * The current orientation of the part in radian */
	protected float rotation;
	
	/** * The rotation degree remaining to turn */
	protected float rotateDistance;
	/** * The current rotation's direction (1 for clockwise or -1 for counterclockwise )*/
	protected int rotateDirection;
	
	/** The unit who owns the entity */
	protected Unit owner;
	
	/**
     * Create a Part
     * 
     * @param owner
     * 				The unit who owns the part
     * @param rotation
     * 				The initial part's rotation
     * @param speedRotate
     * 				The rotation's speed in degrees per seconds
     */
	public Part(Unit owner, float rotation, float speedRotate) {
		this.owner = owner;
		this.rotation = rotation;
		this.speedRotate = speedRotate;
	}
	
	public float getRotation() {
		return rotation;
	}
	
	/** * @return true if the part is rotating */
	public boolean isRotating () {
		if (rotateDistance == 0) {
			return false;
		}else {
			return true;
		}
	}
	
	/**
     * Updates the part's orientation at each game loop
     * 
     * @param dt
     * 				The delta time
     */
	public void updateRotate (long dt) {
		float rotationInDegree = (float) MATH.toDegrees(rotation);
		float newRotationInDegree = (float) (rotationInDegree + speedRotate*(dt/1.0E9D) * rotateDirection);
		
		rotateDistance -= (newRotationInDegree - rotationInDegree) * rotateDirection;
		
		rotation = (float) MATH.toRadians(newRotationInDegree);
		
		if(rotateDistance <= 0) {
			rotateDistance = 0;
		}
	}
}