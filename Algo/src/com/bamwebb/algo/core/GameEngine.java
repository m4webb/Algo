package com.bamwebb.algo.core;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.bamwebb.algo.core.Coffer.CofferEmpty;
import com.bamwebb.algo.core.Location.LocationDoesNotExist;
import com.bamwebb.algo.data.Bijection;
import com.bamwebb.algo.data.Bijection.BijectionException;

public class GameEngine {
    
    private enum Perspective {
        PLAYER_ONE,
        PLAYER_TWO,
    }
    
    // State
    private UUID game;
    private int winner;
    private int turn;
    private Coffer player1Coffer;
    private Coffer player2Coffer;
    private Square[] player1Squares;
    private Square[] player2Squares;
    private Square[] player1VisibleSquares;
    private Square[] player2VisibleSquares;
    private Bijection<Piece, Location> player1PieceLocations;
    private Bijection<Piece, Location> player1Player2PieceLocations;
    private Bijection<Piece, Location> player2PieceLocations;
    private Bijection<Piece, Location> player2Player1PieceLocations;
    // TODO: Add grave yard.
    
    // Resolution
    private boolean[] player1Fog;
    private boolean[] player2Fog;
    private int[] player1Emissions;
    private int[] player2Emissions;
    private int[] player1Occupancy;
    private int[] player2Occupancy;
     
    private FullGameState getFullGameState() {
        return new FullGameState(game, winner, turn, player1Coffer, player2Coffer,
                player1Squares, player2Squares, 
                player1PieceLocations, player2PieceLocations);
    }

    private GameState getPlayerGameState(Perspective perspective) {
        Board board;
        if(perspective == Perspective.PLAYER_ONE) {
            board = new Board(player1VisibleSquares, player1PieceLocations,
                    player1Player2PieceLocations);
            return new GameState(game, winner, turn, player1Coffer, board);
        } else {
            board = new Board(player2VisibleSquares, player2PieceLocations,
                   player2Player1PieceLocations);
            return new GameState(game, winner, turn, player2Coffer, board);
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
            countOccupancy(player1Commands, player1PieceLocations, player1Occupancy);
            removeCollisions(player1Commands, player1PieceLocations, player1Occupancy);
        } while (anyCollisions(player1Occupancy));
        
        movePieces(player1Commands, player1PieceLocations);
        
        do {
            countOccupancy(player2Commands, player2PieceLocations, player2Occupancy);
            removeCollisions(player2Commands, player2PieceLocations, player2Occupancy);
        } while (anyCollisions(player2Occupancy));
        
        movePieces(player2Commands, player2PieceLocations);
        
        //
        // Resolve deployment.
        //
        
        deployPieces(player1Commands, player1PieceLocations, player1Coffer, player1VisibleSquares);
        deployPieces(player2Commands, player2PieceLocations, player2Coffer, player2VisibleSquares);
        
        // TODO: Resolve farming.
        
        // TODO: Resolve razing.
        
        // TODO: Add coffer.
        
        player1Coffer.deposit(64);
        player2Coffer.deposit(64);
        
        // TODO: Check win condition.
        
        //
        // Apply fog to resulting game state.
        //
        
        driftFog(player1PieceLocations, player1Fog);
        driftFog(player2PieceLocations, player2Fog);
        
        applyFog(player1Squares, player1Fog, player1VisibleSquares, player2PieceLocations, player1Player2PieceLocations);
        applyFog(player2Squares, player2Fog, player2VisibleSquares, player1PieceLocations, player2Player1PieceLocations);
        
        turn += 1;
        
        return;
    }
    
    private static void driftFog(Bijection<Piece, Location> playerPieceLocations, boolean[] fog) throws InvariantViolation {
    
        for (int i=0; i < Configuration.MAX_INDEX; i++) {
            fog[i] = true;
        }
        
        for (Piece piece : playerPieceLocations.getDomain()) {
            List<Location> inSight;
            try {
                inSight = Location.radius(playerPieceLocations.mapForward(piece), piece.getSight());
            } catch (BijectionException e) {
                throw new InvariantViolation();
            }
            for(Location location : inSight) {
                fog[location.getIndex()] = false;
            }
        }
    }

    private static void deployPieces(Map<Piece, Command> commands, Bijection<Piece, Location> pieceLocations,
            Coffer coffer, Square[] squares) throws InvariantViolation {
        Iterator<Map.Entry<Piece, Command>> iterator = commands.entrySet().iterator();
        Map.Entry<Piece, Command> entry;
        Piece newPiece;
        Location target;
        Square square;
        Command command;
        int cost;
        
        try {
            while(iterator.hasNext()) {
                entry = iterator.next();
                command = entry.getValue();
                if (command.getWord() != Command.Word.DEPLOY) {
                    continue;
                }
                newPiece = entry.getKey();
                if (pieceLocations.domainContains(newPiece)) {
                    // There is a small probability that a new piece a player generates will be
                    // the cosmic twin of one of their existing pieces (share UUIDs). If this
                    // happens, the universe will unfortunately disallow it from existing on the
                    // same plane. This is a feature.
                    continue;
                }
                target = command.getTarget();
                if (pieceLocations.codomainContains(target)) {
                    continue;
                }
                square = squares[target.getIndex()];
                if (!square.isDeployable()) {
                    continue;
                }
                cost = Piece.cost(newPiece);
                coffer.withdraw(cost);
                try {
                    pieceLocations.addPairing(newPiece, target);
                } catch (BijectionException exception) {
                    throw new InvariantViolation();
                }
            }
        } catch (CofferEmpty exception) {
            return;
        }   
    }
    
