package com.bamwebb.algo.core;

public class Command {
	
	public enum Word {
	    ATTACK,
		MOVE,
		DEPLOY,
		FARM,
		RAZE,
		SURRENDER,
	}

	final private Word word;
	final private Location target;
	
	public Command(Word word, Location target) {
	    this.word = word;
	    this.target = target;
	}
	
	public Word getWord() {
	    return word;
	}
	
	public Location getTarget() {
	    return target;
	}
}


