/*
 * $Id: RestoreViewPhase.java,v 1.36 2006/07/31 23:05:03 rlubke Exp $
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

// RestoreViewPhase.java

package com.sun.faces.lifecycle;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.ViewExpiredException;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.faces.event.PhaseId;
import javax.faces.render.ResponseStateManager;
import javax.servlet.http.HttpServletRequest;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.config.JSFVersionTracker;
import com.sun.faces.config.JSFVersionTracker.Version;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.Util;

/**
 * <B>Lifetime And Scope</B> <P> Same lifetime and scope as
 * DefaultLifecycleImpl.
 *
 * @version $Id: RestoreViewPhase.java,v 1.36 2006/07/31 23:05:03 rlubke Exp $
 */

public class RestoreViewPhase extends Phase {
    
    private static Logger logger = Util.getLogger(Util.FACES_LOGGER 
            + Util.LIFECYCLE_LOGGER);
    
    /**
     * <p>Store the value of <code>DEFAULT_SUFFIX_PARAM_NAME</code>
     * or, if that isn't defined, the value of <code>DEFAULT_SUFFIX</code>
     */
    private String contextDefaultSuffix;
   

    public PhaseId getId() {
        return PhaseId.RESTORE_VIEW;
    }


    /**
     * PRECONDITION: the necessary factories have been installed in the
     * ServletContext attr set. <P>
     * <p/>
     * POSTCONDITION: The facesContext has been initialized with a tree.
     */

    public void execute(FacesContext facesContext) throws FacesException {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Entering RestoreViewPhase");
        }
        if (null == facesContext) {
            throw new FacesException(MessageUtils.getExceptionMessageString(
                MessageUtils.NULL_CONTEXT_ERROR_MESSAGE_ID));
        }
        
        Util.getViewHandler(facesContext).initView(facesContext);        

