package com.bamwebb.algo.core;

import java.util.UUID;

import com.bamwebb.algo.data.ReadOnlyArray;
import com.bamwebb.algo.data.ReadOnlyMap;

final public class FullGameState {
    
    final private UUID game;
    final private int winner;
    final private int turn;
    final private int player1Resources;
    final private int player2Resources;
    final private ReadOnlyArray<Square> squares;
    final private ReadOnlyMap<Piece, Location> player1PieceLocations;
    final private ReadOnlyMap<Piece, Location> player2PieceLocations;
    final private ReadOnlyMap<Location, Piece> player1LocationPieces;
    final private ReadOnlyMap<Location, Piece> player2LocationPieces;
    
    public FullGameState(UUID game, int winner, int turn, int player1Resources, int player2Resources,
            ReadOnlyArray<Square> squares, ReadOnlyMap<Piece, Location> player1PieceLocations,
            ReadOnlyMap<Piece, Location> player2PieceLocations, ReadOnlyMap<Location, Piece> player1LocationPieces,
            ReadOnlyMap<Location, Piece> player2LocationPieces) {
        this.game = game;
        this.winner = winner;
        this.turn = turn;
        this.player1Resources = player1Resources;
        this.player2Resources = player2Resources;
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
    
    public int getPlayer1Resources() {
        return player1Resources;
    }
    
    public int getPlayer2Resources() {
        return player2Resources;
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
