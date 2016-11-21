package com.bamwebb.algo.observers;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.bamwebb.algo.core.Configuration;
import com.bamwebb.algo.core.FullGameState;
import com.bamwebb.algo.core.GameState;
import com.bamwebb.algo.core.InvariantViolation;
import com.bamwebb.algo.core.Location;
import com.bamwebb.algo.core.Location.LocationDoesNotExist;
import com.bamwebb.algo.core.Observer;

public class PNGObserver implements Observer {
    
    private int skip;
    private String directory;
    
    final private static int RGB_WHITE = new Color(255, 255, 255).getRGB();
    final private static int RGB_BLACK = new Color(0, 0, 0).getRGB();
    final private static int RGB_GRAY = new Color(128, 128, 128).getRGB();
    final private static int RGB_BOARD = new Color(218,165,32).getRGB();
    
    public PNGObserver(int skip, String directory) {
        this.skip = skip;
        this.directory = directory;
    }
    
    @Override
    public void observe(FullGameState fullGameState, GameState playe1GameState,
            GameState player2GameState) {
        
        if (skip != 0 && fullGameState.getTurn() % skip != 0) {
            return;
        }
        
        BufferedImage pixels = new BufferedImage(Configuration.MAX_X, Configuration.MAX_Y, BufferedImage.TYPE_INT_RGB);
        
        try {
            int rgb;
            for (int x=0; x < Configuration.MAX_X; x++) {
                for (int y=0; y < Configuration.MAX_Y; y++) {
                    Location location;
                    location = new Location(x, y);
                    boolean whitePresent = fullGameState.getPlayer1LocationPieces().containsKey(location);
                    boolean blackPresent = fullGameState.getPlayer2LocationPIeces().containsKey(location.invertPerspective());
                    if (whitePresent && blackPresent) {
                        rgb = RGB_GRAY;
                    } else if (whitePresent) {
                        rgb = RGB_WHITE;
                    } else if (blackPresent) {
                        rgb = RGB_BLACK;
                    } else {
                        rgb = RGB_BOARD;
                    }
                    pixels.setRGB(x, y, rgb);
                }
            }
        } catch (LocationDoesNotExist e) {
            return;
        } catch (InvariantViolation e) {
            return;
        }
        
        try {
            String fileName = String.format("%s/zombies.%010d.png", directory, fullGameState.getTurn());
            ImageIO.write(pixels, "png", new File(fileName));
        } catch (IOException e) {
            return;
        }
        
        return;
    }
    
}
