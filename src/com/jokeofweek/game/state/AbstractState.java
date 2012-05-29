package com.jokeofweek.game.state;

import com.jokeofweek.game.GameContainer;
import com.jokeofweek.lib.WSwingConsoleInterface;

public abstract class AbstractState {

	public abstract void enter(GameContainer container, WSwingConsoleInterface csi);
	
	public abstract void update(GameContainer container, WSwingConsoleInterface csi);
	
	public abstract void render(GameContainer container, WSwingConsoleInterface csi);
	
	public abstract void leave(GameContainer container, WSwingConsoleInterface csi);
	
}
