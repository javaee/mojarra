/*
 * $Id: RestoreViewPhase.java,v 1.28 2005/08/22 22:10:16 ofung Exp $
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

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.ViewExpiredException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.render.ResponseStateManager;
import javax.servlet.http.HttpServletRequest;

import com.sun.faces.util.Util;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * <B>Lifetime And Scope</B> <P> Same lifetime and scope as
 * DefaultLifecycleImpl.
 *
 * @version $Id: RestoreViewPhase.java,v 1.28 2005/08/22 22:10:16 ofung Exp $
 */

public class RestoreViewPhase extends Phase {

    //
    // Protected Constants
    //
    private static Logger logger = Util.getLogger(Util.FACES_LOGGER 
            + Util.LIFECYCLE_LOGGER);

    //
    // Class Variables
    //

    //
    // Instance Variables
    //    

    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Genericializers    
    //

    //
    // Class methods
    //

    //
    // General Methods
    //

    // 
    // Methods from Phase
    //


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
            throw new FacesException(Util.getExceptionMessageString(
                Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
        }
        
        Util.getViewHandler(facesContext).initView(facesContext);        

        // If an app had explicitely set the tree in the context, use that;
        //
        UIViewRoot viewRoot = facesContext.getViewRoot();
        Locale locale = null;
        if (viewRoot != null) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Found a pre created view in FacesContext");
            }
            locale = facesContext.getExternalContext().getRequestLocale();
            facesContext.getViewRoot().setLocale(locale);
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
            throw new FacesException(Util.getExceptionMessageString(
                Util.NULL_REQUEST_VIEW_ERROR_MESSAGE_ID));
        }

	if (isPostback(facesContext)) {
	    // try to restore the view
	    if (null == (viewRoot = (Util.getViewHandler(facesContext)).
			 restoreView(facesContext, viewId))) {
                Object[] params = {viewId};
		throw new ViewExpiredException(Util.getExceptionMessageString(
                    Util.RESTORE_VIEW_ERROR_MESSAGE_ID, params), viewId);
	    }
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("Postback: Restored view for " + viewId);
            }
	}
	else {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine("New request: creating a view for " + viewId);
            }
            // if that fails, create one
            viewRoot = (Util.getViewHandler(facesContext)).
                createView(facesContext, viewId);
            facesContext.renderResponse();
        } 
        assert (null != viewRoot);

        facesContext.setViewRoot(viewRoot);
        doPerComponentActions(facesContext, viewRoot);

        if (logger.isLoggable(Level.FINE)) {
            logger.fine("Exiting RestoreViewPhase");
        }
    }

    /**
     *
     * @return true if the request method is POST or PUT, or the method
     * is GET but there are query parameters, or the request is not an
     * instance of HttpServletRequest.
     */

    private boolean isPostback(FacesContext context) {
        // Get the renderKitId by calling viewHandler.calculateRenderKitId().
        String renderkitId = 
                context.getApplication().getViewHandler().
                calculateRenderKitId(context);
        ResponseStateManager rsm = Util.getResponseStateManager(context,
                renderkitId);
        return rsm.isPostback(context);
    }


    /**
     * <p>Do any per-component actions necessary during reconstitute</p>
     */
    protected void doPerComponentActions(FacesContext context, UIComponent uic) {
        // if this component has a component value reference expression,
        // make sure to populate the ValueExpression for it.
        ValueExpression valueExpression = null;
        if (null != (valueExpression = uic.getValueExpression("binding"))) {
            valueExpression.setValue(context.getELContext(), uic);
        }

        Iterator kids = uic.getFacetsAndChildren();
        while (kids.hasNext()) {
            doPerComponentActions(context, (UIComponent) kids.next());
        }
      
    }

    // The testcase for this class is TestRestoreViewPhase.java


} // end of class RestoreViewPhase
