/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package j2meDemo.model;

import javax.faces.component.UISelectOne;

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

    public void setSize(String newSize) {
        this.size = newSize;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String newPosition) {
        this.position = newPosition;
    }

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
