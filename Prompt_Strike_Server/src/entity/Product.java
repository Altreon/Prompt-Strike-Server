package entity;

/** 
 * This class is used to store product's data about an entity under construction
 * 
 * @see Entity
 * */
public class Product {
	
	/** * The time remaining before the construction is completed */
	private long prodTimeRemaining;
	
	/** * The type ID of the entity (depends on the constructor)  */
	private int prodType;
	/** *  The name of the entity */
	private String prodName;
	
	/**
     * Create a Product
     * 
     * @param prodType
     * 				The type ID of the entity (depends on the constructor)
     * @param prodName
     * 				The entity's name
     * @param prodTime
     * 				The time of the construction
     */
	public Product (int prodType, String prodName, long prodTime) {
		this.prodType = prodType;
		this.prodName = prodName;
		prodTimeRemaining = prodTime;
	}
	
	public int getType() {
		return prodType;
	}
	
	public String getName() {
		return prodName;
	}

	/**
     * Updates time remaining before the construction is completed
     * 
     * @param dt
     * 				The delta time
     */
	public void updateTimeRemaining(long dt) {
		prodTimeRemaining -= dt;
	}

	/** * @return true if the construction is finished */
	public boolean isFinish() {
		return prodTimeRemaining <= 0;
	}
}
