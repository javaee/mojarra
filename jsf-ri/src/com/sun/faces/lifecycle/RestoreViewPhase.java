/*
 * $Id: RestoreViewPhase.java,v 1.43 2006/12/21 23:03:39 rlubke Exp $
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
import javax.faces.event.PhaseId;
import javax.faces.render.ResponseStateManager;
import javax.servlet.http.HttpServletRequest;

import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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
 * @version $Id: RestoreViewPhase.java,v 1.43 2006/12/21 23:03:39 rlubke Exp $
 */

public class RestoreViewPhase extends Phase {


    private static Logger logger = Util.getLogger(Util.FACES_LOGGER
                                                  + Util.LIFECYCLE_LOGGER);

    // ---------------------------------------------------------- Public Methods


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

        // If an app had explicitely set the tree in the context, use that;
        //
        UIViewRoot viewRoot = facesContext.getViewRoot();
        if (viewRoot != null) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Found a pre created view in FacesContext");
            }
            facesContext.getViewRoot().setLocale(
                 facesContext.getExternalContext().getRequestLocale());
            doPerComponentActions(facesContext, viewRoot);
            return;
        }

        // Reconstitute or create the request tree
        Map requestMap = facesContext.getExternalContext().getRequestMap();
        String viewId = (String)
              requestMap.get("javax.servlet.include.path_info");
        if (viewId == null) {
            viewId = facesContext.getExternalContext().getRequestPathInfo();
        }

        // It could be that this request was mapped using
        // a prefix mapping in which case there would be no
        // path_info.  Query the servlet path.
        if (viewId == null) {
            viewId = (String)
                  requestMap.get("javax.servlet.include.servlet_path");
        }

        if (viewId == null) {
            Object request = facesContext.getExternalContext().getRequest();
            if (request instanceof HttpServletRequest) {
                viewId = ((HttpServletRequest) request).getServletPath();
            }
        }

        if (viewId == null) {
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("viewId is null");
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
                    // NOTE: The ViewHandler or StateManager could
                    //  be registered using something other than
                    //  the faces-config.xml, if this is the case,
                    //  the assume that we're using a 1.2 implementation.
                    boolean viewHandlerIsOld;
                    boolean stateManagerIsOld;
                    Version toTest = tracker.
                          getVersionForTrackedClassName(viewHandler
                                .getClass().getName());
                    if (toTest != null) {
                        Version currentVersion = tracker.getCurrentVersion();


                        viewHandlerIsOld = (toTest.compareTo(currentVersion) < 0);
                        toTest = tracker.
                             getVersionForTrackedClassName(facesContext
                                  .getApplication().getStateManager()
                                  .getClass().getName());
                        stateManagerIsOld = (toTest.compareTo(currentVersion) < 0);
                    } else {
                        viewHandlerIsOld = false;
                        stateManagerIsOld = false;
                    }

                    if (viewHandlerIsOld || stateManagerIsOld) {
                        viewRoot = viewHandler.createView(facesContext, viewId);
                        if (null != viewRoot) {
                            facesContext.renderResponse();
                        }
                    }
                }

                if (null == viewRoot) {
                    Object[] params = {viewId};
                    throw new ViewExpiredException(MessageUtils.getExceptionMessageString(
                          MessageUtils.RESTORE_VIEW_ERROR_MESSAGE_ID, params),
                                                   viewId);
                }
            }

            doPerComponentActions(facesContext, viewRoot);           
            
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

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Exiting RestoreViewPhase");
        }

    }    


    public PhaseId getId() {

        return PhaseId.RESTORE_VIEW;

    }

    // ------------------------------------------------------- Protected Methods


    /**
     * <p>Do any per-component actions necessary during reconstitute</p>
     * @param context the <code>FacesContext</code> for the current request
     * @param uic the <code>UIComponent</code> to process the
     *  <code>binding</code> attribute
     */
    protected void doPerComponentActions(FacesContext context,
                                         UIComponent uic) {

        // if this component has a component value reference expression,
        // make sure to populate the ValueExpression for it.
        ValueExpression valueExpression;
        if (null != (valueExpression = uic.getValueExpression("binding"))) {
            valueExpression.setValue(context.getELContext(), uic);
        }

        for (Iterator<UIComponent> kids =  uic.getFacetsAndChildren();
             kids.hasNext(); ) {
            doPerComponentActions(context, kids.next());
        }

    }

    // --------------------------------------------------------- Private Methods


    /**
     * @param context the <code>FacesContext</code> for the current request
     * @return true if the request method is POST or PUT, or the method
     *         is GET but there are query parameters, or the request is not an
     *         instance of HttpServletRequest.
     */

    private boolean isPostback(FacesContext context) {

        // Get the renderKitId by calling viewHandler.calculateRenderKitId().
        String renderkitId =
              context.getApplication().getViewHandler().
                    calculateRenderKitId(context);
        ResponseStateManager rsm =
              RenderKitUtils.getResponseStateManager(context,
                                                     renderkitId);
        return rsm.isPostback(context);

    }

    // The testcase for this class is TestRestoreViewPhase.java

} // end of class RestoreViewPhase
