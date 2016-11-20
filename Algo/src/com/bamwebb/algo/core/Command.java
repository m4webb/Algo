package com.bamwebb.algo.core;

public class Command {
	
	public enum Word {
		MOVE,
		EMIT,
		FARM,
		RAZE,
	}
	
	final private Piece piece;
	final private Word word;
	final private Square target;
	
	public Command(Piece piece, Word word, Square target) {
	    this.piece = piece;
	    this.word = word;
	    this.target = target;
	}
	
	public Piece getPiece() {
	    return piece;
	}
	
	public Word getWord() {
	    return word;
	}
	
	public Square getTarget() {
	    return target;
	}
}


