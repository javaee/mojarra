/*
 * $Id: NewStateManager.java,v 1.1 2005/03/18 22:12:51 edburns Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */



package com.sun.faces.systest;

import javax.faces.application.StateManager;
import javax.faces.application.StateManagerWrapper;

import javax.faces.FactoryFinder;
import javax.faces.render.RenderKitFactory;
import javax.faces.render.RenderKit;
import javax.faces.render.ResponseStateManager;
import javax.faces.component.UIViewRoot;

import javax.faces.context.FacesContext;

import java.io.IOException;

public class NewStateManager extends StateManagerWrapper {

    private StateManager oldStateManager = null;

    public NewStateManager(StateManager oldStateManager) {
	this.oldStateManager = oldStateManager;
    }

    public StateManager getWrapped() {
	return oldStateManager;
    }

    /**
     * <p>Just save the view in the session.</p>
     */

    public Object saveView(FacesContext context) {    
	String id = null;
	
	synchronized (this) {
	    id = createUniqueRequestId();
	}
	context.getExternalContext().getSessionMap().put(id, 
							 context.getViewRoot());
	return id;
    }

    /**
     * <p>Override superclass processing and call the new version of
     * <code>writeState()</code> that takes <code>Object</code>.</p>
     */
	
    public void writeState(FacesContext context, Object state) throws IOException {
	getResponseStateManager(context).writeState(context, state);
    }


    public UIViewRoot restoreView(FacesContext context, String viewId,
                                  String renderKitId) {
	Object [] stateArray = (Object [])
	    getResponseStateManager(context).getState(context, viewId);	
	String id = (String) stateArray[0];

	UIViewRoot result = (UIViewRoot)
	    context.getExternalContext().getSessionMap().get(id);

	return result;
    }

    private ResponseStateManager getResponseStateManager(FacesContext context){
        RenderKitFactory renderKitFactory = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = renderKitFactory.getRenderKit(context, 
							    RenderKitFactory.HTML_BASIC_RENDER_KIT);
	ResponseStateManager responseStateManager = 
	    renderKit.getResponseStateManager();
	
	return responseStateManager;
    }

    char requestIdSerial = 0;

    private String createUniqueRequestId() {
	if (requestIdSerial++ == Character.MAX_VALUE) {
	    requestIdSerial = 0;
	}
	return UIViewRoot.UNIQUE_ID_PREFIX + ((int) requestIdSerial);
    }


}
