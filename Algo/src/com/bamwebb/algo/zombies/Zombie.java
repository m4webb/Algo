package com.bamwebb.algo.zombies;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.bamwebb.algo.core.Board;
import com.bamwebb.algo.core.Board.LocationHasNoPiece;
import com.bamwebb.algo.core.Board.PieceNotOnBoard;
import com.bamwebb.algo.core.Command;
import com.bamwebb.algo.core.Location;
import com.bamwebb.algo.core.Location.LocationDoesNotExist;
import com.bamwebb.algo.core.Piece;

public class Zombie extends Piece {
    
    public static final int STRENGTH = 2;
    public static final int TOUGHNESS = 3;
    public static final int SPEED = 1;
    public static final int REACH = 1;
    public static final int SIGHT = 6;
    public static final int GLORY = 1;
    
    public static final int COST = Piece.cost(new Piece(STRENGTH, TOUGHNESS, SPEED, REACH, SIGHT, GLORY));
    
    private Piece meat;
    private Random brains;
    
    public Zombie() {
        super(STRENGTH, TOUGHNESS, SPEED, REACH, SIGHT, GLORY);
        brains = new Random();
        meat = null;
    }
    
    public Command lurk(Board board) {
        Location currentLocation = board.getLocationOrNull(this);
        Location meatLocation;
        if (meat != null) {
            try {
                meatLocation = board.getOpponentLocation(meat);
                if (Location.distance(currentLocation, meatLocation) > SIGHT) {
                    meat = null;
                } else if (Location.distance(currentLocation, meatLocation) <= REACH) {
                    return new Command(Command.Word.ATTACK, meatLocation);
                } else {
                    return new Command(Command.Word.MOVE, Location.inTheDirectionOf(currentLocation, meatLocation, SPEED));
                }
            } catch (PieceNotOnBoard e) {
                meat = null;
            }
        }
    
        List<Location> inSight = Location.radius(currentLocation, SIGHT);
        List<Piece> menu = new ArrayList<Piece>();

        for (Location maybeMeatLocation : inSight) {
            try {
                menu.add(board.getOpponentPiece(maybeMeatLocation));
            } catch (LocationHasNoPiece e) {
                continue;
            }
        }
        
        if (!menu.isEmpty()) {
            meat = menu.get(0);
            int closestMeatDistance = Location.distance(board.getOpponentLocationOrNull(meat), currentLocation);
            for (Piece meatOption : menu) {
                int meatOptionDistance = Location.distance(board.getOpponentLocationOrNull(meat), currentLocation);
                if (meatOptionDistance < closestMeatDistance) {
                    closestMeatDistance = meatOptionDistance;
                    meat = meatOption;
                }
            }
            return lurk(board);
        } else {
            Location iHopeTheresMeat;
            double impulse = brains.nextDouble();
            try {
                if (impulse < 0.3) {
                    iHopeTheresMeat = new Location(currentLocation.getX() + SPEED, currentLocation.getY() + SPEED);
                } else if (impulse < 0.5) {
                    iHopeTheresMeat = new Location(currentLocation.getX() + SPEED, currentLocation.getY());
                } else if (impulse < 0.7) {
                    iHopeTheresMeat = new Location(currentLocation.getX(), currentLocation.getY() + SPEED);
                } else if (impulse < 0.8) {
                    iHopeTheresMeat = new Location(currentLocation.getX() + SPEED, currentLocation.getY() - SPEED);
                } else if (impulse < 0.9) {
                    iHopeTheresMeat = new Location(currentLocation.getX() - SPEED, currentLocation.getY() + SPEED);
                } else if (impulse < 0.94) {
                    iHopeTheresMeat = new Location(currentLocation.getX() - SPEED, currentLocation.getY());
                } else if (impulse < 0.98) {
                    iHopeTheresMeat = new Location(currentLocation.getX(), currentLocation.getY() - SPEED);
                } else {
                    iHopeTheresMeat = new Location(currentLocation.getX() - SPEED, currentLocation.getY() - SPEED);
                }
            } catch (LocationDoesNotExist e) {
                iHopeTheresMeat = currentLocation;
            }
            return new Command(Command.Word.MOVE, iHopeTheresMeat);
        }
    }
}
