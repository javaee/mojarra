/*
 * $Id: RestoreViewPhase.java,v 1.1 2003/08/22 16:49:28 eburns Exp $
 */

/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// RestoreViewPhase.java

package com.sun.faces.lifecycle;

import org.mozilla.util.Assert;

import javax.faces.FacesException;
import javax.faces.event.PhaseId;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.component.UICommand;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.Application;
import javax.faces.application.StateManager;
import javax.faces.event.ActionListener;
import java.util.Iterator;
import java.util.Map;
import java.util.Locale;
import com.sun.faces.RIConstants;
import com.sun.faces.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.IOException;

import com.sun.faces.util.DebugUtil;
import com.sun.faces.util.Util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**

 * <B>Lifetime And Scope</B> <P> Same lifetime and scope as
 * DefaultLifecycleImpl.
 *
 * @version $Id: RestoreViewPhase.java,v 1.1 2003/08/22 16:49:28 eburns Exp $
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

* POSTCONDITION: The facesContext has been initialized with a view. 

*/

public void execute(FacesContext facesContext) throws FacesException
{
    if (null == facesContext) {
	throw new FacesException(Util.getExceptionMessage(Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
    }

    // If an app had explicitely set the root component in the context, use that;
    //
    UIComponent view = facesContext.getViewRoot();
    Locale locale = null;
    if (view != null) {
        locale = facesContext.getExternalContext().getRequestLocale();
        facesContext.setLocale(locale);
        processView(facesContext);
	return;
    }

    // Otherwise, we will look to get the view from the page or session;
    // Create the requested component view    
    
    // look up saveStateInClient parameter to check whether to restore
    // state of view from client or server. Default is server.
    String saveState = facesContext.getExternalContext().
        getInitParameter(RIConstants.SAVESTATE_INITPARAM);
    if ( saveState != null ) {
        Assert.assert_it (saveState.equalsIgnoreCase("true") || 
            saveState.equalsIgnoreCase("false"));
    }     
    if (saveState == null || saveState.equalsIgnoreCase("false")) {
        restoreViewFromSession(facesContext);
    } else {
        restoreViewFromPage(facesContext);           
    }
}    
        
public void restoreViewFromPage(FacesContext facesContext) {
    UIViewRoot requestView = null;
    Locale locale = null;
    long beginTime = 0;

    if (log.isTraceEnabled()) {
        beginTime = System.currentTimeMillis();
    }
   
    // reconstitute view from page. 
    Map requestMap = facesContext.getExternalContext().getRequestParameterMap();
    String viewId = (String)requestMap.get("javax.servlet.include.path_info");
    if (viewId == null) {
        viewId = facesContext.getExternalContext().getRequestPathInfo();
    }
    
    if (viewId == null) {
        throw new FacesException(Util.getExceptionMessage(Util.NULL_REQUEST_VIEW_ERROR_MESSAGE_ID));
    }

    String viewRootString = (String)requestMap.get(RIConstants.FACES_VIEW);
    if ( viewRootString == null ) {
        requestView = facesContext.getViewRoot();
    } else {    
        byte[] bytes  = Base64.decode(viewRootString.getBytes());
        try {
            ObjectInputStream ois = new ObjectInputStream(
                    new ByteArrayInputStream(bytes));
            requestView = (UIViewRoot) ois.readObject();
            locale = (Locale) ois.readObject();
            ois.close();
            if (log.isDebugEnabled()) {
                DebugUtil.printTree(requestView, System.out);
            }
        } catch (java.io.OptionalDataException ode) {
            log.error(ode.getMessage(), ode);
        } catch (java.lang.ClassNotFoundException cnfe) {
            log.error(cnfe.getMessage(), cnfe);
        } catch (java.io.IOException iox) {
            log.error(iox.getMessage(), iox);
        }
    }
    facesContext.setViewRoot(requestView);
    if ( locale != null ) {
        facesContext.setLocale(locale);
    }
    processView(facesContext);
    // PENDING(visvan): If we wanted to track time, here is where we'd do it
    if (log.isTraceEnabled()) {
        long endTime = System.currentTimeMillis();
        log.trace("Time to reconstitute view " + (endTime-beginTime));
    }
}

protected void restoreViewFromSession(FacesContext facesContext) {
    UIViewRoot requestView = null;
    
    // PENDING(visvan) - will not deal with simultaneous requests
    // for the same session
    Map sessionMap = Util.getSessionMap(facesContext);

    // Reconstitute or create the request view
    Map requestMap = facesContext.getExternalContext().getRequestMap();
    String viewId = (String) 
               requestMap.get("javax.servlet.include.path_info");
    if (viewId == null) {
        viewId = facesContext.getExternalContext().getRequestPathInfo();
    }

    //PENDING (rogerk) throw exception
    if (viewId == null) {
        throw new FacesException(Util.getExceptionMessage(Util.NULL_REQUEST_VIEW_ERROR_MESSAGE_ID));
	// throw exception;
    }

    requestView = (UIViewRoot) sessionMap.get(RIConstants.FACES_VIEW);
    // If there is nothing in the session, 
    if (requestView == null) {
	// create the view from the pathInfo
        requestView = facesContext.getViewRoot();
    } 
    else {
	// There is something in the session.  Make sure its ViewId,
	// matches the viewId from the pathInfo.
        // PENDING (rlubke) CORRECT IMPLEMENTATION
	if ((null != viewId) && !viewId.equals(requestView.getViewId())) {
	    // If it doesn't match, use the pathInfo
        StateManager manager = Application.getCurrentInstance().getViewHandler().getStateManager();
        try {
            manager.getView(facesContext, viewId);
        } catch (IOException ioe) {
            // pending (rlubke) Localize
            throw new FacesException("Unable to restore view.", ioe);    
        }
	    requestView = facesContext.getViewRoot();
	}
	// If it does match, use the view from the Session
    }
	
    facesContext.setViewRoot(requestView);
    sessionMap.remove(RIConstants.FACES_VIEW);

    // Set up the request locale if needed
    Locale locale = (Locale)sessionMap.get(RIConstants.REQUEST_LOCALE);
    if (locale == null) {
        locale = facesContext.getExternalContext().getRequestLocale();
    }
    facesContext.setLocale(locale);
    sessionMap.remove(RIConstants.REQUEST_LOCALE);
    processView(facesContext);
}

protected void processView(FacesContext facesContext) {
    UIComponent root = facesContext.getViewRoot();
    // PENDING (rlubke) CORRECT IMPLEMENTATION
//    try {        
//        root.processRestoreState(facesContext, );
//    } catch (java.io.IOException iox) {
//        log.error(iox.getMessage(), iox);
//    }
    doPerComponentActions(facesContext, root);
}

/**
  * <p>Do any per-component actions necessary during reconstitute</p>
  */
protected void doPerComponentActions(FacesContext context, UIComponent uic) {
    Iterator kids = uic.getFacetsAndChildren();
    String componentRef = null;
    while (kids.hasNext()) {
        doPerComponentActions(context, (UIComponent) kids.next());
    }
    if (uic instanceof UICommand && null != actionListener) {
      
        // register actionlistener if it has not been registered already
        // PENDING (visvan) This could cause a problem because the components
        // are exposed to applications. Instead move this logic to the root
        // component.
        if ( (uic.getAttribute("com.sun.faces.ActionListener")) == null ) {
            ((UICommand)uic).addActionListener(actionListener);
            uic.setAttribute("com.sun.faces.ActionListener", Boolean.TRUE);
        }     
    }
    if (uic instanceof UIInput) {
	((UIInput)uic).setValid(true);
    }
    // if this component has a componentRef, make sure to populate the
    // ValueBinding for it.
    if (null != (componentRef = uic.getComponentRef())) {
	ValueBinding valueBinding = 
	    context.getApplication().getValueBinding(componentRef);
	valueBinding.setValue(context, uic);
    }
}

// The testcase for this class is TestReconstituteComponentViewPhase.java


} // end of class ReconstituteComponentViewPhase
