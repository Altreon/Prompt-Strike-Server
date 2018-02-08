package entity;

import main.Server;

public class Cannon extends Part{
	
	//garder?
	private final int ESTIMATETIME = 330;
	private long startTime;
	
	private final int IMPACTRADIUS = 32;
	private final int IMPACTDAMAGE = 1;

	public Cannon (Unit owner, float rotation) {
		super(owner, rotation);
		SPEEDROTATE = 45;
	}

	public void fire(int distance, int playerOwner, String nameUnit) {
		//float firePosX = (float) (owner.pos[0] + 64/1.3f*Math.cos(rotation));
		//float firePosY = (float) (owner.pos[1] + 64/1.3f*Math.sin(rotation));
		
		float ImpactPosX = (float) (owner.pos[0] + distance*64*Math.cos(rotation));
		float ImpactPosY = (float) (owner.pos[1] + distance*64*Math.sin(rotation));
		
		Server.applyFire(ImpactPosX, ImpactPosY, IMPACTRADIUS, IMPACTDAMAGE, playerOwner, nameUnit);
		
		startTime = System.currentTimeMillis();
	}
}