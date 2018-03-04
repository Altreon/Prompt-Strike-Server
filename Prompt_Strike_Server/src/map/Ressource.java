package map;

/** 
 * Ressource is a special Tile that player can gather
 * 
 * @see Tile
 */
public class Ressource extends Tile{
	
	/** * The number of money the resource store on it */
	public int amount; //Infinite for now

	/**
     * Create a Ressource
     * 
     * @param type
     * 				The resource's type
     */
	public Ressource(String type) {
		super(type);
	}

}
