package entity;

import main.Server;
import map.Map;

public class Worker extends Unit{
	
	private static final int COST = 10;

	private Part body;
	
	private final int FACTORY = 0;
	
	private String action;
	private int actionTimeRemaining;
	
	private final int[] buildTime = {100}; // à augmenter
	private final int[] buildCost = {10};
	
	private int buildType;
	private String buildName;
	
	private final int GATHERTIME = 1000;
	
	public Worker (int owner, String name, float posX, float posY, float rotation) {
		super(owner, name, posX, posY, 1);
		
		body = new Part(this, rotation);
		parts[0] = body;
		
		SPEEDMOVE = 64;
		body.SPEEDROTATE = 45;
		
		action = "";
	}
	
	public static int getCost () {
		return COST;
	}

	@Override
	public void update(long dt) {
		if(action.equals("build")) {
			
			if(actionTimeRemaining > 0) {
				actionTimeRemaining -= dt;
			}else {
				actionTimeRemaining = 0;
				if(buildType == FACTORY) {
					Server.createFactory(owner, buildName, pos[0], pos[1]);
				}else {
					//rien pour le moment
				}
				action = "";
			}
		
		}else if(action.equals("gather")){
			if(actionTimeRemaining > 0) {
				actionTimeRemaining -= dt;
			}else {
				actionTimeRemaining = 0;
				Server.addMoney(1);
				gather();
			}
		}else {
		
			super.update(dt);
		}
		
	}

	public boolean canBuild(String structure) {
		return structure.equals("factory") && Server.getPlayers().get(owner).sufficientMoney(Factory.getCost());
	}
	
	public void build (String structure, String structName) {
		if(structure.equals("factory")) {
			buildType = FACTORY;
			Server.removeMoney(Factory.getCost());
		}else {
			//rien pour le moment
		}
		
		buildName = structName;
		actionTimeRemaining = buildTime[buildType];
		
		action = "build";
	}
	
	public boolean canGather () {
		return Map.getRessourceType( (int)(pos[0]/64 - 224/64), (int)(pos[1]/64)).equals("crystal");
	}
	
	public void gather () {
		action = "gather";
		actionTimeRemaining = GATHERTIME;
	}

}
