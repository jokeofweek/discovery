package com.jokeofweek.data.map.resource;

import com.jokeofweek.lib.CSIColor;
import com.jokeofweek.lib.entity.Renderable;

public class TreeStump extends Resource {

	private static Renderable renderable = new Renderable('.', CSIColor.BROWN);
	private static TreeStump instance = new TreeStump();
	
	private TreeStump() {};
	
	public Renderable getRenderable() {
		return renderable;
	}
	
	public static TreeStump getInstance() {
		return instance;
	}
	
	
}
