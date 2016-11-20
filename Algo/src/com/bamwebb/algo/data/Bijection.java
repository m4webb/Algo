package com.bamwebb.algo.data;

import java.util.HashMap;
import java.util.Map;

public class Bijection<A,B> {
    
    public static class BijectionException extends Exception {
        
        public BijectionException(String message) {
            super(message);
        }
    }

    private Map<A,B> mapAB;
    private Map<B,A> mapBA;
    
    public Bijection() {
        mapAB = new HashMap<A, B>();
        mapBA = new HashMap<B, A>();
    }
    
    public void clear() {
        mapAB.clear();
        mapBA.clear();
    }
    
    public void addPairing(A a, B b) throws BijectionException {
        if(mapAB.containsKey(a) || mapBA.containsKey(b)) {
            throw new Bijection.BijectionException("Pairing does not contain new values.");
        }
        mapAB.put(a, b);
        mapBA.put(b, a);
    }
    
    public boolean domainContains(A a) {
        return mapAB.containsKey(a);
    }
    
    public boolean codomainContains(B b) {
        return mapBA.containsKey(b);
    }

    public B mapForward(A a) throws BijectionException {
        if(!(mapAB.containsKey(a))) {
            throw new Bijection.BijectionException("Value not found in domain.");
        }
        return mapAB.get(a);
    }
    
    public A mapBackward(B b) throws BijectionException {
        if(!(mapBA.containsKey(b))) {
            throw new Bijection.BijectionException("Value not found in codomain.");
        }
        return mapBA.get(b);
    }
    
    public void removeForward(A a) throws BijectionException {
        if(!(mapAB.containsKey(a))) {
            throw new Bijection.BijectionException("Value not found in domain");
        }
        B b = mapAB.remove(a);
        mapBA.remove(b);
    }
    
    public void removeBackward(B b) throws BijectionException {
        if(!(mapBA.containsKey(b))) {
            throw new Bijection.BijectionException("Value not found in codomain.");
        }
        A a = mapBA.remove(b);
        mapAB.remove(a);
    }
    
    public void removePairing(A a, B b) throws BijectionException {
        if(!(mapAB.containsKey(a) && mapBA.containsKey(b))) {
            throw new Bijection.BijectionException("Pairing values not found.");
        }
        else if(!(mapAB.get(a) == b && mapBA.get(b) == a)) {
            throw new Bijection.BijectionException("Pairing does not match.");
        }
        else {
            mapAB.remove(a);
            mapBA.remove(b);
        }
    }
    
    public ReadOnlyMap<A,B> getForwardReadOnlyMap() {
        return new ReadOnlyMap<A,B>(mapAB);
    }
    
    public ReadOnlyMap<B,A> getBackwardReadOnlyMap() {
        return new ReadOnlyMap<B,A>(mapBA);
    }
}
