/*
 * $Id: FacesListener.java,v 1.3 2003/01/18 01:58:57 craigmcc Exp $
 */

/*
 * Copyright 2002-2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.event;


import java.io.Serializable;
import java.util.EventListener;


/**
 * <p>A generic base interface for event listeners for various types of
 * {@link FacesEvent}s.  All listener interfaces for specific
 * {@link FacesEvent} event types must extend this interface.</p>
 */

public interface FacesListener extends EventListener, Serializable {


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
