package com.jokeofweek.data;

import java.io.Serializable;

import com.jokeofweek.data.map.Map;
import com.jokeofweek.data.map.MapGenerator;
import com.jokeofweek.game.event.PlayerInputEvent;
import com.jokeofweek.lib.time.Time;
import com.jokeofweek.lib.util.Position;
import com.jokeofweek.lib.util.RNG;

public class GameData implements Serializable {

	private static final long serialVersionUID = 7188369860952107642L;
	private static GameData instance = new GameData();
	
	// Map stuff
	private long mapSeed; // This is the seed used to generate the map
	private transient Map map;
	
	// Player
	private Player player = new Player();
	
	// Time
	private Time time = new Time();
	
	public static GameData getInstance() {
		return instance;
	}
	
	public static void seedInstance(long seed){
		instance = new GameData(seed);
	}
	
	public GameData(){
		this(System.currentTimeMillis());
	}
	
	public GameData(long seed){
		mapSeed = seed;
		RNG.seedInstance(seed);
	}
	
	/**
	 * This initializes any game data which needs to be initialized, and should
	 * always be called after a game is created or loaded.
	 */
	public void initialize(){
		this.map = MapGenerator.generate(Map.MAP_SIZE);
		
		this.player.getEntity().setPosition(new Position(35, 50));
		
		this.time.getScheduler().addEvent(new PlayerInputEvent(time.getTicks() + 10));
	}
	
	public Map getMap(){
		return this.map;
	}
	
	public long getMapSeed(){
		return this.mapSeed;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public Time getTime(){
		return time;
	}

	
}
