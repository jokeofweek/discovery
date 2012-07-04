package com.jokeofweek.game.event;

import java.io.Serializable;

import com.jokeofweek.data.GameData;
import com.jokeofweek.game.GameContainer;
import com.jokeofweek.lib.WSwingConsoleInterface;

public abstract class Prompt implements Serializable {
	
	private static final long serialVersionUID = 2916709552304036107L;
	
	private PlayerInputEvent playerInputEvent;

	public Prompt(PlayerInputEvent playerInputEvent) {
		this.playerInputEvent = playerInputEvent;
	}
	
	public final void doPrompt(GameContainer container, WSwingConsoleInterface csi) {
		int ticks = handlePrompt(container, csi);
		
		playerInputEvent.addTicks(ticks);
	}
	
	public abstract void printMessage(GameContainer container, WSwingConsoleInterface csi);
	
	
	/**
	 * @param container
	 * @param csi
	 * @return The number of ticks until the next turn.
	 */
	public abstract int handlePrompt(GameContainer container, WSwingConsoleInterface csi);
	
}
