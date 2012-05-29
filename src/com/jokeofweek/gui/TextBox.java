package com.jokeofweek.gui;

import com.jokeofweek.game.GameContainer;
import com.jokeofweek.lib.CSIColor;
import com.jokeofweek.lib.CharKey;
import com.jokeofweek.lib.WSwingConsoleInterface;
import com.jokeofweek.lib.util.Position;

public class TextBox {

	private int width;
	private int maxLength = 255;
	private String text;
	private Position position;
	private String backgroundString;
	
	public TextBox(Position position){
		this(position, 25);
	}
	
	public TextBox(Position position, int width){
		this.setPosition(position);
		this.setWidth(width);
		this.setText("");
		backgroundString = "";
		for (int i = 0; i < width; i++)
			backgroundString += " ";
	}

	public void update(GameContainer container, WSwingConsoleInterface csi, int keyCode){

		if (keyCode == CharKey.BACKSPACE){
			if (text.length() != 0)
				text = text.substring(0, text.length() - 1);
		} else {
			if (text.length() < this.maxLength){
				if (keyCode >= 90 && keyCode <= 115) {
					text += ((char) (keyCode - 25));
				} else if (keyCode >= 64 && keyCode <= 89) {
					text +=  ((char) (keyCode + 33));
				} else if (keyCode >= 117 && keyCode <= 126) {
		            text += ((char) (keyCode - 69));
		        } else {
		        	
		        	String ret = CharKey.specialCharacters.get(keyCode);
		        	if (ret != null)
			        	text += ret;
		        }
			}
		}
	}
	
	public void render(GameContainer container, WSwingConsoleInterface csi){
		csi.print(this.getPosition().x,
				  this.getPosition().y,
				  this.backgroundString,
				  CSIColor.WHITE,
				  CSIColor.GRAY);
		
		if (this.text.length() >= width){
			csi.print(this.getPosition().x,
					  this.getPosition().y,
					  this.text.substring(this.text.length() - width + 1) + "_",
					  CSIColor.WHITE,
					  CSIColor.GRAY);
		} else {
			csi.print(this.getPosition().x,
					  this.getPosition().y,
					  this.text + "_",
					  CSIColor.WHITE,
					  CSIColor.GRAY);
		}
	}
	
	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
	
	public int getMaxLength() {
		return maxLength;
	}
	
	public void setMaxLength(int maxLength){
		this.maxLength = maxLength;
	}
	
}
