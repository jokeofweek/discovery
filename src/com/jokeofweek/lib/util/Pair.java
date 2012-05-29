package com.jokeofweek.lib.util;

public class Pair <K,L>{
	private K a;
	private L b;
	public Pair(K a, L b){
		this.a = a;
		this.b = b;
	}
	public K getA() {
		return a;
	}
	public void setA(K a) {
		this.a = a;
	}
	public L getB() {
		return b;
	}
	public void setB(L b) {
		this.b = b;
	}
}
