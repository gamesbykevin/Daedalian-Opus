package com.gamesbykevin.daedalianopus.puzzle;

import com.gamesbykevin.daedalianopus.puzzle.piece.Pieces;

public class PuzzleHelper 
{
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
                    puzzle.add(Pieces.Type.LightBlueL);
                    puzzle.add(Pieces.Type.GrayTetrisPiece);
                    puzzle.add(Pieces.Type.BurgondyT);
                    break;
                    
                case Level2:
                    puzzle.add(Pieces.Type.LightBlueL);
                    puzzle.add(Pieces.Type.GrayTetrisPiece);
                    puzzle.add(Pieces.Type.BurgondyT);
                    puzzle.add(Pieces.Type.PinkBox);
                    break;
                    
                case Level3:
                    puzzle.add(Pieces.Type.LightBlueL);
                    puzzle.add(Pieces.Type.GrayTetrisPiece);
                    puzzle.add(Pieces.Type.GreenLine);
                    puzzle.add(Pieces.Type.PinkBox);
                    break;
                    
                case Level4:
                    puzzle.add(Pieces.Type.LightBlueL);
                    puzzle.add(Pieces.Type.GrayTetrisPiece);
                    puzzle.add(Pieces.Type.GreenLine);
                    puzzle.add(Pieces.Type.PinkBox);
                    puzzle.add(Pieces.Type.DarkBlueL);
                    break;
                    
                case Level5:
                    puzzle.add(Pieces.Type.LightBlueL);
                    puzzle.add(Pieces.Type.GrayTetrisPiece);
                    puzzle.add(Pieces.Type.PinkBox);
                    puzzle.add(Pieces.Type.DarkBlueL);
                    puzzle.add(Pieces.Type.BurgondyT);
                    puzzle.add(Pieces.Type.RedC);
                    break;
                    
                case Level6:
                    puzzle.add(Pieces.Type.GreenLine);
                    puzzle.add(Pieces.Type.PinkBox);
                    puzzle.add(Pieces.Type.RedC);
                    puzzle.add(Pieces.Type.DarkBlueL);
                    puzzle.add(Pieces.Type.BurgondyT);
                    puzzle.add(Pieces.Type.LightBlueL);
                    puzzle.add(Pieces.Type.BlueZ);
                    break;
                    
                case Level7:
                    puzzle.add(Pieces.Type.GreenLine);
                    puzzle.add(Pieces.Type.PinkBox);
                    puzzle.add(Pieces.Type.RedC);
                    puzzle.add(Pieces.Type.DarkBlueL);
                    puzzle.add(Pieces.Type.BurgondyT);
                    puzzle.add(Pieces.Type.LightBlueL);
                    puzzle.add(Pieces.Type.BlueZ);
                    puzzle.add(Pieces.Type.GrayTetrisPiece);
                    break;
                    
                case Level8:
                    puzzle.add(Pieces.Type.GrayTetrisPiece);
                    puzzle.add(Pieces.Type.BurgondyT);
                    puzzle.add(Pieces.Type.RedC);
                    puzzle.add(Pieces.Type.BrownPlus);
                    puzzle.add(Pieces.Type.PinkBox);
                    puzzle.add(Pieces.Type.BlueZ);
                    puzzle.add(Pieces.Type.DarkBlueL);
                    puzzle.add(Pieces.Type.LightBlueL);
                    break;
                    
                case Level9:
                    puzzle.add(Pieces.Type.PinkBox);
                    puzzle.add(Pieces.Type.BurgondyT);
                    puzzle.add(Pieces.Type.BlueZ);
                    puzzle.add(Pieces.Type.RedC);
                    puzzle.add(Pieces.Type.GrayTetrisPiece);
                    puzzle.add(Pieces.Type.DarkBlueL);
                    break;