    private static void movePieces(Map<Piece, Command> playerCommands, Bijection<Piece, Location> playerPieceLocations)
            throws InvariantViolation {
        Bijection<Piece, Location> playerPieceLocationsHolder = new Bijection<Piece, Location>();
        Command command;
        Location pieceLocation, target;
        try {
            for (Piece storedPiece : playerPieceLocations.getDomain()) {
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
            
            for (Piece storedPiece : playerPieceLocationsHolder.getDomain()) {
                pieceLocation = playerPieceLocationsHolder.mapForward(storedPiece);
                playerPieceLocations.addPairing(storedPiece, pieceLocation);
            }
        } catch (BijectionException exception) {
            throw new InvariantViolation();
        }
    }
    
    private static void removeCollisions(Map<Piece, Command> playerCommands, Bijection<Piece, Location> playerPieceLocations,
            int[] playerOccupancy) throws InvariantViolation {
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
            if (playerOccupancy[target.getIndex()] > 1) {
                iterator.remove();
            }
        }
    }
   
    private static void countOccupancy(Map<Piece, Command> playerCommands, Bijection<Piece, Location> playerPieceLocations,
            int[] playerOccupancy) throws InvariantViolation {
        Command command;
        Location pieceLocation, target;
        
        for (int i=0; i<Configuration.MAX_INDEX; i++) {
            playerOccupancy[i] = 0;
        }
        
        for (Piece storedPiece : playerPieceLocations.getDomain()) {
            try {
                pieceLocation = playerPieceLocations.mapForward(storedPiece);
            } catch (BijectionException exception) {
                throw new InvariantViolation();
            }
            if (!playerCommands.containsKey(storedPiece)) {
                playerOccupancy[pieceLocation.getIndex()] += 1;
                continue;
            }
            command = playerCommands.get(storedPiece);
            if (command.getWord() != Command.Word.MOVE) {
                playerOccupancy[pieceLocation.getIndex()] += 1;
                continue;
            }
            target = command.getTarget();
            if (Location.distance(target, pieceLocation) > storedPiece.getSpeed()) {
                playerOccupancy[pieceLocation.getIndex()] += 1;
                continue;
            }
            playerOccupancy[target.getIndex()] += 1;
        }
    }
    
    private static boolean anyCollisions(int[] playerOccupancy) {
        for (int i=0; i<Configuration.MAX_INDEX; i++) {
            if (playerOccupancy[i] > 1) return true;
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
            if (command.getWord() == Command.Word.ATTACK) {
                target = command.getTarget();
                if (Location.distance(pieceLocation, target) <= storedPiece.getRange()) {
                    playerEmissions[target.getIndex()] += storedPiece.getStrength();
                }
            }
        }
    }
    
    private static void eliminatePieces(Bijection<Piece, Location> playerPieceLocations, int[] opponentEmissions)
            throws InvariantViolation {
        Location location;
        for (Piece piece : playerPieceLocations.getDomain()) {
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

    private static void applyFog(Square[] squares, boolean[] playerFog, Square[] playerSquares,
            Bijection<Piece, Location> opponentPieceLocations, Bijection<Piece, Location> playerOpponentPieceLocations
            ) throws InvariantViolation {
        playerOpponentPieceLocations.clear();
        Location location;
        Piece piece;
        
        for (int i=0; i<Configuration.MAX_INDEX; i++) {
            if (playerFog[i]) {
                playerSquares[i] = squares[i].setState(Square.State.FOG);
            } else {
                playerSquares[i] = squares[i];
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

    public GameEngine() throws InvariantViolation {
        game = UUID.randomUUID();
        turn = 0;
        winner = 0;
        player1Coffer = new Coffer(Configuration.INITIAL_COFFER_AMOUNT);
        player2Coffer = new Coffer(Configuration.INITIAL_COFFER_AMOUNT);
        player1Squares = new Square[Configuration.MAX_INDEX];
        player2Squares = new Square[Configuration.MAX_INDEX];
        player1VisibleSquares = new Square[Configuration.MAX_INDEX];
        player2VisibleSquares = new Square[Configuration.MAX_INDEX];
        player1PieceLocations = new Bijection<Piece, Location>();
        player2PieceLocations = new Bijection<Piece, Location>();
        player1Player2PieceLocations = new Bijection<Piece, Location>();
        player2Player1PieceLocations = new Bijection<Piece, Location>();
        
        player1Fog = new boolean[Configuration.MAX_INDEX];
        player2Fog = new boolean[Configuration.MAX_INDEX];
        player1Emissions = new int[Configuration.MAX_INDEX];
        player2Emissions = new int[Configuration.MAX_INDEX];
        player1Occupancy = new int[Configuration.MAX_INDEX];
        player2Occupancy = new int[Configuration.MAX_INDEX];
        
        for (int x=0; x < Configuration.MAX_X; x++) {
            for (int y=0; y < Configuration.MAX_Y; y++) {
                Location location;
                try {
                    location = new Location(x, y);
                } catch (LocationDoesNotExist e) {
                    throw new InvariantViolation();
                }
                player1Squares[location.getIndex()] = new Square(location, Square.State.UNOCCUPIED, Square.Terrain.GRASSLAND);
                player2Squares[location.getIndex()] = new Square(location, Square.State.UNOCCUPIED, Square.Terrain.GRASSLAND);
                player1VisibleSquares[location.getIndex()] = new Square(location, Square.State.FOG, Square.Terrain.GRASSLAND);
                player2VisibleSquares[location.getIndex()] = new Square(location, Square.State.FOG, Square.Terrain.GRASSLAND);
            }
        }
    }
    
    public int play(Player player1, Player player2, List<Observer> observers, int maxTurns) throws InvariantViolation {
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
            
            if (turn == maxTurns) break;
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
