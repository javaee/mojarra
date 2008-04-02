/* 
 * $Id: ViewHandlerImpl.java,v 1.7 2003/08/28 15:52:26 rlubke Exp $ 
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
import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.component.base.UIViewRootBase;
import javax.faces.component.UIViewRoot;
import javax.faces.component.UIComponent;
import javax.faces.application.ViewHandler;
import javax.faces.application.StateManager;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;  
import javax.faces.application.StateManager.SerializedView;


/** 
 * <B>ViewHandlerImpl</B> is the default implementation class for ViewHandler. 
 * @version $Id: ViewHandlerImpl.java,v 1.7 2003/08/28 15:52:26 rlubke Exp $ 
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
        Application application = context.getApplication();
        if (application instanceof ApplicationImpl) {
            ((ApplicationImpl) application).responseRendered();
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
            viewRoot = new UIViewRootBase();
            context.renderResponse();
        }
        context.setViewRoot(viewRoot);
        viewRoot.setViewId(viewId);
        return viewRoot;
    }

    public UIViewRoot createView(FacesContext context, String viewId) {
	UIViewRoot result = new UIViewRootBase();
	result.setViewId(viewId);
	// PENDING(): not sure if we should set the RenderKitId here.
	// The UIViewRootBase ctor sets the renderKitId to the default
	// one.
	return result;
    }
    
    public SerializedView getSerializedView(FacesContext context) {
	SerializedView result = null;
        boolean isSaveStateInPage = Util.isSaveStateInPage(context);
        if (!isSaveStateInPage) {
            UIViewRoot viewRoot = context.getViewRoot();
            if (viewRoot.isTransient()) {
                return result;
            }
            // honor the transient property and remove children from the tree
            // that are marked transient.
            removeTransientChildrenAndFacets((UIComponent)viewRoot);   
           
            Map sessionMap = Util.getSessionMap(context);
            String localeKey = RIConstants.REQUEST_LOCALE + "." + 
                    context.getViewRoot().getViewId();
            sessionMap.put(localeKey, context.getLocale());
            sessionMap.put(viewRoot.getViewId(), viewRoot); 
        } else {
	    result = new SerializedView(getTreeStructureToSave(context),
				    getComponentStateToSave(context));
	}
        return result;
    }
    
    public void removeTransientChildrenAndFacets(UIComponent component) {
        UIComponent kid = null;
        // deal with children that are marked transient.
        Iterator kids = component.getChildren().iterator();
        while (kids.hasNext()) {
            kid = (UIComponent) kids.next();
            if (kid.isTransient()) {
                kids.remove();
            } else {
                removeTransientChildrenAndFacets(kid);
            }
        }
        // deal with facets that are marked transient.
        kids = component.getFacets().values().iterator();
        while (kids.hasNext()) {
            kid = (UIComponent) kids.next();
            if (kid.isTransient()) {
                kids.remove();
            } else {
                removeTransientChildrenAndFacets(kid);
            }
            
        }
    }
  
    protected Object getComponentStateToSave(FacesContext context){
        Object state = null;
        UIViewRoot viewRoot =  context.getViewRoot();
        try {
            state = viewRoot.processGetState(context);
        } catch (IOException ioe) {
            Object [] params = { ioe.getMessage() };
            throw new FacesException(Util.getExceptionMessage(
                    Util.SAVING_STATE_ERROR_MESSAGE_ID, params));
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
            viewRoot = (UIViewRoot) sessionMap.get(viewId);
            if (viewRoot != null) {
                // restore locale
                String localeKey = RIConstants.REQUEST_LOCALE + "." + viewId;
                Locale locale = (Locale) sessionMap.get(localeKey);
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
        /* Util.getResponseStateManager.writeState(content, 
                context.getResponseWriter(),state.getStructure(), state.getState());
        */
    }
    
    public void writeStateMarker(FacesContext context) throws IOException {
        Util.getResponseStateManager(context).writeStateMarker(context);
    }
    
} 
