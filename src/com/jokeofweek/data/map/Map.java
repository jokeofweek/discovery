package com.jokeofweek.data.map;

public class Map {
	
	public static final int MAP_SIZE = 513;
	
	private int size;
	private Tile[][] tiles;
	
	public Map(Tile[][] tiles){
		this.tiles = tiles;
		this.setSize(this.tiles.length);
	}

	public Tile[][] getTiles() {
		return tiles;
	}

	public void setTiles(Tile[][] tiles) {
		this.tiles = tiles;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	public Tile getTile(int x, int y) {
		return this.tiles[x][y];
	}
	
	public boolean isTileWalkable(int x, int y){
		return !this.tiles[x][y].checkFlag(Tile.ATTRIBUTE_BLOCK);
	}
}