        // If an app had explicitely set the tree in the context, use that;
        //
        UIViewRoot viewRoot = facesContext.getViewRoot();       
        if (viewRoot != null) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Found a pre created view in FacesContext");
            }
            Locale locale = facesContext.getExternalContext().getRequestLocale();
            facesContext.getViewRoot().setLocale(locale);
            doPerComponentActions(facesContext, viewRoot);
            return;
        }

        // Reconstitute or create the request tree              
        String viewId = getViewId(facesContext);

        if (viewId == null) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("requestPath is null");
            }
            throw new FacesException(MessageUtils.getExceptionMessageString(
                MessageUtils.NULL_REQUEST_VIEW_ERROR_MESSAGE_ID));
        }                

        if (isPostback(facesContext)) {
            // try to restore the view
            ViewHandler viewHandler = Util.getViewHandler(facesContext);
            if (null == (viewRoot =
                  viewHandler.restoreView(facesContext, viewId))) {
                JSFVersionTracker tracker =
                      ApplicationAssociate
                            .getInstance(facesContext.getExternalContext())
                            .getJSFVersionTracker();

                // The tracker will be null if the user turned off the 
                // version tracking feature.  
                if (null != tracker) {
                    // Get the versions of the current ViewHandler and
                    // StateManager.  If they are older than the current
                    // version of the implementation, fall back to the
                    // JSF 1.1 behavior.
                    Version toTest = tracker.
                          getVersionForTrackedClassName(viewHandler
                                .getClass().getName());
                    Version currentVersion = tracker.getCurrentVersion();
                    boolean viewHandlerIsOld;
                    boolean stateManagerIsOld;

                    viewHandlerIsOld = (toTest.compareTo(currentVersion) < 0);
                    toTest = tracker.
                          getVersionForTrackedClassName(facesContext
                                .getApplication().getStateManager()
                                .getClass().getName());
                    stateManagerIsOld = (toTest.compareTo(currentVersion) < 0);

                    if (viewHandlerIsOld || stateManagerIsOld) {
                        viewRoot = viewHandler.createView(facesContext,
                                                          viewId);
                        if (null != viewRoot) {
                            facesContext.renderResponse();
                        }
                    }
                }

                if (null == viewRoot) {                    
                    throw new ViewExpiredException(MessageUtils.getExceptionMessageString(
                          MessageUtils.RESTORE_VIEW_ERROR_MESSAGE_ID, viewId),
                                                   viewId);
                }
            }
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Postback: Restored view for " + viewId);
            }
        } else {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("New request: creating a view for " + viewId);
            }
            // if that fails, create one
            viewRoot = (Util.getViewHandler(facesContext)).
                  createView(facesContext, viewId);
            facesContext.renderResponse();
        }
        assert(null != viewRoot);

        facesContext.setViewRoot(viewRoot);
        doPerComponentActions(facesContext, viewRoot);

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Exiting RestoreViewPhase");
        }
    }

    /**
     * @param context the <code>FacesContext</code> for the current request
     * @return true if the request method is POST or PUT, or the method
     * is GET but there are query parameters, or the request is not an
     * instance of HttpServletRequest.
     */

    private boolean isPostback(FacesContext context) {
        // Get the renderKitId by calling viewHandler.calculateRenderKitId().
        String renderkitId = 
                context.getApplication().getViewHandler().
                calculateRenderKitId(context);
        ResponseStateManager rsm = RenderKitUtils.getResponseStateManager(context,
                renderkitId);
        return rsm.isPostback(context);
    }


    /**
     * <p>Do any per-component actions necessary during reconstitute</p>
     * @param context the <code>FacesContext</code> for the current request
     * @param uic top level <code>UIComponent</code>
     */
    protected void doPerComponentActions(FacesContext context, UIComponent uic) {
        // if this component has a component value reference expression,
        // make sure to populate the ValueExpression for it.
        ValueExpression valueExpression;
        if (null != (valueExpression = uic.getValueExpression("binding"))) {
            valueExpression.setValue(context.getELContext(), uic);
        }

        Iterator<UIComponent> kids = uic.getFacetsAndChildren();
        while (kids.hasNext()) {
            doPerComponentActions(context, kids.next());
        }
      
    }
    
    /**
     * <p>Adjust the viewID per the requirements of Section 2.2.1.</p>
     *
     * @param context current {@link FacesContext}
     * @param viewId  incoming view ID
     * @return the view ID with an altered suffix mapping (if necessary)
     */
    private String convertViewId(FacesContext context, String viewId) {

        if (viewId == null) {
            return null;
        }
                
        initDefaultSuffix(context);
               
        String mapping = Util.getFacesMapping(context);
        
        // if the FacesServlet is mapped to /* throw an 
        // Exception in order to prevent an endless 
        // RequestDispatcher loop
        if ("/*".equals(mapping)) {
            throw new FacesException(MessageUtils.getExceptionMessageString(
                  MessageUtils.FACES_SERVLET_MAPPING_INCORRECT_ID));
        }
        
        boolean isPrefixMapped = Util.isPrefixMapped(mapping);
        ExternalContext extContext = context.getExternalContext();
        
         // maping could be null if a non-faces request triggered
        // this response.
        if (extContext.getRequestPathInfo() == null && mapping != null &&
            Util.isPrefixMapped(mapping)) {
            // this was probably an initial request
            // send them off to the root of the web application
            try {
                context.responseComplete();
                if (logger.isLoggable(Level.FINE)) {
                    logger.log(Level.FINE, "Response Complete for" + viewId);
                }
                extContext.redirect(extContext.getRequestContextPath());
            } catch (IOException ioe) {
                throw new FacesException(ioe);
            }
        }
        
        String convertedViewId = null;
        if (mapping != null && !isPrefixMapped) {
            // if the viewId doesn't already use the above suffix,
            // replace or append.
            if (!viewId.endsWith(contextDefaultSuffix)) {
                StringBuffer buffer = new StringBuffer(viewId);
                int extIdx = viewId.lastIndexOf('.');
                if (extIdx != -1) {
                    buffer.replace(extIdx, viewId.length(),
                                   contextDefaultSuffix);
                } else {
                    // no extension in the provided viewId, append the suffix
                    buffer.append(contextDefaultSuffix);
                }
                convertedViewId = buffer.toString();
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("viewId after appending the context suffix " +
                                convertedViewId);
                }

            }
        } else {
            // if using relative URLs and prefix mapping,
            // there is a chance of having multiple instances
            // of the mapping in the URI.  This attempts to 
            // normalize the URI to prevent a request dispatcher
            // loop
            convertedViewId = viewId;            
            int length = mapping != null ? mapping.length() : 0;
            while (convertedViewId.startsWith(mapping)) {
                convertedViewId = convertedViewId.substring(length);                
            }          
        }
                      
        return convertedViewId;
    }

    /**
     * <p>Initialize the default extension suffix that will be used
     * in view ID adjustments.</p>
     * @param context the <code>FacesContext</code> for the current request
     */
    private void initDefaultSuffix(FacesContext context) {
        if (contextDefaultSuffix == null) {
            synchronized (this) {
                if (contextDefaultSuffix == null) {
                    contextDefaultSuffix =
                          context.getExternalContext().
                                getInitParameter(ViewHandler.DEFAULT_SUFFIX_PARAM_NAME);
                    if (contextDefaultSuffix == null) {
                        contextDefaultSuffix = ViewHandler.DEFAULT_SUFFIX;
                    }
                    if (logger.isLoggable(Level.FINE)) {
                        logger.fine("contextDefaultSuffix "
                                    + contextDefaultSuffix);
                    }
                }
            }
        }
    }


    /**
     * <p>Obtain the View ID for this request per section 2.2.1.</p>
     * @param context the <code>FacesContext</code> for the current request
     * @return the view ID for this request
     */
    private String getViewId(FacesContext context) {
        // Reconstitute or create the request tree
        Map requestMap = context.getExternalContext().getRequestMap();
        String requestPath = (String)
            requestMap.get("javax.servlet.include.path_info");
        if (requestPath == null) {
            requestPath = context.getExternalContext().getRequestPathInfo();
        }
        
        // It could be that this request was mapped using
        // a prefix mapping in which case there would be no
        // path_info.  Query the servlet path.
        if (requestPath == null) {
            requestPath = (String)
                requestMap.get("javax.servlet.include.servlet_path");
        }

        if (requestPath == null) {
            Object request = context.getExternalContext().getRequest();
            if (request instanceof HttpServletRequest) {
                requestPath = ((HttpServletRequest) request).getServletPath();
            }
        }
        
        return convertViewId(context, requestPath);
    }
    

} // end of class RestoreViewPhase
