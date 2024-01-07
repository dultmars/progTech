package hu.nye.wumpusgame.model;

import hu.nye.wumpusgame.service.game.Game;

public class Hero {
    private static int row;
    private static int col;
   private static Direction direction = Direction.EAST;
  static GameBoardVO gameBoardVO;
   private Cell currentCell;

      public Hero(int col, int row, Direction direction, GameBoardVO gameBoardVO) {
    this.col = col;
    this.row = row;
    this.direction = direction;
    if (gameBoardVO != null)
        Hero.gameBoardVO = gameBoardVO;

    }

    public static Direction getDirection() {
        return direction;
    }
    public int getRow() {

        return row;

      }
    public int getCol() {
        return col;
    }

    public static void move() {

        switch (direction) {
            case NORTH -> {
                if (row > 0 && GameBoardVO.cells[row - 1][col] != CellType.WALL) {
                    row--;
                }
            }
            case EAST -> {
                if (col < GameBoardVO.cells[0].length - 1 && GameBoardVO.cells[row][col + 1] != CellType.WALL) {
                    col++;
                }
            }
            case SOUTH -> {
                if (row < GameBoardVO.cells.length - 1 && GameBoardVO.cells[row + 1][col] != CellType.WALL) {
                    row++;
                }
            }
            case WEST -> {
                if (col > 0 && GameBoardVO.cells[row][col - 1] != CellType.WALL) {
                    col--;
                }
            }
        }
    }

    public static void shoot() {
       if (Game.arrows > 0) {


            int arrowRow = row;
            int arrowCol = col;

            switch (direction) {
                case NORTH -> {
                    while (arrowRow > 0 && GameBoardVO.cells[arrowRow][arrowCol] != CellType.WALL) {
                        arrowRow--;
                        if (GameBoardVO.cells[arrowRow][arrowCol] == CellType.WUMPUS) {
                            // Wumpus találat
                            GameBoardVO.cells[arrowRow][arrowCol] = CellType.EMPTY;
                            System.out.println("Sikeres lövés! Wumpus eltalálva!");
                            break;
                        }
                    }
                }
                case EAST -> {
                    if (arrowCol < GameBoardVO.cells[0].length - 1 && GameBoardVO.cells[arrowRow][arrowCol + 1] != CellType.WALL) {
                        arrowCol++;
                    }
                }
                case SOUTH -> {
                    if (arrowRow < GameBoardVO.cells.length - 1 && GameBoardVO.cells[arrowRow + 1][arrowCol] != CellType.WALL) {
                        arrowRow++;
                    }
                }
                case WEST -> {
                    if (arrowCol > 0 && GameBoardVO.cells[arrowRow][arrowCol - 1] != CellType.WALL) {
                        arrowCol--;
                    }
                }
            }
        } else {
            System.out.println("Nincs több nyilad!");
        }
    }

    public static void pickupGold() {
        if (GameBoardVO.cells[row][col] == CellType.GOLD) {
            // Arany felszedése
            GameBoardVO.cells[row][col] = CellType.EMPTY;
            System.out.println("Arany felszedve!");
        } else {
            System.out.println("Itt nincs arany!");
        }
    }

    public static void turnLeft() {

        direction = direction.left();
    }
    public static void turnRight() {
        direction = direction.right();
    }
        }