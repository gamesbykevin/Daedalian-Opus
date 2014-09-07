package com.gamesbykevin.daedalianopus.puzzle;

import com.gamesbykevin.framework.base.Cell;
import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;

import com.gamesbykevin.daedalianopus.puzzle.piece.Piece;
import com.gamesbykevin.daedalianopus.puzzle.piece.Pieces;

import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public final class Puzzle extends Sprite implements Disposable
{
    /**
     * The size of the puzzle.<br>
     * Not all areas within these dimensions are valid
     */
    private final int cols, rows;
    
    /**
     * The valid locations for this puzzle
     */
    private List<Cell> valid;
    
    //the size of each small block
    public static final int BLOCK_SIZE = 16;
    
    /**
     * This is for the border thickness
     */
    private BasicStroke stroke;
    
    //border thickness
    private static final int LINE_THICKNESS = 2;
    
    //the pieces needed to solve the puzzle
    private List<Pieces.Type> pieces;
    
    //the type of puzzle
    private final PuzzleHelper.Type type;
    
    private boolean solved = false;
    
    public Puzzle(final PuzzleHelper.Type type)
    {
        //store the type of puzzle
        this.type = type;
        
        //store the dimensions
        this.cols = type.getCol();
        this.rows = type.getRow();
        
        //create new list that contains the valid locations
        this.valid = new ArrayList<>();
        
        //the list of pieces for this puzzle
        this.pieces = new ArrayList<>();
    }
    
    public void markSolved()
    {
        this.solved = true;
    }
    
    /**
     * Has the puzzle been solved
     * @return true if all pieces in play have been placed inside the puzzle, false otherwise
     */
    public boolean isSolved()
    {
        return this.solved;
    }
    
    public PuzzleHelper.Type getType()
    {
        return this.type;
    }
    
    /**
     * Add piece to puzzle
     * @param piece The puzzle piece needed to solve puzzle.
     */
    public void add(final Pieces.Type type)
    {
        try
        {
            for (int i= 0; i < pieces.size(); i++)
            {
                if (pieces.get(i) == type)
                    throw new Exception("Piece already has been added. " + type.toString());
            }

            pieces.add(type);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public List<Pieces.Type> getValidPieces()
    {
        return this.pieces;
    }
    
    public int getCols()
    {
        return this.cols;
    }
    
    public int getRows()
    {
        return this.rows;
    }
    
    /**
     * Add valid puzzle location to list
     * @param col Column
     * @param row Row
     */
    protected void add(final int col, final int row)
    {
        valid.add(new Cell(col, row));
    }
    
    /**
     * Does the piece intersect with any part of the puzzle
     * @param piece The piece we want to check
     * @return true if one part of the piece is in the valid space, false otherwise
     */
    public boolean intersects(final Piece piece)
    {
        for (int i = 0; i < piece.getPieces().size(); i++)
        {
            Cell cell = piece.getPieces().get(i);
            
            final int col = (int)(cell.getCol() + piece.getCol());
            final int row = (int)(cell.getRow() + piece.getRow());
            
            //if this piece is part of the puzzle return true
            if (isValid(col, row))
                return true;
        }
        
        //this piece is inside the puzzle
        return false;
    }
    
    /**
     * Is the entire piece in the puzzle
     * @param piece The piece we want to check
     * @return true if all parts of the piece are in the puzzle, false otherwise
     */
    public boolean isValid(final Piece piece)
    {
        for (int i = 0; i < piece.getPieces().size(); i++)
        {
            Cell cell = piece.getPieces().get(i);
            
            final int col = (int)(piece.getCol() + cell.getCol());
            final int row = (int)(piece.getRow() + cell.getRow());
            
            //if this piece is not part of the puzzle return false
            if (!isValid(col, row))
                return false;
        }
        
        //this piece is inside the puzzle
        return true;
    }
    
    public boolean isValid(final int col, final int row)
    {
        for (int i = 0; i < valid.size(); i++)
        {
            //if location is part of valid list
            if (valid.get(i).equals(col, row))
                return true;
        }
        
        return false;
    }
    
    @Override
    public void dispose()
    {
        this.valid.clear();
        this.valid = null;
        
        if (pieces != null)
        {
            for (int i = 0; i < pieces.size(); i++)
            {
                pieces.set(i, null);
            }

            pieces.clear();
            pieces = null;
        }
    }
    
    public void render(final Graphics graphics)
    {
        Graphics2D g2d = (Graphics2D)graphics;
        
        if (stroke == null)
            stroke = new BasicStroke(LINE_THICKNESS);
        
        //set line thickness
        g2d.setStroke(stroke);
        
        g2d.setColor(Color.WHITE);
        
        //draw the border for the puzzle
        for (int row = 0; row < getRows(); row++)
        {
            for (int col = 0; col < getCols(); col++)
            {
                if (!isValid(col, row))
                    continue;
                
                //north west starting location for the current column, row
                final int startX = (int)(getX() + (col * BLOCK_SIZE));
                final int startY = (int)(getY() + (row * BLOCK_SIZE));
                
                g2d.fillRect(startX, startY, BLOCK_SIZE, BLOCK_SIZE);
            }
        }
        
        g2d.setColor(Color.DARK_GRAY);
        
        //draw the border for the puzzle
        for (int row = 0; row < getRows(); row++)
        {
            for (int col = 0; col < getCols(); col++)
            {
                //skip if not valid
                if (!isValid(col, row))
                    continue;
                
                final boolean north = isValid(col, row - 1);
                final boolean south = isValid(col, row + 1);
                final boolean east  = isValid(col + 1, row);
                final boolean west  = isValid(col - 1, row);
                
                //north west starting location for the current column, row
                final int startX = (int)(getX() + (col * BLOCK_SIZE));
                final int startY = (int)(getY() + (row * BLOCK_SIZE));
                
                //if the west piece is not valid draw border
                if (!west)
                    g2d.drawLine(startX, startY, startX, startY + BLOCK_SIZE);
                
                if (!east)
                    g2d.drawLine(startX + BLOCK_SIZE, startY, startX + BLOCK_SIZE, startY + BLOCK_SIZE);
                
                if (!north)
                    g2d.drawLine(startX, startY, startX + BLOCK_SIZE, startY);
                
                if (!south)
                    g2d.drawLine(startX, startY + BLOCK_SIZE, startX + BLOCK_SIZE, startY + BLOCK_SIZE);
            }
        }
    }
}