/*
 * $Id: Phase.java,v 1.13 2007/08/28 06:06:15 rlubke Exp $
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

package com.sun.faces.lifecycle;

import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;

import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Timer;
import com.sun.faces.util.Util;


/**
 * <p>A <strong>Phase</strong> is a single step in the processing of a
 * JavaServer Faces request throughout its entire {@link javax.faces.lifecycle.Lifecycle}.
 * Each <code>Phase</code> performs the required transitions on the state
 * information in the {@link FacesContext} associated with this request.
 */

public abstract class Phase {


    private static Logger LOGGER = FacesLogger.LIFECYCLE.getLogger();


    // ---------------------------------------------------------- Public Methods


    /**
     * Performs PhaseListener processing and invokes the execute method
     * of the Phase.
     * @param context the FacesContext for the current request
     * @param lifecycle the lifecycle for this request
     */
    public void doPhase(FacesContext context,
                        Lifecycle lifecycle,
                        ListIterator<PhaseListener> listeners) {

        PhaseEvent event = null;
        if (listeners.hasNext()) {
            event = new PhaseEvent(context, this.getId(), lifecycle);
        }

        // start timing - include before and after phase processing
        Timer timer = Timer.getInstance();
        if (timer != null) {
            timer.startTiming();
        }

        handleBeforePhase(context, listeners, event);
        Exception ex = null;
        try {
            if (!shouldSkip(context)) {
                execute(context);
            }
        } catch (Exception e) {
             if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE,
                     "jsf.lifecycle.phase.exception",
                     new Object[]{
                          this.getId().toString(),
                          ((context.getViewRoot() != null) ? context.getViewRoot().getViewId() : ""),
                          event});
            }

            ex = e;
        } finally {
            handleAfterPhase(context, listeners, event);
            
            // stop timing
            if (timer != null) {
                timer.stopTiming();
                timer.logResult(
                      "Execution time for phase (including any PhaseListeners) -> "
                      + this.getId().toString());
            }
        }

        // handle any exceptions thrown by Phase.execute()
        if (ex != null) {
            if (!(ex instanceof FacesException)) {
                ex = new FacesException(ex);
            }

            throw (FacesException) ex;
        }

    }


     /**
     * <p>Perform all state transitions required by the current phase of the
     * request processing {@link javax.faces.lifecycle.Lifecycle} for a
     * particular request. </p>
     *
     * @param context FacesContext for the current request being processed
     * @throws FacesException if a processing error occurred while
     *                        executing this phase
     */
    public abstract void execute(FacesContext context) throws FacesException;


    /**
     * @return the current {@link javax.faces.lifecycle.Lifecycle}
     * <strong>Phase</strong> identifier.
     */
    public abstract PhaseId getId();


    // ------------------------------------------------------- Protected Methods


    /**
     * Handle <code>afterPhase</code> <code>PhaseListener</code> events.
     * @param context the FacesContext for the current request
     * @param listenersIterator a ListIterator for the PhaseListeners that need
     *  to be invoked
     * @param event the event to pass to each of the invoked listeners
     */
    protected void handleAfterPhase(FacesContext context,
                                    ListIterator<PhaseListener> listenersIterator,
                                    PhaseEvent event) {
               
        while (listenersIterator.hasPrevious()) {
            PhaseListener listener = listenersIterator.previous();
            if (this.getId().equals(listener.getPhaseId()) ||
                PhaseId.ANY_PHASE.equals(listener.getPhaseId())) {
                try {
                    listener.afterPhase(event);
                } catch (Exception e) {
                    if (LOGGER.isLoggable(Level.WARNING)) {
                        LOGGER.log(Level.WARNING,
                                   "jsf.lifecycle.phaselistener.exception",
                                   new Object[]{
                                         listener.getClass().getName()
                                         + ".afterPhase()",
                                         this.getId().toString(),
                                         ((context.getViewRoot() != null)
                                          ? context.getViewRoot().getViewId()
                                          : ""),
                                         e});
                        LOGGER.warning(Util.getStackTraceString(e));
                        return;
                    }
                }
            }
        }

    }


     /**
     * Handle <code>beforePhase</code> <code>PhaseListener</code> events.
     * @param context the FacesContext for the current request
     * @param listenersIterator a ListIterator for the PhaseListeners that need
     *  to be invoked
     * @param event the event to pass to each of the invoked listeners
     */
     protected void handleBeforePhase(FacesContext context,
                                      ListIterator<PhaseListener> listenersIterator,
                                      PhaseEvent event) {

         while (listenersIterator.hasNext()) {
             PhaseListener listener = listenersIterator.next();
             if (this.getId().equals(listener.getPhaseId()) ||
                 PhaseId.ANY_PHASE.equals(listener.getPhaseId())) {
                 try {
                     listener.beforePhase(event);
                 } catch (Exception e) {
                     if (LOGGER.isLoggable(Level.WARNING)) {
                         LOGGER.log(Level.WARNING,
                                    "jsf.lifecycle.phaselistener.exception",
                                    new Object[]{
                                          listener.getClass().getName()
                                          + ".beforePhase()",
                                          this.getId().toString(),
                                          ((context.getViewRoot() != null)
                                           ? context.getViewRoot().getViewId()
                                           : ""),
                                          e});
                         LOGGER.warning(Util.getStackTraceString(e));
                     }
                     // move the iterator pointer back one
                     if (listenersIterator.hasPrevious()) {
                         listenersIterator.previous();
                     }
                     return;
                 }
             }
         }

     }


    // --------------------------------------------------------- Private Methods


    /**
     * @param context the FacesContext for the current request
     * @return <code>true</code> if <code>FacesContext.responseComplete()</code>
     *  or <code>FacesContext.renderResponse()</code> and the phase is not
     *  RENDER_RESPONSE, otherwise return <code>false</code>
     */
    private boolean shouldSkip(FacesContext context) {

        if (context.getResponseComplete()) {
            return (true);
        } else if (context.getRenderResponse() &&
                   !PhaseId.RENDER_RESPONSE.equals(this.getId())) {
            return (true);
        } else {
            return (false);
        }

    }

}
