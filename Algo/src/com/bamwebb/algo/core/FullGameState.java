package com.bamwebb.algo.core;

import java.util.UUID;

import com.bamwebb.algo.data.Bijection;

final public class FullGameState {
    
    final private UUID game;
    final private int winner;
    final private int turn;
    final private Coffer player1Coffer;
    final private Coffer player2Coffer;
    final private Square[] player1Squares;
    final private Square[] player2Squares;
    final private Bijection<Piece, Location> player1PieceLocations;
    final private Bijection<Piece, Location> player2PieceLocations;
    
    // TODO: Use boards instead of raw arrays

    
    public FullGameState(UUID game, int winner, int turn, Coffer player1Coffer, Coffer player2Coffer,
            Square[] player1Squares, Square[] player2Squares,
            Bijection<Piece, Location> player1PieceLocations, Bijection<Piece, Location> player2PieceLocations
            ) {
        this.game = game;
        this.winner = winner;
        this.turn = turn;
        this.player1Coffer = player1Coffer;
        this.player2Coffer = player2Coffer;
        this.player1Squares = player1Squares;
        this.player2Squares = player2Squares;
        this.player1PieceLocations = player1PieceLocations;
        this.player2PieceLocations = player2PieceLocations;
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
    
    public Coffer getPlayer1Coffer() {
        return player1Coffer;
    }
    
    public Coffer getPlayer2Coffer() {
        return player2Coffer;
    }
    
    public Square[] getPlayer1Squares() {
        return player1Squares;
    }
    
    public Square[] getPlayer2Squares() {
        return player2Squares;
    }
    
    public Bijection<Piece, Location> getPlayer1PieceLocations() {
        return player1PieceLocations;
    }
    
    public Bijection<Piece, Location> getPlayer2PieceLocations() {
        return player2PieceLocations;
    }
}
