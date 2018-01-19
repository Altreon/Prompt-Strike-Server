package entity;

import main.Server;

public class Cannon extends Part{
	
	//garder?
	private final int ESTIMATETIME = 330;
	private long startTime;
	
	private final int IMPACTRADIUS = 1;
	private final int IMPACTDAMAGE = 1;

	public Cannon (float posX, float posY, float rotation) {
		super(posX, posY, rotation);
		SPEEDMOVE = 0;
		SPEEDROTATE = 1;
	}
	
	@Override
	public boolean isMoving () {
		return false;
	}
	
	//garder?
	public void update (float x, float y) {
		if(x != pos[0] || y != pos[1]) {
			if(System.currentTimeMillis() - startTime >= ESTIMATETIME) {
				setPosition(x, y);
			}else {
				pos[0] = (float) (pos[0] + 64/100*Math.cos(rotation));
				pos[1] = (float) (pos[1] + 64/100*Math.sin(rotation));
			}
		}
	}

	public void fire(int distance, int playerOwner) {
		float firePosX = (float) (pos[0] + 64/1.3f*Math.cos(rotation));
		float firePosY = (float) (pos[1] + 64/1.3f*Math.sin(rotation));
		
		float ImpactPosX = (float) (pos[0] + distance*64*Math.cos(rotation));
		float ImpactPosY = (float) (pos[1] + distance*64*Math.sin(rotation));
		
		Server.applyDamage(ImpactPosX, ImpactPosY, IMPACTRADIUS, IMPACTDAMAGE, playerOwner);
		//Game.createEffect(new TankFire(firePosX, firePosY, rotaton - 90));
		//Game.createEffect(new TankImpact(ImpactPosX, ImpactPosY, 0));
	
		pos[0] = (float) (pos[0] - 64/5*Math.cos(rotation));
		pos[1] = (float) (pos[1] - 64/5*Math.sin(rotation));
		
		startTime = System.currentTimeMillis();
	}
}