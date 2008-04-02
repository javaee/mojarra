/* 
 * $Id: ViewHandlerImpl.java,v 1.10 2003/03/21 23:22:46 rkitain Exp $ 
 */ 


/*
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


// ViewHandlerImpl.java 

package com.sun.faces.lifecycle; 

import com.sun.faces.util.Util;

import org.mozilla.util.Assert; 
import org.mozilla.util.ParameterCheck; 

import java.io.IOException; 
import javax.servlet.ServletException;

import javax.faces.FacesException;
import javax.faces.context.FacesContext; 
import javax.faces.lifecycle.ViewHandler; 
import javax.faces.tree.Tree; 

import javax.servlet.ServletRequest; 
import javax.servlet.ServletResponse; 
import javax.servlet.RequestDispatcher; 

/** 
 * <B>ViewHandlerImpl</B> is the default implementation class for ViewHandler. 
 * @version $Id: ViewHandlerImpl.java,v 1.10 2003/03/21 23:22:46 rkitain Exp $ 
 * 
 * @see javax.faces.lifecycle.ViewHandler 
 * 
 */ 
public class ViewHandlerImpl implements ViewHandler { 

    public void renderView(FacesContext context) throws IOException, 
             FacesException { 

        if (context == null) { 
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
        } 

        Tree tree = context.getTree(); 
        
        String requestURI = context.getTree().getTreeId();
        context.getExternalContext().dispatchMessage(requestURI);
    } 
} 
