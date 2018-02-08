package entity;

import math.MATH;

public class Part{
	protected float SPEEDROTATE;
	
	protected float rotation;
	
	protected float rotateDistance;
	protected float rotateDirection;
	
	protected Unit owner;
	
	public Part(Unit owner, float rotation) {
		this.owner = owner;
		this.rotation = rotation;
	}
	
	public float getRotation() {
		return rotation;
	}
	
	public boolean isRotating () {
		if (rotateDistance == 0) {
			return false;
		}else {
			return true;
		}
	}
	
	public void updateRotate (long dt) {
		float newRotation = (float) (rotation + SPEEDROTATE*(dt/1.0E9D) * rotateDirection);
		rotateDistance -= (newRotation - rotation) * rotateDirection;
		rotation = newRotation;
		if(rotateDistance <= 0) {
			rotateDistance = 0;
		}
	}
}