package com.jokeofweek.lib.time;

import java.io.Serializable;

import com.jokeofweek.game.GameContainer;
import com.jokeofweek.game.event.PlayerInputEvent;
import com.jokeofweek.lib.WSwingConsoleInterface;

public class Time implements Serializable {

	private static final long serialVersionUID = 7722912121205932689L;

	private long lastState;
	private TimeState timeState;
	private Scheduler scheduler;
	private PlayerInputEvent inputEvent;

	public Time() {
		this.lastState = 0;
		this.timeState = TimeState.DAY;
		this.scheduler = new Scheduler();
		this.inputEvent = new PlayerInputEvent();
	}

	public long getTicks() {
		return inputEvent.getTicks();
	}

	public int getTicksTillNextState() {
		return (int) (this.lastState + timeState.getStateLength() - getTicks());
	}

	public TimeState getTimeState() {
		return timeState;
	}

	public void setTimeState(TimeState timeState) {
		this.timeState = timeState;
	}

	public Scheduler getScheduler() {
		return this.scheduler;
	}

	public void doTick(GameContainer container, WSwingConsoleInterface csi) {

		long ticks = getTicks();
		
		if (this.lastState + timeState.getStateLength() < ticks) {
			timeState = timeState.getNextState();
			lastState = ticks;
		}

		scheduler.doTick(container, csi, this);
		inputEvent.doEvent(container, csi);
	}
}
