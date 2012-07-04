package com.jokeofweek.lib.time;

import java.io.Serializable;

import com.jokeofweek.game.GameContainer;
import com.jokeofweek.lib.WSwingConsoleInterface;

public abstract class Event implements Serializable, Comparable<Event> {

	private static final long serialVersionUID = 4331069763186734284L;

	private long ticks;

	/**
	 * Creates an event which will occur at a specific point in time.
	 * 
	 * @param tick
	 *            This is the specific point in time in ticks at which the event
	 *            should occur.
	 */
	public Event(long ticks) {
		this.ticks = ticks;
	}

	public void setTicks(long ticks) {
		this.ticks = ticks;
	}

	public long getTicks() {
		return this.ticks;
	}

	/**
	 * This performs the event code.
	 * 
	 * @param container
	 * @param csi
	 */
	public abstract void doEvent(GameContainer container,
			WSwingConsoleInterface csi);

	@Override
	public int compareTo(Event o) {
		return (int) (this.getTicks() - o.getTicks());
	}

}
