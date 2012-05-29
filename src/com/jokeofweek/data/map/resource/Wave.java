package com.jokeofweek.data.map.resource;

import com.jokeofweek.lib.CSIColor;
public class Wave extends RandomCharacterResource {

	private static Wave instance = new Wave();
	private static char[] chars = { ',', '.', '\'', '`', '˛' };

	@Override
	public char[] getCharacters() {
		return chars;
	}

	@Override
	public CSIColor getColor() {
		return CSIColor.LIGHT_BLUE;
	}

	public static Wave getInstance() {
		return instance;
	}
}
