package hu.nye.WumpusGame;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class GameState {

    public static void saveGame(String userName, char[][] board, int heroRow, int heroCol,
                                WumpusGame.Direction heroDirection, int wumpusCount, int arrows, boolean hasGold) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("GameState");
            doc.appendChild(rootElement);

            // User info
            Element userElement = doc.createElement("User");
            userElement.setAttribute("name", userName);
            rootElement.appendChild(userElement);

            // Game state
            Element gameElement = doc.createElement("Game");
            // gameElement.setAttribute("steps", String.valueOf(steps));
            gameElement.setAttribute("wumpusCount", String.valueOf(wumpusCount));
            gameElement.setAttribute("arrows", String.valueOf(arrows));
            gameElement.setAttribute("hasGold", String.valueOf(hasGold));
            rootElement.appendChild(gameElement);

            // Hero state
            Element heroElement = doc.createElement("Hero");
            heroElement.setAttribute("row", String.valueOf(heroRow));
            heroElement.setAttribute("col", String.valueOf(heroCol));
            heroElement.setAttribute("direction", heroDirection.name());
            gameElement.appendChild(heroElement);

            // Board state
            Element boardElement = doc.createElement("Board");
            for (int i = 0; i < board.length; i++) {
                Element rowElement = doc.createElement("Row");
                rowElement.setAttribute("index", String.valueOf(i));
                for (int j = 0; j < board[i].length; j++) {
                    Element cellElement = doc.createElement("Cell");
                    cellElement.setAttribute("index", String.valueOf(j));
                    cellElement.setTextContent(String.valueOf(board[i][j]));
                    rowElement.appendChild(cellElement);
                }
                boardElement.appendChild(rowElement);
            }
            gameElement.appendChild(boardElement);

            // Save to XML file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("savedGame.xml"));

            transformer.transform(source, result);

            System.out.println("Game state saved to savedGame.xml");

        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }
    }

    public static void loadGame() {
        try {
            File file = new File("savedGame.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            doc.getDocumentElement().normalize();

            // Implement logic to extract data and set the game state

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