                case Level10:
                    puzzle.add(Pieces.Type.GreenLine);
                    puzzle.add(Pieces.Type.BurgondyT);
                    puzzle.add(Pieces.Type.DarkGreenSteps);
                    puzzle.add(Pieces.Type.PinkBox);
                    puzzle.add(Pieces.Type.GrayTetrisPiece);
                    puzzle.add(Pieces.Type.BlueZ);
                    puzzle.add(Pieces.Type.BrownPlus);
                    puzzle.add(Pieces.Type.RedC);
                    puzzle.add(Pieces.Type.LightBlueL);
                    break;
                    
                case Level11:
                    puzzle.add(Pieces.Type.PinkBox);
                    puzzle.add(Pieces.Type.BurgondyT);
                    puzzle.add(Pieces.Type.LightGreenZ);
                    puzzle.add(Pieces.Type.GrayTetrisPiece);
                    puzzle.add(Pieces.Type.BrownPlus);
                    puzzle.add(Pieces.Type.RedC);
                    puzzle.add(Pieces.Type.DarkGreenSteps);
                    puzzle.add(Pieces.Type.LightBlueL);
                    puzzle.add(Pieces.Type.DarkBlueL);
                    break;

                case Level12:
                    puzzle.add(Pieces.Type.GreenLine);
                    puzzle.add(Pieces.Type.LightBlueL);
                    puzzle.add(Pieces.Type.YellowMisc);
                    puzzle.add(Pieces.Type.GrayTetrisPiece);
                    puzzle.add(Pieces.Type.BurgondyT);
                    puzzle.add(Pieces.Type.DarkGreenSteps);
                    puzzle.add(Pieces.Type.DarkBlueL);
                    puzzle.add(Pieces.Type.LightGreenZ);
                    puzzle.add(Pieces.Type.BlueZ);
                    puzzle.add(Pieces.Type.BrownPlus);
                    puzzle.add(Pieces.Type.RedC);
                    puzzle.add(Pieces.Type.PinkBox);
                    break;
                    
                case Level13:
                    puzzle.add(Pieces.Type.BlueZ);
                    puzzle.add(Pieces.Type.DarkGreenSteps);
                    puzzle.add(Pieces.Type.LightGreenZ);
                    puzzle.add(Pieces.Type.PinkBox);
                    puzzle.add(Pieces.Type.YellowMisc);
                    puzzle.add(Pieces.Type.RedC);
                    puzzle.add(Pieces.Type.GrayTetrisPiece);
                    puzzle.add(Pieces.Type.BurgondyT);
                    puzzle.add(Pieces.Type.DarkBlueL);
                    puzzle.add(Pieces.Type.GreenLine);
                    break;

                case Level14:
                    puzzle.add(Pieces.Type.GreenLine);
                    puzzle.add(Pieces.Type.LightGreenZ);
                    puzzle.add(Pieces.Type.DarkBlueL);
                    puzzle.add(Pieces.Type.BrownPlus);
                    puzzle.add(Pieces.Type.YellowMisc);
                    puzzle.add(Pieces.Type.RedC);
                    puzzle.add(Pieces.Type.GrayTetrisPiece);
                    puzzle.add(Pieces.Type.LightBlueL);
                    puzzle.add(Pieces.Type.BlueZ);
                    puzzle.add(Pieces.Type.DarkGreenSteps);
                    puzzle.add(Pieces.Type.BurgondyT);
                    puzzle.add(Pieces.Type.PinkBox);
                    break;

                case Level15:
                    puzzle.add(Pieces.Type.DarkGreenSteps);
                    puzzle.add(Pieces.Type.GreenLine);
                    puzzle.add(Pieces.Type.PinkBox);
                    puzzle.add(Pieces.Type.LightGreenZ);
                    puzzle.add(Pieces.Type.YellowMisc);
                    puzzle.add(Pieces.Type.BurgondyT);
                    puzzle.add(Pieces.Type.DarkBlueL);
                    puzzle.add(Pieces.Type.LightBlueL);
                    puzzle.add(Pieces.Type.RedC);
                    puzzle.add(Pieces.Type.BlueZ);
                    puzzle.add(Pieces.Type.BrownPlus);
                    puzzle.add(Pieces.Type.GrayTetrisPiece);
                    break;

