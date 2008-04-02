/* 
 * $Id: XulViewHandlerImpl.java,v 1.7 2003/10/07 23:05:49 rkitain Exp $ 
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

package nonjsp.application; 

import nonjsp.util.RIConstants;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSetBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.SimpleLog;
import org.mozilla.util.Assert;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext; 
import javax.faces.context.ResponseWriter; 
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;


/** 
 * <B>XulViewHandlerImpl</B> is the Xul non-JSP ViewHandler implementation
 *
 * @version $Id: XulViewHandlerImpl.java,v 1.7 2003/10/07 23:05:49 rkitain Exp $ 
 * 
 * @see javax.faces.application.ViewHandler 
 * 
 */ 
public class XulViewHandlerImpl implements ViewHandler { 

    // Log instance for this class
    protected static Log log = LogFactory.getLog(XulViewHandlerImpl.class);
    protected static final String CHAR_ENCODING = "ISO-8859-1";
    protected static final String CONTENT_TYPE = "text/html";

    //PENDING(rogerk) maybe config file?
    /**
      * Should we use a validating XML parser to read the configuration file?
      */
    protected boolean validate = false;

    /**
     * The set of public identifiers, and corresponding resource names, for
     * the versions of the configuration file DTDs that we know about.  There
     * <strong>MUST</strong> be an even number of Strings in this list!
     * Only used if you are validating against DTD.	 
     * Could be read from config file instead.
     */
    protected String registrations[] = {
        "-//UIT//DTD UIML 2.0 Draft//EN",
        "UIML2_0d.dtd"
    };


    // Relationship Instance Variables
    protected XmlDialectProvider dialectProvider = null;


    public XulViewHandlerImpl() {
        super();
        dialectProvider = new XulDialectProvider();
    }


    // Render the components
    public void renderView(FacesContext context, 
			   UIViewRoot viewToRender) throws IOException, 
             FacesException { 

        if (context == null || viewToRender == null) { 
            throw new NullPointerException("RenderView: FacesContext is null");
        } 

        RequestDispatcher requestDispatcher = null; 

	log.trace("Determine View Identifier And Build View...");
        String viewId = viewToRender.getViewId();

        HttpServletResponse response = (HttpServletResponse)
        (context.getExternalContext().getResponse());
	log.trace("Set ResponseWriter in FacesContext");

        RenderKitFactory factory = (RenderKitFactory)
            FactoryFinder.getFactory(FactoryFinder.RENDER_KIT_FACTORY);
        RenderKit renderKit = factory.getRenderKit(RenderKitFactory.DEFAULT_RENDER_KIT);

        ResponseWriter writer = renderKit.createResponseWriter(
            response.getWriter(), CONTENT_TYPE, CHAR_ENCODING);
        context.setResponseWriter(writer);
        response.setContentType(CONTENT_TYPE);

	log.trace("Call encode methods on components");
        createHeader(context);
        renderResponse(context);
        createFooter(context);

        log.trace("Save the view and locale in the session");
        Map sessionMap = getSessionMap(context);
        sessionMap.put(RIConstants.REQUEST_LOCALE, context.getLocale());
        sessionMap.put(RIConstants.FACES_VIEW, context.getViewRoot());

    } 

