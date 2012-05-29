package com.jokeofweek.game;

import java.util.HashMap;

import com.jokeofweek.game.state.AbstractState;
import com.jokeofweek.game.state.GameState;
import com.jokeofweek.game.state.InitializingState;
import com.jokeofweek.game.state.MainMenuState;
import com.jokeofweek.game.state.PlayerNameState;
import com.jokeofweek.game.state.SeedingState;
import com.jokeofweek.game.state.State;
import com.jokeofweek.lib.ConsoleSystemInterface;
import com.jokeofweek.lib.WSwingConsoleInterface;

public class GameContainer {

	private WSwingConsoleInterface console;
	private HashMap<State, AbstractState> states = new HashMap<State, AbstractState>();
	private AbstractState state;
	
	public static void main(String[] args){
		new GameContainer().run();
	}
	
	public GameContainer(){
		this.console = new WSwingConsoleInterface("Discovery", false);
		
		this.addState(State.MAIN_MENU, new MainMenuState());
		this.addState(State.INITIALIZING, new InitializingState());
		this.addState(State.SEEDING, new SeedingState());
		this.addState(State.GAME, new GameState());
		this.addState(State.PLAYER_NAME, new PlayerNameState());
		
		this.enterState(State.MAIN_MENU);
	}
	
	public void addState(State state, AbstractState stateObject){
		this.states.put(state, stateObject);
	}
	
	public void enterState(State state){
		if (this.state != null)
			this.state.leave(this, console);
		
		this.state = states.get(state);
		this.state.enter(this, console);
	}
	
	public AbstractState getState(State state){
		return this.states.get(state);
	}
	
	public ConsoleSystemInterface getConsole() {
		return console;
	}
	
	public AbstractState getCurrentState(){
		return this.state;
	}
	
	public void run(){
		while (true){
			console.cls();
			this.state.render(this, console);
			console.refresh();
			this.state.update(this, console);
		}
	}
	
}
