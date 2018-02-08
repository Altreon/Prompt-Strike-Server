package entity;


public class Tank extends Unit {
	
	private static final int COST = 20;
	
	private Chassis chassis;
	private Cannon cannon;

	public Tank (int owner, String name, float posX, float posY, float rotation) {
		super(owner, name, posX, posY, 2);
		
		chassis = new Chassis(this, rotation);
		cannon = new Cannon(this, rotation);
		parts = new Part[] {chassis, cannon};
		
		SPEEDMOVE = 64;
	}
	
	public static int getCost () {
		return COST;
	}
	
	//public Cannon getCannon () {
		//return cannon;
	//}

	public void fire(int distance, int playerOwner) {
		cannon.fire(distance, playerOwner, name);
	}
}
