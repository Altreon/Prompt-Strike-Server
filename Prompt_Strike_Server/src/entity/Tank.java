package entity;


public class Tank extends Unit {
	
	private static final int COST = 20;
	
	private Chassis chassis;
	private Cannon cannon;
	
	private Part[] parts;

	public Tank (String name, float posX, float posY, float rotation) {
		super(name, posX * 64, posY * 64, rotation);
		
		parts = new Part[2];
		
		cannon = new Cannon(posX * 64, posY * 64, rotation);
		chassis = new Chassis(cannon, posX * 64, posY * 64, rotation);
		
		parts[0] = chassis;
		parts[1] = cannon;
	}
	
	public static int getCost () {
		return COST;
	}
	
	public Part[] getParts () {
		return parts;
	}
	
	public Cannon getCannon () {
		return cannon;
	}
	
	@Override
	public void update(int dt) {
		for(Part part : parts) {
			if(part.isMoving()){
				part.updateMove();
			}
			if(part.isRotating()){
				part.updateRotate();
			}
		}
		
		if(!chassis.isRotating() && waitMoveDistance != 0){
			move(waitMoveDistance);
			waitMoveDistance = 0;
		}
		
		//garder?
		cannon.update(pos[0], pos[1]);
	}
	
	@Override
	public void move(int distance) {
		if(distance > 0) {
			chassis.moveDistance = distance*64;
			chassis.moveDirection = 1;
		}else {
			chassis.moveDistance = -distance*64;
			chassis.moveDirection = -1;
		}
	}
	
	@Override
	public void rotate(int distance) {
		if(distance > 0) {
			chassis.rotateDistance = distance;
			chassis.rotateDirection = 1;
		}else {
			chassis.rotateDistance = -distance;
			chassis.rotateDirection = -1;
		}
	}
	
	public void rotateCannon(int distance) {
		if(distance > 0) {
			cannon.rotateDistance = distance;
			cannon.rotateDirection = 1;
		}else {
			cannon.rotateDistance = -distance;
			cannon.rotateDirection = -1;
		}
	}

	public void fire(int distance, int playerOwner) {
		cannon.fire(distance, playerOwner);
	}
}
