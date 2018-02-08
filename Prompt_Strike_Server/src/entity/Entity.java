package entity;

public abstract class Entity {
	
	protected int owner;
	protected String name;
	private int HP;
	
	protected float[] pos;
	
	public abstract void update(long dt);
	
	public Entity(int owner, String name, float posX, float posY) {
		this.owner = owner;
		this.name = name;
		
		pos = new float[2];
		pos[0] = posX;
		pos[1] = posY;
	}
	
	public int getOwner() {
		return owner;
	}
	
	public void setOwner(int owner) {
		this.owner = owner;
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
