package com.bamwebb.algo;

import java.util.List;

public interface Player {
	
	public List<Command> play(GameState state);
}
