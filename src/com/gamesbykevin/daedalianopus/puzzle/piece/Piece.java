package com.gamesbykevin.daedalianopus.puzzle.piece;

import com.gamesbykevin.daedalianopus.puzzle.piece.PiecesHelper.Type;
import com.gamesbykevin.daedalianopus.puzzle.Puzzle;
import com.gamesbykevin.framework.base.Cell;
import com.gamesbykevin.framework.base.Sprite;
import com.gamesbykevin.framework.resources.Disposable;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public final class Piece extends Sprite implements Disposable
{
    //the little pieces that create this piece
    private List<Cell> smallPieces;
    
    //the color of the piece
    private Color color;
    
    //the outline color of the shape
    private Color outline;
    
    //is the piece currently being used
    private boolean valid = false;
    
    private Type type;
    
    /**
     * Create piece of specified type
     * @param type The type of Piece we are creating
     */
    public Piece(final Type type)
    {
        this();
        
        //store the type
        this.type = type;
        
        //create new color
        setColor(type.getRed(), type.getGreen(), type.getBlue());
    }
    
    /**
     * Create random Piece
     */
    public Piece()
    {
        //create list of small pieces that make this piece
        this.smallPieces = new ArrayList<>();
    }
    
    public void setColor(final int red, final int green, final int blue)
    {
        this.color = new Color(red, green, blue);
        
        this.outline = color.darker();
    }
    
    public Type getType()
    {
        return this.type;
    }
    
    /**
     * Get the list of pieces
     * @return The list of pieces that make up this piece
     */
    public List<Cell> getSmallPieces()
    {
        return this.smallPieces;
    }
    
    public int getSmallPieceCount()
    {
        return this.smallPieces.size();
    }
    
    /**
     * Determine if the piece is in play
     * @param valid true if the piece should be in play, false otherwise
     */
    public void setValid(final boolean valid)
    {
        this.valid = valid;
    }
    
    /**
     * Is this piece currently in play
     * @return true if so, false otherwise
     */
    public boolean isValid()
    {
        return this.valid;
    }
    
    /**
     * Add small piece to this Piece
     * @param cell The location of the small piece compared to the origin of this piece (0,0)
     */
    public void add(final Cell cell)
    {
        add((int)cell.getCol(), (int)cell.getRow());
    }
    
    public void add(final double col, final double row)
    {
        add((int)col, (int)row);
    }
    
    public void add(final int col, final int row)
    {
        this.smallPieces.add(new Cell(col, row));
    }
    
    /**
     * Rotate the piece 90 degrees
     */
    public void rotate()
    {
        for (int i = 0; i < smallPieces.size(); i++)
        {
            //get the currect part of this piece
            Cell cell = smallPieces.get(i);
            
            //store the current location
            final double col = smallPieces.get(i).getCol();
            final double row = smallPieces.get(i).getRow();
            
            //now flip the piece
            cell.setCol(row);
            cell.setRow(-col);
        }
    }
    
    /**
     * Flip the piece horizontal
     */
    public void flipHorizontal()
    {
        //while flipping horizontal the y (row) will stay the same only the x (column) will change
        for (int i = 0; i < smallPieces.size(); i++)
        {
            //get the currect part of this piece
            Cell cell = smallPieces.get(i);
            
            //flip column
            cell.setCol(-cell.getCol());
        }
    }
    
    /**
     * Flip the piece vertical
     */
    public void flipVertical()
    {
        //while flipping vertical the x (column) will stay the same only the y (row) will change
        for (int i = 0; i < smallPieces.size(); i++)
        {
            //get the currect part of this piece
            Cell cell = smallPieces.get(i);
            
            //flip row
            cell.setRow(-cell.getRow());
        }
    }
    
    @Override
    public void dispose()
    {
        this.type = null;
        this.color = null;
        this.smallPieces.clear();
        this.smallPieces = null;
    }
    
    /**
     * Do the pieces intersect
     * @param piece The piece we want to check for collision
     * @return true if they intersect, false otherwise
     */
    public boolean hasCollision(final Piece piece)
    {
        for (int i = 0; i < piece.getSmallPieces().size(); i++)
        {
            Cell cell1 = piece.getSmallPieces().get(i);
            
            //calculate the absolute row, col for this piece
            final int row1 = (int)(cell1.getRow() + piece.getRow());
            final int col1 = (int)(cell1.getCol() + piece.getCol());
            
            if (hasCollision(col1, row1))
                return true;
        }
        
        return false;
    }
    
    public boolean hasCollision(final Cell cell)
    {
        return hasCollision((int)cell.getCol(), (int)cell.getRow());
    }
    
    /**
     * Does this piece have the specified location as part of the piece?
     * @param col Column
     * @param row Row
     * @return true if the (column, row) is part of piece, false otherwise
     */
    public boolean hasCollision(final int col, final int row)
    {
        for (int i = 0; i < smallPieces.size(); i++)
        {
            Cell cell = smallPieces.get(i);
            
            //calculate the absolute row, col for this piece
            final int row2 = (int)(cell.getRow() + getRow());
            final int col2 = (int)(cell.getCol() + getCol());
            
            //if the (column, row) matches this piece's (col2, row2) we have collision
            if (col == col2 && row == row2)
                return true;
        }
        
        //no collision found
        return false;
    }
    
    public void render(final Graphics graphics)
    {
        graphics.setColor(color);
        
        for (int i = 0; i < smallPieces.size(); i++)
        {
            Cell cell = smallPieces.get(i);
            
            final int x = (int)(getX() + (cell.getCol() * Puzzle.BLOCK_SIZE));
            final int y = (int)(getY() + (cell.getRow() * Puzzle.BLOCK_SIZE));
            
            graphics.fillRect(x, y, Puzzle.BLOCK_SIZE, Puzzle.BLOCK_SIZE);
        }
        
        graphics.setColor(outline);
        
        for (int i = 0; i < smallPieces.size(); i++)
        {
            Cell cell = smallPieces.get(i);
            
            final int x = (int)(getX() + (cell.getCol() * Puzzle.BLOCK_SIZE));
            final int y = (int)(getY() + (cell.getRow() * Puzzle.BLOCK_SIZE));
            
            graphics.drawRect(x, y, Puzzle.BLOCK_SIZE, Puzzle.BLOCK_SIZE);
        }
    }
}