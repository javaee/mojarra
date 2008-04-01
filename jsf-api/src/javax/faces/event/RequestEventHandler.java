/*
 * $Id: RequestEventHandler.java,v 1.2 2002/07/26 19:02:37 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.event;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;


/**
 * <p>A <strong>RequestEventHandler</strong> is a class that can receive
 * and process request events for a {@link UIComponent}, but is external
 * to the component class itself.  Zero or more
 * <code>RequestEventHandler</code>s can be associated with each
 * {@link UIComponent} in the request tree, and are called during the
 * <em>Handle Request Events Phase</em>.</p>
 */

public abstract class RequestEventHandler {

    /**
     * <p>Process an individual event queued to the specified
     * {@link UIComponent}.  Return <code>true</code> if
     * lifecycle processing should proceed directly to the <em>Render
     * Response</em> phase once all events have been processed for all
     * components, or <code>false</code> for the normal lifecycle flow.</p>
     *
     * @param context FacesContext for the request we are processing
     * @param component {@link UIComponent} which received this event
     * @param event Event to be processed against this component
     *
     * @exception NullPointerException if any of the parameters
     *  are <code>null</code>
     */
    public abstract boolean processEvent(FacesContext context,
                                         UIComponent component,
                                         FacesEvent event);


}
