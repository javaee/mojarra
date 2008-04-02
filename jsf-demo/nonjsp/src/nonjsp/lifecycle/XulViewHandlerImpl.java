/* 
 * $Id: XulViewHandlerImpl.java,v 1.8 2003/08/08 19:17:35 rkitain Exp $ 
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
import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext; 
import javax.faces.context.ResponseWriter; 
import javax.faces.lifecycle.ViewHandler; 
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.faces.tree.Tree; 

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
 * @version $Id: XulViewHandlerImpl.java,v 1.8 2003/08/08 19:17:35 rkitain Exp $ 
 * 
 * @see javax.faces.lifecycle.ViewHandler 
 * 
 */ 
public class XulViewHandlerImpl implements ViewHandler { 

    // Log instance for this class
    protected static Log log = LogFactory.getLog(XulViewHandlerImpl.class);
    protected static final String CHAR_ENCODING = "ISO-8859-1";


    // Render the components
    public void renderView(FacesContext context) throws IOException, 
             FacesException { 

        if (context == null) { 
            throw new NullPointerException("RenderView: FacesContext is null");
        } 

        RequestDispatcher requestDispatcher = null; 

	log.trace("Determine Tree Identifier And Build Tree...");
        String treeId = context.getTree().getTreeId();
        XmlTreeFactoryImpl xmlTree = new XmlTreeFactoryImpl();
        context.setTree(xmlTree.getTree(context, treeId));

        HttpServletResponse response = (HttpServletResponse)
        (context.getExternalContext().getResponse());
	log.trace("Set ResponseWriter in FacesContext");

        RenderKitFactory factory = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = factory.getRenderKit(RenderKitFactory.DEFAULT_RENDER_KIT);

        context.setResponseWriter(
            renderKit.getResponseWriter(response.getWriter(), CHAR_ENCODING));
        response.setContentType("text/html");

	log.trace("Call encode methods on components");
        createHeader(context);
        renderResponse(context);
        createFooter(context);

        log.trace("Save the tree and locale in the session");
        Map sessionMap = getSessionMap(context);
        sessionMap.put(RIConstants.REQUEST_LOCALE, context.getLocale());
        sessionMap.put(RIConstants.FACES_TREE, context.getTree());

    } 

    // Create the header components for this page
    public void createHeader(FacesContext context) throws IOException {

        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("html");
        writer.writeText('\n');
        writer.startElement("head");
        writer.writeText('\n');
        writer.startElement("title");
        writer.writeText(context.getExternalContext().getRequestContextPath());
        writer.endElement("title");
        writer.writeText('\n');
        writer.endElement("head");
        writer.writeText('\n');
        writer.startElement("body");
        writer.writeText('\n');
    }


    // Create the footer components for this page
    public void createFooter(FacesContext context) throws IOException {

        ResponseWriter writer = context.getResponseWriter();

        writer.endElement("body");
        writer.writeText('\n');
        writer.endElement("html");
        writer.writeText('\n');
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

    private Map getSessionMap(FacesContext context) {
        if (context == null) {
            context = FacesContext.getCurrentInstance();
        }
        Map sessionMap = context.getExternalContext().getSessionMap();
        if (sessionMap == null) {
            context.getExternalContext().getSession(true);
            sessionMap = context.getExternalContext().getSessionMap();
        }
        return sessionMap;
    }

} 
