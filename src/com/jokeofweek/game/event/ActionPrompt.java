package com.jokeofweek.game.event;

import com.jokeofweek.data.GameData;
import com.jokeofweek.data.map.Map;
import com.jokeofweek.game.GameContainer;
import com.jokeofweek.game.state.GameState;
import com.jokeofweek.lib.CSIColor;
import com.jokeofweek.lib.CharKey;
import com.jokeofweek.lib.WSwingConsoleInterface;

public class ActionPrompt extends Prompt {

	private static final long serialVersionUID = -299511068651246174L;

	public ActionPrompt(PlayerInputEvent event) {
		super(event);
	}

	@Override
	public void printMessage(GameContainer container,
			WSwingConsoleInterface csi) {
		csi.print(0, 0, "Direction to perform action?", CSIColor.BLACK,
				CSIColor.WHITE);
	}

	@Override
	public int handlePrompt(GameContainer container,
			WSwingConsoleInterface csi) {
		GameData gameData = GameData.getInstance();
		int x = gameData.getPlayer().getEntity().getPosition().x;
		int y = gameData.getPlayer().getEntity().getPosition().y;
		boolean directionKey = true;

		switch (csi.inkey().code) {
		case CharKey.N4:
		case CharKey.LARROW:
			x--;
			break;
		case CharKey.N6:
		case CharKey.RARROW:
			x++;
			break;
		case CharKey.N8:
		case CharKey.UARROW:
			y--;
			break;
		case CharKey.N2:
		case CharKey.DARROW:
			y++;
			break;
		default:
			directionKey = false;
		}


		if (directionKey) {
			if (x >= 0 & y >= 0 & x < Map.MAP_SIZE && y < Map.MAP_SIZE) {
				if (gameData.getMap().getTile(x, y).getResource()
						.hasAction()) {
					gameData.getMap().getTile(x, y).getResource()
							.doAction(gameData.getMap().getTile(x, y));
					return 10;
				}
			}
		}

		return 0;
	}

}
