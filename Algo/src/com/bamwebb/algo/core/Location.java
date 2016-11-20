package com.bamwebb.algo.core;

import java.util.ArrayList;
import java.util.List;

import com.bamwebb.algo.data.ReadOnlyList;

public class Location {
    
    public static class LocationDoesNotExist extends Exception {}
    
    public static Location fromIndex(int i) throws LocationDoesNotExist {
        if(i < 0 || i >= Configuration.MAX_INDEX) {
            throw new LocationDoesNotExist();
        }
        return new Location(i/Configuration.MAX_Y, i%Configuration.MAX_Y);
    }
    

    public static int distance(Location locationOne, Location locationTwo) {
        int dx = Math.abs(locationOne.getX() - locationTwo.getX());
        int dy = Math.abs(locationOne.getY() - locationTwo.getY());
        return dx + dy;
    }
    
    public static ReadOnlyList<Location> radius(Location location, int radius) {
        List<Location> locations = new ArrayList<Location>();
        Location locationCandidate;
        int x = location.getX();
        int y = location.getY();
        for(int i=x-radius; i<x+radius; i++) {
            for(int j=y-radius; j<y+radius; j++) {
                try {
                    locationCandidate = new Location(i, j);
                } catch(LocationDoesNotExist exception) {
                    continue;
                }
                if(distance(location, locationCandidate) > radius) {
                    continue;
                }
                locations.add(locationCandidate);
            }
        }
        return new ReadOnlyList<Location>(locations);
    }
    
    private int x;
    private int y;
    
    @Override
    public int hashCode() {
        return getIndex();
    }
    
    public Location(int x, int y) throws LocationDoesNotExist {
        if(x < 0 || x >= Configuration.MAX_X || y < 0 || y >= Configuration.MAX_Y){
            throw new LocationDoesNotExist();
        }
        this.x = x;
        this.y = y;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getIndex() {
        return x*Configuration.MAX_Y + y;
    }
    
    public Location invertPerspective() throws InvariantViolation {
        try {
            return fromIndex(Configuration.MAX_INDEX - getIndex() - 1);
        } catch (LocationDoesNotExist exception) {
            throw new InvariantViolation();
        }
    }
}
