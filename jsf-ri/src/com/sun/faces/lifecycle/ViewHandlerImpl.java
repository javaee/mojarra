/* 
 * $Id: ViewHandlerImpl.java,v 1.1 2002/06/25 21:08:39 eburns Exp $ 
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


import javax.faces.context.FacesContext; 
import javax.faces.lifecycle.ViewHandler; 
import javax.faces.tree.Tree; 


import javax.servlet.http.HttpServletRequest; 
import javax.servlet.ServletException; 
import javax.servlet.RequestDispatcher; 


import com.sun.faces.tree.XmlTreeImpl; 


/** 
 * <B>ViewHandlerImpl</B> is the default implementation class for ViewHandler. 
 * @version $Id: ViewHandlerImpl.java,v 1.1 2002/06/25 21:08:39 eburns Exp $ 
 * 
 * @see javax.faces.lifecycle.ViewHandler 
 * 
 */ 
public class ViewHandlerImpl implements ViewHandler { 


    /** 
     * This implementaton will forward to a page url associated with 
     * the current reponse tree in the {@link FacesContext}. </p> 
     * 
     * @param context FacesContext for the current request 
     * 
     * @exception IOException if an input/output error occurs 
     * @exception NullPointerException if <code>context</code> 
     * is <code>null</code> 
     */ 
    public void renderView(FacesContext context) throws IOException { 
        if (context == null) { 
            throw new NullPointerException("Null FacesContext"); 
        } 


        HttpServletRequest request = (HttpServletRequest) 
            context.getServletRequest(); 


        RequestDispatcher requestDispatcher = null; 
        Tree tree = context.getResponseTree(); 


        try { 
            //PENDING(rogerk) do we want to add "getPageUrl" method to 
            //javax.faces.tree.Tree class (in which case, we won;t have 
            //to cast?? - probably not, since the concept of the tree 
            //(in this sense) is going away... 
            // 
            String requestURI = ((XmlTreeImpl)tree).getPageUrl(); 
            requestDispatcher = request.getRequestDispatcher(requestURI); 
            requestDispatcher.forward(request, context.getServletResponse()); 
        } catch (IOException e) { 
            throw new IOException("Can't forward:Exception:" + e); 
        } catch (ServletException e) { 
            throw new IOException("Can't forward:Exception:" + e); 
        } 
    } 
} 
