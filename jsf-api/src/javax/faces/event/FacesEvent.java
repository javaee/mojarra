/*
 * $Id: FacesEvent.java,v 1.2 2002/09/20 02:30:17 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.event;


import java.util.EventObject;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;


/**
 * <p><strong>FacesEvent</strong> is the base class for user interface and
 * application events that can be fired by {@link UIComponent}s.  They are
 * used for the following purposes:</p>
 * <ul>
 * <li>Components that notice state changes during the <em>Apply Request
 *     Values</em> phase of the request processing lifecycle can queue
 *     events to other components, to be processed later during the
 *     <em>Handle Request Events</em> phase.</li>
 * <li>Components during any phase prior to the <em>Invoke Application</em>
 *     phase can queue events in the {@link FacesContext}, to be
 *     processed by the application during the <em>Invoke Application</em>
 *     phase.</li>
 * </ul>
 */

public class FacesEvent extends EventObject {


    // ----------------------------------------------------------- Constructors


    /**
     * <p>Construct a new event object from the specified source component.</p>
     *
     * @param component Source {@link UIComponent} for this event
     *
     * @exception IllegalArgumentException if <code>component</code> is
     *  <code>null</code>
     */
    public FacesEvent(UIComponent source) {

        super(source);

    }


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the source {@link UIComponent} that sent this event.
     */
    public UIComponent getComponent() {

        return ((UIComponent) getSource());

    }


}
