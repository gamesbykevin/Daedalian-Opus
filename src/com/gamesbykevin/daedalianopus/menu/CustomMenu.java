package com.gamesbykevin.daedalianopus.menu;

import com.gamesbykevin.framework.display.FullScreen;
import com.gamesbykevin.framework.input.Mouse;
import com.gamesbykevin.framework.menu.*;
import com.gamesbykevin.framework.resources.FontManager;
import com.gamesbykevin.framework.resources.ImageManager;

import com.gamesbykevin.daedalianopus.engine.Engine;
import com.gamesbykevin.daedalianopus.resources.Resources;
import com.gamesbykevin.daedalianopus.shared.IElement;
import com.gamesbykevin.daedalianopus.shared.Shared;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

/**
 * Custom menu setup
 * @author GOD
 */
public final class CustomMenu extends Menu implements IElement
{
    //object used to switch container to full screen
    private FullScreen fullScreen;
    
    //previous Layer key used so when container loses focus we remember where we were at
    private Object previousLayerKey;
    
    /**
     * identify each option we want to access, spelling should match the options id=? in the .xml file
     */
    public enum OptionKey 
    {
        Sound, FullScreen, Mode, Level, Difficulty
    }
    
    /**
     * unique key to identify each Layer, spelling should match the layer id=? in the .xml file
     */
    public enum LayerKey 
    {
        Initial, Credits, MainTitle, Options, OptionsInGame, NewGameConfirm, 
        ExitGameConfirm, NoFocus, GameStart, CreateNewGame, Controls, Instructions, 
        ExitGameConfirmed 
    }
    
    /**
     * Basic option selection on/off, must match text in menu.xml to work properly
     */
    public enum Toggle
    {
        Off, On 
    }
    
    //is full screen enabled, default false
    //private Toggle fullscreen = Toggle.Off;
    
    //store the indec of the full screen setting
    private int fullscreenIndex = 0;
    
    //does the container have focus
    private Toggle focus = Toggle.On;
    
    //here the images for the mouse cursor will be contained
    private ImageManager images;
    
    /**
     * Unique id's used to access the resources and must match the id in the xml file
     */
    private enum MouseKey
    {
        Mouse, MouseDrag
    }
    
    //object that contains all the fonts for the menu
    private FontManager fonts;
    
    //the default font size
    private static final float DEFAULT_FONT_SIZE = 26f;
    
    /**
     * Unique id's used to access the resources and must match the id in the xml file
     */
    private enum FontKey
    {
        Menu
    }
    
    //the mouse images are provided in this node name
    private static final String MOUSE_NODE_NAME = "mouseImage";
    
    public CustomMenu(final Engine engine) throws Exception
    {
        //set the container the menu will reside within
        super(engine.getMain().getScreen(), Resources.XML_CONFIG_MENU, engine.getMain().getContainerClass());
        
        //set the first layer
        super.setLayer(LayerKey.Initial);
        
        //if debugging go straight to game
        if (Shared.DEBUG)
            super.setLayer(LayerKey.GameStart);
        
        //set the last layer so we know when the menu has completed
        super.setFinish(LayerKey.GameStart);
        
        //create container for mouse cursor images, use special node name for the mouse images
        this.images = new ImageManager(Resources.XML_CONFIG_MENU, MOUSE_NODE_NAME);
        
        //load the mouse images
        while(!images.isComplete())
        {
            images.update(engine.getMain().getContainerClass());
        }
        
        //verify if anything is incorrect
        images.verifyLocations(MouseKey.values());
        
        //create container for any menu fonts
        this.fonts = new FontManager(Resources.XML_CONFIG_MENU);
        
        //load the font(s)
        while(!fonts.isComplete())
        {
            fonts.update(engine.getMain().getContainerClass());
        }
        
        //verify if anything is incorrect
        fonts.verifyLocations(FontKey.values());
        
        //get the font just added and change the Font Size
        fonts.set(FontKey.Menu, fonts.get(FontKey.Menu).deriveFont(DEFAULT_FONT_SIZE));
    }
    
