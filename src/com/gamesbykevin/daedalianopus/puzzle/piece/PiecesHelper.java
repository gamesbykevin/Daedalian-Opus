package com.gamesbykevin.daedalianopus.puzzle.piece;

public final class PiecesHelper 
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
    
    /**
     * Create a puzzle piece of specified type
     * @param type The type of piece
     * @return Piece used to play in game
     */
    public static Piece create(final Type type)
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
}
