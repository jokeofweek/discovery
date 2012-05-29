package com.jokeofweek.data.map;

import com.jokeofweek.lib.util.RNG;

public class MapGenerator {

	public static final int[] VALID_SIZES = { 17, 35, 65, 129, 257, 513, 1025, 2049,
			4097 };
	
	private static int MAP_HEIGHT;
	private static int MAP_WIDTH;
	private static Tile[][] data = null;

	/**
	 * This function generates a {@link Map} object of a given size, including
	 * all the tiles for the map.
	 * 
	 * @param size
	 *            The size of the map. The map will be a square map and will be
	 *            of size (size x size). <b>Note that this must be an integer
	 *            which is a power of 2 +1, so (2 ^ x) + 1 = size</b>
	 * @return The Map object.
	 */
	public static Map generate(int size){
		boolean found = false;
		
		for (int i = 0; i < VALID_SIZES.length; i++){
			if (VALID_SIZES[i] == size){
				found = true;
				break;
			}
		}
		
		if (!found){
			throw new IllegalArgumentException("Invalid map size. Must be (2 ^ size) + 1");
		}
		
		data = new Tile[size][size];
	
		// Initialize corners to random values
		Tile tile = new Tile();
		tile.setTemperature((byte) ((RNG.getInstance().nextInt(3) * 127) - 127));
		tile.setPrecipitation((byte) ((RNG.getInstance().nextInt(3) * 127) - 127));
		tile.updateBiome();
		data[0][0] = tile;
		
		tile = new Tile();
		tile.setTemperature((byte) ((RNG.getInstance().nextInt(3) * 127) - 127));
		tile.setPrecipitation((byte) ((RNG.getInstance().nextInt(3) * 127) - 127));
		tile.updateBiome();
		data[0][size-1] = tile;
		
		tile = new Tile();
		tile.setTemperature((byte) ((RNG.getInstance().nextInt(3) * 127) - 127));
		tile.setPrecipitation((byte) ((RNG.getInstance().nextInt(3) * 127) - 127));
		tile.updateBiome();
		data[size-1][0] = tile;
		
		tile = new Tile();
		tile.setTemperature((byte) ((RNG.getInstance().nextInt(3) * 127) - 127));
		tile.setPrecipitation((byte) ((RNG.getInstance().nextInt(3) * 127) - 127));
		tile.updateBiome();
		data[size-1][size-1] = tile;
		
		int length = size - 1;
		int range = 255;
		MAP_HEIGHT = MAP_WIDTH = size;
		
		while (length > 0){

			diamond(length, range);
			square(length, range);
			
			range /= 2;
			if (range == 0) range = 1;
			length >>= 1;
		}
		
		return new Map(data);
	}

	private static void diamond(int length, int range){
		int part = length >> 1;
		int yPart;
		int halfRange = range>>1;
		
		for (int y = 0; y < MAP_HEIGHT - 1; y += length){
			yPart = y + part;
			for (int x = 0; x < MAP_WIDTH - 1; x += length){
				Tile tile = new Tile();
				tile.setTemperature(clampByte(((data[x][y].getTemperature() +
												data[x][y + length].getTemperature() +
												data[x + length][y].getTemperature() +
												data[x + length][y + length].getTemperature())
												/ 4)
												+ RNG.getInstance().nextInt(range) - halfRange));

				tile.setPrecipitation(clampByte(((data[x][y].getPrecipitation() +
												data[x][y + length].getPrecipitation() +
												data[x + length][y].getPrecipitation() +
												data[x + length][y + length].getPrecipitation())
												/ 4)
												+ RNG.getInstance().nextInt(range) - halfRange));
				tile.updateBiome();
				data[x + part][yPart] = tile;
			}
		}
	}
	
	private static void square(int length, int range){
		int part = length >> 1;
		int x1, y1;
		
		for (int y = 0; y < MAP_HEIGHT - 1; y += length){
			y1 = y + part;
			for (int x = 0; x < MAP_WIDTH - 1; x += length){
				x1 = x + part;
				
				doSquare(x1, y, part, range);
				doSquare(x, y1, part, range);
				doSquare(x1, y1 + part, part, range);
				doSquare(x1 + part, y1, part, range);
				
			}
		}
	}

	private static void doSquare(int x, int y, int part, int range){
		int count=0;
		int totalT=0;
		int totalP=0;
		
		if ((x - part) >= 0){
			totalT += data[x-part][y].getTemperature();
			totalP += data[x-part][y].getPrecipitation();
			count++;
		}
		if ((y - part) >= 0){
			totalT += data[x][y-part].getTemperature();
			totalP += data[x][y-part].getPrecipitation();
			count++;
		}
		if (y + part < MAP_HEIGHT){
			totalT += data[x][y+part].getTemperature();
			totalP += data[x][y+part].getPrecipitation();
			count++;
		}
		if (x + part < MAP_WIDTH){
			totalT += data[x+part][y].getTemperature();
			totalP += data[x+part][y].getPrecipitation();
			count++;
		}
		
		Tile tile = new Tile();
		tile.setTemperature(clampByte((totalT / count) + RNG.getInstance().nextInt(range) - (range >> 1)));
		tile.setPrecipitation(clampByte((totalP / count) + RNG.getInstance().nextInt(range) - (range >> 1)));
		tile.updateBiome();
		data[x][y] = tile;
	}
	
	/**
	 * This clamps an integer value to the bounds of a byte, and returns it
	 * as a variable of type byte.
	 * @param b
	 * 		The integer which contains the value to be clamped.
	 * @return
	 * 		The byte representation of the integer
	 */
	private static byte clampByte(int b) {
		if (b > 127)
			return (byte) 127;
		if (b < -128)
			return (byte) -128;
		return (byte) b;
	}

}
