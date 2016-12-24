package com.bamwebb.algo.core;

import java.util.ArrayList;
import java.util.List;

public class Location {
    
    public static class LocationDoesNotExist extends Exception {}
    
    public static final LocationDoesNotExist LOCATION_DOES_NOT_EXIST = new LocationDoesNotExist();
    
    public static Location fromIndex(int i) throws LocationDoesNotExist {
        if(i < 0 || i >= Configuration.MAX_INDEX) {
            throw LOCATION_DOES_NOT_EXIST;
        }
        return new Location(i/Configuration.MAX_Y, i%Configuration.MAX_Y);
    }
    
    /*
    public static Location inTheDirectionOf(Location from, Location to, int speed) {
        int dx, dy, targetDx, targetDy, magnitude;
        double scaledDx, scaledDy;
        
        if (Location.distance(from, to) <= speed) {
            return to;
        }
        
        dx = to.getX() - from.getX();
        dy = to.getY() - from.getY();
        magnitude = Math.abs(dx) + Math.abs(dy);
        scaledDx = (double) dx * speed / magnitude;
        scaledDy = (double) dy * speed / magnitude;
        
        if (scaledDx % 1 != 0) {
            if (scaledDx % 1 > scaledDy % 1) {
                targetDx = dx * speed / magnitude + 1 * (int) Math.signum(scaledDx);
                targetDy = dy * speed / magnitude;
                
            } else {
                targetDx = dx * speed / magnitude;
                targetDy = dy * speed / magnitude + 1 * (int) Math.signum(scaledDy);
            }
        } else {
            targetDx = dx * speed / magnitude;
            targetDy = dy * speed / magnitude;
        }
        
        try {
            return new Location(from.getX() + targetDx, from.getY() + targetDy);
        } catch (LocationDoesNotExist e) {
            return null;
        }
    }
    */
    
    public static Location inTheDirectionOf(Location from, Location to, int speed) {
        int dx = to.getX() - from.getX();
        int dy = to.getY() - from.getY();
        int targetDx = Math.min(Math.abs(dx), speed) * (int)Math.signum(dx);
        int targetDy = Math.min(Math.abs(dy), speed) * (int)Math.signum(dy);
        try {
            return new Location(from.getX() + targetDx, from.getY() + targetDy);
        } catch (LocationDoesNotExist e) {
            return null;
        }
    }

    public static int distance(Location locationOne, Location locationTwo) {
        int dx = Math.abs(locationOne.getX() - locationTwo.getX());
        int dy = Math.abs(locationOne.getY() - locationTwo.getY());
        //return dx + dy;
        return Math.max(dx, dy);
    }
    
    /*
    public static List<Location> radius(Location location, int radius) {
        List<Location> locations = new ArrayList<Location>();
        int x = location.getX();
        int y = location.getY();
        try{ 
            for(int i=-radius; i<=radius; i++) {
                for(int j=-(radius-Math.abs(i)); j<=radius-Math.abs(i); j++) {
                    if (x + i < 0 || x + i >= Configuration.MAX_X || y + j < 0 || y + j >= Configuration.MAX_Y) {
                        continue;
                    }
                    locations.add(new Location(x + i, y + j));
                }
            }
        } catch (LocationDoesNotExist exception) {
            return null;
        }
        
        return locations;
    }
    */
    
    public static List<Location> radius(Location location, int radius) {
        List<Location> locations = new ArrayList<Location>();
        try {
            for (int dx=-radius; dx<=radius; dx++) {
                for (int dy=-radius; dy<=radius; dy++) {
                    int targetX = location.getX() + dx;
                    int targetY = location.getY() + dy;
                    if (targetX < 0 || targetX >= Configuration.MAX_X || targetY < 0 || targetY >= Configuration.MAX_Y) {
                        continue;
                    }
                    locations.add(new Location(targetX, targetY));
                }
            }         
        } catch (LocationDoesNotExist e) {
            return null;
        }
        return locations;
    }
    
    private int x;
    private int y;
    
    @Override
    public int hashCode() {
        return getIndex();
    }
    
    @Override
    public boolean equals(Object object) {
        Location location;
        if (object == null) return false;
        if (object.getClass() == this.getClass()) {
            location = (Location) object;
            return location.getX() == x && location.getY() == y;
        }
        return false;
    }
    
    public Location(int x, int y) throws LocationDoesNotExist {
        if(x < 0 || x >= Configuration.MAX_X || y < 0 || y >= Configuration.MAX_Y){
            throw LOCATION_DOES_NOT_EXIST;
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
            return new Location(Configuration.MAX_X - getX() - 1, Configuration.MAX_Y - getY() - 1);
        } catch (LocationDoesNotExist exception) {
            throw new InvariantViolation();
        }
    }
}
