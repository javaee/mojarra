/*
 * $Id: RestoreViewPhase.java,v 1.13 2004/02/04 23:41:38 ofung Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// RestoreViewPhase.java

package com.sun.faces.lifecycle;

import com.sun.faces.util.Util;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.sun.faces.util.Util;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.ActionListener;
import javax.faces.event.PhaseId;
import javax.servlet.http.HttpServletRequest;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;



/**

 * <B>Lifetime And Scope</B> <P> Same lifetime and scope as
 * DefaultLifecycleImpl.
 *
 * @version $Id: RestoreViewPhase.java,v 1.13 2004/02/04 23:41:38 ofung Exp $
 * 
 */

public class RestoreViewPhase extends Phase {
    //
    // Protected Constants
    //
    // Log instance for this class
    protected static Log log = LogFactory.getLog(RestoreViewPhase.class);

    //
    // Class Variables
    //

    //
    // Instance Variables
    //

    private ActionListener actionListener = null;

    // Attribute Instance Variables

    // Relationship Instance Variables

    //
    // Constructors and Genericializers    
    //

    public RestoreViewPhase() {

        ApplicationFactory aFactory = (ApplicationFactory)
            FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
        if (aFactory != null) {
            actionListener = aFactory.getApplication().getActionListener();
        }
    }

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

    * POSTCONDITION: The facesContext has been initialized with a tree. 

    */

    public void execute(FacesContext facesContext) throws FacesException
    {
        if (log.isDebugEnabled()) {
            log.debug("Entering RestoreViewPhase");
        }
        if (null == facesContext) {
            throw new FacesException(Util.getExceptionMessage(
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
            throw new FacesException(Util.getExceptionMessage(
                    Util.NULL_REQUEST_VIEW_ERROR_MESSAGE_ID));
        }
        
	
	// try to restore the view
	if (null == (viewRoot = (Util.getViewHandler(facesContext)).
		     restoreView(facesContext, viewId))) {
             
            if (log.isDebugEnabled()) {
                log.debug("New request: creating a view for " + viewId);
            }
	    // if that fails, create one
	    viewRoot = (Util.getViewHandler(facesContext)).
                createView(facesContext, viewId);
            facesContext.renderResponse();
	} else {
            if (log.isDebugEnabled()) {
                 log.debug("Postback: Restored view for " + viewId);
            }
        }
	Util.doAssert(null != viewRoot);

        facesContext.setViewRoot(viewRoot);
        doPerComponentActions(facesContext, viewRoot);
        
        if (log.isDebugEnabled()) {
           log.debug("Exiting RestoreViewPhase");
        }
    }    

    /**
      * <p>Do any per-component actions necessary during reconstitute</p>
      */
    protected void doPerComponentActions(FacesContext context, UIComponent uic) {
        Iterator kids = uic.getFacetsAndChildren();
        while (kids.hasNext()) {
            doPerComponentActions(context, (UIComponent) kids.next());
        }
      
        // if this component has a component value reference expression,
        // make sure to populate the ValueBinding for it.
	ValueBinding valueBinding = null;
	if (null != (valueBinding = uic.getValueBinding("binding"))) {
	    valueBinding.setValue(context, uic);
        }
    }

    // The testcase for this class is TestRestoreViewPhase.java


} // end of class RestoreViewPhase
