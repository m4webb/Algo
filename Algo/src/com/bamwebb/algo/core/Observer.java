package com.bamwebb.algo.core;

public interface Observer {

    public void observe(FullGameState fullGameState, GameState playe1GameState, GameState player2GameState);
}
