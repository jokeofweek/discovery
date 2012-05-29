package com.jokeofweek.gui;

import java.util.ArrayList;

import com.jokeofweek.game.GameContainer;
import com.jokeofweek.lib.CSIColor;
import com.jokeofweek.lib.CharKey;
import com.jokeofweek.lib.WSwingConsoleInterface;
import com.jokeofweek.lib.util.Position;

public class ListBox {

	public ArrayList<ListItem> items;
	private boolean border;
	private Border borderElement;
	private Position position = new Position(0, 0);
	private int selection = 0;
	private int maxWidth = 0;
	
	public ListBox(ArrayList<ListItem> items){
		this(items, true);
	}
	
	public ListBox(ArrayList<ListItem> items, boolean border){
		this.items = items;
		this.border = border;
		
		for (ListItem item : items){
			if (item.getCaption().length() > maxWidth)
				maxWidth = item.getCaption().length();
		}
	}
	
	public void setPosition(Position position){
		this.position = position;
		
		if (border)
			borderElement = new Border(position, new Position(maxWidth + 4, this.items.size() + 2));
	}
	
	public void render(GameContainer container, WSwingConsoleInterface csi){
		int count = 0;
		int modifier = (border) ? 1 : 0;
		
		for (ListItem item : items){
			if (count == selection)
				csi.print(this.position.x + modifier,
						  this.position.y + modifier + count, 
						  "» ",
						  item.getColor());
			else
				csi.print(this.position.x + modifier,
						  this.position.y + modifier + count, 
						  "  ",
						  item.getColor());
				
			
			csi.print(this.position.x + modifier + 2, 
					  this.position.y + modifier + count,
					  item.getCaption(),
					  item.getColor());
			
			for (int i = item.getCaption().length(); i <= maxWidth; i++){
				csi.print(this.position.x + modifier + 2 + i, 
						  this.position.y + modifier + count,
						  '█',
						  CSIColor.BLACK,
						  CSIColor.BLACK);
			}
			count++;
		}
		
		if (border){
			borderElement.render(container, csi);
		}
	}
	
	public void update(GameContainer container, WSwingConsoleInterface csi, CharKey key){
		switch (key.code){
		case CharKey.UARROW:
			if (selection > 0) selection--;
			break;
		case CharKey.DARROW:
			if (selection < items.size() - 1) selection++;
			break;
		case CharKey.ENTER:
			this.items.get(selection).invoke(container, csi);
			break;
		}
	}
	
}
