package com.jokeofweek.gui;

import com.jokeofweek.game.GameContainer;
import com.jokeofweek.lib.CSIColor;
import com.jokeofweek.lib.WSwingConsoleInterface;

public abstract class ListItem {

	public CSIColor getColor(){return CSIColor.WHITE;}
	public abstract String getCaption();
	public abstract void invoke(GameContainer container, WSwingConsoleInterface csi);
	
}
