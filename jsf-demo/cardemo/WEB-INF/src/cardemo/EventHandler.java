/*
 * $Id: EventHandler.java,v 1.1 2002/09/30 21:42:20 jball Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package cardemo;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.RequestEventHandler;
import javax.faces.event.FacesEvent;

/**
 * The listener interface for handling request events.
 * An object should implement this interface if it needs
 * to respond to a request event.
 */
public class EventHandler extends RequestEventHandler {

    public EventHandler ( ) {
    }

    public boolean processEvent(FacesContext context,
				UIComponent component,
				FacesEvent event) {
	System.out.println("Received Event: " + event + " component: " + 
			   component);
	return false;
    }
    
}
