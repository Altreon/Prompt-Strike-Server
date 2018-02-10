package entity;

import main.Server;
import map.Map;

public class Worker extends Unit{
	
	private static final int COST = 10;

	private Part body;
	
	private final int FACTORY = 0;
	
	private String action;
	private long actionTimeRemaining;
	
	private final long[] buildTime = {(long) (5*1.0E9D)}; // à augmenter
	
	private int buildType;
	private String buildName;
	
	private final long GATHERTIME = (long) (1*1.0E9D);
	
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
				Server.addMoney(owner, 1);
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
			Server.removeMoney(owner, Factory.getCost());
		}else {
			//rien pour le moment
		}
		
		buildName = structName;
		actionTimeRemaining = buildTime[buildType];
		
		action = "build";
	}
	
	public boolean canGather () {
		return Map.getRessourceType( (int)(pos[0]/64), (int)(pos[1]/64)).equals("crystal");
	}
	
	public void gather () {
		action = "gather";
		actionTimeRemaining = GATHERTIME;
	}

}
