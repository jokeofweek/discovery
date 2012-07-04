package com.jokeofweek.game.state;

import java.util.ArrayList;
import com.jokeofweek.data.GameData;
import com.jokeofweek.data.Player;
import com.jokeofweek.data.map.Fov;
import com.jokeofweek.data.map.Map;
import com.jokeofweek.data.map.Tile;
import com.jokeofweek.game.GameContainer;
import com.jokeofweek.game.event.Prompt;
import com.jokeofweek.gui.Border;
import com.jokeofweek.gui.ListBox;
import com.jokeofweek.gui.ListItem;
import com.jokeofweek.lib.CSIColor;
import com.jokeofweek.lib.CharKey;
import com.jokeofweek.lib.WSwingConsoleInterface;
import com.jokeofweek.lib.camera.Camera;
import com.jokeofweek.lib.camera.EntityCamera;
import com.jokeofweek.lib.entity.Entity;
import com.jokeofweek.lib.entity.Renderable;
import com.jokeofweek.lib.time.TimeState;
import com.jokeofweek.lib.util.Position;

public class GameState extends AbstractState {

	public static final int VISIBLE_MAP_WIDTH = 60;
	public static final int VISIBLE_MAP_HEIGHT = 25;

	private GameData gameData;
	private Camera camera;
	private Player player;
	private Entity playerEntity;

	public boolean inMenu = false;
	private ListBox menuBox;

	private Prompt prompt = null;

	public boolean inDev = false;

	public boolean renderFlag = false;

	// FOV
	private Fov fov;

	// Statistics menu
	private Border playerMenuBorder = new Border(new Position(
			VISIBLE_MAP_WIDTH, 0), new Position(
			WSwingConsoleInterface.screenWidth - VISIBLE_MAP_WIDTH,
			VISIBLE_MAP_HEIGHT), CSIColor.YELLOW);

	@Override
	public void enter(GameContainer container, WSwingConsoleInterface csi) {
		gameData = GameData.getInstance();
		player = gameData.getPlayer();
		playerEntity = player.getEntity();

		camera = new EntityCamera(playerEntity);
		camera.update(container, csi);

		// Initialize the menu
		ArrayList<ListItem> items = new ArrayList<ListItem>(2);
		items.add(new ListItem() {

			@Override
			public void invoke(GameContainer container,
					WSwingConsoleInterface csi) {
				inMenu = false;
			}

			@Override
			public String getCaption() {
				return "Resume";
			}
		});

		items.add(new ListItem() {
			@Override
			public void invoke(GameContainer container,
					WSwingConsoleInterface csi) {
				System.exit(0);
			}

			@Override
			public String getCaption() {
				return "Quit";
			}
		});

		menuBox = new ListBox(items);
		menuBox.setPosition(new Position(5, 10));

		// Set up the fov
		fov = new Fov(gameData);
	}

	@Override
	public void update(GameContainer container, WSwingConsoleInterface csi) {
		if (inMenu) {
			CharKey key = csi.inkey();
			menuBox.update(container, csi, key);
			return;
		} else if (getPrompt() != null) {
			getPrompt().doPrompt(container, csi);
			setPrompt(null);
		} else {
			renderFlag = false;
			while (!renderFlag) {
				gameData.getTime().doTick(container, csi);
			}
		}
	}

	@Override
	public void render(GameContainer container, WSwingConsoleInterface csi) {

		// If its night time, clear the insight and recompute the fov, or else
		// just draw it
		if (gameData.getTime().getTimeState() == TimeState.NIGHT) {
			Tile[][] tiles = gameData.getMap().getTiles();
			for (int y = camera.getPosition().y; y < camera.getPosition().y
					+ VISIBLE_MAP_HEIGHT; y++)
				for (int x = camera.getPosition().x; x < camera
						.getPosition().x + VISIBLE_MAP_WIDTH; x++) {
					tiles[x][y].setFlag(Tile.ATTRIBUTE_INSIGHT, false);
				}

			fov.start(playerEntity.getPosition().x,
					playerEntity.getPosition().y, 5);

			for (int y = camera.getPosition().y; y < camera.getPosition().y
					+ VISIBLE_MAP_HEIGHT; y++)
				for (int x = camera.getPosition().x; x < camera
						.getPosition().x + VISIBLE_MAP_WIDTH; x++) {
					if (tiles[x][y].checkFlag(Tile.ATTRIBUTE_INSIGHT))
						drawTile(x, y, csi);
				}
		} else {
			for (int y = camera.getPosition().y; y < camera.getPosition().y
					+ VISIBLE_MAP_HEIGHT; y++)
				for (int x = camera.getPosition().x; x < camera
						.getPosition().x + VISIBLE_MAP_WIDTH; x++) {
					drawTile(x, y, csi);
				}
		}

		csi.print(playerEntity.getPosition().x - camera.getPosition().x,
				playerEntity.getPosition().y - camera.getPosition().y, "@");

		// Render menu
		playerMenuBorder.render(container, csi);
		
		csi.print(playerMenuBorder.getPosition().x + 1,
				playerMenuBorder.getPosition().y + 1, player.getName());
		
		csi.print(
				playerMenuBorder.getPosition().x + 1,
				playerMenuBorder.getPosition().y
						+ playerMenuBorder.getDimension().y - 2, ""
						+ gameData.getTime().getTicksTillNextState()
						/ 10
						+ " till "
						+ gameData.getTime().getTimeState().getNextState()
								.getName());

		if (inDev) {
			csi.print(0, 0, playerEntity.getPosition().x + ", "
					+ playerEntity.getPosition().y, CSIColor.BLACK,
					CSIColor.WHITE);
			csi.print(0, 1, "Seed: " + GameData.getInstance().getMapSeed());
		}

		if (getPrompt() != null) {
			getPrompt().printMessage(container, csi);
		}

		if (inMenu) {
			menuBox.render(container, csi);
		}

	}

	public void drawTile(int x, int y, WSwingConsoleInterface csi) {
		String draw = " ";
		Renderable renderable;

		switch (gameData.getMap().getTiles()[x][y].getBiome()) {
		case DESERT:
			draw = "D";
			break;
		case RAIN_FOREST:
			draw = "R";
			break;
		case SAVANNAH:
			draw = "V";
			break;
		}
		renderable = gameData.getMap().getTiles()[x][y].getRenderable();
		csi.print(
				x - camera.getPosition().x,
				y - camera.getPosition().y,
				renderable.getCharacter(),
				renderable.getColor(),
				(renderable.getBackgroundColor() == CSIColor.TRANSPARENT) ? gameData
						.getMap().getTiles()[x][y].getBackground()
						: renderable.getBackgroundColor());
	}

	@Override
	public void leave(GameContainer container, WSwingConsoleInterface csi) {
		// TODO Auto-generated method stub

	}

	public Camera getCamera() {
		return this.camera;
	}

	public Prompt getPrompt() {
		return prompt;
	}

	public void setPrompt(Prompt prompt) {
		this.prompt = prompt;
	}

}
