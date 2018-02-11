package entity;

import main.Server;

public class Headquarter extends Structure{
	
	private static final int COST = 50;
	
	private final int WORKER = 0;
	
	private final long[] prodTime = {(long) (2*1.0E9D)};
	
	private boolean produce;
	private long prodTimeRemaining;
	private int prodType;
	private String prodName;
	
	public Headquarter(int owner, String name, float posX, float posY) {
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
				if(prodType == WORKER) {
					Server.createWorker(owner, prodName, pos[0], pos[1]);
				}
				produce = false;
			}
			
		}
	}
	
	@Override
	public boolean canProduce (String product) {
		if(produce) {
			return false;
		}else {
			if(product.equals("worker")) {
				return Server.getPlayers().get(owner).sufficientMoney(Worker.getCost());
			}else {
				return false;
			}
			
		}
	}
	
	@Override
	public void produce (String product, String productName) {
		if(product.equals("worker")) {
			prodType = WORKER;
			Server.removeMoney(owner, Worker.getCost());
		}
		
		prodName = productName;
		prodTimeRemaining = prodTime[prodType];
		produce = true;
	}

}
