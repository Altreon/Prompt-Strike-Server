package entity;

import main.Server;

public class Factory extends Structure{
	
	private static final int COST = 50;
	
	private final int TANK = 0;
	private final int WORKER = 1;
	
	private final int[] prodTime = {10000,1000};
	private final int[] prodCost = {10,2};
	
	private long prodTimeStart;
	private long prodTimeRemaining;
	private int prodType;
	private String prodName;
	
	public Factory(String name, float posX, float posY) {
		super(name, posX, posY);
		prodTimeRemaining = -1;
	}
	
	public static int getCost () {
		return COST;
	}

	@Override
	public void update(int dt) {
		if(prodTimeRemaining >= 0) {
			prodTimeRemaining = System.currentTimeMillis() - prodTimeStart;
			if(prodTimeRemaining >= prodTime[prodType]) {
				if(prodType == TANK) {
					Server.createTank(prodName, pos[0], pos[1]);
				}else {
					Server.createWorker(prodName, pos[0], pos[1]);
				}
				prodTimeRemaining = -1;
			}
		}
	}
	
	public boolean canProduce (String product) {
		if(prodTimeRemaining != -1) {
			return false;
		}else {
			if(product.equals("tank")) {
				return Server.getPlayers().get(0).sufficientMoney(Tank.getCost());
			}else if(product.equals("worker")) {
				return Server.getPlayers().get(0).sufficientMoney(Worker.getCost());
			}else {
				return false;
			}
			
		}
	}
	
	public void produce (String product, String productName) {
		if(product.equals("tank")) {
			prodType = TANK;
			Server.removeMoney(Tank.getCost());
		}else {
			prodType = WORKER;
			Server.removeMoney(Worker.getCost());
		}
		
		prodName = productName;
		prodTimeStart = System.currentTimeMillis();
		prodTimeRemaining = prodTime[prodType];
	}

}
