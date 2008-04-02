/*
 * $Id: FacesEvent.java,v 1.5 2003/07/14 23:00:01 craigmcc Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.event;


import java.util.EventObject;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;


/**
 * <p><strong>FacesEvent</strong> is the base class for user interface and
 * application events that can be fired by {@link UIComponent}s.  Concrete
 * event classes must subclass {@link FacesEvent} in order to be supported
 * by the request processing lifecycle.</p>
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
    public FacesEvent(UIComponent component) {

        super(component);

    }


    // ------------------------------------------------------------- Properties


    /**
     * <p>Return the source {@link UIComponent} that sent this event.
     */
    public UIComponent getComponent() {

        return ((UIComponent) getSource());

    }


}
