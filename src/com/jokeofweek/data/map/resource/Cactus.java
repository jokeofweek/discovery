package com.jokeofweek.data.map.resource;

import com.jokeofweek.data.map.Tile;
import com.jokeofweek.lib.CSIColor;

public class Cactus extends RandomCharacterResource {

	private static Cactus instance = new Cactus();
	private static char[] chars = { '├', '╟', '╢' };

	public int getFlags() {
		return Tile.ATTRIBUTE_BLOCK + Tile.ATTRIBUTE_BLOCKSIGHT;
	}

	@Override
	public char[] getCharacters() {
		return chars;
	}

	@Override
	public CSIColor getColor() {
		return CSIColor.GREEN;
	}

	public static Cactus getInstance() {
		return instance;
	}
}
