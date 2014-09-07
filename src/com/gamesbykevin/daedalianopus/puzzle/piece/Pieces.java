package com.gamesbykevin.daedalianopus.puzzle.piece;

import com.gamesbykevin.framework.base.Cell;
import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.daedalianopus.engine.Engine;
import com.gamesbykevin.daedalianopus.puzzle.Puzzle;
import com.gamesbykevin.daedalianopus.puzzle.Puzzles;
import com.gamesbykevin.daedalianopus.shared.IElement;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class Pieces implements Disposable, IElement
{
    public enum Type
    {
        LightBlueL(102, 255, 255),
        GrayTetrisPiece(64, 64, 64),
        BurgondyT(153, 0, 0),
        PinkBox(255, 0, 255), 
        GreenLine(0, 255, 0),
        DarkBlueL(0, 0, 51),
        RedC(255, 0, 0),
        BlueZ(0, 0, 255), 
        BrownPlus(51, 25, 0), 
        DarkGreenSteps(0, 51, 0), 
        LightGreenZ(0, 204, 102), 
        YellowMisc(255, 255, 0);
        
        //the values that make up the color
        private final int red, green, blue;
        
        private Type(final int red, final int green, final int blue)
        {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }
        
        public int getRed()
        {
            return this.red;
        }
        
        public int getGreen()
        {
            return this.green;
        }
        
        public int getBlue()
        {
            return this.blue;
        }
    }
    
    //all pre-existing pieces for the game
    private List<Piece> pieces;
    
    //the index of the users selected piece
    private int index = NO_SELECTION;
    
    //no piece selected
    public static final int NO_SELECTION = -1;
    
    //how many columns are required on the side
    private static final int SIDE_COLUMN_REQUIREMENT = 5;
    
    //how many rows to move vertically
    private static final int VERTICAL_COLUMN_REQUIREMENT = 5;
    
    //area where pieces can be placed
    private Rectangle piecesArea;
    
    public Pieces()
    {
        //create new area for the pieces
        this.piecesArea = new Rectangle(95, 60, 300, 295);
        
        //all existing pieces
        this.pieces = new ArrayList<>();
        
        //create and add all pieces to list
        for (int i = 0; i < Type.values().length; i++)
        {
            this.pieces.add(create(Type.values()[i]));
        }
    }
    
    /**
     * Is the current puzzle solved?<br>
     * @param puzzle
     * @return true if all pieces in play are inside the puzzle, false otherwise
     */
    public boolean hasSolved(final Puzzle puzzle)
    {
        for (int i = pieces.size() - 1; i >= 0; i--)
        {
            Piece piece = getPiece(i);
            
            //don't check pieces that aren't marked in play
            if (piece == null)
                continue;
            
            if (!puzzle.isValid(piece))
                return false;
        }
        
        return true;
    }
    
    public boolean hasSelection()
    {
        return (index >= 0);
    }
    
    /**
     * Get the current selection
     * @return The puzzle piece we have selected
     */
    public Piece getSelection()
    {
        return getPiece(index);
    }
    
    /**
     * Get the piece
     * @param index Location of piece we want
     * @return The piece we want, if the piece is not in play null will be returned
     */
    private Piece getPiece(final int index)
    {
        //if the piece is not in play return null
        if (!pieces.get(index).isValid())
            return null;
        
        return this.pieces.get(index);
    }
    
    /**
     * Set the selected piece
     * @param location The location of the mouse where we will check
     */
    public void setSelection(final Cell location)
    {
        for (int i = pieces.size() - 1; i >= 0; i--)
        {
            Piece piece = getPiece(i);
            
            if (piece == null)
                continue;
            
            if (piece.hasCollision(location))
            {
                //store selection and return
                setSelection(i);
                return;
            }
        }
        
        //nothing was selected so set no selection
        setSelection(NO_SELECTION);
    }
    
    public void setSelection(final int index)
    {
        this.index = index;
    }
    
    /**
     * Set the current list of pieces that will be used for the current puzzle
     * @param types List of piece types that will be used
     */
    public void setValidPieces(final List<Type> types)
    {
        //first default all to invalid
        for (int i = 0; i < pieces.size(); i++)
        {
            pieces.get(i).setValid(false);
        }
        
        for (int i = 0; i < pieces.size(); i++)
        {
            Piece piece = pieces.get(i);
            
            for (int z = 0; z < types.size(); z++)
            {
                //if it is valid
                if (piece.getType() == types.get(z))
                {
                    piece.setValid(true);
                    break;
                }
            }
        }
    }
    
    /**
     * Does the currently selected piece intersect with any other currently in play?
     * @return true if the location of the current selected piece intersects with another, false otherwise
     */
    public boolean intersects()
    {
        return intersects(getSelection());
    }
    
    /**
     * Do this piece intersect with any other currently in play?
     * @param piece The piece we want to check for collision
     * @return true if the location of the specified piece intersects with another, false otherwise
     */
    public boolean intersects(final Piece piece)
    {
        for (int i = 0; i < pieces.size(); i++)
        {
            Piece tmp = getPiece(i);
            
            //only check pieces in play
            if (tmp == null)
                continue;
            
            //don't check self
            if (piece.getId() == tmp.getId())
                continue;
            
            //does the currect selected piece intersect with this piece
            if (piece.hasCollision(tmp))
                return true;
        }
        
        return false;
    }
    
    /**
     * Place the pieces around the puzzle
     * @param puzzles Object containing the current puzzle
     * @param random Object used to make random decisions
     */
    public void scramble(final Puzzles puzzles, final Random random)
    {
        //get the current puzzle
        Puzzle puzzle = puzzles.getPuzzle();
        
        //adjust y
        puzzle.setY(piecesArea.y);
        
        //keep track of piece index
        int count = 0;
        
        for (int row = 0; row < puzzle.getRows() + (VERTICAL_COLUMN_REQUIREMENT * 2); row++)
        {
            for (int col = -SIDE_COLUMN_REQUIREMENT; col < puzzle.getCols() + SIDE_COLUMN_REQUIREMENT; col++)
            {
                //try to skip general puzzle area
                //if (row >= 0 && row < puzzle.getRows() && col >= 0 && col < puzzle.getCols())
                //    continue;
                
                //we only want to check the pieces that are valid
                while (getPiece(count) == null)
                {
                    count++;
                    
                    //we are done checking all pieces return
                    if (count >= pieces.size())
                        return;
                }
                
                //the current piece we are placing
                Piece piece = getPiece(count);
                
                //set location
                piece.setCol(col);
                piece.setRow(row);
                
                boolean valid = true;
                
                //make sure all locations are within pieces area
                for (int i = 0; i < piece.getPieces().size(); i++)
                {
                    Cell cell = piece.getPieces().get(i);
                    
                    final int tx = (int)(puzzle.getX() + ((piece.getCol() + cell.getCol()) * Puzzle.BLOCK_SIZE));
                    final int ty = (int)(puzzle.getY() + ((piece.getRow() + cell.getRow()) * Puzzle.BLOCK_SIZE));
                    
                    //if this location is not part of the area, it is not a valid location
                    if (!piecesArea.contains(tx, ty))
                    {
                        valid = false;
                        break;
                    }
                }
                
                if (!valid)
                    continue;
                
                //if location is part of puzzle skip
                if (puzzle.intersects(piece))
                    continue;
                
                //don't continue if the location intersects with another piece
                if (intersects(piece))
                    continue;
                
                count++;
                
                //we are done checking all pieces return
                if (count >= pieces.size())
                    return;
            }
        }
    }
    
    @Override
    public void update(final Engine engine)
    {
        //get the current puzzle
        Puzzle puzzle = engine.getManager().getPuzzles().getPuzzle();
        
        //update location of pieces based on their base (col, row)
        for (int i = 0; i < pieces.size(); i++)
        {
            Piece piece = getPiece(i);
            
            //only want pieces in play
            if (piece == null)
                continue;
            
            //set correct coordinates
            piece.setX(puzzle.getX() + (piece.getCol() * Puzzle.BLOCK_SIZE));
            piece.setY(puzzle.getY() + (piece.getRow() * Puzzle.BLOCK_SIZE));
        }
    }
    
    @Override
    public void dispose()
    {
        this.piecesArea = null;
        
        if (pieces != null)
        {
            for (int i = 0; i < pieces.size(); i++)
            {
                pieces.get(i).dispose();
                pieces.set(i, null);
            }

            pieces.clear();
            pieces = null;
        }
    }
    
    /**
     * Create a puzzle piece of specified type
     * @param type The type of piece
     * @return Piece used to play in game
     */
    private static Piece create(final Type type)
    {
        //create new piece
        final Piece piece = new Piece(type);
        
        try
        {
            //add pieces to piece
            switch (type)
            {
                case LightBlueL:
                    piece.add(0, 0);
                    piece.add(1, 0);
                    piece.add(2, 0);
                    piece.add(3, 0);
                    piece.add(0, 1);
                    break;

                case GrayTetrisPiece:
                    piece.add(0, 0);
                    piece.add(1, 0);
                    piece.add(2, 0);
                    piece.add(3, 0);
                    piece.add(1, -1);
                    break;

                case BurgondyT:
                    piece.add(0, 0);
                    piece.add(1, 0);
                    piece.add(2, 0);
                    piece.add(2, -1);
                    piece.add(2, 1);
                    break;

                case PinkBox:
                    piece.add(0, 0);
                    piece.add(0, 1);
                    piece.add(1, 1);
                    piece.add(0, 2);
                    piece.add(1, 2);
                    break;

                case GreenLine:
                    piece.add(0, 0);
                    piece.add(1, 0);
                    piece.add(2, 0);
                    piece.add(3, 0);
                    piece.add(4, 0);
                    break;

                case DarkBlueL:
                    piece.add(0, 0);
                    piece.add(0, 1);
                    piece.add(0, 2);
                    piece.add(1, 2);
                    piece.add(2, 2);
                    break;

                case RedC:
                    piece.add(0, 0);
                    piece.add(1, 0);
                    piece.add(0, 1);
                    piece.add(0, 2);
                    piece.add(1, 2);
                    break;

                case BlueZ:
                    piece.add(0, 0);
                    piece.add(0, 1);
                    piece.add(1, 1);
                    piece.add(1, 2);
                    piece.add(1, 3);
                    break;

                case BrownPlus:
                    piece.add(0, 0);
                    piece.add(1, 0);
                    piece.add(-1, 0);
                    piece.add(0, -1);
                    piece.add(0, 1);
                    break;

                case DarkGreenSteps:
                    piece.add(0, 0);
                    piece.add(0, 1);
                    piece.add(1, 1);
                    piece.add(1, 2);
                    piece.add(2, 2);
                    break;

                case LightGreenZ:
                    piece.add(0, 0);
                    piece.add(0, 1);
                    piece.add(1, 1);
                    piece.add(2, 1);
                    piece.add(2, 2);
                    break;

                case YellowMisc:
                    piece.add(0, 0);
                    piece.add(1, 0);
                    piece.add(1, 1);
                    piece.add(2, 1);
                    piece.add(1, 2);
                    break;

                default:
                    throw new Exception("Piece is not mapped here");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        return piece;
    }
    
    @Override
    public void render(final Graphics graphics)
    {
        if (pieces != null)
        {
            for (int i = 0; i < pieces.size(); i++)
            {
                Piece piece = getPiece(i);
                
                //skip pieces not in play
                if (piece == null)
                    continue;
                
                //draw piece
                piece.render(graphics);
            }
            
            //draw user selection last
            if (hasSelection())
            {
                pieces.get(index).render(graphics);
            }
        }
    }
}