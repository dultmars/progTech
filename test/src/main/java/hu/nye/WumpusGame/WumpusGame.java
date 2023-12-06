
package hu.nye.WumpusGame;

import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class WumpusGame {
    private static char[][] board;
    private static int size;
    private static int initialHeroRow = 0;
    private static int initialHeroCol = 0;
    private static int heroRow;
    private static int heroCol;
    private static Direction heroDirection = Direction.NORTH;
    private static int wumpusCount;
    private static int arrows;
    private static boolean hasGold;
    private static boolean isBoardInitialized = false;

    public static void main(String[] args) {
        System.out.println("Üdvözöllek a Wumpusz játékban!");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Kérem, add meg a felhasználóneved: ");
        String userName = scanner.nextLine();
        System.out.println("Felhasználónév: " + userName);
        //initialHeroRow = heroRow;
        //initialHeroCol = heroCol;
        resetGame();

        while (true) {
            System.out.println("\n--- Alapmenü ---");
            System.out.println("1. Pályaszerkesztés");
            System.out.println("2. Fájlból beolvasás");
            System.out.println("3. Adatbázisból betöltés");
            System.out.println("4. Adatbázisba mentés");
            System.out.println("5. Játék");
            System.out.println("6. Kilépés");

            System.out.print("Válaszd ki a menüpontot (1-6): ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    initializeBoard(scanner, userName);
                    isBoardInitialized = true;
                    break;
                case 2:
                    loadFromFile();
                    break;
                case 3:
                    GameState.loadGame();
                    break;
                case 4:
                    GameState.saveGame(userName, board, heroRow, heroCol, heroDirection, wumpusCount, arrows, hasGold);
                    break;
                case 5:
                    if (isBoardInitialized) {
                        playGame(userName, scanner);
                    } else {
                        System.out.println("Először hozd létre vagy olvasd be a pályát!");
                    }
                    break;
                case 6:
                    System.out.println("Viszlát!");
                    return;
                default:
                    System.out.println("Érvénytelen. Kérlek, válassz újra.");
                    break;
            }
        }
    }

    private static void startGameAutomatically(String userName, Scanner scanner) {
        System.out.println("A pályaszerkesztés elkészült. Induljon a játék!");
        playGame(userName, scanner);
    }

    private static void loadFromFile() {
        System.out.println("Ez még fejlesztés alatt van. Kérlek válassz másik menüpontot.");
    }

    private static void loadFromDb() {
        System.out.println("Ez még fejlesztés alatt van. Kérlek válassz másik menüpontot.");
    }

    private static void saveToDb() {
        System.out.println("Ez még fejlesztés alatt van. Kérlek válassz másik menüpontot.");
    }

    /////// ************ PÁLYA ELEMEINEK LÉTREHOZÁSA ************* /////////

    private static void initializeBoard(Scanner scanner, String userName) {
        System.out.print("A játék egy NxN-es táblán játszható (6<=N<=20). Add meg a pálya méretét: ");
        size = scanner.nextInt();
        scanner.nextLine(); // Consuming the newline character

        if (size < 6 || size > 20) {
            System.out.println("Érvénytelen méret. Kérlek, próbáld újra.");
            return;
        }

        board = new char[size][size];

        int wumpusCount = 0;
        if (size <= 8) {
            wumpusCount = 1;
        } else if (size >= 9 && size <= 14) {
            wumpusCount = 2;
        } else if (size >= 15) {
            wumpusCount = 3;
        }

        arrows = wumpusCount;

        hasGold = false;

        char[][] tempBoard = new char[size][size];
        initializeEmptyBoard();

        addElement(scanner, 'H', "Hős");

        System.out.println("Add meg a Hős kezdőirányát (N/W/S/E): ");
        char heroDirectionChar = Character.toUpperCase(scanner.next().charAt(0));
        setHeroDirection(heroDirectionChar);

        arrows = wumpusCount;

        addElement(scanner, 'G', "Arany");

        addElement(scanner, 'V', "Verem");


        for (int i = 0; i < wumpusCount; i++) {
            addElement(scanner, 'S', "Wumpus");
            System.out.println((wumpusCount - i - 1) + " darab Wumpus-t kell még elhelyezned.");
        }

        printBoard();

    }

    private static void initializeEmptyBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == 0 || i == size - 1 || j == 0 || j == size - 1) {
                    board[i][j] = 'W'; // Fal
                } else {
                    board[i][j] = '_'; // Üres mező
                }
            }
        }
    }

    private static void addElement(Scanner scanner, char elementType, String elementName) {
        while (true) {
            System.out.println("Add meg a " + elementName + " pozícióját:");
            System.out.print(elementName + " - Oszlop (A-Z): ");
            char elementColChar = scanner.next().charAt(0);
            System.out.print(elementName + " - Sor (1-" + size + "): ");
            int elementRowInput = scanner.nextInt();

            int elementColIndex = Character.toUpperCase(elementColChar) - 'A' + 1;
            if (isValidPosition(elementRowInput, elementColIndex) && board[elementRowInput][elementColIndex] != 'W') {
                board[elementRowInput][elementColIndex] = elementType;
                System.out.println(elementName + " hozzáadva!");
                printBoard();
                break;
            } else {
                System.out.println("Érvénytelen pozíció, próbáld újra.");
            }
        }
    }

    /////// ************ PÁLYASZERKSZETÉS *************/////////

    private static void editBoard() {
        Scanner scanner = new Scanner(System.in);
        boolean editing = true;

        while (editing) {
            System.out.println("\nPályaszerkesztés menü:");
            System.out.println("1. Elem hozzáadása");
            System.out.println("2. Elem elvétele");
            System.out.println("3. Pálya kiírása");
            System.out.println("4. Pályaszerkesztés befejezése");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addElement();
                    break;
                case 2:
                    removeElement();
                    break;
                case 3:
                    printBoard();
                    break;
                case 4:
                    editing = false;
                    break;
                default:
                    System.out.println("Érvénytelen választás!");
                    break;
            }
        }
    }

    private static void addElement() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Adja meg az elem típusát (W/H/S/V/G): ");
        char elementType = scanner.next().charAt(0);

        System.out.print("Adja meg az elem sorát (1-" + size + "): ");
        int elementRow = scanner.nextInt();

        System.out.print("Adja meg az elem oszlopát (a-" + (char) ('a' + size - 1) + "): ");
        int elementCol = getColumnIndex(scanner.next().charAt(0));

        if (isValidPosition(elementRow, (char) elementCol) && board[elementRow - 1][elementCol] == '_') {
            board[elementRow - 1][elementCol] = elementType;
            System.out.println("Elem hozzáadva!");
        } else {
            System.out.println("Érvénytelen pozíció vagy a pozíció már foglalt!");
        }
    }

    private static int getColumnIndex(char c) {
        return Character.toUpperCase(c) - 'A' + 1;
    }

    private static void removeElement() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Adja meg az elem sorát (1-" + size + "): ");
        int elementRow = scanner.nextInt();

        System.out.print("Adja meg az elem oszlopát (a-" + (char) ('a' + size - 1) + "): ");
        int elementCol = getColumnIndex(scanner.next().charAt(0));

        if (isValidPosition(elementRow, (char) elementCol) && board[elementRow - 1][elementCol] != '_') {
            board[elementRow - 1][elementCol] = '_';
            System.out.println("Elem eltávolítva!");
        } else {
            System.out.println("Érvénytelen pozíció vagy a pozíció üres!");
        }
    }

    private static void printBoard() {
        System.out.println("\nPálya állapota:");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
        //System.out.println("Hős pozíció: [" + heroRow + ", " + (char) ('A' + heroCol - 1) + "]");
        System.out.println("Hős pozíció: [" + heroRow + ", " + heroRow + "]");
        System.out.println("Hős iránya: " + heroDirection);
        System.out.println("Nyilak száma: " + arrows);
    }


    /////// ************ GAME START *************/////////

    private static void playGame(String userName, Scanner scanner) {
        System.out.println("\nJáték indítása...");
        System.out.println("\nSzerezd meg az aranyat és menj vissza vele a kezdőpozícióba!");

        boolean playing = true;
        int steps = 0;

        while (playing) {
            printBoard();
            System.out.println("\nHős állapota: " + heroRow + heroCol);
            System.out.println("Nyilak száma: " + arrows);
            System.out.println("Nézési irány: " + getDirection());

            System.out.println("\n--- Akciók ---");
            System.out.println("1. Lépés");
            System.out.println("2. Jobbra fordul");
            System.out.println("3. Balra fordul");
            System.out.println("4. Lövés");
            System.out.println("5. Arany felvétele");
            System.out.println("6. Feladás");

            System.out.print("Válaszd ki az akciót (1-6): ");
            int action = scanner.nextInt();

            switch (action) {
                case 1:
                    move();
                    steps++;
                    printBoard();
                    break;
                case 2:
                    turnRight();
                    printBoard();
                    break;
                case 3:
                    turnLeft();
                    printBoard();
                    break;
                case 4:
                    shoot();
                    printBoard();
                    break;
                case 5:
                    pickUpGold();
                    printBoard();
                    break;
                case 6:
                    System.out.println("Feladás...");
                    return;
                default:
                    System.out.println("Érvénytelen választás. Kérlek, válassz újra.");
                    break;
            }
            if (isGameOver(userName, steps)) {
                playing = false;
                System.out.println("Játék vége!");
            }

            if (heroCol == initialHeroCol && heroRow == initialHeroRow && hasGold) {
                saveToDatabase(userName, steps);
                System.out.println("Gratulálok, " + userName + "! Teljesítetted a pályát! Pontszámod: " + steps + "");
                return;
            }
        }
    }

    private static boolean isGameOver(String userName, int steps) {
        if (board[heroRow][heroCol] == 'V') {
            gameOver("A hős a verembe esett. Játék vége!");
            return true;
        } else if (board[heroRow][heroCol] == 'S') {
            gameOver("A hőst elkapta a Wumpus. Játék vége!");
            return true;
        } else if (heroRow == initialHeroRow && heroCol == initialHeroCol && hasGold) {
            saveToFile(userName, steps);
            System.out.println("Gratulálok, " + userName + "! Teljesítetted a pályát! Pontszámod: " + steps);
            System.exit(0);
        }
        return false;
    }

    private static void saveToDatabase(String userName, int steps) {
        System.out.println("Adatok mentése az adatbázisba...");
        System.out.println("Felhasználónév: " + userName);
        System.out.println("Lépések száma: " + steps);
    }

    private static String getDirection() {
        switch (heroDirection) {
            case NORTH:
                return "Észak";
            case WEST:
                return "Nyugat";
            case SOUTH:
                return "Dél";
            case EAST:
                return "Kelet";
            default:
                return "Ismeretlen irány";
        }
    }

    /*private static void move() {
        int newHeroRow = heroRow;
        int newHeroCol = heroCol;
        switch (heroDirection) {
            case NORTH:
                if (heroRow > 1 && isPositionEmpty(heroRow - 1, heroCol)) {
                    heroRow--;
                    System.out.println("Hős lépése északi irányba.");
                    printBoard();
                } else {
                    System.out.println("A Hős nem tud északi irányba lépni.");
                }
                break;
            case WEST:
                if (heroCol > 1 && isPositionEmpty(heroRow, heroCol - 1)) {
                    heroCol--;
                    System.out.println("Hős lépése nyugati irányba.");
                    printBoard();
                } else {
                    System.out.println("A Hős nem tud nyugati irányba lépni.");
                }
                break;
            case SOUTH:
                if (heroRow < size && isPositionEmpty(heroRow + 1, heroCol)) {
                    heroRow++;
                    System.out.println("Hős lépése déli irányba.");
                    printBoard();
                } else {
                    System.out.println("A Hős nem tud déli irányba lépni.");
                }
                break;
            case EAST:
                if (heroCol < size && isPositionEmpty(heroRow, heroCol + 1)) {
                    heroCol++;
                    System.out.println("Hős lépése keleti irányba.");
                    printBoard();
                } else {
                    System.out.println("A Hős nem tud keleti irányba lépni.");
                }
                break;
        }
        // Wumpus ellenőrzése
        if (board[heroRow][heroCol] == 'S') {
            gameOver("A hőst elkapta a Wumpus. Játék vége!");
            return;
        }

        // Kilépés a ciklusból, mert a helyes adatokat kapott a felhasználó
        //break;
    }
    */

    private static void move() {
        int newRow = heroRow;
        int newCol = heroCol;

        //int heroDirectiondirection;
        switch (heroDirection) {
            case NORTH:
                newRow--;
                break;
            case EAST:
                newCol++;
                break;
            case SOUTH:
                newRow++;
                break;
            case WEST:
                newCol--;
                break;
        }
        if (isValidPosition(newRow, (char) newCol) && isPositionEmpty(newRow, newCol)) {
            board[heroRow][heroCol] = '_';  // Az aktuális pozíció törlése.
            heroRow = newRow;
            heroCol = newCol;
            board[heroRow][heroCol] = 'H';  // Az új pozíció frissítése.
            //System.out.println("Új pozíció: [" + newRow + ", " + newCol + "]");
            //System.out.println("Az új pozíció üres: " + isPositionEmpty(newRow, newCol));
            System.out.println("Hős lépése sikeres.");
        } else {
            System.out.println("Érvénytelen lépés!");
        }
        printBoard();
    }

    private static boolean isPositionEmpty ( int row, int col){
        //return board[row][col] == '_';
        return board[row][col] != 'W' && board[row][col] != 'V' && board[row][col] != 'S' && board[row][col] != 'G';
    }

    enum Direction {
        NORTH, WEST, SOUTH, EAST
    }

    private static void setHeroDirection ( char directionChar){
        switch (Character.toUpperCase(directionChar)) {
            case 'N':
                heroDirection = Direction.NORTH;
                break;
            case 'W':
                heroDirection = Direction.WEST;
                break;
            case 'S':
                heroDirection = Direction.SOUTH;
                break;
            case 'E':
                heroDirection = Direction.EAST;
                break;
            default:
                System.out.println("Érvénytelen irány, az irány csak N,W,S vagy E lehet.");
                break;
        }
    }


    private static void gameOver (String message){
        printBoard();
        System.out.println(message);
        System.exit(0); // Kilépés a programból

        int newRow = 0;
        int newCol = 0;
        if (isValidPosition(newRow, newCol))
            // Végrehajtjuk a lépést csak akkor, ha érvényes.
            board[heroRow][heroCol] = ' '; // Az aktuális pozíció törlése.

        if (board[newRow][newCol] == 'V') {
            // Ha a hős verembe lép, a játék véget ér
            gameOver("A hős a verembe esett. Játék vége!");
        } else if (board[newRow][newCol] == 'S') {
            // Ha a hős a wumpusra lő, a wumpus eltűnik, és a nyilak száma csökken.
            board[newRow][newCol] = ' ';
            wumpusCount--;
            arrows--;
            System.out.println("Wumpust sikerült eltalálni!");
        } else if (board[newRow][newCol] == 'G') {
            // Ha a hős aranyat vesz fel, növeljük az aranyak számát.
            hasGold = true;
        }

        heroRow = newRow;
        heroCol = newCol;
        board[heroRow][heroCol] = 'H'; // Az új pozíció frissítése

        if (heroRow == board.length - 1 && heroCol == board.length / 2) {
            // Ha a hős visszatért a kezdő pozíciójába arannyal, a játék teljesítve van.
            gameOver("Gratulálunk! A hős visszatért a kezdő pozíciójába arannyal. Játék teljesítve!");
        } else {
            System.out.println("Érvénytelen lépés!");
        }
    }


    private static void turnRight () {
        switch (heroDirection) {
            case NORTH:
                heroDirection = Direction.EAST;
                System.out.println("Hős jobbra fordult, most kelet felé néz.");
                break;
            case WEST:
                heroDirection = Direction.NORTH;
                System.out.println("Hős jobbra fordult, most észak felé néz.");
                break;
            case SOUTH:
                heroDirection = Direction.WEST;
                System.out.println("Hős jobbra fordult, most nyugat felé néz.");
                break;
            case EAST:
                heroDirection = Direction.SOUTH;
                System.out.println("Hős jobbra fordult, most dél felé néz.");
                break;
        }
    }

    private static void turnLeft () {
        switch (heroDirection) {
            case NORTH:
                heroDirection = Direction.WEST;
                System.out.println("Hős balra fordult, most nyugat felé néz.");
                break;
            case WEST:
                heroDirection = Direction.SOUTH;
                System.out.println("Hős balra fordult, most dél felé néz.");
                break;
            case SOUTH:
                heroDirection = Direction.EAST;
                System.out.println("Hős balra fordult, most kelet felé néz.");
                break;
            case EAST:
                heroDirection = Direction.NORTH;
                System.out.println("Hős balra fordult, most észak felé néz.");
                break;
        }
    }

    private static void shoot () {
        int newRow = heroRow;
        int newCol = heroCol;

        switch (heroDirection) {
            case NORTH:
                newRow = heroRow - 1;
                newCol = heroCol;
                break;
            case WEST:
                newRow = heroRow;
                newCol = heroCol - 1;
                break;
            case SOUTH:
                newRow = heroRow + 1;
                newCol = heroCol;
                break;
            case EAST:
                newRow = heroRow;
                newCol = heroCol + 1;
                break;
            default:
                System.out.println("Érvénytelen irány.");
                return;
        }


        if (isValidPosition(newRow, newCol) && board[newRow][newCol] == 'S') {
            // Ha a célzott pozíció a Wumpus helye
            System.out.println("Nyíl kilőve!");
            board[newRow][newCol] = ' '; // Wumpus eltávolítása
            arrows--; // Nyíl csökkentése
        } else {
            System.out.println("Sajnos nem találtad el a Wumpust.");
        }
        if (arrows > 0) {
            arrows--;
            System.out.println("Hős kilőtte egy nyilát!");
        } else {
            System.out.println("Nincs több nyila a Hősnek!");
        }
        // Ellenőrizze, hogy a hős lelőtte-e wumpust
        if (board[newRow][newCol] == 'S') {
            board[newRow][newCol] = '_'; // Wumpus eltávolítása
            arrows--; // Nyíl csökkentése
            System.out.println("Wumpus lelőve!");
        }
    }

    private static void pickUpGold () {
        int newRow = heroRow;
        int newCol = heroCol;
        if (board[heroRow][heroCol] == 'G') {
            System.out.println("Hős felvette az aranyat!");
            board[heroRow][heroCol] = '_'; // Az aranyat felvettük, üres helyre vált
            hasGold = true;
            System.out.println("Az arany fel van véve!");

            // Ellenőrizze, hogy a hős felvette-e az aranyat és visszatért-e a kiindulási pozícióba
            //if (board[newRow][newCol] == 'G' && newRow == heroRow && newCol == heroCol) {
            //   System.out.println("Gratulálunk! A játék teljesítve!");
            //    System.exit(0);
            // }


            if (hasGold && (heroRow == initialHeroRow && heroCol == initialHeroCol)) {
                System.out.println("Gratulálunk! A játék teljesítve!");
                System.exit(0);
            }
        }   else {
            System.out.println("Nincs arany a Hős pozícióján!");
        }
    }


    private static boolean isValidPosition ( int row, int col){
        return row >= 0 && row < size && col >= 0 && col < size;
    }
    private static void resetGame () {
        heroRow = initialHeroRow;
        heroCol = initialHeroCol;
    }
    private static void saveToFile(String userName, int steps) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("Results.txt", true))) {
            writer.println("Felhasználónév: " + userName + ", Lépések száma: " + steps);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}