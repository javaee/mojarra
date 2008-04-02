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

import java.util.Hashtable; 
import javax.microedition.lcdui.Canvas; 
import javax.microedition.lcdui.Choice; 
import javax.microedition.lcdui.ChoiceGroup; 
import javax.microedition.lcdui.Command; 
import javax.microedition.lcdui.CommandListener; 
import javax.microedition.lcdui.Display; 
import javax.microedition.lcdui.Displayable; 
import javax.microedition.lcdui.Form; 
import javax.microedition.lcdui.Graphics; 
import javax.microedition.lcdui.StringItem; 
import javax.microedition.midlet.MIDlet; 

/**
 * This class implements the old "tic tac toe" game
 * on a client device.
 */
public class TicTacToeMIDlet extends AbstractMIDlet 
    implements CommandListener { 
    private Display display; 
    private StringItem position; 
    private ChoiceGroup boardSize; 
    private StringItem message; 
    private StringItem result; 
    private Command exitCommand; 
    private Command startCommand; 
    private Command selectCommand; 
    private Command sizeSelectCommand; 
    private Command continueCommand; 
    private Command newGameCommand; 
    private BoardCanvas board; 
    private Form startForm; 
    private Form sizeForm; 
    private Form messageForm; 
    private Form waitForm; 
    private Form gameOverForm; 
    private String webform; 
    private ConnectionManager connectionMgr; 
    private Thread connectionThread; 

    // ------ Standard MIDlet methods -----------------------------

    public void startApp() { 
        display = Display.getDisplay(this); 
        exitCommand = new Command("Exit", Command.EXIT, 1); 
        createStartForm(); 
        createSizeForm();
        createBoardCanvas(); 
        createGameOverForm();
        connectionMgr = new ConnectionManager(this);
        connectionThread = new Thread(connectionMgr);
        connectionThread.start();
        waitForm = new Form("Waiting...");
        display.setCurrent(startForm); 
    } 

    public void pauseApp() {} 

    public void destroyApp(boolean unconditional) {} 

    // ------ Create Display Objects ------------------------------
 
    public void createStartForm() { 
        startForm = new Form("Start"); 
        startForm.setTitle("Welcome"); 
        startForm.append("Start the TicTacToe Game"); 
        startCommand = new Command("Start", Command.OK, 0); 
        startForm.addCommand(startCommand); 
        startForm.addCommand(exitCommand); 
        startForm.setCommandListener(this); 
    } 

    public void createSizeForm() { 
        sizeForm = new Form("Size"); 
        sizeForm.setTitle("Game Board Size Selection");
        sizeForm.append("Choose a board size:");
        boardSize = new ChoiceGroup("Size", Choice.EXCLUSIVE);
        sizeForm.append(boardSize);
        sizeSelectCommand = new Command("Select", Command.OK, 0);
        sizeForm.addCommand(sizeSelectCommand);
        sizeForm.addCommand(exitCommand);
        sizeForm.setCommandListener(this); 
    }

    public void createBoardCanvas() { 
        board = new BoardCanvas(); 
        board.setTitle("Game Board"); 
        selectCommand = new Command("Select", Command.OK, 0); 
        board.addCommand(selectCommand); 
        board.setCommandListener(this); 
    } 

    public void createMessageForm() { 
        messageForm = new Form("Message"); 
        message = new StringItem("", null); 
        messageForm.append(message); 
        continueCommand = new Command("Continue", Command.OK, 0); 
        messageForm.addCommand(continueCommand); 
        messageForm.addCommand(exitCommand); 
        messageForm.setCommandListener(this); 
    } 

    public void createGameOverForm() { 
        gameOverForm = new Form("Game Over"); 
        result = new StringItem("", null); 
        gameOverForm.append(result); 
        newGameCommand = new Command("New Game", Command.OK, 0); 
        gameOverForm.addCommand(newGameCommand); 
        gameOverForm.addCommand(exitCommand); 
        gameOverForm.setCommandListener(this); 
    } 

    /**
     * The standard listener method for MIDlet commands..
     */
    public void commandAction(Command c, Displayable s) { 
        if (c == startCommand) {
            doStart(); 
        } else if (c == sizeSelectCommand) {
            doSizeSelect();
        } else if (c == selectCommand) {
            doSelect();
        } else if (c == newGameCommand) {
            notifyDestroyed();
            doNewGame();
        } else if (c == exitCommand) notifyDestroyed(); 
    } 

    // ------ Connection Methods --------------------------------
 
    public void connect(String url, Hashtable request) {
        display.setCurrent(waitForm);
        connectionMgr.connect(url, request);
    }
                                                                                
    public void connectionCompleted(Hashtable response) {
        webform = (String) response.get("form");
        if (webform.equals("start")) {
            displayStart(response);
        } else if (webform.equals("play")) {
            displayPlay(response);
        } else if ((webform.equals("won")) ||
            (webform.equals("lost")) ||
            (webform.equals("draw"))) {
            displayGameOver(response);
        }
    }

    // ------ Helper Methods 

    private void doStart() { 
        connect("start.faces", null);
    } 

    private void doSizeSelect() {
        Hashtable request = new Hashtable();
        request.put("boardsize", boardSize.getString(
            boardSize.getSelectedIndex()));
        request.put("form", "start");
        request.put("submit", "");
        connect("start.faces", request);
    }

    private void doSelect() {
        Hashtable request = new Hashtable();
        request.put("board", board.getString());
        request.put("form", "play");
        request.put("submit", "");
        connect("play.faces", request);
    }

    private void doNewGame() {
        Hashtable request = new Hashtable();
        request.put("form", webform);
        request.put("newgame", "");
        connect(webform + ".faces", request);
    }

    private void displayStart(Hashtable response) { 
        // Display the choice group from response data....
        String name = "boardsize";
        String value = (String) response.get(name);
        String label = null;
        int i = 0;
        boardSize.deleteAll();
        while ((label = (String) response.get(name + ".label." + i)) != null) {
            boardSize.append(label, null);
            if (label.equals(value)) {
                boardSize.setSelectedIndex(i, true);
            }
            i++;
        }
        display.setCurrent(sizeForm); 
    } 

    private void displayPlay(Hashtable response) {
        board.parse((String) response.get("board")); 
        String msg = (String) response.get("messages.board"); 
        if (msg != null) { 
            message.setText(msg); 
            display.setCurrent(messageForm); 
            return; 
        } 
        display.setCurrent(board); 
    }

    private void displayGameOver(Hashtable response) { 
        result.setText((String) response.get("result")); 
        display.setCurrent(gameOverForm); 
    } 
} 

