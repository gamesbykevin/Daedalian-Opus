package com.gamesbykevin.daedalianopus.manager;

import com.gamesbykevin.framework.menu.Menu;
import com.gamesbykevin.framework.util.*;

import com.gamesbykevin.daedalianopus.engine.Engine;
import com.gamesbykevin.daedalianopus.menu.CustomMenu;
import com.gamesbykevin.daedalianopus.menu.CustomMenu.*;
import com.gamesbykevin.daedalianopus.resources.*;
import com.gamesbykevin.daedalianopus.shared.Shared;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

/**
 * The parent class that contains all of the game elements
 * @author GOD
 */
public final class Manager implements IManager
{
    //where gameplay occurs
    private Rectangle window;
    
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

        //maps = new Maps(engine.getResources().getGameImage(GameImages.Keys.Maps), getWindow());
        //hero.setImage(engine.getResources().getGameImage(GameImages.Keys.Heroes));
        //enemies = new Enemies(engine.getResources().getGameImage(GameImages.Keys.Enemies));
        
        //reset game
        reset(engine);
    }
    
    @Override
    public void reset(final Engine engine) throws Exception
    {
        //determine how many lives to set
        //switch (engine.getMenu().getOptionSelectionIndex(CustomMenu.LayerKey.Options, CustomMenu.OptionKey.Lives))
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
        
    }
    
    /**
     * Draw all of our application elements
     * @param graphics Graphics object used for drawing
     */
    @Override
    public void render(final Graphics graphics)
    {
        
    }
}