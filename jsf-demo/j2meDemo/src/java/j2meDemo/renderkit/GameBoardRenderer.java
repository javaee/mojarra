package j2meDemo.renderkit;

import java.io.IOException;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.ValueHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import javax.faces.render.Renderer;

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
            if (i > 0) writer.write("+");
            for (int j = 0; j < board.getColumns(); j++) {
                int cellValue = board.getCellValue(i, j);
                boolean visited = false;
                if (((cellValue & GameBoard.CLIENT_PLAYER) != 0) ||
                    ((cellValue & GameBoard.SERVER_PLAYER) != 0)) {
                    visited = true;
                }
                if (visited) {
                    writer.write('0' + cellValue);
                } else 
                    writer.write('0');
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
        String boardSize = (String)parameters.get("boardsize");
        if (boardSize != null) {
            int size = new Integer(boardSize).intValue();
            if (board.getRows() != size) {
                board.setRows(new Integer(boardSize).intValue());
                board.setColumns(new Integer(boardSize).intValue());
                board.initialize();
            }
        }
        String coords = (String) parameters.get(id);
        if (coords == null) return;      

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

