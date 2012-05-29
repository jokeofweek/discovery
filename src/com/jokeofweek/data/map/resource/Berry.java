package com.jokeofweek.data.map.resource;

import com.jokeofweek.lib.CSIColor;
import com.jokeofweek.lib.entity.Renderable;

public class Berry extends Resource {
	
	private static Berry instance = new Berry();
	private static Renderable renderable = new Renderable('„', CSIColor.RED);
	
	public int getFlags(){return 0;}
		
	public Renderable getRenderable(){ 
		return renderable;
	}

	public static Berry getInstance() {
		return instance;
	}
}
