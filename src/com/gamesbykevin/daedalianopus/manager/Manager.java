package com.gamesbykevin.daedalianopus.manager;

import com.gamesbykevin.framework.input.Keyboard;
import com.gamesbykevin.framework.menu.Menu;
import com.gamesbykevin.framework.util.*;

import com.gamesbykevin.daedalianopus.engine.Engine;
import com.gamesbykevin.daedalianopus.menu.CustomMenu;
import com.gamesbykevin.daedalianopus.menu.CustomMenu.*;
import com.gamesbykevin.daedalianopus.puzzle.piece.Piece;
import com.gamesbykevin.daedalianopus.puzzle.piece.Pieces;
import com.gamesbykevin.daedalianopus.puzzle.Puzzles;
import com.gamesbykevin.daedalianopus.puzzle.PuzzleHelper;
import com.gamesbykevin.daedalianopus.resources.*;
import com.gamesbykevin.daedalianopus.shared.Shared;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

/**
 * The parent class that contains all of the game elements
 * @author GOD
 */
public final class Manager implements IManager
{
    //where gameplay occurs
    private Rectangle window;
    
    //our standard puzzles
    private Puzzles puzzles;
    
    //our pieces for the specified puzzle
    private Pieces pieces;
    
    //background image
    private Image background;
    
    /**
     * Constructor for Manager, this is the point where we load any menu option configurations
     * @param engine Engine for our game that contains all objects needed
     * @throws Exception 
     */
    public Manager(final Engine engine) throws Exception
    {
        //determine if sound is enabled
        final boolean enabled = (Toggle.values()[engine.getMenu().getOptionSelectionIndex(LayerKey.OptionsInGame, OptionKey.Sound)] == Toggle.Off);

        //set the audio depending on menu setting
        engine.getResources().setAudioEnabled(enabled);
        
        //set the game window where game play will occur
        setWindow(engine.getMain().getScreen());
        
        //create standard puzzles
        this.puzzles = new Puzzles(engine);
        
        //set the level based on the menu option
        this.puzzles.setCurrent(engine.getMenu().getOptionSelectionIndex(CustomMenu.LayerKey.Options, CustomMenu.OptionKey.Level));
        
        //create object that contains all the pieces
        this.pieces = new Pieces();
        
        //store background image
        this.background = engine.getResources().getGameImage(GameImages.Keys.Background);
        
        //maps = new Maps(engine.getResources().getGameImage(GameImages.Keys.Maps), getWindow());
        //hero.setImage(engine.getResources().getGameImage(GameImages.Keys.Heroes));
        //enemies = new Enemies(engine.getResources().getGameImage(GameImages.Keys.Enemies));
        
        //reset game
        reset(engine);
    }
    
    public Puzzles getPuzzles()
    {
        return this.puzzles;
    }
    
    public Pieces getPieces()
    {
        return this.pieces;
    }
    
    @Override
    public void reset(final Engine engine) throws Exception
    {
        //now assign the valid pieces
        this.pieces.setValidPieces(puzzles.getPuzzle().getValidPieces());
        
        //now scramble the pieces on screen
        this.pieces.scramble(puzzles, engine.getRandom());
        
        //stop all sound
        engine.getResources().stopAllSound();
        
        //play main theme
        engine.getResources().playGameAudio(GameAudio.Keys.MusicTheme);
    }
    
    @Override
    public Rectangle getWindow()
    {
        return this.window;
    }
    
    @Override
    public void setWindow(final Rectangle window)
    {
        this.window = new Rectangle(window);
    }
    
    /**
     * Free up resources
     */
    @Override
    public void dispose()
    {
        if (window != null)
            window = null;
        
        if (puzzles != null)
        {
            puzzles.dispose();
            puzzles = null;
        }
        
        if (pieces != null)
        {
            pieces.dispose();
            pieces = null;
        }
        
        try
        {
            //recycle objects
            super.finalize();
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Update all elements
     * @param engine Our game engine
     * @throws Exception 
     */
    @Override
    public void update(final Engine engine) throws Exception
    {
        //update piece location(s)
        pieces.update(engine);
        
        //update puzzle etc...
        puzzles.update(engine);
    }
    
    /**
     * Draw all of our application elements
     * @param graphics Graphics object used for drawing
     */
    @Override
    public void render(final Graphics graphics)
    {
        //draw the background
        graphics.drawImage(background, 0, 0, null);
        
        //draw the current puzzle
        this.puzzles.render(graphics);
        
        //draw the pieces
        this.pieces.render(graphics);
        
        //draw the cursor
        this.puzzles.renderCursor(graphics);
    }
}