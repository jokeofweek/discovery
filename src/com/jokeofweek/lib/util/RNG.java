package com.jokeofweek.lib.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class RNG {
	private static MersenneTwisterFast instance = new MersenneTwisterFast();
	
	public static void load(DataInputStream in) throws IOException{
		instance.readState(in);
	}
	
	public static void write(DataOutputStream out) throws IOException{
		instance.writeState(out);
	}
	
	public static void seedInstance(long seed){
		instance = new MersenneTwisterFast(seed);
	}
	
	public static MersenneTwisterFast getInstance(){
		return instance;
	}
}