                case Level16:
                    puzzle.add(Pieces.Type.GreenLine);
                    puzzle.add(Pieces.Type.PinkBox);
                    puzzle.add(Pieces.Type.DarkGreenSteps);
                    puzzle.add(Pieces.Type.GrayTetrisPiece);
                    puzzle.add(Pieces.Type.LightGreenZ);
                    puzzle.add(Pieces.Type.DarkBlueL);
                    puzzle.add(Pieces.Type.BurgondyT);
                    puzzle.add(Pieces.Type.LightBlueL);
                    puzzle.add(Pieces.Type.BlueZ);
                    puzzle.add(Pieces.Type.YellowMisc);
                    puzzle.add(Pieces.Type.BrownPlus);
                    puzzle.add(Pieces.Type.RedC);
                    break;

                case Level17:
                    puzzle.add(Pieces.Type.RedC);
                    puzzle.add(Pieces.Type.BrownPlus);
                    puzzle.add(Pieces.Type.PinkBox);
                    puzzle.add(Pieces.Type.GreenLine);
                    puzzle.add(Pieces.Type.LightBlueL);
                    puzzle.add(Pieces.Type.BlueZ);
                    puzzle.add(Pieces.Type.YellowMisc);
                    puzzle.add(Pieces.Type.BurgondyT);
                    puzzle.add(Pieces.Type.DarkGreenSteps);
                    puzzle.add(Pieces.Type.GrayTetrisPiece);
                    puzzle.add(Pieces.Type.LightGreenZ);
                    puzzle.add(Pieces.Type.DarkBlueL);
                    break;
                    
                case Level18:
                    puzzle.add(Pieces.Type.GrayTetrisPiece);
                    puzzle.add(Pieces.Type.LightGreenZ);
                    puzzle.add(Pieces.Type.BurgondyT);
                    puzzle.add(Pieces.Type.BlueZ);
                    puzzle.add(Pieces.Type.PinkBox);
                    puzzle.add(Pieces.Type.DarkBlueL);
                    puzzle.add(Pieces.Type.GreenLine);
                    puzzle.add(Pieces.Type.YellowMisc);
                    puzzle.add(Pieces.Type.DarkGreenSteps);
                    puzzle.add(Pieces.Type.LightBlueL);
                    puzzle.add(Pieces.Type.BrownPlus);
                    puzzle.add(Pieces.Type.RedC);
                    break;

                case Level19:
                    puzzle.add(Pieces.Type.GreenLine);
                    puzzle.add(Pieces.Type.LightBlueL);
                    puzzle.add(Pieces.Type.GrayTetrisPiece);
                    puzzle.add(Pieces.Type.BlueZ);
                    puzzle.add(Pieces.Type.BurgondyT);
                    puzzle.add(Pieces.Type.DarkBlueL);
                    puzzle.add(Pieces.Type.YellowMisc);
                    puzzle.add(Pieces.Type.DarkGreenSteps);
                    puzzle.add(Pieces.Type.BrownPlus);
                    puzzle.add(Pieces.Type.RedC);
                    puzzle.add(Pieces.Type.LightGreenZ);
                    puzzle.add(Pieces.Type.PinkBox);
                    break;

                case Level20:
                    puzzle.add(Pieces.Type.LightBlueL);
                    puzzle.add(Pieces.Type.BlueZ);
                    puzzle.add(Pieces.Type.LightGreenZ);
                    puzzle.add(Pieces.Type.DarkGreenSteps);
                    puzzle.add(Pieces.Type.GreenLine);
                    puzzle.add(Pieces.Type.BurgondyT);
                    puzzle.add(Pieces.Type.GrayTetrisPiece);
                    puzzle.add(Pieces.Type.PinkBox);
                    puzzle.add(Pieces.Type.BrownPlus);
                    puzzle.add(Pieces.Type.YellowMisc);
                    puzzle.add(Pieces.Type.RedC);
                    puzzle.add(Pieces.Type.DarkBlueL);
                    break;

