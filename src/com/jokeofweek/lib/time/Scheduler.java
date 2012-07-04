package com.jokeofweek.lib.time;

import java.util.PriorityQueue;

import com.jokeofweek.game.GameContainer;
import com.jokeofweek.lib.WSwingConsoleInterface;

public class Scheduler {

	private PriorityQueue<Event> events = new PriorityQueue<Event>();
	
	public void addEvent(Event e){
		events.add(e);
	}
	
	public void doTick(GameContainer container, WSwingConsoleInterface csi, Time time){
		long current = time.getTicks();
		while (!events.isEmpty() && events.peek().getTicks() <= current) {
			events.remove().doEvent(container, csi);
		}
	}
	
}
