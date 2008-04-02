/* 
 * $Id: ViewHandlerImpl.java,v 1.8 2003/01/22 05:38:12 rkitain Exp $ 
 */ 


/* 
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved. 
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */ 


// ViewHandlerImpl.java 

package com.sun.faces.lifecycle; 

import com.sun.faces.util.Util;

import org.mozilla.util.Assert; 
import org.mozilla.util.ParameterCheck; 

import java.io.IOException; 
import javax.servlet.ServletException;

import javax.faces.context.FacesContext; 
import javax.faces.lifecycle.ViewHandler; 
import javax.faces.tree.Tree; 

import javax.servlet.http.HttpServletRequest; 
import javax.servlet.ServletException; 
import javax.servlet.RequestDispatcher; 

/** 
 * <B>ViewHandlerImpl</B> is the default implementation class for ViewHandler. 
 * @version $Id: ViewHandlerImpl.java,v 1.8 2003/01/22 05:38:12 rkitain Exp $ 
 * 
 * @see javax.faces.lifecycle.ViewHandler 
 * 
 */ 
public class ViewHandlerImpl implements ViewHandler { 

    public void renderView(FacesContext context) throws IOException, 
             ServletException { 

        if (context == null) { 
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
        } 

        HttpServletRequest request = (HttpServletRequest) 
            context.getServletRequest(); 
        RequestDispatcher requestDispatcher = null; 
        Tree tree = context.getTree(); 
        
        String requestURI = context.getTree().getTreeId();
        requestDispatcher = request.getRequestDispatcher(requestURI); 
        requestDispatcher.forward(request, context.getServletResponse()); 
    } 
} 