                case Level21:
                    puzzle.add(Pieces.Type.DarkBlueL);
                    puzzle.add(Pieces.Type.LightGreenZ);
                    puzzle.add(Pieces.Type.PinkBox);
                    puzzle.add(Pieces.Type.GreenLine);
                    puzzle.add(Pieces.Type.BrownPlus);
                    puzzle.add(Pieces.Type.BurgondyT);
                    puzzle.add(Pieces.Type.BlueZ);
                    puzzle.add(Pieces.Type.LightBlueL);
                    puzzle.add(Pieces.Type.GrayTetrisPiece);
                    puzzle.add(Pieces.Type.DarkGreenSteps);
                    puzzle.add(Pieces.Type.YellowMisc);
                    puzzle.add(Pieces.Type.RedC);
                    break;

                case Level22:
                    puzzle.add(Pieces.Type.BurgondyT);
                    puzzle.add(Pieces.Type.YellowMisc);
                    puzzle.add(Pieces.Type.DarkGreenSteps);
                    puzzle.add(Pieces.Type.RedC);
                    puzzle.add(Pieces.Type.GreenLine);
                    puzzle.add(Pieces.Type.BlueZ);
                    puzzle.add(Pieces.Type.PinkBox);
                    puzzle.add(Pieces.Type.LightBlueL);
                    puzzle.add(Pieces.Type.DarkBlueL);
                    break;

                case Level23:
                    puzzle.add(Pieces.Type.GreenLine);
                    puzzle.add(Pieces.Type.GrayTetrisPiece);
                    puzzle.add(Pieces.Type.DarkGreenSteps);
                    puzzle.add(Pieces.Type.YellowMisc);
                    puzzle.add(Pieces.Type.BurgondyT);
                    puzzle.add(Pieces.Type.PinkBox);
                    puzzle.add(Pieces.Type.BlueZ);
                    puzzle.add(Pieces.Type.DarkBlueL);
                    puzzle.add(Pieces.Type.BrownPlus);
                    puzzle.add(Pieces.Type.RedC);
                    puzzle.add(Pieces.Type.LightGreenZ);
                    puzzle.add(Pieces.Type.LightBlueL);
                    break;

                case Level24:
                    puzzle.add(Pieces.Type.DarkBlueL);
                    puzzle.add(Pieces.Type.LightBlueL);
                    puzzle.add(Pieces.Type.BlueZ);
                    puzzle.add(Pieces.Type.BurgondyT);
                    puzzle.add(Pieces.Type.LightGreenZ);
                    puzzle.add(Pieces.Type.PinkBox);
                    puzzle.add(Pieces.Type.GreenLine);
                    puzzle.add(Pieces.Type.DarkGreenSteps);
                    puzzle.add(Pieces.Type.GrayTetrisPiece);
                    puzzle.add(Pieces.Type.YellowMisc);
                    puzzle.add(Pieces.Type.BrownPlus);
                    puzzle.add(Pieces.Type.RedC);
                    break;

                case Level25:
                    puzzle.add(Pieces.Type.DarkBlueL);
                    puzzle.add(Pieces.Type.LightBlueL);
                    puzzle.add(Pieces.Type.GreenLine);
                    puzzle.add(Pieces.Type.BlueZ);
                    puzzle.add(Pieces.Type.RedC);
                    puzzle.add(Pieces.Type.GrayTetrisPiece);
                    puzzle.add(Pieces.Type.PinkBox);
                    puzzle.add(Pieces.Type.YellowMisc);
                    puzzle.add(Pieces.Type.LightGreenZ);
                    puzzle.add(Pieces.Type.BrownPlus);
                    break;

                case Level26:
                    puzzle.add(Pieces.Type.YellowMisc);
                    puzzle.add(Pieces.Type.DarkBlueL);
                    puzzle.add(Pieces.Type.RedC);
                    puzzle.add(Pieces.Type.LightBlueL);
                    puzzle.add(Pieces.Type.DarkGreenSteps);
                    puzzle.add(Pieces.Type.GreenLine);
                    puzzle.add(Pieces.Type.BurgondyT);
                    puzzle.add(Pieces.Type.LightGreenZ);
                    puzzle.add(Pieces.Type.BlueZ);
                    puzzle.add(Pieces.Type.PinkBox);
                    puzzle.add(Pieces.Type.BrownPlus);
                    puzzle.add(Pieces.Type.GrayTetrisPiece);
                    break;

