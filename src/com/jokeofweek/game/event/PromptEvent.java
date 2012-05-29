package com.jokeofweek.game.event;

import com.jokeofweek.game.GameContainer;
import com.jokeofweek.lib.WSwingConsoleInterface;
import com.jokeofweek.lib.time.Event;

public abstract class PromptEvent extends Event {

	private static final long serialVersionUID = 3920827850419367100L;

	private PlayerInputEvent inputEvent;
	
	public PromptEvent(PlayerInputEvent inputEvent) {
		super(0);
		this.inputEvent = inputEvent;
	}

	public Event getInputEvent() {
		return inputEvent;
	}
	
	@Override
	public abstract boolean doEvent(GameContainer container, WSwingConsoleInterface csi);
}
