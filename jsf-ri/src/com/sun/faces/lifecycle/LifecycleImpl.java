/*
 * $Id: LifecycleImpl.java,v 1.70 2006/10/03 23:32:13 rlubke Exp $
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

package com.sun.faces.lifecycle;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.render.ResponseStateManager;
import javax.servlet.http.HttpServletRequest;

import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;
import com.sun.faces.RIConstants;

/**
 * <p><b>LifecycleImpl</b> is the stock implementation of the standard
 * Lifecycle in the JavaServer Faces RI.</p>
 */

public class LifecycleImpl extends Lifecycle {


    // -------------------------------------------------------- Static Variables


    // Log instance for this class
    private static Logger LOGGER = Util.getLogger(Util.FACES_LOGGER 
            + Util.LIFECYCLE_LOGGER);


    // ------------------------------------------------------ Instance Variables


    // The set of PhaseListeners registered with this Lifecycle instance
    private CopyOnWriteArrayList<PhaseListener> listeners =
          new CopyOnWriteArrayList<PhaseListener>();


    // The set of Phase instances that are executed by the execute() method
    // in order by the ordinal property of each phase
    private Phase[] phases = {
        null, // ANY_PHASE placeholder, not a real Phase
        new RestoreViewPhase(),
        new ApplyRequestValuesPhase(),
        new ProcessValidationsPhase(),
        new UpdateModelValuesPhase(),
        new InvokeApplicationPhase()
    };


    // The Phase instance for the render() method
    private Phase response = new RenderResponsePhase();
        

    // ------------------------------------------------------- Lifecycle Methods


    // Execute the phases up to but not including Render Response
    public void execute(FacesContext context) throws FacesException {

        if (context == null) {
            throw new NullPointerException
                (MessageUtils.getExceptionMessageString
                 (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "context"));
        }

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("execute(" + context + ")");
        }
        
        context.getExternalContext().getRequestMap().put(RIConstants.DEFAULT_LIFECYCLE,
                                                         Boolean.TRUE);
        
