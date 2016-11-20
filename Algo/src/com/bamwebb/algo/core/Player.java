package com.bamwebb.algo.core;

public interface Player {
    
    public String name();
    
    public PlayerResponse play(GameState state);
}
