package com.jokeofweek.data;

import java.io.Serializable;

import com.jokeofweek.lib.CSIColor;
import com.jokeofweek.lib.entity.Entity;
import com.jokeofweek.lib.time.Event;

public class Player implements Serializable {

	private static final long serialVersionUID = -7410985411581671185L;
	
	private transient Entity entity = new Entity('@', CSIColor.WHITE);
	private Event inputEvent;
	
	private String name;

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	
	public Event getInputEvent() {
		return inputEvent;
	}
	
	public void setInputEvent(Event inputEvent) {
		this.inputEvent = inputEvent;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}

	
}
