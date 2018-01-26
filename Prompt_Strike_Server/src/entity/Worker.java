package entity;

import main.Server;
import map.Map;

public class Worker extends Unit{
	
	private static final int COST = 10;

	private Part sprite;
	
	private final int FACTORY = 0;
	
	private String action;
	private int actionTimeRemaining;
	
	private final int[] buildTime = {100}; // à augmenter
	private final int[] buildCost = {10};
	
	private int buildType;
	private String buildName;
	
	private final int GATHERTIME = 1000;
	
	public Worker (String name, float posX, float posY, float rotation) {
		super(name, posX, posY, rotation);
		
		sprite = new Part(posX, posY, rotation);
		
		sprite.SPEEDMOVE = 64;
		sprite.SPEEDROTATE = 5;
		
		action = "";
	}
	
	public static int getCost () {
		return COST;
	}
	
	@Override
	public float[] getPos() {
		return sprite.pos;
	}
	
	@Override
	public float getRotation() {
		return sprite.rotation;
	}

	@Override
	public void update(long dt) {
		if(action.equals("build")) {
			
			if(actionTimeRemaining > 0) {
				actionTimeRemaining -= dt;
			}else {
				actionTimeRemaining = 0;
				if(buildType == FACTORY) {
					Server.createFactory(buildName, (int)(pos[0]/64 - 224/64), (int)(pos[1]/64));
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
				Server.addMoney(1);;
				gather();
			}
		}else {
		
			if(sprite.isMoving()){
				sprite.updateMove(dt);
			}
			if(sprite.isRotating()){
				sprite.updateRotate(dt);
			}
		
			if(!sprite.isRotating() && waitMoveDistance != 0){
				move(waitMoveDistance);
				waitMoveDistance = 0;
			}
		}
		
	}

	@Override
	public void move(int distance) {
		if(distance > 0) {
			sprite.moveDistance = distance*64;
			sprite.moveDirection = 1;
		}else {
			sprite.moveDistance = -distance*64;
			sprite.moveDirection = -1;
		}
		action = "";
	}

	@Override
	public void rotate(int distance) {
		if(distance > 0) {
			sprite.rotateDistance = distance;
			sprite.rotateDirection = 1;
		}else {
			sprite.rotateDistance = -distance;
			sprite.rotateDirection = -1;
		}
		action = "";
	}

	public boolean canBuild(String structure) {
		return structure.equals("factory") && Server.getPlayers().get(0).sufficientMoney(Factory.getCost());
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

	@Override
	public boolean isMoving() {
		return sprite.isMoving();
	}

	@Override
	public boolean isRotating() {
		return sprite.isRotating();
	}

}
