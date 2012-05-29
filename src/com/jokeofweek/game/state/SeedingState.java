package com.jokeofweek.game.state;

import com.jokeofweek.data.GameData;
import com.jokeofweek.game.GameContainer;
import com.jokeofweek.gui.Border;
import com.jokeofweek.gui.TextBox;
import com.jokeofweek.lib.CharKey;
import com.jokeofweek.lib.WSwingConsoleInterface;
import com.jokeofweek.lib.util.Position;
import com.jokeofweek.lib.util.RNG;

public class SeedingState extends AbstractState {

	private TextBox seedBox;
	private Border border;
	
	@Override
	public void enter(GameContainer container, WSwingConsoleInterface csi) {
		this.seedBox = new TextBox(new Position(3, 6));
		this.border = new Border(new Position(0,0), new Position(WSwingConsoleInterface.screenWidth, WSwingConsoleInterface.screenHeight));
	}

	@Override
	public void update(GameContainer container, WSwingConsoleInterface csi) {
		int code = csi.inkey().code;
		
		if (code == CharKey.ESC){
			container.enterState(State.MAIN_MENU);
		} else if (code == CharKey.ENTER) {
			if (!seedBox.getText().isEmpty()){
				// Check if its numeric, and if not get the hash code
				long value;
				try {
					value = Long.parseLong(seedBox.getText());
				} catch (NumberFormatException nef){
					value = seedBox.getText().hashCode();
				}
				
				GameData.seedInstance(value);
				container.enterState(State.PLAYER_NAME);
			}
		} else {
			this.seedBox.update(container,  csi, code);
		}
	}

	@Override
	public void render(GameContainer container, WSwingConsoleInterface csi) {
		border.render(container, csi);
		
		csi.print(2, 2, "Please enter a seed to generate a map for a new game. Note that");
		csi.print(2, 3, "the seed of a map can be seen by pressing F1 in game.");
		csi.print(2, 4, "Please press enter when you are done.");
		
		this.seedBox.render(container, csi);
	}

	@Override
	public void leave(GameContainer container, WSwingConsoleInterface csi) {
		// TODO Auto-generated method stub

	}

}
