package com.bamwebb.algo.core;

import java.util.List;
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
     
    private void applyFog() throws InvariantViolation {
        player1Player2PieceLocations.clear();
        player2Player1PieceLocations.clear();
        Location location;
        Piece piece;
        
        for(int i=0; i<Configuration.MAX_INDEX; i++) {
            if(player1Fog[i]) {
                player1Squares[i] = squares[i].setState(Square.State.FOG);
            } else {
                player1Squares[i] = squares[i];
                try {
                    location = Location.fromIndex(i);
                    if(player2PieceLocations.codomainContains(location.invertPerspective())) {
                        piece = player2PieceLocations.mapBackward(location.invertPerspective());
                        player1Player2PieceLocations.addPairing(piece, location);
                    }
                } catch (LocationDoesNotExist exception) {
                    throw new InvariantViolation();
                } catch (BijectionException exception) {
                    throw new InvariantViolation();
                }
            }
            if(player2Fog[Configuration.MAX_INDEX - i]) {
                player2Squares[Configuration.MAX_INDEX - i - 1] = squares[i].setState(Square.State.FOG);
            } else {
                player2Squares[Configuration.MAX_INDEX - i - 1] = squares[i];
                try {
                    location = Location.fromIndex(Configuration.MAX_INDEX - i - 1);
                    if(player1PieceLocations.codomainContains(location.invertPerspective())) {
                        piece = player1PieceLocations.mapBackward(location.invertPerspective());
                        player2Player1PieceLocations.addPairing(piece, location);
                    }
                } catch (LocationDoesNotExist exception) {
                    throw new InvariantViolation();
                } catch (BijectionException exception) {
                    throw new InvariantViolation();
                }
            }
        }
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
    
    private FullGameState getFullGameState() {
        return new FullGameState(game, turn, winner, player1Resources, player2Resources, new ReadOnlyArray<Square>(squares),
                player1PieceLocations.getForwardReadOnlyMap(), player2PieceLocations.getForwardReadOnlyMap(),
                player2PieceLocations.getBackwardReadOnlyMap(), player2PieceLocations.getBackwardReadOnlyMap());
    }

    private void resolveCommands(List<Command> player1Commands, List<Command> player2Commands) throws InvariantViolation {
        applyFog();
        return;
    }
    
    public GameEngine() {
        game = UUID.randomUUID();
        turn = 0;
        winner = 0;
        player1Resources = Configuration.INITIAL_RESOURCES;
        player2Resources = Configuration.INITIAL_RESOURCES;
        squares = new Square[Configuration.MAX_INDEX];
        player1Squares = new Square[Configuration.MAX_INDEX];
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
        List<Command> player1Commands, player2Commands;
        
        while(winner == 0) {
            fullGameState = getFullGameState();
            player1GameState = getPlayerGameState(Perspective.PLAYER_ONE);
            player2GameState = getPlayerGameState(Perspective.PLAYER_TWO);
            
            player1Commands = player1.play(player1GameState);
            player2Commands = player2.play(player2GameState);
            for(Observer observer : observers) {
                observer.observe(fullGameState, player1GameState, player2GameState);
            }
            
            resolveCommands(player1Commands, player2Commands);
        }
        
        fullGameState = getFullGameState();
        player1GameState = getPlayerGameState(Perspective.PLAYER_ONE);
        player2GameState = getPlayerGameState(Perspective.PLAYER_TWO);
        
        for(Observer observer : observers) {
            observer.observe(fullGameState, player1GameState, player2GameState);
        }
        
        return winner;
    }
}
