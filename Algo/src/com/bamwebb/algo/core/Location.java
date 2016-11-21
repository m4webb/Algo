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
    
    public static Location inTheDirectionOf(Location from, Location to, int speed) {
        int dx, dy, targetX, targetY;
        double scaledDx, scaledDy;
        
        if (Location.distance(from, to) <= speed) {
            return to;
        }
        
        dx = to.getX() - from.getX();
        dy = to.getY() - from.getY();
        scaledDx = (double) dx * speed / (dx + dy);
        scaledDy = (double) dy * speed / (dx + dy);
        
        if (scaledDx % 1 != 0) {
            if (scaledDx % 1 > scaledDy % 1) {
                targetX = dx * speed / (dx + dy) + 1;
                targetY = dy * speed / (dx + dy);
                
            } else {
                targetX = dx * speed / (dx + dy);
                targetY = dy * speed / (dx + dy) + 1;
            }
        } else {
            targetX = dx * speed / (dx + dy);
            targetY = dx * speed / (dx + dy);
        }
        
        try {
            return new Location(targetX, targetY);
        } catch (LocationDoesNotExist e) {
            return null;
        }
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
    
    public boolean equals(Location location) {
        return location.getX() == x && location.getY() == y;
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
