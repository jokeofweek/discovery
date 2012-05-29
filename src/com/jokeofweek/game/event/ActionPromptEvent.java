package com.jokeofweek.game.event;

import com.jokeofweek.data.GameData;
import com.jokeofweek.data.map.Map;
import com.jokeofweek.game.GameContainer;
import com.jokeofweek.game.state.GameState;
import com.jokeofweek.lib.CharKey;
import com.jokeofweek.lib.WSwingConsoleInterface;

public class ActionPromptEvent extends PromptEvent {

	private static final long serialVersionUID = -299511068651246174L;

	public ActionPromptEvent(PlayerInputEvent event) {
		super(event);
	}

	@Override
	public boolean doEvent(GameContainer container, WSwingConsoleInterface csi) {
		GameData gameData = GameData.getInstance();
		int x = gameData.getPlayer().getEntity().getPosition().x;
		int y = gameData.getPlayer().getEntity().getPosition().y;
		boolean directionKey = true;

		switch (csi.inkey().code) {
		case CharKey.N7:
			x--;
			y--;
			break;
		case CharKey.N9:
			x++;
			y--;
			break;
		case CharKey.N1:
			x--;
			y++;
			break;
		case CharKey.N3:
			x++;
			y++;
			break;
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

		int modifier = 0;
		if (directionKey) {
			if (x >= 0 & y >= 0 & x < Map.MAP_SIZE && y < Map.MAP_SIZE) {
				if (gameData.getMap().getTile(x, y).getResource()
						.hasAction()) {
					gameData.getMap().getTile(x, y).getResource()
							.doAction(gameData.getMap().getTile(x, y));
					modifier = Math.abs(gameData.getPlayer().getEntity().getPosition().x - x) +  Math.abs(gameData.getPlayer().getEntity().getPosition().y - y);
				}
			}
		}
		
		((GameState) container.getCurrentState()).promptingForDirection = false;
		getInputEvent().setTicks(gameData.getTime().getTicks() + (modifier * 10));
		gameData.getTime().getScheduler().addEvent(getInputEvent());
		
		return false;
	}

}
