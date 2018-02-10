package entity;

import main.Server;

public class Factory extends Structure{
	
	private static final int COST = 50;
	
	private final int TANK = 0;
	private final int WORKER = 1;
	
	private final long[] prodTime = {(long) (5*1.0E9D), (long) (2*1.0E9D)};
	
	//private long prodTimeStart;
	private boolean produce;
	private long prodTimeRemaining;
	private int prodType;
	private String prodName;
	
	public Factory(int owner, String name, float posX, float posY) {
		super(owner, name, posX, posY);
	}
	
	public static int getCost () {
		return COST;
	}

	@Override
	public void update(long dt) {
		if(produce) {
			if(prodTimeRemaining > 0) {
				prodTimeRemaining -= dt;
			}else {
				if(prodType == TANK) {
					Server.createTank(owner, prodName, pos[0], pos[1]);
				}else {
					Server.createWorker(owner, prodName, pos[0], pos[1]);
				}
				produce = false;
			}
			
		}
	}
	
	public boolean canProduce (String product) {
		if(produce) {
			return false;
		}else {
			if(product.equals("tank")) {
				return Server.getPlayers().get(owner).sufficientMoney(Tank.getCost());
			}else if(product.equals("worker")) {
				return Server.getPlayers().get(owner).sufficientMoney(Worker.getCost());
			}else {
				return false;
			}
			
		}
	}
	
	public void produce (String product, String productName) {
		if(product.equals("tank")) {
			prodType = TANK;
			Server.removeMoney(owner, Tank.getCost());
		}else {
			prodType = WORKER;
			Server.removeMoney(owner, Worker.getCost());
		}
		
		prodName = productName;
		prodTimeRemaining = prodTime[prodType];
		produce = true;
	}

}
