package com.jokeofweek.game.state;

import java.util.ArrayList;

import com.jokeofweek.game.GameContainer;
import com.jokeofweek.gui.Border;
import com.jokeofweek.gui.ListBox;
import com.jokeofweek.gui.ListItem;
import com.jokeofweek.lib.CSIColor;
import com.jokeofweek.lib.CharKey;
import com.jokeofweek.lib.ConsoleSystemInterface;
import com.jokeofweek.lib.WSwingConsoleInterface;
import com.jokeofweek.lib.util.Position;

public class MainMenuState extends AbstractState {

	private Border border;
	private ListBox menuBox;
	
	@Override
	public void enter(GameContainer container, WSwingConsoleInterface csi) {
		this.border = new Border(new Position(0,0), new Position(WSwingConsoleInterface.screenWidth, WSwingConsoleInterface.screenHeight));
		
		ArrayList<ListItem> items = new ArrayList<ListItem>(3);
		items.add(new ListItem() {
			@Override
			public void invoke(GameContainer container, WSwingConsoleInterface csi) {
				container.enterState(State.PLAYER_NAME);
			}
			@Override
			public String getCaption() {return "New Game";}
		});
		items.add(new ListItem() {
			@Override
			public void invoke(GameContainer container, WSwingConsoleInterface csi) {
				container.enterState(State.SEEDING);
			}
			@Override
			public String getCaption() {return "Seed New Game";}
		});
		items.add(new ListItem() {
			@Override
			public void invoke(GameContainer container, WSwingConsoleInterface csi) {
				System.exit(0);
			}
			@Override
			public String getCaption() {return "Quit";}
		});
		menuBox = new ListBox(items, false);
		menuBox.setPosition(new Position(4, 3));
	
	}

	@Override
	public void update(GameContainer container, WSwingConsoleInterface csi) {
		menuBox.update(container, csi, csi.inkey());
	}

	@Override
	public void render(GameContainer container, WSwingConsoleInterface csi) {
		border.render(container, csi);
		csi.print(4, 2, "Welcome to the wilderness! Select an option:");
		menuBox.render(container, csi);
	}

	@Override
	public void leave(GameContainer container, WSwingConsoleInterface csi) {
		// TODO Auto-generated method stub

	}

}
