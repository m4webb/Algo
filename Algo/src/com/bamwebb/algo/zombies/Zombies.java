package com.bamwebb.algo.zombies;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.bamwebb.algo.core.Board;
import com.bamwebb.algo.core.Board.PieceNotOnBoard;
import com.bamwebb.algo.core.Command;
import com.bamwebb.algo.core.Configuration;
import com.bamwebb.algo.core.GameState;
import com.bamwebb.algo.core.Location;
import com.bamwebb.algo.core.Location.LocationDoesNotExist;
import com.bamwebb.algo.core.Piece;
import com.bamwebb.algo.core.Player;
import com.bamwebb.algo.core.PlayerResponse;
import com.bamwebb.algo.data.ReadOnlyList;

public class Zombies implements Player {
    
    private Random brains;
    
    public Zombies() {
        brains = new Random();
    }
    
    @Override
    public String name() {
        return "Zombies!";
    }

    @Override
    public PlayerResponse play(GameState state) {
        Board board = state.getBoard();
        Map<Piece, Command> commands = new HashMap<Piece, Command>();
        ReadOnlyList<Piece> pieces = board.getPieces();
        Enumeration<Piece> piecesEnumeration = pieces.enumerate();
        Zombie zombie;
        
        //
        // Zombies lurk!
        //
        while(piecesEnumeration.hasMoreElements()) {
            zombie = (Zombie) piecesEnumeration.nextElement();
            try {
                Command command = zombie.lurk(board.getLocation(zombie), board.getlocationOpponentPieces(),
                        board.getOpponentPieceLocations());
                commands.put(zombie, command);    
            } catch (PieceNotOnBoard e) {
                continue;
            }
        }
        
        //
        // More zombies!
        //
        for (int z=0; z < state.getCoffer().peek() / Zombie.COST; z++ ) {
            int deployX = brains.nextInt(Configuration.DEPLOY_HORIZON);
            int deployY = brains.nextInt(Configuration.DEPLOY_HORIZON);
            try {
                commands.put(new Zombie(), new Command(Command.Word.DEPLOY, new Location(deployX, deployY)));
            } catch (LocationDoesNotExist e) {
                continue;
            }
        }
        
        return new PlayerResponse(commands);
    }
}
