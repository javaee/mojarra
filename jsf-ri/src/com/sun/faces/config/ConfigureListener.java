/*
 * $Id: ConfigureListener.java,v 1.104 2007/04/25 04:07:00 rlubke Exp $
 */
/*
 * The contents of this file are subject to the terms
 * of the Common Development and Distribution License
 * (the License). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the License at
 * https://javaserverfaces.dev.java.net/CDDL.html or
 * legal/CDDLv1.0.txt. 
 * See the License for the specific language governing
 * permission and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL
 * Header Notice in each file and include the License file
 * at legal/CDDLv1.0.txt.    
 * If applicable, add the following below the CDDL Header,
 * with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 * 
 * [Name of File] [ver.__] [Date]
 * 
 * Copyright 2005 Sun Microsystems Inc. All Rights Reserved
 */

package com.sun.faces.config;

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter;
import com.sun.faces.config.WebConfiguration.WebContextInitParameter;
import com.sun.faces.el.FacesCompositeELResolver;
import com.sun.faces.el.ELContextListenerImpl;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.Util;
import com.sun.faces.util.Timer;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.el.CompositeELResolver;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.ExpressionFactory;
import javax.el.FunctionMapper;
import javax.el.VariableMapper;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.render.RenderKit;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.jsp.JspApplicationContext;
import javax.servlet.jsp.JspFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.text.MessageFormat;

/**
 * <p>Parse all relevant JavaServer Faces configuration resources, and
 * configure the Reference Implementation runtime environment.</p>
 * <p/>
 */
@SuppressWarnings({"ForLoopReplaceableByForEach"}) 
public class ConfigureListener implements ServletContextListener {


    private static final Logger LOGGER =FacesLogger.CONFIG.getLogger();


    /**
     * <p>All known factory names.</p>
     */
    private static final String[] FACTORY_NAMES = {
        FactoryFinder.APPLICATION_FACTORY,
        FactoryFinder.FACES_CONTEXT_FACTORY,
        FactoryFinder.LIFECYCLE_FACTORY,
        FactoryFinder.RENDER_KIT_FACTORY
    };


    protected WebConfiguration webConfig;

   
    
    // ------------------------------------------ ServletContextListener Methods


