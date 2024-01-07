package hu.nye.wumpusgame;


import java.util.Scanner;

import hu.nye.wumpusgame.model.GameBoardVO;
import hu.nye.wumpusgame.service.game.Game;


public class Main {

    public static void main(String[] args) {
        System.out.println("Üdvözöllek a Wumpusz játékban!");

        System.out.print("Kérem, add meg a felhasználóneved: ");
        Scanner scanner = new Scanner(System.in);
        String userName = scanner.nextLine();
        System.out.println("Felhasználónév: " + userName);
        try {
            GameBoardVO gameBoardVO = new GameBoardVO();
            Game game = new Game(gameBoardVO);

            game.play();
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}



