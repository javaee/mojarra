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

import javax.faces.model.SelectItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameBoard {
    public static final int CLIENT_PLAYER = 1;
    public static final int SERVER_PLAYER = 2;
    public static final int DRAW = 3;

    private static Random generator = new Random();
    private static final int[] BOARD_SIZES = {3, 4, 5, 6};
    private int rows;
    private int columns;
    private int currentRow;
    private int currentColumn;
    private int[][] positions;

    // PROPERTY: rows
    public void setRows(int newValue) {
        rows = newValue;
    }

    public int getRows() {
        return rows;
    }

    // PROPERTY: columns
    public void setColumns(int newValue) {
        columns = newValue;
    }

    public int getColumns() {
        return columns;
    }

    public void initialize() {
        positions = new int[rows][columns];
    }

    public List getBoardSizes() {
        List boardSizes = new ArrayList();
        for (int i = 0; i < BOARD_SIZES.length; i++) {
            String label = "" + BOARD_SIZES[i];
            boardSizes.add(new SelectItem(label, label, null));
        }
        return boardSizes;
    }

    public int getCellValue(int i, int j) {
        if (positions == null) {
            return 0;
        }
        if (0 <= i && i < rows && 0 <= j && j < columns) {
            return positions[i][j];
        } else {
            return 0;
        }
    }

    public void setCurrent(String pos) {
        if (pos == null || pos.length() < 2) {
            throw new IllegalArgumentException();
        }
        int r = pos.charAt(0) - 'A';
        int c = Integer.parseInt(pos.substring(1)) - 1;
        if (r < 0 || r >= rows || c < 0 || c >= columns) {
            throw new IllegalArgumentException();
        }
        currentRow = r;
        currentColumn = c;
    }

    // the client "manual" move
    public boolean move() {
        boolean validMove = false;
        if (positions == null) {
            return validMove;
        }
        if (getCellValue(currentRow, currentColumn) == 0) {
            positions[currentRow][currentColumn] |= CLIENT_PLAYER;
            validMove = true;
        }
        return validMove;
    }

    // the server "automated" move
    public void randomMove() {
        // pick a random cell that hasn't yet been taken 
        while (true) {
            int m = generator.nextInt(rows);
            int n = generator.nextInt(columns);
            if (positions[m][n] == 0) {
                positions[m][n] |= SERVER_PLAYER;
                break;
            }
        }
    }

    /**
     * Returns an integer corresponding to the winning player if
     * there is one.  If there is no winning player, zero will
     * be returned.  If the game is complete with no winner,
     * DRAW (a value 3) will be returned (indicating draw).
     */
    public int gameComplete() {
        // check for consecutive row entries..
        for (int i = 0; i < rows; i++) {
            boolean same = true;
            int playerType = getCellValue(i, 0);
            for (int j = 0; j < columns; j++) {
                if ((getCellValue(i, j) == playerType) && (playerType > 0)) {
                    continue;
                } else {
                    same = false;
                    break;
                }
            }
            if (same == true) {
                return playerType;
            }
        }
        // check for consecutive column entries..
        for (int j = 0; j < columns; j++) {
            boolean same = true;
            int playerType = getCellValue(0, j);
            for (int i = 0; i < rows; i++) {
                if ((getCellValue(i, j) == playerType) && (playerType > 0)) {
                    continue;
                } else {
                    same = false;
                    break;
                }
            }
            if (same == true) {
                return playerType;
            }
        }

        // check for consecutive diagonal entries..
        boolean same = true;
        int playerType = getCellValue(0, 0);
        for (int i = 0; i < rows; i++) {
            if ((getCellValue(i, i) == playerType) && (playerType > 0)) {
                continue;
            } else {
                same = false;
                break;
            }
        }
        if (same) {
            return playerType;
        }
        same = true;
        int j = 0;
        playerType = getCellValue(rows - 1, j);
        for (int i = rows - 1; i >= 0; i--) {
            if ((getCellValue(i, j) == playerType) && (playerType > 0)) {
                j++;
                continue;
            } else {
                same = false;
                break;
            }
        }
        if (same) {
            return playerType;
        }
        // check for draw (all squares occupied)..
        boolean full = true;
        for (int i = 0; i < rows; i++) {
            for (j = 0; j < columns; j++) {
                if (getCellValue(i, j) == 0) {
                    full = false;
                    break;
                }
            }
        }
        if (full) {
            return DRAW;
        }
        return 0;
    }
}
