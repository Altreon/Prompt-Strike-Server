package entity;

import math.MATH;

public class Part{
	protected float SPEEDMOVE;
	protected float SPEEDROTATE;
	
	protected float[] pos;
	protected float rotation;
	
	protected float moveDistance;
	protected float moveDirection;
	protected float rotateDistance;
	protected float rotateDirection;
	
	public Part(float posX, float posY, float rotation) {
		pos[0] = posX;
		pos[1] = posY;
		this.rotation = rotation;
	}
	
	public void setPosition (float posX, float posY) {
		pos[0] = posX;
		pos[1] = posY;
	}
	
	public boolean isMoving () {
		if (moveDistance == 0) {
			return false;
		}else {
			return true;
		}
	}
	
	public boolean isRotating () {
		if (rotateDistance == 0) {
			return false;
		}else {
			return true;
		}
	}
	
	public void updateMove () {
		float[] newPos = new float[2];
		newPos[0] = (float) (pos[0] + SPEEDMOVE * moveDirection * Math.cos(rotation));
		newPos[1] = (float) (pos[1] + SPEEDMOVE * moveDirection * Math.sin(rotation));
		moveDistance -= (float) (MATH.dist(pos, newPos));
		pos[0] = newPos[0];
		pos[1] = newPos[1];
		if(moveDistance <= 0) {
			moveDistance = 0;
		}
	}
	
	public void updateRotate () {
		float newRotation = rotation + SPEEDROTATE * rotateDirection;
		rotateDistance -= (newRotation - rotation) * rotateDirection;
		rotation = newRotation;
		if(rotateDistance <= 0) {
			rotateDistance = 0;
		}
	}
}