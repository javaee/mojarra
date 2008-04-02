/*
 * $Id: ActionEvent.java,v 1.3 2003/02/20 22:46:27 ofung Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.event;


import javax.faces.component.UIComponent;


/**
 * <p>An {@link ActionEvent} represents the activation of a user interface
 * component (such as a <code>UICommand</code>).</p>
 */

public class ActionEvent extends FacesEvent {


    // ----------------------------------------------------------- Constructors


    /**
     * <p>Construct a new event object from the specified source component
     * and action command.</p>
     *
     * @param component Source {@link UIComponent} for this event
     * @param actionCommand String identifying the command that was activated
     *
     * @exception IllegalArgumentException if <code>component</code> is
     *  <code>null</code>
     */
    public ActionEvent(UIComponent source, String actionCommand) {

        super(source);
        this.actionCommand = actionCommand;

    }


    // ------------------------------------------------------------- Properties


    /**
     * <p>The string identifying the command that was activated.</p>
     */
    private String actionCommand = null;


    /**
     * <p>Return the String identifying the command that was activated.</p>
     */
    public String getActionCommand() {

        return (this.actionCommand);

    }


}
