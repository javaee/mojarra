/* 
 * $Id: ViewHandlerImpl.java,v 1.2 2003/08/21 14:16:27 rlubke Exp $ 
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
import javax.faces.component.UIPage;
import javax.faces.application.ViewHandler;
import javax.faces.application.StateManager;
import javax.faces.context.FacesContext;  


/** 
 * <B>ViewHandlerImpl</B> is the default implementation class for ViewHandler. 
 * @version $Id: ViewHandlerImpl.java,v 1.2 2003/08/21 14:16:27 rlubke Exp $ 
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
//        Tree tree = context.getTree(); 
//        
//        String requestURI = context.getTree().getTreeId();
//        context.getExternalContext().dispatchMessage(requestURI);
    }

    // PENDING (rlubke): PROVIDE IMPLEMENTATION
    public UIPage restoreView(FacesContext context, String treeId) {
        return null;  //To change body of implemented methods use Options | File Templates.
    }

    // PENDING (rlubke): PROVIDE IMPLEMENTATION
    public StateManager getStateManager() {
        return null;  //To change body of implemented methods use Options | File Templates.
    }
} 
