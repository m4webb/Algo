package com.bamwebb.algo.core;

public class Coffer {
    
    public static class CofferEmpty extends Exception {}

    private int coins;
    
    public Coffer(int coins) {
        this.coins = coins;
    }
    
    public int peek() {
        return coins;
    }
    
    void deposit(int value) {
        coins += value;
    }
    
    void withdraw(int value) throws CofferEmpty {
        if (value > coins) {
            throw new CofferEmpty();
        }
        coins -= value;
    }
}