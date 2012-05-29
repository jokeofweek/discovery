package com.jokeofweek.gui;

import com.jokeofweek.game.GameContainer;
import com.jokeofweek.lib.CSIColor;
import com.jokeofweek.lib.WSwingConsoleInterface;
import com.jokeofweek.lib.util.Position;

public class Border {
	Position position;
	Position dimension;
	CSIColor color;
	
	public Border(Position position, Position dimension){
		this(position, dimension, CSIColor.WHITE);
	}
	
	public Border(Position position, Position dimension, CSIColor color){
		this.position = position;
		this.dimension = dimension;
		this.color = color;
	}
	
	public void render(GameContainer container, WSwingConsoleInterface csi){
		for (int x = this.position.x + 1; x < this.position.x + this.dimension.x - 1; x++){
			csi.print(x, this.position.y, '═', color);
			csi.print(x, this.position.y + this.dimension.y - 1, '═', color);
		}
		for (int y = this.position.y + 1; y < this.position.y + this.dimension.y - 1; y++){
			csi.print(this.position.x, y, '║', color);
			csi.print(this.position.x + this.dimension.x - 1, y, '║', color);
		}
		csi.print(this.position.x, this.position.y, '╔', color);
		csi.print(this.position.x + this.dimension.x - 1, this.position.y, '╗', color);
		csi.print(this.position.x, this.position.y + this.dimension.y - 1, '╚', color);
		csi.print(this.position.x + this.dimension.x - 1, this.position.y + this.dimension.y - 1, '╝', color);
	
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Position getDimension() {
		return dimension;
	}

	public void setDimension(Position dimension) {
		this.dimension = dimension;
	}

	public CSIColor getColor() {
		return color;
	}

	public void setColor(CSIColor color) {
		this.color = color;
	}
}
