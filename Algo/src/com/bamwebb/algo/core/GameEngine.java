package com.bamwebb.algo.core;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.bamwebb.algo.core.Location.LocationDoesNotExist;
import com.bamwebb.algo.data.Bijection;
import com.bamwebb.algo.data.Bijection.BijectionException;
import com.bamwebb.algo.data.ReadOnlyArray;

public class GameEngine {
    
    private enum Perspective {
        PLAYER_ONE,
        PLAYER_TWO,
    }
    
    // State
    private UUID game;
    private int winner;
    private int turn;
    private int player1Resources;
    private int player2Resources;
    private Square[] squares;
    private Square[] player1Squares;
    private Square[] player2Squares;
    private Boolean[] player1Fog;
    private Boolean[] player2Fog;
    private Bijection<Piece, Location> player1PieceLocations;
    private Bijection<Piece, Location> player1Player2PieceLocations;
    private Bijection<Piece, Location> player2PieceLocations;
    private Bijection<Piece, Location> player2Player1PieceLocations;
    // TODO: Add grave yard.
    
    // Resolution
    private int[] player1Emissions;
    private int[] player2Emissions;
    private int[] player1Arrivals;
    private int[] player2Arrivals;
     
    private FullGameState getFullGameState() {
        return new FullGameState(game, turn, winner, player1Resources, player2Resources, new ReadOnlyArray<Square>(squares),
                player1PieceLocations.getForwardReadOnlyMap(), player2PieceLocations.getForwardReadOnlyMap(),
                player2PieceLocations.getBackwardReadOnlyMap(), player2PieceLocations.getBackwardReadOnlyMap());
    }

    private GameState getPlayerGameState(Perspective perspective) {
        Board board;
        if(perspective == Perspective.PLAYER_ONE) {
            board = new Board(new ReadOnlyArray<Square>(player1Squares), player1PieceLocations.getForwardReadOnlyMap(),
                    player1Player2PieceLocations.getForwardReadOnlyMap(), player1PieceLocations.getBackwardReadOnlyMap(),
                    player1Player2PieceLocations.getBackwardReadOnlyMap());
            return new GameState(game, winner, turn, player1Resources, board);
        } else {
            board = new Board(new ReadOnlyArray<Square>(player2Squares), player2PieceLocations.getForwardReadOnlyMap(),
                   player2Player1PieceLocations.getForwardReadOnlyMap(), player2PieceLocations.getBackwardReadOnlyMap(),
                        player2Player1PieceLocations.getBackwardReadOnlyMap());
            return new GameState(game, winner, turn, player2Resources, board);
        }
    }
    
    private void resolveCommands(PlayerResponse player1Response, PlayerResponse player2Response) throws InvariantViolation {
        
        Map<Piece, Command> player1Commands = player1Response.getCommands();
        Map<Piece, Command> player2Commands = player2Response.getCommands();
        
        //
        // Eliminate pieces.
        //
        
        countEmissions(player1Commands, player1PieceLocations, player1Emissions);
        countEmissions(player2Commands, player2PieceLocations, player2Emissions);
        
        eliminatePieces(player1PieceLocations, player2Emissions);
        eliminatePieces(player2PieceLocations, player1Emissions);
        
        //
        // Resolve movement.
        //
        
        do {
            countArrivals(player1Commands, player1PieceLocations, player1Arrivals);
            removeCollisions(player1Commands, player1PieceLocations, player1Arrivals);
        } while (anyCollisions(player1Arrivals));
        
        movePieces(player1Commands, player1PieceLocations);
        
        do {
            countArrivals(player2Commands, player2PieceLocations, player2Arrivals);
            removeCollisions(player2Commands, player2PieceLocations, player2Arrivals);
        } while (anyCollisions(player2Arrivals));
        
        movePieces(player2Commands, player2PieceLocations);
        
        // Resolve deployment.
        
        // Resolve farming.
        
        // Resolve razing.
        
        // Add resources.
        
        // Check win condition.
        
        //
        // Apply fog to resulting game state.
        //
        
        applyFog(squares, player1Fog, player1Squares, player2PieceLocations, player1Player2PieceLocations, false);
        applyFog(squares, player2Fog, player2Squares, player1PieceLocations, player2Player1PieceLocations, true);
        
        return;
    }
    
