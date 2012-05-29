package com.jokeofweek.game.state;

import com.jokeofweek.data.GameData;
import com.jokeofweek.game.GameContainer;
import com.jokeofweek.gui.Border;
import com.jokeofweek.gui.TextBox;
import com.jokeofweek.lib.CharKey;
import com.jokeofweek.lib.WSwingConsoleInterface;
import com.jokeofweek.lib.util.Position;

public class PlayerNameState extends AbstractState {

	private TextBox nameBox;
	private Border border;
	
	@Override
	public void enter(GameContainer container, WSwingConsoleInterface csi) {
		this.nameBox = new TextBox(new Position(3, 4));
		this.nameBox.setMaxLength(15);
		this.border = new Border(new Position(0,0), new Position(WSwingConsoleInterface.screenWidth, WSwingConsoleInterface.screenHeight));
	}

	@Override
	public void update(GameContainer container, WSwingConsoleInterface csi) {
		int code = csi.inkey().code;
		
		if (code == CharKey.ESC){
			container.enterState(State.MAIN_MENU);
		} else if (code == CharKey.ENTER) {
			if (!nameBox.getText().isEmpty()){
				GameData.getInstance().getPlayer().setName(nameBox.getText());
				container.enterState(State.INITIALIZING);
			}
		} else {
			this.nameBox.update(container,  csi, code);
		}
	}

	@Override
	public void render(GameContainer container, WSwingConsoleInterface csi) {
		border.render(container, csi);
		
		csi.print(2, 2, "Please enter the name of your character and press enter to begin.");
		
		this.nameBox.render(container, csi);
	}

	@Override
	public void leave(GameContainer container, WSwingConsoleInterface csi) {}
}
