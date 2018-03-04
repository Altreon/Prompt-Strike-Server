package map;

/** * It is the storage of all map's data */
public class Map {
	
	/** * The X number of Tile on the map */
	private static final int MAP_SIZE_X = 13;
	/** * The Y number of Tile on the map */
	private static final int MAP_SIZE_Y = 11;
	
	/** * The Tile size in pixel */
	private static final int TILE_SIZE = 64;
	
	/** * The table of tiles on the map */
	private static Tile[][] map;
	/** * The table of resources on the map */
	private static Ressource[][] ressources;
	
	/** * Create the map */
	public static void initialization() {
		map = new Tile[MAP_SIZE_X][MAP_SIZE_Y];
		
		//fill all map with grass tiles
		for (int i=0; i < map.length; i++) {
			for (int j=0; j < map[i].length; j++) {
				map[i][j] = new Tile("grass");
			}
		}
		
		ressources = new Ressource[MAP_SIZE_X][MAP_SIZE_Y];
		
		//add two crystal at specific position
		ressources[1][1] = new Ressource("crystal");
		ressources[11][9] = new Ressource("crystal");
		
	}
	
	/**
     * @param posX
     * 				the X coordinate of the tile
     * @param posY
     * 				the Y coordinate of the tile
     * @return The type of resources at a specific tile
     */
	public static String getRessourceType(int posX, int posY) {
		if(ressources[posX][posY] != null) {
			return ressources[posX][posY].getType();
		}else {
			return "nothing";
		}
	}
	
	public static int getMapSizeX () {
		return MAP_SIZE_X;
	}
	
	public static int getMapSizeY () {
		return MAP_SIZE_Y;
	}
	
	public static int getTileSize () {
		return TILE_SIZE;
	}
}
