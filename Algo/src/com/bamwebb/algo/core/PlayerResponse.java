package com.bamwebb.algo.core;

import java.util.Map;

public class PlayerResponse {

    private Map<Piece, Command> commands;
    
    public PlayerResponse(Map<Piece, Command> commands) {
        this.commands = commands;
    }
    
    public Map<Piece, Command> getCommands() {
        return commands;
    }
}
