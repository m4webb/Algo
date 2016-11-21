package com.bamwebb.algo.zombies;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

import com.bamwebb.algo.core.Command;
import com.bamwebb.algo.core.Location;
import com.bamwebb.algo.core.Piece;
import com.bamwebb.algo.data.ReadOnlyList;
import com.bamwebb.algo.data.ReadOnlyMap;

public class Zombie extends Piece {
    
    public static final int STRENGTH = 3;
    public static final int TOUGHNESS = 1;
    public static final int SPEED = 3;
    public static final int REACH = 1;
    public static final int SIGHT = 5;
    public static final int GLORY = 1;
    
    public static final int COST = Piece.cost(new Piece(STRENGTH, TOUGHNESS, SPEED, REACH, SIGHT, GLORY)); // 21
    
    private Piece meat;
    private Random brains;
    
    public Zombie() {
        super(STRENGTH, TOUGHNESS, SPEED, REACH, SIGHT, GLORY);
        brains = new Random();
        meat = null;
    }
    
    public Command lurk(Location currentLocation, ReadOnlyMap<Location, Piece> locationOpponentPieces,
            ReadOnlyMap<Piece, Location> opponentPieceLocations) {
        Location meatLocation;
        
        if (meat != null) {
            if (opponentPieceLocations.containsKey(meat)) {
                meatLocation = opponentPieceLocations.get(meat);
                if (Location.distance(currentLocation, meatLocation) <= REACH) {
                    return new Command(Command.Word.ATTACK, meatLocation);
                } else {
                    return new Command(Command.Word.MOVE, Location.inTheDirectionOf(currentLocation, meatLocation, SPEED));
                }
            } else {
                meat = null;
            }
        }
    
        ReadOnlyList<Location> inSight = Location.radius(currentLocation, SIGHT);
        Enumeration<Location> inSightEnumeration = inSight.enumerate();
        List<Piece> menu = new ArrayList<Piece>();
        Location maybeMeatLocation;
        
        while(inSightEnumeration.hasMoreElements()) {
            maybeMeatLocation = inSightEnumeration.nextElement();
            if (locationOpponentPieces.containsKey(maybeMeatLocation)) {
                menu.add(locationOpponentPieces.get(maybeMeatLocation));
            }
        }
        
        if (!menu.isEmpty()) {
            int meatDeJour = brains.nextInt(menu.size());
            meat = menu.get(meatDeJour);
            return lurk(currentLocation, locationOpponentPieces, opponentPieceLocations);
        } else {
            int wanderIndex = brains.nextInt(inSight.size());
            Location wanderDestination = inSight.get(wanderIndex);
            return new Command(Command.Word.MOVE, Location.inTheDirectionOf(currentLocation, wanderDestination, SPEED));
        }
    }
}
