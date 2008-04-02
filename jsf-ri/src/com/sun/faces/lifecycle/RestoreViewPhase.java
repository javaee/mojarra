/*
 * $Id: RestoreViewPhase.java,v 1.23 2005/05/16 20:16:22 rlubke Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// RestoreViewPhase.java

package com.sun.faces.lifecycle;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.render.ResponseStateManager;
import javax.servlet.http.HttpServletRequest;

import com.sun.faces.util.Util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * <B>Lifetime And Scope</B> <P> Same lifetime and scope as
 * DefaultLifecycleImpl.
 *
 * @version $Id: RestoreViewPhase.java,v 1.23 2005/05/16 20:16:22 rlubke Exp $
 */

public class RestoreViewPhase extends Phase {

    //
    // Protected Constants
    //
    // Log instance for this class
    private static final Log log = LogFactory.getLog(RestoreViewPhase.class);

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
        if (log.isDebugEnabled()) {
            log.debug("Entering RestoreViewPhase");
        }
        if (null == facesContext) {
            throw new FacesException(Util.getExceptionMessageString(
                Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
        }

        // If an app had explicitely set the tree in the context, use that;
        //
        UIViewRoot viewRoot = facesContext.getViewRoot();
        Locale locale = null;
        if (viewRoot != null) {
            if (log.isDebugEnabled()) {
                log.debug("Found a pre created view in FacesContext");
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
            if (log.isDebugEnabled()) {
                log.debug("viewId is null");
            }
            throw new FacesException(Util.getExceptionMessageString(
                Util.NULL_REQUEST_VIEW_ERROR_MESSAGE_ID));
        }

	if (isPostback(facesContext)) {
	    // try to restore the view
	    if (null == (viewRoot = (Util.getViewHandler(facesContext)).
			 restoreView(facesContext, viewId))) {
		throw new FacesException(Util.getExceptionMessageString(
                Util.NULL_REQUEST_VIEW_ERROR_MESSAGE_ID));
	    }
            if (log.isDebugEnabled()) {
                log.debug("Postback: Restored view for " + viewId);
            }
	}
	else {
            if (log.isDebugEnabled()) {
                log.debug("New request: creating a view for " + viewId);
            }
            // if that fails, create one
            viewRoot = (Util.getViewHandler(facesContext)).
                createView(facesContext, viewId);
            facesContext.renderResponse();
        } 
        assert (null != viewRoot);

        facesContext.setViewRoot(viewRoot);
        doPerComponentActions(facesContext, viewRoot);

        if (log.isDebugEnabled()) {
            log.debug("Exiting RestoreViewPhase");
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
