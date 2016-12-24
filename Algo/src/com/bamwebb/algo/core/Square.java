package com.bamwebb.algo.core;

public class Square {
    
    public enum State {
        FOG,
        UNOCCUPIED,
        PROPERTY,
        OPPONENT_PROPERTY,
    }
    
    public enum Terrain {
        GRASSLAND,
    }

	final private Location location;
	final private State state;
	final private Terrain terrain;
	
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
	
	public Square setState(State state) {
	    return new Square(location, state, terrain);
	}
	
	public boolean isDeployable() {
	    return location.getX() < Configuration.DEPLOY_HORIZON && location.getY() < Configuration.DEPLOY_HORIZON;
	}
}
