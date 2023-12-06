package hu.nye.WumpusGame;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:wumpus_game.db");
                System.out.println("Adatbázis kapcsolat létrehozva.");
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Adatbázis kapcsolat lezárva.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
