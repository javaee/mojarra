/*
 * $Id: PhaseListener.java,v 1.2 2003/07/07 20:49:24 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.event;


import java.io.Serializable;
import java.util.EventListener;


/**
 * <p>An interface implemented by objects that wish to be notified at
 * the beginning and ending of processing for each standard phase of the
 * request processing lifecycle.</p>
 */

public interface PhaseListener extends EventListener, Serializable {


    /**
     * <p>Handle a notification that the processing for a particular
     * phase has just been completed.</p>
     */
    public void afterPhase(PhaseEvent event);


    /**
     * <p>Handle a notification that the processing for a particular
     * phase of the request processing lifecycle is about to begin.</p>
     */
    public void beforePhase(PhaseEvent event);


    /**
     * <p>Return the identifier of the request processing phase during
     * which this listener is interested in processing {@link PhaseEvent}
     * events.  Legal values are the singleton instances defined by the
     * {@link PhaseId} class, including <code>PhaseId.ANY_PHASE</code>
     * to indicate an interest in being notified for all standard phases.</p>
     */
    public PhaseId getPhaseId();


}
