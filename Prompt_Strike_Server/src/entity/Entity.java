package entity;

/** * The entities is all element that players can order */
public abstract class Entity {
	
	/** The player ID who owns the entity */
	protected int owner;
	
	/** The name of the entity */
	protected String name;
	/** The Health Points of the entity */
	private int HP;
	
	/** The 2D coordinate position of entity in pixel */
	protected float[] pos;
	
	/**
     * Create an Entity
     * 
     * @param owner
     * 				The player ID who owns the entity
     * @param name
     * 				The entity's name
     * @param posX
     * 				The X position of the entity
     * @param posY
     * 				The Y position of the entity
     */
	public Entity(int owner, String name, float posX, float posY) {
		this.owner = owner;
		this.name = name;
		
		pos = new float[2];
		pos[0] = posX;
		pos[1] = posY;
	}
	
	/**
     * Updates the state of entity each game loop
     * 
     * @param dt
     * 				The delta time
     */
	public abstract void update(long dt);
	
	public int getOwner() {
		return owner;
	}
	
	/**
     * Changes the owner of the entity
     * 
     * @param owner
     * 				The new player ID who owns the entity
     */
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
	
	/**
     * add or remove Health Points
     * 
     * @param amount
     * 				The adding HP value, can be positive or negative
     */
	public void changeHP(int amount) {
		HP += amount;
	}
}
