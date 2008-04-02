/* 
 * $Id: XulViewHandlerImpl.java,v 1.1 2003/02/11 01:02:44 horwat Exp $ 
 */ 


/* 
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved. 
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 */ 


// XulViewHandlerImpl.java 

package com.sun.faces.lifecycle; 

import java.io.IOException; 
import java.util.Iterator;

import com.sun.faces.RIConstants;
import com.sun.faces.util.Util;
import com.sun.faces.tree.XmlTreeFactoryImpl;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext; 
import javax.faces.lifecycle.ViewHandler; 
import javax.faces.tree.Tree; 
import javax.faces.webapp.ServletResponseWriter;

import javax.servlet.http.HttpServletRequest; 
import javax.servlet.http.HttpServletResponse; 
import javax.servlet.http.HttpSession; 
import javax.servlet.ServletException; 
import javax.servlet.RequestDispatcher; 

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/** 
 * <B>XulViewHandlerImpl</B> is the Xul non-JSP ViewHandler implementation
 *
 * @version $Id: XulViewHandlerImpl.java,v 1.1 2003/02/11 01:02:44 horwat Exp $ 
 * 
 * @see javax.faces.lifecycle.ViewHandler 
 * 
 */ 
public class XulViewHandlerImpl implements ViewHandler { 

    // Log instance for this class
    protected static Log log = LogFactory.getLog(XulViewHandlerImpl.class);

    // Render the components
    public void renderView(FacesContext context) throws IOException, 
             ServletException { 

        if (context == null) { 
            throw new NullPointerException(Util.getExceptionMessage(
                Util.NULL_CONTEXT_ERROR_MESSAGE_ID));
        } 

        HttpServletRequest request = (HttpServletRequest) 
            context.getServletRequest(); 
        HttpServletResponse response = (HttpServletResponse) 
            context.getServletResponse(); 
        HttpSession session = context.getHttpSession();

        RequestDispatcher requestDispatcher = null; 

        log.trace("Step 1: Parse XmlRuleSet");
        String treeId = (String)
                   request.getAttribute("javax.servlet.include.path_info");
        if (treeId == null) {
	    treeId = request.getPathInfo();
        }

	log.trace("Step 2: Set Tree in FacesContext");
        XmlTreeFactoryImpl xmlTree = new XmlTreeFactoryImpl();
        context.setTree(xmlTree.getTree(context, treeId));

	log.trace("Step 3: Set ResponseWriter in FacesContext");
        context.setResponseWriter
            (new ServletResponseWriter(response.getWriter()));
        response.setContentType("text/html");

	log.trace("Step 4: Call encode methods on components");
        createHeader(context);
        renderResponse(context);
        createFooter(context);

        log.trace("Step 5: Save the tree and locale in the session");
        session.setAttribute(RIConstants.REQUEST_LOCALE, context.getLocale());
        session.setAttribute(RIConstants.FACES_TREE, context.getTree());

    } 

    // Create the header components for this page
    public void createHeader(FacesContext context) throws IOException {

        UIOutput header = new UIOutput();
        header.setComponentId("header");
        StringBuffer sb = new StringBuffer("<html>\n");
        sb.append("<head>\n");
        sb.append("<title>");
        sb.append(
            ((HttpServletRequest)context.getServletRequest()).getPathInfo());
        sb.append("</title>\n");
        sb.append("</head>\n");
        sb.append("<body>\n");
        header.setValue(sb.toString());
        renderResponse(context, header);
    }


    // Create the footer components for this page
    public void createFooter(FacesContext context) throws IOException {

        UIOutput footer = new UIOutput();
        footer.setComponentId("footer");
        StringBuffer sb = new StringBuffer("</body>\n");
        sb.append("</html>\n");
        footer.setValue(sb.toString());
        renderResponse(context, footer);
    }

    // Render the response content for the completed page
    public void renderResponse(FacesContext context) throws IOException {

        UIComponent root = context.getTree().getRoot();
        int n = root.getChildCount();
        if (log.isTraceEnabled()) {
            log.trace("Rendering " + root + " with " + n + " children");
        }
        renderResponse(context, root);

    }


    // Render the response content for an individual component
    protected void renderResponse(FacesContext context, UIComponent component)
        throws IOException {

        if (log.isTraceEnabled()) {
            log.trace("Render Begin: " + component.getComponentId());
        }
        component.encodeBegin(context);
        if (component.getRendersChildren()) {
            component.encodeChildren(context);
        } else {
            Iterator kids = component.getChildren();
            while (kids.hasNext()) {
                renderResponse(context, (UIComponent) kids.next());
            }
        }
        if (log.isTraceEnabled()) {
            log.trace("Render End: " + component.getComponentId());
        }
        component.encodeEnd(context);

    }

} 
