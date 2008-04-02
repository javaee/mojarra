/*
 * $Id: LifecycleImpl.java,v 1.43 2004/02/06 18:55:08 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.sun.faces.lifecycle;

import com.sun.faces.util.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * <p><b>LifecycleImpl</b> is the stock implementation of the standard
 * Lifecycle in the JavaServer Faces RI.</p>
 */

public class LifecycleImpl extends Lifecycle {


    // -------------------------------------------------------- Static Variables


    // Log instance for this class
    private static final Log log = LogFactory.getLog(LifecycleImpl.class);


    // ------------------------------------------------------ Instance Variables


    // The set of PhaseListeners registered with this Lifecycle instance
    private List listeners = new ArrayList();


    // The set of Phase instances that are executed by the execute() method
    // in order by the ordinal property of each phase
    private Phase phases[] = {
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
                (Util.getExceptionMessage
                 (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        if (log.isDebugEnabled()) {
            log.debug("execute(" + context + ")");
        }

        for (int i = 1; i < phases.length; i++) { // Skip ANY_PHASE placeholder

            if (context.getRenderResponse() ||
                context.getResponseComplete()) {
                break;
            }

            phase((PhaseId) PhaseId.VALUES.get(i), phases[i], context);

            if (reload((PhaseId) PhaseId.VALUES.get(i), context)) {
                if (log.isDebugEnabled()) {
                    log.debug("Skipping rest of execute() because of a reload");
                }
                context.renderResponse();
            }
        }

    }


    // Execute the Render Response phase
    public void render(FacesContext context) throws FacesException {

        if (context == null) {
            throw new NullPointerException
                (Util.getExceptionMessage
                 (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        if (log.isDebugEnabled()) {
            log.debug("render(" + context + ")");
        }

        if (!context.getResponseComplete()) {
            phase(PhaseId.RENDER_RESPONSE, response, context);
        }

    }


    // Add a new PhaseListener to the set of registered listeners
    public void addPhaseListener(PhaseListener listener) {

        if (listener == null) {
            throw new NullPointerException
                (Util.getExceptionMessage
                 (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        if (log.isDebugEnabled()) {
            log.debug("addPhaseListener(" + listener.getPhaseId().toString()
                      + "," + listener);
        }
        synchronized (listeners) {
            listeners.add(listener);
        }

    }


    // Return the set of PhaseListeners that have been registered
    public PhaseListener[] getPhaseListeners() {

        synchronized (listeners) {
            PhaseListener results[] = new PhaseListener[listeners.size()];
            return ((PhaseListener[]) listeners.toArray(results));
        }

    }


    // Remove a registered PhaseListener from the set of registered listeners
    public void removePhaseListener(PhaseListener listener) {

        if (listener == null) {
            throw new NullPointerException
                (Util.getExceptionMessage
                 (Util.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        if (log.isDebugEnabled()) {
            log.debug("removePhaseListener(" +
                      listener.getPhaseId().toString()
                      + "," + listener);
        }
        synchronized (listeners) {
            listeners.remove(listener);
        }

    }


    // --------------------------------------------------------- Private Methods


    // Execute the specified phase, calling all listeners as well
    private void phase(PhaseId phaseId, Phase phase, FacesContext context)
        throws FacesException {

        if (log.isTraceEnabled()) {
            log.trace("phase(" + phaseId.toString() + "," + context + ")");
        }

        // Notify the "beforePhase" method of interested listeners (ascending)
        synchronized (listeners) {
            if (listeners.size() > 0) {
                PhaseEvent event = new PhaseEvent(context, phaseId, this);
                for (int i = 0; i < listeners.size(); i++) {
                    PhaseListener listener = (PhaseListener) listeners.get(i);
                    if (phaseId.equals(listener.getPhaseId()) ||
                        PhaseId.ANY_PHASE.equals(listener.getPhaseId())) {
                        listener.beforePhase(event);
                    }
                }
            }
        }

        // Execute this phase itself (if still needed)
        if (!skipping(phaseId, context)) {
            phase.execute(context);
        }

        // Notify the "afterPhase" method of interested listeners (descending)
        synchronized (listeners) {
            if (listeners.size() > 0) {
                PhaseEvent event = new PhaseEvent(context, phaseId, this);
                for (int i = listeners.size() - 1; i >= 0; i--) {
                    PhaseListener listener = (PhaseListener) listeners.get(i);
                    if (phaseId.equals(listener.getPhaseId()) ||
                        PhaseId.ANY_PHASE.equals(listener.getPhaseId())) {
                        listener.afterPhase(event);
                    }
                }
            }
        }

    }


    // Return "true" if this request is a browser reload and we just
    // completed the Restore View phase
    private boolean reload(PhaseId phaseId, FacesContext context) {

        if (!phaseId.equals(PhaseId.RESTORE_VIEW)) {
            return (false);
        }
        if (!(context.getExternalContext().getRequest() instanceof
            HttpServletRequest)) {
            return (false);
        }
        HttpServletRequest request = (HttpServletRequest)
            context.getExternalContext().getRequest();
        String method = request.getMethod();

        // Is this a GET request with query parameters?
        if ("GET".equals(method)) {
            Iterator names = context.getExternalContext().
                getRequestParameterNames();
            if (names.hasNext()) {
                return (false);
            }
        }

        // Is this a POST or PUT request?
        if ("POST".equals(method) || "PUT".equals(method)) {
            return (false);
        }

        // Assume this is a reload
        return (true);

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
