package hu.nye.wumpusgame.model;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import static org.aspectj.util.LangUtil.split;


public class GameBoardVO {
    private int size;
     static CellType[][] cells;
     private Hero hero;

    @JsonCreator
    public GameBoardVO(@JsonProperty("size") int size, @JsonProperty("cells") CellType[][] cells) {
        this.size = size;
        this.cells = cells;
    }


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public CellType[][] getCells() {
        return cells;
    }

    public void setCells(CellType[][] cells) {
        this.cells = cells;
    }

    public GameBoardVO() {
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\dultm\\IdeaProjects\\progTech\\" +
                "WumpusGame\\src\\main\\java\\hu\\nye\\wumpusgame\\resources\\wumpuszinput.txt"))) {
            String[] firstLine = br.readLine().split(" ");
            int size = Integer.parseInt(firstLine[0]);
            int col = Integer.parseInt(firstLine[1]);
            int row = Integer.parseInt(firstLine[2]);
            Direction direction = Direction.valueOf(firstLine[3].trim().toUpperCase());
            this.size = size;
            this.cells = new CellType[size][size];
            this.hero = new Hero(col, row, direction, this);

            initializeBoard(br, size);

            placeHeroOnBoard();
        } catch (IOException e) {
            System.out.println("Hiba a fájl olvasása közben: " + e.getMessage());
        }

    }


    private static char[][] board;
    private static boolean hasGold;




    void initializeBoard(BufferedReader br, int size) throws IOException {
        for (int i = 0; i < size; i++) {
            String rowString = br.readLine();
            for (int j = 0; j < size; j++) {
                char cellChar = rowString.charAt(j);
                CellType cellType;

                switch (cellChar) {
                    case 'W':
                        cellType = CellType.WALL;
                        break;
                    case 'H':
                        cellType = CellType.HERO;
                        break;
                    case 'U':
                        cellType = CellType.WUMPUS;
                        break;
                    case 'P':
                        cellType = CellType.PIT;
                        break;
                    case 'G':
                        cellType = CellType.GOLD;
                        break;
                    case '-':
                        cellType = CellType.EMPTY;
                        break;
                    default:
                        cellType = CellType.EMPTY;
                        break;
                }

                cells[i][j] = cellType;
            }
        }
    }


    private void placeHeroOnBoard() {

        cells[hero.getRow()][hero.getCol()] = CellType.HERO;
    }

    public static void printBoard() {
        // Pálya megjelenítése a konzolon
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                char cellSign = getCellSign(cells[i][j]);
                System.out.print(cellSign + " ");
            }
            System.out.println();
        }
    }

    private static char getCellSign(CellType cellType) {
        // Az egyes cellatípusokhoz tartozó karakterek visszaadása
        switch (cellType) {
            case WALL:
                return 'W';
            case HERO:
                return 'H';
            case WUMPUS:
                return 'U';
            case PIT:
                return 'P';
            case GOLD:
                return 'G';
            case EMPTY:
                return '-';
            default:
                return '?'; // Ismeretlen cellatípus esetén '?' karaktert használjunk
        }
    }

}