    /**
     * A subclass of ConfigureListener can override isFeatureEnabled and reset
     * one of the boolean values.  This method lets the user know that this 
     * happened for a particular feature.
     */
    public void logOverriddenContextConfigValues() {
        for (BooleanWebContextInitParameter param : BooleanWebContextInitParameter
             .values()) {

            if (webConfig.getBooleanContextInitParameter(param)
                != isFeatureEnabled(param)) {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.log(Level.INFO,
                               (isFeatureEnabled(param)
                                ? "jsf.config.webconfig.configinfo.reset.enabled"
                                : "jsf.config.webconfig.configinfo.reset.disabled"),
                               new Object[]{
                                    webConfig.getServletContextName(),
                                    param.getQualifiedName()
                               });
                }
            }
        }
    }


    public void contextInitialized(ServletContextEvent sce) {
        Timer timer = Timer.getInstance();
        if (timer != null) {
            timer.startTiming();
        }
        ServletContext context = sce.getServletContext();

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE,
                       MessageFormat.format(
                            "ConfigureListener.contextInitialized({0})",
                            getServletContextIdentifier(context)));
        }

        webConfig = WebConfiguration.getInstance(context);
        logOverriddenContextConfigValues();
        ConfigManager configManager = ConfigManager.getInstance();

        if (configManager.hasBeenInitialized(context)) {
            return;
        }

        // Check to see if the FacesServlet is present in the
        // web.xml.   If it is, perform faces configuration as normal,
        // otherwise, simply return.
        if (!isFeatureEnabled(BooleanWebContextInitParameter.ForceLoadFacesConfigFiles)) {
            WebXmlProcessor processor = new WebXmlProcessor(context);
            if (!processor.isFacesServletPresent()) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE,
                               "No FacesServlet found in deployment descriptor - bypassing configuration");
                }
            } else {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE,
                               "FacesServlet found in deployment descriptor - processing configuration.");
                }
            }
        }

        FacesContext initContext = new InitFacesContext(context);

        try {

            LOGGER.log(Level.INFO,
                       "jsf.config.listener.version",
                       getServletContextIdentifier(context));

            // see if we need to disable our TLValidator
            Util.setHtmlTLVActive(
                  isFeatureEnabled(BooleanWebContextInitParameter.EnableHtmlTagLibraryValidator));

            configManager.initialize(context);

            // Step 7, verify that all the configured factories are available
            // and optionall that configured objects can be created.  Clear
            // the InitFacesContext to ensure it isn't picked up by any of the
            // objects validated by these methods - reinit the context after
            // verification is complete.
            // PENDING interweave verify logic into ConfigProcessors
            verifyFactories();
            registerELResolverAndListenerWithJsp(context);
            
        } finally {
           
            ApplicationAssociate associate =
                 ApplicationAssociate.getInstance(context);
            if (associate != null) {
                associate.setContextName(getServletContextIdentifier(context));
            }
            RenderKitUtils.loadSunJsfJs(initContext.getExternalContext());
            initContext.release();
            LOGGER.log(Level.FINE,
                       "jsf.config.listener.version.complete");
            if (timer != null) {
                timer.stopTiming();
                timer.logResult("Initialization of context " +
                                getServletContextIdentifier(context));
            }           
        }
    }


    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();

        LOGGER.log(Level.FINE,
                   "ConfigureListener.contextDestroyed({0})",
                   context.getServletContextName());

        try {
            // Release any allocated application resources
            FactoryFinder.releaseFactories();
        } finally {
            FacesContext initContext = new InitFacesContext(context);
            ApplicationAssociate
                  .clearInstance(initContext.getExternalContext());
            // Release the initialization mark on this web application
            ConfigManager.getInstance().destory(context);
            initContext.release();
            WebConfiguration.clear(context);
        }

    }    

    // --------------------------------------------------------- Private Methods
   
        

    /**
     * <p>Verify that all of the required factory objects are available.</p>
     *
     * @throws FacesException if a factory cannot be created
     */
    private void verifyFactories() throws FacesException {

        for (int i = 0, len = FACTORY_NAMES.length; i < len; i++) {
            try {
                FactoryFinder.getFactory(FACTORY_NAMES[i]);
            } catch (Exception e) {
                throw new FacesException(e);
            }
        }

    }




    
    /**
     * <p>Determines if a particular feature, configured via the web
     * deployment descriptor as a <code>true/false</code> value, is
     * enabled or not.</p>
     * @param param the <code>BooleanWebContextInitParameter</code> 
     *  of interest
     *
     * @return <code>true</code> if the feature in question is enabled, otherwise
     *  <code>false</code>
     */
    protected boolean isFeatureEnabled(BooleanWebContextInitParameter param) {
        return webConfig.getBooleanContextInitParameter(param);      
    }

    private static String getServletContextIdentifier(ServletContext context) {
        if (context.getMajorVersion() == 2 && context.getMinorVersion() < 5) {
            return context.getServletContextName();
        } else {
            return context.getContextPath();
        }
    }


    
    private static boolean isJspTwoOne() {

        // The following try/catch is a hack to work around
        // a bug in Tomcat 6 where JspFactory.getDefaultFactory() will
        // return null unless JspRuntimeContext has been loaded.
        try {
            Class.forName("org.apache.jasper.compiler.JspRuntimeContext");
        } catch (ClassNotFoundException cnfe) {
            ;
        }

        if (JspFactory.getDefaultFactory() == null) {
            return false;
        }
        try {
            JspFactory.class.getMethod("getJspApplicationContext", 
                                       ServletContext.class );
        } catch (NoSuchMethodException nsme) {
            return false;
        }
        return true;
    }

    public void registerELResolverAndListenerWithJsp(ServletContext context) {               
        
        // check if JSP 2.1
        if (!isJspTwoOne()) {
            
            // not JSP 2.1
            
            // first try to load a factory defined in web.xml
            String elFactType = webConfig.getContextInitParameter(
                  WebContextInitParameter.ExpressionFactory);
            if (elFactType == null || "".equals(elFactType.trim())) {
                // else use EL-RI
                elFactType = WebContextInitParameter.ExpressionFactory
                      .getDefaultValue();
            }
            
            try {
                ExpressionFactory factory = (ExpressionFactory) Class.forName(
                        elFactType).newInstance();
                ApplicationAssociate associate =
                     ApplicationAssociate.getInstance(context);
                if (associate != null) {
                    associate.setExpressionFactory(factory);
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error Instantiating ExpressionFactory", e);
            }                        
        
        } else {
            
            // Is JSP 2.1
            
            // JSP 2.1 specific check
            if (JspFactory.getDefaultFactory().getJspApplicationContext(context) == null) {
                return;
            }
            
            // register an empty resolver for now. It will be populated after the 
            // first request is serviced.
            CompositeELResolver compositeELResolverForJsp = 
                new FacesCompositeELResolver(FacesCompositeELResolver.ELResolverChainType.JSP);
            ApplicationAssociate associate =
                     ApplicationAssociate.getInstance(context);
            if (associate != null) {
                associate.setFacesELResolverForJsp(compositeELResolverForJsp);
            }
                    
            // get JspApplicationContext.
            JspApplicationContext jspAppContext = JspFactory.getDefaultFactory()
                    .getJspApplicationContext(context);
    
            // cache the ExpressionFactory instance in ApplicationAssociate
            if (associate != null) {
                associate.setExpressionFactory(jspAppContext.getExpressionFactory());
            }
    
            // register compositeELResolver with JSP
            try {
                jspAppContext.addELResolver(compositeELResolverForJsp);
            }
            catch (IllegalStateException e) {
                if (!Util.isUnitTestModeEnabled()) {
                    throw e;
                }
            }
    
            // register JSF ELContextListenerImpl with Jsp
            ELContextListenerImpl elContextListener = new ELContextListenerImpl();
            jspAppContext.addELContextListener(elContextListener);
         }
    }


    // ----------------------------------------------------------- Inner classes


    private static class InitFacesContext extends FacesContext {

        private ExternalContext ec;
        private UIViewRoot viewRoot;

        public InitFacesContext(ServletContext sc) {
            ec = new ServletContextAdapter(sc);
            setCurrentInstance(this);
        }

        public Application getApplication() {
            ApplicationFactory factory = (ApplicationFactory)
                 FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
            return factory.getApplication();
        }

        public Iterator<String> getClientIdsWithMessages() {
            List<String> list = Collections.emptyList();
            return list.iterator();
        }

        public ExternalContext getExternalContext() {
            return ec;
        }

        public FacesMessage.Severity getMaximumSeverity() {
            return FacesMessage.SEVERITY_INFO;
        }

        public Iterator<FacesMessage> getMessages() {
            List<FacesMessage> list = Collections.emptyList();
            return list.iterator();
        }

        public Iterator<FacesMessage> getMessages(String clientId) {
            return getMessages();
        }

        public RenderKit getRenderKit() {
            return null;
        }

        public boolean getRenderResponse() {
            return true;
        }

        public boolean getResponseComplete() {
            return true;
        }

        public ResponseStream getResponseStream() {
            return null;
        }

        public void setResponseStream(ResponseStream responseStream) {
            ;
        }

        public ResponseWriter getResponseWriter() {
            return null;
        }

        public void setResponseWriter(ResponseWriter responseWriter) {
            ;
        }

        public UIViewRoot getViewRoot() {
            if (viewRoot == null) {
                viewRoot = new UIViewRoot();
                viewRoot.setLocale(Locale.getDefault());
            }            
            return viewRoot;
        }

        public void setViewRoot(UIViewRoot root) {
            ;
        }

        public void addMessage(String clientId, FacesMessage message) {
            ;
        }

        public void release() {
            setCurrentInstance(null);
        }

        public void renderResponse() {
            ;
        }

        public void responseComplete() {
            ;
        }

        public ELContext getELContext() {
            return new ELContext() {
                public ELResolver getELResolver() {
                    return null;
                }

                public FunctionMapper getFunctionMapper() {
                    return null;
                }

                public VariableMapper getVariableMapper() {
                    return null; 
                }
            };
        }
    }

    private static class ServletContextAdapter extends ExternalContext {
        
        private ServletContext servletContext = null;
        private ApplicationMap applicationMap = null;
        
        public ServletContextAdapter(ServletContext sc) {
            this.servletContext = sc;
        }
        
        public void dispatch(String path) throws IOException {
        }
    
        public String encodeActionURL(String url) {
            return null;
        }   

        public String encodeNamespace(String name) {
            return null;
        }


        public String encodeResourceURL(String url) {
            return null;
        }

       @SuppressWarnings("unchecked")
       public Map<String,Object> getApplicationMap() {
            if (applicationMap == null) {
                applicationMap = 
                    new ApplicationMap(servletContext);
            }
            return applicationMap;
        }
        
        public String getAuthType() {
            return null;
        }

        public Object getContext() {
            return servletContext;
        }

        public String getInitParameter(String name) {
            return null;
        }

        public Map getInitParameterMap() {
            return null;
        }

        public String getRemoteUser() {
            return null;
        }


        public Object getRequest() {
            return null;
        }

	public void setRequest(Object request) {
	}

        public String getRequestContextPath() {
            return null;
        }

        public Map<String,Object> getRequestCookieMap() {
            return null;
        }

        public Map<String,String> getRequestHeaderMap() {
            return null;
        }


        public Map<String,String[]> getRequestHeaderValuesMap() {
            return null;
        }


        public Locale getRequestLocale() {
            return null;
        }

        public Iterator<Locale> getRequestLocales() {
            return null;
        }



        public Map<String,Object> getRequestMap() {
            return null;
        }


        public Map<String,String> getRequestParameterMap() {
            return null;
        }


        public Iterator<String> getRequestParameterNames() {
            return null;
        }


        public Map<String,String[]> getRequestParameterValuesMap() {
            return null;
        }


        public String getRequestPathInfo() {
            return null;
        }


        public String getRequestServletPath() {
            return null;
        }
        
         
    public String getRequestContentType() {
        return null;
    }

    public String getResponseContentType() {
        return null;
    }

        public URL getResource(String path) throws MalformedURLException {
            return null;
        }


        public InputStream getResourceAsStream(String path) {
            return null;
        }

        public Set<String> getResourcePaths(String path) {
            return null;
        }

        public Object getResponse() {
            return null;
        }

        public void setResponse(Object response) {
        }

        public Object getSession(boolean create) {
            return null;
        }

        public Map<String,Object> getSessionMap() {
            return null;
        }

        public java.security.Principal getUserPrincipal() {
            return null;
        }
        
        public boolean isUserInRole(String role) {
            return false;
        }

        public void log(String message) {
        }
        
        public void log(String message, Throwable exception){
        }
        
        public void redirect(String url) throws IOException {
        }

        public String getRequestCharacterEncoding() {
            return null;
        }

        public void setRequestCharacterEncoding(String requestCharacterEncoding) throws UnsupportedEncodingException {

        }

        public String getResponseCharacterEncoding() {
            return null;
        }

        public void setResponseCharacterEncoding(String responseCharacterEncoding) {
        }

    }
    
    static class ApplicationMap extends java.util.AbstractMap {

        private final ServletContext servletContext;

        ApplicationMap(ServletContext servletContext) {
            this.servletContext = servletContext;
        }


        public Object get(Object key) {
            if (key == null) {
                throw new NullPointerException();
            }
            return servletContext.getAttribute(key.toString());
        }


        public Object put(Object key, Object value) {
            if (key == null) {
                throw new NullPointerException();
            }
            String keyString = key.toString();
            Object result = servletContext.getAttribute(keyString);
            servletContext.setAttribute(keyString, value);
            return (result);
        }


        public Object remove(Object key) {
            if (key == null) {
                return null;
            }
            String keyString = key.toString();
            Object result = servletContext.getAttribute(keyString);
            servletContext.removeAttribute(keyString);
            return (result);
        }


        public Set entrySet() {
           throw new UnsupportedOperationException();
        }


        public boolean equals(Object obj) {
            return !(obj == null || !(obj instanceof ApplicationMap))
                 && super.equals(obj);
        }


        public int hashCode() {
            int hashCode = 7 * servletContext.hashCode();
            for (Object o : entrySet()) {
                hashCode += o.hashCode();
            }
            return hashCode;
        }
        
        public void clear() {
            throw new UnsupportedOperationException();
        }

        public void putAll(Map t) {
            throw new UnsupportedOperationException();
        }
       

    } // END ApplicationMap


    /**
     * <p>Processes a web application's deployment descriptor looking
     * for a reference to <code>javax.faces.webapp.FacesServlet</code>.</p>
     */
    private static class WebXmlProcessor {

        private static final String WEB_XML_PATH = "/WEB-INF/web.xml";

        private boolean facesServletPresent;


        /**
         * <p>When instantiated, the web.xml of the current application
         * will be scanned looking for a references to the
         * <code>FacesServlet</code>.  <code>isFacesServletPresent()</code>
         * will return the appropriate value based on the scan.</p>
         * @param context the <code>ServletContext</code> for the application
         *  of interest
         */
        WebXmlProcessor(ServletContext context) {

            if (context != null) {
                scanForFacesServlet(context);
            }

        } // END WebXmlProcessor


        /**
         * @return <code>true</code> if the <code>WebXmlProcessor</code>
         * detected a <code>FacesServlet</code> entry, otherwise return
         * <code>false</code>.</p>        
         */
        boolean isFacesServletPresent() {

            return facesServletPresent;

        } // END isFacesServletPresent


        /**
         * <p>Parse the web.xml for the current application and scan
         * for a FacesServlet entry, if found, set the
         * <code>facesServletPresent</code> property to true.
         * @param context the ServletContext instance for this application
         */
        private void scanForFacesServlet(ServletContext context) {

            SAXParserFactory factory = getConfiguredFactory();
            try {
                SAXParser parser = factory.newSAXParser();
                parser.parse(context.getResourceAsStream(WEB_XML_PATH),
                              new WebXmlHandler());
            } catch (Exception e) {
                // This probably won't happen since the container would
                // catch it before we would, but, if we catch an exception
                // processing the web.xml, set facesServletFound to true to
                // default to our previous behavior of processing the faces
                // configuration.
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.log(Level.WARNING,
                               MessageFormat.format("Unable to process deployment descriptor for context ''{0}''",
                                                    getServletContextIdentifier(context)));
                }
                facesServletPresent = true;
            }

        } // END scanForFacesServlet

        /**
         * <p>Return a <code>SAXParserFactory</code> instance that is
         * non-validating and is namespace aware.</p>
         * @return configured <code>SAXParserFactory</code>
         */
        private SAXParserFactory getConfiguredFactory() {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false);
            factory.setNamespaceAware(true);
            return factory;

        } // END getConfiguredFactory


        /**
         * <p>A simple SAX handler to process the elements of interested
         * within a web application's deployment descriptor.</p>
         */
        private class WebXmlHandler extends DefaultHandler {

            private static final String SERVLET_CLASS = "servlet-class";
            private static final String FACES_SERVLET =
                "javax.faces.webapp.FacesServlet";

            private boolean servletClassFound;
            @SuppressWarnings({"StringBufferField"})
            private StringBuffer content;

            public InputSource resolveEntity(String publicId, String systemId)
            throws SAXException {

                return new InputSource(new StringReader(""));

            } // END resolveEntity


            public void startElement(String uri, String localName,
                                     String qName, Attributes attributes)
            throws SAXException {

                if (!facesServletPresent) {
                    if (SERVLET_CLASS.equals(localName)) {
                        servletClassFound = true;
                        //noinspection StringBufferWithoutInitialCapacity
                        content = new StringBuffer();
                    } else {
                        servletClassFound = false;
                    }
                }

            } // END startElement


            public void characters(char[] ch, int start, int length)
            throws SAXException {

                if (servletClassFound && !facesServletPresent) {
                    content.append(ch, start, length);
                }

            } // END characters


            public void endElement(String uri, String localName, String qName)
            throws SAXException {

                if (servletClassFound && !facesServletPresent) {
                    if (FACES_SERVLET.equals(content.toString().trim())) {
                        facesServletPresent = true;
                    }
                }

            } // END endElement

        } // END WebXmlHandler

    } // END WebXmlProcessor

}

