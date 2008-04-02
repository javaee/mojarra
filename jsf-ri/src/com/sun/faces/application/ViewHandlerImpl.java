/* 
 * $Id: ViewHandlerImpl.java,v 1.4 2003/08/23 00:39:04 jvisvanathan Exp $ 
 */ 


/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


// ViewHandlerImpl.java 

package com.sun.faces.application; 

import com.sun.faces.util.Util;
import com.sun.faces.RIConstants;

import java.io.IOException; 
import java.io.Reader;
import java.util.Map;
import java.util.Locale;

import javax.faces.FacesException;
import javax.faces.component.base.UIViewRootBase;
import javax.faces.component.UIViewRoot;
import javax.faces.application.ViewHandler;
import javax.faces.application.StateManager;
import javax.faces.context.FacesContext;  
import javax.faces.application.StateManager.SerializedView;


/** 
 * <B>ViewHandlerImpl</B> is the default implementation class for ViewHandler. 
 * @version $Id: ViewHandlerImpl.java,v 1.4 2003/08/23 00:39:04 jvisvanathan Exp $ 
 * 
 * @see javax.faces.application.ViewHandler 
 * 
 */ 
public class ViewHandlerImpl extends StateManager 
        implements ViewHandler { 

    public void renderView(FacesContext context) throws IOException, 
             FacesException { 

        if (context == null) { 
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
        } 
        String requestURI = context.getViewRoot().getViewId();
        context.getExternalContext().dispatchMessage(requestURI);

    }

    public StateManager getStateManager() {
	return this;
    }
    
    public UIViewRoot restoreView(FacesContext context, String viewId) {
        UIViewRoot viewRoot = null;
        try {
            viewRoot = getView(context, viewId);
        } catch (IOException ioe) {
            Object [] params = { ioe.getMessage() };
            throw new FacesException(Util.getExceptionMessage(
                    Util.SAVING_STATE_ERROR_MESSAGE_ID, params));
        }
        if ( viewRoot == null) {
            // PENDING (visvan) restoreView javadocs should be updated ???
            viewRoot = new UIViewRootBase();
            context.renderResponse();
        }
        context.setViewRoot(viewRoot);
        viewRoot.setViewId(viewId);
        return viewRoot;
    }
    
    protected Object getComponentStateToSave(FacesContext context){
        Object state = null;
        UIViewRoot viewRoot =  context.getViewRoot();
        // PENDING (visvan) Just to get the save state system working,the first
        // just saves the root in session, instead of saving state and structure
        // separately.
        boolean isSaveStateInPage = Util.isSaveStateInPage(context);
        if (isSaveStateInPage) {
            try {
                state = viewRoot.processGetState(context);
            } catch (IOException ioe) {
                Object [] params = { ioe.getMessage() };
                throw new FacesException(Util.getExceptionMessage(
                        Util.SAVING_STATE_ERROR_MESSAGE_ID, params));
            }
        }
        return state;
    }
    
    
    protected Object getTreeStructureToSave(FacesContext context) {
        // PENDING (visvan) implement in the next phase
        // of state saving. Make sure to honor the transient property while
        // saving the structure.
        return null;
    }
    
    public UIViewRoot getView(FacesContext context, String viewId) throws 
            IOException {
        UIViewRoot viewRoot = null;
        boolean isSaveStateInPage = Util.isSaveStateInPage(context);
        if (isSaveStateInPage) {
            viewRoot = restoreTreeStructure(context, viewId);
            if (viewRoot != null) {
                restoreComponentState(context, viewRoot);
            }
        } else {
            // restore tree from session.
            Map sessionMap = Util.getSessionMap(context);
            // PENDING (visvan) next phase of state saving will restore the
            // serialized state instead of the root which would then be 
            // further processed to get the tree structure and treeState.
            // similar to saveStateInPage ???
            viewRoot = (UIViewRoot) sessionMap.get(viewId);
            if (viewRoot != null) {
                // restore locale
                // PENDING (visvan) do we need to store locale based on viewId ?
                // just like the tree ?
                Locale locale = (Locale)
                        sessionMap.get(RIConstants.REQUEST_LOCALE);
                if (locale != null) {
                    context.setLocale(locale);
                }
            }
        }
        return viewRoot;
    }

    protected void restoreComponentState(FacesContext context, 
            UIViewRoot root) throws IOException {
        // PENDING (visvan) implement saveStateInPage in the next phase
        // of state saving.
    }
   
    protected UIViewRoot restoreTreeStructure(FacesContext context, 
             String viewId) {
        // PENDING (visvan) implement saveStateInPage in the next phase
        // of state saving.
        return null;
    }
    
    public void saveView(FacesContext context, Reader content, 
            SerializedView state) {
        // if the save state in session option is selected, no manipulation 
        // is necessary.
        boolean isSaveStateInPage = Util.isSaveStateInPage(context);
        if (!isSaveStateInPage) {
            Map sessionMap = Util.getSessionMap(context);
            // does the locale need to be persisted based on tree Id ?
            sessionMap.put(RIConstants.REQUEST_LOCALE, context.getLocale());
            // PENDING (visvan) next phase of state saving will save the
            // serialized state instead of the root.
            sessionMap.put(context.getViewRoot().getViewId(), 
                    context.getViewRoot()); 
        } else {
            // PENDING (visvan) implement saveStateInPage in the next phase
            // of state saving.
           /* Util.getResponseStateManager.writeState(content, 
                context.getResponseWriter(),state.getStructure(), state.getState());
            */
        }
    }
    
    public void writeStateMarker(FacesContext context) throws IOException {
        Util.getResponseStateManager(context).writeStateMarker(context);
    }
    
} 
