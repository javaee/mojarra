/*
 * $Id: ConfigureListener.java,v 1.110 2007/06/25 20:10:47 rlubke Exp $
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

package com.sun.faces.config;

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.application.WebappLifecycleListener;
import com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter;
import com.sun.faces.config.WebConfiguration.WebContextInitParameter;
import com.sun.faces.el.ELContextListenerImpl;
import com.sun.faces.el.FacesCompositeELResolver;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.ReflectionUtils;
import com.sun.faces.util.Timer;
import com.sun.faces.util.Util;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.el.CompositeELResolver;
import javax.el.ExpressionFactory;
import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.servlet.*;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.jsp.JspApplicationContext;
import javax.servlet.jsp.JspFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.StringReader;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>Parse all relevant JavaServer Faces configuration resources, and
 * configure the Reference Implementation runtime environment.</p>
 * <p/>
 */
public class ConfigureListener implements ServletRequestListener,
                                          HttpSessionListener,
                                          ServletRequestAttributeListener,
                                          HttpSessionAttributeListener,
                                          ServletContextAttributeListener,
                                          ServletContextListener {


    private static final Logger LOGGER = FacesLogger.CONFIG.getLogger();


    /**
     * <p>All known factory names.</p>
     */
    private static final String[] FACTORY_NAMES = {
        FactoryFinder.APPLICATION_FACTORY,
        FactoryFinder.FACES_CONTEXT_FACTORY,
        FactoryFinder.LIFECYCLE_FACTORY,
        FactoryFinder.RENDER_KIT_FACTORY
    };

    protected WebappLifecycleListener webAppListener = new WebappLifecycleListener();
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
        webAppListener.contextInitialized(sce);
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
                return;
            } else {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE,
                               "FacesServlet found in deployment descriptor - processing configuration.");
                }
            }
        }

        FacesContext initContext = new InitFacesContext(context);
        ReflectionUtils.initCache(Thread.currentThread().getContextClassLoader());

        try {

            LOGGER.log(Level.INFO,
                       "jsf.config.listener.version",
                       getServletContextIdentifier(context));

            // see if we need to disable our TLValidator
            Util.setHtmlTLVActive(
                  isFeatureEnabled(BooleanWebContextInitParameter.EnableHtmlTagLibraryValidator));

            if (isFeatureEnabled(BooleanWebContextInitParameter.VerifyFacesConfigObjects)) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.warning("jsf.config.verifyobjects.developement_only");
                }
                // if we're verifying, force bean validation to occur at startup as well
                webConfig.overrideContextInitParameter(BooleanWebContextInitParameter.EnableLazyBeanValidation, false);
                Verifier.setCurrentInstance(new Verifier());
            }
            configManager.initialize(context);

            // Step 7, verify that all the configured factories are available
            // and optionall that configured objects can be created. 
            Verifier v = Verifier.getCurrentInstance();
            if (v != null && !v.isApplicationValid()) {
                if (LOGGER.isLoggable(Level.SEVERE)) {
                    LOGGER.severe("jsf.config.verifyobjects.failures_detected");
                    StringBuilder sb = new StringBuilder(128);
                    for (String m : v.getMessages()) {
                        sb.append(m).append('\n');
                    }
                    LOGGER.severe(sb.toString());
                }               
            }
            verifyFactories();
            registerELResolverAndListenerWithJsp(context);
            
        } finally {
           
            ApplicationAssociate associate =
                 ApplicationAssociate.getInstance(context);
            if (associate != null) {
                associate.setContextName(getServletContextIdentifier(context));
            }
            RenderKitUtils.loadSunJsfJs(initContext.getExternalContext());
            Verifier.setCurrentInstance(null);
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
        webAppListener.contextDestroyed(sce);
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
            ReflectionUtils.clearCache(Thread.currentThread().getContextClassLoader());
            WebConfiguration.clear(context);
        }

    }


    // ------------------------------------- Methods from ServletRequestListener


    public void requestDestroyed(ServletRequestEvent event) {
        webAppListener.requestDestroyed(event);
    }


    public void requestInitialized(ServletRequestEvent event) {
        webAppListener.requestInitialized(event);
    }


    // ----------------------------------------- Methods from HttpSessionListener


    public void sessionCreated(HttpSessionEvent event) {
        // ignored
    }


    public void sessionDestroyed(HttpSessionEvent event) {
        webAppListener.sessionDestroyed(event);
    }


    // ---------------------------- Methods from ServletRequestAttributeListener


    public void attributeAdded(ServletRequestAttributeEvent event) {
        // ignored
    }


    public void attributeRemoved(ServletRequestAttributeEvent event) {
        webAppListener.attributeRemoved(event);
    }


    public void attributeReplaced(ServletRequestAttributeEvent event) {
        webAppListener.attributeReplaced(event);
    }


    // ------------------------------- Methods from HttpSessionAttributeListener


    public void attributeAdded(HttpSessionBindingEvent event) {
        // ignored
    }


    public void attributeRemoved(HttpSessionBindingEvent event) {
        webAppListener.attributeRemoved(event);
    }


    public void attributeReplaced(HttpSessionBindingEvent event) {
        webAppListener.attributeReplaced(event);
    }


    // ---------------------------- Methods from ServletContextAttributeListener

    
    public void attributeAdded(ServletContextAttributeEvent event) {
        // ignored
    }

    public void attributeRemoved(ServletContextAttributeEvent event) {
        webAppListener.attributeRemoved(event);
    }

    public void attributeReplaced(ServletContextAttributeEvent event) {
        webAppListener.attributeReplaced(event);
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
        } catch (ClassNotFoundException ignored) {
            // ignored
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

