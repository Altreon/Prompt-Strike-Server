package entity;

/** 
 * The structure is an subtype of entity, It use by player to produce units on map
 * 
 * @see Entity
 * @see Unit
 */
public abstract class Structure extends Entity{
	
	/**
     * Create a Structure
     * 
     * @param owner
     * 				The player ID who owns the structure
     * @param name
     * 				The structure's name
     * @param posX
     * 				The X position of the structure
     * @param posY
     * 				The Y position of the structure
     */
	public Structure (int owner, String name, float posX, float posY) {
		super(owner, name, posX, posY);		
	}
	
	
	/* Nowadays, the next functions and product attributes could be generalized here,
	   but in the future, all structures cannot be able to create units */
	
	/** 
	 * @param productType
     * 				The type's name of unit
	 * @return true if the factory can produce a type of unit
	 * */
	public abstract boolean canProduce(String productType);
	/** 
	 * Produce a unit on the map
	 * 
	 * @param productType
     * 				The type's name of unit
	 * @param productName
     * 				The unit's name
	 * */
	public abstract void produce(String productType, String productName);
}
