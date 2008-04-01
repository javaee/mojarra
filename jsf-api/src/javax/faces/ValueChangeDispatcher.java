/*
 * $Id: ValueChangeDispatcher.java,v 1.3 2001/12/20 22:25:46 ofung Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces;

import java.io.IOException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.EventObject;

/**
 * A class which implements the dispatching of value-change events
 * to appropriate target value-change listener objects.  
 *
 * @see WSelectBoolean#addValueChangeListener
 * @see WSelectOne#addValueChangeListener
 * @see WTextEntry#addValueChangeListener
 */
public class ValueChangeDispatcher implements EventDispatcher {

   /**
    * Dispatches the specified value-change event to the value-change
    * listeners registered for the event's source component.  
    *
    * @param request the ServletRequest object corresponding to the client
    *        request where the associated event was generated
    * @param response the ServletResponse object used to render a response
    *        to the associated request
    * @param event the Event object being dispatched
    * @throws IOException if input or output exception occurred
    * @throws FacesException if the event is not an instance of
    *         <code>ValueChangeEvent</code>
    */
    public void dispatch(ServletRequest request, ServletResponse response,
			 EventObject event) throws IOException, FacesException {
    }

}
