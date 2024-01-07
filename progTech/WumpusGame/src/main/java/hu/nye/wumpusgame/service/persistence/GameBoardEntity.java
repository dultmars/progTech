package hu.nye.wumpusgame.service.persistence;

import javax.persistence.*;
import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.nye.wumpusgame.model.HeroEntity;

@Entity
@Table(name = "game_board")
public class GameBoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private static Long id;
    @OneToOne(mappedBy = "gameBoard", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private HeroEntity hero;
    private static String username;
    @Column(name = "size")
    private static int size;
    @Lob
    @Column(name = "board_data", nullable = false)
    private static String boardData; // Ez tartalmazza a pályád adatait, például JSON vagy XML formátumban

    public GameBoardEntity() {

    }

    public static Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public static String getBoardData() {
        return boardData;
    }

    public void setBoardData(String boardData) {
        this.boardData = boardData;
    }

    public GameBoardEntity(Long id, String username, int size, String boardData) {
        this.id = id;
        this.username = username;
        this.size = size;
        this.boardData = boardData;
    }

    public void setBoardDataFromObject(Object boardDataObject) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            this.boardData = Arrays.toString(objectMapper.writeValueAsString(boardDataObject).getBytes());
        } catch (JsonProcessingException e) {
             e.printStackTrace();
        }
    }

    public <T> T getBoardDataAsObject(Class<T> valueType) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(boardData, valueType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

