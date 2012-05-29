package com.jokeofweek.data.map.resource;

import com.jokeofweek.data.map.Tile;
import com.jokeofweek.lib.CSIColor;
import com.jokeofweek.lib.entity.Renderable;
import com.jokeofweek.lib.util.RNG;


public class Resource {

	public static Resource EMPTY = new Resource();
	private static Renderable renderable = new Renderable(' ', CSIColor.WHITE);
	
	public int getFlags() {
		return 0;
	}
	
	public Renderable getRenderable() {
		return renderable;
	}
	
	public boolean hasAction() {
		return false;
	}
	
	public void doAction(Tile tile) {
		return;
	}
}
