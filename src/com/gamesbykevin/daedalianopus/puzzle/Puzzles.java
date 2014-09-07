package com.gamesbykevin.daedalianopus.puzzle;

import com.gamesbykevin.framework.base.Cell;
import com.gamesbykevin.framework.input.Keyboard;
import com.gamesbykevin.framework.resources.Disposable;
import com.gamesbykevin.framework.util.Timer;

import com.gamesbykevin.daedalianopus.engine.Engine;
import com.gamesbykevin.daedalianopus.puzzle.piece.Piece;
import com.gamesbykevin.daedalianopus.puzzle.piece.Pieces;
import com.gamesbykevin.daedalianopus.resources.GameAudio;
import com.gamesbykevin.daedalianopus.resources.GameImages;
import com.gamesbykevin.daedalianopus.shared.IElement;
import com.gamesbykevin.framework.util.Timers;
import java.awt.Color;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public final class Puzzles implements Disposable, IElement
{
    //list of puzzles
    private List<Puzzle> puzzles;
    
    //the current puzzle
    private int index = 0;
    
    //game cursor
    private Image mouse;
    
    //do we hide the cursor
    private boolean hide = false;
    
    //where the cursor is to be displayed
    private Cell location;
    
    //starting puzzle
    private static final int DEFAULT_PUZZLE = 0;
    
    //the center point of the screen
    private Point center;
    
    //keep track of time passed, and countdown till next level
    private Timer timer, countdown;
    
    //time until next level
    private static final long NEXT_LEVEL_COUNTDOWN = Timers.toNanoSeconds(5000L);
    
    public Puzzles(final Engine engine)
    {
        //store the mouse image
        this.mouse = engine.getResources().getGameImage(GameImages.Keys.Cursor);
        
        //get window where game play takes place
        Rectangle window = engine.getMain().getScreen();
        
        //create new timer
        this.timer = new Timer();
        
        //create new countdown
        this.countdown = new Timer(NEXT_LEVEL_COUNTDOWN);
        
        //the location of the cursor
        this.location = new Cell();
        
        //center place of map
        this.center = new Point(window.x + (window.width / 2), window.y + (window.height / 2));
        
        //create container for the standard puzzles
        this.puzzles = new ArrayList<>();
        
        //create all the standard puzzles
        for (int i = 0; i < PuzzleHelper.Type.values().length; i++)
        {
            this.puzzles.add(PuzzleHelper.create(PuzzleHelper.Type.values()[i]));
        }
        
        //default first puzzle
        setCurrent(DEFAULT_PUZZLE);
    }
    
    @Override
    public void dispose()
    {
        if (mouse != null)
        {
            mouse.flush();
            mouse = null;
        }
        
        for (int i = 0; i < puzzles.size(); i++)
        {
            puzzles.get(i).dispose();
            puzzles.set(i, null);
        }
        
        this.timer = null;
        this.center = null;
        
        this.puzzles.clear();
        this.puzzles = null;
    }
    
    @Override
    public void update(final Engine engine)
    {
        //don't continue if we solved the puzzle
        if (getPuzzle().isSolved())
        {
            if (countdown.hasTimePassed())
            {
                //time passed, now start next puzzle
                setCurrent(getCurrent() + 1);
                
                //reset timers
                timer.reset();
                countdown.reset();
                
                try
                {
                    //reset
                    engine.getManager().reset(engine);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            else
            {
                //update timer
                countdown.update(engine.getMain().getTime());
                return;
            }
        }
        
        //update timer
        timer.update(engine.getMain().getTime());
        
        //get keyboard input
        Keyboard keyboard = engine.getKeyboard();
        
        //the object containing the pieces
        Pieces pieces = engine.getManager().getPieces();
        
        if (keyboard.hasKeyPressed(KeyEvent.VK_LEFT))
        {
            location.decreaseCol();
            
            //update selected piece location
            if (pieces.hasSelection())
            {
                pieces.getSelection().setCol(location);
                
                //play move sound effect
                engine.getResources().playGameAudio(GameAudio.Keys.SfxMove);
            }
        }
        else if (keyboard.hasKeyPressed(KeyEvent.VK_RIGHT))
        {
            location.increaseCol();
            
            //update selected piece location
            if (pieces.hasSelection())
            {
                pieces.getSelection().setCol(location);
                
                //play move sound effect
                engine.getResources().playGameAudio(GameAudio.Keys.SfxMove);
            }
        }
        else if (keyboard.hasKeyPressed(KeyEvent.VK_UP))
        {
            location.decreaseRow();
            
            if (location.getRow() < 0)
                location.increaseRow();
            
            //update selected piece location
            if (pieces.hasSelection())
            {
                pieces.getSelection().setRow(location);
                
                //play move sound effect
                engine.getResources().playGameAudio(GameAudio.Keys.SfxMove);
            }
        }
        else if (keyboard.hasKeyPressed(KeyEvent.VK_DOWN))
        {
            location.increaseRow();
            
            //update selected piece location
            if (pieces.hasSelection())
            {
                pieces.getSelection().setRow(location);
                
                //play move sound effect
                engine.getResources().playGameAudio(GameAudio.Keys.SfxMove);
            }
        }
        else if (keyboard.hasKeyPressed(KeyEvent.VK_SPACE))
        {
            if (pieces.hasSelection())
            {
                pieces.getSelection().rotate();
                
                //play move sound effect
                engine.getResources().playGameAudio(GameAudio.Keys.SfxRotate);
            }
        }
        else if (keyboard.hasKeyPressed(KeyEvent.VK_A))
        {
            if (pieces.hasSelection())
            {
                pieces.getSelection().flipHorizontal();
                
                //play move sound effect
                engine.getResources().playGameAudio(GameAudio.Keys.SfxFlip);
            }
        }
        else if (keyboard.hasKeyPressed(KeyEvent.VK_S))
        {
            if (pieces.hasSelection())
            {
                pieces.getSelection().flipVertical();
                
                //play move sound effect
                engine.getResources().playGameAudio(GameAudio.Keys.SfxFlip);
            }
        }
        else if (keyboard.hasKeyPressed(KeyEvent.VK_ENTER))
        {
            //if there is already a selected piece
            if (pieces.hasSelection())
            {
                //make sure placed piece doesn't collide with any other
                if (!pieces.intersects())
                {
                    //is part of the current piece on the puzzle
                    final boolean intersects = getPuzzle().intersects(pieces.getSelection());
                    
                    //is the current piece entirely inside the puzzle
                    final boolean inside = getPuzzle().isValid(pieces.getSelection());
                    
                    //make sure piece is either completely in the puzzle or out
                    if (intersects && inside || !intersects)
                    {
                        //no longer hide cursor
                        hide = false;

                        //unselect current
                        pieces.setSelection(Pieces.NO_SELECTION);

                        if (pieces.hasSolved(getPuzzle()))
                        {
                            //mark it solved
                            getPuzzle().markSolved();
                            
                            //hide cursor
                            hide = true;
                            
                            //stop all other sound
                            engine.getResources().stopAllSound();
                            
                            //play victory sound
                            engine.getResources().playGameAudio(GameAudio.Keys.MusicVictory);
                        }
                        else
                        {
                            //play place sound effect
                            engine.getResources().playGameAudio(GameAudio.Keys.SfxPlace);
                        }
                    }
                    else
                    {
                        //invalid place, play sound effect
                        engine.getResources().playGameAudio(GameAudio.Keys.SfxInvalid);
                    }
                }
                else
                {
                    //invalid place, play sound effect
                    engine.getResources().playGameAudio(GameAudio.Keys.SfxInvalid);
                }
            }
            else
            {
                //set selection based where the cursor is located
                pieces.setSelection(location);
                
                //if we have found a selection, hide cursor
                hide = pieces.hasSelection();
                
                if (hide)
                    engine.getResources().playGameAudio(GameAudio.Keys.SfxPickup);
            }
        }
        
        //reset input
        keyboard.reset();
    }
    
    /**
     * The index of the current puzzle in play
     * @return The index of the list of the current puzzle
     */
    private int getCurrent()
    {
        return this.index;
    }
    
    /**
     * Set the current puzzle we are to play
     * @param index The position in the list of puzzles that we want to play.
     */
    public void setCurrent(final int index)
    {
        //store index location
        this.index = index;
        
        //reset timer when setting puzzle
        this.timer.reset();
        
        //now that current puzzle is set, position in center
        getPuzzle().setLocation(
            center.x - ((getPuzzle().getCols() / 2) * Puzzle.BLOCK_SIZE), 
            center.y - ((getPuzzle().getRows() / 2) * Puzzle.BLOCK_SIZE));
        
        //set start cursor location
        resetCursorLocation();
    }
    
    /**
     * Reset the cursor to be in the center of the puzzle
     */
    private void resetCursorLocation()
    {
        //don't hide cursor
        this.hide = false;
        
        //position in middle
        location.setCol(getPuzzle().getCols() / 2);
        location.setRow(getPuzzle().getRows() / 2);
    }
    
    /**
     * Get the puzzle
     * @return The current puzzle in play
     */
    public Puzzle getPuzzle()
    {
        return puzzles.get(getCurrent());
    }
    
    @Override
    public void render(final Graphics graphics)
    {
        //draw puzzle
        getPuzzle().render(graphics);
        
        //draw game status
        graphics.setColor(Color.BLACK);
        graphics.drawString("Time - " + timer.getDescPassed(Timers.FORMAT_3), 225, 40);
        
        if (getPuzzle().isSolved())
        {
            graphics.drawString("Next - " + countdown.getDescRemaining(Timers.FORMAT_5), 50, 40);
        }
        else
        {
            graphics.drawString("Level - " + (index+1), 50, 40);
        }
    }
    
    public void renderCursor(final Graphics graphics)
    {
        //make sure we aren't hiding the cursor
        if (!hide)
        {
            if (location != null)
            {
                //calculate current location
                final int x = (int)(getPuzzle().getX() + (location.getCol() * Puzzle.BLOCK_SIZE)) + (Puzzle.BLOCK_SIZE / 2);
                final int y = (int)(getPuzzle().getY() + (location.getRow() * Puzzle.BLOCK_SIZE)) + (Puzzle.BLOCK_SIZE / 2);
                
                //draw cursor
                graphics.drawImage(mouse, x, y, null);
            }
        }
    }
}