    public UIViewRoot restoreView(FacesContext context, String viewId) {
        if (context == null) {
            throw new NullPointerException("RestoreView: FacesContext is null");
        }

        if (log.isTraceEnabled()) {
            log.trace("viewId: " + viewId);
        }
        UIViewRoot root = null;
        InputStream viewInput = null;
        RuleSetBase ruleSet = null;

        root = new UIViewRoot();

        if (null == viewId) {
            // PENDING(edburns): need name for default view
            // PENDING(rogerk) : what to specify for page url
            // (last parameter)????
            root.setViewId("default");
            context.setViewRoot(root);
            return root;
        }

        try {
            viewInput = context.getExternalContext().getResourceAsStream(viewId);
 	    if (null == viewInput) {
                throw new NullPointerException();
            }
        }
        catch (Throwable e) {
            throw new FacesException("Can't get stream for " + viewId, e);
        }

        // PENDING(edburns): can this digester instance be maintained as an
        // ivar?

        Digester digester = new Digester();

        // SimpleLog implements the Log interface (from commons.logging).
        // This replaces deprecated "Digester.setDebug" method.
        // PENDING(rogerk) Perhaps the logging level should be configurable..
        // For debugging, you can set the log level to
        // "SimpleLog.LOG_LEVEL_DEBUG".
        //
        SimpleLog sLog = new SimpleLog("digesterLog");
        sLog.setLevel(SimpleLog.LOG_LEVEL_ERROR);
        digester.setLogger(sLog);

        digester.setNamespaceAware(true);

        digester.setValidating(validate);

        ruleSet = dialectProvider.getRuleSet();

        Assert.assert_it(null != ruleSet);

        digester.addRuleSet(ruleSet);

        if (validate) {
            for (int i = 0; i < registrations.length; i += 2) {
                URL url = this.getClass().getResource(registrations[i+1]);
                if (url != null) {
                    digester.register(registrations[i], url.toString());
                }
            }
        }

        digester.push(root);
        try {
            root = (UIViewRoot)digester.parse(viewInput);
        } catch (Throwable e) {
            throw new FacesException("Can't parse stream for " + viewId, e);
        }

        //Print view for debugging
        if (log.isDebugEnabled()) {
            printView(root);
        }

        root.setViewId(viewId);
        context.setViewRoot(root);

        return root;
    }

    public UIViewRoot createView(FacesContext context, String viewId) {
        if (context == null) {
            throw new NullPointerException("CreateView: FacesContext is null");
        }

        return restoreView(context, viewId);
    }

    //PENDING (horwat) Do we need state management for non-jsp example?
    public StateManager getStateManager() {
        return null;
    }

    public String getViewIdPath(FacesContext context, String viewId) {
        if (viewId.charAt(0) != '/') {            
            throw new IllegalArgumentException(
		"Illegal view ID "+viewId+". the ID must begin with '/'");
        }
        return viewId; 
    }


    // Create the header components for this page
    private void createHeader(FacesContext context) throws IOException {

        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("html", null);
        writer.writeText("\n", null);
        writer.startElement("head", null);
        writer.writeText("\n", null);
        writer.startElement("title", null);
        writer.writeText(context.getExternalContext().getRequestContextPath(), null);
        writer.endElement("title");
        writer.writeText("\n", null);
        writer.endElement("head");
        writer.writeText("\n", null);
        writer.startElement("body", null);
        writer.writeText("\n", null);
    }


    // Create the footer components for this page
    private void createFooter(FacesContext context) throws IOException {

        ResponseWriter writer = context.getResponseWriter();

        writer.endElement("body");
        writer.writeText("\n", null);
        writer.endElement("html");
        writer.writeText("\n", null);
    }

    // Render the response content for the completed page
    private void renderResponse(FacesContext context) throws IOException {

        UIComponent root = context.getViewRoot();
        if (log.isTraceEnabled()) {
            log.trace("Rendering " + root + " with " + root.getChildCount() + " children");
        }
        renderResponse(context, root);

    }


    // Render the response content for an individual component
    private void renderResponse(FacesContext context, UIComponent component)
        throws IOException {

        if (log.isTraceEnabled()) {
            log.trace("Render Begin: " + component.getId());
        }
        component.encodeBegin(context);
        if (component.getRendersChildren()) {
            component.encodeChildren(context);
        } else {
            Iterator kids = component.getChildren().iterator();
            while (kids.hasNext()) {
                renderResponse(context, (UIComponent) kids.next());
            }
        }
        if (log.isTraceEnabled()) {
            log.trace("Render End: " + component.getId());
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

    private void printView(UIComponent uic) {
        Iterator kids = uic.getChildren().iterator();
        while (kids.hasNext()) {
            printView((UIComponent) kids.next());
        }
        log.debug("VIEW: " + uic.getId());
    }

    public void writeState(FacesContext context) throws IOException {
    }
} 