    private static void movePieces(Map<Piece, Command> playerCommands, Bijection<Piece, Location> playerPieceLocations)
            throws InvariantViolation {
        Bijection<Piece, Location> playerPieceLocationsHolder = new Bijection<Piece, Location>();
        Command command;
        Location pieceLocation, target;
        Piece storedPiece;
        Enumeration<Piece> allPieces, movedPieces;
        
        try {
            allPieces = playerPieceLocations.getDomain().enumerate();
            while(allPieces.hasMoreElements()) {
                storedPiece = allPieces.nextElement();
                pieceLocation = playerPieceLocations.mapForward(storedPiece);
                if (!playerCommands.containsKey(storedPiece)) {
                    continue;
                }
                command = playerCommands.get(storedPiece);
                if (command.getWord() != Command.Word.MOVE) {
                    continue;
                }
                target = command.getTarget();
                if (Location.distance(target, pieceLocation) > storedPiece.getSpeed()) {
                    continue;
                }
                playerPieceLocationsHolder.addPairing(storedPiece, target);
                playerPieceLocations.removePairing(storedPiece, pieceLocation);
            }
            movedPieces = playerPieceLocationsHolder.getDomain().enumerate();
            while(movedPieces.hasMoreElements()) {
                storedPiece = movedPieces.nextElement();
                pieceLocation = playerPieceLocationsHolder.mapForward(storedPiece);
                playerPieceLocations.addPairing(storedPiece, pieceLocation);
            }
        } catch (BijectionException exception) {
            throw new InvariantViolation();
        }
    }
    
    private static void removeCollisions(Map<Piece, Command> playerCommands, Bijection<Piece, Location> playerPieceLocations,
            int[] playerArrivals) throws InvariantViolation {
        Iterator<Map.Entry<Piece, Command>> iterator = playerCommands.entrySet().iterator();
        Map.Entry<Piece, Command> entry;
        Piece playerPiece, storedPiece;
        Location pieceLocation, target;
        Command command;
        
        while(iterator.hasNext()) {
            entry = iterator.next();
            playerPiece = entry.getKey();
            if (!playerPieceLocations.domainContains(playerPiece)) {
                continue;
            }
            command = entry.getValue();
            if (command.getWord() != Command.Word.MOVE) {
                continue;
            }
            target = command.getTarget();
            try {
                pieceLocation = playerPieceLocations.mapForward(playerPiece);
                storedPiece = playerPieceLocations.mapBackward(pieceLocation);
            } catch (BijectionException exception) {
                throw new InvariantViolation();
            }
            if (Location.distance(pieceLocation, target) > storedPiece.getSpeed()) {
                continue;
            }
            if (playerArrivals[target.getIndex()] > 1) {
                iterator.remove();
            }
        }
    }
   
    private static void countArrivals(Map<Piece, Command> playerCommands, Bijection<Piece, Location> playerPieceLocations,
            int[] playerArrivals) throws InvariantViolation {
        Command command;
        Location pieceLocation, target;
        Piece storedPiece;
        Enumeration<Piece> allPieces = playerPieceLocations.getDomain().enumerate();
        
        for (int i=0; i<Configuration.MAX_INDEX; i++) {
            playerArrivals[i] = 0;
        }
        
        while(allPieces.hasMoreElements()) {
            storedPiece = allPieces.nextElement();
            try {
                pieceLocation = playerPieceLocations.mapForward(storedPiece);
            } catch (BijectionException exception) {
                throw new InvariantViolation();
            }
            if (!playerCommands.containsKey(storedPiece)) {
                playerArrivals[pieceLocation.getIndex()] += 1;
                continue;
            }
            command = playerCommands.get(storedPiece);
            if (command.getWord() != Command.Word.MOVE) {
                playerArrivals[pieceLocation.getIndex()] += 1;
                continue;
            }
            target = command.getTarget();
            if (Location.distance(target, pieceLocation) > storedPiece.getSpeed()) {
                playerArrivals[pieceLocation.getIndex()] += 1;
                continue;
            }
            playerArrivals[target.getIndex()] += 1;
        }
    }
    
    private static boolean anyCollisions(int[] playerArrivals) {
        for (int i=0; i<Configuration.MAX_INDEX; i++) {
            if (playerArrivals[i] > 1) return true;
        }
        return false;
    }
    
