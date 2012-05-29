package com.jokeofweek.lib.time;

public enum TimeState {
	
	NIGHT(){
		public TimeState getNextState(){return TimeState.DAWN;}
		public String getName(){return "Night";}
		public int getStateLength(){return 3300;}
	},
	DAWN(){
		public TimeState getNextState(){return TimeState.DAY;}
		public String getName(){return "Dawn";}
		public int getStateLength(){return 300;}
	},
	DAY(){
		public TimeState getNextState(){return TimeState.DUSK;}
		public String getName(){return "Day";}
		public int getStateLength(){return 3300;}
	},
	DUSK(){
		public TimeState getNextState(){return TimeState.NIGHT;}
		public String getName(){return "Dusk";}
		public int getStateLength(){return 300;}
	};

	public abstract TimeState getNextState();
	public abstract int getStateLength();
	public abstract String getName();
}
