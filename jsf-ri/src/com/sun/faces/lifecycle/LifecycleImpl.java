/*
 * $Id: LifecycleImpl.java,v 1.82 2007/08/20 22:42:59 rlubke Exp $
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

import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;
import com.sun.faces.util.Timer;
import com.sun.faces.util.FacesLogger;

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


/**
 * <p><b>LifecycleImpl</b> is the stock implementation of the standard
 * Lifecycle in the JavaServer Faces RI.</p>
 */

public class LifecycleImpl extends Lifecycle {


    // -------------------------------------------------------- Static Variables


    // Log instance for this class
    private static Logger LOGGER = FacesLogger.LIFECYCLE.getLogger();


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
        
        for (int i = 1; i < phases.length; i++) { // Skip ANY_PHASE placeholder

            if (context.getRenderResponse() ||
                context.getResponseComplete()) {
                break;
            }

            PhaseId phaseId = (PhaseId) PhaseId.VALUES.get(i);
            phase(phaseId, phases[i], context);

            if (reload(phaseId, context)) {
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

        if (isListenerPresent(listener)) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE,
                           "jsf.lifecycle.duplicate_phase_listener_detected",
                           listener.getClass().getName());
            }
        } else {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("addPhaseListener("
                            + listener.getPhaseId().toString()
                            + ","
                            + listener);
            }
            listeners.add(listener);
        }

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

        if (PhaseId.RESTORE_VIEW.equals(phaseId)) {
            Util.getViewHandler(context).initView(context);
        }
               
        ListIterator<PhaseListener> listenersIterator = listeners.listIterator();
        String listenerClass = null;
        try {
            // Notify the "beforePhase" method of interested listeners
            // (ascending)

            if (listenersIterator.hasNext()) {
                PhaseEvent event = new PhaseEvent(context, phaseId, this);
                while (listenersIterator.hasNext())  {
                    PhaseListener listener = listenersIterator.next();
                    listenerClass = listener.getClass().getName();
                    if (phaseId.equals(listener.getPhaseId()) ||
                        PhaseId.ANY_PHASE.equals(listener.getPhaseId())) {
                        listener.beforePhase(event);                        
                    }                   
                }
            }
        } catch (Exception e) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING,
                         "jsf.lifecycle.phaselistener.exception",
                         new Object[]{
                              listenerClass + ".beforePhase()",
                              phaseId.toString(),
                              ((context.getViewRoot() != null) ? context.getViewRoot().getViewId() : ""),
                              e});
                    LOGGER.warning(Util.getStackTraceString(e));
            }
            // move the iterator pointer back one
            if (listenersIterator.hasPrevious()) {
                listenersIterator.previous();
            }
        }

        try {
            // Execute this phase itself (if still needed)
            if (!skipping(phaseId, context)) {
                Timer timer = Timer.getInstance();
                if (timer != null) {
                    timer.startTiming();
                }

                phase.execute(context);

                if (timer != null) {
                    timer.stopTiming();
                    timer.logResult("Execution time for phase '"
                        + phaseId.toString());
                }             
            }
        } catch (Exception e) {
            // Log the problem, but continue
            if (LOGGER.isLoggable(Level.SEVERE)) {
                LOGGER.log(Level.SEVERE,
                     "jsf.lifecycle.phase.exception",
                     new Object[]{
                          phaseId.toString(),
                          ((context.getViewRoot() != null) ? context.getViewRoot().getViewId() : ""),
                          e});
            }

            ex = e;
            exceptionThrown = true;
        } finally {
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
            } catch (Throwable e) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING,
                         "jsf.lifecycle.phaselistener.exception",
                         new Object[]{
                              listenerClass + ".afterPhase()",
                              phaseId.toString(),
                              ((context.getViewRoot() != null) ? context.getViewRoot().getViewId() : ""),
                              e});
                    LOGGER.warning(Util.getStackTraceString(e));
                }
            }
        }
        // Allow all afterPhase listeners to execute before throwing the
        // exception caught during the phase execution.
        if (exceptionThrown) {
 
            if (!(ex instanceof FacesException)) {
                ex = new FacesException(ex);
            }

            throw(FacesException) ex;
        }
    }


    // Return "true" if this request is a browser reload and we just
    // completed the Restore View phase
    private boolean reload(PhaseId phaseId, FacesContext context) {

        if (!PhaseId.RESTORE_VIEW.equals(phaseId)) {
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
                   !PhaseId.RENDER_RESPONSE.equals(phaseId)) {
            return (true);
        } else {
            return (false);
        }

    }

    private boolean isListenerPresent(PhaseListener listener) {

        for (int i = 0, len = listeners.size(); i < len; i++) {
            if (listeners.get(i).getClass().getName()
                  .equals(listener.getClass().getName())) {
                return true;
            }
        }
        return false;

    }
        
}
