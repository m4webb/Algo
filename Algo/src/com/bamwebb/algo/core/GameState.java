package com.bamwebb.algo.core;

import java.util.UUID;

final public class GameState {
	
    final private UUID game;
    final private int winner;
	final private int turn;
	final private int resources;
	final private Board board;
	
	public GameState(UUID game, int winner, int turn, int resources, Board board) {
	    this.game = game;
	    this.winner = winner;
		this.turn = turn;
		this.resources = resources;
		this.board = board;
	}
	
	public UUID getGame() {
        return game;
    }
	
	public int getWinner() {
	    return winner;
	}

    public int getTurn() {
		return turn;
	}
    
    public int getResources() {
        return resources;
    }
	
	public Board getBoard() {
	    return board;
	}
}
