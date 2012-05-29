package com.jokeofweek.lib.entity;

import com.jokeofweek.lib.CSIColor;
import com.jokeofweek.lib.util.Position;

public class Entity extends Renderable {
	
	private Position position;
	
	public Entity(char character, CSIColor color){
		super(character, color);
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

}
