package com.jokeofweek.data.map.resource;

import com.jokeofweek.lib.CSIColor;

public class Grass extends RandomCharacterResource {

	private static Grass instance = new Grass();
	private static char[] chars = { ',', '.', '\'', '`', '˛' };

	@Override
	public char[] getCharacters() {
		return chars;
	}

	@Override
	public CSIColor getColor() {
		return CSIColor.GREEN;
	}

	public static Grass getInstance() {
		return instance;
	}
}
