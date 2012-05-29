package com.jokeofweek.data.items;

import com.jokeofweek.lib.CSIColor;

public class Item {

	private String name;
	private int weight;
	private char character;
	private CSIColor color;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
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
	
}
