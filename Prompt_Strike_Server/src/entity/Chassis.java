package entity;

public class Chassis extends Part{
	
	private Part attachPart;
	
	public Chassis (Part attachPart, float posX, float posY, float rotation) {
		super(posX, posY, rotation);
		
		SPEEDMOVE = 1;
		SPEEDROTATE = 1;
		
		this.attachPart = attachPart;
	}
	
	//@Override
	//public void updateMove () {
		
		//float moveX = (float) (pos[0] + SPEEDMOVE * moveDirection * Math.cos(rotation));
		//float moveY = (float) (pos[1] + SPEEDMOVE * moveDirection * Math.sin(rotation));
		//moveDistance -= (float) (dist(pos, newPos));
		//attachPart.setX(moveX);
		//attachPart.setY(moveY);
		//setX(moveX);
		//setY(moveY);
		//if(moveDistance <= 0) {
			//moveDistance = 0;
		//}
	//}
}
