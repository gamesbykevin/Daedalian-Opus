package com.gamesbykevin.daedalianopus.intermission;

import com.gamesbykevin.framework.base.Animation;
import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;
import com.gamesbykevin.framework.util.Timer;

import com.gamesbykevin.daedalianopus.engine.Engine;
import com.gamesbykevin.daedalianopus.puzzle.Puzzle;
import com.gamesbykevin.daedalianopus.puzzle.piece.Piece;
import com.gamesbykevin.daedalianopus.puzzle.piece.Pieces;
import com.gamesbykevin.daedalianopus.puzzle.piece.PiecesHelper;
import com.gamesbykevin.daedalianopus.resources.GameAudio;
import com.gamesbykevin.daedalianopus.shared.IElement;
import com.gamesbykevin.framework.util.Timers;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.Random;

public class Intermission implements Disposable, IElement
{
    //is this active
    private boolean active = false;
    
    //should we reset
    private boolean reset = true;
    
    //the dimensions of each building
    private static final int BUILDING_WIDTH = 128;
    private static final int BUILDING_HEIGHT = 160;
    
    //the dimensions of the player
    private static final int PLAYER_WIDTH = 25;
    private static final int PLAYER_HEIGHT = 40;
    
    //what is the current level
    private int current;
    
    //the buildings
    private Sprite buildingWest, buildingEast;
    
    //the player
    private Sprite player;
    
    //background to draw
    private Image emptyBackground;
    
    //duration of intermission
    private long DELAY = Timers.toNanoSeconds(10000L);
    
    //timer for intermission
    private Timer timer;
    
    //the start and goal for the player
    private static final int START_X  = 130;
    private static final int START_Y = 350;
    private static final int FINISH_X = 416;
    
    //the locations for the buildings
    private static final Point WEST = new Point(50, 192);
    private static final Point EAST = new Point(334, 192);
    
    //the new piece to be displayed
    private Piece newPiece;
    
    private enum BuildingType
    {
        Building1, Building2, Building3, Building4, Building5 
    }
    
    public Intermission(final Image buildings, final Image player, final Image emptyBackground)
    {
        //create new timer
        this.timer = new Timer(DELAY);
        
        //store image reference
        this.emptyBackground = emptyBackground;
        
        this.buildingWest = new Sprite();
        this.buildingWest.setImage(buildings);
        this.buildingWest.createSpriteSheet();
        
        this.buildingEast = new Sprite();
        this.buildingEast.setImage(buildings);
        this.buildingEast.createSpriteSheet();
        
        this.player = new Sprite();
        this.player.setLocation(164, 220);
        this.player.setImage(player);
        this.player.createSpriteSheet();
        
        //setup animations
        setupAnimations();
    }
    
    private void setupAnimations()
    {
        //default delay time
        final long delay = Timers.toNanoSeconds(175L);
        
        //create new animations
        Animation animation1 = new Animation(BUILDING_WIDTH * 0, 0, BUILDING_WIDTH, BUILDING_HEIGHT, delay);
        Animation animation2 = new Animation(BUILDING_WIDTH * 1, 0, BUILDING_WIDTH, BUILDING_HEIGHT, delay);
        Animation animation3 = new Animation(BUILDING_WIDTH * 2, 0, BUILDING_WIDTH, BUILDING_HEIGHT, delay);
        Animation animation4 = new Animation(BUILDING_WIDTH * 3, 0, BUILDING_WIDTH, BUILDING_HEIGHT, delay);
        Animation animation5 = new Animation(BUILDING_WIDTH * 4, 0, BUILDING_WIDTH, BUILDING_HEIGHT, delay);
        
        //setup building sprites
        this.buildingEast.getSpriteSheet().add(animation1, BuildingType.Building1);
        this.buildingEast.getSpriteSheet().add(animation2, BuildingType.Building2);
        this.buildingEast.getSpriteSheet().add(animation3, BuildingType.Building3);
        this.buildingEast.getSpriteSheet().add(animation4, BuildingType.Building4);
        this.buildingEast.getSpriteSheet().add(animation5, BuildingType.Building5);
        
        //setup building sprites
        this.buildingWest.getSpriteSheet().add(animation1, BuildingType.Building1);
        this.buildingWest.getSpriteSheet().add(animation2, BuildingType.Building2);
        this.buildingWest.getSpriteSheet().add(animation3, BuildingType.Building3);
        this.buildingWest.getSpriteSheet().add(animation4, BuildingType.Building4);
        this.buildingWest.getSpriteSheet().add(animation5, BuildingType.Building5);
        
        Animation east = new Animation();
        east.add(PLAYER_WIDTH * 0, PLAYER_HEIGHT * 0, PLAYER_WIDTH, PLAYER_HEIGHT, delay);
        east.add(PLAYER_WIDTH * 1, PLAYER_HEIGHT * 0, PLAYER_WIDTH, PLAYER_HEIGHT, delay);
        east.add(PLAYER_WIDTH * 2, PLAYER_HEIGHT * 0, PLAYER_WIDTH, PLAYER_HEIGHT, delay);
        east.add(PLAYER_WIDTH * 3, PLAYER_HEIGHT * 0, PLAYER_WIDTH, PLAYER_HEIGHT, delay);
        east.add(PLAYER_WIDTH * 4, PLAYER_HEIGHT * 0, PLAYER_WIDTH, PLAYER_HEIGHT, delay);
        east.setLoop(true);
        
        /*
        Animation north = new Animation();
        north.add(PLAYER_WIDTH * 0, PLAYER_HEIGHT * 1, PLAYER_WIDTH, PLAYER_HEIGHT, delay);
        north.add(PLAYER_WIDTH * 1, PLAYER_HEIGHT * 1, PLAYER_WIDTH, PLAYER_HEIGHT, delay);
        north.add(PLAYER_WIDTH * 2, PLAYER_HEIGHT * 1, PLAYER_WIDTH, PLAYER_HEIGHT, delay);
        north.add(PLAYER_WIDTH * 3, PLAYER_HEIGHT * 1, PLAYER_WIDTH, PLAYER_HEIGHT, delay);
        north.add(PLAYER_WIDTH * 4, PLAYER_HEIGHT * 1, PLAYER_WIDTH, PLAYER_HEIGHT, delay);
        north.setLoop(true);
        */
        
        this.player.getSpriteSheet().add(east, "Default");
    }
    
