package entity;

public abstract class Structure extends Entity{
	
	public Structure (String name, float posX, float posY) {
		super(name, posX * 64, posY * 64);		
	}
}
