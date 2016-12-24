package com.bamwebb.algo.observers;

import com.bamwebb.algo.core.FullGameState;
import com.bamwebb.algo.core.GameState;
import com.bamwebb.algo.core.Observer;

public class StdoutObserver implements Observer {

    @Override
    public void observe(FullGameState fullGameState, GameState playe1GameState,
            GameState player2GameState) {
        String status = String.format("Turn: %010d | Player 1 Units: %05d | Player 2 Units: %05d", fullGameState.getTurn(),
                fullGameState.getPlayer1PieceLocations().size(), fullGameState.getPlayer2PieceLocations().size() );
        System.out.println(status);
    }

}