    /**
     * Update game menu
     * @param engine Our game engine containing all resources etc... needed to update menu
     * 
     * @throws Exception 
     */
    @Override
    public void update(final Engine engine) throws Exception
    {
        //if the menu is not on the last layer we need to check for changes made in the menu
        if (!super.hasFinished())
        {
            //the option selection for the sound and fullscreen
            int tmpFullscreenIndex = fullscreenIndex;
            
            //are we currently in the in-game options
            final boolean isInGameOptionsLayer = super.isCurrentLayer(LayerKey.OptionsInGame);
            
            //if starting a new game change layer, stop all sound
            if (super.isCurrentLayer(LayerKey.CreateNewGame))
            {
                //go to specified layer
                super.setLayer(LayerKey.GameStart);
            }
            
            //if we are in the in-game options layer
            if (isInGameOptionsLayer)
            {
                //determine the current selection for sound
                final Toggle tmp = Toggle.values()[getOptionSelectionIndex(LayerKey.OptionsInGame, OptionKey.Sound)];

                //the first index is off, even though the menu says on
                final boolean enabled = (tmp == Toggle.Off);
                
                //set the audio enabled/disabled depending on setting
                engine.getResources().setAudioEnabled(enabled);
                
                if (!enabled)
                    engine.getResources().stopAllSound();
                
                //if the audio is off then on, or on then off set the value in the menu
                if (!isEnabled() && enabled || isEnabled() && !enabled)
                {
                    //set the audio enabled/disabled in the menu as well
                    super.setEnabled(enabled);
                }
                
                //get the setting for the fullscreen window
                tmpFullscreenIndex = getOptionSelectionIndex(LayerKey.OptionsInGame, OptionKey.FullScreen);
            }
                
            //if on the options screen check fullscreen setting
            if (super.isCurrentLayer(LayerKey.Options))
                tmpFullscreenIndex = getOptionSelectionIndex(LayerKey.Options, OptionKey.FullScreen);
            
            //if the values are not equal to each other a change was made
            if (tmpFullscreenIndex != fullscreenIndex)
            {
                if (fullScreen == null)
                    fullScreen = new FullScreen();

                //switch fullscreen
                if (engine.getMain().getApplet() != null)
                {
                    fullScreen.switchFullScreen(engine.getMain().getApplet());
                }
                else
                {
                    fullScreen.switchFullScreen(engine.getMain().getPanel());
                }
                
                //grab the rectangle coordinates of the full screen
                engine.getMain().setFullScreen();

                //store the new setting
                this.fullscreenIndex = tmpFullscreenIndex;
            }
            
            //does the container have focus
            final Toggle tmpFocus = (engine.getMain().hasFocus()) ? Toggle.On : Toggle.Off;
            
            //if the values are not equal a change was made
            if (focus != tmpFocus)
            {
                //if the previous Layer is stored
                if (previousLayerKey != null)
                {
                    //set the menu to the previous Layer
                    super.setLayer(previousLayerKey);
                    
                    //there no longer is a previous Layer
                    previousLayerKey = null;
                }
                else
                {
                    //the previous Layer has not been set 
                    previousLayerKey = getKey();
                    
                    //set the current Layer to NoFocus
                    super.setLayer(LayerKey.NoFocus);
                }
                
                this.focus = tmpFocus;
            }
            
            super.update(engine.getMouse(), engine.getKeyboard(), engine.getMain().getTime());
            
            //if confirming exit from the game, stop sound
            if (super.isCurrentLayer(LayerKey.ExitGameConfirmed))
            {
                //recycle game related objects
                engine.setReset();
                
                //go to specified layer
                super.setLayer(LayerKey.MainTitle);
            }
            
            //if we now have finished the menu flag the engine to reset
            if (super.hasFinished())
            {
                //if we previously weren't in this layer reset the game
                if (!isInGameOptionsLayer)
                    engine.setReset();
            }
        }
        else
        {
            //the menu has finished and the user has pressed 'escape' so we will bring up the in game options
            if (engine.getKeyboard().hasKeyPressed(KeyEvent.VK_ESCAPE))
            {
                super.setLayer(LayerKey.OptionsInGame);
                engine.getKeyboard().reset();
            }
        }
    }
    
    public boolean hasFocus()
    {
        return (this.focus == Toggle.On);
    }
    
    @Override
    public void render(final Graphics graphics)
    {
        //set menu font
        graphics.setFont(fonts.get(FontKey.Menu));
        
        //draw menu
        super.render(graphics);
    }
    
    /**
     * Draw the mouse
     * @param graphics Graphics object used to write mouse
     * @param mouse Object representing the state of the mouse
     */
    public void renderMouse(final Graphics graphics, final Mouse mouse)
    {
        if (mouse.getLocation() != null && images != null)
        {
            if (mouse.isMouseDragged())
            {
                graphics.drawImage(images.get(MouseKey.MouseDrag), mouse.getLocation().x, mouse.getLocation().y, null);
            }
            else
            {
                graphics.drawImage(images.get(MouseKey.Mouse), mouse.getLocation().x, mouse.getLocation().y, null);
            }
        }
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        
        if (fullScreen != null)
        {
            fullScreen.dispose();
            fullScreen = null;
        }
        
        if (images != null)
        {
            images.dispose();
            images = null;
        }

        if (fonts != null)
        {
            fonts.dispose();
            fonts = null;
        }
        
        previousLayerKey = null;
    }
}