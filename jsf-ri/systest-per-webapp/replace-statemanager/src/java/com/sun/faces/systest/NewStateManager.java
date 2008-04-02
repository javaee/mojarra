/*
 * $Id: NewStateManager.java,v 1.5 2006/03/29 23:04:33 rlubke Exp $
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
        return oldStateManager.saveView(context);
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
	
        return oldStateManager.restoreView(context, viewId, renderKitId);
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
