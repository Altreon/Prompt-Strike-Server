package entity;

import math.MATH;

public abstract class Unit extends Entity{
	
	private final int MAINPART = 0;
	protected Part[] parts;
	
	private int waitMoveDistance;
	
	protected float SPEEDMOVE;
	
	private float moveDistance;
	private float moveDirection;
	
	public Unit(int owner, String name, float posX, float posY, int nbPart) {
		super(owner, name, posX, posY);
		parts = new Part[nbPart];
	}
	
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
		
		if(!parts[MAINPART].isRotating() && waitMoveDistance != 0){
			move(waitMoveDistance);
			waitMoveDistance = 0;
		}
	}
	
	public Part[] getParts () {
		return parts;
	}
	
	public void move(int distance) {
		if(distance > 0) {
			moveDistance = distance*64;
			moveDirection = 1;
		}else {
			moveDistance = -distance*64;
			moveDirection = -1;
		}
	}

	public void move(int x, int y) {
		float[] vectDir = {x, y};
		float[] vectRot = {(float) Math.cos(Math.toRadians(parts[MAINPART].getRotation())), (float) Math.sin(Math.toRadians(parts[MAINPART].getRotation()))};
		
		float angle = (float) Math.toDegrees(Math.acos(MATH.scalaire(vectDir, vectRot) / (MATH.norme(vectDir))));
		
		if(MATH.det(vectRot, vectDir) < 0) {
			angle = -angle;
		}
		
		rotate(angle, MAINPART);
		//NON TERMINE
		
		waitMoveDistance = (int) (MATH.norme(vectDir));
		
	}
	
	public float getRotation(int idPart) {
		return parts[idPart].rotation;
	}

	public void rotate(float distance, int idPart) {
		if(distance > 0) {
			parts[idPart].rotateDistance = distance;
			parts[idPart].rotateDirection = 1;
		}else {
			parts[idPart].rotateDistance = -distance;
			parts[idPart].rotateDirection = -1;
		}
	}
	
	public boolean isMoving () {
		if (moveDistance == 0) {
			return false;
		}else {
			return true;
		}
	}
	
	public void updateMove (long dt) {
		float[] newPos = new float[2];
		newPos[0] = (float) (pos[0] + SPEEDMOVE*(dt/1.0E9D) * moveDirection * Math.cos(Math.toRadians(parts[MAINPART].rotation)));
		newPos[1] = (float) (pos[1] + SPEEDMOVE*(dt/1.0E9D) * moveDirection * Math.sin(Math.toRadians(parts[MAINPART].rotation)));
		moveDistance -= (float) (MATH.dist(pos, newPos));
		pos[0] = newPos[0];
		pos[1] = newPos[1];
		if(moveDistance <= 0) {
			moveDistance = 0;
		}
	}
	
	public boolean isRotating () {
		return parts[MAINPART].isRotating();
	}
	
}