    private static void countEmissions(Map<Piece, Command> playerCommands, Bijection<Piece, Location> playerPieceLocations,
            int[] playerEmissions) throws InvariantViolation {
        Piece storedPiece;
        Location pieceLocation, target;
        Command command;
        
        for (int i=0; i<Configuration.MAX_INDEX; i++) {
            playerEmissions[i] = 0;
        }
        
        for (Piece playerPiece : playerCommands.keySet()) {
            if (!playerPieceLocations.domainContains(playerPiece)) {
                continue;
            }
            try {
                pieceLocation = playerPieceLocations.mapForward(playerPiece);
                storedPiece = playerPieceLocations.mapBackward(pieceLocation);
            } catch (BijectionException exception) {
                throw new InvariantViolation();
            }
            command = playerCommands.get(playerPiece);
            if (command.getWord() == Command.Word.EMIT) {
                target = command.getTarget();
                if (Location.distance(pieceLocation, target) <= storedPiece.getReach()) {
                    playerEmissions[target.getIndex()] += storedPiece.getStrength();
                }
            }
        }
    }
    
    private static void eliminatePieces(Bijection<Piece, Location> playerPieceLocations, int[] opponentEmissions)
            throws InvariantViolation {
        Enumeration<Piece> playerPiecesEnumeration = playerPieceLocations.getDomain().enumerate();
        Piece piece;
        Location location;
        while (playerPiecesEnumeration.hasMoreElements()) {
            piece = playerPiecesEnumeration.nextElement();
            try {
                location = playerPieceLocations.mapForward(piece);
            } catch (BijectionException exception) {
                throw new InvariantViolation();
            }
            if (opponentEmissions[location.invertPerspective().getIndex()] >= piece.getToughness()) {
                try {
                    playerPieceLocations.removeForward(piece);
                } catch (BijectionException exception) {
                    throw new InvariantViolation();
                }
            }
        }
    }

    private static void applyFog(Square[] squares, Boolean[] playerFog, Square[] playerSquares,
            Bijection<Piece, Location> opponentPieceLocations, Bijection<Piece, Location> playerOpponentPieceLocations,
            boolean invertSquaresIndex) throws InvariantViolation {
        playerOpponentPieceLocations.clear();
        Location location;
        Piece piece;
        int squaresIndex;
        
        for (int i=0; i<Configuration.MAX_INDEX; i++) {
            if (invertSquaresIndex) {
                squaresIndex = Configuration.MAX_INDEX - i - 1;
            } else {
                squaresIndex = i;
            }
            if (playerFog[i]) {
                playerSquares[i] = squares[squaresIndex].setState(Square.State.FOG);
            } else {
                playerSquares[i] = squares[squaresIndex];
                try {
                    location = Location.fromIndex(i);
                    if (opponentPieceLocations.codomainContains(location.invertPerspective())) {
                        piece = opponentPieceLocations.mapBackward(location.invertPerspective());
                        playerOpponentPieceLocations.addPairing(piece, location);
                    }
                } catch (LocationDoesNotExist exception) {
                    throw new InvariantViolation();
                } catch (BijectionException exception) {
                    throw new InvariantViolation();
                }
            }
        }
    }

    public GameEngine() {
        game = UUID.randomUUID();
        turn = 0;
        winner = 0;
        player1Resources = Configuration.INITIAL_RESOURCES;
        player2Resources = Configuration.INITIAL_RESOURCES;
        squares = new Square[Configuration.MAX_INDEX];
        player2Squares = new Square[Configuration.MAX_INDEX];
        player2Squares = new Square[Configuration.MAX_INDEX];
        player1Fog = new Boolean[Configuration.MAX_INDEX];
        player2Fog = new Boolean[Configuration.MAX_INDEX];
        player1PieceLocations = new Bijection<Piece, Location>();
        player2PieceLocations = new Bijection<Piece, Location>();
        player1Player2PieceLocations = new Bijection<Piece, Location>();
        player2Player1PieceLocations = new Bijection<Piece, Location>();
    }
    
    public int play(Player player1, Player player2, List<Observer> observers) throws InvariantViolation {
        FullGameState fullGameState;
        GameState player1GameState, player2GameState;
        PlayerResponse player1Response, player2Response;
        
        while (winner == 0) {
            fullGameState = getFullGameState();
            player1GameState = getPlayerGameState(Perspective.PLAYER_ONE);
            player2GameState = getPlayerGameState(Perspective.PLAYER_TWO);
            
            player1Response = player1.play(player1GameState);
            player2Response = player2.play(player2GameState);
            for(Observer observer : observers) {
                observer.observe(fullGameState, player1GameState, player2GameState);
            }
            
            resolveCommands(player1Response, player2Response);
        }
        
        fullGameState = getFullGameState();
        player1GameState = getPlayerGameState(Perspective.PLAYER_ONE);
        player2GameState = getPlayerGameState(Perspective.PLAYER_TWO);
        
        for (Observer observer : observers) {
            observer.observe(fullGameState, player1GameState, player2GameState);
        }
        
        return winner;
    }
}
