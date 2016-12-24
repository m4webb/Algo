package com.bamwebb.algo.observers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.bamwebb.algo.core.Configuration;
import com.bamwebb.algo.core.FullGameState;
import com.bamwebb.algo.core.GameState;
import com.bamwebb.algo.core.InvariantViolation;
import com.bamwebb.algo.core.Location;
import com.bamwebb.algo.core.Location.LocationDoesNotExist;
import com.bamwebb.algo.core.Observer;

public class PNGObserver implements Observer {
      
    private class ImagePanel extends JPanel{

        private BufferedImage image;

        public ImagePanel(BufferedImage image) {
            this.image = image;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this); // see javadoc for more info on the parameters            
        }

    }
    
    private int skip;
    private int expand;
    private String directory;
    private JFrame frame;
    private ImagePanel panel;
    private BufferedImage pixels;
    
    final private static int RGB_WHITE = new Color(255, 255, 255).getRGB();
    final private static int RGB_BLACK = new Color(0, 0, 0).getRGB();
    final private static int RGB_GRAY = new Color(128, 128, 128).getRGB();
    final private static int RGB_BOARD = new Color(218,165,32).getRGB();
    
    public PNGObserver(int skip, int expand, String directory) {
        this.skip = skip;
        this.expand = expand;
        this.directory = directory;
        pixels = new BufferedImage(Configuration.MAX_X*expand, Configuration.MAX_Y*expand, BufferedImage.TYPE_INT_RGB);
        frame = new JFrame();
        panel = new ImagePanel(pixels);
        frame.add(panel);
        frame.setVisible(true);
        frame.setBounds(100, 100, Configuration.MAX_X*expand + 50, Configuration.MAX_Y*expand + 50);
    }
    
    @Override
    public void observe(FullGameState fullGameState, GameState playe1GameState,
            GameState player2GameState) {
        
        if (skip != 0 && fullGameState.getTurn() % skip != 0) {
            return;
        }
        
        try {
            int rgb;
            for (int x=0; x < Configuration.MAX_X; x++) {
                for (int y=0; y < Configuration.MAX_Y; y++) {
                    Location location;
                    location = new Location(x, y);
                    boolean whitePresent = fullGameState.getPlayer1PieceLocations().codomainContains(location);
                    boolean blackPresent = fullGameState.getPlayer2PieceLocations().codomainContains(location.invertPerspective());
                    if (whitePresent && blackPresent) {
                        rgb = RGB_GRAY;
                    } else if (whitePresent) {
                        rgb = RGB_WHITE;
                    } else if (blackPresent) {
                        rgb = RGB_BLACK;
                    } else {
                        rgb = RGB_BOARD;
                    }
                    for (int i=0; i<expand; i++) {
                        for (int j=0; j<expand; j++) {
                            pixels.setRGB(x*expand + i, y*expand + j, rgb);
                        }
                    }
                }
            }
        } catch (LocationDoesNotExist e) {
            return;
        } catch (InvariantViolation e) {
            return;
        }
        
        panel.repaint();
        
        if (directory != null) {
            try {
                String fileName = String.format("%s/zombies.%010d.png", directory, fullGameState.getTurn());
                ImageIO.write(pixels, "png", new File(fileName));
            } catch (IOException e) {
                return;
            }
        }
        
        return;
    }
    
}
