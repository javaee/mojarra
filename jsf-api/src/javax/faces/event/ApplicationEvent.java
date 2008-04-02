/*
 * $Id: ApplicationEvent.java,v 1.1 2002/09/21 22:24:34 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.event;


import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;


/**
 * <p><strong>ApplicationEvent</strong> is a subclass of {@link FacesEvent}
 * that describes an event queued in any request processing lifecycle phase
 * up to (and including) the <em>Invoke Application</em> phase, and is
 * handled during the <em>Invoke Application</em> phase.</p>
 */

public class ApplicationEvent extends FacesEvent {


    // ----------------------------------------------------------- Constructors


    /**
     * <p>Construct a new event object from the specified source component.</p>
     *
     * @param component Source {@link UIComponent} for this event
     *
     * @exception IllegalArgumentException if <code>component</code> is
     *  <code>null</code>
     */
    public ApplicationEvent(UIComponent source) {

        super(source);

    }


    // --------------------------------------------------------- Public Methods


    /**
     * <p>Return a String rendition of this object value.</p>
     */
    public String toString() {

        StringBuffer sb = new StringBuffer("ApplicationEvent[source=");
        sb.append(getSource());
        sb.append("]");
        return (sb.toString());

    }


}
