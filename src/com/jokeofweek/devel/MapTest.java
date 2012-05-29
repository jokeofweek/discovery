package com.jokeofweek.devel;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JWindow;

import com.jokeofweek.data.map.Map;
import com.jokeofweek.data.map.MapGenerator;
import com.jokeofweek.data.map.Tile;
import com.jokeofweek.lib.util.RNG;

public class MapTest extends JFrame {

	private static final long serialVersionUID = -3439859531587935393L;
	static final int MAP_WIDTH = 513;
	static final int MAP_HEIGHT = 513;
	static byte[][] map = new byte[MAP_HEIGHT][MAP_WIDTH];
	
	public MapTest(){
		Map map = MapGenerator.generate(513);
		Tile[][] tiles = map.getTiles();
		
		this.setTitle("Blarg");
		this.setSize(500, 500);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
		
		Graphics g = this.getGraphics();
		
		for (int y = 0; y < MAP_HEIGHT; y++)
			for (int x = 0; x < MAP_WIDTH; x++){
				
				switch(tiles[y][x].getBiome()){
				case OCEAN:
					g.setColor(new Color(0, 0, 255 - ((tiles[y][x].getPrecipitation() - 80) * 2)));
					break;
				default:
					g.setColor(new Color(tiles[y][x].getTemperature() + 128, tiles[y][x].getTemperature() + 128, tiles[y][x].getTemperature() + 128));
					break;
					

				}
				g.fillRect(x, y, 1, 1);
			}
	}
	
	public static void main(String... args){
		/*map[0][0] = (byte) (RNG.getInstance().nextInt(255) - 128);
		map[MAP_HEIGHT - 1][0] = (byte) (RNG.getInstance().nextInt(255) - 128);
		map[0][MAP_WIDTH - 1] = (byte) (RNG.getInstance().nextInt(255) - 128);
		map[MAP_HEIGHT - 1][MAP_WIDTH - 1] = (byte) (RNG.getInstance().nextInt(255) - 128);
		
		int length = MAP_WIDTH - 1;
		int range = 400;
		
		while (length > 0){

			diamond(length, range);
			square(length, range);
			
			range /= 2;
			if (range == 0) range = 1;
			length >>= 1;
		}*/
		
		new MapTest();
		
	}
	
	public static void diamond(int length, int range, byte[][] map){
		int part = length >> 1;
		int yPart;
		int halfRange = range>>1;
		
		for (int y = 0; y < MAP_HEIGHT - 1; y += length){
			yPart = y + part;
			for (int x = 0; x < MAP_WIDTH - 1; x += length){
				map[yPart][x + part] = clamp(((map[y][x] +
											  map[y + length][x] +
											  map[y][x + length] +
											  map[y + length][x + length])
											  / 4)
											  + RNG.getInstance().nextInt(range) - halfRange);
			}
		}
	}
	
	public static void square(int length, int range){
		int part = length >> 1;
		int x1, y1;
		
		for (int y = 0; y < MAP_HEIGHT - 1; y += length){
			for (int x = 0; x < MAP_WIDTH - 1; x += length){
				x1 = x + part;
				y1 = y + part;
				
				doSquare(x1, y, part, range);
				doSquare(x, y1, part, range);
				doSquare(x1, y1 + part, part, range);
				doSquare(x1 + part, y1, part, range);
				
			}
		}
	}

	public static void doSquare(int x, int y, int part, int range){
		int count=0;
		int total=0;
		
		if ((x - part) >= 0){
			total += map[y][x-part];
			count++;
		}
		if ((y - part) >= 0){
			total += map[y-part][x];
			count++;
		}
		if (y + part < MAP_HEIGHT){
			total += map[y+part][x];
			count++;
		}
		if (x + part < MAP_WIDTH){
			total += map[y][x+part];
			count++;
		}
		
		map[y][x] = clamp((total / count) + RNG.getInstance().nextInt(range) - (range >> 1));
	}
	
	public static byte clamp(int b){
		if (b > 127) return (byte)127;
		if (b < -128) return (byte)-128;
		return (byte) b;
	}
	
}
