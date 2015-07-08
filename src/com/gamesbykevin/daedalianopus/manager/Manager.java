package com.gamesbykevin.daedalianopus.manager;

import com.gamesbykevin.framework.input.Keyboard;
import com.gamesbykevin.framework.menu.Menu;
import com.gamesbykevin.framework.util.*;

import com.gamesbykevin.daedalianopus.engine.Engine;
import com.gamesbykevin.daedalianopus.intermission.Intermission;
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
    
    //how we will handle switching to the next level
    private Intermission intermission;
    
    //background and victory image
    private Image background, victory;
    
    //are we generating random Levels
    private CustomMenu.Toggle random;
    
    //is the game over
    private boolean gameover = false;
    
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
        
        //create object that contains all the pieces
        this.pieces = new Pieces();
        
        //store background image
        this.background = engine.getResources().getGameImage(GameImages.Keys.Background);
        
        //store victory image
        this.victory = engine.getResources().getGameImage(GameImages.Keys.Victory);
        
        //create new intermission
        this.intermission = new Intermission(
            engine.getResources().getGameImage(GameImages.Keys.Buildings),
            engine.getResources().getGameImage(GameImages.Keys.Player),
            engine.getResources().getGameImage(GameImages.Keys.EmptyBackground)
            );
        
        //maps = new Maps(engine.getResources().getGameImage(GameImages.Keys.Maps), getWindow());
        //hero.setImage(engine.getResources().getGameImage(GameImages.Keys.Heroes));
        //enemies = new Enemies(engine.getResources().getGameImage(GameImages.Keys.Enemies));
        
        //are we playing random Mode or Campaign
        random = CustomMenu.Toggle.values()[engine.getMenu().getOptionSelectionIndex(CustomMenu.LayerKey.Options, CustomMenu.OptionKey.Mode)];
        
        //set start level
        puzzles.setCurrent(engine.getMenu().getOptionSelectionIndex(CustomMenu.LayerKey.Options, CustomMenu.OptionKey.Level) - 1);
        
        //reset game
        reset(engine);
    }
    
    /**
     * Is random mode selected
     * @return true if so, false otherwise
     */
    public boolean hasRandomMode()
    {
        return (random == Toggle.On);
    }
    
    public Puzzles getPuzzles()
    {
        return this.puzzles;
    }
    
    public Pieces getPieces()
    {
        return this.pieces;
    }
    
    public Intermission getIntermission()
    {
        return this.intermission;
    }
    
    @Override
    public void reset(final Engine engine) throws Exception
    {
        if (hasRandomMode())
        {
            //always will be random
            puzzles.setCurrent(-1);
            
            //get the index of the difficulty selected
            final int index = engine.getMenu().getOptionSelectionIndex(CustomMenu.LayerKey.Options, CustomMenu.OptionKey.Difficulty);
            
            //set the next random puzzle
            puzzles.setRandomPuzzle(PuzzleHelper.createRandom(engine.getRandom(), pieces, PuzzleHelper.Difficulty.values()[index]));
        }
        else
        {
            //show the intermission except for first level
            if (puzzles.getCurrent() > -1)
                intermission.markActive();
            
            //set next puzzle
            puzzles.setCurrent(puzzles.getCurrent() + 1);
            
            //determine if the game is over
            if (puzzles.getCurrent() >= puzzles.getPuzzlesCount())
            {
                gameover = true;
                return;
            }
            
            //mark all pieces invalid
            pieces.markPiecesInvalid();
            
            //now set valid based on current puzzle
            pieces.setValidPieces(puzzles.getPuzzle().getValidPieces());
        }
        
        //position in center
        puzzles.resetPuzzleLocation();
        
        //now scramble the pieces on screen
        pieces.scramble(puzzles, engine.getRandom());

        //stop all sound
        engine.getResources().stopAllSound();

        //if no intermission start playing theme
        if (!intermission.isActive())
        {
            //play main theme
            engine.getResources().playGameAudio(GameAudio.Keys.MusicTheme, true);
        }
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
    
    private boolean hasGameover()
    {
        return this.gameover;
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
        
        if (intermission != null)
        {
            intermission.dispose();
            intermission = null;
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
        if (hasGameover())
            return;
        
        if (intermission.isActive())
        {
            //update object
            intermission.update(engine);
        }
        else
        {
            //update piece location(s)
            pieces.update(engine);

            //update puzzle etc...
            puzzles.update(engine);
        }
    }
    
    /**
     * Draw all of our application elements
     * @param graphics Graphics object used for drawing
     */
    @Override
    public void render(final Graphics graphics) throws Exception
    {
        if (hasGameover())
        {
            //draw the victory screen
            graphics.drawImage(victory, 0, 0, null);
        }
        else
        {
            if (intermission.isActive())
            {
                //draw the intermission
                this.intermission.render(graphics);
            }
            else
            {
                //draw the background
                graphics.drawImage(background, 0, 0, null);

                //draw the current puzzle
                this.puzzles.render(graphics);

                //draw the pieces
                this.pieces.render(graphics);

                //draw the outline of the current puzzle
                this.puzzles.renderOutline(graphics);

                //draw the cursor
                this.puzzles.renderCursor(graphics);
            }
        }
    }
}