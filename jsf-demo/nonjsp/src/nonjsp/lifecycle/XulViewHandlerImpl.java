/* 
 * $Id: XulViewHandlerImpl.java,v 1.3 2003/03/27 19:44:06 jvisvanathan Exp $ 
 */ 


/*
 * Copyright 2002, 2003 Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * 
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *    
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *  
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *  
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */


// XulViewHandlerImpl.java 

package nonjsp.lifecycle; 

import java.io.IOException; 
import java.util.Iterator;
import java.util.Map;

import nonjsp.util.RIConstants;
import nonjsp.util.Util;

import nonjsp.tree.XmlTreeFactoryImpl;

import javax.faces.FacesException;
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
 * @version $Id: XulViewHandlerImpl.java,v 1.3 2003/03/27 19:44:06 jvisvanathan Exp $ 
 * 
 * @see javax.faces.lifecycle.ViewHandler 
 * 
 */ 
public class XulViewHandlerImpl implements ViewHandler { 

    // Log instance for this class
    protected static Log log = LogFactory.getLog(XulViewHandlerImpl.class);

    // Render the components
    public void renderView(FacesContext context) throws IOException, 
             FacesException { 

        if (context == null) { 
            throw new NullPointerException("RenderView: FacesContext is null");
        } 

        RequestDispatcher requestDispatcher = null; 

        log.trace("Step 1: Parse XmlRuleSet");
        Map requestMap = context.getExternalContext().getRequestMap();
        String treeId = (String)
                   requestMap.get("javax.servlet.include.path_info");
        if (treeId == null) {
	    treeId = context.getExternalContext().getRequestPathInfo();
        }

	log.trace("Step 2: Set Tree in FacesContext");
        XmlTreeFactoryImpl xmlTree = new XmlTreeFactoryImpl();
        context.setTree(xmlTree.getTree(context, treeId));

        HttpServletResponse response = (HttpServletResponse)
        (context.getExternalContext().getResponse());
	log.trace("Step 3: Set ResponseWriter in FacesContext");
        context.setResponseWriter
            (new ServletResponseWriter(response.getWriter()));
        response.setContentType("text/html");

	log.trace("Step 4: Call encode methods on components");
        createHeader(context);
        renderResponse(context);
        createFooter(context);

        log.trace("Step 5: Save the tree and locale in the session");
        Map sessionMap = context.getExternalContext().getSessionMap();
        sessionMap.put(RIConstants.REQUEST_LOCALE, context.getLocale());
        sessionMap.put(RIConstants.FACES_TREE, context.getTree());

    } 

    // Create the header components for this page
    public void createHeader(FacesContext context) throws IOException {

        UIOutput header = new UIOutput();
        header.setComponentId("header");
        StringBuffer sb = new StringBuffer("<html>\n");
        sb.append("<head>\n");
        sb.append("<title>");
        sb.append(context.getExternalContext().getRequestContextPath());
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
