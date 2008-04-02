/*
 * $Id: ApplicationHandler.java,v 1.7 2003/02/20 22:46:33 ofung Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.lifecycle;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.FacesEvent;


/**
 * <p><strong>ApplicationHandler</strong> is an interface defining
 * application callbacks that will be invoked during the <em>Invoke
 * Application</em> phase of the request processing lifecycle.  Each
 * application event that has been queued to the {@link FacesContext}
 * will trigger a call to one of the methods defined on this interface.</p>
 *
 * <p>It is the responsibility of any application wishing to respond to
 * application level events to register an instance of this interface,
 * by calling the <code>setApplicationHandler()</code> method of the
 * {@link Lifecycle} instance used to process incoming requests, prior
 * to receiving the first request.</p>
 *
 * @deprecated The current mechanism for handling application events is a
 *  placeholder, and will be replaced in the next public release of
 *  JavaServer Faces.
 */

public interface ApplicationHandler {


    /**
     * <p>Process a command event that has been queued for the application.
     * Return <code>true</code> if control should be transferred directly
     * to the <em>Render Response</em> phase (bypassing any remaining
     * application events that might have been queued), or <code>false</code>
     * to process any remaining events normally.</p>
     *
     * @param context FacesContext for the current request
     * @param event FacesEvent to be processed
     *
     * @exception NullPointerException if any parameter is <code>null</code>
     */
    public boolean processEvent(FacesContext context, FacesEvent event);


}
