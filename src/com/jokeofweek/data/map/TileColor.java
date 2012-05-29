package com.jokeofweek.data.map;

import java.util.HashMap;

import com.jokeofweek.lib.CSIColor;

public class TileColor {

	private static TileColor instance = new TileColor();
	
	private HashMap<BiomeSubtype, CSIColor> colors = new HashMap<BiomeSubtype, CSIColor>();
	
	public TileColor(){
		// Fixed colors
		colors.put(BiomeSubtype.REGULAR, CSIColor.BLACK);
		
		colors.put(BiomeSubtype.SNOW, new CSIColor(250, 250, 250));
		
		colors.put(BiomeSubtype.BEACH, new CSIColor(238, 214, 175));
		
		colors.put(BiomeSubtype.SAVANNAH, new CSIColor(193, 154, 107));
		
		colors.put(BiomeSubtype.DESERT, new CSIColor(238, 214, 175));
		
		colors.put(BiomeSubtype.SWAMP, new CSIColor(131,138,70));
		
		colors.put(BiomeSubtype.RAIN_FOREST, new CSIColor(59, 122, 87));

		colors.put(BiomeSubtype.TUNDRA1, new CSIColor(129, 180, 150));
		colors.put(BiomeSubtype.TUNDRA2, new CSIColor(162, 200, 176));
		colors.put(BiomeSubtype.TUNDRA3, new CSIColor(202, 223, 210));
		
		colors.put(BiomeSubtype.FOREST1, new CSIColor(116, 166, 80));
		colors.put(BiomeSubtype.FOREST2, new CSIColor(92, 176, 39));
		colors.put(BiomeSubtype.FOREST3, new CSIColor(75, 181, 23));
		colors.put(BiomeSubtype.FOREST4, new CSIColor(50, 187, 9));
		
		colors.put(BiomeSubtype.OCEAN_SHALLOW, new CSIColor(120, 122, 205));
		colors.put(BiomeSubtype.OCEAN_WATER, new CSIColor(80, 83, 190));
		colors.put(BiomeSubtype.OCEAN_DEEP2, new CSIColor(63, 66, 169));
		colors.put(BiomeSubtype.OCEAN_DEEP1, new CSIColor(51, 53, 136));
		colors.put(BiomeSubtype.OCEAN_DEEP3, new CSIColor(37, 39, 101));
		
	}
	
	public CSIColor getColor(BiomeSubtype type){
		return this.colors.get(type);
	}
	
	public static CSIColor get(BiomeSubtype type){
		return instance.getColor(type);
	}
	
}