                case Level27:
                    puzzle.add(Pieces.Type.DarkBlueL);
                    puzzle.add(Pieces.Type.LightGreenZ);
                    puzzle.add(Pieces.Type.GreenLine);
                    puzzle.add(Pieces.Type.BlueZ);
                    puzzle.add(Pieces.Type.BurgondyT);
                    puzzle.add(Pieces.Type.YellowMisc);
                    puzzle.add(Pieces.Type.DarkGreenSteps);
                    puzzle.add(Pieces.Type.LightBlueL);
                    puzzle.add(Pieces.Type.PinkBox);
                    puzzle.add(Pieces.Type.GrayTetrisPiece);
                    puzzle.add(Pieces.Type.BrownPlus);
                    puzzle.add(Pieces.Type.RedC);
                    break;

                case Level28:
                    puzzle.add(Pieces.Type.PinkBox);
                    puzzle.add(Pieces.Type.GreenLine);
                    puzzle.add(Pieces.Type.DarkGreenSteps);
                    puzzle.add(Pieces.Type.LightBlueL);
                    puzzle.add(Pieces.Type.BlueZ);
                    puzzle.add(Pieces.Type.BurgondyT);
                    puzzle.add(Pieces.Type.DarkBlueL);
                    puzzle.add(Pieces.Type.YellowMisc);
                    puzzle.add(Pieces.Type.LightGreenZ);
                    puzzle.add(Pieces.Type.BrownPlus);
                    puzzle.add(Pieces.Type.RedC);
                    puzzle.add(Pieces.Type.GrayTetrisPiece);
                    break;

                case Level29:
                    puzzle.add(Pieces.Type.GreenLine);
                    puzzle.add(Pieces.Type.LightBlueL);
                    puzzle.add(Pieces.Type.LightGreenZ);
                    puzzle.add(Pieces.Type.DarkBlueL);
                    puzzle.add(Pieces.Type.YellowMisc);
                    puzzle.add(Pieces.Type.DarkGreenSteps);
                    puzzle.add(Pieces.Type.BurgondyT);
                    puzzle.add(Pieces.Type.GrayTetrisPiece);
                    puzzle.add(Pieces.Type.BlueZ);
                    puzzle.add(Pieces.Type.BrownPlus);
                    puzzle.add(Pieces.Type.RedC);
                    puzzle.add(Pieces.Type.PinkBox);
                    break;

                case Level30:
                    puzzle.add(Pieces.Type.GreenLine);
                    puzzle.add(Pieces.Type.DarkBlueL);
                    puzzle.add(Pieces.Type.DarkGreenSteps);
                    puzzle.add(Pieces.Type.GrayTetrisPiece);
                    puzzle.add(Pieces.Type.LightGreenZ);
                    puzzle.add(Pieces.Type.BrownPlus);
                    puzzle.add(Pieces.Type.YellowMisc);
                    puzzle.add(Pieces.Type.PinkBox);
                    puzzle.add(Pieces.Type.BlueZ);
                    puzzle.add(Pieces.Type.BurgondyT);
                    puzzle.add(Pieces.Type.RedC);
                    puzzle.add(Pieces.Type.LightBlueL);
                    break;

                case Level31:
                    puzzle.add(Pieces.Type.BlueZ);
                    puzzle.add(Pieces.Type.GreenLine);
                    puzzle.add(Pieces.Type.LightBlueL);
                    puzzle.add(Pieces.Type.LightGreenZ);
                    puzzle.add(Pieces.Type.YellowMisc);
                    puzzle.add(Pieces.Type.DarkBlueL);
                    puzzle.add(Pieces.Type.RedC);
                    puzzle.add(Pieces.Type.BrownPlus);
                    puzzle.add(Pieces.Type.BurgondyT);
                    puzzle.add(Pieces.Type.DarkGreenSteps);
                    puzzle.add(Pieces.Type.PinkBox);
                    puzzle.add(Pieces.Type.GrayTetrisPiece);
                    break;

