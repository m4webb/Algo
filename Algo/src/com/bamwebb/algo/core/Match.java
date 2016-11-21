package com.bamwebb.algo.core;

import java.util.ArrayList;
import java.util.List;

import com.bamwebb.algo.observers.PNGObserver;
import com.bamwebb.algo.zombies.Zombies;

public class Match {

    public static void main(String[] args) throws InvariantViolation {
        
        GameEngine engine = new GameEngine();
        Player player1 = new Zombies();
        Player player2 = new Zombies();
        Observer observer = new PNGObserver(0, "/home/matthew/zombies");
        List<Observer> observers = new ArrayList<Observer>();
        observers.add(observer);
        
        engine.play(player1, player2, observers, 10000);
        
    }

}
