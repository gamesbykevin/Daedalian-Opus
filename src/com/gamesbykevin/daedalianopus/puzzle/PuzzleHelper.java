package com.gamesbykevin.daedalianopus.puzzle;

import com.gamesbykevin.framework.base.Cell;

import com.gamesbykevin.framework.maze.Room;
import com.gamesbykevin.framework.maze.Room.Wall;

import com.gamesbykevin.daedalianopus.puzzle.piece.Piece;
import com.gamesbykevin.daedalianopus.puzzle.piece.Pieces;
import com.gamesbykevin.daedalianopus.puzzle.piece.PiecesHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class PuzzleHelper 
{
    public enum Difficulty
    {
        Easy, Medium, Hard
    }
    
    public enum Type
    {
        Level1(5, 3),
        Level2(5, 4),
        Level3(10, 2),
        Level4(5, 5),
        Level5(10, 3),
        Level6(7, 5),
        Level7(8, 5),
        Level8(10, 4),
        Level9(6, 7),
        Level10(9, 5),
        Level11(11, 7),
        Level12(10, 6),
        Level13(8, 7),
        Level14(10, 8),
        Level15(10, 10),
        Level16(10, 8),
        Level17(20, 3),
        Level18(8, 9),
        Level19(8, 9),
        Level20(12, 8),
        Level21(12, 11),
        Level22(9, 11),
        Level23(11, 6),
        Level24(15, 6),
        Level25(5, 14),
        Level26(13, 8),
        Level27(11, 12),
        Level28(9, 9),
        Level29(13, 6),
        Level30(8, 8),
        Level31(17, 10),
        Level32(8, 15),
        Level33(15, 4),
        Level34(9, 9),
        Level35(10, 14),
        Level36(8, 8);
        
        private final int col, row;
        
        private Type(final int col, final int row)
        {
            this.col = col;
            this.row = row;
        }
        
        public int getCol()
        {
            return this.col;
        }
        
        public int getRow()
        {
            return this.row;
        }
    }
    
    //the minimum,maximum dimensions when creating random puzzle for each difficulty
    private static final int MAX_DIMENSIONS_EASY = 6;
    private static final int MIN_DIMENSIONS_EASY = 5;
    private static final int MAX_DIMENSIONS_MEDIUM = 9;
    private static final int MIN_DIMENSIONS_MEDIUM = 7;
    private static final int MAX_DIMENSIONS_HARD = 12;
    private static final int MIN_DIMENSIONS_HARD = 10;
    
    //the maximum amount of small pieces to create a piece
    private static final int MAX_SMALL_PIECE_SIZE = 7;
    
    //the minimum amount of small pieces to create a piece
    private static final int MIN_SMALL_PIECE_SIZE = 4;
    
    //we can only join a piece where the count of that pieces group is less than 3
    private static final int MERGE_COUNT_MAX = 2;
    
    /**
     * Count how many locations have the same group
     * @param locations Array of locations to check
     * @param group The group we are looking for
     * @return the total number of locations with the matching group
     */
    private static int getCount(final Room[][] locations, final UUID group)
    {
        int count = 0;
        
        for (int col = 0; col < locations[0].length; col++)
        {
            for (int row = 0; row < locations.length; row++)
            {
                if (locations[row][col].hasId(group))
                    count++;
            }
        }
        
        return count;
    }
    
    /**
     * Mark all locations of the specified group as visited
     * @param locations Array of locations to check
     * @param group The group we are looking for
     */
    private static void markVisited(final Room[][] locations, final UUID group)
    {
        for (int col = 0; col < locations[0].length; col++)
        {
            for (int row = 0; row < locations.length; row++)
            {
                if (locations[row][col].hasId(group))
                    locations[row][col].setVisited(true);
            }
        }
    }
    
    /**
     * Are all locations been visited
     * @param locations Array of locations to check
     * @return true if so, false otherwise
     */
    private static boolean allVisited(final Room[][] locations)
    {
        for (int col = 0; col < locations[0].length; col++)
        {
            for (int row = 0; row < locations.length; row++)
            {
                if (!locations[row][col].hasVisited())
                    return false;
            }
        }
        
        return true;
    }
    
    /**
     * Create a random puzzle
     * @param random Object used to make random decisions
     * @param pieces Collection of pieces that can be used for play
     * @param difficulty The difficulty level, which will determine the size of the puzzle
     * @return A puzzle with valid locations and types of pieces allowed for play
     */
    public static Puzzle createRandom(final Random random, final Pieces pieces, final Difficulty difficulty) throws Exception
    {
        //remove any existing random pieces
        pieces.removeRandomPieces();
        
        //set all remaining as invalid
        pieces.markPiecesInvalid();
        
        //pick random size for our puzzle
        final int columns, rows;
        
        switch (difficulty)
        {
            case Easy:
            default:
                columns = random.nextInt(MAX_DIMENSIONS_EASY - MIN_DIMENSIONS_EASY) + MIN_DIMENSIONS_EASY;
                rows = random.nextInt(MAX_DIMENSIONS_EASY - MIN_DIMENSIONS_EASY) + MIN_DIMENSIONS_EASY;
                break;
                
            case Medium:
                columns = random.nextInt(MAX_DIMENSIONS_MEDIUM - MIN_DIMENSIONS_MEDIUM) + MIN_DIMENSIONS_MEDIUM;
                rows = random.nextInt(MAX_DIMENSIONS_MEDIUM - MIN_DIMENSIONS_MEDIUM) + MIN_DIMENSIONS_MEDIUM;
                break;
                
            case Hard:
                columns = random.nextInt(MAX_DIMENSIONS_HARD - MIN_DIMENSIONS_HARD) + MIN_DIMENSIONS_HARD;
                rows = random.nextInt(MAX_DIMENSIONS_HARD - MIN_DIMENSIONS_HARD) + MIN_DIMENSIONS_HARD;
                break;
        }
        
        //create new puzzle of this size
        Puzzle puzzle = new Puzzle(columns, rows);
        
        //array of locations
        Room[][] locations = new Room[puzzle.getRows()][puzzle.getCols()];
        
        //list for walls
        List<Wall> walls = new ArrayList<>();
        
        //list of groups
        List<UUID> groups = new ArrayList<>();
        
        //create all locations
        for (int col = 0; col < locations[0].length; col++)
        {
            for (int row = 0; row < locations.length; row++)
            {
                //create new location
                locations[row][col] = new Room(col, row);
            }
        }
        
        while (!allVisited(locations))
        {
            //add all locations to list
            for (int col = 0; col < locations[0].length; col++)
            {
                for (int row = 0; row < locations.length; row++)
                {
                    //get the group of the current location
                    final UUID group = locations[row][col].getId();
                    
                    //clear wall selection list
                    walls.clear();

                    //count how many already belong to the current group
                    final int groupCount = getCount(locations, group);
                    
                    int directionCount = 0;
                    
                    if (row < locations.length - 1)
                    {
                        Room south = locations[row + 1][col];
                        
                        if (!south.hasVisited() && !south.hasId(group))
                        {
                            directionCount = getCount(locations, south.getId());
                            
                            if (groupCount + directionCount <= MAX_SMALL_PIECE_SIZE && directionCount <= MERGE_COUNT_MAX)
                            {
                                walls.add(Wall.South);
                            }
                        }
                    }
                    
                    if (row > 0)
                    {
                        Room north = locations[row - 1][col]; 
                        
                        if (!north.hasVisited() && !north.hasId(group))
                        {
                            directionCount = getCount(locations, north.getId());
                            
                            if (groupCount + directionCount <= MAX_SMALL_PIECE_SIZE && directionCount <= MERGE_COUNT_MAX)
                            {
                                walls.add(Wall.North);
                            }
                        }
                    }
                    
                    if (col < locations[0].length - 1)
                    {
                        Room east = locations[row][col + 1];
                        
                        if (!east.hasVisited() && !east.hasId(group))
                        {
                            directionCount = getCount(locations, east.getId());
                            
                            if (groupCount + directionCount <= MAX_SMALL_PIECE_SIZE && directionCount <= MERGE_COUNT_MAX)
                            {
                                walls.add(Wall.East);
                            }
                        }
                    }
                    
                    if (col > 0)
                    {
                        Room west = locations[row][col - 1];
                        
                        if (!west.hasVisited() && !west.hasId(group))
                        {
                            directionCount = getCount(locations, west.getId());
                            
                            if (groupCount + directionCount <= MAX_SMALL_PIECE_SIZE && directionCount <= MERGE_COUNT_MAX)
                            {
                                walls.add(Wall.West);
                            }
                        }
                    }

                    //if there is an option continue
                    if (!walls.isEmpty())
                    {
                        //pick random direction
                        Wall wall = walls.get(random.nextInt(walls.size()));

                        switch (wall)
                        {
                            case North:
                                //mark as part of group
                                locations[row - 1][col].setId(group);
                                break;

                            case South:
                                locations[row + 1][col].setId(group);
                                break;

                            case East:
                                locations[row][col + 1].setId(group);
                                break;

                            case West:
                                locations[row][col - 1].setId(group);
                                break;
                        }

                        //count how many are part of group
                        final int count = getCount(locations, group);

                        //if meet maximum requirements mark all visited
                        if (count >= MAX_SMALL_PIECE_SIZE)
                        {
                            //mark all of this group visited
                            markVisited(locations, group);
                        }
                    }
                    else
                    {
                        //mark all of this group visited
                        markVisited(locations, group);
                    }
                }
            }
        }
        
        //get all unique groups in the locations
        for (int col = 0; col < locations[0].length; col++)
        {
            for (int row = 0; row < locations.length; row++)
            {
                final UUID group = locations[row][col].getId();
                
                boolean valid = true;
                
                for (int i = 0; i < groups.size(); i++)
                {
                    if (groups.get(i) == group)
                    {
                        valid = false;
                        break;
                    }
                }
                
                if (valid)
                    groups.add(group);
            }
        }
        
        //this list will contain invalid locations for puzzle
        List<Cell> invalid = new ArrayList<>();
        
        //list of smaller pieces to create piece
        List<Cell> cells = new ArrayList<>();
        
        //now check each group to see if we have enough to make a piece
        for (int i = 0; i < groups.size(); i++)
        {
            //list of small pieces that make a piece
            cells.clear();

            //base point of piece
            Cell base = null;

            for (int row = 0; row < locations.length; row++)
            {
                for (int col = 0; col < locations[0].length; col++)
                {
                    if (locations[row][col].hasId(groups.get(i)))
                    {
                        cells.add(new Cell(col, row));

                        if (base == null)
                            base = new Cell(col, row);
                    }
                }
            }
            
            //we have enough to create a piece
            if (cells.size() >= MIN_SMALL_PIECE_SIZE)
            {
                //create new piece
                Piece piece = new Piece();

                //add small pieces to puzzle
                for (int z = 0; z < cells.size(); z++)
                {
                    piece.add(cells.get(z).getCol() - base.getCol(), cells.get(z).getRow() - base.getRow());
                }
                
                //choose random color
                piece.setColor(random.nextInt(200) + 56, random.nextInt(200) + 56, random.nextInt(200) + 56);

                //yes piece is valid
                piece.setValid(true);

                if (random.nextBoolean())
                    piece.rotate();
                if (random.nextBoolean())
                    piece.flipVertical();
                if (random.nextBoolean())
                    piece.flipHorizontal();

                //add piece to list
                pieces.addPiece(piece);
            }
            else
            {
                //not enough for piece, so mark all invalid
                for (int z = 0; z < cells.size(); z++)
                {
                    invalid.add(cells.get(z));
                }
            }
        }
        
        //add valid locations
        for (int col = 0; col < puzzle.getCols(); col++)
        {
            for (int row = 0; row < puzzle.getRows(); row++)
            {
                boolean valid = true;
                
                for (int z = 0; z < invalid.size(); z++)
                {
                    //if invalid mark valid false
                    if (invalid.get(z).equals(col, row))
                    {
                        valid = false;
                        break;
                    }
                }
                
                if (valid)
                    puzzle.add(col, row);
            }
        }
        
        return puzzle;
    }
    
    /**
     * Create puzzle
     * @param type The puzzle we want to create
     * @return A puzzle with valid locations and types of pieces allowed for play
     */
    public static Puzzle create(final Type type)
    {
        Puzzle puzzle = null;
        
        try
        {
            //create new puzzle
            puzzle = new Puzzle(type);

            //add valid locations
            for (int col = 0; col < puzzle.getCols(); col++)
            {
                for (int row = 0; row < puzzle.getRows(); row++)
                {
                    switch (type)
                    {
                        case Level1:
                        case Level2:
                        case Level3:
                        case Level4:
                        case Level5:
                        case Level6:
                        case Level7:
                        case Level8:
                        case Level10:
                        case Level12:
                        case Level17:
                        case Level33:
                            break;

                        case Level9:
                            if (col > 2 && row < 4)
                                continue;
                            break;
                            
                        case Level11:
                            if (col < 4 && row < 4)
                                continue;
                            if (col > 6  && row < 4)
                                continue;
                            break;
         
                        case Level13:
                            if (col < 3 && row < 1)
                                continue;
                            if (col > 4  && row < 1)
                                continue;
                            break;
                            
                        case Level14:
                            if (col < 1 && row < 5)
                                continue;
                            if (col > 8  && row < 5)
                                continue;
                            if (col < 3 && row == 3 || col > 6 && row == 3)
                                continue;
                            if (col < 4 && row == 4 || col > 5 && row == 4)
                                continue;
                            break;
                            
                        case Level15:
                            if (col < 2 && row > 4)
                                continue;
                            if (col > 7  && row > 4)
                                continue;
                            if (col == 0 && row < 4)
                                continue;
                            if (col == 1 && row < 3)
                                continue;
                            if (col == 2 && row < 2)
                                continue;
                            if (col == 3 && row < 1)
                                continue;
                            if (col == 9 && row < 4)
                                continue;
                            if (col == 8 && row < 3)
                                continue;
                            if (col == 7 && row < 2)
                                continue;
                            if (col == 6 && row < 1)
                                continue;
                            break;
                            
                        case Level16:
                            if (col > 2 && col < 7 && row > 2)
                                continue;
                            break;
                            
                        case Level18:
                            if (col == 0 && (row < 2 || row > 6))
                                continue;
                            if (col == 1 && (row < 1 || row > 7))
                                continue;
                            if (col == 7 && (row < 2 || row > 6))
                                continue;
                            if (col == 6 && (row < 1 || row > 7))
                                continue;
                            break;
                            
                        case Level19:
                            if (col > 2 && col < 5 && (row < 3 || row > 5))
                                continue;
                            break;
                            
                        case Level20:
                            if (row > 2 && (col == 0 || col == 11))
                                continue;
                            if (row > 3 && (col == 1 || col == 10))
                                continue;
                            if (row > 4 && (col == 2 || col == 9))
                                continue;
                            if (row > 5 && (col == 3 || col == 8))
                                continue;
                            if (col == 4 && (row > 6 || row < 1))
                                continue;
                            if (col == 7 && (row > 6 || row < 1))
                                continue;
                            if (col > 4 && col < 7 && row < 2)
                                continue;
                            break;
                            
                        case Level21:
                            if (row < 8 && col > 2)
                                continue;
                            break;
                            
                        case Level22:
                            if ((col < 2 || col > 6) && (row < 7 || row > 8))
                                continue;
                            if ((col == 2 || col == 6) && (row < 4 || row == 6))
                                continue;
                            if ((col == 3 || col == 5) && (row < 2 || row > 8))
                                continue;
                            break;
                            
                        case Level23:
                            if (row > 1 && row < 4 && col > 3 && col < 7)
                                continue;
                            break;
                            
                        case Level24:
                            if (row > 2 && (col < 5 || col > 9))
                                continue;
                            if (row > 2 && (col < 5 || col > 9))
                                continue;
                            break;
                            
                        case Level25:
                            if ((col == 0 || col == 4) && (row < 2 || row > 2 && row < 5 || row > 7 && row < 13))
                                continue;
                            if ((col == 1 || col == 3) && row < 1)
                                continue;
                            break;
                            
                        case Level26:
                            if ((col == 0 || col == 12) && row != 6)
                                continue;
                            if ((col == 1 || col == 11) && row < 5)
                                continue;
                            if ((col == 2 || col == 10) && row < 4)
                                continue;
                            if ((col == 3 || col == 9) && row < 3)
                                continue;
                            if ((col == 4 || col == 8) && row < 2)
                                continue;
                            if ((col == 5 || col == 7) && row < 1)
                                continue;
                            break;
                            
                        case Level27:
                            if (col < 4 && row < 4)
                                continue;
                            if (col > 6 && row < 4)
                                continue;
                            if (col < 4 && row > 6)
                                continue;
                            if (col > 6 && row > 6)
                                continue;
                            break;
                            
                        case Level28:
                            if (col == 0 && row < 6)
                                continue;
                            if (col == 1 && row < 5)
                                continue;
                            if (col == 2 && row < 4)
                                continue;
                            if (col == 3 && row < 3)
                                continue;
                            if (col == 4 && row < 2)
                                continue;
                            if (col == 5 && row < 1)
                                continue;
                            break;
                            
                        case Level29:
                            if (row < 1 && (col > 1 && col < 11))
                                continue;
                            if (row > 4 && (col > 1 && col < 11))
                                continue;
                            break;
                            
                        case Level30:
                            if (col == 2 && row == 2)
                                continue;
                            if (col == 5 && row == 2)
                                continue;
                            if (col == 2 && row == 5)
                                continue;
                            if (col == 5 && row == 5)
                                continue;
                            break;
                            
                        case Level31:
                            if (row == 0 && col != 13)
                                continue;
                            if (row == 1 && (col > 14 || col < 12 && col != 7))
                                continue;
                            if (row == 2 && (col < 2 || col > 13))
                                continue;
                            if (row == 3 && col > 13)
                                continue;
                            if (row == 4 && (col < 7 || col > 13))
                                continue;
                            if (row == 5 && (col < 10 || col == 11 || col > 14))
                                continue;
                            if (row == 6 && (col < 10 || col > 14))
                                continue;
                            if (row == 7 && (col < 12 || col > 15))
                                continue;
                            if (row == 8 && (col < 12 || col > 15))
                                continue;
                            if (row == 9 && col < 12)
                                continue;
                            break;
                            
                        case Level32:
                            if (col > 2 && (row < 6 || row > 8))
                                continue;
                            break;
                            
                        case Level34:
                            if (row == 0 && (col < 2 || col > 4))
                                continue;
                            if (row > 3 && (col < 2 || col == 5))
                                continue;
                            break;
                            
                        case Level35:
                            if (row == 0 && col < 9)
                                continue;
                            if (row == 1 && col < 7)
                                continue;
                            if (row == 2 && col < 6)
                                continue;
                            if (row == 3 && (col < 3 || col > 8))
                                continue;
                            if (row == 4 && col > 8)
                                continue;
                            if (col > 6 && row > 4)
                                continue;
                            if (col < 4 && row > 7)
                                continue;
                            if (row == 7 && (col > 1 && col < 4))
                                continue;
                            break;
                            
                        case Level36:
                            if (col > 1 && col < 4 && row > 3 && row < 6)
                                continue;
                            break;
                            
                        default:
                            throw new Exception("Puzzle type not found");
                    }

                    puzzle.add(col, row);
                }
            }
            
            //now that puzzle has been created assign pieces to puzzle
            switch (type)
            {
                case Level1:
                    puzzle.add(PiecesHelper.Type.LightBlueL);
                    puzzle.add(PiecesHelper.Type.GrayTetrisPiece);
                    puzzle.add(PiecesHelper.Type.BurgondyT);
                    break;
                    
                case Level2:
                    puzzle.add(PiecesHelper.Type.LightBlueL);
                    puzzle.add(PiecesHelper.Type.GrayTetrisPiece);
                    puzzle.add(PiecesHelper.Type.BurgondyT);
                    puzzle.add(PiecesHelper.Type.PinkBox);
                    break;
                    
                case Level3:
                    puzzle.add(PiecesHelper.Type.LightBlueL);
                    puzzle.add(PiecesHelper.Type.GrayTetrisPiece);
                    puzzle.add(PiecesHelper.Type.GreenLine);
                    puzzle.add(PiecesHelper.Type.PinkBox);
                    break;
                    
                case Level4:
                    puzzle.add(PiecesHelper.Type.LightBlueL);
                    puzzle.add(PiecesHelper.Type.GrayTetrisPiece);
                    puzzle.add(PiecesHelper.Type.GreenLine);
                    puzzle.add(PiecesHelper.Type.PinkBox);
                    puzzle.add(PiecesHelper.Type.DarkBlueL);
                    break;
                    
                case Level5:
                    puzzle.add(PiecesHelper.Type.LightBlueL);
                    puzzle.add(PiecesHelper.Type.GrayTetrisPiece);
                    puzzle.add(PiecesHelper.Type.PinkBox);
                    puzzle.add(PiecesHelper.Type.DarkBlueL);
                    puzzle.add(PiecesHelper.Type.BurgondyT);
                    puzzle.add(PiecesHelper.Type.RedC);
                    break;
                    
                case Level6:
                    puzzle.add(PiecesHelper.Type.GreenLine);
                    puzzle.add(PiecesHelper.Type.PinkBox);
                    puzzle.add(PiecesHelper.Type.RedC);
                    puzzle.add(PiecesHelper.Type.DarkBlueL);
                    puzzle.add(PiecesHelper.Type.BurgondyT);
                    puzzle.add(PiecesHelper.Type.LightBlueL);
                    puzzle.add(PiecesHelper.Type.BlueZ);
                    break;
                    
                case Level7:
                    puzzle.add(PiecesHelper.Type.GreenLine);
                    puzzle.add(PiecesHelper.Type.PinkBox);
                    puzzle.add(PiecesHelper.Type.RedC);
                    puzzle.add(PiecesHelper.Type.DarkBlueL);
                    puzzle.add(PiecesHelper.Type.BurgondyT);
                    puzzle.add(PiecesHelper.Type.LightBlueL);
                    puzzle.add(PiecesHelper.Type.BlueZ);
                    puzzle.add(PiecesHelper.Type.GrayTetrisPiece);
                    break;
                    
                case Level8:
                    puzzle.add(PiecesHelper.Type.GrayTetrisPiece);
                    puzzle.add(PiecesHelper.Type.BurgondyT);
                    puzzle.add(PiecesHelper.Type.RedC);
                    puzzle.add(PiecesHelper.Type.BrownPlus);
                    puzzle.add(PiecesHelper.Type.PinkBox);
                    puzzle.add(PiecesHelper.Type.BlueZ);
                    puzzle.add(PiecesHelper.Type.DarkBlueL);
                    puzzle.add(PiecesHelper.Type.LightBlueL);
                    break;
                    
                case Level9:
                    puzzle.add(PiecesHelper.Type.PinkBox);
                    puzzle.add(PiecesHelper.Type.BurgondyT);
                    puzzle.add(PiecesHelper.Type.BlueZ);
                    puzzle.add(PiecesHelper.Type.RedC);
                    puzzle.add(PiecesHelper.Type.GrayTetrisPiece);
                    puzzle.add(PiecesHelper.Type.DarkBlueL);
                    break;

                case Level10:
                    puzzle.add(PiecesHelper.Type.GreenLine);
                    puzzle.add(PiecesHelper.Type.BurgondyT);
                    puzzle.add(PiecesHelper.Type.DarkGreenSteps);
                    puzzle.add(PiecesHelper.Type.PinkBox);
                    puzzle.add(PiecesHelper.Type.GrayTetrisPiece);
                    puzzle.add(PiecesHelper.Type.BlueZ);
                    puzzle.add(PiecesHelper.Type.BrownPlus);
                    puzzle.add(PiecesHelper.Type.RedC);
                    puzzle.add(PiecesHelper.Type.LightBlueL);
                    break;
                    
                case Level11:
                    puzzle.add(PiecesHelper.Type.PinkBox);
                    puzzle.add(PiecesHelper.Type.BurgondyT);
                    puzzle.add(PiecesHelper.Type.LightGreenZ);
                    puzzle.add(PiecesHelper.Type.GrayTetrisPiece);
                    puzzle.add(PiecesHelper.Type.BrownPlus);
                    puzzle.add(PiecesHelper.Type.RedC);
                    puzzle.add(PiecesHelper.Type.DarkGreenSteps);
                    puzzle.add(PiecesHelper.Type.LightBlueL);
                    puzzle.add(PiecesHelper.Type.DarkBlueL);
                    break;

                case Level12:
                    puzzle.add(PiecesHelper.Type.GreenLine);
                    puzzle.add(PiecesHelper.Type.LightBlueL);
                    puzzle.add(PiecesHelper.Type.YellowMisc);
                    puzzle.add(PiecesHelper.Type.GrayTetrisPiece);
                    puzzle.add(PiecesHelper.Type.BurgondyT);
                    puzzle.add(PiecesHelper.Type.DarkGreenSteps);
                    puzzle.add(PiecesHelper.Type.DarkBlueL);
                    puzzle.add(PiecesHelper.Type.LightGreenZ);
                    puzzle.add(PiecesHelper.Type.BlueZ);
                    puzzle.add(PiecesHelper.Type.BrownPlus);
                    puzzle.add(PiecesHelper.Type.RedC);
                    puzzle.add(PiecesHelper.Type.PinkBox);
                    break;
                    
                case Level13:
                    puzzle.add(PiecesHelper.Type.BlueZ);
                    puzzle.add(PiecesHelper.Type.DarkGreenSteps);
                    puzzle.add(PiecesHelper.Type.LightGreenZ);
                    puzzle.add(PiecesHelper.Type.PinkBox);
                    puzzle.add(PiecesHelper.Type.YellowMisc);
                    puzzle.add(PiecesHelper.Type.RedC);
                    puzzle.add(PiecesHelper.Type.GrayTetrisPiece);
                    puzzle.add(PiecesHelper.Type.BurgondyT);
                    puzzle.add(PiecesHelper.Type.DarkBlueL);
                    puzzle.add(PiecesHelper.Type.GreenLine);
                    break;

                case Level14:
                    puzzle.add(PiecesHelper.Type.GreenLine);
                    puzzle.add(PiecesHelper.Type.LightGreenZ);
                    puzzle.add(PiecesHelper.Type.DarkBlueL);
                    puzzle.add(PiecesHelper.Type.BrownPlus);
                    puzzle.add(PiecesHelper.Type.YellowMisc);
                    puzzle.add(PiecesHelper.Type.RedC);
                    puzzle.add(PiecesHelper.Type.GrayTetrisPiece);
                    puzzle.add(PiecesHelper.Type.LightBlueL);
                    puzzle.add(PiecesHelper.Type.BlueZ);
                    puzzle.add(PiecesHelper.Type.DarkGreenSteps);
                    puzzle.add(PiecesHelper.Type.BurgondyT);
                    puzzle.add(PiecesHelper.Type.PinkBox);
                    break;

                case Level15:
                    puzzle.add(PiecesHelper.Type.DarkGreenSteps);
                    puzzle.add(PiecesHelper.Type.GreenLine);
                    puzzle.add(PiecesHelper.Type.PinkBox);
                    puzzle.add(PiecesHelper.Type.LightGreenZ);
                    puzzle.add(PiecesHelper.Type.YellowMisc);
                    puzzle.add(PiecesHelper.Type.BurgondyT);
                    puzzle.add(PiecesHelper.Type.DarkBlueL);
                    puzzle.add(PiecesHelper.Type.LightBlueL);
                    puzzle.add(PiecesHelper.Type.RedC);
                    puzzle.add(PiecesHelper.Type.BlueZ);
                    puzzle.add(PiecesHelper.Type.BrownPlus);
                    puzzle.add(PiecesHelper.Type.GrayTetrisPiece);
                    break;

                case Level16:
                    puzzle.add(PiecesHelper.Type.GreenLine);
                    puzzle.add(PiecesHelper.Type.PinkBox);
                    puzzle.add(PiecesHelper.Type.DarkGreenSteps);
                    puzzle.add(PiecesHelper.Type.GrayTetrisPiece);
                    puzzle.add(PiecesHelper.Type.LightGreenZ);
                    puzzle.add(PiecesHelper.Type.DarkBlueL);
                    puzzle.add(PiecesHelper.Type.BurgondyT);
                    puzzle.add(PiecesHelper.Type.LightBlueL);
                    puzzle.add(PiecesHelper.Type.BlueZ);
                    puzzle.add(PiecesHelper.Type.YellowMisc);
                    puzzle.add(PiecesHelper.Type.BrownPlus);
                    puzzle.add(PiecesHelper.Type.RedC);
                    break;

                case Level17:
                    puzzle.add(PiecesHelper.Type.RedC);
                    puzzle.add(PiecesHelper.Type.BrownPlus);
                    puzzle.add(PiecesHelper.Type.PinkBox);
                    puzzle.add(PiecesHelper.Type.GreenLine);
                    puzzle.add(PiecesHelper.Type.LightBlueL);
                    puzzle.add(PiecesHelper.Type.BlueZ);
                    puzzle.add(PiecesHelper.Type.YellowMisc);
                    puzzle.add(PiecesHelper.Type.BurgondyT);
                    puzzle.add(PiecesHelper.Type.DarkGreenSteps);
                    puzzle.add(PiecesHelper.Type.GrayTetrisPiece);
                    puzzle.add(PiecesHelper.Type.LightGreenZ);
                    puzzle.add(PiecesHelper.Type.DarkBlueL);
                    break;
                    
                case Level18:
                    puzzle.add(PiecesHelper.Type.GrayTetrisPiece);
                    puzzle.add(PiecesHelper.Type.LightGreenZ);
                    puzzle.add(PiecesHelper.Type.BurgondyT);
                    puzzle.add(PiecesHelper.Type.BlueZ);
                    puzzle.add(PiecesHelper.Type.PinkBox);
                    puzzle.add(PiecesHelper.Type.DarkBlueL);
                    puzzle.add(PiecesHelper.Type.GreenLine);
                    puzzle.add(PiecesHelper.Type.YellowMisc);
                    puzzle.add(PiecesHelper.Type.DarkGreenSteps);
                    puzzle.add(PiecesHelper.Type.LightBlueL);
                    puzzle.add(PiecesHelper.Type.BrownPlus);
                    puzzle.add(PiecesHelper.Type.RedC);
                    break;

                case Level19:
                    puzzle.add(PiecesHelper.Type.GreenLine);
                    puzzle.add(PiecesHelper.Type.LightBlueL);
                    puzzle.add(PiecesHelper.Type.GrayTetrisPiece);
                    puzzle.add(PiecesHelper.Type.BlueZ);
                    puzzle.add(PiecesHelper.Type.BurgondyT);
                    puzzle.add(PiecesHelper.Type.DarkBlueL);
                    puzzle.add(PiecesHelper.Type.YellowMisc);
                    puzzle.add(PiecesHelper.Type.DarkGreenSteps);
                    puzzle.add(PiecesHelper.Type.BrownPlus);
                    puzzle.add(PiecesHelper.Type.RedC);
                    puzzle.add(PiecesHelper.Type.LightGreenZ);
                    puzzle.add(PiecesHelper.Type.PinkBox);
                    break;

                case Level20:
                    puzzle.add(PiecesHelper.Type.LightBlueL);
                    puzzle.add(PiecesHelper.Type.BlueZ);
                    puzzle.add(PiecesHelper.Type.LightGreenZ);
                    puzzle.add(PiecesHelper.Type.DarkGreenSteps);
                    puzzle.add(PiecesHelper.Type.GreenLine);
                    puzzle.add(PiecesHelper.Type.BurgondyT);
                    puzzle.add(PiecesHelper.Type.GrayTetrisPiece);
                    puzzle.add(PiecesHelper.Type.PinkBox);
                    puzzle.add(PiecesHelper.Type.BrownPlus);
                    puzzle.add(PiecesHelper.Type.YellowMisc);
                    puzzle.add(PiecesHelper.Type.RedC);
                    puzzle.add(PiecesHelper.Type.DarkBlueL);
                    break;

                case Level21:
                    puzzle.add(PiecesHelper.Type.DarkBlueL);
                    puzzle.add(PiecesHelper.Type.LightGreenZ);
                    puzzle.add(PiecesHelper.Type.PinkBox);
                    puzzle.add(PiecesHelper.Type.GreenLine);
                    puzzle.add(PiecesHelper.Type.BrownPlus);
                    puzzle.add(PiecesHelper.Type.BurgondyT);
                    puzzle.add(PiecesHelper.Type.BlueZ);
                    puzzle.add(PiecesHelper.Type.LightBlueL);
                    puzzle.add(PiecesHelper.Type.GrayTetrisPiece);
                    puzzle.add(PiecesHelper.Type.DarkGreenSteps);
                    puzzle.add(PiecesHelper.Type.YellowMisc);
                    puzzle.add(PiecesHelper.Type.RedC);
                    break;

                case Level22:
                    puzzle.add(PiecesHelper.Type.BurgondyT);
                    puzzle.add(PiecesHelper.Type.YellowMisc);
                    puzzle.add(PiecesHelper.Type.DarkGreenSteps);
                    puzzle.add(PiecesHelper.Type.RedC);
                    puzzle.add(PiecesHelper.Type.GreenLine);
                    puzzle.add(PiecesHelper.Type.BlueZ);
                    puzzle.add(PiecesHelper.Type.PinkBox);
                    puzzle.add(PiecesHelper.Type.LightBlueL);
                    puzzle.add(PiecesHelper.Type.DarkBlueL);
                    break;

                case Level23:
                    puzzle.add(PiecesHelper.Type.GreenLine);
                    puzzle.add(PiecesHelper.Type.GrayTetrisPiece);
                    puzzle.add(PiecesHelper.Type.DarkGreenSteps);
                    puzzle.add(PiecesHelper.Type.YellowMisc);
                    puzzle.add(PiecesHelper.Type.BurgondyT);
                    puzzle.add(PiecesHelper.Type.PinkBox);
                    puzzle.add(PiecesHelper.Type.BlueZ);
                    puzzle.add(PiecesHelper.Type.DarkBlueL);
                    puzzle.add(PiecesHelper.Type.BrownPlus);
                    puzzle.add(PiecesHelper.Type.RedC);
                    puzzle.add(PiecesHelper.Type.LightGreenZ);
                    puzzle.add(PiecesHelper.Type.LightBlueL);
                    break;

                case Level24:
                    puzzle.add(PiecesHelper.Type.DarkBlueL);
                    puzzle.add(PiecesHelper.Type.LightBlueL);
                    puzzle.add(PiecesHelper.Type.BlueZ);
                    puzzle.add(PiecesHelper.Type.BurgondyT);
                    puzzle.add(PiecesHelper.Type.LightGreenZ);
                    puzzle.add(PiecesHelper.Type.PinkBox);
                    puzzle.add(PiecesHelper.Type.GreenLine);
                    puzzle.add(PiecesHelper.Type.DarkGreenSteps);
                    puzzle.add(PiecesHelper.Type.GrayTetrisPiece);
                    puzzle.add(PiecesHelper.Type.YellowMisc);
                    puzzle.add(PiecesHelper.Type.BrownPlus);
                    puzzle.add(PiecesHelper.Type.RedC);
                    break;

                case Level25:
                    puzzle.add(PiecesHelper.Type.DarkBlueL);
                    puzzle.add(PiecesHelper.Type.LightBlueL);
                    puzzle.add(PiecesHelper.Type.GreenLine);
                    puzzle.add(PiecesHelper.Type.BlueZ);
                    puzzle.add(PiecesHelper.Type.RedC);
                    puzzle.add(PiecesHelper.Type.GrayTetrisPiece);
                    puzzle.add(PiecesHelper.Type.PinkBox);
                    puzzle.add(PiecesHelper.Type.YellowMisc);
                    puzzle.add(PiecesHelper.Type.LightGreenZ);
                    puzzle.add(PiecesHelper.Type.BrownPlus);
                    break;

                case Level26:
                    puzzle.add(PiecesHelper.Type.YellowMisc);
                    puzzle.add(PiecesHelper.Type.DarkBlueL);
                    puzzle.add(PiecesHelper.Type.RedC);
                    puzzle.add(PiecesHelper.Type.LightBlueL);
                    puzzle.add(PiecesHelper.Type.DarkGreenSteps);
                    puzzle.add(PiecesHelper.Type.GreenLine);
                    puzzle.add(PiecesHelper.Type.BurgondyT);
                    puzzle.add(PiecesHelper.Type.LightGreenZ);
                    puzzle.add(PiecesHelper.Type.BlueZ);
                    puzzle.add(PiecesHelper.Type.PinkBox);
                    puzzle.add(PiecesHelper.Type.BrownPlus);
                    puzzle.add(PiecesHelper.Type.GrayTetrisPiece);
                    break;

                case Level27:
                    puzzle.add(PiecesHelper.Type.DarkBlueL);
                    puzzle.add(PiecesHelper.Type.LightGreenZ);
                    puzzle.add(PiecesHelper.Type.GreenLine);
                    puzzle.add(PiecesHelper.Type.BlueZ);
                    puzzle.add(PiecesHelper.Type.BurgondyT);
                    puzzle.add(PiecesHelper.Type.YellowMisc);
                    puzzle.add(PiecesHelper.Type.DarkGreenSteps);
                    puzzle.add(PiecesHelper.Type.LightBlueL);
                    puzzle.add(PiecesHelper.Type.PinkBox);
                    puzzle.add(PiecesHelper.Type.GrayTetrisPiece);
                    puzzle.add(PiecesHelper.Type.BrownPlus);
                    puzzle.add(PiecesHelper.Type.RedC);
                    break;

                case Level28:
                    puzzle.add(PiecesHelper.Type.PinkBox);
                    puzzle.add(PiecesHelper.Type.GreenLine);
                    puzzle.add(PiecesHelper.Type.DarkGreenSteps);
                    puzzle.add(PiecesHelper.Type.LightBlueL);
                    puzzle.add(PiecesHelper.Type.BlueZ);
                    puzzle.add(PiecesHelper.Type.BurgondyT);
                    puzzle.add(PiecesHelper.Type.DarkBlueL);
                    puzzle.add(PiecesHelper.Type.YellowMisc);
                    puzzle.add(PiecesHelper.Type.LightGreenZ);
                    puzzle.add(PiecesHelper.Type.BrownPlus);
                    puzzle.add(PiecesHelper.Type.RedC);
                    puzzle.add(PiecesHelper.Type.GrayTetrisPiece);
                    break;

                case Level29:
                    puzzle.add(PiecesHelper.Type.GreenLine);
                    puzzle.add(PiecesHelper.Type.LightBlueL);
                    puzzle.add(PiecesHelper.Type.LightGreenZ);
                    puzzle.add(PiecesHelper.Type.DarkBlueL);
                    puzzle.add(PiecesHelper.Type.YellowMisc);
                    puzzle.add(PiecesHelper.Type.DarkGreenSteps);
                    puzzle.add(PiecesHelper.Type.BurgondyT);
                    puzzle.add(PiecesHelper.Type.GrayTetrisPiece);
                    puzzle.add(PiecesHelper.Type.BlueZ);
                    puzzle.add(PiecesHelper.Type.BrownPlus);
                    puzzle.add(PiecesHelper.Type.RedC);
                    puzzle.add(PiecesHelper.Type.PinkBox);
                    break;

                case Level30:
                    puzzle.add(PiecesHelper.Type.GreenLine);
                    puzzle.add(PiecesHelper.Type.DarkBlueL);
                    puzzle.add(PiecesHelper.Type.DarkGreenSteps);
                    puzzle.add(PiecesHelper.Type.GrayTetrisPiece);
                    puzzle.add(PiecesHelper.Type.LightGreenZ);
                    puzzle.add(PiecesHelper.Type.BrownPlus);
                    puzzle.add(PiecesHelper.Type.YellowMisc);
                    puzzle.add(PiecesHelper.Type.PinkBox);
                    puzzle.add(PiecesHelper.Type.BlueZ);
                    puzzle.add(PiecesHelper.Type.BurgondyT);
                    puzzle.add(PiecesHelper.Type.RedC);
                    puzzle.add(PiecesHelper.Type.LightBlueL);
                    break;

                case Level31:
                    puzzle.add(PiecesHelper.Type.BlueZ);
                    puzzle.add(PiecesHelper.Type.GreenLine);
                    puzzle.add(PiecesHelper.Type.LightBlueL);
                    puzzle.add(PiecesHelper.Type.LightGreenZ);
                    puzzle.add(PiecesHelper.Type.YellowMisc);
                    puzzle.add(PiecesHelper.Type.DarkBlueL);
                    puzzle.add(PiecesHelper.Type.RedC);
                    puzzle.add(PiecesHelper.Type.BrownPlus);
                    puzzle.add(PiecesHelper.Type.BurgondyT);
                    puzzle.add(PiecesHelper.Type.DarkGreenSteps);
                    puzzle.add(PiecesHelper.Type.PinkBox);
                    puzzle.add(PiecesHelper.Type.GrayTetrisPiece);
                    break;

                case Level32:
                    puzzle.add(PiecesHelper.Type.DarkBlueL);
                    puzzle.add(PiecesHelper.Type.LightGreenZ);
                    puzzle.add(PiecesHelper.Type.BlueZ);
                    puzzle.add(PiecesHelper.Type.LightBlueL);
                    puzzle.add(PiecesHelper.Type.BrownPlus);
                    puzzle.add(PiecesHelper.Type.DarkGreenSteps);
                    puzzle.add(PiecesHelper.Type.YellowMisc);
                    puzzle.add(PiecesHelper.Type.RedC);
                    puzzle.add(PiecesHelper.Type.PinkBox);
                    puzzle.add(PiecesHelper.Type.GreenLine);
                    puzzle.add(PiecesHelper.Type.GrayTetrisPiece);
                    puzzle.add(PiecesHelper.Type.BurgondyT);
                    break;

                case Level33:
                    puzzle.add(PiecesHelper.Type.GrayTetrisPiece);
                    puzzle.add(PiecesHelper.Type.BurgondyT);
                    puzzle.add(PiecesHelper.Type.LightBlueL);
                    puzzle.add(PiecesHelper.Type.DarkGreenSteps);
                    puzzle.add(PiecesHelper.Type.BrownPlus);
                    puzzle.add(PiecesHelper.Type.PinkBox);
                    puzzle.add(PiecesHelper.Type.BlueZ);
                    puzzle.add(PiecesHelper.Type.LightGreenZ);
                    puzzle.add(PiecesHelper.Type.DarkBlueL);
                    puzzle.add(PiecesHelper.Type.GreenLine);
                    puzzle.add(PiecesHelper.Type.YellowMisc);
                    puzzle.add(PiecesHelper.Type.RedC);
                    break;

                case Level34:
                    puzzle.add(PiecesHelper.Type.PinkBox);
                    puzzle.add(PiecesHelper.Type.BrownPlus);
                    puzzle.add(PiecesHelper.Type.RedC);
                    puzzle.add(PiecesHelper.Type.YellowMisc);
                    puzzle.add(PiecesHelper.Type.GreenLine);
                    puzzle.add(PiecesHelper.Type.BlueZ);
                    puzzle.add(PiecesHelper.Type.BurgondyT);
                    puzzle.add(PiecesHelper.Type.DarkGreenSteps);
                    puzzle.add(PiecesHelper.Type.LightBlueL);
                    puzzle.add(PiecesHelper.Type.GrayTetrisPiece);
                    puzzle.add(PiecesHelper.Type.LightGreenZ);
                    puzzle.add(PiecesHelper.Type.DarkBlueL);
                    break;

                case Level35:
                    puzzle.add(PiecesHelper.Type.BurgondyT);
                    puzzle.add(PiecesHelper.Type.RedC);
                    puzzle.add(PiecesHelper.Type.BrownPlus);
                    puzzle.add(PiecesHelper.Type.LightGreenZ);
                    puzzle.add(PiecesHelper.Type.PinkBox);
                    puzzle.add(PiecesHelper.Type.DarkBlueL);
                    puzzle.add(PiecesHelper.Type.YellowMisc);
                    puzzle.add(PiecesHelper.Type.DarkGreenSteps);
                    puzzle.add(PiecesHelper.Type.GreenLine);
                    puzzle.add(PiecesHelper.Type.BlueZ);
                    puzzle.add(PiecesHelper.Type.LightBlueL);
                    puzzle.add(PiecesHelper.Type.GrayTetrisPiece);
                    break;

                case Level36:
                    puzzle.add(PiecesHelper.Type.GreenLine);
                    puzzle.add(PiecesHelper.Type.DarkBlueL);
                    puzzle.add(PiecesHelper.Type.PinkBox);
                    puzzle.add(PiecesHelper.Type.YellowMisc);
                    puzzle.add(PiecesHelper.Type.DarkGreenSteps);
                    puzzle.add(PiecesHelper.Type.BrownPlus);
                    puzzle.add(PiecesHelper.Type.LightGreenZ);
                    puzzle.add(PiecesHelper.Type.RedC);
                    puzzle.add(PiecesHelper.Type.GrayTetrisPiece);
                    puzzle.add(PiecesHelper.Type.BlueZ);
                    puzzle.add(PiecesHelper.Type.LightBlueL);
                    puzzle.add(PiecesHelper.Type.BurgondyT);
                    break;

                default:
                    throw new Exception("Puzzle type not found");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        //return created puzzle
        return puzzle;
    }
}