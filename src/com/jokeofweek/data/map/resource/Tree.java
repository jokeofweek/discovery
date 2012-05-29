package com.jokeofweek.data.map.resource;

import com.jokeofweek.data.map.Tile;
import com.jokeofweek.lib.CSIColor;

public class Tree extends RandomCharacterResource {

	private static Tree instance = new Tree();
	private static char[] chars =  { '╦', 'T', '♣', '┬' };
	
	private Tree() {}
	
	public int getFlags(){return Tile.ATTRIBUTE_BLOCK + Tile.ATTRIBUTE_BLOCKSIGHT;}

	@Override
	public char[] getCharacters() {
		return chars;
	}

	@Override
	public CSIColor getColor() {
		return CSIColor.DARK_GREEN;
	}

	public static Tree getInstance() {
		return instance;
	}
	
	@Override
	public boolean hasAction() {
		return true;
	}
	
	@Override
	public void doAction(Tile tile) {
		tile.setResource(TreeStump.getInstance());
	}
	
}
