package hu.nye.wumpusgame.service.game;

import hu.nye.wumpusgame.model.GameBoardVO;

public class GameState {
    public static GameStateBuilder builder() {
        return new GameStateBuilder();
    }

    private String username;
    private GameBoardVO currentBoard;
    private boolean shouldExit;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public GameBoardVO getCurrentBoard() {
        return currentBoard;
    }

    public void setCurrentBoard(GameBoardVO currentBoard) {
        this.currentBoard = currentBoard;
    }

    public boolean isShouldExit() {
        return shouldExit;
    }

    public void setShouldExit(boolean shouldExit) {
        this.shouldExit = shouldExit;
    }

    public GameState(String username, GameBoardVO currentBoard, boolean shouldExit) {
        this.username = username;
        this.currentBoard = currentBoard;
        this.shouldExit = shouldExit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GameState)) return false;

        GameState gameState = (GameState) o;

        if (shouldExit != gameState.shouldExit) return false;
        if (!username.equals(gameState.username)) return false;
        return currentBoard.equals(gameState.currentBoard);
    }

    @Override
    public int hashCode() {
        int result = username.hashCode();
        result = 31 * result + currentBoard.hashCode();
        result = 31 * result + (shouldExit ? 1 : 0);
        return result;
    }

    public static final class GameStateBuilder {
        private String username;
        private GameBoardVO currentBoard;
        private boolean shouldExit;

        private GameStateBuilder builder() {
            return new GameStateBuilder();
        }

        public GameStateBuilder withusername(String username) {
            this.username = username;
            return this;
        }

        public GameStateBuilder withcurrentBoard(GameBoardVO currentBoard) {
            this.currentBoard = currentBoard;
            return this;
        }

        public GameStateBuilder withshouldExit(boolean shouldExit) {
            this.shouldExit = shouldExit;
            return this;
        }
    }
}