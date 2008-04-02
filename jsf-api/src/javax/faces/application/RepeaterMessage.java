/*
 * $Id: RepeaterMessage.java,v 1.2 2003/09/11 15:25:57 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.application;


import java.io.Serializable;
import javax.faces.component.Repeater;


/**
 * <p><strong>RepeaterMessage</strong> is a wrapper for messages that are
 * queued by a child component of a {@link Repeater}, for a particular
 * row's instantiation of that component.</p>
 */

public class RepeaterMessage implements Message, Serializable {


    // ------------------------------------------------------------ Constructors


    /**
     * <p>Construct a new {@link RepeaterMessage} wrapping the specified
     * {@link Message}.</p>
     *
     * @param message The {@link Message} to be wrapped
     * @param rowIndex The row index for the associated {@link Repeater}
     */
    public RepeaterMessage(Message message, int rowIndex) {

        this.message = message;
        this.rowIndex = rowIndex;

    }


    // ------------------------------------------------------ Instance Variables


    private Message message;
    private int rowIndex;


    // -------------------------------------------------------------- Properties


    /**
     * <p>Return the {@link Message} we are wrapping.</p>
     */
    public Message getMessage() {

        return (this.message);

    }


    /**
     * <p>Return the row index for the corresponding {@link Repeater}.</p>
     */
    public int getRowIndex() {

        return (this.rowIndex);

    }


    // --------------------------------------------------------- Public Methods


    public String getDetail() {

        return (message.getDetail());

    }


    public int getSeverity() {

        return (message.getSeverity());

    }


    public String getSummary() {

        return (message.getSummary());

    }


}
