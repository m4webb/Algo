package com.bamwebb.algo.core;

import java.util.List;

import com.bamwebb.algo.data.Bijection;
import com.bamwebb.algo.data.Bijection.BijectionException;

public class Board {
    
    public static class PieceNotOnBoard extends Exception {}
    public static class LocationHasNoPiece extends Exception {}
    
    final private Square[] squares;
    final private Bijection<Piece, Location> pieceLocations;
    final private Bijection<Piece, Location> opponentPieceLocations;
    
    public Board(Square[] squares, Bijection<Piece, Location> pieceLocations,
            Bijection<Piece, Location> opponentPieceLocations ) {
        this.squares = squares;
        this.pieceLocations = pieceLocations;
        this.opponentPieceLocations = opponentPieceLocations;
    }
    
    public Square getSquare(Location location) {
        return squares[location.getIndex()];
    }
    
    public List<Piece> getPieces() {
        return pieceLocations.getDomain();
    }
    
    public List<Piece> getOpponentPieces() {
        return opponentPieceLocations.getDomain();
    }
    
    public List<Location> getLocations() {
        return pieceLocations.getCodomain();
    }
    
    public List<Location> getOpponentLocations() {
        return opponentPieceLocations.getCodomain();
    }
    
    public Location getLocation(Piece piece) throws PieceNotOnBoard {
        try {
            return pieceLocations.mapForward(piece);
        } catch (BijectionException e) {
            throw new PieceNotOnBoard();
        }
    }
    
    public Location getLocationOrNull(Piece piece) {
        try {
            return pieceLocations.mapForward(piece);
        } catch (BijectionException e) {
            return null;
        }
    }
    
    public Location getOpponentLocation(Piece piece) throws PieceNotOnBoard {
       try {
           return opponentPieceLocations.mapForward(piece);
       } catch (BijectionException e) {
           throw new PieceNotOnBoard();
       }
    }
    
    public Location getOpponentLocationOrNull(Piece piece) {
        try {
            return opponentPieceLocations.mapForward(piece);
        } catch (BijectionException e) {
            return null;
        }
    }
    
    public Piece getPiece(Location location) throws LocationHasNoPiece {
        try {
            return pieceLocations.mapBackward(location);
        } catch (BijectionException e) {
            throw new LocationHasNoPiece();
        }
    }
    
    public Piece getPieceOrNull(Location location) {
        try {
            return pieceLocations.mapBackward(location);
        } catch (BijectionException e) {
            return null;
        }
    }
    
    public Piece getOpponentPiece(Location location) throws LocationHasNoPiece {
        try {
            return opponentPieceLocations.mapBackward(location);
        } catch (BijectionException e) {
            throw new LocationHasNoPiece();
        }
    }
    
    public Piece getOpponentPieceOrNull(Location location) {
        try {
            return opponentPieceLocations.mapBackward(location);
        } catch (BijectionException e) {
            return null;
        }
    }
    
    public boolean locationHasPiece(Location location) {
        return pieceLocations.codomainContains(location);
    }
    
    public boolean locationHasOpponentPiece(Location location) {
        return opponentPieceLocations.codomainContains(location);
    }
    
    public boolean pieceOnBoard(Piece piece) {
        return pieceLocations.domainContains(piece);
    }
    
    public boolean opponentPieceOnBoard(Piece piece) {
        return opponentPieceLocations.domainContains(piece);
    }
}