/*
 * $Id: LifecycleImpl.java,v 1.62 2006/05/11 18:48:04 rlubke Exp $
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
    
    // used to track if the first request has been serviced.
    protected static final String FIRST_REQUEST_SERVICED = 
            "com.sun.faces.FIRST_REQUEST_SERVICED";


    // ------------------------------------------------------- Lifecycle Methods


    // Execute the phases up to but not including Render Response
    public void execute(FacesContext context) throws FacesException {

        if (context == null) {
            throw new NullPointerException
                (MessageUtils.getExceptionMessageString
                 (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("execute(" + context + ")");
        }                
        
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
                 (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID));
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
                 (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID));
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
                        (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID));
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
            LOGGER.fine("phase(" + phaseId.toString() + "," + context + ")");
        }

	
        int size = listeners.size();
        int revStartIndex = 0;
    try {
            // Notify the "beforePhase" method of interested listeners
	    // (ascending)
           
	    if (size > 0) {
                PhaseEvent event = new PhaseEvent(context, phaseId, this);
            for (PhaseListener listener : listeners) {
                if (phaseId.equals(listener.getPhaseId()) ||
                    PhaseId.ANY_PHASE.equals(listener.getPhaseId())) {
                    listener.beforePhase(event);  
                    revStartIndex++;
                }
                
            }
            }
	}
	catch (Exception e) {
	    if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning("phase(" + phaseId.toString() + "," + context + 
			  ") threw exception: " + e + " " + e.getMessage() +
			  "\n" + Util.getStackTraceString(e));
	    }        
        }
	    
	try {   
	    // Execute this phase itself (if still needed)
	    if (!skipping(phaseId, context)) {
		phase.execute(context);
	    }
	} catch (Exception e) {
            // Log the problem, but continue
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.log(Level.WARNING, "executePhase(" + phaseId.toString() + "," 
                        + context + ") threw exception", e);
            }
            ex = e;
            exceptionThrown = true;
        } 
	finally {
            try {
                // Notify the "afterPhase" method of interested listeners
                // (descending)
                if (size > 0) {
                    PhaseEvent event = new PhaseEvent(context, phaseId, this);
                    for (ListIterator<PhaseListener> iter = listeners.listIterator(revStartIndex);
                          iter.hasPrevious(); ) {                    
                        PhaseListener listener = iter.previous();
                        if (phaseId.equals(listener.getPhaseId()) ||
                            PhaseId.ANY_PHASE.equals(listener.getPhaseId())) {
                            listener.afterPhase(event);
                        }
                    }
                }
            }
            catch (Throwable e) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.warning("phase(" + phaseId.toString() + "," + context + 
                              ") threw exception: " + e + " " + e.getMessage() +
                              "\n" + Util.getStackTraceString(e));
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

            throw (FacesException) ex;
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
        String renderkitId = 
                context.getApplication().getViewHandler().
                calculateRenderKitId(context);
        ResponseStateManager rsm = RenderKitUtils.getResponseStateManager(context,
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
