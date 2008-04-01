/* 
 * $Id: ViewHandlerImpl.java,v 1.5 2002/07/23 05:17:01 eburns Exp $ 
 */ 


/* 
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved. 
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */ 


// ViewHandlerImpl.java 

package com.sun.faces.lifecycle; 

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
 * @version $Id: ViewHandlerImpl.java,v 1.5 2002/07/23 05:17:01 eburns Exp $ 
 * 
 * @see javax.faces.lifecycle.ViewHandler 
 * 
 */ 
public class ViewHandlerImpl implements ViewHandler { 

    public void renderView(FacesContext context) throws IOException, 
             ServletException { 
        if (context == null) { 
            throw new NullPointerException("Null FacesContext"); 
        } 
        HttpServletRequest request = (HttpServletRequest) 
            context.getServletRequest(); 
        RequestDispatcher requestDispatcher = null; 
        Tree tree = context.getResponseTree(); 
        
        try { 
            String requestURI = context.getResponseTree().getTreeId();
            requestDispatcher = request.getRequestDispatcher(requestURI); 
            requestDispatcher.forward(request, context.getServletResponse()); 
        } catch (IOException ioe) {
            // e.printStackTrace();
            throw new IOException(ioe.getMessage()); 
	} catch (ServletException se) {
            throw new ServletException(se.getMessage()); 
	}
    } 
} 
