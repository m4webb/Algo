package com.bamwebb.algo.core;

import com.bamwebb.algo.data.ReadOnlyArray;
import com.bamwebb.algo.data.ReadOnlyList;
import com.bamwebb.algo.data.ReadOnlyMap;

public class Board {
    
    public static class PieceNotOnBoard extends Exception {}
    public static class LocationHasNoPiece extends Exception {}
    
    final private ReadOnlyArray<Square> squares;
    final private ReadOnlyMap<Piece, Location> pieceLocations;
    final private ReadOnlyMap<Piece, Location> opponentPieceLocations;
    final private ReadOnlyMap<Location, Piece> locationPieces;
    final private ReadOnlyMap<Location, Piece> locationOpponentPieces;
    

    public Board(ReadOnlyArray<Square> squares, ReadOnlyMap<Piece, Location> pieceLocations,
            ReadOnlyMap<Piece, Location> opponentPieceLocations, ReadOnlyMap<Location, Piece> locationPieces,
            ReadOnlyMap<Location, Piece> locationOpponentPieces ) {
        this.squares = squares;
        this.pieceLocations = pieceLocations;
        this.opponentPieceLocations = opponentPieceLocations;
        this.locationPieces = locationPieces;
        this.locationOpponentPieces = locationOpponentPieces;
    }
    
    public ReadOnlyArray<Square> getSquares() {
        return squares;
    }
    
    public ReadOnlyMap<Piece, Location> getPieceLocations() {
        return pieceLocations;
    }
    
    public ReadOnlyMap<Piece, Location> getOpponentPieceLocations() {
        return opponentPieceLocations;
    }
    
    public ReadOnlyMap<Location, Piece> getLocationPieces() {
        return locationPieces;
    }
    
    public ReadOnlyMap<Location, Piece> getlocationOpponentPieces() {
        return locationOpponentPieces;
    }
    
    public ReadOnlyList<Piece> getPieces() {
        return pieceLocations.keyList();
    }
    
    public ReadOnlyList<Piece> getOpponentPieces() {
        return opponentPieceLocations.keyList();
    }
    
    public ReadOnlyList<Location> getLocations() {
        return locationPieces.keyList();
    }
    
    public ReadOnlyList<Location> getOpponentLocations() {
        return locationOpponentPieces.keyList();
    }
    
    public Square getSquare(Location location) {
        return squares.getIndex(location.getIndex());
    }
    
    public Location getLocation(Piece piece) throws PieceNotOnBoard {
        if(!(pieceLocations.containsKey(piece))){
            throw new PieceNotOnBoard();
        }
        return pieceLocations.get(piece);
    }
    
    public Location getOpponentLocation(Piece piece) throws PieceNotOnBoard {
        if(!(opponentPieceLocations.containsKey(piece))) {
            throw new PieceNotOnBoard();
        }
        return opponentPieceLocations.get(piece);
    }
    
    public Piece getPiece(Location location) throws LocationHasNoPiece {
        if(!(locationPieces.containsKey(location))) {
            throw new LocationHasNoPiece();
        }
        return locationPieces.get(location);
    }
    
    public Piece getOpponentPiece(Location location) throws LocationHasNoPiece {
        if(!(locationOpponentPieces.containsKey(location))) {
            throw new LocationHasNoPiece();
        }
        return locationOpponentPieces.get(location);
    }
}