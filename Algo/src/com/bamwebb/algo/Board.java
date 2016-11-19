package com.bamwebb.algo;

import com.bamwebb.data.ReadOnlyArray;
import com.bamwebb.data.ReadOnlyList;
import com.bamwebb.data.ReadOnlyMap;

public class Board {
    
    public static class PieceNotOnBoard extends Exception {}
    public static class SquareHasNoPiece extends Exception {}
    public static class SquareDoesNotExist extends Exception {}
    public static class SquareIsInFog extends Exception {}
    
    public static int distance(Square squareOne, Square squareTwo) {
        int dx = Math.abs(squareOne.getX() - squareTwo.getX());
        int dy = Math.abs(squareOne.getY() - squareTwo.getY());
        return dx + dy;
    }
    
    final private ReadOnlyArray<Square> squares;
    final private ReadOnlyMap<Piece, Square> pieceSquares;
    final private ReadOnlyMap<Square, Piece> squarePieces;
    final private ReadOnlyMap<Square, Piece> squareOpponentPieces;
    
    private void checkIndex(int index) throws SquareDoesNotExist {
        if(index < 0 || index >= Configuration.MAX_INDEX) {
            throw new SquareDoesNotExist();
        }
    }
    
    public Board(ReadOnlyArray<Square> squares, ReadOnlyMap<Piece, Square> pieceSquares,
            ReadOnlyMap<Square, Piece> squarePieces, ReadOnlyMap<Square, Piece> squareOpponentPieces ) {
        this.squares = squares;
        this.pieceSquares = pieceSquares;
        this.squarePieces = squarePieces;
        this.squareOpponentPieces = squareOpponentPieces;
    }
    
    public Square getSquare(Piece piece) throws PieceNotOnBoard {
        if(!(pieceSquares.containsKey(piece))){
            throw new PieceNotOnBoard();
        }
        return pieceSquares.get(piece);
    }
    
    public Square getSquare(int x, int y) throws SquareDoesNotExist, SquareIsInFog {
        checkIndex(Square.getIndex(x, y));
        return squares.getIndex(Square.getIndex(x, y));
    }
    
    public Piece getPiece(Square square) throws SquareDoesNotExist, SquareIsInFog, SquareHasNoPiece {
        checkIndex(square.getIndex());
        if(!(squarePieces.containsKey(square))) {
            throw new SquareHasNoPiece();
        }
        return squarePieces.get(square);
    }
    
    public Piece getOpponentPiece(Square square) throws SquareDoesNotExist, SquareIsInFog, SquareHasNoPiece {
        checkIndex(square.getIndex());
        if(!(squareOpponentPieces.containsKey(square))) {
            throw new SquareHasNoPiece();
        }
        return squareOpponentPieces.get(square);
    }
    
    public ReadOnlyList<Piece> getPieces() {
        return squarePieces.valuesList();
    }
    
    public ReadOnlyList<Piece> getOpponentPieces() {
        return squareOpponentPieces.valuesList();
    }
}