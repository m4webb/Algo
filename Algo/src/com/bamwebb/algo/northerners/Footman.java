package com.bamwebb.algo.northerners;

import java.util.Random;

import com.bamwebb.algo.core.Piece;

public class Footman extends Piece {
    
    public static final int STRENGTH = 2;
    public static final int TOUGHNESS = 3;
    public static final int SPEED = 1;
    public static final int REACH = 1;
    public static final int SIGHT = 6;
    public static final int GLORY = 1;
    
    public static final int COST = Piece.cost(new Piece(STRENGTH, TOUGHNESS, SPEED, REACH, SIGHT, GLORY));
    
    public Footman() {
        super(STRENGTH, TOUGHNESS, SPEED, REACH, SIGHT, GLORY);
    }
}
