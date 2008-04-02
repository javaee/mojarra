/*
 * $Id: LifecycleImpl.java,v 1.58 2006/01/11 15:28:06 rlubke Exp $
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

import com.sun.faces.util.Util;
import com.sun.faces.util.MessageUtils;

import java.util.logging.Logger;
import java.util.logging.Level;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import javax.servlet.http.HttpServletRequest;
import javax.faces.render.ResponseStateManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.sun.faces.el.ImplicitObjectELResolverForJsp;
import com.sun.faces.el.ManagedBeanELResolver;
import com.sun.faces.el.PropertyResolverChainWrapper;
import com.sun.faces.el.VariableResolverChainWrapper;
import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.el.FacesResourceBundleELResolver;
import com.sun.faces.renderkit.RenderKitUtils;

import javax.el.CompositeELResolver;
import javax.el.ELResolver;

/**
 * <p><b>LifecycleImpl</b> is the stock implementation of the standard
 * Lifecycle in the JavaServer Faces RI.</p>
 */

public class LifecycleImpl extends Lifecycle {


    // -------------------------------------------------------- Static Variables


    // Log instance for this class
    private static Logger logger = Util.getLogger(Util.FACES_LOGGER 
            + Util.LIFECYCLE_LOGGER);


    // ------------------------------------------------------ Instance Variables


