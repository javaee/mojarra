/* 
 * $Id: XulViewHandlerImpl.java,v 1.6 2007/04/27 22:00:35 ofung Exp $ 
 */

/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright 1997-2007 Sun Microsystems, Inc. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License. You can obtain
 * a copy of the License at https://glassfish.dev.java.net/public/CDDL+GPL.html
 * or glassfish/bootstrap/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * Sun designates this particular file as subject to the "Classpath" exception
 * as provided by Sun in the GPL Version 2 section of the License file that
 * accompanied this code.  If applicable, add the following below the License
 * Header, with the fields enclosed by brackets [] replaced by your own
 * identifying information: "Portions Copyrighted [year]
 * [name of copyright owner]"
 * 
 * Contributor(s):
 * 
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */

// XulViewHandlerImpl.java 

package nonjsp.application;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSetBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.impl.SimpleLog;

import nonjsp.util.RIConstants;


/**
 * <B>XulViewHandlerImpl</B> is the Xul non-JSP ViewHandler implementation
 *
 * @version $Id: XulViewHandlerImpl.java,v 1.6 2007/04/27 22:00:35 ofung Exp $ *
 * @see javax.faces.application.ViewHandler
 */
public class XulViewHandlerImpl extends ViewHandler {

    // Log instance for this class
    protected static Log log = LogFactory.getLog(XulViewHandlerImpl.class);
    protected static final String CHAR_ENCODING = "ISO-8859-1";
    protected static final String CONTENT_TYPE = "text/html";

    //PENDING(rogerk) maybe config file?
    /** Should we use a validating XML parser to read the configuration file? */
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
        RenderKit renderKit = factory.getRenderKit(context,
                                                   RenderKitFactory.HTML_BASIC_RENDER_KIT);

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
        sessionMap.put(RIConstants.REQUEST_LOCALE,
                       context.getViewRoot().getLocale());
        sessionMap.put(javax.faces.render.ResponseStateManager.VIEW_STATE_PARAM,
                       context.getViewRoot());

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
        root.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);

        if (null == viewId) {
            // PENDING(edburns): need name for default view
            // PENDING(rogerk) : what to specify for page url
            // (last parameter)????
            root.setViewId("default");
            context.setViewRoot(root);
            Locale locale = calculateLocale(context);
            root.setLocale(locale);
            return root;
        }

        try {
            viewInput =
                  context.getExternalContext().getResourceAsStream(viewId);
            if (null == viewInput) {
                throw new NullPointerException();
            }
        } catch (Throwable e) {
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

        digester.addRuleSet(ruleSet);

        if (validate) {
            for (int i = 0; i < registrations.length; i += 2) {
                URL url = this.getClass().getResource(registrations[i + 1]);
                if (url != null) {
                    digester.register(registrations[i], url.toString());
                }
            }
        }

        digester.push(root);
        try {
            root = (UIViewRoot) digester.parse(viewInput);
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


    public String getActionURL(FacesContext context, String viewId) {
        if (viewId.charAt(0) != '/') {
            throw new IllegalArgumentException(
                  "Illegal view ID " + viewId + ". the ID must begin with '/'");
        }
        // PENDING(edburns): do a more complete implementation that
        // deals with the vagaries of prefix and suffix mapping.  For
        // now , just slap "/faces" onto the front.
        if (!viewId.startsWith("/faces")) {
            viewId = "/faces" + viewId;
        }
        return context.getExternalContext().getRequestContextPath() + viewId;
    }


    public String getResourceURL(FacesContext context, String path) {
        if (path.startsWith("/")) {
            return context.getExternalContext().getRequestContextPath() + path;
        } else {
            return (path);
        }
    }


    // Create the header components for this page
    private void createHeader(FacesContext context) throws IOException {

        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("html", null);
        writer.writeText("\n", null);
        writer.startElement("head", null);
        writer.writeText("\n", null);
        writer.startElement("title", null);
        writer.writeText(context.getExternalContext().getRequestContextPath(),
                         null);
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
            log.trace(
                  "Rendering " + root + " with " + root.getChildCount() +
                  " children");
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


    public Locale calculateLocale(FacesContext context) {
        Locale result = null;
        // determine the locales that are acceptable to the client based on the 
        // Accept-Language header and the find the best match among the 
        // supported locales specified by the client.
        Enumeration e = ((ServletRequest)
              context.getExternalContext().getRequest()).getLocales();
        while (e.hasMoreElements()) {
            Locale perf = (Locale) e.nextElement();
            result = findMatch(context, perf);
            if (result != null) {
                break;
            }
        }
        // no match is found.
        if (result == null) {
            if (context.getApplication().getDefaultLocale() == null) {
                result = Locale.getDefault();
            } else {
                result = context.getApplication().getDefaultLocale();
            }
        }
        return result;
    }


    public String calculateRenderKitId(FacesContext context) {
        return null;
    }


    /**
     * Attempts to find a matching locale based on <code>perf></code> and
     * list of supported locales, using the matching algorithm
     * as described in JSTL 8.3.2.
     */
    protected Locale findMatch(FacesContext context, Locale perf) {
        Locale result = null;
        Iterator it = context.getApplication().getSupportedLocales();
        while (it.hasNext()) {
            Locale supportedLocale = (Locale) it.next();

            if (perf.equals(supportedLocale)) {
                // exact match
                result = supportedLocale;
                break;
            } else {
                // Make sure the preferred locale doesn't have  country set, when 
                // doing a language match, For ex., if the preferred locale is
                // "en-US", if one of supported locales is "en-UK", even though 
                // its language matches that of the preferred locale, we must 
                // ignore it.
                if (perf.getLanguage().equals(supportedLocale.getLanguage()) &&
                    supportedLocale.getCountry().equals("")) {
                    result = supportedLocale;
                }
            }
        }
        return result;
    }
} 
