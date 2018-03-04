package entity;

import Server.Server;

/** 
 * The Factory is the building that builds offensive units on the map
 * 
 * @see Structure
 * @see Tank
 * */
public class Factory extends Structure{
	
	/** * The construction cost for the players */
	private static final int COST = 50;
	
	/** * The list of units production's time */
	private final long[] prodTime = {(long) (5*1.0E9D)};
	/** * The tank id on list of units production's time */
	private final int TANK = 0;
	
	/** * if the Factory is building something */
	private boolean produce;
	/** * The current unit under construction */
	private Product product;
	
	/**
     * Create a Factory
     * 
     * @param owner
     * 				The player ID who owns the factory
     * @param name
     * 				The factory's name
     * @param posX
     * 				The X position of the factory
     * @param posY
     * 				The Y position of the factory
     */
	public Factory(int owner, String name, float posX, float posY) {
		super(owner, name, posX, posY);
	}
	
	public static int getCost () {
		return COST;
	}

	/**
     * Updates the production state of factory each game loop
     * 
     * @param dt
     * 				The delta time
     */
	@Override
	public void update(long dt) {
		if(produce) {
			product.updateTimeRemaining(dt);
			if(product.isFinish()) {
				if(product.getType() == TANK) {
					Server.createTank(owner, product.getName(), pos[0], pos[1]);
				}
				produce = false;
			}
			
		}
	}
	
	/** 
	 * @param productType
     * 				The type's name of unit
	 * @return true if the factory can produce a type of unit
	 * */
	@Override
	public boolean canProduce (String productType) {
		if(produce) {
			return false;
		}else {
			if(productType.equals("tank")) {
				return Server.getPlayers().get(owner).sufficientMoney(Tank.getCost());
			}else {
				return false;
			}
			
		}
	}
	
	/** 
	 * Produce a unit on the map
	 * 
	 * @param productType
     * 				The type's name of unit
	 * @param productName
     * 				The unit's name
	 * */
	@Override
	public void produce (String productType, String productName) {
		if(productType.equals("tank")) {
			product = new Product(TANK, productName, prodTime[TANK]);
			Server.removeMoney(owner, Tank.getCost());
		}
		
		produce = true;
	}

}
