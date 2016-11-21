package com.bamwebb.algo.core;

import java.util.UUID;

import com.bamwebb.algo.core.Coffer.ReadOnlyCoffer;
import com.bamwebb.algo.data.ReadOnlyArray;
import com.bamwebb.algo.data.ReadOnlyMap;

final public class FullGameState {
    
    final private UUID game;
    final private int winner;
    final private int turn;
    final private ReadOnlyCoffer player1Coffer;
    final private ReadOnlyCoffer player2Coffer;
    final private ReadOnlyArray<Square> squares;
    final private ReadOnlyMap<Piece, Location> player1PieceLocations;
    final private ReadOnlyMap<Piece, Location> player2PieceLocations;
    final private ReadOnlyMap<Location, Piece> player1LocationPieces;
    final private ReadOnlyMap<Location, Piece> player2LocationPieces;
    
    public FullGameState(UUID game, int winner, int turn, ReadOnlyCoffer player1Coffer, ReadOnlyCoffer player2Coffer,
            ReadOnlyArray<Square> squares, ReadOnlyMap<Piece, Location> player1PieceLocations,
            ReadOnlyMap<Piece, Location> player2PieceLocations, ReadOnlyMap<Location, Piece> player1LocationPieces,
            ReadOnlyMap<Location, Piece> player2LocationPieces) {
        this.game = game;
        this.winner = winner;
        this.turn = turn;
        this.player1Coffer = player1Coffer;
        this.player2Coffer = player2Coffer;
        this.squares = squares;
        this.player1PieceLocations = player1PieceLocations;
        this.player2PieceLocations = player2PieceLocations;
        this.player1LocationPieces = player1LocationPieces;
        this.player2LocationPieces = player2LocationPieces;
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
    
    public ReadOnlyCoffer getPlayer1Coffer() {
        return player1Coffer;
    }
    
    public ReadOnlyCoffer getPlayer2Coffer() {
        return player2Coffer;
    }
    
    public ReadOnlyArray<Square> getSquares() {
        return squares;
    }
    
    public ReadOnlyMap<Piece, Location> getPlayer1PieceLocations() {
        return player1PieceLocations;
    }
    
    public ReadOnlyMap<Piece, Location> getPlayer2PieceLocations() {
        return player2PieceLocations;
    }
    
    public ReadOnlyMap<Location, Piece> getPlayer1LocationPieces() {
        return player1LocationPieces;
    }
    
    public ReadOnlyMap<Location, Piece> getPlayer2LocationPIeces() {
        return player2LocationPieces;
    }
}