                case Level32:
                    puzzle.add(Pieces.Type.DarkBlueL);
                    puzzle.add(Pieces.Type.LightGreenZ);
                    puzzle.add(Pieces.Type.BlueZ);
                    puzzle.add(Pieces.Type.LightBlueL);
                    puzzle.add(Pieces.Type.BrownPlus);
                    puzzle.add(Pieces.Type.DarkGreenSteps);
                    puzzle.add(Pieces.Type.YellowMisc);
                    puzzle.add(Pieces.Type.RedC);
                    puzzle.add(Pieces.Type.PinkBox);
                    puzzle.add(Pieces.Type.GreenLine);
                    puzzle.add(Pieces.Type.GrayTetrisPiece);
                    puzzle.add(Pieces.Type.BurgondyT);
                    break;

                case Level33:
                    puzzle.add(Pieces.Type.GrayTetrisPiece);
                    puzzle.add(Pieces.Type.BurgondyT);
                    puzzle.add(Pieces.Type.LightBlueL);
                    puzzle.add(Pieces.Type.DarkGreenSteps);
                    puzzle.add(Pieces.Type.BrownPlus);
                    puzzle.add(Pieces.Type.PinkBox);
                    puzzle.add(Pieces.Type.BlueZ);
                    puzzle.add(Pieces.Type.LightGreenZ);
                    puzzle.add(Pieces.Type.DarkBlueL);
                    puzzle.add(Pieces.Type.GreenLine);
                    puzzle.add(Pieces.Type.YellowMisc);
                    puzzle.add(Pieces.Type.RedC);
                    break;

                case Level34:
                    puzzle.add(Pieces.Type.PinkBox);
                    puzzle.add(Pieces.Type.BrownPlus);
                    puzzle.add(Pieces.Type.RedC);
                    puzzle.add(Pieces.Type.YellowMisc);
                    puzzle.add(Pieces.Type.GreenLine);
                    puzzle.add(Pieces.Type.BlueZ);
                    puzzle.add(Pieces.Type.BurgondyT);
                    puzzle.add(Pieces.Type.DarkGreenSteps);
                    puzzle.add(Pieces.Type.LightBlueL);
                    puzzle.add(Pieces.Type.GrayTetrisPiece);
                    puzzle.add(Pieces.Type.LightGreenZ);
                    puzzle.add(Pieces.Type.DarkBlueL);
                    break;

                case Level35:
                    puzzle.add(Pieces.Type.BurgondyT);
                    puzzle.add(Pieces.Type.RedC);
                    puzzle.add(Pieces.Type.BrownPlus);
                    puzzle.add(Pieces.Type.LightGreenZ);
                    puzzle.add(Pieces.Type.PinkBox);
                    puzzle.add(Pieces.Type.DarkBlueL);
                    puzzle.add(Pieces.Type.YellowMisc);
                    puzzle.add(Pieces.Type.DarkGreenSteps);
                    puzzle.add(Pieces.Type.GreenLine);
                    puzzle.add(Pieces.Type.BlueZ);
                    puzzle.add(Pieces.Type.LightBlueL);
                    puzzle.add(Pieces.Type.GrayTetrisPiece);
                    break;

                case Level36:
                    puzzle.add(Pieces.Type.GreenLine);
                    puzzle.add(Pieces.Type.DarkBlueL);
                    puzzle.add(Pieces.Type.PinkBox);
                    puzzle.add(Pieces.Type.YellowMisc);
                    puzzle.add(Pieces.Type.DarkGreenSteps);
                    puzzle.add(Pieces.Type.BrownPlus);
                    puzzle.add(Pieces.Type.LightGreenZ);
                    puzzle.add(Pieces.Type.RedC);
                    puzzle.add(Pieces.Type.GrayTetrisPiece);
                    puzzle.add(Pieces.Type.BlueZ);
                    puzzle.add(Pieces.Type.LightBlueL);
                    puzzle.add(Pieces.Type.BurgondyT);
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