package com.bamwebb.algo.core;

import java.util.List;

public interface Player {
    
    public String name();
    
    public List<Command> play(GameState state);
}
