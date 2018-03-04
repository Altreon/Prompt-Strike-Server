package entity;

import Server.Server;

/** 
 * The Headquarter is the base of your army, Destroy the oppenent's one to win the game.
 * It is also the building that builds offensive units on the map
 * 
 * @see Structure
 * @see Worker
 * */
public class Headquarter extends Structure{
		
	/** * The list of units production's time */
	private final long[] prodTime = {(long) (2*1.0E9D)};
	/** * The worker id on list of units production's time */
	private final int WORKER = 0;
	
	/** * if the Headquarter is building something */
	private boolean produce;
	/** * The current unit under construction */
	private Product product;
	
	
	/**
     * Create a Headquarter
     * 
     * @param owner
     * 				The player ID who owns the factory
     * @param name
     * 				The headquarter's name
     * @param posX
     * 				The X position of the headquarter
     * @param posY
     * 				The Y position of the headquarter
     */
	public Headquarter(int owner, String name, float posX, float posY) {
		super(owner, name, posX, posY);
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
				if(product.getType() == WORKER) {
					Server.createWorker(owner, product.getName(), pos[0], pos[1]);
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
			if(productType.equals("worker")) {
				return Server.getPlayers().get(owner).sufficientMoney(Worker.getCost());
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
		if(productType.equals("worker")) {
			product = new Product(WORKER, productName, prodTime[WORKER]);
			Server.removeMoney(owner, Worker.getCost());
		}
		
		produce = true;
	}

}
