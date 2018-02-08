package entity;

public class Chassis extends Part{
	
	//private Part attachPart;
	
	public Chassis (Unit owner, float rotation) {
		super(owner, rotation);
		
		SPEEDROTATE = 45;
		
		//this.attachPart = attachPart;
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
