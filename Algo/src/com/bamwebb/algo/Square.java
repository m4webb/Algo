package com.bamwebb.algo;

public class Square {
    
    public enum State {
        FOG,
        BARREN,
        PROPERTY,
        OPPONENT_PROPERTY,
    }
    
    public enum Terrain {
        GRASSLAND,
    }

	final private int x;
	final private int y;
	final private State state;
	final private Terrain terrain;
	
	public static int getIndex(int x, int y) {
	    return x*Configuration.MAX_Y + y;
	}
	
	public static int getIndex(Square square) {
	    return getIndex(square.getX(), square.getY());
	}
	
	public Square(int x, int y, State state, Terrain terrain) {
		this.x = x;
		this.y = y;
		this.state = state;
		this.terrain = terrain;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getIndex() {
	    return getIndex(this);
	}
	
	public State getState() {
	    return state;
	}
	
	public Terrain getTerrain() {
	    return terrain;
	}
}
