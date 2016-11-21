package com.bamwebb.algo.core;

import java.util.UUID;

import com.bamwebb.algo.core.Coffer.ReadOnlyCoffer;

final public class GameState {
	
    final private UUID game;
    final private int winner;
	final private int turn;
	final private ReadOnlyCoffer coffer;
	final private Board board;
	
	public GameState(UUID game, int winner, int turn, ReadOnlyCoffer coffer, Board board) {
	    this.game = game;
	    this.winner = winner;
		this.turn = turn;
		this.coffer = coffer;
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
    
    public ReadOnlyCoffer getCoffer() {
        return coffer;
    }
	
	public Board getBoard() {
	    return board;
	}
}
