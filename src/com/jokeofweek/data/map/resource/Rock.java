package com.jokeofweek.data.map.resource;

import com.jokeofweek.lib.CSIColor;
import com.jokeofweek.lib.entity.Renderable;

public class Rock extends Resource {

	private static Rock instance = new Rock();
	private static Renderable renderable = new Renderable('o',
			CSIColor.GREEN);
	
	@Override
	public Renderable getRenderable() {
		return renderable;
	}
	
	public static Rock getInstance() {
		return instance;
	}
}
