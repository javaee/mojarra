/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
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
