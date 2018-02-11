package entity;

public abstract class Structure extends Entity{
	
	public Structure (int owner, String name, float posX, float posY) {
		super(owner, name, posX, posY);		
	}
	
	public abstract boolean canProduce(String product);
	public abstract void produce(String product, String productName);
}
