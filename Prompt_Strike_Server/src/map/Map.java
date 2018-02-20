package map;

public class Map {
	
	static Tile[][] map;
	static Ressource[][] ressources;
	
	public Map() {
		map = new Tile[13][11];
		for (int i=0; i < map.length; i++) {
			for (int j=0; j < map[0].length; j++) {
				map[i][j] = new Tile("grass");
			}
		}
		
		ressources = new Ressource[13][11];
		ressources[0][0] = new Ressource("crystal");
		ressources[10][8] = new Ressource("crystal");
		
	}
	
	static public String getRessourceType(int posX, int posY) {
		if(ressources[posX][posY] != null) {
			return ressources[posX][posY].getType();
		}else {
			return "";
		}
	}

}
