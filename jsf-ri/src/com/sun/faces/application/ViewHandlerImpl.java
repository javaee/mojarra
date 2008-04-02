/* 
 * $Id: ViewHandlerImpl.java,v 1.10 2003/09/13 12:58:47 eburns Exp $ 
 */ 


/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


// ViewHandlerImpl.java 

package com.sun.faces.application; 

import com.sun.faces.util.Util;
import com.sun.faces.util.TreeStructure;
import com.sun.faces.RIConstants;

import java.io.IOException; 
import java.io.Reader;
import java.util.Map;
import java.util.HashMap;
import java.util.Locale;
import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.component.base.UIViewRootBase;
import javax.faces.component.UIViewRoot;
import javax.faces.component.UIComponent;
import javax.faces.application.ViewHandler;
import javax.faces.application.StateManager;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;  
import javax.faces.context.ResponseWriter;  
import javax.faces.application.StateManager.SerializedView;
import javax.faces.render.ResponseStateManager;

/** 
 * <B>ViewHandlerImpl</B> is the default implementation class for ViewHandler. 
 * @version $Id: ViewHandlerImpl.java,v 1.10 2003/09/13 12:58:47 eburns Exp $ 
 * 
 * @see javax.faces.application.ViewHandler 
 * 
 */ 
public class ViewHandlerImpl extends Object
        implements ViewHandler { 

    //
    // Relationship Instance Variables
    // 

    protected StateManager stateManagerImpl = null;

    public ViewHandlerImpl() {
	stateManagerImpl = new StateManagerImpl();
    }

    public void renderView(FacesContext context) throws IOException, 
             FacesException { 

        if (context == null) { 
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
        } 
        Application application = context.getApplication();
        if (application instanceof ApplicationImpl) {
            ((ApplicationImpl) application).responseRendered();
        }
        String requestURI = context.getViewRoot().getViewId();
        context.getExternalContext().dispatchMessage(requestURI);

    }

    public StateManager getStateManager() {
	return stateManagerImpl;
    }
    
    public UIViewRoot restoreView(FacesContext context, String viewId) {
        if (context == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
        }

        UIViewRoot viewRoot = null;
        try {
            viewRoot = getStateManager().getView(context, viewId);
        } catch (IOException ioe) {
            Object [] params = { ioe.getMessage() };
            throw new FacesException(Util.getExceptionMessage(
                    Util.SAVING_STATE_ERROR_MESSAGE_ID, params));
        }
        if ( viewRoot == null) {
            viewRoot = new UIViewRootBase();
            context.renderResponse();
        }
        context.setViewRoot(viewRoot);
        viewRoot.setViewId(viewId);
        return viewRoot;
    }

    public UIViewRoot createView(FacesContext context, String viewId) {
        if (context == null) {
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
        }

	UIViewRoot result = new UIViewRootBase();
	result.setViewId(viewId);
	// PENDING(): not sure if we should set the RenderKitId here.
	// The UIViewRootBase ctor sets the renderKitId to the default
	// one.
	return result;
    }

    public void writeState(FacesContext context) throws IOException {
        if (context == null) {
            throw new NullPointerException(Util.getExceptionMessage(
								    Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
        }
	if (getStateManager().isSavingStateInClient(context)) {
	    context.getResponseWriter().writeText(RIConstants.SAVESTATE_FIELD_MARKER,null);
	}
    }

} 
