package com.jokeofweek.lib.camera;

import com.jokeofweek.data.map.Map;
import com.jokeofweek.game.GameContainer;
import com.jokeofweek.game.state.GameState;
import com.jokeofweek.lib.WSwingConsoleInterface;
import com.jokeofweek.lib.entity.Entity;
import com.jokeofweek.lib.util.Position;

public class EntityCamera extends Camera {
	
	private int halfWidth = GameState.VISIBLE_MAP_WIDTH>> 1;
	private int halfHeight = GameState.VISIBLE_MAP_HEIGHT >> 1;
	private int xLimit = Map.MAP_SIZE - GameState.VISIBLE_MAP_WIDTH;
	private int yLimit = Map.MAP_SIZE - GameState.VISIBLE_MAP_HEIGHT;
	
	Position position = new Position(0,0);
	Entity entity;
	
	public EntityCamera(Entity entity){
		this.entity = entity;
	}

	@Override
	public Position getPosition() {
		return this.position;
	}
	
	@Override
	public void update(GameContainer container, WSwingConsoleInterface csi) {
		int x, y;
		
		if (entity.getPosition().x < halfWidth) {
			x = 0;
		} else {
			if (entity.getPosition().x > xLimit + halfWidth){
				x = xLimit;
			} else {
				x = entity.getPosition().x - halfWidth;
			}
		}
		
		if (entity.getPosition().y < halfHeight){
			y = 0;
		} else {
			if (entity.getPosition().y > yLimit + halfHeight){
				y = yLimit;
			} else {
				y = entity.getPosition().y - halfHeight;
			}
		}
		
		this.position.x = x;
		this.position.y = y;
	}

}
