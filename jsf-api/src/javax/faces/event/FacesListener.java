/*
 * $Id: FacesListener.java,v 1.5 2003/08/18 16:38:26 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.event;


import java.util.EventListener;


/**
 * <p>A generic base interface for event listeners for various types of
 * {@link FacesEvent}s.  All listener interfaces for specific
 * {@link FacesEvent} event types must extend this interface.</p>
 *
 * <p>Implementations of this interface must have a zero-args public
 * constructor.  If the class that implements this interface has state
 * that needs to be saved and restored between requests, the class must
 * also implement {@link javax.faces.component.StateHolder}.</p>
 */

public interface FacesListener extends EventListener {


    /**
     * <p>Return the identifier of the request processing phase during
     * which this listener is interested in processing events of
     * the type we are supporting.  Legal values are the singleton
     * instances defined by the {@link PhaseId} class.</p>
     *
     * <p>{@link FacesListener} instances should generally return
     * <code>PhaseId.ANY_PHASE</code> to be notified about events no matter
     * what the current request processing phase is.</p>
     */
    public PhaseId getPhaseId();


}