        for (int i = 1; i < phases.length; i++) { // Skip ANY_PHASE placeholder

            if (context.getRenderResponse() ||
                context.getResponseComplete()) {
                break;
            }

            phase((PhaseId) PhaseId.VALUES.get(i), phases[i], context);

            if (reload((PhaseId) PhaseId.VALUES.get(i), context)) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Skipping rest of execute() because of a reload");
                }
                context.renderResponse();
            }
        }

    }


    // Execute the Render Response phase
    public void render(FacesContext context) throws FacesException {

        if (context == null) {
            throw new NullPointerException
                (MessageUtils.getExceptionMessageString
                 (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "context"));
        }

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("render(" + context + ")");
        }

        if (!context.getResponseComplete()) {
            phase(PhaseId.RENDER_RESPONSE, response, context);
        }

    }


    // Add a new PhaseListener to the set of registered listeners
    public void addPhaseListener(PhaseListener listener) {

        if (listener == null) {
            throw new NullPointerException
                (MessageUtils.getExceptionMessageString
                 (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "listener"));
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("addPhaseListener(" + listener.getPhaseId().toString()
                      + "," + listener);
        }
        listeners.add(listener);        

    }


    // Return the set of PhaseListeners that have been registered
    public PhaseListener[] getPhaseListeners() {

        return listeners.toArray(new PhaseListener[listeners.size()]);      

    }


    // Remove a registered PhaseListener from the set of registered listeners
    public void removePhaseListener(PhaseListener listener) {

        if (listener == null) {
            throw new NullPointerException
                  (MessageUtils.getExceptionMessageString
                        (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID, "listener"));
        }
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("removePhaseListener(" +
                        listener.getPhaseId().toString()
                        + "," + listener);
        }

        listeners.remove(listener);


    }


    // --------------------------------------------------------- Private Methods


    // Execute the specified phase, calling all listeners as well
    private void phase(PhaseId phaseId, Phase phase, FacesContext context)
          throws FacesException {
        boolean exceptionThrown = false;
        Throwable ex = null;
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("phase(" + phaseId.toString() + ',' + context + ')');
        }
               
        ListIterator<PhaseListener> listenersIterator = listeners.listIterator();
        try {
            // Notify the "beforePhase" method of interested listeners
            // (ascending)

            if (listenersIterator.hasNext()) {
                PhaseEvent event = new PhaseEvent(context, phaseId, this);
                while (listenersIterator.hasNext())  {
                    PhaseListener listener = listenersIterator.next();
                    if (phaseId.equals(listener.getPhaseId()) ||
                        PhaseId.ANY_PHASE.equals(listener.getPhaseId())) {
                        listener.beforePhase(event);                        
                    }                   
                }
            }
        }
        catch (Exception e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning("phase("
                               + phaseId.toString()
                               + ','
                               + context
                               +
                               ") threw exception: "
                               + e
                               + ' '
                               + e.getMessage()
                               +
                               "\n"
                               + Util.getStackTraceString(e));
            }
            // move the iterator pointer back one
            if (listenersIterator.hasPrevious()) {
                listenersIterator.previous();
            }
        }

        try {
            // Execute this phase itself (if still needed)
            if (!skipping(phaseId, context)) {
                long start = System.currentTimeMillis();
                phase.execute(context);
                long stop = System.currentTimeMillis();
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("TIMING: Exectution time for phase '"
                        + phaseId.toString() 
                        + "': "
                        + (stop - start));
                }
            }
        } catch (Exception e) {
            // Log the problem, but continue
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING,
                           "executePhase(" + phaseId.toString() + ','
                           + context + ") threw exception",
                           e);
            }
            ex = e;
            exceptionThrown = true;
        }
        finally {
            try {
                // Notify the "afterPhase" method of interested listeners
                // (descending)
                if (listenersIterator.hasPrevious()) {
                    PhaseEvent event = new PhaseEvent(context, phaseId, this);
                    while (listenersIterator.hasPrevious()) {
                        PhaseListener listener = listenersIterator.previous();
                        if (phaseId.equals(listener.getPhaseId()) ||
                            PhaseId.ANY_PHASE.equals(listener.getPhaseId())) {
                            listener.afterPhase(event);
                        }
                    }
                }
            }
            catch (Throwable e) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.warning("phase("
                                   + phaseId.toString()
                                   + ','
                                   + context
                                   +
                                   ") threw exception: "
                                   + e
                                   + ' '
                                   + e.getMessage()
                                   +
                                   "\n"
                                   + Util.getStackTraceString(e));
                }
            }
        }
        // Allow all afterPhase listeners to execute before throwing the
        // exception caught during the phase execution.
        if (exceptionThrown) {
            // unwind exceptions to root cause
            while (ex.getCause() != null) {
                ex = ex.getCause();
            }

            if (!(ex instanceof FacesException)) {
                ex = new FacesException(ex);
            }

            throw(FacesException) ex;
        }
    }


    // Return "true" if this request is a browser reload and we just
    // completed the Restore View phase
    private boolean reload(PhaseId phaseId, FacesContext context) {

        if (!phaseId.equals(PhaseId.RESTORE_VIEW)) {
            return (false);
        }
        if (!(context.getExternalContext().getRequest()instanceof
              HttpServletRequest)) {
            return (false);
        }
        String renderkitId =
              context.getApplication().getViewHandler().
                    calculateRenderKitId(context);
        ResponseStateManager rsm =
              RenderKitUtils.getResponseStateManager(context,
                                                     renderkitId);
        boolean postback = rsm.isPostback(context);
        if (postback) {
            return false;
        }
        // assume it is reload.
        return true;
    }


    // Return "true" if we should be skipping the actual phase execution
    private boolean skipping(PhaseId phaseId, FacesContext context) {

        if (context.getResponseComplete()) {
            return (true);
        } else if (context.getRenderResponse() &&
                   !phaseId.equals(PhaseId.RENDER_RESPONSE)) {
            return (true);
        } else {
            return (false);
        }

    }
        
}