/**
 * Canvas used for displaying and manipulating the game board.
 */
class BoardCanvas extends Canvas { 
    public static final int SELECTED = 1; 
    public static final int TAKEN = 2; 
    private int[][] positions;
    private int rows = 0;
    private int columns = 0;
    private int currentRow = 0; 
    private int currentColumn = 0; 
    public void parse(String layout) { 
        for (int i = 0; i < layout.length(); i++) {
            if (layout.charAt(i) == ' ') {
                rows = i;
                break;
            }
        }
        columns = rows;
        positions = new int[rows][columns];

        int n = 0; 
        for (int i = 0; i < rows; i++) { 
            for (int j = 0; j < columns; j++) { 
                char c = layout.charAt(n); 
                n++; 
                positions[i][j] = c - '0'; 
            } 
            n++; 
        } 
    } 
    public String getString() { 
        return "" + (char) ('A' + currentRow) + (1 + currentColumn); 
    } 
    public void paint(Graphics g) { 
        int width = getWidth(); 
        int height = getHeight(); 
        int oldColor = g.getColor(); 
        g.setColor(0xFFFFFF); 
        g.fillRect(0, 0, width, height); 
        g.setColor(oldColor); 
        int cellWidth = width / (columns + 2); 
        int cellHeight = height / (rows + 2); 
        int cellSize = Math.min(cellWidth, cellHeight); 
        for (int i = 0; i <= rows; i++) { 
            int y = (i + 1) * cellSize; 
            g.drawLine(cellSize, y, (columns + 1) * cellSize, y); 
        } 
        for (int j = 0; j <= columns; j++) { 
            int x = (j + 1) * cellSize; 
            g.drawLine(x, cellSize, x, (rows + 1) * cellSize); 
        } 
        for (int i = 0; i < rows; i++) { 
            int y = (i + 1) * cellSize; 
            for (int j = 0; j < columns; j++) { 
                int x = (j + 1) * cellSize; 
                int p = positions[i][j]; 
                if ((p & SELECTED) != 0) 
                    g.drawRect(x+6, y+6, cellSize-12, cellSize-12); 
                if ((p & TAKEN) != 0) { 
                    if (p == (TAKEN | SELECTED)) { 
                        oldColor = g.getColor(); 
                        g.setColor(0xFFFFFF); 
                    } 
                    g.drawLine(x, y, x + cellSize, y + cellSize); 
                    g.drawLine(x + cellSize, y, x, y + cellSize); 
                    if (p == (TAKEN | SELECTED)) g.setColor(oldColor); 
                } 
            } 
        } 
        int x = (currentColumn + 1) * cellSize; 
        int y = (currentRow + 1) * cellSize; 
        g.drawRect(x - 1, y - 1, cellSize + 2, cellSize + 2); 
    } 

    public void keyPressed(int keyCode) { 
        int gameAction = getGameAction(keyCode); 

        if (gameAction == LEFT) { 
            currentColumn = (currentColumn + columns - 1) % columns; 
        } else if (gameAction == RIGHT) { 
            currentColumn = (currentColumn + 1) % columns; 
        } else if (gameAction == UP) { 
            currentRow = (currentRow + rows - 1) % rows; 
        } else if (gameAction == DOWN) { 
            currentRow = (currentRow + 1) % rows; 
        } 
        repaint(); 
    } 
}