    /**
     * Reset the player location and pick random building(s)
     * @param random
     * @param current 
     * @param pieces
     */
    private void reset(final Random random, final int current, final Pieces pieces)
    {
        //no longer need to reset
        this.reset = false;
        
        //store the current level
        this.current = current;
        
        switch(current)
        {
            case 1:
                newPiece = pieces.getPiece(PiecesHelper.Type.PinkBox);
                break;
                
            case 2:
                newPiece = pieces.getPiece(PiecesHelper.Type.GreenLine);
                break;
                
            case 3:
                newPiece = pieces.getPiece(PiecesHelper.Type.DarkBlueL);
                break;
                
            case 4:
                newPiece = pieces.getPiece(PiecesHelper.Type.RedC);
                break;
                
            case 5:
                newPiece = pieces.getPiece(PiecesHelper.Type.BlueZ);
                break;
                
            case 7:
                newPiece = pieces.getPiece(PiecesHelper.Type.BrownPlus);
                break;
                
            case 9:
                newPiece = pieces.getPiece(PiecesHelper.Type.DarkGreenSteps);
                break;
                
            case 10:
                newPiece = pieces.getPiece(PiecesHelper.Type.LightGreenZ);
                break;
                
            case 11:
                newPiece = pieces.getPiece(PiecesHelper.Type.YellowMisc);
                break;
        }
        
        //reset timer
        this.timer.reset();
        
        //start the player walking east
        this.player.getSpriteSheet().setCurrent("Default");
        this.player.setDimensions();
        this.player.setLocation(START_X, START_Y);
        
        //choose 2 random buildings
        this.buildingWest.getSpriteSheet().setCurrent(BuildingType.values()[random.nextInt(BuildingType.values().length)]);
        this.buildingWest.setLocation(WEST);
        this.buildingWest.setDimensions();
        
        this.buildingEast.getSpriteSheet().setCurrent(BuildingType.values()[random.nextInt(BuildingType.values().length)]);
        this.buildingEast.setLocation(EAST);
        this.buildingEast.setDimensions();
    }
    
    @Override
    public void dispose()
    {
        if (buildingWest != null)
        {
            buildingWest.dispose();
            buildingWest = null;
        }
        
        if (buildingEast != null)
        {
            buildingEast.dispose();
            buildingEast = null;
        }
        
        if (player != null)
        {
            player.dispose();
            player = null;
        }
        
        timer = null;
    }
    
    public void markActive()
    {
        this.active = true;
        this.reset = true;
    }
    
    private void unflagActive()
    {
        this.active = false;
    }
    
    public boolean isActive()
    {
        return this.active;
    }
    
    @Override
    public void update(final Engine engine) throws Exception
    {
        //if not active don't continue
        if (!isActive())
            return;
        
        //do we reset
        if (reset)
        {
            //stop the sound
            engine.getResources().stopAllSound();
            
            //play intermission sound
            engine.getResources().playGameAudio(GameAudio.Keys.MusicIntermission);
            
            reset(
                engine.getRandom(), 
                engine.getManager().getPuzzles().getCurrent(), 
                engine.getManager().getPieces()
            );
        }
        
        //if timer has expired no longer active
        if (timer.hasTimePassed())
        {
            //unflag active
            unflagActive();
            
            //stop the sound
            engine.getResources().stopAllSound();
            
            //start theme again
            engine.getResources().playGameAudio(GameAudio.Keys.MusicTheme, true);
        }
        
        //get time deduction
        final long time = engine.getMain().getTime();
        
        //update timer
        this.timer.update(time);
        
        //update player location
        this.player.setX(START_X + ((FINISH_X - START_X) * timer.getProgress()));
        this.player.getSpriteSheet().update(time);
    }
    
    @Override
    public void render(final Graphics graphics)
    {
        //if not active or need to reset don't continue
        if (!isActive() || reset)
            return;
        
        //draw backgroun
        graphics.drawImage(emptyBackground, 0, 0, null);
        
        //draw buildings
        buildingEast.draw(graphics);
        buildingWest.draw(graphics);
        
        //draw the level #'s on buildings
        graphics.drawString("#" + (current + 0), WEST.x + 35, WEST.y + 53);
        graphics.drawString("#" + (current + 1), EAST.x + 35, EAST.y + 53);
        
        //draw the new piece if one exists and is valid
        if (newPiece != null && newPiece.isValid())
        {
            graphics.drawString("New Piece", 190, 192 - (Puzzle.BLOCK_SIZE * 4));
            newPiece.setLocation(256, 202);
            newPiece.render(graphics);
        }
        
        final double x = player.getX();
        final double y = player.getY();
        
        //offset location
        player.setX(x - (player.getWidth() / 2));
        player.setY(y - (player.getHeight() / 2));
        
        //draw player
        player.draw(graphics);
        
        //reset location
        player.setLocation(x, y);
    }
}
