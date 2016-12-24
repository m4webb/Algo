package com.bamwebb.algo.core;

import java.util.ArrayList;
import java.util.List;

import com.bamwebb.algo.observers.PNGObserver;
import com.bamwebb.algo.observers.StdoutObserver;
import com.bamwebb.algo.zombies.Zombies;

public class Match {

    public static void main(String[] args) throws InvariantViolation {
        
        GameEngine engine = new GameEngine();
        Player player1 = new Zombies();
        Player player2 = new Zombies();
        Observer pngObserver = new PNGObserver(0, 3, null);
        //Observer pngObserver = new PNGObserver(0, "/home/matthew/zombies");
        Observer stdoutObserver = new StdoutObserver();
        List<Observer> observers = new ArrayList<Observer>();
        observers.add(pngObserver);
        observers.add(stdoutObserver);
        
        engine.play(player1, player2, observers, 20000);
        
    }

}
