package com.jokeofweek.lib.time;

import java.io.Serializable;

import com.jokeofweek.game.GameContainer;
import com.jokeofweek.lib.WSwingConsoleInterface;

public class Time implements Serializable{
	
	private static final long serialVersionUID = 7722912121205932689L;
	
	private long ticks;
	private long lastState;
	private TimeState timeState;
	private Scheduler scheduler;

	public Time(){
		this.ticks = 0;
		this.lastState = 0;
		this.timeState = TimeState.DAY;
		this.scheduler = new Scheduler();
	}
	
	public long getTicks() {
		return ticks;
	}
	public void setTicks(long ticks) {
		this.ticks = ticks;
	}
	public int getTicksTillNextState(){
		return (int) (this.lastState + timeState.getStateLength() - ticks);
	}
	
	public TimeState getTimeState() {
		return timeState;
	}
	public void setTimeState(TimeState timeState) {
		this.timeState = timeState;
	}
	public Scheduler getScheduler(){
		return this.scheduler;
	}
	public void doTick(GameContainer container, WSwingConsoleInterface csi){
		this.ticks++;
		
		if (this.lastState + timeState.getStateLength() < ticks){
			timeState = timeState.getNextState();
			lastState = ticks;
		}
		
		scheduler.doTick(container, csi, this);
	}
}