    // The set of PhaseListeners registered with this Lifecycle instance
    private ArrayList<PhaseListener> listeners = new ArrayList<PhaseListener>();


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

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("execute(" + context + ")");
        }
        
        // populate the FacesCompositeELResolver stack if a request is being
        // processed for the very first time.
        populateFacesELResolverForJsp(context);
        
        for (int i = 1; i < phases.length; i++) { // Skip ANY_PHASE placeholder

            if (context.getRenderResponse() ||
                context.getResponseComplete()) {
                break;
            }

            phase((PhaseId) PhaseId.VALUES.get(i), phases[i], context);

            if (reload((PhaseId) PhaseId.VALUES.get(i), context)) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("Skipping rest of execute() because of a reload");
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

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("render(" + context + ")");
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
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("addPhaseListener(" + listener.getPhaseId().toString()
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
                (MessageUtils.getExceptionMessageString
                 (MessageUtils.NULL_PARAMETERS_ERROR_MESSAGE_ID));
        }
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("removePhaseListener(" +
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
        boolean exceptionThrown = false;
        Throwable ex = null;
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("phase(" + phaseId.toString() + "," + context + ")");
        }

	int 
	    i = 0,
	    maxBefore = 0;
        List<PhaseListener> tempListeners = (ArrayList<PhaseListener>)listeners.clone();
	try {
            // Notify the "beforePhase" method of interested listeners
	    // (ascending)
            // Fix for bug 6223295. Get a pointer to 'listeners' so that 
            // we still have reference to the original list for the current 
            // thread. As a result, any listener added would not show up 
            // until the NEXT phase but we want to avoid the lengthy
            // synchronization block. Due to this, "listeners" should be 
            // modified only via add/remove methods and must never be updated
            // directly.
	    if (tempListeners.size() > 0) {
                PhaseEvent event = new PhaseEvent(context, phaseId, this);
                for (i = 0; i < tempListeners.size(); i++) {
                    PhaseListener listener = tempListeners.get(i);
                    if (phaseId.equals(listener.getPhaseId()) ||
                        PhaseId.ANY_PHASE.equals(listener.getPhaseId())) {
                        listener.beforePhase(event);
                    }
                    maxBefore = i;
                }
            }
	}
	catch (Exception e) {
	    if (logger.isLoggable(Level.WARNING)) {
                logger.warning("phase(" + phaseId.toString() + "," + context + 
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
            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, "executePhase(" + phaseId.toString() + "," 
                        + context + ") threw exception", e);
            }
            ex = e;
            exceptionThrown = true;
        } 
	finally {
            try {
                // Notify the "afterPhase" method of interested listeners
                // (descending)
                if (tempListeners.size() > 0) {
                    PhaseEvent event = new PhaseEvent(context, phaseId, this);
                    for (i = maxBefore; i >= 0; i--) {
                        PhaseListener listener = tempListeners.get(i);
                        if (phaseId.equals(listener.getPhaseId()) ||
                            PhaseId.ANY_PHASE.equals(listener.getPhaseId())) {
                            listener.afterPhase(event);
                        }
                    }
                }
            }
            catch (Throwable e) {
                if (logger.isLoggable(Level.WARNING)) {
                    logger.warning("phase(" + phaseId.toString() + "," + context + 
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
    
    /**
     * Populate the FacesCompositeELResolver stack registered with JSP 
     * if a request is being processed for the very first time. At the 
     * application initialiazation time, an empty CompositeELResolver is
     * registered with JSP because ELResolvers can be added until the first
     * request is serviced.
     */
    protected void populateFacesELResolverForJsp(FacesContext context) {
        
        Map<String,Object> applicationMap =  
            context.getExternalContext().getApplicationMap();
        String requestServiced = (String) 
            applicationMap.get(this.FIRST_REQUEST_SERVICED);
        if (requestServiced != null) {
            // first request has been serviced, so ELResolvers have
            // been populated already.
            return;
        }
        
        synchronized(applicationMap) { 
            requestServiced = (String) 
                applicationMap.get(this.FIRST_REQUEST_SERVICED);
            if (requestServiced == null) {
                // this needs to be set irrespective whether the FacesResolvers
                // are added to compositeELResolverForJsp or not.
                applicationMap.put(this.FIRST_REQUEST_SERVICED, "true");  
                
                ApplicationAssociate appAssociate =  
                ApplicationAssociate.getInstance(context.getExternalContext());
                CompositeELResolver compositeELResolverForJsp = 
                        appAssociate.getFacesELResolverForJsp();
                if (compositeELResolverForJsp == null) {
                    if (logger.isLoggable(Level.INFO)) {
                        logger.info("FacesELResolvers not registered with Jsp.");
                    }
                    return;
                }
                compositeELResolverForJsp.add(new ImplicitObjectELResolverForJsp());
                compositeELResolverForJsp.add(new ManagedBeanELResolver());
                compositeELResolverForJsp.add(new FacesResourceBundleELResolver());

                // add ELResolvers from faces-config.xml
                ArrayList elResolversFromFacesConfig = 
                        appAssociate.geELResolversFromFacesConfig();
                if (elResolversFromFacesConfig != null) {
                    Iterator it = elResolversFromFacesConfig.iterator();
                    while (it.hasNext()) {
                        compositeELResolverForJsp.add((ELResolver) it.next());
                    }
                }

                // register legacy VariableResolver if any.
                if (appAssociate.getLegacyVariableResolver() != null ) {
                    compositeELResolverForJsp.add(new VariableResolverChainWrapper(
                            appAssociate.getLegacyVariableResolver()));
                } else if (appAssociate.getLegacyVRChainHead() != null) {
                    compositeELResolverForJsp.add(new VariableResolverChainWrapper(
                            appAssociate.getLegacyVRChainHead()));   
                }

                // add legacy PropertyResolvers if any
                if (appAssociate.getLegacyPropertyResolver() != null ) {
                    compositeELResolverForJsp.add(new PropertyResolverChainWrapper(
                            appAssociate.getLegacyPropertyResolver()));
                } else if (appAssociate.getLegacyPRChainHead() != null) {
                    compositeELResolverForJsp.add(new PropertyResolverChainWrapper(
                            appAssociate.getLegacyPRChainHead()));   
                }

                // add ELResolvers added via Application.addELResolver()
                ArrayList elResolversFromApplication = 
                    appAssociate.getApplicationELResolvers();
                if (elResolversFromApplication != null) {
                    Iterator it = elResolversFromApplication.iterator();
                    while (it.hasNext()) {
                        compositeELResolverForJsp.add((ELResolver) it.next());
                    }
                }
                
            }
        }
    }

}
