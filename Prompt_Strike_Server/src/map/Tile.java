package map;

/** 
 * Tiles is the case whose are composed the map
 * 
 * @see Map
 */
public class Tile {
	
	/** * The type of the tile */
	private String type;
	
	/**
     * Create a Tile
     * 
     * @param type
     * 				The tile's type
     */
	public Tile (String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
}
