/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
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
import javax.faces.context.Flash;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.lifecycle.Lifecycle;

import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Timer;
import com.sun.faces.util.RequestStateManager;


/**
 * <p>A <strong>Phase</strong> is a single step in the processing of a
 * JavaServer Faces request throughout its entire {@link javax.faces.lifecycle.Lifecycle}.
 * Each <code>Phase</code> performs the required transitions on the state
 * information in the {@link FacesContext} associated with this request.
 */

public abstract class Phase {

    private static final Logger LOGGER = FacesLogger.LIFECYCLE.getLogger();

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

        context.setCurrentPhaseId(getId());
        PhaseEvent event = null;
        if (listeners.hasNext()) {
            event = new PhaseEvent(context, this.getId(), lifecycle);
        }

        // start timing - include before and after phase processing
        Timer timer = Timer.getInstance();
        if (timer != null) {
            timer.startTiming();
        }

        try {
            handleBeforePhase(context, listeners, event);
            if (!shouldSkip(context)) {
                execute(context);
            }
        } catch (Throwable e) {
            queueException(context, e);
        } finally {
            try {
                handleAfterPhase(context, listeners, event);
            } catch (Throwable e) {
                queueException(context, e);
            }
            // stop timing
            if (timer != null) {
                timer.stopTiming();
                timer.logResult(
                      "Execution time for phase (including any PhaseListeners) -> "
                      + this.getId().toString());
            }

            context.getExceptionHandler().handle();
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


     protected void queueException(FacesContext ctx, Throwable t) {

        queueException(ctx, t, null);

    }


    protected void queueException(FacesContext ctx, Throwable t, String booleanKey) {

        ExceptionQueuedEventContext extx = new ExceptionQueuedEventContext(ctx, t);
        if (booleanKey != null) {
            extx.getAttributes().put(booleanKey, Boolean.TRUE);
        }
        ctx.getApplication().publishEvent(ctx, ExceptionQueuedEvent.class, extx);

    }


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

        try {
            Flash flash = context.getExternalContext().getFlash();
            flash.doPostPhaseActions(context);
        } catch (UnsupportedOperationException uoe) {
            if (LOGGER.isLoggable(Level.FINE)) {
                 LOGGER.fine("ExternalContext.getFlash() throw UnsupportedOperationException -> Flash unavailable");
            }    
        }
        while (listenersIterator.hasPrevious()) {
            PhaseListener listener = listenersIterator.previous();
            if (this.getId().equals(listener.getPhaseId()) ||
                PhaseId.ANY_PHASE.equals(listener.getPhaseId())) {
                try {
                    listener.afterPhase(event);
                } catch (Exception e) {
                    queueException(context,
                                   e,
                                   ExceptionQueuedEventContext.IN_AFTER_PHASE_KEY);
                    return;
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

         try {
            Flash flash = context.getExternalContext().getFlash();
            flash.doPrePhaseActions(context);
         } catch (UnsupportedOperationException uoe) {
             if (LOGGER.isLoggable(Level.FINE)) {
                 LOGGER.fine("ExternalContext.getFlash() throw UnsupportedOperationException -> Flash unavailable");
             }
         }
         RequestStateManager.clearAttributesForPhase(context,
                                                     context.getCurrentPhaseId());
         while (listenersIterator.hasNext()) {
             PhaseListener listener = listenersIterator.next();
             if (this.getId().equals(listener.getPhaseId()) ||
                 PhaseId.ANY_PHASE.equals(listener.getPhaseId())) {
                 try {
                     listener.beforePhase(event);
                 } catch (Exception e) {
                     queueException(context,
                                    e,
                                    ExceptionQueuedEventContext.IN_BEFORE_PHASE_KEY);
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
