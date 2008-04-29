/*
 * $Id: ConfigureListener.java,v 1.118.2.2 2008/04/11 13:40:23 edburns Exp $
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

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.el.CompositeELResolver;
import javax.el.ExpressionFactory;
import javax.faces.FactoryFinder;
import javax.faces.application.ApplicationFactory;
import javax.faces.application.Application;
import javax.faces.application.ProjectStage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.jsp.JspApplicationContext;
import javax.servlet.jsp.JspFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import javax.faces.component.UIViewRoot;
import javax.faces.event.ViewMapCreatedEvent;
import javax.faces.event.ViewMapDestroyedEvent;

import com.sun.faces.application.ApplicationAssociate;
import com.sun.faces.application.WebappLifecycleListener;
import com.sun.faces.config.WebConfiguration.BooleanWebContextInitParameter;
import com.sun.faces.config.WebConfiguration.WebContextInitParameter;
import com.sun.faces.el.ELContextListenerImpl;
import com.sun.faces.el.ELUtils;
import com.sun.faces.el.FacesCompositeELResolver;
import com.sun.faces.scripting.GroovyHelper;
import com.sun.faces.scripting.GroovyHelperFactory;
import com.sun.faces.mgbean.BeanBuilder;
import com.sun.faces.mgbean.BeanManager;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.util.FacesLogger;
import com.sun.faces.util.MessageUtils;
import com.sun.faces.util.ReflectionUtils;
import com.sun.faces.util.Timer;
import com.sun.faces.util.Util;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

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

    private ScheduledThreadPoolExecutor webResourcePool =
          new ScheduledThreadPoolExecutor(1);

    protected WebappLifecycleListener webAppListener;
    protected WebConfiguration webConfig;

   
    
    // ------------------------------------------ ServletContextListener Methods


    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        webAppListener = new WebappLifecycleListener(context);
        webAppListener.contextInitialized(sce);
        Timer timer = Timer.getInstance();
        if (timer != null) {
            timer.startTiming();
        }


        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE,
                       MessageFormat.format(
                            "ConfigureListener.contextInitialized({0})",
                            getServletContextIdentifier(context)));
        }

        webConfig = WebConfiguration.getInstance(context);
        ConfigManager configManager = ConfigManager.getInstance();

        if (configManager.hasBeenInitialized(context)) {
            return;
        }

        // Check to see if the FacesServlet is present in the
        // web.xml.   If it is, perform faces configuration as normal,
        // otherwise, simply return.
        if (!webConfig.isOptionEnabled(BooleanWebContextInitParameter.ForceLoadFacesConfigFiles)) {
            WebXmlProcessor processor = new WebXmlProcessor(context);
            if (!processor.isFacesServletPresent()) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE,
                               "No FacesServlet found in deployment descriptor - bypassing configuration");
                }
                WebConfiguration.clear(context);
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

            if (LOGGER.isLoggable(Level.INFO)) {
                LOGGER.log(Level.INFO,
                          "jsf.config.listener.version",
                          getServletContextIdentifier(context));
            }

            // see if we need to disable our TLValidator
            Util.setHtmlTLVActive(
                  webConfig.isOptionEnabled(BooleanWebContextInitParameter.EnableHtmlTagLibraryValidator));

            if (webConfig.isOptionEnabled(BooleanWebContextInitParameter.VerifyFacesConfigObjects)) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.warning("jsf.config.verifyobjects.development_only");
                }
                // if we're verifying, force bean validation to occur at startup as well
                webConfig.overrideContextInitParameter(BooleanWebContextInitParameter.EnableLazyBeanValidation, false);
                Verifier.setCurrentInstance(new Verifier());
            }
            initScripting();
            configManager.initialize(context);
            initConfigMonitoring(context);

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
            registerELResolverAndListenerWithJsp(context, false);
            ApplicationAssociate associate =
                 ApplicationAssociate.getInstance(context);
            if (associate != null) {
                associate.setContextName(getServletContextIdentifier(context));
            }
            RenderKitUtils.loadSunJsfJs(initContext.getExternalContext());
            
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.getApplication().subscribeToEvent(ViewMapCreatedEvent.class, 
                    UIViewRoot.class, webAppListener);
            facesContext.getApplication().subscribeToEvent(ViewMapDestroyedEvent.class, 
                    UIViewRoot.class, webAppListener);
            
            
        } finally {
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
        webAppListener = null;
        ServletContext context = sce.getServletContext();
        GroovyHelper helper = GroovyHelper.getCurrentInstance(context);
        if (helper != null) {
            helper.setClassLoader();
        }

        LOGGER.log(Level.FINE,
                   "ConfigureListener.contextDestroyed({0})",
                   context.getServletContextName());

        try {
            // Release any allocated application resources
            FactoryFinder.releaseFactories();
            //monitor.cancel(true);
            //webResourcePool.purge();
            webResourcePool.shutdown();
        } finally {
            FacesContext initContext = new InitFacesContext(context);
            ApplicationAssociate
                  .clearInstance(initContext.getExternalContext());
            ApplicationAssociate.setCurrentInstance(null);
            // Release the initialization mark on this web application
            ConfigManager.getInstance().destory(context);
            initContext.release();
            ReflectionUtils.clearCache(Thread.currentThread().getContextClassLoader());
            WebConfiguration.clear(context);
        }

    }


    // ------------------------------------- Methods from ServletRequestListener


    public void requestDestroyed(ServletRequestEvent event) {
        if (webAppListener != null) {
            webAppListener.requestDestroyed(event);
        }
    }


    public void requestInitialized(ServletRequestEvent event) {
        if (webAppListener != null) {
            webAppListener.requestInitialized(event);
        }
    }


    // ----------------------------------------- Methods from HttpSessionListener


    public void sessionCreated(HttpSessionEvent event) {
       if (webAppListener != null) {
           webAppListener.sessionCreated(event);
       }
    }


    public void sessionDestroyed(HttpSessionEvent event) {
        if (webAppListener != null) {
            webAppListener.sessionDestroyed(event);
        }
    }


    // ---------------------------- Methods from ServletRequestAttributeListener


    public void attributeAdded(ServletRequestAttributeEvent event) {
        // ignored
    }


    public void attributeRemoved(ServletRequestAttributeEvent event) {
        if (webAppListener != null) {
            webAppListener.attributeRemoved(event);
        }
    }


    public void attributeReplaced(ServletRequestAttributeEvent event) {
        if (webAppListener != null) {
            webAppListener.attributeReplaced(event);
        }
    }


    // ------------------------------- Methods from HttpSessionAttributeListener


    public void attributeAdded(HttpSessionBindingEvent event) {
        // ignored
    }


    public void attributeRemoved(HttpSessionBindingEvent event) {
        if (webAppListener != null) {
            webAppListener.attributeRemoved(event);
        }
    }


    public void attributeReplaced(HttpSessionBindingEvent event) {
        if (webAppListener != null) {
            webAppListener.attributeReplaced(event);
        }
    }


    // ---------------------------- Methods from ServletContextAttributeListener

    
    public void attributeAdded(ServletContextAttributeEvent event) {
        // ignored
    }

    public void attributeRemoved(ServletContextAttributeEvent event) {
        if (webAppListener != null) {
            webAppListener.attributeRemoved(event);
        }
    }

    public void attributeReplaced(ServletContextAttributeEvent event) {
        if (webAppListener != null) {
            webAppListener.attributeReplaced(event);
        }
    }


    // --------------------------------------------------------- Private Methods

    
    private void initConfigMonitoring(ServletContext context) {

        //noinspection unchecked
        List<URL> webURLs =
              (List<URL>) context.getAttribute("com.sun.faces.webresources");
        if (isDevModeEnabled() && webURLs != null && !webURLs.isEmpty()) {
            webResourcePool.scheduleAtFixedRate(new WebConfigResourceMonitor(context, webURLs),
                                               2000,
                                               2000,
                                               TimeUnit.MILLISECONDS);
        }
        context.removeAttribute("com.sun.faces.webresources");

    }

    private void initScripting() {
        //if (isDevModeEnabled()) {
            GroovyHelper helper = GroovyHelperFactory.createHelper();
            if (helper != null) {
                helper.setClassLoader();
            }
        //}
    }


    private boolean isDevModeEnabled() {

        // interrogate the init parameter directly vs looking up the application
        return "Development".equals(webConfig.getOptionValue(WebContextInitParameter.JavaxFacesProjectStage));

    }


    /**
     * This method will be invoked {@link WebConfigResourceMonitor} when
     * changes to any of the faces-config.xml files included in WEB-INF
     * are modified.
     */
    private void reload(ServletContext sc) {

        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO,
                       "Reloading JSF configuration for context {0}",
                       getServletContextIdentifier(sc));
        }
        GroovyHelper helper = GroovyHelper.getCurrentInstance();
        if (helper != null) {
            helper.setClassLoader();
        }
        // tear down the application
        try {
            List<HttpSession> sessions = webAppListener.getActiveSessions();
            if (sessions != null) {
                for (HttpSession session : sessions) {
                    if (LOGGER.isLoggable(Level.INFO)) {
                        LOGGER.log(Level.INFO,
                                   "Invalidating Session {0}",
                                   session.getId());
                    }
                    session.invalidate();
                }
            }
            ApplicationAssociate associate = ApplicationAssociate.getInstance(sc);
            if (associate != null) {
                BeanManager manager = associate.getBeanManager();
                for (Map.Entry<String,BeanBuilder> entry : manager.getRegisteredBeans().entrySet()) {
                    String name = entry.getKey();
                    BeanBuilder bean = entry.getValue();
                    if (bean.getScope() == ELUtils.Scope.APPLICATION) {
                        if (LOGGER.isLoggable(Level.INFO)) {
                            LOGGER.log(Level.INFO,
                                       "Removing application scoped managed bean: {0}",
                                       name);
                        }
                        sc.removeAttribute(name);
                    }

                }
            }
            // Release any allocated application resources
            FactoryFinder.releaseFactories();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            FacesContext initContext = new InitFacesContext(sc);
            ApplicationAssociate
                  .clearInstance(initContext.getExternalContext());
            ApplicationAssociate.setCurrentInstance(null);
            // Release the initialization mark on this web application
            ConfigManager.getInstance().destory(sc);
            initContext.release();
            ReflectionUtils.clearCache(Thread.currentThread().getContextClassLoader());
            WebConfiguration.clear(sc);
        }

        // bring the application back up, avoid re-registration of certain JSP
        // artifacts.  No verification will be performed either to make this
        // light weight.

        // init a new WebAppLifecycleListener so that the cached ApplicationAssociate
        // is removed.
        webAppListener = new WebappLifecycleListener(sc);

        FacesContext initContext = new InitFacesContext(sc);
        ReflectionUtils
              .initCache(Thread.currentThread().getContextClassLoader());

        try {
            ConfigManager configManager = ConfigManager.getInstance();
            configManager.initialize(sc);


            registerELResolverAndListenerWithJsp(sc, true);
            ApplicationAssociate associate =
                  ApplicationAssociate.getInstance(sc);
            if (associate != null) {
                associate.setContextName(getServletContextIdentifier(sc));
            }
            RenderKitUtils.loadSunJsfJs(initContext.getExternalContext());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            initContext.release();
        }

        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.log(Level.INFO,
                       "Reload complete.",
                       getServletContextIdentifier(sc));
        }

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
                                       ServletContext.class);
        } catch (NoSuchMethodException nsme) {
            return false;
        }
        return true;

    }

    public void registerELResolverAndListenerWithJsp(ServletContext context, boolean reloaded) {

        if (webConfig.isSet(WebContextInitParameter.ExpressionFactory) ||
              !isJspTwoOne()) {

            // first try to load a factory defined in web.xml
            if (!installExpressionFactory(context,
                                          webConfig.getOptionValue(
                                                WebContextInitParameter.ExpressionFactory))) {

                    throw new ConfigurationException(
                          MessageUtils.getExceptionMessageString(
                                MessageUtils.INCORRECT_JSP_VERSION_ID,
                                WebContextInitParameter.ExpressionFactory.getDefaultValue(),
                                WebContextInitParameter.ExpressionFactory.getQualifiedName()));

            }

        } else {

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
                ApplicationFactory factory = (ApplicationFactory)
                      FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
                Application app = factory.getApplication();
                if (app.getProjectStage() != ProjectStage.UnitTest && !reloaded) {
                    throw e;
                }
            }
    
            // register JSF ELContextListenerImpl with Jsp
            ELContextListenerImpl elContextListener = new ELContextListenerImpl();
            jspAppContext.addELContextListener(elContextListener);
         }
    }

    private boolean installExpressionFactory(ServletContext sc,
                                             String elFactoryType){

        if (elFactoryType == null) {
            return false;
        }
        try {
            ExpressionFactory factory = (ExpressionFactory)
                  Class.forName(elFactoryType).newInstance();
            ApplicationAssociate associate =
                 ApplicationAssociate.getInstance(sc);
            if (associate != null) {
                associate.setExpressionFactory(factory);
            }
            return true;
        } catch (Exception e) {
            LOGGER.severe(MessageFormat.format("Unable to instantiate ExpressionFactory ''{0}''",
                                               elFactoryType));
            return false;
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


     private class WebConfigResourceMonitor implements Runnable {

        private List<Monitor> monitors;
        private ServletContext sc;

        // -------------------------------------------------------- Constructors


        public WebConfigResourceMonitor(ServletContext sc, List<URL> urls) {

            assert (urls != null);
            this.sc = sc;
            for (URL url : urls) {
                if (monitors == null) {
                    monitors = new ArrayList<Monitor>(urls.size());
                }
                monitors.add(new Monitor(url));
            }

        }


        // ----------------------------------------------- Methods from Runnable

        /**
         * PENDING javadocs
         */
        public void run() {

            assert (monitors != null);
            boolean reloaded = false;
            for (Monitor m : monitors) {
                if (m.hasBeenModified()) {
                    if (!reloaded) {
                        reloaded = true;
                    }
                }
            }
            if (reloaded) {
                reload(sc);
            }

        }


        // ------------------------------------------------------- Inner Classes


        private class Monitor {

            private URL url;
            private long timestamp = -1;

            // ---------------------------------------------------- Constructors


            Monitor(URL url) {

                this.url = url;
                this.timestamp = getLastModified();
                LOGGER.log(Level.INFO,
                           "Monitoring {0} for modifications",
                           url.toExternalForm());

            }


            // ----------------------------------------- Package Private Methods


            boolean hasBeenModified() {
                long temp = getLastModified();
                if (timestamp < temp) {
                    timestamp = temp;
                    LOGGER.log(Level.INFO,
                           "{0} changed!",
                           url.toExternalForm());
                    return true;
                }
                return false;

            }


            // ------------------------------------------------- Private Methods


            private long getLastModified() {

                InputStream in = null;
                try {
                    URLConnection conn = url.openConnection();
                    conn.connect();
                    in = conn.getInputStream();
                    return conn.getLastModified();
                } catch (IOException ioe) {
                    if (LOGGER.isLoggable(Level.SEVERE)) {
                        LOGGER.log(Level.SEVERE,
                                   "Unable to check JAR timestamp.",
                                   ioe);
                    }
                    return this.timestamp;
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException ignored) {
                        }
                    }
                }

            }

        } // END Monitor

    } // END WebConfigResourceMonitor

}

