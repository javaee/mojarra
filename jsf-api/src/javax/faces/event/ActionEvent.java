/*
 * $Id: ActionEvent.java,v 1.5 2003/07/14 23:00:01 craigmcc Exp $
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
     *
     * @exception IllegalArgumentException if <code>component</code> is
     *  <code>null</code>
     */
    public ActionEvent(UIComponent component) {

        super(component);

    }


}
