/* 
 * $Id: ViewHandlerImpl.java,v 1.3 2003/08/22 16:49:40 eburns Exp $ 
 */ 


/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


// ViewHandlerImpl.java 

package com.sun.faces.application; 

import com.sun.faces.util.Util; 

import java.io.IOException; 

import javax.faces.FacesException;
import javax.faces.component.UIViewRoot;
import javax.faces.application.ViewHandler;
import javax.faces.application.StateManager;
import javax.faces.context.FacesContext;  


/** 
 * <B>ViewHandlerImpl</B> is the default implementation class for ViewHandler. 
 * @version $Id: ViewHandlerImpl.java,v 1.3 2003/08/22 16:49:40 eburns Exp $ 
 * 
 * @see javax.faces.application.ViewHandler 
 * 
 */ 
public class ViewHandlerImpl implements ViewHandler { 

    public void renderView(FacesContext context) throws IOException, 
             FacesException { 

        if (context == null) { 
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
        } 

        // PENDING (rlubke): CORRECT IMPLEMENTATION
//        UIViewRoot root = context.getViewRoot(); 
//        
//        String requestURI = context.getViewRoot().getViewId();
//        context.getExternalContext().dispatchMessage(requestURI);
    }

    // PENDING (rlubke): PROVIDE IMPLEMENTATION
    public UIViewRoot restoreView(FacesContext context, String viewId) {
        return null;  //To change body of implemented methods use Options | File Templates.
    }

    // PENDING (rlubke): PROVIDE IMPLEMENTATION
    public StateManager getStateManager() {
        return null;  //To change body of implemented methods use Options | File Templates.
    }
} 
