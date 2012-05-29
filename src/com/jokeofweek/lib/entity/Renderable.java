package com.jokeofweek.lib.entity;

import com.jokeofweek.lib.CSIColor;

public class Renderable {

	protected char character;
	protected CSIColor color;
	protected CSIColor backgroundColor;
	
	public Renderable(char character, CSIColor color){
		this(character, color, CSIColor.TRANSPARENT);
	}
	
	public Renderable(char character, CSIColor color, CSIColor backgroundColor){
		this.character = character;
		this.color = color;
		this.backgroundColor = backgroundColor;
	}

	public char getCharacter() {
		return character;
	}

	public void setCharacter(char character) {
		this.character = character;
	}

	public CSIColor getColor() {
		return color;
	}

	public void setColor(CSIColor color) {
		this.color = color;
	}
	public CSIColor getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(CSIColor backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
}
