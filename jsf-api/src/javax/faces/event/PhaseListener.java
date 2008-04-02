/*
 * $Id: PhaseListener.java,v 1.5 2005/08/22 22:08:06 ofung Exp $
 */

/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
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
