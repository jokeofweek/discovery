package com.jokeofweek.game.event;

import com.jokeofweek.data.GameData;
import com.jokeofweek.data.map.Map;
import com.jokeofweek.data.map.Tile;
import com.jokeofweek.game.GameContainer;
import com.jokeofweek.game.state.GameState;
import com.jokeofweek.lib.CharKey;
import com.jokeofweek.lib.WSwingConsoleInterface;
import com.jokeofweek.lib.entity.Entity;
import com.jokeofweek.lib.time.Event;

public class PlayerInputEvent {

	private static final long serialVersionUID = -2204141898532145955L;
	private GameData gameData;
	private Entity playerEntity;
	private long ticks;
	
	public PlayerInputEvent() {
		ticks = 0;
		gameData = GameData.getInstance();
		playerEntity = gameData.getPlayer().getEntity();
	}
	
	public void addTicks(long ticks) {
		this.ticks += ticks;
	}
	
	public long getTicks() {
		return ticks;
	}

	public void doEvent(GameContainer container, WSwingConsoleInterface csi) {
		int modifier = 1;
		int x = playerEntity.getPosition().x, y = playerEntity
				.getPosition().y;
		
		boolean keyPressed = false;
		
		while (!keyPressed) {
			keyPressed = true;
			switch (csi.inkey().code) {
			case CharKey.N4:
			case CharKey.LARROW:
				if (x > 0 && gameData.getMap().isTileWalkable(x - 1, y))
					playerEntity.getPosition().x--;
				break;
			case CharKey.N6:
			case CharKey.RARROW:
				if (x < Map.MAP_SIZE - 1
						&& gameData.getMap().isTileWalkable(x + 1, y))
					playerEntity.getPosition().x++;
				break;
			case CharKey.N8:
			case CharKey.UARROW:
				if (y > 0 && gameData.getMap().isTileWalkable(x, y - 1))
					playerEntity.getPosition().y--;
				break;
			case CharKey.N2:
			case CharKey.DARROW:
				if (y < Map.MAP_SIZE - 1
						&& gameData.getMap().isTileWalkable(x, y + 1))
					playerEntity.getPosition().y++;
				break;
			case CharKey.SPACE:
				// Prompt the user for the action direction.
				// Skip the action since we are prompting, as we don't want to lose a turn if it was an accident
				((GameState) container.getCurrentState()).setPrompt(new ActionPrompt(this));
				modifier = 0;
				break;
			case CharKey.F1:
				((GameState) container.getCurrentState()).inDev = !((GameState) container
						.getCurrentState()).inDev;
				modifier = 0;
				break;
			case CharKey.ESC:
				((GameState) container.getCurrentState()).inMenu = !((GameState) container
						.getCurrentState()).inMenu;
				modifier = 0;
				break;
			default:
				keyPressed = false;
			}
		}
		
		GameState gameState = (GameState) container.getCurrentState();
		gameState.getCamera().update(container, csi);
		gameState.renderFlag = true;

		this.ticks += (modifier * 10);
	}

}
