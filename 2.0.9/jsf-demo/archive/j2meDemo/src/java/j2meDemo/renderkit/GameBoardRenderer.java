/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2011 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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

package j2meDemo.renderkit;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import java.io.IOException;
import java.util.Map;

import j2meDemo.model.GameBoard;

/**
 * This renderer generates a "string" representaton of
 * game board positions (encode).
 * When decoding, the renderer reconfigures the board size
 * (if necessary) and sets current coordinates from client.
 */
public class GameBoardRenderer extends Renderer {
    /**
     * Examine the "cells" on the game board and produce a string
     * indicating what cells have been visited (marked) by either player.
     * Each row in the string is seperated by "+".  For example, a
     * standard tic tac toe board would be rendered as:
     * 000+010+200   (1-player1  2-player2).
     * The client device can easily parse this information to
     * graphically represent it.
     */
    public void encodeBegin(FacesContext context, UIComponent component)
          throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String id = component.getId();
        Object value = ((ValueHolder) component).getValue();
        GameBoard board = (GameBoard) value;
        writer.write(id + "=");

        // go through all cells and write their values to the 
        // response stream.
        for (int i = 0; i < board.getRows(); i++) {
            if (i > 0) {
                writer.write("+");
            }
            for (int j = 0; j < board.getColumns(); j++) {
                int cellValue = board.getCellValue(i, j);
                boolean visited = false;
                if (((cellValue & GameBoard.CLIENT_PLAYER) != 0) ||
                    ((cellValue & GameBoard.SERVER_PLAYER) != 0)) {
                    visited = true;
                }
                if (visited) {
                    writer.write('0' + cellValue);
                } else {
                    writer.write('0');
                }
            }
        }
    }

    /**
     * Reconfigure the size of the game board (if necessary),
     * and record current coordinates coming from the client
     * device.
     */
    public void decode(FacesContext context, UIComponent component) {
        if (context == null || component == null) {
            return;
        }

        UIInput input = (UIInput) component;
        String id = input.getId();
        Object value = input.getValue();
        GameBoard board = (GameBoard) value;

        input.setValue(value);

        Map parameters = context.getExternalContext().getRequestParameterMap();
        String boardSize = (String) parameters.get("boardsize");
        if (boardSize != null) {
            int size = new Integer(boardSize).intValue();
            if (board.getRows() != size) {
                board.setRows(new Integer(boardSize).intValue());
                board.setColumns(new Integer(boardSize).intValue());
                board.initialize();
            }
        }
        String coords = (String) parameters.get(id);
        if (coords == null) {
            return;
        }

        try {
            board.setCurrent(coords);
        } catch (Exception ex) {
            input.setValid(false);
            context.addMessage(id, new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                                    "Invalid position",
                                                    "The position that you specified is invalid"));
        }
    }
}

