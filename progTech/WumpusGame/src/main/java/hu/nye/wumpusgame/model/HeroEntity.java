package hu.nye.wumpusgame.model;

import javax.persistence.*;

import hu.nye.wumpusgame.service.persistence.GameBoardEntity;

@Entity
@Table(name = "hero")
public class HeroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "row", nullable = false)
    private int row;

    @Column(name = "col", nullable = false)
    private int col;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_board_id")
    private GameBoardEntity gameBoard;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public GameBoardEntity getGameBoard() {
        return gameBoard;
    }

    public void setGameBoard(GameBoardEntity gameBoard) {
        this.gameBoard = gameBoard;
    }

    public HeroEntity(Long id) {
        this.id = id;
    }
}

