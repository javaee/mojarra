package j2meDemo.model;

public class Game {
    private GameBoard board;

    public Game() { 
        initialize(); 
    }

    public GameBoard getBoard() { 
        return board; 
    }   

    public void setBoard(GameBoard board) { 
        this.board = board; 
    }

    public String initialize() {
        board = new GameBoard();
        board.setRows(3);
        board.setColumns(3);
        board.initialize();
        return "setup";
    }
}
