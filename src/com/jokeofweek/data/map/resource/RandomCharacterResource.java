package com.jokeofweek.data.map.resource;

import com.jokeofweek.lib.CSIColor;
import com.jokeofweek.lib.entity.Renderable;
import com.jokeofweek.lib.util.RNG;

public abstract class RandomCharacterResource extends Resource {

	public Renderable getRenderable() {
		return new Renderable(getCharacters()[RNG.getInstance()
				.nextInt(getCharacters().length)], getColor());
	}
	
	public abstract char[] getCharacters();
	public abstract CSIColor getColor();

}
