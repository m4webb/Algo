package com.bamwebb.algo.core;

public class Coffer {
    
    public static class CofferEmpty extends Exception {}
    
    public static class ReadOnlyCoffer {
        
        private final Coffer coffer;
        
        public ReadOnlyCoffer(Coffer coffer) {
            this.coffer = coffer;
        }
        
        public int peek() {
            return coffer.peek();
        }
    }

    private int coins;
    
    public Coffer(int coins) {
        this.coins = coins;
    }
    
    public int peek() {
        return coins;
    }
    
    public void deposit(int value) {
        coins += value;
    }
    
    public void withdraw(int value) throws CofferEmpty {
        if (value > coins) {
            throw new CofferEmpty();
        }
        coins -= value;
    }
    
    public ReadOnlyCoffer getReadOnlyCoffer() {
        return new ReadOnlyCoffer(this);
    }
}