package com.bamwebb.algo.core;

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

	final private Location location;
	final private State state;
	final private Terrain terrain;
	
	@Override
	public int hashCode() {
	    return location.hashCode();
	}
	
	public Square(Location location, State state, Terrain terrain) {
		this.location = location;
		this.state = state;
		this.terrain = terrain;
	}

	public Location getLocation() {
	    return location;
	}
	
	public State getState() {
	    return state;
	}
	
	public Terrain getTerrain() {
	    return terrain;
	}
	
	public Square setLocation(Location location) {
	    return new Square(location, state, terrain);
	}
	
	public Square setState(State state) {
	    return new Square(location, state, terrain);
	}
	
	public Square setTerrain(Terrain terrain) {
	    return new Square(location, state, terrain);
	}
}
