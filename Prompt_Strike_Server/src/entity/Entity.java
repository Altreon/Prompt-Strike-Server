package entity;

public abstract class Entity {
	
	private String name;
	private int HP;
	
	protected float[] pos;
	
	public abstract void update(long dt);
	
	public Entity(String name, float posX, float posY) {
		this.name = name;
		
		pos = new float[2];
		pos[0] = posX;
		pos[1] = posY;
	}
	
	public String getName() {
		return name;
	}
	
	public int getHP () {
		return HP;
	}
	
	public float[] getPos() {
		return pos;
	}
	
	public void changeHP(int amount) {
		HP += amount;
	}
}
