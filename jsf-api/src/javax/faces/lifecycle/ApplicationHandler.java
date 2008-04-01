/*
 * $Id: ApplicationHandler.java,v 1.2 2002/06/03 19:34:27 craigmcc Exp $
 */

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.lifecycle;

import java.util.SortedMap;
import javax.faces.FacesException;         // FIXME - subpackage?
import javax.faces.context.FacesContext;
import javax.faces.event.CommandEvent;
import javax.faces.event.FormEvent;


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
 */

public interface ApplicationHandler {


    /**
     * <p>Process a command event that has been queued for the application.
     * <strong>FIXME</strong> - does the application need to provide any
     * feedback to the lifecycle state machine?</p>
     *
     * @param context FacesContext for the current request
     * @param event CommandEvent to be processed
     */
    public void commandEvent(FacesContext context, CommandEvent event);


    /**
     * <p>Process a form event that has been queued for the application.
     * <strong>FIXME</strong> - does the application need to provide any
     * feedback to the lifecycle state machine?</p>
     *
     * @param context FacesContext for the current request
     * @param event FormEvent to be processed
     */
    public void formEvent(FacesContext context, FormEvent event);


}
