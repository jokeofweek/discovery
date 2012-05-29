package com.jokeofweek.data.map;

import com.jokeofweek.data.map.resource.*;
import com.jokeofweek.lib.CSIColor;
import com.jokeofweek.lib.entity.Renderable;
import com.jokeofweek.lib.util.RNG;

public class Tile {

	/*
	 * Tile attribute flags
	 */
	public static final int ATTRIBUTE_BLOCK = 1 << 0;
	public static final int ATTRIBUTE_BLOCKSIGHT = 1 << 1;
	public static final int ATTRIBUTE_INSIGHT = 1 << 2;
	
	private byte temperature;
	private byte precipitation;
	private Biome biome;
	private CSIColor background;
	private int flags;
	private BiomeSubtype biomeSubtype;
	private Resource resource = Resource.EMPTY;
	private Renderable renderable = Resource.EMPTY.getRenderable();

	public byte getTemperature() {
		return temperature;
	}

	public void setTemperature(byte temperature) {
		this.temperature = temperature;
	}

	public byte getPrecipitation() {
		return precipitation;
	}

	public void setPrecipitation(byte precipitation) {
		this.precipitation = precipitation;
	}

	public Biome getBiome() {
		return biome;
	}

	public CSIColor getBackground() {
		return this.background;
	}

	public BiomeSubtype getBiomeSubtype() {
		return biomeSubtype;
	}

	public void setBiomeSubtype(BiomeSubtype biomeSubtype) {
		this.biomeSubtype = biomeSubtype;
		this.background = TileColor.get(biomeSubtype);
	}
	
	public Resource getResource() {
		return this.resource;
	}

	public void setResource(Resource resource) {
		if (resource == null) {
			resource = Resource.EMPTY;
		}
		
		this.resource = resource;
		this.renderable = resource.getRenderable();
		this.flags = resource.getFlags();
	}
	
	public Renderable getRenderable() {
		return this.renderable;
	}

	public boolean checkFlag(int flag) {
		return (flags & flag) == flag;
	}

	public void setFlag(int flag, boolean value) {
		if (value)
			flags |= flag;
		else
			flags &= ~flag;
	}
	
	/**
	 * This method will generate a biome associated with a tile based on the
	 * following temperature vs. precipitation relation
	 * */
	public void updateBiome() {
		if (temperature < -65) {
			if (precipitation < 0) {
				biome = Biome.TUNDRA;
			} else if (precipitation < 64) {
				biome = Biome.FOREST;
			} else {
				biome = Biome.SNOW;
			}
		} else if (temperature < 64) {
			if (precipitation < 80) {
				biome = Biome.FOREST;
			} else if (precipitation < 94) {
				biome = Biome.SWAMP;
			} else {
				biome = Biome.OCEAN;
			}
		} else {
			if (precipitation < -65) {
				biome = Biome.DESERT;
			} else if (precipitation < 0) {
				biome = Biome.SAVANNAH;
			} else if (precipitation < 64) {
				biome = Biome.FOREST;
			} else if (precipitation < 88) {
				biome = Biome.RAIN_FOREST;
			} else if (precipitation < 94) {
				biome = Biome.BEACH;
			} else {
				biome = Biome.OCEAN;
			}
		}

		updateColor();
		updateResource();
	}

	/**
	 * This updates the tile background color based on the biome and the biome
	 * information.
	 */
	private void updateColor() {
		if (biome == Biome.OCEAN) {
			// Switch water depth based on precipitation percentage
			int part = (127 - 94) / 5;

			switch ((this.getPrecipitation() - 94) / part) {
			case 0:
				this.setBiomeSubtype(BiomeSubtype.OCEAN_SHALLOW);
				break;
			case 1:
				this.setBiomeSubtype(BiomeSubtype.OCEAN_WATER);
				break;
			case 2:
				this.setBiomeSubtype(BiomeSubtype.OCEAN_DEEP1);
				break;
			case 3:
				this.setBiomeSubtype(BiomeSubtype.OCEAN_DEEP2);
				break;
			case 4:
			case 5:
				this.setBiomeSubtype(BiomeSubtype.OCEAN_DEEP3);
				break;
			}
		} else if (biome == Biome.TUNDRA) {
			int part = 128 / 3;

			switch (-this.getPrecipitation() / part) {
			case 0:
				this.setBiomeSubtype(BiomeSubtype.TUNDRA1);
				break;
			case 1:
				this.setBiomeSubtype(BiomeSubtype.TUNDRA2);
				break;
			default:
				this.setBiomeSubtype(BiomeSubtype.TUNDRA3);
				break;
			}
		} else if (biome == Biome.BEACH) {
			this.setBiomeSubtype(BiomeSubtype.BEACH);
		} else if (biome == Biome.RAIN_FOREST) {
			this.setBiomeSubtype(BiomeSubtype.RAIN_FOREST);
		} else if (biome == Biome.SWAMP) {
			this.setBiomeSubtype(BiomeSubtype.SWAMP);
		} else if (biome == Biome.SNOW) {
			this.setBiomeSubtype(BiomeSubtype.SNOW);
		} else if (biome == Biome.DESERT) {
			this.setBiomeSubtype(BiomeSubtype.DESERT);
		} else if (biome == Biome.SAVANNAH) {
			this.setBiomeSubtype(BiomeSubtype.SAVANNAH);
		} else if (biome == Biome.FOREST) {
			if (this.getPrecipitation() < 0) {
				if (this.getPrecipitation() > -65) {
					this.setBiomeSubtype(BiomeSubtype.FOREST2);
				} else {
					this.setBiomeSubtype(BiomeSubtype.FOREST1);
				}
			} else {
				if (this.getPrecipitation() > 40) {
					this.setBiomeSubtype(BiomeSubtype.FOREST4);
				} else {
					this.setBiomeSubtype(BiomeSubtype.FOREST3);
				}
			}
		} else {
			this.setBiomeSubtype(BiomeSubtype.REGULAR);
		}
	}

	private void updateResource() {
		int chance;
		switch (this.getBiome()) {
		case FOREST:
			chance = RNG.getInstance().nextInt(100);
			if (chance < 18) // 18% of tree
				this.setResource(Tree.getInstance());
			else if (chance < 20) // 2% of berries
				this.setResource(Berry.getInstance());
			else
				// Rest is grass
				this.setResource(Grass.getInstance());

			break;
		case DESERT:
			chance = RNG.getInstance().nextInt(100);
			if (chance < 2) // 2% of cactus
				this.setResource(Cactus.getInstance());
			break;
		case OCEAN:
			chance = RNG.getInstance().nextInt(100);
			if (chance < 15) // 15% chance of wave
				this.setResource(Wave.getInstance());
			break;
		}

	}

}
