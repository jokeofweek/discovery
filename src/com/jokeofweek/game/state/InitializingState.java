package com.jokeofweek.game.state;

import com.jokeofweek.data.GameData;
import com.jokeofweek.game.GameContainer;
import com.jokeofweek.gui.Border;
import com.jokeofweek.lib.WSwingConsoleInterface;
import com.jokeofweek.lib.util.Position;

public class InitializingState extends AbstractState {

	private Border border;
	
	@Override
	public void enter(GameContainer container, WSwingConsoleInterface csi) {
		this.border = new Border(new Position(0,0), new Position(WSwingConsoleInterface.screenWidth, WSwingConsoleInterface.screenHeight));
	}

	@Override
	public void update(GameContainer container, WSwingConsoleInterface csi) {
		// Initialize all game data
		GameData.getInstance().initialize();
		
		container.enterState(State.GAME);
	}

	@Override
	public void render(GameContainer container, WSwingConsoleInterface csi) {
		border.render(container, csi);
		String caption = "Building game data...";
		csi.print( (WSwingConsoleInterface.screenWidth / 2) - (caption.length() / 2), 
				   WSwingConsoleInterface.screenHeight / 2, 
				   caption);
	}

	@Override
	public void leave(GameContainer container, WSwingConsoleInterface csi) {}

}
