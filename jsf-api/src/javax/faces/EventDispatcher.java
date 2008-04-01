/*
 * $Id: EventDispatcher.java,v 1.4 2002/01/25 18:35:06 visvan Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

import java.io.IOException;
import java.util.EventObject;

/**
 * The interface used to implement objects which are capable of
 * dispatching an event to appropriate target listeners.  
 * <p>
 * An EventDispatcher object is obtained from the event context.
 * @see EventContext#getEventDispatcher
 * <p>
 * By default the UIComponent instances which generate events will
 * act as the event dispatcher for those events, however applications
 * can change this behavior by overriding <code>getEventDispatcher</code>.
 */
public interface EventDispatcher {

   /**
    * Dispatches the specified Event object to its appropriate target
    * listeners.
    *
    * @param event the Event object being dispatched
    * @throws IOException if input or output exception occurred
    * @throws FacesException if dispatcher is unable to dispatch the
    *         specified event
    */
    void dispatch(EventObject event) throws IOException, FacesException;

}
