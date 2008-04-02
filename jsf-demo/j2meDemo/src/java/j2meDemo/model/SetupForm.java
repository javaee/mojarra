package j2meDemo.model;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class SetupForm {
    private String size = "3";
    private String position = "";
    private UISelectOne boardSize;
    private GameBoard gameBoard;

    // PROPERTY: size
    public String getSize() {
/*
      if (battleGround.getAvailableSizes().size() > 0)
         size = ((SelectItem) 
            battleGround.getAvailableSizes().get(0)).getLabel();
*/
       return size;
    }
    public void setSize(String newSize) { this.size = newSize; }

    public String getPosition() { return position; }
    public void setPosition(String newPosition) { this.position = newPosition; }

    public UISelectOne getBoardSize() { 
        return boardSize; 
    }
    public void setBoardSize(UISelectOne boardSize) { 
        this.boardSize = boardSize;
    }

    public void setGameBoard(GameBoard newGameBoard) {
        this.gameBoard = newGameBoard;
    }   

    public String play() {
        // client moves
        if (!gameBoard.move()) {
            return "play"; 
        }
        if (gameBoard.gameComplete() == gameBoard.CLIENT_PLAYER) {
            return "won";
        } else if (gameBoard.gameComplete() == gameBoard.DRAW) {
            return "draw";
        } 
        // server moves
        gameBoard.randomMove();
        if (gameBoard.gameComplete() == gameBoard.SERVER_PLAYER) {
            return "lost";
        } else if (gameBoard.gameComplete() == gameBoard.DRAW) {
            return "draw";
        } 
        // we're not done yet...
        return "play";
    }         

    public String submit() {
        return "play";
    }
}
