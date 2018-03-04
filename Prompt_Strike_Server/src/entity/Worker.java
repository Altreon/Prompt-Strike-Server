package entity;

import Server.Server;
import map.Map;

/** 
 * The worker is the only support unit for now.
 * It is making in headquarter and can construct factory and gather money on crystal
 * 
 * @see Headquarter
 * @see Factory
 */
public class Worker extends Unit{
	
	/** * The construction cost for the players */
	private static final int COST = 10;
	/** * The worker's move speed in pixels per seconds */
	private static final float SPEED_MOVE = Map.getTileSize();
	private final float BODY_SPEED_ROTATE = 45;

	/** * The main part of the worker */
	private Part body;
	
	/** * The current worker's action */
	private String action;
	
	/** * The time for worker to gather one value of money */
	private final long GATHER_TIME = (long) (1*1.0E9D);
	/** * The time remaining before the gathering was done */
	private long gatherTimeRemaining;
	
	/** * The list of structures production's time */
	private final long[] prodTime = {(long) (5*1.0E9D)};
	/** * The factory id on list of structures production's time */
	private final int FACTORY = 0;
	
	/** * The current structure under construction */
	private Product product;
	
	/**
     * Create a Worker
     * 
     * @param owner
     * 				The player ID who owns the worker
     * @param name
     * 				The worker's name
     * @param posX
     * 				The X position of the worker
     * @param posY
     * 				The Y position of the worker
     * @param rotation
     * 				The work's initial rotation
     */
	public Worker (int owner, String name, float posX, float posY, float rotation) {
		super(owner, name, posX, posY, SPEED_MOVE);
		
		parts = new Part[1];
		body = new Part(this, rotation, BODY_SPEED_ROTATE);
		parts[0] = body;
		
		action = ""; // "" mean nothing
	}
	
	public static int getCost () {
		return COST;
	}

	/**
     * Updates the current action of the worker each game loop
     * 
     * @param dt
     * 				The delta time
     */
	@Override
	public void update(long dt) {
		if(action.equals("build")) {
			
			product.updateTimeRemaining(dt);
			if(product.isFinish()) {
				if(product.getType() == FACTORY) {
					Server.createFactory(owner, product.getName(), pos[0], pos[1]);
				}else {
					//nothing for now
				}
				action = ""; // "" mean nothing
			}
		
		}else if(action.equals("gather")){
			if(gatherTimeRemaining > 0) {
				gatherTimeRemaining -= dt;
			}else {
				Server.addMoney(owner, 1);
				gather();
			}
		}else { // moving
		
			super.update(dt);
		}
		
	}

	/** 
	 * @param structType
     * 				The type's name of structure
	 * @return true if the worker can build a type of structure
	 * */
	public boolean canBuild(String structType) {
		return structType.equals("factory") && Server.getPlayers().get(owner).sufficientMoney(Factory.getCost());
	}
	
	/** 
	 * Build a structure on the map
	 * 
	 * @param structType
     * 				The type's name of structure
	 * @param structName
     * 				The structure's name
	 * */
	public void build (String structType, String structName) {
		if(structType.equals("factory")) {
			product = new Product(FACTORY, structName, prodTime[FACTORY]);
			Server.removeMoney(owner, Factory.getCost());
		}else {
			//nothing for now
		}
		
		action = "build";
	}
	
	/** * @return True if the worker is on a crystal */
	public boolean canGather () {
		return Map.getRessourceType( (int)(pos[0]/Map.getTileSize()), (int)(pos[1]/Map.getTileSize())).equals("crystal"); //is he approximately on crystal
	}
	
	/** * Starts unit to gather */
	public void gather () {
		action = "gather";
		gatherTimeRemaining = GATHER_TIME;
	}
	
	/** 
	 * Moves the worker on the map on their current orientation
	 * 
	 * @param distance
     * 				The distance to reach
	 * */
	@Override
	public void move(int distance) {
		super.move(distance);
		action = "move";
	}

	/** 
	 * Move the worker on the map to a relative position
	 * 
	 * @param x
     * 				The relative X coordinate
     * @param y
     * 				The relative Y coordinate
	 * */
	@Override
	public void move(int x, int y) {
		super.move(x, y);
		action = "move";
		
	}

}
